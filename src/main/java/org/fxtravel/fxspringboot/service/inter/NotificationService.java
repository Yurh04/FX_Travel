package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Notification;

import java.util.List;

public interface NotificationService {
    void createNotification(NotificationRequestDTO request);
    List<Notification> getUserNotifications(Integer userId);

}