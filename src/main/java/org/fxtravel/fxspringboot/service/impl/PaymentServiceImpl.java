package org.fxtravel.fxspringboot.service.impl;

import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.mapper.PaymentMapper;
import org.fxtravel.fxspringboot.pojo.dto.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    private final Map<String, CompletableFuture<Boolean>> pendingPayments = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private static final DateTimeFormatter ORDER_NUMBER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // -------------------- 基础支付操作实现 --------------------
    @Override
    @Transactional
    public payment createPayment(Integer userId, E_PaymentType type, Double amount, Integer relatedId) {
        payment payment = new payment();
        payment.setUserId(userId);
        payment.setType(type);
        payment.setAmount(amount);
        payment.setRelatedId(relatedId);
        payment.setStatus(E_PaymentStatus.IDLE);
        payment.setOrderNumber(generateOrderNumber(type));

        paymentMapper.insert(payment);

        return payment;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean completePayment(String orderNumber) {
        // 检查当前状态是否为PENDING
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null || payment.getStatus() != E_PaymentStatus.PENDING) {
            return false;
        }

        // 更新支付状态
        int result = paymentMapper.updateStatus(orderNumber, E_PaymentStatus.COMPLETED, LocalDateTime.now());

        // 通知等待中的支付流程
        CompletableFuture<Boolean> future = pendingPayments.remove(orderNumber);
        if (future != null) {
            future.complete(true);
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean failPayment(String orderNumber) {
        // 检查当前状态是否为PENDING
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null || payment.getStatus() != E_PaymentStatus.PENDING) {
            return false;
        }

        // 更新支付状态
        int result = paymentMapper.updateStatus(orderNumber, E_PaymentStatus.FAILED, LocalDateTime.now());

        // 通知等待中的支付流程
        CompletableFuture<Boolean> future = pendingPayments.remove(orderNumber);
        if (future != null) {
            future.complete(false);
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean refundPayment(String orderNumber) {
        // 检查当前状态是否为COMPLETED
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null || payment.getStatus() != E_PaymentStatus.COMPLETED) {
            return false;
        }

        return paymentMapper.updateStatus(orderNumber, E_PaymentStatus.REFUNDED, LocalDateTime.now()) > 0;
    }

    // -------------------- 管理员查询接口实现 --------------------
    @Override
    public payment getPaymentById(Integer id) {
        return paymentMapper.selectById(id);
    }

    @Override
    public payment getPaymentByOrderNumber(String orderNumber) {
        return paymentMapper.selectByOrderNumber(orderNumber);
    }

    @Override
    public List<payment> queryPayments(PaymentQueryDTO queryDTO) {
        return paymentMapper.selectByConditionsDTO(queryDTO);
    }

    @Override
    public List<payment> getPaymentsByType(E_PaymentType type) {
        return paymentMapper.selectByType(type);
    }

    @Override
    public Double sumAmountByType(E_PaymentType type) {
        return paymentMapper.sumAmountByType(type);
    }

    // -------------------- 支付模拟接口实现 --------------------
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentResultDTO simulatePaymentProcess(String orderNumber, long timeout) {
        PaymentResultDTO result = new PaymentResultDTO();
        result.setOrderNumber(orderNumber);

        // 检查订单是否存在
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null) {
            result.setCurrentStatus(null);
            result.setMessage("Order not found");
            return result;
        }

        // 获取当前状态
        E_PaymentStatus currentStatus = payment.getStatus();
        result.setCurrentStatus(currentStatus);

        // 如果状态不是IDLE(未开启)，直接返回当前状态
        if (currentStatus != E_PaymentStatus.IDLE) {
            result.setMessage(currentStatus == E_PaymentStatus.PENDING ? "Payment already in progress"
                    : "Payment already processed");
            return result;
        }

        // 将状态更新为PENDING
        paymentMapper.updateStatus(orderNumber, E_PaymentStatus.PENDING, LocalDateTime.now());
        result.setCurrentStatus(E_PaymentStatus.PENDING);

        // 模拟支付处理
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingPayments.put(orderNumber, future);

        // 设置超时处理
        scheduler.schedule(() -> {
            if (!future.isDone()) {
                // 检查状态是否仍然是PENDING（可能已经被其他操作改变）
                payment currentPayment = paymentMapper.selectByOrderNumber(orderNumber);
                if (currentPayment != null && currentPayment.getStatus() == E_PaymentStatus.PENDING) {
                    paymentMapper.updateStatus(orderNumber, E_PaymentStatus.FAILED, LocalDateTime.now());
                    future.complete(false);
                    pendingPayments.remove(orderNumber);
                }
            }
        }, timeout, TimeUnit.SECONDS);

        result.setMessage("Payment processing started");
        return result;
    }

    @Override
    public E_PaymentStatus checkPaymentStatus(String orderNumber) {
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null) {
            return null;
        }
        return payment.getStatus();
    }

    // -------------------- 私有方法 --------------------
    private String generateOrderNumber(E_PaymentType type) {
        String prefix = "";
        switch (type) {
            case TRAIN_TICKET:
                prefix = "TT";
                break;
            case TRAIN_MEAL:
                prefix = "TM";
                break;
            case HOTEL:
                prefix = "HT";
                break;
        }
        String timestamp = LocalDateTime.now().format(ORDER_NUMBER_DATE_FORMAT);
        String random = String.format("%04d", new Random().nextInt(10000));
        return prefix + timestamp + random;
    }
}
