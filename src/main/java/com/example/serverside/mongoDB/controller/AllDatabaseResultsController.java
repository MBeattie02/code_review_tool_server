package com.example.serverside.mongoDB.controller;

import com.example.serverside.analysis.result.CombinedResultsList;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for aggregating and managing various analysis results.
 * This controller provides endpoints for retrieving results from different analysis services.
 */
@RestController
@RequestMapping("/api/combined-results")
public class AllDatabaseResultsController {
    // Fields for different analysis result services
    private final SecurityResultService securityResultService;
    private final ComplexityResultService complexityResultService;
    private final SmellResultService smellResultService;
    private final QualityResultService qualityResultService;
    private final StyleResultService styleResultService;
    private final AllResultService allResultService;

    /**
     * Constructs an AllDatabaseResultsController with the specified services.
     *
     * @param securityResultService   The service for handling security result data.
     * @param complexityResultService The service for handling complexity result data.
     * @param smellResultService      The service for handling code smell result data.
     * @param qualityResultService    The service for handling quality result data.
     * @param styleResultService      The service for handling style result data.
     * @param allResultService        The service for handling all combined result data.
     */
    @Autowired
    public AllDatabaseResultsController(SecurityResultService securityResultService,
                                      ComplexityResultService complexityResultService,
                                      SmellResultService smellResultService,
                                      QualityResultService qualityResultService,
                                      StyleResultService styleResultService,
                                      AllResultService allResultService) {
        this.securityResultService = securityResultService;
        this.complexityResultService = complexityResultService;
        this.smellResultService = smellResultService;
        this.qualityResultService = qualityResultService;
        this.styleResultService = styleResultService;
        this.allResultService = allResultService;
    }

    /**
     * Endpoint to retrieve all security results.
     * Returns a list of all security result documents.
     *
     * @return A ResponseEntity containing a list of SecurityResultDocuments.
     */
    @GetMapping("/security")
    public ResponseEntity<List<SecurityResultDocument>> getAllSecurityResults() {
        List<SecurityResultDocument> results = securityResultService.getAllSecurityResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve all complexity results.
     * Returns a list of all complexity result documents.
     *
     * @return A ResponseEntity containing a list of ComplexityResultDocuments.
     */
    @GetMapping("/complexity")
    public ResponseEntity<List<ComplexityResultDocument>> getAllComplexityResults() {
        List<ComplexityResultDocument> results = complexityResultService.getAllComplexityResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve all style results.
     * Returns a list of all style result documents.
     *
     * @return A ResponseEntity containing a list of StyleResultDocuments.
     */
    @GetMapping("/style")
    public ResponseEntity<List<StyleResultDocument>> getAllStyleResults() {
        List<StyleResultDocument> results = styleResultService.getAllStyleResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve all smell results.
     * Returns a list of all smell result documents.
     *
     * @return A ResponseEntity containing a list of CodeSmellResultDocuments.
     */
    @GetMapping("/smell")
    public ResponseEntity<List<CodeSmellResultDocument>> getAllSmellResults() {
        List<CodeSmellResultDocument> results = smellResultService.getAllSmellResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve all quality results.
     * Returns a list of all quality result documents.
     *
     * @return A ResponseEntity containing a list of QualityResultDocuments.
     */
    @GetMapping("/quality")
    public ResponseEntity<List<QualityResultDocument>> getAllQualityResults() {
        List<QualityResultDocument> results = qualityResultService.getAllQualityResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve a combined list of all analysis results.
     * Aggregates all types of analysis results into a single response.
     *
     * @return A ResponseEntity containing the CombinedResultsList.
     */
    @GetMapping("/all")
    public ResponseEntity<CombinedResultsList> getAllResults() {
        List<SecurityResultDocument> securityResults = securityResultService.getAllSecurityResults();
        List<ComplexityResultDocument> complexityResults = complexityResultService.getAllComplexityResults();
        List<CodeSmellResultDocument> codeSmellResults = smellResultService.getAllSmellResults();
        List<QualityResultDocument> qualityResults = qualityResultService.getAllQualityResults();
        List<StyleResultDocument> styleResults = styleResultService.getAllStyleResults();
        List<CombinedAnalysisResultDocument> allCombinedResults = allResultService.getAllResults();

        CombinedResultsList combinedResultslist = new CombinedResultsList(
                securityResults,
                complexityResults,
                codeSmellResults,
                qualityResults,
                styleResults,
                allCombinedResults
        );

        return ResponseEntity.ok(combinedResultslist);
    }

    /**
     * Endpoint to retrieve a combined list of all analysis results filtered by path.
     * Aggregates all types of analysis results into a single response based on the specified path.
     *
     * @param path The path used to filter the results.
     * @return A ResponseEntity containing the CombinedResultsList.
     */
    @GetMapping("/all/path/{path}")
    public ResponseEntity<CombinedResultsList> getAllResultsByUsername(@PathVariable String path) {
        List<SecurityResultDocument> securityResults = securityResultService.getSecurityResultByPath(path);
        List<ComplexityResultDocument> complexityResults = complexityResultService.getComplexityResultPath(path);
        List<CodeSmellResultDocument> codeSmellResults = smellResultService.getSmellResultsByPath(path);
        List<QualityResultDocument> qualityResults = qualityResultService.getQualityResultByPath(path);
        List<StyleResultDocument> styleResults = styleResultService.getStyleResultByPath(path);
        List<CombinedAnalysisResultDocument> allCombinedResults = allResultService.getAllResultByPath(path);

        CombinedResultsList combinedResultsList = new CombinedResultsList(
                securityResults,
                complexityResults,
                codeSmellResults,
                qualityResults,
                styleResults,
                allCombinedResults
        );

        return ResponseEntity.ok(combinedResultsList);
    }

}
