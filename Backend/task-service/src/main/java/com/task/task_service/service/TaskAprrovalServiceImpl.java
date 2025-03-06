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
    public TaskApproval requestApproval(Long taskId, Long submitterId, String role, Long id) {
        TaskApproval approval = new TaskApproval();
        approval.setTaskId(taskId);
        approval.setSubmitterId(submitterId);

        if ("Project Manager".equals(role)) {
            approval.setProjectId(id);

        } else if ("Team Leader".equals(role)) {
            approval.setTeamId(id);
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    // call the task servce and change the task status to pending 
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

}
