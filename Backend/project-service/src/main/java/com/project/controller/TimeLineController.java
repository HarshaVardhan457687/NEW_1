package com.project.controller;

import com.project.model.TimeLine;
import com.project.services.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timelines")
public class TimeLineController {

    private final TimeLineService timeLineService;

    @Autowired
    public TimeLineController(TimeLineService timeLineService) {
        this.timeLineService = timeLineService;
    }

    // Create a new timeline
    @PostMapping
    public ResponseEntity<TimeLine> createTimeLine(@RequestBody TimeLine timeLine) {
        TimeLine createdTimeLine = timeLineService.createTimeLine(timeLine);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeLine);
    }

    // Get all timelines
    @GetMapping
    public ResponseEntity<List<TimeLine>> getAllTimeLines() {
        List<TimeLine> timeLines = timeLineService.getAllTimeLine();
        return ResponseEntity.ok(timeLines);
    }

    // Get a specific timeline by ID
    @GetMapping("/{timeLineId}")
    public ResponseEntity<TimeLine> getTimeLineById(@PathVariable Long timeLineId) {
        TimeLine timeLine = timeLineService.getTimeLineById(timeLineId);
        return ResponseEntity.ok(timeLine);
    }

    // Update a specific timeline
    @PatchMapping("/{timeLineId}")
    public ResponseEntity<TimeLine> updateTimeLine(
            @PathVariable Long timeLineId,
            @RequestBody TimeLine timeLine) {
        TimeLine updatedTimeLine = timeLineService.updateTimeLine(timeLineId, timeLine);
        return ResponseEntity.ok(updatedTimeLine);
    }

    // Delete a specific timeline
    @DeleteMapping("/{timeLineId}")
    public ResponseEntity<Void> removeTimeLine(@PathVariable Long timeLineId) {
        timeLineService.removeTimeLine(timeLineId);
        return ResponseEntity.noContent().build();
    }

    // Get all timelines by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TimeLine>> getTimeLinesByProjectId(@PathVariable Long projectId) {
        List<TimeLine> timeLines = timeLineService.getAllTimeLine(projectId);
        return ResponseEntity.ok(timeLines);
    }
}