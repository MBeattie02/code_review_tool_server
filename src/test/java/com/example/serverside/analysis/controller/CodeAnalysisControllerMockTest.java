package com.example.serverside.analysis.controller;

import com.example.serverside.analysis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CodeAnalysisControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeSecurityAnalysisService codeSecurityAnalysisService;

    @MockBean
    private CodeSmellAnalysisService codeSmellAnalysisService;

    @MockBean
    private CodeComplexityAnalysisService codeComplexityAnalysisService;

    @MockBean
    private CodeQualityAnalysisService codeQualityAnalysisService;

    @MockBean
    private CodeStyleAnalysisService codeStyleAnalysisService;

    @Test
    public void testAnalyseComplexityThrowsExceptionWithoutGitHubService() throws Exception {

        // Configure codeSecurityAnalysisService to throw an exception
        when(codeComplexityAnalysisService.calculateComplexity(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-complexity")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAnalyseStyleThrowsExceptionWithoutGitHubService() throws Exception {

        // Configure codeSecurityAnalysisService to throw an exception
        when(codeStyleAnalysisService.analyse(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-style")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAnalyseSecurityThrowsExceptionWithoutGitHubService() throws Exception {

        // Configure codeSecurityAnalysisService to throw an exception
        when(codeSecurityAnalysisService.analyse(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-security")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }

        @Test
    public void testAnalyseSmellsThrowsException() throws Exception {
        // Configure one of the services to throw an exception
        when(codeSmellAnalysisService.analyse(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-code-smells")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAnalyseQualityThrowsException() throws Exception {
        // Configure one of the services to throw an exception
        when(codeQualityAnalysisService.analyse(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-quality")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAnalyseAllThrowsException() throws Exception {
        // Configure one of the services to throw an exception
        when(codeSmellAnalysisService.analyse(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Perform the request and expect a 500 Internal Server Error
        mockMvc.perform(get("/api/analyse-all")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepo")
                        .param("commitId", "main")
                        .param("path", "SQL.java"))
                .andExpect(status().isInternalServerError());
    }
}
