package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.SmellResultRepository;
import com.example.serverside.mongoDB.document.CodeSmellResultDocument;
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

public class SmellResultServiceTest {

    @Mock
    private SmellResultRepository smellResultRepository;

    @InjectMocks
    private SmellResultService smellResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSmellResults() {
        CodeSmellResultDocument document1 = new CodeSmellResultDocument();
        CodeSmellResultDocument document2 = new CodeSmellResultDocument();
        List<CodeSmellResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(smellResultRepository.findAll()).thenReturn(expectedResults);

        List<CodeSmellResultDocument> results = smellResultService.getAllSmellResults();

        assertEquals(expectedResults, results);
        verify(smellResultRepository, times(1)).findAll();
    }

    @Test
    void testGetSmellResultById() {
        String testId = "testId";
        Optional<CodeSmellResultDocument> expectedResult = Optional.of(new CodeSmellResultDocument());

        when(smellResultRepository.findById(testId)).thenReturn(expectedResult);

        Optional<CodeSmellResultDocument> result = smellResultService.getSmellResultById(testId);

        assertEquals(expectedResult, result);
        verify(smellResultRepository, times(1)).findById(testId);
    }

    @Test
    void testGetSmellResultByPath_ReturnsCorrectDocuments() {
        String path = "some/path";
        List<CodeSmellResultDocument> expectedDocuments = Arrays.asList(new CodeSmellResultDocument(), new CodeSmellResultDocument());

        when(smellResultRepository.findByRepositoryPath(path)).thenReturn(expectedDocuments);

        List<CodeSmellResultDocument> actualDocuments = smellResultService.getSmellResultsByPath(path);

        assertEquals(expectedDocuments, actualDocuments);
        verify(smellResultRepository).findByRepositoryPath(path);
    }

    @Test
    void testGetSmellResultByCustomId_ReturnsCorrectDocument() {
        String customId = "12345";
        Optional<CodeSmellResultDocument> expectedDocument = Optional.of(new CodeSmellResultDocument());

        when(smellResultRepository.findByCustomId(customId)).thenReturn(expectedDocument);

        Optional<CodeSmellResultDocument> actualDocument = smellResultService.getSmellResultByCustomId(customId);

        assertEquals(expectedDocument, actualDocument);
        verify(smellResultRepository).findByCustomId(customId);
    }
}
