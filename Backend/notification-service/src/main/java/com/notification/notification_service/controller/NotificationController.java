package com.notification.notification_service.controller;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.entity.Notification;
import com.notification.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationRequestDTO request) {
        notificationService.createAndSendNotification(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getNotificationForUSer(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationForUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotification() {
        return ResponseEntity.ok(notificationService.getAllnotification());
    }
    

}