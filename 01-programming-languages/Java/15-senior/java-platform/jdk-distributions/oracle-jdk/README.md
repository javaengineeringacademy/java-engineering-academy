# Oracle JDK

## Overview

Oracle JDK is Oracle Corporation's commercial distribution of the Java Development Kit. It is the direct descendant of the original Sun Microsystems JDK and remains one of the most widely used Java distributions in enterprise environments.

## History

### Sun Microsystems Era (1995–2010)

- **1995**: Sun Microsystems releases Java 1.0 with the original JDK
- **2006**: Sun open-sources HotSpot JVM under GPLv2
- **2007**: OpenJDK project founded as the open-source reference implementation
- **2009**: Oracle acquires Sun Microsystems

### Oracle Era (2010–Present)

- **2011**: Oracle JDK 7 released; first major release under Oracle
- **2014**: Oracle JDK 8 released with Lambda expressions
- **2017**: Java 9 introduces modular system (Jigsaw)
- **2018**: Oracle changes licensing from BCL to OTN for JDK 11+
- **2021**: Java 17 LTS released under NFTC (free for production)
- **2023**: Java 21 LTS; Oracle continues quarterly updates

## Licensing

### Binary Code License (BCL) — Pre-2018

- Free for development and testing
- Production use required a paid Java SE Subscription
- Commercial features (Flight Recorder, Mission Control) required subscription

### Oracle Technology Network (OTN) — 2018–2021

- Free for development and testing
- Production use required subscription
- Applied to JDK 11 through JDK 16

### No-Fee Terms and Conditions (NFTC) — 2021–Present

- Free for all uses (development, testing, production)
- No subscription required for production use
- Commercial support available via Oracle Java SE Subscription
- Applies to JDK 17 and later LTS releases

### Oracle Java SE Subscription (Commercial Support)

- Paid subscription for production support
- Includes Oracle support SLA, patches, and updates
- Required for extended long-term support beyond public updates

## Features

### Core Features (Shared with OpenJDK)

- **HotSpot JVM**: Industry-standard JVM with JIT compilation
- **C1/C2 Compilers**: Client and server JIT compilers
- **Garbage Collectors**: G1, ZGC, Shenandoah, Serial, Parallel
- **Java Module System**: Strong encapsulation and reliable configuration

### Oracle-Specific Features

- **Java Flight Recorder (JFR)**: Low-overhead profiling and diagnostics (now open source)
- **Java Mission Control (JMC)**: Advanced monitoring and management console
- **Oracle Cloud Integration**: Optimized for Oracle Cloud Infrastructure (OCI)
- **Oracle GraalVM**: Optional GraalVM integration for polyglot and native compilation
- **Oracle branding and certification**: Oracle-tested and certified binaries

### Enterprise Features

- **Commercial cryptography providers**: Oracle JCE providers
- **Oracle-specific tools**: JConsole enhancements, VisualVM integration
- **Priority bug fixes**: Critical patches addressed faster with subscription
- **Long-term support**: Extended LTS available for 8+ years

## How to Install

### Direct Download

Visit [Oracle Java SE Downloads](https://www.oracle.com/java/technologies/downloads/) for the latest JDK versions.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask oracle-jdk

# Linux (YUM/DNF)
sudo yum install java-21-oracle-jdk

# Linux (APT)
sudo apt install oracle-java21-installer

# SDKMAN
sdk install java 21-oracle
```

### Docker

```bash
docker pull container-registry.oracle.com/java/jdk:21
```

## When to Choose Oracle JDK

### Choose Oracle JDK When:

- You need **Oracle-backed support** with guaranteed SLA
- **Compliance requirements** mandate Oracle-certified binaries
- You are in the **Oracle ecosystem** (Oracle Database, OCI, WebLogic)
- You need **extended LTS** with guaranteed support beyond community updates
- You require **Oracle-specific tools** and integrations
- Your organization already has an **Oracle Java SE Subscription**

### Avoid Oracle JDK When:

- You need a completely free, no-strings-attached distribution
- You want to avoid vendor lock-in
- You plan to create custom distributions
- You have in-house Java expertise and community support is sufficient

## Cost

| Use Case | Cost |
|----------|------|
| Development | Free |
| Testing | Free |
| Production (NFTC) | Free |
| Commercial Support | Paid subscription (~$25/CPU/month) |
| Extended LTS | Included with subscription |
| Training/Certification | Oracle University (paid) |

## Support Model

- **Public updates**: Free for current releases and LTS (Java 17+)
- **Premier support**: Paid subscription with SLA guarantees
- **Extended support**: Available for LTS releases beyond public update period
- **Security patches**: Critical patches available to subscribers

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| JDK 8 | March 2014 | Yes | Lambdas, Streams, Optional |
| JDK 9 | September 2017 | No | Module System (Jigsaw) |
| JDK 10 | March 2018 | No | Local variable type inference (var) |
| JDK 11 | September 2018 | Yes | HTTP Client, String methods, removals |
| JDK 17 | September 2021 | Yes | Sealed classes, Pattern matching |
| JDK 21 | September 2023 | Yes | Virtual threads, Pattern matching for switch |

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

## Further Reading

- [Oracle Java SE Documentation](https://docs.oracle.com/en/java/javase/)
- [Oracle Java SE Downloads](https://www.oracle.com/java/technologies/downloads/)
- [Oracle Java SE Subscription](https://www.oracle.com/java/java-se-subscription/)
- [Oracle Cloud Infrastructure Java](https://docs.oracle.com/en-us/iaas/Content/Java/Concepts/javaoverview.htm)

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
