package com.user.user_service.entity;

import java.util.List;

public class Team {
    private Long teamId;
    private String teamName;
    private Long teamLead;
    private List<Long> teamMembers;
    private List<Long> assignedProjects;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Long teamLead) {
        this.teamLead = teamLead;
    }

    public List<Long> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Long> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<Long> getAssignedProjects() {
        return assignedProjects;
    }

    public void setAssignedProjects(List<Long> assignedProjects) {
        this.assignedProjects = assignedProjects;
    }
}
