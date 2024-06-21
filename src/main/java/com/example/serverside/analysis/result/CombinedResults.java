package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;

/**
 * Class representing a combination of various analysis results.
 * This class encapsulates results from quality, code smell, security, complexity, and style analyses.
 * It is useful for presenting a comprehensive view of the analyses performed on a repository.
 */
public class CombinedResults {
    private QualityResult qualityResult; // Quality analysis results
    private CodeSmellResult codeSmellResult; // Code smell analysis results
    private SecurityResult securityResult; // Security analysis results
    private ComplexityResult complexityResult; // Complexity analysis results
    private StyleResult styleResult; // Style analysis results

    private String id; // MongoDB document ID

    private String customId; // Custom identifier

    private RepositoryInfo repositoryInfo; // Information about the associated repository

    /**
     * Default constructor for CombinedResults.
     */
    public CombinedResults() {
    }

    /**
     * Constructor that initializes CombinedResults with individual analysis results.
     *
     * @param qualityResult Quality analysis results.
     * @param codeSmellResult Code smell analysis results.
     * @param securityResult Security analysis results.
     * @param complexityResult Complexity analysis results.
     * @param styleResult Style analysis results.
     * @param repositoryInfo Information about the associated repository.
     */
    public CombinedResults(QualityResult qualityResult,
                           CodeSmellResult codeSmellResult,
                           SecurityResult securityResult,
                           ComplexityResult complexityResult,
                           StyleResult styleResult,
                           RepositoryInfo repositoryInfo
                           ) {
        this.qualityResult = qualityResult;
        this.codeSmellResult = codeSmellResult;
        this.securityResult = securityResult;
        this.complexityResult = complexityResult;
        this.styleResult = styleResult;
        this.repositoryInfo = repositoryInfo;
    }

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
     * Gets the quality analysis results.
     *
     * @return The QualityResult object.
     */
    public QualityResult getQualityResult() {
        return qualityResult;
    }

    /**
     * Sets the quality analysis results.
     *
     * @param qualityResult The QualityResult object to set.
     */
    public void setQualityResult(QualityResult qualityResult) {
        this.qualityResult = qualityResult;
    }

    /**
     * Gets the code smell analysis results.
     *
     * @return The CodeSmellResult object.
     */
    public CodeSmellResult getCodeSmellResult() {
        return codeSmellResult;
    }

    /**
     * Sets the code smell analysis results.
     *
     * @param codeSmellResult The CodeSmellResult object to set.
     */
    public void setCodeSmellResult(CodeSmellResult codeSmellResult) {
        this.codeSmellResult = codeSmellResult;
    }

    /**
     * Gets the security analysis results.
     *
     * @return The SecurityResult object.
     */
    public SecurityResult getSecurityResult() {
        return securityResult;
    }

    /**
     * Sets the security analysis results.
     *
     * @param securityResult The SecurityResult object to set.
     */
    public void setSecurityResult(SecurityResult securityResult) {
        this.securityResult = securityResult;
    }

    /**
     * Gets the complexity analysis results.
     *
     * @return The ComplexityResult object.
     */
    public ComplexityResult getComplexityResult() {
        return complexityResult;
    }

    /**
     * Sets the complexity analysis results.
     *
     * @param complexityResult The ComplexityResult object to set.
     */
    public void setComplexityResult(ComplexityResult complexityResult) {
        this.complexityResult = complexityResult;
    }

    /**
     * Gets the style analysis results.
     *
     * @return The StyleResult object.
     */
    public StyleResult getStyleResult() {
        return styleResult;
    }

    /**
     * Sets the style analysis results.
     *
     * @param styleResult The StyleResult object to set.
     */
    public void setStyleResult(StyleResult styleResult) {
        this.styleResult = styleResult;
    }

    /**
     * Gets the repository information.
     *
     * @return The RepositoryInfo object.
     */
    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    /**
     * Sets the repository information.
     *
     * @param repositoryInfo The RepositoryInfo object to set.
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
     * @param id The document ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Provides a string representation of the CombinedResults object.
     * This representation includes details about all the analysis results contained in the object
     *
     * @return A string representation of the CombinedResults object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code Analysis Result:\n");
        sb.append("  Custom ID: '").append(customId).append("',\n");
        sb.append("  Quality Results: '").append(qualityResult.toString2()).append("',\n");
        sb.append("  Smells Results: '").append(codeSmellResult.toString2()).append("',\n");
        sb.append("  Complexity Results: '").append(complexityResult.toString2()).append("',\n");
        sb.append("  Security Results: '").append(securityResult.toString2()).append("',\n");
        sb.append("  Style Results: '").append(styleResult.toString2()).append("',\n");

        return sb.toString();
    }
}
