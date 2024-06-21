package com.example.serverside.mongoDB.controller;

import com.example.serverside.analysis.result.CombinedResult;
import com.example.serverside.mongoDB.document.*;
import com.example.serverside.mongoDB.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST Controller for managing combined analysis result data.
 * This controller handles HTTP requests for retrieving combined results from various analysis services.
 */
@RestController
@RequestMapping("/api/results-all")
public class CombinedResultController {

    // Individual services for different types of analysis results
    private final SecurityResultService securityResultService;
    private final ComplexityResultService complexityResultService;
    private final SmellResultService smellResultService;
    private final QualityResultService qualityResultService;
    private final StyleResultService styleResultService;
    private final AllResultService allResultService;

    /**
     * Constructs a CombinedResultController with the specified services.
     *
     * @param securityResultService   The service for handling security result data.
     * @param complexityResultService The service for handling complexity result data.
     * @param smellResultService      The service for handling code smell result data.
     * @param qualityResultService    The service for handling quality result data.
     * @param styleResultService      The service for handling style result data.
     * @param allResultService        The service for handling all combined result data.
     */
    @Autowired
    public CombinedResultController(SecurityResultService securityResultService,
                                    ComplexityResultService complexityResultService,
                                    SmellResultService smellResultService,
                                    QualityResultService qualityResultService,
                                    StyleResultService styleResultService,
                                    AllResultService allResultService
    ) {
        this.securityResultService = securityResultService;
        this.complexityResultService = complexityResultService;
        this.smellResultService = smellResultService;
        this.qualityResultService = qualityResultService;
        this.styleResultService = styleResultService;
        this.allResultService = allResultService;
    }

    /**
     * Endpoint to retrieve a combined analysis result by its ID.
     * This endpoint aggregates results from different analysis services into a single response.
     *
     * @param id The ID of the combined analysis result to retrieve.
     * @return A ResponseEntity containing the CombinedResult.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CombinedResult> getCombinedResultById(@PathVariable String id) {
        Optional<SecurityResultDocument> securityResult = securityResultService.getSecurityResultById(id);
        Optional<ComplexityResultDocument> complexityResult = complexityResultService.getComplexityResultById(id);
        Optional<CodeSmellResultDocument> smellResult = smellResultService.getSmellResultById(id);
        Optional<QualityResultDocument> qualityResult = qualityResultService.getQualityResultById(id);
        Optional<StyleResultDocument> styleResult = styleResultService.getStyleResultById(id);
        Optional<CombinedAnalysisResultDocument> allResult = allResultService.getAllResultById(id);

        CombinedResult combinedResult = new CombinedResult(
                securityResult.orElse(null),
                complexityResult.orElse(null),
                smellResult.orElse(null),
                qualityResult.orElse(null),
                styleResult.orElse(null),
                allResult.orElse(null)

        );

        if (!securityResult.isPresent() && !complexityResult.isPresent() && !smellResult.isPresent() && !qualityResult.isPresent() && !styleResult.isPresent() && !allResult.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(combinedResult);
    }

    /**
     * Endpoint to retrieve a combined analysis result by its customID.
     * This endpoint aggregates results from different analysis services into a single response.
     *
     * @param customId The ID of the combined analysis result to retrieve.
     * @return A ResponseEntity containing the CombinedResult.
     */
    @GetMapping("/custom/{customId}")
    public ResponseEntity<CombinedResult> getCombinedResultByCustomId(@PathVariable String customId) {
        Optional<SecurityResultDocument> securityResult = securityResultService.getSecurityResultByCustomId(customId);
        Optional<ComplexityResultDocument> complexityResult = complexityResultService.getComplexityResultByCustomId(customId);
        Optional<CodeSmellResultDocument> smellResult = smellResultService.getSmellResultByCustomId(customId);
        Optional<QualityResultDocument> qualityResult = qualityResultService.getQualityResultByCustomId(customId);
        Optional<StyleResultDocument> styleResult = styleResultService.getStyleResultByCustomId(customId);
        Optional<CombinedAnalysisResultDocument> allResult = allResultService.getAllResultByCustomId(customId);

        CombinedResult combinedResult = new CombinedResult(
                securityResult.orElse(null),
                complexityResult.orElse(null),
                smellResult.orElse(null),
                qualityResult.orElse(null),
                styleResult.orElse(null),
                allResult.orElse(null)

        );

        if (!securityResult.isPresent() && !complexityResult.isPresent() && !smellResult.isPresent() && !qualityResult.isPresent() && !styleResult.isPresent() && !allResult.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(combinedResult);
    }
}
