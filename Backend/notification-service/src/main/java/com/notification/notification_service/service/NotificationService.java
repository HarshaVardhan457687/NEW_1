package com.notification.notification_service.service;

import com.notification.notification_service.entity.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
