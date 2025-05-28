package org.fxtravel.fxspringboot.service.impl;

import org.fxtravel.fxspringboot.common.TrainType;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.repository.TrainRepository;
import org.fxtravel.fxspringboot.service.inter.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainServiceImp implements TrainService {
    @Autowired
    TrainRepository trainRepository;
    @Override
    public void addTrain(String trainNumber, TrainType trainType, String fromStation, String toStation, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (arrivalTime.isBefore(departureTime)) {
            throw new IllegalArgumentException("Arrival time should be after departure time");
        }
        if (fromStation.equals(toStation)) {
            throw new IllegalArgumentException("FromStation and toStation are the same");
        }
        Train t = new Train();
        t.setTrainNumber(trainNumber);
        t.setTrainType(trainType);
        t.setFromStation(fromStation);
        t.setToStation(toStation);
        t.setDepartureTime(departureTime);
        t.setArrivalTime(arrivalTime);
        trainRepository.save(t);
    }

    @Override
    public boolean deleteTrain(Integer trainId) {
        var t = trainRepository.findById(trainId).orElse(null);
        if (t == null) {
            return false;
        }
        trainRepository.deleteById(trainId);
        return true;
    }

    @Override
    public boolean updateTrain(Integer trainId,
                               TrainType trainType, String trainNumber,
                               String fromStation, String toStation,
                               LocalDateTime departureTime, LocalDateTime arrivalTime) {
        var t = trainRepository.findById(trainId).orElse(null);
        if (t == null) {
            return false;
        }
        if (trainType!=null){
            t.setTrainType(trainType);
        }
        if (trainNumber!=null && trainNumber!=""){
            t.setTrainNumber(trainNumber);
        }
        if (fromStation!=null && fromStation!=""){
            t.setFromStation(fromStation);
        }
        if (toStation!=null && toStation!=""){
            t.setToStation(toStation);
        }
        if (t.getFromStation().equals(t.getToStation())) {
            throw new IllegalArgumentException("FromStation and toStation are the same");
        }
        if (departureTime!=null){
            t.setDepartureTime(departureTime);
        }
        if (arrivalTime!=null){
            t.setArrivalTime(arrivalTime);
        }
        if (t.getArrivalTime().isBefore(t.getDepartureTime())) {
            throw new IllegalArgumentException("Arrival time should be after departure time");
        }
        trainRepository.save(t);
        return true;
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train getTrainById(Integer trainId) {
        return trainRepository.findById(trainId).orElse(null);
    }
}
