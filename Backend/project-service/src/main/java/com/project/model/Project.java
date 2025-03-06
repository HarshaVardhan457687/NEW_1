package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.project.constants.ProjectPriority;
import com.project.constants.ProjectStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "complex-long-id-generator")
    @GenericGenerator(name = "complex-long-id-generator", strategy = "com.project.util.ComplexLongIdGenerator")
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

    @JsonFormat(pattern = "yyyy-MM-dd")
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

    public Double getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(Double projectProgress) {
        this.projectProgress = projectProgress;
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
}

