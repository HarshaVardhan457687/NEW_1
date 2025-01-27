package com.project.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.project.constants.ProjectPriority;
import com.project.constants.ProjectStatus;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String projectDescription;

    @Enumerated(EnumType.STRING)
    private ProjectPriority projectPriority;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ElementCollection
    private List<Long> teamsInvolvedId;

    @Transient
    private List<Team> teams;

    @ElementCollection
    private List<Long> objectiveId;

    @Transient
    private List<Objective> objectives;

    @Temporal(TemporalType.DATE)
    private Date projectDueDate;

    @Column(nullable = false)
    private Long projectManager;

    // Getters and Setters
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Long> getTeamsInvolvedId() {
        return teamsInvolvedId;
    }

    public void setTeamsInvolvedId(List<Long> teamsInvolvedId) {
        this.teamsInvolvedId = teamsInvolvedId;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
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

    public Date getProjectDueDate() {
        return projectDueDate;
    }

    public void setProjectDueDate(Date projectDueDate) {
        this.projectDueDate = projectDueDate;
    }

    public Long getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Long projectManager) {
        this.projectManager = projectManager;
    }
}

// Enums remain the same as in previous implementation

