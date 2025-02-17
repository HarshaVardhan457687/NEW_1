package com.user.user_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.user.user_service.constants.ObjectiveStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

public class Objective {
    private Long objectiveId;
    private String objectiveName;
    private Long mappedProject;

    @ElementCollection
    private List<Long> assignedToTeam;

    @ElementCollection
    private List<Long> keyResultIds;

    @Transient
    private List<KeyResult> keyResult;

    @ElementCollection
    private List<Long> objectiveTaskIds;

    @Transient
    private List<Task> objectiveTaskList;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date projectCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveDueDate;

    private ObjectiveStatus objectiveStatus;
    private boolean objectiveIsActive;

    public Long getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(Long objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public Long getMappedProject() {
        return mappedProject;
    }

    public void setMappedProject(Long mappedProject) {
        this.mappedProject = mappedProject;
    }

    public List<Long> getAssignedToTeam() {
        return assignedToTeam;
    }

    public void setAssignedToTeam(List<Long> assignedToTeam) {
        this.assignedToTeam = assignedToTeam;
    }

    public List<Long> getKeyResultIds() {
        return keyResultIds;
    }

    public void setKeyResultIds(List<Long> keyResultIds) {
        this.keyResultIds = keyResultIds;
    }

    public List<KeyResult> getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(List<KeyResult> keyResult) {
        this.keyResult = keyResult;
    }

    public List<Long> getObjectiveTaskIds() {
        return objectiveTaskIds;
    }

    public void setObjectiveTaskIds(List<Long> objectiveTaskIds) {
        this.objectiveTaskIds = objectiveTaskIds;
    }

    public List<Task> getObjectiveTaskList() {
        return objectiveTaskList;
    }

    public void setObjectiveTaskList(List<Task> objectiveTaskList) {
        this.objectiveTaskList = objectiveTaskList;
    }

    public Date getProjectCreatedAt() {
        return projectCreatedAt;
    }

    public void setProjectCreatedAt(Date projectCreatedAt) {
        this.projectCreatedAt = projectCreatedAt;
    }

    public Date getObjectiveDueDate() {
        return objectiveDueDate;
    }

    public void setObjectiveDueDate(Date objectiveDueDate) {
        this.objectiveDueDate = objectiveDueDate;
    }

    public ObjectiveStatus getObjectiveStatus() {
        return objectiveStatus;
    }

    public void setObjectiveStatus(ObjectiveStatus objectiveStatus) {
        this.objectiveStatus = objectiveStatus;
    }

    public boolean isObjectiveIsActive() {
        return objectiveIsActive;
    }

    public void setObjectiveIsActive(boolean objectiveIsActive) {
        this.objectiveIsActive = objectiveIsActive;
    }
}
