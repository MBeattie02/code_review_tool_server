package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.CodeSmellResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.Statement;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CodeSmellAnalysisServiceTest {

    private CodeSmellAnalysisService service = new CodeSmellAnalysisService();

    private CodeSmellResult analyzeCode(String code) {
        return service.analyse(code);
    }

    @Test
    void testExcessiveMethodParameters() {
        String code = "class TestClass { void testMethod(int a, int b, int c, int d) {} }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect methods with too many parameters.");
    }

    @Test
    void testLongMethod() {
        StringBuilder codeBuilder = new StringBuilder("class TestClass { void testMethod() {");
        for (int i = 0; i < 35; i++) {
            codeBuilder.append(" int x").append(i).append(" = ").append(i).append(";");
        }
        codeBuilder.append("}}");

        CodeSmellResult result = analyzeCode(codeBuilder.toString());
        assertFalse(result.getSmells().isEmpty(), "Should detect long methods.");
    }

    @Test
    void testGodClass() {
        StringBuilder codeBuilder = new StringBuilder("class TestClass {");
        for (int i = 0; i < 15; i++) {
            codeBuilder.append(" void testMethod").append(i).append("() {}");
        }
        codeBuilder.append("}");

        CodeSmellResult result = analyzeCode(codeBuilder.toString());
        assertFalse(result.getSmells().isEmpty(), "Should detect God Classes.");
    }

    @Test
    void testEmptyTryBlocks() {
        String code = "class TestClass { void testMethod() { try {} catch(Exception e) {} } }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect empty try blocks.");
    }

    @Test
    void testLargeClass() {
        StringBuilder codeBuilder = new StringBuilder("class TestClass {");
        for (int i = 0; i < 210; i++) {
            codeBuilder.append(" void testMethod").append(i).append("() {}");
        }
        codeBuilder.append("}");

        CodeSmellResult result = analyzeCode(codeBuilder.toString());
        assertFalse(result.getSmells().isEmpty(), "Should detect Large Classes.");
    }

    @Test
    void testConstructorTooManyParameters() {
        String code = "class TestClass { TestClass(int a, int b, int c, int d, int e, int f) {} }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect constructors with too many parameters.");
    }

    @Test
    void testPrimitiveObsession() {
        String code = "class TestClass { void testMethod(int a, int b, int c, int d) {} }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect methods with primitive obsession.");
    }

    @Test
    void testLackOfComments() {
        String code = "public class TestClass { public void testMethod() {} }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect lack of comments in classes and methods.");
    }

    @Test
    void testSwallowedExceptions() {
        String code = "class TestClass { void testMethod() { try { int x = 0; } catch (Exception e) {} } }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect swallowed exceptions.");
    }

    @Test
    void testExcessiveMethodChaining() {
        String code = "class TestClass { void testMethod() { getA().getB().getC().getD().getE(); } " +
                "A getA() { return new A(); } } class A { B getB() { return new B(); } } " +
                "class B { C getC() { return new C(); } } class C { D getD() { return new D(); } } " +
                "class D { E getE() { return new E(); } } class E {}";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect excessive method chaining.");
    }

    @Test
    void testUnusedPrivateMethods() {
        String code = "class TestClass { private void unusedMethod() {} }";
        CodeSmellResult result = analyzeCode(code);
        assertFalse(result.getSmells().isEmpty(), "Should detect unused private methods.");
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkParameters(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkParameters(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkParameters() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkParameters(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkParameters() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkParameters(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkLongMethod() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkLongMethod(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkLongMethod() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkLongMethod(cu, null));
    }

    @Test
    void testMethodExceedsMaxLengthAddsSmell() {

        CodeSmellAnalysisService checker = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();
        final int MAX_METHOD_LENGTH = 30;

        StringBuilder methodBody = new StringBuilder("int a = 0;");
        for (int i = 0; i < MAX_METHOD_LENGTH; i++) {
            methodBody.append("\na++;");
        }
        String classWithLongMethod = "class TestClass {\n void longMethod() {\n" + methodBody.toString() + "\n}\n}";

        CompilationUnit cu = StaticJavaParser.parse(classWithLongMethod);

        checker.checkLongMethod(cu, smells);

        assertFalse(smells.isEmpty(), "Smells list should contain violations for long methods.");
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkGodClass() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkGodClass(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkGodClass() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkGodClass(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkLargeClass() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkLargeClass(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkLargeClass() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkLargeClass(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkTryBlocks() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkTryBlocks(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkTryBlocks() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkTryBlocks(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkDataClumps() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkDataClumps(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkDataClumps() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkDataClumps(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkPrimitives() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkPrimitives(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkPrimitives() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkPrimitives(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkComments() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkComments(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkComments() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkComments(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkDeadMethods() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkDeadMethods(null, smells));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkDeadMethods() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkDeadMethods(cu, null));
    }

    @Test
    void testWhenCuIsNullAndSmellsIsNotNull_checkMethodChaining() {
        List<String> smells = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkMethodChaining(null, smells,1));
    }

    @Test
    void testWenCuIsNotNullAndSmellsIsNull_checkMethodChaining() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkMethodChaining(cu, null,1));
    }

    @Test
    void testClassExceedsMaxLength() {
        CodeSmellAnalysisService checker = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();
        final int MAX_CLASS_LENGTH = 200;

        StringBuilder classBody = new StringBuilder();
        for (int i = 0; i < MAX_CLASS_LENGTH; i++) {
            classBody.append("\n    void dummyMethod").append(i).append("() {}");
        }
        String largeClass = "class TestClass {" + classBody.toString() + "\n}";

        CompilationUnit cu = StaticJavaParser.parse(largeClass);


        checker.checkLargeClass(cu, smells);

        assertFalse(smells.isEmpty(), "Smells list should contain violations for large classes.");
    }

    @Test
    void testConstructorNotExceedingMaxParams() {
        // Assuming MAX_CONSTRUCTOR_PARAMS is 5
        final int MAX_CONSTRUCTOR_PARAMS = 5; // Use the actual value from your service class
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        // Construct a class with a constructor that has exactly MAX_CONSTRUCTOR_PARAMS parameters
        String code = "class TestClass {" +
                "    TestClass(int a, int b, int c, int d, int e) {}" + // Adjust the number of parameters to match your MAX_CONSTRUCTOR_PARAMS
                "}";
        CompilationUnit cu = StaticJavaParser.parse(code);

        service.checkDataClumps(cu, smells);

        assertTrue(smells.isEmpty(), "Expected no data clump smells for constructor with parameters not exceeding the maximum.");
    }


    @Test
    void testIsRethrowingExceptionWithNameExpr() {
        try {
            String code = "throw e;";
            Statement stmt = StaticJavaParser.parseStatement(code);

            assertNotNull(stmt, "Parsed statement should not be null.");

            CodeSmellAnalysisService serviceInstance = new CodeSmellAnalysisService();
            Method method = CodeSmellAnalysisService.class.getDeclaredMethod("isRethrowingException", Statement.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(serviceInstance, stmt);

            assertTrue(result, "Should be true for rethrowing with NameExpr.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke method: " + e.toString());
        }
    }

    @Test
    void testGodClassWithInterface() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String interfaceWithMethods =
                "interface TestInterface {" +
                        "    void method1();" +
                        "    void method2();" +
                        "}";

        CompilationUnit cu = StaticJavaParser.parse(interfaceWithMethods);
        service.checkGodClass(cu, smells);

        assertTrue(smells.isEmpty(), "Expected no god class smell for an interface.");
    }

    @Test
    void testCheckLargeClassWithInterface() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String interfaceWithMethods =
                "interface TestInterface {" +
                        "    void method1();" +
                        "    void method2();" +
                        "}";

        CompilationUnit cu = StaticJavaParser.parse(interfaceWithMethods);
        service.checkLargeClass(cu, smells);

        assertTrue(smells.isEmpty(), "Expected no god class smell for an interface.");
    }

    @Test
    void testClassWithJavadocCommentDoesNotAddSmell() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String codeWithJavadoc =
                "/**\n" +
                        " * A class with a Javadoc comment.\n" +
                        " */\n" +
                        "class TestClass {\n" +
                        "    /**\n" +
                        "     * A method with a Javadoc comment.\n" +
                        "     */\n" +
                        "    public void testMethod() {}\n" +
                        "}";

        CompilationUnit cu = StaticJavaParser.parse(codeWithJavadoc);
        service.checkComments(cu, smells);

        assertTrue(smells.isEmpty(), "Classes and methods with Javadoc comments should not add smells.");
    }

    @Test
    void testPublicMethodWithJavadocCommentDoesNotAddSmell() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String codeWithMethodJavadoc =
                "/**\n" +
                        " * Class Javadoc here.\n" +
                        " */\n" +
                        "class TestClass {\n" +
                        "    /**\n" +
                        "     * Method Javadoc here.\n" +
                        "     */\n" +
                        "    public void testMethod() {}\n" +
                        "}";


        CompilationUnit cu = StaticJavaParser.parse(codeWithMethodJavadoc);
        service.checkComments(cu, smells);

        assertTrue(smells.isEmpty(), "A public method with a Javadoc comment should not add smells.");
    }

    @Test
    void testPrivateMethodUsedDoesNotAddSmell() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String code =
                "class TestClass {" +
                        "    private void usedPrivateMethod() {}" +
                        "    public void publicMethod() {" +
                        "        usedPrivateMethod();" + // Call to ensure the private method is used
                        "    }" +
                        "}";

        CompilationUnit cu = StaticJavaParser.parse(code);
        service.checkDeadMethods(cu, smells);

        assertTrue(smells.isEmpty(), "Expected no smells for used private methods.");
    }

    @Test
    void testCatchingSpecificExceptionDoesNotAddGenericExceptionSmell() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String code = """
        class TestClass {
            public void testMethod() {
                try {
                    // Some code that might throw a specific exception
                } catch (FileNotFoundException e) {
                    // Handling logic for FileNotFoundException
                }
            }
        }
        """;

        CompilationUnit cu = StaticJavaParser.parse(code);
        service.checkExceptionHandling(cu, smells);

        boolean hasGenericExceptionSmell = smells.stream()
                .anyMatch(smell -> smell.contains("Generic catch block") && smell.contains("FileNotFoundException"));
        assertFalse(hasGenericExceptionSmell, "Expected no smells for catching a specific (non-generic) exception type.");
    }

    @Test
    void testCatchBlockThatDoesNotSwallowExceptionDoesNotAddSmell() {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();
        List<String> smells = new ArrayList<>();

        String code = """
        import java.io.FileNotFoundException;
        import java.util.logging.Logger;

        class TestClass {
            private static final Logger LOGGER = Logger.getLogger(TestClass.class.getName());

            public void testMethod() {
                try {
                    // Some code that might throw an exception
                } catch (FileNotFoundException e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred", e);
                }
            }
        }
        """;

        CompilationUnit cu = StaticJavaParser.parse(code);
        service.checkExceptionHandling(cu, smells);

        boolean hasSwallowedExceptionSmell = smells.stream()
                .anyMatch(smell -> smell.contains("Swallowed exception"));
        assertFalse(hasSwallowedExceptionSmell, "Expected no smells for catch blocks that do not swallow exceptions.");
    }

    @Test
    void testIsLogStatementWithNonExpressionStmt() throws Exception {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();

        String code = """
        {
            int a = 0; // Not an expression statement in the context of this check
        }
        """;
        Statement stmt = StaticJavaParser.parseBlock(code);


        assertFalse(invokeIsLogStatement(service, stmt), "Expected false for a non-expression statement.");
    }

    @Test
    void testIsLogStatementWithNonMethodCallExpression() throws Exception {
        CodeSmellAnalysisService service = new CodeSmellAnalysisService();

        String code = "int a = 0;"; // Variable assignment, not a method call
        Statement stmt = StaticJavaParser.parseStatement(code);

        boolean result = invokeIsLogStatement(service, stmt);

        assertFalse(result, "Expected false for an expression that is not a MethodCallExpr.");
    }

    private boolean invokeIsLogStatement(CodeSmellAnalysisService service, Statement stmt) throws Exception {
        Method method = CodeSmellAnalysisService.class.getDeclaredMethod("isLogStatement", Statement.class);
        method.setAccessible(true);
        return (boolean) method.invoke(service, stmt);
    }

    @Test
    void testIsRethrowingExceptionWithNonNameExpr() {
        try {
            String code = "throw new Exception();";
            Statement stmt = StaticJavaParser.parseStatement(code);

            assertNotNull(stmt, "Parsed statement should not be null.");

            CodeSmellAnalysisService serviceInstance = new CodeSmellAnalysisService();
            Method method = CodeSmellAnalysisService.class.getDeclaredMethod("isRethrowingException", Statement.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(serviceInstance, stmt);

            assertFalse(result, "Should be false for rethrowing with non-NameExpr.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke method: " + e.toString());
        }
    }

}
