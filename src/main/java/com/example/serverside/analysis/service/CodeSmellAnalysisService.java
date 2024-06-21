package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.CodeSmellResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CodeSmellAnalysisService {

    private static final int MAX_METHOD_PARAMS = 3;
    private static final int MAX_METHOD_LENGTH = 30;
    private static final int MAX_CLASS_METHODS = 10;
    private static final int MAX_CLASS_LENGTH = 200;
    private static final int MAX_CONSTRUCTOR_PARAMS = 5;
    private static final int MAX_PRIMITIVE_PARAMS = 3;

    public CodeSmellResult analyse(String code) {
        CompilationUnit cu = StaticJavaParser.parse(code);
        CodeSmellResult result = new CodeSmellResult();
        List<String> smells = new ArrayList<>();


        checkParameters(cu, smells);
        checkLongMethod(cu, smells);
        checkGodClass(cu, smells);
        checkLargeClass(cu, smells);
        checkTryBlocks(cu, smells);
        checkDataClumps(cu, smells);
        checkPrimitives(cu, smells);
        checkComments(cu, smells);
        checkExceptionHandling(cu,smells);
        checkMethodChaining(cu,smells, 2);
        checkDeadMethods(cu,smells);

        result.setSmellsCount(smells.size());
        result.setSmells(smells);
        return result;
    }

    /**
     * Checks each method in the given {@link CompilationUnit} for an excessive number of parameters.
     * Methods with parameters equal to or greater than MAX_METHOD_PARAMS are considered to have too many.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkParameters(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(MethodDeclaration.class).forEach(method -> {
            int paramCount = method.getParameters().size();
            if (paramCount >= MAX_METHOD_PARAMS) {
                int line = method.getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Method Parameters : The Method '%s' has too many parameters (%d).",
                        line, method.getNameAsString(), paramCount);
                smells.add(message);
            }
        });
    }

    /**
     * Checks each method in the given {@link CompilationUnit} for excessive length.
     * Methods longer than MAX_METHOD_LENGTH lines are identified as violations.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkLongMethod(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(MethodDeclaration.class).forEach(method -> {
            method.getBegin().ifPresent(begin -> method.getEnd().ifPresent(end -> {
                int lineCount = end.line - begin.line + 1;
                if (lineCount > MAX_METHOD_LENGTH) {
                    String message = String.format("Violation at line %d: Method Length : The Method '%s' is too long (%d lines).",
                            begin.line, method.getNameAsString(), lineCount);
                    smells.add(message);
                }
            }));
        });
    }

    /**
     * Checks each class in the given {@link CompilationUnit} for having too many methods, which might indicate a "God Class".
     * A "God Class" typically has too many responsibilities and can be difficult to maintain.
     * The threshold for the number of methods is defined by MAX_CLASS_METHODS.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkGodClass(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> {
            if (!classOrInterface.isInterface()) {
                int methodCount = classOrInterface.getMethods().size();
                if (methodCount > MAX_CLASS_METHODS) {
                    int line = classOrInterface.getBegin().map(pos -> pos.line).orElse(-1);
                    String message = String.format("Violation at line %d: God Class : Class '%s' has too many methods (%d methods).",
                            line, classOrInterface.getNameAsString(), methodCount);
                    smells.add(message);
                }
            }
        });
    }

    /**
     * Checks each class in the given {@link CompilationUnit} for excessive length, which might indicate a "Large Class".
     * A "Large Class" can be difficult to maintain due to its size. The threshold for class length is defined by  MAX_CLASS_LENGTH.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkLargeClass(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> {
            if (!classOrInterface.isInterface()) {
                classOrInterface.getBegin().ifPresent(begin -> classOrInterface.getEnd().ifPresent(end -> {
                    int lineCount = end.line - begin.line + 1;
                    if (lineCount > MAX_CLASS_LENGTH) {
                        String message = String.format("Violation at line %d: Large Class : Class '%s' is too large (%d lines).",
                                begin.line, classOrInterface.getNameAsString(), lineCount);
                        smells.add(message);
                    }
                }));
            }
        });
    }

    /**
     * Checks for empty try blocks or try blocks with only comment statements in the given {@link CompilationUnit}.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkTryBlocks(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(TryStmt.class).forEach(tryStmt -> {
            BlockStmt tryBlock = tryStmt.getTryBlock();
            int startLine = tryBlock.getBegin().map(pos -> pos.line).orElse(-1);
            int endLine = tryBlock.getEnd().map(pos -> pos.line).orElse(-1);

            // Check if the try block is empty or contains only comments
            boolean isBlockEmptyOrCommentsOnly = tryBlock.getStatements().stream()
                    .allMatch(this::isStatementComment);

            if (isBlockEmptyOrCommentsOnly) {
                String message = String.format("Violation at line %d: Try Block : Empty or comment-only try block (ending at line %d).", startLine, endLine);
                smells.add(message);
            }
        });
    }

    /**
     * Checks if a given statement is a comment.
     *
     * @param stmt The statement to check.
     * @return true if the statement is a comment, false otherwise.
     */
    private boolean isStatementComment(Statement stmt) {
        String stmtString = stmt.toString().trim();
        return stmtString.startsWith("//") || stmtString.startsWith("/*");
    }

    /**
     * Checks each constructor in the given {@link CompilationUnit} for having an excessive number of parameters,
     * potentially indicating a "Data Clump" code smell. Constructors with more than MAX_CONSTRUCTOR_PARAMS parameters are identified.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkDataClumps(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(ConstructorDeclaration.class).forEach(constructor -> {
            int paramCount = constructor.getParameters().size();
            if (paramCount > MAX_CONSTRUCTOR_PARAMS) {
                int line = constructor.getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Data Clumps : Constructor '%s' has too many parameters (%d).",
                        line, constructor.getDeclarationAsString(), paramCount);
                smells.add(message);
            }
        });
    }

    /**
     * Checks each method in the given {@link CompilationUnit} for an excessive number of primitive type parameters,
     * potentially indicating a "Primitive Obsession" code smell. Methods with more than MAX_PRIMITIVE_PARAMS primitive parameters are identified.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkPrimitives(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(MethodDeclaration.class).forEach(method -> {
            long primitiveCount = method.getParameters().stream()
                    .filter(param -> param.getType().isPrimitiveType())
                    .count();
            if (primitiveCount > MAX_PRIMITIVE_PARAMS) {
                int line = method.getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Primitive Obsession : Method '%s' has primitive obsession with %d primitive parameters.",
                        line, method.getNameAsString(), primitiveCount);
                smells.add(message);
            }
        });
    }

    /**
     * Checks for classes and public methods in the given {@link CompilationUnit} that lack Javadoc comments.
     * Any identified violations are added to the provided list.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkComments(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> {
            if (!classOrInterface.getJavadocComment().isPresent()) {
                int line = classOrInterface.getBegin().map(pos -> pos.line).orElse(-1);
                String message = String.format("Violation at line %d: Javadoc Class Comments : Class '%s' lacks a Javadoc comment.",
                        line, classOrInterface.getNameAsString());
                smells.add(message);
            }

            classOrInterface.getMethods().stream()
                    .filter(MethodDeclaration::isPublic)
                    .filter(method -> !method.getJavadocComment().isPresent())
                    .forEach(method -> {
                        int line = method.getBegin().map(pos -> pos.line).orElse(-1);
                        String message = String.format("Violation at line %d: Javadoc Method Comments : Public method '%s' in class '%s' lacks a Javadoc comment.",
                                line, method.getNameAsString(), classOrInterface.getNameAsString());
                        smells.add(message);
                    });
        });
    }


    /**
     * Identifies and adds to the provided list any private methods in the given {@link CompilationUnit} that are never called,
     * indicating potential dead code.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     */
    public void checkDeadMethods(CompilationUnit cu, List<String> smells) {
        if (cu == null || smells == null) {
            return;
        }

        Set<String> calledMethodNames = new HashSet<>();
        cu.findAll(MethodCallExpr.class).forEach(mce -> calledMethodNames.add(mce.getNameAsString()));

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
            cls.getMethods().stream()
                    .filter(MethodDeclaration::isPrivate)
                    .filter(method -> !calledMethodNames.contains(method.getNameAsString()))
                    .forEach(unusedMethod -> {
                        int line = unusedMethod.getBegin().map(pos -> pos.line).orElse(-1);
                        String message = String.format("Violation at line %d: Dead Method : Unused private method '%s' in class '%s'.",
                                line, unusedMethod.getNameAsString(), cls.getNameAsString());
                        smells.add(message);
                    });
        });
    }





    /**
     * Checks for excessive method chaining in the given {@link CompilationUnit}.
     * A method chain is considered excessive if its length exceeds {@code maxChainLength}.
     *
     * @param cu The {@link CompilationUnit} to be analyzed. If null, the method will return immediately.
     * @param smells A list to which violation messages will be added. If null, the method will return immediately.
     * @param maxChainLength The maximum allowed method chain length.
     */
    public void checkMethodChaining(CompilationUnit cu, List<String> smells, int maxChainLength) {
        if (cu == null || smells == null) {
            return;
        }

        Set<MethodCallExpr> processedChains = new HashSet<>();

        cu.findAll(MethodCallExpr.class).forEach(methodCall -> {
            MethodCallExpr topMethodCall = getTopMethodCall(methodCall);

            if (!processedChains.contains(topMethodCall)) {
                processedChains.add(topMethodCall);

                int chainLength = calculateChainLength(topMethodCall);

                if (chainLength > maxChainLength) {
                    int line = topMethodCall.getBegin().map(pos -> pos.line).orElse(-1);
                    String message = String.format("Violation at line %d: Excessive method chaining (length %d).",
                            line, chainLength);
                    smells.add(message);
                }
            }
        });
    }


    /**
     * Retrieves the topmost method call in a chain of method calls.
     * This method traverses up the AST (Abstract Syntax Tree) from a given method call expression
     * to find the outermost method call in the chain.
     *
     * A method call chain can be something like "a.b().c().d()", where "a.b()" would be
     * the topmost call in the chain starting from "d()".
     *
     * @param methodCall The {@link MethodCallExpr} from which to start the search for the topmost method call.
     * @return The outermost {@link MethodCallExpr} in the chain of method calls.
     */
    private MethodCallExpr getTopMethodCall(MethodCallExpr methodCall) {
        // Traverse up the AST to find the topmost method call in the chain
        Optional<Expression> current = Optional.of(methodCall);
        while (current.isPresent() && current.get() instanceof MethodCallExpr) {
            methodCall = (MethodCallExpr) current.get();
            current = methodCall.getParentNode()
                    .flatMap(node -> node instanceof Expression ? Optional.of((Expression) node) : Optional.empty());
        }
        return methodCall;
    }

    /**
     * Calculates the length of a method call chain.
     * This method counts the number of consecutive method call expressions, starting from the provided method call.
     * For example, in the chain "a.b().c().d()", the length would be 4 starting from "a.b()".
     *
     * @param methodCall The starting {@link MethodCallExpr} of the chain.
     * @return The length of the method call chain as an integer.
     */
    private int calculateChainLength(MethodCallExpr methodCall) {
        int chainLength = 1;
        Optional<Expression> current = Optional.of(methodCall);

        while (current.isPresent() && current.get() instanceof MethodCallExpr) {
            current = ((MethodCallExpr) current.get()).getScope();
            if (current.isPresent() && current.get() instanceof MethodCallExpr) {
                chainLength++;
            }
        }

        return chainLength;
    }




    // Set of common logging method names
    private static final Set<String> LOG_METHOD_NAMES = Set.of("log", "error", "warn", "info", "debug", "trace");

    /**
     * Checks for common anti patterns in exception handling within the provided {@link CompilationUnit}.
     * It identifies generic catch blocks, empty catch blocks, and swallowed exceptions.
     *
     * @param cu The {@link CompilationUnit} to be analyzed.
     * @param smells A list to which violation messages will be added.
     */
    public void checkExceptionHandling(CompilationUnit cu, List<String> smells) {
        cu.findAll(CatchClause.class).forEach(catchClause -> {
            String catchType = catchClause.getParameter().getType().asString();
            int catchLine = catchClause.getBegin().map(pos -> pos.line).orElse(-1);

            if (isGenericType(catchType)) {
                String message = String.format("Violation at line %d: Generic catch block: Catching '%s' can hide the true nature of an exception. Consider catching more specific exception types.", catchLine, catchType);
                smells.add(message);
            }

            if (catchClause.getBody().getStatements().isEmpty()) {
                String message = String.format("Violation at line %d: Empty catch block: An empty catch block may swallow an exception and hinder debugging. Consider adding either handling logic or a comment explaining why it's empty.", catchLine);
                smells.add(message);
            } else if (isExceptionSwallowed(catchClause)) {
                String message = String.format("Violation at line %d: Swallowed exception: The catch block may silently ignore the exception. Consider logging or rethrowing the exception.", catchLine);
                smells.add(message);
            }
        });
    }


    /**
     * Checks if the provided type name is a generic exception type.
     * This method is used to identify catch blocks that are catching overly broad exception types like Exception, Throwable, or RuntimeException.
     *
     * @param typeName The name of the type to check.
     * @return true if the type name represents a generic exception type, false otherwise.
     */
    private boolean isGenericType(String typeName) {
        // Include RuntimeException for broader checking
        return Set.of("Exception", "Throwable", "RuntimeException").contains(typeName);
    }


    /**
     * Determines whether an exception is swallowed in the given catch clause.
     * An exception is considered swallowed if the catch block does not contain any statements that log the exception or rethrow it.
     *
     * @param catchClause The catch clause to be analyzed.
     * @return true if the exception is swallowed in the catch block, false otherwise.
     */
    private boolean isExceptionSwallowed(CatchClause catchClause) {
        return catchClause.getBody().getStatements().stream()
                .noneMatch(stmt -> isLogStatement(stmt) || isRethrowingException(stmt));
    }


    /**
     * Checks if the given statement is a log statement.
     * This method is used to determine if a catch block is handling an exception by logging it, based on common logging method names.
     *
     * @param stmt The statement to check.
     * @return true if the statement is a log statement, false otherwise.
     */
    private boolean isLogStatement(Statement stmt) {
        if (stmt.isExpressionStmt()) {
            Expression expression = stmt.asExpressionStmt().getExpression();
            if (expression instanceof MethodCallExpr) {
                MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
                if (methodCallExpr.getScope().isPresent()) {
                    String methodName = methodCallExpr.getNameAsString();
                    return LOG_METHOD_NAMES.contains(methodName);
                }
            }
        }
        return false;
    }


    /**
     * Checks if the given statement is rethrowing an exception.
     * This method identifies if a statement in a catch block is throwing an exception, which can be either the caught exception itself or a new exception.
     *
     * @param stmt The statement to check.
     * @return true if the statement is rethrowing an exception, false otherwise.
     */
    private boolean isRethrowingException(Statement stmt) {
        if (stmt instanceof ThrowStmt) {
            ThrowStmt throwStmt = (ThrowStmt) stmt;
            Expression thrownExpr = throwStmt.getExpression();

            if (thrownExpr.isNameExpr()) {
                return true;
            }

            if (thrownExpr instanceof ObjectCreationExpr) {
                ObjectCreationExpr thrownObject = (ObjectCreationExpr) thrownExpr;
                return thrownObject.getArguments().stream()
                        .anyMatch(arg -> arg.isNameExpr() || arg.isMethodCallExpr());
            }
        }
        return false;
    }


}
