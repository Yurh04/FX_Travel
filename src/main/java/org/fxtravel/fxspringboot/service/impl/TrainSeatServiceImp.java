package org.fxtravel.fxspringboot.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.common.SeatType;
import org.fxtravel.fxspringboot.enent.EventCenter;
import org.fxtravel.fxspringboot.enent.EventType;
import org.fxtravel.fxspringboot.enent.data.PaymentInfo;
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
    @Autowired
    EventCenter eventCenter;

    @PostConstruct
    public void init() {
        eventCenter.subscribe(EventType.TT_STATUS_CHANGED, this::handlePaymentStatusChange);
    }
    @PreDestroy
    public void destroy() {
        eventCenter.unsubscribe(EventType.TT_STATUS_CHANGED,this::handlePaymentStatusChange);
    }

    // 处理支付状态变更的回调方法
    private void handlePaymentStatusChange(PaymentInfo info) {
        switch (info.getNewStatus()){
            case FAILED:
            case REFUNDED:
                putBack(info.getGoodId(), info.getQuantity());
                break;
        }
    }

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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean checkAndGet(int id, int count) {
        TrainSeat ts = trainSeatRepository.findById(id).orElse(null);
        if (ts == null || ts.getAvailable() < count) {
            return false;
        }
        int updated = trainSeatRepository.deductInventory(id, count);
        return updated > 0;
    }

    @Override
    public void putBack(int id, int count) {
        trainSeatRepository.addInventory(id, count);
    }
}
