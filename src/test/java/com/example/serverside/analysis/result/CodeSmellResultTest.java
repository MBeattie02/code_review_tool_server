package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeSmellResultTest {

    private CodeSmellResult codeSmellResult;
    private List<String> testSmells;
    private RepositoryInfo testRepositoryInfo;

    @BeforeEach
    void setUp() {
        codeSmellResult = new CodeSmellResult();
        testSmells = Arrays.asList("Smell1", "Smell2");
        testRepositoryInfo = new RepositoryInfo();
        // Initialize testRepositoryInfo with some test data
    }

    @Test
    void testGettersAndSetters() {
        codeSmellResult.setSmells(testSmells);
        codeSmellResult.setRepositoryInfo(testRepositoryInfo);
        codeSmellResult.setId("testId");
        codeSmellResult.setSmellsCount(2);

        assertEquals(testSmells, codeSmellResult.getSmells());
        assertEquals(testRepositoryInfo, codeSmellResult.getRepositoryInfo());
        assertEquals("testId", codeSmellResult.getId());
        assertEquals(2, codeSmellResult.getSmellsCount());
    }

    @Test
    void testToString2_NoSmells() {
        codeSmellResult.setSmellsCount(0);
        codeSmellResult.setSmells(Collections.emptyList());
        String expected = "Code Smell Analysis Result:\n" +
                "  Number of Smells: '0',\n" +
                "  Smells: [\n" +
                "\n  ]";
        assertEquals(expected, codeSmellResult.toString2());
    }

    @Test
    void testToString_NoSmells() {

        codeSmellResult.setCustomId("ID123");
        codeSmellResult.setSmellsCount(0);
        codeSmellResult.setSmells(Collections.emptyList());
        String expected = "Code Smell Analysis Result:\n" +
                "  Custom ID: 'ID123',\n" +
                "  Number of Smells: '0',\n" +
                "  Smells: [\n";
        assertEquals(expected, codeSmellResult.toString());
    }
}
