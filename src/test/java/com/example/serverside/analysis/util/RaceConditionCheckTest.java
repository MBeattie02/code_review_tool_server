package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RaceConditionCheckTest {

    private List<String> findRaceConditionsInCode(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> raceConditions = new ArrayList<>();
        RaceConditionCheck visitor = new RaceConditionCheck(raceConditions);
        visitor.visit(cu, null);

        return raceConditions;
    }

    private void assertContainsRaceCondition(List<String> raceConditions, String expectedMessage) {
        assertTrue(raceConditions.stream().anyMatch(msg -> msg.contains(expectedMessage)),
                "Expected race condition not found: " + expectedMessage);
    }

    @Test
    public void testRaceConditionDetected() {
        String code = "public class Test {\n" +
                "    private static int sharedVar;\n" +
                "    public void method() {\n" +
                "        sharedVar = 1;\n" +
                "    }\n" +
                "}";

        List<String> raceConditions = findRaceConditionsInCode(code);
        assertContainsRaceCondition(raceConditions, "sharedVar");
    }

    @Test
    public void testNoRaceConditionWhenSynchronized() {
        String code = "public class Test {\n" +
                "    private static int sharedVar;\n" +
                "    public synchronized void method() {\n" +
                "        sharedVar = 1;\n" +
                "    }\n" +
                "}";

        List<String> raceConditions = findRaceConditionsInCode(code);
        assertTrue(raceConditions.isEmpty(), "No race condition should be detected.");
    }

    @Test
    public void testRaceConditionWithFieldAccess() {
        String code =
                "public class Test {\n" +
                        "    private static int sharedVar;\n" +
                        "    public void method() {\n" +
                        "        Test.sharedVar = 2;\n" +  // Field access that could lead to a race condition
                        "    }\n" +
                        "}";

        List<String> raceConditions = findRaceConditionsInCode(code);
        assertContainsRaceCondition(raceConditions, "sharedVar");
    }

    @Test
    public void testRaceConditionInMethodCall() {
        String code =
                "public class Test {\n" +
                        "    private static int sharedResource;\n" +
                        "    public void method() {\n" +
                        "        sharedResource();\n" + // Method name matches shared resource name
                        "    }\n" +
                        "    public static void sharedResource() {}\n" +
                        "}";

        List<String> raceConditions = findRaceConditionsInCode(code);
        assertContainsRaceCondition(raceConditions, "sharedResource");
    }

}
