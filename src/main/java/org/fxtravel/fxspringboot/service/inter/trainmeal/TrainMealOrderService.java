package org.fxtravel.fxspringboot.service.inter.trainmeal;

import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderDTO;
import org.fxtravel.fxspringboot.pojo.entities.trainmeal.TrainMealOrder;

import java.util.List;

public interface TrainMealOrderService {
    TrainMealOrder getOrderById(Integer id);

    List<TrainMealOrder> getOrdersByUser(Integer userId);

    List<TrainMealOrder> getOrdersBySeatOrder(Integer seatOrderId);

    TrainMealOrder createOrder(TrainMealOrderDTO orderDTO);

    boolean existsBySeatOrder(Integer seatOrderId);
}
