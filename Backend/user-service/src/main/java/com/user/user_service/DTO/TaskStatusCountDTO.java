package com.user.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusCountDTO {
    private long totalTasks;
    private long completedTasks;
    private long waitingForApprovalTasks;
    private long pendingTasks;

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getWaitingForApprovalTasks() {
        return waitingForApprovalTasks;
    }

    public void setWaitingForApprovalTasks(long waitingForApprovalTasks) {
        this.waitingForApprovalTasks = waitingForApprovalTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }
}