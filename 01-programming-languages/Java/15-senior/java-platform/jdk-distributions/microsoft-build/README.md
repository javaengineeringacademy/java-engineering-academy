# Microsoft Build of OpenJDK

## Overview

Microsoft Build of OpenJDK is a free, production-ready distribution of OpenJDK provided by Microsoft. It is optimized for Azure workloads and offers long-term support with regular updates for Java developers on Microsoft platforms.

## History

### Microsoft and Java (1996–Present)

- **1996**: Microsoft licenses Java from Sun Microsystems
- **1997**: Microsoft J++ introduced (Windows-specific Java)
- **2001**: Sun sues Microsoft over Java licensing violations
- **2002**: Microsoft settles with Sun; abandons J++
- **2006**: Microsoft announces support for OpenJDK on Windows
- **2021**: Microsoft Build of OpenJDK introduced
- **2022**: Microsoft achieves TCK certification
- **2023**: Microsoft Build supports JDK 11, 17, 21

### Why Microsoft Created Their Distribution

- Azure customers needed a **free, supported JDK** for cloud workloads
- Optimize Java for **Azure services** (App Service, Functions, AKS)
- Provide **first-class Java support** in Visual Studio Code
- Align with **Microsoft's open-source** strategy

## Features

### Azure Optimized

- **Azure App Service**: Pre-installed and optimized
- **Azure Functions**: Fast cold starts for serverless
- **Azure Kubernetes Service (AKS)**: Container-optimized builds
- **Azure DevOps**: Integrated CI/CD support
- **Visual Studio Code**: Optimized for Java development

### Free Long-Term Support

- **No-cost**: Completely free for all uses
- **LTS versions**: JDK 11, 17, 21 (and future LTS releases)
- **Quarterly updates**: Regular security patches and bug fixes
- **Extended support**: Continued updates for LTS versions

### TCK Certified

- All Microsoft Build binaries are **TCK-certified**
- Verified compatibility with Java SE specification
- Safe for production use in regulated environments

### Platform Support

| Platform | Architectures | Notes |
|----------|---------------|-------|
| Linux | x64, ARM64 | Azure-optimized |
| macOS | x64, ARM64 (Apple Silicon) | Standard builds |
| Windows | x64, ARM64 | Azure-optimized |
| Alpine Linux | x64, ARM64 | Container builds |
| Docker | x64, ARM64 | Azure Container Registry |

### Additional Tools

- **Microsoft OpenJDK for VS Code**: Optimized Java extension pack
- **Azure CLI integration**: Easy deployment and management
- **GitHub Actions**: Pre-configured Java setup action
- **Azure Pipelines**: Native Java support

## How to Install

### Direct Download

Visit [https://learn.microsoft.com/en-us/java/openjdk/download](https://learn.microsoft.com/en-us/java/openjdk/download) for the latest Microsoft Build binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask microsoft-openjdk

# Ubuntu/Debian
sudo apt install microsoft-openjdk-21

# Fedora/RHEL
sudo dnf install microsoft-openjdk-21-jdk

# Windows (winget)
winget install Microsoft.OpenJDK.21

# Windows (Chocolatey)
choco install microsoft-openjdk21

# SDKMAN
sdk install java 21-ms
```

### Docker

```bash
# Official Microsoft Build images
docker pull mcr.microsoft.com/openjdk/jdk:21-ubuntu
docker pull mcr.microsoft.com/openjdk/jdk:21-alpine
docker pull mcr.microsoft.com/openjdk/jdk:21-mariner

# Azure-specific images
docker pull mcr.microsoft.com/azure-functions/java:4-java21
```

### Azure-Specific Installation

```bash
# Azure CLI
az webapp config set --resource-group myGroup --name myApp \
  --java-version 21

# Azure Functions
func init --java
func new --name myFunction --template "HTTP trigger"
```

## When to Choose Microsoft Build

### Choose Microsoft Build When:

- You're running on **Azure** (App Service, Functions, AKS)
- You're using **Visual Studio Code** for Java development
- You need **free LTS support** without commercial subscription
- You want **Azure-optimized performance** and integration
- You're building **serverless applications** on Azure Functions
- You need **first-class Microsoft support** for Java

### Avoid Microsoft Build When:

- You're not on Azure (consider Temurin or other distributions)
- You need commercial support with SLA (consider Oracle JDK)
- You need ultra-low latency GC (consider Azul Zing)
- You want the most widely adopted distribution (consider Temurin)

## Use Cases

### Azure App Service

```bash
# Deploy Java app to Azure App Service
az webapp create --resource-group myGroup --plan myPlan \
  --name myApp --runtime "JAVA:21-java21"

# Configure Java version
az webapp config set --resource-group myGroup --name myApp \
  --java-version 21
```

### Azure Functions

```java
// Azure Functions with Microsoft Build
public class Function {
    @FunctionName("http-trigger")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        return request.createResponseBuilder(HttpStatus.OK).body("Hello!").build();
    }
}
```

### Docker on Azure

```dockerfile
FROM mcr.microsoft.com/openjdk/jdk:21-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### VS Code Development

```json
// settings.json
{
    "java.jdt.ls.java.home": "/path/to/microsoft-jdk-21",
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-21",
            "path": "/path/to/microsoft-jdk-21",
            "default": true
        }
    ]
}
```

## Comparison with Other Distributions

| Feature | Microsoft Build | Temurin | Corretto | Oracle JDK |
|---------|----------------|---------|----------|------------|
| Provider | Microsoft | Eclipse Foundation | Amazon | Oracle |
| Cost | Free | Free | Free | Free* |
| LTS Support | Yes | Yes | Yes | Yes |
| TCK Certified | Yes | Yes | Yes | Yes |
| Azure Optimization | Yes | No | No | No |
| VS Code Integration | Yes | No | No | No |
| Docker Images | Good | Excellent | Good | Good |

*Free under NFTC; paid for commercial support

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| Microsoft Build 11 | 2021 | Yes | HTTP Client, String methods, removals |
| Microsoft Build 17 | 2021 | Yes | Sealed classes, Pattern matching |
| Microsoft Build 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

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

- [Microsoft Build of OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/)
- [Microsoft Build Downloads](https://learn.microsoft.com/en-us/java/openjdk/download)
- [Azure Java Documentation](https://learn.microsoft.com/en-us/azure/java/)
- [VS Code Java](https://code.visualstudio.com/docs/languages/java)
- [Azure Functions Java](https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-java)

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
