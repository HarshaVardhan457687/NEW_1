package com.task.task_service.repository;

import com.task.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskAssociatedProjectInAndTaskOwner(List<Long> projectIds, Long taskOwner);
    List<Task> findAllByTaskAssociatedKeyResult(Long keyresultId);
    List<Task> findAllByTaskOwner(Long userId);
    List<Task> findByTaskIdInAndTaskAssociatedProjectIn(List<Long> taskIds, List<Long> projectIds);
    List<Task> findByTaskAssociatedProject(Long projectId);
    List<Task> findByTaskAssociatedProjectAndTaskIsActive(Long projectId, boolean taskIsActive);
    List<Task> findByTaskAssociatedProjectAndTaskOwnerAndTaskIsActive(Long projectId, Long userId, boolean isActive);
}

