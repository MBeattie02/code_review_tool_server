package com.example.serverside.analysis.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NamingConventionCheckerTest {

    private CodeStyleAnalysisService codeStyleAnalysisService;
    private List<String> violations;

    @BeforeEach
    void setUp() {
        codeStyleAnalysisService = new CodeStyleAnalysisService();
        violations = new ArrayList<>();
    }

    @Test
    void testConstantNamingConvention() {
        String code = "class Test { private static final int MY_CONSTANT = 1; }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertTrue(violations.isEmpty(), "Constants correctly named should not add violations.");
    }

    @Test
    void testConstantNamingViolation() {
        String code = "class Test { private static final int myConstant = 1; }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertFalse(violations.isEmpty(), "Constants not following naming conventions should add a violation.");
    }

    @Test
    void testNonFinalFieldCamelCase() {
        String code = "class Test { private int myVariable = 1; }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertTrue(violations.isEmpty(), "Non-final fields correctly following camelCase should not add violations.");
    }

    @Test
    void testNonFinalFieldCamelCaseViolation() {
        String code = "class Test { private int MyVariable = 1; }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertFalse(violations.isEmpty(), "Non-final fields not following camelCase should add a violation.");
    }

    @Test
    void testLocalVariableCamelCase() {
        String code = "class Test { void method() { int myVariable = 1; } }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertTrue(violations.isEmpty(), "Local variables correctly following camelCase should not add violations.");
    }

    @Test
    void testLocalVariableCamelCaseViolation() {
        String code = "class Test { void method() { int MyVariable = 1; } }";
        CompilationUnit cu = StaticJavaParser.parse(code);

        codeStyleAnalysisService.checkVariableNamingConventions(cu, violations);

        assertFalse(violations.isEmpty(), "Local variables not following camelCase should add a violation.");
    }
}
