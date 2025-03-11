package com.team.team_service.service;

import com.team.team_service.DTO.TeamMemberProgressDto;
import com.team.team_service.DTO.TeamResponseDto;
import com.team.team_service.entity.Team;
import com.team.team_service.entity.User;
import com.team.team_service.exception.ResourceNotFoundException;
import com.team.team_service.exception.TeamNotFoundException;
import com.team.team_service.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);


    @Value("${user.service.url}")
    private String USER_SERVICE_URL;

    @Value("${keyresult.service.url}")
    private String KEY_RESULT_SERVICE_URL;

    @Value("${project.service.url}")
    private String PROJECT_SERVICE_URL;

    public String getUserServiceUrl() {
        return USER_SERVICE_URL;
    }

    public String getKeyResultServiceUrl() {
        return KEY_RESULT_SERVICE_URL;
    }

    public String getProjectServiceUrl() {
        return PROJECT_SERVICE_URL;
    }


    /**
     * Creates a new team in the database.
     * This method accepts a Team object, saves it, and returns the saved Team object.
     *
     * @param newTeam The Team object to be created.
     * @return The saved Team object.
     */
    @Override
    public Team createTeam(Team newTeam) {
        LOGGER.info("Creating a new Team with name: {}", newTeam.getTeamName());

        Team savedTeam = teamRepository.save(newTeam);

        updateUserTeams(savedTeam.getTeamMembers(), savedTeam.getTeamId(), savedTeam.getAssignedProject(), savedTeam.getTeamLead());

        updateProjectTeams(savedTeam.getAssignedProject(), savedTeam.getTeamId());

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

    private void updateProjectTeams(Long projectId, Long teamId) {
        if (projectId == null || teamId == null) {
            LOGGER.warn("Project ID or Team ID is null. Skipping Project Service update.");
            return;
        }

        Map<String, Object> request = new HashMap<>();
        request.put("projectId", projectId);
        request.put("teamId", teamId);

        try {
            String url = PROJECT_SERVICE_URL + "/update-teams";
            restTemplate.patchForObject(url, new HttpEntity<>(request), String.class);
            LOGGER.info("Updated Project Service: Added Team {} to Project {}", teamId, projectId);
        } catch (Exception e) {
            LOGGER.error("Failed to update Project Service: {}", e.getMessage());
        }
    }




    /**
     * Fetches a team by its ID from the database.
     * If the team does not exist, throws a ResourceNotFoundException.
     *
     * @param teamId The ID of the team to be fetched.
     * @return The Team object corresponding to the teamId.
     * @throws TeamNotFoundException if the team is not found.
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
     * @throws TeamNotFoundException if the team does not exist.
     */
    @Override
    public Team updateTeam(Team toUpdate, Long teamId) {
        LOGGER.info("Updating the Team with teamId: {}", teamId);

        return teamRepository.findById(teamId)
                .map(existingTeam -> {
                    existingTeam.setTeamName(toUpdate.getTeamName());
                    existingTeam.setTeamLead(toUpdate.getTeamLead());
                    existingTeam.setTeamMembers(toUpdate.getTeamMembers());
                    existingTeam.setAssignedProject(toUpdate.getAssignedProject());
                    existingTeam.setAssignedKeyResult(toUpdate.getAssignedKeyResult());

                    LOGGER.info("Team with teamID: {} updated successfully", teamId);

                    return teamRepository.save(existingTeam);
                })
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));
    }

    /**
     * Removes a team from the database.
     * If the team exists, deletes it. If the team does not exist, throws a ResourceNotFoundException.
     *
     * @param teamId The ID of the team to be removed.
     * @throws TeamNotFoundException if the team does not exist.
     */
    @Override
    public void removeTeam(Long teamId) {
        LOGGER.info("Removing the Team with teamId: {}", teamId);

        if (!teamRepository.existsById(teamId)) {
            throw new TeamNotFoundException("Team not found with ID: " + teamId);
        }

        teamRepository.deleteById(teamId);
        LOGGER.info("Team with teamID: {} deleted successfully", teamId);
    }

    @Override
    public double teamProgress(Long projectId, Long teamId) {
        long totalTasks = 0;
        long completedTasks = 0;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers();

        for (Long userId : userIds) {
            String url = USER_SERVICE_URL + "/project/user/all-and-active-tasks?projectId=" + projectId + "&userId=" + userId;

            try {
                ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
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

        if (totalTasks == 0) {
            return 0.0;
        }

        return ((double) completedTasks / totalTasks) * 100;
    }

    public Map<String, Long> getTotalAndCompletedTasks(Long projectId, Long teamId) {
        long totalTasks = 0;
        long completedTasks = 0;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers();

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

        Long teamLeaderId = team.getTeamLead();

                String userUrl = USER_SERVICE_URL + "/" + team.getTeamLead();
        ResponseEntity<User> userResponse = restTemplate.exchange(
                userUrl, HttpMethod.GET, null, new ParameterizedTypeReference<User>() {}
        );

        User leader = userResponse.getBody();
        String leaderName = (leader != null) ? leader.getUserName() : "Unknown";
        String leaderProfile = (leader != null) ? leader.getUserProfilePhoto() : "";

        return new TeamResponseDto(
                team.getTeamId(),
                team.getTeamName(),
                keyResultSummary,
                team.getTeamMembers().size(),
                progress,
                taskCount,
                teamLeaderId,
                leaderName,
                leaderProfile
        );
    }


    @Override
    public List<TeamMemberProgressDto> getTeamMembersProgress(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        List<Long> userIds = team.getTeamMembers();
        List<TeamMemberProgressDto> teamMembersProgress = new ArrayList<>();

        for (Long userId : userIds) {
            try {
                String userUrl = USER_SERVICE_URL + "/" +  userId;
                ResponseEntity<User> userResponse = restTemplate.getForEntity(userUrl, User.class);

                User user = userResponse.getBody();
                String userName = (user != null) ? user.getUserName() : "Unknown";
                String userProfile = (user != null) ? user.getUserProfilePhoto() : "";
                String role = (user != null) ? user.getUserDesignation() : "";
                String taskUrl = USER_SERVICE_URL + "/project/user/all-and-active-tasks?projectId=" + projectId + "&userId=" + userId;
                ResponseEntity<Map<String, Integer>> taskResponse = restTemplate.exchange(
                        taskUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Integer>>() {}
                );

                Map<String, Integer> taskData = taskResponse.getBody();
                int totalTasks = (taskData != null) ? taskData.getOrDefault("totalTask", 0) : 0;
                int completedTasks = (taskData != null) ? taskData.getOrDefault("completedTask", 0) : 0;

                double progress = (totalTasks == 0) ? 0.0 : ((double) completedTasks / totalTasks) * 100;

                teamMembersProgress.add(new TeamMemberProgressDto(userId, userName, userProfile, role,totalTasks, completedTasks, progress));
            } catch (Exception e) {
                LOGGER.error("Failed to fetch details for user {}: {}", userId, e.getMessage());
            }
        }

        return teamMembersProgress;
    }

    @Override
    public Long getTeamLeadId(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));
        return team.getTeamLead();
    }

    @Override
    public String getTeamName(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        return team.getTeamName();
    }

    @Override
    public boolean isTeamMappedToProject(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        return team.getAssignedProject().equals(projectId);
    }

    @Override
    public String addKeyResultToTeam(Long teamId, List<Long> request) {
        if (request == null || request.isEmpty()) {
            return "Invalid request body";
        }

        Long keyResultId = request.get(0);

        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();

            if (team.getAssignedKeyResult() == null) {
                team.setAssignedKeyResult(new ArrayList<>());
            }

            if (!team.getAssignedKeyResult().contains(keyResultId)) {
                team.getAssignedKeyResult().add(keyResultId);
                teamRepository.save(team);
                return "KeyResult added to Team successfully";
            }
            return "KeyResult already assigned to Team";
        }
        return "Team not found";
    }



}



