package com.example.serverside.analysis.result;

import com.example.serverside.mongoDB.document.*;

/**
 * Class representing a combination of results from various document analyses.
 * This class is used to aggregate and access the results from different types of analyses such as security, complexity, code smells, quality, and style.
 * It allows for easy checking and comparison of IDs from all related documents.
 */
public class CombinedResult {
    private SecurityResultDocument securityResult; // Security analysis result
    private ComplexityResultDocument complexityResult; // Complexity analysis result
    private CodeSmellResultDocument codeSmellResult; // Code smell analysis result
    private QualityResultDocument qualityResult; // Quality analysis result
    private StyleResultDocument styleResult; // Style analysis result
    private CombinedAnalysisResultDocument combinedResults; // Combined analysis result

    /**
     * Default constructor for CombinedResult.
     */
    public CombinedResult() {
    }

    /**
     * Constructor that initializes CombinedResult with individual document analysis results.
     *
     * @param securityResult    The security result document.
     * @param complexityResult  The complexity result document.
     * @param codeSmellResult   The code smell result document.
     * @param qualityResult     The quality result document.
     * @param styleResult       The style result document.
     * @param combinedResults   The combined analysis result document.
     */
    public CombinedResult(SecurityResultDocument securityResult,
                          ComplexityResultDocument complexityResult,
                          CodeSmellResultDocument codeSmellResult,
                          QualityResultDocument qualityResult,
                          StyleResultDocument styleResult,
                          CombinedAnalysisResultDocument combinedResults
                          ) {
        this.securityResult = securityResult;
        this.complexityResult = complexityResult;
        this.codeSmellResult = codeSmellResult;
        this.qualityResult = qualityResult;
        this.styleResult = styleResult;
        this.combinedResults = combinedResults;
    }

    /**
     * Gets the security result document.
     *
     * @return The SecurityResultDocument object.
     */
    public SecurityResultDocument getSecurityResult() {
        return securityResult;
    }

    /**
     * Sets the security result document.
     *
     * @param securityResult The SecurityResultDocument object to set.
     */
    public void setSecurityResult(SecurityResultDocument securityResult) {
        this.securityResult = securityResult;
    }

    /**
     * Gets the complexity result document.
     *
     * @return The ComplexityResultDocument object.
     */
    public ComplexityResultDocument getComplexityResult() {
        return complexityResult;
    }

    /**
     * Sets the complexity result document.
     *
     * @param complexityResult The ComplexityResultDocument object to set.
     */
    public void setComplexityResult(ComplexityResultDocument complexityResult) {
        this.complexityResult = complexityResult;
    }

    /**
     * Gets the code smell result document.
     *
     * @return The CodeSmellResultDocument object.
     */
    public CodeSmellResultDocument getCodeSmellResult() {
        return codeSmellResult;
    }

    /**
     * Sets the code smell result document.
     *
     * @param codeSmellResult The CodeSmellResultDocument object to set.
     */
    public void setCodeSmellResult(CodeSmellResultDocument codeSmellResult) {
        this.codeSmellResult = codeSmellResult;
    }

    /**
     * Gets the quality result document.
     *
     * @return The QualityResultDocument object.
     */
    public QualityResultDocument getQualityResult() {
        return qualityResult;
    }

    /**
     * Sets the quality result document.
     *
     * @param qualityResult The QualityResultDocument object to set.
     */
    public void setQualityResult(QualityResultDocument qualityResult) {
        this.qualityResult = qualityResult;
    }

    /**
     * Gets the style result document.
     *
     * @return The StyleResultDocument object.
     */
    public StyleResultDocument getStyleResult() {
        return styleResult;
    }

    /**
     * Sets the style result document.
     *
     * @param styleResult The StyleResultDocument object to set.
     */
    public void setStyleResult(StyleResultDocument styleResult) {
        this.styleResult = styleResult;
    }

    /**
     * Gets the combined analysis result document.
     *
     * @return The CombinedAnalysisResultDocument object.
     */
    public CombinedAnalysisResultDocument getCombinedResult() {
        return combinedResults;
    }

    /**
     * Sets the combined analysis result document.
     *
     * @param combinedResults The CombinedAnalysisResultDocument object to set.
     */
    public void setCombinedResult(CombinedAnalysisResultDocument combinedResults) {
        this.combinedResults = combinedResults;
    }

}
