package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Model for a document representing the combined analysis results in MongoDB.
 * This document aggregates various types of analysis results for a given repository.
 */
@Document(collection = "combined_results")
public class CombinedAnalysisResultDocument {

    @Id
    private String id; // MongoDB document ID

    private String customId; // Field to store custom ID

    private RepositoryInfo repositoryInfo; // Information about the repository

    // Documents for each type of analysis
    private QualityResultDocument qualityResultDocument;
    private CodeSmellResultDocument codeSmellResultDocument;
    private SecurityResultDocument securityResultDocument;
    private ComplexityResultDocument complexityResultDocument;
    private StyleResultDocument styleResultDocument;

    private LocalDateTime timestamp; // Timestamp of when the document was created or updated

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
     * @return The timestamp of the document.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the document.
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Default constructor for CombinedAnalysisResultDocument.
     */
    public CombinedAnalysisResultDocument() {
    }

    /**
     * All-args constructor for CombinedAnalysisResultDocument.
     *
     * @param qualityResultDocument     The quality analysis result document.
     * @param codeSmellResultDocument   The code smell analysis result document.
     * @param securityResultDocument    The security analysis result document.
     * @param complexityResultDocument  The complexity analysis result document.
     * @param styleResultDocument       The style analysis result document.
     * @param repositoryInfo            The repository information associated with these results.
     */
    public CombinedAnalysisResultDocument(QualityResultDocument qualityResultDocument,
                                          CodeSmellResultDocument codeSmellResultDocument,
                                          SecurityResultDocument securityResultDocument,
                                          ComplexityResultDocument complexityResultDocument,
                                          StyleResultDocument styleResultDocument,
                                          RepositoryInfo repositoryInfo) {
        this.qualityResultDocument = qualityResultDocument;
        this.codeSmellResultDocument = codeSmellResultDocument;
        this.securityResultDocument = securityResultDocument;
        this.complexityResultDocument = complexityResultDocument;
        this.styleResultDocument = styleResultDocument;
        this.repositoryInfo = repositoryInfo;
    }

    /**
     * Gets the QualityResultDocument.
     * This document contains the detailed results of the quality analysis.
     *
     * @return The QualityResultDocument associated with this combined result.
     */
    public QualityResultDocument getQualityResultDocument() {
        return qualityResultDocument;
    }

    /**
     * Sets the QualityResultDocument.
     * This document should contain the detailed results of the quality analysis.
     *
     * @param qualityResultDocument The QualityResultDocument to set.
     */
    public void setQualityResultDocument(QualityResultDocument qualityResultDocument) {
        this.qualityResultDocument = qualityResultDocument;
    }

    /**
     * Gets the CodeSmellResultDocument.
     * This document contains the detailed results of the smell analysis.
     *
     * @return The CodeSmellResultDocument associated with this combined result.
     */
    public CodeSmellResultDocument getCodeSmellResultDocument() {
        return codeSmellResultDocument;
    }

    /**
     * Sets the CodeSmellResultDocument.
     * This document should contain the detailed results of the code smell analysis.
     *
     * @param codeSmellResultDocument The CodeSmellResultDocument to set.
     */
    public void setCodeSmellResultDocument(CodeSmellResultDocument codeSmellResultDocument) {
        this.codeSmellResultDocument = codeSmellResultDocument;
    }

    /**
     * Gets the SecurityResultDocument.
     * This document contains the detailed results of the security analysis.
     *
     * @return The SecurityResultDocument associated with this combined result.
     */
    public SecurityResultDocument getSecurityResultDocument() {
        return securityResultDocument;
    }

    /**
     * Sets the SecurityResultDocument.
     * This document should contain the detailed results of the security analysis.
     *
     * @param securityResultDocument The SecurityResultDocument to set.
     */
    public void setSecurityResultDocument(SecurityResultDocument securityResultDocument) {
        this.securityResultDocument = securityResultDocument;
    }

    /**
     * Gets the ComplexityResultDocument.
     * This document contains the detailed results of the complexity analysis.
     *
     * @return The ComplexityResultDocument associated with this combined result.
     */
    public ComplexityResultDocument getComplexityResultDocument() {
        return complexityResultDocument;
    }

    /**
     * Sets the ComplexityResultDocument.
     * This document should contain the detailed results of the complexity analysis.
     *
     * @param complexityResultDocument The ComplexityResultDocument to set.
     */
    public void setComplexityResultDocument(ComplexityResultDocument complexityResultDocument) {
        this.complexityResultDocument = complexityResultDocument;
    }

    /**
     * Gets the StyleResultDocument.
     * This document contains the detailed results of the style analysis.
     *
     * @return The StyleResultDocument associated with this combined result.
     */
    public StyleResultDocument getStyleResultDocument() {
        return styleResultDocument;
    }

    /**
     * Sets the StyleResultDocument.
     * This document should contain the detailed results of the style analysis.
     *
     * @param styleResultDocument The StyleResultDocument to set.
     */
    public void setStyleResultDocument(StyleResultDocument styleResultDocument) {
        this.styleResultDocument = styleResultDocument;
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
     * Provides a string representation of the combined analysis results.
     * @return A string representation of the document.
     */
    @Override
    public String toString() {
        return "AnalysisResults{" +
                "qualityResult=" + qualityResultDocument +
                ", codeSmellResult=" + codeSmellResultDocument +
                ", securityResult=" + securityResultDocument +
                ", complexityResult=" + complexityResultDocument +
                ", styleResult=" + styleResultDocument +
                '}';
    }
}
