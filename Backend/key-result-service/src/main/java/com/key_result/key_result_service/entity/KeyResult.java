package com.key_result.key_result_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.key_result.key_result_service.constants.KeyResultPriority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyResultId;
    private String keyResultName;
    private Long keyResultOwnerId;
    private Long associatedObjectiveId;
    private int keyResultcurrentVal = 0;
    private int keyResultTargetVal;
    private String keyResultunit;

    private KeyResultPriority keyResultPriority = KeyResultPriority.LOW;

    @ElementCollection
    private List<Long> keyResultAssociatedTasksId;

    @Transient
    private List<Task> keyResultAssociatedTasks;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date keyResultCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date keyResultDueDate;

    private Long teamId;

    //GETTERS AND SETTERS


    public Long getKeyResultId() {
        return keyResultId;
    }

    public void setKeyResultId(Long keyResultId) {
        this.keyResultId = keyResultId;
    }

    public String getKeyResultName() {
        return keyResultName;
    }

    public void setKeyResultName(String keyResultName) {
        this.keyResultName = keyResultName;
    }

    public Long getKeyResultOwnerId() {
        return keyResultOwnerId;
    }

    public void setKeyResultOwnerId(Long keyResultOwnerId) {
        this.keyResultOwnerId = keyResultOwnerId;
    }

    public Long getAssociatedObjectiveId() {
        return associatedObjectiveId;
    }

    public void setAssociatedObjectiveId(Long associatedObjectiveId) {
        this.associatedObjectiveId = associatedObjectiveId;
    }

    public int getKeyResultcurrentVal() {
        return keyResultcurrentVal;
    }

    public void setKeyResultcurrentVal(int keyResultcurrentVal) {
        this.keyResultcurrentVal = keyResultcurrentVal;
    }

    public int getKeyResultTargetVal() {
        return keyResultTargetVal;
    }

    public void setKeyResultTargetVal(int keyResultTargetVal) {
        this.keyResultTargetVal = keyResultTargetVal;
    }

    public String getKeyResultunit() {
        return keyResultunit;
    }

    public void setKeyResultunit(String keyResultunit) {
        this.keyResultunit = keyResultunit;
    }

    public Date getKeyResultCreatedAt() {
        return keyResultCreatedAt;
    }

    public void setKeyResultCreatedAt(Date keyResultCreatedAt) {
        this.keyResultCreatedAt = keyResultCreatedAt;
    }

    public KeyResultPriority getKeyResultPriority() {
        return keyResultPriority;
    }

    public void setKeyResultPriority(KeyResultPriority keyResultPriority) {
        this.keyResultPriority = keyResultPriority;
    }

    public List<Long> getKeyResultAssociatedTasksId() {
        return keyResultAssociatedTasksId;
    }

    public void setKeyResultAssociatedTasksId(List<Long> keyResultAssociatedTasksId) {
        this.keyResultAssociatedTasksId = keyResultAssociatedTasksId;
    }

    public List<Task> getKeyResultAssociatedTasks() {
        return keyResultAssociatedTasks;
    }

    public void setKeyResultAssociatedTasks(List<Task> keyResultAssociatedTasks) {
        this.keyResultAssociatedTasks = keyResultAssociatedTasks;
    }

    public Date getProjectCreatedAt() {
        return keyResultCreatedAt;
    }

    public void setProjectCreatedAt(Date projectCreatedAt) {
        this.keyResultCreatedAt = projectCreatedAt;
    }

    public Date getKeyResultDueDate() {
        return keyResultDueDate;
    }

    public void setKeyResultDueDate(Date keyResultDueDate) {
        this.keyResultDueDate = keyResultDueDate;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
