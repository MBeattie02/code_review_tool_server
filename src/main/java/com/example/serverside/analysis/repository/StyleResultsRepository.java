package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.StyleResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for StyleResultDocument.
 * Extends MongoRepository to provide custom query methods for StyleResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting style result documents.
 */
public interface StyleResultsRepository extends MongoRepository<StyleResultDocument, String> {

    /**
     * Custom query method to find StyleResultDocument objects by a specific repository path.
     * This method is used for fetching style analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the style results.
     * @return A list of StyleResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<StyleResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find StyleResultDocument objects by a specific customId.
     * This method is used for fetching style analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the style results.
     * @return A list of StyleResultDocument objects that match the given customId.
     */
    Optional<StyleResultDocument> findByCustomId(String customId);
}
