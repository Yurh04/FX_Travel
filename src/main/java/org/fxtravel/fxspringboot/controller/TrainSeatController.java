package org.fxtravel.fxspringboot.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.train.AddTrainSeatRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.UpdateTrainSeatRequest;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
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
@RequestMapping("/api/train/seat")
public class TrainSeatController {
    @Autowired
    TrainSeatService trainSeatService;

    @PostMapping("/admin/add")
    public ResponseEntity<?> addTrainSeat(@Valid @RequestBody AddTrainSeatRequest request,
                                          BindingResult bindingResult,
                                          HttpSession session) {
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
            trainSeatService.addTrainSeat(request.getTrainId(), request.getSeatType(),request.getPrice(), request.getAvailable());
            return ResponseEntity.ok(Map.of("message", "已成功添加该列车座次"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
    // 删除座位接口
    @DeleteMapping("/admin/delete/{seatId}")
    public ResponseEntity<?> deleteTrainSeat(@PathVariable Integer seatId,
                                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if (user.getRole().equals(Role.REGULAR)) {
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            trainSeatService.deleteTrainSeat(seatId);
            return ResponseEntity.ok(Map.of("message", "已成功删除该座位"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/get/{seatId}")
    public ResponseEntity<?> getTrainSeat(@PathVariable Integer seatId,
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        try {
            TrainSeat seat = trainSeatService.getTrainSeatById(seatId);
            if (seat == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(seat);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list/{trainId}")
    public ResponseEntity<?> getTrainSeatsByTrainId(@PathVariable Integer trainId,
                                                    HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        try {
            List<TrainSeat> seats = trainSeatService.getTrainSeatsByTrainId(trainId);
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // 更新座位接口
    @PutMapping("/update/{seatId}")
    public ResponseEntity<?> updateTrainSeat(@PathVariable Integer seatId,
                                             @Valid @RequestBody UpdateTrainSeatRequest request,
                                             BindingResult bindingResult,
                                             HttpSession session) {
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
        if (user.getRole().equals(Role.REGULAR)) {
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }

        try {
            trainSeatService.updateTrainSeat(seatId, request.getPrice(), request.getAvailable());
            return ResponseEntity.ok(Map.of("message", "已成功更新该座位信息"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
