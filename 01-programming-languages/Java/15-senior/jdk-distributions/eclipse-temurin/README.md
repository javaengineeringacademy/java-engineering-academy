# Eclipse Temurin (Adoptium)

## Overview

Eclipse Temurin is the set of production-ready OpenJDK distributions provided by the Eclipse Adoptium project. It is one of the most widely adopted free Java distributions, offering TCK-certified, long-term support binaries for multiple platforms.

## History

### AdoptOpenJDK Era (2017–2021)

- **2017**: AdoptOpenJDK founded as a community-driven project to provide free, pre-built OpenJDK binaries
- **2018**: First AdoptOpenJDK binaries released (JDK 8 and 11)
- **2019**: AdoptOpenJDK becomes the most popular free JDK distribution
- **2020**: Project applies to join Eclipse Foundation for better governance

### Eclipse Adoptium Era (2021–Present)

- **2021**: AdoptOpenJDK moves to Eclipse Foundation, renamed Eclipse Adoptium
- **2021**: Eclipse Temurin brand established (name for the JDK binaries)
- **2022**: Temurin achieves TCK certification for all supported versions
- **2023**: Temurin supports JDK 8, 11, 17, and 21 with LTS

## Features

### TCK Certified

- All Temurin binaries are **TCK-certified** (Technology Compatibility Kit)
- Ensures compatibility with the Java SE specification
- Verified by Eclipse Foundation's automated testing infrastructure
- Safe for production use in regulated environments

### Long-Term Support

- **LTS versions**: JDK 8, 11, 17, 21 (and future LTS releases)
- **Extended support**: Community-supported updates for LTS versions
- **Quarterly releases**: Aligned with OpenJDK release cadence
- **Security patches**: Timely security updates

### Multi-Platform Support

| Platform | Architectures |
|----------|---------------|
| Linux | x64, ARM64, s390x, ppc64le |
| macOS | x64, ARM64 (Apple Silicon) |
| Windows | x64, ARM64 |
| Alpine Linux | x64, ARM64 |
| AIX | ppc64le |

### Quality Assurance

- **Automated testing**: Extensive CI/CD pipeline
- **Compatibility testing**: TCK verification for every release
- **Performance testing**: Benchmarking against reference implementations
- **Security scanning**: Regular vulnerability assessments

## How to Install

### Direct Download

Visit [https://adoptium.net/](https://adoptium.net/) for the latest Temurin binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask temurin

# Ubuntu/Debian
sudo apt install temurin-21-jdk

# Fedora/RHEL
sudo dnf install temurin-21-jdk

# Windows (Chocolatey)
choco install temurin21

# Windows (winget)
winget install EclipseAdoptium.Temurin.21.JDK

# SDKMAN
sdk install java 21-tem
```

### Docker

```bash
# Official Temurin images
docker pull eclipse-temurin:21-jdk
docker pull eclipse-temurin:21-jre

# Alpine variants
docker pull eclipse-temurin:21-jdk-alpine
```

### Available Image Tags

```bash
eclipse-temurin:8-jdk
eclipse-temurin:11-jdk
eclipse-temurin:17-jdk
eclipse-temurin:21-jdk
eclipse-temurin:21-jre
eclipse-temurin:21-jdk-alpine
eclipse-temurin:21-jdk-jammy    # Ubuntu 22.04
eclipse-temurin:21-jdk-noble    # Ubuntu 24.04
```

## When to Choose Temurin

### Choose Temurin When:

- You want a **well-tested, free** distribution with LTS
- You need **excellent Docker/container support**
- You want **community-backed** distribution with Eclipse Foundation governance
- You need **multi-platform support** (including Alpine, ARM64)
- You want the **most widely adopted** free JDK distribution
- You're setting up **CI/CD pipelines** (Jenkins, GitHub Actions, etc.)

### Avoid Temurin When:

- You need commercial support with SLA (consider Oracle JDK or Azul)
- You're in a cloud-specific ecosystem (consider Corretto, Microsoft Build)
- You need ultra-low latency GC (consider Azul Zing)
- You require vendor-specific optimizations

## Comparison with Other Distributions

| Feature | Temurin | OpenJDK | Oracle JDK | Corretto |
|---------|---------|---------|------------|----------|
| TCK Certified | Yes | Community | Yes | Yes |
| LTS Support | Yes | Community | Yes | Yes |
| Docker Images | Excellent | Varies | Good | Good |
| Alpine Support | Yes | No | No | No |
| ARM64 Support | Yes | Varies | Yes | Yes |
| Provider | Eclipse Foundation | Oracle/Community | Oracle | Amazon |

## Use Cases

### CI/CD Pipelines

```yaml
# GitHub Actions example
- uses: actions/setup-java@v4
  with:
    distribution: 'temurin'
    java-version: '21'
```

### Docker Multi-Stage Build

```dockerfile
# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Runtime stage
FROM eclipse-temurin:21-jre
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Local Development

```bash
# Install via SDKMAN
sdk install java 21-tem
sdk use java 21-tem

# Verify installation
java -version
javac -version
```

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| JDK 8 | 2018 | Yes | Lambdas, Streams, Optional |
| JDK 11 | 2018 | Yes | HTTP Client, String methods, removals |
| JDK 17 | 2021 | Yes | Sealed classes, Pattern matching |
| JDK 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

## Further Reading

- [Eclipse Adoptium](https://adoptium.net/)
- [Temurin Documentation](https://adoptium.net/docs/)
- [Temurin GitHub](https://github.com/adoptium/)
- [Available Versions](https://adoptium.net/releases/)
- [Docker Images](https://hub.docker.com/u/eclipsetemurin)
