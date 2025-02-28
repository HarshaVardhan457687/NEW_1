package com.project.DTO;

public class TeamDetailsDTO {
    private String teamName;
    private String teamLeaderName;
    private String teamLeaderProfile;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLeaderName() {
        return teamLeaderName;
    }

    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }

    public String getTeamLeaderProfile() {
        return teamLeaderProfile;
    }

    public void setTeamLeaderProfile(String teamLeaderProfile) {
        this.teamLeaderProfile = teamLeaderProfile;
    }
}