package com.example.serverside.analysis.scheduledAnalysis;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Model for a document representing a scheduled task in MongoDB.
 * This class stores details about tasks scheduled for execution, including information about a GitHub repository and specific endpoints.
 */
@Document(collection = "scheduledTasks")
public class ScheduledTask {
    @Id
    private String id; // Unique identifier for the scheduled task
    private String username; // GitHub username
    private String repo; // GitHub repository name
    private String commitId; // Specific commit ID in the repository
    private String path; // File path within the repository
    private String endpoint; // API endpoint to be called for the task
    private LocalDateTime scheduleTime; // Time at which the task is scheduled
    private boolean shouldPostComment;
    private Map<String, String> additionalParams; // Additional parameters for the task

    /**
     * Default constructor for ScheduledTask.
     */
    public ScheduledTask() {
        // Default constructor
    }

    /**
     * Gets the boolean value if scheduled task should post comment.
     * @return shouldPostComment.
     */
    public boolean getShouldPostComment() {
        return shouldPostComment;
    }

    /**
     * Gets the unique identifier of the scheduled task.
     * @return The task's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the username of the scheduled task.
     * @return The task's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the repository of the scheduled task.
     * @return The task's repository.
     */
    public String getRepo() {
        return repo;
    }

    /**
     * Gets the commitID of the scheduled task.
     * @return The task's commitID.
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * Gets the path of the scheduled task.
     * @return The task's path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the endpoint of the scheduled task.
     * @return The task's endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Gets the schedule time of the scheduled task.
     * @return The task's schedule time.
     */
    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    /**
     * Gets the additional parameters of the scheduled task.
     * @return The task's additional parameters.
     */
    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Sets the unique identifier of the scheduled task.
     * @param id The task's ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the username of the scheduled task.
     * @param username The task's username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the boolean value of the scheduled task comment.
     * @param shouldPostComment The task's comment should be sent.
     */
    public void setShouldPostComment(boolean shouldPostComment) {
        this.shouldPostComment = shouldPostComment;
    }

    /**
     * Sets the repo of the scheduled task.
     * @param repo The task's repo to set.
     */
    public void setRepo(String repo) {
        this.repo = repo;
    }

    /**
     * Sets the commitId of the scheduled task.
     * @param commitId The task's commitId to set.
     */
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    /**
     * Sets the path of the scheduled task.
     * @param path The task's path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Sets the endpoint of the scheduled task.
     * @param endpoint The task's endpoint to set.
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Sets the scheduleTime of the scheduled task.
     * @param scheduleTime The task's scheduleTime to set.
     */
    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * Sets the additionalParams of the scheduled task.
     * @param additionalParams The task's additionalParams to set.
     */
    public void setAdditionalParams(Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }
}
