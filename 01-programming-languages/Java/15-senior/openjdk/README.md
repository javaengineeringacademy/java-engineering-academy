# OpenJDK — The Reference Implementation

## What is OpenJDK?

OpenJDK is the **reference implementation** of Java SE (Standard Edition). It is the open-source version of the Java Development Kit, providing the canonical implementation of the Java programming language, JVM, and class libraries.

**Key characteristics:**
- Fully open source (GPLv2 + Classpath Exception)
- Reference implementation for all Java SE specifications
- Developed collaboratively by Oracle, Red Hat, Amazon, Google, and community
- Available at [openjdk.org](https://openjdk.org)

## History

### Sun Microsystems Era (1995–2010)
- Java created by James Gosling at Sun Microsystems (1995)
- JDK source code was partially released under various licenses
- Community contributions began through informal channels

### Oracle Acquisition (2010–2011)
- Oracle acquired Sun Microsystems (2010)
- Oracle announced OpenJDK as the open-source reference implementation (2011)
- Java SE 7 became the first OpenJDK release under Oracle's stewardship

### Community Growth (2011–Present)
- OpenJDK became the de facto standard for Java distributions
- Major contributions from Red Hat, Google, Amazon, SAP, and others
- AdoptOpenJDK (now Eclipse Temurin) emerged as a community distribution
- Mercurial → Git migration completed (2020)
- GitHub became the primary hosting platform

## Relationship to Oracle JDK

```
OpenJDK (source code)
    ↓
Oracle JDK (binary distribution)
    ├── Adds Oracle branding
    ├── Adds commercial tools (historically)
    ├── Passes Oracle's internal testing
    └── Receives Oracle's certification
```

**Key relationships:**
- Oracle JDK is built from OpenJDK source
- OpenJDK is the upstream for all Java SE implementations
- Oracle contributes most features to OpenJDK first
- Other vendors (Red Hat, Amazon, etc.) build their distributions from OpenJDK

## How to Get OpenJDK

### Pre-built Binaries
- **Eclipse Temurin**: [adoptium.net](https://adoptium.net) — recommended for most users
- **Amazon Corretto**: [corretto.aws](https://corretto.aws)
- **Azul Zulu**: [azul.com/products/java](https://azul.com/products/java/)
- **Liberica JDK**: [bellsoft.com/products/java](https://bellsoft.com/products/java)

### Build from Source
```bash
git clone https://github.com/openjdk/jdk.git
cd jdk
bash configure
make images
```

### Package Managers
```bash
# macOS
brew install openjdk

# Ubuntu/Debian
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel
```

## Key Projects Inside OpenJDK

### HotSpot
- The default JVM implementation
- JIT compilation, garbage collection, runtime services
- Source: `src/hotspot/`

### Class Libraries
- Core Java APIs (`java.base`, `java.sql`, `java.xml`, etc.)
- Source: `src/java.base/`

### Nashorn (Deprecated)
- JavaScript engine for JVM
- Replaced by GraalVM/JavaScript

### Panama
- Foreign Function & Memory API (Java 22+)
- Interact with native code without JNI

### Valhalla
- Value types and primitive classes
- Performance optimizations for data-oriented programming

### Lilliput
- Reduce object header size
- Compressed class pointers
- Memory efficiency improvements

## OpenJDK Governance

- **JEP process**: Java Enhancement Proposals guide feature development
- **Release cadence**: Every 6 months (non-LTS) and 2 years (LTS)
- **Governance board**: Oracle, Red Hat, Google, Amazon, Microsoft, and others
- **OpenJDK Projects**: Individual features developed in separate projects

## Why OpenJDK Matters

1. **Transparency**: Full source code available for inspection
2. **Innovation**: Community-driven feature development
3. **Compatibility**: Ensures all distributions remain compatible
4. **Freedom**: No vendor lock-in; interchangeable distributions
5. **Education**: Ideal for learning JVM internals
