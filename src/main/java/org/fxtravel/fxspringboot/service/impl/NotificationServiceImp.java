package org.fxtravel.fxspringboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.fxtravel.fxspringboot.mapper.NotificationMapper;
import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp {
    private final NotificationMapper notificationMapper;

    @Async
    public void createNotification(NotificationRequestDTO request) {
        // 合并构建和发送逻辑
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(request.getUserId());
        notification.setEventType(request.getEventType());
        notification.setContent(generateContent(request));
        notification.setChannel(request.getPreferredChannel() != null ?
                request.getPreferredChannel() : "IN_APP");
        notification.setStatus("PENDING");
        notification.setCreateTime(LocalDateTime.now());

        notificationMapper.insert(notification);
        sendNotification(notification); // 直接调用发送
    }

    private void sendNotification(Notification notification) {
        try {
            boolean success = sendToChannel(notification);
            notification.setStatus(success ? "SUCCESS" : "FAILED");
            notification.setUpdateTime(LocalDateTime.now());
            notificationMapper.update(notification);
        } catch (Exception e) {
            notification.setStatus("FAILED");
            notification.setUpdateTime(LocalDateTime.now());
            notificationMapper.update(notification);
        }
    }

    public List<Notification> getUserNotifications(Integer userId) {
        return notificationMapper.findByUserId(userId);
    }
    private String generateContent(NotificationRequestDTO request) {
        // 根据事件类型生成不同的通知内容
        switch (request.getEventType()) {
            case "TICKET_ORDER":
                return "您的车票订单已成功创建";
            case "HOTEL_ORDER":
                return "您的酒店预订已确认";
            case "MEAL_ORDER":
                return "您的餐食订单已接收";
            default:
                return "您有一条新的通知";
        }
    }

    private boolean sendToChannel(Notification notification) {
        try {
            // 根据渠道类型发送通知
            switch (notification.getChannel()) {
                case "EMAIL":
                    return sendEmail(notification);
                case "IN_APP":
                    return sendInApp(notification);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sendEmail(Notification notification) {
        // 实际发送邮件的逻辑
        // 这里应该是调用邮件服务发送邮件
        // 返回true表示发送成功，false表示失败
        return true; // 模拟发送成功
    }

    private boolean sendInApp(Notification notification) {
        // 实际发送应用内通知的逻辑
        // 这里应该是调用WebSocket或其他应用内通知机制
        // 返回true表示发送成功，false表示失败
        return true; // 模拟发送成功
    }
}
