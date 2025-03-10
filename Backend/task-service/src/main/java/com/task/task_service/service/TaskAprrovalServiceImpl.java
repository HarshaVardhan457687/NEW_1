package com.task.task_service.service;

import com.task.task_service.constants.ApprovalStatus;
import com.task.task_service.constants.TaskStatus;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.repository.TaskApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskAprrovalServiceImpl implements TaskApprovalService{

    @Autowired
    private TaskApprovalRepository taskApprovalRepository;

    @Autowired
    private TaskService taskService;

    @Override
    public TaskApproval requestApproval(Long taskId, String role, Long id) {
        TaskApproval approval = new TaskApproval();
        approval.setTaskId(taskId);
        approval.setSubmitterId(taskService.getTaskById(taskId).getTaskOwner());

        if ("team_member".equals(role)) {
            approval.setTeamId(id);

        } else if ("team_leader".equals(role)) {
            approval.setProjectId(id);
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        taskService.updateTaskStatus(taskId, TaskStatus.WAITING_FOR_APPROVAL, true);
        approval.setStatus(ApprovalStatus.PENDING);
        return taskApprovalRepository.save(approval);
    }

    @Override
    public TaskApproval approveTask(Long approvalId) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setApprovedDate(new Date());

        taskService.updateTaskStatus(approval.getTaskId(), TaskStatus.COMPLETED, false);

        return taskApprovalRepository.save(approval);
    }

    @Override
    public TaskApproval rejectTask(Long approvalId) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setApprovedDate(new Date());

        taskService.updateTaskStatus(approval.getTaskId(), TaskStatus.PENDING, true);

        return taskApprovalRepository.save(approval);
    }

    @Override
    public List<TaskApproval> getAllTaskApprovalOfProject(Long projectId) {
        return taskApprovalRepository.findByProjectId(projectId);

    }

    @Override
    public List<TaskApproval> getAllTaskApprovalOfTeam(Long teamId) {
        return taskApprovalRepository.findByTeamId(teamId);
    }


}
