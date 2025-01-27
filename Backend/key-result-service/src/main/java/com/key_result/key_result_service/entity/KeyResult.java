package com.key_result.key_result_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

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

    @ElementCollection
    private List<Long> keyResultAssociatedTasksId;

    @Transient
    private List<Task> keyResultAssociatedTasks;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date keyResultDueDate;

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

    public Date getKeyResultDueDate() {
        return keyResultDueDate;
    }

    public void setKeyResultDueDate(Date keyResultDueDate) {
        this.keyResultDueDate = keyResultDueDate;
    }
}
