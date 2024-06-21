package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.QualityResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for QualityResultDocument.
 * Extends MongoRepository to provide custom query methods for QualityResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting quality result documents.
 */
public interface QualityResultRepository extends MongoRepository<QualityResultDocument, String> {

    /**
     * Custom query method to find QualityResultDocument objects by a specific repository path.
     * This method is used for fetching quality analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the quality results.
     * @return A list of QualityResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<QualityResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find QualityResultDocument objects by a specific customId.
     * This method is used for fetching quality analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the quality results.
     * @return A list of QualityResultDocument objects that match the given customId.
     */
    Optional<QualityResultDocument> findByCustomId(String customId);
}
