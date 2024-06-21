package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.ComplexityResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for ComplexityResultDocument.
 * Extends MongoRepository to provide custom query methods for ComplexityResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting complexity result documents.
 */
public interface ComplexityResultRepository extends MongoRepository<ComplexityResultDocument, String> {

    /**
     * Custom query method to find ComplexityResultDocument objects by a specific repository path.
     * This method is used for fetching complexity analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the complexity results.
     * @return A list of ComplexityResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<ComplexityResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find ComplexityResultDocument objects by a specific customId.
     * This method is used for fetching complexity analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the complexity results.
     * @return A list of ComplexityResultDocument objects that match the given customId.
     */
    Optional<ComplexityResultDocument> findByCustomId(String customId);
}
