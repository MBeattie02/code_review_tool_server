package com.example.serverside.analysis.util;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class extends VoidVisitorAdapter to check for potential race conditions in Java source code.
 * It identifies accesses to shared resources (like static or volatile fields) and checks if these accesses
 * are properly synchronized to prevent race conditions.
 */
public class RaceConditionCheck extends VoidVisitorAdapter<Void> {

    private final List<String> raceConditions;
    private final Set<String> sharedResources;


    /**
     * Constructor for RaceConditionCheck.
     *
     * @param raceConditions A list to store detected race conditions.
     */
    public RaceConditionCheck(List<String> raceConditions) {
        this.raceConditions = raceConditions;
        this.sharedResources = new HashSet<>();
    }


    /**
     * Visits FieldDeclaration nodes to identify shared resources.
     * Marks static or volatile fields as shared resources.
     *
     * @param n   The FieldDeclaration node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(FieldDeclaration n, Void arg) {
        super.visit(n, arg);
        if (n.isStatic() || n.isVolatile()) {
            sharedResources.add(n.getVariables().get(0).getNameAsString());
        }
    }


    /**
     * Visits MethodCallExpr nodes to detect accesses to shared resources that might lead to race conditions.
     *
     * @param n   The MethodCallExpr node.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (sharedResources.contains(n.getNameAsString()) && !isSynchronized(n)) {
            raceConditions.add("Violation at line " +
                    n.getRange().map(r -> r.begin.line).orElse(-1) +
                    ": Potential race condition detected with shared resource '" +
                    n.getNameAsString() + "'");
        }
    }


    /**
     * Visits FieldAccessExpr nodes to check for race conditions related to shared resource access.
     *
     * @param n   The FieldAccessExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        super.visit(n, arg);
        if (sharedResources.contains(n.getNameAsString()) && !isSynchronized(n)) {
            raceConditions.add("Violation at line " +
                    n.getRange().map(r -> r.begin.line).orElse(-1) +
                    ": Potential race condition detected with shared resource '" +
                    n.getNameAsString() + "'");
        }
    }


    /**
     * Visits AssignExpr nodes to detect race conditions in assignments to shared resources.
     *
     * @param n   The AssignExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(AssignExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getTarget() instanceof NameExpr) {
            NameExpr nameExpr = (NameExpr) n.getTarget();
            if (sharedResources.contains(nameExpr.getNameAsString()) && !isSynchronized(n)) {
                raceConditions.add("Violation at line " +
                        n.getRange().map(r -> r.begin.line).orElse(-1) +
                        ": Potential race condition detected in assignment to shared resource '" +
                        nameExpr.getNameAsString() + "'");
            }
        }
    }


    /**
     * Visits NameExpr nodes to identify usage of shared resources and check for race conditions.
     *
     * @param n   The NameExpr node being visited.
     * @param arg A user argument (not used in this implementation).
     */
    @Override
    public void visit(NameExpr n, Void arg) {
        super.visit(n, arg);
        if (sharedResources.contains(n.getNameAsString()) && !isSynchronized(n)) {
            raceConditions.add("Violation at line " +
                    n.getRange().map(r -> r.begin.line).orElse(-1) +
                    ": Potential race condition detected with shared resource '" +
                    n.getNameAsString() + "'");
        }
    }

    /**
     * Checks if the provided node is within a synchronized context.
     * This can be a synchronized method or a synchronized block.
     *
     * @param node The AST node to check.
     * @return True if the node is within a synchronized context, false otherwise.
     */
    private boolean isSynchronized(Node node) {
        // Check for synchronization in the current context
        Node current = node;
        while (current != null) {
            if (current instanceof SynchronizedStmt ||
                    (current instanceof MethodDeclaration && ((MethodDeclaration) current).isSynchronized())) {
                return true;
            }
            current = current.getParentNode().orElse(null);
        }
        return false;
    }

}
