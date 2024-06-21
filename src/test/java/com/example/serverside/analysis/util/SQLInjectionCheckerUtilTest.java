package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLInjectionCheckerUtilTest {

    private List<String> findSQLInjections(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        SQLInjectionCheckerUtil visitor = new SQLInjectionCheckerUtil(vulnerabilities);
        visitor.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsVulnerabilityInDirectConcatenation() {
        String code =
                "public class Test {\n" +
                        "    public void executeQuery(String userInput) {\n" +
                        "        String query = \"SELECT * FROM table WHERE column = '\" + userInput + \"'\";\n" +
                        "        database.executeQuery(query);\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findSQLInjections(code);
        assertFalse(vulnerabilities.isEmpty(), "SQL Injection vulnerability should be detected.");
    }

    @Test
    void noVulnerabilityWhenUsingPreparedStatements() {
        String code =
                "public class Test {\n" +
                        "    public void executeQuery(String userInput) {\n" +
                        "        PreparedStatement stmt = database.prepareStatement(\"SELECT * FROM table WHERE column = ?\");\n" +
                        "        stmt.setString(1, userInput);\n" +
                        "        stmt.executeQuery();\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findSQLInjections(code);
        assertTrue(vulnerabilities.isEmpty(), "No SQL Injection vulnerability should be detected with prepared statements.");
    }

    @Test
    void detectsVulnerabilityInStringAssignmentConcatenation() {
        String code =
                "public class Test {\n" +
                        "    public void executeQuery(String userInput) {\n" +
                        "        String query;\n" +
                        "        query = \"SELECT * FROM table WHERE column = '\" + userInput + \"'\";\n" +
                        "        database.executeQuery(query);\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findSQLInjections(code);
        assertFalse(vulnerabilities.isEmpty(), "SQL Injection vulnerability should be detected in string assignment concatenation.");
    }



}
