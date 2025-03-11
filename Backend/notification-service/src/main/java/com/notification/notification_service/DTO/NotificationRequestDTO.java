package com.notification.notification_service.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestDTO {
    private String message;
    private Long targetUser;
    private Long taskId;
    private LocalDateTime createdTime;

}
