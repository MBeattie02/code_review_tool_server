package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.SecurityResultRepository;
import com.example.serverside.mongoDB.document.SecurityResultDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing security analysis results.
 * This service interfaces with the SecurityResultRepository to
 * retrieve, update, or delete security result documents.
 */
@Service
public class SecurityResultService {

    /**
     * Repository for accessing security result documents.
     */
    private final SecurityResultRepository securityResultRepository;

    /**
     * Constructs a new SecurityResultService with the specified repository.
     *
     * @param securityResultRepository The repository used for retrieving security result documents.
     */
    @Autowired
    public SecurityResultService(SecurityResultRepository securityResultRepository) {
        this.securityResultRepository = securityResultRepository;
    }

    /**
     * Retrieves all security result documents from the repository.
     *
     * @return A list of all security result documents.
     */
    public List<SecurityResultDocument> getAllSecurityResults() {
        return securityResultRepository.findAll();
    }

    /**
     * Retrieves a security result document by its ID.
     *
     * @param id The ID of the security result document.
     * @return An Optional containing the security result document if found, or an empty Optional otherwise.
     */
    public Optional<SecurityResultDocument> getSecurityResultById(String id) {
        return securityResultRepository.findById(id);
    }

    /**
     * Retrieves security result documents that match a specific repository path.
     *
     * @param path The repository path to search for in the security results.
     * @return A list of security result documents that match the given path.
     */
    public List<SecurityResultDocument> getSecurityResultByPath(String path) {
        return securityResultRepository.findByRepositoryPath(path);
    }

    /**
     * Retrieves security result documents that match a specific customId.
     *
     * @param customId The repository path to search for in the security results.
     * @return A list of security result documents that match the given customId.
     */
    public Optional<SecurityResultDocument> getSecurityResultByCustomId(String customId) {
        return securityResultRepository.findByCustomId(customId);
    }
}
