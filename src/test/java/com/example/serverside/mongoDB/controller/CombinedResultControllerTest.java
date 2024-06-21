package com.example.serverside.mongoDB.controller;

import com.example.serverside.analysis.result.CombinedResult;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CombinedResultControllerTest {

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
    private CombinedResultController combinedResultController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetCombinedResultByCustomId_Found() {
        String testId = "testId";
        when(securityResultService.getSecurityResultByCustomId(testId)).thenReturn(Optional.of(new SecurityResultDocument()));
        when(complexityResultService.getComplexityResultByCustomId(testId)).thenReturn(Optional.of(new ComplexityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CombinedResult);
    }

    @Test
    void testGetCombinedResultByCustomId_NotFound() {
        String testId = "testId";
        when(securityResultService.getSecurityResultByCustomId(testId)).thenReturn(Optional.empty());
        when(complexityResultService.getComplexityResultByCustomId(testId)).thenReturn(Optional.empty());

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testGetCombinedResultByCustomId_AllServicesReturnEmpty() {
        String testId = "testId";
        mockAllServicesToReturnEmpty();

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }


    @Test
    void testGetCombinedResultByCustomId_OnlySecurityResultFound() {
        String testId = "uniqueSecurityId";
        mockAllServicesToReturnEmpty();

        when(securityResultService.getSecurityResultByCustomId(testId)).thenReturn(Optional.of(new SecurityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getSecurityResult());

    }

    @Test
    void testGetCombinedResultByCustomId_OnlyComplexityResultFound() {
        String testId = "uniqueComplexityId";
        mockAllServicesToReturnEmpty();
        when(complexityResultService.getComplexityResultByCustomId(testId)).thenReturn(Optional.of(new ComplexityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getComplexityResult());
    }

    @Test
    void testGetCombinedResultByCustomId_OnlySmellResultFound() {
        String testId = "uniqueSmellId";
        mockAllServicesToReturnEmpty();
        when(smellResultService.getSmellResultByCustomId(testId)).thenReturn(Optional.of(new CodeSmellResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getCodeSmellResult());
    }

    @Test
    void testGetCombinedResultByCustomId_OnlyQualityResultFound() {
        String testId = "uniqueQualityId";
        mockAllServicesToReturnEmpty();
        when(qualityResultService.getQualityResultByCustomId(testId)).thenReturn(Optional.of(new QualityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getQualityResult());
    }

    @Test
    void testGetCombinedResultByCustomId_OnlyStyleResultFound() {
        String testId = "uniqueStyleId";
        mockAllServicesToReturnEmpty();
        when(styleResultService.getStyleResultByCustomId(testId)).thenReturn(Optional.of(new StyleResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getStyleResult());
    }

    @Test
    void testGetCombinedResultByCustomId_OnlyAllResultFound() {
        String testId = "uniqueAllId";
        mockAllServicesToReturnEmptyCustom();
        when(allResultService.getAllResultByCustomId(testId)).thenReturn(Optional.of(new CombinedAnalysisResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultByCustomId(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getCombinedResult());
    }

    private void mockAllServicesToReturnEmptyCustom() {
        when(securityResultService.getSecurityResultByCustomId(anyString())).thenReturn(Optional.empty());
        when(complexityResultService.getComplexityResultByCustomId(anyString())).thenReturn(Optional.empty());
        when(smellResultService.getSmellResultByCustomId(anyString())).thenReturn(Optional.empty());
        when(qualityResultService.getQualityResultByCustomId(anyString())).thenReturn(Optional.empty());
        when(styleResultService.getStyleResultByCustomId(anyString())).thenReturn(Optional.empty());
        when(allResultService.getAllResultByCustomId(anyString())).thenReturn(Optional.empty());
    }

    private void mockAllServicesToReturnEmpty() {
        when(securityResultService.getSecurityResultById(anyString())).thenReturn(Optional.empty());
        when(complexityResultService.getComplexityResultById(anyString())).thenReturn(Optional.empty());
        when(smellResultService.getSmellResultById(anyString())).thenReturn(Optional.empty());
        when(qualityResultService.getQualityResultById(anyString())).thenReturn(Optional.empty());
        when(styleResultService.getStyleResultById(anyString())).thenReturn(Optional.empty());
        when(allResultService.getAllResultById(anyString())).thenReturn(Optional.empty());
    }

    /*
        Tests for getCombinedResultById
     */

    @Test
    void testGetCombinedResultById_Found() {
        String testId = "testId";
        when(securityResultService.getSecurityResultById(testId)).thenReturn(Optional.of(new SecurityResultDocument()));
        when(complexityResultService.getComplexityResultById(testId)).thenReturn(Optional.of(new ComplexityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CombinedResult);
    }

    @Test
    void testGetCombinedResultById_NotFound() {
        String testId = "testId";
        when(securityResultService.getSecurityResultById(testId)).thenReturn(Optional.empty());
        when(complexityResultService.getComplexityResultById(testId)).thenReturn(Optional.empty());

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testGetCombinedResultById_AllServicesReturnEmpty() {
        String testId = "testId";
        mockAllServicesToReturnEmpty();

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }


    @Test
    void testGetCombinedResultById_OnlySecurityResultFound() {
        String testId = "uniqueSecurityId";
        mockAllServicesToReturnEmpty();

        when(securityResultService.getSecurityResultById(testId)).thenReturn(Optional.of(new SecurityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getSecurityResult());


    }

    @Test
    void testGetCombinedResultById_OnlyComplexityResultFound() {
        String testId = "uniqueComplexityId";
        mockAllServicesToReturnEmpty();
        when(complexityResultService.getComplexityResultById(testId)).thenReturn(Optional.of(new ComplexityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getComplexityResult());
    }

    @Test
    void testGetCombinedResultById_OnlySmellResultFound() {
        String testId = "uniqueSmellId";
        mockAllServicesToReturnEmpty();
        when(smellResultService.getSmellResultById(testId)).thenReturn(Optional.of(new CodeSmellResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getCodeSmellResult());
    }

    @Test
    void testGetCombinedResultById_OnlyQualityResultFound() {
        String testId = "uniqueQualityId";
        mockAllServicesToReturnEmpty();
        when(qualityResultService.getQualityResultById(testId)).thenReturn(Optional.of(new QualityResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getQualityResult());
    }

    @Test
    void testGetCombinedResultById_OnlyStyleResultFound() {
        String testId = "uniqueStyleId";
        mockAllServicesToReturnEmpty();
        when(styleResultService.getStyleResultById(testId)).thenReturn(Optional.of(new StyleResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getStyleResult());
    }

    @Test
    void testGetCombinedResultById_OnlyAllResultFound() {
        String testId = "uniqueAllId";
        mockAllServicesToReturnEmpty();
        when(allResultService.getAllResultById(testId)).thenReturn(Optional.of(new CombinedAnalysisResultDocument()));

        ResponseEntity<CombinedResult> response = combinedResultController.getCombinedResultById(testId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getCombinedResult());
    }


}
