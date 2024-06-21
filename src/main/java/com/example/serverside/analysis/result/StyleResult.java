package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

import java.util.List;

/**
 * Class representing the results of a style analysis.
 * This includes a list of style violations, repository information, and a count of the violations.
 */
public class StyleResult {

    private List<String> violations; // List of style violations

    private RepositoryInfo repositoryInfo; // Information about the associated repository

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private int violationCount; // Count of style violations

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
     * Gets the list of style violations.
     *
     * @return A list of strings representing the style violations.
     */
    public List<String> getViolations() {
        return violations;
    }

    /**
     * Sets the list of style violations.
     *
     * @param violations A list of strings representing the style violations.
     */
    public void setViolations(List<String> violations) {
        this.violations = violations;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo associated with this style result.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo to be associated with this style result.
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
     * Gets the count of style violations.
     *
     * @return The number of style violations.
     */
    public int getViolationCount() {
        return violationCount;
    }

    /**
     * Sets the count of style violations.
     *
     * @param violationCount The number of style violations.
     */
    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    /**
     * Provides a string representation of the StyleResult object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the StyleResult object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Style Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Number of Violations: '").append(violationCount).append("',\n");
        sb.append("  Violations: [\n");

        for (String violation : violations) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!violations.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Style Analysis Result:\n");
        sb.append("  Number of Violations: '").append(violationCount).append("',\n");
        sb.append("  Violations: [\n");

        for (String violation : violations) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!violations.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("\n  ]");
        return sb.toString();
    }


}

