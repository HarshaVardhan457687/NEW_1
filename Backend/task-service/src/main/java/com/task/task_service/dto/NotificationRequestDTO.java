package com.task.task_service.dto;

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
