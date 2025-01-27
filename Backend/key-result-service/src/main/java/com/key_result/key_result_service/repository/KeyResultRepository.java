package com.key_result.key_result_service.repository;

import com.key_result.key_result_service.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    List<KeyResult> findKeyResultByAssociatedObjectiveId(Long objectiveId);
}
