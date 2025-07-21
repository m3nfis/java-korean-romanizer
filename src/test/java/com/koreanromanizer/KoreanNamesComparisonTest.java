package com.koreanromanizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class that compares our Korean Romanizer against a dataset of Korean names
 * and their accepted transliterations.
 */
public class KoreanNamesComparisonTest {

    private static List<KoreanName> koreanNames;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void loadTestData() {
        try (InputStream inputStream = KoreanNamesComparisonTest.class
                .getResourceAsStream("/korean_names.KO_test.json")) {
            
            if (inputStream == null) {
                throw new RuntimeException("Could not find korean_names.KO_test.json in test resources");
            }
            
            koreanNames = objectMapper.readValue(inputStream, new TypeReference<List<KoreanName>>() {});
            System.out.println("Loaded " + koreanNames.size() + " Korean names for testing");
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    @Test
    public void testKoreanNamesAccuracy() {
        int totalTests = 0;
        int exactMatches = 0;
        int lastNameMatches = 0;
        int firstNameMatches = 0;
        
        List<ComparisonResult> mismatches = new ArrayList<>();
        
        for (KoreanName name : koreanNames) {
            totalTests++;
            
            // Test full name
            String ourFullName = KoreanRomanizer.romanize(name.fullNameHangul);
            boolean fullNameMatch = ourFullName.equals(name.fullNameRomamized);
            
            // Test last name
            String ourLastName = KoreanRomanizer.romanize(name.lastNameHangul);
            boolean lastNameMatch = ourLastName.equals(name.lastNameRomamized);
            
            // Test first name  
            String ourFirstName = KoreanRomanizer.romanize(name.firstNameHangul);
            boolean firstNameMatch = ourFirstName.equals(name.firstNameRomamized);
            
            if (fullNameMatch) {
                exactMatches++;
            }
            if (lastNameMatch) {
                lastNameMatches++;
            }
            if (firstNameMatch) {
                firstNameMatches++;
            }
            
            // Record mismatches for analysis
            if (!fullNameMatch || !lastNameMatch || !firstNameMatch) {
                mismatches.add(new ComparisonResult(
                    name, ourLastName, ourFirstName, ourFullName,
                    lastNameMatch, firstNameMatch, fullNameMatch
                ));
            }
        }
        
        // Calculate accuracy percentages
        double fullNameAccuracy = (double) exactMatches / totalTests * 100;
        double lastNameAccuracy = (double) lastNameMatches / totalTests * 100;
        double firstNameAccuracy = (double) firstNameMatches / totalTests * 100;
        
        // Print detailed results
        System.out.println("\n=== KOREAN ROMANIZER ACCURACY REPORT ===");
        System.out.println("Total names tested: " + totalTests);
        System.out.println();
        System.out.println("Full name exact matches: " + exactMatches + " (" + String.format("%.2f", fullNameAccuracy) + "%)");
        System.out.println("Last name matches: " + lastNameMatches + " (" + String.format("%.2f", lastNameAccuracy) + "%)");
        System.out.println("First name matches: " + firstNameMatches + " (" + String.format("%.2f", firstNameAccuracy) + "%)");
        System.out.println();
        
        // Show first 20 mismatches for analysis
        System.out.println("=== ANALYSIS OF MISMATCHES (first 20) ===");
        int shown = 0;
        for (ComparisonResult result : mismatches) {
            if (shown >= 20) break;
            
            System.out.println("Korean: " + result.name.fullNameHangul + 
                             " | Expected: " + result.name.fullNameRomamized + 
                             " | Our result: " + result.ourFullName);
            
            if (!result.lastNameMatch) {
                System.out.println("  Last name: " + result.name.lastNameHangul + 
                                 " → Expected: " + result.name.lastNameRomamized + 
                                 " | Ours: " + result.ourLastName);
            }
            if (!result.firstNameMatch) {
                System.out.println("  First name: " + result.name.firstNameHangul + 
                                 " → Expected: " + result.name.firstNameRomamized + 
                                 " | Ours: " + result.ourFirstName);
            }
            System.out.println();
            shown++;
        }
        
        // Analyze common patterns in mismatches
        System.out.println("=== COMMON MISMATCH PATTERNS ===");
        analyzeMismatchPatterns(mismatches);
        
        // Don't fail the test, just report results
        System.out.println("Test completed. Results logged above.");
    }
    
    private void analyzeMismatchPatterns(List<ComparisonResult> mismatches) {
        // Count hyphenation differences
        int hyphenationDiffs = 0;
        int spacingDiffs = 0;
        int transcriptionDiffs = 0;
        
        for (ComparisonResult result : mismatches) {
            String expected = result.name.fullNameRomamized.toLowerCase();
            String ours = result.ourFullName.toLowerCase();
            
            // Check if difference is just hyphenation
            if (expected.replace("-", "").equals(ours.replace("-", "")) &&
                expected.replace(" ", "").equals(ours.replace(" ", ""))) {
                hyphenationDiffs++;
            }
            // Check if difference is just spacing
            else if (expected.replace(" ", "").equals(ours.replace(" ", ""))) {
                spacingDiffs++;
            }
            // Otherwise it's a transcription difference
            else {
                transcriptionDiffs++;
            }
        }
        
        System.out.println("Hyphenation/formatting differences: " + hyphenationDiffs);
        System.out.println("Spacing differences: " + spacingDiffs);
        System.out.println("Actual transcription differences: " + transcriptionDiffs);
        System.out.println("Total mismatches: " + mismatches.size());
    }

    /**
     * Data class representing a Korean name entry from the test dataset.
     */
    public static class KoreanName {
        public String lastNameHangul;
        public String lastNameRomamized;
        public String firstNameHangul;
        public String firstNameRomamized;
        public String fullNameHangul;
        public String fullNameRomamized;
    }
    
    /**
     * Data class for storing comparison results.
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