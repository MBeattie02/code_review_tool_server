package com.example.serverside.analysis.util;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class that extends VoidVisitorAdapter to check for hardcoded credentials in Java source code.
 * It identifies potential security vulnerabilities where sensitive information like passwords,
 * tokens, or secrets are hardcoded.
 */
public class HardcodedCredentialsCheckerUtil extends VoidVisitorAdapter<Void> {
    private static final Pattern CREDENTIALS_PATTERN = Pattern.compile(".*(password|secret|token).*", Pattern.CASE_INSENSITIVE);
    private final List<String> vulnerabilities;


    /**
     * Constructs a HardcodedCredentialsCheckerUtil instance.
     *
     * @param vulnerabilities A list to store detected vulnerabilities regarding hardcoded credentials.
     */
    public HardcodedCredentialsCheckerUtil(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }


    /**
     * Visits a VariableDeclarator node in the AST.
     * It checks variable names and initializers for hardcoded credentials.
     *
     * @param n   The VariableDeclarator node.
     * @param arg A user argument (not used in this implementation).
     */

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        super.visit(n, arg);
        checkForHardcodedCredentials(n.getNameAsString(), n.getInitializer().orElse(null), n.getBegin().orElse(null));
    }


    /**
     * Visits a MethodCallExpr node in the AST.
     * It checks method call arguments for hardcoded credentials.
     *
     * @param n   The MethodCallExpr node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        n.getArguments().forEach(argExpr -> checkForHardcodedCredentials(null, argExpr, argExpr.getBegin().orElse(null)));
    }


    /**
     * Checks for hardcoded credentials in variable names or values.
     * If detected, it adds a message to the vulnerabilities list.
     *
     * @param varName    The name of the variable to check.
     * @param valueExpr  The expression representing the variable's value.
     * @param position   The position of the expression in the source code.
     */
    private void checkForHardcodedCredentials(String varName, Expression valueExpr, Position position) {
        if (valueExpr != null && valueExpr.isStringLiteralExpr()) {
            String value = valueExpr.asStringLiteralExpr().getValue();
            Matcher valueMatcher = CREDENTIALS_PATTERN.matcher(value);
            if ((varName != null && CREDENTIALS_PATTERN.matcher(varName).matches()) || valueMatcher.matches()) {
                String message = position != null ?
                        "Violation at line " + position.line + ": Potential hardcoded credentials detected: \"" + value + "\" at line " + position.line :
                        "Violation at line : Potential hardcoded credentials detected: \"" + value + "\"";
                vulnerabilities.add(message);
            }
        }
    }
}

