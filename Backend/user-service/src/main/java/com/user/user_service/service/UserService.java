package com.user.user_service.service;

import com.user.user_service.DTO.UserSummaryDTO;
import com.user.user_service.entity.Task;
import com.user.user_service.entity.Team;
import com.user.user_service.entity.User;

import java.util.List;

public interface UserService {
    public User createNewUser(User user);
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public User updateUserById(Long userId, User user, boolean isPatch);
    public void deleteUser(Long userId);
    public User getUserByEmail(String email);
    public int getActiveTasksCountForUserInProject(Long projectId, Long userId);
    public UserSummaryDTO getUserSummary(Long userId);
    public boolean updateProjectMangerProject(Long userId, Long projectId);
}
