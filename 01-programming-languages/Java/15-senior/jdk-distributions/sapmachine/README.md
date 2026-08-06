# SapMachine (SAP)

## Overview

SapMachine is a free, production-ready OpenJDK distribution provided by SAP. It is designed for enterprise Java environments, particularly those within the SAP ecosystem, offering long-term support and SAP-optimized features.

## History

### SAP and Java (1995–Present)

- **1995**: SAP early adopter of Java for enterprise applications
- **2000s**: SAP builds Java-based platforms (NetWeaver, SAP Cloud Platform)
- **2017**: SAP contributes to OpenJDK and AdoptOpenJDK
- **2018**: SapMachine introduced as SAP's own OpenJDK distribution
- **2019**: SapMachine achieves TCK certification
- **2021**: SapMachine supports JDK 11, 17 LTS
- **2023**: SapMachine supports JDK 11, 17, 21 LTS

### Why SAP Created SapMachine

- SAP customers needed a **free, supported JDK** for SAP environments
- Optimize Java for **SAP platforms** (S/4HANA, BTP, Cloud Foundry)
- Provide **enterprise-grade support** for mission-critical applications
- Align with **SAP's cloud strategy** (SAP Business Technology Platform)

## Features

### Enterprise-Grade

- **SAP-tested**: Rigorous testing for SAP environments
- **TCK certified**: Fully compatible with Java SE specification
- **LTS support**: JDK 11, 17, 21 (and future LTS releases)
- **Quarterly updates**: Regular security patches and bug fixes

### SAP Optimized

- **S/4HANA compatibility**: Optimized for SAP's flagship ERP
- **SAP Cloud Platform**: First-class support for BTP
- **SAP NetWeaver**: Compatible with legacy SAP platforms
- **SAP Cloud Foundry**: Optimized for Cloud Foundry deployments

### Platform Support

| Platform | Architectures | Notes |
|----------|---------------|-------|
| Linux | x64, ARM64 | SAP-optimized |
| macOS | x64, ARM64 (Apple Silicon) | Standard builds |
| Windows | x64 | SAP-optimized |
| Alpine Linux | x64, ARM64 | Container builds |

### Additional Features

- **SAP-specific patches**: Security and bug fixes for SAP environments
- **Performance tuning**: Optimized for SAP workloads
- **SAP support integration**: Seamless with SAP support contracts
- **SAP documentation**: Comprehensive guides for SAP developers

## How to Install

### Direct Download

Visit [https://sapmachine.io/](https://sapmachine.io/) for the latest SapMachine binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask sapmachine

# Ubuntu/Debian
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0xB199836121949F5
sudo apt-add-repository 'deb https://repos.azul.com/zulu/deb stable main'
sudo apt update
sudo apt install sapmachine-21-jdk

# SDKMAN
sdk install java 21-sapm

# Windows (Chocolatey)
choco install sapmachine21

# Windows (winget)
winget install SAP.SapMachine.21
```

### Docker

```bash
# Official SapMachine images
docker pull sapmachine:21-jdk
docker pull sapmachine:21-jre

# Alpine variants
docker pull sapmachine:21-alpine
```

### SAP-Specific Installation

```bash
# SAP Cloud Platform
# Use SAP's recommended Java version for BTP applications

# SAP Cloud Foundry
# Deploy Java apps with SapMachine as runtime

# SAP S/4HANA
# Use SapMachine for ABAP-based Java extensions
```

## When to Choose SapMachine

### Choose SapMachine When:

- You're running on **SAP platforms** (S/4HANA, BTP, NetWeaver)
- You need **free LTS support** without commercial subscription
- You want **SAP-optimized performance** and compatibility
- You're building **SAP extensions** or **SAP-integrated applications**
- You need **enterprise-grade support** for mission-critical SAP workloads
- You want a **TCK-certified** distribution with LTS

### Avoid SapMachine When:

- You're not in the SAP ecosystem (consider Temurin or other distributions)
- You need commercial support with SLA (consider Oracle JDK)
- You need ultra-low latency GC (consider Azul Zing)
- You want the most widely adopted distribution (consider Temurin)

## Use Cases

### SAP Cloud Platform (BTP)

```java
// Java application for SAP BTP
@WebServlet("/api/sap")
public class SapServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("Hello from SapMachine!");
    }
}
```

### SAP Cloud Foundry

```yaml
# manifest.yml
applications:
  - name: my-sap-app
    path: target/my-app.war
    memory: 512M
    instances: 1
    buildpacks:
      - sap_java_buildpack
```

### Docker with SAP

```dockerfile
FROM sapmachine:21-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### SAP S/4HANA Extension

```java
// CDS view service for S/4HANA
@Service
public class SalesOrderService {
    @Autowired
    private SalesOrderRepository repository;
    
    public List<SalesOrder> getOrders() {
        return repository.findAll();
    }
}
```

## Comparison with Other Distributions

| Feature | SapMachine | Temurin | Corretto | Oracle JDK |
|---------|------------|---------|----------|------------|
| Provider | SAP | Eclipse Foundation | Amazon | Oracle |
| Cost | Free | Free | Free | Free* |
| LTS Support | Yes | Yes | Yes | Yes |
| TCK Certified | Yes | Yes | Yes | Yes |
| SAP Optimization | Yes | No | No | No |
| SAP Integration | Yes | No | No | No |
| Docker Images | Good | Excellent | Good | Good |

*Free under NFTC; paid for commercial support

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| SapMachine 11 | 2018 | Yes | HTTP Client, String methods, removals |
| SapMachine 17 | 2021 | Yes | Sealed classes, Pattern matching |
| SapMachine 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

## Further Reading

- [SapMachine](https://sapmachine.io/)
- [SapMachine Documentation](https://sap.github.io/SapMachine/)
- [SapMachine GitHub](https://github.com/SAP/SapMachine)
- [SAP Cloud Platform Java](https://help.sap.com/viewer/65de2977205c4032a5b358e4d0e54e74/Cloud/en-US)
- [SAP Cloud Foundry Java](https://docs.cloudfoundry.org/buildpacks/java/index.html)
