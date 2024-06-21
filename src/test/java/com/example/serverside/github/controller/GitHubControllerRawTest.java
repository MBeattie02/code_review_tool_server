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

public class GitHubControllerRawTest {

    @Mock
    private GitHubService gitHubService;

    private GitHubController controller;

    @InjectMocks
    private GitHubControllerRaw gitHubControllerRaw;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new GitHubController();
    }

    @Test
    void testGetUserRepositories_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "testcommit";
        String path = "testpath";
        String expectedResponse = "mock response";

        when(gitHubService.getRaw(username, repo, commitId, path)).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = gitHubControllerRaw.getUserRepositories(username, repo, commitId, path);

        verify(gitHubService, times(1)).getRaw(username, repo, commitId, path);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserRepositories_Error() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "testcommit";
        String path = "testpath";

        when(gitHubService.getRaw(username, repo, commitId, path)).thenThrow(new Exception("Test exception"));

        ResponseEntity<String> responseEntity = gitHubControllerRaw.getUserRepositories(username, repo, commitId, path);

        verify(gitHubService, times(1)).getRaw(username, repo, commitId, path);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getUserRepositoriesParam_Success() throws Exception {
        // Setup
        String username = "username";
        String repo = "repo";
        String commitId = "commitId";
        String path = "path/to/file";
        String expectedContent = "Expected raw content";

        when(gitHubService.getRaw(username, repo, commitId, path)).thenReturn(expectedContent);

        // Execution
        ResponseEntity<String> response = gitHubControllerRaw.getUserRepositoriesParam(username, repo, commitId, path);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedContent, response.getBody());
    }

    @Test
    void getUserRepositoriesParam_Error() throws Exception {
        // Setup
        String username = "username";
        String repo = "repo";
        String commitId = "commitId";
        String path = "path/to/file";

        when(gitHubService.getRaw(username, repo, commitId, path)).thenThrow(new RuntimeException("Service exception"));

        // Execution
        ResponseEntity<String> response = gitHubControllerRaw.getUserRepositoriesParam(username, repo, commitId, path);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred.", response.getBody());
    }
}



