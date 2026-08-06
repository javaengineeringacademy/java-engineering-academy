# OpenJDK (Reference Implementation)

## Overview

OpenJDK is the open-source reference implementation of the Java SE Platform. It is the foundation upon which all other JDK distributions are built and serves as the canonical source for Java SE development.

## What is OpenJDK?

OpenJDK is:

- The **reference implementation** of Java SE (as defined by the Java Community Process)
- An **open-source project** hosted at [openjdk.org](https://openjdk.org/)
- The **upstream source** for Oracle JDK and most other distributions
- A **community-driven project** with contributions from Oracle, Red Hat, Google, Amazon, Microsoft, and others
- Licensed under **GNU General Public License v2 with Classpath Exception** (GPLv2+CE)

OpenJDK provides:

- The **HotSpot JVM** (virtual machine)
- The **Java Class Library** (standard APIs)
- The **Java compiler** (javac)
- **Development tools** (javadoc, jar, jdeps, jlink, etc.)

## Relationship to Oracle JDK

OpenJDK and Oracle JDK share the same codebase:

| Aspect | OpenJDK | Oracle JDK |
|--------|---------|------------|
| Source code | Fully open source | Built from OpenJDK + Oracle additions |
| Licensing | GPLv2+CE | NFTC (free) / OTN (older) |
| Build process | Community builds | Oracle-certified builds |
| Branding | No branding | Oracle branding |
| Features | Reference features | Reference + Oracle-specific additions |
| Support | Community | Oracle (paid) |

**Key insight**: Oracle JDK is essentially OpenJDK with Oracle-specific build infrastructure, testing, certification, and branding. The core code is nearly identical.

## How to Get OpenJDK

### Official Builds

OpenJDK itself provides source code. Pre-built binaries are available from:

- **Oracle**: [https://jdk.java.net/](https://jdk.java.net/) (GPLv2+CE builds)
- **Eclipse Temurin**: [https://adoptium.net/](https://adoptium.net/)
- **Azul Zulu**: [https://www.azul.com/downloads/](https://www.azul.com/downloads/)
- **Amazon Corretto**: [https://aws.amazon.com/corretto/](https://aws.amazon.com/corretto/)

### Package Managers

```bash
# macOS (Homebrew)
brew install openjdk

# Ubuntu/Debian
sudo apt install openjdk-21-jdk

# Fedora/RHEL
sudo dnf install java-21-openjdk-devel

# Arch Linux
sudo pacman -S jdk21-openjdk

# SDKMAN
sdk install java 21-open
```

### Building from Source

```bash
# Clone the repository
hg clone https://hg.openjdk.java.net/jdk/jdk21
cd jdk22

# Configure and build
bash configure
make images

# Verify
./build/*/images/jdk/bin/java -version
```

## Community Governance

### Project Structure

OpenJDK is governed by the **OpenJDK Governing Board** with representatives from:

- Oracle (Chair)
- Red Hat
- Eclipse Foundation
- Other contributing organizations

### JEP Process

New features are proposed through **JDK Enhancement Proposals (JEPs)**:

1. **Draft**: Initial proposal
2. **Proposed**: Under review
3. **Submitted**: Formally submitted for consideration
4. **Approved**: Accepted for development
5. **Completed**: Implemented and integrated
6. **Closed**: Rejected or withdrawn

### Contributing

- **Bug reports**: [https://bugs.openjdk.java.net/](https://bugs.openjdk.java.net/)
- **Source code**: [https://github.com/openjdk/jdk](https://github.com/openjdk/jdk)
- **Mailing lists**: [https://openjdk.org/lists/](https://openjdk.org/lists/)
- **Quality groups**: Specialized groups for different components

## Version History

OpenJDK releases follow a rapid release model:

- **Feature releases**: Every 6 months (non-LTS)
- **LTS releases**: Every 2 years (JDK 8, 11, 17, 21, ...)
- **Security updates**: Critical patches as needed

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| JDK 7 | July 2011 | Yes | Diamond operator, try-with-resources |
| JDK 8 | March 2014 | Yes | Lambdas, Streams, Optional |
| JDK 9 | September 2017 | No | Module System (Jigsaw) |
| JDK 10 | March 2018 | No | Local variable type inference (var) |
| JDK 11 | September 2018 | Yes | HTTP Client, String methods, removals |
| JDK 12 | March 2019 | No | Switch expressions (preview) |
| JDK 13 | September 2019 | No | Text blocks (preview) |
| JDK 14 | March 2020 | No | Records (preview), Pattern matching (preview) |
| JDK 15 | September 2020 | No | Sealed classes (preview), Text blocks |
| JDK 16 | March 2021 | No | Records, Pattern matching for instanceof |
| JDK 17 | September 2021 | Yes | Sealed classes, Pattern matching for switch (preview) |
| JDK 18 | March 2022 | No | Simple web server, Code snippets in javadoc |
| JDK 19 | September 2022 | No | Virtual threads (preview), Structured concurrency (preview) |
| JDK 20 | March 2023 | No | Scoped values (preview), Record patterns (preview) |
| JDK 21 | September 2023 | Yes | Virtual threads, Pattern matching for switch, Record patterns |

## Key Differences from Other Distributions

| Feature | OpenJDK | Other Distributions |
|---------|---------|---------------------|
| Source availability | Full source code | Built from OpenJDK source |
| Certification | Community TCK | Vendor TCK certification |
| Update cadence | Every 6 months | Varies (quarterly common) |
| LTS support | Community-driven | Vendor-specific support |
| Branding | None | Vendor branding |
| Additional tools | Standard tools | May include vendor tools |

## When to Choose OpenJDK

### Choose OpenJDK When:

- You want the **source of truth** for Java SE
- You plan to **build custom JDK distributions**
- You need **bleeding-edge features** immediately
- You have **in-house Java expertise** and community support is sufficient
- You want **complete freedom** from vendor dependencies
- You're **contributing to Java development** upstream

### Avoid OpenJDK When:

- You need guaranteed long-term support with SLA
- You want pre-built, tested binaries (use Temurin, Corretto, etc.)
- You require commercial support
- You need extended LTS beyond community updates

## Further Reading

- [OpenJDK Project](https://openjdk.org/)
- [OpenJDK GitHub](https://github.com/openjdk/jdk)
- [OpenJDK Bug Database](https://bugs.openjdk.java.net/)
- [JDK Enhancement Proposals](https://openjdk.org/jeps/0)
- [Oracle OpenJDK Builds](https://jdk.java.net/)
