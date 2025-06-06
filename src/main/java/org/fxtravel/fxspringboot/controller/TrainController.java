package org.fxtravel.fxspringboot.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.train.AddTrainRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.UpdateTrainRequest;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.TrainService;
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
public class TrainController {
    @Autowired
    private TrainService trainService;
    @PostMapping("/admin/add")
    public ResponseEntity<?> addTrain(@Valid @RequestBody AddTrainRequest request,
                                      BindingResult bindingResult,
                                      HttpSession session) {
        // 检查验证结果
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            trainService.addTrain(request.getTrainNumber(), request.getTrainType(),
                    request.getFromStation(), request.getToStation(),
                    request.getDepartureTime(), request.getArrivalTime());
            return ResponseEntity.ok(Map.of("message", "已成功添加该列车"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/admin/delete/{trainId}")
    public ResponseEntity<?> deleteTrain(@PathVariable Integer trainId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            boolean deleted = trainService.deleteTrain(trainId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "已成功删除列车班次" + trainId));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "列车班次不存在或删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "删除列车班次时发生错误: " + e.getMessage()));
        }
    }

    @PostMapping("/admin/update/{trainId}")
    public ResponseEntity<?> updateTrain(@PathVariable Integer trainId,
                                         @RequestBody UpdateTrainRequest request,
                                         HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            boolean updated = trainService.updateTrain(trainId, request.getTrainType(),request.getTrainNumber(),
                    request.getFromStation(), request.getToStation(),
                    request.getDepartureTime(), request.getArrivalTime());
            if (updated) {
                return ResponseEntity.ok(Map.of("message", "已成功更新列车班次 " + trainId + " 的信息"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "列车班次不存在或更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "更新列车信息时发生错误: " + e.getMessage()));
        }
    }

    @GetMapping("/admin/list")
    public ResponseEntity<?> getAllTrains(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            List<Train> trains = trainService.getAllTrains();
            return ResponseEntity.ok(Map.of("trains", trains));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取列车列表时发生错误: " + e.getMessage()));
        }
    }

    @GetMapping("/{trainId}")
    public ResponseEntity<?> getTrainById(@PathVariable Integer trainId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }
        try {
            Train train = trainService.getTrainById(trainId);
            if (train != null) {
                return ResponseEntity.ok(train);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "列车不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取列车信息时发生错误: " + e.getMessage()));
        }
    }
}
