# Module 20: Redis

## Overview

This module covers Redis, the in-memory data store used as database, cache, and message broker. Students will learn Redis data structures, caching strategies, pub/sub messaging, and integration with Spring Boot for building high-performance applications.

## Learning Objectives

By the end of this module, you will be able to:

- Understand Redis architecture and data structures
- Implement caching strategies with Redis
- Use Redis for session management
- Implement pub/sub messaging patterns
- Integrate Redis with Spring Data Redis
- Configure Redis clusters for high availability
- Optimize Redis performance and memory usage

## Prerequisites

- [Module 19: Apache Kafka](../19-apache-kafka/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Redis Fundamentals](01-redis-fundamentals/) | 2 hours | Installation, CLI, configuration |
| 02 | [Data Structures](02-redis-data-structures/) | 2 hours | Strings, lists, sets, hashes, sorted sets |
| 03 | [Caching](03-redis-caching/) | 3 hours | Cache patterns, TTL, eviction policies |
| 04 | [Pub/Sub](04-redis-pub-sub/) | 2 hours | Publish/subscribe messaging |
| 05 | [Spring Data Redis](05-spring-data-redis/) | 2 hours | RedisTemplate, repositories, serialization |
| 06 | [Redis Cluster](06-redis-cluster/) | 2 hours | Sharding, replication,哨兵模式 |

## Key Concepts

- In-memory vs. persistent storage
- Cache-aside, write-through, write-behind patterns
- Data structure use cases
- Memory optimization techniques
- High availability and clustering

## Enterprise Applications

Redis is critical for improving application performance through caching, session storage, real-time analytics, and message brokering in enterprise systems requiring low-latency data access.

## Estimated Total Time

**13 hours**

## Module Project

Build a **Real-Time Leaderboard System** that:
- Uses Redis sorted sets for ranking
- Implements caching for user profiles
- Demonstrates pub/sub for live updates
- Configures Redis cluster for scalability
- Monitors performance and memory usage

## Resources

- [Redis Documentation](https://redis.io/documentation)
- [Spring Data Redis Reference](https://spring.io/projects/spring-data-redis)

**Previous Module**: [Module 19: Apache Kafka](../19-apache-kafka/)
**Next Module**: [Module 21: Docker](../21-docker/)