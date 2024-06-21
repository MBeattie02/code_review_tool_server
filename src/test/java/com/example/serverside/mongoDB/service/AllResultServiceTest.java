package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.CombinedAnalysisResultRepository;
import com.example.serverside.mongoDB.document.CombinedAnalysisResultDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AllResultServiceTest {

    @Mock
    private CombinedAnalysisResultRepository combinedAnalysisResultRepository;

    @InjectMocks
    private AllResultService allResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllResults() {
        CombinedAnalysisResultDocument document1 = new CombinedAnalysisResultDocument();
        CombinedAnalysisResultDocument document2 = new CombinedAnalysisResultDocument();
        List<CombinedAnalysisResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(combinedAnalysisResultRepository.findAll()).thenReturn(expectedResults);

        List<CombinedAnalysisResultDocument> results = allResultService.getAllResults();

        assertEquals(expectedResults, results);
        verify(combinedAnalysisResultRepository, times(1)).findAll();
    }

    @Test
    void testGetAllResultById() {
        String testId = "testId";
        Optional<CombinedAnalysisResultDocument> expectedResult = Optional.of(new CombinedAnalysisResultDocument());

        when(combinedAnalysisResultRepository.findById(testId)).thenReturn(expectedResult);

        Optional<CombinedAnalysisResultDocument> result = allResultService.getAllResultById(testId);

        assertEquals(expectedResult, result);
        verify(combinedAnalysisResultRepository, times(1)).findById(testId);
    }

    @Test
    void testGetAllResultByPath_ReturnsCorrectDocuments() {
        String path = "some/path";
        List<CombinedAnalysisResultDocument> expectedDocuments = Arrays.asList(new CombinedAnalysisResultDocument(), new CombinedAnalysisResultDocument());

        when(combinedAnalysisResultRepository.findByRepositoryPath(path)).thenReturn(expectedDocuments);

        List<CombinedAnalysisResultDocument> actualDocuments = allResultService.getAllResultByPath(path);

        assertEquals(expectedDocuments, actualDocuments);
        verify(combinedAnalysisResultRepository).findByRepositoryPath(path);
    }

    @Test
    void testGetAllResultByCustomId_ReturnsCorrectDocument() {
        String customId = "12345";
        Optional<CombinedAnalysisResultDocument> expectedDocument = Optional.of(new CombinedAnalysisResultDocument());

        when(combinedAnalysisResultRepository.findByCustomId(customId)).thenReturn(expectedDocument);

        Optional<CombinedAnalysisResultDocument> actualDocument = allResultService.getAllResultByCustomId(customId);

        assertEquals(expectedDocument, actualDocument);
        verify(combinedAnalysisResultRepository).findByCustomId(customId);
    }
}
