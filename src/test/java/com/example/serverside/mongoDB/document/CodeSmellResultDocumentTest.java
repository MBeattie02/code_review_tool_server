package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeSmellResultDocumentTest {

    private CodeSmellResultDocument document;
    private List<String> testSmells;
    private RepositoryInfo repositoryInfo;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        document = new CodeSmellResultDocument();
        testSmells = Arrays.asList("Smell1", "Smell2");
        repositoryInfo = new RepositoryInfo();
        testTimestamp = LocalDateTime.now();

    }

    @Test
    void testSmellsGetterSetter() {
        document.setSmells(testSmells);
        assertEquals(testSmells, document.getSmells());
    }

    @Test
    void testCustomIdGetterSetter() {
        String customId = "MBeattie02-TestRepoDemo-XSS.java-2024-01-27T19:06:35.077423";
        document.setCustomId(customId);
        assertEquals(customId, document.getCustomId());
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
    void testSmellsCountGetterSetter() {
        int smellsCount = 5;
        document.setSmellsCount(smellsCount);
        assertEquals(smellsCount, document.getSmellsCount());
    }

    @Test
    void testIdGetterSetter() {
        String testId = null;
        assertEquals(testId, document.getId());
    }
}
