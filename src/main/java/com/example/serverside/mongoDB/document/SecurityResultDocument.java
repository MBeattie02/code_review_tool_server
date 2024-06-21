package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model for a document representing security analysis results in MongoDB.
 */
@Document(collection = "security_results")
public class SecurityResultDocument {

    @Id
    private String id; // MongoDB document ID

    private String customId; // Field to store custom ID

    private List<String> vulnerabilities; // List of security vulnerabilities

    private int vulnerabilitiesCount; // Count of security vulnerabilities

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
     * Gets the list of security vulnerabilities.
     *
     * @return The list of vulnerabilities.
     */
    public List<String> getVulnerabilities() {
        return vulnerabilities;
    }

    /**
     * Sets the list of security vulnerabilities.
     *
     * @param vulnerabilities The list of vulnerabilities to set.
     */
    public void setVulnerabilities(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
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
     * Gets the count of security vulnerabilities.
     *
     * @return The count of vulnerabilities.
     */
    public int getVulnerabilitiesCount() {
        return vulnerabilitiesCount;
    }

    /**
     * Sets the count of security vulnerabilities.
     *
     * @param vulnerabilitiesCount The vulnerabilities count to set.
     */
    public void setVulnerabilitiesCount(int vulnerabilitiesCount) {
        this.vulnerabilitiesCount = vulnerabilitiesCount;
    }


}

