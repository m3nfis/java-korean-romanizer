#!/bin/bash

# Korean Romanizer Release Script
# Usage: ./create-release.sh [version]
# Example: ./create-release.sh 1.0.0

set -e

if [ $# -eq 0 ]; then
    echo "Usage: $0 <version>"
    echo "Example: $0 1.0.0"
    exit 1
fi

VERSION=$1
TAG="v$VERSION"

echo "🚀 Creating release for version $VERSION"

# Check if we're on main branch
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    echo "❌ Error: You must be on the main branch to create a release"
    exit 1
fi

# Check if working directory is clean
if [ -n "$(git status --porcelain)" ]; then
    echo "❌ Error: Working directory is not clean. Please commit or stash your changes."
    exit 1
fi

# Update version in pom.xml
echo "📝 Updating version in pom.xml..."
mvn versions:set -DnewVersion=$VERSION -DgenerateBackupPoms=false

# Build the project
echo "🔨 Building project..."
mvn clean package -DskipTests

# Commit version change
echo "💾 Committing version change..."
git add pom.xml
git commit -m "Bump version to $VERSION"

# Create and push tag
echo "🏷️  Creating tag $TAG..."
git tag -a $TAG -m "Release $VERSION"
git push origin $TAG

echo "✅ Release $VERSION created successfully!"
echo "📦 JAR file available at: target/korean-romanizer-$VERSION.jar"
echo "🌐 GitHub release will be created automatically via workflow"
echo ""
echo "Next steps:"
echo "1. Wait for the GitHub workflow to complete"
echo "2. Review the release at: https://github.com/$(git config --get remote.origin.url | sed 's/.*github.com[:/]\([^/]*\/[^/]*\).*/\1/')/releases/tag/$TAG"
echo "3. Edit the release notes if needed" 