package com.team.team_service.service;


import com.team.team_service.DTO.TeamMemberProgressDto;
import com.team.team_service.DTO.TeamResponseDto;
import com.team.team_service.entity.Team;

import java.util.List;

public interface TeamService {
    public Team createTeam(Team newTeam);
    public Team getTeam(Long teamId);
    public List<Team> getAllTeams();
    public Team updateTeam(Team toUpdate, Long teamId);
    public void removeTeam(Long teamId);
    public double teamProgress(Long projectId, Long teamId);
    public TeamResponseDto getTeamDetails(Long teamId);
    public List<TeamMemberProgressDto> getTeamMembersProgress(Long teamId, Long projectId);
    public Long getTeamLeadId(Long teamId);
    public boolean isTeamMappedToProject(Long teamId, Long projectId);
    public String addKeyResultToTeam(Long teamId, List<Long> request);
}
