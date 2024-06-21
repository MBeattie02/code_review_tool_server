package com.example.serverside.analysis.service;

import com.example.serverside.analysis.result.SecurityResult;
import com.example.serverside.analysis.util.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeSecurityAnalysisService {

    public SecurityResult analyse(String code) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(code);
        SecurityResult result = new SecurityResult();
        List<String> vulnerabilities = new ArrayList<>();

        DeprecatedApiCheckerUtil deprecatedApiChecker = new DeprecatedApiCheckerUtil(vulnerabilities);
        deprecatedApiChecker.visit(cu, null);


        SQLInjectionCheckerUtil sqlInjectionChecker = new SQLInjectionCheckerUtil(vulnerabilities);
        sqlInjectionChecker.visit(cu, null); // This will start the analysis

        XSSCheckUtil xssCheckUtil = new XSSCheckUtil(vulnerabilities);
        xssCheckUtil.visit(cu, null); // This will start the analysis

        InsecureDeserializationCheck insecureDeserializationCheck = new InsecureDeserializationCheck(vulnerabilities);
        insecureDeserializationCheck.visit(cu, null); // This will start the analysis

        HardcodedCredentialsCheckerUtil credentialsChecker = new HardcodedCredentialsCheckerUtil(vulnerabilities);
        credentialsChecker.visit(cu, null);

        RaceConditionCheck raceConditionCheck = new RaceConditionCheck(vulnerabilities);
        raceConditionCheck.visit(cu, null);

        InsecureCryptoPracticesCheck insecureCryptoPracticesCheck = new InsecureCryptoPracticesCheck(vulnerabilities);
        insecureCryptoPracticesCheck.visit(cu, null);

        checkHighEntropyStrings(cu, vulnerabilities);
        result.setVulnerabilitiesCount(vulnerabilities.size());
        result.setVulnerabilities(vulnerabilities);

        return result;

    }


    private static final double ENTROPY_THRESHOLD = 4.0; // Configurable threshold
    private static final int MIN_STRING_LENGTH = 8; // Minimum length to consider for entropy check

    // High Entropy Strings Check
    private void checkHighEntropyStrings(CompilationUnit cu, List<String> vulnerabilities) {
        HighEntropyStringVisitor visitor = new HighEntropyStringVisitor(vulnerabilities);
        visitor.visit(cu, null);
    }

    /**
     * Custom Visitor for detecting high entropy strings in a {@link CompilationUnit}.
     * This visitor checks each string literal's entropy and reports those with unusually high values.
     */
    private static class HighEntropyStringVisitor extends VoidVisitorAdapter<Void> {
        private final List<String> vulnerabilities;

        public HighEntropyStringVisitor(List<String> vulnerabilities) {
            this.vulnerabilities = vulnerabilities;
        }

        @Override
        public void visit(StringLiteralExpr n, Void arg) {
            super.visit(n, arg);
            String value = n.getValue();
            if (value.length() >= MIN_STRING_LENGTH) {
                double entropy = EntropyAnalysisUtil.calculateShannonEntropy(value);
                if (entropy > ENTROPY_THRESHOLD) {
                    int line = n.getRange().map(r -> r.begin.line).orElse(-1);
                    String message = String.format("Violation at line %d: High entropy string detected: \"%s\".", line, value);
                    vulnerabilities.add(message);
                }
            }
        }
    }

}