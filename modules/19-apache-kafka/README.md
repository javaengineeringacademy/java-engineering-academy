# Module 19: Apache Kafka

## Overview

This module covers Apache Kafka, the distributed event streaming platform. Students will learn Kafka architecture, producer/consumer patterns, stream processing, and integration with Spring Boot for building real-time data pipelines and event-driven systems.

## Learning Objectives

By the end of this module, you will be able to:

- Understand Kafka architecture and core concepts
- Implement producers for publishing events
- Build consumers for processing messages
- Use Kafka Streams for real-time processing
- Integrate Kafka with Spring Boot applications
- Configure topics, partitions, and replication
- Monitor and troubleshoot Kafka deployments

## Prerequisites

- [Module 18: Microservices](../18-microservices/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Kafka Fundamentals](01-kafka-fundamentals/) | 2 hours | Architecture, topics, partitions, brokers |
| 02 | [Kafka Producer](02-kafka-producer/) | 2 hours | Configuration, serialization, reliability |
| 03 | [Kafka Consumer](03-kafka-consumer/) | 2 hours | Consumer groups, offsets, rebalancing |
| 04 | [Kafka Streams](04-kafka-streams/) | 3 hours | Stream processing, transformations, windows |
| 05 | [Kafka Connect](05-kafka-connect/) | 2 hours | Source/sink connectors, data integration |
| 06 | [Spring Kafka](06-spring-kafka/) | 3 hours | Spring integration, templates, listeners |

## Key Concepts

- Event-driven architecture
- Message ordering and guarantees
- Exactly-once semantics
- Stream processing vs. batch processing
- Schema evolution and compatibility

## Enterprise Applications

Apache Kafka is essential for building real-time data pipelines, event sourcing systems, and microservices communication in enterprise environments requiring high throughput, fault tolerance, and horizontal scalability.

## Estimated Total Time

**14 hours**

## Module Project

Build a **Real-Time Order Processing System** that:
- Publishes order events to Kafka topics
- Processes orders using Kafka Streams
- Integrates multiple services via event-driven architecture
- Implements exactly-once processing guarantees
- Monitors throughput and latency metrics

## Resources

- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Reference](https://spring.io/projects/spring-kafka)

**Previous Module**: [Module 18: Microservices](../18-microservices/)
**Next Module**: [Module 20: Redis](../20-redis/)