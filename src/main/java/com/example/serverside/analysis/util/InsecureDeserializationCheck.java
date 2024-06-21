package com.example.serverside.analysis.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class extends VoidVisitorAdapter to check for potential insecure deserialization vulnerabilities
 * in Java source code. It identifies usage of ObjectInputStream and checks if it is being fed data from
 * user-controlled sources, which could lead to security vulnerabilities.
 */
public class InsecureDeserializationCheck extends VoidVisitorAdapter<Void> {

    private final List<String> vulnerabilities;
    private final Set<String> objectInputStreamVariables;
    private final Set<String> userControlledDataSources;


    /**
     * Constructor for InsecureDeserializationCheck.
     *
     * @param vulnerabilities A list to store detected vulnerabilities.
     */
    public InsecureDeserializationCheck(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
        this.objectInputStreamVariables = new HashSet<>();
        this.userControlledDataSources = new HashSet<>();
    }


    /**
     * Visits VariableDeclarator nodes and identifies ObjectInputStream variables and
     * variables with user-controlled data sources.
     *
     * @param var The VariableDeclarator node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(VariableDeclarator var, Void arg) {
        super.visit(var, arg);
        String varName = var.getNameAsString();
        if (var.getType().asString().equals("ObjectInputStream")) {
            objectInputStreamVariables.add(varName);
        } else if (var.getInitializer().isPresent()) {
            Expression initializer = var.getInitializer().get();
            if (isUserControlledSource(initializer)) {
                userControlledDataSources.add(varName);
            }
        }
    }


    /**
     * Visits ObjectCreationExpr nodes, particularly to check for ObjectInputStream creation
     * with potentially unsafe data sources.
     *
     * @param n   The ObjectCreationExpr node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getType().asString().equals("ObjectInputStream")) {
            n.getArguments().forEach(argExpr -> {
                if (argExpr instanceof NameExpr && userControlledDataSources.contains(((NameExpr) argExpr).getNameAsString())) {
                    reportInsecureDeserialization(n.getRange().map(r -> r.begin.line).orElse(-1));
                }
            });
        }
    }


    /**
     * Visits MethodCallExpr nodes, especially looking for 'readObject' calls on ObjectInputStream
     * instances to detect potential deserialization vulnerabilities.
     *
     * @param n   The MethodCallExpr node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getNameAsString().equals("readObject") && n.getScope().isPresent()) {
            String scopeName = n.getScope().get().toString();
            if (objectInputStreamVariables.contains(scopeName)) {
                reportInsecureDeserialization(n.getRange().map(r -> r.begin.line).orElse(-1));
            }
        }
    }


    /**
     * Determines if the provided expression is a user-controlled source. This can be direct user input
     * or data derived from a user-controlled source.
     *
     * @param expr The expression to check.
     * @return True if the expression is user-controlled, false otherwise.
     */
    private boolean isUserControlledSource(Expression expr) {
        if (expr.isMethodCallExpr()) {
            return isDirectUserInputMethod(expr.asMethodCallExpr());
        } else if (expr.isNameExpr()) {
            return userControlledDataSources.contains(expr.asNameExpr().getNameAsString());
        }

        return false;
    }


    /**
     * Checks if the provided method call expression is a direct user input method.
     * This method can be configured to include more methods based on the application's context.
     *
     * @param methodCallExpr The method call expression to check.
     * @return True if the method is known to be a direct user input method, false otherwise.
     */
    private boolean isDirectUserInputMethod(MethodCallExpr methodCallExpr) {
        String methodName = methodCallExpr.getNameAsString();

        Set<String> userInputMethods = new HashSet<>(Set.of("getParameter", "getHeader", "getQueryString"));
        return userInputMethods.contains(methodName);
    }


    /**
     * Reports a potential insecure deserialization vulnerability at the specified line number.
     *
     * @param line The line number where the vulnerability was detected.
     */
    private void reportInsecureDeserialization(int line) {
        String message = "Violation at line " + line + ": Potential insecure deserialization detected at line " + line;
        vulnerabilities.add(message);
    }
}
