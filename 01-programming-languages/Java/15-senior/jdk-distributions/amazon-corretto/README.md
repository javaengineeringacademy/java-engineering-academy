# Amazon Corretto

## Overview

Amazon Corretto is a free, production-ready distribution of the OpenJDK provided by Amazon Web Services (AWS). It offers long-term support with quarterly updates and performance optimizations for AWS workloads.

## History

### Origins (2018–Present)

- **2018**: Amazon announces Corretto as a no-cost, multi-platform, production-ready distribution of OpenJDK
- **2019**: Corretto 8 released as the first version, based on OpenJDK 8
- **2019**: Corretto 11 released with Amazon's performance enhancements
- **2020**: Corretto achieves TCK certification
- **2021**: Corretto 17 released for Java 17 LTS
- **2023**: Corretto 21 released with latest LTS support

### Why Amazon Created Corretto

- AWS customers needed a **free, supported JDK** for production
- Oracle's licensing changes (OTN) created uncertainty for commercial use
- Amazon wanted to **optimize Java for AWS** infrastructure
- Community needed a **reliable, long-term support** distribution

## Features

### Free Long-Term Support

- **No-cost**: Completely free for all uses (development, testing, production)
- **LTS versions**: JDK 8, 11, 17, 21 (and future LTS releases)
- **Quarterly updates**: Regular security patches and bug fixes
- **Extended support**: Continued updates for LTS versions

### AWS Optimizations

- **Performance tuning**: Optimized for AWS EC2 instances
- **Memory management**: Enhanced garbage collection for cloud workloads
- **Startup optimization**: Faster cold starts for serverless (Lambda)
- **ARM64 support**: Optimized for AWS Graviton processors

### TCK Certified

- All Corretto binaries are **TCK-certified**
- Verified compatibility with Java SE specification
- Safe for production use in regulated environments

### Multi-Platform Support

| Platform | Architectures |
|----------|---------------|
| Linux | x64, ARM64 (Graviton) |
| macOS | x64, ARM64 (Apple Silicon) |
| Windows | x64 |
| Alpine Linux | x64, ARM64 |

## How to Install

### Direct Download

Visit [https://aws.amazon.com/corretto/](https://aws.amazon.com/corretto/) for the latest Corretto binaries.

### Package Managers

```bash
# macOS (Homebrew)
brew install --cask corretto

# Ubuntu/Debian
sudo apt install software-properties-common
sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
sudo apt update
sudo apt install java-21-amazon-corretto-jdk

# Amazon Linux 2023
sudo dnf install java-21-amazon-corretto-devel

# Fedora/RHEL
sudo dnf install java-21-amazon-corretto-devel

# SDKMAN
sdk install java 21-amzn
```

### Docker

```bash
# Official Corretto images
docker pull amazoncorretto:21
docker pull amazoncorretto:21-alpine

# With JDK
docker pull amazoncorretto:21-jdk

# With JRE
docker pull amazoncorretto:21-jre
```

### AWS-Specific Installation

```bash
# Amazon Linux 2023
sudo dnf install java-21-amazon-corretto

# Set as default
sudo alternatives --set java /usr/lib/jvm/java-21-amazon-corretto/bin/java
sudo alternatives --set javac /usr/lib/jvm/java-21-amazon-corretto/bin/javac
```

## When to Choose Corretto

### Choose Corretto When:

- You're running on **AWS** (EC2, ECS, EKS, Lambda, Fargate)
- You need **free LTS support** without commercial subscription
- You want **AWS-optimized performance** (especially Graviton)
- You need **quarterly security updates** for production
- You're building **serverless applications** (Lambda, Step Functions)
- You want a **reliable, well-tested** distribution backed by Amazon

### Avoid Corretto When:

- You're not on AWS (consider Temurin or other distributions)
- You need commercial support with SLA (consider Oracle JDK)
- You need ultra-low latency GC (consider Azul Zing)
- You require vendor-specific tools beyond AWS ecosystem

## Use Cases

### AWS Lambda

```java
// Corretto 21 optimized for Lambda cold starts
public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody("Hello from Corretto!");
    }
}
```

### Docker on AWS ECS/EKS

```dockerfile
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### EC2 with Graviton

```bash
# On Graviton (ARM6) instances
sudo dnf install java-21-amazon-corretto-devel
java -version
# openjdk version "21.0.x" Amazon Corretto 21.0.x.x.x
```

### CI/CD with CodeBuild

```yaml
# buildspec.yml
version: 0.2
phases:
  install:
    commands:
      - yum install -y java-21-amazon-corretto-devel
  build:
    commands:
      - ./gradlew build
```

## Comparison with Other Distributions

| Feature | Corretto | Temurin | Oracle JDK | Zulu |
|---------|----------|---------|------------|------|
| Provider | Amazon | Eclipse Foundation | Oracle | Azul |
| Cost | Free | Free | Free* | Free |
| LTS Support | Yes | Yes | Yes | Yes |
| TCK Certified | Yes | Yes | Yes | Yes |
| AWS Optimization | Yes | No | No | No |
| Graviton Support | Yes | Yes | Yes | Yes |
| Docker Images | Good | Excellent | Good | Good |

*Free under NFTC; paid for commercial support

## Version History

| Version | Release Date | LTS | Key Features |
|---------|--------------|-----|--------------|
| Corretto 8 | 2019 | Yes | Lambdas, Streams, Optional |
| Corretto 11 | 2019 | Yes | HTTP Client, String methods, removals |
| Corretto 17 | 2021 | Yes | Sealed classes, Pattern matching |
| Corretto 21 | 2023 | Yes | Virtual threads, Pattern matching for switch |

## Further Reading

- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Corretto Documentation](https://docs.aws.amazon.com/corretto/)
- [Corretto GitHub](https://github.com/corretto/)
- [AWS Lambda Java](https://docs.aws.amazon.com/lambda/latest/dg/java-package.html)
- [AWS Graviton](https://aws.amazon.com/ec2/graviton/)
