package org.fxtravel.fxspringboot.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.enent.EventCenter;
import org.fxtravel.fxspringboot.enent.EventType;
import org.fxtravel.fxspringboot.enent.data.PaymentInfo;
import org.fxtravel.fxspringboot.mapper.TrainMealMapper;
import org.fxtravel.fxspringboot.mapper.TrainMealOrderMapper;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;
import org.fxtravel.fxspringboot.service.inter.GoodService;
import org.fxtravel.fxspringboot.service.inter.TrainMealOrderService;
import org.fxtravel.fxspringboot.service.inter.TrainMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TrainMealServiceImpl implements TrainMealService {

    @Autowired
    private TrainMealMapper trainMealMapper;

    @Autowired
    private EventCenter eventCenter;

    @PostConstruct
    public void init() {
        // 注册回调，确保在服务启动时就注册
        eventCenter.subscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
    }

    @PreDestroy
    public void destroy() {
        // 服务关闭时注销回调
        eventCenter.unsubscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
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

    // 用户查询接口实现

    @Override
    public List<train_meal> getMealsByTrain4User(Integer trainId) {
        return trainMealMapper.selectByTrain4User(trainId);
    }

    @Override
    public List<train_meal> getMealsByConditions4User(Integer trainId, String name, String mealTime,
                                                      Double priceMin, Double priceMax, Boolean remain) {
        return trainMealMapper.selectByConditions4User(trainId, name,mealTime, priceMin, priceMax, remain);
    }

    @Override
    public List<train_meal> getMealsByConditions4User(TrainMealQueryDTO queryDTO) {
        return getMealsByConditions4User(
                queryDTO.getTrainId(),
                queryDTO.getName(),
                queryDTO.getMealTime(),
                queryDTO.getPriceMin(),
                queryDTO.getPriceMax(),
                queryDTO.getRemain()
        );
    }

    // 管理员查询接口实现

    @Override
    public List<train_meal> getMealsByTrain4Admin(Integer trainId) {
        return trainMealMapper.selectByTrain4Admin(trainId);
    }

    @Override
    public List<train_meal> getMealsByConditions4Admin(Integer trainId, String name, String mealTime,
                                                       Double priceMin, Double priceMax, Boolean remain,
                                                       Boolean enabled) {
        return trainMealMapper.selectByConditions4Admin(
                trainId, name, mealTime, priceMin, priceMax, remain, enabled
        );
    }

    @Override
    public List<train_meal> getMealsByConditions4Admin(TrainMealQueryDTO queryDTO) {
        return getMealsByConditions4Admin(
                queryDTO.getTrainId(),
                queryDTO.getName(),
                queryDTO.getMealTime(),
                queryDTO.getPriceMin(),
                queryDTO.getPriceMax(),
                queryDTO.getRemain(),
                queryDTO.getEnabled()
        );
    }

    // 管理员增删改接口实现

    @Override
    public boolean addMeal(train_meal meal) {
        // 设置默认值
        if (meal.getRemain() == null) {
            meal.setRemain(0);
        }
        if (meal.getEnabled() == null) {
            meal.setEnabled(true);
        }
        return trainMealMapper.insert(meal) > 0;
    }

    @Override
    public List<train_meal> batchAddMeals(List<train_meal> meals) {
        if (meals == null || meals.isEmpty()) {
            return Collections.emptyList();
        }

        List<train_meal> addedMeals = new ArrayList<>();
        for (train_meal meal : meals) {
            // 设置默认值
            if (meal.getRemain() == null) {
                meal.setRemain(0);
            }
            if (meal.getEnabled() == null) {
                meal.setEnabled(true);
            }

            int result = trainMealMapper.insert(meal);
            if (result > 0) {
                addedMeals.add(meal);
            }
        }

        return addedMeals;
    }

    @Override
    public boolean updateMeal(train_meal meal) {
        return trainMealMapper.updateById(meal) > 0;
    }

    @Override
    public boolean disableMeal(Integer id) {
        train_meal meal = new train_meal();
        meal.setId(id);
        meal.setEnabled(false);
        return updateMeal(meal);
    }

    @Override
    public boolean enableMeal(Integer id) {
        train_meal meal = new train_meal();
        meal.setId(id);
        meal.setEnabled(true);
        return updateMeal(meal);
    }

    @Override
    public boolean deleteMeal(Integer id) {
        int result = trainMealMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public train_meal getMealById(Integer id) {
        return trainMealMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean checkAndGet(int id, int count) {
        // 1. 检查库存是否充足
        train_meal obj = trainMealMapper.selectByIdForUpdate(id);
        if (obj == null || obj.getRemain() < count) {
            return false;
        }

        // 2. 扣减库存
        int updated = trainMealMapper.deductInventory(id, count);

        return updated > 0;
    }

    @Override
    public void putBack(int id, int count) {
        trainMealMapper.addInventory(id, count);
    }
}
