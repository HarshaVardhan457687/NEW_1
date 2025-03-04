package com.objective.objective_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.objective.objective_service.constants.ObjectivePriority;
import com.objective.objective_service.constants.ObjectiveStatus;
import com.objective.objective_service.dto.KeyResultSummaryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "objective")
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objectiveId;
    private String objectiveName;
    private Long mappedProject;


    @ElementCollection
    private List<Long> keyResultIds;

    @Transient
    private List<KeyResultSummaryDto> keyResult;


    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date objectiveCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date objectiveDueDate;

    private ObjectiveStatus objectiveStatus;
    private ObjectivePriority objectivePriority;
    private boolean objectiveIsActive;

    //GETTERS AND SETTERS

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

    public List<Long> getKeyResultIds() {
        return keyResultIds;
    }

    public void setKeyResultIds(List<Long> keyResultIds) {
        this.keyResultIds = keyResultIds;
    }

    public List<KeyResultSummaryDto> getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(List<KeyResultSummaryDto> keyResult) {
        this.keyResult = keyResult;
    }

    public Date getProjectCreatedAt() {
        return objectiveCreatedAt;
    }

    public void setProjectCreatedAt(Date projectCreatedAt) {
        this.objectiveCreatedAt = projectCreatedAt;
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

    public Date getObjectiveCreatedAt() {
        return objectiveCreatedAt;
    }

    public void setObjectiveCreatedAt(Date objectiveCreatedAt) {
        this.objectiveCreatedAt = objectiveCreatedAt;
    }

    public ObjectivePriority getObjectivePriority() {
        return objectivePriority;
    }

    public void setObjectivePriority(ObjectivePriority objectivePriority) {
        this.objectivePriority = objectivePriority;
    }

    public boolean isObjectiveIsActive() {
        return objectiveIsActive;
    }

    public void setObjectiveIsActive(boolean objectiveIsActive) {
        this.objectiveIsActive = objectiveIsActive;
    }
}
