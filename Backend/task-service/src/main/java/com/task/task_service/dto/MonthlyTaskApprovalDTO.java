package com.task.task_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonthlyTaskApprovalDTO {
    private int year;
    private int month;
    private long totalTasks;
}
