package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexityResultTest {

    private ComplexityResult complexityResult;
    private RepositoryInfo repositoryInfo;

    @BeforeEach
    void setUp() {
        complexityResult = new ComplexityResult(10); // Example cyclomatic complexity
        repositoryInfo = new RepositoryInfo();

    }

    @Test
    void testCyclomaticComplexityGetterSetter() {
        // Test setting and getting cyclomatic complexity
        complexityResult.setCyclomaticComplexity(15);
        assertEquals(15, complexityResult.getCyclomaticComplexity());
    }

    @Test
    void testRepositoryInfoGetterSetter() {
        // Test setting and getting repository information
        complexityResult.setRepositoryInfo(repositoryInfo);
        assertEquals(repositoryInfo, complexityResult.getRepositoryInfo());
    }

    @Test
    void testIdGetterSetter() {
        // Test setting and getting the ID
        complexityResult.setId("12345");
        assertEquals("12345", complexityResult.getId());
    }
}
