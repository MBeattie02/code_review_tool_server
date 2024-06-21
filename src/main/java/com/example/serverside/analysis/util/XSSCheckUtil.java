package com.example.serverside.analysis.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class extends VoidVisitorAdapter to check for potential Cross-Site Scripting (XSS) vulnerabilities in Java source code.
 * It identifies and flags instances where user input might be unsafely included in string concatenations,
 * potentially leading to XSS attacks.
 */
public class XSSCheckUtil extends VoidVisitorAdapter<Void> {
    private final List<String> vulnerabilities;
    private final Set<Integer> reportedLines;
    private final Set<String> userInputVariables;


    /**
     * Constructor for XSSCheckUtil.
     *
     * @param vulnerabilities A list to store detected XSS vulnerabilities.
     */
    public XSSCheckUtil(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
        this.reportedLines = new HashSet<>();
        this.userInputVariables = new HashSet<>();
    }


    /**
     * Visits VariableDeclarator nodes in the AST.
     * Checks if any variables are initialized with values that might be user input, potentially leading to XSS.
     *
     * @param var The VariableDeclarator node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(VariableDeclarator var, Void arg) {
        super.visit(var, arg);
        if (var.getInitializer().isPresent()) {
            Expression initializer = var.getInitializer().get();
            if (isUserInputSource(initializer)) {
                userInputVariables.add(var.getNameAsString());
            }
        }
    }


    /**
     * Visits BinaryExpr nodes in the AST.
     * Checks for string concatenations that include user input, which might be vulnerable to XSS.
     *
     * @param n   The BinaryExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getOperator() == BinaryExpr.Operator.PLUS) {
            int lineNum = n.getRange().map(r -> r.begin.line).orElse(-1);
            if (!reportedLines.contains(lineNum) && containsUserInput(n)) {
                vulnerabilities.add("Violation at line " + lineNum + ": Potential XSS vulnerability detected in string concatenation at line " + lineNum);
                reportedLines.add(lineNum);
            }
        }
    }


    /**
     * Determines if an expression contains user input.
     * User input is considered a potential source of XSS vulnerabilities.
     *
     * @param expr The expression to check.
     * @return True if the expression contains user input, false otherwise.
     */
    private boolean containsUserInput(Expression expr) {
        if (expr.isMethodCallExpr() || expr.isNameExpr()) {
            return isUserInputSource(expr);
        }

        if (expr.isBinaryExpr()) {
            BinaryExpr binaryExpr = expr.asBinaryExpr();
            return containsUserInput(binaryExpr.getLeft()) || containsUserInput(binaryExpr.getRight());
        }

        return false;
    }


    /**
     * Determines if an expression represents a user input source.
     * User input sources are typically methods like getParameter or getHeader.
     *
     * @param expr The expression to check.
     * @return True if the expression is a user input source, false otherwise.
     */
    private boolean isUserInputSource(Expression expr) {
        if (expr.isMethodCallExpr()) {
            MethodCallExpr methodCall = (MethodCallExpr) expr;
            String methodName = methodCall.getNameAsString();
            return methodName.equals("getParameter") || methodName.equals("getHeader");
        }

        if (expr.isNameExpr()) {
            return userInputVariables.contains(expr.asNameExpr().getNameAsString());
        }

        return false;
    }
}
