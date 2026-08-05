## Cosmos DB

Globally distributed, multi-model NoSQL database with guaranteed low latency and high availability.

## Overview

Azure Cosmos DB is a globally distributed NoSQL database supporting multiple APIs (SQL, MongoDB, Cassandra, Gremlin, Table). It provides single-digit millisecond latency at any scale.

## Why It Matters

- Global distribution with multi-master
- Guaranteed single-digit millisecond latency
- Multiple API support (SQL, MongoDB, etc.)
- Automatic indexing and tuning
- Unlimited scale and storage

## Key Concepts

- **Container**: Partitioned collection of items
- **Partition Key**: Determines data distribution
- **Consistency Levels**: Strong, Bounded Staleness, Session, Consistent Prefix, Eventual
- **Throughput**: RU/s (Request Units per second)
- **Change Feed**: Stream of item changes
- **Multi-Master**: Active-active replication

## Core Topics

- Database and container creation
- Partition key selection
- SQL API queries
- Change feed processing
- Multi-region configuration
- Consistency level selection
- EF Core provider for Cosmos DB

## Best Practices

- Choose partition key carefully (high cardinality, even distribution)
- Monitor and adjust RU/s based on throughput needs
- Use change feed for event-driven patterns
- Use Point Read for single-item lookups
- Implement retry logic with exponential backoff

## Hands-on Labs

- Create a Cosmos DB container
- Query with SQL API
- Process change feed events
- Configure multi-region replication

## Interview Questions

1. How do you choose a partition key?
2. What are the consistency levels in Cosmos DB?
3. How does change feed work?

## References

- https://learn.microsoft.com/azure/cosmos-db/
- https://learn.microsoft.com/azure/cosmos-db/sql-api-get-started
- https://learn.microsoft.com/azure/cosmos-db/change-feed
