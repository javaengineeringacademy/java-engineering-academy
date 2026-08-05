# RabbitMQ Architecture

> Exchange, queue, binding, channel, connection, and broker topology.

## Broker Architecture

```mermaid
graph TB
    subgraph "RabbitMQ Broker"
        A[Connection 1]
        B[Connection 2]
        C[Channel 1.1]
        D[Channel 1.2]
        E[Channel 2.1]
        F[Exchange: orders]
        G[Exchange: logs]
        H[Queue: order-queue]
        I[Queue: log-queue]
        J[Binding]
    end

    A --> C
    A --> D
    B --> E
    C --> F
    D --> F
    E --> G
    F --> J
    J --> H
    G --> I
```

## Core Components

| Component | Description |
|-----------|-------------|
| Broker | Server that receives, stores, and delivers messages |
| Connection | TCP connection between client and broker |
| Channel | Virtual connection within a connection |
| Exchange | Routes messages to queues based on rules |
| Queue | Buffer storing messages until consumed |
| Binding | Rule linking exchange to queue |
| Consumer | Application receiving messages |
| Producer | Application sending messages |

## Connection and Channel Model

```mermaid
graph TB
    subgraph "Producer"
        P[Application]
    end

    subgraph "Connection"
        C1[Channel 1]
        C2[Channel 2]
        C3[Channel 3]
    end

    subgraph "Broker"
        B[Broker Process]
    end

    P --> C1
    P --> C2
    P --> C3
    C1 --> B
    C2 --> B
    C3 --> B
```

### Channel Benefits

- Multiplexing: Many channels share one TCP connection
- Lower overhead than opening new connections
- AMQP operations happen on channels, not connections
- Each channel has its own session state

## Exchange Types

```mermaid
graph TB
    subgraph "Direct Exchange"
        DE[Direct] -->|routing key match| Q1[Queue A]
        DE -->|routing key match| Q2[Queue B]
    end

    subgraph "Topic Exchange"
        TE[Topic] -->|pattern match| Q3[Queue C]
        TE -->|pattern match| Q4[Queue D]
    end

    subgraph "Fanout Exchange"
        FE[Fanout] --> Q5[Queue E]
        FE --> Q6[Queue F]
        FE --> Q7[Queue G]
    end
```

## Message Flow

```mermaid
sequenceDiagram
    participant P as Producer
    participant E as Exchange
    participant B as Binding
    participant Q as Queue
    participant C as Consumer

    P->>E: Publish message
    E->>B: Route by rules
    B->>Q: Enqueue message
    C->>Q: Fetch message
    Q->>C: Deliver message
    C->>Q: Acknowledge
```

## Virtual Hosts

Virtual hosts provide logical isolation within a broker.

| Feature | Description |
|---------|-------------|
| Isolation | Separate exchanges, queues, permissions |
| Namespace | Each vhost has its own resources |
| Security | Permissions scoped to vhost |
| Default | `/` is the default virtual host |

## Erlang Distribution

```mermaid
graph TB
    subgraph "Cluster Node 1"
        N1[Node rabbit@node1]
        M1[Mnesia]
        E1[Erlang Distribution]
    end

    subgraph "Cluster Node 2"
        N2[Node rabbit@node2]
        M2[Mnesia]
        E2[Erlang Distribution]
    end

    subgraph "Cluster Node 3"
        N3[Node rabbit@node3]
        M3[Mnesia]
        E3[Erlang Distribution]
    end

    E1 <--> E2
    E2 <--> E3
    E1 <--> E3
```

## Message Storage

Messages persist in memory and optionally to disk:

| Storage | Description |
|---------|-------------|
| RAM | Fast access, lost on restart if not durable |
| Disk | Durable storage, survives restarts |
| Lazy queues | Move messages to disk to save RAM |
| Quorum queues | Raft-based replicated storage |

## References

- [RabbitMQ Architecture Guide](https://www.rabbitmq.com/tutorials/amqp-concepts)
- [RabbitMQ Clustering](https://www.rabbitmq.com/clustering.html)

---
**Prerequisites:** [RabbitMQ core-concepts](core-concepts.md)
**Related:** [RabbitMQ configuration](configuration.md) | [RabbitMQ production](production.md)
**Next:** [RabbitMQ core-concepts](core-concepts.md)
