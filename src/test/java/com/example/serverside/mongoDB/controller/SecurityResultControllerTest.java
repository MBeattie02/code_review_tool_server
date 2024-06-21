package com.example.serverside.mongoDB.controller;

import com.example.serverside.mongoDB.document.SecurityResultDocument;
import com.example.serverside.mongoDB.service.SecurityResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SecurityResultControllerTest {

    @Mock
    private SecurityResultService securityResultService;

    @InjectMocks
    private SecurityResultController securityResultController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSecurityResults() {
        SecurityResultDocument doc1 = new SecurityResultDocument();
        SecurityResultDocument doc2 = new SecurityResultDocument();
        List<SecurityResultDocument> resultList = Arrays.asList(doc1, doc2);

        when(securityResultService.getAllSecurityResults()).thenReturn(resultList);

        ResponseEntity<List<SecurityResultDocument>> response = securityResultController.getAllSecurityResults();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resultList, response.getBody());
        verify(securityResultService, times(1)).getAllSecurityResults();
    }

    @Test
    void testGetSecurityResultById_Found() {
        String testId = "testId";
        SecurityResultDocument resultDocument = new SecurityResultDocument();
        when(securityResultService.getSecurityResultById(testId)).thenReturn(Optional.of(resultDocument));

        ResponseEntity<SecurityResultDocument> response = securityResultController.getSecurityResultById(testId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resultDocument, response.getBody());
    }

    @Test
    void testGetSecurityResultById_NotFound() {
        String testId = "testId";
        when(securityResultService.getSecurityResultById(testId)).thenReturn(Optional.empty());

        ResponseEntity<SecurityResultDocument> response = securityResultController.getSecurityResultById(testId);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody() == null);
    }
}
