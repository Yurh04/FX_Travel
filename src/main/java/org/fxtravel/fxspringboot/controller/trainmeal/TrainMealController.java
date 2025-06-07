package org.fxtravel.fxspringboot.controller.trainmeal;

import jakarta.servlet.http.HttpSession;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;
import org.fxtravel.fxspringboot.service.inter.trainmeal.TrainMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<train_meal>> getUserMealsByTrain(@RequestParam Integer trainId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<train_meal> meals = trainMealService.getMealsByTrain4User(trainId);
        return ResponseEntity.ok(meals);
    }

    /**
     * 用户复杂查询 - DTO参数版本
     * @param queryDTO 查询条件DTO
     * @return 符合条件的餐食列表
     */
    @PostMapping("/train_meal/query")
    public ResponseEntity<List<train_meal>> getUserMealsByConditions(@RequestBody TrainMealQueryDTO queryDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<train_meal> meals = trainMealService.getMealsByConditions4User(queryDTO);
        return ResponseEntity.ok(meals);
    }
}
