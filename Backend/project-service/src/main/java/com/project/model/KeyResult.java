package com.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;

public class KeyResult {
    private Long keyResultId;
    private String keyResultName;
    private Long ownerId;
    private List<Long> associatedTasks;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getAssociatedTasks() {
        return associatedTasks;
    }

    public void setAssociatedTasks(List<Long> associatedTasks) {
        this.associatedTasks = associatedTasks;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
