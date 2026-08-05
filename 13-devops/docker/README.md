# Docker Fundamentals

## Overview
Docker is a platform for developing, shipping, and running applications in containers.

## Topics
- Containerization Concepts
- Dockerfile
- Images and Layers
- Containers
- Volumes
- Networks
- Docker Compose
- Multi-stage Builds
- Security Best Practices
- Container Orchestration

## Learning Objectives
- Containerize applications
- Write efficient Dockerfiles
- Manage container lifecycle

## Prerequisites
- Basic command line

## Architecture

```mermaid
graph TD
    A[Dockerfile] --> B[Docker Build]
    B --> C[Docker Image]
    C --> D[Container Runtime]
    D --> E[Running Container]
    E --> F[Docker Registry]

    C --> C1[Base Image Layer]
    C --> C2[Application Layer]
    C --> C3[Config Layer]

    F --> F1[Docker Hub]
    F --> F2[Private Registry]
    F --> F3[ECR/GCR]

    style A fill:#6cf,stroke:#333,stroke-width:2px
    style C fill:#f96,stroke:#333,stroke-width:2px
    style F fill:#bfb,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Application Type} -->|Microservices| Micro[Docker Containers]
    Start -->|Monolith| Mono[Consider VMs]
    Start -->|Dev Environment| Dev[Docker Compose]
    Start -->|Production| Prod[Container Orchestrator]

    Micro -->|Stateless| Stateless[Multi-stage Build]
    Micro -->|Stateful| Stateful[Volume Mounts]

    Dev -->|Local Stack| Stack[Compose YAML]
    Dev -->|Isolation| Isol[Network Namespaces]

    Prod -->|Single Host| Single[Docker Swarm]
    Prod -->|Cluster| Cluster[Kubernetes]

    style Micro fill:#f96,stroke:#333,stroke-width:2px
    style Dev fill:#6cf,stroke:#333,stroke-width:2px
    style Prod fill:#fc6,stroke:#333,stroke-width:2px
```
