//package com.project.services;
//
//import com.project.model.Project;
//import com.project.repository.ProjectRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//class ProjectServiceTest {
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @InjectMocks
//    private ProjectService projectService;
//
//    @Test
//    void testCreateProject() {
//        Project project = new Project();
//        project.setProjectName("Test Project");
//
//        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);
//
//        Project createdProject = projectService.createProject(project);
//
//        assertEquals("Test Project", createdProject.getProjectName());
//    }
//
//    @Test
//    void testGetAllProjects() {
//        List<Project> projects = Arrays.asList(
//                new Project(),
//                new Project()
//        );
//        Page<Project> projectPage = new PageImpl<>(projects);
//
//        when(projectRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(projectPage);
//
//        Page<Project> result = projectService.getAllProjects(0, 10, "projectId", "desc", null);
//
//        assertEquals(2, result.getTotalElements());
//    }
//
//    @Test
//    void testUpdateProject() {
//        Project existingProject = new Project();
//        existingProject.setProjectName("Existing Project");
//
//        Project updatedDetails = new Project();
//        updatedDetails.setProjectName("Updated Project");
//
//        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(existingProject));
//        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(updatedDetails);
//
//        Project updatedProject = projectService.updateProject(1L, updatedDetails);
//
//        assertEquals("Updated Project", updatedProject.getProjectName());
//    }
//
//    @Test
//    void testDeleteProject() {
//        Project project = new Project();
//        project.setProjectName("Project to Delete");
//
//        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
//
//        projectService.deleteProject(1L);
//
//        Mockito.verify(projectRepository).delete(project);
//    }
////
//    @Test
//    void testSearchProjects() {
//        List<Project> projects = Arrays.asList(
//                new Project()
//        );
//
//        when(projectRepository.searchProjects("Search")).thenReturn(projects);
//
//        List<Project> result = projectService.searchProjects("Search");
//
//        assertEquals(1, result.size());
//    }
//}
