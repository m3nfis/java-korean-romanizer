package com.koreanromanizer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Main class for romanizing Korean text following the Revised Romanization of Korean rules.
 */
public class Romanizer {
    private String text;

    // Vowel mappings
    private static final Map<String, String> VOWEL = new HashMap<>();
    static {
        // 단모음 monophthongs
        VOWEL.put("ㅏ", "a");
        VOWEL.put("ㅓ", "eo");
        VOWEL.put("ㅗ", "o");
        VOWEL.put("ㅜ", "u");
        VOWEL.put("ㅡ", "eu");
        VOWEL.put("ㅣ", "i");
        VOWEL.put("ㅐ", "ae");
        VOWEL.put("ㅔ", "e");
        VOWEL.put("ㅚ", "oe");
        VOWEL.put("ㅟ", "wi");
        
        // 이중모음 diphthongs
        VOWEL.put("ㅑ", "ya");
        VOWEL.put("ㅕ", "yeo");
        VOWEL.put("ㅛ", "yo");
        VOWEL.put("ㅠ", "yu");
        VOWEL.put("ㅒ", "yae");
        VOWEL.put("ㅖ", "ye");
        VOWEL.put("ㅘ", "wa");
        VOWEL.put("ㅙ", "wae");
        VOWEL.put("ㅝ", "wo");
        VOWEL.put("ㅞ", "we");
        VOWEL.put("ㅢ", "ui");
    }

    // 초성 Onset consonants
    private static final Map<String, String> ONSET = new HashMap<>();
    static {
        // 파열음 stops/plosives
        ONSET.put("ᄀ", "g");
        ONSET.put("ᄁ", "kk");
        ONSET.put("ᄏ", "k");
        ONSET.put("ᄃ", "d");
        ONSET.put("ᄄ", "tt");
        ONSET.put("ᄐ", "t");
        ONSET.put("ᄇ", "b");
        ONSET.put("ᄈ", "pp");
        ONSET.put("ᄑ", "p");
        // 파찰음 affricates
        ONSET.put("ᄌ", "j");
        ONSET.put("ᄍ", "jj");
        ONSET.put("ᄎ", "ch");
        // 마찰음 fricatives
        ONSET.put("ᄉ", "s");
        ONSET.put("ᄊ", "ss");
        ONSET.put("ᄒ", "h");
        // 비음 nasals
        ONSET.put("ᄂ", "n");
        ONSET.put("ᄆ", "m");
        // 유음 liquids
        ONSET.put("ᄅ", "r");
        // Null sound
        ONSET.put("ᄋ", "");
    }

    // 종성 Coda consonants
    private static final Map<String, String> CODA = new HashMap<>();
    static {
        // 파열음 stops/plosives
        CODA.put("ᆨ", "k");
        CODA.put("ᆮ", "t");
        CODA.put("ᆸ", "p");
        // 비음 nasals
        CODA.put("ᆫ", "n");
        CODA.put("ᆼ", "ng");
        CODA.put("ᆷ", "m");
        // 유음 liquids
        CODA.put("ᆯ", "l");
        CODA.put(null, "");
    }

    // Compatible onset mappings for single jamo characters
    private static final Map<String, String> COMPAT_ONSET = new HashMap<>();
    static {
        COMPAT_ONSET.put("ㄱ", "g");
        COMPAT_ONSET.put("ㄲ", "kk");
        COMPAT_ONSET.put("ㄴ", "n");
        COMPAT_ONSET.put("ㄷ", "d");
        COMPAT_ONSET.put("ㄸ", "tt");
        COMPAT_ONSET.put("ㄹ", "r");
        COMPAT_ONSET.put("ㅁ", "m");
        COMPAT_ONSET.put("ㅂ", "b");
        COMPAT_ONSET.put("ㅃ", "pp");
        COMPAT_ONSET.put("ㅅ", "s");
        COMPAT_ONSET.put("ㅆ", "ss");
        COMPAT_ONSET.put("ㅇ", "");
        COMPAT_ONSET.put("ㅈ", "j");
        COMPAT_ONSET.put("ㅉ", "jj");
        COMPAT_ONSET.put("ㅊ", "ch");
        COMPAT_ONSET.put("ㅋ", "k");
        COMPAT_ONSET.put("ㅌ", "t");
        COMPAT_ONSET.put("ㅍ", "p");
        COMPAT_ONSET.put("ㅎ", "h");
    }

    private static final Pattern HANGUL_PATTERN = Pattern.compile("[가-힣ㄱ-ㅣ]");

    /**
     * Creates a Romanizer for the given Korean text.
     * 
     * @param text The Korean text to romanize
     */
    public Romanizer(String text) {
        this.text = text;
    }

    /**
     * Romanizes the Korean text.
     * 
     * @return The romanized text
     */
    public String romanize() {
        Pronouncer pronouncer = new Pronouncer(this.text);
        String pronounced = pronouncer.getPronounced();
        
        StringBuilder romanized = new StringBuilder();
        
        for (char c : pronounced.toCharArray()) {
            String charStr = String.valueOf(c);
            
            if (HANGUL_PATTERN.matcher(charStr).matches()) {
                Syllable syllable = new Syllable(c);
                
                if (syllable.getMedial() == null && syllable.getFinal() == null) {
                    // Single jamo character (not a full syllable)
                    if (VOWEL.containsKey(charStr)) {
                        romanized.append(VOWEL.get(charStr));
                    } else if (ONSET.containsKey(charStr)) {
                        romanized.append(ONSET.get(charStr));
                    } else if (COMPAT_ONSET.containsKey(charStr)) {
                        romanized.append(COMPAT_ONSET.get(charStr));
                    } else {
                        romanized.append(charStr);
                    }
                } else {
                    // Full syllable
                    String initialRoman = ONSET.getOrDefault(syllable.getInitial(), "");
                    String medialRoman = VOWEL.getOrDefault(syllable.getMedial(), "");
                    String finalRoman = CODA.getOrDefault(syllable.getFinal(), "");
                    
                    romanized.append(initialRoman).append(medialRoman).append(finalRoman);
                }
            } else {
                // Non-Korean character
                romanized.append(charStr);
            }
        }
        
        return romanized.toString();
    }

    /**
     * Static method for convenient romanization.
     * 
     * @param text The Korean text to romanize
     * @return The romanized text
     */
    public static String romanize(String text) {
        return new Romanizer(text).romanize();
    }
} 