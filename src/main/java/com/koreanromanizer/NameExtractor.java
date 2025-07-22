package com.koreanromanizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to extract Korean names from various file formats in the test_strings directory.
 * This serves as the unified source of truth for comprehensive testing.
 */
public class NameExtractor {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Represents a Korean name with its romanization
     */
    public static class KoreanName {
        private final String hangul;
        private final String romanized;
        private final String source;
        
        public KoreanName(String hangul, String romanized, String source) {
            this.hangul = hangul;
            this.romanized = romanized;
            this.source = source;
        }
        
        public String getHangul() { return hangul; }
        public String getRomanized() { return romanized; }
        public String getSource() { return source; }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - %s", hangul, romanized, source);
        }
    }
    
    /**
     * Extract all Korean names from all files in the test_strings directory
     */
    public static List<KoreanName> extractAllNames() {
        List<KoreanName> allNames = new ArrayList<>();
        
        try {
            // Extract from JSON files
            allNames.addAll(extractFromJsonFiles());
            
            // Extract from HTML files
            allNames.addAll(extractFromHtmlFiles());
            
            // Extract from text files
            allNames.addAll(extractFromTextFiles());
            
            // Remove duplicates and sort
            allNames = removeDuplicates(allNames);
            
            System.out.printf("Extracted %d unique Korean names from all sources%n", allNames.size());
            
        } catch (Exception e) {
            System.err.println("Error extracting names: " + e.getMessage());
            e.printStackTrace();
        }
        
        return allNames;
    }
    
    private static List<KoreanName> extractFromJsonFiles() {
        List<KoreanName> names = new ArrayList<>();
        
        try {
            // Extract from korean_names_1000.json
            String jsonContent = Files.readString(Paths.get("test_strings/korean_names_1000.json"));
            JsonNode root = objectMapper.readTree(jsonContent);
            
            if (root.isArray()) {
                for (JsonNode node : root) {
                    if (node.has("fullNameHangul") && node.has("fullNameRomanized")) {
                        String hangul = node.get("fullNameHangul").asText();
                        String romanized = node.get("fullNameRomanized").asText();
                        names.add(new KoreanName(hangul, romanized, "korean_names_1000.json"));
                    }
                }
            }
            
            System.out.printf("Extracted %d names from JSON files%n", names.size());
            
        } catch (Exception e) {
            System.err.println("Error extracting from JSON: " + e.getMessage());
        }
        
        return names;
    }
    
    private static List<KoreanName> extractFromHtmlFiles() {
        List<KoreanName> names = new ArrayList<>();
        
        try {
            // Extract from examples_korean_last_names_300.html
            String lastNameContent = Files.readString(Paths.get("test_strings/examples_korean_last_names_300.html"));
            names.addAll(extractLastNamesFromHtml(lastNameContent, "examples_korean_last_names_300.html"));
            
            // Extract from examples_korean_first_names_1000.html
            String firstNameContent = Files.readString(Paths.get("test_strings/examples_korean_first_names_1000.html"));
            names.addAll(extractFirstNamesFromHtml(firstNameContent, "examples_korean_first_names_1000.html"));
            
            System.out.printf("Extracted %d names from HTML files%n", names.size());
            
        } catch (Exception e) {
            System.err.println("Error extracting from HTML: " + e.getMessage());
        }
        
        return names;
    }
    
    private static List<KoreanName> extractFromTextFiles() {
        List<KoreanName> names = new ArrayList<>();
        
        try {
            // Extract from List of Korean surnames.txt
            String surnameContent = Files.readString(Paths.get("test_strings/List of Korean surnames.txt"));
            names.addAll(extractSurnamesFromText(surnameContent, "List of Korean surnames.txt"));
            
            // Extract from List of Korean given names.txt
            String givenNameContent = Files.readString(Paths.get("test_strings/List of Korean given names.txt"));
            names.addAll(extractGivenNamesFromText(givenNameContent, "List of Korean given names.txt"));
            
            // Extract from name_examples.txt
            String examplesContent = Files.readString(Paths.get("test_strings/name_examples.txt"));
            names.addAll(extractExamplesFromText(examplesContent, "name_examples.txt"));
            
            System.out.printf("Extracted %d names from text files%n", names.size());
            
        } catch (Exception e) {
            System.err.println("Error extracting from text: " + e.getMessage());
        }
        
        return names;
    }
    
    private static List<KoreanName> extractLastNamesFromHtml(String content, String source) {
        List<KoreanName> names = new ArrayList<>();
        
        // Pattern to match: <strong>Kim</strong> – Gold  – 金
        Pattern pattern = Pattern.compile("<strong>([^<]+)</strong>\\s*–\\s*([^–]+)\\s*–\\s*([^\\s]+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String romanized = matcher.group(1).trim();
            String meaning = matcher.group(2).trim();
            String hanja = matcher.group(3).trim();
            
            // For surnames, we need to find the corresponding Hangul
            // This is a simplified approach - in practice, you might need a mapping
            if (romanized.equals("Kim")) names.add(new KoreanName("김", romanized, source));
            else if (romanized.equals("Lee")) names.add(new KoreanName("이", romanized, source));
            else if (romanized.equals("Park")) names.add(new KoreanName("박", romanized, source));
            else if (romanized.equals("Jeong")) names.add(new KoreanName("정", romanized, source));
            else if (romanized.equals("Choi")) names.add(new KoreanName("최", romanized, source));
            else if (romanized.equals("Cho")) names.add(new KoreanName("조", romanized, source));
            else if (romanized.equals("Kang")) names.add(new KoreanName("강", romanized, source));
            else if (romanized.equals("Yoon")) names.add(new KoreanName("윤", romanized, source));
            else if (romanized.equals("Jang")) names.add(new KoreanName("장", romanized, source));
            else if (romanized.equals("Lim")) names.add(new KoreanName("임", romanized, source));
            // Add more mappings as needed
        }
        
        return names;
    }
    
    private static List<KoreanName> extractFirstNamesFromHtml(String content, String source) {
        List<KoreanName> names = new ArrayList<>();
        
        // Pattern to match: <strong>Ji-An</strong> – Wisdom and tranquility – 智安
        Pattern pattern = Pattern.compile("<strong>([^<]+)</strong>\\s*–\\s*([^–]+)\\s*–\\s*([^\\s]+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String romanized = matcher.group(1).trim();
            String meaning = matcher.group(2).trim();
            String hanja = matcher.group(3).trim();
            
            // For given names, we need to find the corresponding Hangul
            // This is a simplified approach - in practice, you might need a mapping
            if (romanized.equals("Ji-An")) names.add(new KoreanName("지안", romanized, source));
            else if (romanized.equals("Ha-Yoon")) names.add(new KoreanName("하윤", romanized, source));
            else if (romanized.equals("Seo-Ah")) names.add(new KoreanName("서아", romanized, source));
            else if (romanized.equals("Ha-Eun")) names.add(new KoreanName("하은", romanized, source));
            else if (romanized.equals("Seo-Yun")) names.add(new KoreanName("서윤", romanized, source));
            else if (romanized.equals("Ha-Rin")) names.add(new KoreanName("하린", romanized, source));
            else if (romanized.equals("Ji-Yoo")) names.add(new KoreanName("지유", romanized, source));
            else if (romanized.equals("Ji-Woo")) names.add(new KoreanName("지우", romanized, source));
            else if (romanized.equals("Soo-Ah")) names.add(new KoreanName("수아", romanized, source));
            else if (romanized.equals("Ji-a")) names.add(new KoreanName("지아", romanized, source));
            // Add more mappings as needed
        }
        
        return names;
    }
    
    private static List<KoreanName> extractSurnamesFromText(String content, String source) {
        List<KoreanName> names = new ArrayList<>();
        
        // Pattern to match: 김	金, 钅 [sic]	Gim	Kim	Ghim,[3] Kin[3]	10,689,967	21.5065%
        // Updated pattern to handle the tab-separated format better
        Pattern pattern = Pattern.compile("([가-힣]+)\\s+([^\\t]+)\\s+([^\\t]+)\\s+([^\\t]+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String hangul = matcher.group(1).trim();
            String hanja = matcher.group(2).trim();
            String rr = matcher.group(3).trim();
            String mr = matcher.group(4).trim();
            
            // Use RR (Revised Romanization) as the standard
            names.add(new KoreanName(hangul, rr, source));
        }
        
        return names;
    }
    
    private static List<KoreanName> extractGivenNamesFromText(String content, String source) {
        List<KoreanName> names = new ArrayList<>();
        
        // Pattern to match: Ga-young (가영)
        Pattern pattern = Pattern.compile("([A-Za-z-]+)\\s*\\(([가-힣]+)\\)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String romanized = matcher.group(1).trim();
            String hangul = matcher.group(2).trim();
            
            names.add(new KoreanName(hangul, romanized, source));
        }
        
        return names;
    }
    
    private static List<KoreanName> extractExamplesFromText(String content, String source) {
        List<KoreanName> names = new ArrayList<>();
        
        // Pattern to match: Jimin (지민) – Popularized by BTS's Park Jimin
        Pattern pattern = Pattern.compile("([A-Za-z]+)\\s*\\(([가-힣]+)\\)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String romanized = matcher.group(1).trim();
            String hangul = matcher.group(2).trim();
            
            names.add(new KoreanName(hangul, romanized, source));
        }
        
        return names;
    }
    
    private static List<KoreanName> removeDuplicates(List<KoreanName> names) {
        Map<String, KoreanName> uniqueNames = new LinkedHashMap<>();
        
        for (KoreanName name : names) {
            String key = name.getHangul() + "|" + name.getRomanized();
            if (!uniqueNames.containsKey(key)) {
                uniqueNames.put(key, name);
            }
        }
        
        return new ArrayList<>(uniqueNames.values());
    }
    
    /**
     * Main method for testing the extractor
     */
    public static void main(String[] args) {
        List<KoreanName> allNames = extractAllNames();
        
        System.out.println("\n=== EXTRACTED NAMES SAMPLE ===");
        for (int i = 0; i < Math.min(20, allNames.size()); i++) {
            System.out.println(allNames.get(i));
        }
        
        System.out.printf("\nTotal unique names extracted: %d%n", allNames.size());
    }
} 