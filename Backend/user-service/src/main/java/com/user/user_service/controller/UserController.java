package com.user.user_service.controller;

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

    /**
     * API to update the user
     * */
    @PutMapping("/{userId}")
    public User updateUserById(@PathVariable String userEmail, @RequestBody User updatedUser){
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return userServiceImpl.updateUserById(userId, updatedUser);
    }

    @PostMapping("/{userId}/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String imageUrl = userServiceImpl.uploadUserProfilePhoto(userId, file);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/get/profile-pic")
    public ResponseEntity<String> getUserProfilePic(@RequestParam String userEmail) {
        Long userId = userServiceImpl.getUserByEmail(userEmail).getUserId();
        return ResponseEntity.ok(userServiceImpl.getProfilePicture(userId));
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
    @GetMapping("tasks")
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


}


