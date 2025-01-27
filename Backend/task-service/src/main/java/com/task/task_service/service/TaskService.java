package com.task.task_service.service;

import com.task.task_service.entity.Task;

import java.util.List;

public interface TaskService {

    public Task addTask(Task task);
    public Task getTaskById(Long taskId);
    public List<Task> getAllTask();
    public Task updateTask(Long taskId, Task taskToUpdate);
    public void removeTask(Long taskId);
    public List<Task> getAllTaskWithObjectiveId(Long objectiveId);
}
