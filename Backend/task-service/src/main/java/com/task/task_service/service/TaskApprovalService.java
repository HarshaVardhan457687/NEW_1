package com.task.task_service.service;

import com.task.task_service.entity.TaskApproval;

public interface TaskApprovalService {
    public TaskApproval requestApproval(Long taskId, Long submitterId, Long projectId, Long teamId);

}
