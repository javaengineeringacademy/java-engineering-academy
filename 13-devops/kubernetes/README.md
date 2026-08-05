# Kubernetes Fundamentals

## Overview
Kubernetes (K8s) is an open-source system for automating deployment, scaling, and management of containerized applications.

## Topics
- Pods
- Services
- Deployments
- ReplicaSets
- ConfigMaps and Secrets
- Namespaces
- Ingress
- Persistent Volumes
- StatefulSets
- Helm Charts

## Learning Objectives
- Deploy applications on K8s
- Manage cluster resources
- Implement scaling strategies

## Prerequisites
- Docker basics

## Architecture

```mermaid
graph TD
    subgraph Control Plane
        API[API Server]
        ETCD[etcd]
        SCHED[Scheduler]
        CM[Controller Manager]
    end

    subgraph Worker Node 1
        K1[kubelet]
        KP1[kube-proxy]
        P1[Pod A]
        P2[Pod B]
    end

    subgraph Worker Node 2
        K2[kubelet]
        KP2[kube-proxy]
        P3[Pod C]
        P4[Pod D]
    end

    API --> ETCD
    API --> SCHED
    API --> CM
    SCHED --> K1
    SCHED --> K2
    K1 --> P1
    K1 --> P2
    K2 --> P3
    K2 --> P4

    style API fill:#f96,stroke:#333,stroke-width:2px
    style ETCD fill:#6cf,stroke:#333,stroke-width:2px
    style K1 fill:#fc6,stroke:#333,stroke-width:2px
    style K2 fill:#fc6,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Deployment Scale} -->|Single Container| Docker[Use Docker]
    Start -->|Multiple Containers| K8s[Kubernetes]
    Start -->|Enterprise| K8s

    K8s -->|Simple Apps| Simple[Deployments]
    K8s -->|Stateful Apps| Stateful[StatefulSets]
    K8s -->|Batch Jobs| Jobs[Jobs/CronJobs]

    Simple -->|Web App| Web[Deployment + Service]
    Simple -->|Static Site| Static[Deployment + Ingress]

    Stateful -->|Database| DB[StatefulSet + PVC]
    Stateful -->|Cache| Cache[StatefulSet + Headless]

    Jobs -->|One-time| One[Job]
    Jobs -->|Scheduled| Cron[CronJob]

    style K8s fill:#326ce5,stroke:#333,stroke-width:2px
    style Simple fill:#6cf,stroke:#333,stroke-width:2px
    style Stateful fill:#fc6,stroke:#333,stroke-width:2px
```
