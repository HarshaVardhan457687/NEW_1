package com.user.user_service.entity;

import com.user.user_service.constants.ProjectPriority;
import com.user.user_service.constants.ProjectStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

public class Project {

    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String projectDescription;

    @Enumerated(EnumType.STRING)
    private ProjectPriority projectPriority = ProjectPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus = ProjectStatus.ON_TRACK;

    @Column
    private Boolean isActive = true;

    private Long projectManagerId;

    @ElementCollection
    private List<Long> teamsInvolvedId;

    @ElementCollection
    private List<Long> objectiveId;

    @Transient
    private List<Objective> objectives;

    @ElementCollection
    private List<Long> keyResultIds;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date projectCreatedAt;

    @Temporal(TemporalType.DATE)
    private Date projectDueDate;

    @Transient
    private Double projectProgress;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ProjectPriority getProjectPriority() {
        return projectPriority;
    }

    public void setProjectPriority(ProjectPriority projectPriority) {
        this.projectPriority = projectPriority;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public List<Long> getTeamsInvolvedId() {
        return teamsInvolvedId;
    }

    public void setTeamsInvolvedId(List<Long> teamsInvolvedId) {
        this.teamsInvolvedId = teamsInvolvedId;
    }

    public List<Long> getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(List<Long> objectiveId) {
        this.objectiveId = objectiveId;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    public List<Long> getKeyResultIds() {
        return keyResultIds;
    }

    public void setKeyResultIds(List<Long> keyResultIds) {
        this.keyResultIds = keyResultIds;
    }

    public Date getProjectCreatedAt() {
        return projectCreatedAt;
    }

    public void setProjectCreatedAt(Date projectCreatedAt) {
        this.projectCreatedAt = projectCreatedAt;
    }

    public Date getProjectDueDate() {
        return projectDueDate;
    }

    public void setProjectDueDate(Date projectDueDate) {
        this.projectDueDate = projectDueDate;
    }

    public Double getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(Double projectProgress) {
        this.projectProgress = projectProgress;
    }
}
