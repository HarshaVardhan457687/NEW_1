package com.task.task_service.controller;

import com.task.task_service.entity.Task;
import com.task.task_service.service.TaskService;
import com.task.task_service.service.TaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Controller class for handling HTTP requests related to Task operations.
 * Provides RESTful APIs for operations on tasks.
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


    @GetMapping("/keyresult/{keyresultId}")
    public ResponseEntity<List<Task>> getAllTaskByKeyresult(@PathVariable  Long keyresultId){
        List<Task> allTasksOfKeyResult = taskService.getAlltaskWithKeyResultId(keyresultId);
        return ResponseEntity.ok(allTasksOfKeyResult);
    }

    /**
     * API to get all task by  userID.
     *
     * @param userId ID of the user to get all task assigned.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Task>> getAllTaskByUserId(@PathVariable  Long userId){
        List<Task> allTasksOfUser = taskService.getAlltaskWithUserId(userId);
        return ResponseEntity.ok(allTasksOfUser);
    }

    /**
     * API to get all active task by  userID.
     *
     * @param userId ID of the user to get active tasks.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */
    @GetMapping("/active-task/users/{userId}")
    public ResponseEntity<List<Task>> getAllActiveTaskByUserId(@PathVariable  Long userId){
        List<Task> allActiveTasksOfUser = taskService.getAllActiveTasksWithUserId(userId);
        return ResponseEntity.ok(allActiveTasksOfUser);
    }


    /**
     * API to aprrove a task by its ID.
     *
     * @param taskId ID of the task to approve.
     * @return ResponseEntity with HTTP status indicating success or failure.
     */

    @PutMapping("/{taskId}/approve")
    public ResponseEntity<String> approveTask(@PathVariable Long taskId) {
        LOGGER.info("approving the task with taskId "+ taskId);
        taskService.approveTask(taskId);
        return ResponseEntity.ok("task is approved");
    }


    @PostMapping("/by-projects-and-user")
    public ResponseEntity<List<Task>> getTasksByProjectIdsAndUserId(
            @RequestBody List<Long> projectIds,
            @RequestParam Long userId) {
        List<Task> tasks = taskService.getTasksByProjectIdsAndUserId(projectIds, userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/tasks-by-ids-and-projects")
    public List<Task> getTasksByIdsAndProjects(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> taskIds = requestBody.get("taskIds");
        List<Long> projectIds = requestBody.get("projectIds");

        return taskService.getTasksByIdsAndProjects(taskIds, projectIds);
    }

    @GetMapping("/project/task-info/{projectId}")
    public Map<String, Integer> getAllTaskInfoForProject(@PathVariable Long projectId){
        return taskService.getAllTasksInfoForProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/user/{userId}/active-tasks")
    public ResponseEntity<Integer> getActiveTasksCount(@PathVariable Long projectId, @PathVariable Long userId) {
        int activeTaskCount = taskService.getActiveTasksCountForUserInProject(projectId, userId);
        return ResponseEntity.ok(activeTaskCount);
    }

    @GetMapping("/project/{projectId}/user/{userId}/all-tasks-count")
    public ResponseEntity<Map<String,Integer>> getAllAndActiveTasksCount(@PathVariable Long projectId, @PathVariable Long userId) {
        Map<String, Integer> map = taskService.getAllAndActiveTasksCountForUserInProject(projectId, userId);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/project/{projectId}/user/{userId}/all-tasks")
    public ResponseEntity<List<Task>> getAllTasksForProject(@PathVariable Long projectId, @PathVariable Long userId) {
        List<Task> activeTaskCount = taskService.getAllTasksForUserInProject(projectId, userId);
        return ResponseEntity.ok(activeTaskCount);
    }

    @GetMapping("/get/keyresult/{taskId}")
    public ResponseEntity<Long> getKeyResultByTaskId(@PathVariable Long taskId) {
        try {
            Long keyResultId = taskService.getKeyResultByTaskId(taskId);
            return ResponseEntity.ok(keyResultId);  // Return keyResultId if task exists
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();  // Task not found, return 404
        }
    }

}
