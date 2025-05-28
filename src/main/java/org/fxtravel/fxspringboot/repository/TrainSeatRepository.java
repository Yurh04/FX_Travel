package org.fxtravel.fxspringboot.repository;

import org.fxtravel.fxspringboot.common.SeatType;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainSeatRepository extends JpaRepository<TrainSeat, Integer> {
    public boolean existsByTrainAndSeatType(Train train, SeatType seatType);

    public List<TrainSeat> findByTrain(Train train);

}
