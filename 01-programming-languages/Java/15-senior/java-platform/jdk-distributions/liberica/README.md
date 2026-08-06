# Liberica JDK (BellSoft)

## Overview

Liberica JDK is a free, production-ready OpenJDK distribution provided by BellSoft. It is known for its container-optimized builds, Alpine Linux support, and full JRE option, making it ideal for microservices and containerized workloads.

## History

### BellSoft Origins (2017–Present)

- **2017**: BellSoft founded as a Java technology company
- **2018**: Liberica JDK introduced as a free OpenJDK distribution
- **2019**: Alpine Linux support added for minimal container images
- **2020**: Full JRE option introduced (includes JavaFX and other modules)
- **2021**: Liberica achieves TCK certification
- **2023**: Liberica supports JDK 8, 11, 17, 21 with LTS

### BellSoft's Focus

- **Container-first**: Optimized for Docker and Kubernetes
- **Minimal images**: Smallest possible Docker image sizes
- **Full JRE**: Includes JavaFX and other optional modules
- **Alpine Linux**: Native support for musl-based distributions

## Features

### Container-Optimized

- **Alpine Linux support**: Native musl-based builds for minimal images
- **Smallest Docker images**: Optimized for container deployments
- **Multi-stage builds**: Ideal for Docker multi-stage workflows
- **Kubernetes-ready**: Designed for cloud-native applications

### Full JRE Option

- **Complete runtime**: Includes JavaFX, JNDI-LDAP, and other modules
- **Client applications**: Suitable for desktop and rich client apps
- **JavaFX support**: Built-in JavaFX for UI development
- **All modules**: No need to add additional dependencies

### TCK Certified

- All Liberica binaries are **TCK-certified**
- Verified compatibility with Java SE specification
- Safe for production use in regulated environments

### Platform Support

| Platform | Architectures | Notes |
|----------|---------------|-------|
| Linux | x64, ARM64 | Standard builds |
| Linux (Alpine) | x64, ARM64 | musl-based, minimal |
| macOS | x64, ARM64 (Apple Silicon) | Standard builds |
| Windows | x64 | Standard builds |
| Linux (Full JRE) | x64, ARM64 | Includes JavaFX |

### Docker Images

| Image | Size | Use Case |
|-------|------|----------|
| liberica-jdk-alpine | ~50MB | Minimal containers |
| liberica-jdk | ~100MB | Standard containers |
| liberica-jre-alpine | ~30MB | Runtime only (minimal) |
| liberica-jre | ~70MB | Runtime only (standard) |
| liberica-jdk-full | ~150MB | Includes JavaFX |

## How to Install

### Direct Download

Visit [https://bell-sw.com/liberica-jdk/](https://bell-sw.com/liberica-jdk/) for the latest Liberica binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask liberica-jdk

# Ubuntu/Debian
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0xB199836121949F5
sudo apt-add-repository 'deb https://repos.azul.com/zulu/deb stable main'
sudo apt update
sudo apt install liberica21-jdk

# SDKMAN
sdk install java 21-libr

# Windows (Chocolatey)
choco install liberica21

# Windows (winget)
winget install BellSoft.LibericaJDK.21
```

### Docker

```bash
# Alpine (smallest)
docker pull bellsoft/liberica-jdk-alpine:21
docker pull bellsoft/liberica-jdk-alpine:21-crac  # With CRaC support

# Standard
docker pull bellsoft/liberica-jdk:21

# JRE only
docker pull bellsoft/liberica-jre-alpine:21
docker pull bellsoft/liberica-jre:21

# Full JRE (with JavaFX)
docker pull bellsoft/liberica-jdk-full:21
```

### Docker Multi-Stage Example

```dockerfile
# Build stage
FROM bellsoft/liberica-jdk-alpine:21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Runtime stage (smallest possible)
FROM bellsoft/liberica-jre-alpine:21
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## When to Choose Liberica

### Choose Liberica When:

- You want the **smallest Docker images** for containers
- You need **Alpine Linux support** for minimal deployments
- You require a **full JRE with JavaFX** for client applications
- You're building **microservices** with Kubernetes
- You want a **TCK-certified** distribution with LTS
- You need **CRaC support** for instant recovery (available in Liberica)

### Avoid Liberica When:

- You need commercial support with SLA (consider Oracle JDK or Azul)
- You're in a cloud-specific ecosystem (consider Corretto, Microsoft Build)
- You need ultra-low latency GC (consider Azul Zing)
- You want the most widely adopted distribution (consider Temurin)

## Use Cases

### Minimal Docker Container

```dockerfile
# Smallest possible Java container
FROM bellsoft/liberica-jre-alpine:21
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
# Final image size: ~30MB
```

### Microservices with JavaFX

```dockerfile
# Full JRE with JavaFX
FROM bellsoft/liberica-jdk-full:21
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Spring Boot with Alpine

```dockerfile
# Build stage
FROM bellsoft/liberica-jdk-alpine:21 AS builder
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY src/ src/
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM bellsoft/liberica-jre-alpine:21
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Comparison with Other Distributions

| Feature | Liberica | Temurin | Corretto | Zulu |
|---------|----------|---------|----------|------|
| Provider | BellSoft | Eclipse Foundation | Amazon | Azul |
| Cost | Free | Free | Free | Free |
| Alpine Support | Excellent | Good | Good | Good |
| Full JRE | Yes | No | No | No |
| JavaFX Included | Yes (Full) | No | No | No |
| Smallest Images | Yes | Good | Good | Good |
| CRaC Support | Yes | No | No | Yes (Commercial) |

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| Liberica 8 | 2018 | Yes | Lambdas, Streams, Optional |
| Liberica 11 | 2018 | Yes | HTTP Client, String methods, removals |
| Liberica 17 | 2021 | Yes | Sealed classes, Pattern matching |
| Liberica 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Further Reading

- [BellSoft Liberica JDK](https://bell-sw.com/liberica-jdk/)
- [Liberica Documentation](https://bell-sw.com/pages/liberica-runtime/)
- [Docker Hub](https://hub.docker.com/u/bellsoft)
- [Alpine Linux Support](https://bell-sw.com/liberica-jdk-alpine/)
- [Full JRE](https://bell-sw.com/liberica-jdk-full/)

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
