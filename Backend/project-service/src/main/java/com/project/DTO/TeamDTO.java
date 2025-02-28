package com.project.DTO;

public class TeamDTO {
    private Long teamId;
    private String teamName;
    private Long teamLead;

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
}
