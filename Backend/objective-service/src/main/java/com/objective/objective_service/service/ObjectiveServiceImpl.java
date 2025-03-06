package com.objective.objective_service.service;

import com.objective.objective_service.constants.ObjectiveStatus;
import com.objective.objective_service.dto.KeyResultSummaryDto;
import com.objective.objective_service.dto.ObjectiveSummaryDTO;
import com.objective.objective_service.dto.UserSummaryDTO;
import com.objective.objective_service.entity.KeyResult;
import com.objective.objective_service.entity.Objective;
import com.objective.objective_service.exception.ObjectiveNotFoundException;
import com.objective.objective_service.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    private static final String TEAM_SERVICE_URL = "http://localhost:8084/api/teams/";
    private static final String PROJECT_SERVICE_URL = "http://localhost:8085/api/projects/";
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
     * @param objective Objective object to be created.
     * @return The saved Objective.
     */
    @Override
    public Objective createObjective(Objective objective) {
        LOGGER.info("Creating a new objective...");
        Objective savedObjective = objectiveRepository.save(objective);

        updateProjectWithObjective(savedObjective.getMappedProject(), savedObjective.getObjectiveId());
        return savedObjective;
    }

    private void updateProjectWithObjective(Long projectId, Long objectiveId) {
        if (projectId == null || objectiveId == null) {
            LOGGER.warn("Project ID or Objective ID is null. Skipping update.");
            return;
        }

        // Prepare request (We send a single objectiveId in a List)
        List<Long> requestBody = Collections.singletonList(objectiveId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Define API Endpoint
        String updateUrl = PROJECT_SERVICE_URL  + projectId + "/add-objective";

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    updateUrl, HttpMethod.PATCH, requestEntity, String.class);

            LOGGER.info("Updated Project {} with new Objective {} via API. Response: {}",
                    projectId, objectiveId, response.getBody());

        } catch (Exception e) {
            LOGGER.error("Failed to update Project {} with Objective {}. Error: {}",
                    projectId, objectiveId, e.getMessage(), e);
        }
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
    public List<ObjectiveSummaryDTO> getAllObjectiveByProjectId(Long projectId) {
        LOGGER.info("Fetching all objectives for projectID: " + projectId);

        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);
        List<ObjectiveSummaryDTO> objectiveSummaries = new ArrayList<>();

        for (Objective objective : objectives) {
            Long objectiveId = objective.getObjectiveId();

            // Calculate objective progress and status
            double progress = calculateObjectiveProgress(objectiveId);
            ObjectiveStatus newStatus = calculateObjectiveStatus(objective.getObjectiveCreatedAt(), objective.getObjectiveDueDate(), progress);
            objective.setObjectiveStatus(newStatus);

            List<KeyResultSummaryDto> keyResultSummaries = new ArrayList<>();

            // Fetch all KeyResults for the given objective
            List<KeyResult> keyResults = fetchKeyResultsByObjectiveId(objectiveId);

            for (KeyResult keyResult : keyResults) {
                KeyResultSummaryDto keyResultSummary = new KeyResultSummaryDto();
                keyResultSummary.setKeyResultId(keyResult.getKeyResultId());
                keyResultSummary.setName(keyResult.getKeyResultName());
                keyResultSummary.setPriority(keyResult.getKeyResultPriority().name());
                keyResultSummary.setCurrKeyResultVal((double) keyResult.getKeyResultcurrentVal());
                keyResultSummary.setTargetKeyResultVal((double) keyResult.getKeyResultTargetVal());
                keyResultSummary.setUnit(keyResult.getKeyResultunit());
                keyResultSummary.setDueDate(keyResult.getKeyResultDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                keyResultSummary.setProgress(getKeyResultProgress(keyResult.getKeyResultId()));
                keyResultSummary.setTeamName(fetchTeamName(keyResult.getTeamId()));
                // Fetch team leader ID from Teams service
                Long teamLeaderId = fetchTeamLeaderId(keyResult.getTeamId());

                // Fetch team leader details from User service
                if (teamLeaderId != null) {
                    UserSummaryDTO userDetails = fetchUserDetails(teamLeaderId);
                    keyResultSummary.setTeamLeaderProfilePic(userDetails.getUserProfilePhoto());
                }

                keyResultSummaries.add(keyResultSummary);
            }

            // Create an ObjectiveSummaryDto and add it to the list
            ObjectiveSummaryDTO summaryDto = new ObjectiveSummaryDTO();
            summaryDto.setObjectiveId(objective.getObjectiveId());
            summaryDto.setObjectiveName(objective.getObjectiveName());
            summaryDto.setObjectiveStatus(objective.getObjectiveStatus());
            summaryDto.setObjectiveProgress(progress);
            summaryDto.setKeyResults(keyResultSummaries);

            objectiveSummaries.add(summaryDto);
        }

        return objectiveSummaries;
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

    private String fetchTeamName(Long teamId) {
        try {
            String url = TEAM_SERVICE_URL + "get-team-name/" + teamId;
            LOGGER.info("Fetching team name from URL: {}", url);

            String teamName = restTemplate.getForObject(url, String.class);
            LOGGER.info("Received team name: {}", teamName);

            return teamName;
        } catch (Exception e) {
            LOGGER.error("Error fetching team name for teamId: {}", teamId, e);
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



    public ObjectiveStatus calculateObjectiveStatus(Date startDate, Date dueDate, double actualProgress) {
        if (startDate == null || dueDate == null) return ObjectiveStatus.ON_TRACK;

        Date currentDate = new Date();
        long totalDays = ChronoUnit.DAYS.between(startDate.toInstant(), dueDate.toInstant());
        long daysPassed = ChronoUnit.DAYS.between(startDate.toInstant(), currentDate.toInstant());

        if (totalDays <= 0) return ObjectiveStatus.ON_TRACK;

        double expectedProgress = (100.0 / totalDays) * daysPassed;

        if (daysPassed == 0) return ObjectiveStatus.ON_TRACK;
        return actualProgress >= expectedProgress ? ObjectiveStatus.ON_TRACK : ObjectiveStatus.AT_RISK;
    }

    @Override
    public String addKeyResultToObjective(Long objectiveId, List<Long> request) {
        if (request == null || request.isEmpty()) {
            return "Invalid request body";
        }

        Long keyResultId = request.get(0); // ✅ Fix: Get the first key result ID from the list

        Optional<Objective> optionalObjective = objectiveRepository.findById(objectiveId);
        if (optionalObjective.isPresent()) {
            Objective objective = optionalObjective.get();

            if (objective.getKeyResultIds() == null) {
                objective.setKeyResultIds(new ArrayList<>());
            }

            if (!objective.getKeyResultIds().contains(keyResultId)) {
                objective.getKeyResultIds().add(keyResultId);
                objectiveRepository.save(objective);
                return "KeyResult added successfully";
            }
            return "KeyResult already exists";
        }
        return "Objective not found";
    }


}

