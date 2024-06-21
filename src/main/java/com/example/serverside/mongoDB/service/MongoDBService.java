package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.*;
import com.example.serverside.analysis.result.*;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Service class for managing and storing various analysis results into MongoDB.
 * This service handles operations for security, complexity, code smell, quality,
 * style, and combined analysis results.
 */
@Service
public class MongoDBService {

    // Repository declarations
    private final SecurityResultRepository securityResultRepository;
    private final ComplexityResultRepository complexityResultRepository;
    private final CombinedAnalysisResultRepository combinedAnalysisResultRepository;
    private final SmellResultRepository smellResultRepository;
    private final QualityResultRepository qualityResultRepository;
    private final StyleResultsRepository styleResultsRepository;

    /**
     * Constructs a new MongoDBService with specified repositories.
     * Each repository corresponds to a different type of analysis result.
     *
     * @param securityResultRepository          Repository for security results.
     * @param complexityResultRepository        Repository for complexity results.
     * @param combinedAnalysisResultRepository  Repository for combined analysis results.
     * @param smellResultRepository             Repository for code smell results.
     * @param qualityResultRepository           Repository for quality results.
     * @param styleResultsRepository            Repository for style results.
     */
    @Autowired
    public MongoDBService(SecurityResultRepository securityResultRepository,
                          ComplexityResultRepository complexityResultRepository,
                          CombinedAnalysisResultRepository combinedAnalysisResultRepository,
                          SmellResultRepository smellResultRepository,
                          QualityResultRepository qualityResultRepository,
                          StyleResultsRepository styleResultsRepository) {
        this.securityResultRepository = securityResultRepository;
        this.complexityResultRepository = complexityResultRepository;
        this.combinedAnalysisResultRepository = combinedAnalysisResultRepository;
        this.smellResultRepository = smellResultRepository;
        this.qualityResultRepository = qualityResultRepository;
        this.styleResultsRepository = styleResultsRepository;
    }

    /**
     * Saves a security result to the repository and returns its ID.
     *
     * @param result The SecurityResult to be saved.
     * @return The ID of the saved document.
     */
    public String saveSecurityResult(SecurityResult result) {
        SecurityResultDocument document = convertToSecurityResultDocument(result);
        document.setVulnerabilitiesCount(result.getVulnerabilities().size()); // Set the count
        document.setTimestamp(LocalDateTime.now());
        SecurityResultDocument savedDocument = securityResultRepository.save(document);
        return savedDocument.getId();
    }

    /**
     * Saves a complexity result to the repository and returns its ID.
     *
     * @param result The ComplexityResult to be saved.
     * @return The ID of the saved document.
     */
    public String saveComplexityResult(ComplexityResult result) {
        ComplexityResultDocument document = convertToComplexityResultDocument(result);
        document.setTimestamp(LocalDateTime.now());
        ComplexityResultDocument savedDocument = complexityResultRepository.save(document);
        return savedDocument.getId();
    }

    /**
     * Saves a codeSmell result to the repository and returns its ID.
     *
     * @param result The CodeSmellResult to be saved.
     * @return The ID of the saved document.
     */
    public String saveCodeSmellResult(CodeSmellResult result) {
        CodeSmellResultDocument document = convertToCodeSmellResultDocument(result);
        document.setSmellsCount(result.getSmells().size()); // Set the count
        document.setTimestamp(LocalDateTime.now());
        CodeSmellResultDocument savedDocument = smellResultRepository.save(document);
        return savedDocument.getId();
    }

    /**
     * Saves a quality result to the repository and returns its ID.
     *
     * @param result The QualityResult to be saved.
     * @return The ID of the saved document.
     */
    public String saveQualityResult(QualityResult result) {
        QualityResultDocument document = convertToQualityResultDocument(result);
        document.setQualityCount(result.getDuplications().size()); // Set the count
        document.setTimestamp(LocalDateTime.now());
        QualityResultDocument savedDocument = qualityResultRepository.save(document);
        return savedDocument.getId();
    }

    /**
     * Saves a style result to the repository and returns its ID.
     *
     * @param result The StyleResult to be saved.
     * @return The ID of the saved document.
     */
    public String saveStyleResult(StyleResult result) {
        StyleResultDocument document = convertToStyleResultDocument(result);
        document.setViolationCount(result.getViolations().size()); // Set the count
        document.setTimestamp(LocalDateTime.now());
        StyleResultDocument savedDocument = styleResultsRepository.save(document);
        return savedDocument.getId();
    }

    /**
     * Converts a combined analysis result into its corresponding document format.
     *
     * @param results The CombinedResults to be converted.
     * @return The converted CombinedAnalysisResultDocument.
     */
    public CombinedAnalysisResultDocument convertToCombinedAnalysisResultDocument(CombinedResults results) {
        CombinedAnalysisResultDocument document = new CombinedAnalysisResultDocument();
        document.setQualityResultDocument(convertToQualityResultDocument(results.getQualityResult()));
        document.setCodeSmellResultDocument(convertToCodeSmellResultDocument(results.getCodeSmellResult()));
        document.setSecurityResultDocument(convertToSecurityResultDocument(results.getSecurityResult()));
        document.setComplexityResultDocument(convertToComplexityResultDocument(results.getComplexityResult()));
        document.setStyleResultDocument(convertToStyleResultDocument(results.getStyleResult()));
        RepositoryInfo repositoryInfo = results.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(results.getCustomId()); // Set the custom ID
        document.setTimestamp(LocalDateTime.now());
        return document;
    }

    /**
     * Converts a quality analysis result into its corresponding document format.
     *
     * @param result The QualityResult to be converted.
     * @return The converted QualityResultDocument.
     */
    QualityResultDocument convertToQualityResultDocument(QualityResult result) {
        QualityResultDocument document = new QualityResultDocument();
        RepositoryInfo repositoryInfo = result.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(result.getCustomId()); // Set the custom ID
        document.setQualityCount(result.getDuplications().size()); // Set the count
        document.setDuplications(result.getDuplications());
        return document;
    }

    /**
     * Converts a codeSmell analysis result into its corresponding document format.
     *
     * @param result The CodeSmellResult to be converted.
     * @return The converted CodeSmellResultDocument.
     */
    CodeSmellResultDocument convertToCodeSmellResultDocument(CodeSmellResult result) {
        CodeSmellResultDocument document = new CodeSmellResultDocument();
        RepositoryInfo repositoryInfo = result.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(result.getCustomId()); // Set the custom ID
        document.setSmellsCount(result.getSmells().size()); // Set the count
        document.setSmells(result.getSmells());
        return document;
    }

    /**
     * Converts a complexity analysis result into its corresponding document format.
     *
     * @param result The ComplexityResult to be converted.
     * @return The converted ComplexityResultDocument.
     */
    ComplexityResultDocument convertToComplexityResultDocument(ComplexityResult result) {
        ComplexityResultDocument document = new ComplexityResultDocument();
        RepositoryInfo repositoryInfo = result.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(result.getCustomId()); // Set the custom ID
        document.setCyclomaticComplexity(result.getCyclomaticComplexity());
        return document;
    }

    /**
     * Converts a style analysis result into its corresponding document format.
     *
     * @param result The StyleResult to be converted.
     * @return The converted StyleResultDocument.
     */
    StyleResultDocument convertToStyleResultDocument(StyleResult result) {
        StyleResultDocument document = new StyleResultDocument();
        RepositoryInfo repositoryInfo = result.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(result.getCustomId()); // Set the custom ID
        document.setViolationCount(result.getViolations().size()); // Set the count
        document.setViolations(result.getViolations());

        return document;
    }

    /**
     * Saves a combined analysis result document to the repository and returns its ID.
     *
     * @param document The CombinedAnalysisResultDocument to be saved.
     * @return The ID of the saved document.
     */
    public String saveCombinedAnalysisResult(CombinedAnalysisResultDocument document) {
        CombinedAnalysisResultDocument savedDocument = combinedAnalysisResultRepository.save(document);
        document.setTimestamp(LocalDateTime.now());
        return savedDocument.getId();
    }

    /**
     * Converts a security analysis result into its corresponding document format.
     *
     * @param result The SecurityResult to be converted.
     * @return The converted SecurityResultDocument.
     */
    SecurityResultDocument convertToSecurityResultDocument(SecurityResult result) {
        SecurityResultDocument document = new SecurityResultDocument();
        RepositoryInfo repositoryInfo = result.getRepositoryInfo();
        document.setRepositoryInfo(repositoryInfo);
        document.setCustomId(result.getCustomId()); // Set the custom ID
        document.setVulnerabilitiesCount(result.getVulnerabilities().size()); // Set the count
        document.setVulnerabilities(result.getVulnerabilities());

        return document;
    }



}
