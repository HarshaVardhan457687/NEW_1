package com.objective.objective_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.service.ObjectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObjectiveController.class)
class ObjectiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ObjectiveService objectiveService;

    @Autowired
    private ObjectMapper objectMapper;

    private Objective sampleObjective;
    private Date futureDueDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup future due date (3 months from now)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 3);
        futureDueDate = calendar.getTime();

        // Setup sample data
        sampleObjective = new Objective();
        sampleObjective.setObjectiveId(1L);
        sampleObjective.setObjectiveName("Increase Revenue");
        sampleObjective.setObjectiveDescription("Increase company revenue by 20%");
        sampleObjective.setDueDate(futureDueDate);
    }

    @Test
    void createObjective_Success() throws Exception {
        // Arrange
        when(objectiveService.createObjective(any(Objective.class))).thenReturn(sampleObjective);

        // Act & Assert
        mockMvc.perform(post("/api/objective")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleObjective)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.objectiveId").value(sampleObjective.getObjectiveId()))
                .andExpect(jsonPath("$.objectiveName").value(sampleObjective.getObjectiveName()));

        verify(objectiveService, times(1)).createObjective(any(Objective.class));
    }

    @Test
    void getAllObjective_Success() throws Exception {
        // Arrange
        List<Objective> objectives = Arrays.asList(sampleObjective);
        when(objectiveService.getAllObjective()).thenReturn(objectives);

        // Act & Assert
        mockMvc.perform(get("/api/objective")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].objectiveName").value(sampleObjective.getObjectiveName()));

        verify(objectiveService, times(1)).getAllObjective();
    }

    @Test
    void getObjectiveById_Success() throws Exception {
        // Arrange
        when(objectiveService.getObjective(1L)).thenReturn(sampleObjective);

        // Act & Assert
        mockMvc.perform(get("/api/objective/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objectiveId").value(sampleObjective.getObjectiveId()))
                .andExpect(jsonPath("$.objectiveName").value(sampleObjective.getObjectiveName()));

        verify(objectiveService, times(1)).getObjective(1L);
    }

    @Test
    void getObjectiveById_NotFound() throws Exception {
        // Arrange
        when(objectiveService.getObjective(anyLong())).thenThrow(new RuntimeException("Objective not found"));

        // Act & Assert
        mockMvc.perform(get("/api/objective/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(objectiveService, times(1)).getObjective(1L);
    }

    @Test
    void updateObjective_Success() throws Exception {
        // Arrange
        Objective updatedObjective = new Objective();
        updatedObjective.setObjectiveId(1L);
        updatedObjective.setObjectiveName("Updated Objective");
        updatedObjective.setDueDate(futureDueDate);

        when(objectiveService.updateObjective(any(Objective.class), anyLong())).thenReturn(updatedObjective);

        // Act & Assert
        mockMvc.perform(put("/api/objective/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedObjective)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objectiveName").value("Updated Objective"));

        verify(objectiveService, times(1)).updateObjective(any(Objective.class), anyLong());
    }

    @Test
    void removeObjective_Success() throws Exception {
        // Arrange
        doNothing().when(objectiveService).removeObjective(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/objective/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(objectiveService, times(1)).removeObjective(1L);
    }

    @Test
    void removeObjective_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Objective not found")).when(objectiveService).removeObjective(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/objective/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(objectiveService, times(1)).removeObjective(1L);
    }
}
