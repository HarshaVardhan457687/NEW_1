package com.objective.objective_service.repository;

import com.objective.objective_service.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    List<Objective> findByMappedProject(Long projectId);
    // Get all objectives for the given list of projects
    List<Objective> findByMappedProjectIn(List<Long> projectIds);
    // Get only active objectives for the given list of projects
    List<Objective> findByMappedProjectInAndObjectiveIsActiveTrue(List<Long> projectIds);
}
