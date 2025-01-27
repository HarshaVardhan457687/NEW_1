package com.key_result.key_result_service.service;

import com.key_result.key_result_service.entity.KeyResult;

import java.util.List;

public interface KeyResultService {

    public KeyResult addKeyResult(KeyResult keyResult);
    public List<KeyResult> getAllKeyResult();
    public KeyResult getKeyResult(Long id);
    public List<KeyResult> getKeyResultsByObjectiveId(Long objectiveId);
    public KeyResult updateKeyResult(Long id, KeyResult toUpdateKeyResult);
    public void removeKeyResult(Long id);
}
