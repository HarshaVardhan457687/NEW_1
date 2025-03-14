package com.notification.notification_service.service;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.entity.Notification;
import com.notification.notification_service.exception.NotificationNotFoundException;
import com.notification.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void createAndSendNotification(NotificationRequestDTO request) {
        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setTargetUser(request.getTargetUser());
        notification.setCreatedAt(request.getCreatedTime() != null ? request.getCreatedTime() : LocalDateTime.now());
        notification.setRead(false);

        notification = notificationRepository.save(notification);

        // Send real-time notification via WebSocket to user-specific queue
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + notification.getTargetUser(),
                notification
        );
    }

    @Override
    public void readNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID : "+ notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
        
        // Send an update to the user that the notification has been read
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + notification.getTargetUser() + "/updates",
                notification
        );
    }

    @Override
    public List<Notification> getNotificationForUser(Long userId){
        return notificationRepository.findByTargetUser(userId);
    }

    @Override
    public List<Notification> getAllnotification(){
        return notificationRepository.findAll();
    }
    
    /**
     * Utility method to convert various payload types to Long
     * @param rawPayload The payload object to convert
     * @return The converted Long value, or null if conversion fails
     */
    public Long convertPayloadToLong(Object rawPayload) {
        try {
            if (rawPayload instanceof Long) {
                return (Long) rawPayload;
            } else if (rawPayload instanceof Integer) {
                return ((Integer) rawPayload).longValue();
            } else if (rawPayload instanceof String) {
                return Long.parseLong((String) rawPayload);
            } else if (rawPayload instanceof byte[]) {
                // Handle byte array payload
                byte[] bytes = (byte[]) rawPayload;
                String payloadStr = new String(bytes);
                return Long.parseLong(payloadStr);
            } else {
                logger.error("Unexpected payload type: {}", rawPayload.getClass().getName());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error converting payload to Long", e);
            return null;
        }
    }
    
    /**
     * Handles marking a notification as read via WebSocket
     * @param rawPayload The raw payload containing the notification ID
     */
    public void handleMarkNotificationAsRead(Object rawPayload) {
        Long notificationId = convertPayloadToLong(rawPayload);
        if (notificationId != null) {
            readNotification(notificationId);
        }
    }
    
    /**
     * Handles sending all notifications for a user via WebSocket
     * @param rawPayload The raw payload containing the user ID
     */
    public void handleGetUserNotifications(Object rawPayload) {
        Long userId = convertPayloadToLong(rawPayload);
        if (userId != null) {
            List<Notification> notifications = getNotificationForUser(userId);
            
            // Send all notifications as a single message to a specific destination for initial loads
            Map<String, Object> response = new HashMap<>();
            response.put("notifications", notifications);
            response.put("type", "INITIAL_LOAD");
            
            String destination = "/queue/notifications/" + userId + "/initial";
            messagingTemplate.convertAndSend(destination, response);
            
            // Also send to the regular notifications queue for backward compatibility
            for (Notification notification : notifications) {
                messagingTemplate.convertAndSend("/queue/notifications/" + userId, notification);
            }
        }
    }
}