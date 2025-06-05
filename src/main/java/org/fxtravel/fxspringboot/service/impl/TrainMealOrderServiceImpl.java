package org.fxtravel.fxspringboot.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.enent.EventCenter;
import org.fxtravel.fxspringboot.enent.EventType;
import org.fxtravel.fxspringboot.enent.data.PaymentInfo;
import org.fxtravel.fxspringboot.mapper.TrainMealMapper;
import org.fxtravel.fxspringboot.mapper.TrainMealOrderMapper;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderDTO;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.fxtravel.fxspringboot.service.inter.TrainMealOrderService;
import org.fxtravel.fxspringboot.service.inter.TrainMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@DependsOn("paymentServiceImpl")
public class TrainMealOrderServiceImpl implements TrainMealOrderService {

    @Autowired
    private TrainMealOrderMapper trainMealOrderMapper;

    @Autowired
    private TrainMealService trainMealService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EventCenter eventCenter;

    @PostConstruct
    public void init() {
        // 注册回调，确保在服务启动时就注册
        eventCenter.subscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
    }

    @PreDestroy
    public void destroy() {
        // 服务关闭时注销回调
        eventCenter.unsubscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
    }

    // 处理支付状态变更的回调方法
    private void handlePaymentStatusChange(PaymentInfo info) {
        trainMealOrderMapper.updateStatus(info.getOrderId(), info.getNewStatus());
    }

    @Override
    public train_meal_order getOrderById(Integer id) {
        return trainMealOrderMapper.selectById(id);
    }

    @Override
    public List<train_meal_order> getOrdersByUserId(Integer userId) {
        return trainMealOrderMapper.selectByUserId(userId);
    }

    @Override
    public List<train_meal_order> getOrdersByTicketReservationId(Integer ticketReservationId) {
        return trainMealOrderMapper.selectByTicketReservationId(ticketReservationId);
    }

    @Override
    public List<train_meal_order> queryOrders(TrainMealOrderQueryDTO queryDTO) {
        return trainMealOrderMapper.selectByConditionsDTO(queryDTO);
    }

    @Override
    public train_meal_order createOrder(TrainMealOrderDTO orderDTO) {
        // 1. 验证餐食信息
        train_meal meal = trainMealService.getMealById(orderDTO.getTrainMealId());
        if (meal == null || !meal.getEnabled()) {
            throw new RuntimeException("餐食不存在或已下架");
        }
        // TODO 更多校验需要等待相关系统完成，暂时不管

        // 2. 计算总金额
        Double totalAmount = meal.getPrice() * orderDTO.getQuantity();

        // 3. 创建订单实体
        train_meal_order order = new train_meal_order();
        order.setUserId(orderDTO.getUserId());
        order.setTicketReservationId(orderDTO.getTicketReservationId());
        order.setTrainMealId(orderDTO.getTrainMealId());
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalAmount(totalAmount);
        order.setStatus(E_PaymentStatus.IDLE);
        order.setCreateTime(LocalDateTime.now());

        // 4. 保存订单
        trainMealOrderMapper.insert(order);

        // 5. 创建支付记录
        payment payment = paymentService.createPayment(
                order.getUserId(),
                E_PaymentType.TRAIN_MEAL,
                order.getTotalAmount(),
                order.getId(),
                order.getQuantity(),
                order.getTrainMealId()
        );

        // 6. 启动异步支付流程
        paymentService.simulatePaymentProcess(payment.getOrderNumber(), 30,
                () -> trainMealService.checkAndGet(meal.getId(), order.getQuantity()));

        return order;
    }

    @Override
    public Double sumAmountByUserId(Integer userId) {
        return trainMealOrderMapper.sumAmountByUserId(userId);
    }

    @Override
    public Integer sumQuantityByTrainMealId(Integer trainMealId) {
        return trainMealOrderMapper.sumQuantityByTrainMealId(trainMealId);
    }
}
