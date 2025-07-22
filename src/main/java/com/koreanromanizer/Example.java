package com.koreanromanizer;

/**
 * Example usage of the Korean Romanizer library.
 */
public class Example {
    public static void main(String[] args) {
        System.out.println("=== Basic Korean Romanization ===");
        System.out.println("안녕하세요 -> " + KoreanRomanizer.romanize("안녕하세요"));
        System.out.println("한국어 -> " + KoreanRomanizer.romanize("한국어"));
        System.out.println("아이유 방탄소년단 -> " + KoreanRomanizer.romanize("아이유 방탄소년단"));
        
        System.out.println("\n=== Official Government Rules Examples ===");
        System.out.println("Position-dependent consonant transcription:");
        System.out.println("구미 (Gumi) -> " + KoreanRomanizer.romanize("구미"));
        System.out.println("영동 (Yeongdong) -> " + KoreanRomanizer.romanize("영동"));
        System.out.println("백암 (Baegam) -> " + KoreanRomanizer.romanize("백암"));
        System.out.println("옥천 (Okcheon) -> " + KoreanRomanizer.romanize("옥천"));
        System.out.println("합덕 (Hapdeok) -> " + KoreanRomanizer.romanize("합덕"));
        
        System.out.println("\n=== ㄹ Transcription Rules ===");
        System.out.println("구리 (Guri) -> " + KoreanRomanizer.romanize("구리"));
        System.out.println("설악 (Seorak) -> " + KoreanRomanizer.romanize("설악"));
        System.out.println("칠곡 (Chilgok) -> " + KoreanRomanizer.romanize("칠곡"));
        System.out.println("임실 (Imsil) -> " + KoreanRomanizer.romanize("임실"));
        System.out.println("울릉 (Ulleung) -> " + KoreanRomanizer.romanize("울릉"));
        
        System.out.println("\n=== Complex Pronunciation Rules ===");
        System.out.println("밝다 -> " + KoreanRomanizer.romanize("밝다"));
        System.out.println("없다 -> " + KoreanRomanizer.romanize("없다"));
        System.out.println("좋아하고 -> " + KoreanRomanizer.romanize("좋아하고"));
        System.out.println("읊다 -> " + KoreanRomanizer.romanize("읊다"));
        
        System.out.println("\n=== Assimilation Examples ===");
        System.out.println("했었어요 -> " + KoreanRomanizer.romanize("했었어요"));
        System.out.println("없었다 -> " + KoreanRomanizer.romanize("없었다"));
        System.out.println("앉아봐 -> " + KoreanRomanizer.romanize("앉아봐"));
        
        System.out.println("\n=== Individual Jamo Characters ===");
        System.out.println("ㅠㄴㅁㄱ -> " + KoreanRomanizer.romanize("ㅠㄴㅁㄱ"));
        System.out.println("ㅠ동 -> " + KoreanRomanizer.romanize("ㅠ동"));
        
        System.out.println("\n=== Korean Names with Official Standards ===");
        System.out.println("김민준 -> " + KoreanRomanizer.romanizeName("김민준"));
        System.out.println("이하윤 -> " + KoreanRomanizer.romanizeName("이하윤"));
        System.out.println("박도윤 -> " + KoreanRomanizer.romanizeName("박도윤"));
        System.out.println("최서아 -> " + KoreanRomanizer.romanizeName("최서아"));
        System.out.println("정은우 -> " + KoreanRomanizer.romanizeName("정은우"));
    }
} 