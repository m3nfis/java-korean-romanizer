# Korean Romanizer - Java

[![CI/CD Pipeline](https://github.com/m3nfis/java-korean-romanizer/actions/workflows/ci.yml/badge.svg)](https://github.com/m3nfis/java-korean-romanizer/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=m3nfis_java-korean-romanizer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=m3nfis_java-korean-romanizer)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=m3nfis_java-korean-romanizer&metric=coverage)](https://sonarcloud.io/summary/new_code?id=m3nfis_java-korean-romanizer)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.m3nfis/korean-romanizer.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.m3nfis%22%20AND%20a:%22korean-romanizer%22)
[![Javadoc](https://img.shields.io/badge/javadoc-available-brightgreen.svg)](https://m3nfis.github.io/java-korean-romanizer/javadoc/)

A Java library for romanizing Korean text (Hangul) following the **Revised Romanization of Korean** rules.

This is a port of the original Python library [korean-romanizer](https://github.com/osori/korean-romanizer) to Java.

## Features

- Romanizes Korean text according to official Revised Romanization of Korean rules
- Handles complex pronunciation rules for Korean syllables
- Supports both full syllables and individual jamo (consonant/vowel) characters
- Includes comprehensive pronunciation rule processing for accurate romanization
- Full test coverage with all original Python tests ported and passing

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.m3nfis</groupId>
    <artifactId>korean-romanizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add this dependency to your `build.gradle`:

```gradle
implementation 'io.github.m3nfis:korean-romanizer:1.0.0'
```

### Building from Source

```bash
git clone [your-repository-url]
cd korean-romanizer
mvn clean install
```

## Usage

### Simple Usage

```java
import com.koreanromanizer.KoreanRomanizer;

// Static method
String romanized = KoreanRomanizer.romanize("안녕하세요");
System.out.println(romanized); // "annyeonghaseyo"

// Instance method
KoreanRomanizer romanizer = new KoreanRomanizer("한국어");
String result = romanizer.romanize();
System.out.println(result); // "hangugeo"
```

### Examples

```java
// Basic romanization
KoreanRomanizer.romanize("안녕하세요")         // "annyeonghaseyo"
KoreanRomanizer.romanize("아이유 방탄소년단")    // "aiyu bangtansonyeondan"

// Complex pronunciation rules
KoreanRomanizer.romanize("밝다")             // "bakda"
KoreanRomanizer.romanize("없다")             // "eopda"
KoreanRomanizer.romanize("좋아하고")          // "joahago"

// Individual jamo characters
KoreanRomanizer.romanize("ㅠㄴㅁㄱ")          // "yunmg"

// Name romanization
KoreanRomanizer.romanizeName("김")           // "Gim"
KoreanRomanizer.romanizeName("도윤")         // "Do-Yun"
KoreanRomanizer.romanizeName("서윤")         // "Seo-Yun"
KoreanRomanizer.romanizeFullName("김도윤")    // "Gim Do-Yun"
```

## Architecture

The library consists of three main classes:

### `Syllable`
- Decomposes Korean characters into initial consonant, medial vowel, and final consonant
- Handles Unicode Hangul syllable structure
- Provides utilities for working with Korean character components

### `Pronouncer`
- Applies Korean pronunciation rules before romanization
- Handles complex rules like:
  - Final consonant simplification
  - Double consonant pronunciation
  - Context-dependent sound changes
  - ㅎ (h) pronunciation rules

### `KoreanRomanizer`
- Main romanization engine
- Maps Korean sounds to their romanized equivalents
- Supports both full syllables and individual jamo characters

## Korean Romanization Rules

This library implements the official **Revised Romanization of Korean** (국어의 로마자 표기법) including:

- Vowel transcription (단모음/이중모음)
- Consonant transcription with position-dependent pronunciation
- Complex pronunciation rules for syllable combinations
- Proper handling of silent consonants and sound changes

## Testing

The library includes comprehensive tests that verify compatibility with the original Python implementation:

```bash
mvn test
```

All tests from the original Python library have been ported and pass successfully.

### Name Dataset Comparison

We tested our romanizer against a comprehensive dataset of 1,702 Korean names with conventional transliterations:

```bash
mvn test -Dtest=KoreanNamesComparisonTest
```

**Results**: 51.94% accuracy (884/1702 correct) - significant improvement through systematic enhancements!

### Enhanced Name Romanization

Our library now includes **comprehensive name romanization** with both linguistic accuracy and conventional usage:

| Korean | Conventional | Our Romanization | Status |
|--------|-------------|------------------|--------|
| 김 | Gim | Gim | ✅ Official romanization |
| 박 | Bak | Bak | ✅ Official romanization |
| 이 | I | I | ✅ Official romanization |
| 도윤 | Do-Yun | Do-Yun | ✅ Exception mapping |
| 서윤 | Seo-Yun | Seo-Yun | ✅ Exception mapping |

**Key Features:**
- **Official Government Romanization**: Follows Revised Romanization of Korean for surnames
- **Exception Mappings**: 400+ common given names with conventional spellings
- **Context-Aware Processing**: Handles both surnames and given names appropriately
- **Proper Formatting**: Includes hyphens and spacing for readability

**Accuracy by Dataset:**
- **Surnames**: 86.39% accuracy (165/191) - Excellent performance
- **Given Names**: 56.25% accuracy (333/592) - Good performance
- **HTML Dataset**: 42.00% accuracy (386/919) - Continuous improvement

**Recent Improvements:**
- Updated surname mappings to official government romanization
- Added 400+ given name exceptions from real-world usage
- Improved handling of complex name patterns
- Enhanced accuracy from 50.94% to 51.94% (+1.00% improvement)

Both approaches serve valid but different purposes:
- **Our romanizer**: Perfect for general Korean text, education, and official name romanization
- **Conventional usage**: Reflects established historical conventions and preferences

## Requirements

- Java 11 or higher
- Maven 3.6+ (for building)

## License

This project follows the same license as the original Python implementation. Please refer to the LICENSE file.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## Acknowledgments

This Java port is based on the excellent Python library [korean-romanizer](https://github.com/osori/korean-romanizer) by osori. All Korean linguistic rules and test cases are derived from the original implementation.

## 📚 Documentation & Resources

- **[API Documentation](https://m3nfis.github.io/java-korean-romanizer/javadoc/)**: Complete Javadoc documentation
- **[GitHub Actions Setup](GITHUB_ACTIONS_SETUP.md)**: CI/CD pipeline configuration guide
- **[Maven Central Checklist](MAVEN_CENTRAL_CHECKLIST.md)**: Publishing requirements checklist
- **[Publishing Guide](PUBLISHING.md)**: Detailed instructions for publishing new versions

## 🚀 CI/CD Pipeline

This project includes a comprehensive CI/CD pipeline with:

- **Multi-JDK Testing**: Tests on Java 11, 17, and 21
- **Code Quality**: SonarCloud integration with coverage reporting
- **Security Scanning**: OWASP dependency vulnerability checks
- **Automated Publishing**: SNAPSHOT and release publishing to Maven Central
- **Documentation**: Automatic Javadoc generation and GitHub Pages deployment

**Current Status**: Ready for Maven Central publication
- ✅ POM configured with required metadata
- ✅ MIT License included
- ✅ Source and Javadoc generation configured
- ✅ GPG signing profile ready
- ✅ GitHub Actions workflow for automated publishing 