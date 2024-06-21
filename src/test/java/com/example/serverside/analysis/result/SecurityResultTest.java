package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityResultTest {

    private SecurityResult securityResult;
    private List<String> testVulnerabilities;
    private RepositoryInfo testRepositoryInfo;

    @BeforeEach
    void setUp() {
        securityResult = new SecurityResult();
        testVulnerabilities = Arrays.asList("Vulnerability1", "Vulnerability2");
        testRepositoryInfo = new RepositoryInfo();

    }

    @Test
    void testVulnerabilitiesGetterSetter() {
        securityResult.setVulnerabilities(testVulnerabilities);
        assertEquals(testVulnerabilities, securityResult.getVulnerabilities());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        securityResult.setRepositoryInfo(testRepositoryInfo);
        assertEquals(testRepositoryInfo, securityResult.getRepositoryInfo());
    }

    @Test
    void testIdGetterSetter() {
        securityResult.setId("testId");
        assertEquals("testId", securityResult.getId());
    }

    @Test
    void testVulnerabilitiesCountGetterSetter() {
        securityResult.setVulnerabilitiesCount(3);
        assertEquals(3, securityResult.getVulnerabilitiesCount());
    }

    @Test
    void testToString2_NoVulnerabilities() {
        securityResult.setVulnerabilitiesCount(0);
        securityResult.setVulnerabilities(Collections.emptyList());
        String expected = "Code Security Analysis Result:\n" +
                "  Number of Vulnerabilities: '0',\n" +
                "  Vulnerabilities: [\n" +
                "\n  ]";
        assertEquals(expected, securityResult.toString2());
    }

    @Test
    void testToString_NoVulnerabilities() {

        securityResult.setCustomId("ID123");
        securityResult.setVulnerabilitiesCount(0);
        securityResult.setVulnerabilities(Collections.emptyList());
        String expected = "Code Security Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Vulnerabilities: '0',\n" +
                "  Vulnerabilities: [\n";
        assertEquals(expected, securityResult.toString());
    }
}
