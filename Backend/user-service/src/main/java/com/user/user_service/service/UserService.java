package com.user.user_service.service;

import com.user.user_service.entity.Task;
import com.user.user_service.entity.Team;
import com.user.user_service.entity.User;

import java.util.List;

public interface UserService {
    public User createNewUser(User user);
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public User updateUserById(Long userId, User user);
    public void deleteUser(Long userId);
    public List<Task> getAllTasksOfUser(Long userId);
    public List<Long> getAllProjectOfUser(Long userId);
    public List<Team> getAllTeamsOfUser(Long userId);

}
