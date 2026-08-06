# Azul Zulu

## Overview

Azul Zulu is a free, production-ready OpenJDK distribution provided by Azul Systems. It offers both free (Zulu) and commercial (Zing/Platform Prime) distributions, with a focus on performance, low latency, and advanced garbage collection.

## History

### Azul Systems Origins (2002–Present)

- **2002**: Azul Systems founded to build custom hardware and software for Java
- **2005**: Azul Vega custom hardware for Java acceleration
- **2012**: Azul Zulu introduced as a free OpenJDK distribution
- **2017**: Azul Zing gains prominence with C4 garbage collector
- **2019**: Azul Platform Prime introduced (commercial distribution)
- **2021**: Azul achieves TCK certification for Zulu
- **2023**: Azul supports JDK 8, 11, 17, 21 with LTS

### Azul's Differentiation

- **Performance focus**: Advanced GC and JVM optimizations
- **Low latency**: C4 garbage collector for sub-millisecond pauses
- **Enterprise support**: Commercial support and SLA
- **Innovation**: CRaC (Coordinated Restore at Cloud), ReadyNow

## Features

### Azul Zulu (Free Distribution)

- **TCK certified**: Fully compatible with Java SE specification
- **LTS support**: JDK 8, 11, 17, 21 (and future LTS releases)
- **Multi-platform**: Linux, macOS, Windows, ARM64, s390x
- **Free for all uses**: Development, testing, production
- **Community support**: Forums, documentation, community resources

### Azul Platform Prime (Commercial Distribution)

- **C4 garbage collector**: Concurrent, compacting, collection-less GC
- **ReadyNow**: Warm-up optimization for predictable startup
- **CRaC**: Coordinated Restore at Cloud for instant recovery
- **Zing JVM**: Optimized JVM for low-latency workloads
- **Enterprise support**: SLA-backed support and consulting

### Technical Features

| Feature | Zulu (Free) | Platform Prime (Commercial) |
|---------|-------------|----------------------------|
| TCK Certified | Yes | Yes |
| LTS Support | Yes | Yes |
| Standard GC | G1, ZGC, Shenandoah | C4 (advanced) |
| ReadyNow | No | Yes |
| CRaC | No | Yes |
| Performance Tuning | Standard | Advanced |

### Platform Support

| Platform | Architectures |
|----------|---------------|
| Linux | x64, ARM64, s390x, ppc64le |
| macOS | x64, ARM64 (Apple Silicon) |
| Windows | x64, ARM64 |
| Alpine Linux | x64, ARM64 |
| Solaris | SPARC, x64 |

## How to Install

### Direct Download

Visit [https://www.azul.com/downloads/](https://www.azul.com/downloads/) for the latest Azul binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask zulu

# Ubuntu/Debian
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0xB199836121949F5
sudo apt-add-repository 'deb https://repos.azul.com/zulu/deb stable main'
sudo apt update
sudo apt install zulu21-jdk

# Fedora/RHEL
sudo rpm --import https://repos.azul.com/zulu/RPM-GPG-KEY-AZUL
sudo curl -o /etc/yum.repos.d/zulu.repo https://repos.azul.com/zulu/zulu.repo
sudo yum install zulu21-jdk

# SDKMAN
sdk install java 21-zulu

# Windows (Chocolatey)
choco install zulu21

# Windows (winget)
winget install Azul.Zulu.21
```

### Docker

```bash
# Official Zulu images
docker pull azul/zulu:21-jdk
docker pull azul/zulu:21-jre

# Alpine variants
docker pull azul/zulu:21-jdk-alpine

# Specific versions
docker pull azul/zulu:17-jdk
docker pull azul/zulu:11-jdk
docker pull azul/zulu:8-jdk
```

### Platform Prime (Commercial)

Contact Azul Systems for commercial licensing and support.

## When to Choose Azul Zulu

### Choose Zulu (Free) When:

- You want a **free, TCK-certified** distribution with LTS
- You need **multi-platform support** (including Solaris, s390x)
- You want **community support** without commercial subscription
- You're looking for a **well-tested, reliable** distribution
- You need **Alpine Linux** support for containers

### Choose Platform Prime (Commercial) When:

- You need **ultra-low latency** (sub-millisecond GC pauses)
- You require **predictable startup** (ReadyNow)
- You need **instant recovery** (CRaC)
- You're building **financial trading** or **real-time** applications
- You need **enterprise support** with SLA

### Avoid Azul When:

- You're in a cloud-specific ecosystem (consider Corretto, Microsoft Build)
- You need a distribution with cloud provider optimizations
- You want the most widely adopted distribution (consider Temurin)

## Use Cases

### Ultra-Low Latency (Platform Prime)

```bash
# Using C4 garbage collector
java -XX:+UseC4 -XX:MaxGCPauseMillis=1 \
  -jar application.jar

# Using ReadyNow for predictable startup
java -XX:ReadyNowFile=/path/to/profile \
  -XX:+UseC4 \
  -jar application.jar
```

### Docker Containers

```dockerfile
FROM azul/zulu:21-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### CI/CD Pipelines

```yaml
# GitHub Actions
- uses: actions/setup-java@v4
  with:
    distribution: 'zulu'
    java-version: '21'
```

## Comparison with Other Distributions

| Feature | Zulu | Temurin | Corretto | Oracle JDK |
|---------|------|---------|----------|------------|
| Provider | Azul | Eclipse Foundation | Amazon | Oracle |
| Cost | Free | Free | Free | Free* |
| LTS Support | Yes | Yes | Yes | Yes |
| TCK Certified | Yes | Yes | Yes | Yes |
| Advanced GC | C4 (commercial) | Standard | Standard | Standard |
| ReadyNow | Commercial | No | No | No |
| CRaC | Commercial | No | No | No |

*Free under NFTC; paid for commercial support

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| Zulu 8 | 2014 | Yes | Lambdas, Streams, Optional |
| Zulu 11 | 2018 | Yes | HTTP Client, String methods, removals |
| Zulu 17 | 2021 | Yes | Sealed classes, Pattern matching |
| Zulu 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

## Further Reading

- [Azul Systems](https://www.azul.com/)
- [Azul Zulu Downloads](https://www.azul.com/downloads/)
- [Azul Platform Prime](https://www.azul.com/products/prime/)
- [C4 Garbage Collector](https://www.azul.com/products/garbage-collector/)
- [ReadyNow](https://www.azul.com/products/readynow/)
- [CRaC](https://docs.azul.com/crac/)
