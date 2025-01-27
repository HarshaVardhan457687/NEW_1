package com.objective.objective_service.service;

import com.objective.objective_service.entity.KeyResult;
import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.entity.Task;
import com.objective.objective_service.exception.ObjectiveNotFoundException;
import com.objective.objective_service.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ObjectiveServiceImpl implements ObjectiveService {

    private final ObjectiveRepository objectiveRepository;
    private static final Logger LOGGER = Logger.getLogger(ObjectiveServiceImpl.class.getName());
    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8082/api/keyresults/";
    private static final String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";

    @Autowired
    public ObjectiveServiceImpl(ObjectiveRepository objectiveRepository){
        this.objectiveRepository = objectiveRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Objective> getAllObjective() {
        LOGGER.info("Fetching all objectives from the database...");
        List<Objective> objectives = objectiveRepository.findAll();

        for (Objective objective : objectives) {
            Long objectiveId = objective.getObjectiveId();

            try {
                // Fetch KeyResults
                String keyResultUrl = KEYRESULT_SERVICE_URL + "objective/" + objectiveId;
                List<KeyResult> keyResults = Optional.ofNullable(
                        restTemplate.getForObject(keyResultUrl, KeyResult[].class)
                ).map(Arrays::asList).orElse(Collections.emptyList());
                objective.setKeyResult(keyResults);

                // Fetch Tasks
                String taskUrl = TASK_SERVICE_URL + "objective/" + objectiveId;
                List<Task> tasks = Optional.ofNullable(
                        restTemplate.getForObject(taskUrl, Task[].class)
                ).map(Arrays::asList).orElse(Collections.emptyList());
                objective.setObjectiveTaskList(tasks);

            } catch (RestClientException e) {
                LOGGER.severe("Error fetching data for Objective ID: " + objectiveId + " - " + e.getMessage());
                e.printStackTrace(); // Log the stack trace for debugging
            }
        }

        return objectives;
    }


    @Override
    public Objective getObjective(Long id) {
        LOGGER.info("Fetching objective with ID: " + id);

        // Fetch Objective from the database
        Optional<Objective> objectiveOpt = objectiveRepository.findById(id);
        if (!objectiveOpt.isPresent()) {
            throw new ObjectiveNotFoundException("Objective not found with ID: " + id);
        }

        Objective objective = objectiveOpt.get();

        // Fetch related KeyResults from KeyResult Microservice
        String url = KEYRESULT_SERVICE_URL + "objective/" + id;
        LOGGER.info("Fetching key results from: " + url);

        try {
            List<KeyResult> keyResults = Arrays.asList(
                    restTemplate.getForObject(url, KeyResult[].class)
            );
            // Set KeyResults to the Objective
            objective.setKeyResult(keyResults);
        } catch (RestClientException e) {
            LOGGER.severe("Failed to fetch key results for Objective ID: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to fetch key results from the microservice.");
        }

        //Fetch Tasks from task microservice
        String taskurl = TASK_SERVICE_URL + "objective/" + id;
        LOGGER.info("Fetching tasks from: " + taskurl);

        try {
            List<Task> tasks = Arrays.asList(
                    restTemplate.getForObject(taskurl, Task[].class)
            );
            // Set KeyResults to the Objective
            objective.setObjectiveTaskList(tasks);

        } catch (RestClientException e) {
            LOGGER.severe("Failed to fetch tasks for Objective ID: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to fetch key results from the tasks microservice.");
        }

        return objective;
    }

    @Override
    public Objective createObjective(Objective obj) {
        LOGGER.info("Creating a new objective...");
        return objectiveRepository.save(obj);
    }

    @Override
    public Objective updateObjective(Objective objectiveToUpdate, Long objectiveId) {
        LOGGER.info("Updating objective with ID: "+ objectiveId);

        // Check if the objective exists
        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ObjectiveNotFoundException("Objective with ID " + objectiveId + " not found."));

        // Update fields of the existing objective
        existingObjective.setObjectiveName(objectiveToUpdate.getObjectiveName());
        existingObjective.setObjectiveDescription(objectiveToUpdate.getObjectiveDescription());
        existingObjective.setMappedProject(objectiveToUpdate.getMappedProject());
        existingObjective.setAssignedTo(objectiveToUpdate.getAssignedTo());
        existingObjective.setKeyResultIds(objectiveToUpdate.getKeyResultIds());
        existingObjective.setKeyResult(objectiveToUpdate.getKeyResult());
        existingObjective.setObjectiveTaskIds(objectiveToUpdate.getObjectiveTaskIds());
        existingObjective.setObjectiveTaskList(objectiveToUpdate.getObjectiveTaskList());
        existingObjective.setObjectiveStartDate(objectiveToUpdate.getObjectiveStartDate());
        existingObjective.setObjectiveDueDate(objectiveToUpdate.getObjectiveDueDate());
        existingObjective.setObjectiveStatus(objectiveToUpdate.getObjectiveStatus());
        existingObjective.setObjectiveIsActive(objectiveToUpdate.isObjectiveIsActive());

        // Save the updated objective
        Objective updatedObjective = objectiveRepository.save(existingObjective);
        LOGGER.info("Objective with ID: " + objectiveId + "updated successfully.");

        return updatedObjective;
    }


    @Override
    public void removeObjective(Long objectiveId) {
        LOGGER.info("Removing objective with ID: " + objectiveId);

        Objective objectiveToDelete = getObjective(objectiveId);
        objectiveRepository.deleteById(objectiveId);
    }

    @Override
    public List<Objective> getAllObjectiveByProjectId(Long projectId){
        LOGGER.info("Fetching all the objective of projectID: "+ projectId);
        return objectiveRepository.findByMappedProject(projectId);
    }

    @Override
    public double getProgress(Long projectId) {
        LOGGER.info("Fetching all objectives for project ID: " + projectId);

        // Fetch objectives from the repository
        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);

        double totalProgress = 0.0;
        int totalObjectives = objectives.size();

        for (Objective objective : objectives) {
            Long objectiveId = objective.getObjectiveId();

            // Fetch related tasks from the Task Microservice
            String taskUrl = TASK_SERVICE_URL + "objective/" + objectiveId;
            LOGGER.info("Fetching tasks for Objective ID: " + objectiveId + " from: " + taskUrl);

            try {
                // Fetch tasks from Task microservice
                List<Task> tasks = Optional.ofNullable(
                        restTemplate.getForObject(taskUrl, Task[].class)
                ).map(Arrays::asList).orElse(Collections.emptyList());

                // Calculate the completion percentage of tasks for each objective
                double completionPercentage = 0.0;
                if (!tasks.isEmpty()) {
                    long completedTasks = tasks.stream()
                            .filter(task -> !task.isTaskIsActive()) // Active tasks are not completed
                            .count();
                    completionPercentage = ((double) completedTasks / tasks.size()) * 100;
                }

                // Add this objective's progress to the total progress
                totalProgress += completionPercentage;
            } catch (RestClientException e) {
                LOGGER.severe("Failed to fetch tasks for Objective ID: " + objectiveId + " - " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to fetch tasks for Objective ID: " + objectiveId);
            }
        }

        // Calculate the overall project progress as the average of all objectives' progress
        return totalObjectives > 0 ? totalProgress / totalObjectives : 0.0;
    }




}