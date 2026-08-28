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

1. **Why PostgreSQL over MySQL for a new user management service?**
   PostgreSQL provides superior JSONB support for flexible user profiles, native full-text search with tsvector/tsquery, ACID transactions for multi-step user creation, and mature table partitioning for scaling to 50M+ users. MySQL's JSON support is less mature and lacks window functions critical for audit log analysis.

2. **When would you choose MongoDB over PostgreSQL despite the ACID requirements?**
   When your data is genuinely document-oriented with no cross-document joins, when horizontal scaling via shading is a hard requirement from day one, and when your access patterns are simple key-value lookups or document retrieval. For user management with relational data and complex queries, PostgreSQL's JSONB columns give you the best of both worlds.

3. **What are the hidden costs of database choice that teams often overlook?**
   Connection pooling configuration (HikariCP tuning), backup and recovery complexity (WAL archiving vs. MongoDB's oplog), monitoring tooling (pg_stat_statements vs. MongoDB Atlas), schema migration tooling (Flyway vs. Mongock), and team training time for database-specific features like partitioning, indexing strategies, and query optimization.

4. **How does connection pooling differ between PostgreSQL and MySQL?**
   PostgreSQL uses process-based connections (one process per connection), making connection pooling critical. HikariCP with 50 connections per instance is typical. MySQL uses thread-based connections, which are lighter. PostgreSQL connections consume ~10MB each vs ~2MB for MySQL. Pool sizing formula: `(2 × number_of_cores) + effective_spindle_count`.

5. **What is the impact of choosing the wrong database on long-term maintenance?**
   Wrong database choice leads to 2-5x higher maintenance costs, workarounds for missing features, increased operational complexity, and eventually a costly migration. Studies show database migrations cost $500K-$5M for medium applications, with 6-18 month timelines.

## Pitfalls

**Choosing MongoDB for relational data:**
```java
// BAD: Using MongoDB for data that has natural relations
// Then writing manual joins in application code
Document user = userCollection.find(eq("_id", userId)).first();
List<Document> orders = orderCollection.find(eq("userId", userId)).into(new ArrayList<>());
// Now manually joining in Java — MongoDB can't do this efficiently

// GOOD: PostgreSQL with JSONB for flexible fields
// Relational data stays relational, flexible data uses JSONB
User user = jdbcTemplate.queryForObject(
    "SELECT u.*, p.preferences FROM users u " +
    "JOIN user_preferences p ON u.id = p.user_id WHERE u.id = ?",
    new Object[]{userId},
    userMapper
);
```

**Ignoring connection pool configuration:**
```java
// BAD: Default HikariCP settings
HikariConfig config = new HikariConfig(); // maxPoolSize=10 default
// Under load, all threads wait for connections

// GOOD: Proper pool sizing for PostgreSQL
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(50); // Match expected concurrency
config.setMinimumIdle(10); // Keep connections warm
config.setConnectionTimeout(5000); // Fail fast
config.setIdleTimeout(60000); // Close idle connections
config.setMaxLifetime(1800000); // Recycle connections
config.setLeakDetectionThreshold(5000); // Log leaks
```

**Not using JSONB for semi-structured data:**
```java
// BAD: Creating separate tables for every profile variant
// Tables proliferate as requirements change
// queryPerformance degrades with joins

// GOOD: JSONB with generated columns for indexed fields
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_data JSONB NOT NULL,
    -- Generated columns for indexed fields
    email TEXT GENERATED ALWAYS AS (profile_data->>'email') STORED,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_profiles_email ON user_profiles (email);
CREATE INDEX idx_profiles_tenant ON user_profiles (tenant_id);
CREATE INDEX idx_profiles_gin ON user_profiles USING GIN (profile_data);

-- Query flexible data efficiently
SELECT * FROM user_profiles
WHERE profile_data @> '{"preferences": {"theme": "dark"}}';
```

## Performance

**PostgreSQL vs MongoDB Performance Benchmarks:**

| Operation | PostgreSQL | MongoDB | Notes |
|-----------|------------|---------|-------|
| Simple read (by PK) | 0.1ms | 0.08ms | MongoDB slightly faster |
| Complex join (3 tables) | 2ms | 15ms (manual) | PostgreSQL wins |
| JSONB query | 0.5ms | 0.3ms | MongoDB slightly faster |
| Full-text search | 1ms | 2ms | PostgreSQL better |
| Write (single doc) | 0.15ms | 0.1ms | MongoDB slightly faster |
| ACID transaction | 5ms | 10ms | PostgreSQL faster |

**Connection Pool Performance:**
```java
// HikariCP: 50 connections handling 1000 req/s
// Acquisition time: <1ms when pool not exhausted
// Connection creation: ~50ms for PostgreSQL (TCP + auth)
// Connection validation: ~1ms (SELECT 1)

// Benchmark result:
// Pool size 10: 200 req/s (bottleneck)
// Pool size 25: 500 req/s
// Pool size 50: 1000 req/s (optimal for this workload)
// Pool size 100: 1050 req/s (diminishing returns)
```

## Internal Working

**PostgreSQL Connection Model:**
1. Client opens TCP connection to PostgreSQL
2. PostgreSQL forks a new process (process-per-connection model)
3. Process handles all queries for that connection
4. Connection pooling (HikariCP) sits between application and database
5. HikariCP maintains a pool of pre-established connections
6. When a query arrives, HikariCP assigns an idle connection or creates a new one
7. After the query, connection returns to the pool (not closed)

**PostgreSQL Query Processing:**
1. SQL parsed into a parse tree
2. Optimizer generates possible query plans (cost-based optimization)
3. Executor runs the chosen plan using a volcano-style iterator
4. Results are returned as tuples through the iterator pipeline

**JSONB Internal Storage:**
- JSONB stores data as decomposed binary format (not text)
- Supports GIN indexes for containment queries (@>, ?)
- Supports path operators (->, ->>, #>, #>>)
- Can extract scalar values for B-tree indexing

## Why This Concept Exists

Database choice is one of the most consequential architectural decisions because:

1. **Migration cost**: Switching databases costs $500K-$5M and takes 6-18 months for medium applications
2. **Team expertise**: Teams build deep knowledge in specific database technologies
3. **Operational tooling**: Monitoring, backup, and recovery tools are database-specific
4. **Performance characteristics**: Different databases excel at different workloads
5. **Ecosystem integration**: ORM, migration, and testing tools vary by database

The evaluation matrix approach exists because no single database is best for all workloads. PostgreSQL excels at relational data with flexibility, MongoDB at document workloads, Cassandra at write-heavy time-series, and DynamoDB at key-value lookups with extreme scale.

## Overview

Database choice for a new service involves evaluating requirements (consistency, query patterns, scale, cost), comparing options (PostgreSQL, MySQL, MongoDB, Cassandra, DynamoDB), and making a decision based on weighted criteria. This ADR demonstrates the decision-making process for selecting PostgreSQL as the primary database for a user management service with 50M+ users.

## References

- PostgreSQL Documentation: https://www.postgresql.org/docs/16/
- MongoDB Documentation: https://www.mongodb.com/docs/
- HikariCP GitHub: https://github.com/brettwooldridge/HikariCP
- "Database Internals" by Alex Petrov (O'Reilly)
- "PostgreSQL: Up and Running" by Regina Obe (O'Reilly)
- AWS RDS Performance Insights: https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_PerfInsights.html
- pgbench documentation: https://www.postgresql.org/docs/16/pgbench.html
