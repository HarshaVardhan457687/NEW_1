package com.project.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SelectTeamDTO {
    private Long teamId;
    private String teamName;
}
