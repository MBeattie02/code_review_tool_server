package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.QualityResultRepository;
import com.example.serverside.mongoDB.document.QualityResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing quality analysis results.
 * This service interacts with the QualityResultRepository to
 * perform operations like retrieving, updating, or deleting quality result documents.
 */
@Service
public class QualityResultService {

    /**
     * Repository for accessing quality result documents.
     */
    private final QualityResultRepository qualityResultRepository;

    /**
     * Constructs a new QualityResultService with the specified repository.
     *
     * @param qualityResultRepository The repository used for retrieving quality result documents.
     */
    @Autowired
    public QualityResultService(QualityResultRepository qualityResultRepository) {
        this.qualityResultRepository = qualityResultRepository;
    }

    /**
     * Retrieves all quality result documents from the repository.
     *
     * @return A list of all quality result documents.
     */
    public List<QualityResultDocument> getAllQualityResults() {
        return qualityResultRepository.findAll();
    }

    /**
     * Retrieves a quality result document by its ID.
     *
     * @param id The ID of the quality result document.
     * @return An Optional containing the quality result document if found, or an empty Optional otherwise.
     */
    public Optional<QualityResultDocument> getQualityResultById(String id) {
        return qualityResultRepository.findById(id);
    }

    /**
     * Retrieves quality result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the quality results.
     * @return A list of quality result documents that match the given path.
     */
    public List<QualityResultDocument> getQualityResultByPath(String path) {
        return qualityResultRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves quality result documents that match a specific customId.
     *
     * @param customId The customId to search for in the quality results.
     * @return A list of quality result documents that match the given customId.
     */
    public Optional<QualityResultDocument> getQualityResultByCustomId(String customId) {
        return qualityResultRepository.findByCustomId(customId);
    }
}
