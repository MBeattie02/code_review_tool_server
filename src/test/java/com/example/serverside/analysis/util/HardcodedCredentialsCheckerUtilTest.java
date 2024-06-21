package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HardcodedCredentialsCheckerUtilTest {

    private List<String> findHardcodedCredentials(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        HardcodedCredentialsCheckerUtil checker = new HardcodedCredentialsCheckerUtil(vulnerabilities);
        checker.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsHardcodedCredentialsInVariables() {
        String code =
                "public class Test {\n" +
                        "    private String password = \"hardcodedPassword\";\n" +
                        "}";

        List<String> vulnerabilities = findHardcodedCredentials(code);
        assertFalse(vulnerabilities.isEmpty(), "Hardcoded credentials should be detected in variables.");
    }

    @Test
    void detectsHardcodedCredentialsInMethodArguments() {
        String code =
                "public class Test {\n" +
                        "    public void connect() {\n" +
                        "        authenticate(\"admin\", \"hardcodedPassword\");\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findHardcodedCredentials(code);
        assertFalse(vulnerabilities.isEmpty(), "Hardcoded credentials should be detected in method arguments.");
    }

    @Test
    void noFalsePositivesForSafeCode() {
        String code =
                "public class Test {\n" +
                        "    private String username = \"admin\";\n" +
                        "    private String password;\n" +
                        "}";

        List<String> vulnerabilities = findHardcodedCredentials(code);
        assertTrue(vulnerabilities.isEmpty(), "No hardcoded credentials should be detected in safe code.");
    }
}
