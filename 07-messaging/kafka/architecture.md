# Kafka Architecture

> Broker, partition, ISR, controller, ZooKeeper/KRaft, and consumer group architecture.

## Cluster Architecture

```mermaid
graph TB
    subgraph "Kafka Cluster"
        A[Broker 1]
        B[Broker 2]
        C[Broker 3]
        D[Controller]
    end
    
    E[Producer 1] --> A
    E --> B
    F[Producer 2] --> C
    
    A --> G[Topic: orders]
    B --> G
    C --> G
    
    G --> H[Partition 0]
    G --> I[Partition 1]
    G --> J[Partition 2]
    
    H --> K[Consumer Group A]
    I --> K
    J --> L[Consumer Group B]
```

## Core Components

| Component | Description |
|-----------|-------------|
| Broker | Server storing and serving data |
| Topic | Logical category of messages |
| Partition | Ordered, immutable sequence of records |
| Offset | Unique ID for each record in partition |
| Replica | Copy of partition for fault tolerance |
| ISR | In-Sync Replicas - replicas caught up with leader |
| Controller | Broker managing partition leadership |
| ZooKeeper/KRaft | Cluster coordination service |

## Partition Distribution

```mermaid
graph TB
    subgraph "Topic: orders (3 partitions)"
        P0[Partition 0]
        P1[Partition 1]
        P2[Partition 2]
    end
    
    subgraph "Brokers"
        B1[Broker 1]
        B2[Broker 2]
        B3[Broker 3]
    end
    
    P0 --> B1
    P1 --> B2
    P2 --> B3
    
    P0 -.-> B2[Replica]
    P1 -.-> B3[Replica]
    P2 -.-> B1[Replica]
```

## Replication Flow

```mermaid
sequenceDiagram
    participant P as Producer
    participant L as Leader
    participant F as Follower
    participant C as Consumer
    
    P->>L: Send message
    L->>L: Write to log
    L->>F: Replicate
    F->>L: Ack (ISR)
    L->>P: Ack (min.insync.replicas)
    C->>L: Fetch messages
    L->>C: Return messages
```

## ZooKeeper Architecture (Legacy)

```mermaid
graph TB
    subgraph "ZooKeeper Cluster"
        ZK1[ZK Node 1]
        ZK2[ZK Node 2]
        ZK3[ZK Node 3]
    end
    
    subgraph "Kafka Cluster"
        B1[Broker 1]
        B2[Broker 2]
        B3[Broker 3]
        C[Controller]
    end
    
    C --> ZK1
    C --> ZK2
    C --> ZK3
    B1 --> ZK1
    B2 --> ZK2
    B3 --> ZK3
    
    ZK1 --> ZK2
    ZK2 --> ZK3
```

### ZooKeeper Responsibilities

| Responsibility | Description |
|----------------|-------------|
| Broker Registration | Track active brokers |
| Topic Metadata | Partition assignments, configs |
| Controller Election | Elect controller broker |
| ISR Management | Track in-sync replicas |
| ACL Management | Access control lists |

## KRaft Mode (Java 11+)

```mermaid
graph TB
    subgraph "KRaft Controller Quorum"
        K1[Controller 1 - Leader]
        K2[Controller 2 - Follower]
        K3[Controller 3 - Follower]
    end
    
    subgraph "Kafka Brokers"
        B1[Broker 1]
        B2[Broker 2]
        B3[Broker 3]
    end
    
    K1 --> B1
    K1 --> B2
    K1 --> B3
    K2 -.-> K1
    K3 -.-> K1
```

### KRaft vs ZooKeeper

| Feature | ZooKeeper | KRaft |
|---------|-----------|-------|
| External Service | Yes | No (built-in) |
| Metadata Storage | ZK + Kafka | Kafka only |
| Scalability | Limited by ZK | Scales to millions of partitions |
| Operations | More complex | Simpler |
| Default Since | Legacy | Kafka 3.3+ |

## Consumer Group Architecture

```mermaid
graph TB
    subgraph "Consumer Group: order-service"
        C1[Consumer 1]
        C2[Consumer 2]
        C3[Consumer 3]
    end
    
    subgraph "Topic: orders (6 partitions)"
        P0[Partition 0]
        P1[Partition 1]
        P2[Partition 2]
        P3[Partition 3]
        P4[Partition 4]
        P5[Partition 5]
    end
    
    P0 --> C1
    P1 --> C1
    P2 --> C2
    P3 --> C2
    P4 --> C3
    P5 --> C3
```

### Consumer Group Rules

1. Each partition consumed by exactly one consumer in group
2. Consumers within group share load across partitions
3. More consumers than partitions = idle consumers
4. Each consumer group has independent offset tracking

## Record Flow

```mermaid
graph LR
    A[Producer] --> B[Broker]
    B --> C[Log Segment]
    C --> D[Index]
    C --> E[TimeIndex]
    B --> F[Replication]
    F --> G[Follower Broker]
    H[Consumer] --> B
    B --> I[Fetch Response]
```

## Log Structure

```
/kafka-logs/
├── orders-0/
│   ├── 00000000000000000000.log
│   ├── 00000000000000000000.index
│   ├── 00000000000000000000.timeindex
│   └── leader-epoch-checkpoint
├── orders-1/
│   └── ...
└── __consumer_offsets-0/
    └── ...
```

## References

- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka Internals](https://kafka.apache.org/documentation/#internals)

---
**Prerequisites:** [Kafka core-concepts](core-concepts.md)
**Related:** [Kafka configuration](configuration.md) | [Kafka production](../../14-cloud/azure/production.md)
**Next:** [Kafka configuration](configuration.md)
