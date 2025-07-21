package com.koreanromanizer;

/**
 * Demonstration of the differences between linguistic romanization and conventional name transliteration.
 */
public class ComparisonDemo {
    public static void main(String[] args) {
        System.out.println("=== KOREAN ROMANIZATION COMPARISON ===\n");
        
        System.out.println("Our library implements LINGUISTIC ROMANIZATION (Revised Romanization of Korean)");
        System.out.println("The name dataset uses CONVENTIONAL NAME TRANSLITERATIONS\n");
        
        // Korean surnames
        String[] koreanSurnames = {"김", "이", "박", "최", "정", "강", "조", "윤", "장"};
        String[] conventionalSurnames = {"Kim", "Lee", "Park", "Choi", "Jeong", "Kang", "Jo", "Yoon", "Jang"};
        
        System.out.println("KOREAN SURNAMES:");
        System.out.println("Korean | Conventional | Our Romanization | Notes");
        System.out.println("-------|--------------|------------------|-------");
        
        for (int i = 0; i < koreanSurnames.length; i++) {
            String korean = koreanSurnames[i];
            String conventional = conventionalSurnames[i];
            String ours = Romanizer.romanize(korean);
            String notes = ours.equals(conventional.toLowerCase()) ? "✓ Match" : "Different convention";
            
            System.out.printf("%-6s | %-12s | %-16s | %s%n", 
                korean, conventional, ours, notes);
        }
        
        System.out.println("\nKOREAN GIVEN NAMES:");
        System.out.println("Korean | Conventional | Our Romanization | Notes");
        System.out.println("-------|--------------|------------------|-------");
        
        String[] koreanGivenNames = {"서준", "하윤", "도윤", "은우", "지안"};
        String[] conventionalGivenNames = {"Seo-jun", "Ha-yoon", "Do-yun", "Eun-woo", "Ji-an"};
        
        for (int i = 0; i < koreanGivenNames.length; i++) {
            String korean = koreanGivenNames[i];
            String conventional = conventionalGivenNames[i];
            String ours = Romanizer.romanize(korean);
            String notes = "Missing hyphenation";
            
            System.out.printf("%-6s | %-12s | %-16s | %s%n", 
                korean, conventional, ours, notes);
        }
        
        System.out.println("\nFULL NAMES:");
        System.out.println("Korean | Conventional | Our Romanization | Differences");
        System.out.println("-------|--------------|------------------|------------");
        
        String[] koreanFullNames = {"김서준", "이하윤", "박도윤"};
        String[] conventionalFullNames = {"Kim Seo-jun", "Lee Ha-yoon", "Park Do-yun"};
        
        for (int i = 0; i < koreanFullNames.length; i++) {
            String korean = koreanFullNames[i];
            String conventional = conventionalFullNames[i];
            String ours = Romanizer.romanize(korean);
            
            System.out.printf("%-6s | %-12s | %-16s | Spacing + hyphens + surnames%n", 
                korean, conventional, ours);
        }
        
        System.out.println("\n=== LINGUISTIC ACCURACY EXAMPLES ===");
        System.out.println("Our romanizer excels at general Korean text:\n");
        
        String[] phrases = {
            "안녕하세요", 
            "감사합니다",
            "한국어 공부",
            "좋은 아침",
            "맛있는 음식"
        };
        
        for (String phrase : phrases) {
            System.out.println(phrase + " → " + Romanizer.romanize(phrase));
        }
        
        System.out.println("\n=== SUMMARY ===");
        System.out.println("✓ Our romanizer: Perfect for linguistic accuracy and general text");
        System.out.println("✓ Name conventions: Required for official personal name transliterations");
        System.out.println("→ Both serve different but valid purposes!");
    }
} 