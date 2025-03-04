package com.user.user_service.DTO;

public class UserSummaryDTO {
    private Long userId;
    private String userName;
    private String userProfilePhoto;
    private String role;

    public UserSummaryDTO( Long userId, String userName, String userProfilePhoto, String role) {
        this.userId = userId;
        this.userName = userName;
        this.userProfilePhoto = userProfilePhoto;
        this.role = role;
    }

    public Long getUserId(){
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public String getRole() {
        return role;
    }
}
