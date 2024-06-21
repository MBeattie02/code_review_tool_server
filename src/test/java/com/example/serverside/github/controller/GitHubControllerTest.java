package com.example.serverside.github.controller;

import com.example.serverside.github.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GitHubControllerTest {

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private GitHubController gitHubController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserRepositories_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String expectedResponse = "mock response";

        when(gitHubService.getUserRepositories(username, repo)).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = gitHubController.getUserRepositories(username, repo);

        verify(gitHubService, times(1)).getUserRepositories(username, repo);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserRepositories_Error() throws Exception {
        String username = "testuser";
        String repo = "testrepo";

        when(gitHubService.getUserRepositories(username, repo)).thenThrow(new Exception("Test exception"));

        ResponseEntity<String> responseEntity = gitHubController.getUserRepositories(username, repo);

        verify(gitHubService, times(1)).getUserRepositories(username, repo);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetTrees_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "testcommit";
        String expectedResponse = "mock response";

        when(gitHubService.getTrees(username, repo, commitId)).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = gitHubController.getTrees(username, repo, commitId);

        verify(gitHubService, times(1)).getTrees(username, repo, commitId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetTrees_Error() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "testcommit";

        when(gitHubService.getTrees(username, repo, commitId)).thenThrow(new Exception("Test exception"));

        ResponseEntity<String> responseEntity = gitHubController.getTrees(username, repo, commitId);

        verify(gitHubService, times(1)).getTrees(username, repo, commitId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetCommits_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String expectedResponse = "mock response";

        when(gitHubService.getCommits(username, repo)).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = gitHubController.getCommits(username, repo);

        verify(gitHubService, times(1)).getCommits(username, repo);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetCommits_Error() throws Exception {
        String username = "testuser";
        String repo = "testrepo";

        when(gitHubService.getCommits(username, repo)).thenThrow(new Exception("Test exception"));

        ResponseEntity<String> responseEntity = gitHubController.getCommits(username, repo);

        verify(gitHubService, times(1)).getCommits(username, repo);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
