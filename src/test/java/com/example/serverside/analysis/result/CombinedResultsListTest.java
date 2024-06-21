package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.document.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CombinedResultsListTest {

    private CombinedResultsList combinedResultsList;
    private List<SecurityResultDocument> securityResults;
    private List<ComplexityResultDocument> complexityResults;
    private List<CodeSmellResultDocument> codeSmellResults;
    private List<QualityResultDocument> qualityResults;
    private List<StyleResultDocument> styleResults;
    private List<CombinedAnalysisResultDocument> combinedResults;

    @BeforeEach
    void setUp() {
        combinedResultsList = new CombinedResultsList();
        securityResults = Arrays.asList(new SecurityResultDocument());
        complexityResults = Arrays.asList(new ComplexityResultDocument());
        codeSmellResults = Arrays.asList(new CodeSmellResultDocument());
        qualityResults = Arrays.asList(new QualityResultDocument());
        styleResults = Arrays.asList(new StyleResultDocument());
        combinedResults = Arrays.asList(new CombinedAnalysisResultDocument());
    }

    @Test
    void testGettersAndSetters() {
        combinedResultsList.setSecurityResults(securityResults);
        combinedResultsList.setComplexityResults(complexityResults);
        combinedResultsList.setCodeSmellResults(codeSmellResults);
        combinedResultsList.setQualityResults(qualityResults);
        combinedResultsList.setStyleResults(styleResults);
        combinedResultsList.setCombinedResults(combinedResults);

        assertEquals(securityResults, combinedResultsList.getSecurityResults());
        assertEquals(complexityResults, combinedResultsList.getComplexityResults());
        assertEquals(codeSmellResults, combinedResultsList.getCodeSmellResults());
        assertEquals(qualityResults, combinedResultsList.getQualityResults());
        assertEquals(styleResults, combinedResultsList.getStyleResults());
        assertEquals(combinedResults, combinedResultsList.getCombinedResults());
    }
}
