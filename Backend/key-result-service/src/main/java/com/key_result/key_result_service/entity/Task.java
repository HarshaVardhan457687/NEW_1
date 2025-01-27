package com.key_result.key_result_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long taskId;
    private String taskHeading;
    private String taskDescription;
    private Long taskOwner;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taskStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taskDueDate;
    private String taskTag;
    private boolean taskIsActive;
    private Long taskAssociatedKeyResult;
    private Long taskAssociatedObjective;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskHeading() {
        return taskHeading;
    }

    public void setTaskHeading(String taskHeading) {
        this.taskHeading = taskHeading;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(Long taskOwner) {
        this.taskOwner = taskOwner;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Date taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public boolean isTaskIsActive() {
        return taskIsActive;
    }

    public void setTaskIsActive(boolean taskIsActive) {
        this.taskIsActive = taskIsActive;
    }

    public Long getTaskAssociatedKeyResult() {
        return taskAssociatedKeyResult;
    }

    public void setTaskAssociatedKeyResult(Long taskAssociatedKeyResult) {
        this.taskAssociatedKeyResult = taskAssociatedKeyResult;
    }

    public Long getTaskAssociatedObjective() {
        return taskAssociatedObjective;
    }

    public void setTaskAssociatedObjective(Long taskAssociatedObjective) {
        this.taskAssociatedObjective = taskAssociatedObjective;
    }
}
