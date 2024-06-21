package com.example.serverside.analysis.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * This class is used to check for deprecated or insecure API usage in Java source code.
 * It extends VoidVisitorAdapter to traverse the AST nodes and identify deprecated imports.
 */
public class DeprecatedApiCheckerUtil extends VoidVisitorAdapter<Void> {
    private List<String> vulnerabilities;
    static final Map<String, String> insecureImports = new HashMap<>();

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/main/resources/deprecatedApis.json");
            Map<String, Map<String, String>> data = objectMapper.readValue(
                    file, new TypeReference<Map<String, Map<String, String>>>() {}
            );
            insecureImports.putAll(data.get("insecureImports"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs a DeprecatedApiCheckerUtil instance.
     *
     * @param vulnerabilities A list to store detected vulnerabilities related to deprecated API usage.
     */
    public DeprecatedApiCheckerUtil(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }


    /**
     * Visits an ImportDeclaration node in the AST.
     * If the import matches any entry in the insecureImports map, a vulnerability is recorded.
     *
     * @param importDeclaration The ImportDeclaration node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(ImportDeclaration importDeclaration, Void arg) {
        super.visit(importDeclaration, arg);
        String importName = importDeclaration.getNameAsString();
        if (insecureImports.containsKey(importName)) {
            importDeclaration.getRange().ifPresent(range -> {
                String message = String.format("Violation at line %d: Insecure import used: %s. Recommended alternative: %s",
                        range.begin.line, importName, insecureImports.get(importName));
                vulnerabilities.add(message);
            });
        }
    }
}