package com.key_result.key_result_service.controller;

import com.key_result.key_result_service.dto.KeyResultUnitDTO;
import com.key_result.key_result_service.entity.KeyResult;
import com.key_result.key_result_service.service.KeyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/keyresults")
public class KeyResultController {

    private final KeyResultService keyResultService;

    @Autowired
    public KeyResultController(KeyResultService keyResultService) {
        this.keyResultService = keyResultService;
    }

    /**
     * Create a new Key Result.
     *
     * @param keyResult the Key Result to add
     * @return the created Key Result
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KeyResult addKeyResult(@RequestBody KeyResult keyResult) {
        return keyResultService.addKeyResult(keyResult);
    }

    /**
     * Retrieve all Key Results.
     *
     * @return a list of all Key Results
     */
    @GetMapping
    public List<KeyResult> getAllKeyResults() {
        return keyResultService.getAllKeyResult();
    }

    /**
     * Retrieve a Key Result by its ID.
     *
     * @param id the ID of the Key Result
     * @return the Key Result with the specified ID
     */
    @GetMapping("/{id}")
    public KeyResult getKeyResultById(@PathVariable Long id) {
        return keyResultService.getKeyResult(id);
    }

    /**
     * Update an existing Key Result by its ID.
     *
     * @param id        the ID of the Key Result to update
     * @param keyResult the updated Key Result details
     * @return the updated Key Result
     */
    @PutMapping("/{id}")
    public KeyResult updateKeyResult(@PathVariable Long id, @RequestBody KeyResult keyResult) {
        return keyResultService.updateKeyResult(id, keyResult);
    }


    @PatchMapping("/currentVal/{keyResultId}")
    public ResponseEntity<KeyResult> updateKeyResultCurrentVal(
            @PathVariable Long keyResultId,
            @RequestParam int currentVal) {
        KeyResult updatedKeyResult = keyResultService.updateKeyResultCurrentVal(keyResultId, currentVal);
        return ResponseEntity.ok(updatedKeyResult);
    }


    /**
     * Delete a Key Result by its ID.
     *
     * @param id the ID of the Key Result to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeKeyResult(@PathVariable Long id) {
        keyResultService.removeKeyResult(id);
    }

    /**
     * Retrieve all Key Results associated with a specific Objective ID.
     *
     * @param objectiveId the Objective ID
     * @return a list of Key Results associated with the Objective ID
     */
    @GetMapping("/objective/{objectiveId}")
    public ResponseEntity<List<KeyResult>> getKeyResultsByObjectiveId(@PathVariable Long objectiveId) {
        List<KeyResult> keyResults = keyResultService.getKeyResultsByObjectiveId(objectiveId);

        if (keyResults.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // Return 200 OK with an empty list
        }

        return ResponseEntity.ok(keyResults);
    }


    /**
     *Retrive all the all keyResults and active keyResults
     *
     * @param objectiveIds list of the objectives
     * @return list of the keyResults
     */
    @PostMapping("/by-objectives")
    public ResponseEntity<Map<String, List<KeyResult>>> getKeyResultsByObjectiveIds(@RequestBody List<Long> objectiveIds) {
        Map<String, List<KeyResult>> keyResults = keyResultService.getKeyResultsByObjectiveIds(objectiveIds);
        return ResponseEntity.ok(keyResults);
    }

    @PostMapping("/all/by-objectives")
    public ResponseEntity<List<KeyResult>> getALLKeyResultsByObjectiveIds(@RequestBody List<Long> objectiveIds) {
        List<KeyResult> keyResults = keyResultService.getALLKeyResultsByObjectiveIds(objectiveIds);
        return ResponseEntity.ok(keyResults);
    }

    @PostMapping("/completed-count")
    public ResponseEntity<Long> getCompletedKeyResults(@RequestBody List<Long> keyResultIds) {
        Long completedCount = keyResultService.getCompletedKeyResults(keyResultIds);
        return ResponseEntity.ok(completedCount);
    }

    @GetMapping("/progress/{keyresultId}")
    public ResponseEntity<Float> getKeyResultProgress(@PathVariable Long keyresultId){
        float progress = keyResultService.getProgressOfKeyResult(keyresultId);
        return ResponseEntity.ok(progress);
    }

    /**
     * Endpoint to add a task to a key result.
     *
     * @param keyResultId The ID of the key result.
     * @param taskId      The ID of the task to be added.
     * @return HTTP response indicating success or failure of the operation.
     */
    @PatchMapping("/{keyResultId}/add-task")
    public ResponseEntity<String> addTaskToKeyResult(@PathVariable Long keyResultId, @RequestBody Long taskId) {
        boolean taskAdded = keyResultService.addTaskToKeyResult(keyResultId, taskId);
        if (taskAdded) {
            return ResponseEntity.ok("Task added to KeyResult successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KeyResult not found");
    }

    @GetMapping("/get-progress/{taskId}")
    public ResponseEntity<KeyResultUnitDTO> getKeyResultUnit(@PathVariable Long taskId) {
        KeyResultUnitDTO keyResultUnitDTO = keyResultService.getKeyResultUnitById(taskId);
        return ResponseEntity.ok(keyResultUnitDTO);
    }
}

