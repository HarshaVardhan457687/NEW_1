package com.key_result.key_result_service.service;

import com.key_result.key_result_service.entity.KeyResult;
import com.key_result.key_result_service.entity.Task;
import com.key_result.key_result_service.exception.ResourceNotFoundException;
import com.key_result.key_result_service.repository.KeyResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class KeyResultServiceImpl implements KeyResultService {

    private final KeyResultRepository keyResultRepository;
    private final static String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";

    @Autowired
    public KeyResultServiceImpl(KeyResultRepository keyResultRepository) {
        this.keyResultRepository = keyResultRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public KeyResult addKeyResult(KeyResult keyResult) {
        return keyResultRepository.save(keyResult);
    }

    @Override
    public List<KeyResult> getAllKeyResult() {
        return keyResultRepository.findAll();
    }

    @Override
    public KeyResult getKeyResult(Long id) {
        return keyResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KeyResult not found with id: " + id));
    }

    @Override
    public List<KeyResult> getKeyResultsByObjectiveId(Long objectiveId) {
        List<KeyResult> keyResults = keyResultRepository.findKeyResultByAssociatedObjectiveId(objectiveId);
        if (keyResults.isEmpty()) {
            throw new ResourceNotFoundException("No KeyResults found for Objective ID: " + objectiveId);
        }
        return keyResults;
    }

    @Override
    public KeyResult updateKeyResult(Long id, KeyResult updatedKeyResult) {
        return keyResultRepository.findById(id)
                .map(existingKeyResult -> {
                    existingKeyResult.setKeyResultName(updatedKeyResult.getKeyResultName());
                    existingKeyResult.setKeyResultOwnerId(updatedKeyResult.getKeyResultOwnerId());
                    existingKeyResult.setAssociatedObjectiveId(updatedKeyResult.getAssociatedObjectiveId());
                    existingKeyResult.setKeyResultAssociatedTasksId(updatedKeyResult.getKeyResultAssociatedTasksId());
                    existingKeyResult.setKeyResultAssociatedTasks(updatedKeyResult.getKeyResultAssociatedTasks());
                    existingKeyResult.setKeyResultDueDate(updatedKeyResult.getKeyResultDueDate());
                    return keyResultRepository.save(existingKeyResult);
                })
                .orElseThrow(() -> new ResourceNotFoundException("KeyResult not found with id: " + id));
    }

    @Override
    public void removeKeyResult(Long id) {
        if (!keyResultRepository.existsById(id)) {
            throw new ResourceNotFoundException("KeyResult not found with id: " + id);
        }
        keyResultRepository.deleteById(id);
    }


}

