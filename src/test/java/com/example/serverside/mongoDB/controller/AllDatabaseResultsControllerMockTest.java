package com.example.serverside.mongoDB.controller;

import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AllDatabaseResultsControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityResultService securityResultService;

    @MockBean
    private ComplexityResultService complexityResultService;

    @MockBean
    private SmellResultService smellResultService;

    @MockBean
    private QualityResultService qualityResultService;

    @MockBean
    private StyleResultService styleResultService;

    @MockBean
    private AllResultService allResultService;



    @Test
    void getAllResultsByUsernameReturnsCombinedResults() throws Exception {
        String path = "XSS.java";

        when(securityResultService.getSecurityResultByPath(anyString())).thenReturn(List.of(new SecurityResultDocument()));
        when(complexityResultService.getComplexityResultPath(anyString())).thenReturn(List.of(new ComplexityResultDocument()));
        when(smellResultService.getSmellResultsByPath(anyString())).thenReturn(List.of(new CodeSmellResultDocument()));
        when(qualityResultService.getQualityResultByPath(anyString())).thenReturn(List.of(new QualityResultDocument()));
        when(styleResultService.getStyleResultByPath(anyString())).thenReturn(List.of(new StyleResultDocument()));
        when(allResultService.getAllResultByPath(anyString())).thenReturn(List.of(new CombinedAnalysisResultDocument()));

        mockMvc.perform(get("/api/combined-results/all/path/{path}", path)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.securityResults").isNotEmpty())
                .andExpect(jsonPath("$.complexityResults").isNotEmpty())
                .andExpect(jsonPath("$.codeSmellResults").isNotEmpty())
                .andExpect(jsonPath("$.qualityResults").isNotEmpty())
                .andExpect(jsonPath("$.styleResults").isNotEmpty());

    }
}
