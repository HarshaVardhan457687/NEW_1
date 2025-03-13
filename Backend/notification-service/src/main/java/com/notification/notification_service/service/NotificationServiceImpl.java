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

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void createAndSendNotification(NotificationRequestDTO request) {
        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setTargetUser(request.getTargetUser());
        notification.setCreatedAt(request.getCreatedTime());
        notification.setTargetUser(request.getTargetUser());

        notification = notificationRepository.save(notification);

        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + notification.getTargetUser(),
                notification
        );
    }

    @Override
    public void readNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID : "+ notificationId ));
        notification.setRead(true);
    }

    @Override
    public List<Notification> getNotificationForUser(Long userId){
        return notificationRepository.findByTargetUser(userId);
    }

    @Override
    public List<Notification> getAllnotification(){
        return notificationRepository.findAll();
    }
}