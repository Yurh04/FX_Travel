package org.fxtravel.fxspringboot.service.inter.trainseat;

import org.fxtravel.fxspringboot.pojo.dto.train.GetTicketRequest;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeatOrder;

import java.util.List;

public interface TrainSeatOrderService {
    List<TrainSeatOrder> getOrdersByUserId(Integer userId);
    TrainSeatOrder getOrderById(Integer orderId);
    TrainSeatOrder createOrder(GetTicketRequest request);
}
