

package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.constants.ProjectStatus;
import com.project.model.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom query to count completed projects
    @Query("SELECT COUNT(p) FROM Project p WHERE p.projectStatus = 'COMPLETED'")
    long countCompletedProjects();

    // Additional query methods can be added here
    long countByProjectStatus(ProjectStatus status);
    long countByIsActive(Boolean isActive);
}

































































//package com.project.repository;
//
//import com.project.model.Project;
//import com.project.model.Project.ProjectPriority;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Date;
//import java.util.List;
//
//@Repository
//public interface ProjectRepository extends JpaRepository<Project, Long> {
//
//    @Query("SELECT p FROM Project p WHERE p.projectDueDate BETWEEN :startDate AND :endDate")
//    List<Project> findProjectsDueBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
//
//    @Query("SELECT p FROM Project p WHERE " +
//            "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(p.projectDescription) LIKE LOWER(CONCAT('%', :query, '%'))")
//    List<Project> searchProjects(@Param("query") String query);
//
//    @Query("SELECT p FROM Project p WHERE p.projectPriority = :priority")
//    List<Project> findByProjectPriority(@Param("priority") Project.ProjectPriority priority);
//
//    @Query("SELECT COUNT(p) FROM Project p WHERE p.projectStatus = 'COMPLETED'")
//    Long countCompletedProjects();
//
//	Page<Project> findByProjectPriority(PageRequest of, ProjectPriority valueOf);
//}
