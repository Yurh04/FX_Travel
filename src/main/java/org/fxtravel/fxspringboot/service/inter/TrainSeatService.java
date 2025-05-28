package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.SeatType;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;

import java.math.BigDecimal;
import java.util.List;

public interface TrainSeatService {
    public void addTrainSeat(Integer trainId, SeatType seatType, BigDecimal price, Integer available);

    public void deleteTrainSeat(Integer seatId);

    public TrainSeat getTrainSeatById(Integer seatId);

    public List<TrainSeat> getTrainSeatsByTrainId(Integer trainId);

    public void updateTrainSeat(Integer seatId, BigDecimal price, Integer available);
}
