package com.objective.objective_service.service;

import com.objective.objective_service.dto.ObjectiveSummaryDTO;
import com.objective.objective_service.entity.Objective;

import java.util.List;
import java.util.Map;

public interface ObjectiveService {
    public List<Objective> getAllObjective();
    public Objective getObjective(Long objectiveId);
    public Objective createObjective(Objective obj);
    public Objective updateObjective(Objective objectiveToUpdate, Long objectiveId);
    public void removeObjective(Long objectiveId);
    public List<ObjectiveSummaryDTO> getAllObjectiveByProjectId(Long projectId);
    public Map<String, List<Objective>> getObjectivesByProjects(List<Long> projectIds);
    public List<Objective> getAllObjectiveByProjectIds(List<Long> projectIds);
    public double calculateObjectiveProgress(Long objectiveId);
    public double calculateProjectProgress(Long projectId);
    public String addKeyResultToObjective(Long objectiveId, List<Long> request);
    public Map<String, Integer> calculateObjectivePerformance(Long projectId);
}
