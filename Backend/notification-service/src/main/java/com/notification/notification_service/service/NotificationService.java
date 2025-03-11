package com.notification.notification_service.service;

import com.notification.notification_service.DTO.NotificationRequestDTO;

public interface NotificationService {
    public void createAndSendNotification(NotificationRequestDTO request);
}
