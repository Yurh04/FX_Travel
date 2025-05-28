package org.fxtravel.fxspringboot.service.impl;

import org.fxtravel.fxspringboot.common.SeatType;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.fxtravel.fxspringboot.repository.TrainRepository;
import org.fxtravel.fxspringboot.repository.TrainSeatRepository;
import org.fxtravel.fxspringboot.service.inter.TrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TrainSeatServiceImp implements TrainSeatService {
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    TrainSeatRepository trainSeatRepository;
    @Override
    public void addTrainSeat(Integer trainId, SeatType seatType, BigDecimal price, Integer available) {
        var t = trainRepository.findById(trainId).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Train not found");
        }
        if (trainSeatRepository.existsByTrainAndSeatType(t, seatType)) {
            throw new IllegalArgumentException("Seat in train already exists");
        }
        TrainSeat ts = new TrainSeat();
        ts.setTrain(t);
        ts.setSeatType(seatType);
        ts.setPrice(price);
        ts.setAvailable(available);
        trainSeatRepository.save(ts);
    }

    @Override
    public void deleteTrainSeat(Integer seatId) {
        var ts = trainSeatRepository.findById(seatId).orElse(null);
        if (ts == null) {
            throw new IllegalArgumentException("TrainSeat not found");
        }
        trainSeatRepository.delete(ts);
    }

    @Override
    public TrainSeat getTrainSeatById(Integer seatId) {
        var ts = trainSeatRepository.findById(seatId).orElse(null);
        if (ts == null) {
            throw new IllegalArgumentException("TrainSeat not found");
        }
        return ts;
    }

    @Override
    public List<TrainSeat> getTrainSeatsByTrainId(Integer trainId) {
        var t = trainRepository.findById(trainId).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Train not found");
        }
        var ts = trainSeatRepository.findByTrain(t);
        if (ts == null || ts.isEmpty()) {
            throw new IllegalStateException("No seats found for this train");
        }
        return ts;
    }

    @Override
    public void updateTrainSeat(Integer seatId, BigDecimal price, Integer available) {
        var ts = trainSeatRepository.findById(seatId).orElse(null);
        if (ts == null) {
            throw new IllegalArgumentException("TrainSeat not found");
        }
        ts.setPrice(price);
        ts.setAvailable(available);
        trainSeatRepository.save(ts);
    }
}
