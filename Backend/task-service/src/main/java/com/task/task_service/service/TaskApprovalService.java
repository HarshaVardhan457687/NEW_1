package com.task.task_service.service;

import com.task.task_service.dto.MonthlyTaskApprovalDTO;
import com.task.task_service.dto.TaskApprovalResponseDTO;
import com.task.task_service.entity.TaskApproval;

import java.util.List;

public interface TaskApprovalService {
    public List<TaskApproval> getAllTaskApproval();
    public TaskApproval requestApproval(Long taskId, String role, Long id);
    public TaskApproval approveTask(Long approvalId, Double increment);
    public TaskApproval rejectTask(Long approvalId);
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfProject(Long projectId);
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfTeam(Long teamId);
    public List<MonthlyTaskApprovalDTO> getApprovedTasksByMonth(Long projectId);
}
