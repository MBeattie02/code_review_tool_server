package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.ComplexityResultRepository;
import com.example.serverside.mongoDB.document.ComplexityResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing complexity analysis results.
 * This service provides methods to interact with the ComplexityResultRepository
 * for operations such as retrieving, updating, or deleting complexity result documents.
 */
@Service
public class ComplexityResultService {

    /**
     * Repository for accessing complexity result documents.
     */
    private final ComplexityResultRepository complexityResultRepository;

    /**
     * Constructs a new ComplexityResultService with the specified repository.
     *
     * @param complexityResultRepository The repository used for retrieving complexity result documents.
     */
    @Autowired
    public ComplexityResultService(ComplexityResultRepository complexityResultRepository) {
        this.complexityResultRepository = complexityResultRepository;
    }

    /**
     * Retrieves all complexity result documents from the repository.
     *
     * @return A list of all complexity result documents.
     */
    public List<ComplexityResultDocument> getAllComplexityResults() {
        return complexityResultRepository.findAll();
    }

    /**
     * Retrieves a complexity result document by its ID.
     *
     * @param id The ID of the complexity result document.
     * @return An Optional containing the complexity result document if found, or an empty Optional otherwise.
     */
    public Optional<ComplexityResultDocument> getComplexityResultById(String id) {
        return complexityResultRepository.findById(id);
    }

    /**
     * Retrieves complexity result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the complexity results.
     * @return A list of complexity result documents that match the given path.
     */
    public List<ComplexityResultDocument> getComplexityResultPath(String path) {
        return complexityResultRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves complexity result documents that match a specific customId.
     *
     * @param customId The customId to search for in the complexity results.
     * @return A list of complexity result documents that match the given customId.
     */
    public Optional<ComplexityResultDocument> getComplexityResultByCustomId(String customId) {
        return complexityResultRepository.findByCustomId(customId);
    }
}
