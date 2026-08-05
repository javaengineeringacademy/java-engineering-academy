# Kotlin Installation

## IntelliJ IDEA

The primary IDE for Kotlin development.

```bash
# Download from https://www.jetbrains.com/idea/
# Community Edition (free) includes Kotlin support
# Ultimate Edition adds framework integrations

# Kotlin plugin is bundled with IntelliJ IDEA
# Verify: Settings > Plugins > Kotlin
```

## SDKMAN

Manage Kotlin and Java SDKs.

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Install Kotlin
sdk install kotlin

# Install specific version
sdk install kotlin 1.9.0

# List available versions
sdk list kotlin

# Use specific version
sdk use kotlin 1.9.0

# Verify
kotlin -version
```

## Gradle Plugin

Add Kotlin to any Gradle project.

```kotlin
// build.gradle.kts
plugins {
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
```

```bash
# Run Kotlin code via Gradle
./gradlew run
./gradlew test
./gradlew build
```

## Kotlin REPL

Interactive Kotlin environment.

```bash
# Start Kotlin REPL
kotlin

# Or with specific classpath
kotlin -classpath path/to/your/classes

# Run a Kotlin script
kotlin script.kts
```

## Command Line Compiler

```bash
# Install Kotlin compiler
# macOS
brew install kotlin

# Linux (manual)
wget https://github.com/JetBrains/kotlin/releases/download/v1.9.0/kotlin-compiler-1.9.0.zip
unzip kotlin-compiler-1.9.0.zip
export PATH=$PATH:/path/to/kotlinc/bin

# Compile
kotlinc Main.kt -include-runtime -d Main.jar

# Run
java -jar Main.jar
```

## Docker

```bash
# Official Kotlin image
docker run -it --rm jetbrains/kotlin-compiler kotlin -version

# Custom Dockerfile
FROM jetbrains/kotlin-compiler:1.9.0
WORKDIR /app
COPY src/ /app/src/
RUN kotlinc src/Main.kt -include-runtime -d app.jar
CMD ["java", "-jar", "app.jar"]
```

## Verification

```bash
kotlin -version          # Check Kotlin version
kotlinc -version         # Check compiler version
gradle --version         # Verify Gradle with Kotlin DSL
```
