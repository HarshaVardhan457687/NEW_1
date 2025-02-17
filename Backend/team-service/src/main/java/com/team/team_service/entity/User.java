package com.team.team_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long UserId;
    private String userName;
    private String userDesignation;
    private String email;
    private String userPhoneNo;
    private String userAddress;
    private Boolean userNotificationAlert;
    private List<Long> userProject;

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Boolean getUserNotificationAlert() {
        return userNotificationAlert;
    }

    public void setUserNotificationAlert(Boolean userNotificationAlert) {
        this.userNotificationAlert = userNotificationAlert;
    }

    public List<Long> getUserProject() {
        return userProject;
    }

    public void setUserProject(List<Long> userProject) {
        this.userProject = userProject;
    }
}
