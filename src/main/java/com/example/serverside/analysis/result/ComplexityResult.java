package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

/**
 * Class representing the results of a complexity analysis.
 * Contains information about cyclomatic complexity and repository details.
 */
public class ComplexityResult {

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private int cyclomaticComplexity; // Cyclomatic complexity score

    private RepositoryInfo repositoryInfo; // Information about the associated repository

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
     * Constructor for ComplexityResult.
     *
     * @param cyclomaticComplexity The cyclomatic complexity score.
     */
    public ComplexityResult(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    /**
     * Gets the cyclomatic complexity score.
     *
     * @return The cyclomatic complexity score.
     */
    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    /**
     * Sets the cyclomatic complexity score.
     *
     * @param cyclomaticComplexity The cyclomatic complexity score to set.
     */
    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo associated with this complexity result.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo to be associated with this complexity result.
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
     * Provides a string representation of the ComplexityResult object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the ComplexityResult object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Complexity Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Complexity Value: '").append(cyclomaticComplexity).append("',\n");

        return sb.toString();
    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Complexity Analysis Result:\n");
        sb.append("  Complexity Value: '").append(cyclomaticComplexity);

        return sb.toString();
    }

}
