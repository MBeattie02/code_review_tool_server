package com.example.serverside.analysis.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

/**
 * This class is responsible for checking insecure cryptographic practices in Java source code.
 * It extends VoidVisitorAdapter to traverse the AST nodes and identify potential vulnerabilities.
 */
public class InsecureCryptoPracticesCheck extends VoidVisitorAdapter<Void> {
    private List<String> vulnerabilities;
    private Set<String> weakAlgorithms;
    private Map<String, String> variableValues;


    /**
     * Constructs an InsecureCryptoPracticesCheck instance.
     *
     * @param vulnerabilities A list to store detected vulnerabilities.
     */
    public InsecureCryptoPracticesCheck(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
        this.weakAlgorithms = new HashSet<>(Arrays.asList("DES", "MD5", "RC4"));
        this.variableValues = new HashMap<>();
    }


    /**
     * Visits a VariableDeclarator node in the AST.
     * It captures the value of any string literals assigned to variables for later analysis.
     *
     * @param n   The VariableDeclarator node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(VariableDeclarator n, Void arg) {
        super.visit(n, arg);
        n.getInitializer().ifPresent(initializer -> {
            if (initializer.isStringLiteralExpr()) {
                variableValues.put(n.getNameAsString(), initializer.asStringLiteralExpr().getValue());
            }
        });
    }


    /**
     * Visits a MethodCallExpr node in the AST.
     * Specifically looks for uses of cryptographic methods and checks if weak algorithms are used.
     *
     * @param n   The MethodCallExpr node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getNameAsString().equals("getInstance") && !n.getArguments().isEmpty()) {
            String algorithm = resolveAlgorithm(n.getArgument(0));
            if (algorithm != null && weakAlgorithms.contains(algorithm)) {
                reportVulnerability(algorithm, n.getBegin().map(r -> r.line).orElse(-1));
            }
        }
    }


    /**
     * Resolves the algorithm name from an expression.
     * It handles direct string literals and references to variables.
     *
     * @param expression The expression potentially representing an algorithm name.
     * @return The resolved algorithm name, or null if it cannot be resolved.
     */
    private String resolveAlgorithm(Expression expression) {
        if (expression.isStringLiteralExpr()) {
            return expression.asStringLiteralExpr().getValue();
        } else if (expression.isNameExpr()) {
            return variableValues.getOrDefault(expression.asNameExpr().getNameAsString(), null);
        }

        return null;
    }

    private void reportVulnerability(String algorithm, int line) {
        vulnerabilities.add("Violation at line " + line +": Weak cryptographic algorithm '" + algorithm + "' used at line " + line);
    }
}


