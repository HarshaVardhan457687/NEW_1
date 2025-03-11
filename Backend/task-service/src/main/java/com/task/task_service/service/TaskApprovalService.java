package com.task.task_service.service;

import com.task.task_service.dto.TaskApprovalResponseDTO;
import com.task.task_service.entity.TaskApproval;

import java.util.List;

public interface TaskApprovalService {
    public TaskApproval requestApproval(Long taskId, String role, Long id);
    public TaskApproval approveTask(Long approvalId);
    public TaskApproval rejectTask(Long approvalId);
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfProject(Long projectId);
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfTeam(Long teamId);
}
