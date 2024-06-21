package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.StyleResult;
import com.example.serverside.analysis.util.IndentationCheckerUtil;
import com.github.javaparser.Position;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.stmt.*;
import org.springframework.stereotype.Service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@Service
public class CodeStyleAnalysisService {

    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);

    public StyleResult analyse(String code) {
        CompilationUnit cu = StaticJavaParser.parse(code);
        StyleResult result = new StyleResult();
        List<String> violations = Collections.synchronizedList(new ArrayList<>());

        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        // CompletableFuture.runAsync for non-blocking operations
        tasks.add(CompletableFuture.runAsync(() -> checkIndentationConsistency(code, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkBraceStyle(cu, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkImportOrganization(cu, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkVariableNamingConventions(cu, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkMagicNumbers(cu, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkMethodDeclaration(cu, violations), executor));
        tasks.add(CompletableFuture.runAsync(() -> checkNamingConventionViolations(cu, violations), executor));

        // Wait for all tasks to complete
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
        try {
            allTasks.get(60, TimeUnit.SECONDS); // Wait for all tasks to complete or timeout
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error waiting for analysis tasks to complete", e);
        }

        result.setViolations(violations);
        result.setViolationCount(violations.size());
        return result;
    }

    /**
     * Examines all class or interface declarations within a given {@link CompilationUnit}
     * and identifies naming convention violations. Specifically, it checks if the names
     * start with an uppercase letter. Each identified violation is added to the provided
     * list of violation messages.
     *
     * @param cu The {@link CompilationUnit} to be analyzed for naming convention violations.
     *           If this is {@code null}, the method exits without performing any checks.
     * @param violations A list to which violation messages will be added. Each message includes
     *                   the line number and the name of the class or interface that violates
     *                   the naming convention.
     *
     * @see CompilationUnit#findAll(Class)
     * @see ClassOrInterfaceDeclaration
     */
    public void checkNamingConventionViolations(CompilationUnit cu, List<String> violations) {
        if (cu == null) {
            return;
        }

        for (ClassOrInterfaceDeclaration classOrInterface : cu.findAll(ClassOrInterfaceDeclaration.class)) {
            String name = classOrInterface.getNameAsString();
            if (!name.isEmpty() && !Character.isUpperCase(name.charAt(0))) {
                Optional<Position> beginPos = classOrInterface.getBegin();
                int line = beginPos.map(position -> position.line).orElse(-1);
                String message = String.format("Violation at line %d: Class or interface name '%s' should start with an uppercase letter.", line, name);
                violations.add(message);
            }
        }

    }

    /**
     * Checks all method declarations in the given {@link CompilationUnit} for naming conventions.
     * Specifically, it verifies that each method name starts with a lowercase letter.
     * Any violations found are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analysed. If null, the method will return immediately.
     * @param violations A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkMethodDeclaration(CompilationUnit cu, List<String> violations) {
        if (cu == null || violations == null) {
            return;
        }

        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            String name = method.getNameAsString();
            if (!name.isEmpty() && !Character.isLowerCase(name.charAt(0))) {
                // Retrieving the line number from the method's position
                Optional<Integer> line = method.getBegin().map(pos -> pos.line);
                String message = String.format("Violation at line %d: Method name '%s' should start with a lowercase letter.", line.orElse(-1), name);
                violations.add(message);
            }
        }
    }

    /**
     * Checks for the usage of magic numbers (numeric literals without a named constant declaration)
     * in a given {@link CompilationUnit}. Any found violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param violations A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkMagicNumbers(CompilationUnit cu, List<String> violations) {
        if (cu == null || violations == null) {
            return;
        }

        cu.findAll(IntegerLiteralExpr.class).forEach(literal -> {
            if (literal.findAncestor(VariableDeclarator.class).isEmpty()) {
                int line = literal.getRange().map(r -> r.begin.line).orElse(-1);
                String message = String.format("Violation at line %d: Magic number '%s' found without a named constant declaration.", line, literal.asInt());
                violations.add(message);
            }
        });
    }

    /**
     * Checks the consistency of indentation in the provided code string. This method utilizes
     * the {@link IndentationCheckerUtil} to perform the actual indentation analysis. Any identified
     * indentation violations are added to the provided list of violations.
     *
     * @param code A string representing the code to be analyzed for indentation consistency. Should not be null.
     * @param violations A list where any identified indentation violations will be added. This list should be
     *                   initialized by the caller and should not be null.
     */
    public static void checkIndentationConsistency(String code, List<String> violations) {
        if (code == null || violations == null) {
            return;
        }
            IndentationCheckerUtil indentationChecker = new IndentationCheckerUtil(violations, code);
            indentationChecker.check();
    }

    /**
     * Checks if the opening braces of block statements in the given {@link CompilationUnit} are placed
     * correctly according to the specified style. It verifies whether each opening brace is on the same
     * line as the start of its parent node. Any violations found are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param violations A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkBraceStyle(CompilationUnit cu, List<String> violations) {
        if (cu == null || violations == null) {
            return;
        }

        cu.findAll(BlockStmt.class).forEach(block -> {
            block.getBegin().ifPresent(blockPos -> {
                block.getParentNode().ifPresent(parent -> {
                    boolean isRelevantParentType = parent instanceof MethodDeclaration || parent instanceof ConstructorDeclaration || parent instanceof ClassOrInterfaceDeclaration ||
                            parent instanceof IfStmt;;
                    if (isRelevantParentType) {
                        int blockLine = blockPos.line;
                        // Adjusted to consider annotations: find the start line of the actual signature.
                        int parentLine = getParentStartLineIgnoringAnnotations(parent);
                        if (blockLine != parentLine) {
                            String message = String.format("Violation at line %d: Opening brace should be on the same line as its parent statement.", blockLine);
                            violations.add(message);
                        }
                    }
                });
            });
        });
    }

    private int getParentStartLineIgnoringAnnotations(Node parent) {
        if (parent instanceof NodeWithAnnotations<?>) {

            Optional<Node> firstNonAnnotationChild = parent.getChildNodes().stream()
                    .filter(child -> !(child instanceof AnnotationExpr)) // Correctly filter without casting
                    .findFirst();

            return firstNonAnnotationChild
                    .flatMap(Node::getBegin)
                    .map(position -> position.line)
                    .orElse(parent.getBegin().map(position -> position.line).orElse(-1)); // Default to parent start
        }

        return parent.getBegin().map(position -> position.line).orElse(-1);
    }



    /**
     * Checks if the import statements in the given {@link CompilationUnit} are organized in alphabetical order.
     * Any found violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param violations A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkImportOrganization(CompilationUnit cu, List<String> violations) {
        if (cu == null || violations == null) {
            return;
        }

        List<ImportDeclaration> imports = cu.getImports();
        if (imports.isEmpty()) {
            return;
        }

        for (int i = 1; i < imports.size(); i++) {
            String currentImport = imports.get(i).getNameAsString();
            String previousImport = imports.get(i - 1).getNameAsString();
            if (currentImport.compareTo(previousImport) < 0) {
                int line = imports.get(i).getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Import Organisation : Import '%s' is not in alphabetical order.", line, currentImport);
                violations.add(message);
            }
        }
    }

    /**
     * Checks the naming conventions of variables in the given {@link CompilationUnit}.
     * Constants are expected to be named in all uppercase letters, while other variables should follow camelCase convention.
     * Any found violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analysed. If null, the method will return immediately.
     * @param violations A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkVariableNamingConventions(CompilationUnit cu, List<String> violations) {
        if (cu == null || violations == null) {
            return;
        }

        final Pattern camelCasePattern = Pattern.compile("^[a-z][a-zA-Z0-9]*$");


        cu.findAll(VariableDeclarator.class).forEach(variable -> {
            String name = variable.getNameAsString();

            // Check if it is a field (class-level variable)
            Optional<FieldDeclaration> field = variable.findAncestor(FieldDeclaration.class);
            if (field.isPresent()) {
                if (field.get().isFinal() && !name.matches("[A-Z_]+")) {
                    addViolation(variable, "Constant variable name '%s' should be all uppercase.", name, violations);
                } else if (!field.get().isFinal() && !camelCasePattern.matcher(name).matches()) {
                    addViolation(variable, "Variable name '%s' should follow camelCase naming convention.", name, violations);
                }
            } else {
                // Check for local variables inside methods
                if (!camelCasePattern.matcher(name).matches()) {
                    addViolation(variable, "Local variable name '%s' should follow camelCase naming convention.", name, violations);
                }
            }
        });
    }

    private void addViolation(VariableDeclarator variable, String messageFormat, String name, List<String> violations) {
        int line = variable.getBegin().map(pos -> pos.line).orElse(-1);
        String message = String.format("Violation at line %d: " + messageFormat, line, name);
        violations.add(message);
    }
}
