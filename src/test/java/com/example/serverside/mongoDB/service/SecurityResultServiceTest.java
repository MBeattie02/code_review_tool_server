package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.SecurityResultRepository;
import com.example.serverside.mongoDB.document.SecurityResultDocument;
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

public class SecurityResultServiceTest {

    @Mock
    private SecurityResultRepository securityResultRepository;

    @InjectMocks
    private SecurityResultService securityResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSecurityResults() {
        SecurityResultDocument document1 = new SecurityResultDocument();
        SecurityResultDocument document2 = new SecurityResultDocument();
        List<SecurityResultDocument> expectedResults = Arrays.asList(document1, document2);

        when(securityResultRepository.findAll()).thenReturn(expectedResults);

        List<SecurityResultDocument> results = securityResultService.getAllSecurityResults();

        assertEquals(expectedResults, results);
        verify(securityResultRepository, times(1)).findAll();
    }

    @Test
    void testGetSecurityResultById() {
        String testId = "testId";
        Optional<SecurityResultDocument> expectedResult = Optional.of(new SecurityResultDocument());

        when(securityResultRepository.findById(testId)).thenReturn(expectedResult);

        Optional<SecurityResultDocument> result = securityResultService.getSecurityResultById(testId);

        assertEquals(expectedResult, result);
        verify(securityResultRepository, times(1)).findById(testId);
    }

    @Test
    void testGetSecurityResultByPath_ReturnsCorrectDocuments() {
        String path = "some/path";
        List<SecurityResultDocument> expectedDocuments = Arrays.asList(new SecurityResultDocument(), new SecurityResultDocument());

        when(securityResultRepository.findByRepositoryPath(path)).thenReturn(expectedDocuments);

        List<SecurityResultDocument> actualDocuments = securityResultService.getSecurityResultByPath(path);

        assertEquals(expectedDocuments, actualDocuments);
        verify(securityResultRepository).findByRepositoryPath(path);
    }

    @Test
    void testGetSecurityResultByCustomId_ReturnsCorrectDocument() {
        String customId = "12345";
        Optional<SecurityResultDocument> expectedDocument = Optional.of(new SecurityResultDocument());

        when(securityResultRepository.findByCustomId(customId)).thenReturn(expectedDocument);

        Optional<SecurityResultDocument> actualDocument = securityResultService.getSecurityResultByCustomId(customId);

        assertEquals(expectedDocument, actualDocument);
        verify(securityResultRepository).findByCustomId(customId);
    }
}
