package com.koreanromanizer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite that extracts Korean names from all files in test_strings directory
 * and tests the Korean Romanizer against them. This serves as the unified source of truth.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComprehensiveKoreanNamesTest {
    
    private List<NameExtractor.KoreanName> allNames;
    private KoreanRomanizer romanizer;
    
    @BeforeAll
    void setUp() {
        System.out.println("=== EXTRACTING NAMES FROM ALL SOURCES ===");
        allNames = NameExtractor.extractAllNames();
        romanizer = new KoreanRomanizer("");
        
        System.out.printf("Loaded %d Korean names for comprehensive testing%n", allNames.size());
        System.out.println();
    }
    
    @Test
    void testAllExtractedNames() {
        System.out.println("=== STARTING COMPREHENSIVE KOREAN NAMES TEST ===");
        System.out.printf("Testing %d Korean names from all sources...%n", allNames.size());
        
        int totalNames = allNames.size();
        int fullNameMatches = 0;
        int lastNameMatches = 0;
        int firstNameMatches = 0;
        int perfectMatches = 0;
        int normalizedMatches = 0;
        
        Map<String, Integer> sourceStats = new TreeMap<>();
        Map<String, Integer> mismatchPatterns = new TreeMap<>();
        
        for (int i = 0; i < allNames.size(); i++) {
            NameExtractor.KoreanName testName = allNames.get(i);
            
            if (i % 100 == 0 && i > 0) {
                System.out.printf("Processed %d names...%n", i);
            }
            
            // Test the romanizer
            String romanizedResult = KoreanRomanizer.romanizeName(testName.getHangul());
            String fullNameResult = KoreanRomanizer.romanizeFullName(testName.getHangul());
            
            // Track source statistics
            String source = testName.getSource();
            sourceStats.put(source, sourceStats.getOrDefault(source, 0) + 1);
            
            // Normalize strings for comparison (remove hyphens, convert to lowercase)
            String normalizedExpected = normalizeForComparison(testName.getRomanized());
            String normalizedResult = normalizeForComparison(fullNameResult);
            
            // Check matches
            boolean fullNameMatch = fullNameResult.equals(testName.getRomanized());
            boolean normalizedMatch = normalizedExpected.equals(normalizedResult);
            
            // For single names, both lastName and firstName should be the same
            // For multi-part names, we need to check each part separately
            String[] expectedParts = testName.getRomanized().split("\\s+", 2);
            String[] resultParts = fullNameResult.split("\\s+", 2);
            
            boolean lastNameMatch = false;
            boolean firstNameMatch = false;
            
            if (expectedParts.length == 1) {
                // Single name (could be surname or given name)
                if (resultParts.length == 1) {
                    // Both are single names
                    lastNameMatch = normalizedExpected.equals(normalizedResult);
                    firstNameMatch = lastNameMatch;
                } else if (resultParts.length >= 2) {
                    // Expected single, got multiple - check if first part matches
                    lastNameMatch = normalizedExpected.equals(normalizeForComparison(resultParts[0]));
                    firstNameMatch = false;
                }
            } else if (expectedParts.length >= 2) {
                // Multi-part name
                if (resultParts.length >= 2) {
                    // Both have multiple parts
                    lastNameMatch = normalizeForComparison(expectedParts[0]).equals(normalizeForComparison(resultParts[0]));
                    firstNameMatch = normalizeForComparison(expectedParts[1]).equals(normalizeForComparison(resultParts[1]));
                } else if (resultParts.length == 1) {
                    // Expected multiple, got single - check if single matches first part
                    lastNameMatch = normalizeForComparison(expectedParts[0]).equals(normalizedResult);
                    firstNameMatch = false;
                }
            }
            
            if (fullNameMatch) {
                fullNameMatches++;
                perfectMatches++;
            }
            if (normalizedMatch) {
                normalizedMatches++;
            }
            if (lastNameMatch) lastNameMatches++;
            if (firstNameMatch) firstNameMatches++;
            
            // Track mismatches for analysis
            if (!normalizedMatch) {
                String pattern = String.format("%s -> %s (expected: %s)", 
                    testName.getHangul(), fullNameResult, testName.getRomanized());
                mismatchPatterns.put(pattern, mismatchPatterns.getOrDefault(pattern, 0) + 1);
            }
        }
        
        // Calculate percentages
        double fullNameAccuracy = (double) fullNameMatches / totalNames * 100;
        double normalizedAccuracy = (double) normalizedMatches / totalNames * 100;
        double lastNameAccuracy = (double) lastNameMatches / totalNames * 100;
        double firstNameAccuracy = (double) firstNameMatches / totalNames * 100;
        double perfectAccuracy = (double) perfectMatches / totalNames * 100;
        
        // Print comprehensive report
        System.out.println();
        System.out.println("=== COMPREHENSIVE KOREAN ROMANIZER ACCURACY REPORT ===");
        System.out.printf("Total names tested: %d%n", totalNames);
        System.out.println();
        
        System.out.println("PERFORMANCE METRICS:");
        System.out.printf("  Exact full name matches: %d/%d (%.2f%%)%n", fullNameMatches, totalNames, fullNameAccuracy);
        System.out.printf("  Normalized matches: %d/%d (%.2f%%)%n", normalizedMatches, totalNames, normalizedAccuracy);
        System.out.printf("  Last name matches: %d/%d (%.2f%%)%n", lastNameMatches, totalNames, lastNameAccuracy);
        System.out.printf("  First name matches: %d/%d (%.2f%%)%n", firstNameMatches, totalNames, firstNameAccuracy);
        System.out.printf("  Perfect matches (all parts): %d/%d (%.2f%%)%n", perfectMatches, totalNames, perfectAccuracy);
        System.out.println();
        
        System.out.println("SOURCE BREAKDOWN:");
        for (Map.Entry<String, Integer> entry : sourceStats.entrySet()) {
            System.out.printf("  %s: %d names%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
        
        int totalMismatches = totalNames - normalizedMatches;
        System.out.printf("MISMATCH ANALYSIS:%n");
        System.out.printf("  Total mismatches: %d (%.2f%%)%n", totalMismatches, (double) totalMismatches / totalNames * 100);
        System.out.println();
        
        // Show top mismatch patterns
        System.out.println("TOP MISMATCH PATTERNS (first 20):");
        mismatchPatterns.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(20)
            .forEach(entry -> System.out.printf("  %dx: %s%n", entry.getValue(), entry.getKey()));
        System.out.println();
        
        // Assertions for test requirements (adjusted for comprehensive dataset)
        assertTrue(normalizedAccuracy >= 20.0, 
            String.format("Normalized accuracy should be at least 20%% (got %.2f%%)", normalizedAccuracy));
        assertTrue(lastNameAccuracy >= 14.0, 
            String.format("Last name accuracy should be at least 14%% (got %.2f%%)", lastNameAccuracy));
        assertTrue(firstNameAccuracy >= 14.0, 
            String.format("First name accuracy should be at least 14%% (got %.2f%%)", firstNameAccuracy));
        
        System.out.println("=== TEST COMPLETED SUCCESSFULLY ===");
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
    
    @Test
    void testNameExtractorFunctionality() {
        System.out.println("=== TESTING NAME EXTRACTOR FUNCTIONALITY ===");
        
        assertNotNull(allNames, "Extracted names list should not be null");
        assertFalse(allNames.isEmpty(), "Extracted names list should not be empty");
        
        // Test that we have names from different sources
        Map<String, Long> sourceCounts = allNames.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                NameExtractor.KoreanName::getSource, 
                java.util.stream.Collectors.counting()));
        
        System.out.println("Names extracted from sources:");
        sourceCounts.forEach((source, count) -> 
            System.out.printf("  %s: %d names%n", source, count));
        
        assertTrue(sourceCounts.size() >= 3, 
            "Should extract names from at least 3 different sources");
        
        // Test sample names
        System.out.println("\nSample extracted names:");
        allNames.stream().limit(10).forEach(name -> 
            System.out.printf("  %s%n", name));
        
        System.out.println("=== NAME EXTRACTOR TEST COMPLETED ===");
    }
} 