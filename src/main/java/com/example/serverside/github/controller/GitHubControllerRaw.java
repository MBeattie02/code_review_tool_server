package com.example.serverside.github.controller;

import com.example.serverside.github.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for fetching raw content from GitHub repositories.
 * This controller handles HTTP requests to retrieve raw file content from GitHub using the GitHubService.
 */
@RestController
@RequestMapping("/raw/githubusercontent")
public class GitHubControllerRaw {

    @Autowired
    private GitHubService gitHubService; // Service for interacting with GitHub

    /**
     * Endpoint to retrieve raw content of a file in a GitHub repository.
     * Retrieves the content based on the specified username, repository name, commit ID, and file path.
     *
     * @param username The GitHub username of the repository owner.
     * @param repo The name of the GitHub repository.
     * @param commitId The commit ID in the repository.
     * @param path The path of the file in the repository.
     * @return A ResponseEntity containing the raw file content or an error message.
     */
    @GetMapping("/{username}/{repo}/{commitId}/{path}")
    public ResponseEntity<String> getUserRepositories(@PathVariable String username, @PathVariable String repo, @PathVariable String commitId, @PathVariable String path) {
        try {
            String response = gitHubService.getRaw(username, repo, commitId, path);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the raw content of a file from a specific commit in a GitHub repository.
     *
     * @param username The username of the GitHub account.
     * @param repo The name of the repository.
     * @param commitId The ID of the commit.
     * @param path The file path within the repository.
     * @return A ResponseEntity containing the raw file content or an error message.
     */
    @GetMapping
    public ResponseEntity<String> getUserRepositoriesParam(
            @RequestParam("username") String username,
            @RequestParam("repo") String repo,
            @RequestParam("commitId") String commitId,
            @RequestParam("path") String path) {
        try {
            String response = gitHubService.getRaw(username, repo, commitId, path);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
