package com.notification.notification_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    private Long notificationId;
    private String message;
    private String type;
    private Long targetUser;
    private LocalDateTime createdAt;
    private boolean isRead = false;
}
