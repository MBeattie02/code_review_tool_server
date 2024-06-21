package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QualityResultTest {

    private QualityResult qualityResult;
    private List<String> testDuplications;
    private RepositoryInfo testRepositoryInfo;

    @BeforeEach
    void setUp() {
        qualityResult = new QualityResult();
        testDuplications = Arrays.asList("Duplication1", "Duplication2");
        testRepositoryInfo = new RepositoryInfo();
    }

    @Test
    void testDuplicationsGetterSetter() {
        qualityResult.setDuplications(testDuplications);
        assertEquals(testDuplications, qualityResult.getDuplications());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        qualityResult.setRepositoryInfo(testRepositoryInfo);
        assertEquals(testRepositoryInfo, qualityResult.getRepositoryInfo());
    }

    @Test
    void testIdGetterSetter() {
        qualityResult.setId("testId");
        assertEquals("testId", qualityResult.getId());
    }

    @Test
    void testQualityCountGetterSetter() {
        qualityResult.setQualityCount(5);
        assertEquals(5, qualityResult.getQualityCount());
    }

    @Test
    void testToString2_NoIssues() {
        qualityResult.setQualityCount(0);
        qualityResult.setDuplications(Collections.emptyList());
        String expected = "Code Quality Analysis Result:\n" +
                "  Number of Issues: '0',\n" +
                "  Issues: [\n" +
                "\n  ]";
        assertEquals(expected, qualityResult.toString2());
    }

    @Test
    void testToString_NoIssues() {

        qualityResult.setCustomId("ID123");
        qualityResult.setQualityCount(0);
        qualityResult.setDuplications(Collections.emptyList());
        String expected = "Code Quality Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Issues: '0',\n" +
                "  Issues: [\n";
        assertEquals(expected, qualityResult.toString());
    }
}
