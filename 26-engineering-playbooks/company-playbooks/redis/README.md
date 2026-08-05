# Redis Internals Playbook

## Overview

Redis is an open-source, in-memory data structure store used as a database, cache, message broker, and streaming engine. Its high performance, flexible data types, and simplicity make it a foundational component in modern architectures.

## Data Structures

### Strings

Strings are the most basic Redis data type, storing text or binary data up to 512MB. Strings support atomic operations like increment, decrement, and append, making them suitable for counters, locks, and simple caches.

### Lists

Lists are linked lists of strings, supporting push and pop operations at both ends with O(1) complexity. Lists are useful for queues, stacks, and recent item tracking. Blocking pop operations enable queue-like patterns.

### Hashes

Hashes store maps of field-value pairs, efficient for storing objects with multiple attributes. Hashes support partial updates, reading or writing individual fields without affecting the entire structure.

### Sets

Sets are unordered collections of unique strings, supporting operations like intersection, union, and difference. Sets are useful for tagging, membership tracking, and unique item collections.

### Sorted Sets

Sorted sets associate each member with a score, maintaining members in sorted order. They support range queries, rank lookups, and priority queues. Sorted sets are ideal for leaderboards, time series, and scheduling.

### Streams

Streams provide an append-only log data structure, similar to Kafka topics. They support consumer groups for parallel processing, message acknowledgment, and persistence. Streams enable event sourcing and real-time data pipelines.

## Persistence

### RDB Snapshots

RDB creates point-in-time snapshots of the dataset at specified intervals. Snapshots are compact and fast to load, but may lose data written since the last snapshot. RDB is suitable for backups and disaster recovery.

### AOF (Append-Only File)

AOF logs every write operation, enabling reconstruction of the dataset. AOF provides better durability than RDB, as it can be configured to fsync on every write, every second, or never. AOF files can become large and require periodic rewriting.

### Hybrid Persistence

Redis supports combining RDB and AOF, using AOF for durability and RDB for fast startup. This approach balances performance with data safety.

## Clustering

### Redis Cluster

Redis Cluster distributes data across multiple nodes using hash slots. The 16384 hash slots are distributed among master nodes, each responsible for a subset of slots. Replicas provide redundancy for each master.

Cluster mode enables horizontal scaling of both reads and writes. Client requests are routed to the appropriate node based on the key's hash slot.

### Sentinel

Redis Sentinel provides high availability for standalone Redis instances. Sentinel monitors master and replica health, performing automatic failover when the master becomes unavailable. Sentinels also provide configuration discovery and notification.

## Performance Characteristics

### In-Memory Operations

Redis operations are primarily in-memory, achieving sub-millisecond latency. The single-threaded model eliminates contention for most operations, with I/O threads handling network operations.

### Memory Efficiency

Redis uses optimized data structures and encoding to minimize memory usage. Small collections use compact encodings, and references are shared where possible. Memory usage should be monitored to prevent out-of-memory conditions.

### Throughput

Redis can handle over 100,000 operations per second on a single instance. Throughput scales horizontally with cluster mode. Pipelining reduces network round trips for batch operations.
