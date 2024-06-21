package com.example.serverside.mongoDB.controller;

import com.example.serverside.analysis.result.CombinedResultsList;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AllDatabaseResultsControllerTest {

    @Mock
    private SecurityResultService securityResultService;
    @Mock
    private ComplexityResultService complexityResultService;
    @Mock
    private SmellResultService smellResultService;
    @Mock
    private QualityResultService qualityResultService;
    @Mock
    private StyleResultService styleResultService;
    @Mock
    private AllResultService allResultService;

    @InjectMocks
    private AllDatabaseResultsController allDatabaseResultsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSecurityResults() {
        SecurityResultDocument securityResult = new SecurityResultDocument();
        List<SecurityResultDocument> results = Arrays.asList(securityResult);
        when(securityResultService.getAllSecurityResults()).thenReturn(results);

        ResponseEntity<List<SecurityResultDocument>> response = allDatabaseResultsController.getAllSecurityResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(results, response.getBody());
    }

    @Test
    void testGetAllComplexityResults() {
        ComplexityResultDocument complexityResult = new ComplexityResultDocument();
        List<ComplexityResultDocument> results = Arrays.asList(complexityResult);
        when(complexityResultService.getAllComplexityResults()).thenReturn(results);

        ResponseEntity<List<ComplexityResultDocument>> response = allDatabaseResultsController.getAllComplexityResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(results, response.getBody());
    }

    @Test
    void testGetAllStyleResults() {
        StyleResultDocument styleResult = new StyleResultDocument();
        List<StyleResultDocument> results = Arrays.asList(styleResult);
        when(styleResultService.getAllStyleResults()).thenReturn(results);

        ResponseEntity<List<StyleResultDocument>> response = allDatabaseResultsController.getAllStyleResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(results, response.getBody());
    }

    @Test
    void testGetAllSmellResults() {
        CodeSmellResultDocument smellResult = new CodeSmellResultDocument();
        List<CodeSmellResultDocument> results = Arrays.asList(smellResult);
        when(smellResultService.getAllSmellResults()).thenReturn(results);

        ResponseEntity<List<CodeSmellResultDocument>> response = allDatabaseResultsController.getAllSmellResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(results, response.getBody());
    }

    @Test
    void testGetAllQualityResults() {
        QualityResultDocument qualityResult = new QualityResultDocument();
        List<QualityResultDocument> results = Arrays.asList(qualityResult);
        when(qualityResultService.getAllQualityResults()).thenReturn(results);

        ResponseEntity<List<QualityResultDocument>> response = allDatabaseResultsController.getAllQualityResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(results, response.getBody());
    }

    @Test
    void testGetAllResults() {
        // Mocking results for all services
        when(securityResultService.getAllSecurityResults()).thenReturn(Arrays.asList(new SecurityResultDocument()));
        when(complexityResultService.getAllComplexityResults()).thenReturn(Arrays.asList(new ComplexityResultDocument()));
        when(smellResultService.getAllSmellResults()).thenReturn(Arrays.asList(new CodeSmellResultDocument()));
        when(qualityResultService.getAllQualityResults()).thenReturn(Arrays.asList(new QualityResultDocument()));
        when(styleResultService.getAllStyleResults()).thenReturn(Arrays.asList(new StyleResultDocument()));
        when(allResultService.getAllResults()).thenReturn(Arrays.asList(new CombinedAnalysisResultDocument()));

        ResponseEntity<CombinedResultsList> response = allDatabaseResultsController.getAllResults();

        assertEquals(200, response.getStatusCodeValue());
    }
}
