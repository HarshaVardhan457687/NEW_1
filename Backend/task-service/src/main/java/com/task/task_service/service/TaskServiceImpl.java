package com.task.task_service.service;

import com.task.task_service.entity.Task;
import com.task.task_service.exception.ResourceNotFoundException;
import com.task.task_service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the TaskService interface to manage task operations.
 * This class interacts with the database using TaskRepository and provides
 * CRUD operations for Task entities.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * Constructor for dependency injection of the TaskRepository.
     *
     * @param taskRepository Repository interface for task-related database operations.
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Adds a new task to the database.
     *
     * @param task The Task object containing details of the new task.
     * @return The saved Task object with an assigned ID.
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


}
