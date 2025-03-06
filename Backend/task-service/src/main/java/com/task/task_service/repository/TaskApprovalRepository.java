package com.task.task_service.repository;

import com.task.task_service.entity.TaskApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskApprovalRepository extends JpaRepository<TaskApproval, Long> {
}
