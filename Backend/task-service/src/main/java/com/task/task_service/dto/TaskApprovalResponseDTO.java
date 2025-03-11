package com.task.task_service.dto;

import com.task.task_service.constants.ApprovalStatus;
import com.task.task_service.constants.TaskPriority;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskApprovalResponseDTO {

    private Long taskApprovalId;
    private String taskName;
    private String taskTag;
    private String ownerName;
    private TaskPriority taskPriority;
    private String taskDescription;
    private Date taskDueDate;
    private ApprovalStatus approvalStatus;
}
