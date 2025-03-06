package com.task.task_service.service;

import com.task.task_service.constants.TaskStatus;
import com.task.task_service.entity.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {

    public Task addTask(Task task);
    public Task getTaskById(Long taskId);
    public List<Task> getAllTask();
    public Task updateTask(Long taskId, Task taskToUpdate);
    public Task updateTaskStatus(Long taskId, TaskStatus status, boolean isActive);
    public void removeTask(Long taskId);
    public List<Task> getAlltaskWithKeyResultId(Long keyresulId);
    public List<Task> getAlltaskWithUserId(Long userId);
    public List<Task> getAllActiveTasksWithUserId(Long userId);
    public String approveTask(Long taskId);
    public List<Task> getTasksByProjectIdsAndUserId(List<Long> projectIds, Long userId);
    public List<Task> getTasksByIdsAndProjects(List<Long> taskIds, List<Long> projectIds);
    public Map<String, Integer> getAllTasksInfoForProjectId(Long projectId);
    public int getActiveTasksCountForUserInProject(Long projectId, Long userId);
    public Map<String, Integer> getAllAndActiveTasksCountForUserInProject(Long projectId, Long userId);
}
