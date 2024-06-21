package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

import java.util.List;

/**
 * Class representing the results of a quality analysis.
 * Contains a list of code duplications, repository information, and a count of the quality issues.
 */
public class QualityResult {

    private List<String> duplications; // List of identified code duplications

    private RepositoryInfo repositoryInfo; // Information about the associated repository

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private int qualityCount; // Count of quality issues identified

    /**
     * Sets the custom identifier.
     *
     * @param customId The custom identifier to set.
     */
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * Gets the custom identifier.
     *
     * @return The custom identifier.
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * Gets the list of code duplications.
     *
     * @return A list of strings representing the code duplications.
     */
    public List<String> getDuplications() {
        return duplications;
    }

    /**
     * Sets the list of code duplications.
     *
     * @param duplications A list of strings representing the code duplications.
     */
    public void setDuplications(List<String> duplications) {
        this.duplications = duplications;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo associated with this quality result.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo to be associated with this quality result.
     */
    public void setRepositoryInfo(RepositoryInfo repositoryInfo) {
        this.repositoryInfo = repositoryInfo;
    }

    /**
     * Gets the MongoDB document ID.
     *
     * @return The document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the MongoDB document ID.
     *
     * @param id The document ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the count of quality issues.
     *
     * @return The number of quality issues identified.
     */
    public int getQualityCount() {
        return qualityCount;
    }

    /**
     * Sets the count of quality issues.
     *
     * @param qualityCount The number of quality issues.
     */
    public void setQualityCount(int qualityCount) {
        this.qualityCount = qualityCount;
    }

    /**
     * Provides a string representation of the QualityResult object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the ComplexityResult object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Quality Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Number of Issues: '").append(qualityCount).append("',\n");
        sb.append("  Issues: [\n");

        for (String duplications : duplications) {
            sb.append("    ").append(duplications).append(",\n");
        }

        if (!duplications.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }


    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Quality Analysis Result:\n");
        sb.append("  Number of Issues: '").append(qualityCount).append("',\n");
        sb.append("  Issues: [\n");

        for (String duplications : duplications) {
            sb.append("    ").append(duplications).append(",\n");
        }

        if (!duplications.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("\n  ]");
        return sb.toString();
    }
}
