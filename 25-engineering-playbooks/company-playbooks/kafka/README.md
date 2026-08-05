# Kafka Internals Playbook

## Overview

Apache Kafka is a distributed event streaming platform designed for high-throughput, fault-tolerant, and durable data streaming. Originally developed at LinkedIn, Kafka has become the backbone of event-driven architectures at thousands of companies.

## Core Architecture

### Topic Partitions

Kafka topics are divided into partitions, which are the basic unit of parallelism. Each partition is an ordered, immutable sequence of records. Partitions are distributed across brokers in the cluster, enabling horizontal scaling.

Producers specify which partition to write to based on a partitioning strategy. Common strategies include round-robin, key-based hashing, and custom partitioners. Partitioning determines the ordering guarantees for records.

### Replication

Each partition is replicated across multiple brokers for fault tolerance. One broker is elected as the partition leader, handling all read and write requests. Followers replicate the leader's data, taking over if the leader fails.

The replication factor determines how many copies of each partition exist. Higher replication factors provide better durability but increase storage and network overhead.

### Consumer Groups

Consumers subscribe to topics as part of a consumer group. Each partition is assigned to exactly one consumer in the group, enabling parallel consumption. This model scales consumers horizontally while maintaining partition ordering guarantees.

## Storage Model

### Commit Log

Kafka stores records in a commit log, an append-only data structure. Records are assigned monotonically increasing offsets, enabling efficient sequential reads and writes.

The commit log model provides durability, as records are written to disk and replicated before acknowledgment. It also enables replay, as consumers can re-read records from any offset.

### Log Segments

Partitions are divided into log segments, which are files on disk. Active segments are written to, while completed segments are immutable. Older segments can be deleted based on retention policies.

Log compaction removes older records for the same key, keeping only the most recent value. This enables Kafka to serve as a durable key-value store in addition to a streaming platform.

## Producer and Consumer Mechanics

### Producer Batching

Producers batch records before sending to brokers, reducing network overhead and improving throughput. Batching is configurable based on time and size thresholds.

Producers can choose acknowledgment levels: zero acknowledgments for maximum throughput, one acknowledgment from the leader, or all acknowledgments from in-sync replicas for maximum durability.

### Consumer Fetching

Consumers fetch records in batches, pulling data from brokers rather than having it pushed. This pull-based model allows consumers to process records at their own pace.

Consumer offsets are committed to Kafka, tracking the last processed record for each partition. This enables exactly-once processing semantics when combined with transactional producers.

## Operational Considerations

### Partition Rebalancing

Adding or removing brokers triggers partition rebalancing. Kafka moves partitions between brokers to maintain balance while minimizing data movement. Rebalancing is automatic but can temporarily affect performance.

### Monitoring

Key metrics include broker health, partition distribution, consumer lag, and throughput. Consumer lag indicates how far behind consumers are from the latest record, helping identify processing bottlenecks.

### Capacity Planning

Kafka clusters require planning for throughput, storage, and retention. Replication factor, retention period, and partition count all affect resource requirements.
