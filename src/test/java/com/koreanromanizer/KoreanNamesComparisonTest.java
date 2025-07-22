package com.koreanromanizer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for comparing Korean romanization accuracy across different sources.
 * Now uses the comprehensive NameExtractor for unified testing.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KoreanNamesComparisonTest {
    
    private List<NameExtractor.KoreanName> testNames;
    
    @BeforeAll
    void loadTestData() {
        System.out.println("=== LOADING TEST DATA FROM ALL SOURCES ===");
        testNames = NameExtractor.extractAllNames();
        
        // Filter to get a reasonable subset for comparison testing
        testNames = testNames.stream()
            .filter(name -> name.getSource().contains("given names") || name.getSource().contains("surnames"))
            .limit(200) // Use first 200 names for comparison
            .collect(java.util.stream.Collectors.toList());
        
        System.out.printf("Loaded %d Korean names for comparison testing%n", testNames.size());
    }
    
    @Test
    void testRomanizationAccuracy() {
        System.out.println("=== STARTING KOREAN ROMANIZATION COMPARISON TEST ===");
        System.out.printf("Testing %d Korean names...%n", testNames.size());
        
        int totalNames = testNames.size();
        int exactMatches = 0;
        int normalizedMatches = 0;
        int partialMatches = 0;
        
        Map<String, Integer> sourceStats = new TreeMap<>();
        Map<String, Integer> mismatchTypes = new TreeMap<>();
        
        for (NameExtractor.KoreanName testName : testNames) {
            // Test the romanizer
            String romanizedResult = KoreanRomanizer.romanizeFullName(testName.getHangul());
            
            // Track source statistics
            String source = testName.getSource();
            sourceStats.put(source, sourceStats.getOrDefault(source, 0) + 1);
            
            // Check different types of matches
            boolean exactMatch = romanizedResult.equals(testName.getRomanized());
            boolean normalizedMatch = normalizeForComparison(romanizedResult).equals(normalizeForComparison(testName.getRomanized()));
            boolean partialMatch = romanizedResult.toLowerCase().contains(testName.getRomanized().toLowerCase()) || 
                                 testName.getRomanized().toLowerCase().contains(romanizedResult.toLowerCase());
            
            if (exactMatch) {
                exactMatches++;
            }
            if (normalizedMatch) {
                normalizedMatches++;
            }
            if (partialMatch) {
                partialMatches++;
            }
            
            // Track mismatch types
            if (!normalizedMatch) {
                String mismatchType = String.format("%s -> %s (expected: %s)", 
                    testName.getHangul(), romanizedResult, testName.getRomanized());
                mismatchTypes.put(mismatchType, mismatchTypes.getOrDefault(mismatchType, 0) + 1);
            }
        }
        
        // Calculate percentages
        double exactAccuracy = (double) exactMatches / totalNames * 100;
        double normalizedAccuracy = (double) normalizedMatches / totalNames * 100;
        double partialAccuracy = (double) partialMatches / totalNames * 100;
        
        // Print comprehensive report
        System.out.println();
        System.out.println("=== KOREAN ROMANIZATION COMPARISON REPORT ===");
        System.out.printf("Total names tested: %d%n", totalNames);
        System.out.println();
        
        System.out.println("ACCURACY METRICS:");
        System.out.printf("  Exact matches: %d/%d (%.2f%%)%n", exactMatches, totalNames, exactAccuracy);
        System.out.printf("  Normalized matches: %d/%d (%.2f%%)%n", normalizedMatches, totalNames, normalizedAccuracy);
        System.out.printf("  Partial matches: %d/%d (%.2f%%)%n", partialMatches, totalNames, partialAccuracy);
        System.out.println();
        
        System.out.println("SOURCE BREAKDOWN:");
        for (Map.Entry<String, Integer> entry : sourceStats.entrySet()) {
            System.out.printf("  %s: %d names%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
        
        // Show top mismatch patterns
        System.out.println("TOP MISMATCH PATTERNS (first 10):");
        mismatchTypes.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> System.out.printf("  %dx: %s%n", entry.getValue(), entry.getKey()));
        System.out.println();
        
        // Assertions
        assertTrue(normalizedAccuracy >= 30.0, 
            String.format("Normalized accuracy should be at least 30%% (got %.2f%%)", normalizedAccuracy));
        assertTrue(partialAccuracy >= 50.0, 
            String.format("Partial accuracy should be at least 50%% (got %.2f%%)", partialAccuracy));
        
        System.out.println("=== COMPARISON TEST COMPLETED SUCCESSFULLY ===");
    }
    
    /**
     * Normalize strings for comparison by removing hyphens, converting to lowercase,
     * and handling common romanization variations
     */
    private String normalizeForComparison(String text) {
        if (text == null) return "";
        
        return text.toLowerCase()
            .replaceAll("-", " ")
            .replaceAll("\\s+", " ")
            .trim();
    }
} 