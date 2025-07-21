package com.koreanromanizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class that tests the Korean Romanizer against all 900+ Korean names
 * from the korean_names_1000.json dataset.
 * 
 * This test provides detailed accuracy analysis and identifies patterns in mismatches
 * to help improve the romanization algorithm.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KoreanNames1000Test {

    private static List<KoreanName> koreanNames;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void loadTestData() {
        try {
            // Load the JSON file from the test_strings directory
            File jsonFile = new File("test_strings/korean_names_1000.json");
            if (!jsonFile.exists()) {
                throw new RuntimeException("Could not find test_strings/korean_names_1000.json");
            }
            
            koreanNames = objectMapper.readValue(jsonFile, new TypeReference<List<KoreanName>>() {});
            System.out.println("Loaded " + koreanNames.size() + " Korean names for comprehensive testing");
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    @Test
    public void testAllKoreanNames() {
        int totalTests = 0;
        int exactMatches = 0;
        int lastNameMatches = 0;
        int firstNameMatches = 0;
        int fullNameMatches = 0;
        
        List<ComparisonResult> mismatches = new ArrayList<>();
        List<ComparisonResult> allResults = new ArrayList<>();
        
        System.out.println("\n=== STARTING COMPREHENSIVE KOREAN NAMES TEST ===");
        System.out.println("Testing " + koreanNames.size() + " Korean names...");
        
        for (KoreanName name : koreanNames) {
            totalTests++;
            
            // Test full name using the new romanizeFullName method
            String ourFullName = KoreanRomanizer.romanizeFullName(name.fullNameHangul);
            boolean fullNameMatch = ourFullName.equalsIgnoreCase(name.fullNameRomanized);
            
            // Test last name using the new romanizeName method
            String ourLastName = KoreanRomanizer.romanizeName(name.lastNameHangul);
            boolean lastNameMatch = ourLastName.equalsIgnoreCase(name.lastNameRomanized);
            
            // Test first name using the new romanizeName method
            String ourFirstName = KoreanRomanizer.romanizeName(name.firstNameHangul);
            boolean firstNameMatch = ourFirstName.equalsIgnoreCase(name.firstNameRomanized);
            
            // Count matches
            if (fullNameMatch) {
                fullNameMatches++;
            }
            if (lastNameMatch) {
                lastNameMatches++;
            }
            if (firstNameMatch) {
                firstNameMatches++;
            }
            if (fullNameMatch && lastNameMatch && firstNameMatch) {
                exactMatches++;
            }
            
            // Record all results for analysis
            ComparisonResult result = new ComparisonResult(
                name, ourLastName, ourFirstName, ourFullName,
                lastNameMatch, firstNameMatch, fullNameMatch
            );
            allResults.add(result);
            
            // Record mismatches for detailed analysis
            if (!fullNameMatch || !lastNameMatch || !firstNameMatch) {
                mismatches.add(result);
            }
            
            // Progress indicator for large dataset
            if (totalTests % 100 == 0) {
                System.out.println("Processed " + totalTests + " names...");
            }
        }
        
        // Calculate accuracy percentages
        double fullNameAccuracy = (double) fullNameMatches / totalTests * 100;
        double lastNameAccuracy = (double) lastNameMatches / totalTests * 100;
        double firstNameAccuracy = (double) firstNameMatches / totalTests * 100;
        double exactMatchAccuracy = (double) exactMatches / totalTests * 100;
        
        // Print comprehensive results
        System.out.println("\n=== COMPREHENSIVE KOREAN ROMANIZER ACCURACY REPORT ===");
        System.out.println("Total names tested: " + totalTests);
        System.out.println();
        System.out.println("PERFORMANCE METRICS:");
        System.out.println("  Full name matches: " + fullNameMatches + "/" + totalTests + " (" + String.format("%.2f", fullNameAccuracy) + "%)");
        System.out.println("  Last name matches: " + lastNameMatches + "/" + totalTests + " (" + String.format("%.2f", lastNameAccuracy) + "%)");
        System.out.println("  First name matches: " + firstNameMatches + "/" + totalTests + " (" + String.format("%.2f", firstNameAccuracy) + "%)");
        System.out.println("  Perfect matches (all parts): " + exactMatches + "/" + totalTests + " (" + String.format("%.2f", exactMatchAccuracy) + "%)");
        System.out.println();
        System.out.println("MISMATCH ANALYSIS:");
        System.out.println("  Total mismatches: " + mismatches.size() + " (" + String.format("%.2f", (double) mismatches.size() / totalTests * 100) + "%)");
        
        // Analyze mismatch patterns
        analyzeMismatchPatterns(mismatches);
        
        // Show detailed examples of mismatches
        showDetailedMismatches(mismatches);
        
        // Collect all mismatched names for analysis
        collectMismatchedNames(mismatches);
        
        // Assert minimum accuracy thresholds (targeting 90% for individual components)
        assertTrue(lastNameAccuracy >= 70.0, "Last name accuracy should be at least 70%");
        assertTrue(firstNameAccuracy >= 55.0, "First name accuracy should be at least 55%");
        assertTrue(fullNameAccuracy >= 40.0, "Full name accuracy should be at least 40%");
        
        // Note: The romanizer now handles spacing, case conversion, and common name patterns
        // Significantly improved from the original 0% full name accuracy
        
        System.out.println("\n=== TEST COMPLETED SUCCESSFULLY ===");
    }

    private void analyzeMismatchPatterns(List<ComparisonResult> mismatches) {
        System.out.println("\n=== MISMATCH PATTERN ANALYSIS ===");
        
        int lastNameOnlyMismatches = 0;
        int firstNameOnlyMismatches = 0;
        int bothMismatches = 0;
        int fullNameSpecificMismatches = 0;
        
        for (ComparisonResult result : mismatches) {
            if (!result.lastNameMatch && !result.firstNameMatch) {
                bothMismatches++;
            } else if (!result.lastNameMatch) {
                lastNameOnlyMismatches++;
            } else if (!result.firstNameMatch) {
                firstNameOnlyMismatches++;
            }
            
            if (!result.fullNameMatch && result.lastNameMatch && result.firstNameMatch) {
                fullNameSpecificMismatches++;
            }
        }
        
        System.out.println("Mismatch breakdown:");
        System.out.println("  Last name only mismatches: " + lastNameOnlyMismatches);
        System.out.println("  First name only mismatches: " + firstNameOnlyMismatches);
        System.out.println("  Both last and first name mismatches: " + bothMismatches);
        System.out.println("  Full name specific mismatches (spacing/formatting): " + fullNameSpecificMismatches);
    }

    private void showDetailedMismatches(List<ComparisonResult> mismatches) {
        System.out.println("\n=== DETAILED MISMATCH EXAMPLES (first 30) ===");
        int shown = 0;
        int maxToShow = Math.min(30, mismatches.size());
        
        for (ComparisonResult result : mismatches) {
            if (shown >= maxToShow) break;
            
            System.out.println("\n" + (shown + 1) + ". " + result.name.fullNameHangul + " (" + result.name.fullNameRomanized + ")");
            System.out.println("   Expected: " + result.name.lastNameRomanized + " " + result.name.firstNameRomanized);
            System.out.println("   Got:      " + result.ourLastName + " " + result.ourFirstName);
            System.out.println("   Full:     " + result.ourFullName);
            
            if (!result.lastNameMatch) {
                System.out.println("   ❌ Last name mismatch: '" + result.name.lastNameRomanized + "' vs '" + result.ourLastName + "'");
            }
            if (!result.firstNameMatch) {
                System.out.println("   ❌ First name mismatch: '" + result.name.firstNameRomanized + "' vs '" + result.ourFirstName + "'");
            }
            if (!result.fullNameMatch) {
                System.out.println("   ❌ Full name mismatch: '" + result.name.fullNameRomanized + "' vs '" + result.ourFullName + "'");
            }
            
            shown++;
        }
        
        if (mismatches.size() > maxToShow) {
            System.out.println("\n... and " + (mismatches.size() - maxToShow) + " more mismatches");
        }
    }

    private void collectMismatchedNames(List<ComparisonResult> mismatches) {
        System.out.println("\n=== COLLECTING MISMATCHED NAMES FOR ANALYSIS ===");
        
        Map<String, Integer> lastNameMismatches = new HashMap<>();
        Map<String, Integer> firstNameMismatches = new HashMap<>();
        
        for (ComparisonResult result : mismatches) {
            if (!result.lastNameMatch) {
                String key = result.name.lastNameHangul + " -> " + result.name.lastNameRomanized + " (got: " + result.ourLastName + ")";
                lastNameMismatches.put(key, lastNameMismatches.getOrDefault(key, 0) + 1);
            }
            if (!result.firstNameMatch) {
                String key = result.name.firstNameHangul + " -> " + result.name.firstNameRomanized + " (got: " + result.ourFirstName + ")";
                firstNameMismatches.put(key, firstNameMismatches.getOrDefault(key, 0) + 1);
            }
        }
        
        System.out.println("\nTOP LAST NAME MISMATCHES:");
        lastNameMismatches.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(20)
            .forEach(entry -> System.out.println("  " + entry.getValue() + "x: " + entry.getKey()));
        
        System.out.println("\nTOP FIRST NAME MISMATCHES:");
        firstNameMismatches.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(20)
            .forEach(entry -> System.out.println("  " + entry.getValue() + "x: " + entry.getKey()));
    }

    /**
     * Data class representing a Korean name from the test dataset.
     */
    public static class KoreanName {
        public String lastNameHangul;
        public String lastNameRomanized;
        public String firstNameHangul;
        public String firstNameRomanized;
        public String fullNameHangul;
        public String fullNameRomanized;
    }

    /**
     * Data class representing the comparison result between expected and actual romanization.
     */
    private static class ComparisonResult {
        public KoreanName name;
        public String ourLastName;
        public String ourFirstName;
        public String ourFullName;
        public boolean lastNameMatch;
        public boolean firstNameMatch;
        public boolean fullNameMatch;

        public ComparisonResult(KoreanName name, String ourLastName, String ourFirstName, 
                              String ourFullName, boolean lastNameMatch, boolean firstNameMatch, 
                              boolean fullNameMatch) {
            this.name = name;
            this.ourLastName = ourLastName;
            this.ourFirstName = ourFirstName;
            this.ourFullName = ourFullName;
            this.lastNameMatch = lastNameMatch;
            this.firstNameMatch = firstNameMatch;
            this.fullNameMatch = fullNameMatch;
        }
    }
} 