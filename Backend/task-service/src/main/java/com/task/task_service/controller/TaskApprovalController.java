package com.task.task_service.controller;

import com.task.task_service.entity.TaskApproval;
import com.task.task_service.service.TaskApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approvals")
public class TaskApprovalController {

    @Autowired
    private TaskApprovalService taskApprovalService;

    @PostMapping("/request")
    public ResponseEntity<TaskApproval> requestApproval(
            @RequestParam Long taskId,
            @RequestParam Long submitterId,
            @RequestParam String role,
            @RequestParam Long id) {

        TaskApproval approval = taskApprovalService.requestApproval(taskId, submitterId, role, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }

    @PostMapping("/{approvalId}/approve")
    public ResponseEntity<TaskApproval> approveTask(@PathVariable Long approvalId) {
        TaskApproval approvedTask = taskApprovalService.approveTask(approvalId);
        return ResponseEntity.ok(approvedTask);
    }

    @PostMapping("/{approvalId}/reject")
    public ResponseEntity<TaskApproval> rejectTask(@PathVariable Long approvalId) {
        TaskApproval rejectedTask = taskApprovalService.rejectTask(approvalId);
        return ResponseEntity.ok(rejectedTask);
    }
}
