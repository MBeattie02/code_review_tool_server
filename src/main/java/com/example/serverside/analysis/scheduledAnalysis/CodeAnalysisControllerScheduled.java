package com.example.serverside.analysis.scheduledAnalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for scheduling code analysis tasks.
 * This controller handles HTTP requests to schedule tasks for code analysis, storing them in a repository for later execution.
 */
@RestController
@RequestMapping("/api")
public class CodeAnalysisControllerScheduled {

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository; // Repository for storing scheduled tasks

    /**
     * Endpoint to schedule a new code analysis task.
     * Receives task details as a request body and saves it to the scheduled task's repository.
     *
     * @param taskDetails The details of the task to be scheduled, provided in the request body.
     * @return A ResponseEntity indicating the successful scheduling of the task.
     */
    @PostMapping("/schedule-analysis")
    public ResponseEntity<String> scheduleAnalysis(@RequestBody ScheduledTask taskDetails) {
        scheduledTaskRepository.save(taskDetails);
        return ResponseEntity.ok("Analysis scheduled for: " + taskDetails.getScheduleTime());
    }
}
