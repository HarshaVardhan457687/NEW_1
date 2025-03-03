package com.user.user_service.controller;

import com.user.user_service.DTO.UserSummaryDTO;
import com.user.user_service.entity.*;
import com.user.user_service.repository.UserRepository;
import com.user.user_service.service.UserServiceImpl;
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
    * API to create the new user
    * */
    @PostMapping
    public User createNewUser(@RequestBody User user){
        return userServiceImpl.createNewUser(user);
    }

    /**
     * API to fetch all the users
     * */
    @GetMapping
    public List<User> getAllUser(){
        return userServiceImpl.getAllUsers();
    }

    /**
     * API to fetch user by userId
     * */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userServiceImpl.getUserById(userId);
    }

    /**
     * API to fetch user by userEmail
     * */
    @GetMapping("/by-email/{userEmail}")
    public User getUserById(@PathVariable String userEmail) {
        User user = userServiceImpl.getUserByEmail(userEmail);
        return userServiceImpl.getUserById(user.getUserId());
    }

    @GetMapping("/user-summary")
    public UserSummaryDTO getUserSummary(@PathVariable Long userId){
        UserSummaryDTO userSummary = userServiceImpl.getUserSummary(userId);
        return userSummary;
    }

    /**
     * API to update the user
     * */
    @PatchMapping("/{userEmail}")
    public ResponseEntity<User> patchUser(@PathVariable String userEmail, @RequestBody User user) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.updateUserById(userId, user, true));
    }

    @PutMapping("/{userEmail}")
    public ResponseEntity<User> putUser(@PathVariable String userEmail, @RequestBody User user) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.updateUserById(userId, user, false));
    }


    @PostMapping("/{userEmail}/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String userEmail, @RequestParam("file") MultipartFile file) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        String imageUrl = userServiceImpl.uploadUserProfilePhoto(userId, file);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/get/profile-pic")
    public ResponseEntity<String> getUserProfilePic(@RequestParam String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getProfilePicture(userId));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersWithProfile() {
        List<UserSummaryDTO> users = userServiceImpl.findAllUsersWithProfile();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userEmail){
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        userServiceImpl.deleteUser(userId);
    }

    /**
     * API to fetch active project count for a user.
     */
    @GetMapping("/projects/active/count")
    public Map<String, Long> getActiveAndTotalProjectsCount(@RequestParam String userEmail, @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return userServiceImpl.getActiveAndTotalProjectsCount(userId, userRole);
    }

    /**
     * API to fetch active project count for a user.
     */
    @GetMapping("/all/projects")
    public ResponseEntity<List<Project>> getAllProjects(@RequestParam String userEmail, @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getAllProjects(userId, userRole));
    }

    /**
     * API to fetch all objectives by userIds.
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
     * API to fetch all active objectives and all objective by userIds.
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
     * API to fetch count of all objectives and active objectives.
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
     * API to fetch all keyresult  and all keyresult by userIds.
     */

    @PostMapping("/key-results")
    public ResponseEntity<Map<String, List<KeyResult>>> getKeyResultsForProjects(@RequestParam String userEmail,
                                                                                 @RequestParam String userRole) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, List<KeyResult>> keyResults = userServiceImpl.getKeyResultsForProjects(userId, userRole);
        return ResponseEntity.ok(keyResults);
    }


    /**
     * API to fetch count of all keyresult  and active keyresult by userIds.
     */
    @GetMapping("/key-results/count")
    public ResponseEntity<Map<String, Long>> getKeyResultsCountByRole(
            @RequestParam String userEmail,
            @RequestParam String userRole) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, Long> countMap = userServiceImpl.getKeyResultsCountByRole(userId, userRole);
        return ResponseEntity.ok(countMap);
    }

    // Get all tasks for the user's projects based on their role
    @GetMapping("/tasks")
    public ResponseEntity<Map<String, List<Task>>> getTasksForUserProjects(
            @RequestParam Long userId,
            @RequestParam String userRole) {
        Map<String, List<Task>> tasks = userServiceImpl.getTasksForProjects(userId, userRole);
        return ResponseEntity.ok(tasks);
    }

    // Get task count for the user's projects based on their role
    @GetMapping("/tasks/tasks-count")
    public ResponseEntity<Map<String, Long>> getTaskCountForUserProjects(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Map<String, Long> taskCount = userServiceImpl.getTasksCountByRole(userId, userRole);
        return ResponseEntity.ok(taskCount);
    }

    @GetMapping("/tasks/active")
    public ResponseEntity<List<Task>> getActiveTaskForUserProjects(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        List<Task> allActiveTasks = userServiceImpl.getActiveTasksForUser(userId, userRole);
        return ResponseEntity.ok(allActiveTasks);
    }


    //api to get active project with progress for user
    @GetMapping("/project/find-active")
    public ResponseEntity<List<Project>> getAllActiveProjectWithProgress(
            @RequestParam String userEmail,
            @RequestParam String userRole) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        List<Project> allActiveProject = userServiceImpl.getActiveProjects(userId, userRole);
        return ResponseEntity.ok(allActiveProject);
    }

    @GetMapping("/project/{projectId}/user/active-tasks")
    public ResponseEntity<Integer> getActiveTasksCountForUser(@PathVariable Long projectId, @RequestParam String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        // Call UserService to get the active task count
        int activeTasksCount = userServiceImpl.getActiveTasksCountForUserInProject(projectId, userId);

        // Return the active task count in the response
        return ResponseEntity.ok(activeTasksCount);
    }

    @GetMapping("/project/{projectId}/user/{userId}/active-tasks")
    public ResponseEntity<Integer> getActiveTasksCountForUserByUserId(@PathVariable Long projectId, @RequestParam Long userId) {
        // Call UserService to get the active task count
        int activeTasksCount = userServiceImpl.getActiveTasksCountForUserInProject(projectId, userId);

        // Return the active task count in the response
        return ResponseEntity.ok(activeTasksCount);
    }

    @GetMapping("/project/user/all-and-active-tasks")
    public ResponseEntity<Map<String, Integer>> getAllAndActiveTasksCountForUserByUserId(@RequestParam Long projectId, @RequestParam Long userId) {
        // Call UserService to get the active task count
        Map<String, Integer> map = userServiceImpl.getAllAndActiveTasksCountForUserInProject(projectId, userId);

        // Return the active task count in the response
        return ResponseEntity.ok(map);
    }

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

    @GetMapping("/team-mapped-to-project")
    public ResponseEntity<Long> getMappedTeamForUser(
            @RequestParam String userEmail,
            @RequestParam Long projectId) {

        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        Long mappedTeamId = userServiceImpl.findMappedTeamForUser(userId, projectId);
        return ResponseEntity.ok(mappedTeamId);
    }




}


