# 05 - Data Platforms

## Overview

Comprehensive guide to data platforms covering relational databases, NoSQL databases, caching systems, and core data engineering concepts including transactions, replication, sharding, partitioning, indexing, and query optimization.

## Module Structure

### SQL - Relational Databases

| Database | Topics |
|----------|--------|
| [MySQL](sql/relational-databases/mysql/) | Fundamentals, Queries, Optimization, Replication, High Availability |
| [PostgreSQL](sql/relational-databases/postgresql/) | Fundamentals, Queries, Optimization, Replication, Extensions |
| [Oracle](sql/relational-databases/oracle/) | Fundamentals, PL/SQL, Performance |
| [SQL Server](sql/relational-databases/sql-server/) | Fundamentals, Queries, Performance |

### NoSQL Databases

| Database | Topics |
|----------|--------|
| [MongoDB](nosql/mongodb/) | Fundamentals, Queries, Aggregation, Replication, Sharding |
| [Cassandra](nosql/cassandra/) | Fundamentals, Data Model, Queries, Consistency, Repair |
| [DynamoDB](nosql/dynamodb/) | Fundamentals, Queries, GSI, LSI, Streams |
| Document Databases | Fundamentals, Patterns |
| Key-Value Databases | Fundamentals, Patterns |
| Column-Family Databases | Fundamentals, Patterns |
| [Neo4j](nosql/graph-databases/neo4j/) | Fundamentals, Queries, Cypher, Optimization |
| [InfluxDB](nosql/time-series-databases/influxdb/) | Fundamentals, Queries, Retention |
| Elasticsearch | Fundamentals, Queries, Analysis, Cluster, Plugins |
| OpenSearch | Fundamentals, Queries, Plugins |

### Caching

| System | Topics |
|--------|--------|
| [Redis](caching/redis/) | Fundamentals, Data Structures, Pub/Sub, Clustering, Persistence |
| Memcached | Fundamentals, Optimization |

### Core Concepts

| Topic | Subtopics |
|-------|-----------|
| [Design](design/) | Normalization, Denormalization, Schema Design |
| [Transactions](transactions/) | ACID, Isolation Levels, Lock Types, Deadlocks |
| [Replication](replication/) | Master-Slave, Multi-Master, Conflict Resolution |
| [Sharding](sharding/) | Strategies, Routing, Resharding |
| [Partitioning](partitioning/) | Horizontal, Vertical, Time-Based |
| [Indexing](indexing/) | B-Tree, Hash, Composite, Partial |
| [Query Optimization](query-optimization/) | EXPLAIN, ANALYZE, Hints, Statistics |

## Learning Path

### Beginner

1. SQL Fundamentals (MySQL or PostgreSQL)
2. Database Design (Normalization, Schema Design)
3. Basic Queries and Indexing
4. ACID Transactions

### Intermediate

1. Advanced Queries (CTEs, Window Functions)
2. Indexing Strategies (B-Tree, Composite, Partial)
3. Query Optimization (EXPLAIN, Statistics)
4. Replication Basics
5. Caching with Redis

### Advanced

1. Database Internals (MVCC, Lock Types)
2. Sharding and Partitioning
3. High Availability Architectures
4. Distributed Consensus and Conflict Resolution
5. Performance Tuning at Scale

## Quick Reference

### Database Selection Guide

```
Need ACID transactions? → PostgreSQL, MySQL, Oracle, SQL Server
Need high write throughput? → Cassandra, DynamoDB
Need full-text search? → Elasticsearch, OpenSearch
Need graph relationships? → Neo4j
Need time-series data? → InfluxDB, TimescaleDB
Need caching? → Redis, Memcached
Need flexible documents? → MongoDB, CouchDB
```

### CAP Theorem

- **CP**: MongoDB, HBase, Redis Cluster, ZooKeeper
- **AP**: Cassandra, DynamoDB, CouchDB, Riak
- **CA**: PostgreSQL, MySQL (single node)

## Prerequisites

- Basic understanding of data structures
- Familiarity with command line
- Understanding of client-server architecture
- Basic networking concepts

## Resources

- [Database Internals](https://www.oreilly.com/library/view/database-internals/9781492040330/)
- [Designing Data-Intensive Applications](https://dataintensive.net/)
- [Use The Index, Luke](https://use-the-index-luke.com/)
