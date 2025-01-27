//package com.project.repository;
//
//import com.project.model.Project;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//class ProjectRepositoryTest {
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Test
//    void testFindProjectsDueBetween() {
//        Date now = new Date();
//        Date futureDate = new Date(System.currentTimeMillis() + 86400000L); // 1 day later
//
//        Project project = new Project();
//        project.setProjectName("Due Date Project");
//        project.setProjectDescription("Description");
//        project.setProjectPriority(Project.ProjectPriority.MEDIUM);
//        project.setProjectDueDate(futureDate);
//        project.setIsActive(true);
//
//        projectRepository.save(project);
//
//        List<Project> result = projectRepository.findProjectsDueBetween(now, futureDate);
//
//        assertEquals(1, result.size());
//        assertEquals("Due Date Project", result.get(0).getProjectName());
//    }
//
//    @Test
//    void testSearchProjects() {
//        Project project = new Project();
//        project.setProjectName("Searchable Project");
//        project.setProjectDescription("This project is for testing search");
//        project.setProjectPriority(Project.ProjectPriority.LOW);
//        project.setProjectDueDate(new Date());
//        project.setIsActive(true);
//
//        projectRepository.save(project);
//
//        List<Project> result = projectRepository.searchProjects("testing");
//
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).getProjectDescription().contains("testing search"));
//    }
//
//    @Test
//    void testFindByPriority() {
//        Project project = new Project();
//        project.setProjectName("High Priority Project");
//        project.setProjectPriority(Project.ProjectPriority.HIGH);
//        project.setProjectDescription("Description");
//        project.setProjectDueDate(new Date());
//        project.setIsActive(true);
//
//        projectRepository.save(project);
//
//        List<Project> result = projectRepository.findByProjectPriority(Project.ProjectPriority.HIGH);
//
//        assertEquals(1, result.size());
//        assertEquals("High Priority Project", result.get(0).getProjectName());
//    }
//
//    @Test
//    void testCountCompletedProjects() {
//        Project project = new Project();
//        project.setProjectName("Completed Project");
//        project.setProjectDescription("Description");
//        project.setProjectPriority(Project.ProjectPriority.LOW);
//        project.setProjectStatus(Project.ProjectStatus.COMPLETED);
//        project.setProjectDueDate(new Date());
//        project.setIsActive(true);
//
//        projectRepository.save(project);
//
//        Long completedCount = projectRepository.countCompletedProjects();
//
//        assertEquals(1L, completedCount);
//    }
//
//    @Test
//    void testDeleteProject() {
//        Project project = new Project();
//        project.setProjectName("Project to Delete");
//        project.setProjectDescription("Description");
//        project.setProjectPriority(Project.ProjectPriority.LOW);
//        project.setProjectDueDate(new Date());
//        project.setIsActive(true);
//
//        projectRepository.save(project);
//
//        projectRepository.deleteById(project.getProjectId());
//
//        assertTrue(projectRepository.findById(project.getProjectId()).isEmpty());
//    }
//}
