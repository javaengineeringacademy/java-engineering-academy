# JDK Distributions

## Overview

Java SE is a specification. Multiple vendors provide compatible implementations (distributions) that must pass the Technology Compatibility Kit (TCK) to be certified as Java SE compatible. While all distributions share the same core APIs, they differ in licensing, support, update cadence, and specialized features.

## Why Multiple Distributions Exist

- **Different vendors** contribute to OpenJDK and build their own distributions
- **Licensing requirements** vary (open source vs commercial)
- **Support models** range from community-driven to enterprise SLAs
- **Platform optimizations** target specific workloads (cloud, low-latency, containers)
- **Cost considerations** — some distributions are completely free, others require subscriptions

## Major Distributions at a Glance

| Distribution | Provider | License | Cost | LTS Support | Best For |
|--------------|----------|---------|------|-------------|----------|
| OpenJDK | Oracle/Community | GPLv2+CE | Free | Community | Reference implementation, learning |
| Oracle JDK | Oracle | NFTC | Free* | Yes | Enterprise, compliance, Oracle ecosystem |
| Eclipse Temurin | Eclipse Foundation | GPLv2+CE | Free | Yes | General production, CI/CD |
| Amazon Corretto | Amazon | GPLv2+CE | Free | Yes | AWS workloads |
| Azul Zulu | Azul Systems | GPLv2+CE | Free | Yes | General purpose |
| Azul Zing | Azul Systems | Commercial | Paid | Yes | Ultra-low latency, trading |
| Liberica JDK | BellSoft | GPLv2+CE | Free | Yes | Containers, Alpine, microservices |
| Microsoft Build | Microsoft | GPLv2+CE | Free | Yes | Azure workloads |
| SapMachine | SAP | GPLv2+CE | Free | Yes | SAP ecosystem |

*Free under NFTC for production use; paid for commercial support

## Decision Framework

### Choose Based on Your Needs

1. **Need commercial support with SLA?**
   - Oracle JDK (Oracle-backed) or Azul Platform Prime (Azul-backed)

2. **Running on a specific cloud provider?**
   - AWS → Amazon Corretto
   - Azure → Microsoft Build of OpenJDK
   - SAP Cloud → SapMachine

3. **Containerized workloads?**
   - Liberica JDK (Alpine-native, smallest images)
   - Eclipse Temurin (excellent Docker support)

4. **Ultra-low latency requirements?**
   - Azul Zing (C4 garbage collector, sub-millisecond pauses)

5. **General purpose / no strong preference?**
   - Eclipse Temurin (widely adopted, well-tested)

6. **Building custom distributions?**
   - OpenJDK (reference implementation, full source)

## Key Considerations

### Licensing

| License | Implications |
|---------|--------------|
| GPLv2+CE | Free to use, modify, distribute; Classpath Exception allows proprietary linking |
| NFTC | Free for production; commercial support requires subscription |
| Commercial | Paid subscription; includes support and SLA |

### Long-Term Support (LTS) Versions

Java LTS releases: **8, 11, 17, 21** (and future releases every 2 years)

Most distributions provide extended support for LTS versions beyond Oracle's public updates.

### TCK Certification

All distributions listed here are TCK-certified, ensuring compatibility with the Java SE specification. Always verify certification for production use.

## Getting Started

### Quick Installation

```bash
# macOS (Homebrew)
brew install --cask temurin       # Eclipse Temurin
brew install --cask corretto      # Amazon Corretto
brew install --cask zulu          # Azul Zulu

# Ubuntu/Debian
sudo apt install openjdk-21-jdk   # OpenJDK (varies by distro)

# SDKMAN (any platform)
sdk install java 21-tem           # Eclipse Temurin
sdk install java 21-amzn          # Amazon Corretto
sdk install java 21-zulu          # Azul Zulu
```

## Directory Structure

- [Oracle JDK](oracle-jdk/README.md) — Oracle's commercial distribution
- [OpenJDK](openjdk/README.md) — The reference implementation
- [Eclipse Temurin](eclipse-temurin/README.md) — Adoptium's community distribution
- [Amazon Corretto](amazon-corretto/README.md) — Amazon's free distribution
- [Azul Zulu](azul-zulu/README.md) — Azul's free and commercial distributions
- [Liberica JDK](liberica/README.md) — BellSoft's container-optimized distribution
- [Microsoft Build](microsoft-build/README.md) — Microsoft's Azure-optimized distribution
- [SapMachine](sapmachine/README.md) — SAP's enterprise distribution
- [Comparison](comparison/README.md) — Detailed side-by-side comparison
- [Oracle vs OpenJDK](oracle-vs-openjdk/README.md) — Deep dive into the two main options

## Further Reading

- [OpenJDK Project](https://openjdk.org/)
- [Oracle Java SE](https://www.oracle.com/java/)
- [Eclipse Adoptium](https://adoptium.net/)
- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Azul Systems](https://www.azul.com/)
- [BellSoft Liberica](https://bell-sw.com/liberica-jdk/)
- [Microsoft Build of OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/)
- [SapMachine](https://.sapmachine.io/)
