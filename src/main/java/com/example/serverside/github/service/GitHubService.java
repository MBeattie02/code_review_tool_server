package com.example.serverside.github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service class for interacting with the GitHub API.
 * This service handles operations such as retrieving repositories, trees, raw content, and commits from GitHub.
 */
@Service
public class GitHubService {

    @Value("${github.token}")
    private String githubToken; // GitHub API token for authorization

    private final RestTemplate restTemplate;

    /**
     * Constructs a GitHubService with a configured RestTemplate.
     *
     * @param restTemplateBuilder The builder to create a RestTemplate instance.
     */
    @Autowired
    public GitHubService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Retrieves the details of a specific repository.
     *
     * @param username The username of the repository owner.
     * @param repo The name of the repository.
     * @return A String representation of the repository details.
     * @throws Exception if the request fails or the response is not successful.
     */
    public String getUserRepositories(String username, String repo) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/repos/" + username + "/" + repo;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Failed to fetch repositories");
        }
    }

    /**
     * Retrieves the tree structure of a repository at a specific commit.
     *
     * @param username The username of the repository owner.
     * @param repo The name of the repository.
     * @param commitId The commit ID for which the tree is to be retrieved.
     * @return A String representation of the repository's tree structure.
     * @throws Exception if the request fails or the response is not successful.
     */
    public String getTrees(String username, String repo, String commitId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/repos/" + username + "/" + repo + "/git" + "/trees/" + commitId;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Failed to fetch branches");
        }
    }

    /**
     * Retrieves the raw content of a file in a repository at a specific commit.
     *
     * @param username The username of the repository owner.
     * @param repo The name of the repository.
     * @param commitId The commit ID.
     * @param path The file path within the repository.
     * @return A String representation of the file's raw content.
     * @throws Exception if the request fails or the response is not successful.
     */
    public String getRaw(String username, String repo, String commitId, String path) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://raw.githubusercontent.com/" + username + "/" + repo + "/" + commitId + "/" + path;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Handle non-200 responses accordingly
            throw new Exception("Failed to fetch branches");
        }
    }

    /**
     * Retrieves the commit history of a repository.
     *
     * @param username The username of the repository owner.
     * @param repo The name of the repository.
     * @return A String representation of the repository's commit history.
     * @throws Exception if the request fails or the response is not successful.
     */
    public String getCommits(String username, String repo) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/repos/" + username + "/" + repo + "/commits";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Handle non-200 responses accordingly
            throw new Exception("Failed to fetch branches");
        }
    }


    public void postComment(String username, String repo, String commitSha, String comment) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommentPayload payloadObj = new CommentPayload(comment);
        String payload = objectMapper.writeValueAsString(payloadObj);

        String url = String.format("https://api.github.com/repos/%s/%s/commits/%s/comments", username, repo, commitSha);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + githubToken)
                .header("Accept", "application/vnd.github.v3+json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            throw new IOException("Failed to post comment, Status code: " + response.statusCode() + ", Response: " + response.body());
        }
    }

    public String getLatestCommitSha(String owner, String repo, String path) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?path=%s", owner, repo, path);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + githubToken)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Failed to retrieve commit history for the file: " + response.body());
        }

        System.out.println("API Response:");
        System.out.println(response.body());

        // Parse the response to extract the most recent commit SHA
        String commitSha = extractCommitShaFromResponse(response.body());
        return commitSha;
    }

    private String extractCommitShaFromResponse(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Check if the response is an array and not empty
        if (rootNode.isArray() && rootNode.size() > 0) {
            // Get the "sha" value from the first JSON object
            JsonNode firstObject = rootNode.get(0);
            if (firstObject.has("sha")) {
                return firstObject.get("sha").asText();
            }
        }

        throw new IOException("SHA key not found in the response.");
    }


    private static class CommentPayload {
        private final String body;

        public CommentPayload(String body) {
            this.body = body;
        }

        public String getBody() {
            return body;
        }
    }

}
