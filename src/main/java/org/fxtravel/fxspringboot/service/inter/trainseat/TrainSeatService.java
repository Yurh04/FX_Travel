package org.fxtravel.fxspringboot.service.inter.trainseat;

import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.service.inter.common.GoodService;

import java.time.LocalDate;
import java.util.List;

public interface TrainSeatService extends GoodService {
    Train getTrainById(Integer trainId);
    List<TrainSearchResult> findByRouteAndTimeOrderByTime(String fromStation, String toStation, LocalDate departureDate);
    List<TrainSearchResult> findByRouteAndTimeOrderByDuration(String fromStation, String toStation, LocalDate departureDate);
}
