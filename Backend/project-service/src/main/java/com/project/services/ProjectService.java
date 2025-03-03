package com.project.services;

import com.project.DTO.TeamDTO;
import com.project.DTO.TeamDetailsDTO;
import com.project.DTO.TeamResponseDTO;
import com.project.DTO.UserDTO;
import com.project.constants.ProjectStatus;
import com.project.model.Objective;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProjectService {

    private static final String OBJECTIVE_URL = "http://localhost:8081/api/objective/";
    private static final String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";
    private static final String TEAM_SERVICE_URL = "http://localhost:8084/api/teams/";
    private static final String USER_SERVICE_URL = "http://localhost:8086/api/users/";


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Create Project
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Get All Projects (No Pagination)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get Project by ID
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id)
            .map(project -> {
                double progress = getProjectProgressById(project.getProjectId());
                project.setProjectProgress(progress);
                return project;
            });
    }
    

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
            return 0; // Default progress if service fails
        }
    }

    // Update Project
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

    // PATCH PROJECT
    public Project patchProject(Long id, Project projectDetails){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Update only non-null fields
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


    // Delete Project
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Dashboard Methods


    // Additional query methods
    public long getProjectCountByStatus(ProjectStatus status) {
        return projectRepository.countByProjectStatus(status);
    }

    public long getProjectCountByActiveStatus(Boolean isActive) {
        return projectRepository.countByIsActive(isActive);
    }


    //get all the associated objective of that project
    public List<Objective> getAllObjectiveAssociatedWithProject(Long projectId){
        String url = OBJECTIVE_URL + "project/" + projectId;
        List<Objective> allAssociatedObjective = Arrays.asList(
                restTemplate.getForObject(url, Objective[].class)
        );
        return allAssociatedObjective;
    }

    //get all the objective give List of the projectIds;


    //get how many project active from that project
    public long getActiveProjectsCount(List<Long> projectIds) {
        return projectRepository.countByProjectIdInAndIsActiveTrue(projectIds);
    }

    public String getNameOfProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(Project::getProjectName)  // If present, get the project name
                .orElse("Project not found");  // If not present, return a default message
    }

    public List<Project> getAllActiveProjects(List<Long> projectIds) {
        return projectRepository.findByProjectIdInAndIsActive(projectIds, true);
    }

    public List<Project> getAllProjects(List<Long> projectIds) {
        List<Project> allProjects = new ArrayList<>(projectRepository.findByProjectIdInAndIsActive(projectIds, false));
        allProjects.addAll(projectRepository.findByProjectIdInAndIsActive(projectIds, true));
        return allProjects;
    }

//    public Map<String, Integer> getObjectivesInfo(List<Long> objectivesIds) {
//        Map<String, Integer> mapInfo = new HashMap<>();
//        mapInfo.put("totalObjectives", objectivesIds.size());
//
//        int completedObjectives = 0;
//        int inProgressObjectives = 0;
//        int notStartedObjectives = 0;
//
//        for (Long objectiveId : objectivesIds) {
//            String url = OBJECTIVE_URL + "progress/" + objectiveId;
//
//            try {
//                ResponseEntity<Double> currProgress = restTemplate.getForEntity(url, Double.class);
//                if (currProgress.getBody() == null) {
//                    notStartedObjectives++;
//                } else {
//                    double progress = currProgress.getBody();
//                    if (progress == 0.0) {
//                        notStartedObjectives++;
//                    } else if (progress == 100.0) {
//                        completedObjectives++;
//                    } else {
//                        inProgressObjectives++;
//                    }
//                }
//            } catch (Exception e) {
//                notStartedObjectives++;
//            }
//        }
//
//        mapInfo.put("completedObjectives", completedObjectives);
//        mapInfo.put("inProgressObjectives", inProgressObjectives);
//        mapInfo.put("notStartedObjectives", notStartedObjectives);
//
//        return mapInfo;
//    }
public Map<String, Integer> getObjectivesInfoByProject(Long projectId) {
    Map<String, Integer> mapInfo = new HashMap<>();

    // Fetch objectives related to the project
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


    public Map<String, Integer> getTaskInfoForProject(Long projectId) {
        // URL to call the TaskController's endpoint
        String url =  TASK_SERVICE_URL + "project/task-info/{projectId}";

        try {
            // Make GET request to TaskController's endpoint and get response
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null, // No request body needed
                    Map.class, // Response is of type Map (task info)
                    projectId // Pass the projectId as a path variable
            );

            return response.getBody(); // Return the task info (map of task count data)
        } catch (Exception e) {
            // Handle errors gracefully (e.g., TaskController is down, network issues)
            return Collections.emptyMap(); // Return an empty map if there's an error
        }
    }

    public List<TeamDetailsDTO> getProjectTeamsDetails(Long projectId) {
        // Fetch the project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Long> teamIds = project.getTeamsInvolvedId(); // Get team IDs

        List<TeamDetailsDTO> teamDetailsList = new ArrayList<>();

        for (Long teamId : teamIds) {
            TeamDTO team = restTemplate.getForObject(TEAM_SERVICE_URL + teamId, TeamDTO.class);

            Long teamLeadId = team.getTeamLead();
            UserDTO teamLeader = restTemplate.getForObject(USER_SERVICE_URL + teamLeadId, UserDTO.class);

            // Create DTO response
            TeamDetailsDTO teamDetails = new TeamDetailsDTO();
            teamDetails.setTeamName(team.getTeamName());
            teamDetails.setTeamLeaderName(teamLeader.getUserName());
            teamDetails.setTeamLeaderProfile(teamLeader.getUserProfilePhoto());

            teamDetailsList.add(teamDetails);
        }

        return teamDetailsList;
    }

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




}