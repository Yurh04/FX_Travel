package org.fxtravel.fxspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Notification;
import org.fxtravel.fxspringboot.service.impl.NotificationServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImp notificationService;

    @PostMapping
    public ResponseEntity<Void> triggerNotification(@RequestBody NotificationRequestDTO request) {
        notificationService.createNotification(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<Notification>> getNotificationHistory(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
}
