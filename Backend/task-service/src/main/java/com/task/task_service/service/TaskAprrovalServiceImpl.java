package com.task.task_service.service;

import com.task.task_service.constants.ApprovalStatus;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.repository.TaskApprovalRepository;
import com.task.task_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskAprrovalServiceImpl implements TaskApprovalService{

    @Autowired
    private TaskApprovalRepository taskApprovalRepository;

    @Override
    public TaskApproval requestApproval(Long taskId, Long submitterId, Long projectId, Long teamId) {
        TaskApproval approval = new TaskApproval();
        approval.setTaskId(taskId);
        approval.setSubmitterId(submitterId);
        approval.setProjectId(projectId);
        approval.setTeamId(teamId);

        // Assign approver based on whether task is team-related or not
        if (teamId != null) {
            approval.setApproverId(getTeamLeaderId(teamId)); // Fetch from DB or API
        } else if (projectId != null) {
            approval.setApproverId(getProjectManagerId(projectId)); // Fetch from DB or API
        }

        approval.setStatus(ApprovalStatus.PENDING);
        return taskApprovalRepository.save(approval);
    }

    public TaskApproval approveTask(Long approvalId) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setApprovedDate(new Date());
        return taskApprovalRepository.save(approval);
    }

    public TaskApproval rejectTask(Long approvalId, String reason) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setApprovedDate(new Date());
        return taskApprovalRepository.save(approval);
    }

    private Long getTeamLeaderId(Long teamId) {
        // Fetch team leader ID from the Team Service
        return 101L; // Placeholder
    }

    private Long getProjectManagerId(Long projectId) {
        // Fetch project manager ID from the Project Service
        return 201L; // Placeholder
    }


}
