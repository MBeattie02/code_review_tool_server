package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.QualityResultRepository;
import com.example.serverside.mongoDB.document.QualityResultDocument;
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

public class QualityResultServiceTest {

    @Mock
    private QualityResultRepository qualityResultRepository;

    @InjectMocks
    private QualityResultService qualityResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQualityResults() {
        QualityResultDocument document1 = new QualityResultDocument();
        QualityResultDocument document2 = new QualityResultDocument();
        List<QualityResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(qualityResultRepository.findAll()).thenReturn(expectedResults);

        List<QualityResultDocument> results = qualityResultService.getAllQualityResults();

        assertEquals(expectedResults, results);
        verify(qualityResultRepository, times(1)).findAll();
    }

    @Test
    void testGetQualityResultById() {
        String testId = "testId";
        Optional<QualityResultDocument> expectedResult = Optional.of(new QualityResultDocument());

        when(qualityResultRepository.findById(testId)).thenReturn(expectedResult);

        Optional<QualityResultDocument> result = qualityResultService.getQualityResultById(testId);

        assertEquals(expectedResult, result);
        verify(qualityResultRepository, times(1)).findById(testId);
    }

    @Test
    void testGetQualityResultByPath_ReturnsCorrectDocuments() {
        String path = "some/path";
        List<QualityResultDocument> expectedDocuments = Arrays.asList(new QualityResultDocument(), new QualityResultDocument());

        when(qualityResultRepository.findByRepositoryPath(path)).thenReturn(expectedDocuments);

        List<QualityResultDocument> actualDocuments = qualityResultService.getQualityResultByPath(path);

        assertEquals(expectedDocuments, actualDocuments);
        verify(qualityResultRepository).findByRepositoryPath(path);
    }

    @Test
    void testGetQualityResultByCustomId_ReturnsCorrectDocument() {
        String customId = "12345";
        Optional<QualityResultDocument> expectedDocument = Optional.of(new QualityResultDocument());

        when(qualityResultRepository.findByCustomId(customId)).thenReturn(expectedDocument);

        Optional<QualityResultDocument> actualDocument = qualityResultService.getQualityResultByCustomId(customId);

        assertEquals(expectedDocument, actualDocument);
        verify(qualityResultRepository).findByCustomId(customId);
    }
}
