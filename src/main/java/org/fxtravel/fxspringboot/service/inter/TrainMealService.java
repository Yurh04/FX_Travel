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

    // 管理员查询接口
    /**
     * 根据列车ID获取所有餐食列表（包含enabled=false的记录）
     * @param trainId 列车ID
     * @return 餐食列表
     */
    List<train_meal> getMealsByTrain4Admin(Integer trainId);

    /**
     * 管理员复杂查询（多参数版本，可查询所有记录）
     * @param trainId 列车ID（必须）
     * @param name 餐食名称（模糊查询）
     * @param mealTime 餐食时间枚举值
     * @param priceMin 最低价格
     * @param priceMax 最高价格
     * @param remain 是否只查询有余量的
     * @param enabled 是否只查询启用的（null表示查询所有状态）
     * @return 符合条件的餐食列表
     */
    public List<train_meal> getMealsByConditions4Admin(Integer trainId, String name, String mealTime,
                                                      Double priceMin, Double priceMax, Boolean remain,
                                                      Boolean enabled);

    /**
     * 管理员复杂查询（DTO参数版本）
     * @param queryDTO 查询条件DTO
     * @return 符合条件的餐食列表
     */
    List<train_meal> getMealsByConditions4Admin(TrainMealQueryDTO queryDTO);

    // 管理员增删改接口
    /**
     * 添加餐食
     * @param meal 餐食实体
     * @return 是否添加成功
     */
    boolean addMeal(train_meal meal);

    /**
     * 批量添加餐食
     * @param meals 餐食实体列表
     * @return 新增成功的餐食列表
     */
    List<train_meal> batchAddMeals(List<train_meal> meals);

    /**
     * 更新餐食信息
     * @param meal 餐食实体
     * @return 是否更新成功
     */
    boolean updateMeal(train_meal meal);

    /**
     * 删除餐食（逻辑删除，设置enabled=false）
     * @param id 餐食ID
     * @return 是否删除成功
     */
    boolean disableMeal(Integer id);

    /**
     * 启用餐食（设置enabled=true）
     * @param id 餐食ID
     * @return 是否启用成功
     */
    boolean enableMeal(Integer id);

    /**
     * 物理删除餐食（从数据库彻底删除记录）
     * @param id 餐食ID
     * @return 是否删除成功
     */
    boolean deleteMeal(Integer id);

    /**
     * 根据ID获取餐食详情（预留，暂无实际用处）
     * @param id 餐食ID
     * @return 餐食实体
     */
    train_meal getMealById(Integer id);
}