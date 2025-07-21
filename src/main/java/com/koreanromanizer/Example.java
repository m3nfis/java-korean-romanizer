package com.koreanromanizer;

/**
 * Example usage of the Korean Romanizer library.
 */
public class Example {
    public static void main(String[] args) {
        // Basic examples
        System.out.println("Basic romanization examples:");
        System.out.println("안녕하세요 -> " + Romanizer.romanize("안녕하세요"));
        System.out.println("한국어 -> " + Romanizer.romanize("한국어"));
        System.out.println("아이유 방탄소년단 -> " + Romanizer.romanize("아이유 방탄소년단"));
        
        System.out.println("\nComplex pronunciation rules:");
        System.out.println("밝다 -> " + Romanizer.romanize("밝다"));
        System.out.println("없다 -> " + Romanizer.romanize("없다"));
        System.out.println("좋아하고 -> " + Romanizer.romanize("좋아하고"));
        System.out.println("읊다 -> " + Romanizer.romanize("읊다"));
        
        System.out.println("\nDouble consonant finals:");
        System.out.println("했었어요 -> " + Romanizer.romanize("했었어요"));
        System.out.println("없었다 -> " + Romanizer.romanize("없었다"));
        System.out.println("앉아봐 -> " + Romanizer.romanize("앉아봐"));
        
        System.out.println("\nIndividual jamo characters:");
        System.out.println("ㅠㄴㅁㄱ -> " + Romanizer.romanize("ㅠㄴㅁㄱ"));
        System.out.println("ㅠ동 -> " + Romanizer.romanize("ㅠ동"));
    }
} 