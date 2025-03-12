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

    /**
     * Updates the given project by adding the objective ID.
     *
     * @param projectId The ID of the project to update.
     * @param objectiveId The ID of the objective to add.
     */
    private void updateProjectWithObjective(Long projectId, Long objectiveId) {
        if (projectId == null || objectiveId == null) {
            LOGGER.warn("Project ID or Objective ID is null. Skipping update.");
            return;
        }

        List<Long> requestBody = Collections.singletonList(objectiveId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(requestBody, headers);

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

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ObjectiveNotFoundException("Objective with ID " + objectiveId + " not found."));

        existingObjective.setObjectiveName(objectiveToUpdate.getObjectiveName());
        existingObjective.setMappedProject(objectiveToUpdate.getMappedProject());
        existingObjective.setKeyResultIds(objectiveToUpdate.getKeyResultIds());
        existingObjective.setKeyResult(objectiveToUpdate.getKeyResult());
        existingObjective.setObjectiveDueDate(objectiveToUpdate.getObjectiveDueDate());
        existingObjective.setObjectivePriority(objectiveToUpdate.getObjectivePriority());
        existingObjective.setObjectiveStatus(objectiveToUpdate.getObjectiveStatus());
        existingObjective.setObjectiveIsActive(objectiveToUpdate.isObjectiveIsActive());

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

        try{
            List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);

            List<ObjectiveSummaryDTO> objectiveSummaries = new ArrayList<>();

            for (Objective objective : objectives) {
                Long objectiveId = objective.getObjectiveId();
                double progress = calculateObjectiveProgress(objectiveId);

                List<KeyResultSummaryDto> keyResultSummaries = new ArrayList<>();

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

                    Long teamLeaderId = fetchTeamLeaderId(keyResult.getTeamId());

                    if (teamLeaderId != null) {
                        UserSummaryDTO userDetails = fetchUserDetails(teamLeaderId);
                        keyResultSummary.setTeamLeaderProfilePic(userDetails.getUserProfilePhoto());
                    }
                    keyResultSummaries.add(keyResultSummary);
                }

                ObjectiveSummaryDTO summaryDto = new ObjectiveSummaryDTO();
                summaryDto.setObjectiveId(objective.getObjectiveId());
                summaryDto.setObjectiveName(objective.getObjectiveName());
                summaryDto.setObjectiveStatus(objective.getObjectiveStatus());
                summaryDto.setObjectiveProgress(progress);
                summaryDto.setKeyResults(keyResultSummaries);

                objectiveSummaries.add(summaryDto);
            }
            return objectiveSummaries;
        }catch (Exception e){
            LOGGER.info("Error fetching key results for all objective ");
            return null;
        }
    }

    /**
     * Fetches key results related to a specific objective.
     *
     * @param objectiveId The ID of the objective.
     * @return A list of KeyResult entities associated with the objective.
     */
    private List<KeyResult> fetchKeyResultsByObjectiveId(Long objectiveId) {
        try {
            String url = KEYRESULT_SERVICE_URL + "objective/" + objectiveId;
            ResponseEntity<List<KeyResult>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<KeyResult>>() {});
            return response.getBody() != null ? response.getBody() : null;
        } catch (Exception e) {
            LOGGER.info("Error fetching key results for objective ID: " + objectiveId);
            return null;
        }
    }

    /**
     * Retrieves the progress of a specific key result.
     *
     * @param keyResultId The ID of the key result.
     * @return The progress as a percentage (0 to 100).
     */
    private Float getKeyResultProgress(Long keyResultId) {
        try {
            String url = KEYRESULT_SERVICE_URL + "progress/" + keyResultId; // Ensure correct URL formatting
            LOGGER.info("Fetching KeyResult Progress for KeyResultId: {}", keyResultId);

            Float progress = restTemplate.getForObject(url, Float.class);
            LOGGER.info("Received progress for KeyResultId {}: {}", keyResultId, progress);

            return progress;
        } catch (Exception e) {
            LOGGER.error("Error fetching progress for KeyResultId: {}", keyResultId, e);
            return 0.0f;
        }
    }

    /**
     * Fetches the team leader's ID for a given team.
     * @param teamId The ID of the team whose leader's ID needs to be fetched.
     * @return The ID of the team leader, or null if an error occurs.
     */
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

    /**
     * Fetches the name of the team associated with the given team ID.
     * This method interacts with the Team service to retrieve the team name.
     * If an error occurs, it logs the error and returns null.
     *
     * @param teamId The ID of the team whose name is to be fetched.
     * @return The name of the team, or null if an error occurs.
     */
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

    /**
     * Fetches the user details for a given user ID.
     * This method interacts with the User service to retrieve the user's details.
     * If an error occurs during the communication, it logs the error and returns null.
     *
     * @param userId The ID of the user whose details need to be fetched.
     * @return A UserSummaryDTO object containing the user details, or null if an error occurs.
     */
    private UserSummaryDTO fetchUserDetails(Long userId) {
        try {
            String url = USER_SERVICE_URL + "user-summary/" + userId;
            return restTemplate.getForObject(url, UserSummaryDTO.class);
        } catch (Exception e) {
            LOGGER.info("Error fetching user details for userId: {}" + userId);
            return null;
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
        return (totalWeight > 0) ? (weightedSum / totalWeight) * 100 : 0.0;
    }

    private double getPriorityWeight(String priority) {
        return switch (priority.toUpperCase()) {
            case "HIGH" -> 1.5;
            case "MEDIUM" -> 1.0;
            case "LOW" -> 0.5;
            default -> 1.0;
        };
    }

    /**
     * Calculates the performance of objectives within a given project, categorizing them into different statuses.
     * This method iterates over all objectives mapped to a specific project, counting the number of objectives
     * that are 'At Risk', 'On Track', or 'Completed'. It then calculates the percentage of objectives in each status
     * relative to the total number of objectives.
     *
     * @param projectId The ID of the project whose objectives' performance needs to be calculated.
     * @return A map containing the percentage of objectives in each status: "AT_RISK", "ON_TRACK", "COMPLETED".
     *         Returns 0% if no objectives are found.
     */
    @Override
    public Map<String, Integer> calculateObjectivePerformance(Long projectId) {
        List<Objective> allObjectives = objectiveRepository.findByMappedProject(projectId);

        int atRiskCount = 0;
        int onTrackCount = 0;
        int completedCount = 0;

        for (Objective objective : allObjectives) {
            switch (objective.getObjectiveStatus()) {
                case AT_RISK:
                    atRiskCount++;
                    break;
                case ON_TRACK:
                    onTrackCount++;
                    break;
                case COMPLETED:
                    completedCount++;
                    break;
            }
        }
        int totalObjectives = allObjectives.size();

        Map<String, Integer> statusPercentage = new HashMap<>();

        if (totalObjectives > 0) {
            statusPercentage.put("AT_RISK", (int) ((double) atRiskCount / totalObjectives * 100));
            statusPercentage.put("ON_TRACK", (int) ((double) onTrackCount / totalObjectives * 100));
            statusPercentage.put("COMPLETED", (int) ((double) completedCount / totalObjectives * 100));
        } else {
            statusPercentage.put("AT_RISK", 0);
            statusPercentage.put("ON_TRACK", 0);
            statusPercentage.put("COMPLETED", 0);
        }
        return statusPercentage;
    }

    /**
     * Calculates the overall progress of a project based on the progress of its objectives.
     * The project progress is calculated as a weighted average of the progress of each objective,
     * where the weight is determined by the priority of each objective.
     *
     * @param projectId The ID of the project whose progress needs to be calculated.
     * @return A double value representing the project's progress as a percentage (0.0 to 100.0).
     */
    @Override
    public double calculateProjectProgress(Long projectId) {
        List<Objective> objectives = objectiveRepository.findByMappedProject(projectId);
        if (objectives.isEmpty()) {
            return 0.0;
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

    /**
     * Determines the status of an objective based on its start date, due date, and actual progress.
     * The objective's progress is compared to the expected progress based on the days passed.
     * If the actual progress is at or above the expected progress, the status is 'ON_TRACK'.
     * Otherwise, the status is 'AT_RISK'.
     *
     * @param startDate The start date of the objective.
     * @param dueDate The due date of the objective.
     * @param actualProgress The actual progress made towards the objective (0-100%).
     * @return The calculated objective status: 'ON_TRACK' or 'AT_RISK'.
     */
    @Override
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

    /**
     * Adds a Key Result to an Objective. This method checks if the request contains a valid Key Result ID,
     * and if the objective is found, it adds the Key Result to the list of Key Results for that objective.
     * If the Key Result is already present, it returns a message indicating so.
     *
     * @param objectiveId The ID of the objective to which the Key Result should be added.
     * @param request A list containing the Key Result ID to be added to the objective.
     * @return A message indicating whether the Key Result was successfully added or if there was an issue.
     */
    @Override
    public String addKeyResultToObjective(Long objectiveId, List<Long> request) {
        if (request == null || request.isEmpty()) {
            return "Invalid request body";
        }
        Long keyResultId = request.get(0);
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

