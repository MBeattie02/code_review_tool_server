package com.example.serverside.analysis.controller;

import com.example.serverside.github.service.GitHubService;
import com.example.serverside.analysis.result.*;
import com.example.serverside.analysis.service.*;
import com.example.serverside.mongoDB.document.CombinedAnalysisResultDocument;
import com.example.serverside.mongoDB.info.RepositoryInfo;
import com.example.serverside.mongoDB.service.MongoDBService;
import com.example.serverside.slack.message.ComposeSlackMessage;
import com.example.serverside.slack.service.SlackNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST Controller for handling code analysis operations.
 * This controller provides endpoints for performing style, complexity, security, code smell, quality analyses,
 * and a combined analysis on code. It interacts with various analysis services and a MongoDB service for data persistence.
 */
@RestController
@RequestMapping("/api")
public class CodeAnalysisController {
    // Services for different types of code analysis and data persistence
    private final GitHubService gitHubService;

    private final CodeSecurityAnalysisService codeSecurityAnalysisService;

    private final CodeStyleAnalysisService codeStyleAnalysisService;

    private final CodeComplexityAnalysisService codeComplexityAnalysisService;

    private final CodeSmellAnalysisService codeSmellAnalysisService;

    private final CodeQualityAnalysisService codeQualityAnalysisService;

    private final MongoDBService mongoDBService;

    private final SlackNotificationService slackNotificationService;

    /**
     * Constructor for CodeAnalysisController.
     * Initializes the controller with necessary services for code analysis and data handling.
     */
    public CodeAnalysisController(GitHubService gitHubService,
                                  CodeSecurityAnalysisService codeSecurityAnalysisService,
                                  CodeStyleAnalysisService codeStyleAnalysisService,
                                  CodeComplexityAnalysisService codeComplexityAnalysisService,
                                  CodeSmellAnalysisService codeSmellAnalysisService,
                                  CodeQualityAnalysisService codeQualityAnalysisService,
                                  MongoDBService mongoDBService,
                                  SlackNotificationService slackNotificationService
                                  ) {
        this.gitHubService = gitHubService;
        this.codeSecurityAnalysisService = codeSecurityAnalysisService;
        this.codeStyleAnalysisService = codeStyleAnalysisService;
        this.codeComplexityAnalysisService = codeComplexityAnalysisService;
        this.codeSmellAnalysisService = codeSmellAnalysisService;
        this.codeQualityAnalysisService = codeQualityAnalysisService;
        this.mongoDBService = mongoDBService;
        this.slackNotificationService = slackNotificationService;
    }

    /**
     * Endpoint for analyzing code style.
     * Fetches code from GitHub and performs a style analysis.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the StyleResult or an error message.
     */
    @GetMapping("/analyse-style")
    public ResponseEntity<StyleResult> analyseCodeStyle(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);
            StyleResult result = codeStyleAnalysisService.analyse(code);
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            result.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            result.setCustomId(customId); // Set custom ID for readability

            String documentId = mongoDBService.saveStyleResult(result);
            result.setId(documentId); // Set the ID in the complexityResult object
            String comment = result.toString();

            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }

            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageStyle(result, documentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(result);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint for analyzing code complexity.
     * Fetches code from GitHub and performs a complexity analysis.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the ComplexityResult or an error message.
     */
    @GetMapping("/analyse-complexity")
    public ResponseEntity<ComplexityResult> analyseComplexity(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);

            ComplexityResult complexityResult = codeComplexityAnalysisService.calculateComplexity(code);
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            complexityResult.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            complexityResult.setCustomId(customId); // Set custom ID for readability

            complexityResult.setCyclomaticComplexity(complexityResult.getCyclomaticComplexity());
            String documentId = mongoDBService.saveComplexityResult(complexityResult);
            complexityResult.setId(documentId); // Set the ID in the complexityResult object
            String comment = complexityResult.toString();

            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }

            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageComplexity(complexityResult, documentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(complexityResult);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint for analyzing code security.
     * Fetches code from GitHub and performs a security analysis.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the SecurityResult or an error message.
     */
    @GetMapping("/analyse-security")
    public ResponseEntity<SecurityResult> analyseEntropy(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);

            SecurityResult result = codeSecurityAnalysisService.analyse(code);
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            result.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            result.setCustomId(customId); // Set custom ID for readability

            String documentId = mongoDBService.saveSecurityResult(result);
            result.setId(documentId); // Set the ID in the complexityResult object

            String comment = result.toString();
            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }
            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageSecurity(result, documentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(result);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint for analyzing code smells.
     * Fetches code from GitHub and performs an analysis for code smells.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the CodeSmellResult or an error message.
     */
    @GetMapping("/analyse-code-smells")
    public ResponseEntity<CodeSmellResult> analyseSmells(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);

            CodeSmellResult result = codeSmellAnalysisService.analyse(code);
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            result.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            result.setCustomId(customId); // Set custom ID for readability

            String documentId = mongoDBService.saveCodeSmellResult(result);
            result.setId(documentId); // Set the ID in the complexityResult object

            String comment = result.toString();

            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }

            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageSmell(result, documentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(result);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint for analyzing code quality.
     * Fetches code from GitHub and performs a quality analysis.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the QualityResult or an error message.
     */
    @GetMapping("/analyse-quality")
    public ResponseEntity<QualityResult> analyseQuality(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);

            QualityResult result = codeQualityAnalysisService.analyse(code);
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            result.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            result.setCustomId(customId); // Set custom ID for readability

            String documentId = mongoDBService.saveQualityResult(result);
            result.setId(documentId); // Set the ID in the complexityResult object

            String comment = result.toString();

            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }

            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageQuality(result, documentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(result);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint for analyzing all aspects (style, complexity, security, code smell, quality) of code.
     * Fetches code from GitHub and performs all analysis, returning a combined result.
     *
     * @param username GitHub username of the repository owner.
     * @param repo Name of the GitHub repository.
     * @param commitId Commit ID for the analysis.
     * @param path Path of the file in the repository.
     * @return ResponseEntity containing the CombinedResults or an error message.
     */
    @GetMapping("/analyse-all")
    public ResponseEntity<CombinedResults> analyseAll(
            @RequestParam String username,
            @RequestParam String repo,
            @RequestParam String commitId,
            @RequestParam String path,
            @RequestParam(required = false, defaultValue = "false") boolean shouldPostComment) {

        try {
            String code = gitHubService.getRaw(username, repo, commitId, path);

            // Convert CombinedResults to CombinedAnalysisResultDocument
            CombinedResults results = new CombinedResults();
            results.setQualityResult(codeQualityAnalysisService.analyse(code));
            results.setCodeSmellResult(codeSmellAnalysisService.analyse(code));
            results.setSecurityResult(codeSecurityAnalysisService.analyse(code));
            results.setComplexityResult(codeComplexityAnalysisService.calculateComplexity(code));
            results.setStyleResult(codeStyleAnalysisService.analyse(code));

            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setUsername(username);
            repositoryInfo.setRepo(repo);
            repositoryInfo.setCommitId(commitId);
            repositoryInfo.setPath(path);

            results.setRepositoryInfo(repositoryInfo);

            String customId = username + "-" + repo + "-" + path + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            results.setCustomId(customId); // Set custom ID for readability

            CombinedAnalysisResultDocument document = mongoDBService.convertToCombinedAnalysisResultDocument(results);
            String combinedDocumentId = mongoDBService.saveCombinedAnalysisResult(document);

            // Set other fields in results
            results.setId(combinedDocumentId); // Set the ID in the CombinedResults response
            mongoDBService.saveCombinedAnalysisResult(document);

            String comment = results.toString();
            if (shouldPostComment) {
                String commitSha = gitHubService.getLatestCommitSha(username, repo, path);
                System.out.println("Commit SHA: " + commitSha);
                gitHubService.postComment(username, repo, commitSha, comment);
            }

            // Compose and send the Slack message
            String slackMessage = ComposeSlackMessage.composeSlackMessageAll(results, combinedDocumentId);
            slackNotificationService.send(slackMessage);

            return ResponseEntity.ok(results);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


