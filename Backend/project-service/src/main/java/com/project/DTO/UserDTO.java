package com.project.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String userName;
    private String userProfilePhoto;
}
