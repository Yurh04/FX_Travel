package org.fxtravel.fxspringboot.controller.trainmeal;

import jakarta.servlet.http.HttpSession;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.pojo.entities.trainmeal.TrainMeal;
import org.fxtravel.fxspringboot.service.inter.trainmeal.TrainMealService;
import org.fxtravel.fxspringboot.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/train/meal")
public class TrainMealController {
    @Autowired
    private TrainMealService trainMealService;

    @GetMapping("/")
    public ResponseEntity<?> getUserMeals(@RequestParam Integer trainId,
                                                        BindingResult bindingResult,
                                                        HttpSession session) {
        User user = (User) session.getAttribute("user");

        ResponseEntity<? extends Map<String, ?>> errors = AuthUtil.check(bindingResult, user);
        if (errors != null) return errors;



        List<TrainMeal> results = trainMealService.getMealsByTrain4User(trainId);
        return ResponseEntity.ok(Map.of(
                "message", "查询成功",
                "data", results,
                "sortBy", "price"
        ));
    }
}
