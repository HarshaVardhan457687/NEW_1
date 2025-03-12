package com.project.controller;

import com.project.DTO.ProjectDTO;
import com.project.DTO.SelectTeamDTO;
import com.project.DTO.TeamDetailsDTO;
import com.project.DTO.TeamResponseDTO;
import com.project.constants.ProjectStatus;
import com.project.model.KeyResult;
import com.project.model.Project;
import com.project.model.Objective;
import com.project.services.ProjectServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceImpl projectService;


    /**
     * Endpoint to create a new project.
     *
     * @param project DTO containing project details for creation.
     * @return ResponseEntity containing the created Project object.
     */
    @PostMapping("/new")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    /**
     * Endpoint to retrieve all projects.
     *
     * @return ResponseEntity containing a list of all projects.
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Endpoint to retrieve a project by its ID.
     *
     * @param id The ID of the project to retrieve.
     * @return ResponseEntity containing the project if found, or a 404 Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to update an existing project.
     *
     * @param id The ID of the project to update.
     * @param projectDetails The new project details to update.
     * @return ResponseEntity containing the updated Project object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
        @PathVariable Long id,
        @RequestBody Project projectDetails
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDetails));
    }

    /**
     * Endpoint to partially update a project. This allows for updating only specific fields.
     *
     * @param id The ID of the project to update.
     * @param projectDetails The project details to update.
     * @return ResponseEntity containing the partially updated Project object.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Project> patchProject(
            @PathVariable Long id,
            @RequestBody Project projectDetails
    ) {
        return ResponseEntity.ok(projectService.patchProject(id, projectDetails));
    }

    /**
     * Endpoint to delete a project by its ID.
     *
     * @param id The ID of the project to delete.
     * @return ResponseEntity with status 200 OK upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to retrieve all objectives associated with a specific project.
     *
     * @param projectId The ID of the project to retrieve objectives for.
     * @return ResponseEntity containing a list of objectives for the given project.
     */
    @GetMapping("/objective/{projectId}")
    public ResponseEntity<List<Objective>> getAllObjective(@PathVariable Long projectId){
        List<Objective> allObjectiveOfProject = projectService.getAllObjectiveAssociatedWithProject(projectId);
        return ResponseEntity.ok(allObjectiveOfProject);
    }

    /**
     * Endpoint to get the count of active projects from a list of project IDs.
     *
     * @param projectIds A list of project IDs to check for active projects.
     * @return ResponseEntity containing the count of active projects.
     */
    @PostMapping("/active/count")
    public ResponseEntity<Long> getActiveProjectsCount(@RequestBody List<Long> projectIds) {
        long activeCount = projectService.getActiveProjectsCount(projectIds);
        return ResponseEntity.ok(activeCount);
    }

    /**
     * Endpoint to retrieve the name of a project by its ID.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing the name of the project.
     */
    @GetMapping("/getname")
    public ResponseEntity<String> getNameByProjectId(@RequestParam Long projectId){
        String projectName = projectService.getNameOfProjectById(projectId);
        return ResponseEntity.ok(projectName);
    }

    /**
     * Endpoint to retrieve all active projects from a list of project IDs.
     *
     * @param projectIds A list of project IDs to retrieve active projects.
     * @return ResponseEntity containing the list of active projects, or 204 if none found.
     */
    @PostMapping("/active")
    public ResponseEntity<List<Project>> getActiveProjects(@RequestBody List<Long> projectIds) {
        List<Project> allActiveProjects = projectService.getAllActiveProjects(projectIds);

        if (allActiveProjects.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 if no active projects found
        }
        return ResponseEntity.ok(allActiveProjects);
    }

    /**
     * Endpoint to retrieve all projects from a list of project IDs.
     *
     * @param projectIds A list of project IDs.
     * @return ResponseEntity containing the list of projects, or 204 if none found.
     */
    @PostMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(@RequestBody List<Long> projectIds){
        List<Project> allProjects = projectService.getAllProjects(projectIds);

        if (allProjects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allProjects);
    }

    /**
     * Endpoint to retrieve detailed information about objectives for a specific project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing a map of objectives information.
     */
    @PostMapping("/objectives-info/{projectId}")
    public ResponseEntity<Map<String, Integer>> getObjectivesInfo(@PathVariable Long projectId) {
        Map<String, Integer> objectiveInfo = projectService.getObjectivesInfoByProject(projectId);
        return ResponseEntity.ok(objectiveInfo);
    }

    /**
     * Endpoint to retrieve task-related information for a specific project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing task information as a map.
     */
    @GetMapping("/task-info/{projectId}")
    public ResponseEntity<Map<String, Integer>> getTaskInfoForProject(@PathVariable Long projectId) {
        Map<String, Integer> taskInfo = projectService.getTaskInfoForProject(projectId);
        return ResponseEntity.ok(taskInfo);
    }

    /**
     * Endpoint to retrieve detailed team information for a specific project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing a list of team details for the project.
     */
    @GetMapping("/{projectId}/teams-details")
    public ResponseEntity<List<TeamDetailsDTO>> getProjectTeamsDetails(@PathVariable Long projectId) {
        List<TeamDetailsDTO> teamDetailsList = projectService.getProjectTeamsDetails(projectId);
        return ResponseEntity.ok(teamDetailsList);
    }

    /**
     * Endpoint to retrieve a list of teams that can be selected for a specific project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing a list of teams that can be selected for the project.
     */
    @GetMapping("/teams-select")
    public ResponseEntity<List<SelectTeamDTO>> getProjectTeamsSelect(@RequestParam Long projectId) {
        List<SelectTeamDTO> teamDetailsList = projectService.getProjectTeamsSelection(projectId);
        return ResponseEntity.ok(teamDetailsList);
    }

    /**
     * Endpoint to retrieve all teams associated with a specific project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing a list of teams for the project.
     */
    @GetMapping("/teams")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsForProject(@RequestParam Long projectId) {
        List<TeamResponseDTO> teams = projectService.getTeamsForProject(projectId);
        return ResponseEntity.ok(teams);
    }

    /**
     * Endpoint to retrieve a specific team associated with a project by team ID.
     *
     * @param projectId The ID of the project.
     * @param teamId The ID of the team.
     * @return ResponseEntity containing the team details.
     */
    @GetMapping("/team")
    public ResponseEntity<TeamResponseDTO> getProjectTeam(@RequestParam Long projectId, @RequestParam Long teamId){
        TeamResponseDTO team = projectService.getTeamForProject(projectId, teamId);
        return ResponseEntity.ok(team);
    }

    /**
     * Endpoint to retrieve all key results associated with a project’s objectives.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing a list of key results for the project.
     */
    @GetMapping("/keyresults")
    public ResponseEntity<List<KeyResult>> getKeyResultsByObjectives(@RequestParam Long projectId){
        List<KeyResult> keyResultsList = projectService.getAllKeyResult(projectId);
        return ResponseEntity.ok(keyResultsList);
    }

    /**
     * Endpoint to update project teams by adding a new team to a project.
     *
     * @param request A map containing the projectId and teamId to be added.
     * @return ResponseEntity with a success message.
     */
    @PatchMapping("/update-teams")
    public ResponseEntity<String> updateProjectTeams(@RequestBody Map<String, Object> request) {
        Long projectId = ((Number) request.get("projectId")).longValue();
        Long teamId = ((Number) request.get("teamId")).longValue();

        projectService.addTeamToProject(projectId, teamId);
        return ResponseEntity.ok("Updated Project with new Team");
    }

    /**
     * Endpoint to add objectives to a project.
     *
     * @param projectId The ID of the project to add objectives to.
     * @param request A list of objective IDs to add.
     * @return ResponseEntity with success or error message.
     */
    @PatchMapping("/{projectId}/add-objective")
    public ResponseEntity<String> addObjectiveToProject(@PathVariable Long projectId, @RequestBody List<Long> request) {
        String response = projectService.addObjectiveToProject(projectId, request);

        if ("Project not found".equals(response)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve the total count and completed count of key results for a specific project.
     *
     * @param projectId The ID of the project.
     * @return A map containing the total key result count and completed key result count.
     */
    @GetMapping("/keyresults/count")
    public Map<String, Integer> getKeyResultsCounts(@RequestParam Long projectId) {
        return projectService.getKeyResultsCounts(projectId);
    }

}