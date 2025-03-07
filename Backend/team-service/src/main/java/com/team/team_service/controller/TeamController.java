package com.team.team_service.controller;

import com.team.team_service.DTO.TeamMemberProgressDto;
import com.team.team_service.DTO.TeamResponseDto;
import com.team.team_service.entity.Team;
import com.team.team_service.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/progress")
    public double getTeamProgress(@RequestParam Long projectId, @RequestParam Long teamId) {
        return teamService.teamProgress(projectId, teamId);
    }

    @GetMapping("/details")
    public ResponseEntity<TeamResponseDto> getTeamDetails(@RequestParam Long teamId) {
        return ResponseEntity.ok(teamService.getTeamDetails(teamId));
    }

    @GetMapping("/project/members-progress")
    public ResponseEntity<List<TeamMemberProgressDto>> getTeamMembersProgress(
            @RequestParam Long teamId,
            @RequestParam Long projectId) {
        return ResponseEntity.ok(teamService.getTeamMembersProgress(teamId, projectId));
    }

    @GetMapping("/get-team-lead/{teamId}")
    public ResponseEntity<Long> getTeamLead(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.getTeamLeadId(teamId));
    }

    @GetMapping("/get-team-name/{teamId}")
    public ResponseEntity<String> getTeamName(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.getTeamName(teamId));
    }

    @GetMapping("/is-mapped-to-project")
    public ResponseEntity<Boolean> isTeamMappedToProject(
            @RequestParam Long teamId,
            @RequestParam Long projectId) {

        boolean isMapped = teamService.isTeamMappedToProject(teamId, projectId);

        return ResponseEntity.ok(isMapped);
    }

    @PatchMapping("/{teamId}/add-key-result")
    public ResponseEntity<String> addKeyResultToTeam(@PathVariable Long teamId, @RequestBody List<Long> request) {
        String response = teamService.addKeyResultToTeam(teamId, request);

        if ("Team not found".equals(response)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
