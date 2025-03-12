package com.project.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDTO {
    private Long teamId;
    private String teamName;
    private Long teamLead;
}
