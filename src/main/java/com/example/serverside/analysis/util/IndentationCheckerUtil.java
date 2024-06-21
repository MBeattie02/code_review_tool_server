package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Utility class for checking the indentation consistency in Java source code.
 * This class extends {@link VoidVisitorAdapter} to traverse the AST (Abstract Syntax Tree)
 * of the source code and check the indentation of block statements.
 *
 * It uses a standard indentation level defined by {@code INDENTATION_SPACES} and compares
 * the indentation of each block statement and its child statements against this standard.
 */
public class IndentationCheckerUtil extends VoidVisitorAdapter<Void> {

    private static final int INDENTATION_SPACES = 4; // Standard number of spaces per indentation level
    private List<String> violations; // List to store identified indentation violations
    private String[] codeLines; // Array of code lines to check


    /**
     * Constructs an {@code IndentationCheckerUtil} instance.
     *
     * @param violations A {@link List} that will be populated with messages about indentation violations.
     * @param code The Java source code as a single string. It will be split into lines for processing.
     */
    public IndentationCheckerUtil(List<String> violations, String code) {
        this.violations = violations;
        this.codeLines = code.split("\\r?\\n"); // Splitting on both Unix and Windows line endings
    }


    /**
     * Initiates the indentation check on the provided source code.
     * Parses the source code into a {@link CompilationUnit} and starts the AST traversal to check indentation.
     */
    public void check() {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = javaParser.parse(String.join("\n", codeLines));
        parseResult.getResult().ifPresent(compilationUnit -> {
            this.visit(compilationUnit, null);
        });
    }

    /**
     * Visits a {@link BlockStmt} node in the AST.
     * Checks the indentation level of the block statement and its child statements.
     *
     * @param n The block statement node to visit.
     * @param arg A context argument (unused in this implementation).
     */
    @Override
    public void visit(BlockStmt n, Void arg) {
        // First, find the block's own starting line to determine its indentation
        int blockStartLine = n.getBegin().get().line - 1;
        String blockStartLineText = codeLines[blockStartLine];
        int blockIndentationLevel = getIndentationLevel(blockStartLineText);

        // Now check each statement in the block
        n.getStatements().forEach(statement -> {
            int statementLine = statement.getBegin().get().line - 1;
            String line = codeLines[statementLine];
            int statementIndentationLevel = getIndentationLevel(line);

            // Expect the statement to be indented one level deeper than the block's indentation
            if (statementIndentationLevel != blockIndentationLevel + INDENTATION_SPACES) {
                String violationMessage = String.format("Violation at line %d: Incorrect indentation . Expected: %d spaces but got: %d. Line: \"%s\"",
                        statementLine + 1,
                        blockIndentationLevel + INDENTATION_SPACES,
                        statementIndentationLevel,
                        line.trim());
                violations.add(violationMessage);
            }
        });

        // It's important to call super to ensure proper traversal of the AST
        super.visit(n, arg);
    }

    /**
     * Determines the indentation level of a given line of code.
     *
     * @param line The line of code whose indentation level is to be measured.
     * @return The number of leading spaces in the line, representing its indentation level.
     */
    private int getIndentationLevel(String line) {
        int level = 0;
        while (level < line.length() && line.charAt(level) == ' ') {
            level++;
        }
        return level; // Return the counted indentation level
    }
}
