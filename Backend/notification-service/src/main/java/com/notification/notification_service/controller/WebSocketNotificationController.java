package com.notification.notification_service.controller;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WebSocketNotificationController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketNotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Handles WebSocket messages sent to /app/notification
     * Creates and sends a notification to the specified user
     */
    @MessageMapping("/notification")
    public void sendNotification(@Payload NotificationRequestDTO request) {
        if (request.getCreatedTime() == null) {
            request.setCreatedTime(java.time.LocalDateTime.now());
        }
        notificationService.createAndSendNotification(request);
    }

    /**
     * Handles WebSocket messages sent to /app/notification/read
     * Marks a notification as read
     */
    @MessageMapping("/notification/read")
    public void markNotificationAsRead(@Payload Object rawPayload) {
        try {
            notificationService.handleMarkNotificationAsRead(rawPayload);
        } catch (Exception e) {
            logger.error("Error in markNotificationAsRead controller method", e);
        }
    }
    
    /**
     * Handles WebSocket messages sent to /app/notifications/user
     * Sends all notifications for a specific user as a single message
     */
    @MessageMapping("/notifications/user")
    public void getUserNotifications(@Payload Object rawPayload) {
        try {
            notificationService.handleGetUserNotifications(rawPayload);
        } catch (Exception e) {
            logger.error("Error in getUserNotifications controller method", e);
        }
    }
}