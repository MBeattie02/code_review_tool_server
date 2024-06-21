package com.example.serverside.mongoDB.service;

import com.example.serverside.analysis.repository.*;
import com.example.serverside.analysis.result.*;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MongoDBServiceTest {

    @Mock
    private SecurityResultRepository securityResultRepository;
    @Mock
    private ComplexityResultRepository complexityResultRepository;
    @Mock
    private CombinedAnalysisResultRepository combinedAnalysisResultRepository;
    @Mock
    private SmellResultRepository smellResultRepository;
    @Mock
    private QualityResultRepository qualityResultRepository;
    @Mock
    private StyleResultsRepository styleResultsRepository;

    @InjectMocks
    private MongoDBService mongoDBService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mongoDBService = new MongoDBService(
                securityResultRepository,
                complexityResultRepository,
                combinedAnalysisResultRepository,
                smellResultRepository,
                qualityResultRepository,
                styleResultsRepository
        );
    }

    @Test
    public void testSaveSecurityResult() {
        // Create a sample SecurityResult
        SecurityResult securityResult = new SecurityResult();
        securityResult.setRepositoryInfo(new RepositoryInfo());
        List<String> vulnerabilities = new ArrayList<>();
        vulnerabilities.add("Vulnerability 1");
        vulnerabilities.add("Vulnerability 2");
        securityResult.setVulnerabilities(vulnerabilities);

        // Create a sample SecurityResultDocument
        SecurityResultDocument savedDocument = new SecurityResultDocument();
        savedDocument.setId("123");
        when(securityResultRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveSecurityResult(securityResult);

        // Verify that the save method was called with the correct document
        verify(securityResultRepository).save(any());

        // Assert that the savedId is not null
        assert (savedId != null);
    }

    @Test
    public void testSaveComplexityResult() {
        // Create a sample ComplexityResult
        ComplexityResult complexityResult = new ComplexityResult(1);
        complexityResult.setRepositoryInfo(new RepositoryInfo());
        complexityResult.setCyclomaticComplexity(42);

        // Create a sample ComplexityResultDocument
        ComplexityResultDocument savedDocument = new ComplexityResultDocument();
        savedDocument.setId("456");
        when(complexityResultRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveComplexityResult(complexityResult);

        // Verify that the save method was called with the correct document
        verify(complexityResultRepository).save(any());

        // Assert that the savedId is not null
        assert (savedId != null);
    }

    @Test
    public void testSaveCodeSmellResult() {
        // Create a sample CodeSmellResult
        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setRepositoryInfo(new RepositoryInfo());
        List<String> smells = new ArrayList<>();
        smells.add("Smell 1");
        smells.add("Smell 2");
        codeSmellResult.setSmells(smells);

        // Create a sample CodeSmellResultDocument
        CodeSmellResultDocument savedDocument = new CodeSmellResultDocument();
        savedDocument.setId("123");
        when(smellResultRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveCodeSmellResult(codeSmellResult);

        // Verify that the save method was called with the correct document
        verify(smellResultRepository).save(any());

        // Assert that the savedId is not null
        assertNotNull(savedId);
    }

    @Test
    public void testSaveQualityResult() {
        // Create a sample QualityResult
        QualityResult qualityResult = new QualityResult();
        qualityResult.setRepositoryInfo(new RepositoryInfo());
        List<String> duplications = new ArrayList<>();
        duplications.add("Duplication 1");
        duplications.add("Duplication 2");
        qualityResult.setDuplications(duplications);

        // Create a sample QualityResultDocument
        QualityResultDocument savedDocument = new QualityResultDocument();
        savedDocument.setId("123");
        when(qualityResultRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveQualityResult(qualityResult);

        // Verify that the save method was called with the correct document
        verify(qualityResultRepository).save(any());

        // Assert that the savedId is not null
        assertNotNull(savedId);
    }

    @Test
    public void testSaveStyleResult() {
        // Create a sample StyleResult
        StyleResult styleResult = new StyleResult();
        styleResult.setRepositoryInfo(new RepositoryInfo());
        List<String> violations = new ArrayList<>();
        violations.add("Violation 1");
        violations.add("Violation 2");
        styleResult.setViolations(violations);

        // Create a sample StyleResultDocument
        StyleResultDocument savedDocument = new StyleResultDocument();
        savedDocument.setId("123");
        when(styleResultsRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveStyleResult(styleResult);

        // Verify that the save method was called with the correct document
        verify(styleResultsRepository).save(any());

        // Assert that the savedId is not null
        assertNotNull(savedId);
    }

    @Test
    public void testSaveCombinedAnalysisResult() {
        // Create a sample CombinedResults

        CombinedAnalysisResultDocument combinedResults = new
                CombinedAnalysisResultDocument();


        // Create a sample CombinedAnalysisResultDocument
        CombinedAnalysisResultDocument savedDocument = new CombinedAnalysisResultDocument();
        savedDocument.setId("123");
        when(combinedAnalysisResultRepository.save(any())).thenReturn(savedDocument);

        // Call the service method
        String savedId = mongoDBService.saveCombinedAnalysisResult(combinedResults);

        // Verify that the save method was called with the correct document
        verify(combinedAnalysisResultRepository).save(any());

        // Assert that the savedId is not null
        assertNotNull(savedId);
    }

}
