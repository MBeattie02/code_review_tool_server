package com.example.serverside.analysis.scheduledAnalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for scheduling and executing tasks.
 * This service periodically checks for due tasks and executes them.
 */
@Service
public class TaskSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerService.class);

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository; // Repository for scheduled tasks

    @Autowired
    private RestTemplate restTemplate; // RestTemplate for making HTTP requests

    /**
     * Periodically executes tasks that are due.
     * This method runs every 60 seconds and checks for tasks that need to be executed.
     */
    @Scheduled(fixedRate = 60000) // Run every 60 seconds
    public void executeTasks() {
        List<ScheduledTask> dueTasks = scheduledTaskRepository.findByScheduleTimeBefore(LocalDateTime.now());
        for (ScheduledTask task : dueTasks) {
            executeTask(task);
            logger.info("Executing tasks at {}", LocalDateTime.now());

        }
    }

    /**
     * Executes a single scheduled task.
     * Makes an HTTP request based on the task's stored endpoint and other details.
     *
     * @param task The task to be executed.
     */
    private void executeTask(ScheduledTask task) {
        String baseUrl = "http://localhost:8080/api";
//        String baseUrl = "https://analyser-serverside.azurewebsites.net/api";

        // Construct the full URL by appending task.getEndpoint() to baseUrl
        String url = baseUrl + task.getEndpoint();

        try {
            // Construct the URL with query parameters
            String fullUrl = url + "?username=" + task.getUsername()
                    + "&repo=" + task.getRepo()
                    + "&commitId=" + task.getCommitId()
                    + "&path=" + task.getPath()
                    + "&shouldPostComment=" + task.getShouldPostComment();


            // Log that the task is about to be executed
            logger.info("Executing task: {}", task);

            restTemplate.getForObject(fullUrl, Void.class);

            // Log that the task has been successfully executed
            logger.info("Task executed successfully: {}", task);

            scheduledTaskRepository.delete(task);
            logger.info("Executing task: {}", task);
        } catch (Exception e) {
            // Handle exceptions
            logger.error("Error executing task: {}", task, e);
        }
    }

}
