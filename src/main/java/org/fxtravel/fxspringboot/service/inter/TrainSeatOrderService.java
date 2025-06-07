package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.pojo.dto.train.GetTicketRequest;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeatOrder;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;

import java.util.List;

public interface TrainSeatOrderService {
    List<TrainSeatOrder> getOrdersByUserId(Integer userId);
    TrainSeatOrder getOrderById(Integer orderId);
    TrainSeatOrder createOrder(GetTicketRequest request);
}
