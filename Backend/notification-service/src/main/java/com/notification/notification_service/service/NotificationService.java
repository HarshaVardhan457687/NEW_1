package com.notification.notification_service.service;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService {
    public void createAndSendNotification(NotificationRequestDTO request);
    public void readNotification(Long notificationId);
    public List<Notification> getNotificationForUser(Long userId);
}
