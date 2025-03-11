package com.notification.notification_service.controller;

import com.notification.notification_service.DTO.NotificationRequestDTO;
import com.notification.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}