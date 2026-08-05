# Kafka to Redpanda Migration

## Overview

Apache Kafka is the dominant event streaming platform, but Redpanda offers a compatible alternative with simpler operations, lower latency, and reduced resource consumption. This playbook covers migrating from Kafka to Redpanda.

## Migration Strategy

### Compatibility Assessment

Redpanda is wire-compatible with Kafka, supporting the same APIs, protocols, and tools. Most Kafka clients and connectors work with Redpanda without modification. Assess specific compatibility needs for your Kafka version and features.

### Cluster Setup

Deploy a Redpanda cluster alongside the existing Kafka cluster. Redpanda's simpler architecture (single binary, no JVM, no ZooKeeper) reduces operational complexity compared to Kafka.

### Data Migration

Migrate topics and their data from Kafka to Redpanda. Options include:

- MirrorMaker 2 for continuous topic mirroring
- kafka-connect-redpanda for connector-based migration
- Custom producers for one-time data migration

### Cutover

Switch producers and consumers from Kafka to Redpanda. Monitor performance and correctness during the transition. Keep Kafka running until Redpanda is validated in production.

## Implementation Patterns

### Topic Migration

Redpanda supports the same topic configuration as Kafka. Migrate topics with their configuration, including partition count, replication factor, and retention policies.

Use Kafka's topic management tools to export topic configurations and recreate them in Redpanda. Verify topic creation before migrating data.

### Connector Migration

Kafka Connect connectors work with Redpanda with minimal changes. Update bootstrap servers in connector configurations to point to Redpanda. Test connector functionality with Redpanda before cutover.

### Client Migration

Kafka clients connect to Redpanda using the same libraries and configuration. Update bootstrap server lists to include Redpanda brokers. No code changes are required for standard Kafka client usage.

### Schema Registry

Redpanda includes a compatible Schema Registry. Migrate schemas from Kafka's Schema Registry to Redpanda's registry. Ensure schema compatibility modes are preserved.

## Key Differences

### Architecture

Kafka uses a JVM-based architecture with ZooKeeper for coordination (or KRaft mode). Redpanda uses a C++ architecture with Raft consensus, eliminating external dependencies.

### Operations

Kafka requires JVM tuning, ZooKeeper management, and complex configuration. Redpanda simplifies operations with a single binary, self-tuning, and fewer configuration parameters.

### Performance

Redpanda achieves lower latency through its thread-per-core architecture and elimination of JVM garbage collection. Throughput is comparable to Kafka with lower resource consumption.

### Resource Usage

Redpanda typically requires fewer resources than Kafka for equivalent workloads. The absence of JVM overhead and ZooKeeper reduces memory and CPU requirements.

## Lessons Learned

### Validate Compatibility

Test all Kafka features used by your applications with Redpanda before migration. While Redpanda is compatible, edge cases may exist with specific Kafka versions or features.

### Migrate Incrementally

Migrate topics and consumers one at a time rather than switching all at once. This reduces risk and allows validation at each step.

### Monitor Performance

Compare Kafka and Redpanda performance during migration. Redpanda may show different performance characteristics depending on workload patterns.

### Plan Rollback

Keep Kafka running during migration to enable rollback if issues arise. Only decommission Kafka after Redpanda is fully validated in production.
