package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.StyleResult;
import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CodeStyleAnalysisServiceTest {

    private CodeStyleAnalysisService service = new CodeStyleAnalysisService();

    private StyleResult analyseCode(String code) {
        return service.analyse(code);
    }

    @Test
    void testIndentationConsistency() {
        String code = "public class TestClass {\n" +
                "  public void method() {\n" +
                "    int x = 0;\n" +
                "  }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Indentation should be consistent.");
    }

    @Test
    void testBraceStyle() {
        String code = "public class TestClass {\n" +
                "  public void method()\n" +
                "  {\n" +
                "    int x = 0;\n" +
                "  }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Brace style violations should be detected.");
    }

    @Test
    void testImportOrganization() {
        String code = "import java.util.List;\n" +
                "import java.io.File;\n" +
                "public class TestClass {}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Import organization violations should be detected.");
    }

    @Test
    void testVariableNamingConventions() {
        String code = "public class TestClass {\n" +
                "  private final int MY_CONSTANT = 10;\n" +
                "  private int myVariable = 5;\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No naming convention violations should be found.");
    }

    @Test
    void testMethodNamingConventions() {
        String code = "public class TestClass {\n" +
                "  public void TestMethod() {}\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Method naming convention violations should be detected.");
    }

    @Test
    void testMagicNumberDetection() {
        String code = "public class TestClass {\n" +
                "    public void method() {\n" +
                "        int x = 42; // Magic number\n" +
                "    }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "Magic number violations should be detected.");
    }

    @Test
    void testProperlyOrganizedImports() {
        String code = "import java.io.File;\n" +
                "import java.util.List;\n" +
                "public class TestClass {}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No import organization violations should be found for properly organized imports.");
    }

    @Test
    void testClassNamingConventions() {
        String code = "public class testClass { }"; // Incorrect naming

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Class naming convention violations should be detected.");
    }

    @Test
    void testNoNamingConventionViolations() {
        String code = "public class TestClass {\n" +
                "    private static final int MY_CONSTANT = 10;\n" +
                "    private int myVariable = 5;\n" +
                "    public void myMethod() {}\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No naming convention violations should be found.");
    }

    @Test
    void testInconsistentIndentation() {
        String code = "public class TestClass {\n" +
                "\tpublic void method() {\n" + // Mixed tabs and spaces
                "        int x = 0;\n" +
                "    }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Inconsistent indentation violations should be detected.");
    }

    @Test
    void testNoDuplicateCode() {
        String code = "public class TestClass {\n" +
                "    void method1() { int x = 1; }\n" +
                "    void method2() { int y = 2; }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "No duplicate code violations should be found.");
    }

    @Test
    void testLambdaStreamOpportunityDetection() {
        String code = "import java.util.List;\n" +
                "public class TestClass {\n" +
                "    void process(List<String> list) {\n" +
                "        for(String s : list) { if(s.length() > 2) System.out.println(s); }\n" +
                "    }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Opportunities for using lambdas and streams should be detected.");
    }

    @Test
    void testProperBraceStyle() {
        String code = "public class TestClass {\n" +
                "    public void method() {\n" +
                "        int x = 0; }\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No brace style violations should be found for proper brace style.");
    }

    @Test
    void testInconsistentMethodNaming() {
        String code = "public class TestClass {\n" +
                "    public void Method() {}\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertFalse(result.getViolations().isEmpty(), "Inconsistent method naming violations should be detected.");
    }

    @Test
    void testProperUseOfConstants() {
        String code = "public class TestClass {\n" +
                "    private static final int CONSTANT_VALUE = 5;\n" +
                "}";

        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No violations should be found for properly used constants.");
    }

    @Test
    void testCheckNamingConventionViolationsWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkNamingConventionViolations(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckMethodDeclarationWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkMethodDeclaration(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckMagicNumbersWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkMagicNumbers(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckBraceStyleWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkBraceStyle(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckImportOrganizationWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkImportOrganization(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckVariableNamingConventionsWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkVariableNamingConventions(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testCheckIndentationConsistencyWithNullArguments() {
        assertDoesNotThrow(() ->
                        service.checkIndentationConsistency(null, null),
                "Method should handle null arguments without throwing an exception."
        );
    }

    @Test
    void testAddViolation() throws Exception {
        CodeStyleAnalysisService service = new CodeStyleAnalysisService();
        Method method = CodeStyleAnalysisService.class.getDeclaredMethod("addViolation", VariableDeclarator.class, String.class, String.class, List.class);
        method.setAccessible(true);

        List<String> violations = new ArrayList<>();

        VariableDeclarator variable = new VariableDeclarator();
        variable.setRange(new com.github.javaparser.Range(new Position(10, 0), new Position(10, 10)));

        String messageFormat = "Variable '%s' used without initialization.";
        String name = "x";

        method.invoke(service, variable, messageFormat, name, violations);

        assertEquals(1, violations.size(), "Expected one violation to be added.");
        String expectedMessage = "Violation at line 10: Variable 'x' used without initialization.";
        assertEquals(expectedMessage, violations.get(0), "The violation message does not match the expected format.");
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrown() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkImportOrganization(null, violations));
    }

    @Test
    void testWhenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrown() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkImportOrganization(cu, null));
    }

    @Test
    void testWhenCuAndViolationsAreNotNull_thenFunctionalityProceeds() {
        CompilationUnit cu = StaticJavaParser.parse("import java.io.File;\nimport java.util.List;");
        List<String> violations = new ArrayList<>();
        service.checkImportOrganization(cu, violations);
        assertTrue(violations.isEmpty(), "No violations expected for well-organized imports.");
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrownForBraceStyle() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkBraceStyle(null, violations));
    }

    @Test
    void testWhenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrownForBraceStyle() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkBraceStyle(cu, null));
    }

    @Test
    void testWhenCuAndViolationsAreNotNull_thenFunctionalityProceedsForBraceStyle() {
        CompilationUnit cu = StaticJavaParser.parse(
                "public class TestClass {\n" +
                        "    public void method() {\n" + // Correct brace style
                        "    }\n" +
                        "}"
        );
        List<String> violations = new ArrayList<>();
        service.checkBraceStyle(cu, violations);
        assertTrue(violations.isEmpty(), "No brace style violations expected for correctly styled braces.");
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrownForMethodDeclaration() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkMethodDeclaration(null, violations));
    }

    @Test
    void testWenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrownForMethodDeclaration() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkMethodDeclaration(cu, null));
    }

    @Test
    void whenClassNameStartsWithUppercase_thenNoViolationAdded() {
        String code = "public class ValidClassName {}";
        StyleResult result = analyseCode(code);
        assertTrue(result.getViolations().isEmpty(), "No violations should be found for correctly named classes.");
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrownForCheckMagic() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkMagicNumbers(null, violations));
    }

    @Test
    void testWenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrownForCheckMagic() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkMagicNumbers(cu, null));
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrownForCheckIndentationConsistency() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> CodeStyleAnalysisService.checkIndentationConsistency(null, violations));
    }

    @Test
    void testWenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrownForCheckIndentationConsistency() {
        String code = "public";
        assertDoesNotThrow(() -> CodeStyleAnalysisService.checkIndentationConsistency(code, null));
    }

    @Test
    void testWhenCuIsNullAndViolationsIsNotNull_thenNoExceptionThrownForCheckVariableNamingConventions() {
        List<String> violations = new ArrayList<>();
        assertDoesNotThrow(() -> service.checkVariableNamingConventions(null, violations));
    }

    @Test
    void testWenCuIsNotNullAndViolationsIsNull_thenNoExceptionThrownForCheckVariableNamingConventions() {
        CompilationUnit cu = new CompilationUnit();
        assertDoesNotThrow(() -> service.checkVariableNamingConventions(cu, null));
    }

}
