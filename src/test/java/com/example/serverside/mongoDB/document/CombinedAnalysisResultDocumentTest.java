package com.example.serverside.mongoDB.document;

import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CombinedAnalysisResultDocumentTest {

    private CombinedAnalysisResultDocument document;
    private QualityResultDocument qualityResultDocument;
    private CodeSmellResultDocument codeSmellResultDocument;
    private SecurityResultDocument securityResultDocument;
    private ComplexityResultDocument complexityResultDocument;
    private StyleResultDocument styleResultDocument;
    private LocalDateTime timestamp;
    private RepositoryInfo repositoryInfo;

    @BeforeEach
    void setUp() {
        document = new CombinedAnalysisResultDocument();
        qualityResultDocument = new QualityResultDocument();
        codeSmellResultDocument = new CodeSmellResultDocument();
        securityResultDocument = new SecurityResultDocument();
        complexityResultDocument = new ComplexityResultDocument();
        styleResultDocument = new StyleResultDocument();
        timestamp = LocalDateTime.now();
        repositoryInfo = new RepositoryInfo();

    }

    @Test
    void testGetterSetterMethods() {
        document.setQualityResultDocument(qualityResultDocument);
        document.setCodeSmellResultDocument(codeSmellResultDocument);
        document.setSecurityResultDocument(securityResultDocument);
        document.setComplexityResultDocument(complexityResultDocument);
        document.setStyleResultDocument(styleResultDocument);
        document.setTimestamp(timestamp);
        document.setRepositoryInfo(repositoryInfo);

        assertEquals(qualityResultDocument, document.getQualityResultDocument());
        assertEquals(codeSmellResultDocument, document.getCodeSmellResultDocument());
        assertEquals(securityResultDocument, document.getSecurityResultDocument());
        assertEquals(complexityResultDocument, document.getComplexityResultDocument());
        assertEquals(styleResultDocument, document.getStyleResultDocument());
        assertEquals(timestamp, document.getTimestamp());
        assertEquals(repositoryInfo, document.getRepositoryInfo());
    }

    @Test
    void testIdGetterSetter() {
        String testId = null;
        assertEquals(testId, document.getId());
    }

    @Test
    void testCustomIdGetterSetter() {
        String customId = "MBeattie02-TestRepoDemo-XSS.java-2024-01-27T19:06:35.077423";
        document.setCustomId(customId);
        assertEquals(customId, document.getCustomId());
    }

    @Test
    void testToString() {
        // Set all the fields of the document
        document.setQualityResultDocument(qualityResultDocument);
        document.setCodeSmellResultDocument(codeSmellResultDocument);
        document.setSecurityResultDocument(securityResultDocument);
        document.setComplexityResultDocument(complexityResultDocument);
        document.setStyleResultDocument(styleResultDocument);

        // Create the expected string
        String expectedString = "AnalysisResults{" +
                "qualityResult=" + qualityResultDocument +
                ", codeSmellResult=" + codeSmellResultDocument +
                ", securityResult=" + securityResultDocument +
                ", complexityResult=" + complexityResultDocument +
                ", styleResult=" + styleResultDocument +
                '}';

        // Assert that the actual toString output matches the expected string
        assertEquals(expectedString, document.toString());
    }

    CombinedAnalysisResultDocument combinedAnalysisResultDocument = new CombinedAnalysisResultDocument(qualityResultDocument, codeSmellResultDocument, securityResultDocument, complexityResultDocument, styleResultDocument, repositoryInfo);
}
