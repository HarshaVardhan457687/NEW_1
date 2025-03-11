package com.task.task_service.service;

import com.task.task_service.constants.TaskStatus;
import com.task.task_service.dto.NotificationRequestDTO;
import com.task.task_service.entity.Notification;
import com.task.task_service.entity.Task;
import com.task.task_service.exception.ResourceNotFoundException;
import com.task.task_service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the TaskService interface to manage task operations.
 * This class interacts with the database using TaskRepository and provides
 * CRUD operations for Task entities.
 */
@Service
public class TaskServiceImpl implements TaskService {

//    private final SimpMessagingTemplate messagingTemplate; // Inject WebSocket messaging
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * Constructor for dependency injection of the TaskRepository.
     *
     * @param taskRepository Repository interface for task-related database operations.
     */
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8082/api/keyresults/";
    private static final String USER_SERVICE_URL = "http://localhost:8086/api/users/";
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8087/api/notifications/";
    /**
     * Adds a new task to the database.
     *
     * @param task The Task object containing details of the new task.
     * @return The saved Task object.
     */
    @Override
    public Task addTask(Task task) {
        LOGGER.info("Adding new task with heading: {}", task.getTaskHeading());
        Task savedTask = taskRepository.save(task);

        updateNotification(savedTask.getTaskId(), savedTask.getTaskOwner());
        updateUserService(savedTask.getTaskOwner(), savedTask.getTaskId());
        updateKeyResultService(savedTask.getTaskAssociatedKeyResult(), savedTask.getTaskId());
        return savedTask;
    }

    private void updateNotification(Long taskId, Long userId){
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setMessage("new TASK is created");
        notificationRequest.setTaskId(taskId);
        notificationRequest.setTargetUser(userId);
        notificationRequest.setCreatedTime(LocalDateTime.now());

        try {
            restTemplate.postForEntity(
                    NOTIFICATION_SERVICE_URL ,
                    notificationRequest,
                    Void.class
            );
            LOGGER.info("Notification sent for task: {}", taskId);
        } catch (Exception e) {
            LOGGER.error("Failed to send notification for task: {}", taskId, e);
        }
    }

    private void updateUserService(Long userId, Long taskId) {
        String url = USER_SERVICE_URL + "{userId}/assign-task";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Long> requestEntity = new HttpEntity<>(taskId, headers);

            restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Void.class, userId);
            LOGGER.info("Successfully updated UserService for user {} with task {}", userId, taskId);
        } catch (Exception e) {
            LOGGER.error("Failed to update UserService: {}", e.getMessage());
        }
    }

    private void updateKeyResultService(Long keyResultId, Long taskId) {
        String url = KEYRESULT_SERVICE_URL + "{keyResultId}/add-task";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Long> requestEntity = new HttpEntity<>(taskId, headers);

            restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Void.class, keyResultId);
            LOGGER.info("Successfully updated KeyResultService for keyResult {} with task {}", keyResultId, taskId);
        } catch (Exception e) {
            LOGGER.error("Failed to update KeyResultService: {}", e.getMessage());
        }
    }

    /**
     * Fetches a task by its ID.
     *
     * @param taskId The ID of the task to fetch.
     * @return The Task object if found.
     * @throws ResourceNotFoundException if no task is found with the given ID.
     */
    @Override
    public Task getTaskById(Long taskId) {
        LOGGER.info("Fetching task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
    }

    /**
     * Fetches all tasks from the database.
     *
     * @return A list of all Task objects.
     */
    @Override
    public List<Task> getAllTask() {
        LOGGER.info("Fetching all tasks...");
        return taskRepository.findAll();
    }

    /**
     * Updates an existing task in the database.
     *
     * @param taskId       The ID of the task to update.
     * @param taskToUpdate The Task object containing updated details.
     * @return The updated Task object.
     * @throws ResourceNotFoundException if no task is found with the given ID.
     */
    @Override
    public Task updateTask(Long taskId, Task taskToUpdate) {
        LOGGER.info("Updating task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    // Update fields of the existing task with new values
                    existingTask.setTaskHeading(taskToUpdate.getTaskHeading());
                    existingTask.setTaskDescription(taskToUpdate.getTaskDescription());
                    existingTask.setTaskOwner(taskToUpdate.getTaskOwner());
                    existingTask.setTaskStartDate(taskToUpdate.getTaskStartDate());
                    existingTask.setTaskDueDate(taskToUpdate.getTaskDueDate());
                    existingTask.setTaskTag(taskToUpdate.getTaskTag());
                    existingTask.setTaskIsActive(taskToUpdate.isTaskIsActive());
                    existingTask.setTaskAssociatedProject(taskToUpdate.getTaskAssociatedProject());
                    existingTask.setTaskAssociatedKeyResult(taskToUpdate.getTaskAssociatedKeyResult());
                    existingTask.setTaskStatus(taskToUpdate.getTaskStatus());
                    existingTask.setTaskPriority(taskToUpdate.getTaskPriority());
                    LOGGER.info("Task with ID: {} updated successfully.", taskId);
                    return taskRepository.save(existingTask); // Save updated task
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
    }

    @Override
    public Task updateTaskStatus(Long taskId, TaskStatus status, boolean isActive) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        task.setTaskStatus(status);
        task.setTaskIsActive(isActive);
        return taskRepository.save(task);
    }


    /**
     * Deletes a task by its ID.
     *
     * @param taskId The ID of the task to delete.
     * @throws ResourceNotFoundException if no task is found with the given ID.
     */
    @Override
    public void removeTask(Long taskId) {
        LOGGER.info("Removing task with ID: {}", taskId);
        if (!taskRepository.existsById(taskId)) {
            // Check if the task exists before attempting to delete
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }
        taskRepository.deleteById(taskId);
        LOGGER.info("Task with ID: {} removed successfully.", taskId);
    }


    @Override
    public List<Task> getAlltaskWithKeyResultId(Long keyresultId){
        LOGGER.info("Fetching all the tasks associated with keyresultId: {}",keyresultId);
        List<Task> allByTaskAssociatedKeyResult = taskRepository.findAllByTaskAssociatedKeyResult(keyresultId);
        return allByTaskAssociatedKeyResult;
    }

    @Override
    public List<Task> getAlltaskWithUserId(Long userId){
        LOGGER.info("Fetching all the tasks associated with keyresultId: {}",userId);
        List<Task> allByTaskAssociatedToUser = taskRepository.findAllByTaskOwner(userId);
        return allByTaskAssociatedToUser;
    }

    @Override
    public String approveTask(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTaskIsActive(false);
            taskRepository.save(task);

            // Send Notification via WebSocket
            Notification notification = new Notification(
                    "Task '" + task.getTaskHeading() + "' has been approved and marked as completed.",
                    "TASK_APPROVED",
                    task.getTaskOwner().toString(), // Send to the task owner
                    LocalDateTime.now()
            );

//            messagingTemplate.convertAndSend("/topic/notifications", notification);
            return "Task Approved Successfully!";
        } else {
            return "Task not found!";
        }
    }

    public List<Task> getAllActiveTasksWithUserId(Long userId) {
        LOGGER.info("Fetching all the active tasks associated with userId: {}", userId);
        return taskRepository.findAllByTaskOwner(userId)
                .stream()
                .filter(Task::isTaskIsActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByProjectIdsAndUserId(List<Long> projectIds, Long userId) {
        return taskRepository.findByTaskAssociatedProjectInAndTaskOwner(projectIds, userId);
    }

    @Override
    public List<Task> getTasksByIdsAndProjects(List<Long> taskIds, List<Long> projectIds) {
        return taskRepository.findByTaskIdInAndTaskAssociatedProjectIn(taskIds, projectIds);
    }

    @Override
    public Map<String, Integer> getAllTasksInfoForProjectId(Long projectId){
        List<Task> totalTaskCnt = taskRepository.findByTaskAssociatedProject(projectId);
        List<Task> totalCompletedTaskCnt = taskRepository.findByTaskAssociatedProjectAndTaskIsActive(projectId, false);
        Map<String, Integer> taskInfo = new HashMap<>();
        taskInfo.put("totalTask", totalTaskCnt.size());
        taskInfo.put("totalCompletedTask", totalCompletedTaskCnt.size());
        taskInfo.put("totalActiveTask", totalTaskCnt.size() - totalCompletedTaskCnt.size());

        return taskInfo;

    }

    @Override
    public int getActiveTasksCountForUserInProject(Long projectId, Long userId) {
        List<Task> activeTasks = taskRepository.findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(projectId, userId, true);
        return activeTasks.size();
    }

    @Override
    public Map<String, Integer> getAllAndActiveTasksCountForUserInProject(Long projectId, Long userId) {
        List<Task> activeTasks = taskRepository.findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(projectId, userId, true);
        List<Task> completedTasks = taskRepository.findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(projectId, userId, false);
        Map<String, Integer> map = new HashMap<>();
        map.put("totalTask", activeTasks.size()+completedTasks.size());
        map.put("completedTask", completedTasks.size());

        return map;
    }

    @Override
    public List<Task> getAllTasksForUserInProject(Long projectId, Long userId) {
        List<Task> activeTasks = taskRepository.findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(projectId, userId, true);
        List<Task> inactiveTasks = taskRepository.findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(projectId, userId, false);
        activeTasks.addAll(inactiveTasks);
        return activeTasks;
    }

    @Override
    public Long getKeyResultByTaskId(Long taskId){
        return taskRepository.findById(taskId).get().getTaskAssociatedKeyResult();
    }


}
