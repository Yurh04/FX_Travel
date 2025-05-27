package org.fxtravel.fxspringboot.controller;

import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealDTO;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;
import org.fxtravel.fxspringboot.service.inter.TrainMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainMealController {
    @Autowired
    private TrainMealService trainMealService;

    // -------------------- 用户接口 --------------------

    /**
     * 用户基础查询 - 根据列车ID获取可见餐食
     * @param trainId 列车ID
     * @return 餐食列表
     */
    @GetMapping("/train_meal")
    public ResponseEntity<List<train_meal>> getUserMealsByTrain(@RequestParam Integer trainId) {
        List<train_meal> meals = trainMealService.getMealsByTrain4User(trainId);
        return ResponseEntity.ok(meals);
    }

    /**
     * 用户复杂查询 - DTO参数版本
     * @param queryDTO 查询条件DTO
     * @return 符合条件的餐食列表
     */
    @PostMapping("/train_meal/query")
    public ResponseEntity<List<train_meal>> getUserMealsByConditions(@RequestBody TrainMealQueryDTO queryDTO) {
        List<train_meal> meals = trainMealService.getMealsByConditions4User(queryDTO);
        return ResponseEntity.ok(meals);
    }

    // -------------------- 管理员接口 --------------------

    /**
     * 管理员基础查询 - 根据列车ID获取所有餐食
     * @param trainId 列车ID
     * @return 餐食列表
     */
    @GetMapping("/train_meal_admin")
    public ResponseEntity<List<train_meal>> getAdminMealsByTrain(@RequestParam Integer trainId) {
        List<train_meal> meals = trainMealService.getMealsByTrain4Admin(trainId);
        return ResponseEntity.ok(meals);
    }

    /**
     * 管理员复杂查询 - DTO参数版本
     * @param queryDTO 查询条件DTO
     * @return 符合条件的餐食列表
     */
    @PostMapping("/train_meal_admin/query")
    public ResponseEntity<List<train_meal>> getAdminMealsByConditions(@RequestBody TrainMealQueryDTO queryDTO) {
        List<train_meal> meals = trainMealService.getMealsByConditions4Admin(queryDTO);
        return ResponseEntity.ok(meals);
    }

    /**
     * 添加餐食
     * @param dto 餐食实体
     * @return 操作结果
     */
    @PostMapping("/train_meal_admin")
    public ResponseEntity<Boolean> addMeal(@RequestBody TrainMealDTO dto) {
        train_meal meal = new train_meal();
        BeanUtils.copyProperties(dto, meal);
        boolean result = trainMealService.addMeal(meal);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量添加餐食
     * @param dtos 餐食实体列表
     * @return 新增成功的餐食列表
     */
    @PostMapping("/train_meal_admin/batch")
    public ResponseEntity<List<train_meal>> batchAddMeals(@RequestBody List<TrainMealDTO> dtos) {
        List<train_meal> meals = new ArrayList<>();
        for (TrainMealDTO dto : dtos) {
            train_meal meal = new train_meal();
            BeanUtils.copyProperties(dto, meal);
            meals.add(meal);
        }
        List<train_meal> addedMeals = trainMealService.batchAddMeals(meals);
        return ResponseEntity.ok(addedMeals);
    }

    /**
     * 更新餐食信息
     * @param meal 餐食实体
     * @return 操作结果
     */
    @PutMapping("/train_meal_admin")
    public ResponseEntity<Boolean> updateMeal(@RequestBody train_meal meal) {
        boolean result = trainMealService.updateMeal(meal);
        return ResponseEntity.ok(result);
    }

    /**
     * 禁用餐食（逻辑删除）
     * @param id 餐食ID
     * @return 操作结果
     */
    @DeleteMapping("/train_meal_admin/disable")
    public ResponseEntity<Boolean> disableMeal(@RequestParam Integer id) {
        boolean result = trainMealService.disableMeal(id);
        return ResponseEntity.ok(result);
    }

    /**
     * 启用餐食
     * @param id 餐食ID
     * @return 操作结果
     */
    @PutMapping("/train_meal_admin/enable")
    public ResponseEntity<Boolean> enableMeal(@RequestParam Integer id) {
        boolean result = trainMealService.enableMeal(id);
        return ResponseEntity.ok(result);
    }

    /**
     * 物理删除餐食（从数据库彻底删除记录）
     * @param id 餐食ID
     * @return 是否删除成功
     */
    @DeleteMapping("/train_meal_admin/delete")
    public ResponseEntity<Boolean> deleteMeal(@RequestParam Integer id) {
        boolean result = trainMealService.deleteMeal(id);
        return ResponseEntity.ok(result);
    }
}
