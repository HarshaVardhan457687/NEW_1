package com.notification.notification_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.notification.notification_service.util.ComplexLongIdGenerator")
    private Long notificationId;
    private String message;
    private String type;
    private Long targetUser;
    private LocalDateTime createdAt;
    private boolean isRead = false;
}
