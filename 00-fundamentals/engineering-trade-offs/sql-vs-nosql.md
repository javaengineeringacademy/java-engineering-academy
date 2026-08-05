# SQL vs NoSQL

## Problem Statement

Should you use a relational database or a NoSQL database? SQL databases enforce structure and consistency. NoSQL databases offer flexibility and scale. The right choice depends on your data model, access patterns, and requirements.

## The Core Tension

SQL databases enforce schema, relationships, and ACID guarantees. They are predictable but rigid. NoSQL databases relax these constraints for flexibility, performance, or scale. They are powerful but require more discipline.

## ACID vs BASE

**ACID** (SQL databases):
- Atomicity: Transactions are all-or-nothing
- Consistency: Data always moves from one valid state to another
- Isolation: Concurrent transactions do not interfere
- Durability: Committed data survives crashes

**BASE** (NoSQL databases):
- Basically Available: System guarantees availability
- Soft state: State may change over time
- Eventual consistency: Data will eventually be consistent

## When SQL Wins

**Relational data**: When your data has natural relationships (users have orders, orders have items). Foreign keys and joins are powerful.

**Complex queries**: When you need ad-hoc queries, aggregations, and reporting. SQL is a query language designed for this.

**Data integrity**: When incorrect data is unacceptable. Constraints, triggers, and transactions enforce rules at the database level.

**ACID transactions**: When you need guarantees across multiple operations. Banking, inventory, and booking systems require this.

**Mature ecosystem**: Tooling, monitoring, backup, and recovery are well-established.

### When to Choose PostgreSQL

PostgreSQL is the default choice for most applications. It handles JSON, full-text search, geospatial data, and custom types. It scales read-heavy workloads with replicas and write-heavy with Citus.

### When to Choose MySQL

MySQL is simpler and faster for read-heavy workloads. Good for web applications where the data model is straightforward.

## When NoSQL Wins

**Flexible schema**: When your data shape changes frequently or varies between records. Document stores let each record have different fields.

**Massive scale**: When you need to write terabytes per day. Cassandra and DynamoDB scale horizontally with no single point of failure.

**High write throughput**: When you need millions of writes per second. Log-structured storage engines are optimized for append-heavy workloads.

**Simple access patterns**: When your queries are always by primary key or known indexes. Key-value stores are extremely fast for this.

**Geographic distribution**: When you need data close to users in multiple regions. Some NoSQL databases handle multi-region replication natively.

## Document Store (MongoDB, CouchDB)

Best for: Content management, user profiles, product catalogs, event logging.

Schema: JSON-like documents. Each document can have different fields. Nesting is natural.

Trade-off: No joins. You either embed related data (duplicates data, complicates updates) or reference it (requires multiple queries).

## Key-Value Store (Redis, DynamoDB)

Best for: Caching, session storage, feature flags, leaderboards.

Schema: Key maps to opaque value. No querying inside the value.

Trade-off: Extremely fast, but you must design your access patterns upfront. Queries are limited to key lookups.

## Wide-Column Store (Cassandra, HBase)

Best for: Time-series data, IoT data, event sourcing, write-heavy workloads.

Schema: Rows and columns, but columns can vary per row. Optimized for queries over ranges of rows.

Trade-off: Excellent write performance, but data modeling is query-driven. You must know your queries before designing the schema.

## Graph Database (Neo4j, Neptune)

Best for: Social networks, recommendation engines, fraud detection, knowledge graphs.

Schema: Nodes and edges with properties. Relationships are first-class citizens.

Trade-off: Traversal queries are extremely fast, but aggregation queries are not their strength. Not a general-purpose database.

## Decision Matrix

| Factor | SQL | NoSQL |
|--------|-----|-------|
| Data relationships | Rich | Minimal |
| Schema changes | Migrations | Flexible |
| ACID transactions | Yes | Rarely |
| Horizontal scaling | Hard | Easy |
| Query complexity | High | Low |
| Consistency | Strong | Eventual |
| Team expertise | Common | Specialized |

## Real-World Examples

**E-commerce**: SQL for orders and inventory (ACID critical). NoSQL for product catalog (flexible schema) and session storage (high throughput).

**Social media**: SQL for user accounts and relationships. NoSQL for feed storage (write-heavy) and media metadata (flexible schema).

**IoT platform**: SQL for device metadata and user accounts. NoSQL for sensor data (time-series, massive write volume).

## Interview Relevance

**Common questions**:
- "Design the database for a social media platform"
- "When would you choose MongoDB over PostgreSQL?"
- "How would you migrate from SQL to NoSQL?"

**What interviewers want**:
- You understand that different data stores serve different purposes
- You can identify the access patterns that determine the right choice
- You know the CAP implications of each choice
- You understand data modeling for both approachs

**Red flags**:
- Choosing NoSQL because "it scales better" without understanding why
- Not mentioning ACID when discussing financial data
- Ignoring the operational complexity of running multiple database types
- Not considering the team's existing expertise

## Key Takeaway

Most applications use both SQL and NoSQL databases. The question is not which one is better, but which data goes where. Use SQL for data that requires integrity and complex queries. Use NoSQL for data that requires flexibility, scale, or specific access patterns.
