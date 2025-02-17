package com.team.team_service.controller;

import com.team.team_service.entity.Team;
import com.team.team_service.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams") // Base URL for all endpoints in this controller
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Create a new team.
     *
     * @param newTeam The team details to create.
     * @return The created team.
     */
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team newTeam) {
        Team createdTeam = teamService.createTeam(newTeam);
        return ResponseEntity.ok(createdTeam);
    }

    /**
     * Get a team by its ID.
     *
     * @param teamId The ID of the team.
     * @return The team details.
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeam(teamId);
        return ResponseEntity.ok(team);
    }

    /**
     * Get all teams.
     *
     * @return List of all teams.
     */
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    /**
     * Update a team by its ID.
     *
     * @param teamId   The ID of the team to update.
     * @param toUpdate The updated team details.
     * @return The updated team.
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long teamId, @RequestBody Team toUpdate) {
        Team updatedTeam = teamService.updateTeam(toUpdate, teamId);
        return ResponseEntity.ok(updatedTeam);
    }

    /**
     * Remove a team by its ID.
     *
     * @param teamId The ID of the team to remove.
     * @return A response indicating successful deletion.
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> removeTeam(@PathVariable Long teamId) {
        teamService.removeTeam(teamId);
        return ResponseEntity.noContent().build(); // 204 No Content status
    }
}
