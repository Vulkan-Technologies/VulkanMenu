# VulkanMenu Release Process

This document describes the release process for VulkanMenu using the Maven Release Plugin.

## Prerequisites

Before performing a release, ensure you have:

1. **Git Configuration**
   - Push access to the repository
   - Git configured with your name and email:
     ```bash
     git config user.name "Your Name"
     git config user.email "your.email@example.com"
     ```

2. **GPG Signing Key**
   - GPG key for signing artifacts
   - GPG key added to your GitHub account
   - GPG agent running:
     ```bash
     gpg-agent --daemon
     ```

3. **Maven Central Credentials**
   - Sonatype OSSRH account credentials
   - Settings in `~/.m2/settings.xml`:
     ```xml
     <settings>
       <servers>
         <server>
           <id>central</id>
           <username>your-username</username>
           <password>your-password</password>
         </server>
       </servers>
     </settings>
     ```

4. **Clean Working Directory**
   - No uncommitted changes
   - On the main branch or a release branch

## Version Numbering Convention

VulkanMenu follows semantic versioning (SemVer):

- **MAJOR.MINOR.PATCH** format (e.g., 1.0.6)
- **MAJOR**: Breaking API changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

## Release Process

### 1. Prepare the Release

Before starting, ensure all changes are committed and pushed:

```bash
git status
git pull origin main
```

### 2. Run the Release Prepare

This step will:
- Check for uncommitted changes
- Update version from SNAPSHOT to release version
- Create a git tag
- Update version to next SNAPSHOT
- Commit changes

```bash
mvn release:prepare
```

You'll be prompted for:
- Release version (e.g., 1.0.7)
- Release tag (e.g., v1.0.7)
- Next development version (e.g., 1.0.8-SNAPSHOT)

Or run with predefined versions:

```bash
mvn release:prepare \
  -DreleaseVersion=1.0.7 \
  -DdevelopmentVersion=1.0.8-SNAPSHOT \
  -Dtag=v1.0.7
```

### 3. Perform the Release

This step will:
- Checkout the release tag
- Build the project
- Deploy artifacts to Maven Central
- Deploy JAR to server (via shade plugin configuration)

```bash
mvn release:perform
```

### 4. Verify the Release

After the release:

1. Check GitHub for the new tag:
   ```bash
   git tag -l "v*"
   ```

2. Verify on Maven Central (may take a few hours to sync):
   - https://central.sonatype.com/artifact/com.vulkantechnologies/menu

3. Test the deployed JAR in the server:
   ```bash
   cd server
   java -jar paper-1.21.4-230.jar
   ```

## Quick Release (One Command)

For experienced users, prepare and perform in one command:

```bash
mvn release:prepare release:perform \
  -DreleaseVersion=1.0.7 \
  -DdevelopmentVersion=1.0.8-SNAPSHOT \
  -Dtag=v1.0.7
```

## Rollback a Failed Release

If something goes wrong during the release process:

### Rollback Prepare Phase

```bash
mvn release:rollback
```

This will:
- Remove the release.properties file
- Restore the pom.xml backup

### Clean Up Git Tags (if needed)

```bash
# Delete local tag
git tag -d v1.0.7

# Delete remote tag
git push origin :refs/tags/v1.0.7
```

### Clean Up Commits (if needed)

```bash
# Reset to before release commits
git reset --hard HEAD~2
git push --force-with-lease origin main
```

## Troubleshooting

### GPG Signing Issues

If GPG signing fails:

1. Ensure GPG agent is running:
   ```bash
   gpg-agent --daemon
   ```

2. Test GPG signing:
   ```bash
   echo "test" | gpg --clearsign
   ```

3. Configure Maven to use GPG:
   ```bash
   export GPG_TTY=$(tty)
   ```

### Authentication Issues

If Maven Central authentication fails:

1. Verify credentials in `~/.m2/settings.xml`
2. Check your Sonatype account is active
3. Ensure you have deployment permissions

### Build Failures

If the build fails during release:

1. Run a clean build first:
   ```bash
   mvn clean verify
   ```

2. Fix any issues before attempting release
3. Ensure all tests pass (or skip with `-DskipTests`)

### Network Issues

If deployment fails due to network:

1. Check your internet connection
2. Verify Maven Central is accessible
3. Try again with increased timeout:
   ```bash
   mvn release:perform -Dmaven.wagon.http.timeout=120000
   ```

## Post-Release Tasks

After a successful release:

1. **Update Documentation**
   - Update README with new version
   - Update installation instructions
   - Add release notes to GitHub Releases

2. **Announce the Release**
   - Create GitHub Release with changelog
   - Update project website/wiki
   - Notify users via appropriate channels

3. **Monitor Deployment**
   - Check Maven Central for artifact availability
   - Monitor for any user-reported issues
   - Verify plugin loads correctly in Minecraft servers

## Release Checklist

- [ ] All features for this release are complete
- [ ] All tests pass
- [ ] Documentation is updated
- [ ] CLAUDE.md is current
- [ ] No uncommitted changes
- [ ] On correct branch (main or release branch)
- [ ] GPG key is available and working
- [ ] Maven Central credentials configured
- [ ] Version number decided (following SemVer)
- [ ] Release notes prepared

## Maven Release Plugin Configuration

The Maven Release Plugin is configured in `pom.xml` with:

- **Tag Format**: `v@{project.version}` (e.g., v1.0.7)
- **Goals**: `deploy` (builds and deploys to Maven Central)
- **Arguments**: `-DskipTests` (skips tests during release)
- **SCM Comment Prefix**: `[RELEASE]` (prefixes commit messages)
- **Push Changes**: Automatically pushes commits and tags

## Additional Resources

- [Maven Release Plugin Documentation](https://maven.apache.org/maven-release/maven-release-plugin/)
- [Semantic Versioning](https://semver.org/)
- [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
- [GPG Signing Guide](https://central.sonatype.org/publish/requirements/gpg/)