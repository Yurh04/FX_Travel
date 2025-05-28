package org.fxtravel.fxspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.train.GetTicketRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.SearchTrainRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.TrainTicket;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.TrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;
@RestController
@RequestMapping("/api/v1/train")
public class TrainTicketController {
    @Autowired
    private TrainTicketService trainTicketService;

    // 按出发时间排序查询车次接口
    @GetMapping("/search/by-departure-time")
    public ResponseEntity<?> searchTrainByDepartureTime(@Valid @RequestBody SearchTrainRequest request,
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

        try {
            List<TrainSearchResult> results = trainTicketService.searchTrainsByDepartureTime(
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

    // 按总旅途时间排序查询车次接口
    @GetMapping("/search/by-travel-duration")
    public ResponseEntity<?> searchTrainByTravelDuration(@Valid @RequestBody SearchTrainRequest request,
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

        try {
            List<TrainSearchResult> results = trainTicketService.searchTrainsByTravelDuration(
                    request.getDepartureStation(),
                    request.getArrivalStation(),
                    request.getDepartureDate()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "查询成功",
                    "data", results,
                    "sortBy", "travelDuration"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询失败: " + e.getMessage()));
        }
    }

    // 根据座次生成车票接口
    @PutMapping("/ticket/get/{userId}")
    public ResponseEntity<?> getTicket(@PathVariable Integer userId,
                                       @Valid @RequestBody GetTicketRequest request,
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

        // 验证用户只能为自己生成车票（除非是管理员）
        if (!user.getRole().equals(Role.ADMIN) && user.getId() != (userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "无权限为其他用户生成车票"));
        }

        try {
            // 调用服务层生成车票
            TrainTicket ticket = trainTicketService.generateTicket(user, request.getTrainSeatId());

            return ResponseEntity.ok(Map.of(
                    "message", "车票生成成功",
                    "ticket", ticket
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "生成车票失败: " + e.getMessage()));
        }
    }

    // 取消车票接口
    @PutMapping("/ticket/cancel/{ticketId}")
    public ResponseEntity<?> cancelTicket(@PathVariable Integer ticketId,
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        try {
            // 调用服务层取消车票
            TrainTicket cancelledTicket = trainTicketService.cancelTicket(ticketId, user.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "车票取消成功",
                    "ticket", cancelledTicket,
                    "refundAmount", cancelledTicket.getTrainSeat().getPrice()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "取消车票失败: " + e.getMessage()));
        }
    }
}
