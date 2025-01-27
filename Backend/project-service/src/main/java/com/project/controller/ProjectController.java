package com.project.controller;

import com.project.constants.ProjectStatus;
import com.project.model.Objective;
import com.project.model.Project;
import com.project.services.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/new")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
        @PathVariable Long id,
        @RequestBody Project projectDetails
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard/completed")
    public ResponseEntity<Long> getCompletedProjectsCount() {
        return ResponseEntity.ok(projectService.getCompletedProjectsCount());
    }

    @GetMapping("/dashboard/status/{status}")
    public ResponseEntity<Long> getProjectCountByStatus(@PathVariable ProjectStatus status) {
        return ResponseEntity.ok(projectService.getProjectCountByStatus(status));
    }

    @GetMapping("/dashboard/active/{isActive}")
    public ResponseEntity<Long> getProjectCountByActiveStatus(@PathVariable Boolean isActive) {
        return ResponseEntity.ok(projectService.getProjectCountByActiveStatus(isActive));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(
        @RequestParam(required = false) String projectName,
        @RequestParam(required = false) ProjectStatus projectStatus,
        @RequestParam(required = false) Boolean isActive,
        @RequestParam(required = false) Long projectManager
    ) {
        List<Project> projects = projectService.getAllProjects().stream()
            .filter(p -> projectName == null || p.getProjectName().contains(projectName))
            .filter(p -> projectStatus == null || p.getProjectStatus() == projectStatus)
            .filter(p -> isActive == null || p.getIsActive().equals(isActive))
            .filter(p -> projectManager == null || p.getProjectManager().equals(projectManager))
            .toList();

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/objective/{projectId}")
    public ResponseEntity<List<Objective>> getAllObjective(@PathVariable Long projectId){
        List<Objective> allObjectiveOfProject = projectService.getAllObjectiveAssociatedWithProject(projectId);
        return ResponseEntity.ok(allObjectiveOfProject);
    }
}