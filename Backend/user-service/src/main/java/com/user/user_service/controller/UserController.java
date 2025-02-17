package com.user.user_service.controller;

import com.user.user_service.entity.KeyResult;
import com.user.user_service.entity.Objective;
import com.user.user_service.entity.User;
import com.user.user_service.repository.UserRepository;
import com.user.user_service.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * API to fetch all the users
     * */
    @GetMapping("/{userId}")
    public User getUserById(@RequestParam Long userId){
        return userServiceImpl.getUserById(userId);
    }

    /**
     * API to update the user
     * */
    @PutMapping("/{userId}")
    public User updateUserById(@RequestParam Long userId, @RequestBody User updatedUser){
        return userServiceImpl.updateUserById(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestParam Long userId){
        userServiceImpl.deleteUser(userId);
    }

    /**
     * API to fetch active project count for a user.
     */
    @GetMapping("/projects/active/count")
    public Long getActiveProjectsCount(@RequestParam Long userId, @RequestParam String userRole) {
        return userServiceImpl.getActiveProjectsCount(userId, userRole);
    }

    /**
     * API to fetch all objectives by userIds.
     */
    @PostMapping("/objectives")
    public ResponseEntity<List<Objective>> getAllObjectivesByRole(
            @RequestParam Long userId,
            @RequestParam String userRole) {

        List<Objective> objectives = userServiceImpl.getAllObjectivesByRole(userId, userRole);
        return ResponseEntity.ok(objectives);
    }

    /**
     * API to fetch all active objectives and all objective by userIds.
     */
    @PostMapping("/objectives/by-role")
    public ResponseEntity<Map<String, List<Objective>>> getObjectivesByRole(
            @RequestParam Long userId,
            @RequestParam String userRole) {

        Map<String, List<Objective>> objectives = userServiceImpl.getObjectivesByRole(userId, userRole);
        return ResponseEntity.ok(objectives);
    }

    /**
     * API to fetch all keyresult  and all keyresult by userIds.
     */
    @PostMapping("/key-results")
    public ResponseEntity<Map<String, List<KeyResult>>> getKeyResultsForProjects(@RequestBody List<Long> projectIds) {
        Map<String, List<KeyResult>> keyResults = userServiceImpl.getKeyResultsForProjects(projectIds);
        return ResponseEntity.ok(keyResults);
    }


}


