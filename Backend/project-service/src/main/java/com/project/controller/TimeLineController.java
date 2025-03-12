package com.project.controller;

import com.project.model.TimeLine;
import com.project.services.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing TimeLines.
 * This controller handles CRUD operations for TimeLine entities.
 *
 * Base URL: /api/timelines
 */
@RestController
@RequestMapping("/api/timelines")
public class TimeLineController {

    private final TimeLineService timeLineService;

    /**
     * Constructor-based dependency injection for TimeLineService.
     *
     * @param timeLineService Service layer responsible for business logic related to TimeLines.
     */
    @Autowired
    public TimeLineController(TimeLineService timeLineService) {
        this.timeLineService = timeLineService;
    }


    /**
     * Creates a new TimeLine.
     *
     * @param timeLine The TimeLine object to be created.
     * @return ResponseEntity with the created TimeLine and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<TimeLine> createTimeLine(@RequestBody TimeLine timeLine) {
        TimeLine createdTimeLine = timeLineService.createTimeLine(timeLine);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeLine);
    }

    /**
     * Retrieves all TimeLines.
     *
     * @return ResponseEntity with a list of TimeLines and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<TimeLine>> getAllTimeLines() {
        List<TimeLine> timeLines = timeLineService.getAllTimeLine();
        return ResponseEntity.ok(timeLines);
    }

    /**
     * Retrieves a specific TimeLine by its ID.
     *
     * @param timeLineId The ID of the TimeLine to retrieve.
     * @return ResponseEntity containing the requested TimeLine and HTTP status 200 (OK).
     */
    @GetMapping("/{timeLineId}")
    public ResponseEntity<TimeLine> getTimeLineById(@PathVariable Long timeLineId) {
        TimeLine timeLine = timeLineService.getTimeLineById(timeLineId);
        return ResponseEntity.ok(timeLine);
    }

    /**
     * Updates an existing TimeLine using partial updates (PATCH).
     *
     * @param timeLineId The ID of the TimeLine to update.
     * @param timeLine The TimeLine object containing updated fields.
     * @return ResponseEntity with the updated TimeLine and HTTP status 200 (OK).
     */
    @PatchMapping("/{timeLineId}")
    public ResponseEntity<TimeLine> updateTimeLine(
            @PathVariable Long timeLineId,
            @RequestBody TimeLine timeLine) {
        TimeLine updatedTimeLine = timeLineService.updateTimeLine(timeLineId, timeLine);
        return ResponseEntity.ok(updatedTimeLine);
    }

    /**
     * Deletes a specific TimeLine by its ID.
     *
     * @param timeLineId The ID of the TimeLine to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping("/{timeLineId}")
    public ResponseEntity<Void> removeTimeLine(@PathVariable Long timeLineId) {
        timeLineService.removeTimeLine(timeLineId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all TimeLines associated with a specific Project ID.
     *
     * @param projectId The ID of the project whose TimeLines are to be retrieved.
     * @return ResponseEntity with a list of TimeLines for the given project and HTTP status 200 (OK).
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TimeLine>> getTimeLinesByProjectId(@PathVariable Long projectId) {
        List<TimeLine> timeLines = timeLineService.getAllTimeLine(projectId);
        return ResponseEntity.ok(timeLines);
    }
}