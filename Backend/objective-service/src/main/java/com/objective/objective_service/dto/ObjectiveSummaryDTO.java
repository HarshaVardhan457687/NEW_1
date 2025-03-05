package com.objective.objective_service.dto;

import com.objective.objective_service.constants.ObjectivePriority;
import com.objective.objective_service.constants.ObjectiveStatus;
import com.objective.objective_service.entity.Objective;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveSummaryDTO {
    private String objectiveName;
    private ObjectiveStatus objectiveStatus;
    private double objectiveProgress;
    private List<KeyResultSummaryDto> keyResults;

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public ObjectiveStatus getObjectiveStatus() {
        return objectiveStatus;
    }

    public void setObjectiveStatus(ObjectiveStatus objectiveStatus) {
        this.objectiveStatus = objectiveStatus;
    }

    public double getObjectiveProgress() {
        return objectiveProgress;
    }

    public void setObjectiveProgress(double objectiveProgress) {
        this.objectiveProgress = objectiveProgress;
    }

    public List<KeyResultSummaryDto> getKeyResults() {
        return keyResults;
    }

    public void setKeyResults(List<KeyResultSummaryDto> keyResults) {
        this.keyResults = keyResults;
    }
}
