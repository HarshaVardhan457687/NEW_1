package com.project.DTO;

import java.util.Map;

public class TeamResponseDTO {
    private String teamName;
    private int totalKeyResults;
    private int totalMembers;
    private double teamProgress;
    private Map<String,Long> teamTasksCount;
    private String teamLeaderName;
    private String teamLeaderProfile;

    public TeamResponseDTO(String teamName, int totalKeyResults, int totalMembers, double teamProgress, Map<String, Long> teamTasksCount, String teamLeaderName, String teamLeaderProfile) {
        this.teamName = teamName;
        this.totalKeyResults = totalKeyResults;
        this.totalMembers = totalMembers;
        this.teamProgress = teamProgress;
        this.teamTasksCount = teamTasksCount;
        this.teamLeaderName = teamLeaderName;
        this.teamLeaderProfile = teamLeaderProfile;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTotalKeyResults() {
        return totalKeyResults;
    }

    public void setTotalKeyResults(int totalKeyResults) {
        this.totalKeyResults = totalKeyResults;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public double getTeamProgress() {
        return teamProgress;
    }

    public void setTeamProgress(double teamProgress) {
        this.teamProgress = teamProgress;
    }

    public Map<String, Long> getTeamTasksCount() {
        return teamTasksCount;
    }

    public void setTeamTasksCount(Map<String, Long> teamTasksCount) {
        this.teamTasksCount = teamTasksCount;
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
