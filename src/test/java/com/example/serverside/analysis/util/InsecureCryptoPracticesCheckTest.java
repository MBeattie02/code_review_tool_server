package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InsecureCryptoPracticesCheckTest {

    private List<String> findInsecureCryptoPractices(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        InsecureCryptoPracticesCheck checker = new InsecureCryptoPracticesCheck(vulnerabilities);
        checker.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsWeakAlgorithmUsage() {
        String code =
                "public class Test {\n" +
                        "    public void encrypt() {\n" +
                        "        Cipher cipher = Cipher.getInstance(\"DES\");\n" +  // Weak algorithm
                        "        // ... encryption logic ...\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureCryptoPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "Weak cryptographic algorithm usage should be detected.");
    }

    @Test
    void noViolationForSecureAlgorithms() {
        String code =
                "public class Test {\n" +
                        "    public void encrypt() {\n" +
                        "        Cipher cipher = Cipher.getInstance(\"AES\");\n" +  // Secure algorithm
                        "        // ... encryption logic ...\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureCryptoPractices(code);
        assertTrue(vulnerabilities.isEmpty(), "No vulnerabilities should be detected for secure cryptographic algorithms.");
    }

    @Test
    void capturesStringLiteralInitializers() {
        String weakAlgorithm = "DES"; // Example weak algorithm
        String code =
                "public class Test {\n" +
                        "    private static final String ALGORITHM = \"" + weakAlgorithm + "\";\n" +
                        "    public void encrypt() {\n" +
                        "        Cipher cipher = Cipher.getInstance(ALGORITHM);\n" +  // Using the variable
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureCryptoPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "Weak cryptographic algorithm usage with string literal should be detected.");

        // Check if the vulnerability message includes the weak algorithm
        boolean messageFound = vulnerabilities.stream().anyMatch(message ->
                message.contains(weakAlgorithm)
        );
        assertTrue(messageFound, "Expected vulnerability message with weak algorithm not found.");
    }

}
