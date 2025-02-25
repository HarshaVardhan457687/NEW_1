package com.task.task_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.task.task_service.constants.TaskPriority;
import com.task.task_service.constants.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String taskHeading;
    private String taskDescription;
    private Long taskOwner;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date taskStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taskDueDate;
    private String taskTag;
    private boolean taskIsActive;
    private Long taskAssociatedProject;
    private Long taskAssociatedKeyResult;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;


    //GETTERS AND SETTERS
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

    public Long getTaskAssociatedProject() {
        return taskAssociatedProject;
    }

    public void setTaskAssociatedProject(Long taskAssociatedProject) {
        this.taskAssociatedProject = taskAssociatedProject;
    }

    public Long getTaskAssociatedKeyResult() {
        return taskAssociatedKeyResult;
    }

    public void setTaskAssociatedKeyResult(Long taskAssociatedKeyResult) {
        this.taskAssociatedKeyResult = taskAssociatedKeyResult;
    }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }
    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
