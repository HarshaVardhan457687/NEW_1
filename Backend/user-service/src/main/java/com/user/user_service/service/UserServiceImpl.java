package com.user.user_service.service;

import com.user.user_service.DTO.TaskDetailsDTO;
import com.user.user_service.DTO.TaskStatusCountDTO;
import com.user.user_service.DTO.UserSummaryDTO;
import com.user.user_service.config.ServiceUrlsConfig;
import com.user.user_service.constants.TaskStatus;
import com.user.user_service.entity.*;
import com.user.user_service.exception.ResourceNotFoundException;
import com.user.user_service.exception.UserNotFoundException;
import com.user.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    // Logger instance for logging important events and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ServiceUrlsConfig serviceUrlsConfig;

    //CREATE OR UPLOAD REQUEST ARE HERE
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
     * Uploads a user's profile photo to Cloudinary and updates the user record.
     *
     * @param userId The ID of the user whose profile photo is being uploaded
     * @param file The MultipartFile containing the image data
     * @return The URL of the uploaded image
     * @throws UserNotFoundException if the user with the given ID does not exist
     * @throws RuntimeException if there's an error uploading the image to Cloudinary
     */
    public String uploadUserProfilePhoto(Long userId, MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        String imageUrl = cloudinaryService.uploadImage(file);

        user.setUserProfilePhoto(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    //GET REQUESTS ARE HERE
    /**
     * Retrieves all users from the database.
     * @return List of all users
     */
    @Override
    public List<User> getAllUsers() {
        LOGGER.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Retrieves a specific user by ID.
     * @param userId The ID of the user to retrieve.
     * @return The user object if found, otherwise throws an exception.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public User getUserById(Long userId) {
        LOGGER.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }


    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public String getUserNameById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user not found"));
        return user.getUserName();
    }

    public String getProfilePicture(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return user.getUserProfilePhoto();
    }

    public List<UserSummaryDTO> findAllUsersWithProfile(){
        return userRepository.findAllUsersWithProfile();
    }

    @Override
    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserSummaryDTO(user.getUserId(), user.getUserName(), user.getUserProfilePhoto());
    }

    /**
     * Updates a user's information either partially (PATCH) or completely (PUT).
     *
     * In PATCH mode, only the non-null fields in the provided user object will be updated.
     * In PUT mode, the entire user object will be replaced (except for the ID).
     *
     * @param userId The ID of the user to update
     * @param user The user object containing the updated fields
     * @param isPatch If true, performs a partial update; if false, performs a complete replacement
     * @return The updated User object
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    @Override
    public User updateUserById(Long userId, User user, boolean isPatch) {
        User existingUser = getUserById(userId);

        if (isPatch) {
            if (user.getUserName() != null) existingUser.setUserName(user.getUserName());
            if (user.getUserEmail() != null) existingUser.setUserEmail(user.getUserEmail());
            if (user.getUserDesignation() != null) existingUser.setUserDesignation(user.getUserDesignation());
            if (user.getUserProfilePhoto() != null) existingUser.setUserProfilePhoto(user.getUserProfilePhoto());
            if (user.getUserPhoneNo() != null) existingUser.setUserPhoneNo(user.getUserPhoneNo());
            if (user.getUserAddress() != null) existingUser.setUserAddress(user.getUserAddress());
            if (user.getUserTimeZone() != null) existingUser.setUserTimeZone(user.getUserTimeZone());
            if (user.getUserIsNotificationAlert() != null) existingUser.setUserIsNotificationAlert(user.getUserIsNotificationAlert());
            if (user.getUserRole() != null) existingUser.setUserRole(user.getUserRole());
            if (user.getUserJoiningDate() != null) existingUser.setUserJoiningDate(user.getUserJoiningDate());
            if (user.getUserTaskAssigned() != null) existingUser.setUserTaskAssigned(user.getUserTaskAssigned());
            if (user.getUserInvolvedTeamsId() != null) existingUser.setUserInvolvedTeamsId(user.getUserInvolvedTeamsId());
            if (user.getUserManagerProjectId() != null) existingUser.setUserManagerProjectId(user.getUserManagerProjectId());
            if (user.getUserTeamLeaderProjectId() != null) existingUser.setUserTeamLeaderProjectId(user.getUserTeamLeaderProjectId());
            if (user.getUserTeamMemberProjectId() != null) existingUser.setUserTeamMemberProjectId(user.getUserTeamMemberProjectId());
        } else {
            existingUser = user;
            existingUser.setUserId(userId);
        }

        return userRepository.save(existingUser);
    }

    //HELPER METHOD
    private List<Long> getProjectIdsByRole(User user, String userRole) {
        return switch (userRole.toUpperCase()) {
            case "PROJECT_MANAGER" -> user.getUserManagerProjectId();
            case "TEAM_LEADER" -> user.getUserTeamLeaderProjectId();
            case "TEAM_MEMBER" -> user.getUserTeamMemberProjectId();
            default -> throw new RuntimeException("Invalid role: " + userRole);
        };
    }

    //Projects

    /**
     * Retrieves active projects based on user role and calculates their progress.
     *
     * @param userId The ID of the user
     * @param userRole The role of the user ("PROJECT_MANAGER", "TEAM_LEADER", or "TEAM_MEMBER")
     * @return List of active Project objects with calculated progress
     * @throws UserNotFoundException if the user with the given ID does not exist
     * @throws RuntimeException if the role is invalid or if there's an error communicating with other services
     */
    public List<Project> getActiveProjects(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        if (projectIds.isEmpty()) {
            return Collections.emptyList();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        ResponseEntity<List<Project>> response = restTemplate.exchange(
                serviceUrlsConfig.getPROJECT_SERVICE_URL() + "/active",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<Project>>() {}
        );

        List<Project> activeProjects = response.getBody();
        if (activeProjects == null || activeProjects.isEmpty()) {
            return Collections.emptyList();
        }

        for (Project project : activeProjects) {
            double progress = getProjectProgressById(project.getProjectId());
            project.setProjectProgress(progress);
        }

        return activeProjects;
    }

    // take the all projects list for particular role
    public List<Project> getAllProjects(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        if (projectIds.isEmpty()) {
            return Collections.emptyList();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        ResponseEntity<List<Project>> response = restTemplate.exchange(
                serviceUrlsConfig.getPROJECT_SERVICE_URL() + "/all",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<Project>>() {}
        );
        List<Project> allProjects = response.getBody();
        if (allProjects == null || allProjects.isEmpty()) {
            return Collections.emptyList();
        }

        for (Project project : allProjects) {
            double progress = getProjectProgressById(project.getProjectId());
            project.setProjectProgress(progress);
        }

        return allProjects;
    }

    private int getProjectProgressById(Long projectId) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    serviceUrlsConfig.getOBJECTIVE_SERVICE_URL() + "/by-project/progress/" + projectId,
                    HttpMethod.GET,
                    null,
                    Integer.class
            );
            return response.getBody() != null ? response.getBody() : 0;
        } catch (Exception e) {
            System.err.println("Error fetching progress for project ID: " + projectId);
            return 0;
        }
    }


    /**
     * Fetch active project count based on user role.
     */
    public Map<String, Long> getActiveAndTotalProjectsCount(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        long totalProjects = projectIds.size();

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                serviceUrlsConfig.getPROJECT_SERVICE_URL() + "/active/count",
                HttpMethod.POST,
                requestEntity,
                Long.class
        );
        long activeProjects = response.getBody();

        Map<String, Long> result = new HashMap<>();
        result.put("activeProjects", activeProjects);
        result.put("totalProjects", totalProjects);
        return result;
    }


    //Objectives

    /**
     * Fetch objectives based on user role and project list.
     */
    public List<Objective> getAllObjectivesByRole(Long userId, String userRole) {
        // Fetch user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Select the correct project list based on role
        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Objective Service
        try {
            ResponseEntity<List<Objective>> response = restTemplate.exchange(
                    serviceUrlsConfig.getOBJECTIVE_SERVICE_URL() + "/all/by-projects",
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
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Select the correct project list based on role
        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        // Make a POST request to Objective Service
        ResponseEntity<Map<String, List<Objective>>> response = restTemplate.exchange(
                serviceUrlsConfig.getOBJECTIVE_SERVICE_URL() + "/by-projectIds",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, List<Objective>>>() {
                }
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

    //KeyResults

    /**
     * Fetches all KeyResults for projects associated with a user based on their role.
     *
     * The response contains two lists:
     * - "allKeyResults": All KeyResults regardless of status
     * - "activeKeyResults": Only active KeyResults
     * @param userId The ID of the user
     * @param userRole The role of the user ("PROJECT_MANAGER", "TEAM_LEADER", or "TEAM_MEMBER")
     * @return Map containing lists of all KeyResults and active KeyResults
     * @throws UserNotFoundException if the user with the given ID does not exist
     * @throws RuntimeException if there's an error in service communication
     */
    public Map<String, List<KeyResult>> getKeyResultsForProjects(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        ResponseEntity<List<Objective>> objectiveResponse = restTemplate.exchange(
                serviceUrlsConfig.getOBJECTIVE_SERVICE_URL() + "/all/by-projects",
                HttpMethod.POST,
                new HttpEntity<>(projectIds),
                new ParameterizedTypeReference<>() {
                }
        );

        List<Objective> objectives = objectiveResponse.getBody();
        if (objectives == null || objectives.isEmpty()) {
            return Map.of("allKeyResults", List.of(), "activeKeyResults", List.of());
        }

        List<Long> objectiveIds = objectives.stream()
                .map(Objective::getObjectiveId)
                .toList();

        ResponseEntity<Map<String, List<KeyResult>>> keyResultResponse = restTemplate.exchange(
                serviceUrlsConfig.getKEYRESULT_SERVICE_URL() + "/by-objectives",
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

    //Tasks

    public Map<String, List<Task>> getTasksForProjects(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(projectIds, headers);

        ResponseEntity<List<Task>> taskResponse = restTemplate.exchange(
                serviceUrlsConfig.getTASK_SERVICE_URL() + "/by-projects-and-user?userId=" + userId,  // Pass userId correctly
                HttpMethod.POST,
                requestEntity,  // Send only projectIds in the body
                new ParameterizedTypeReference<>() {}
        );

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

    /**
     * Retrieves active tasks assigned to a specific user.
     *
     * @param userId The ID of the user
     * @param userRole The role of the user ("PROJECT_MANAGER", "TEAM_LEADER", or "TEAM_MEMBER")
     * @return List of active Task objects assigned to the user
     * @throws UserNotFoundException if the user with the given ID does not exist
     * @throws RuntimeException if there's an error communicating with the Task Service
     */
    public List<Task> getActiveTasksForUser(Long userId, String userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Long> projectIds = getProjectIdsByRole(user, userRole);

        List<Long> taskIds = user.getUserTaskAssigned();

        Map<String, List<Long>> requestBody = new HashMap<>();
        requestBody.put("taskIds", taskIds);
        requestBody.put("projectIds", projectIds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<Long>>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<Task>> taskResponse = restTemplate.exchange(
                serviceUrlsConfig.getTASK_SERVICE_URL() + "/tasks-by-ids-and-projects",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<Task>>() {}
        );

        List<Task> allTasks = taskResponse.getBody();
        if (allTasks == null) {
            return new ArrayList<>();
        }

        return allTasks.stream()
                .filter(Task::isTaskIsActive)
                .toList();
    }

    @Override
    public List<TaskDetailsDTO> getTasksForUser(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        ResponseEntity<List<Task>> taskResponse = restTemplate.exchange(
                serviceUrlsConfig.getTASK_SERVICE_URL()+"/project/"+projectId+"/user/"+userId+"/all-tasks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {}
        );

        List<Task> tasks = taskResponse.getBody();
        if (tasks == null) {
            return Collections.emptyList();
        }

        return tasks.stream()
                .map(task -> new TaskDetailsDTO(
                        task.getTaskId(),
                        task.getTaskHeading(),
                        task.getTaskDueDate(),
                        task.getTaskTag(),
                        task.getTaskPriority(),
                        task.getTaskStatus(),
                        task.getTaskDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatusCountDTO getTaskStatusCounts(String userEmail, Long projectId) {
        Long userId = getUserByEmail(userEmail).getUserId();

        List<TaskDetailsDTO> tasks = getTasksForUser(userId, projectId);

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.COMPLETED).count();
        long waitingForApprovalTasks = tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.WAITING_FOR_APPROVAL).count();
        long pendingTasks = tasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.PENDING).count();

        return new TaskStatusCountDTO(totalTasks, completedTasks, waitingForApprovalTasks, pendingTasks);
    }

    @Override
    public int getActiveTasksCountForUserInProject(Long projectId, Long userId) {
        String url = serviceUrlsConfig.getTASK_SERVICE_URL() + "/project/" + projectId + "/user/" + userId + "/active-tasks" ;

        ResponseEntity<Integer> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Integer.class,
                projectId,
                userId
        );
        return response.getBody();
    }

    public Map<String, Integer> getAllAndActiveTasksCountForUserInProject(Long projectId, Long userId) {
        String url = serviceUrlsConfig.getTASK_SERVICE_URL() + "/project/" + projectId + "/user/" + userId + "/all-tasks-count";

        try {
            ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null, // No request body
                    new ParameterizedTypeReference<Map<String, Integer>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch task counts for project {} and user {}: {}", projectId, userId, e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Assigns a task to a user.
     *
     * @param userId The ID of the user.
     * @param taskId The ID of the task to be assigned.
     * @return true if the task was assigned successfully, false if user was not found.
     */
    public boolean assignTaskToUser(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (user != null) {
            List<Long> userTasks = user.getUserTaskAssigned();
            if (userTasks == null) {
                userTasks = new ArrayList<>();
            }
            userTasks.add(taskId);
            user.setUserTaskAssigned(userTasks);
            updateUserById(userId, user, true);
            return true;
        }
        return false;
    }

    //TEAM END POINTS ARE HERE

    @Transactional
    public void updateUserTeams(Map<String, Object> request) {
        List<Long> teamMemberIds = (List<Long>) request.get("teamMemberIds");
        Long teamId = ((Number) request.get("teamId")).longValue();
        Long assignedProject = ((Number) request.get("assignedProject")).longValue();
        Long teamLead = ((Number) request.get("teamLead")).longValue();

        User teamLeader = userRepository.findById(teamLead)
                .orElseThrow(() -> new UserNotFoundException("Team leader not found with id: " + teamLead));

        List<Long> teamLeadersTeam = teamLeader.getUserInvolvedTeamsId();
        if (teamLeadersTeam == null) {
            teamLeadersTeam = new ArrayList<>();
        }
        if (!teamLeadersTeam.contains(teamId)) {
            teamLeadersTeam.add(teamId);
        }
        teamLeader.setUserInvolvedTeamsId(teamLeadersTeam);

        List<User> users = userRepository.findAllById(teamMemberIds);
        for (User user : users) {
            List<Long> userTeams = user.getUserInvolvedTeamsId();
            if (userTeams == null) {
                userTeams = new ArrayList<>();
            }
            if (!userTeams.contains(teamId)) {
                userTeams.add(teamId);
            }
            user.setUserInvolvedTeamsId(userTeams);

            if (!user.getUserId().equals(teamLead)) {
                List<Long> memberProjects = user.getUserTeamMemberProjectId();
                if (memberProjects == null) {
                    memberProjects = new ArrayList<>();
                }
                if (!memberProjects.contains(assignedProject)) {
                    memberProjects.add(assignedProject);
                }
                user.setUserTeamMemberProjectId(memberProjects);
            }
        }

        List<Long> leaderProjects = teamLeader.getUserTeamLeaderProjectId();
        if (leaderProjects == null) {
            leaderProjects = new ArrayList<>();
        }
        if (!leaderProjects.contains(assignedProject)) {
            leaderProjects.add(assignedProject);
        }
        teamLeader.setUserTeamLeaderProjectId(leaderProjects);

        users.add(teamLeader);
        userRepository.saveAll(users);
    }



    /**
     * Deletes a user by its ID.
     *
     * @param userId The ID of the user to delete.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public void deleteUser(Long userId) {
        LOGGER.info("Deleting user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        userRepository.delete(user);
    }

    //MAPPED PROJECT FOR THE USER
    public Long findMappedTeamForUser(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<Long> teamIds = user.getUserInvolvedTeamsId();

        for (Long teamId : teamIds) {
            String url = serviceUrlsConfig.getTEAM_SERVICE_URL() + "/is-mapped-to-project" +"?teamId=" + teamId + "&projectId=" + projectId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

            if (Boolean.TRUE.equals(response.getBody())) {
                return teamId;
            }
        }
        return null;
    }

    @Override
    public boolean updateProjectMangerProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (user != null) {
            List<Long> userManagerList = user.getUserManagerProjectId();
            if (userManagerList == null) {
                userManagerList = new ArrayList<>();
            }
            userManagerList.add(projectId);
            user.setUserManagerProjectId(userManagerList);
            updateUserById(userId, user, true);
            return true;
        }
        return false;
    }
}
