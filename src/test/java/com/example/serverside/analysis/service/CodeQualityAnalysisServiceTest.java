package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.QualityResult;

import java.lang.reflect.Method;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodeQualityAnalysisServiceTest {

    private CodeQualityAnalysisService service = new CodeQualityAnalysisService();

    private QualityResult analyseCode(String code) {
        return service.analyse(code);
    }

    @Test
    void detectsDuplicateCodeBlocks() {
        String code = "public class TestClass {\n" +
                "    public void method1() {\n" +
                "        int a = 10; // Duplicate block\n" +
                "    }\n" +
                "    public void method2() {\n" +
                "        int a = 10; // Duplicate block\n" +
                "    }\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Duplicate code blocks should be detected.");
    }

    @Test
    void detectsImproperAccessModifiers() {
        String code = "public class TestClass {\n" +
                "    public int data; // Should be more restrictive\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Improper access modifiers should be detected.");
    }

    @Test
    void detectsOpportunitiesForLambdasAndStreams() {
        String code = "import java.util.List;\n" +
                "public class TestClass {\n" +
                "    public void processList(List<String> list) {\n" +
                "        for (String item : list) { System.out.println(item); }\n" +  // Can use streams
                "    }\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Opportunities for using lambdas and streams should be detected.");
    }

    @Test
    void testDuplicateCodeDetection() {
        String code = "public class TestClass {\n" +
                "    void method1() { int x = 1; }\n" +
                "    void method2() { int x = 1; }\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Duplicate code should be detected.");
    }

    @Test
    void testAccessModifierCheck() {
        String code = "public class TestClass {\n" +
                "    public int x;\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Improper access modifier should be detected.");
    }


    @Test
    void testUseOfLambdasAndStreams() {
        String code = "import java.util.List;\n" +
                "public class TestClass {\n" +
                "    void process(List<String> list) {\n" +
                "        for(String s : list) { System.out.println(s); }\n" +
                "    }\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "Opportunity to use lambdas and streams should be detected.");
    }

    @Test
    void testNoViolations() {
        String code = "public class TestClass {\n" +
                "    private int x;\n" +
                "    void method() { System.out.println(\"Hello\"); }\n" +
                "}";

        QualityResult result = analyseCode(code);
        assertFalse(result.getDuplications().isEmpty(), "No code quality issues should be found.");
    }


    @Test
    void testIsSimpleBinaryOperation() throws Exception {

        CodeQualityAnalysisService service = new CodeQualityAnalysisService();

        Expression expr = new IntegerLiteralExpr(1);

        Method method = CodeQualityAnalysisService.class.getDeclaredMethod("isSimpleBinaryOperation", Expression.class);
        method.setAccessible(true);

        Boolean result = (Boolean) method.invoke(service, expr);

        assertTrue(result);
    }

    @Test
    void testSimpleNestedBinaryOperation() throws Exception {
        CodeQualityAnalysisService service = new CodeQualityAnalysisService();

        Expression leftInner = new BinaryExpr(new IntegerLiteralExpr(1), new IntegerLiteralExpr(2), BinaryExpr.Operator.PLUS);
        Expression right = new IntegerLiteralExpr(3);
        Expression expr = new BinaryExpr(leftInner, right, BinaryExpr.Operator.PLUS);

        Method method = CodeQualityAnalysisService.class.getDeclaredMethod("isSimpleBinaryOperation", Expression.class);
        method.setAccessible(true);

        Boolean result = (Boolean) method.invoke(service, expr);
        assertTrue(result, "Expected simple nested binary operation to be considered simple.");
    }


}
