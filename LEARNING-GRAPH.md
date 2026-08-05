# Learning Dependency Graph

## Overview

This document maps all major learning paths with their prerequisites and dependencies. Each path represents a progressive skill journey where foundational knowledge is required before advancing to complex topics.

## 1. Java Path

```mermaid
flowchart LR
    A[Java Fundamentals] --> B[OOP]
    B --> C[Collections]
    C --> D[Streams]
    D --> E[Concurrency]
    E --> F[Spring Core]
    F --> G[Spring Boot]
    G --> H[Spring Cloud]
    H --> I[Microservices]
    I --> J[Kubernetes]
```

## 2. Python Path

```mermaid
flowchart LR
    A[Python Basics] --> B[OOP]
    B --> C[Decorators]
    C --> D[Async]
    D --> E[Django/FastAPI]
    E --> F[Data Science]
    F --> G[ML]
```

## 3. Go Path

```mermaid
flowchart LR
    A[Go Basics] --> B[Concurrency]
    B --> C[Web Services]
    C --> D[Microservices]
    D --> E[Cloud Native]
```

## 4. JavaScript Path

```mermaid
flowchart LR
    A[JS Basics] --> B[DOM]
    B --> C[Async]
    C --> D[React/Vue/Angular]
    D --> E[Node.js]
    E --> F[TypeScript]
```

## 5. DevOps Path

```mermaid
flowchart LR
    A[Linux] --> B[Git]
    B --> C[Docker]
    C --> D[CI/CD]
    D --> E[Kubernetes]
    E --> F[Terraform]
    F --> G[Ansible]
    G --> H[Monitoring]
```

## 6. Data Path

```mermaid
flowchart LR
    A[SQL] --> B[PostgreSQL]
    B --> C[Redis]
    C --> D[MongoDB]
    D --> E[Kafka]
    E --> F[Spark]
    F --> G[Flink]
    G --> H[Airflow]
```

## 7. Cloud Path

```mermaid
flowchart LR
    A[Networking] --> B[AWS/Azure/GCP]
    B --> C[Serverless]
    C --> D[Containers]
    D --> E[Orchestration]
    E --> F[Service Mesh]
```

## 8. Security Path

```mermaid
flowchart LR
    A[Networking] --> B[OWASP]
    B --> C[Cryptography]
    C --> D[IAM]
    D --> E[Pen Testing]
    E --> F[Compliance]
```

## 9. Architecture Path

```mermaid
flowchart LR
    A[Design Patterns] --> B[SOLID]
    B --> C[DDD]
    C --> D[System Design]
    D --> E[C4]
    E --> F[ADR]
```

## 10. SRE Path

```mermaid
flowchart LR
    A[Linux] --> B[Networking]
    B --> C[Monitoring]
    C --> D[Logging]
    D --> E[Tracing]
    E --> F[Incident Response]
    F --> G[Chaos Engineering]
```

## Cross-Path Dependencies

```mermaid
flowchart TD
    Linux --> DevOps
    Linux --> SRE
    Networking --> Cloud
    Networking --> Security
    Networking --> SRE
    Containers --> DevOps
    Containers --> Cloud
    Kubernetes --> DevOps
    Kubernetes --> Cloud
    Kafka --> Data
    Spark --> Data
    Monitoring --> SRE
    Monitoring --> DevOps
```
