package org.fxtravel.fxspringboot.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Notification;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> triggerNotification(
            @Valid @RequestBody NotificationRequestDTO request,
            BindingResult bindingResult,
            HttpSession session) {

        // 参数验证
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        // 权限验证
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "未登录"));
        }

        try {
            notificationService.createNotification(request);
            return ResponseEntity.accepted()
                    .body(Map.of("message", "通知请求已接收"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "创建通知失败: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getNotificationHistory(
            @RequestParam Integer userId,
            HttpSession session) {

        // 权限验证
        User user = (User) session.getAttribute("user");
        if (user == null || user.getId()!=userId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "无权访问该用户的通知记录"));
        }

        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(Map.of("notifications", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取通知历史失败: " + e.getMessage()));
        }
    }
}