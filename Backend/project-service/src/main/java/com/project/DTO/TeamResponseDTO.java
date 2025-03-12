package com.project.DTO;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamResponseDTO {
    private Long teamId;
    private String teamName;
    private Map<String, Long> totalKeyResults;
    private int totalMembers;
    private double teamProgress;
    private Map<String,Long> teamTasksCount;
    private Long teamLeaderId;
    private String teamLeaderName;
    private String teamLeaderProfile;
}
