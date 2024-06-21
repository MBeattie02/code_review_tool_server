package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.SecurityResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for SecurityResultDocument.
 * Extends MongoRepository to provide custom query methods for SecurityResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting security result documents.
 */
public interface SecurityResultRepository extends MongoRepository<SecurityResultDocument, String> {

    /**
     * Custom query method to find SecurityResultDocument objects by a specific repository path.
     * This method is used for fetching security analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the security results.
     * @return A list of SecurityResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<SecurityResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find SecurityResultDocument objects by a specific customId.
     * This method is used for fetching security analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the security results.
     * @return A list of SecurityResultDocument objects that match the given customId.
     */
    Optional<SecurityResultDocument> findByCustomId(String customId);
}
