package com.user.user_service.service;

import com.user.user_service.entity.*;
import com.user.user_service.exception.ResourceNotFoundException;
import com.user.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService{

    // Logger instance for logging important events and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired RestTemplate restTemplate;

    private static final String PROJECT_SERVICE_URL = "http://localhost:8085/api/projects/active/count";
    private static final String OBJECTIVE_SERVICE_URL = "http://localhost:8081/api/objectives/all/by-projects";
    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8081/api/objectives/all/by-projects";
    /**
     * Creates a new user and saves it to the database.
     * @param user User object to be created.
     * @return The saved User.
     */
    @Override
    public User createNewUser(User user) {
        LOGGER.info("Creating new user...");
        return userRepository.save(user);
    }

    /**
     * Fetches all users.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> getAllUsers() {
        LOGGER.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Fetches a user by its ID.
     *
     * @param userId The ID of the user to fetch.
     * @return The User object if found.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public User getUserById(Long userId) {
        LOGGER.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Updates a user by its ID.
     *
     * @param userId The ID of the user to update.
     * @return The updated User object.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public User updateUserById(Long userId, User user) {
        // Get existing user
        User existingUser = getUserById(userId);

        existingUser.setUserName(user.getUserName());
        existingUser.setUserEmail(user.getUserEmail());
        existingUser.setUserDesignation(user.getUserDesignation());
        existingUser.setUserProfilePhoto(user.getUserProfilePhoto());
        existingUser.setUserPhoneNo(user.getUserPhoneNo());
        existingUser.setUserAddress(user.getUserAddress());
        existingUser.setUserTimeZone(user.getUserTimeZone());
        existingUser.setUserIsNotificationAlert(user.getUserIsNotificationAlert());
        existingUser.setUserRole(user.getUserRole());
        existingUser.setUserInvolvedTeamsId(user.getUserInvolvedTeamsId());
        existingUser.setUserManagerProjectId(user.getUserManagerProjectId());
        existingUser.setUserTeamLeaderProjectId(user.getUserTeamLeaderProjectId());
        existingUser.setUserTeamMemberProjectId(user.getUserTeamMemberProjectId());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param userId The ID of the user to delete.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public void deleteUser(Long userId) {
        LOGGER.info("Deleting user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        userRepository.delete(user);
    }

    /**
     * Fetches all tasks assigned to a user.
     *
     * @param userId The ID of the user whose tasks are to be fetched.
     * @return A list of tasks assigned to the user.
     */
//    @Override
//    public List<Task> getAllTasksOfUser(Long userId) {
//        LOGGER.info("Fetching all tasks for user with ID: {}", userId);
//        return userRepository.;
//    }
    @Override
    public List<Task> getAllTasksOfUser(Long userId) {
        return List.of();
    }

    /**
     * Fetches all project IDs associated with a user.
     *
     * @param userId The ID of the user whose projects are to be fetched.
     * @return A list of project IDs associated with the user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public List<Long> getAllProjectOfUser(Long userId) {
        LOGGER.info("Fetching all projects for user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return user.getUserProject();
    }

    @Override
    public List<Team> getAllTeamsOfUser(Long userId) {
        return List.of();
    }


    /**
     * Fetch active project count based on user role.
     */
    public Long getActiveProjectsCount(Long userId, String userRole) {
        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Select the correct project list based on role
        List<Long> projectIds = switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Project Service
        ResponseEntity<Long> response = restTemplate.exchange(
                PROJECT_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                Long.class
        );

        return response.getBody(); // Return project count
    }

    /**
     * Fetch objectives based on user role and project list.
     */
    public List<Objective> getAllObjectivesByRole(Long userId, String userRole) {
        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Select the correct project list based on role
        List<Long> projectIds = switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Objective Service
        ResponseEntity<List<Objective>> response = restTemplate.exchange(
                OBJECTIVE_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<Objective>>() {} // To handle list response
        );

        return response.getBody(); // Return objectives
    }

    /**
     * Fetch all objectives and active objectives based on user role.
     */
    public Map<String, List<Objective>> getObjectivesByRole(Long userId, String userRole) {
        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Select the correct project list based on role
        List<Long> projectIds = switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Objective Service
        ResponseEntity<Map<String, List<Objective>>> response = restTemplate.exchange(
                OBJECTIVE_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, List<Objective>>>() {} // Handling map response
        );

        return response.getBody(); // Return the map containing objectives
    }

    /**
     * Fetch all KeyResult and active KeyResult based on user role.
     */
   // Just take all the objective first by projectID then take the for taht objective call the keyResult reservice it will//
   // give all the key results with active and all the keyResults; and endpoint is commented for this
    public Map<String, List<KeyResult>> getKeyResultsForProjects(List<Long> projectIds) {
        // 1️⃣ Fetch Objectives for given Projects
        ResponseEntity<List<Objective>> objectiveResponse = restTemplate.exchange(
                OBJECTIVE_SERVICE_URL + "/objectives/by-projects",
                HttpMethod.POST,
                new HttpEntity<>(projectIds),
                new ParameterizedTypeReference<>() {}
        );

        List<Objective> objectives = objectiveResponse.getBody();
        if (objectives == null || objectives.isEmpty()) {
            return Map.of("allKeyResults", List.of(), "activeKeyResults", List.of());
        }

        // Extract Objective IDs
        List<Long> objectiveIds = objectives.stream()
                .map(Objective::getObjectiveId)
                .toList();

        // 2️⃣ Fetch KeyResults for the obtained Objectives
        ResponseEntity<Map<String, List<KeyResult>>> keyResultResponse = restTemplate.exchange(
                KEYRESULT_SERVICE_URL + "/key-results/by-objectives",
                HttpMethod.POST,
                new HttpEntity<>(objectiveIds),
                new ParameterizedTypeReference<>() {}
        );

        return keyResultResponse.getBody();
    }
}
