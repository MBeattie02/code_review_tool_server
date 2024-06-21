package com.example.serverside.slack.message;

import com.example.serverside.analysis.result.*;

public class ComposeSlackMessage {


    /**
     * Composes a Slack message for security analysis results.
     *
     * @param result     The result of the security analysis.
     * @param documentId The ID of the document analyzed.
     * @return A formatted string representing the security analysis report.
     */
    public static String composeSlackMessageSecurity(SecurityResult result, String documentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Security Analysis Report (ID: ").append(documentId).append("):\n");
        sb.append("Repository Info : ").append(result.getRepositoryInfo().toString()).append("):\n");
        sb.append("Security Issues Detected (Total: ").append(result.getVulnerabilities().size()).append("):\n");
        if (result.getVulnerabilities().isEmpty()) {
            sb.append("No vulnerabilities found.");
        } else {
            for (String vulnerability : result.getVulnerabilities()) {
                sb.append(" - ").append(vulnerability).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Composes a Slack message for style analysis results.
     *
     * @param result     The result of the style analysis.
     * @param documentId The ID of the document analyzed.
     * @return A formatted string representing the style analysis report.
     */
    public static String composeSlackMessageStyle(StyleResult result, String documentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Style Analysis Report (ID: ").append(documentId).append("):\n");
        sb.append("Repository Info : ").append(result.getRepositoryInfo().toString()).append("):\n");
        sb.append("Style Issues Detected (Total: ").append(result.getViolations().size()).append("):\n");
        if(result.getViolations().isEmpty()){
            sb.append("No style issues detected.");
        } else {
            for (String vulnerability : result.getViolations()) {
                sb.append(" - ").append(vulnerability).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Composes a Slack message for complexity analysis results.
     *
     * @param complexityResult The result of the complexity analysis.
     * @param documentId       The ID of the document analyzed.
     * @return A string summarizing the complexity analysis report.
     */
    public static String composeSlackMessageComplexity(ComplexityResult complexityResult, String documentId) {
        return "Complexity Analysis Report (ID: " + documentId + "):\n" +
                "Repository Info: " + complexityResult.getRepositoryInfo().toString() + "\n" +
                "Cyclomatic Complexity Score: " + complexityResult.getCyclomaticComplexity();
    }

    /**
     * Composes a Slack message for code smell analysis results.
     *
     * @param result     The result of the code smell analysis.
     * @param documentId The ID of the document analyzed.
     * @return A formatted string representing the code smell analysis report.
     */
    public static String composeSlackMessageSmell(CodeSmellResult result, String documentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Smell Analysis Report (ID: ").append(documentId).append("):\n");
        sb.append("Repository Info : ").append(result.getRepositoryInfo().toString()).append("):\n");
        sb.append("Smell Issues Detected (Total: ").append(result.getSmells().size()).append("):\n");
        if(result.getSmells().isEmpty()){
            sb.append("No code smells detected.");
        } else {
            for (String vulnerability : result.getSmells()) {
                sb.append(" - ").append(vulnerability).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Composes a Slack message for quality analysis results.
     *
     * @param result     The result of the quality analysis.
     * @param documentId The ID of the document analyzed.
     * @return A formatted string representing the quality analysis report.
     */
    public static String composeSlackMessageQuality(QualityResult result, String documentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Quality Analysis Report (ID: ").append(documentId).append("):\n");
        sb.append("Repository Info : ").append(result.getRepositoryInfo().toString()).append("):\n");
        sb.append("Quality Issues Detected (Total: ").append(result.getDuplications().size()).append("):\n");
        if(result.getDuplications().isEmpty()){
            sb.append("No Quality issues detected.");
        } else {
            for (String vulnerability : result.getDuplications()) {
                sb.append(" - ").append(vulnerability).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Composes a Slack message summarizing all analysis results.
     *
     * @param results    The combined results from all analyses.
     * @param documentId The ID of the document analyzed.
     * @return A string summarizing all analysis results in a single report.
     */
    public static String composeSlackMessageAll(CombinedResults results, String documentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Combined Analysis Report (ID: ").append(documentId).append("):\n");
        sb.append("Repository Info : ").append(results.getRepositoryInfo().toString()).append("):\n");

        // Append quality result summary
        sb.append("Quality Analysis: ").append(summaryOfQualityResult(results.getQualityResult())).append("\n");

        // Append code smell result summary
        sb.append("Code Smell Analysis: ").append(summaryOfCodeSmellResult(results.getCodeSmellResult())).append("\n");

        // Append security result summary
        sb.append("Security Analysis: ").append(summaryOfSecurityResult(results.getSecurityResult())).append("\n");

        // Append complexity result summary
        sb.append("Complexity Analysis: ").append(summaryOfComplexityResult(results.getComplexityResult())).append("\n");

        // Append style result summary
        sb.append("Style Analysis: ").append(summaryOfStyleResult(results.getStyleResult())).append("\n");

        return sb.toString();
    }

    // Private methods to summarize different analysis results

    private static String summaryOfQualityResult(QualityResult qualityResult) {
        if (qualityResult == null || qualityResult.getDuplications() == null || qualityResult.getDuplications().isEmpty()) {
            return "No quality issues detected. \n";
        }
        StringBuilder sb = new StringBuilder("Quality Issues Detected (Total: " + qualityResult.getDuplications().size() + "):\n");
        for (String issue : qualityResult.getDuplications()) {
            sb.append(" - ").append(issue).append("\n");
        }
        return sb.toString();
    }

    private static String summaryOfCodeSmellResult(CodeSmellResult codeSmellResult) {
        if (codeSmellResult == null || codeSmellResult.getSmells() == null || codeSmellResult.getSmells().isEmpty()) {
            return "No code smells detected. \n";
        }
        StringBuilder sb = new StringBuilder("Code Smells Detected (Total: " + codeSmellResult.getSmells().size() + "):\n");
        for (String smell : codeSmellResult.getSmells()) {
            sb.append(" - ").append(smell).append("\n");
        }
        return sb.toString();
    }

    private static String summaryOfComplexityResult(ComplexityResult complexityResult) {
        if (complexityResult == null || complexityResult.getCyclomaticComplexity() == 0) {
            return "Cyclomatic complexity not calculated. \n";
        }
        return "Cyclomatic complexity score: " + complexityResult.getCyclomaticComplexity() + "\n";
    }

    private static String summaryOfStyleResult(StyleResult styleResult) {
        if (styleResult == null || styleResult.getViolations() == null || styleResult.getViolations().isEmpty()) {
            return "No style violations found. \n";
        }
        StringBuilder sb = new StringBuilder("Style Violations Detected (Total: " + styleResult.getViolations().size() + "):\n");
        for (String violation : styleResult.getViolations()) {
            sb.append(" - ").append(violation).append("\n");
        }
        return sb.toString();
    }

    private static String summaryOfSecurityResult(SecurityResult securityResult) {
        if (securityResult == null || securityResult.getVulnerabilities() == null || securityResult.getVulnerabilities().isEmpty()) {
            return "No security vulnerabilities detected. \n";
        }
        StringBuilder sb = new StringBuilder("Security Vulnerabilities Detected (Total: " + securityResult.getVulnerabilities().size() + "):\n");
        for (String vulnerability : securityResult.getVulnerabilities()) {
            sb.append(" - ").append(vulnerability).append("\n");
        }
        return sb.toString();
    }




}
