package com.task.task_service.controller;

import com.task.task_service.dto.TaskApprovalRequestDTO;
import com.task.task_service.dto.TaskApprovalResponseDTO;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.service.TaskApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class TaskApprovalController {

    @Autowired
    private TaskApprovalService taskApprovalService;

    @PostMapping("/request")
    public ResponseEntity<TaskApproval> requestApproval(@RequestBody TaskApprovalRequestDTO request) {
        TaskApproval approval = taskApprovalService.requestApproval(request.getTaskId(), request.getRole(), request.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }


    @PostMapping("/approve")
    public ResponseEntity<TaskApproval> approveTask(@RequestParam Long approvalId, @RequestParam Double increment) {
        TaskApproval approvedTask = taskApprovalService.approveTask(approvalId, increment);
        return ResponseEntity.ok(approvedTask);
    }

    @PostMapping("/reject")
    public ResponseEntity<TaskApproval> rejectTask(@RequestParam Long approvalId) {
        TaskApproval rejectedTask = taskApprovalService.rejectTask(approvalId);
        return ResponseEntity.ok(rejectedTask);
    }

    @GetMapping("/project")
    public ResponseEntity<List<TaskApprovalResponseDTO>> getAllTaskApprovalForProjectManager(@RequestParam Long projectId){
        return ResponseEntity.ok(taskApprovalService.getAllTaskApprovalOfProject(projectId));
    }

    @GetMapping("/team")
    public ResponseEntity<List<TaskApprovalResponseDTO>> getAllTaskApprovalForTeamLeader(@RequestParam Long teamId){
        return ResponseEntity.ok(taskApprovalService.getAllTaskApprovalOfTeam(teamId));

    }
}
