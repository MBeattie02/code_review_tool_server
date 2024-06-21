package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.SmellResultRepository;
import com.example.serverside.mongoDB.document.CodeSmellResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing code smell analysis results.
 * Provides methods to interact with the smell result repository for
 * retrieving, saving, or updating code smell result documents.
 */
@Service
public class SmellResultService {

    /**
     * Repository for accessing code smell result documents.
     */
    private final SmellResultRepository smellResultRepository;

    /**
     * Constructs a new SmellResultService with the specified repository.
     *
     * @param smellResultRepository The repository used for retrieving code smell result documents.
     */
    @Autowired
    public SmellResultService(SmellResultRepository smellResultRepository) {
        this.smellResultRepository = smellResultRepository;
    }

    /**
     * Retrieves all code smell result documents from the repository.
     *
     * @return A list of all code smell result documents.
     */
    public List<CodeSmellResultDocument> getAllSmellResults() {
        return smellResultRepository.findAll();
    }

    /**
     * Retrieves a code smell result document by its ID.
     *
     * @param id The ID of the code smell result document.
     * @return An Optional containing the code smell result document if found, or an empty Optional otherwise.
     */
    public Optional<CodeSmellResultDocument> getSmellResultById(String id) {
        return smellResultRepository.findById(id);
    }

    /**
     * Retrieves code smell result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the code smell results.
     * @return A list of code smell result documents that match the given path.
     */
    public List<CodeSmellResultDocument> getSmellResultsByPath(String path) {
        return smellResultRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves code smell result documents that match a specific customId.
     *
     * @param customId The customId to search for in the code smell results.
     * @return A list of code smell result documents that match the given customId.
     */
    public Optional<CodeSmellResultDocument> getSmellResultByCustomId(String customId) {
        return smellResultRepository.findByCustomId(customId);
    }

}
