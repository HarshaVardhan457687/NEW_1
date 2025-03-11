package com.task.task_service.service;

import com.task.task_service.constants.ApprovalStatus;
import com.task.task_service.constants.TaskStatus;
import com.task.task_service.dto.TaskApprovalResponseDTO;
import com.task.task_service.entity.Task;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.repository.TaskApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskAprovalServiceImpl implements TaskApprovalService{

    @Autowired
    private TaskApprovalRepository taskApprovalRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

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
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfProject(Long projectId) {
        List<TaskApproval> allTaskApprovalList = taskApprovalRepository.findByProjectId(projectId);

        List<TaskApprovalResponseDTO> allTaskApprovalResponseList = new ArrayList<>();

        for (TaskApproval taskApproval : allTaskApprovalList) {
            Task task = taskService.getTaskById(taskApproval.getTaskId());

            TaskApprovalResponseDTO currTaskApprovalResponseDTO = new TaskApprovalResponseDTO();
            currTaskApprovalResponseDTO.setTaskApprovalId(taskApproval.getApprovalId());
            currTaskApprovalResponseDTO.setTaskName(task.getTaskHeading());
            currTaskApprovalResponseDTO.setTaskTag(task.getTaskTag());
            currTaskApprovalResponseDTO.setTaskPriority(task.getTaskPriority());
            currTaskApprovalResponseDTO.setTaskDescription(task.getTaskDescription());
            currTaskApprovalResponseDTO.setTaskDueDate(task.getTaskDueDate());
            currTaskApprovalResponseDTO.setSubmittedDate(taskApproval.getSubmittedDate());
            currTaskApprovalResponseDTO.setApprovalStatus(taskApproval.getStatus());

            String userServiceUrl = "http://localhost:8086/api/users/by-name/" + task.getTaskOwner();
            String ownerName = restTemplate.getForObject(userServiceUrl, String.class);

            currTaskApprovalResponseDTO.setOwnerName(ownerName);

            allTaskApprovalResponseList.add(currTaskApprovalResponseDTO);
        }

        return allTaskApprovalResponseList;
    }


    @Override
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfTeam(Long teamId) {
        List<TaskApproval> allTaskApprovalList = taskApprovalRepository.findByTeamId(teamId);
        List<TaskApprovalResponseDTO> allTaskApprovalResponseList = new ArrayList<>();

        for (TaskApproval taskApproval : allTaskApprovalList) {
            Task task = taskService.getTaskById(taskApproval.getTaskId());

            TaskApprovalResponseDTO currTaskApprovalResponseDTO = new TaskApprovalResponseDTO();
            currTaskApprovalResponseDTO.setTaskApprovalId(taskApproval.getApprovalId());
            currTaskApprovalResponseDTO.setTaskName(task.getTaskHeading());
            currTaskApprovalResponseDTO.setTaskTag(task.getTaskTag());
            currTaskApprovalResponseDTO.setTaskPriority(task.getTaskPriority());
            currTaskApprovalResponseDTO.setTaskDescription(task.getTaskDescription());
            currTaskApprovalResponseDTO.setTaskDueDate(task.getTaskDueDate());
            currTaskApprovalResponseDTO.setSubmittedDate(taskApproval.getSubmittedDate());
            currTaskApprovalResponseDTO.setApprovalStatus(taskApproval.getStatus());
            String userServiceUrl = "http://localhost:8086/api/users/by-name/" + task.getTaskOwner();
            String ownerName = restTemplate.getForObject(userServiceUrl, String.class);

            currTaskApprovalResponseDTO.setOwnerName(ownerName);
            allTaskApprovalResponseList.add(currTaskApprovalResponseDTO);
        }

        return allTaskApprovalResponseList;
    }


}
