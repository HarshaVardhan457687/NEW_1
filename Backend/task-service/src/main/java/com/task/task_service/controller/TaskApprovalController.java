package com.task.task_service.controller;

import com.task.task_service.dto.TaskApprovalRequestDTO;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.service.TaskApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approvals")
public class TaskApprovalController {

    @Autowired
    private TaskApprovalService taskApprovalService;

    @PostMapping("/request")
    public ResponseEntity<TaskApproval> requestApproval(@RequestBody TaskApprovalRequestDTO request) {
        TaskApproval approval = taskApprovalService.requestApproval(request.getTaskId(), request.getSubmitterId(), request.getRole(), request.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }


    @PostMapping("/approve")
    public ResponseEntity<TaskApproval> approveTask(@RequestParam Long approvalId) {
        TaskApproval approvedTask = taskApprovalService.approveTask(approvalId);
        return ResponseEntity.ok(approvedTask);
    }

    @PostMapping("/reject")
    public ResponseEntity<TaskApproval> rejectTask(@RequestParam Long approvalId) {
        TaskApproval rejectedTask = taskApprovalService.rejectTask(approvalId);
        return ResponseEntity.ok(rejectedTask);
    }
}
