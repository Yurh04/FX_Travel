package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.TrainType;
import org.fxtravel.fxspringboot.pojo.entities.Train;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainService {
    public void addTrain(String trainNumber, TrainType trainType, String fromStation, String toStation, LocalDateTime departureTime, LocalDateTime arrivalTime);
    public boolean deleteTrain(Integer trainId);
    public boolean updateTrain(Integer trainId,TrainType trainType, String trainNumber, String fromStation, String toStation,LocalDateTime departureTime, LocalDateTime arrivalTime);
    public List<Train> getAllTrains();
    public Train getTrainById(Integer trainId);
}
