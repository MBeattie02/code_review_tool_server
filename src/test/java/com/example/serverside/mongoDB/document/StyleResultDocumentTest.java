package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleResultDocumentTest {

    private StyleResultDocument document;
    private List<String> testViolations;
    private RepositoryInfo repositoryInfo;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        document = new StyleResultDocument();
        testViolations = Arrays.asList("Violation1", "Violation2");
        repositoryInfo = new RepositoryInfo();
        testTimestamp = LocalDateTime.now();

    }

    @Test
    void testCustomIdGetterSetter() {
        String customId = "MBeattie02-TestRepoDemo-XSS.java-2024-01-27T19:06:35.077423";
        document.setCustomId(customId);
        assertEquals(customId, document.getCustomId());
    }

    @Test
    void testViolationsGetterSetter() {
        document.setViolations(testViolations);
        assertEquals(testViolations, document.getViolations());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        document.setRepositoryInfo(repositoryInfo);
        assertEquals(repositoryInfo, document.getRepositoryInfo());
    }

    @Test
    void testTimestampGetterSetter() {
        document.setTimestamp(testTimestamp);
        assertEquals(testTimestamp, document.getTimestamp());
    }

    @Test
    void testViolationCountGetterSetter() {
        int testViolationCount = 5;
        document.setViolationCount(testViolationCount);
        assertEquals(testViolationCount, document.getViolationCount());
    }

    @Test
    void testIdGetterSetter() {
        String testId = null;
        assertEquals(testId, document.getId());
    }
}

