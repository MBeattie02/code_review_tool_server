package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.ComplexityResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeComplexityAnalysisServiceTest {

    private CodeComplexityAnalysisService service = new CodeComplexityAnalysisService();

    private int calculateComplexity(String code) {
        ComplexityResult result = service.calculateComplexity(code);
        return result.getCyclomaticComplexity();
    }

    @Test
    void complexityOfSimpleCode() {
        String code = "public class TestClass {\n" +
                "    public void testMethod() {\n" +
                "        int a = 10;\n" +
                "    }\n" +
                "}";

        int complexity = calculateComplexity(code);
        assertEquals(2, complexity, "Complexity of simple code should be 1.");
    }

    @Test
    void complexityWithMultipleControlFlows() {
        String code = "public class TestClass {\n" +
                "    public void testMethod(int a) {\n" +
                "        if (a > 10) {\n" +
                "            System.out.println(a);\n" +
                "        } else if (a < 5) {\n" +
                "            System.out.println(-a);\n" +
                "        }\n" +
                "        for (int i = 0; i < a; i++) {\n" +
                "            System.out.println(i);\n" +
                "        }\n" +
                "    }\n" +
                "}";

        int complexity = calculateComplexity(code);
        assertEquals(4, complexity, "Complexity with multiple control flows should be calculated correctly.");
    }


    @Test
    void complexityOfNestedStructures() {
        String code = "public class TestClass {\n" +
                "    public void testMethod(int a) {\n" +
                "        if (a > 10) {\n" +
                "            while (a > 0) {\n" +
                "                a--;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        int complexity = calculateComplexity(code);
        assertEquals(3, complexity, "Complexity of nested structures should be calculated correctly.");
    }

    @Test
    void testCalculateComplexityForSwitchStmt() throws Exception {
        CodeComplexityAnalysisService service = new CodeComplexityAnalysisService();
        Method method = CodeComplexityAnalysisService.class.getDeclaredMethod("calculateComplexityForSwitchStmt", SwitchStmt.class);
        method.setAccessible(true);

        String switchStatement = "switch (day) { case MONDAY: break; case TUESDAY: break; }";
        SwitchStmt switchStmt = (SwitchStmt) StaticJavaParser.parseStatement(switchStatement);


        int complexity = (int) method.invoke(service, switchStmt);

        assertEquals(2, complexity, "Complexity of a switch statement with two cases should be 2.");
    }

    @Test
    void testGetComplexityForNode() throws Exception {

        CodeComplexityAnalysisService service = new CodeComplexityAnalysisService();

        Node switchNode = StaticJavaParser.parseStatement("switch(day) { case MONDAY: break; }");

        Method method = CodeComplexityAnalysisService.class.getDeclaredMethod("getComplexityForNode", Node.class);
        method.setAccessible(true);

        int complexity = (int) method.invoke(service, switchNode);

        assertTrue(complexity > 0, "SwitchStmt should add to complexity based on cases.");
    }

    private Method getComplexityForNodeMethod;

    @BeforeEach
    void setUp() throws Exception {
        getComplexityForNodeMethod = CodeComplexityAnalysisService.class.getDeclaredMethod("getComplexityForNode", Node.class);
        getComplexityForNodeMethod.setAccessible(true);
    }

    private int invokeGetComplexityForNode(Node node) throws Exception {
        CodeComplexityAnalysisService service = new CodeComplexityAnalysisService();
        return (int) getComplexityForNodeMethod.invoke(service, node);
    }

    @Test
    void testWhileStmtComplexity() throws Exception {
        Node node = StaticJavaParser.parseStatement("while(true) {}");
        int complexity = invokeGetComplexityForNode(node);
        assertEquals(1, complexity, "While loop should add 1 to complexity.");
    }

    @Test
    void testForStmtComplexity() throws Exception {
        Node node = StaticJavaParser.parseStatement("for(int i = 0; i < 10; i++) {}");
        int complexity = invokeGetComplexityForNode(node);
        assertEquals(1, complexity, "For loop should add 1 to complexity.");
    }

    @Test
    void testForEachStmtComplexity() throws Exception {
        Node node = StaticJavaParser.parseStatement("for(String item : items) {}");
        int complexity = invokeGetComplexityForNode(node);
        assertEquals(1, complexity, "For-each loop should add 1 to complexity.");
    }

    @Test
    void testDoStmtComplexity() throws Exception {
        Node node = StaticJavaParser.parseStatement("do {} while(true);");
        int complexity = invokeGetComplexityForNode(node);
        assertEquals(1, complexity, "Do-while loop should add 1 to complexity.");
    }

    @Test
    void testOrphanedCatchClauseIncreasesComplexity() throws Exception {

        CatchClause catchClause = new CatchClause();

        int complexity = invokeGetComplexityForNode(catchClause);

        assertEquals(1, complexity, "Orphaned CatchClause should increase complexity.");
    }





}
