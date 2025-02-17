package com.project.services;

import com.project.constants.ProjectStatus;
import com.project.model.Objective;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final String OBJECTIVE_URL = "http://localhost:8081/api/objective/project/";

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
        project.setProjectDueDate(projectDetails.getProjectDueDate());
        project.setProjectManagerId(projectDetails.getProjectManagerId());

        return projectRepository.save(project);
    }

    // Delete Project
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Dashboard Methods
    public long getCompletedProjectsCount() {
        return projectRepository.countCompletedProjects();
    }

    // Additional query methods
    public long getProjectCountByStatus(ProjectStatus status) {
        return projectRepository.countByProjectStatus(status);
    }

    public long getProjectCountByActiveStatus(Boolean isActive) {
        return projectRepository.countByIsActive(isActive);
    }


    //get all the associated objective of that project
    public List<Objective> getAllObjectiveAssociatedWithProject(Long projectId){
        String url = OBJECTIVE_URL + projectId;
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
    
}