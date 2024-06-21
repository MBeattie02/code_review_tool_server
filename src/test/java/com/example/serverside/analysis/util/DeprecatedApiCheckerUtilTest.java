package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeprecatedApiCheckerUtilTest {

    @BeforeEach
    void setup() {
        // Manually add entries to the insecureImports map before running the tests
        DeprecatedApiCheckerUtil.insecureImports.put("java.security.MessageDigest", "Use java.security.SecureRandom instead");
    }

    private List<String> findDeprecatedApiUsage(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        DeprecatedApiCheckerUtil checker = new DeprecatedApiCheckerUtil(vulnerabilities);
        checker.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsDeprecatedApiUsage() {
        String insecureImport = "java.security.MessageDigest";
        String code = "import " + insecureImport + ";\n" +
                "public class TestClass { }\n";

        List<String> vulnerabilities = findDeprecatedApiUsage(code);
        assertFalse(vulnerabilities.isEmpty(), "Deprecated API usage should be detected.");
    }

    @Test
    void noViolationForSafeImports() {
        String code =
                "import java.util.List;\n" +
                        "public class Test {\n" +
                        "    // ... class body ...\n" +
                        "}";

        List<String> vulnerabilities = findDeprecatedApiUsage(code);
        assertTrue(vulnerabilities.isEmpty(), "No vulnerabilities should be detected for safe imports.");
    }

    @Test
    void detectsDeprecatedImport() {
        String insecureImport = "java.security.MessageDigest";
        String code = "import " + insecureImport + ";\n" +
                "public class TestClass { }\n";

        List<String> vulnerabilities = findDeprecatedApiUsage(code);
        assertFalse(vulnerabilities.isEmpty(), "Deprecated import should be detected.");
    }

    @Test
    void noViolationForNonDeprecatedImport() {
        String code = "import java.util.List;\n" +
                "public class TestClass { }\n";

        List<String> vulnerabilities = findDeprecatedApiUsage(code);
        assertTrue(vulnerabilities.isEmpty(), "No vulnerabilities should be detected for non-deprecated imports.");
    }

}
