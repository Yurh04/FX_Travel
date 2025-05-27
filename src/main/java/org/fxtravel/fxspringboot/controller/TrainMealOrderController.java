package org.fxtravel.fxspringboot.controller;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderDTO;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.fxtravel.fxspringboot.service.inter.TrainMealOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/train_meal_order")
public class TrainMealOrderController {

    @Autowired
    private TrainMealOrderService trainMealOrderService;

    @Autowired
    private PaymentService paymentService;

    // -------------------- 订单查询接口 --------------------

    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<train_meal_order> getOrderById(@PathVariable Integer id) {
        train_meal_order order = trainMealOrderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 根据用户ID获取订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<train_meal_order>> getOrdersByUserId(@PathVariable Integer userId) {
        List<train_meal_order> orders = trainMealOrderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据车票预购ID获取订单列表
     * @param ticketReservationId 车票预购ID
     * @return 订单列表
     */
    @GetMapping("/ticket/{ticketReservationId}")
    public ResponseEntity<List<train_meal_order>> getOrdersByTicketReservationId(
            @PathVariable Integer ticketReservationId) {
        List<train_meal_order> orders = trainMealOrderService.getOrdersByTicketReservationId(ticketReservationId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 多条件查询订单
     * @param queryDTO 查询条件
     * @return 符合条件的订单列表
     */
    @PostMapping("/query")
    public ResponseEntity<List<train_meal_order>> queryOrders(@RequestBody TrainMealOrderQueryDTO queryDTO) {
        List<train_meal_order> orders = trainMealOrderService.queryOrders(queryDTO);
        return ResponseEntity.ok(orders);
    }

    // -------------------- 订单操作接口 --------------------

    /**
     * 创建列车餐订单
     * @param orderDTO 订单信息
     * @return 创建的订单详情
     */
    @PostMapping
    public ResponseEntity<train_meal_order> createOrder(@RequestBody TrainMealOrderDTO orderDTO) {
        train_meal_order order = trainMealOrderService.createOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    // -------------------- 支付状态接口 --------------------

    /**
     * 获取订单支付状态(前端轮询检查支付状态)
     * @param orderId 订单ID
     * @return 支付状态信息
     */
    @GetMapping("/{orderId}/payment_status")
    public ResponseEntity<PaymentResultDTO> getOrderPaymentStatus(@PathVariable Integer orderId) {
        train_meal_order order = trainMealOrderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        PaymentResultDTO result = order.getRelatedPaymentId() != null ?
                paymentService.checkPaymentStatus(order.getRelatedPaymentId()) : null;

        return ResponseEntity.ok(result);
    }

    // -------------------- 统计接口 --------------------

    /**
     * 统计用户订单总金额
     * @param userId 用户ID
     * @return 总金额
     */
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Double> sumAmountByUserId(@PathVariable Integer userId) {
        Double total = trainMealOrderService.sumAmountByUserId(userId);
        return ResponseEntity.ok(total);
    }

    /**
     * 统计特定餐食的总销量
     * @param trainMealId 餐食ID
     * @return 总销量
     */
    @GetMapping("/stats/meal/{trainMealId}")
    public ResponseEntity<Integer> sumQuantityByTrainMealId(@PathVariable Integer trainMealId) {
        Integer total = trainMealOrderService.sumQuantityByTrainMealId(trainMealId);
        return ResponseEntity.ok(total);
    }
}
