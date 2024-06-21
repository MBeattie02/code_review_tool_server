package com.example.serverside.analysis.repository;

import com.example.serverside.mongoDB.document.CodeSmellResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for CodeSmellResultDocument.
 * Extends MongoRepository to provide custom query methods for CodeSmellResultDocument objects.
 * This interface allows for operations such as retrieving, updating, or deleting code smell result documents.
 */
public interface SmellResultRepository extends MongoRepository<CodeSmellResultDocument, String> {

    /**
     * Custom query method to find CodeSmellResultDocument objects by a specific repository path.
     * This method is used for fetching code smell analysis results that are associated with a particular path in a repository.
     *
     * @param path The repository path to search for in the code smell results.
     * @return A list of CodeSmellResultDocument objects that match the given repository path.
     */
    @Query("{'repositoryInfo.path': ?0}")
    List<CodeSmellResultDocument> findByRepositoryPath(String path);

    /**
     * Custom method to find CodeSmellResultDocument objects by a specific customId.
     * This method is used for fetching smell analysis results that are associated with a particular customId in a repository.
     *
     * @param customId The repository path to search for in the smell results.
     * @return A list of CodeSmellResultDocument objects that match the given customId.
     */
    Optional<CodeSmellResultDocument> findByCustomId(String customId);
}
