# ADR: Database Choice for New Service

## Status

Accepted — Q2 2024

## Context

We are building a new user management service that handles authentication, authorization, profile management, and audit logging. The service will support 50M+ users with 10K+ concurrent sessions.

Requirements:
- Strong consistency for authentication and authorization data
- Flexible schema for user profiles (varies by tenant)
- Full-text search across user profiles and audit logs
- JSON document storage for preferences and metadata
- ACID transactions for multi-step operations (user creation + role assignment)
- Mature ecosystem with strong Java driver support
- Cost-effective at scale

## Decision

Use PostgreSQL as the primary database for the user management service.

## Alternatives Considered

### MySQL
- Pros: Widely used, familiar to team, good replication support
- Cons: JSON support less mature, limited full-text search, weaker ACID guarantees for complex transactions, no native partitioning in older versions
- **Rejected**: PostgreSQL provides superior JSON and full-text search capabilities out of the box.

### MongoDB
- Pros: Flexible schema, native JSON, good horizontal scaling
- Cons: No ACID transactions across documents (limited in 4.0+), eventual consistency challenges, weaker relational query support, operational complexity at scale
- **Rejected**: ACID transactions are critical for user management operations. MongoDB's transaction support is limited and adds complexity.

### Amazon DynamoDB
- Pros: Fully managed, auto-scaling, low operational overhead
- Cons: Limited query flexibility, expensive at scale for complex access patterns, vendor lock-in, weak support for ad-hoc queries and reporting
- **Rejected**: Query flexibility is insufficient for our search and reporting requirements. Cost model doesn't fit our access patterns.

### Apache Cassandra
- Pros: Excellent write throughput, linear horizontal scaling, no single point of failure
- Cons: Limited query flexibility (no joins, limited secondary indexes), eventual consistency model, operational complexity, poor fit for our read-heavy workload
- **Rejected**: Read-heavy access patterns and need for complex queries make Cassandra a poor fit.

## Evaluation Matrix

| Criterion | Weight | PostgreSQL | MySQL | MongoDB | DynamoDB | Cassandra |
|-----------|--------|------------|-------|---------|----------|-----------|
| ACID compliance | 20% | Excellent | Good | Fair | Fair | Poor |
| JSON support | 15% | Excellent | Good | Excellent | N/A | N/A |
| Full-text search | 15% | Excellent | Fair | Good | Poor | Poor |
| Query flexibility | 15% | Excellent | Good | Fair | Poor | Poor |
| Cost at scale | 15% | Good | Good | Fair | Fair | Good |
| Team expertise | 10% | Good | Excellent | Fair | Fair | Poor |
| Operational maturity | 10% | Excellent | Excellent | Good | Excellent | Fair |

## Consequences

### Positive
- Native JSONB type provides schema flexibility without sacrificing queryability
- Built-in full-text search eliminates need for separate search engine initially
- Strong ACID guarantees for critical user operations
- Mature partitioning and indexing for scale
- Rich extension ecosystem (PostGIS, pg_trgm, uuid-ossp)
- Single database simplifies operations

### Negative
- **Training**: Team needs PostgreSQL-specific training (JSONB queries, partitioning, tuning)
- **Tooling**: Some MySQL-specific tooling needs replacement
- **Migration path**: Existing MySQL services may need data migration patterns
- **Connection pooling**: PostgreSQL connection model differs from MySQL (process-based vs thread-based)
- **Replication**: Streaming replication requires careful configuration

## Implementation Notes

- Use JSONB for flexible profile data, with generated columns for indexed fields
- Implement table partitioning by tenant ID for horizontal scaling
- Use pg_trgm extension for fuzzy search on user profiles
- Enable SSL connections and row-level security for multi-tenant isolation
- Connection pool: HikariCP with 50 connections per service instance
- Backup strategy: pg_dump daily + WAL archiving for point-in-time recovery

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
