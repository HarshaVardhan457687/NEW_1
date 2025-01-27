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

@RestController
@RequestMapping("/api/objective")
public class ObjectiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectiveController.class);

    @Autowired
    private ObjectiveService objectiveService;

    // Create a new objective
    @PostMapping
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective) {
        LOGGER.info("Received request to create objective: {}", objective);
        Objective createdObjective = objectiveService.createObjective(objective);
        return new ResponseEntity<>(createdObjective, HttpStatus.CREATED); // 201 Created
    }

    // Get all objectives
    @GetMapping
    public ResponseEntity<List<Objective>> getAllObjective() {
        LOGGER.info("Fetching all objectives...");
        List<Objective> objectives = objectiveService.getAllObjective();
        return ResponseEntity.ok(objectives); // 200 OK
    }

    // Get objective by ID
    @GetMapping("/{id}")
    public ResponseEntity<Objective> getObjectiveById(@PathVariable Long id) {
        LOGGER.info("Fetching objective with ID: {}", id);
        Objective objective = objectiveService.getObjective(id);
        return ResponseEntity.ok(objective); // 200 OK
    }

    // Update objective
    @PutMapping("/{id}")
    public ResponseEntity<Objective> updateObjective(@PathVariable Long id, @RequestBody Objective objectiveToUpdate) {
        LOGGER.info("Updating objective with ID: {}", id);
        Objective updatedObjective = objectiveService.updateObjective(objectiveToUpdate, id);
        return ResponseEntity.ok(updatedObjective); // 200 OK
    }

    // Remove objective
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeObjective(@PathVariable Long id) {
        LOGGER.info("Removing objective with ID: {}", id);
        objectiveService.removeObjective(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Objective>> getAllObjectiveByProjectId(@PathVariable Long projectId){
        LOGGER.info("Fetching objective with project associated with projectID: {}", projectId);
        List<Objective> objectivesWithProjectId = objectiveService.getAllObjectiveByProjectId(projectId);
        return ResponseEntity.ok(objectivesWithProjectId);
    }

    @GetMapping("/project/progress/{projectId}")
    public ResponseEntity<Double> getProjectProgress(@PathVariable Long projectId){
        LOGGER.info("Fetching objective with project associated with projectID: {}", projectId);
        Double projectProgress = objectiveService.getProgress(projectId);
        return ResponseEntity.ok(projectProgress);
    }
}
