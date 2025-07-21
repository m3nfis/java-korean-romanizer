package com.koreanromanizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Applies Korean pronunciation rules to text before romanization.
 * Handles complex pronunciation changes based on syllable combinations.
 */
public class Pronouncer {
    private List<Syllable> syllables;
    private String pronounced;

    // Double consonant finals and their decomposition
    private static final Map<String, String[]> DOUBLE_CONSONANT_FINAL = new HashMap<>();
    static {
        DOUBLE_CONSONANT_FINAL.put("ᆪ", new String[]{"ᆨ", "ᆺ"});
        DOUBLE_CONSONANT_FINAL.put("ᆬ", new String[]{"ᆫ", "ᆽ"});
        DOUBLE_CONSONANT_FINAL.put("ᆭ", new String[]{"ᆫ", "ᇂ"});
        DOUBLE_CONSONANT_FINAL.put("ᆰ", new String[]{"ᆯ", "ᆨ"});
        DOUBLE_CONSONANT_FINAL.put("ᆱ", new String[]{"ᆯ", "ᆷ"});
        DOUBLE_CONSONANT_FINAL.put("ᆲ", new String[]{"ᆯ", "ᆸ"});
        DOUBLE_CONSONANT_FINAL.put("ᆳ", new String[]{"ᆯ", "ᆻ"});
        DOUBLE_CONSONANT_FINAL.put("ᆴ", new String[]{"ᆯ", "ᇀ"});
        DOUBLE_CONSONANT_FINAL.put("ᆵ", new String[]{"ᆯ", "ᇁ"});
        DOUBLE_CONSONANT_FINAL.put("ᆶ", new String[]{"ᆯ", "ᇂ"});
        DOUBLE_CONSONANT_FINAL.put("ᆹ", new String[]{"ᆸ", "ᆺ"});
        DOUBLE_CONSONANT_FINAL.put("ㅆ", new String[]{"ㅅ", "ㅅ"});
    }

    private static final String NULL_CONSONANT = "ᄋ";

    /**
     * Creates a Pronouncer for the given text.
     * 
     * @param text The Korean text to process
     */
    public Pronouncer(String text) {
        this.syllables = new ArrayList<>();
        for (char c : text.toCharArray()) {
            this.syllables.add(new Syllable(c));
        }
        
        List<Syllable> processedSyllables = finalSubstitute();
        StringBuilder sb = new StringBuilder();
        for (Syllable syllable : processedSyllables) {
            sb.append(syllable.toString());
        }
        this.pronounced = sb.toString();
    }

    /**
     * Applies final consonant substitution rules.
     * 
     * @return List of syllables with pronunciation rules applied
     */
    private List<Syllable> finalSubstitute() {
        for (int i = 0; i < syllables.size(); i++) {
            Syllable syllable = syllables.get(i);
            Syllable nextSyllable = (i + 1 < syllables.size()) ? syllables.get(i + 1) : null;

            boolean finalIsBeforeC = false;
            boolean finalIsBeforeV = false;
            
            if (syllable.getFinal() != null && nextSyllable != null) {
                String nextInitial = nextSyllable.getInitial();
                finalIsBeforeC = nextInitial != null && !nextInitial.equals(NULL_CONSONANT);
                finalIsBeforeV = nextInitial != null && nextInitial.equals(NULL_CONSONANT);
            }

            boolean isLastSyllable = syllable.getFinal() != null && nextSyllable == null;

            // Rules for final consonant simplification before consonants or at word end
            if (isLastSyllable || finalIsBeforeC) {
                String finalChar = syllable.getFinal();
                if (finalChar != null) {
                    // Rule 1 & 2: Simplify complex finals to representative sounds
                    if (finalChar.equals("ᆩ") || finalChar.equals("ᆿ") || 
                        finalChar.equals("ᆪ") || finalChar.equals("ᆰ")) {
                        syllable.setFinal("ᆨ"); // -> [ㄱ]
                    } else if (finalChar.equals("ᆺ") || finalChar.equals("ᆻ") || 
                               finalChar.equals("ᆽ") || finalChar.equals("ᆾ") || 
                               finalChar.equals("ᇀ")) {
                        syllable.setFinal("ᆮ"); // -> [ㄷ]
                    } else if (finalChar.equals("ᇁ") || finalChar.equals("ᆹ") || 
                               finalChar.equals("ᆵ")) {
                        syllable.setFinal("ᆸ"); // -> [ㅂ]
                    } else if (finalChar.equals("ᆬ")) {
                        syllable.setFinal("ᆫ"); // -> [ㄴ]
                    } else if (finalChar.equals("ᆲ") || finalChar.equals("ᆳ") || 
                               finalChar.equals("ᆴ")) {
                        syllable.setFinal("ᆯ"); // -> [ㄹ]
                    } else if (finalChar.equals("ᆱ")) {
                        syllable.setFinal("ᆷ"); // -> [ㅁ]
                    }
                }
            }

            // Rules for ㅎ pronunciation
            if (syllable.getFinal() != null && 
                (syllable.getFinal().equals("ᇂ") || syllable.getFinal().equals("ᆭ") || 
                 syllable.getFinal().equals("ᆶ"))) {
                
                Map<String, String> withoutH = new HashMap<>();
                withoutH.put("ᆭ", "ᆫ");
                withoutH.put("ᆶ", "ᆯ");
                withoutH.put("ᇂ", null);

                if (nextSyllable != null) {
                    String nextInitial = nextSyllable.getInitial();
                    
                    // ㅎ + ㄱ,ㄷ,ㅈ,ㅅ -> ㅋ,ㅌ,ㅊ,ㅆ
                    if (nextInitial.equals("ᄀ") || nextInitial.equals("ᄃ") || 
                        nextInitial.equals("ᄌ") || nextInitial.equals("ᄉ")) {
                        
                        Map<String, String> changeTo = new HashMap<>();
                        changeTo.put("ᄀ", "ᄏ");
                        changeTo.put("ᄃ", "ᄐ");
                        changeTo.put("ᄌ", "ᄎ");
                        changeTo.put("ᄉ", "ᄊ");
                        
                        syllable.setFinal(withoutH.get(syllable.getFinal()));
                        nextSyllable.setInitial(changeTo.get(nextInitial));
                    }
                    // ㅎ + ㄴ -> ㄴ
                    else if (nextInitial.equals("ᄂ")) {
                        if (syllable.getFinal().equals("ᆭ") || syllable.getFinal().equals("ᆶ")) {
                            syllable.setFinal(withoutH.get(syllable.getFinal()));
                        } else {
                            syllable.setFinal("ᆫ");
                        }
                    }
                    // ㅎ before vowel
                    else if (nextInitial.equals(NULL_CONSONANT)) {
                        if (syllable.getFinal().equals("ᆭ") || syllable.getFinal().equals("ᆶ")) {
                            syllable.setFinal(syllable.getFinal().equals("ᆭ") ? "ᆫ" : "ᆯ");
                        } else {
                            syllable.setFinal(null);
                        }
                    }
                    // ㅎ + ㄹ
                    else if (nextInitial.equals("ᄅ")) {
                        if (syllable.getFinal().equals("ᆶ")) {
                            syllable.setFinal("ᆯ");
                        }
                    } else {
                        if (syllable.getFinal().equals("ᇂ")) {
                            syllable.setFinal(null);
                        }
                    }
                } else {
                    syllable.setFinal(withoutH.get(syllable.getFinal()));
                }
            }

            // Rule 6: Double consonant before vowel
            if (syllable.getFinal() != null && 
                DOUBLE_CONSONANT_FINAL.containsKey(syllable.getFinal()) &&
                nextSyllable != null && nextSyllable.getInitial().equals(NULL_CONSONANT)) {
                
                String[] doubleConsonant = DOUBLE_CONSONANT_FINAL.get(syllable.getFinal());
                syllable.setFinal(doubleConsonant[0]);
                nextSyllable.setInitial(nextSyllable.finalToInitial(doubleConsonant[1]));
            }

            // Rule 5: Single/double final before vowel
            if (nextSyllable != null && finalIsBeforeV && 
                nextSyllable.getInitial().equals(NULL_CONSONANT) &&
                syllable.getFinal() != null && 
                !syllable.getFinal().equals("ᆼ")) {
                
                nextSyllable.setInitial(nextSyllable.finalToInitial(syllable.getFinal()));
                syllable.setFinal(null);
            }
        }

        return syllables;
    }

    /**
     * Gets the pronounced text with pronunciation rules applied.
     * 
     * @return The pronounced text
     */
    public String getPronounced() {
        return pronounced;
    }
} 