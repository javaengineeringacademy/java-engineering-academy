# PostgreSQL Optimization

## Table of Contents

1. [EXPLAIN ANALYZE](#explain-analyze)
2. [Index Optimization](#index-optimization)
3. [Query Tuning](#query-tuning)
4. [Configuration Tuning](#configuration-tuning)
5. [Connection Pooling](#connection-pooling)
6. [Partitioning](#partitioning)
7. [Monitoring](#monitoring)
8. [Best Practices](#best-practices)

---

## EXPLAIN ANALYZE

### Basic Usage

```sql
-- Basic EXPLAIN ANALYZE
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';

-- With BUFFERS
EXPLAIN (ANALYZE, BUFFERS)
SELECT u.username, COUNT(o.id) AS order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;

-- With TIMING
EXPLAIN (ANALYZE, TIMING, VERBOSE)
SELECT * FROM orders WHERE user_id = 1 AND created_at > '2024-01-01';

-- FORMAT options
EXPLAIN (ANALYZE, FORMAT TEXT) SELECT * FROM users LIMIT 10;
EXPLAIN (ANALYZE, FORMAT JSON) SELECT * FROM users LIMIT 10;
EXPLAIN (ANALYZE, FORMAT XML) SELECT * FROM users LIMIT 10;
```

### Reading EXPLAIN Output

```
HashAggregate  (cost=1234.56..1234.57 rows=1 width=84) (actual time=12.345..12.346 rows=1 loops=1)
  Group Key: u.id
  Batches: 1  Memory Usage: 24kB
  ->  Hash Left Join  (cost=1.00..1234.00 rows=100 width=88) (actual time=0.123..12.234 rows=100 loops=1)
        Hash Cond: (o.user_id = u.id)
        ->  Seq Scan on orders o  (cost=0.00..1000.00 rows=1000 width=12) (actual time=0.008..8.765 rows=1000 loops=1)
              Filter: (created_at > '2024-01-01'::date)
              Rows Removed by Filter: 500
        ->  Hash  (cost=1.00..1.00 rows=100 width=84) (actual time=0.056..0.057 rows=100 loops=1)
              Buckets: 1024  Batches: 1  Memory Usage: 16kB
              ->  Seq Scan on users u  (cost=0.00..1.00 rows=100 width=84) (actual time=0.012..0.034 rows=100 loops=1)
Planning Time: 0.156 ms
Execution Time: 12.456 ms
```

### Key Metrics

```sql
-- Cost: Estimated cost (startup..total)
-- Lower is better
-- actual time: Actual execution time in milliseconds
-- rows: Number of rows
-- loops: Number of times node was executed

-- Important nodes to look for:
-- Seq Scan: Sequential scan (full table)
-- Index Scan: Using index
-- Bitmap Scan: Using bitmap index
-- Hash Join: Hash join algorithm
-- Merge Join: Merge join algorithm
-- Nested Loop: Nested loop join
-- Sort: Sorting operation
-- Aggregate: Aggregation operation
```

### EXPLAIN Tips

```sql
-- Compare estimated vs actual rows
-- Large difference = outdated statistics

-- Check for high-cost operations
-- Seq Scan on large tables (consider index)
-- Sort operations (consider index or work_mem)
-- Hash operations (consider work_mem)

-- Check for filter operations
-- "Rows Removed by Filter" = rows scanned but not returned
-- Consider adding index for filtered columns

-- Example: Adding index for filtered column
CREATE INDEX idx_orders_created ON orders (created_at);
EXPLAIN ANALYZE
SELECT * FROM orders WHERE created_at > '2024-01-01';
```

---

## Index Optimization

### Index Types

```sql
-- B-tree (default, most common)
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_orders_created ON orders (created_at);

-- Hash (equality comparisons only)
CREATE INDEX idx_users_email_hash ON users USING HASH (email);

-- GiST (geometry, full-text search)
CREATE INDEX idx_locations_geom ON locations USING GiST (geom);

-- GIN (arrays, JSONB, full-text search)
CREATE INDEX idx_users_profile ON users USING GIN (profile);
CREATE INDEX idx_articles_search ON articles USING GIN (search_vector);

-- BRIN (Block Range Index, for large tables with natural ordering)
CREATE INDEX idx_logs_created ON logs USING BRIN (created_at);
```

### Composite Indexes

```sql
-- Column order matters
CREATE INDEX idx_orders_user_created ON orders (user_id, created_at);

-- Covers: WHERE user_id = ? AND created_at > ?
-- Also covers: WHERE user_id = ?
-- Does NOT cover well: WHERE created_at > ? (without user_id)

-- Partial indexes (filter at index creation)
CREATE INDEX idx_orders_active ON orders (user_id)
WHERE status = 'active';

-- Expression indexes
CREATE INDEX idx_users_lower_email ON users (LOWER(email));
CREATE INDEX idx_users_email_pattern ON users (email LIKE 'test%');

-- Unique index
CREATE UNIQUE INDEX idx_users_email_unique ON users (email);
```

### Index Maintenance

```sql
-- Reindex (locks table)
REINDEX TABLE users;
REINDEX INDEX idx_users_email;
REINDEX DATABASE mydb;

-- Concurrent reindex (no lock)
REINDEX TABLE CONCURRENTLY users;
REINDEX INDEX CONCURRENTLY idx_users_email;

-- Check index bloat
SELECT
  indexname,
  pg_size_pretty(pg_relation_size(indexname::regclass)) AS index_size,
  idx_scan
FROM pg_indexes
JOIN pg_stat_user_indexes USING (indexname)
WHERE schemaname = 'public'
ORDER BY pg_relation_size(indexname::regclass) DESC;

-- Find unused indexes
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  pg_size_pretty(pg_relation_size(indexname::regclass)) AS size
FROM pg_stat_user_indexes
WHERE idx_scan = 0
  AND indexname NOT LIKE '%pkey%'
ORDER BY pg_relation_size(indexname::regclass) DESC;

-- Find redundant indexes
SELECT
  a.indexname AS redundant_index,
  b.indexname AS dominant_index,
  pg_size_pretty(pg_relation_size(a.indexname::regclass)) AS redundant_size
FROM pg_indexes a
JOIN pg_indexes b
  ON a.tablename = b.tablename
  AND a.indexname != b.indexname
  AND a.indexname LIKE b.indexname || '%'
WHERE a.schemaname = 'public'
  AND b.schemaname = 'public';
```

---

## Query Tuning

### Query Rewriting

```sql
-- BAD: Using functions on indexed columns
SELECT * FROM users WHERE EXTRACT(YEAR FROM created_at) = 2024;

-- GOOD: Range query
SELECT * FROM users
WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';

-- BAD: Using OR with different columns
SELECT * FROM users WHERE email = 'test@example.com' OR username = 'test';

-- GOOD: Use UNION
SELECT * FROM users WHERE email = 'test@example.com'
UNION
SELECT * FROM users WHERE username = 'test';

-- BAD: Using NOT IN with subquery
SELECT * FROM users WHERE id NOT IN (SELECT user_id FROM banned);

-- GOOD: NOT EXISTS
SELECT * FROM users u
WHERE NOT EXISTS (SELECT 1 FROM banned b WHERE b.user_id = u.id);

-- BAD: Correlated subquery
SELECT * FROM users u
WHERE (SELECT COUNT(*) FROM orders o WHERE o.user_id = u.id) > 5;

-- GOOD: JOIN with aggregation
SELECT u.*
FROM users u
INNER JOIN (
  SELECT user_id, COUNT(*) AS order_count
  FROM orders
  GROUP BY user_id
  HAVING COUNT(*) > 5
) o ON u.id = o.user_id;
```

### Join Optimization

```sql
-- Ensure join columns are indexed
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);

-- Join order matters (filter early)
-- BAD
SELECT *
FROM large_table a
INNER JOIN medium_table b ON a.id = b.a_id
INNER JOIN small_table c ON b.id = c.b_id
WHERE c.status = 'active';

-- GOOD (filter first, then join)
SELECT *
FROM (
  SELECT * FROM small_table WHERE status = 'active'
) c
INNER JOIN medium_table b ON b.id = c.b_id
INNER JOIN large_table a ON a.id = b.a_id;

-- Use CTE for complex joins
WITH active_small AS (
  SELECT * FROM small_table WHERE status = 'active'
)
SELECT *
FROM active_small c
INNER JOIN medium_table b ON b.id = c.b_id
INNER JOIN large_table a ON a.id = b.a_id;
```

### Subquery Optimization

```sql
-- Convert correlated subqueries to joins
-- BAD (correlated)
SELECT * FROM users u
WHERE (SELECT COUNT(*) FROM orders o WHERE o.user_id = u.id) > 5;

-- GOOD (JOIN)
SELECT u.*
FROM users u
INNER JOIN (
  SELECT user_id, COUNT(*) AS order_count
  FROM orders
  GROUP BY user_id
  HAVING COUNT(*) > 5
) o ON u.id = o.user_id;

-- Use LATERAL for row-dependent subqueries
SELECT
  u.username,
  recent.order_id,
  recent.total
FROM users u
CROSS JOIN LATERAL (
  SELECT id AS order_id, total
  FROM orders
  WHERE user_id = u.id
  ORDER BY created_at DESC
  LIMIT 1
) recent;
```

---

## Configuration Tuning

### Memory Settings

```sql
-- shared_buffers: Cache for data pages
-- Recommended: 25% of RAM (up to 8GB)
ALTER SYSTEM SET shared_buffers = '4GB';

-- effective_cache_size: Estimate of total memory available
-- Recommended: 75% of RAM
ALTER SYSTEM SET effective_cache_size = '12GB';

-- work_mem: Memory for sorts and hashes
-- Increase for complex queries
ALTER SYSTEM SET work_mem = '256MB';

-- maintenance_work_mem: Memory for VACUUM, CREATE INDEX
ALTER SYSTEM SET maintenance_work_mem = '1GB';

-- Check current settings
SHOW shared_buffers;
SHOW effective_cache_size;
SHOW work_mem;
SHOW maintenance_work_mem;
```

### WAL Settings

```sql
-- wal_level: WAL verbosity
-- minimal, replica, logical
ALTER SYSTEM SET wal_level = 'replica';

-- max_wal_senders: WAL sender processes
ALTER SYSTEM SET max_wal_senders = 10;

-- wal_keep_size: WAL retention
ALTER SYSTEM SET wal_keep_size = '1GB';

-- checkpoint_completion_target: Spread checkpoint writes
ALTER SYSTEM SET checkpoint_completion_target = 0.9;

-- max_wal_size: Maximum WAL size
ALTER SYSTEM SET max_wal_size = '4GB';

-- min_wal_size: Minimum WAL size
ALTER SYSTEM SET min_wal_size = '1GB';
```

### Parallel Query

```sql
-- max_parallel_workers_per_gather: Workers per query node
ALTER SYSTEM SET max_parallel_workers_per_gather = 4;

-- max_parallel_workers: Total parallel workers
ALTER SYSTEM SET max_parallel_workers = 8;

-- max_parallel_maintenance_workers: Workers for maintenance
ALTER SYSTEM SET max_parallel_maintenance_workers = 4;

-- parallel_tuple_cost: Cost of parallel tuple processing
ALTER SYSTEM SET parallel_tuple_cost = 0.01;

-- parallel_setup_cost: Cost of parallel setup
ALTER SYSTEM SET parallel_setup_cost = 1000;

-- min_parallel_table_scan_size: Minimum table size for parallel scan
ALTER SYSTEM SET min_parallel_table_scan_size = '8MB';
```

### Autovacuum

```sql
-- autovacuum: Enable autovacuum
ALTER SYSTEM SET autovacuum = on;

-- autovacuum_max_workers: Number of autovacuum workers
ALTER SYSTEM SET autovacuum_max_workers = 3;

-- autovacuum_naptime: Time between autovacuum runs
ALTER SYSTEM SET autovacuum_naptime = '1min';

-- autovacuum_vacuum_threshold: Minimum dead tuples to vacuum
ALTER SYSTEM SET autovacuum_vacuum_threshold = 50;

-- autovacuum_vacuum_scale_factor: Fraction of table to trigger vacuum
ALTER SYSTEM SET autovacuum_vacuum_scale_factor = 0.1;

-- autovacuum_analyze_threshold: Minimum changes for analyze
ALTER SYSTEM SET autovacuum_analyze_threshold = 50;

-- autovacuum_analyze_scale_factor: Fraction of table to trigger analyze
ALTER SYSTEM SET autovacuum_analyze_scale_factor = 0.05;
```

---

## Connection Pooling

### PgBouncer

```ini
# pgbouncer.ini
[databases]
mydb = host=127.0.0.1 port=5432 dbname=mydb

[pgbouncer]
listen_addr = 127.0.0.1
listen_port = 6432
auth_type = md5
auth_file = /etc/pgbouncer/userlist.txt
pool_mode = transaction
default_pool_size = 20
min_pool_size = 5
reserve_pool_size = 5
max_client_conn = 1000
max_db_connections = 100
```

```bash
# Start PgBouncer
pgbouncer -d /etc/pgbouncer/pgbouncer.ini

# Check status
psql -h 127.0.0.1 -p 6432 -U admin pgbouncer -c "SHOW POOLS;"
```

### Connection Pool Modes

```sql
-- Session mode: Connection held for entire session
-- Good for: SET commands, prepared statements

-- Transaction mode: Connection held for transaction only
-- Good for: Most applications

-- Statement mode: Connection held for single statement
-- Good for: Simple queries, no transactions

-- Check pool statistics
SHOW POOLS;
SHOW CLIENTS;
SHOW SERVERS;
SHOW STATS;
```

---

## Partitioning

### Declarative Partitioning

```sql
-- Range partitioning
CREATE TABLE orders (
  id BIGSERIAL,
  user_id INTEGER NOT NULL,
  total DECIMAL(10,2),
  status VARCHAR(20),
  created_at TIMESTAMPTZ NOT NULL,
  PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

-- Create partitions
CREATE TABLE orders_2024_q1 PARTITION OF orders
  FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');
CREATE TABLE orders_2024_q2 PARTITION OF orders
  FOR VALUES FROM ('2024-04-01') TO ('2024-07-01');
CREATE TABLE orders_2024_q3 PARTITION OF orders
  FOR VALUES FROM ('2024-07-01') TO ('2024-10-01');
CREATE TABLE orders_2024_q4 PARTITION OF orders
  FOR VALUES FROM ('2024-10-01') TO ('2025-01-01');

-- List partitioning
CREATE TABLE orders_by_region (
  id BIGSERIAL,
  region VARCHAR(20),
  total DECIMAL(10,2),
  PRIMARY KEY (id, region)
) PARTITION BY LIST (region);

CREATE TABLE orders_us PARTITION OF orders_by_region FOR VALUES IN ('US');
CREATE TABLE orders_eu PARTITION OF orders_by_region FOR VALUES IN ('EU');
CREATE TABLE orders_asia PARTITION OF orders_by_region FOR VALUES IN ('ASIA');

-- Hash partitioning
CREATE TABLE users (
  id BIGSERIAL,
  username VARCHAR(50),
  email VARCHAR(100),
  PRIMARY KEY (id)
) PARTITION BY HASH (id);

CREATE TABLE users_0 PARTITION OF users FOR VALUES WITH (MODULUS 4, REMAINDER 0);
CREATE TABLE users_1 PARTITION OF users FOR VALUES WITH (MODULUS 4, REMAINDER 1);
CREATE TABLE users_2 PARTITION OF users FOR VALUES WITH (MODULUS 4, REMAINDER 2);
CREATE TABLE users_3 PARTITION OF users FOR VALUES WITH (MODULUS 4, REMAINDER 3);
```

### Partition Pruning

```sql
-- Automatic partition pruning
EXPLAIN ANALYZE
SELECT * FROM orders
WHERE created_at >= '2024-01-01' AND created_at < '2024-04-01';
-- Only scans orders_2024_q1 partition

-- Manual partition pruning
SET enable_partition_pruning = on;

-- Check partition pruning
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) AS size
FROM pg_tables
WHERE tablename LIKE 'orders_%'
ORDER BY tablename;
```

---

## Monitoring

### Performance Queries

```sql
-- Active queries
SELECT
  pid,
  now() - pg_stat_activity.query_start AS duration,
  query,
  state,
  wait_event_type,
  wait_event
FROM pg_stat_activity
WHERE state = 'active'
ORDER BY duration DESC;

-- Long running queries
SELECT
  pid,
  now() - pg_stat_activity.query_start AS duration,
  query
FROM pg_stat_activity
WHERE (now() - pg_stat_activity.query_start) > interval '5 minutes'
  AND state = 'active';

-- Table statistics
SELECT
  schemaname,
  relname AS tablename,
  seq_scan,
  seq_tup_read,
  idx_scan,
  idx_tup_fetch,
  n_tup_ins,
  n_tup_upd,
  n_tup_del,
  n_live_tup,
  n_dead_tup,
  last_vacuum,
  last_autovacuum,
  last_analyze,
  last_autoanalyze
FROM pg_stat_user_tables
ORDER BY n_dead_tup DESC;

-- Index usage
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch,
  pg_size_pretty(pg_relation_size(indexname::regclass)) AS index_size
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

### Lock Monitoring

```sql
-- Current locks
SELECT
  l.locktype,
  l.relation::regclass AS tablename,
  l.mode,
  l.granted,
  a.pid,
  a.query,
  a.state
FROM pg_locks l
JOIN pg_stat_activity a ON l.pid = a.pid
WHERE NOT l.granted
  AND l.relation IS NOT NULL;

-- Lock conflicts
SELECT
  blocked_locks.pid AS blocked_pid,
  blocked_activity.usename AS blocked_user,
  blocking_locks.pid AS blocking_pid,
  blocking_activity.usename AS blocking_user,
  blocked_activity.query AS blocked_query,
  blocking_activity.query AS blocking_query
FROM pg_catalog.pg_locks blocked_locks
JOIN pg_catalog.pg_stat_activity blocked_activity
  ON blocked_activity.pid = blocked_locks.pid
JOIN pg_catalog.pg_locks blocking_locks
  ON blocking_locks.locktype = blocked_locks.locktype
  AND blocking_locks.relation = blocked_locks.relation
  AND blocking_locks.pid != blocked_locks.pid
JOIN pg_catalog.pg_stat_activity blocking_activity
  ON blocking_activity.pid = blocking_locks.pid
WHERE NOT blocked_locks.granted;
```

---

## Best Practices

### Query Optimization Checklist

- [ ] Use EXPLAIN ANALYZE for all slow queries
- [ ] Check for Seq Scan on large tables
- [ ] Verify statistics are up to date
- [ ] Add missing indexes
- [ ] Remove unused indexes
- [ ] Rewrite subqueries to joins
- [ ] Use LIMIT for large result sets
- [ ] Avoid SELECT *

### Configuration Checklist

- [ ] Set shared_buffers to 25% of RAM
- [ ] Set effective_cache_size to 75% of RAM
- [ ] Tune work_mem for complex queries
- [ ] Configure autovacuum appropriately
- [ ] Set wal_level to replica
- [ ] Enable parallel query if CPU-bound

### Monitoring Checklist

- [ ] Monitor slow queries
- [ ] Check table bloat
- [ ] Verify index usage
- [ ] Monitor lock contention
- [ ] Check connection pool stats
- [ ] Review autovacuum activity

---

## Summary

| Area | Key Actions |
|------|-------------|
| EXPLAIN ANALYZE | Analyze query execution plans |
| Indexes | Design and maintain proper indexes |
| Query Rewriting | Optimize SQL patterns |
| Configuration | Tune PostgreSQL parameters |
| Connection Pooling | Use PgBouncer |
| Partitioning | Partition large tables |
| Monitoring | Track performance metrics |

## Next Steps

- [PostgreSQL Replication](../replication/) - High availability
- [PostgreSQL Extensions](../extensions/) - Extension ecosystem
- Query Optimization - General optimization concepts
