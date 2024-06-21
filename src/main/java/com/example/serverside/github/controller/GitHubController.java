package com.example.serverside.github.controller;

import com.example.serverside.github.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing interactions with GitHub repositories.
 * This controller uses the GitHubService to fetch repository data, trees, and commits from GitHub.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService; // Service for interacting with GitHub

    /**
     * Endpoint to retrieve details of a specific repository on GitHub.
     * Returns the repository details based on the given username and repository name.
     *
     * @param username The GitHub username of the repository owner.
     * @param repo The name of the GitHub repository.
     * @return A ResponseEntity containing the repository details or an error message.
     */
    @GetMapping("/repos/{username}/{repo}")
    public ResponseEntity<String> getUserRepositories(@PathVariable String username, @PathVariable String repo) {
        try {
            String response = gitHubService.getUserRepositories(username, repo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve the tree structure of a GitHub repository at a specific commit.
     * Returns the tree structure for the specified commit ID.
     *
     * @param username The GitHub username of the repository owner.
     * @param repo The name of the repository.
     * @param commitId The commit ID for which the tree is to be retrieved.
     * @return A ResponseEntity containing the tree structure or an error message.
     */
    @GetMapping("/repos/{username}/{repo}/git/trees/{commitId}")
    public ResponseEntity<String> getTrees(@PathVariable String username, @PathVariable String repo, @PathVariable String commitId) {
        try {
            String response = gitHubService.getTrees(username, repo, commitId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve the commit history of a GitHub repository.
     * Returns the commit history for the specified repository.
     *
     * @param username The GitHub username of the repository owner.
     * @param repo The name of the repository.
     * @return A ResponseEntity containing the commit history or an error message.
     */
    @GetMapping("/repos/{username}/{repo}/commits")
    public ResponseEntity<String> getCommits(@PathVariable String username, @PathVariable String repo) {
        try {
            String response = gitHubService.getCommits(username, repo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
