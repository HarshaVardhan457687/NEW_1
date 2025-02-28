package com.user.user_service.DTO;

public class UserSummaryDTO {
    private Long userId;
    private String userName;
    private String userProfilePhoto;

    public UserSummaryDTO( Long userId, String userName, String userProfilePhoto) {
        this.userId = userId;
        this.userName = userName;
        this.userProfilePhoto = userProfilePhoto;
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
}
