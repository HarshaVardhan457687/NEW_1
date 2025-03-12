package com.project.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.constants.ProjectPriority;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDTO {

    private String projectName;
    private String projectManagerEmail;
    private String projectDescription;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date projectDueDate;
    private ProjectPriority projectPriority;

}
