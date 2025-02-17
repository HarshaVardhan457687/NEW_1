package com.key_result.key_result_service.service;

import com.key_result.key_result_service.entity.KeyResult;
import com.key_result.key_result_service.entity.Task;
import com.key_result.key_result_service.exception.ResourceNotFoundException;
import com.key_result.key_result_service.repository.KeyResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class KeyResultServiceImpl implements KeyResultService {

    // Logger instance for logging important events and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyResultServiceImpl.class);

    // Injecting the KeyResultRepository to interact with the database
    private final KeyResultRepository keyResultRepository;

    // Defining the TASK_SERVICE_URL to interact with Task Service via HTTP
    private final static String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";

    // Constructor-based dependency injection for KeyResultRepository
    @Autowired
    public KeyResultServiceImpl(KeyResultRepository keyResultRepository) {
        this.keyResultRepository = keyResultRepository;
    }

    // Autowiring RestTemplate to make HTTP requests to other services (like Task Service)
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Adds a new KeyResult to the database.
     * Logs the operation and returns the saved KeyResult instance.
     *
     * @param keyResult The KeyResult object to be added.
     * @return The saved KeyResult object.
     */
    @Override
    public KeyResult addKeyResult(KeyResult keyResult) {
        LOGGER.info("Adding a new KeyResult: {}", keyResult);
        return keyResultRepository.save(keyResult);
    }

    /**
     * Retrieves all KeyResults from the database.
     * Logs the retrieval process.
     *
     * @return A list of all KeyResults.
     */
    @Override
    public List<KeyResult> getAllKeyResult() {
        LOGGER.info("Fetching all KeyResults from the database.");
        List<KeyResult> keyResults = keyResultRepository.findAll();

        for (KeyResult keyResult : keyResults) {
            Long keyResultId = keyResult.getKeyResultId();

            try {
                // Fetch Tasks from Task service using the objective ID
                String taskUrl = TASK_SERVICE_URL + "keyresult/" + keyResultId;
                List<Task> tasks = Optional.ofNullable(
                        restTemplate.getForObject(taskUrl, Task[].class)
                ).map(Arrays::asList).orElse(Collections.emptyList());
                keyResult.setKeyResultAssociatedTasks(tasks);

            } catch (RestClientException e) {
                // Log error if there's an issue while fetching data from external services
                LOGGER.error("Error fetching data for Objective ID: " + keyResultId + " - " + e.getMessage());

            }
        }

        return keyResults;
    }

    /**
     * Retrieves a specific KeyResult by its ID.
     * Logs the retrieval process and throws an exception if not found.
     *
     * @param id The ID of the KeyResult to be retrieved.
     * @return The retrieved KeyResult.
     */
    @Override
    public KeyResult getKeyResult(Long id) {
        LOGGER.info("Fetching KeyResult with ID: {}", id);

        KeyResult keyResult = keyResultRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("KeyResult not found with ID: {}", id);
                    return new ResourceNotFoundException("KeyResult not found with id: " + id);
                });

        try {
            // Fetch Tasks from Task service
            String taskUrl = TASK_SERVICE_URL + "keyresult/" + id;
            List<Task> tasks = Optional.ofNullable(
                    restTemplate.getForObject(taskUrl, Task[].class)
            ).map(Arrays::asList).orElse(Collections.emptyList());

            keyResult.setKeyResultAssociatedTasks(tasks);

        } catch (RestClientException e) {
            LOGGER.error("Error fetching tasks for KeyResult ID {}: {}", id, e.getMessage(), e);
        }

        return keyResult;
    }


    /**
     * Retrieves all KeyResults associated with a given Objective ID.
     * Logs the retrieval process and throws an exception if none are found.
     *
     * @param objectiveId The Objective ID to filter KeyResults.
     * @return A list of KeyResults linked to the given Objective ID.
     */
    @Override
    public List<KeyResult> getKeyResultsByObjectiveId(Long objectiveId) {
        LOGGER.info("Fetching KeyResults for Objective ID: {}", objectiveId);
        List<KeyResult> keyResults = keyResultRepository.findKeyResultByAssociatedObjectiveId(objectiveId);
        if (keyResults.isEmpty()) {
            LOGGER.warn("No KeyResults found for Objective ID: {}", objectiveId);
            throw new ResourceNotFoundException("No KeyResults found for Objective ID: " + objectiveId);
        }
        return keyResults;
    }

    /**
     * Updates an existing KeyResult with new data.
     * Logs the update process and throws an exception if the KeyResult is not found.
     *
     * @param id The ID of the KeyResult to be updated.
     * @param updatedKeyResult The updated KeyResult object containing new values.
     * @return The updated KeyResult.
     */
    @Override
    public KeyResult updateKeyResult(Long id, KeyResult updatedKeyResult) {
        LOGGER.info("Updating KeyResult with ID: {}", id);
        return keyResultRepository.findById(id)
                .map(existingKeyResult -> {
                    LOGGER.info("Existing KeyResult found. Updating fields...");
                    existingKeyResult.setKeyResultName(updatedKeyResult.getKeyResultName());
                    existingKeyResult.setKeyResultOwnerId(updatedKeyResult.getKeyResultOwnerId());
                    existingKeyResult.setAssociatedObjectiveId(updatedKeyResult.getAssociatedObjectiveId());
                    existingKeyResult.setCurrentVal(updatedKeyResult.getCurrentVal());
                    existingKeyResult.setTargetVal(updatedKeyResult.getTargetVal());
                    existingKeyResult.setKeyResultPriority(updatedKeyResult.getKeyResultPriority());
                    existingKeyResult.setKeyResultAssociatedTasksId(updatedKeyResult.getKeyResultAssociatedTasksId());
                    existingKeyResult.setKeyResultAssociatedTasks(updatedKeyResult.getKeyResultAssociatedTasks());
                    existingKeyResult.setKeyResultDueDate(updatedKeyResult.getKeyResultDueDate());
                    existingKeyResult.setTeamId(updatedKeyResult.getTeamId());

                    LOGGER.info("Saving updated KeyResult...");
                    return keyResultRepository.save(existingKeyResult);
                })
                .orElseThrow(() -> {
                    LOGGER.error("KeyResult not found with ID: {}", id);
                    return new ResourceNotFoundException("KeyResult not found with id: " + id);
                });

    }

    /**
     * Removes a KeyResult by its ID.
     * Logs the deletion process and throws an exception if the KeyResult is not found.
     *
     * @param id The ID of the KeyResult to be deleted.
     */
    @Override
    public void removeKeyResult(Long id) {
        LOGGER.info("Attempting to delete KeyResult with ID: {}", id);
        if (!keyResultRepository.existsById(id)) {
            LOGGER.error("KeyResult not found with ID: {}. Deletion aborted.", id);
            throw new ResourceNotFoundException("KeyResult not found with id: " + id);
        }
        keyResultRepository.deleteById(id);
        LOGGER.info("Successfully deleted KeyResult with ID: {}", id);
    }

    /**
     * Give all the keyResults by list of objectiveIds
     * @param objectiveIds The Ids of the objectives to be fetch.
     * @return List of KeyResults
     */
    @Override
    public Map<String, List<KeyResult>> getKeyResultsByObjectiveIds(List<Long> objectiveIds) {
        // Fetch all key results
        List<KeyResult> allKeyResults = keyResultRepository.findByObjectiveIdIn(objectiveIds);

        // Filter active key results (currVal < 100%)
        List<KeyResult> activeKeyResults = allKeyResults.stream()
                .filter(kr -> kr.getCurrentVal() < 100.0)
                .toList();

        // Return both lists in a map
        Map<String, List<KeyResult>> result = new HashMap<>();
        result.put("allKeyResults", allKeyResults);
        result.put("activeKeyResults", activeKeyResults);

        return result;
    }

}




