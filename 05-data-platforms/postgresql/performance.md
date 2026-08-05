# PostgreSQL Performance

## Query Optimization

### EXPLAIN Basics

```sql
EXPLAIN SELECT * FROM users WHERE email = 'alice@example.com';

EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'alice@example.com';
```

### Understanding EXPLAIN Output

```
Seq Scan on users  (cost=0.00..10.00 rows=1 width=100)
  Filter: (email = 'alice@example.com'::text)
  Rows Removed by Filter: 99
```

### Key Metrics

- cost: Estimated cost
- rows: Estimated rows
- actual time: Actual execution time
- loops: Number of iterations

## Indexing Strategies

### B-tree Indexes

```sql
-- Standard index
CREATE INDEX idx_users_email ON users(email);

-- Unique index
CREATE UNIQUE INDEX idx_users_email_unique ON users(email);

-- Partial index
CREATE INDEX idx_active_users ON users(email)
WHERE status = 'active';
```

### GIN Indexes

```sql
-- JSONB indexing
CREATE INDEX idx_products_tags ON products USING GIN(tags);

-- Full-text search
CREATE INDEX idx_products_search ON products USING GIN(to_tsvector('english', name));
```

### BRIN Indexes

```sql
-- For large tables with natural ordering
CREATE INDEX idx_logs_timestamp ON logs USING BRIN(created_at);
```

## Query Rewriting

### Subquery to JOIN

```sql
-- Slow
SELECT * FROM users WHERE id IN (SELECT user_id FROM orders);

-- Fast
SELECT DISTINCT u.* FROM users u
INNER JOIN orders o ON u.id = o.user_id;
```

### EXISTS vs IN

```sql
-- EXISTS for large datasets
SELECT * FROM users u
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

## Partitioning

### Range Partitioning

```sql
CREATE TABLE orders (
    id SERIAL,
    created_at TIMESTAMP,
    amount DECIMAL
) PARTITION BY RANGE (created_at);

CREATE TABLE orders_2024 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
```

### Hash Partitioning

```sql
CREATE TABLE users (
    id SERIAL,
    email VARCHAR(255)
) PARTITION BY HASH (id);

CREATE TABLE users_p0 PARTITION OF users
FOR VALUES WITH (MODULUS 4, REMAINDER 0);
```

## Connection Pooling

### PgBouncer Settings

```
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 25
```

## Caching

### Materialized Views

```sql
CREATE MATERIALIZED VIEW user_stats AS
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id;

-- Refresh periodically
REFRESH MATERIALIZED VIEW CONCURRENTLY user_stats;
```

## Monitoring Performance

### pg_stat_statements

```sql
-- Enable extension
CREATE EXTENSION pg_stat_statements;

-- Find slow queries
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

### Index Usage

```sql
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

## Vacuum and Analyze

### Manual Vacuum

```sql
VACUUM VERBOSE users;
ANALYZE users;
```

### Autovacuum Tuning

```
autovacuum_vacuum_threshold = 50
autovacuum_analyze_threshold = 50
autovacuum_vacuum_scale_factor = 0.1
```

## Best Practices

1. Always use EXPLAIN ANALYZE
2. Add indexes on WHERE and JOIN columns
3. Avoid SELECT *
4. Use appropriate data types
5. Monitor slow queries regularly
