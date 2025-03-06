package com.project.services;

import com.project.DTO.TeamDetailsDTO;
import com.project.DTO.TeamResponseDTO;
import com.project.model.KeyResult;
import com.project.model.Objective;
import com.project.model.Project;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    public Project createProject(Project project);
    public List<Project> getAllProjects();
    public Optional<Project> getProjectById(Long id);
    public Project updateProject(Long id, Project projectDetails);
    public Project patchProject(Long id, Project projectDetails);
    public void deleteProject(Long id);
    public List<Objective> getAllObjectiveAssociatedWithProject(Long projectId);
    public long getActiveProjectsCount(List<Long> projectIds);
    public String getNameOfProjectById(Long projectId);
    public List<Project> getAllActiveProjects(List<Long> projectIds);
    public List<Project> getAllProjects(List<Long> projectIds);
    public Map<String, Integer> getObjectivesInfoByProject(Long projectId);
    public Map<String, Integer> getTaskInfoForProject(Long projectId);
    public List<TeamDetailsDTO> getProjectTeamsDetails(Long projectId);
    public List<TeamResponseDTO> getTeamsForProject(Long projectId);
    public TeamResponseDTO getTeamForProject(Long projectId, Long teamId);
    public List<KeyResult> getAllKeyResult(Long projectId);
    public void addTeamToProject(Long projectId, Long teamId);
    public String addObjectiveToProject(Long projectId, List<Long> request);
}
