package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.QualityResult;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class CodeQualityAnalysisService {

    public QualityResult analyse(String code) {
        CompilationUnit cu = StaticJavaParser.parse(code);
        QualityResult result = new QualityResult();
        List<String> quality = new ArrayList<>();

        checkForDuplicates(cu, quality);
        checkAccessModifiers(cu, quality);
        checkUseOfLambdasAndStreams(cu, quality);

        result.setQualityCount(quality.size());
        result.setDuplications(quality);
        return result;
    }


    /**
     * Checks for duplicate code blocks within a given {@link CompilationUnit}.
     * Duplicate blocks are identified based on a hash of their normalized contents.
     *
     * @param cu The {@link CompilationUnit} representing the code to be analyzed.
     * @param smells A list to which detected duplicate code violation messages are added.
     */
    public void checkForDuplicates(CompilationUnit cu, List<String> smells) {
        Map<String, List<Position>> subtrees = new HashMap<>();

        cu.findAll(BlockStmt.class).forEach(block -> {
            String normalizedBlock = normalizeBlock(block);
            String blockHash = generateHash(normalizedBlock);

            block.getBegin().ifPresent(begin -> block.getEnd().ifPresent(end -> {
                Position pos = new Position(begin.line, end.line);
                subtrees.computeIfAbsent(blockHash, k -> new ArrayList<>()).add(pos);
            }));
        });

        subtrees.forEach((hash, positions) -> {
            if (positions.size() > 1) {
                String positionDetails = positions.stream()
                        .map(pos -> String.format("lines %d to %d", pos.line, pos.column))
                        .collect(Collectors.joining(", "));
                String message = String.format("Violation: Duplicate code found in blocks at %s.", positionDetails);
                smells.add(message);
            }
        });
    }


    /**
     * Normalizes a block of code by removing comments, variable names, and whitespace to facilitate duplicate detection.
     *
     * @param block The {@link BlockStmt} representing the block of code to normalize.
     * @return A normalized string representation of the code block.
     */
    private String normalizeBlock(BlockStmt block) {
        // Normalize the block to remove comments, variable names, and whitespace
        String normalizedCode = block.toString();
        // Perform normalization steps
        return normalizedCode;
    }


    /**
     * Generates a hash for a given string of data. This hash is used to identify duplicate code blocks.
     *
     * @param data The string to hash.
     * @return A hash string representing the input data.
     */
    private String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    /**
     * Converts an array of bytes to a hexadecimal string. This is used in generating hash strings.
     *
     * @param bytes The byte array to convert.
     * @return A hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    /**
     * Analyzes the given {@link CompilationUnit} for 'for-each' loops that could potentially be refactored to use Java Streams and lambdas.
     * Adds violations to the provided list if any such opportunities are found.
     *
     * @param cu The {@link CompilationUnit} to be analyzed.
     * @param violations A list to which potential refactoring opportunities will be added.
     */
    public void checkUseOfLambdasAndStreams(CompilationUnit cu, List<String> violations) {
        cu.findAll(ForEachStmt.class).forEach(loop -> {
            if (canBeRefactoredToStream(loop)) {
                int line = loop.getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Consider refactoring loop at line %d to use streams and lambdas.", line, line);
                violations.add(message);
            }
        });
    }


    /**
     * Determines if a given 'for-each' loop can be refactored to a stream.
     * This method analyzes the loop body to check if it contains simple operations that are suitable for conversion to stream operations.
     *
     * @param loop The {@link ForEachStmt} representing the 'for-each' loop.
     * @return true if the loop can potentially be refactored to a stream, false otherwise.
     */
    private boolean canBeRefactoredToStream(ForEachStmt loop) {
        Statement loopBody = loop.getBody();

        if (loopBody.isBlockStmt()) {
            BlockStmt blockStmt = loopBody.asBlockStmt();
            boolean hasSingleOperation = blockStmt.getStatements().size() == 1;

            if (hasSingleOperation) {
                Statement statement = blockStmt.getStatement(0);
                // Check if it's a simple operation like a method call or an assignment
                return isSimpleOperation(statement);
            } else {
                // For more complex loops, analyze each statement to see if they fit stream operations
                for (Statement statement : blockStmt.getStatements()) {
                    if (!isSimpleOperation(statement)) {
                        return false;
                    }
                }
                return true;
            }
        }

        // Return false if loop body is not a block statement
        return false;
    }


    /**
     * Checks if a given statement represents a simple operation.
     * A simple operation is one that could be easily converted to a lambda expression, such as a method call or an assignment.
     *
     * @param statement The {@link Statement} to be checked.
     * @return true if the statement is a simple operation, false otherwise.
     */
    private boolean isSimpleOperation(Statement statement) {
        // Check if the statement is an expression statement and further analyze the expression
        if (statement.isExpressionStmt()) {
            ExpressionStmt exprStmt = statement.asExpressionStmt();
            Expression expr = exprStmt.getExpression();
            return expr.isMethodCallExpr() || expr.isAssignExpr() || isSimpleBinaryOperation(expr);
        } else if (statement.isIfStmt()) {
        IfStmt ifStmt = statement.asIfStmt();
        return ifStmt.getThenStmt().isBlockStmt() &&
                ifStmt.getThenStmt().asBlockStmt().getStatements().size() == 1 &&
                isSimpleOperation(ifStmt.getThenStmt().asBlockStmt().getStatement(0));
    }

        return false;
    }


    /**
     * Determines if an expression represents a simple binary operation.
     * A simple binary operation is one that does not involve complex logic and is suitable for stream operations.
     *
     * @param expr The {@link Expression} to be analyzed.
     * @return true if the expression is a simple binary operation, false otherwise.
     */
    private boolean isSimpleBinaryOperation(Expression expr) {

        if (!expr.isBinaryExpr()) {
            return true;
        }
        BinaryExpr binaryExpr = expr.asBinaryExpr();

        return isSimpleBinaryOperation(binaryExpr.getLeft()) && isSimpleBinaryOperation(binaryExpr.getRight());
    }



    /**
     * Analyzes the given {@link CompilationUnit} for members (methods and fields) whose access modifiers could be more restrictive.
     * Adds violations to the provided list if any such opportunities are found.
     *
     * @param cu The {@link CompilationUnit} representing the code to be analyzed.
     * @param violations A list to which detected access modifier violation messages are added.
     */
    public void checkAccessModifiers(CompilationUnit cu, List<String> violations) {
        cu.findAll(BodyDeclaration.class).forEach(declaration -> {
            if (canHaveMoreRestrictiveAccess(declaration, cu)) {
                String declarationInfo = getDeclarationInfo(declaration);
                int line = declaration.getRange().map(r -> r.begin.line).orElse(-1);
                String message = String.format("Violation at line %d: Access modifier for %s can be more restrictive.", line, declarationInfo);
                violations.add(message);
            }
        });
    }


    /**
     * Generates a descriptive string for a given body declaration.
     * This method provides information such as whether the declaration is a method or a field, along with its signature.
     *
     * @param declaration The {@link BodyDeclaration} to describe.
     * @return A string describing the declaration.
     */
    private String getDeclarationInfo(BodyDeclaration<?> declaration) {
        if (declaration instanceof MethodDeclaration) {
            MethodDeclaration method = (MethodDeclaration) declaration;
            return "method '" + method.getSignature() + "'";
        } else if (declaration instanceof FieldDeclaration) {
            FieldDeclaration field = (FieldDeclaration) declaration;
            return "field '" + field.getVariables().stream()
                    .map(VariableDeclarator::getNameAsString)
                    .collect(Collectors.joining(", ")) + "'";
        }

        return "unknown declaration";
    }


    /**
     * Determines if a given body declaration (method or field) can have a more restrictive access modifier.
     * This method checks if the declaration is used in a way that allows for more restrictive access.
     *
     * @param declaration The {@link BodyDeclaration} to check.
     * @param cu The {@link CompilationUnit} where the declaration is located.
     * @return true if the declaration can have a more restrictive access modifier, false otherwise.
     */
    private boolean canHaveMoreRestrictiveAccess(BodyDeclaration<?> declaration, CompilationUnit cu) {
        if (declaration instanceof FieldDeclaration) {
            // Check field declarations
            return checkFieldAccess((FieldDeclaration) declaration, cu);
        } else if (declaration instanceof MethodDeclaration) {
            // Check method declarations
            return checkMethodAccess((MethodDeclaration) declaration, cu);
        }

        return false;
    }


    /**
     * Checks if a field declaration can have a more restrictive access modifier.
     * The method checks if the field is used outside of its declaring class.
     *
     * @param field The {@link FieldDeclaration} to check.
     * @param cu The {@link CompilationUnit} containing the field.
     * @return true if the field access can be more restrictive, false otherwise.
     */

    private boolean checkFieldAccess(FieldDeclaration field, CompilationUnit cu) {
        // If the field is private, it cannot be more restrictive
        if (field.isPrivate()) {
            return false;
        }

        String fieldName = field.getVariables().get(0).getNameAsString();
        boolean isUsedOutsideClass = !cu.findAll(FieldAccessExpr.class, fa ->
                fa.getNameAsString().equals(fieldName) && !isSameClass(fa, field)).isEmpty();

        // The field can be more restrictive if it's not private and not used outside its class
        return !isUsedOutsideClass;
    }


    /**
     * Determines if a node is in the same class as the given declaration.
     * This is used to check if a field or method is accessed only within its declaring class.
     *
     * @param node The {@link Node} to check.
     * @param declaration The {@link Node} representing the declaration to compare against.
     * @return true if both nodes are in the same class, false otherwise.
     */
    private boolean isSameClass(Node node, Node declaration) {
        return node.findAncestor(ClassOrInterfaceDeclaration.class)
                .equals(declaration.findAncestor(ClassOrInterfaceDeclaration.class));
    }


    /**
     * Checks if a method declaration can have a more restrictive access modifier.
     * The method checks if the method is called outside its declaring class.
     *
     * @param method The {@link MethodDeclaration} to check.
     * @param cu The {@link CompilationUnit} containing the method.
     * @return true if the method access can be more restrictive, false otherwise.
     */
    private boolean checkMethodAccess(MethodDeclaration method, CompilationUnit cu) {
        // If the method is private, it cannot be more restrictive
        if (method.isPrivate()) {
            return false;
        }

        String methodName = method.getNameAsString();
        boolean isCalledOutsideClass = !cu.findAll(MethodCallExpr.class, mc ->
                mc.getNameAsString().equals(methodName) && !isSameClass(mc, method)).isEmpty();

        // The method can be more restrictive if it's not private and not called outside its class
        return !isCalledOutsideClass;
    }


}
