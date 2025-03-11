package com.team.team_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TeamResponseDto {
    private Long teamId;
    private String teamName;
    private Map<String, Long> totalKeyResults;
    private int totalMembers;
    private double teamProgress;
    private Map<String,Long> teamTasksCount;
    private Long teamLeaderId;
    private String teamLeaderName;
    private String teamLeaderProfile;

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

    public Map<String, Long> getTotalKeyResults() {
        return totalKeyResults;
    }

    public void setTotalKeyResults(Map<String, Long> totalKeyResults) {
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

    public Long getTeamLeaderId() {
        return teamLeaderId;
    }

    public void setTeamLeaderId(Long teamLeaderId) {
        this.teamLeaderId = teamLeaderId;
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
