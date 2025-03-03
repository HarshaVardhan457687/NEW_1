package com.team.team_service.service;

import com.team.team_service.DTO.TeamMemberProgressDto;
import com.team.team_service.DTO.TeamResponseDto;
import com.team.team_service.entity.Team;
import com.team.team_service.entity.User;
import com.team.team_service.exception.ResourceNotFoundException;
import com.team.team_service.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
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
    private static final String KEY_RESULT_SERVICE_URL = "http://localhost:8082/api/keyresults";

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

    @Override
    public double teamProgress(Long projectId, Long teamId) {
        long totalTasks = 0;
        long completedTasks = 0;

        // Fetch the team details
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers(); // Fetching list of users in the team

        for (Long userId : userIds) {
            String url = USER_SERVICE_URL + "/project/user/all-and-active-tasks?projectId=" + projectId + "&userId=" + userId;

            try {
                ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null, // No request body
                        new ParameterizedTypeReference<Map<String, Integer>>() {}
                );

                if (response.getBody() != null) {
                    totalTasks += response.getBody().getOrDefault("totalTask", 0);
                    completedTasks += response.getBody().getOrDefault("completedTask", 0);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to fetch task count for user {} in project {}: {}", userId, projectId, e.getMessage());
            }
        }

        // Avoid division by zero
        if (totalTasks == 0) {
            return 0.0; // Returning 0.0 for no tasks
        }

        // Calculate team progress as a double percentage
        return ((double) completedTasks / totalTasks) * 100;
    }

    public Map<String, Long> getTotalAndCompletedTasks(Long projectId, Long teamId) {
        long totalTasks = 0;
        long completedTasks = 0;

        // Fetch the team details
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers(); // Fetching list of users in the team

        for (Long userId : userIds) {
            String url = USER_SERVICE_URL + "/project/user/all-and-active-tasks?projectId=" + projectId + "&userId=" + userId;

            try {
                ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null, // No request body
                        new ParameterizedTypeReference<Map<String, Integer>>() {}
                );

                if (response.getBody() != null) {
                    totalTasks += response.getBody().getOrDefault("totalTask", 0);
                    completedTasks += response.getBody().getOrDefault("completedTask", 0);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to fetch task count for user {} in project {}: {}", userId, projectId, e.getMessage());
            }
        }

        // Return a map containing the total and completed tasks
        Map<String, Long> taskData = new HashMap<>();
        taskData.put("totalTasks", totalTasks);
        taskData.put("completedTasks", completedTasks);
        return taskData;
    }


    @Override
    public TeamResponseDto getTeamDetails(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        double progress = teamProgress(team.getAssignedProject(), teamId);
        Map<String, Long> taskCount = getTotalAndCompletedTasks(team.getAssignedProject(), teamId);

        // Fetching KeyResult IDs from the team
        List<Long> keyResultIds = team.getAssignedKeyResult();
        long totalKeyResults = keyResultIds.size();
        long completedKeyResults = 0;

        if (!keyResultIds.isEmpty()) {
            String keyResultUrl = KEY_RESULT_SERVICE_URL + "/completed-count";
            ResponseEntity<Long> response = restTemplate.exchange(
                    keyResultUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(keyResultIds),
                    Long.class
            );
            completedKeyResults = response.getBody() != null ? response.getBody() : 0;
        }

        Map<String, Long> keyResultSummary = new HashMap<>();
        keyResultSummary.put("total", totalKeyResults);
        keyResultSummary.put("completed", completedKeyResults);

        // Fetch team leader details from UserService
        String userUrl = USER_SERVICE_URL + "/" + team.getTeamLead();
        ResponseEntity<User> userResponse = restTemplate.exchange(
                userUrl, HttpMethod.GET, null, new ParameterizedTypeReference<User>() {}
        );

        User leader = userResponse.getBody();
        String leaderName = (leader != null) ? leader.getUserName() : "Unknown";
        String leaderProfile = (leader != null) ? leader.getUserProfilePhoto() : "";

        // Populate the DTO
        return new TeamResponseDto(
                team.getTeamName(),
                keyResultSummary,
                team.getTeamMembers().size(),
                progress,
                taskCount,
                leaderName,
                leaderProfile
        );
    }


    @Override
    public List<TeamMemberProgressDto> getTeamMembersProgress(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers();
        List<TeamMemberProgressDto> teamMembersProgress = new ArrayList<>();

        for (Long userId : userIds) {
            try {
                // Fetch user details (name & profile)
                String userUrl = USER_SERVICE_URL + "/" +  userId;
                ResponseEntity<User> userResponse = restTemplate.getForEntity(userUrl, User.class);

                User user = userResponse.getBody();
                String userName = (user != null) ? user.getUserName() : "Unknown";
                String userProfile = (user != null) ? user.getUserProfilePhoto() : "";

                // Fetch task count (total & completed)
                String taskUrl = USER_SERVICE_URL + "/project/user/all-and-active-tasks?projectId=" + projectId + "&userId=" + userId;
                ResponseEntity<Map<String, Integer>> taskResponse = restTemplate.exchange(
                        taskUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Integer>>() {}
                );

                Map<String, Integer> taskData = taskResponse.getBody();
                int totalTasks = (taskData != null) ? taskData.getOrDefault("totalTask", 0) : 0;
                int completedTasks = (taskData != null) ? taskData.getOrDefault("completedTask", 0) : 0;

                // Calculate progress
                double progress = (totalTasks == 0) ? 0.0 : ((double) completedTasks / totalTasks) * 100;

                // Add to list
                teamMembersProgress.add(new TeamMemberProgressDto( userName, userProfile, totalTasks, completedTasks, progress));
            } catch (Exception e) {
                LOGGER.error("Failed to fetch details for user {}: {}", userId, e.getMessage());
            }
        }

        return teamMembersProgress;
    }

    @Override
    public Long getTeamLeadId(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Return the team lead's ID
        return team.getTeamLead();
    }

    @Override
    public boolean isTeamMappedToProject(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return team.getAssignedProject().equals(projectId);
    }


}



