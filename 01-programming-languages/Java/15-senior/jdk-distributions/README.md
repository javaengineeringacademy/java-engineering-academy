# JDK Distributions Comparison

## Overview

Java SE is a specification; multiple vendors provide compatible implementations (distributions). All distributions must pass the Technology Compatibility Kit (TCK) to be certified as Java SE compatible.

## OpenJDK

| Property | Details |
|----------|---------|
| **Provider** | Oracle (reference implementation) + community |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception |
| **Support** | Community support; Oracle provides patches for mainline |
| **Cost** | Free |
| **Unique Features** | Reference implementation, always latest features first, fully open source |
| **Best For** | Learning, contributing to Java, building custom distributions, bleeding-edge features |
| **When to Choose** | You want the source of truth, plan to build your own JDK, or need the latest features immediately |

## Oracle JDK

| Property | Details |
|----------|---------|
| **Provider** | Oracle Corporation |
| **License** | Oracle No-Fee Terms and Conditions (NFTC) for production; Java SE Subscription for commercial features |
| **Support** | Oracle premier support (paid subscription) |
| **Cost** | Free for development/testing; paid for commercial support and some features |
| **Unique Features** | Oracle-tested, Oracle-specific tools (Flight Recorder historically), commercial monitoring tools |
| **Best For** | Enterprise environments needing Oracle support, compliance, certified binaries |
| **When to Choose** | You need Oracle-backed binaries, commercial support, or are already in the Oracle ecosystem |

## Eclipse Temurin (Adoptium)

| Property | Details |
|----------|---------|
| **Provider** | Eclipse Foundation (formerly AdoptOpenJDK) |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception |
| **Support** | Community + Eclipse Foundation backing |
| **Cost** | Free |
| **Unique Features** | TCK certified, multi-platform binaries, automated testing, LTS support, widely adopted |
| **Best For** | Production workloads, CI/CD pipelines, Docker containers, general-purpose development |
| **When to Choose** | You want a well-tested, free, community-driven distribution with long-term support |

## Amazon Corretto

| Property | Details |
|----------|---------|
| **Provider** | Amazon Web Services |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception |
| **Support** | Amazon support, free quarterly updates |
| **Cost** | Free |
| **Unique Features** | Long-term support (LTS), Amazon-tested, performance optimizations for AWS, no-charge updates for years |
| **Best For** | AWS workloads, production systems needing long-term support, Amazon ecosystem |
| **When to Choose** | You run on AWS, need free LTS, or want Amazon-backed support without cost |

## Azul Zulu

| Property | Details |
|----------|---------|
| **Provider** | Azul Systems |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception (community); commercial license for Zing |
| **Support** | Community (Zulu) + Commercial (Zing/Platform Prime) |
| **Cost** | Zulu: Free; Zing: Commercial (subscription) |
| **Unique Features** | Zing (advanced GC with C4), ReadyNow (warm-up optimization), CRaC (Coordinated Restore at Cloud), best-in-class GC tuning |
| **Best For** | Low-latency applications, financial trading, applications needing advanced garbage collection |
| **When to Choose** | You need ultra-low latency (sub-millisecond GC pauses), warm-up optimization, or enterprise-grade GC |

## Liberica JDK (BellSoft)

| Property | Details |
|----------|---------|
| **Provider** | BellSoft |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception |
| **Support** | Community + Commercial support available |
| **Cost** | Free (standard); Commercial for enterprise support |
| **Unique Features** | Full JRE (not just JDK), Alpine Linux native support, smallest Docker images, GraalVM integration |
| **Best For** | Containerized applications, microservices, Docker, Alpine Linux, Spring Boot |
| **When to Choose** | You need minimal Docker image sizes, Alpine Linux support, or a full JRE for client apps |

## SapMachine

| Property | Details |
|----------|---------|
| **Provider** | SAP |
| **License** | GNU General Public License v2 (GPLv2) + Classpath Exception |
| **Support** | SAP support (free for community, enterprise support available) |
| **Cost** | Free |
| **Unique Features** | SAP-tested, enterprise-grade, ARM64 support, SAP ecosystem integration |
| **Best For** | SAP environments, enterprise Java, SAP Cloud Platform, on-premise SAP systems |
| **When to Choose** | You run SAP software, need SAP-backed binaries, or are in the SAP ecosystem |

## Comparison Table

| Distribution | Provider | License | Cost | LTS | Docker Support | GC Options | Best Use Case |
|-------------|----------|---------|------|-----|----------------|------------|---------------|
| OpenJDK | Oracle/Community | GPLv2+CE | Free | Community | Good | Standard | Reference, learning |
| Oracle JDK | Oracle | NFTC | Free* | Yes | Good | Standard + Flight | Enterprise, compliance |
| Eclipse Temurin | Eclipse Foundation | GPLv2+CE | Free | Yes | Excellent | Standard | Production, CI/CD |
| Amazon Corretto | Amazon | GPLv2+CE | Free | Yes | Good | Standard | AWS, production |
| Azul Zulu | Azul Systems | GPLv2+CE | Free | Yes | Good | Standard | General purpose |
| Azul Zing | Azul Systems | Commercial | Paid | Yes | Good | C4 (advanced) | Low-latency, trading |
| Liberica JDK | BellSoft | GPLv2+CE | Free | Yes | Excellent (Alpine) | Standard | Containers, microservices |
| SapMachine | SAP | GPLv2+CE | Free | Yes | Good | Standard | SAP ecosystem |

*Free under NFTC for production use; paid for commercial support/subscription features

## Choosing a Distribution — Decision Tree

```
Need commercial support?
├── Yes → Oracle JDK (subscription) or Azul Platform Prime
└── No
    ├── Running on AWS? → Amazon Corretto
    ├── Running on SAP? → SapMachine
    ├── Containerized/Alpine? → Liberica JDK
    ├── Need latest features? → OpenJDK or Temurin
    ├── Need ultra-low latency? → Azul Zing
    └── General purpose? → Eclipse Temurin
```

## Key Considerations

1. **Licensing**: GPLv2+CE is permissive; you can distribute modified binaries without open-sourcing your app
2. **LTS versions**: Java 8, 11, 17, 21 are LTS releases
3. **TCK certification**: Ensures compatibility — always verify your distribution is certified
4. **Update frequency**: Most distributions update quarterly; some monthly for security patches
5. **Vendor lock-in risk**: All GPLv2 distributions are interchangeable — no lock-in
