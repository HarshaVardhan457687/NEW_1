package com.notification.notification_service.service;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService {
    public void createAndSendNotification(NotificationRequestDTO request);
    public void readNotification(Long notificationId);
    public List<Notification> getNotificationForUser(Long userId);
    public List<Notification> getAllnotification();
    
    /**
     * Utility method to convert various payload types to Long
     * @param rawPayload The payload object to convert
     * @return The converted Long value, or null if conversion fails
     */
    Long convertPayloadToLong(Object rawPayload);
    
    /**
     * Handles marking a notification as read via WebSocket
     * @param rawPayload The raw payload containing the notification ID
     */
    void handleMarkNotificationAsRead(Object rawPayload);
    
    /**
     * Handles sending all notifications for a user via WebSocket
     * @param rawPayload The raw payload containing the user ID
     */
    void handleGetUserNotifications(Object rawPayload);
}
