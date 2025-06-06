package org.fxtravel.fxspringboot.service.impl;

import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.fxtravel.fxspringboot.pojo.entities.TrainTicket;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.repository.TrainRepository;
import org.fxtravel.fxspringboot.repository.TrainSeatRepository;
import org.fxtravel.fxspringboot.repository.TrainTicketRepository;
import org.fxtravel.fxspringboot.service.inter.TrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainTicketServiceImp implements TrainTicketService {
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    TrainSeatRepository trainSeatRepository;
    @Autowired
    TrainTicketRepository trainTicketRepository;

    @Override
    public TrainTicket generateTicket(User user, Integer trainSeatId) {
        var trainSeat = trainSeatRepository.findById(trainSeatId).orElse(null);
        if (trainSeat == null) {
            throw new IllegalArgumentException("TrainSeat not found");
        }
        if (trainTicketRepository.existsByTrainSeatAndUser(trainSeat,user)) {
            throw new IllegalArgumentException("You have already bought the train ticket");
        }
        if (trainSeat.getAvailable() == 0){
            throw new IllegalStateException("Not enough balance for the ticket");
        }

        TrainTicket trainTicket = new TrainTicket();
        trainTicket.setUser(user);
        trainTicket.setTrainSeat(trainSeat);
        trainTicketRepository.save(trainTicket);
        return trainTicket;
    }

    @Override
    public TrainTicket cancelTicket(Integer ticketId, Integer userId) {
        TrainTicket trainTicket = trainTicketRepository.findById(ticketId).orElse(null);
        if (trainTicket == null) {
            throw new IllegalArgumentException("TrainTicket not found");
        }
        if (trainTicket.getUser().getId() != userId){
            throw new SecurityException("Ticket doesn't belong to user");
        }
        trainTicketRepository.delete(trainTicket);

        TrainSeat trainSeat = trainSeatRepository.findById(trainTicket.getTrainSeat().getId()).orElse(null);
        if (trainSeat == null) {
            throw new IllegalStateException("TrainSeat not found");
        }

        return trainTicket;
    }

    @Override
    public List<TrainSearchResult> searchTrainsByDepartureTime(String fromStation, String toStation, LocalDate departureDate) {
        // 计算查询日期的开始和结束时间（当天00:00:00到23:59:59）
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);

        // 查询符合条件的列车，按出发时间排序
        List<Train> trains = trainRepository.findByFromStationAndToStationAndDepartureTimeBetweenOrderByDepartureTime(
                fromStation, toStation, startOfDay, endOfDay);

        if (trains.isEmpty()) {
            throw new IllegalArgumentException("No trains found");
        }

        // 转换为搜索结果
        List<TrainSearchResult> results = new ArrayList<>();

        for (Train train : trains) {
            // 获取该列车的所有座位信息
            List<TrainSeat> trainSeats = trainSeatRepository.findByTrain(train);
            // 若列车没有设置座次，则不显示该列车
            if (trainSeats.isEmpty()) {
                continue;
            }
            // 创建搜索结果对象
            TrainSearchResult result = new TrainSearchResult();
            result.setTrain(train);
            result.setTrainSeats(trainSeats);

            results.add(result);
        }

        return results;
    }

    @Override
    public List<TrainSearchResult> searchTrainsByTravelDuration(String fromStation, String toStation, LocalDate departureDate) {
        // 计算查询日期的开始和结束时间（当天00:00:00到23:59:59）
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);

        // 查询符合条件的列车，按出发时间排序
        List<Train> trains = trainRepository.findByFromStationAndToStationAndDepartureTimeBetweenOrderByDurationMinutes(
                fromStation, toStation, startOfDay, endOfDay);

        if (trains.isEmpty()) {
            throw new IllegalArgumentException("No trains found");
        }

        // 转换为搜索结果
        List<TrainSearchResult> results = new ArrayList<>();

        for (Train train : trains) {
            // 获取该列车的所有座位信息
            List<TrainSeat> trainSeats = trainSeatRepository.findByTrain(train);
            // 若列车没有设置座次，则不显示该列车
            if (trainSeats.isEmpty()) {
                continue;
            }
            // 创建搜索结果对象
            TrainSearchResult result = new TrainSearchResult();
            result.setTrain(train);
            result.setTrainSeats(trainSeats);

            results.add(result);
        }

        return results;
    }
}
