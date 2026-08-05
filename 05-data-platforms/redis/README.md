# Redis Fundamentals

## Overview
Redis is an open-source, in-memory data structure store used as database, cache, and message broker.

## Topics
- Data Structures (Strings, Lists, Sets, Hashes, Sorted Sets)
- Persistence
- Replication
- Pub/Sub
- Transactions
- Lua Scripting
- Cluster Mode
- Security
- Memory Management
- Use Cases

## Learning Objectives
- Use Redis as cache
- Implement pub/sub patterns
- Optimize memory usage

## Prerequisites
- Basic database concepts

## Architecture

```mermaid
graph TD
    A[Redis Server] --> B[Cache Layer]
    A --> C[Session Store]
    A --> D[Message Queue]
    A --> E[Analytics Engine]
    A --> F[Distributed Lock]

    B --> B1[Page Cache]
    B --> B2[Query Cache]
    C --> C1[User Sessions]
    C --> C2[Shopping Cart]
    D --> D1[Pub/Sub]
    D --> D2[Streams]
    E --> E1[Counters]
    E --> E2[Leaderboards]
    F --> F1[Redlock]
    F --> F2[Mutex]

    style A fill:#f66,stroke:#333,stroke-width:2px
    style B fill:#6cf,stroke:#333,stroke-width:2px
    style D fill:#fc6,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Use Case} -->|Caching| Cache[Redis Cache]
    Start -->|Session| Session[Redis Sessions]
    Start -->|Queue| Queue[Redis Queue]
    Start -->|Real-time| Realtime[Redis Analytics]

    Cache -->|Simple Cache| Simple[String/Hash]
    Cache -->|Pattern Cache| Pattern[Sorted Sets]

    Session -->|Web Session| Web[Hash + TTL]
    Session -->|Shopping Cart| Cart[Hash Structure]

    Queue -->|Task Queue| Task[List + BRPOP]
    Queue -->|Event Stream| Event[Streams]

    Realtime -->|Counters| Counter[INCR/DECR]
    Realtime -->|Leaderboard| Leader[Sorted Sets]

    style Cache fill:#6cf,stroke:#333,stroke-width:2px
    style Session fill:#fc6,stroke:#333,stroke-width:2px
    style Queue fill:#f96,stroke:#333,stroke-width:2px
```
