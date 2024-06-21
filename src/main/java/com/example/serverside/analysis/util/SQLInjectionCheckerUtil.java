package com.example.serverside.analysis.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class extends VoidVisitorAdapter to check for potential SQL injection vulnerabilities in Java source code.
 * It identifies and flags instances where SQL queries are built using string concatenation, which might be susceptible to SQL injection attacks.
 */
public class SQLInjectionCheckerUtil extends VoidVisitorAdapter<Void> {
    private final List<String> vulnerabilities;
    private final Set<String> potentiallyUnsafeVariables = new HashSet<>();


    /**
     * Constructor for SQLInjectionCheckerUtil.
     *
     * @param vulnerabilities A list to store detected SQL injection vulnerabilities.
     */
    public SQLInjectionCheckerUtil(List<String> vulnerabilities)  {
        this.vulnerabilities = vulnerabilities;
    }


    /**
     * Visits VariableDeclarator nodes in the AST.
     * Checks for String variables that are initialized using concatenation, as they may be used unsafely in SQL queries.
     *
     * @param variableDeclarator The VariableDeclarator node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(VariableDeclarator variableDeclarator, Void arg) {
        super.visit(variableDeclarator, arg);
        if (variableDeclarator.getType().asString().equals("String") && variableDeclarator.getInitializer().isPresent()) {
            Expression initializer = variableDeclarator.getInitializer().get();
            if (isPotentiallyUnsafeConcatenation(initializer)) {
                potentiallyUnsafeVariables.add(variableDeclarator.getNameAsString());
            }
        }
    }


    /**
     * Visits AssignExpr nodes in the AST.
     * Checks for assignments to String variables using concatenation that might be susceptible to SQL injection.
     *
     * @param assignExpr The AssignExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(AssignExpr assignExpr, Void arg) {
        super.visit(assignExpr, arg);
        if (assignExpr.getTarget().isNameExpr() && assignExpr.getValue().isBinaryExpr()) {
            if (isPotentiallyUnsafeConcatenation(assignExpr.getValue())) {
                potentiallyUnsafeVariables.add(assignExpr.getTarget().toString());
            }
        }
    }


    /**
     * Determines if an expression involves potentially unsafe string concatenation.
     *
     * @param expr The expression to check.
     * @return True if the expression involves concatenation, false otherwise.
     */
    private boolean isPotentiallyUnsafeConcatenation(Expression expr) {
        // Logic to determine if the expression involves potentially unsafe string concatenation
        return expr.isBinaryExpr() && expr.asBinaryExpr().getOperator() == BinaryExpr.Operator.PLUS;
    }


    /**
     * Visits MethodCallExpr nodes in the AST.
     * Checks for SQL execution methods that use potentially unsafe concatenated strings.
     *
     * @param methodCallExpr The MethodCallExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(MethodCallExpr methodCallExpr, Void arg) {
        super.visit(methodCallExpr, arg);
        if (isSqlExecuteMethod(methodCallExpr.getNameAsString())) {
            for (Expression argExpr : methodCallExpr.getArguments()) {
                if (argExpr.isNameExpr() && potentiallyUnsafeVariables.contains(argExpr.asNameExpr().getNameAsString())) {
                    int line = argExpr.getBegin().map(r -> r.line).orElse(-1);
                    String message = "Violation at line " + line + ": Potential SQL Injection detected with variable '" + argExpr.toString() + "' at line " + line;
                    vulnerabilities.add(message);
                }
            }
        }
    }


    /**
     * Checks if a method name corresponds to an SQL execution method.
     *
     * @param methodName The name of the method to check.
     * @return True if the method is a known SQL execution method, false otherwise.
     */
    private boolean isSqlExecuteMethod(String methodName) {
        Set<String> sqlMethods = new HashSet<>(Set.of("executeQuery", "execute", "executeUpdate"));
        return sqlMethods.contains(methodName);
    }
}

