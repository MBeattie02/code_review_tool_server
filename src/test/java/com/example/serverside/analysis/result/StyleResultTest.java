package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleResultTest {

    private StyleResult styleResult;
    private List<String> testViolations;
    private RepositoryInfo testRepositoryInfo;

    @BeforeEach
    void setUp() {
        styleResult = new StyleResult();
        testViolations = Arrays.asList("Violation1", "Violation2");
        testRepositoryInfo = new RepositoryInfo();

    }

    @Test
    void testViolationsGetterSetter() {
        styleResult.setViolations(testViolations);
        assertEquals(testViolations, styleResult.getViolations());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        styleResult.setRepositoryInfo(testRepositoryInfo);
        assertEquals(testRepositoryInfo, styleResult.getRepositoryInfo());
    }

    @Test
    void testIdGetterSetter() {
        styleResult.setId("testId");
        assertEquals("testId", styleResult.getId());
    }

    @Test
    void testViolationCountGetterSetter() {
        styleResult.setViolationCount(2);
        assertEquals(2, styleResult.getViolationCount());
    }

    @Test
    void testToString_NoViolations() {

        styleResult.setCustomId("ID123");
        styleResult.setViolationCount(0);
        styleResult.setViolations(Collections.emptyList());
        String expected = "Code Style Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Violations: '0',\n" +
                "  Violations: [\n";
        assertEquals(expected, styleResult.toString());
    }

    @Test
    void testToString_OneViolation() {
        styleResult.setCustomId("ID123");
        styleResult.setViolationCount(1);
        styleResult.setViolations(Arrays.asList("Violation1"));
        String expected = "Code Style Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Violations: '1',\n" +
                "  Violations: [\n" +
                "    Violation1" ;
        assertEquals(expected, styleResult.toString());
    }

    @Test
    void testToString_MultipleViolations() {
        styleResult.setCustomId("ID123");
        styleResult.setViolationCount(2);
        styleResult.setViolations(Arrays.asList("Violation1", "Violation2"));
        String expected = "Code Style Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Violations: '2',\n" +
                "  Violations: [\n" +
                "    Violation1,\n" +
                "    Violation2" ;
        assertEquals(expected, styleResult.toString());
    }

    @Test
    void testToString2_NoViolations() {
        styleResult.setViolationCount(0);
        styleResult.setViolations(Collections.emptyList());
        String expected = "Code Style Analysis Result:\n" +
                "  Number of Violations: '0',\n" +
                "  Violations: [\n" +
                "\n  ]";
        assertEquals(expected, styleResult.toString2());
    }

    @Test
    void testToString2_OneViolation() {
        styleResult.setViolationCount(1);
        styleResult.setViolations(Arrays.asList("Violation1"));
        String expected = "Code Style Analysis Result:\n" +
                "  Number of Violations: '1',\n" +
                "  Violations: [\n" +
                "    Violation1\n  ]";
        assertEquals(expected, styleResult.toString2());
    }

    @Test
    void testToString2_MultipleViolations() {
        styleResult.setViolationCount(2);
        styleResult.setViolations(Arrays.asList("Violation1", "Violation2"));
        String expected = "Code Style Analysis Result:\n" +
                "  Number of Violations: '2',\n" +
                "  Violations: [\n" +
                "    Violation1,\n" +
                "    Violation2\n  ]";
        assertEquals(expected, styleResult.toString2());
    }

}

