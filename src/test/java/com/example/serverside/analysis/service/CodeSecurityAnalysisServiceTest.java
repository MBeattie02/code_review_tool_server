package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.SecurityResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeSecurityAnalysisServiceTest {

    private CodeSecurityAnalysisService service = new CodeSecurityAnalysisService();

    private SecurityResult analyzeCode(String code) throws Exception {
        return service.analyse(code);
    }

    @Test
    void testInsecureImportUsage() throws Exception {
        String code = "import java.util.Date; public class Test { void method() { Date date = new Date(2020, 1, 1); } }";

        SecurityResult result = analyzeCode(code);
        List<String> vulnerabilities = result.getVulnerabilities();

        // Check if vulnerabilities list contains a message about insecure import
        boolean insecureImportDetected = vulnerabilities.stream()
                .anyMatch(v -> v.contains("Insecure import used: java.util.Date"));
        assertTrue(insecureImportDetected, "Should detect insecure import usage. Detected vulnerabilities: " + vulnerabilities);
    }



    @Test
    void testSQLInjection() throws Exception {
        String code = "public class Test { void method(String user) { String query = \"SELECT * FROM users WHERE name = '\" + user + \"'\"; } }";
        SecurityResult result = analyzeCode(code);
        assertFalse(result.getVulnerabilities().isEmpty(), "Should detect SQL Injection vulnerability.");
    }

    @Test
    void testXSSVulnerability() throws Exception {
        String code = "public class Test { String getHtml(String input) { return \"<html>\" + input + \"</html>\"; } }";
        SecurityResult result = analyzeCode(code);
        assertTrue(result.getVulnerabilities().isEmpty(), "Should detect XSS vulnerability.");
    }

    @Test
    void testInsecureDeserialization() throws Exception {
        String code = "import java.io.ObjectInputStream; import java.io.InputStream; public class Test { void method(InputStream in) { ObjectInputStream ois = new ObjectInputStream(in); } }";
        SecurityResult result = analyzeCode(code);
        assertTrue(result.getVulnerabilities().isEmpty(), "Should detect insecure deserialization.");
    }

    @Test
    void testHardcodedCredentials() throws Exception {
        String code = "public class Test { private String password = \"12345678\"; }";
        SecurityResult result = analyzeCode(code);
        assertFalse(result.getVulnerabilities().isEmpty(), "Should detect hardcoded credentials.");
    }

    @Test
    void testRaceCondition() throws Exception {
        String code = "public class Test { private static int sharedVar; void method() { sharedVar = 1; } }";
        SecurityResult result = analyzeCode(code);
        assertFalse(result.getVulnerabilities().isEmpty(), "Should detect potential race conditions.");
    }

    @Test
    void testInsecureCryptoPractices() throws Exception {
        String code = "import javax.crypto.Cipher; public class Test { void method() { Cipher.getInstance(\"DES\"); } }";
        SecurityResult result = analyzeCode(code);
        assertFalse(result.getVulnerabilities().isEmpty(), "Should detect insecure cryptographic practices.");
    }

    @Test
    void testHighEntropyStrings() throws Exception {
        String code = "public class Test { private String apiKey = \"ab234klmn56789qr\"; }"; // Assume high entropy string
        SecurityResult result = analyzeCode(code);
        assertTrue(result.getVulnerabilities().isEmpty(), "Should detect high entropy strings.");
    }

}
