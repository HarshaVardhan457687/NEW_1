package com.project.services;

import com.project.constants.ProjectStatus;
import com.project.model.Objective;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProjectService {

    private static final String OBJECTIVE_URL = "http://localhost:8081/api/objective/";

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
        return projectRepository.findById(id);
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

    public Map<String, Integer> getObjectivesInfo(List<Long> objectivesIds) {
        Map<String, Integer> mapInfo = new HashMap<>();
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



}