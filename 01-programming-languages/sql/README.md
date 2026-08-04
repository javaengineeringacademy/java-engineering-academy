# SQL

Structured Query Language (SQL) is the standard language for managing and querying relational databases.

## Contents

| Section | Description |
|---------|-------------|
| [Fundamentals](fundamentals/README.md) | SELECT, JOINs, GROUP BY, subqueries, NULL handling |
| [Advanced](advanced/README.md) | Window functions, CTEs, PIVOT, partitioning |
| [Queries](queries/README.md) | 50+ SQL query examples with explanations |
| [Optimization](optimization/README.md) | Indexing, EXPLAIN plans, query tuning |
| [Projects](projects/README.md) | Database design and query challenges |

## Supported Databases

| Database | Key Features |
|----------|--------------|
| PostgreSQL | Advanced indexing, CTEs, window functions |
| MySQL | InnoDB engine, replication, partitioning |
| SQLite | Embedded, serverless, zero-configuration |
| SQL Server | T-SQL, PIVOT, materialized views |
| Oracle | PL/SQL, advanced analytics, RAC |

## Core Concepts

```
Database → Schema → Tables → Rows/Columns
                                 ↓
                          Relationships (FK, PK, UK)
```

## Quick Reference

```sql
-- Basic query structure
SELECT columns FROM table
WHERE condition
GROUP BY columns
HAVING aggregate_condition
ORDER BY columns
LIMIT n;

-- Common operations
SELECT * FROM users WHERE age > 25;
SELECT COUNT(*) FROM orders GROUP BY status;
SELECT u.name, o.total FROM users u JOIN orders o ON u.id = o.user_id;
```

## Learning Path

1. Start with [Fundamentals](fundamentals/README.md)
2. Move to [Advanced](advanced/README.md) topics
3. Practice with [Query Examples](queries/README.md)
4. Study [Optimization](optimization/README.md) techniques
5. Build [Projects](projects/README.md)
