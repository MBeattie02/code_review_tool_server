package com.example.serverside.mongoDB.controller;

import com.example.serverside.mongoDB.document.SecurityResultDocument;
import com.example.serverside.mongoDB.service.SecurityResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing security result data.
 * This controller handles HTTP requests for retrieving security analysis results.
 */
@RestController
@RequestMapping("/api/results")
public class SecurityResultController {
    private final SecurityResultService securityResultService;

    /**
     * Constructs a SecurityResultController with the specified SecurityResultService.
     *
     * @param securityResultService The service to be used for handling security result data.
     */
    @Autowired
    public SecurityResultController(SecurityResultService securityResultService) {
        this.securityResultService = securityResultService;
    }

    /**
     * Endpoint to retrieve all security results.
     * Returns a list of all security result documents.
     *
     * @return A ResponseEntity containing a list of SecurityResultDocuments.
     */
    @GetMapping
    public ResponseEntity<List<SecurityResultDocument>> getAllSecurityResults() {
        List<SecurityResultDocument> results = securityResultService.getAllSecurityResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to retrieve a specific security result by its ID.
     * Returns the security result document or a 404 not found response if not available.
     *
     * @param id The ID of the security result document to retrieve.
     * @return A ResponseEntity containing the requested SecurityResultDocument.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SecurityResultDocument> getSecurityResultById(@PathVariable String id) {
        Optional<SecurityResultDocument> result = securityResultService.getSecurityResultById(id);
        return result
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
