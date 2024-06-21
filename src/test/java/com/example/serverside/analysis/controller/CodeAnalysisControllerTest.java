package com.example.serverside.analysis.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CodeAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAnalyseCodeStyle() throws Exception {
        // Define the request parameters
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        // Perform the request and assert the response
        mockMvc.perform(get("/api/analyse-style")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseStyleWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-style")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseComplexity() throws Exception {
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        mockMvc.perform(get("/api/analyse-complexity")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseComplexityWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-complexity")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseSecurity() throws Exception {
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        mockMvc.perform(get("/api/analyse-security")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseSecurityWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-security")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseSmells() throws Exception {
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        mockMvc.perform(get("/api/analyse-code-smells")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseSmellsWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-code-smells")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseQuality() throws Exception {
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        mockMvc.perform(get("/api/analyse-quality")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseQualityWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-quality")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseAll() throws Exception {
        String username = "MBeattie02";
        String repo = "TestRepo";
        String commitId = "main";
        String path = "SQL.java";

        mockMvc.perform(get("/api/analyse-all")
                        .param("username", username)
                        .param("repo", repo)
                        .param("commitId", commitId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testAnalyseAllWithPostingComment() throws Exception {

        mockMvc.perform(get("/api/analyse-all")
                        .param("username", "MBeattie02")
                        .param("repo", "TestRepoDemo")
                        .param("commitId", "main")
                        .param("path", "TestComments.java")
                        .param("shouldPostComment", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

}
