package org.fxtravel.fxspringboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.fxtravel.fxspringboot.common.E_NotificationChannel;
import org.fxtravel.fxspringboot.common.E_NotificationStatus;
import org.fxtravel.fxspringboot.common.E_NotificationEventType;
import org.fxtravel.fxspringboot.mapper.NotificationMapper;
import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Notification;
import org.fxtravel.fxspringboot.service.inter.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp {
    @Autowired
    private final NotificationMapper notificationMapper;

    @Autowired
    private MailService mailService;


    @Async
    public void createNotification(NotificationRequestDTO request) {
        // 合并构建和发送逻辑
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(request.getUserId());
        notification.setEventType(request.getEventType());
        notification.setContent(generateContent(request));
        notification.setChannel(request.getPreferredChannel() != null ?
                request.getPreferredChannel() : E_NotificationChannel.IN_APP);
        notification.setStatus(E_NotificationStatus.PENDING);
        notification.setCreateTime(LocalDateTime.now());

        notificationMapper.insert(notification);
        sendNotification(notification); // 直接调用发送
    }

    private void sendNotification(Notification notification) {
        try {
            boolean success = sendToChannel(notification);
            notification.setStatus(success ? E_NotificationStatus.SUCCESS : E_NotificationStatus.FAILED);
            notification.setUpdateTime(LocalDateTime.now());
            notificationMapper.update(notification);
        } catch (Exception e) {
            notification.setStatus(E_NotificationStatus.FAILED);
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
            case TICKET_PURCHASED:
                return "您的车票订单已成功创建";
            case HOTEL_BOOKED:
                return "您的酒店预订已确认";
            case MEAL_ORDERED:
                return "您的餐食订单已接收";
            case ORDER_CANCELLED:
                return "取消订单成功";
            case PAYMENT_COMPLETED:
                return "成功支付";
            default:
                return "您有一条新的通知";
        }
    }

    private boolean sendToChannel(Notification notification) {
        try {
            // 根据渠道类型发送通知
            switch (notification.getChannel()) {
                case EMAIL:
                    return sendEmail(notification);
                case IN_APP:
                    return sendInApp(notification);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sendEmail(Notification notification) {
        try {
            return mailService.sendNotificationEmail(notification);
        } catch (Exception e) {
            log.error("邮件通知发送失败");
            return false;
        }
    }

    private boolean sendInApp(Notification notification) {
       //还没实现
       return true;
    }

}
