package org.fxtravel.fxspringboot.controller.trainseat;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.train.AddTrainSeatRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.SearchTrainRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.dto.train.UpdateTrainSeatRequest;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.TrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/train")
public class TrainSeatController {
    @Autowired
    TrainSeatService trainSeatService;

    @GetMapping("/")
    public Train getTrain(Integer id) {
        return trainSeatService.getTrainById(id);
    }

    // 按出发时间排序查询车次接口
    @PostMapping("/seat/by-departure-time")
    public ResponseEntity<?> searchTrainByDepartureTime(@Valid @RequestBody SearchTrainRequest request,
                                                        BindingResult bindingResult,
                                                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        try {
            List<TrainSearchResult> results = trainSeatService.findByRouteAndTimeOrderByTime(
                    request.getDepartureStation(),
                    request.getArrivalStation(),
                    request.getDepartureDate()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "查询成功",
                    "data", results,
                    "sortBy", "departureTime"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询失败: " + e.getMessage()));
        }
    }

    @PostMapping("/seat/by-duration-time")
    public ResponseEntity<?> searchTrainByDuration(@Valid @RequestBody SearchTrainRequest request,
                                                        BindingResult bindingResult,
                                                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        try {
            List<TrainSearchResult> results = trainSeatService.findByRouteAndTimeOrderByDuration(
                    request.getDepartureStation(),
                    request.getArrivalStation(),
                    request.getDepartureDate()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "查询成功",
                    "data", results,
                    "sortBy", "departureTime"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询失败: " + e.getMessage()));
        }
    }
}
