package com.example.serverside.analysis.util;


import java.util.HashMap;
import java.util.Map;

public class EntropyAnalysisUtil {
    /**
     * Calculates the Shannon entropy of a given string. Shannon entropy is a measure of the unpredictability or randomness of data.
     *
     * @param s The string for which to calculate the entropy.
     * @return The calculated Shannon entropy.
     */
    public static double calculateShannonEntropy(String s) {
        if (s == null || s.isEmpty()) {
            return 0.0;
        }

        Map<Character, Integer> charCounts = new HashMap<>();
        for (char c : s.toCharArray()) {
            charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
        }

        double entropy = 0.0;
        for (int count : charCounts.values()) {
            double probability = (double) count / s.length();
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }
}
