# GitHub Release Guide

## 🚀 Creating Your First Release

Follow these steps to create your first GitHub release for the Korean Romanizer Java library:

## Step 1: Create GitHub Repository

1. Go to [GitHub.com](https://github.com) and sign in
2. Click the **"+"** button in the top right → **"New repository"**
3. Fill in the details:
   - **Repository name**: `korean-romanizer-java`
   - **Description**: `A Java library for romanizing Korean text following the Revised Romanization of Korean rules`
   - **Public** (required for Maven Central)
   - **Don't** initialize with README (we already have one)
4. Click **"Create repository"**

## Step 2: Connect Local Repository to GitHub

After creating the repository, GitHub will show you commands. Run these in your terminal:

```bash
# Add the remote repository
git remote add origin https://github.com/YOUR_USERNAME/korean-romanizer-java.git

# Push your code to GitHub
git push -u origin main
```

## Step 3: Create Your First Release

1. **Go to your GitHub repository page**
2. **Click "Releases"** (on the right side of the main page)
3. **Click "Create a new release"**
4. **Fill in the release details:**

### Release Form:
- **Tag version**: `v1.0.0` (create new tag)
- **Release title**: `Korean Romanizer v1.0.0 - Initial Release`
- **Description** (copy this):

```markdown
# Korean Romanizer v1.0.0 🇰🇷

The first release of the Korean Romanizer Java library! This is a complete port of the original Python implementation to Java, ready for use as a Maven dependency.

## ✨ Features

- **Complete Korean romanization** following Revised Romanization of Korean rules
- **Advanced pronunciation rules** including complex consonant combinations
- **Comprehensive testing** with all original Python tests ported and passing
- **Maven Central ready** for easy dependency management
- **Zero external dependencies** for the main library

## 📦 Maven Dependency

```xml
<dependency>
    <groupId>io.github.korean-romanizer</groupId>
    <artifactId>korean-romanizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚀 Quick Start

```java
import com.koreanromanizer.KoreanRomanizer;

// Simple usage
String result = KoreanRomanizer.romanize("안녕하세요");
System.out.println(result); // "annyeonghaseyo"
```

## 📊 Test Results

- ✅ **11/11 original tests passing**
- ✅ **Clean build with no errors**
- ✅ **Comprehensive Korean names analysis**
- ✅ **Maven Central compliance verified**

## 🙏 Acknowledgments

Based on the excellent Python library [korean-romanizer](https://github.com/osori/korean-romanizer) by osori.

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.
```

5. **Check "Set as the latest release"**
6. **Click "Publish release"**

## Step 4: What Happens Next

🎉 **Automated Publishing**: The GitHub Actions workflow will automatically:
- Run all tests
- Build the JAR files (main, sources, javadoc)
- Sign artifacts with GPG
- Deploy to Maven Central (once secrets are configured)

## 🔑 Required Secrets (for Maven Central)

Before the workflow can publish to Maven Central, add these secrets to your GitHub repository:

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Add these repository secrets:
   - `OSSRH_USERNAME`: Your Sonatype JIRA username
   - `OSSRH_TOKEN`: Your Sonatype JIRA password/token
   - `GPG_PRIVATE_KEY`: Your GPG private key
   - `GPG_PASSPHRASE`: Your GPG key passphrase

## 📚 Next Steps

1. **Set up Sonatype OSSRH account** (see [PUBLISHING.md](PUBLISHING.md))
2. **Generate GPG key** for signing
3. **Configure GitHub secrets**
4. **Create new releases** for future versions

---

**You're ready to release! 🚀** 