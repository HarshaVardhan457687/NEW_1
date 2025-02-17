//package com.objective.objective_service.service;
//
//import com.objective.objective_service.entity.KeyResult;
//import com.objective.objective_service.entity.Objective;
//import com.objective.objective_service.exception.BadRequestException;
//import com.objective.objective_service.exception.ObjectiveNotFoundException;
//import com.objective.objective_service.repository.ObjectiveRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//class ObjectiveServiceImplTest {
//
//    @Mock
//    private ObjectiveRepository objectiveRepository;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private ObjectiveServiceImpl objectiveService;
//
//    private Objective sampleObjective;
//    private KeyResult sampleKeyResult;
//    private Date futureDueDate;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Setup future due date (3 months from now)
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, 3);
//        futureDueDate = calendar.getTime();
//
//        // Setup sample data
//        sampleObjective = new Objective();
//        sampleObjective.setObjectiveId(1L);
//        sampleObjective.setObjectiveName("Increase Revenue");
//        sampleObjective.setObjectiveDescription("Increase company revenue by 20%");
//        sampleObjective.setObjectiveDueDate(futureDueDate);
//
//        sampleKeyResult = new KeyResult();
//        sampleKeyResult.setKeyResultId(1L);
//        sampleKeyResult.setKeyResultName("Achieve $1M in sales");
//    }
//
//    @Test
//    void getAllObjective_Success() {
//        // Arrange
//        List<Objective> objectives = List.of(sampleObjective);
//        when(objectiveRepository.findAll()).thenReturn(objectives);
//
//        // Act
//        List<Objective> result = objectiveService.getAllObjective();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Increase Revenue", result.get(0).getObjectiveName());
//        verify(objectiveRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getObjective_Success() {
//        // Arrange
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(sampleObjective));
//        when(restTemplate.getForObject(anyString(), eq(KeyResult[].class)))
//                .thenReturn(new KeyResult[]{sampleKeyResult});
//
//        // Act
//        Objective result = objectiveService.getObjective(1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Increase Revenue", result.getObjectiveName());
//        assertNotNull(result.getKeyResult());
//        assertEquals(1, result.getKeyResult().size());
//    }
//
//    @Test
//    void getObjective_ObjectiveNotFound() {
//        // Arrange
//        when(objectiveRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ObjectiveNotFoundException exception = assertThrows(ObjectiveNotFoundException.class, () -> {
//            objectiveService.getObjective(1L);
//        });
//        assertEquals("Objective not found with id: 1", exception.getMessage());
//    }
//
//    @Test
//    void getObjective_KeyResultServiceFailure() {
//        // Arrange
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(sampleObjective));
//        when(restTemplate.getForObject(anyString(), eq(KeyResult[].class)))
//                .thenThrow(new RestClientException("Service unavailable"));
//
//        // Act & Assert
//        RestClientException exception = assertThrows(RestClientException.class, () -> {
//            objectiveService.getObjective(1L);
//        });
//        assertEquals("Service unavailable", exception.getMessage());
//    }
//
//    @Test
//    void createObjective_Success() {
//        // Arrange
//        when(objectiveRepository.save(any(Objective.class))).thenReturn(sampleObjective);
//
//        // Act
//        Objective result = objectiveService.createObjective(sampleObjective);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Increase Revenue", result.getObjectiveName());
//        assertEquals(futureDueDate, result.getDueDate());
//        verify(objectiveRepository, times(1)).save(any(Objective.class));
//    }
//
//    @Test
//    void createObjective_InvalidData_ThrowsBadRequestException() {
//        // Arrange
//        Objective invalidObjective = new Objective();
//
//        // Act & Assert
//        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
//            objectiveService.createObjective(invalidObjective);
//        });
//        assertEquals("Missing required fields for creating an objective", exception.getMessage());
//    }
//
//    @Test
//    void updateObjective_Success() {
//        // Arrange
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, 6);
//        Date newDueDate = calendar.getTime();
//
//        Objective updatedObjective = new Objective();
//        updatedObjective.setObjectiveName("Updated Objective");
//        updatedObjective.setDueDate(newDueDate);
//
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(sampleObjective));
//        when(objectiveRepository.save(any(Objective.class))).thenReturn(updatedObjective);
//
//        // Act
//        Objective result = objectiveService.updateObjective(updatedObjective, 1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Updated Objective", result.getObjectiveName());
//        assertEquals(newDueDate, result.getDueDate());
//    }
//
//    @Test
//    void updateObjective_ObjectiveNotFound() {
//        // Arrange
//        when(objectiveRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ObjectiveNotFoundException exception = assertThrows(ObjectiveNotFoundException.class, () -> {
//            objectiveService.updateObjective(sampleObjective, 1L);
//        });
//        assertEquals("Objective not found with id: 1", exception.getMessage());
//    }
//
//    @Test
//    void removeObjective_Success() {
//        // Arrange
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(sampleObjective));
//        doNothing().when(objectiveRepository).deleteById(1L);
//
//        // Act
//        objectiveService.removeObjective(1L);
//
//        // Assert
//        verify(objectiveRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void removeObjective_ObjectiveNotFound() {
//        // Arrange
//        when(objectiveRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ObjectiveNotFoundException exception = assertThrows(ObjectiveNotFoundException.class, () -> {
//            objectiveService.removeObjective(1L);
//        });
//        assertEquals("Objective not found with id: 1", exception.getMessage());
//    }
//}
