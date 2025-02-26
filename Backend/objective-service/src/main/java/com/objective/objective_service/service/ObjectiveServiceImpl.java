package com.objective.objective_service.service;

import com.objective.objective_service.entity.KeyResult;
import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.entity.Task;
import com.objective.objective_service.exception.ObjectiveNotFoundException;
import com.objective.objective_service.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ObjectiveServiceImpl implements ObjectiveService {

    // Repository for Objective entity, responsible for database operations
    private final ObjectiveRepository objectiveRepository;

    // Logger to log important service-related information
    private static final Logger LOGGER = Logger.getLogger(ObjectiveServiceImpl.class.getName());

    // URL for communicating with the KeyResult service
    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8082/api/keyresults/";

    // URL for communicating with the Task service
    private static final String TASK_SERVICE_URL = "http://localhost:8083/api/tasks/";

    // Constructor injection for ObjectiveRepository, allows dependency injection
    @Autowired
    public ObjectiveServiceImpl(ObjectiveRepository objectiveRepository){
        this.objectiveRepository = objectiveRepository;
    }

    // RestTemplate to interact with external services (KeyResult & Task services)
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves all objectives from the database and fetches associated key results and tasks.
     * @return List of all objectives with related key results and tasks.
     */
    @Override
    public List<Objective> getAllObjective() {
        LOGGER.info("Fetching all objectives from the database...");

        // Retrieve all objectives from the repository
        List<Objective> objectives = objectiveRepository.findAll();

        // Iterate through each objective to fetch related KeyResults and Tasks
        for (Objective objective : objectives) {
            Long objectiveId = objective.getObjectiveId();

            try {
                // Fetch KeyResults from KeyResult service using the objective ID
                String keyResultUrl = KEYRESULT_SERVICE_URL + "objective/" + objectiveId;
                List<KeyResult> keyResults = Optional.ofNullable(
                        restTemplate.getForObject(keyResultUrl, KeyResult[].class)
                ).map(Arrays::asList).orElse(Collections.emptyList());
                objective.setKeyResult(keyResults);

            } catch (RestClientException e) {
                // Log error if there's an issue while fetching data from external services
                LOGGER.severe("Error fetching data for Objective ID: " + objectiveId + " - " + e.getMessage());
                e.printStackTrace(); // Log the stack trace for debugging
            }
        }

        // Return the list of objectives, now populated with KeyResults and Tasks
        return objectives;
    }

    /**
     * Retrieves an objective by its ID and fetches associated key results and tasks.
     * @param id Objective ID
     * @return Objective object with related key results and tasks.
     */
    @Override
    public Objective getObjective(Long id) {
        LOGGER.info("Fetching objective with ID: " + id);

        // Retrieve the objective from the database
        Optional<Objective> objectiveOpt = objectiveRepository.findById(id);
        if (!objectiveOpt.isPresent()) {
            throw new ObjectiveNotFoundException("Objective not found with ID: " + id);
        }

        Objective objective = objectiveOpt.get();

        // Fetch related KeyResults from KeyResult service
        String url = KEYRESULT_SERVICE_URL + "objective/" + id;
        LOGGER.info("Fetching key results from: " + url);

        try {
            // Get the list of KeyResults for the objective
            List<KeyResult> keyResults = Arrays.asList(
                    restTemplate.getForObject(url, KeyResult[].class)
            );
            objective.setKeyResult(keyResults);
        } catch (RestClientException e) {
            // Log error if unable to fetch key results
            LOGGER.severe("Failed to fetch key results for Objective ID: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to fetch key results from the microservice.");
        }



        // Return the objective with its related KeyResults and Tasks
        return objective;
    }

    /**
     * Creates a new objective and saves it to the database.
     * @param obj Objective object to be created.
     * @return The saved Objective.
     */
    @Override
    public Objective createObjective(Objective obj) {
        LOGGER.info("Creating a new objective...");

        // Save the objective and return it
        return objectiveRepository.save(obj);
    }

    /**
     * Updates an existing objective with new data.
     * @param objectiveToUpdate Updated objective data.
     * @param objectiveId ID of the objective to update.
     * @return Updated Objective.
     */
    @Override
    public Objective updateObjective(Objective objectiveToUpdate, Long objectiveId) {
        LOGGER.info("Updating objective with ID: " + objectiveId);

        // Fetch the existing objective to update
        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ObjectiveNotFoundException("Objective with ID " + objectiveId + " not found."));

        // Update the existing objective's fields with the new values
        existingObjective.setObjectiveName(objectiveToUpdate.getObjectiveName());
        existingObjective.setMappedProject(objectiveToUpdate.getMappedProject());
        existingObjective.setKeyResultIds(objectiveToUpdate.getKeyResultIds());
        existingObjective.setKeyResult(objectiveToUpdate.getKeyResult());
        existingObjective.setObjectiveDueDate(objectiveToUpdate.getObjectiveDueDate());
        existingObjective.setObjectiveStatus(objectiveToUpdate.getObjectiveStatus());
        existingObjective.setObjectiveIsActive(objectiveToUpdate.isObjectiveIsActive());

        // Save the updated objective and return it
        Objective updatedObjective = objectiveRepository.save(existingObjective);
        LOGGER.info("Objective with ID: " + objectiveId + " updated successfully.");

        return updatedObjective;
    }

    /**
     * Deletes an objective by its ID.
     * @param objectiveId ID of the objective to be deleted.
     */
    @Override
    public void removeObjective(Long objectiveId) {
        LOGGER.info("Removing objective with ID: " + objectiveId);

        // Retrieve the objective before deleting it to ensure it exists
        Objective objectiveToDelete = getObjective(objectiveId);
        objectiveRepository.deleteById(objectiveId);
    }

    /**
     * Retrieves all objectives associated with a specific project.
     * @param projectId ID of the project.
     * @return List of objectives mapped to the project.
     */
    @Override
    public List<Objective> getAllObjectiveByProjectId(Long projectId) {
        LOGGER.info("Fetching all objectives for projectID: " + projectId);

        // Return objectives mapped to the given project
        return objectiveRepository.findByMappedProject(projectId);
    }


    /**
     * Take all the objectives of given project along with active projects.
     * @param projectIds List of the projects.
     * @return Map of allObjective and activeObjectives.
     */
    @Override
    public Map<String, List<Objective>> getObjectivesByProjects(List<Long> projectIds) {
        List<Objective> allObjectives = objectiveRepository.findByMappedProjectIn(projectIds);
        List<Objective> activeObjectives = objectiveRepository.findByMappedProjectInAndObjectiveIsActiveTrue(projectIds);

        Map<String, List<Objective>> result = new HashMap<>();
        result.put("allObjectives", allObjectives);
        result.put("activeObjectives", activeObjectives);

        return result;
    }

    /**
     * Take the list of the projectIds and give all the objectives
     * that are present in those projects
     * @param projectIds List of the prokjectIds
     * @return List of Ids of all the Objectives present in given projects
     * */
    public List<Objective> getAllObjectiveByProjectIds(List<Long> projectIds){
        LOGGER.info("Fetching all the objectives for given projectIds");
        List<Objective> allObjectives = objectiveRepository.findByMappedProjectIn(projectIds);
        return allObjectives;
    }




    /**
     * Take the objectiveId of the objectives and give progress of it
     * @param objectiveId ID of the objective
     * @return progress of the objective
     * */
    public double calculateObjectiveProgress(Long objectiveId) {
        String url = KEYRESULT_SERVICE_URL + "/objective/" + objectiveId;

        ResponseEntity<List<KeyResult>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<KeyResult> keyResults = response.getBody();

        // No KeyResults, progress is 0%
        if (keyResults == null || keyResults.isEmpty()) {
            return 0.0;
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (KeyResult keyResult : keyResults) {
            if (keyResult.getKeyResultTargetVal() > 0) { // Avoid division by zero
                double progress = (double) keyResult.getKeyResultcurrentVal() / keyResult.getKeyResultTargetVal();
                double weight = getPriorityWeight(getPriorityWeight(keyResult.getKeyResultPriority()));

                weightedSum += progress * weight;
                totalWeight += weight;
            }
        }

        // Avoid division by zero if all weights are zero
        return (totalWeight > 0) ? (weightedSum / totalWeight) * 100 : 0.0;
    }

    // Helper method to get the weight for a given priority
    private double getPriorityWeight(String priority) {
        return switch (priority.toUpperCase()) {
            case "HIGH" -> 1.5;
            case "MEDIUM" -> 1.0;
            case "LOW" -> 0.5;
            default -> 1.0; // Default to MEDIUM weight if unknown priority
        };
    }



    /**
     * Take the progress of objective
     */
    @Override
    public double calculateProjectProgress(Long projectId) {
        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);

        if (objectives.isEmpty()) {
            return 0.0; // No objectives, progress is 0%
        }

        double totalProgress = 0.0;

        for (Objective objective : objectives) {
            totalProgress += calculateObjectiveProgress(objective.getObjectiveId());
        }

        return totalProgress / objectives.size(); // Average progress of all objectives
    }


}

