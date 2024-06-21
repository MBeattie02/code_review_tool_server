package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

import java.util.List;

/**
 * Class representing the results of a code smell analysis.
 * Contains a list of identified code smells, repository information, and a count of the code smells.
 */
public class CodeSmellResult {
    private List<String> smells; // List of identified code smells

    private RepositoryInfo repositoryInfo; // Information about the associated repository

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private int smellsCount; // Count of code smells identified

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
     * Gets the list of code smells.
     *
     * @return A list of strings representing the code smells.
     */
    public List<String> getSmells() {
        return smells;
    }

    /**
     * Sets the list of code smells.
     *
     * @param smells A list of strings representing the code smells.
     */
    public void setSmells(List<String> smells) {
        this.smells = smells;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo associated with this code smell result.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo to be associated with this code smell result.
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
     * Gets the count of code smells.
     *
     * @return The number of code smells identified.
     */
    public int getSmellsCount() {
        return smellsCount;
    }

    /**
     * Sets the count of code smells.
     *
     * @param smellsCount The number of code smells.
     */
    public void setSmellsCount(int smellsCount) {
        this.smellsCount = smellsCount;
    }

    /**
     * Provides a string representation of the CodeSmellResults object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the CodeSmellResults object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Smell Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Number of Smells: '").append(smellsCount).append("',\n");
        sb.append("  Smells: [\n");

        for (String violation : smells) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!smells.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    /**
     * Provides a string representation of the CodeSmellResults object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the CodeSmellResults object.
     */
    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Smell Analysis Result:\n");
        sb.append("  Number of Smells: '").append(smellsCount).append("',\n");
        sb.append("  Smells: [\n");

        for (String violation : smells) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!smells.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("\n  ]");
        return sb.toString();
    }

}
