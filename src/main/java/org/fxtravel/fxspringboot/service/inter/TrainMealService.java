package org.fxtravel.fxspringboot.service.inter;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;

import java.util.List;

public interface TrainMealService extends GoodService {
    // 用户查询接口
    /**
     * 根据列车ID获取用户可见的餐食列表（enabled=true）
     * @param trainId 列车ID
     * @return 餐食列表
     */
    List<train_meal> getMealsByTrain4User(Integer trainId);

    /**
     * 用户复杂查询（自动过滤enabled=false的记录）
     * @param trainId 列车ID
     * @param name 餐食名称（模糊查询）
     * @param mealTime 餐食时间枚举值
     * @param priceMin 最低价格
     * @param priceMax 最高价格
     * @param remain 是否只查询有余量的
     * @return 符合条件的餐食列表
     */
    List<train_meal> getMealsByConditions4User(Integer trainId, String name, String mealTime,
                                              Double priceMin, Double priceMax, Boolean remain);

    /**
     * 用户复杂查询（DTO参数版本，自动过滤enabled=false的记录）
     * @param queryDTO 查询条件DTO（会自动忽略其中的enabled字段）
     * @return 符合条件的餐食列表
     */
    List<train_meal> getMealsByConditions4User(TrainMealQueryDTO queryDTO);

    /**
     * 根据ID获取餐食详情（预留，暂无实际用处）
     * @param id 餐食ID
     * @return 餐食实体
     */
    train_meal getMealById(Integer id);
}