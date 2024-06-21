package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.CombinedAnalysisResultRepository;
import com.example.serverside.mongoDB.document.CombinedAnalysisResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing all types of analysis results combined.
 * This service interacts with the CombinedAnalysisResultRepository to
 * perform operations such as retrieving, updating, or deleting combined analysis result documents.
 */
@Service
public class AllResultService {

    /**
     * Repository for accessing combined analysis result documents.
     */
    private final CombinedAnalysisResultRepository combinedAnalysisResultRepository;

    /**
     * Constructs a new AllResultService with the specified repository.
     * This repository handles data operations for combined analysis results.
     *
     * @param combinedAnalysisResultRepository The repository used for retrieving combined analysis result documents.
     */
    @Autowired
    public AllResultService(CombinedAnalysisResultRepository combinedAnalysisResultRepository) {
        this.combinedAnalysisResultRepository = combinedAnalysisResultRepository;
    }

    /**
     * Retrieves all combined analysis result documents from the repository.
     *
     * @return A list of all combined analysis result documents.
     */
    public List<CombinedAnalysisResultDocument> getAllResults() {
        return combinedAnalysisResultRepository.findAll();
    }

    /**
     * Retrieves a specific combined analysis result document by its ID.
     *
     * @param id The ID of the combined analysis result document.
     * @return An Optional containing the combined analysis result document if found, or an empty Optional otherwise.
     */
    public Optional<CombinedAnalysisResultDocument> getAllResultById(String id) {
        return combinedAnalysisResultRepository.findById(id);
    }

    /**
     * Retrieves combined analysis result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the combined analysis results.
     * @return A list of combined analysis result documents that match the given path.
     */
    public List<CombinedAnalysisResultDocument> getAllResultByPath(String path) {
        return combinedAnalysisResultRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves combined analysis result documents that match a specific customId.
     *
     * @param customId The customId to search for in the combined analysis results.
     * @return A list of combined analysis result documents that match the given customId.
     */
    public Optional<CombinedAnalysisResultDocument> getAllResultByCustomId(String customId) {
        return combinedAnalysisResultRepository.findByCustomId(customId);
    }
}
