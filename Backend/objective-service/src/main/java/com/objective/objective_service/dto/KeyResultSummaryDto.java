package com.objective.objective_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyResultSummaryDto {
    private String name;
    private String priority;
    private Double currKeyResultVal;
    private LocalDate dueDate;
    private Float progress;
    private String teamLeaderName;
    private String teamLeaderProfilePic;

    // Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Double getCurrKeyResultVal() {
        return currKeyResultVal;
    }

    public void setCurrKeyResultVal(Double currKeyResultVal) {
        this.currKeyResultVal = currKeyResultVal;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public String getTeamLeaderName() {
        return teamLeaderName;
    }

    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }

    public String getTeamLeaderProfilePic() {
        return teamLeaderProfilePic;
    }

    public void setTeamLeaderProfilePic(String teamLeaderProfilePic) {
        this.teamLeaderProfilePic = teamLeaderProfilePic;
    }
}
