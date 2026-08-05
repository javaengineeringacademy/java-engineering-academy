# Kafka Fundamentals

## Overview
Apache Kafka is a distributed event streaming platform for high-performance data pipelines.

## Topics
- Topics and Partitions
- Producers and Consumers
- Consumer Groups
- Offsets
- Replication
- Serialization
- Kafka Connect
- Kafka Streams
- Schema Registry
- Monitoring

## Learning Objectives
- Build event-driven systems
- Implement streaming pipelines
- Ensure data reliability

## Prerequisites
- Distributed systems basics

## Architecture

```mermaid
graph LR
    P1[Producer 1] --> B1[Broker 1]
    P2[Producer 2] --> B1
    P1 --> B2[Broker 2]
    P2 --> B2

    B1 --> T1[Topic: orders]
    B2 --> T1
    B1 --> T2[Topic: events]
    B2 --> T2

    T1 --> CG1[Consumer Group A]
    T2 --> CG2[Consumer Group B]

    CG1 --> C1[Consumer 1]
    CG1 --> C2[Consumer 2]
    CG2 --> C3[Consumer 3]

    ZK[ZooKeeper/KRaft] -.-> B1
    ZK -.-> B2

    style B1 fill:#f96,stroke:#333,stroke-width:2px
    style B2 fill:#f96,stroke:#333,stroke-width:2px
    style ZK fill:#6cf,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Requirements} -->|Event Streaming| Kafka[Choose Kafka]
    Start -->|Task Queue| Rabbit[Choose RabbitMQ]
    Start -->|Log Aggregation| Log[Kafka Logs]
    Start -->|Real-time| Real[Kafka Streams]

    Kafka -->|High Throughput| High[Partitioned Topics]
    Kafka -->|Durability| Durable[Replicated Topics]
    Kafka -->|Ordering| Order[Single Partition]

    Log -->|Metrics| Metrics[Metrics Pipeline]
    Log -->|Audit| Audit[Audit Trail]

    Real -->|Transform| Transform[Stream Processing]
    Real -->|Aggregate| Aggregate[Window Operations]

    style Kafka fill:#f96,stroke:#333,stroke-width:2px
    style High fill:#6cf,stroke:#333,stroke-width:2px
    style Real fill:#bfb,stroke:#333,stroke-width:2px
```
