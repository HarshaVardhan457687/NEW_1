package com.task.task_service.service;

import com.task.task_service.entity.TaskApproval;

import java.util.List;

public interface TaskApprovalService {
    public TaskApproval requestApproval(Long taskId, Long submitterId, String role, Long id);
    public TaskApproval approveTask(Long approvalId);
    public TaskApproval rejectTask(Long approvalId);
    public List<TaskApproval> getAllTaskApprovalOfProject(Long projectId);
    public List<TaskApproval> getAllTaskApprovalOfTeam(Long teamId);
}
