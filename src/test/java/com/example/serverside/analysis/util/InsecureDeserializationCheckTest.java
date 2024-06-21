package com.example.serverside.analysis.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InsecureDeserializationCheckTest {

    private List<String> findInsecureDeserializationPractices(String code) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertNotNull(cu, "Failed to parse the code.");

        List<String> vulnerabilities = new ArrayList<>();
        InsecureDeserializationCheck checker = new InsecureDeserializationCheck(vulnerabilities);
        checker.visit(cu, null);

        return vulnerabilities;
    }

    @Test
    void detectsInsecureDeserializationFromUserInput() {
        String code =
                "public class Test {\n" +
                        "    public void readData(HttpServletRequest request) throws Exception {\n" +
                        "        ObjectInputStream ois = new ObjectInputStream(request.getInputStream());\n" +
                        "        Object obj = ois.readObject();\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureDeserializationPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "Insecure deserialization from user input should be detected.");
    }

    @Test
    void noViolationForSafeSerializationSource() {
        String code =
                "public class Test {\n" +
                        "    public void safeRead() throws Exception {\n" +
                        "        FileInputStream fis = new FileInputStream(\"data.ser\");\n" +
                        "        ObjectInputStream ois = new ObjectInputStream(fis);\n" +
                        "        Object obj = ois.readObject();\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureDeserializationPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "No vulnerabilities should be detected for safe serialization sources.");
    }

    @Test
    void noViolationForLiteralStreamSource() {
        String code =
                "public class Test {\n" +
                        "    public void readFromLiteralSource() throws Exception {\n" +
                        "        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[10]);\n" +
                        "        ObjectInputStream ois = new ObjectInputStream(bais);\n" +
                        "        Object obj = ois.readObject();\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureDeserializationPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "No vulnerabilities should be detected for a literal stream source.");
    }

    @Test
    void detectsInsecureDeserializationFromComplexUserControlledSource() {
        String code =
                "public class Test {\n" +
                        "    public void readComplexData(HttpServletRequest request) throws Exception {\n" +
                        "        String data = request.getParameter(\"data\");\n" +
                        "        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());\n" +
                        "        ObjectInputStream ois = new ObjectInputStream(bais);\n" +
                        "        Object obj = ois.readObject();\n" +
                        "    }\n" +
                        "}";

        List<String> vulnerabilities = findInsecureDeserializationPractices(code);
        assertFalse(vulnerabilities.isEmpty(), "Insecure deserialization from a complex user-controlled source should be detected.");
    }

}
