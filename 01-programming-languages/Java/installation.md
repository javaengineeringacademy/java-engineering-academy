# Java Installation

> JDK installation on Windows, macOS, and Linux with SDKMAN and JAVA_HOME configuration.

## JDK Distribution Overview

| Distribution | Vendor | License | Best For |
|-------------|--------|---------|----------|
| Oracle JDK | Oracle | GFTL/Commercial | Enterprise with support |
| OpenJDK | Oracle/OpenJDK | GPL v2 | General development |
| Eclipse Temurin | Adoptium | GPL v2 | Production, LTS |
| Amazon Corretto | Amazon | GPL v2 | AWS environments |
| Azul Zulu | Azul | GPL v2 | All platforms |
| GraalVM CE | Oracle | GPL v2 | Native compilation |

## Linux Installation

### Ubuntu/Debian

```bash
# Install OpenJDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Install specific distribution
sudo apt install openjdk-21-jdk-headless  # without GUI
sudo apt install openjdk-21-jre           # JRE only

# Verify installation
java -version
javac -version

# Set as default (if multiple versions)
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

### RHEL/CentOS/Fedora

```bash
# Fedora
sudo dnf install java-21-openjdk-devel

# RHEL/CentOS
sudo yum install java-21-openjdk-devel

# Verify
java -version
```

### Manual Installation

```bash
# Download and extract
wget https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz
tar -xzf openjdk-21.0.2_linux-x64_bin.tar.gz
sudo mv jdk-21.0.2 /usr/local/

# Set JAVA_HOME
export JAVA_HOME=/usr/local/jdk-21.0.2
export PATH=$JAVA_HOME/bin:$PATH

# Add to ~/.bashrc or ~/.zshrc
echo 'export JAVA_HOME=/usr/local/jdk-21.0.2' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

## macOS Installation

### Homebrew (Recommended)

```bash
# Install
brew install openjdk@21

# Link (creates symlink for system Java)
sudo ln -sfn $(brew --prefix openjdk@21)/libexec/openjdk.jdk \
    /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
```

### Direct Download

```bash
# Download from Adoptium or Oracle
# Run .pkg installer
# JAVA_HOME set automatically via /usr/libexec/java_home

# Verify
/usr/libexec/java_home -V  # List all installed versions
/usr/libexec/java_home -v 21  # Get Java 21 home
```

## Windows Installation

### Manual Installation

```powershell
# Download MSI installer from Adoptium or Oracle
# Run installer with default options

# Verify in PowerShell
java -version
javac -version

# JAVA_HOME (set via System Properties > Environment Variables)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", 
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot", 
    "Machine")

# Add to PATH (permanent)
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = "$currentPath;%JAVA_HOME%\bin"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
```

### Chocolatey

```powershell
choco install temurin21
choco install temurin21-devel  # includes javac
```

### Winget

```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

## SDKMAN (Cross-Platform)

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# List available JDKs
sdk list java

# Install a specific JDK
sdk install java 21.0.2-tem    # Temurin 21
sdk install java 21.0.2-amzn   # Amazon Corretto 21
sdk install java 21.0.2-zulu   # Azul Zulu 21

# Switch between versions
sdk use java 21.0.2-tem        # current shell
sdk default java 21.0.2-tem   # all new shells

# Verify
sdk current java
sdk version
```

### SDKMAN Commands

| Command | Description |
|---------|-------------|
| `sdk list java` | List all available JDKs |
| `sdk install java <version>` | Install JDK |
| `sdk use java <version>` | Switch in current shell |
| `sdk default java <version>` | Set default |
| `sdk current java` | Show current version |
| `sdk remove java <version>` | Uninstall |
| `sdk update` | Update SDKMAN |

## JAVA_HOME Configuration

### Finding JAVA_HOME

```bash
# Linux
echo $JAVA_HOME
/usr/libexec/java_home -V 2>/dev/null

# macOS
/usr/libexec/java_home -v 21

# Windows (PowerShell)
[System.Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")

# Cross-platform
java -XshowSettings:properties -version 2>&1 | grep java.home
```

### Shell Profile Setup

```bash
# ~/.bashrc or ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$JAVA_HOME/bin:$PATH

# ~/.profile
if [ -d "$HOME/.sdkman" ]; then
    export SDKMAN_DIR="$HOME/.sdkman"
    [[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"
fi
```

## Verifying Installation

```bash
# Check all tools
java -version          # JRE/JDK version
javac -version         # Compiler version
jar --version          # JAR tool version
javadoc --version      # Documentation generator
jdeps --version        # Dependency analyzer
jlink --version        # Custom runtime image
jpackage --version     # Native packaging

# Verify JAVA_HOME
echo $JAVA_HOME
ls -la $JAVA_HOME/bin/

# Test compilation
echo 'public class Test { public static void main(String[] args) { System.out.println("Hello"); } }' > Test.java
javac Test.java
java Test
rm Test.java Test.class
```

## Multiple JDK Management

```bash
# Linux update-alternatives
sudo update-alternatives --install /usr/bin/java java /path/to/jdk21/bin/java 1
sudo update-alternatives --install /usr/bin/javac javac /path/to/jdk21/bin/javac 1
sudo update-alternatives --config java
sudo update-alternatives --config javac

# macOS with Homebrew
brew install openjdk@17 openjdk@21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# SDKMAN (recommended)
sdk use java 17.0.9-tem    # switch to 17
sdk use java 21.0.2-tem    # switch to 21
```

## References

- [Adoptium Downloads](https://adoptium.net/temurin/releases/)
- [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/)
- [SDKMAN Documentation](https://sdkman.io/usage)

---
**Prerequisites:** None
**Related:** [Java configuration](configuration.md) | [Java project-structure](project-structure.md)
**Next:** [Java core-concepts](core-concepts.md)
