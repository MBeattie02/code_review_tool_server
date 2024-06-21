package com.example.serverside.github.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

     private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        gitHubService = new GitHubService(restTemplateBuilder);
        ReflectionTestUtils.setField(gitHubService, "githubToken", "fakeToken");
    }

    @Test
    void testGetUserRepositories_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String mockResponse = "mock response";
        String url = "https://api.github.com/repos/" + username + "/" + repo;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        String response = gitHubService.getUserRepositories(username, repo);
        assertEquals(mockResponse, response);
    }

    @Test
    void testGetUserRepositories_Failure() {
        String username = "testuser";
        String repo = "testrepo";
        String url = "https://api.github.com/repos/" + username + "/" + repo;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(Exception.class, () -> {
            gitHubService.getUserRepositories(username, repo);
        });

        assertTrue(exception.getMessage().contains("Failed to fetch repositories"));
    }

    @Test
    void testGetTrees_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "123abc";
        String mockResponse = "mock response";
        String url = "https://api.github.com/repos/" + username + "/" + repo + "/git/trees/" + commitId;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        String response = gitHubService.getTrees(username, repo, commitId);
        assertEquals(mockResponse, response);
    }

    @Test
    void testGetTrees_Failure() {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "123abc";
        String url = "https://api.github.com/repos/" + username + "/" + repo + "/git/trees/" + commitId;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(Exception.class, () -> {
            gitHubService.getTrees(username, repo, commitId);
        });

        assertFalse(exception.getMessage().contains("Failed to fetch trees"));
    }

    @Test
    void testGetRaw_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "123abc";
        String path = "sample.txt";
        String mockResponse = "mock response";
        String url = "https://raw.githubusercontent.com/" + username + "/" + repo + "/" + commitId + "/" + path;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        String response = gitHubService.getRaw(username, repo, commitId, path);
        assertEquals(mockResponse, response);
    }

    @Test
    void testGetRaw_Failure() {
        String username = "testuser";
        String repo = "testrepo";
        String commitId = "123abc";
        String path = "sample.txt";
        String url = "https://raw.githubusercontent.com/" + username + "/" + repo + "/" + commitId + "/" + path;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(Exception.class, () -> {
            gitHubService.getRaw(username, repo, commitId, path);
        });

        assertFalse(exception.getMessage().contains("Failed to fetch raw content"));
    }


    @Test
    void testGetCommits_Success() throws Exception {
        String username = "testuser";
        String repo = "testrepo";
        String mockResponse = "mock response";
        String url = "https://api.github.com/repos/" + username + "/" + repo + "/commits";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        String response = gitHubService.getCommits(username, repo);
        assertEquals(mockResponse, response);
    }

    @Test
    void testGetCommits_Failure() {
        String username = "testuser";
        String repo = "testrepo";
        String url = "https://api.github.com/repos/" + username + "/" + repo + "/commits";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(Exception.class, () -> {
            gitHubService.getCommits(username, repo);
        });

        assertFalse(exception.getMessage().contains("Failed to fetch commits"));
    }

    @Test
    void testPostCommentThrowsIOExceptionForNon201Response() throws IOException, InterruptedException {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(400);
        when(mockResponse.body()).thenReturn("Error message from server");
        when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        try (MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            IOException exception = assertThrows(IOException.class, () ->
                    gitHubService.postComment("username", "repo", "commitSha", "Test comment")
            );

            assertTrue(exception.getMessage().contains("Failed to post comment, Status code: 400"));
        }
    }

    @Test
    void testGetLatestCommitShaThrowsIOExceptionForNon200Response() throws IOException, InterruptedException {

        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(404);
        when(mockResponse.body()).thenReturn("Error message indicating not found");
        when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        try (MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            IOException exception = assertThrows(IOException.class, () ->
                    gitHubService.getLatestCommitSha("owner", "repo", "path")
            );

            assertTrue(exception.getMessage().contains("Failed to retrieve commit history for the file"));
        }
    }

    @Test
    void testExtractCommitShaFromValidResponse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String jsonResponse = "[{\"sha\": \"abc123\"}]";
        GitHubService service = new GitHubService(new RestTemplateBuilder());

        Method method = GitHubService.class.getDeclaredMethod("extractCommitShaFromResponse", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(service, jsonResponse);

        Assertions.assertEquals("abc123", result);
    }

    @Test
    void testExtractCommitShaFromResponseWithoutShaKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String jsonResponse = "[{\"no_sha\": \"value\"}]";
        GitHubService service = new GitHubService(new RestTemplateBuilder());

        Method method = GitHubService.class.getDeclaredMethod("extractCommitShaFromResponse", String.class);
        method.setAccessible(true);

        Exception exception = Assertions.assertThrows(InvocationTargetException.class, () ->
                method.invoke(service, jsonResponse)
        );

        Assertions.assertTrue(exception.getCause() instanceof IOException);
        Assertions.assertTrue(exception.getCause().getMessage().contains("SHA key not found in the response."));
    }

    @Test
    void testExtractCommitShaFromNonArrayOrEmptyArrayResponse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] jsonResponses = {"{}", "[]"};
        GitHubService service = new GitHubService(new RestTemplateBuilder());

        Method method = GitHubService.class.getDeclaredMethod("extractCommitShaFromResponse", String.class);
        method.setAccessible(true);

        for (String jsonResponse : jsonResponses) {
            Exception exception = Assertions.assertThrows(InvocationTargetException.class, () ->
                    method.invoke(service, jsonResponse)
            );

            Assertions.assertTrue(exception.getCause() instanceof IOException);
            Assertions.assertTrue(exception.getCause().getMessage().contains("SHA key not found in the response."));
        }
    }
}

