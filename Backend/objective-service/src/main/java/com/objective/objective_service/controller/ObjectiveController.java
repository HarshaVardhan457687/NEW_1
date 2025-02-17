package com.objective.objective_service.controller;

import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController // Marks this class as a REST controller, enabling it to handle HTTP requests.
@RequestMapping("/api/objective") // Base URL mapping for all endpoints in this controller.
public class ObjectiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectiveController.class);
    // Logger for recording informational and debugging messages.

    @Autowired // Dependency injection for the ObjectiveService.
    private ObjectiveService objectiveService;

    /**
     * Endpoint to create a new objective.
     * @param objective - The objective data sent in the request body.
     * @return ResponseEntity containing the created objective and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective) {
        LOGGER.info("Received request to create objective: {}", objective);
        Objective createdObjective = objectiveService.createObjective(objective);
        return new ResponseEntity<>(createdObjective, HttpStatus.CREATED); // 201 Created
    }

    /**
     * Endpoint to fetch all objectives.
     * @return ResponseEntity containing the list of all objectives and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Objective>> getAllObjective() {
        LOGGER.info("Fetching all objectives...");
        List<Objective> objectives = objectiveService.getAllObjective();
        return ResponseEntity.ok(objectives); // 200 OK
    }

    /**
     * Endpoint to fetch an objective by its ID.
     * @param id - The ID of the objective to fetch.
     * @return ResponseEntity containing the fetched objective and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Objective> getObjectiveById(@PathVariable Long id) {
        LOGGER.info("Fetching objective with ID: {}", id);
        Objective objective = objectiveService.getObjective(id);
        return ResponseEntity.ok(objective); // 200 OK
    }

    /**
     * Endpoint to update an existing objective.
     * @param id - The ID of the objective to update.
     * @param objectiveToUpdate - The updated objective data.
     * @return ResponseEntity containing the updated objective and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Objective> updateObjective(@PathVariable Long id, @RequestBody Objective objectiveToUpdate) {
        LOGGER.info("Updating objective with ID: {}", id);
        Objective updatedObjective = objectiveService.updateObjective(objectiveToUpdate, id);
        return ResponseEntity.ok(updatedObjective); // 200 OK
    }

    /**
     * Endpoint to remove an objective by its ID.
     * @param id - The ID of the objective to remove.
     * @return ResponseEntity with HTTP status 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeObjective(@PathVariable Long id) {
        LOGGER.info("Removing objective with ID: {}", id);
        objectiveService.removeObjective(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Endpoint to fetch all objectives associated with a specific project ID.
     * @param projectId - The ID of the project.
     * @return ResponseEntity containing the list of objectives for the project and HTTP status 200 (OK).
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Objective>> getAllObjectiveByProjectId(@PathVariable Long projectId) {
        LOGGER.info("Fetching objective with project associated with projectID: {}", projectId);
        List<Objective> objectivesWithProjectId = objectiveService.getAllObjectiveByProjectId(projectId);
        return ResponseEntity.ok(objectivesWithProjectId);
    }

    /**
     * Endpoint to calculate and fetch the progress of a project based on its objectives.
     * @param projectId - The ID of the project.
     * @return ResponseEntity containing the project progress (percentage) and HTTP status 200 (OK).
     */
    @GetMapping("/project/progress/{projectId}")
    public ResponseEntity<Double> getProjectProgress(@PathVariable Long projectId) {
        LOGGER.info("Fetching objective with project associated with projectID: {}", projectId);
        Double projectProgress = objectiveService.getProgress(projectId);
        return ResponseEntity.ok(projectProgress);
    }

    /**
     * Endpoint to calculate and fetch the progress of a project based on its objectives.
     * @param projectId - The ID of the project.
     * @return ResponseEntity containing the project progress (percentage) and HTTP status 200 (OK).
     */
//    @GetMapping("/project/progress/{projectId}")
//    public ResponseEntity<Double> getActiveObjectiveByTaskId(@PathVariable List projectId) {
//        LOGGER.info("Fetching objective with project associated with projectID: {}", projectId);
//        Double projectProgress = objectiveService.getProgress(projectId);
//        return ResponseEntity.ok(projectProgress);
//    }

    /**
     * Endpoint to fetch all project and active projects based on its objectives.
     * @param projectIds - The IDs of the project.
     * @return ResponseEntity containing the all project and active project of givenId and HTTP status 200 (OK).
     */
    @PostMapping("/by-projectIds")
    public ResponseEntity<Map<String, List<Objective>>> getObjectivesByProjects(@RequestBody List<Long> projectIds) {
        Map<String, List<Objective>> response = objectiveService.getObjectivesByProjects(projectIds);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to fetch all projects based on its objectives.
     * @param projectIds - The IDs of the project.
     * @return ResponseEntity containing the all projects of givenId and HTTP status 200 (OK).
     */
    @PostMapping("/all/by-projects")
    public ResponseEntity<List<Objective>> getAllObjectivesByProjectIds(@RequestBody List<Long> projectIds) {
        List<Objective> response = objectiveService.getAllObjectiveByProjectIds(projectIds);
        return ResponseEntity.ok(response);
    }

}
