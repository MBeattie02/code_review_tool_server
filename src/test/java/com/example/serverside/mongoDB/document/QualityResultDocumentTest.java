package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QualityResultDocumentTest {

    private QualityResultDocument document;
    private List<String> testDuplications;
    private RepositoryInfo repositoryInfo;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        document = new QualityResultDocument();
        testDuplications = Arrays.asList("Duplication1", "Duplication2");
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
    void testDuplicationsGetterSetter() {
        document.setDuplications(testDuplications);
        assertEquals(testDuplications, document.getDuplications());
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
    void testQualityCountGetterSetter() {
        int testQualityCount = 5;
        document.setQualityCount(testQualityCount);
        assertEquals(testQualityCount, document.getQualityCount());
    }

    @Test
    void testIdGetterSetter() {
        String testId = null;
        assertEquals(testId, document.getId());
    }
}
