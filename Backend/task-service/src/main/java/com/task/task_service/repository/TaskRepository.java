package com.task.task_service.repository;

import com.task.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskAssociatedProjectInAndTaskOwner(List<Long> projectIds, Long taskOwner);
    public List<Task> findAllByTaskAssociatedKeyResult(Long keyresultId);
    public List<Task> findAllByTaskOwner(Long userId);
}
