package org.fxtravel.fxspringboot.service.inter.trainseat;

import org.fxtravel.fxspringboot.pojo.dto.train.GetTicketRequest;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeatOrder;

import java.util.List;

public interface TrainSeatOrderService {
    boolean existsByTrainAndUser(Integer trainId, Integer userId);
    List<TrainSeatOrder> getOrdersByUserId(Integer userId);
    TrainSeatOrder getOrderById(Integer orderId);
    TrainSeatOrder getOrderByNumber(String orderNumber);
    TrainSeatOrder createOrder(GetTicketRequest request);
}
