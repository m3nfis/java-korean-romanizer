# Publishing to Maven Central

This document outlines the steps to publish the Korean Romanizer library to Maven Central.

## Prerequisites

1. **Sonatype OSSRH Account**: Create an account at [Sonatype JIRA](https://issues.sonatype.org)
2. **GPG Key**: Generate a GPG key pair for signing artifacts
3. **GitHub Repository**: Set up a public GitHub repository
4. **Domain Verification**: For `io.github.username` group ID, you need a GitHub account

## Setup Steps

### 1. Sonatype OSSRH Account Setup

1. Create a Sonatype JIRA account
2. Create a new issue to request a new project repository
3. For `io.github.korean-romanizer` group ID, you'll need to verify GitHub account ownership

### 2. GPG Key Generation

```bash
# Generate a GPG key
gpg --gen-key

# List keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export the public key to keyservers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID

# Export private key for GitHub Secrets
gpg --armor --export-secret-keys YOUR_KEY_ID
```

### 3. GitHub Secrets Configuration

Set up the following secrets in your GitHub repository:

- `OSSRH_USERNAME`: Your Sonatype JIRA username
- `OSSRH_TOKEN`: Your Sonatype JIRA password or token
- `GPG_PRIVATE_KEY`: Your GPG private key (exported with --armor)
- `GPG_PASSPHRASE`: The passphrase for your GPG key

### 4. Maven Settings

Create `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>${env.MAVEN_USERNAME}</username>
      <password>${env.MAVEN_PASSWORD}</password>
    </server>
  </servers>
</settings>
```

## Publishing Process

### Manual Release

```bash
# Clean and test
mvn clean test

# Deploy to staging repository
mvn clean deploy -P release

# If using nexus-staging-maven-plugin with autoReleaseAfterClose=false:
mvn nexus-staging:release
```

### Automated Release (GitHub Actions)

1. Create a new release on GitHub
2. The GitHub workflow will automatically:
   - Run tests
   - Build artifacts
   - Sign with GPG
   - Deploy to Maven Central

## Project Configuration

The `pom.xml` is configured with:

- **Group ID**: `io.github.korean-romanizer`
- **Artifact ID**: `korean-romanizer`
- **License**: MIT License
- **SCM**: GitHub repository information
- **Distribution Management**: Sonatype OSSRH

## Required Artifacts

Maven Central requires these artifacts:

1. **Main JAR**: `korean-romanizer-1.0.0.jar`
2. **Sources JAR**: `korean-romanizer-1.0.0-sources.jar`
3. **Javadoc JAR**: `korean-romanizer-1.0.0-javadoc.jar`
4. **POM**: `korean-romanizer-1.0.0.pom`
5. **GPG Signatures**: `.asc` files for all above

## After Publishing

Once published, developers can use the library by adding this dependency to their `pom.xml`:

```xml
<dependency>
    <groupId>io.github.korean-romanizer</groupId>
    <artifactId>korean-romanizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Troubleshooting

### Common Issues

1. **Signing Failures**: Ensure GPG key is properly configured
2. **Staging Failures**: Check OSSRH credentials and group ID permissions
3. **Validation Errors**: Ensure all required metadata is present in POM

### Useful Commands

```bash
# Validate POM
mvn validate

# Generate sources and javadoc without deploying
mvn clean compile source:jar javadoc:jar

# Check what will be deployed
mvn clean deploy -DskipTests -DaltDeploymentRepository=local::default::file:./target/staging-deploy
``` 