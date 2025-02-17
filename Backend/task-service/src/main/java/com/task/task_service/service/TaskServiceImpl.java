package com.task.task_service.service;

import com.task.task_service.entity.Notification;
import com.task.task_service.entity.Task;
import com.task.task_service.exception.ResourceNotFoundException;
import com.task.task_service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the TaskService interface to manage task operations.
 * This class interacts with the database using TaskRepository and provides
 * CRUD operations for Task entities.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SimpMessagingTemplate messagingTemplate; // Inject WebSocket messaging
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * Constructor for dependency injection of the TaskRepository.
     *
     * @param taskRepository Repository interface for task-related database operations.
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, SimpMessagingTemplate messagingTemplate) {
        this.taskRepository = taskRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Adds a new task to the database.
     *
     * @param task The Task object containing details of the new task.
     * @return The saved Task object.
     */
    @Override
    public Task addTask(Task task) {
        LOGGER.info("Adding new task with heading: {}", task.getTaskHeading());
        return taskRepository.save(task);
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
                    existingTask.setTaskAssociatedKeyResult(taskToUpdate.getTaskAssociatedKeyResult());
                    existingTask.setTaskAssociatedObjective(taskToUpdate.getTaskAssociatedObjective());
                    existingTask.setTaskStatus(taskToUpdate.getTaskStatus());
                    LOGGER.info("Task with ID: {} updated successfully.", taskId);
                    return taskRepository.save(existingTask); // Save updated task
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
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
    public List<Task> getAllTaskWithObjectiveId(Long objectiveId) {
        LOGGER.info("Fetching all the tasks associated with ObjectiveId: {}",objectiveId);
        List<Task> allByTaskAssociatedObjective = taskRepository.findAllByTaskAssociatedObjective(objectiveId);
        return allByTaskAssociatedObjective;
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

            messagingTemplate.convertAndSend("/topic/notifications", notification);
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



}
