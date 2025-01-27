package com.key_result.key_result_service.controller;

import com.key_result.key_result_service.entity.KeyResult;
import com.key_result.key_result_service.service.KeyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Retrieve all Key Results associated with a specific Objective ID.
     *
     * @param objectiveId the Objective ID
     * @return a list of Key Results associated with the Objective ID
     */
    @GetMapping("/objective/{objectiveId}")
    public List<KeyResult> getKeyResultsByObjectiveId(@PathVariable Long objectiveId) {
        return keyResultService.getKeyResultsByObjectiveId(objectiveId);
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
}

