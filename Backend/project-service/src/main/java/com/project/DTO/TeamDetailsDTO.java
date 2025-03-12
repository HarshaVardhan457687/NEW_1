package com.project.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDetailsDTO {
    private String teamName;
    private String teamLeaderName;
    private String teamLeaderProfile;
}