package com.user.user_service.service;

import com.user.user_service.entity.*;
import com.user.user_service.exception.ResourceNotFoundException;
import com.user.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    // Logger instance for logging important events and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    private static final String PROJECT_SERVICE_URL = "http://localhost:8085/api/projects/active/count";
    private static final String OBJECTIVE_SERVICE_URL = "http://localhost:8081/api/objective";
    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8082/api/keyresults";
    private static final String TASK_SERVICE_URL = "http://localhost:8083/api/tasks";

    /**
     * Creates a new user and saves it to the database.
     *
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


    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
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


    @Override
    public List<Team> getAllTeamsOfUser(Long userId) {
        return List.of();
    }


    /**
     * Fetch active project count based on user role.
     */
    public Map<String, Long> getActiveAndTotalProjectsCount(Long userId, String userRole) {
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

        // Total project count
        long totalProjects = projectIds.size();

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Project Service to get active projects count
        ResponseEntity<Long> response = restTemplate.exchange(
                PROJECT_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                Long.class
        );
        long activeProjects = response.getBody();

        // Return both active and total project counts
        Map<String, Long> result = new HashMap<>();
        result.put("activeProjects", activeProjects);
        result.put("totalProjects", totalProjects);
        return result;
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
        try {
            ResponseEntity<List<Objective>> response = restTemplate.exchange(
                    OBJECTIVE_SERVICE_URL + "/all/by-projects",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<Objective>>() {
                    }
            );
            return response.getBody();
        } catch (HttpServerErrorException e) {
            LOGGER.info("Error calling Objective Service: " + e.getMessage());
            throw new RuntimeException("Objective service failed: " + e.getMessage());
        }
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
                OBJECTIVE_SERVICE_URL + "/by-projectIds",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, List<Objective>>>() {
                } // Handling map response
        );

        return response.getBody(); // Return the map containing objectives
    }

    // get count of the active objective and all objective
    public Map<String, Long> getObjectivesCountByRole(Long userId, String userRole) {
        Map<String, List<Objective>> objectives = getObjectivesByRole(userId, userRole);

        long totalObjectives = objectives.getOrDefault("allObjectives", List.of()).size();
        long activeObjectives = objectives.getOrDefault("activeObjectives", List.of()).size();

        Map<String, Long> countMap = new HashMap<>();
        countMap.put("totalObjectives", totalObjectives);
        countMap.put("activeObjectives", activeObjectives);

        return countMap;
    }

    /**
     * Fetch all KeyResult and active KeyResult based on user role.
     */
    // Just take all the objective first by projectID then take the for taht objective call the keyResult reservice it will//
    // give all the key results with active and all the keyResults; and endpoint is commented for this
    public Map<String, List<KeyResult>> getKeyResultsForProjects(Long userId, String userRole) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Select the correct project list based on role
        List<Long> projectIds = switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };
        // 1️⃣ Fetch Objectives for given Projects
        ResponseEntity<List<Objective>> objectiveResponse = restTemplate.exchange(
                OBJECTIVE_SERVICE_URL + "/all/by-projects",
                HttpMethod.POST,
                new HttpEntity<>(projectIds),
                new ParameterizedTypeReference<>() {
                }
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
                KEYRESULT_SERVICE_URL + "/by-objectives",
                HttpMethod.POST,
                new HttpEntity<>(objectiveIds),
                new ParameterizedTypeReference<>() {
                }
        );

        return keyResultResponse.getBody();
    }

    // Get count active and all key results
    public Map<String, Long> getKeyResultsCountByRole(Long userId, String userRole) {
        Map<String, List<KeyResult>> keyResults = getKeyResultsForProjects(userId, userRole);

        long totalKeyResults = keyResults.getOrDefault("allKeyResults", List.of()).size();
        long activeKeyResults = keyResults.getOrDefault("activeKeyResults", List.of()).size();

        Map<String, Long> countMap = new HashMap<>();
        countMap.put("totalKeyResults", totalKeyResults);
        countMap.put("activeKeyResults", activeKeyResults);

        return countMap;
    }

    public Map<String, List<Task>> getTasksForProjects(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> projectIds = switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };

        // Prepare request body with projectIds and userId
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("projectIds", projectIds);
        requestBody.put("userId", userId);

        ResponseEntity<List<Task>> taskResponse = restTemplate.exchange(
                TASK_SERVICE_URL + "/by-projects-and-user",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<>() {
                }
        );

        // Grouping tasks into categories
        List<Task> allTasks = taskResponse.getBody();
        List<Task> activeTasks = allTasks.stream()
                .filter(Task::isTaskIsActive)
                .toList();

        Map<String, List<Task>> tasksMap = new HashMap<>();
        tasksMap.put("allTasks", allTasks);
        tasksMap.put("activeTasks", activeTasks);

        return tasksMap;
    }

    public Map<String, Long> getTasksCountByRole(Long userId, String userRole) {
        Map<String, List<Task>> tasks = getTasksForProjects(userId, userRole);

        long totalTasks = tasks.getOrDefault("allTasks", List.of()).size();
        long activeTasks = tasks.getOrDefault("activeTasks", List.of()).size();

        Map<String, Long> countMap = new HashMap<>();
        countMap.put("totalTasks", totalTasks);
        countMap.put("activeTasks", activeTasks);

        return countMap;
    }

}
