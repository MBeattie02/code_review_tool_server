package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

import java.util.List;

/**
 * Class representing the results of a security analysis.
 * Contains a list of identified vulnerabilities, repository information, and a count of the vulnerabilities.
 */
public class SecurityResult {

    private List<String> vulnerabilities; // List of identified security vulnerabilities

    private RepositoryInfo repositoryInfo; // Information about the associated repository

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private int vulnerabilitiesCount; // Count of identified security vulnerabilities

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
     * Gets the list of identified security vulnerabilities.
     *
     * @return A list of strings representing the security vulnerabilities.
     */
    public List<String> getVulnerabilities() {
        return vulnerabilities;
    }

    /**
     * Sets the list of identified security vulnerabilities.
     *
     * @param vulnerabilities A list of strings representing the security vulnerabilities.
     */
    public void setVulnerabilities(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo associated with this security result.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo to be associated with this security result.
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
     * Gets the count of identified security vulnerabilities.
     *
     * @return The number of identified security vulnerabilities.
     */
    public int getVulnerabilitiesCount() {
        return vulnerabilitiesCount;
    }

    /**
     * Sets the count of identified security vulnerabilities.
     *
     * @param vulnerabilitiesCount The number of identified security vulnerabilities.
     */
    public void setVulnerabilitiesCount(int vulnerabilitiesCount) {
        this.vulnerabilitiesCount = vulnerabilitiesCount;
    }

    /**
     * Provides a string representation of the SecurityResult object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the SecurityResult object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Security Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Number of Vulnerabilities: '").append(vulnerabilitiesCount).append("',\n");
        sb.append("  Vulnerabilities: [\n");

        for (String violation : vulnerabilities) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!vulnerabilities.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Security Analysis Result:\n");
        sb.append("  Number of Vulnerabilities: '").append(vulnerabilitiesCount).append("',\n");
        sb.append("  Vulnerabilities: [\n");

        for (String violation : vulnerabilities) {
            sb.append("    ").append(violation).append(",\n");
        }

        if (!vulnerabilities.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("\n  ]");
        return sb.toString();
    }

}
