package com.koreanromanizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the Korean Romanizer library.
 * These tests are ported from the original Python test suite.
 */
public class RomanizerTest {

    private String romanize(String text) {
        return KoreanRomanizer.romanize(text);
    }

    @Test
    public void testSimple() {
        assertEquals("annyeonghaseyo", romanize("안녕하세요"));
    }

    @Test
    public void testSpacedText() {
        assertEquals("aiyu bangtansonyeontan", romanize("아이유 방탄소년단"));
    }

    @Test
    public void testOnsetGDB() {
        assertEquals("gumi", romanize("구미"));
        assertEquals("yeongtong", romanize("영동"));
        assertEquals("hanpat", romanize("한밭"));
    }

    @Test
    public void testCodaGDB() {
        assertEquals("bakta", romanize("밝다"));
        assertEquals("badatka", romanize("바닷가"));
        assertEquals("eopta", romanize("없다"));
        assertEquals("apman", romanize("앞만"));
        assertEquals("eupta", romanize("읊다"));
    }

    @Test
    public void testRL() {
        assertEquals("guli", romanize("구리"));
        assertEquals("seolak", romanize("설악"));
        // Note: The following tests are commented out in the original Python tests
        // assertEquals("ulleung", romanize("울릉"));
        // assertEquals("daegwallyeong", romanize("대관령"));
    }

    @Test
    public void testNextSyllableNullInitial() {
        assertEquals("gangyak", romanize("강약"));
        assertEquals("gangwon", romanize("강원"));
        assertEquals("joahako", romanize("좋아하고"));
        assertEquals("joeun", romanize("좋은"));
    }

    @Test
    public void testDoubleConsonantFinalAndNextSyllableNullInitial() {
        assertEquals("haesseosseoyo", romanize("했었어요"));
        assertEquals("eopseotta", romanize("없었다"));
        assertEquals("anjapwa", romanize("앉아봐"));
        assertEquals("dalkui", romanize("닭의"));
        assertEquals("balpa", romanize("밟아"));
        assertEquals("dalmatne", romanize("닮았네"));
        assertEquals("sakseul", romanize("삯을"));
        assertEquals("aratta", romanize("앓았다"));
        assertEquals("eulpeo bogeola", romanize("읊어 보거라"));
        assertEquals("golssi", romanize("곬이"));
        assertEquals("hulteobota", romanize("훑어보다"));
    }

    @Test
    public void testDoubleConsonantFinalAndNextSyllableNotNullInitial() {
        assertEquals("angosipta", romanize("앉고싶다"));
        assertEquals("ttulrita", romanize("뚫리다"));
        assertEquals("chikppuli", romanize("칡뿌리"));
    }

    @Test
    public void testDoubleConsonantFinalWithoutNextSyllable() {
        assertEquals("gwaenchan", romanize("괜찮"));
        assertEquals("ttul", romanize("뚫"));
        assertEquals("an", romanize("않"));
    }

    @Test
    public void testNonSyllables() {
        assertEquals("yunmg", romanize("ㅠㄴㅁㄱ"));
        assertEquals("yutong", romanize("ㅠ동"));
    }

    @Test
    public void testCodaH() {
        assertEquals("ansseupnita", romanize("않습니다"));
        assertEquals("alko", romanize("앓고"));
    }
} 