package com.project.repository;

import com.project.model.TimeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLineRepository extends JpaRepository<TimeLine, Long> {
    public List<TimeLine> findAllByTimeLineAssociatedProject(Long projectId);

}
