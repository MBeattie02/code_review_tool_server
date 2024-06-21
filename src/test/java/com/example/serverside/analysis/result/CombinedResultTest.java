package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.document.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CombinedResultTest {

    private CombinedResult combinedResult;
    private SecurityResultDocument securityResult;
    private ComplexityResultDocument complexityResult;
    private CodeSmellResultDocument codeSmellResult;
    private QualityResultDocument qualityResult;
    private StyleResultDocument styleResult;
    private CombinedAnalysisResultDocument combinedResults;

    @BeforeEach
    void setUp() {
        combinedResult = new CombinedResult();
        securityResult = new SecurityResultDocument();
        complexityResult = new ComplexityResultDocument();
        codeSmellResult = new CodeSmellResultDocument();
        qualityResult = new QualityResultDocument();
        styleResult = new StyleResultDocument();
        combinedResults = new CombinedAnalysisResultDocument();

    }

    @Test
    void testSecurityResultGetterSetter() {
        combinedResult.setSecurityResult(securityResult);
        assertEquals(securityResult, combinedResult.getSecurityResult());
    }

    @Test
    void testComplexityResultGetterSetter() {
        combinedResult.setComplexityResult(complexityResult);
        assertEquals(complexityResult, combinedResult.getComplexityResult());
    }

    @Test
    void testCodeSmellResultGetterSetter() {
        combinedResult.setCodeSmellResult(codeSmellResult);
        assertEquals(codeSmellResult, combinedResult.getCodeSmellResult());
    }

    @Test
    void testQualityResultGetterSetter() {
        combinedResult.setQualityResult(qualityResult);
        assertEquals(qualityResult, combinedResult.getQualityResult());
    }

    @Test
    void testStyleResultGetterSetter() {
        combinedResult.setStyleResult(styleResult);
        assertEquals(styleResult, combinedResult.getStyleResult());
    }

    @Test
    void testCombinedResultsGetterSetter() {
        combinedResult.setCombinedResult(combinedResults);
        assertEquals(combinedResults, combinedResult.getCombinedResult());
    }
}
