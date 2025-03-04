package com.objective.objective_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private Long userId;
    private String userName;
    private String userProfilePhoto;

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

