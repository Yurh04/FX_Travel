package org.fxtravel.fxspringboot.service.impl;

import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.mapper.PaymentMapper;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    // 回调注册表
    private final Map<E_PaymentType, PaymentStatusCallback> callbacks = new ConcurrentHashMap<>();

    // 注册回调
    @Override
    public void registerCallback(E_PaymentType type, PaymentStatusCallback callback) {
        callbacks.put(type, callback);
    }

    // 注销回调
    @Override
    public void unregisterCallback(E_PaymentType type) {
        callbacks.remove(type);
    }

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
        payment.setTimeoutSeconds(0L);

        paymentMapper.insert(payment);

        return payment;
    }

    // 支付状态变更方法，添加回调通知
    private void notifyPaymentStatusChanged(E_PaymentType type, Integer relatedId, E_PaymentStatus newStatus) {
        PaymentStatusCallback callback = callbacks.get(type);
        if (callback != null) {
            try {
                callback.onPaymentStatusChanged(relatedId, newStatus);
            } catch (Exception e) {
                // 记录错误但不要中断主流程
                System.err.println("Error notifying payment status change: " + e.getMessage());
            }
        }
        else
            System.err.println("No callback found for type " + type);
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
        if (result > 0) {
            notifyPaymentStatusChanged(payment.getType(), payment.getRelatedId(), E_PaymentStatus.COMPLETED);
        }

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
        if (result > 0) {
            notifyPaymentStatusChanged(payment.getType(), payment.getRelatedId(), E_PaymentStatus.FAILED);
        }

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
        // 检查当前状态是否为COMPLETED(且不是FINISHED)
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null ||
                payment.getStatus() != E_PaymentStatus.COMPLETED) {
            return false;
        }

        int result = paymentMapper.updateStatus(orderNumber, E_PaymentStatus.REFUNDED, LocalDateTime.now());
        if (result > 0) {
            notifyPaymentStatusChanged(payment.getType(), payment.getRelatedId(), E_PaymentStatus.REFUNDED);
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean finishPayment(String orderNumber) {
        // 检查当前状态是否为COMPLETED
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        if (payment == null || payment.getStatus() != E_PaymentStatus.COMPLETED) {
            return false;
        }

        int result = paymentMapper.updateStatus(orderNumber, E_PaymentStatus.FINISHED, LocalDateTime.now());
        if (result > 0) {
            notifyPaymentStatusChanged(payment.getType(), payment.getRelatedId(), E_PaymentStatus.FINISHED);
        }

        return result > 0;
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
            result.setRemainingTimeSeconds(0L);
            return result;
        }

        // 获取当前状态
        E_PaymentStatus currentStatus = payment.getStatus();
        result.setCurrentStatus(currentStatus);

        // 如果状态不是IDLE(未开启)，直接返回当前状态
        if (currentStatus != E_PaymentStatus.IDLE) {
            result.setMessage(currentStatus == E_PaymentStatus.PENDING ? "Payment already in progress"
                    : "Payment already processed");
            // 计算剩余时间
            if (currentStatus == E_PaymentStatus.PENDING) {
                result.setRemainingTimeSeconds(calculateRemainingTime(payment));
            } else {
                result.setRemainingTimeSeconds(0L);
            }

            return result;
        }

        // 更新支付记录，设置状态和超时时间
        payment.setStatus(E_PaymentStatus.PENDING);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTimeoutSeconds(timeout);
        paymentMapper.updateById(payment);

        result.setCurrentStatus(E_PaymentStatus.PENDING);
        result.setRemainingTimeSeconds(timeout);
        result.setMessage("Payment processing started");

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

        return result;
    }

    @Override
    public PaymentResultDTO checkPaymentStatus(String orderNumber) {
        payment payment = paymentMapper.selectByOrderNumber(orderNumber);
        return getPaymentResultDTO(payment);
    }

    @Override
    public PaymentResultDTO checkPaymentStatus(Integer paymentId) {
        payment payment = paymentMapper.selectById(paymentId);
        return getPaymentResultDTO(payment);
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

    // 计算剩余时间
    private long calculateRemainingTime(payment payment) {
        if (payment.getStatus() != E_PaymentStatus.PENDING ||
                payment.getPaymentTime() == null ||
                payment.getTimeoutSeconds() == null) {
            return 0L;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeoutTime = payment.getPaymentTime().plusSeconds(payment.getTimeoutSeconds());
        long remaining = Duration.between(now, timeoutTime).getSeconds();
        return Math.max(0, remaining);
    }

    private PaymentResultDTO getPaymentResultDTO(payment payment) {
        PaymentResultDTO result = new PaymentResultDTO();
        if (payment == null) {
            result.setCurrentStatus(null);
            result.setMessage("Order not found");
            result.setRemainingTimeSeconds(0L);
            return result;
        }

        result.setCurrentStatus(payment.getStatus());
        result.setOrderNumber(payment.getOrderNumber());

        if (payment.getStatus() == E_PaymentStatus.PENDING) {
            result.setRemainingTimeSeconds(calculateRemainingTime(payment));
            result.setMessage("Payment in progress");
        } else {
            result.setRemainingTimeSeconds(0L);
            result.setMessage("Payment not in progress");
        }
        return result;
    }
}
