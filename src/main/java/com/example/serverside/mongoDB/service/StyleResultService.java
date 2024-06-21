package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.StyleResultsRepository;
import com.example.serverside.mongoDB.document.StyleResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing style analysis results.
 * This service provides methods to retrieve style result documents
 * from a style results repository.
 */
@Service
public class StyleResultService {

    /**
     * Repository for accessing style result documents.
     */
    private final StyleResultsRepository styleResultsRepository;

    /**
     * Constructs a new StyleResultService with the specified repository.
     *
     * @param styleResultsRepository The repository used for retrieving style result documents.
     */
    @Autowired
    public StyleResultService(StyleResultsRepository styleResultsRepository) {
        this.styleResultsRepository = styleResultsRepository;
    }

    /**
     * Retrieves all style result documents from the repository.
     *
     * @return A list of all style result documents.
     */
    public List<StyleResultDocument> getAllStyleResults() {
        return styleResultsRepository.findAll();
    }

    /**
     * Retrieves a style result document by its ID.
     *
     * @param id The ID of the style result document.
     * @return An Optional containing the style result document if found, or an empty Optional otherwise.
     */
    public Optional<StyleResultDocument> getStyleResultById(String id) {
        return styleResultsRepository.findById(id);
    }

    /**
     * Retrieves style result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the style results.
     * @return A list of style result documents that match the given path.
     */
    public List<StyleResultDocument> getStyleResultByPath(String path) {
        return styleResultsRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves style result documents that match a specific customId.
     *
     * @param customId The customId path to search for in the style results.
     * @return A list of style result documents that match the given customId.
     */
    public Optional<StyleResultDocument> getStyleResultByCustomId(String customId) {
        return styleResultsRepository.findByCustomId(customId);
    }
}
