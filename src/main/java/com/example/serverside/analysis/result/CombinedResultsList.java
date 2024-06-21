package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.document.*;

import java.util.List;

/**
 * Class representing a combined list of various analysis results.
 * Used to aggregate and return lists of different analysis results including security, complexity, code smells, quality, style, and combined analysis.
 * This aids in providing a comprehensive view of all analysis results, useful for checking if analysis has improved.
 */
public class CombinedResultsList {
    private List<SecurityResultDocument> securityResults; // List of security analysis results
    private List<ComplexityResultDocument> complexityResults; // List of complexity analysis results
    private List<CodeSmellResultDocument> codeSmellResults; // List of code smell analysis results
    private List<QualityResultDocument> qualityResults; // List of quality analysis results
    private List<StyleResultDocument> styleResults; // List of style analysis results
    private List<CombinedAnalysisResultDocument> combinedResults; // List of combined analysis results

    /**
     * Default constructor for CombinedResultsList.
     */
    public CombinedResultsList() {
    }

    /**
     * Constructor with all types of result lists.
     * Initializes the object with lists of all types of analysis results.
     *
     * @param securityResults     List of SecurityResultDocument objects.
     * @param complexityResults   List of ComplexityResultDocument objects.
     * @param codeSmellResults    List of CodeSmellResultDocument objects.
     * @param qualityResults      List of QualityResultDocument objects.
     * @param styleResults        List of StyleResultDocument objects.
     * @param combinedResults     List of CombinedAnalysisResultDocument objects.
     */
    public CombinedResultsList(List<SecurityResultDocument> securityResults,
                               List<ComplexityResultDocument> complexityResults,
                               List<CodeSmellResultDocument> codeSmellResults,
                               List<QualityResultDocument> qualityResults,
                               List<StyleResultDocument> styleResults,
                               List<CombinedAnalysisResultDocument> combinedResults) {
        this.securityResults = securityResults;
        this.complexityResults = complexityResults;
        this.codeSmellResults = codeSmellResults;
        this.qualityResults = qualityResults;
        this.styleResults = styleResults;
        this.combinedResults = combinedResults;
    }

    /**
     * Gets the list of security results.
     *
     * @return The list of SecurityResultDocument objects.
     */
    public List<SecurityResultDocument> getSecurityResults() {
        return securityResults;
    }

    /**
     * Sets the list of security results.
     *
     * @param securityResults The list of SecurityResultDocument objects to set.
     */
    public void setSecurityResults(List<SecurityResultDocument> securityResults) {
        this.securityResults = securityResults;
    }

    /**
     * Gets the list of complexity results.
     *
     * @return The list of ComplexityResultDocument objects.
     */
    public List<ComplexityResultDocument> getComplexityResults() {
        return complexityResults;
    }

    /**
     * Sets the list of complexity results.
     *
     * @param complexityResults The list of ComplexityResultDocument objects to set.
     */
    public void setComplexityResults(List<ComplexityResultDocument> complexityResults) {
        this.complexityResults = complexityResults;
    }

    /**
     * Gets the list of code smell results.
     *
     * @return The list of CodeSmellResultDocument objects.
     */
    public List<CodeSmellResultDocument> getCodeSmellResults() {
        return codeSmellResults;
    }

    /**
     * Sets the list of code smell results.
     *
     * @param codeSmellResults The list of CodeSmellResultDocument objects to set.
     */
    public void setCodeSmellResults(List<CodeSmellResultDocument> codeSmellResults) {
        this.codeSmellResults = codeSmellResults;
    }

    /**
     * Gets the list of quality results.
     *
     * @return The list of QualityResultDocument objects.
     */
    public List<QualityResultDocument> getQualityResults() {
        return qualityResults;
    }

    /**
     * Sets the list of quality results.
     *
     * @param qualityResults The list of QualityResultDocument objects to set.
     */
    public void setQualityResults(List<QualityResultDocument> qualityResults) {
        this.qualityResults = qualityResults;
    }

    /**
     * Gets the list of style results.
     *
     * @return The list of StyleResultDocument objects.
     */
    public List<StyleResultDocument> getStyleResults() {
        return styleResults;
    }

    /**
     * Sets the list of style results.
     *
     * @param styleResults The list of StyleResultDocument objects to set.
     */
    public void setStyleResults(List<StyleResultDocument> styleResults) {
        this.styleResults = styleResults;
    }

    /**
     * Gets the list of combined analysis results.
     *
     * @return The list of CombinedAnalysisResultDocument objects.
     */
    public List<CombinedAnalysisResultDocument> getCombinedResults() {
        return combinedResults;
    }

    /**
     * Sets the list of combined analysis results.
     *
     * @param combinedResults The list of CombinedAnalysisResultDocument objects to set.
     */
    public void setCombinedResults(List<CombinedAnalysisResultDocument> combinedResults) {
        this.combinedResults = combinedResults;
    }
}
