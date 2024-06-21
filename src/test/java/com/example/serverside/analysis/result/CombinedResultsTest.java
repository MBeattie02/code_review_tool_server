package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CombinedResultsTest {

    private CombinedResults combinedResults;
    private QualityResult qualityResult;
    private CodeSmellResult codeSmellResult;
    private SecurityResult securityResult;
    private ComplexityResult complexityResult;
    private StyleResult styleResult;
    private RepositoryInfo repositoryInfo;
    private String customId = "customTestId";

    CombinedResults combined = new CombinedResults(qualityResult, codeSmellResult, securityResult, complexityResult, styleResult, repositoryInfo);

    @BeforeEach
    void setUp() {
        // Mocking the dependent objects
        qualityResult = mock(QualityResult.class);
        codeSmellResult = mock(CodeSmellResult.class);
        securityResult = mock(SecurityResult.class);
        complexityResult = mock(ComplexityResult.class);
        styleResult = mock(StyleResult.class);

        // Setting up dummy return values for toString2 method
        when(qualityResult.toString2()).thenReturn("QualityResultString");
        when(codeSmellResult.toString2()).thenReturn("CodeSmellResultString");
        when(securityResult.toString2()).thenReturn("SecurityResultString");
        when(complexityResult.toString2()).thenReturn("ComplexityResultString");
        when(styleResult.toString2()).thenReturn("StyleResultString");

        // Instantiating CombinedResults and setting its fields
        combinedResults = new CombinedResults();
        combinedResults.setCustomId(customId);
        combinedResults.setQualityResult(qualityResult);
        combinedResults.setCodeSmellResult(codeSmellResult);
        combinedResults.setSecurityResult(securityResult);
        combinedResults.setComplexityResult(complexityResult);
        combinedResults.setStyleResult(styleResult);
    }

    @Test
    void testGettersAndSetters() {
        combinedResults.setQualityResult(qualityResult);
        combinedResults.setCodeSmellResult(codeSmellResult);
        combinedResults.setSecurityResult(securityResult);
        combinedResults.setComplexityResult(complexityResult);
        combinedResults.setStyleResult(styleResult);
        combinedResults.setRepositoryInfo(repositoryInfo);
        combinedResults.setId("testId");

        assertEquals(qualityResult, combinedResults.getQualityResult());
        assertEquals(codeSmellResult, combinedResults.getCodeSmellResult());
        assertEquals(securityResult, combinedResults.getSecurityResult());
        assertEquals(complexityResult, combinedResults.getComplexityResult());
        assertEquals(styleResult, combinedResults.getStyleResult());
        assertEquals(repositoryInfo, combinedResults.getRepositoryInfo());
        assertEquals("testId", combinedResults.getId());
    }

    @Test
    void testToString() {
        // Building the expected string
        String expected = "Code Analysis Result:\n" +
                "  Custom ID: '" + customId + "',\n" +
                "  Quality Results: 'QualityResultString',\n" +
                "  Smells Results: 'CodeSmellResultString',\n" +
                "  Complexity Results: 'ComplexityResultString',\n" +
                "  Security Results: 'SecurityResultString',\n" +
                "  Style Results: 'StyleResultString',\n";

        // Asserting the expected string with actual
        assertEquals(expected, combinedResults.toString());
    }
}
