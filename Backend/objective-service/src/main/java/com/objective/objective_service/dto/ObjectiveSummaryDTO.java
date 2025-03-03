package com.objective.objective_service.dto;

import com.objective.objective_service.entity.Objective;

import java.util.List;

public class ObjectiveSummaryDTO {
    private Objective objective;
    private List<KeyResultSummaryDto> keyResults;

    public ObjectiveSummaryDTO(Objective objective, List<KeyResultSummaryDto> keyResults) {
        this.objective = objective;
        this.keyResults = keyResults;
    }

    // Getters and Setters

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public List<KeyResultSummaryDto> getKeyResults() {
        return keyResults;
    }

    public void setKeyResults(List<KeyResultSummaryDto> keyResults) {
        this.keyResults = keyResults;
    }
}
