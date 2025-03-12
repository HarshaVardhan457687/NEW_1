package com.task.task_service.service;

import com.task.task_service.constants.ApprovalStatus;
import com.task.task_service.constants.TaskStatus;
import com.task.task_service.dto.MonthlyTaskApprovalDTO;
import com.task.task_service.dto.NotificationRequestDTO;
import com.task.task_service.dto.TaskApprovalResponseDTO;
import com.task.task_service.entity.Task;
import com.task.task_service.entity.TaskApproval;
import com.task.task_service.repository.TaskApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskAprovalServiceImpl implements TaskApprovalService{

    @Autowired
    private TaskApprovalRepository taskApprovalRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TaskApproval> getAllTaskApproval(){
        return taskApprovalRepository.findAll();
    }

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
    public TaskApproval approveTask(Long approvalId, Double increment) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setApprovedDate(new Date());

        taskService.updateTaskStatus(approval.getTaskId(), TaskStatus.COMPLETED, false);

        Long keyResultId = taskService.getKeyResultByTaskId(approval.getTaskId());
        String keyResultServiceUrl = "http://localhost:8082/api/keyresults/currentVal/" + keyResultId + "?currentVal=" + increment.intValue();
        restTemplate.patchForObject(keyResultServiceUrl, null, Void.class);

        sendNotification(
                "Task approved successfully!",
                approval.getSubmitterId(),
                approval.getTaskId()
        );

        return taskApprovalRepository.save(approval);
    }

    @Override
    public TaskApproval rejectTask(Long approvalId) {
        TaskApproval approval = taskApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setApprovedDate(new Date());

        taskService.updateTaskStatus(approval.getTaskId(), TaskStatus.PENDING, true);

        sendNotification(
                "Task has been rejected.",
                approval.getSubmitterId(),
                approval.getTaskId()
        );

        return taskApprovalRepository.save(approval);
    }

    /**
     * Helper method to send a notification via the Notification Service.
     *
     * @param message    The content of the notification.
     * @param targetUser The user ID to whom the notification is sent.
     * @param taskId     The related task ID.
     */
    private void sendNotification(String message, Long targetUser, Long taskId) {
        String notificationServiceUrl = "http://localhost:8087/api/notifications"; // Adjust URL as needed

        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setMessage(message);
        notificationRequest.setTargetUser(targetUser);
        notificationRequest.setTaskId(taskId);
        notificationRequest.setCreatedTime(LocalDateTime.now());

        try {
            restTemplate.postForEntity(notificationServiceUrl, notificationRequest, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    public List<TaskApprovalResponseDTO> getAllTaskApprovalOfProject(Long projectId) {
        List<TaskApproval> allTaskApprovalList = taskApprovalRepository.findByProjectId(projectId);

        List<TaskApprovalResponseDTO> allTaskApprovalResponseList = new ArrayList<>();

        for (TaskApproval taskApproval : allTaskApprovalList) {
            Task task = taskService.getTaskById(taskApproval.getTaskId());

            TaskApprovalResponseDTO currTaskApprovalResponseDTO = new TaskApprovalResponseDTO();
            currTaskApprovalResponseDTO.setTaskApprovalId(taskApproval.getApprovalId());
            currTaskApprovalResponseDTO.setTaskId(taskApproval.getTaskId());
            currTaskApprovalResponseDTO.setTaskName(task.getTaskHeading());
            currTaskApprovalResponseDTO.setTaskTag(task.getTaskTag());
            currTaskApprovalResponseDTO.setTaskPriority(task.getTaskPriority());
            currTaskApprovalResponseDTO.setTaskDescription(task.getTaskDescription());
            currTaskApprovalResponseDTO.setTaskDueDate(task.getTaskDueDate());
            currTaskApprovalResponseDTO.setSubmittedDate(taskApproval.getSubmittedDate());
            currTaskApprovalResponseDTO.setApprovalDate(taskApproval.getApprovedDate());
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
            currTaskApprovalResponseDTO.setTaskId(taskApproval.getTaskId());
            currTaskApprovalResponseDTO.setTaskName(task.getTaskHeading());
            currTaskApprovalResponseDTO.setTaskTag(task.getTaskTag());
            currTaskApprovalResponseDTO.setTaskPriority(task.getTaskPriority());
            currTaskApprovalResponseDTO.setTaskDescription(task.getTaskDescription());
            currTaskApprovalResponseDTO.setTaskDueDate(task.getTaskDueDate());
            currTaskApprovalResponseDTO.setSubmittedDate(taskApproval.getSubmittedDate());
            currTaskApprovalResponseDTO.setApprovalDate(taskApproval.getApprovedDate());
            currTaskApprovalResponseDTO.setApprovalStatus(taskApproval.getStatus());
            String userServiceUrl = "http://localhost:8086/api/users/by-name/" + task.getTaskOwner();
            String ownerName = restTemplate.getForObject(userServiceUrl, String.class);

            currTaskApprovalResponseDTO.setOwnerName(ownerName);
            allTaskApprovalResponseList.add(currTaskApprovalResponseDTO);
        }

        return allTaskApprovalResponseList;
    }


    /**
     * Retrieves the count of approved tasks grouped by year and month for a given project.
     *
     * @param projectId The ID of the project.
     * @return List of MonthlyTaskApprovalDTO representing year, month, and count of approved tasks.
     */
    @Override
    public List<MonthlyTaskApprovalDTO> getApprovedTasksByMonth(Long projectId) {
        List<Object[]> results = taskApprovalRepository.countApprovedTasksByMonth(projectId);

        return results.stream()
                .map(row -> new MonthlyTaskApprovalDTO(
                        (int) row[0],
                        (int) row[1],
                        (long) row[2]
                ))
                .toList();
    }

}
