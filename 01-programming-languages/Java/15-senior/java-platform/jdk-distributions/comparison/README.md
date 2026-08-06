# JDK Distribution Comparison

## Complete Comparison Table

| Distribution | Provider | License | Cost | LTS Support | TCK Certified | Unique Features | Best For |
|--------------|----------|---------|------|-------------|---------------|-----------------|----------|
| OpenJDK | Oracle/Community | GPLv2+CE | Free | Community | Community | Reference implementation, full source | Learning, building custom JDKs |
| Oracle JDK | Oracle | NFTC | Free* | Yes | Yes | Flight Recorder, Oracle tools, certification | Enterprise, compliance, Oracle ecosystem |
| Eclipse Temurin | Eclipse Foundation | GPLv2+CE | Free | Yes | Yes | TCK certified, excellent Docker support | General production, CI/CD |
| Amazon Corretto | Amazon | GPLv2+CE | Free | Yes | Yes | AWS optimized, Graviton support | AWS workloads, serverless |
| Azul Zulu | Azul Systems | GPLv2+CE | Free | Yes | Yes | Multi-platform, Alpine support | General purpose, cross-platform |
| Azul Zing | Azul Systems | Commercial | Paid | Yes | Yes | C4 GC, ReadyNow, CRaC | Ultra-low latency, trading |
| Liberica JDK | BellSoft | GPLv2+CE | Free | Yes | Yes | Full JRE, Alpine, smallest images | Containers, microservices |
| Microsoft Build | Microsoft | GPLv2+CE | Free | Yes | Yes | Azure optimized, VS Code integration | Azure workloads |
| SapMachine | SAP | GPLv2+CE | Free | Yes | Yes | SAP optimized, enterprise support | SAP ecosystem |

*Free under NFTC for production use; paid for commercial support

## Detailed Feature Comparison

### Licensing and Cost

| Distribution | License | Free for Production | Commercial Support | Cost |
|--------------|---------|--------------------|--------------------|------|
| OpenJDK | GPLv2+CE | Yes | Community only | Free |
| Oracle JDK | NFTC | Yes | Oracle Subscription | Free + Paid |
| Eclipse Temurin | GPLv2+CE | Yes | Community only | Free |
| Amazon Corretto | GPLv2+CE | Yes | Community only | Free |
| Azul Zulu | GPLv2+CE | Yes | Azul Subscription | Free + Paid |
| Azul Zing | Commercial | No | Azul Subscription | Paid |
| Liberica JDK | GPLv2+CE | Yes | BellSoft Subscription | Free + Paid |
| Microsoft Build | GPLv2+CE | Yes | Community only | Free |
| SapMachine | GPLv2+CE | Yes | SAP Subscription | Free + Paid |

### Long-Term Support (LTS)

| Distribution | JDK 8 | JDK 11 | JDK 17 | JDK 21 | Update Cadence |
|--------------|-------|--------|--------|--------|----------------|
| OpenJDK | Yes | Yes | Yes | Yes | Every 6 months |
| Oracle JDK | Yes | Yes | Yes | Yes | Quarterly |
| Eclipse Temurin | Yes | Yes | Yes | Yes | Quarterly |
| Amazon Corretto | Yes | Yes | Yes | Yes | Quarterly |
| Azul Zulu | Yes | Yes | Yes | Yes | Quarterly |
| Liberica JDK | Yes | Yes | Yes | Yes | Quarterly |
| Microsoft Build | No | Yes | Yes | Yes | Quarterly |
| SapMachine | No | Yes | Yes | Yes | Quarterly |

### Platform Support

| Distribution | Linux x64 | Linux ARM64 | macOS x64 | macOS ARM64 | Windows x64 | Alpine | s390x | ppc64le |
|--------------|-----------|-------------|-----------|-------------|-------------|--------|-------|---------|
| OpenJDK | Yes | Yes | Yes | Yes | Yes | No | Yes | Yes |
| Oracle JDK | Yes | Yes | Yes | Yes | Yes | No | No | No |
| Eclipse Temurin | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Amazon Corretto | Yes | Yes | Yes | Yes | Yes | Yes | No | No |
| Azul Zulu | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Liberica JDK | Yes | Yes | Yes | Yes | Yes | Yes | No | No |
| Microsoft Build | Yes | Yes | Yes | Yes | Yes | Yes | No | No |
| SapMachine | Yes | Yes | Yes | Yes | Yes | Yes | No | No |

### Docker Support

| Distribution | Official Images | Alpine Images | ARM64 Images | Image Size |
|--------------|-----------------|---------------|--------------|------------|
| OpenJDK | Varies | Varies | Varies | Varies |
| Oracle JDK | Yes | No | Yes | Medium |
| Eclipse Temurin | Excellent | Yes | Yes | Small |
| Amazon Corretto | Good | Yes | Yes | Small |
| Azul Zulu | Good | Yes | Yes | Small |
| Liberica JDK | Excellent | Yes | Yes | Smallest |
| Microsoft Build | Good | Yes | Yes | Small |
| SapMachine | Good | Yes | Yes | Small |

### Garbage Collection Options

| Distribution | G1 | ZGC | Shenandoah | Serial | Parallel | C4 | ReadyNow |
|--------------|-----|-----|------------|--------|----------|-----|----------|
| OpenJDK | Yes | Yes | Yes | Yes | Yes | No | No |
| Oracle JDK | Yes | Yes | Yes | Yes | Yes | No | No |
| Eclipse Temurin | Yes | Yes | Yes | Yes | Yes | No | No |
| Amazon Corretto | Yes | Yes | Yes | Yes | Yes | No | No |
| Azul Zulu | Yes | Yes | Yes | Yes | Yes | No | No |
| Azul Zing | No | No | No | No | No | Yes | Yes |
| Liberica JDK | Yes | Yes | Yes | Yes | Yes | No | No |
| Microsoft Build | Yes | Yes | Yes | Yes | Yes | No | No |
| SapMachine | Yes | Yes | Yes | Yes | Yes | No | No |

## Decision Tree

```
Need commercial support with SLA?
├── Yes → Oracle JDK (Oracle-backed) or Azul Platform Prime (Azul-backed)
└── No
    │
    Running on a specific cloud provider?
    ├── AWS → Amazon Corretto
    ├── Azure → Microsoft Build of OpenJDK
    ├── SAP Cloud → SapMachine
    └── No specific cloud
        │
        Containerized workloads?
        ├── Yes
        │   ├── Need smallest images? → Liberica JDK (Alpine)
        │   ├── Need best Docker support? → Eclipse Temurin
        │   └── General containers? → Eclipse Temurin or Liberica
        └── No
            │
            Ultra-low latency requirements?
            ├── Yes → Azul Zing (C4 garbage collector)
            └── No
                │
                Need specific features?
                ├── Full JRE with JavaFX → Liberica JDK
                ├── Multi-platform (Solaris, s390x) → Azul Zulu
                ├── SAP ecosystem → SapMachine
                ├── Building custom JDK → OpenJDK (source)
                └── No specific requirements
                    │
                    General purpose?
                    └── Eclipse Temurin (most widely adopted, well-tested)
```

## Use Case Recommendations

### Enterprise Applications

| Scenario | Recommended Distribution | Reason |
|----------|-------------------------|--------|
| Oracle ecosystem | Oracle JDK | Oracle support and integration |
| AWS deployment | Amazon Corretto | AWS optimization and support |
| Azure deployment | Microsoft Build | Azure optimization |
| SAP environment | SapMachine | SAP optimization |
| Cloud-agnostic | Eclipse Temurin | Widely supported, well-tested |

### Containerized Applications

| Scenario | Recommended Distribution | Reason |
|----------|-------------------------|--------|
| Minimal image size | Liberica JDK | Smallest Alpine images |
| Best Docker support | Eclipse Temurin | Excellent Docker integration |
| AWS containers | Amazon Corretto | AWS optimized |
| Azure containers | Microsoft Build | Azure optimized |
| Multi-architecture | Azul Zulu | Widest platform support |

### Performance-Critical Applications

| Scenario | Recommended Distribution | Reason |
|----------|-------------------------|--------|
| Ultra-low latency | Azul Zing | C4 garbage collector |
| Predictable startup | Azul Zing | ReadyNow optimization |
| High throughput | Any (G1/ZGC) | Standard GC options |
| Memory-constrained | Liberica JDK | Smallest footprint |

### Development Environments

| Scenario | Recommended Distribution | Reason |
|----------|-------------------------|--------|
| Learning/education | OpenJDK | Reference implementation |
| VS Code development | Microsoft Build | VS Code integration |
| IntelliJ IDEA | Any distribution | IDE-agnostic |
| CI/CD pipelines | Eclipse Temurin | Widely supported |

## Migration Guide

### From Oracle JDK to OpenJDK/Temurin

1. **Check dependencies**: Run `jdeps` to identify Oracle-specific APIs
2. **Test thoroughly**: Run your test suite
3. **Replace binaries**: Swap JDK installation
4. **Update build scripts**: Change JAVA_HOME and build tool configurations
5. **Verify licensing**: Ensure compliance with GPLv2+CE

### From One Distribution to Another

1. **Verify TCK certification**: Ensure target distribution is certified
2. **Check LTS support**: Verify target distribution supports your Java version
3. **Test compatibility**: Run your application test suite
4. **Update deployment**: Change JDK in Docker images, CI/CD pipelines
5. **Monitor performance**: Benchmark critical workloads

## Quick Reference

### Installation Commands

```bash
# SDKMAN (any distribution)
sdk install java 21-tem           # Eclipse Temurin
sdk install java 21-amzn          # Amazon Corretto
sdk install java 21-zulu          # Azul Zulu
sdk install java 21-libr          # Liberica JDK
sdk install java 21-ms            # Microsoft Build
sdk install java 21-sapm          # SapMachine
sdk install java 21-oracle        # Oracle JDK

# Homebrew (macOS)
brew install --cask temurin       # Eclipse Temurin
brew install --cask corretto      # Amazon Corretto
brew install --cask zulu          # Azul Zulu
brew install --cask liberica-jdk  # Liberica JDK
brew install --cask microsoft-openjdk  # Microsoft Build
brew install --cask sapmachine    # SapMachine
brew install --cask oracle-jdk    # Oracle JDK
```

### Docker Images

```bash
# Eclipse Temurin
docker pull eclipse-temurin:21-jdk
docker pull eclipse-temurin:21-jre-alpine

# Amazon Corretto
docker pull amazoncorretto:21
docker pull amazoncorretto:21-alpine

# Azul Zulu
docker pull azul/zulu:21-jdk
docker pull azul/zulu:21-alpine

# Liberica JDK
docker pull bellsoft/liberica-jdk-alpine:21
docker pull bellsoft/liberica-jre-alpine:21

# Microsoft Build
docker pull mcr.microsoft.com/openjdk/jdk:21-ubuntu
docker pull mcr.microsoft.com/openjdk/jdk:21-alpine

# SapMachine
docker pull sapmachine:21-jdk
docker pull sapmachine:21-alpine

# Oracle JDK
docker pull container-registry.oracle.com/java/jdk:21
```

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

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Further Reading

- [OpenJDK](https://openjdk.org/)
- [Oracle Java SE](https://www.oracle.com/java/)
- [Eclipse Adoptium](https://adoptium.net/)
- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Azul Systems](https://www.azul.com/)
- [BellSoft Liberica](https://bell-sw.com/liberica-jdk/)
- [Microsoft Build of OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/)
- [SapMachine](https://sapmachine.io/)

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
