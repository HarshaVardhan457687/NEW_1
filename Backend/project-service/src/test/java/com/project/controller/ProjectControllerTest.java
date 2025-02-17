package com.project.controller;
//
//import com.project.model.Project;
//import com.project.services.ProjectService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.Date;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProjectController.class)
class ProjectControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProjectService projectService;
//
//    @Test
//    void testCreateProject() throws Exception {
//        Project project = new Project();
//        project.setProjectId(1L);
//        project.setProjectName("Test Project");
//        project.setProjectDescription("Description");
//        project.setProjectPriority(Project.ProjectPriority.HIGH);
//        project.setProjectStatus(Project.ProjectStatus.NOT_STARTED);
//        project.setIsActive(true);
//
//        when(projectService.createProject(Mockito.any(Project.class))).thenReturn(project);
//
//        mockMvc.perform(post("/api/projects/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"projectName\":\"Test Project\",\"projectDescription\":\"Description\",\"projectPriority\":\"HIGH\",\"isActive\":true}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.projectName").value("Test Project"))
//                .andExpect(jsonPath("$.projectPriority").value("HIGH"));
//    }
//
//    @Test
//    void testGetProjectById() throws Exception {
//        Project project = new Project();
//        project.setProjectId(1L);
//        project.setProjectName("Test Project");
//
//        when(projectService.getProjectById(1L)).thenReturn(project);
//
//        mockMvc.perform(get("/api/projects/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.projectName").value("Test Project"));
//    }
//
//    @Test
//    void testGetAllProjects() throws Exception {
//        when(projectService.getAllProjects(0, 10, "projectId", "desc", null))
//                .thenReturn(Page.empty());
//
//        mockMvc.perform(get("/api/projects?page=0&size=10&sortBy=projectId&sortDir=desc"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testUpdateProject() throws Exception {
//        Project project = new Project();
//        project.setProjectId(1L);
//        project.setProjectName("Updated Project");
//        project.setProjectDescription("Updated Description");
//        project.setProjectPriority(Project.ProjectPriority.MEDIUM);
//
//        when(projectService.updateProject(Mockito.eq(1L), Mockito.any(Project.class))).thenReturn(project);
//
//        mockMvc.perform(put("/api/projects/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"projectName\":\"Updated Project\",\"projectDescription\":\"Updated Description\",\"projectPriority\":\"MEDIUM\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.projectName").value("Updated Project"));
//    }
//
//    @Test
//    void testDeleteProject() throws Exception {
//        Mockito.doNothing().when(projectService).deleteProject(1L);
//
//        mockMvc.perform(delete("/api/projects/1"))
//                .andExpect(status().isNoContent());
//    }
}
