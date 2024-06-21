package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexityResultDocumentTest {

    private ComplexityResultDocument document;
    private RepositoryInfo repositoryInfo;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        document = new ComplexityResultDocument();
        repositoryInfo = new RepositoryInfo();
        testTimestamp = LocalDateTime.now();

    }

    @Test
    void testTimestampGetterSetter() {
        document.setTimestamp(testTimestamp);
        assertEquals(testTimestamp, document.getTimestamp());
    }

    @Test
    void testCustomIdGetterSetter() {
        String customId = "MBeattie02-TestRepoDemo-XSS.java-2024-01-27T19:06:35.077423";
        document.setCustomId(customId);
        assertEquals(customId, document.getCustomId());
    }

    @Test
    void testIdGetterSetter() {
        String testId = "testId";
        document.setId(testId);
        assertEquals(testId, document.getId());
    }

    @Test
    void testCyclomaticComplexityGetterSetter() {
        int testComplexity = 5;
        document.setCyclomaticComplexity(testComplexity);
        assertEquals(testComplexity, document.getCyclomaticComplexity());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        document.setRepositoryInfo(repositoryInfo);
        assertEquals(repositoryInfo, document.getRepositoryInfo());
    }
}
