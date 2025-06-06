package org.fxtravel.fxspringboot.repository;

import org.fxtravel.fxspringboot.common.SeatType;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainSeatRepository extends JpaRepository<TrainSeat, Integer> {
    public boolean existsByTrainAndSeatType(Train train, SeatType seatType);

    public List<TrainSeat> findByTrain(Train train);

    @Modifying
    @Query("UPDATE TrainSeat ts " +
            "SET ts.available = ts.available - ?2 " +
            "WHERE ts.id = ?1 AND ts.available >= ?2")
    public int deductInventory(int id, int count);

    @Modifying
    @Query("UPDATE TrainSeat ts " +
            "SET ts.available = ts.available + ?2 " +
            "WHERE ts.id = ?1 AND ts.available >= ?2")
    public int addInventory(int id, int count);
}
