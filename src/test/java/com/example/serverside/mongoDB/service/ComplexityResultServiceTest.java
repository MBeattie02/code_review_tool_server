package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.ComplexityResultRepository;
import com.example.serverside.mongoDB.document.ComplexityResultDocument;
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

public class ComplexityResultServiceTest {

    @Mock
    private ComplexityResultRepository complexityResultRepository;

    @InjectMocks
    private ComplexityResultService complexityResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllComplexityResults() {
        ComplexityResultDocument document1 = new ComplexityResultDocument();
        ComplexityResultDocument document2 = new ComplexityResultDocument();
        List<ComplexityResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(complexityResultRepository.findAll()).thenReturn(expectedResults);

        List<ComplexityResultDocument> results = complexityResultService.getAllComplexityResults();

        assertEquals(expectedResults, results);
        verify(complexityResultRepository, times(1)).findAll();
    }

    @Test
    void testGetComplexityResultById() {
        String testId = "testId";
        Optional<ComplexityResultDocument> expectedResult = Optional.of(new ComplexityResultDocument());

        when(complexityResultRepository.findById(testId)).thenReturn(expectedResult);

        Optional<ComplexityResultDocument> result = complexityResultService.getComplexityResultById(testId);

        assertEquals(expectedResult, result);
        verify(complexityResultRepository, times(1)).findById(testId);
    }

    @Test
    void testGetComplexityResultPath_ReturnsResults() {
        String path = "test/path";
        List<ComplexityResultDocument> expectedResults = Arrays.asList(new ComplexityResultDocument(), new ComplexityResultDocument());
        when(complexityResultRepository.findByRepositoryPath(path)).thenReturn(expectedResults);

        List<ComplexityResultDocument> actualResults = complexityResultService.getComplexityResultPath(path);

        assertEquals(expectedResults, actualResults, "The returned results should match the expected results.");
        verify(complexityResultRepository).findByRepositoryPath(path);
    }

    @Test
    void getComplexityResultByCustomId_ReturnsResult() {
        String customId = "customId123";
        Optional<ComplexityResultDocument> expectedResult = Optional.of(new ComplexityResultDocument());
        when(complexityResultRepository.findByCustomId(customId)).thenReturn(expectedResult);

        Optional<ComplexityResultDocument> actualResult = complexityResultService.getComplexityResultByCustomId(customId);

        assertEquals(expectedResult, actualResult, "The returned result should match the expected result.");
        verify(complexityResultRepository).findByCustomId(customId);
    }
}
