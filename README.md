# Korean Romanizer - Java

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
    <groupId>io.github.korean-romanizer</groupId>
    <artifactId>korean-romanizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add this dependency to your `build.gradle`:

```gradle
implementation 'io.github.korean-romanizer:korean-romanizer:1.0.0'
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
import com.koreanromanizer.Romanizer;

// Static method
String romanized = Romanizer.romanize("안녕하세요");
System.out.println(romanized); // "annyeonghaseyo"

// Instance method
Romanizer romanizer = new Romanizer("한국어");
String result = romanizer.romanize();
System.out.println(result); // "hangugeo"
```

### Examples

```java
// Basic romanization
Romanizer.romanize("안녕하세요")         // "annyeonghaseyo"
Romanizer.romanize("아이유 방탄소년단")    // "aiyu bangtansonyeondan"

// Complex pronunciation rules
Romanizer.romanize("밝다")             // "bakda"
Romanizer.romanize("없다")             // "eopda"
Romanizer.romanize("좋아하고")          // "joahago"

// Individual jamo characters
Romanizer.romanize("ㅠㄴㅁㄱ")          // "yunmg"
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

### `Romanizer`
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

We tested our romanizer against a dataset of 230 Korean names with conventional transliterations:

```bash
mvn test -Dtest=KoreanNamesComparisonTest
```

**Results**: 0% exact matches - but this is expected and reveals important insights!

### Why the Low Name Accuracy?

Our library implements **linguistic romanization** (Revised Romanization of Korean), while Korean names use **conventional transliterations**:

| Korean | Name Convention | Our Romanization | Notes |
|--------|----------------|------------------|-------|
| 김 | Kim | gim | Historical convention vs. linguistic |
| 이 | Lee | i | Established usage vs. phonetic |
| 박 | Park | bak | Different systems |

**Key Differences:**
- **Spacing**: Names use spaces (`Kim Seo-jun`) vs. continuous text (`gimseojun`)
- **Hyphens**: Names hyphenate (`Seo-jun`) vs. continuous (`seojun`) 
- **Conventions**: Names follow historical conventions vs. systematic rules

**See `ANALYSIS.md` for detailed comparison**

Both approaches serve valid but different purposes:
- **Our romanizer**: Perfect for general Korean text, education, linguistic accuracy
- **Name conventions**: Required for official personal name transliterations

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

## Publishing to Maven Central

This library is configured for publication to Maven Central. See [PUBLISHING.md](PUBLISHING.md) for detailed instructions on how to publish new versions.

**Current Status**: Ready for Maven Central publication
- ✅ POM configured with required metadata
- ✅ MIT License included
- ✅ Source and Javadoc generation configured
- ✅ GPG signing profile ready
- ✅ GitHub Actions workflow for automated publishing 