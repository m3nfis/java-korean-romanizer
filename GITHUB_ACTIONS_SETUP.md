# GitHub Actions CI/CD Setup Guide

This guide will help you set up the complete CI/CD pipeline for your Korean Romanizer Java library.

## 🚀 Quick Setup

I've created a comprehensive CI/CD pipeline with the following workflows:

### 1. Main CI/CD Pipeline (`.github/workflows/ci.yml`)
- **Multi-JDK Testing**: Tests on JDK 11, 17, and 21
- **Build & Package**: Creates JAR artifacts
- **Code Quality**: SonarCloud integration with code coverage
- **Dependency Security**: OWASP dependency vulnerability scanning
- **Automated Publishing**: 
  - SNAPSHOT versions on main branch pushes
  - Release versions on GitHub releases

### 2. Documentation Pipeline (`.github/workflows/docs.yml`)
- **Javadoc Generation**: Automatically generates API documentation
- **GitHub Pages**: Publishes docs to `https://m3nfis.github.io/java-korean-romanizer/javadoc/`

## 🔧 Required GitHub Secrets

To enable all features, add these secrets to your GitHub repository:

### For Maven Central Publishing
```
OSSRH_USERNAME=your-sonatype-username
OSSRH_TOKEN=your-sonatype-token
GPG_PRIVATE_KEY=your-gpg-private-key
GPG_PASSPHRASE=your-gpg-passphrase
```

### For SonarCloud (Optional but Recommended)
```
SONAR_TOKEN=your-sonarcloud-token
```

## 📋 Setup Steps

### 1. Enable GitHub Actions
- Go to your repository → Settings → Actions → General
- Ensure "Allow all actions and reusable workflows" is selected

### 2. Enable GitHub Pages (for documentation)
- Go to Settings → Pages
- Source: "Deploy from a branch"
- Branch: `gh-pages` / `/ (root)`

### 3. Set up SonarCloud (Optional)
1. Go to [SonarCloud.io](https://sonarcloud.io)
2. Sign in with GitHub
3. Import your repository
4. Get your project token
5. Add `SONAR_TOKEN` to GitHub secrets

### 4. Set up Maven Central Publishing (Optional)
1. Create account at [Sonatype OSSRH](https://issues.sonatype.org)
2. Request namespace for `io.github.m3nfis`
3. Generate GPG key pair
4. Add all required secrets to GitHub

### 5. Create Your First Release
1. Push your code to `main` branch
2. Go to Releases → Create a new release
3. Tag version: `v1.0.0`
4. The workflow will automatically publish to Maven Central

## 🔄 Workflow Triggers

### Automatic Triggers
- **Push to main/develop**: Runs tests, builds, and publishes SNAPSHOT
- **Pull Request to main**: Runs tests and builds
- **Release published**: Publishes release version to Maven Central

### Manual Triggers
- All workflows can be triggered manually from Actions tab

## 📊 What Each Job Does

### `test`
- Runs on multiple JDK versions (11, 17, 21)
- Executes all unit tests
- Generates test reports
- Caches Maven dependencies for faster builds

### `build`
- Compiles the code
- Packages JAR files
- Uploads build artifacts

### `code-quality`
- Runs SonarCloud analysis
- Generates code coverage reports
- Checks code quality metrics

### `dependency-check`
- Scans for known security vulnerabilities
- Generates security reports

### `publish-snapshot`
- Publishes SNAPSHOT versions on main branch
- Only runs if Maven Central secrets are configured

### `publish-release`
- Publishes release versions
- Signs artifacts with GPG
- Deploys to Maven Central staging

## 🛠️ Local Development

### Run tests locally:
```bash
mvn clean test
```

### Generate coverage report:
```bash
mvn clean test jacoco:report
```

### Run SonarCloud analysis locally:
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=m3nfis_java-korean-romanizer \
  -Dsonar.organization=m3nfis \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=YOUR_SONAR_TOKEN
```

### Build release locally:
```bash
mvn clean package -Prelease
```

## 📈 Monitoring

### Build Status
- Check the Actions tab for build status
- Green checkmarks = successful builds
- Red X = failed builds (click for details)

### Code Quality
- Visit your SonarCloud dashboard
- Monitor code coverage, bugs, vulnerabilities

### Dependencies
- Review dependency-check reports in Actions artifacts
- Update vulnerable dependencies promptly

## 🔍 Troubleshooting

### Common Issues

1. **Tests failing on different JDK versions**
   - Check for JDK-specific code
   - Update dependencies if needed

2. **SonarCloud analysis failing**
   - Verify SONAR_TOKEN is correct
   - Check SonarCloud project configuration

3. **Maven Central publishing failing**
   - Verify all secrets are set correctly
   - Check GPG key format and passphrase

4. **GitHub Pages not updating**
   - Ensure gh-pages branch exists
   - Check Pages settings in repository

### Getting Help
- Check the Actions logs for detailed error messages
- Review the workflow files for configuration issues
- Ensure all required secrets are properly set

## 🎉 You're All Set!

Your repository now has:
- ✅ Automated testing on multiple JDK versions
- ✅ Code quality monitoring
- ✅ Security vulnerability scanning
- ✅ Automated documentation publishing
- ✅ Maven Central publishing pipeline
- ✅ Professional CI/CD setup

Push your code and watch the magic happen! 🚀