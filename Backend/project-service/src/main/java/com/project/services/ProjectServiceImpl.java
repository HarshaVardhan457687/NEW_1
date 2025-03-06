package com.project.services;

import com.project.DTO.*;
import com.project.model.KeyResult;
import com.project.model.Objective;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProjectServiceImpl implements ProjectService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);


    private static final String OBJECTIVE_URL = "http://localhost:8081/api/objective/";
    private static final String KEYRESULT_URL = "http://localhost:8082/api/keyresults/";
    private static final String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";
    private static final String TEAM_SERVICE_URL = "http://localhost:8084/api/teams/";
    private static final String USER_SERVICE_URL = "http://localhost:8086/api/users/";


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Create a new project.
     * @param project The project object to be created.
     * @return The created project object.
     */
    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Get a list of all projects
     * @return List of all projects.
     */
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Get a project by its ID.
     * It also calculates and sets the project progress.
     * @param id The ID of the project.
     * @return An Optional containing the project if found, else empty.
     */
    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id)
            .map(project -> {
                double progress = getProjectProgressById(project.getProjectId());
                project.setProjectProgress(progress);
                return project;
            });
    }

    /**
     * Helper method to get the progress of a project.
     * It calls an external service to fetch the progress.
     * @param projectId The ID of the project.
     * @return The project progress as a percentage.
     */

    private int getProjectProgressById(Long projectId) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    OBJECTIVE_URL + "/by-project/progress/" + projectId,
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
     * Update project details.
     * @param id The ID of the project to update.
     * @param projectDetails The updated project details.
     * @return The updated project.
     */
    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        project.setProjectName(projectDetails.getProjectName());
        project.setProjectDescription(projectDetails.getProjectDescription());
        project.setProjectPriority(projectDetails.getProjectPriority());
        project.setProjectStatus(projectDetails.getProjectStatus());
        project.setActive(projectDetails.getActive());
        project.setTeamsInvolvedId(projectDetails.getTeamsInvolvedId());
        project.setObjectiveId(projectDetails.getObjectiveId());
        project.setKeyResultIds(projectDetails.getKeyResultIds());
        project.setProjectDueDate(projectDetails.getProjectDueDate());
        project.setProjectManagerId(projectDetails.getProjectManagerId());
        project.setProjectProgress(projectDetails.getProjectProgress());

        return projectRepository.save(project);
    }

    /**
     * Partially update project details.
     * This method only updates fields that are not null in the request.
     * @param id The ID of the project to update.
     * @param projectDetails The project details to update.
     * @return The updated project.
     */
    @Override
    public Project patchProject(Long id, Project projectDetails){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (projectDetails.getProjectName() != null) project.setProjectName(projectDetails.getProjectName());
        if (projectDetails.getProjectDescription() != null) project.setProjectDescription(projectDetails.getProjectDescription());
        if (projectDetails.getProjectPriority() != null) project.setProjectPriority(projectDetails.getProjectPriority());
        if (projectDetails.getProjectStatus() != null) project.setProjectStatus(projectDetails.getProjectStatus());
        if (projectDetails.getActive() != null) project.setActive(projectDetails.getActive());
        if (projectDetails.getTeamsInvolvedId() != null) project.setTeamsInvolvedId(projectDetails.getTeamsInvolvedId());
        if (projectDetails.getObjectiveId() != null) project.setObjectiveId(projectDetails.getObjectiveId());
        if (projectDetails.getKeyResultIds() != null) project.setKeyResultIds(projectDetails.getKeyResultIds());
        if (projectDetails.getProjectDueDate() != null) project.setProjectDueDate(projectDetails.getProjectDueDate());
        if (projectDetails.getProjectManagerId() != null) project.setProjectManagerId(projectDetails.getProjectManagerId());
        if (projectDetails.getProjectProgress() != null) project.setProjectProgress(projectDetails.getProjectProgress());

        return projectRepository.save(project);
    }

    /**
     * Delete a project by its ID.
     * @param id The ID of the project to delete.
     */
    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    /**
     * Get all objectives associated with a project.
     * @param projectId The project ID.
     * @return A list of objectives associated with the project.
     */
    @Override
    public List<Objective> getAllObjectiveAssociatedWithProject(Long projectId){
        String url = OBJECTIVE_URL + "project/" + projectId;
        List<Objective> allAssociatedObjective = Arrays.asList(
                restTemplate.getForObject(url, Objective[].class)
        );
        return allAssociatedObjective;
    }

    /**
     * Get the count of active projects from a list of project IDs.
     * @param projectIds List of project IDs.
     * @return The count of active projects.
     */
    @Override
    public long getActiveProjectsCount(List<Long> projectIds) {
        return projectRepository.countByProjectIdInAndIsActiveTrue(projectIds);
    }

    /**
     * Get the project name by its ID.
     * @param projectId The ID of the project.
     * @return The project name if found, or a default message if not found.
     */
    @Override
    public String getNameOfProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(Project::getProjectName)  // If present, get the project name
                .orElse("Project not found");  // If not present, return a default message
    }

    /**
     * Get all active projects from a list of project IDs.
     * @param projectIds List of project IDs.
     * @return List of active projects.
     */
    @Override
    public List<Project> getAllActiveProjects(List<Long> projectIds) {
        return projectRepository.findByProjectIdInAndIsActive(projectIds, true);
    }

    /**
     * Get all projects from a list of project IDs, including both active and inactive.
     * @param projectIds List of project IDs.
     * @return Combined list of active and inactive projects.
     */
    @Override
    public List<Project> getAllProjects(List<Long> projectIds) {
        List<Project> allProjects = new ArrayList<>(projectRepository.findByProjectIdInAndIsActive(projectIds, false));
        allProjects.addAll(projectRepository.findByProjectIdInAndIsActive(projectIds, true));
        return allProjects;
    }

    /**
     * Get detailed information about objectives associated with a project.
     * This includes the total number of objectives and their progress status (completed, in-progress, not-started).
     * @param projectId The project ID.
     * @return A map containing the number of completed, in-progress, and not-started objectives.
     */
    @Override
    public Map<String, Integer> getObjectivesInfoByProject(Long projectId) {
        Map<String, Integer> mapInfo = new HashMap<>();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        List<Long> objectivesIds = project.getObjectiveId();

        mapInfo.put("totalObjectives", objectivesIds.size());

        int completedObjectives = 0;
        int inProgressObjectives = 0;
        int notStartedObjectives = 0;

        for (Long objectiveId : objectivesIds) {
            String url = OBJECTIVE_URL + "progress/" + objectiveId;

            try {
                ResponseEntity<Double> currProgress = restTemplate.getForEntity(url, Double.class);
                if (currProgress.getBody() == null) {
                    notStartedObjectives++;
                } else {
                    double progress = currProgress.getBody();
                    if (progress == 0.0) {
                        notStartedObjectives++;
                    } else if (progress == 100.0) {
                        completedObjectives++;
                    } else {
                        inProgressObjectives++;
                    }
                }
            } catch (Exception e) {
                notStartedObjectives++;
            }
        }

        mapInfo.put("completedObjectives", completedObjectives);
        mapInfo.put("inProgressObjectives", inProgressObjectives);
        mapInfo.put("notStartedObjectives", notStartedObjectives);

        return mapInfo;
    }

    /**
     * Retrieves task information for a specific project.
     *
     * This method calls the external task service to fetch task data related to a given project.
     * The response is expected to be a map containing task-related information, such as task counts.
     * If the request fails due to any network or service issues, an empty map is returned as a fallback.
     *
     * @param projectId the ID of the project for which task info is being fetched
     * @return a map containing task-related information (task counts, etc.) for the project
     */
    @Override
    public Map<String, Integer> getTaskInfoForProject(Long projectId) {
        String url =  TASK_SERVICE_URL + "project/task-info/{projectId}";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Map.class,
                    projectId
            );

            return response.getBody();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * Fetches detailed team information (team name and leader details) for a project.
     *
     * This method queries the project repository to get a list of team IDs associated with a project.
     * It then calls the external team and user services to fetch detailed information about each team,
     * including the team name, leader name, and leader profile picture.
     *
     * @param projectId the ID of the project whose team details are to be fetched
     * @return a list of TeamDetailsDTO containing details of all teams involved in the project
     */
    @Override
    public List<TeamDetailsDTO> getProjectTeamsDetails(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Long> teamIds = project.getTeamsInvolvedId();

        List<TeamDetailsDTO> teamDetailsList = new ArrayList<>();

        for (Long teamId : teamIds) {
            TeamDTO team = restTemplate.getForObject(TEAM_SERVICE_URL + teamId, TeamDTO.class);

            Long teamLeadId = team.getTeamLead();
            UserDTO teamLeader = restTemplate.getForObject(USER_SERVICE_URL + teamLeadId, UserDTO.class);

            TeamDetailsDTO teamDetails = new TeamDetailsDTO();
            teamDetails.setTeamName(team.getTeamName());
            teamDetails.setTeamLeaderName(teamLeader.getUserName());
            teamDetails.setTeamLeaderProfile(teamLeader.getUserProfilePhoto());

            teamDetailsList.add(teamDetails);
        }
        return teamDetailsList;
    }

    /**
     * Retrieves all teams associated with a specific project.
     *
     * This method queries the project repository to get all team IDs associated with the given project.
     * It then fetches detailed team information (such as team name and team leader) for each team from
     * the external team service using the team ID.
     *
     * If the service call fails for any team, that particular team’s details will be skipped.
     *
     * @param projectId the ID of the project whose team details are to be fetched
     * @return a list of TeamResponseDTO containing the detailed team information
     */
    @Override
    public List<TeamResponseDTO> getTeamsForProject(Long projectId) {
        List<TeamResponseDTO> teams = new ArrayList<>();
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new RuntimeException("Project not found"));
        List<Long> teamIds = project.getTeamsInvolvedId();
        for (Long teamId : teamIds) {

            String url = TEAM_SERVICE_URL + "details?teamId=" + teamId;

            try {
                ResponseEntity<TeamResponseDTO> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<TeamResponseDTO>() {}
                );

                if (response.getBody() != null) {
                    teams.add(response.getBody());
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch details for team ID " + teamId + ": " + e.getMessage());
            }
        }
        return teams;
    }

    /**
     * Retrieves details for a specific team by projectId and teamId.
     *
     * This method fetches the project first to ensure that the project exists. Then it fetches the
     * details of a specific team associated with the project, using the teamId.
     *
     * @param projectId the ID of the project
     * @param teamId the ID of the team whose details are to be fetched
     * @return a TeamResponseDTO containing the detailed team information if found, or null if not found
     */
    @Override
    public TeamResponseDTO getTeamForProject(Long projectId, Long teamId) {
        projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        String url = TEAM_SERVICE_URL + "details?teamId=" + teamId;
        try {
            ResponseEntity<TeamResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<TeamResponseDTO>() {}
            );

            if (response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch details for team ID " + teamId + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all key results associated with the objectives of a project.
     *
     * This method first retrieves the project using its projectId and extracts the list of objectives
     * associated with it. It then calls an external service to fetch key results based on the objectiveIds.
     *
     * The method ensures that the response is valid and contains key results before returning the list.
     * If any error occurs or no key results are found, an empty list is returned.
     *
     * @param projectId the ID of the project for which key results are being fetched
     * @return a list of KeyResult objects related to the project’s objectives
     */
    @Override
    public List<KeyResult> getAllKeyResult(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Long> objectiveIds = project.getObjectiveId();
        System.out.println("Objective IDs retrieved: " + objectiveIds);

        String url = KEYRESULT_URL + "all/by-objectives";

        try {
            ResponseEntity<List<KeyResult>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(objectiveIds),
                    new ParameterizedTypeReference<List<KeyResult>>() {}
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<KeyResult> keyResults = responseEntity.getBody();
                return keyResults != null ? keyResults : Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Adds a team to a project if the team is not already associated with it.
     *
     * This method first checks whether the team is already part of the project by checking
     * the project's list of involved teams. If the team is not part of the project, it is added,
     * and the project is saved back to the repository. Logging is done to track the addition of teams.
     * If the team is already part of the project, a warning is logged to indicate this.
     *
     * @param projectId the ID of the project to which the team is being added
     * @param teamId the ID of the team being added to the project
     */
    @Override
    public void addTeamToProject(Long projectId, Long teamId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getTeamsInvolvedId().contains(teamId)) {
            project.getTeamsInvolvedId().add(teamId);
            projectRepository.save(project);
            LOGGER.info("Team {} added to Project {}", teamId, projectId);
        } else {
            LOGGER.warn("Team {} is already part of Project {}", teamId, projectId);
        }
    }

    /**
     * Adds an objective to a project if it is not already associated with it.
     *
     * This method checks if the objective is already associated with the project. If the objective
     * is not already part of the project, it is added to the project’s list of objectives and the
     * project is saved. A success or failure message is returned based on the result.
     *
     * @param projectId the ID of the project to which the objective is being added
     * @param request a list of objective IDs to be added to the project
     * @return a string message indicating the result of the operation (success or failure)
     */
    @Override
    public String addObjectiveToProject(Long projectId, List<Long> request) {
        if (request == null || request.isEmpty()) {
            return "Invalid request body";
        }

        Long objectiveId = request.get(0);

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            if (project.getObjectiveId() == null) {
                project.setObjectiveId(new ArrayList<>());
            }

            if (!project.getObjectiveId().contains(objectiveId)) {
                project.getObjectiveId().add(objectiveId);
                projectRepository.save(project);
                return "Objective added to Project successfully";
            }
            return "Objective already assigned to Project";
        }
        return "Project not found";
    }

    public Map<String, Integer> getKeyResultsCounts(Long projectId) {
        // Fetch the project from the repository by projectId
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Retrieve the list of objective IDs associated with the project
        List<Long> objectiveIds = project.getObjectiveId();
        System.out.println("Objective IDs retrieved: " + objectiveIds);

        // URL for the external service to fetch key results based on objective IDs
        String url = KEYRESULT_URL + "all/by-objectives";

        // Initialize counters for completed and total key results
        int totalKeyResultsCount = 0;
        int completedKeyResultsCount = 0;

        try {
            // Make a POST request to the external service to fetch key results for the given objective IDs
            ResponseEntity<List<KeyResultActiveDTO>> responseEntity = restTemplate.exchange(
                    url,                         // URL to fetch key results
                    HttpMethod.POST,              // HTTP POST request
                    new HttpEntity<>(objectiveIds), // Request body containing the objective IDs
                    new ParameterizedTypeReference<List<KeyResultActiveDTO>>() {} // Expected response type (List of KeyResultActiveDTOs)
            );

            // If the response is successful, process the key results
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<KeyResultActiveDTO> keyResults = responseEntity.getBody();

                // Check if the response body is not null
                if (keyResults != null) {
                    // Loop through each KeyResultActiveDTO
                    for (KeyResultActiveDTO keyResult : keyResults) {
                        totalKeyResultsCount++; // Increment the total key results count for every key result

                        // Compare current value and target value to determine if the key result is completed
                        if (keyResult.getKeyResultcurrentVal() == keyResult.getKeyResultTargetVal()) {
                            completedKeyResultsCount++; // Increment the count for completed key results
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log and handle any errors that occur during the request
            e.printStackTrace();
        }

        // Prepare the result as a map with both counts
        Map<String, Integer> resultCounts = new HashMap<>();
        resultCounts.put("totalKeyResults", totalKeyResultsCount); // Total key results count
        resultCounts.put("completedKeyResults", completedKeyResultsCount); // Completed key results count

        // Return the map containing both counts
        return resultCounts;
    }


}