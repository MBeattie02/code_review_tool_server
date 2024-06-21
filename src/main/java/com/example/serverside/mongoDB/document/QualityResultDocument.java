package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model for a document representing quality analysis results in MongoDB.
 * This document stores information about code duplications and other quality metrics.
 */
@Document(collection = "quality_results")
public class QualityResultDocument {

    @Id
    private String id; // MongoDB document ID

    private String customId; // Field to store custom ID

    private List<String> duplications; // List of identified code duplications

    private int qualityCount; // Count of quality issues identified

    private LocalDateTime timestamp; // Timestamp of when the document was created

    private RepositoryInfo repositoryInfo; // Information about the repository associated with this document

    /**
     * Retrieves the custom identifier.
     * This method returns the custom ID that uniquely identifies the analysis result.
     * The custom ID is a string constructed from  username, repository, path, and timestamp.
     *
     * @return The custom identifier as a String.
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * Sets the custom identifier.
     * This method assigns a custom ID to the analysis result.
     *
     * @param customId The custom identifier to be set.
     */
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * Gets the timestamp of the document.
     *
     * @return The timestamp of the document.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the document.
     *
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the list of code duplications.
     *
     * @return The list of duplications.
     */
    public List<String> getDuplications() {
        return duplications;
    }

    /**
     * Sets the list of code duplications.
     *
     * @param duplications The list of duplications to set.
     */
    public void setDuplications(List<String> duplications) {
        this.duplications = duplications;
    }

    /**
     * Gets the repository information associated with this document.
     *
     * @return The repository information.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information for this document.
     *
     * @param repositoryInfo The repository information to set.
     */
    public void setRepositoryInfo(RepositoryInfo repositoryInfo) {
        this.repositoryInfo = repositoryInfo;
    }

    /**
     * Gets the ID of the document.
     *
     * @return The document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the document.
     *
     * @param id The ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the count of quality issues.
     *
     * @return The count of quality issues.
     */
    public int getQualityCount() {
        return qualityCount;
    }

    /**
     * Sets the count of quality issues.
     *
     * @param qualityCount The quality count to set.
     */
    public void setQualityCount(int qualityCount) {
        this.qualityCount = qualityCount;
    }

}
