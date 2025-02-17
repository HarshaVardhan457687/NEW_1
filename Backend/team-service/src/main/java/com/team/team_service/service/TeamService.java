package com.team.team_service.service;


import com.team.team_service.entity.Team;

import java.util.List;

public interface TeamService {
    public Team createTeam(Team newTeam);
    public Team getTeam(Long teamId);
    public List<Team> getAllTeams();
    public Team updateTeam(Team toUpdate, Long teamId);
    public void removeTeam(Long teamId);

}
