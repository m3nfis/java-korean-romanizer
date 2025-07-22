# Release Guide

This guide explains how to create releases for the Korean Romanizer library.

## Automated Release Process

The project uses GitHub Actions to automatically create releases when you push a version tag.

### Prerequisites

1. Make sure you're on the `main` branch
2. Ensure all changes are committed
3. Have write access to the repository

### Creating a Release

#### Option 1: Using the Release Script (Recommended)

```bash
# Create a release for version 1.0.0
./create-release.sh 1.0.0
```

The script will:
- Update the version in `pom.xml`
- Build the project
- Commit the version change
- Create and push a version tag
- Trigger the GitHub Actions workflow

#### Option 2: Manual Process

1. **Update version in pom.xml:**
   ```bash
   mvn versions:set -DnewVersion=1.0.0 -DgenerateBackupPoms=false
   ```

2. **Build the project:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Commit and tag:**
   ```bash
   git add pom.xml
   git commit -m "Bump version to 1.0.0"
   git tag -a v1.0.0 -m "Release 1.0.0"
   git push origin v1.0.0
   ```

### What Happens Next

1. **GitHub Actions Workflow** (`release.yml`) automatically:
   - Builds the project on Ubuntu with JDK 11
   - Creates release assets (JAR, README, LICENSE)
   - Creates a GitHub release with the assets
   - Generates release notes

2. **Release Assets** include:
   - `korean-romanizer-{version}.jar` - The compiled JAR file
   - `README.md` - Project documentation
   - `LICENSE` - Project license

3. **Release Notes** are automatically generated from:
   - Commit messages since the last release
   - Pull requests merged since the last release

### Versioning Guidelines

- Use [Semantic Versioning](https://semver.org/): `MAJOR.MINOR.PATCH`
- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

### Examples

```bash
# Patch release (bug fix)
./create-release.sh 1.0.1

# Minor release (new feature)
./create-release.sh 1.1.0

# Major release (breaking change)
./create-release.sh 2.0.0
```

### Manual Release Creation

If you need to create a release manually:

1. Go to the GitHub repository
2. Click "Releases" in the right sidebar
3. Click "Create a new release"
4. Choose a tag (or create a new one)
5. Add release title and description
6. Upload the JAR file from `target/korean-romanizer-{version}.jar`
7. Publish the release

### Troubleshooting

#### Workflow Fails
- Check the GitHub Actions tab for error details
- Ensure the repository has the necessary permissions
- Verify the tag format is correct (`v*`)

#### JAR Not Building
- Run `mvn clean package -DskipTests` locally to test
- Check for compilation errors
- Ensure all dependencies are available

#### Tag Already Exists
- Delete the existing tag: `git tag -d v1.0.0 && git push origin :refs/tags/v1.0.0`
- Create a new tag with a different version

### CI/CD Pipeline

The project also includes a CI pipeline (`ci.yml`) that:
- Runs on every push to `main`
- Tests on multiple Java versions (11, 17, 21)
- Builds the project
- Uploads JAR artifacts

This ensures the project is always in a buildable state.

## Current Version

The current version is defined in `pom.xml` and should be updated before each release.

## Support

For issues with the release process, check:
1. GitHub Actions logs
2. Maven build output
3. Git tag and branch status 