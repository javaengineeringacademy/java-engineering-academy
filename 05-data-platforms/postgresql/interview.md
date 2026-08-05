# PostgreSQL Interview Questions

## Basic Questions

### 1. What is PostgreSQL?

PostgreSQL is an open-source relational database management system (RDBMS) known for its extensibility, standards compliance, and advanced features. It supports SQL, JSON, full-text search, and many other features.

### 2. What is MVCC?

MVCC (Multi-Version Concurrency Control) allows multiple transactions to access the database simultaneously without blocking each other. Each transaction sees a snapshot of the data as it was at the start of the transaction.

### 3. What is WAL?

WAL (Write-Ahead Logging) ensures data durability by writing changes to a log before applying them to the database. This enables point-in-time recovery and replication.

### 4. What is the difference between TEXT and VARCHAR?

TEXT is variable-length with no limit, while VARCHAR(n) has a specified maximum length. TEXT is more flexible and often preferred.

### 5. What are the different index types?

- B-tree: Default, for equality and range queries
- Hash: For equality only
- GIN: For composite types, arrays, JSONB
- GiST: For geometric and full-text search
- BRIN: For large tables with natural ordering

## Intermediate Questions

### 6. What is a materialized view?

A materialized view stores the result of a query physically. Unlike a regular view, it doesn't re-execute the query each time. It must be refreshed manually.

### 7. What is the difference between INNER JOIN and LEFT JOIN?

INNER JOIN returns only matching rows. LEFT JOIN returns all rows from the left table and matching rows from the right table, with NULLs for non-matches.

### 8. How do you find slow queries?

```sql
-- Using pg_stat_statements
SELECT query, mean_exec_time, calls
FROM pg_stat_statements
ORDER BY mean_exec_time DESC;

-- Using pg_stat_activity
SELECT query, query_start, state
FROM pg_stat_activity
WHERE state = 'active';
```

### 9. What is a CTE?

A Common Table Expression (CTE) is a temporary named result set that exists within the scope of a single statement. It improves readability and allows recursion.

```sql
WITH active_users AS (
    SELECT * FROM users WHERE status = 'active'
)
SELECT * FROM active_users;
```

### 10. What is the difference between DELETE and TRUNCATE?

DELETE removes rows one by one, logs each deletion, and can be rolled back. TRUNCATE removes all rows, is faster, and doesn't fire triggers.

## Advanced Questions

### 11. How does PostgreSQL handle concurrency?

PostgreSQL uses MVCC for concurrency. Readers don't block writers, and writers don't block readers. Dead tuples are removed by autovacuum.

### 12. What is connection pooling and why use it?

Connection pooling reduces the overhead of creating new connections by reusing existing ones. Tools like PgBouncer manage connection pools.

### 13. How do you implement row-level security?

```sql
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;

CREATE POLICY user_orders ON orders
  FOR ALL
  TO app_user
  USING (user_id = current_setting('app.user_id')::int);
```

### 14. What is the difference between streaming replication and logical replication?

Streaming replication copies entire database clusters. Logical replication allows selective replication of tables and can be used across versions.

### 15. How do you optimize a slow query?

1. Run EXPLAIN ANALYZE
2. Add appropriate indexes
3. Rewrite the query
4. Update table statistics
5. Check for table bloat
6. Consider partitioning

## System Design Questions

### 16. Design a database schema for an e-commerce system

Key tables: users, products, orders, order_items, payments, inventory.

Consider: Foreign keys, indexes on frequently queried columns, audit columns.

### 17. How would you handle a table with billions of rows?

1. Partition the table by date or ID range
2. Use BRIN indexes for time-series data
3. Implement read replicas
4. Consider archiving old data
5. Use connection pooling

### 18. Design a backup and recovery strategy

1. Daily pg_dump backups
2. WAL archiving for point-in-time recovery
3. Test restores regularly
4. Monitor backup jobs
5. Document recovery procedures

## Best Practices Questions

### 19. What are PostgreSQL security best practices?

1. Use role-based access control
2. Enable SSL for connections
3. Use strong passwords
4. Enable row-level security
5. Audit sensitive operations

### 20. What are PostgreSQL performance best practices?

1. Use EXPLAIN ANALYZE
2. Add indexes strategically
3. Use connection pooling
4. Tune memory settings
5. Monitor slow queries

## Tricky Questions

### 21. What is the difference between COUNT(*) and COUNT(column)?

COUNT(*) counts all rows including NULLs. COUNT(column) counts only non-NULL values.

### 22. What happens when you UPDATE a row in PostgreSQL?

The old row is marked as dead, and a new row version is created. The dead row is reclaimed by autovacuum.

### 23. What is transaction ID wraparound?

Transaction IDs are 32-bit integers. When they overflow, PostgreSQL performs a shutdown checkpoint to prevent data loss. Autovacuum prevents this.

### 24. How do you handle NULLs in queries?

Use IS NULL, IS NOT NULL, COALESCE, NULLIF, or CASE statements.

### 25. What is the difference between WHERE and HAVING?

WHERE filters rows before grouping. HAVING filters groups after GROUP BY.
