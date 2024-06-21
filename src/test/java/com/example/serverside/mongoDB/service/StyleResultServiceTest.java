package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.StyleResultsRepository;
import com.example.serverside.mongoDB.document.StyleResultDocument;
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

public class StyleResultServiceTest {

    @Mock
    private StyleResultsRepository styleResultsRepository;

    @InjectMocks
    private StyleResultService styleResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStyleResults() {
        StyleResultDocument document1 = new StyleResultDocument();
        StyleResultDocument document2 = new StyleResultDocument();
        List<StyleResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(styleResultsRepository.findAll()).thenReturn(expectedResults);

        List<StyleResultDocument> results = styleResultService.getAllStyleResults();

        assertEquals(expectedResults, results);
        verify(styleResultsRepository, times(1)).findAll();
    }

    @Test
    void testGetStyleResultById() {
        String testId = "testId";
        Optional<StyleResultDocument> expectedResult = Optional.of(new StyleResultDocument());

        when(styleResultsRepository.findById(testId)).thenReturn(expectedResult);

        Optional<StyleResultDocument> result = styleResultService.getStyleResultById(testId);

        assertEquals(expectedResult, result);
        verify(styleResultsRepository, times(1)).findById(testId);
    }

    @Test
    void testGetStyleResultByPath_ReturnsCorrectDocuments() {
        String path = "some/path";
        List<StyleResultDocument> expectedDocuments = Arrays.asList(new StyleResultDocument(), new StyleResultDocument());

        when(styleResultsRepository.findByRepositoryPath(path)).thenReturn(expectedDocuments);

        List<StyleResultDocument> actualDocuments = styleResultService.getStyleResultByPath(path);

        assertEquals(expectedDocuments, actualDocuments);
        verify(styleResultsRepository).findByRepositoryPath(path);
    }

    @Test
    void testGetStyleResultByCustomId_ReturnsCorrectDocument() {
        String customId = "12345";
        Optional<StyleResultDocument> expectedDocument = Optional.of(new StyleResultDocument());

        when(styleResultsRepository.findByCustomId(customId)).thenReturn(expectedDocument);

        Optional<StyleResultDocument> actualDocument = styleResultService.getStyleResultByCustomId(customId);

        assertEquals(expectedDocument, actualDocument);
        verify(styleResultsRepository).findByCustomId(customId);
    }


}
