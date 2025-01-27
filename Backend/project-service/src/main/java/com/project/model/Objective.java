package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.util.Date;
import java.util.List;

public class Objective {
    private Long objectiveId;
    private String objectiveName;
    private String objectiveDescription;
    private Long mappedProject;
    @ElementCollection
    private List<Long> assignedTo;
    @ElementCollection
    private List<Long> keyResultIds;
    @Transient
    private List<KeyResult> keyResult;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveDueDate;
    private String objectiveStatus;
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

    public String getObjectiveDescription() {
        return objectiveDescription;
    }

    public void setObjectiveDescription(String objectiveDescription) {
        this.objectiveDescription = objectiveDescription;
    }

    public Long getMappedProject() {
        return mappedProject;
    }

    public void setMappedProject(Long mappedProject) {
        this.mappedProject = mappedProject;
    }

    public List<Long> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<Long> assignedTo) {
        this.assignedTo = assignedTo;
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

    public Date getObjectiveStartDate() {
        return objectiveStartDate;
    }

    public void setObjectiveStartDate(Date objectiveStartDate) {
        this.objectiveStartDate = objectiveStartDate;
    }

    public Date getObjectiveDueDate() {
        return objectiveDueDate;
    }

    public void setObjectiveDueDate(Date objectiveDueDate) {
        this.objectiveDueDate = objectiveDueDate;
    }

    public String getObjectiveStatus() {
        return objectiveStatus;
    }

    public void setObjectiveStatus(String objectiveStatus) {
        this.objectiveStatus = objectiveStatus;
    }

    public boolean isObjectiveIsActive() {
        return objectiveIsActive;
    }

    public void setObjectiveIsActive(boolean objectiveIsActive) {
        this.objectiveIsActive = objectiveIsActive;
    }
}
