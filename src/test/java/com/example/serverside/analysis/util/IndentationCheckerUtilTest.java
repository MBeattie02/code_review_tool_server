package com.example.serverside.analysis.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndentationCheckerUtilTest {

    private List<String> checkIndentation(String code) {
        List<String> violations = new ArrayList<>();
        IndentationCheckerUtil checker = new IndentationCheckerUtil(violations, code);
        checker.check();
        return violations;
    }


    @Test
    void detectsIncorrectIndentation() {
        String code =
                "public class Test {\n" +
                        "    public void method() {\n" +
                        "      int x = 1;\n" +  // Incorrect indentation
                        "    }\n" +
                        "}";

        List<String> violations = checkIndentation(code);
        assertFalse(violations.isEmpty(), "Indentation violations should be detected.");
    }

    @Test
    void noViolationsForCorrectIndentation() {
        String code =
                "public class Test {\n" +
                        "    public void method() {\n" +
                        "        int x = 1;\n" +
                        "    }\n" +
                        "}";

        List<String> violations = checkIndentation(code);
        assertTrue(violations.isEmpty(), "No indentation violations should be detected for correctly indented code.");
    }


    @Test
    void testNoIndentation() throws ReflectiveOperationException {
        Assertions.assertEquals(0, getIndentationLevel("Test with no leading spaces"));
    }

    @Test
    void testFourSpacesIndentation() throws ReflectiveOperationException {
        Assertions.assertEquals(4, getIndentationLevel("    Test with 4 leading spaces"));
    }

    @Test
    void testAllSpaces() throws ReflectiveOperationException {
        Assertions.assertEquals(5, getIndentationLevel("     "));
    }

    @Test
    void testEmptyString() throws ReflectiveOperationException {
        Assertions.assertEquals(0, getIndentationLevel(""));
    }

    @Test
    void testMixedSpacesAndTabs() throws ReflectiveOperationException {

        Assertions.assertEquals(2, getIndentationLevel("  \tTest with mixed spaces and tabs"));
    }

    private int getIndentationLevel(String line) throws ReflectiveOperationException {
        IndentationCheckerUtil checker = new IndentationCheckerUtil(new ArrayList<>(), "");
        Method method = IndentationCheckerUtil.class.getDeclaredMethod("getIndentationLevel", String.class);
        method.setAccessible(true);
        return (int) method.invoke(checker, line);
    }

}
