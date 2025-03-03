package com.objective.objective_service.dto;

import java.time.LocalDate;

public class KeyResultSummaryDto {
    private String name;
    private String priority;
    private Double currKeyResultVal;
    private LocalDate dueDate;
    private String teamLeaderName;
    private String teamLeaderProfilePic;

    // Constructor
    public KeyResultSummaryDto(String name, String priority, Double currKeyResultVal,
                               LocalDate dueDate, String teamLeaderName, String teamLeaderProfilePic) {
        this.name = name;
        this.priority = priority;
        this.currKeyResultVal = currKeyResultVal;
        this.dueDate = dueDate;
        this.teamLeaderName = teamLeaderName;
        this.teamLeaderProfilePic = teamLeaderProfilePic;
    }

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
