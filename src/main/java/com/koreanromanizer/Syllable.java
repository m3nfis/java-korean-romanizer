package com.koreanromanizer;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Korean syllable and provides methods for decomposing and constructing Korean characters.
 * Based on the Unicode Hangul syllable structure.
 */
public class Syllable {
    private char character;
    private String initial;
    private String medial;
    private String finalConsonant;

    // Unicode constants
    private static final int UNICODE_OFFSET = 44032;
    private static final int UNICODE_INITIAL_OFFSET = 588;
    private static final int UNICODE_MEDIAL_OFFSET = 28;

    // Unicode initial consonants (choseong)
    private static final List<String> UNICODE_INITIAL = Arrays.asList(
        "ᄀ", "ᄁ", "ᄂ", "ᄃ", "ᄄ", "ᄅ", "ᄆ", "ᄇ", "ᄈ", "ᄉ", "ᄊ", "ᄋ", "ᄌ", "ᄍ", "ᄎ", "ᄏ", "ᄐ", "ᄑ", "ᄒ"
    );

    // Unicode medial vowels (jungseong)
    private static final List<String> UNICODE_MEDIAL = Arrays.asList(
        "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"
    );

    // Unicode final consonants (jongseong) - null represents no final consonant
    private static final List<String> UNICODE_FINAL = Arrays.asList(
        null, "ᆨ", "ᆩ", "ᆪ", "ᆫ", "ᆬ", "ᆭ", "ᆮ", "ᆯ", "ᆰ", "ᆱ", "ᆲ", "ᆳ", "ᆴ", "ᆵ", "ᆶ", "ᆷ", "ᆸ", "ᆹ", "ᆺ", "ᆻ", "ᆼ", "ᆽ", "ᆾ", "ᆿ", "ᇀ", "ᇁ", "ᇂ"
    );

    // Compatible consonants (single jamo characters)
    private static final List<String> UNICODE_COMPATIBLE_CONSONANTS = Arrays.asList(
        "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    );

    // Mapping from compatible finals to Unicode finals
    private static final List<String> UNICODE_COMPATIBLE_FINALS = Arrays.asList(
        "ᆨ", "ᆩ", "ᆫ", "ᆮ", "_", "ᆯ", "ᆷ", "ᆸ", "_", "ᆺ", "ᆻ", "ᆼ", "ᆽ", "_", "ᆾ", "ᆿ", "ᇀ", "ᇁ", "ᇂ"
    );

    /**
     * Constructs a Syllable object from a Korean character.
     * 
     * @param character The Korean character to decompose
     */
    public Syllable(char character) {
        this.character = character;
        SyllableDecomposition decomposition = separateSyllable(character);
        
        if (decomposition.isHangul) {
            this.initial = UNICODE_INITIAL.get(decomposition.initial);
            this.medial = UNICODE_MEDIAL.get(decomposition.medial);
            this.finalConsonant = UNICODE_FINAL.get(decomposition.finalIndex);
        } else {
            this.initial = String.valueOf((char) decomposition.initial);
            this.medial = null;
            this.finalConsonant = null;
        }
    }

    /**
     * Separates a character into its constituent parts.
     * 
     * @param character The character to separate
     * @return SyllableDecomposition containing the decomposed parts
     */
    private SyllableDecomposition separateSyllable(char character) {
        if (isHangul(character)) {
            int charCode = character - UNICODE_OFFSET;
            int initial = charCode / UNICODE_INITIAL_OFFSET;
            int medial = (charCode - UNICODE_INITIAL_OFFSET * initial) / UNICODE_MEDIAL_OFFSET;
            int finalIndex = charCode - UNICODE_INITIAL_OFFSET * initial - UNICODE_MEDIAL_OFFSET * medial;
            
            return new SyllableDecomposition(true, initial, medial, finalIndex);
        } else {
            return new SyllableDecomposition(false, (int) character, 0, 0);
        }
    }

    /**
     * Constructs a syllable from its constituent parts.
     * 
     * @param initial The initial consonant
     * @param medial The medial vowel
     * @param finalConsonant The final consonant (can be null)
     * @return The constructed character
     */
    public char constructSyllable(String initial, String medial, String finalConsonant) {
        if (isHangul(this.character)) {
            int initialIndex = UNICODE_INITIAL.indexOf(initial);
            int medialIndex = UNICODE_MEDIAL.indexOf(medial);
            int finalIndex = finalConsonant == null ? 0 : UNICODE_FINAL.indexOf(finalConsonant);
            
            int constructed = (initialIndex * UNICODE_INITIAL_OFFSET) + 
                             (medialIndex * UNICODE_MEDIAL_OFFSET) + 
                             finalIndex + UNICODE_OFFSET;
                             
            this.character = (char) constructed;
            return this.character;
        }
        return this.character;
    }

    /**
     * Checks if a character is a Hangul syllable.
     * 
     * @param character The character to check
     * @return true if the character is Hangul, false otherwise
     */
    private boolean isHangul(char character) {
        return character >= 0xAC00 && character <= 0xD7A3;
    }

    /**
     * Converts a final consonant to its corresponding initial consonant.
     * 
     * @param finalChar The final consonant character
     * @return The corresponding initial consonant
     */
    public String finalToInitial(String finalChar) {
        int index = UNICODE_COMPATIBLE_FINALS.indexOf(finalChar);
        if (index != -1 && index < UNICODE_INITIAL.size()) {
            return UNICODE_INITIAL.get(index);
        }
        return finalChar;
    }

    // Getters
    public char getCharacter() {
        return character;
    }

    public String getInitial() {
        return initial;
    }

    public String getMedial() {
        return medial;
    }

    public String getFinal() {
        return finalConsonant;
    }

    public void setFinal(String finalConsonant) {
        this.finalConsonant = finalConsonant;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    @Override
    public String toString() {
        constructSyllable(this.initial, this.medial, this.finalConsonant);
        return String.valueOf(this.character);
    }

    /**
     * Inner class to hold syllable decomposition results.
     */
    private static class SyllableDecomposition {
        boolean isHangul;
        int initial;
        int medial;
        int finalIndex;

        SyllableDecomposition(boolean isHangul, int initial, int medial, int finalIndex) {
            this.isHangul = isHangul;
            this.initial = initial;
            this.medial = medial;
            this.finalIndex = finalIndex;
        }
    }
} 