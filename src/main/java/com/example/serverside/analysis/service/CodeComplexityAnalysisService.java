package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.ComplexityResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

import org.springframework.stereotype.Service;

@Service
public class CodeComplexityAnalysisService {

    /**
     * Calculates the cyclomatic complexity of a given piece of code.
     * Cyclomatic complexity is a software metric used to indicate the complexity of a program.
     *
     * @param code The source code to analyse.
     * @return The complexity result which includes the cyclomatic complexity score.
     */
    public ComplexityResult calculateComplexity(String code) {
        CompilationUnit cu = StaticJavaParser.parse(code);
        int complexityScore = calculateComplexity(cu);
        System.out.println("Final Calculated Complexity: " + complexityScore);
        return new ComplexityResult(complexityScore);
    }


    /**
     * Calculates the cyclomatic complexity of a given AST node, considering its child nodes.
     * Cyclomatic complexity is computed based on the number of decision points within the node.
     *
     * @param node The AST node for which the complexity needs to be calculated.
     * @return The computed complexity of the node.
     */
    private int calculateComplexity(Node node) {
        int complexity = 0; // Initialize complexity to 0
        for (Node child : node.getChildNodes()) {
            int childComplexity = getComplexityForNode(child);
            complexity += childComplexity;
            System.out.println("Node Type: " + child.getClass().getSimpleName() + ", Child Complexity: " + childComplexity + ", Cumulative Complexity: " + complexity);
        }
        if (node instanceof MethodDeclaration || node instanceof ClassOrInterfaceDeclaration) {
            complexity++; // Increment for method or class declaration
        }
        return complexity;
    }


    /**
     * Determines the complexity contributed by an individual node.
     * This method checks the type of the node and calculates complexity accordingly.
     * Decision structures such as 'if', 'for', 'while', 'switch', and 'do' statements increase complexity
     *
     * @param node The AST node for which the complexity needs to be determined.
     * @return The complexity contributed by this node.
     */
    private int getComplexityForNode(Node node) {
        int complexity = 0;
        if (node instanceof IfStmt) {
            complexity = 1; // 'if' itself is a decision point
        } else if (node instanceof SwitchStmt) {
            complexity = calculateComplexityForSwitchStmt((SwitchStmt) node);
        } else if (node instanceof WhileStmt || node instanceof ForStmt ||
                node instanceof ForEachStmt || node instanceof DoStmt) {
            complexity = 1;
        } else if (node instanceof CatchClause) {
            // Only count catch clause if it's not part of a try-catch block already counted
            if (!(node.getParentNode().orElse(null) instanceof TryStmt)) {
                complexity = 1;
            }
        } else if (node instanceof MethodDeclaration || node instanceof ClassOrInterfaceDeclaration) {
            // Reset complexity for new method or class declarations
            complexity = calculateComplexity(node);
        } else {
            // For other nodes, don't add complexity but continue to check their children
            for (Node child : node.getChildNodes()) {
                complexity += getComplexityForNode(child);
            }
        }
        System.out.println("Node Type: " + node.getClass().getSimpleName() + ", Complexity: " + complexity);
        return complexity;
    }

    /**
     * Calculates the complexity of a switch statement.
     * In cyclomatic complexity, each case within a switch statement is considered a decision point,
     * and contributes to the complexity.
     *
     * @param switchStmt The switch statement AST node.
     * @return The number of decision points in the switch statement, equivalent to the number of cases.
     */
    private int calculateComplexityForSwitchStmt(SwitchStmt switchStmt) {
        return switchStmt.getEntries().size(); // Each case is a decision point
    }
}
