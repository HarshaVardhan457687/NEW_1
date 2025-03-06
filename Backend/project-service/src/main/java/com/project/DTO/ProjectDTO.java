package com.project.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.constants.ProjectPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private String projectName;
    private String projectManagerEmail;
    private String projectDescription;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date projectDueDate;
    private ProjectPriority projectPriority;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Date getProjectDueDate() {
        return projectDueDate;
    }

    public void setProjectDueDate(Date projectDueDate) {
        this.projectDueDate = projectDueDate;
    }

    public ProjectPriority getProjectPriority() {
        return projectPriority;
    }

    public void setProjectPriority(ProjectPriority projectPriority) {
        this.projectPriority = projectPriority;
    }
}
