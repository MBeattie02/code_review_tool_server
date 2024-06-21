package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.CombinedAnalysisResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for CombinedAnalysisResultDocument.
 * Extends MongoRepository to provide custom query methods for CombinedAnalysisResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting combined result documents.
 */
public interface CombinedAnalysisResultRepository extends MongoRepository<CombinedAnalysisResultDocument, String> {

    /**
     * Custom query method to find CombinedAnalysisResultDocument objects by a specific repository path.
     * This method is used for fetching combined analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the combined results.
     * @return A list of CombinedAnalysisResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<CombinedAnalysisResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find CombinedAnalysisResultDocument objects by a specific customId.
     * This method is used for fetching combined analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the combined results.
     * @return A list of CombinedAnalysisResultDocument objects that match the given customId.
     */
    Optional<CombinedAnalysisResultDocument> findByCustomId(String customId);
}
