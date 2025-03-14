package com.user.user_service.controller;

import com.user.user_service.DTO.TaskDetailsDTO;
import com.user.user_service.DTO.TaskStatusCountDTO;
import com.user.user_service.DTO.UserSummaryDTO;
import com.user.user_service.entity.*;
import com.user.user_service.exception.UserNotFoundException;
import com.user.user_service.repository.UserRepository;
import com.user.user_service.service.UserServiceImpl;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserServiceImpl userServiceImpl;


    /**
     * Creates a new user in the system.
     *
     * @param user The user entity with required details for creation
     * @return The created user with generated ID and other system-populated fields
     */
    @PostMapping
    public User createNewUser(@RequestBody User user){
        return userServiceImpl.createNewUser(user);
    }

    /**
     * Retrieves all users registered in the system.
     *
     * @return List of all users with their complete details
     */
    @GetMapping
    public List<User> getAllUser(){
        return userServiceImpl.getAllUsers();
    }

    /**
     * Retrieves a specific user by their unique ID.
     *
     * @param userId The unique identifier of the user
     * @return The user entity if found
     * @throws UserNotFoundException if user with specified ID is not found
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userServiceImpl.getUserById(userId);
    }

    /**
     * Retrieves a user by their email address.
     * This endpoint first fetches the user by email, then uses the user's ID
     * to retrieve the complete user information.
     *
     * @param userEmail The email address of the user to retrieve
     * @return The user entity if found
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/by-email/{userEmail}")
    public User getUserById(@PathVariable String userEmail) {
        User user = userServiceImpl.getUserByEmail(userEmail);
        return userServiceImpl.getUserById(user.getUserId());
    }

    @GetMapping("/by-name/{userId}")
    public String getUserNameById(@PathVariable Long userId){
        return userServiceImpl.getUserNameById(userId);
    }

    /**
     * Retrieves a summary of user information including profile details
     * and key metrics.
     *
     * @param userId The unique identifier of the user
     * @return DTO containing summarized user information
     * @throws UserNotFoundException if user with specified ID is not found
     */
    @GetMapping("/user-summary/{userId}")
    public UserSummaryDTO getUserSummary(@PathVariable Long userId){
        UserSummaryDTO userSummary = userServiceImpl.getUserSummary(userId);
        return userSummary;
    }

    /**
     * Partially updates a user identified by their email address.
     * This endpoint allows for updating only specific fields of the user entity
     * without affecting other fields.
     *
     * @param userEmail The email address of the user to update
     * @param user The user entity with fields to update
     * @return ResponseEntity containing the updated user entity
     * @throws UserNotFoundException if user with specified email is not found
     */
    @PatchMapping("/{userEmail}")
    public ResponseEntity<User> patchUser(@PathVariable String userEmail, @RequestBody User user) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.updateUserById(userId, user, true));
    }

    /**
     * Retrieves the user ID for a given email address.
     * This is a utility endpoint used for quick user ID lookup by email.
     *
     * @param userEmail The email address to lookup
     * @return ResponseEntity containing the user ID if found
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("getUserId/{userEmail}")
    public ResponseEntity<Long> patchUser(@PathVariable String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userId);
    }

    /**
     * Completely replaces a user entity with the provided data.
     * Unlike PATCH, this PUT operation replaces the entire user entity.
     *
     * @param userEmail The email address of the user to replace
     * @param user The new user entity data
     * @return ResponseEntity containing the updated user entity
     * @throws UserNotFoundException if user with specified email is not found
     */
    @PutMapping("/{userEmail}")
    public ResponseEntity<User> putUser(@PathVariable String userEmail, @RequestBody User user) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.updateUserById(userId, user, false));
    }

    /**
     * Uploads a profile picture for a user identified by email.
     *
     * @param userEmail The email address of the user
     * @param file The image file to upload as profile picture
     * @return ResponseEntity containing the URL to the uploaded image
     * @throws RuntimeException if user with specified email is not found or if upload fails
     */
    @PostMapping("/{userEmail}/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String userEmail, @RequestParam("file") MultipartFile file) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        String imageUrl = userServiceImpl.uploadUserProfilePhoto(userId, file);
        return ResponseEntity.ok(imageUrl);
    }

    /**
     * Retrieves the profile picture URL for a user.
     *
     * @param userEmail The email address of the user
     * @return ResponseEntity containing the URL of the user's profile picture
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/get/profile-pic")
    public ResponseEntity<String> getUserProfilePic(@RequestParam String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getProfilePicture(userId));
    }

    //NOT USIGN UPDATED 
    /**
     * Retrieves a summary of all users including their profile information.
     * This endpoint is useful for displaying user listings with key information.
     *
     * @return ResponseEntity containing a list of user summary DTOs
     */
    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersWithProfile() {
        List<UserSummaryDTO> users = userServiceImpl.findAllUsersWithProfile();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/summary/exclude-manager")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersExceptProjectManager(@RequestParam Long projectId) {
        List<UserSummaryDTO> users = userServiceImpl.findAllUsersExceptProjectManager(projectId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/available-users")
    public ResponseEntity<List<UserSummaryDTO>> getUsersNotInProjectTeams(@RequestParam Long projectId) {
        List<UserSummaryDTO> users = userServiceImpl.findUsersNotInProjectTeams(projectId);
        return ResponseEntity.ok(users);
    }


    /**
     * Deletes a user from the system.
     *
     * @param userEmail The email address of the user to delete
     * @throws UserNotFoundException if user with specified email is not found
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userEmail){
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        userServiceImpl.deleteUser(userId);
    }

    /**
     * Retrieves counts of active and total projects for a user based on their role.
     * This endpoint provides metrics for dashboard display.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return Map containing counts of active and total projects
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/projects/active/count")
    public Map<String, Long> getActiveAndTotalProjectsCount(@RequestParam String userEmail, @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return userServiceImpl.getActiveAndTotalProjectsCount(userId, userRole);
    }

    /**
     * Retrieves all projects associated with a user based on their role.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a list of projects
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/all/projects")
    public ResponseEntity<List<Project>> getAllProjects(@RequestParam String userEmail, @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getAllProjects(userId, userRole));
    }

    /**
     * Retrieves all objectives associated with a user based on their role.
     * This endpoint supports OKR (Objectives and Key Results) functionality.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a list of objectives
     * @throws RuntimeException if user with specified email is not found
     */
    @PostMapping("/objectives")
    public ResponseEntity<List<Objective>> getAllObjectivesByRole(
            @RequestParam String userEmail,
            @RequestParam String userRole) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        List<Objective> objectives = userServiceImpl.getAllObjectivesByRole(userId, userRole);
        return ResponseEntity.ok(objectives);
    }

    /**
     * Retrieves both active objectives and all objectives for a user.
     * The response is categorized into active and all objectives.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map with categorized objective lists
     * @throws UserNotFoundException if user with specified email is not found
     */
    @PostMapping("/objectives/active")
    public ResponseEntity<Map<String, List<Objective>>> getObjectivesByRole(
            @RequestParam String userEmail,
            @RequestParam String userRole) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, List<Objective>> objectives = userServiceImpl.getObjectivesByRole(userId, userRole);
        return ResponseEntity.ok(objectives);
    }

    /**
     * Retrieves counts of active and total objectives for a user.
     * This endpoint provides metrics for dashboard display.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map with objective counts
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("objectives/count")
    public ResponseEntity<Map<String, Long>> getObjectivesCountByRole(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, Long> countMap = userServiceImpl.getObjectivesCountByRole(userId, userRole);
        return ResponseEntity.ok(countMap);
    }

    /**
     * Retrieves key results for projects associated with a user.
     * The response is categorized by project.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map of key results by project
     * @throws UserNotFoundException if user with specified email is not found
     */
    @PostMapping("/key-results")
    public ResponseEntity<Map<String, List<KeyResult>>> getKeyResultsForProjects(@RequestParam String userEmail,
                                                                                 @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, List<KeyResult>> keyResults = userServiceImpl.getKeyResultsForProjects(userId, userRole);
        return ResponseEntity.ok(keyResults);
    }

    /**
     * Retrieves counts of active and total key results for a user.
     * This endpoint provides metrics for dashboard display.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map with key result counts
     * @throws RuntimeException if user with specified email is not found
     */
    @GetMapping("/key-results/count")
    public ResponseEntity<Map<String, Long>> getKeyResultsCountByRole(
            @RequestParam String userEmail,
            @RequestParam String userRole) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, Long> countMap = userServiceImpl.getKeyResultsCountByRole(userId, userRole);
        return ResponseEntity.ok(countMap);
    }

    /**
     * Retrieves tasks for projects associated with a user.
     * The response is categorized by project.
     *
     * @param userId The unique identifier of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map of tasks by project
     * @throws UserNotFoundException if user with specified ID is not found
     */
    @GetMapping("/tasks")
    public ResponseEntity<Map<String, List<Task>>> getTasksForUserProjects(
            @RequestParam Long userId,
            @RequestParam String userRole) {
        Map<String, List<Task>> tasks = userServiceImpl.getTasksForProjects(userId, userRole);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Retrieves task counts for projects associated with a user.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a map with task counts by project
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/tasks/tasks-count")
    public ResponseEntity<Map<String, Long>> getTaskCountForUserProjects(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, Long> taskCount = userServiceImpl.getTasksCountByRole(userId, userRole);
        return ResponseEntity.ok(taskCount);
    }

    /**
     * Retrieves active tasks for projects associated with a user.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a list of active tasks
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/tasks/active")
    public ResponseEntity<List<Task>> getActiveTaskForUserProjects(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        List<Task> allActiveTasks = userServiceImpl.getActiveTasksForUser(userId, userRole);
        return ResponseEntity.ok(allActiveTasks);
    }

    /**
     * Retrieves detailed task information for a user.
     * This endpoint provides task details including metadata for display.
     *
     * @param userEmail The email address of the user
     * @param projectId The role of the user in the system
     * @return ResponseEntity containing a list of task detail DTOs
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/user-tasks")
    public ResponseEntity<List<TaskDetailsDTO>> getTasksForUser(
            @RequestParam String userEmail,
            @RequestParam Long projectId) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getTasksForUser(userId, projectId));
    }

    /**
     * Retrieves counts of tasks by status for a user.
     * This endpoint provides metrics for dashboard display.
     *
     * @param userEmail The unique identifier of the user
     * @param projectId The role of the user in the system
     * @return DTO containing task counts by status
     * @throws UserNotFoundException if user with specified ID is not found
     */
    @GetMapping("/user-task-status-counts")
    public TaskStatusCountDTO getTaskStatusCounts(
            @RequestParam String userEmail,
            @RequestParam Long projectId) {
        return userServiceImpl.getTaskStatusCounts(userEmail, projectId);
    }

    /**
     * Retrieves active projects with progress information for a user.
     * This endpoint provides detailed project information including completion metrics.
     *
     * @param userEmail The email address of the user
     * @param userRole The role of the user in the system
     * @return ResponseEntity containing a list of active projects with progress details
     * @throws UserNotFoundException if user with specified email is not found
     */
    @GetMapping("/project/find-active")
    public ResponseEntity<List<Project>> getAllActiveProjectWithProgress(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        List<Project> allActiveProject = userServiceImpl.getActiveProjects(userId, userRole);
        return ResponseEntity.ok(allActiveProject);
    }

    /**
     * Retrieves the count of active tasks for a user in a specific project.
     *
     * @param projectId The unique identifier of the project
     * @param userEmail The email address of the user
     * @return ResponseEntity containing the count of active tasks
     * @throws UserNotFoundException if user with specified email or project with specified ID is not found
     */
    @GetMapping("/project/{projectId}/user/active-tasks")
    public ResponseEntity<Integer> getActiveTasksCountForUser(@PathVariable Long projectId, @RequestParam String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        int activeTasksCount = userServiceImpl.getActiveTasksCountForUserInProject(projectId, userId);

        return ResponseEntity.ok(activeTasksCount);
    }

    /**
     * Retrieves the count of active tasks for a user in a specific project by user ID.
     * This endpoint is an alternative to retrieving by email.
     *
     * @param projectId The unique identifier of the project
     * @param userId The unique identifier of the user
     * @return ResponseEntity containing the count of active tasks
     * @throws UserNotFoundException if user or project with specified IDs are not found
     */
    @GetMapping("/project/{projectId}/user/{userId}/active-tasks")
    public ResponseEntity<Integer> getActiveTasksCountForUserByUserId(@PathVariable Long projectId, @RequestParam Long userId) {
        int activeTasksCount = userServiceImpl.getActiveTasksCountForUserInProject(projectId, userId);

        return ResponseEntity.ok(activeTasksCount);
    }

    /**
     * Retrieves counts of all tasks and active tasks for a user in a specific project.
     *
     * @param projectId The unique identifier of the project
     * @param userId The unique identifier of the user
     * @return ResponseEntity containing a map with task counts
     * @throws UserNotFoundException if user or project with specified IDs are not found
     */
    @GetMapping("/project/user/all-and-active-tasks")
    public ResponseEntity<Map<String, Integer>> getAllAndActiveTasksCountForUserByUserId(@RequestParam Long projectId, @RequestParam Long userId) {
        Map<String, Integer> map = userServiceImpl.getAllAndActiveTasksCountForUserInProject(projectId, userId);

        return ResponseEntity.ok(map);
    }

    /**
     * Updates team assignments for users.
     * This endpoint handles batch updates for team assignments.
     *
     * @param request Map containing user and team assignment information
     * @return ResponseEntity with success message or error details
     */
    @PatchMapping("/update-teams")
    public ResponseEntity<String> updateUserTeams(@RequestBody Map<String, Object> request) {
        try {
            userServiceImpl.updateUserTeams(request);
            return ResponseEntity.ok("Users updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update users");
        }
    }

    /**
     * Retrieves the team ID that a user is mapped to for a specific project.
     *
     * @param userEmail The email address of the user
     * @param projectId The unique identifier of the project
     * @return ResponseEntity containing the team ID
     * @throws UserNotFoundException if user with specified email is not found or mapping doesn't exist
     */
    @GetMapping("/team-mapped-to-project")
    public ResponseEntity<Long> getMappedTeamForUser(
            @RequestParam String userEmail,
            @RequestParam Long projectId) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Long mappedTeamId = userServiceImpl.findMappedTeamForUser(userId, projectId);
        return ResponseEntity.ok(mappedTeamId);
    }

    /**
     * Assigns a task to a user.
     *
     * @param userId The unique identifier of the user
     * @param taskId The unique identifier of the task to assign
     * @return ResponseEntity with success message or error details
     */
    @PatchMapping("/{userId}/assign-task")
    public ResponseEntity<String> assignTaskToUser(@PathVariable Long userId, @RequestBody Long taskId) {
        boolean taskAssigned = userServiceImpl.assignTaskToUser(userId, taskId);
        if (taskAssigned) {
            return ResponseEntity.ok("Task assigned to user successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    /**
     * Updates the project manager's list of managed projects.
     *
     * @param userId The unique identifier of the project manager
     * @param projectId The unique identifier of the project to add to the manager's list
     * @return ResponseEntity with success message or error details
     */
    @PatchMapping("/update-project-manger-list/{userId}")
    public ResponseEntity<String> projectManagerList(@PathVariable Long userId, @RequestBody Long projectId){
        boolean managerListUpdated = userServiceImpl.updateProjectMangerProject(userId, projectId);
        if (managerListUpdated) {
            return ResponseEntity.ok("project updated to project manger successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

}


