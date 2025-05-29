# Publishing a Spring Boot Starter to Maven Central - Complete Guide

## Prerequisites
1. GitHub account
2. Sonatype Central Portal account (create at https://central.sonatype.com)
3. GPG key for signing artifacts
4. Java 17+ and Maven installed
5. Access to a command line/terminal

## Initial Setup Checklist
1. [ ] Create Sonatype account
2. [ ] Generate GPG key
3. [ ] Configure GitHub repository
4. [ ] Set up Maven settings.xml
5. [ ] Configure pom.xml
6. [ ] Publish GPG key

## Step 1: Sonatype Account Setup
1. Visit https://central.sonatype.com
2. Create a new account
3. Note your username and generate a token for authentication
4. The token will be used in Maven settings.xml

## Step 1.1: GitHub Setup
1. Create a GitHub repository for your starter
2. Note your GitHub username (e.g., `rp-1701`)
3. Configure SSH for GitHub (if using multiple accounts):
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your_email@example.com" -f ~/.ssh/id_ed25519_personal

# Add to SSH config (~/.ssh/config)
Host github.com-personal
    HostName github.com
    User git
    IdentityFile ~/.ssh/id_ed25519_personal
```

## Step 2: GPG Setup
```bash
# Install GPG
brew install gnupg

# Generate GPG key
gpg --full-generate-key
# Choose:
# - RSA and RSA
# - 4096 bits
# - No expiration
# - Your details

# List keys to get key ID
gpg --list-secret-keys --keyid-format=long
# Note the key ID (e.g., 891CCF22D572114A)

# Export public key
gpg --armor --export YOUR_KEY_ID > public.key
```

### Publishing Your GPG Key
You must publish your GPG public key to key servers. There are two ways:

#### Option 1: Command Line
```bash
# Try these commands (if one fails, try the other)
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver hkps://keys.openpgp.org --send-keys YOUR_KEY_ID
```

#### Option 2: Manual Upload (Recommended if command line fails)
1. Export your public key:
```bash
gpg --armor --export YOUR_KEY_ID > public.key
```

2. Visit https://keys.openpgp.org/
3. Click "Upload"
4. Open your public.key file and copy all content
5. Paste the content into the text area
6. Click "Upload"
7. You will receive a verification email
8. Click the verification link in the email

You can verify your key is published by searching for your email or key ID at:
- https://keys.openpgp.org/
- https://keyserver.ubuntu.com/

Note: It may take some time for the key to propagate across different key servers.

### Important GPG Notes
- Keep your GPG passphrase secure
- Back up your GPG keys (~/.gnupg directory)
- If using multiple GitHub accounts, ensure correct SSH config
- GPG key must match the email used in GitHub

## Step 3: Maven Settings Configuration
Create/update `~/.m2/settings.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- Server configurations -->
    <servers>
        <server>
            <id>central</id>
            <username>YOUR_SONATYPE_USERNAME</username>
            <password>YOUR_SONATYPE_TOKEN</password>
        </server>
    </servers>

    <!-- Mirror configurations (if using corporate proxy) -->
    <mirrors>
        <mirror>
            <id>nexus</id>
            <mirrorOf>*,!central</mirrorOf>
            <url>https://artifact.intuit.com/artifactory/maven-proxy/</url>
        </mirror>
    </mirrors>

    <!-- Profiles -->
    <profiles>
        <profile>
            <id>personal</id>
            <properties>
                <gpg.keyname>YOUR_GPG_KEY_ID</gpg.keyname>
            </properties>
        </profile>
    </profiles>
</settings>
```

### Working with Corporate Proxies
If you're behind a corporate proxy (like Intuit's artifactory):
```xml
<mirrors>
    <mirror>
        <id>nexus</id>
        <mirrorOf>*,!central</mirrorOf>
        <url>https://artifact.intuit.com/artifactory/maven-proxy/</url>
    </mirror>
</mirrors>
```
Note: The `!central` in mirrorOf is crucial to allow direct access to Maven Central.

## Step 4: POM Configuration
Key changes needed in `pom.xml`:
```xml
<project>
    <!-- Group ID must match GitHub username -->
    <groupId>io.github.YOUR_GITHUB_USERNAME</groupId>
    <artifactId>YOUR_ARTIFACT_ID</artifactId>
    <version>0.0.1</version>
    
    <!-- Required metadata -->
    <name>Project Name</name>
    <description>Project Description</description>
    <url>https://github.com/USERNAME/REPO</url>

    <!-- License (Required) -->
    <licenses>
        <license>
            <name>Apache License, Version 2.0</name>
            <url>https://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
        </license>
    </licenses>

    <!-- Developer information (Required) -->
    <developers>
        <developer>
            <id>YOUR_ID</id>
            <name>YOUR_NAME</name>
            <email>YOUR_EMAIL</email>
        </developer>
    </developers>

    <!-- SCM Information (Required) -->
    <scm>
        <connection>scm:git:git://github.com/USERNAME/REPO.git</connection>
        <developerConnection>scm:git:ssh://git@github.com-personal:USERNAME/REPO.git</developerConnection>
        <url>https://github.com/USERNAME/REPO</url>
    </scm>

    <build>
        <plugins>
            <!-- Source Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>3.3.0</version>
                <executions>
                    <execution>
                        <id>attach-sources</id>
                        <goals>
                            <goal>jar-no-fork</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- Javadoc Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.6.3</version>
                <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- GPG Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-gpg-plugin</artifactId>
                <version>3.1.0</version>
                <executions>
                    <execution>
                        <id>sign-artifacts</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>sign</goal>
                        </goals>
                        <configuration>
                            <gpgArguments>
                                <arg>--pinentry-mode</arg>
                                <arg>loopback</arg>
                            </gpgArguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- Central Publishing Plugin -->
            <plugin>
                <groupId>org.sonatype.central</groupId>
                <artifactId>central-publishing-maven-plugin</artifactId>
                <version>0.3.0</version>
                <extensions>true</extensions>
                <configuration>
                    <publishingServerId>central</publishingServerId>
                    <tokenEnabled>true</tokenEnabled>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Step 5: Publishing Process
1. Set GPG passphrase as environment variable:
```bash
export GPG_PASSPHRASE=your_gpg_passphrase
```

2. Deploy to Maven Central:
```bash
mvn clean deploy -Ppersonal
```

### Common Errors and Solutions:

1. **401 Unauthorized**
   - Check Sonatype credentials in settings.xml
   - Verify server ID matches in POM and settings.xml
   - Ensure token is valid and not expired

2. **GPG signing errors**
   - Verify GPG key ID in settings.xml
   - Check GPG passphrase environment variable
   - Ensure GPG key is not expired
   - Add `--pinentry-mode loopback` to gpg arguments

3. **Mirror blocking access**
   - Add `!central` to mirrorOf configuration
   - Example: `<mirrorOf>*,!central</mirrorOf>`

4. **Validation errors**
   - Ensure all required POM elements are present
   - Check groupId matches GitHub username
   - Verify all URLs are correct
   - Include license, developers, and SCM information

## Step 6: Post-Deployment
1. Visit https://central.sonatype.com/publishing/deployments
2. Find your deployment using the ID from the Maven output
3. Review and publish the deployment
4. Wait for the artifact to be available (usually within 30 minutes)

## Verification
Once published, your artifact can be used with:
```xml
<dependency>
    <groupId>io.github.your-github-username</groupId>
    <artifactId>your-artifact-id</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Publishing Updates

### Version Numbering
Follow semantic versioning (MAJOR.MINOR.PATCH):
- MAJOR version (1.0.0) - Incompatible API changes
- MINOR version (0.1.0) - Backwards-compatible new features
- PATCH version (0.0.2) - Backwards-compatible bug fixes

### Steps to Publish an Update
1. Update the version in pom.xml
2. Make and test your changes
3. Run `mvn clean deploy -Ppersonal`
4. Visit https://central.sonatype.com/publishing/deployments
5. Find and publish the new deployment

### Best Practices for Updates
1. Never reuse a version number
2. Document changes in README/CHANGELOG
3. Test thoroughly before publishing
4. Consider maintaining release notes
5. Keep backwards compatibility when possible
6. Update documentation to reflect changes

## Troubleshooting Guide

### GPG Issues
1. **Key not found error**
   ```bash
   gpg: skipped "YOUR_KEY_ID": No secret key
   ```
   Solution: Verify key ID with `gpg --list-secret-keys --keyid-format=long`

2. **Signing failed**
   ```bash
   gpg: signing failed: Inappropriate ioctl for device
   ```
   Solution: Add `export GPG_TTY=$(tty)` to your shell configuration

### Maven Issues
1. **401 Unauthorized**
   - Check token validity
   - Verify server ID matches between pom.xml and settings.xml
   - Ensure token has required permissions

2. **Mirror blocking access**
   - Add `!central` to mirrorOf configuration
   - Check corporate proxy settings

### Deployment Issues
1. **Validation failures**
   - Verify all required POM elements are present
   - Check groupId matches GitHub username
   - Ensure all URLs are correct
   - Verify license information

2. **Publishing delays**
   - Normal sync time to Maven Central: ~30 minutes
   - Check status at https://central.sonatype.com/publishing/deployments

## Post-Publishing Checklist
1. [ ] Verify artifact appears in Maven Central
2. [ ] Test downloading the artifact in a new project
3. [ ] Update project documentation
4. [ ] Tag the release in GitHub
5. [ ] Announce the release (if public)

## Security Best Practices
1. Never commit sensitive information (tokens, passwords)
2. Use environment variables for sensitive data
3. Regularly rotate GPG keys and tokens
4. Keep your local environment secure
