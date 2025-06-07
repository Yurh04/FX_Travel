package org.fxtravel.fxspringboot.service.inter.trainmeal;

import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.trainmeal.TrainMeal;
import org.fxtravel.fxspringboot.service.inter.common.GoodService;

import java.util.List;

public interface TrainMealService extends GoodService {
    List<TrainMeal> getMealsByTrain4User(Integer trainId);
    TrainMeal getMealById(Integer id);
}