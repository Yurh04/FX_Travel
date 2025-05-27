package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderDTO;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;

import java.util.List;

public interface TrainMealOrderService {
    /**
     * 根据ID获取订单
     * @param id 订单ID
     * @return 订单实体
     */
    train_meal_order getOrderById(Integer id);

    /**
     * 根据用户ID获取订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<train_meal_order> getOrdersByUserId(Integer userId);

    /**
     * 根据车票预购ID获取订单列表
     * @param ticketReservationId 车票预购ID
     * @return 订单列表
     */
    List<train_meal_order> getOrdersByTicketReservationId(Integer ticketReservationId);

    /**
     * 多条件查询订单
     * @param queryDTO 查询条件
     * @return 符合条件的订单列表
     */
    List<train_meal_order> queryOrders(TrainMealOrderQueryDTO queryDTO);

    /**
     * 创建列车餐订单
     * @param orderDTO 订单DTO
     * @return 创建的订单实体
     */
    train_meal_order createOrder(TrainMealOrderDTO orderDTO);

    /**
     * 统计用户订单总金额
     * @param userId 用户ID
     * @return 总金额
     */
    Double sumAmountByUserId(Integer userId);

    /**
     * 统计特定餐食的总销量
     * @param trainMealId 餐食ID
     * @return 总销量
     */
    Integer sumQuantityByTrainMealId(Integer trainMealId);
}
