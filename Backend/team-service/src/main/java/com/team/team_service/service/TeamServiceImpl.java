package com.team.team_service.service;

import com.team.team_service.entity.Team;
import com.team.team_service.exception.ResourceNotFoundException;
import com.team.team_service.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.event.WindowFocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService {

    // Injecting the TeamRepository dependency to interact with the database
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RestTemplate restTemplate;
    // Logger to log the activities performed in the service
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);

    private static final String USER_SERVICE_URL = "http://localhost:8086/api/users";

    /**
     * Creates a new team in the database.
     * This method accepts a Team object, saves it, and returns the saved Team object.
     *
     * @param newTeam The Team object to be created.
     * @return The saved Team object.
     */
    @Override
    public Team createTeam(Team newTeam) {
        // Logging the creation of a new team
        LOGGER.info("Creating a new Team with name: {}", newTeam.getTeamName());

        // Saving the new team to the database
        Team savedTeam = teamRepository.save(newTeam);

        // Call User Service to update user fields
        updateUserTeams(savedTeam.getTeamMembers(), savedTeam.getTeamId(), savedTeam.getAssignedProject(), savedTeam.getTeamLead());

        return savedTeam;
    }

    private void updateUserTeams(List<Long> teamMemberIds, Long teamId, Long assignedProject, Long teamLead) {
        if (teamMemberIds == null || teamMemberIds.isEmpty()) {
            return;
        }

        Map<String, Object> request = new HashMap<>();
        request.put("teamMemberIds", teamMemberIds);
        request.put("teamId", teamId);
        request.put("assignedProject", assignedProject);
        request.put("teamLead", teamLead);

        try {
            String url = USER_SERVICE_URL + "/update-teams";
            restTemplate.patchForObject(url, new HttpEntity<>(request), String.class);
            LOGGER.info("Updated user details for team: {}, project: {}, team members: {}", teamId, assignedProject, teamMemberIds);
        } catch (Exception e) {
            LOGGER.error("Failed to update user details in User Service: {}", e.getMessage());
        }
    }



    /**
     * Fetches a team by its ID from the database.
     * If the team does not exist, throws a ResourceNotFoundException.
     *
     * @param teamId The ID of the team to be fetched.
     * @return The Team object corresponding to the teamId.
     * @throws ResourceNotFoundException if the team is not found.
     */
    @Override
    public Team getTeam(Long teamId) {
        // Attempt to retrieve the team by ID, or throw an exception if not found
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with teamID " + teamId));
    }

    /**
     * Fetches all teams from the database.
     * This method logs the operation of fetching all teams.
     *
     * @return A list of all teams present in the database.
     */
    @Override
    public List<Team> getAllTeams() {
        // Logging the operation of fetching all teams
        LOGGER.info("Fetching all the Teams from the database...");

        // Returning all teams from the database
        return teamRepository.findAll();
    }

    /**
     * Updates an existing team in the database.
     * If the team exists, updates its details and saves the updated team.
     * If the team does not exist, throws a ResourceNotFoundException.
     *
     * @param toUpdate The Team object containing the updated information.
     * @param teamId   The ID of the team to be updated.
     * @return The updated Team object.
     * @throws ResourceNotFoundException if the team does not exist.
     */
    @Override
    public Team updateTeam(Team toUpdate, Long teamId) {
        // Logging the attempt to update a team
        LOGGER.info("Updating the Team with teamId: {}", teamId);

        // Trying to find the team by ID, and if found, updating its fields
        return teamRepository.findById(teamId)
                .map(existingTeam -> {
                    // Updating the existing team's fields with the new values
                    existingTeam.setTeamName(toUpdate.getTeamName());
                    existingTeam.setTeamLead(toUpdate.getTeamLead());
                    existingTeam.setTeamMembers(toUpdate.getTeamMembers());
                    existingTeam.setAssignedProject(toUpdate.getAssignedProject());
                    existingTeam.setAssignedKeyResult(toUpdate.getAssignedKeyResult());

                    // Logging the successful update
                    LOGGER.info("Team with teamID: {} updated successfully", teamId);

                    // Saving the updated team and returning it
                    return teamRepository.save(existingTeam);
                })
                // If the team is not found, throw an exception
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));
    }

    /**
     * Removes a team from the database.
     * If the team exists, deletes it. If the team does not exist, throws a ResourceNotFoundException.
     *
     * @param teamId The ID of the team to be removed.
     * @throws ResourceNotFoundException if the team does not exist.
     */
    @Override
    public void removeTeam(Long teamId) {
        // Logging the attempt to remove a team
        LOGGER.info("Removing the Team with teamId: {}", teamId);

        // Checking if the team exists in the database
        if (!teamRepository.existsById(teamId)) {
            // If the team does not exist, throwing an exception
            throw new ResourceNotFoundException("Team with teamId: " + teamId + " not found");
        }

        // Deleting the team by its ID if it exists
        teamRepository.deleteById(teamId);

        // Logging the successful deletion of the team
        LOGGER.info("Team with teamID: {} deleted successfully", teamId);
    }


}

