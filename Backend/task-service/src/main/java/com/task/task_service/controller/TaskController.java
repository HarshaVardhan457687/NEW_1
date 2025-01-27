package com.task.task_service.controller;

import com.task.task_service.entity.Task;
import com.task.task_service.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling HTTP requests related to Task operations.
 * Provides RESTful APIs for CRUD operations on tasks.
 */
@RestController
@RequestMapping("/api/tasks") // Base URL for task-related endpoints
public class TaskController {

    private final TaskService taskService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    /**
     * Constructor for injecting the TaskService dependency.
     *
     * @param taskService Service layer for managing task operations.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * API to add a new task.
     *
     * @param task Task object with the details of the new task.
     * @return ResponseEntity containing the saved Task and HTTP status.
     */
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        LOGGER.info("Received request to add a new task.");
        Task savedTask = taskService.addTask(task);
        return ResponseEntity.ok(savedTask);
    }

    /**
     * API to get a task by its ID.
     *
     * @param taskId ID of the task to fetch.
     * @return ResponseEntity containing the requested Task and HTTP status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long taskId) {
        LOGGER.info("Received request to fetch task with ID: {}", taskId);
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    /**
     * API to fetch all tasks.
     *
     * @return ResponseEntity containing a list of all tasks and HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        LOGGER.info("Received request to fetch all tasks.");
        List<Task> tasks = taskService.getAllTask();
        return ResponseEntity.ok(tasks);
    }

    /**
     * API to update an existing task by its ID.
     *
     * @param taskId       ID of the task to update.
     * @param taskToUpdate Task object containing updated details.
     * @return ResponseEntity containing the updated Task and HTTP status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long taskId,
            @RequestBody Task taskToUpdate) {
        LOGGER.info("Received request to update task with ID: {}", taskId);
        Task updatedTask = taskService.updateTask(taskId, taskToUpdate);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * API to delete a task by its ID.
     *
     * @param taskId ID of the task to delete.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskId) {
        LOGGER.info("Received request to delete task with ID: {}", taskId);
        taskService.removeTask(taskId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/objective/{objectiveId}")
    public ResponseEntity<List<Task>> getAllTaskByObjective(@PathVariable  Long objectiveId){
        List<Task> allTasksOfObjective = taskService.getAllTaskWithObjectiveId(objectiveId);
        return ResponseEntity.ok(allTasksOfObjective);

    }

}
