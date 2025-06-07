package org.fxtravel.fxspringboot.service.impl.trainmeal;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.event.EventCenter;
import org.fxtravel.fxspringboot.event.EventType;
import org.fxtravel.fxspringboot.event.data.PaymentInfo;
import org.fxtravel.fxspringboot.mapper.TrainMealMapper;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;
import org.fxtravel.fxspringboot.service.inter.trainmeal.TrainMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                putBack(info.getGoodId(), info.getQuantity(), null);
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

    @Override
    public train_meal getMealById(Integer id) {
        return trainMealMapper.selectById(id);
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean checkAndGet(int id, int count, Object data) {
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
    public void putBack(int id, int count, Object data) {
        trainMealMapper.addInventory(id, count);
    }
}
