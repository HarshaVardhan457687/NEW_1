package com.task.task_service.entity;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private String type; // Example: "TASK_APPROVED"
    private String targetUser; // Example: "user123"
    private LocalDateTime timestamp;

    public Notification(String message, String type, String targetUser, LocalDateTime timestamp) {
        this.message = message;
        this.type = type;
        this.targetUser = targetUser;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
