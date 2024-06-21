package com.example.serverside.slack.message;

import com.example.serverside.analysis.result.*;
import com.example.serverside.mongoDB.info.RepositoryInfo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComposeSlackMessageTest {

    ComposeSlackMessage composeSlackMessage = new ComposeSlackMessage();

    @Test
    void testComposeSlackMessageSecurity_WithVulnerabilities() {
        SecurityResult securityResult = new SecurityResult();
        securityResult.setVulnerabilities(Arrays.asList("Vulnerability1", "Vulnerability2"));
        securityResult.setRepositoryInfo(new RepositoryInfo());
        securityResult.getRepositoryInfo().setUsername("username");
        securityResult.getRepositoryInfo().setRepo("repo");
        securityResult.getRepositoryInfo().setCommitId("commitId");
        securityResult.getRepositoryInfo().setPath("path");
        String documentId = "123";

        String expectedMessage = "Security Analysis Report (ID: 123):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Security Issues Detected (Total: 2):\n" +
                " - Vulnerability1\n" +
                " - Vulnerability2\n";
        String actualMessage = ComposeSlackMessage.composeSlackMessageSecurity(securityResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageSecurity_NoVulnerabilities() {
        // Setup
        SecurityResult securityResult = new SecurityResult();
        securityResult.setVulnerabilities(Arrays.asList()); // No vulnerabilities
        securityResult.setRepositoryInfo(new RepositoryInfo());
        securityResult.getRepositoryInfo().setUsername("username");
        securityResult.getRepositoryInfo().setRepo("repo");
        securityResult.getRepositoryInfo().setCommitId("commitId");
        securityResult.getRepositoryInfo().setPath("path");
        String documentId = "123ABC";

        // Expected Slack message when no vulnerabilities are found
        String expectedMessage = "Security Analysis Report (ID: 123ABC):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Security Issues Detected (Total: 0):\n" +
                "No vulnerabilities found.";

        // Execution
        String actualMessage = ComposeSlackMessage.composeSlackMessageSecurity(securityResult, documentId);

        // Assertion
        assertEquals(expectedMessage, actualMessage, "The composed Slack message should correctly report no vulnerabilities.");
    }

    @Test
    void testComposeSlackMessageStyle_WithViolations() {
        StyleResult styleResult = new StyleResult();
        styleResult.setViolations(Arrays.asList());
        styleResult.setRepositoryInfo(new RepositoryInfo());
        styleResult.getRepositoryInfo().setUsername("username");
        styleResult.getRepositoryInfo().setRepo("repo");
        styleResult.getRepositoryInfo().setCommitId("commitId");
        styleResult.getRepositoryInfo().setPath("path");
        String documentId = "456";

        String expectedMessage = "Style Analysis Report (ID: 456):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Style Issues Detected (Total: 0):\n" +
                "No style issues detected.";
        String actualMessage = ComposeSlackMessage.composeSlackMessageStyle(styleResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageStyle_NoViolations() {
        StyleResult styleResult = new StyleResult();
        styleResult.setViolations(Arrays.asList("Violation1", "Violation2"));
        styleResult.setRepositoryInfo(new RepositoryInfo());
        styleResult.getRepositoryInfo().setUsername("username");
        styleResult.getRepositoryInfo().setRepo("repo");
        styleResult.getRepositoryInfo().setCommitId("commitId");
        styleResult.getRepositoryInfo().setPath("path");
        String documentId = "456";

        String expectedMessage = "Style Analysis Report (ID: 456):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Style Issues Detected (Total: 2):\n" +
                " - Violation1\n" +
                " - Violation2\n";
        String actualMessage = ComposeSlackMessage.composeSlackMessageStyle(styleResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageComplexity() {
        ComplexityResult complexityResult = new ComplexityResult(5);
        complexityResult.setRepositoryInfo(new RepositoryInfo());
        complexityResult.getRepositoryInfo().setUsername("username");
        complexityResult.getRepositoryInfo().setRepo("repo");
        complexityResult.getRepositoryInfo().setCommitId("commitId");
        complexityResult.getRepositoryInfo().setPath("path");
        complexityResult.setCyclomaticComplexity(5);
        String documentId = "789";

        String expectedMessage = "Complexity Analysis Report (ID: 789):\n" +
                "Repository Info: username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'\n" +
                "Cyclomatic Complexity Score: 5";
        String actualMessage = ComposeSlackMessage.composeSlackMessageComplexity(complexityResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageSmell_WithSmells() {
        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setRepositoryInfo(new RepositoryInfo());
        codeSmellResult.getRepositoryInfo().setUsername("username");
        codeSmellResult.getRepositoryInfo().setRepo("repo");
        codeSmellResult.getRepositoryInfo().setCommitId("commitId");
        codeSmellResult.getRepositoryInfo().setPath("path");
        codeSmellResult.setSmells(Arrays.asList("Smell1", "Smell2"));
        String documentId = "101112";

        String expectedMessage = "Smell Analysis Report (ID: 101112):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Smell Issues Detected (Total: 2):\n" +
                " - Smell1\n" +
                " - Smell2\n";
        String actualMessage = ComposeSlackMessage.composeSlackMessageSmell(codeSmellResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageSmell_NoSmells() {
        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setSmells(Arrays.asList());
        codeSmellResult.setRepositoryInfo(new RepositoryInfo());
        codeSmellResult.getRepositoryInfo().setUsername("username");
        codeSmellResult.getRepositoryInfo().setRepo("repo");
        codeSmellResult.getRepositoryInfo().setCommitId("commitId");
        codeSmellResult.getRepositoryInfo().setPath("path");
        String documentId = "101112";

        String expectedMessage = "Smell Analysis Report (ID: 101112):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Smell Issues Detected (Total: 0):\n" +
                "No code smells detected.";

        String actualMessage = ComposeSlackMessage.composeSlackMessageSmell(codeSmellResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageQuality_WithDuplications() {
        QualityResult qualityResult = new QualityResult();
        qualityResult.setDuplications(Arrays.asList("Duplication1", "Duplication2"));
        qualityResult.setRepositoryInfo(new RepositoryInfo());
        qualityResult.getRepositoryInfo().setUsername("username");
        qualityResult.getRepositoryInfo().setRepo("repo");
        qualityResult.getRepositoryInfo().setCommitId("commitId");
        qualityResult.getRepositoryInfo().setPath("path");
        String documentId = "131415";

        String expectedMessage = "Quality Analysis Report (ID: 131415):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Quality Issues Detected (Total: 2):\n" +
                " - Duplication1\n" +
                " - Duplication2\n";
        String actualMessage = ComposeSlackMessage.composeSlackMessageQuality(qualityResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageQuality_NoDuplications() {
        QualityResult qualityResult = new QualityResult();
        qualityResult.setDuplications(Arrays.asList());
        qualityResult.setRepositoryInfo(new RepositoryInfo());
        qualityResult.getRepositoryInfo().setUsername("username");
        qualityResult.getRepositoryInfo().setRepo("repo");
        qualityResult.getRepositoryInfo().setCommitId("commitId");
        qualityResult.getRepositoryInfo().setPath("path");
        String documentId = "131415";

        String expectedMessage = "Quality Analysis Report (ID: 131415):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Quality Issues Detected (Total: 0):\n" +
                "No Quality issues detected.";
        String actualMessage = ComposeSlackMessage.composeSlackMessageQuality(qualityResult, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testComposeSlackMessageAll() {
        CombinedResults results = new CombinedResults();
        results.setRepositoryInfo(new RepositoryInfo());
        results.getRepositoryInfo().setUsername("username");
        results.getRepositoryInfo().setRepo("repo");
        results.getRepositoryInfo().setCommitId("commitId");
        results.getRepositoryInfo().setPath("path");

        QualityResult qualityResult = new QualityResult();
        qualityResult.setDuplications(Arrays.asList("Duplication1"));
        results.setQualityResult(qualityResult);

        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setSmells(Arrays.asList("Smell1"));
        results.setCodeSmellResult(codeSmellResult);

        SecurityResult securityResult = new SecurityResult();
        securityResult.setVulnerabilities(Arrays.asList("Vulnerability1"));
        results.setSecurityResult(securityResult);

        ComplexityResult complexityResult = new ComplexityResult(3);
        results.setComplexityResult(complexityResult);

        StyleResult styleResult = new StyleResult();
        styleResult.setViolations(Arrays.asList("Violation1"));
        results.setStyleResult(styleResult);

        String documentId = "161718";
        String expectedMessage = "Combined Analysis Report (ID: 161718):\n" +
                "Repository Info : username='username'\n" +
                "repo='repo'\n" +
                "commitId='commitId'\n" +
                "path='path'):\n" +
                "Quality Analysis: Quality Issues Detected (Total: 1):\n" +
                " - Duplication1\n\n" +
                "Code Smell Analysis: Code Smells Detected (Total: 1):\n" +
                " - Smell1\n\n" +
                "Security Analysis: Security Vulnerabilities Detected (Total: 1):\n" +
                " - Vulnerability1\n\n" +
                "Complexity Analysis: Cyclomatic complexity score: 3\n\n" +
                "Style Analysis: Style Violations Detected (Total: 1):\n" +
                " - Violation1\n\n";
        String actualMessage = ComposeSlackMessage.composeSlackMessageAll(results, documentId);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testSummaryOfQualityResult_NoIssues() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        QualityResult qualityResult = new QualityResult();
        qualityResult.setDuplications(Collections.emptyList());

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfQualityResult", QualityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, qualityResult);

        String expected = "No quality issues detected. \n";

        assertEquals(expected, actual);
    }

    @Test
    void testSummaryOfQualityResult_WhenResultIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        QualityResult qualityResult = null;

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfQualityResult", QualityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, qualityResult);

        String expected = "No quality issues detected. \n";
        assertEquals(expected, actual, "The summary should indicate no quality issues when the result is null.");
    }

    @Test
    void testSummaryOfQualityResult_WhenDuplicationsIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        QualityResult qualityResult = new QualityResult();
        qualityResult.setDuplications(null);

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfQualityResult", QualityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, qualityResult);

        String expected = "No quality issues detected. \n";
        assertEquals(expected, actual, "The summary should indicate no quality issues when duplications are null.");
    }

    @Test
    void testSummaryOfCodeSmellResult_NoIssues() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setSmells(Collections.emptyList());

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfCodeSmellResult", CodeSmellResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, codeSmellResult);

        String expected = "No code smells detected. \n";

        assertEquals(expected, actual);
    }

    @Test
    void testSummaryOfCodeSmellResult_WhenResultIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        CodeSmellResult codeSmellResult = null;

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfCodeSmellResult", CodeSmellResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, codeSmellResult);

        String expected = "No code smells detected. \n";
        assertEquals(expected, actual, "The summary should indicate no code smell issues when the result is null.");
    }

    @Test
    void testSummaryOfCodeSmellResult_WhenSmellsIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        CodeSmellResult codeSmellResult = new CodeSmellResult();
        codeSmellResult.setSmells(null);

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfCodeSmellResult", CodeSmellResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, codeSmellResult);

        String expected = "No code smells detected. \n";
        assertEquals(expected, actual, "The summary should indicate no smell issues when duplications are null.");
    }

    @Test
    void testSummaryOfComplexityResult_NoIssues() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        ComplexityResult complexityResult = new ComplexityResult(0);

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfComplexityResult", ComplexityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, complexityResult);

        String expected = "Cyclomatic complexity not calculated. \n";

        assertEquals(expected, actual);
    }

    @Test
    void testSummaryOfComplexityResult_WhenResultIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        ComplexityResult complexityResult = null;


        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfComplexityResult", ComplexityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, complexityResult);

        String expected = "Cyclomatic complexity not calculated. \n";
        assertEquals(expected, actual, "The summary should indicate no complexity when the result is null.");
    }

    @Test
    void testSummaryOfSecurityResult_NoIssues() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        SecurityResult securityResult = new SecurityResult();
        securityResult.setVulnerabilities(Collections.emptyList());

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfSecurityResult", SecurityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, securityResult);

        String expected = "No security vulnerabilities detected. \n";

        assertEquals(expected, actual);
    }

    @Test
    void testSummaryOfSecurityResult_WhenResultIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        SecurityResult securityResult = null;

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfSecurityResult", SecurityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, securityResult);

        String expected = "No security vulnerabilities detected. \n";
        assertEquals(expected, actual, "The summary should indicate no code security issues when the result is null.");
    }

    @Test
    void testSummaryOfSecurityResult_WhenVulnerabilitiesIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        SecurityResult securityResult = new SecurityResult();
        securityResult.setVulnerabilities(null);

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfSecurityResult", SecurityResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, securityResult);

        String expected = "No security vulnerabilities detected. \n";
        assertEquals(expected, actual, "The summary should indicate no security issues when vulnerabilities are null.");
    }

    @Test
    void testSummaryOfStyleResult_NoIssues() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        StyleResult styleResult = new StyleResult();
        styleResult.setViolations(Collections.emptyList());

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfStyleResult", StyleResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, styleResult);

        String expected = "No style violations found. \n";

        assertEquals(expected, actual);
    }

    @Test
    void testSummaryOfStyleResult_WhenResultIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        StyleResult styleResult = null;

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfStyleResult", StyleResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, styleResult);

        String expected = "No style violations found. \n";
        assertEquals(expected, actual, "The summary should indicate no code style issues when the result is null.");
    }

    @Test
    void testSummaryOfStyleResult_WhenViolationsIsNull() throws Exception {
        ComposeSlackMessage instance = new ComposeSlackMessage();

        StyleResult styleResult = new StyleResult();
        styleResult.setViolations(null);

        Method method = ComposeSlackMessage.class.getDeclaredMethod("summaryOfStyleResult", StyleResult.class);
        method.setAccessible(true);

        String actual = (String) method.invoke(instance, styleResult);

        String expected = "No style violations found. \n";
        assertEquals(expected, actual, "The summary should indicate no style issues when violations are null.");
    }


}
