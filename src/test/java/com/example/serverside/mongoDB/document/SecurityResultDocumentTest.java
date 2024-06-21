package com.example.serverside.mongoDB.document;


import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityResultDocumentTest {

    private SecurityResultDocument document;
    private List<String> testVulnerabilities;
    private RepositoryInfo repositoryInfo;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        document = new SecurityResultDocument();
        testVulnerabilities = Arrays.asList("Vulnerability1", "Vulnerability2");
        repositoryInfo = new RepositoryInfo();
        testTimestamp = LocalDateTime.now();

    }

    @Test
    void testVulnerabilitiesGetterSetter() {
        document.setVulnerabilities(testVulnerabilities);
        assertEquals(testVulnerabilities, document.getVulnerabilities());
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
    void testVulnerabilitiesCountGetterSetter() {
        int testVulnerabilitiesCount = 5;
        document.setVulnerabilitiesCount(testVulnerabilitiesCount);
        assertEquals(testVulnerabilitiesCount, document.getVulnerabilitiesCount());
    }

    @Test
    void testIdGetterSetter() {
        String testId = null;
        document.setId(testId);
        assertEquals(testId, document.getId());
    }
}
