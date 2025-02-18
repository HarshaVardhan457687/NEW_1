package com.user.user_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.user.user_service.constants.KeyResultPriority;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

public class KeyResult {
    private Long keyResultId;
    private String keyResultName;
    private Long keyResultOwnerId;
    private Long associatedObjectiveId;
    private int currentVal = 0;
    private int TargetVal;

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
}
