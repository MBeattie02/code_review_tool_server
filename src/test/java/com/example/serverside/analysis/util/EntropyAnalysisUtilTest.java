package com.example.serverside.analysis.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntropyAnalysisUtilTest {

    @Test
    void calculateShannonEntropy_withNormalString() {
        double entropy = EntropyAnalysisUtil.calculateShannonEntropy("hello world");
        assertTrue(entropy > 0, "Entropy should be greater than 0 for a non-empty string.");
    }

    @Test
    void calculateShannonEntropy_withRepeatingCharacters() {
        double entropy = EntropyAnalysisUtil.calculateShannonEntropy("aaaa");
        assertEquals(0.0, entropy, "Entropy should be 0 for a string with repeating characters.");
    }

    @Test
    void calculateShannonEntropy_withEmptyString() {
        double entropy = EntropyAnalysisUtil.calculateShannonEntropy("");
        assertEquals(0.0, entropy, "Entropy should be 0 for an empty string.");
    }

    @Test
    void calculateShannonEntropy_withNullString() {
        double entropy = EntropyAnalysisUtil.calculateShannonEntropy(null);
        assertEquals(0.0, entropy, "Entropy should be 0 for a null string.");
    }

    EntropyAnalysisUtil entropyAnalysisUtil = new EntropyAnalysisUtil();
}
