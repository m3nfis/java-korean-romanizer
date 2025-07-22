package com.koreanromanizer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused test to achieve 90% first name accuracy using the original 1000 names dataset.
 * This test uses the validated romanizations from the original JSON file.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FocusedAccuracyTest {
    
    private List<NameExtractor.KoreanName> testNames;
    
    @BeforeAll
    void loadTestData() {
        System.out.println("=== LOADING ORIGINAL 1000 NAMES DATASET ===");
        testNames = NameExtractor.extractAllNames();
        
        // Filter to only use names from the original JSON file (which has validated romanizations)
        testNames = testNames.stream()
            .filter(name -> name.getSource().contains("korean_names_1000.json"))
            .collect(java.util.stream.Collectors.toList());
        
        System.out.printf("Loaded %d Korean names from original dataset%n", testNames.size());
    }
    
    @Test
    void testFirstNameAccuracy90Percent() {
        System.out.println("=== FOCUSED FIRST NAME ACCURACY TEST ===");
        System.out.printf("Testing %d Korean names for 90%% first name accuracy...%n", testNames.size());
        
        KoreanRomanizer romanizer = new KoreanRomanizer("");
        
        int totalNames = testNames.size();
        int firstNameMatches = 0;
        int lastNameMatches = 0;
        int fullNameMatches = 0;
        
        Map<String, Integer> mismatchPatterns = new TreeMap<>();
        
        for (int i = 0; i < testNames.size(); i++) {
            NameExtractor.KoreanName testName = testNames.get(i);
            
            if (i % 100 == 0) {
                System.out.printf("Processed %d names...%n", i);
            }
            
            String fullNameResult = romanizer.romanizeFullName(testName.getHangul());
            String expectedRomanized = testName.getRomanized();
            
            // Normalize for comparison (remove extra spaces, convert to lowercase)
            String normalizedExpected = expectedRomanized.toLowerCase().replaceAll("\\s+", " ").trim();
            String normalizedResult = fullNameResult.toLowerCase().replaceAll("\\s+", " ").trim();
            
            // Check matches
            boolean fullNameMatch = fullNameResult.equals(testName.getRomanized());
            boolean normalizedMatch = normalizedExpected.equals(normalizedResult);
            
            // Split into parts for detailed analysis
            String[] expectedParts = testName.getRomanized().split("\\s+", 2);
            String[] resultParts = fullNameResult.split("\\s+", 2);
            
            boolean lastNameMatch = false;
            boolean firstNameMatch = false;
            
            if (expectedParts.length == 1) {
                // Single name (could be surname or given name)
                if (resultParts.length == 1) {
                    lastNameMatch = expectedParts[0].equals(resultParts[0]);
                    firstNameMatch = expectedParts[0].equals(resultParts[0]);
                }
            } else {
                // Full name with surname and given name
                if (resultParts.length >= 2) {
                    lastNameMatch = expectedParts[0].equals(resultParts[0]);
                    firstNameMatch = expectedParts[1].equals(resultParts[1]);
                }
            }
            
            if (lastNameMatch) lastNameMatches++;
            if (firstNameMatch) firstNameMatches++;
            if (fullNameMatch) fullNameMatches++;
            
            // Track mismatches for analysis
            if (!firstNameMatch) {
                String pattern = String.format("%s -> %s (expected: %s)", 
                    testName.getHangul(), 
                    resultParts.length > 1 ? resultParts[1] : fullNameResult,
                    expectedParts.length > 1 ? expectedParts[1] : expectedRomanized);
                mismatchPatterns.merge(pattern, 1, Integer::sum);
            }
        }
        
        // Calculate accuracies
        double firstNameAccuracy = (double) firstNameMatches / totalNames * 100;
        double lastNameAccuracy = (double) lastNameMatches / totalNames * 100;
        double fullNameAccuracy = (double) fullNameMatches / totalNames * 100;
        
        // Print results
        System.out.println("\n=== FOCUSED ACCURACY RESULTS ===");
        System.out.printf("Total names tested: %d%n%n", totalNames);
        System.out.println("PERFORMANCE METRICS:");
        System.out.printf("  First name matches: %d/%d (%.2f%%)%n", firstNameMatches, totalNames, firstNameAccuracy);
        System.out.printf("  Last name matches: %d/%d (%.2f%%)%n", lastNameMatches, totalNames, lastNameAccuracy);
        System.out.printf("  Full name matches: %d/%d (%.2f%%)%n", fullNameMatches, totalNames, fullNameAccuracy);
        
        if (firstNameMatches < totalNames) {
            System.out.println("\nTOP FIRST NAME MISMATCHES (first 20):");
            mismatchPatterns.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .forEach(entry -> System.out.printf("  %dx: %s%n", entry.getValue(), entry.getKey()));
        }
        
        // Assertions
        assertTrue(firstNameAccuracy >= 90.0, 
            String.format("First name accuracy should be at least 90%% (got %.2f%%)", firstNameAccuracy));
        assertTrue(lastNameAccuracy >= 70.0, 
            String.format("Last name accuracy should be at least 70%% (got %.2f%%)", lastNameAccuracy));
        
        System.out.println("\n=== TEST COMPLETED SUCCESSFULLY ===");
    }
} 