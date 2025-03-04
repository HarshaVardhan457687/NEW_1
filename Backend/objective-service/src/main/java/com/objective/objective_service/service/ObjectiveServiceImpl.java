package com.objective.objective_service.service;

import com.objective.objective_service.dto.KeyResultSummaryDto;
import com.objective.objective_service.dto.UserSummaryDTO;
import com.objective.objective_service.entity.KeyResult;
import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.exception.ObjectiveNotFoundException;
import com.objective.objective_service.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class ObjectiveServiceImpl implements ObjectiveService {

    // Repository for Objective entity, responsible for database operations
    private final ObjectiveRepository objectiveRepository;

    // Logger to log important service-related information
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    // URL for communicating with the KeyResult service
    private static final String KEYRESULT_SERVICE_URL = "http://localhost:8082/api/keyresults/";
    private static final String USER_SERVICE_URL = "http://localhost:8086/api/users/";
    private static final String TEAM_SERVICE_URL = "http://localhost:8060/api/teams/";
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
        List<Objective> objectives = objectiveRepository.findAll();
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
        existingObjective.setObjectivePriority(objectiveToUpdate.getObjectivePriority());
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

        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);

        for (Objective objective : objectives) {
            Long objectiveId = objective.getObjectiveId();
            List<KeyResultSummaryDto> keyResultSummaries = new ArrayList<>();

            // Fetch all KeyResults for the given objective from KeyResult Service
            List<KeyResult> keyResults = fetchKeyResultsByObjectiveId(objectiveId);

            for (KeyResult keyResult : keyResults) {
                KeyResultSummaryDto keyResultSummary = new KeyResultSummaryDto();
                keyResultSummary.setName(keyResult.getKeyResultName());
                keyResultSummary.setPriority(keyResult.getKeyResultPriority().name());
                keyResultSummary.setCurrKeyResultVal((double) keyResult.getKeyResultcurrentVal());
                keyResultSummary.setDueDate(keyResult.getKeyResultDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                keyResultSummary.setProgress(getKeyResultProgress(keyResult.getKeyResultId()));
                // Fetch team leader ID from Teams service
                Long teamLeaderId = fetchTeamLeaderId(keyResult.getTeamId());

                // Fetch team leader details from User service
                if (teamLeaderId != null) {
                    UserSummaryDTO userDetails = fetchUserDetails(teamLeaderId);
                    keyResultSummary.setTeamLeaderName(userDetails.getUserName());
                    keyResultSummary.setTeamLeaderProfilePic(userDetails.getUserProfilePhoto());
                }

                keyResultSummaries.add(keyResultSummary);
            }
            objective.setKeyResult(keyResultSummaries);
        }

        return objectives;
    }

    private List<KeyResult> fetchKeyResultsByObjectiveId(Long objectiveId) {
        try {
            String url = KEYRESULT_SERVICE_URL + "objective/" + objectiveId;
            ResponseEntity<List<KeyResult>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<KeyResult>>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            LOGGER.info("Error fetching key results for objective ID: " + objectiveId);
            return Collections.emptyList();
        }
    }

    private Float getKeyResultProgress(Long keyResultId) {
        try {
            String url = KEYRESULT_SERVICE_URL + "progress/" + keyResultId; // Ensure correct URL formatting
            LOGGER.info("Fetching KeyResult Progress for KeyResultId: {}", keyResultId);

            // Make the HTTP request and get the progress
            Float progress = restTemplate.getForObject(url, Float.class);
            LOGGER.info("Received progress for KeyResultId {}: {}", keyResultId, progress);

            return progress;
        } catch (Exception e) {
            // Corrected to log keyResultId instead of teamId
            LOGGER.error("Error fetching progress for KeyResultId: {}", keyResultId, e);
            // You can return null instead of 0 if you want to signify an error more clearly
            return 0.0f;
        }
    }


    private Long fetchTeamLeaderId(Long teamId) {
        try {
            String url = TEAM_SERVICE_URL + "get-team-lead/" + teamId;
            LOGGER.info("Fetching team leader ID from URL: {}", url);

            Long teamLeadId = restTemplate.getForObject(url, Long.class);
            LOGGER.info("Received team leader ID: {}", teamLeadId);

            return teamLeadId;
        } catch (Exception e) {
            LOGGER.error("Error fetching team leader ID for teamId: {}", teamId, e);
            return null;
        }
    }


    private UserSummaryDTO fetchUserDetails(Long userId) {
        try {
            String url = USER_SERVICE_URL + "user-summary/" + userId;
            return restTemplate.getForObject(url, UserSummaryDTO.class);
        } catch (Exception e) {
            LOGGER.info("Error fetching user details for userId: {}" + userId);
            return null; // Return null or handle with a default object if needed
        }
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
                double weight = getPriorityWeight(keyResult.getKeyResultPriority().name());

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
            default -> 1.0;
        };
    }



    @Override
    public double calculateProjectProgress(Long projectId) {
        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);

        if (objectives.isEmpty()) {
            return 0.0; // No objectives, progress is 0%
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Objective objective : objectives) {
            double objectiveProgress = calculateObjectiveProgress(objective.getObjectiveId());
            double weight = getPriorityWeight(objective.getObjectivePriority().name()); // Convert Enum to String

            weightedSum += objectiveProgress * weight;
            totalWeight += weight;
        }


        return (totalWeight > 0) ? (weightedSum / totalWeight) : 0.0;
    }


}

