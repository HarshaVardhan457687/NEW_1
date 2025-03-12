package com.task.task_service.repository;

import com.task.task_service.entity.TaskApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskApprovalRepository extends JpaRepository<TaskApproval, Long> {
    List<TaskApproval> findByProjectId(Long projectId);
    List<TaskApproval> findByTeamId(Long teamId);

    @Query("SELECT EXTRACT(YEAR FROM t.approvedDate), EXTRACT(MONTH FROM t.approvedDate), COUNT(t.approvalId) " +
            "FROM TaskApproval t " +
            "WHERE t.projectId = :projectId AND t.status = 'APPROVED' " +
            "GROUP BY EXTRACT(YEAR FROM t.approvedDate), EXTRACT(MONTH FROM t.approvedDate)")
    List<Object[]> countApprovedTasksByMonth(@Param("projectId") Long projectId);
}
