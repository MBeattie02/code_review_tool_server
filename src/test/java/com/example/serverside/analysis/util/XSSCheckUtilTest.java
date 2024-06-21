package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XSSCheckUtilTest {

    private List<String> findXSSVulnerabilities(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        XSSCheckUtil visitor = new XSSCheckUtil(vulnerabilities);
        visitor.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsVulnerabilityInStringConcatenation() {
        String code =
                "public class Test {\n" +
                        "    public void vulnerableMethod(HttpServletRequest request) {\n" +
                        "        String userInput = request.getParameter(\"data\");\n" +
                        "        String response = \"<html>\" + userInput + \"</html>\";\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findXSSVulnerabilities(code);
        assertFalse(vulnerabilities.isEmpty(), "XSS vulnerability should be detected in string concatenation.");
    }

    @Test
    void noVulnerabilityWhenUserInputIsSanitized() {
        String code =
                "public class Test {\n" +
                        "    public void safeMethod(HttpServletRequest request) {\n" +
                        "        String userInput = sanitize(request.getParameter(\"data\"));\n" +
                        "        String response = \"<html>\" + userInput + \"</html>\";\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findXSSVulnerabilities(code);
        assertTrue(vulnerabilities.isEmpty(), "No XSS vulnerability should be detected when user input is sanitized.");
    }
}
