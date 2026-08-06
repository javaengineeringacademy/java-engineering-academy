# MySQL Optimization

## Table of Contents

1. [EXPLAIN Analysis](#explain-analysis)
2. [Slow Query Log](#slow-query-log)
3. [Index Optimization](#index-optimization)
4. [Query Optimization](#query-optimization)
5. [Configuration Tuning](#configuration-tuning)
6. [InnoDB Optimization](#innodb-optimization)
7. [Server Performance](#server-performance)
8. [Monitoring](#monitoring)

---

## EXPLAIN Analysis

### Basic EXPLAIN

```sql
-- Simple query analysis
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';

-- Output columns:
-- id: Query identifier
-- select_type: SIMPLE, PRIMARY, SUBQUERY, DERIVED, UNION
-- table: Table name
-- partitions: Partition accessed
-- type: Access type (performance indicator)
-- possible_keys: Indexes that could be used
-- key: Index actually used
-- key_len: Length of index used
-- ref: Columns compared to index
-- rows: Estimated rows examined
-- filtered: Percentage of rows filtered
-- Extra: Additional information
```

### Access Types (Best to Worst)

```sql
-- 1. system: System table (1 row)
EXPLAIN SELECT * FROM mysql.user WHERE User = 'root';

-- 2. const: Primary key or unique index lookup
EXPLAIN SELECT * FROM users WHERE id = 1;

-- 3. eq_ref: Join on primary key/unique index
EXPLAIN SELECT * FROM orders o
INNER JOIN users u ON o.user_id = u.id;

-- 4. ref: Non-unique index lookup
EXPLAIN SELECT * FROM users WHERE status = 'active';

-- 5. range: Range scan on index
EXPLAIN SELECT * FROM orders WHERE created_at > '2024-01-01';

-- 6. index: Full index scan
EXPLAIN SELECT id FROM users;

-- 7. ALL: Full table scan (WORST)
EXPLAIN SELECT * FROM users WHERE first_name LIKE '%john%';
```

### EXPLAIN FORMAT=JSON

```sql
EXPLAIN FORMAT=JSON
SELECT u.username, o.total
FROM users u
INNER JOIN orders o ON u.id = o.user_id
WHERE u.status = 'active';

-- Key fields in JSON output:
-- query_block.query_table: Table access info
-- cost_info: Query cost estimation
-- read_rt_next_row: Estimated rows
-- accessing_table.access_type: How table is accessed
-- attaching_index: Index used for access
```

### EXPLAIN ANALYZE (MySQL 8.0.18+)

```sql
EXPLAIN ANALYZE
SELECT
  u.username,
  COUNT(o.id) AS order_count,
  SUM(o.total) AS total_spent
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE u.created_at >= '2024-01-01'
GROUP BY u.id
HAVING COUNT(o.id) > 5;

-- Output includes:
-- Actual execution time
-- Actual rows vs estimated rows
-- Loop iterations
-- Cost estimates
-- Actual vs predicted values
```

### Reading EXPLAIN Output

```sql
-- GOOD: const type with key used
+----+-------------+-------+------------+-------+---------------+---------+---------+-------+------+----------+-------+
| id | select_type | table | partitions | type  | possible_keys | key     | key_len | ref   | rows | filtered | Extra |
+----+-------------+-------+------------+-------+---------------+---------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | users | NULL       | const | PRIMARY       | PRIMARY | 4       | const |    1 |   100.00 | NULL  |
+----+-------------+-------+------------+-------+---------------+---------+---------+-------+------+----------+-------+

-- BAD: ALL type with no key
+----+-------------+-------+------------+------+---------------+------+---------+-------+--------+----------+-------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref   | rows   | filtered | Extra |
+----+-------------+-------+------------+------+---------------+------+---------+-------+--------+----------+-------+
|  1 | SIMPLE      | users | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 100000 |    10.00 | Using where |
+----+-------------+-------+------------+------+---------------+------+---------+-------+--------+----------+-------+

-- Warning signs:
-- type = ALL (full table scan)
-- rows = high number
-- filtered = low percentage
-- Extra contains "Using temporary" or "Using filesort"
```

---

## Slow Query Log

### Enable Slow Query Log

```sql
-- Check current settings
SHOW VARIABLES LIKE 'slow_query%';
SHOW VARIABLES LIKE 'long_query_time';

-- Enable slow query log
SET GLOBAL slow_query_log = ON;
SET GLOBAL slow_query_log_file = '/var/log/mysql/slow.log';
SET GLOBAL long_query_time = 2;  -- Log queries > 2 seconds

-- Enable in my.cnf
[mysqld]
slow_query_log = ON
slow_query_log_file = /var/log/mysql/slow.log
long_query_time = 2
log_queries_not_using_indexes = ON
min_examined_row_limit = 1000
```

### Analyze Slow Queries

```bash
# Using mysqldumpslow
mysqldumpslow -s t -t 10 /var/log/mysql/slow.log
mysqldumpslow -s c -t 10 /var/log/mysql/slow.log

# Using pt-query-digest (Percona Toolkit)
pt-query-digest /var/log/mysql/slow.log > slow_report.txt

# Analyze specific time range
pt-query-digest --since '2024-01-01' --until '2024-01-02' \
  /var/log/mysql/slow.log
```

### Performance Schema Queries

```sql
-- Top 10 queries by total time
SELECT
  DIGEST_TEXT AS query,
  COUNT_STAR AS exec_count,
  ROUND(SUM_TIMER_WAIT / 1000000000000, 2) AS total_time_sec,
  ROUND(AVG_TIMER_WAIT / 1000000000000, 4) AS avg_time_sec,
  SUM_ROWS_EXAMINED AS rows_examined,
  SUM_ROWS_SENT AS rows_sent
FROM performance_schema.events_statements_summary_by_digest
ORDER BY SUM_TIMER_WAIT DESC
LIMIT 10;

-- Queries not using indexes
SELECT
  DIGEST_TEXT AS query,
  COUNT_STAR AS exec_count,
  SUM_NO_index_used AS no_index_used
FROM performance_schema.events_statements_summary_by_digest
WHERE SUM_NO_index_used > 0
ORDER BY SUM_NO_index_used DESC;

-- Full table scans
SELECT
  DIGEST_TEXT AS query,
  COUNT_STAR AS exec_count,
  SUM_NO_index_used AS full_scans
FROM performance_schema.events_statements_summary_by_digest
WHERE SUM_NO_index_used > 0
ORDER BY SUM_NO_index_used DESC
LIMIT 10;
```

---

## Index Optimization

### Index Design Principles

```sql
-- 1. Selective columns first (high cardinality)
CREATE INDEX idx_email_status ON users (email, status);
-- email has high cardinality (many unique values)
-- status has low cardinality (few unique values)

-- 2. Covering index for frequent queries
CREATE INDEX idx_covering ON orders (user_id, status, total);
-- Covers: SELECT status, total FROM orders WHERE user_id = ?

-- 3. Prefix index for long text
CREATE INDEX idx_email_prefix ON users (email(20));
-- Saves space, good for LIKE 'prefix%'

-- 4. Composite index for multi-column queries
CREATE INDEX idx_composite ON orders (user_id, created_at, total);
-- Covers: WHERE user_id = ? AND created_at > ?
```

### Index Analysis

```sql
-- Check index usage
SELECT
  object_schema,
  object_name,
  index_name,
  count_star,
  count_read,
  count_fetch,
  count_insert,
  count_update,
  count_delete
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE object_schema = 'mydb'
ORDER BY count_read DESC;

-- Find unused indexes
SELECT
  object_schema,
  object_name,
  index_name
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE index_name IS NOT NULL
  AND count_star = 0
  AND object_schema NOT IN ('mysql', 'performance_schema', 'sys');

-- Find redundant indexes
SELECT * FROM sys.schema_redundant_indexes
WHERE table_schema = 'mydb';

-- Index statistics
SELECT
  table_name,
  index_name,
  cardinality,
  sub_part,
  packed,
  nullable,
  index_type
FROM information_schema.statistics
WHERE table_schema = 'mydb'
ORDER BY table_name, index_name;
```

### Index Maintenance

```sql
-- Check index fragmentation
SELECT
  table_name,
  data_free,
  ROUND(data_free / 1024 / 1024, 2) AS fragmented_mb
FROM information_schema.tables
WHERE table_schema = 'mydb'
  AND data_free > 0
ORDER BY data_free DESC;

-- Analyze table to update statistics
ANALYZE TABLE users;

-- Optimize table (rebuild indexes)
OPTIMIZE TABLE users;

-- Online DDL for index operations
ALTER TABLE users ADD INDEX idx_email (email), ALGORITHM=INPLACE, LOCK=NONE;
ALTER TABLE users DROP INDEX idx_old, ALGORITHM=INPLACE, LOCK=NONE;
```

---

## Query Optimization

### Query Rewriting Techniques

```sql
-- 1. Replace OR with UNION
-- BAD
SELECT * FROM users WHERE status = 'active' OR status = 'pending';

-- GOOD (if indexes differ)
SELECT * FROM users WHERE status = 'active'
UNION
SELECT * FROM users WHERE status = 'pending';

-- 2. Avoid functions on indexed columns
-- BAD
SELECT * FROM orders WHERE YEAR(created_at) = 2024;

-- GOOD
SELECT * FROM orders
WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';

-- 3. Avoid leading wildcards
-- BAD
SELECT * FROM users WHERE username LIKE '%test%';

-- GOOD: Use full-text index
SELECT * FROM users
WHERE MATCH(username) AGAINST('test' IN BOOLEAN MODE);

-- 4. Replace NOT IN with NOT EXISTS
-- BAD (slow with NULLs)
SELECT * FROM users WHERE id NOT IN (SELECT user_id FROM banned);

-- GOOD
SELECT * FROM users u
WHERE NOT EXISTS (SELECT 1 FROM banned b WHERE b.user_id = u.id);

-- 5. Use EXISTS for existence checks
-- BAD
SELECT * FROM users WHERE id IN (SELECT user_id FROM orders);

-- GOOD
SELECT * FROM users u
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

### JOIN Optimization

```sql
-- 1. Ensure join columns are indexed
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);

-- 2. Join order matters (filter early)
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

-- 3. Use STRAIGHT_JOIN to force join order
SELECT STRAIGHT_JOIN
  a.*, b.*, c.*
FROM small_table c
INNER JOIN medium_table b ON b.id = c.b_id
INNER JOIN large_table a ON a.id = b.a_id;

-- 4. Avoid SELECT * in joins
SELECT a.id, a.name, b.total
FROM users a
INNER JOIN orders b ON a.id = b.user_id;
```

### Subquery Optimization

```sql
-- 1. Convert correlated subqueries to joins
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

-- 2. Use derived tables for complex subqueries
SELECT
  d.department,
  d.avg_salary,
  e.name AS highest_paid
FROM (
  SELECT department, MAX(salary) AS max_salary, AVG(salary) AS avg_salary
  FROM employees
  GROUP BY department
) d
INNER JOIN employees e
  ON e.department = d.department
  AND e.salary = d.max_salary;

-- 3. Use LATERAL for row-dependent subqueries
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

### Buffer Pool Settings

```sql
-- Check buffer pool size
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';

-- Recommended: 70-80% of available RAM
SET GLOBAL innodb_buffer_pool_size = 8589934592; -- 8GB

-- Buffer pool instances (for large pools)
SET GLOBAL innodb_buffer_pool_instances = 8;

-- Monitor buffer pool hit ratio
SHOW STATUS LIKE 'Innodb_buffer_pool_read%';

-- Calculate hit ratio
-- Hit Ratio = 1 - (Innodb_buffer_pool_reads / Innodb_buffer_pool_read_requests)
-- Should be > 99%
```

### Log Settings

```sql
-- Redo log size
SET GLOBAL innodb_log_file_size = 1073741824; -- 1GB

-- Log buffer size
SET GLOBAL innodb_log_buffer_size = 67108864; -- 64MB

-- Flush settings
SET GLOBAL innodb_flush_log_at_trx_commit = 1; -- Safest
-- 0: Write to buffer, flush every second
-- 1: Flush every transaction (ACID compliant)
-- 2: Write to OS buffer, flush every second
```

### Connection Settings

```sql
-- Max connections
SET GLOBAL max_connections = 500;

-- Thread cache
SET GLOBAL thread_cache_size = 16;

-- Table cache
SET GLOBAL table_open_cache = 4000;
SET GLOBAL table_definition_cache = 2000;

-- Sort and join buffers
SET GLOBAL sort_buffer_size = 4194304; -- 4MB
SET GLOBAL join_buffer_size = 4194304;  -- 4MB
SET GLOBAL read_buffer_size = 2097152;  -- 2MB
SET GLOBAL read_rnd_buffer_size = 8388608; -- 8MB
```

### Query Cache (Deprecated in MySQL 8.0)

```sql
-- MySQL 5.7 only
SET GLOBAL query_cache_type = ON;
SET GLOBAL query_cache_size = 67108864; -- 64MB

-- Monitor query cache
SHOW STATUS LIKE 'Qcache%';

-- Note: Query cache removed in MySQL 8.0
-- Use ProxySQL or application-level caching instead
```

---

## InnoDB Optimization

### Buffer Pool Tuning

```sql
-- Multiple buffer pool instances
SET GLOBAL innodb_buffer_pool_instances = 8;

-- Pre-load buffer pool
SET GLOBAL innodb_buffer_pool_dump_at_shutdown = ON;
SET GLOBAL innodb_buffer_pool_load_at_startup = ON;

-- Monitor buffer pool
SHOW ENGINE INNODB STATUS\G

-- Key metrics from InnoDB Status:
-- BUFFER POOL AND MEMORY
-- Total memory allocated
-- Buffer pool hit rate
-- Pages read/written
```

### I/O Optimization

```sql
-- I/O capacity
SET GLOBAL innodb_io_capacity = 2000;
SET GLOBAL innodb_io_capacity_max = 4000;

-- Flush method
SET GLOBAL innodb_flush_method = 'O_DIRECT';

-- Double write buffer
SET GLOBAL innodb_doublewrite = ON;

-- Adaptive flushing
SET GLOBAL innodb_adaptive_flushing = ON;
```

### Transaction Log

```sql
-- Log file size (increase for write-heavy workloads)
SET GLOBAL innodb_log_file_size = 1073741824; -- 1GB

-- Log buffer size
SET GLOBAL innodb_log_buffer_size = 67108864; -- 64MB

-- Flush frequency
SET GLOBAL innodb_flush_log_at_trx_commit = 1;
```

---

## Server Performance

### Memory Optimization

```sql
-- Check memory usage
SELECT * FROM sys.memory_global_total;

-- Memory by component
SELECT
  event_name,
  current_count_used,
  ROUND(current_number_of_bytes_used / 1024 / 1024, 2) AS mb_used
FROM performance_schema.memory_summary_global_by_event_name
ORDER BY current_number_of_bytes_used DESC
LIMIT 10;

-- Per-thread buffers (multiply by max_connections)
-- sort_buffer_size
-- join_buffer_size
-- read_buffer_size
-- read_rnd_buffer_size
-- thread_stack
-- binlog_cache_size
```

### Thread Optimization

```sql
-- Thread cache
SET GLOBAL thread_cache_size = 16;

-- Monitor threads
SHOW STATUS LIKE 'Threads%';

-- Thread cache hit rate
-- Thread_cache_hits = 1 - (Connections / Threads_created)
-- Should be > 90%

-- Connection pooling
SET GLOBAL max_connections = 500;
SET GLOBAL wait_timeout = 28800;
SET GLOBAL interactive_timeout = 28800;
```

### Disk I/O

```sql
-- InnoDB I/O settings
SET GLOBAL innodb_read_io_threads = 8;
SET GLOBAL innodb_write_io_threads = 8;
SET GLOBAL innodb_io_capacity = 2000;

-- Temporary tables
SET GLOBAL tmp_table_size = 67108864; -- 64MB
SET GLOBAL max_heap_table_size = 67108864; -- 64MB

-- Check disk I/O
SELECT * FROM sys.io_global_by_file_by_bytes LIMIT 10;
```

---

## Monitoring

### Performance Schema

```sql
-- Enable performance schema
UPDATE performance_schema.setup_consumers
SET ENABLED = 'YES'
WHERE NAME LIKE '%events_statements%';

-- Top queries by execution time
SELECT
  DIGEST_TEXT,
  COUNT_STAR,
  ROUND(SUM_TIMER_WAIT / 1000000000000, 2) AS total_sec,
  ROUND(AVG_TIMER_WAIT / 1000000000000, 4) AS avg_sec
FROM performance_schema.events_statements_summary_by_digest
ORDER BY SUM_TIMER_WAIT DESC
LIMIT 10;

-- Table statistics
SELECT
  object_schema,
  object_name,
  count_star,
  count_read,
  count_write
FROM performance_schema.table_io_waits_summary_by_table
WHERE object_schema = 'mydb'
ORDER BY count_star DESC;
```

### Sys Schema

```sql
-- Schema analysis
SELECT * FROM sys.schema_table_statistics
WHERE table_schema = 'mydb'
ORDER BY total_latency DESC;

-- Unused indexes
SELECT * FROM sys.schema_unused_indexes;

-- Redundant indexes
SELECT * FROM sys.schema_redundant_indexes;

-- Statement analysis
SELECT * FROM sys.statements_with_runtimes_in_95th_percentile
LIMIT 10;

-- Schema analysis
SELECT * FROM sys.schema_analysis
WHERE table_schema = 'mydb';
```

### Key Metrics to Monitor

```sql
-- Connection metrics
SHOW STATUS LIKE 'Threads_connected';
SHOW STATUS LIKE 'Max_used_connections';
SHOW STATUS LIKE 'Aborted%';

-- InnoDB metrics
SHOW STATUS LIKE 'Innodb_row_lock%';
SHOW STATUS LIKE 'Innodb_buffer_pool%';
SHOW STATUS LIKE 'Innodb_data%';

-- Query metrics
SHOW STATUS LIKE 'Slow_queries';
SHOW STATUS LIKE 'Questions';
SHOW STATUS LIKE 'Queries';
```

---

## Optimization Checklist

### Pre-Optimization

- [ ] Enable slow query log
- [ ] Set appropriate long_query_time
- [ ] Enable log_queries_not_using_indexes
- [ ] Configure Performance Schema

### Index Optimization

- [ ] Review EXPLAIN output for all slow queries
- [ ] Add missing indexes
- [ ] Remove unused indexes
- [ ] Check composite index order
- [ ] Analyze index cardinality

### Query Optimization

- [ ] Rewrite slow queries
- [ ] Eliminate SELECT *
- [ ] Use JOINs instead of subqueries where appropriate
- [ ] Avoid functions on indexed columns
- [ ] Use LIMIT for large result sets

### Configuration Tuning

- [ ] Set innodb_buffer_pool_size to 70-80% of RAM
- [ ] Configure innodb_log_file_size appropriately
- [ ] Set max_connections based on workload
- [ ] Tune sort_buffer_size and join_buffer_size

### Monitoring

- [ ] Set up regular performance reviews
- [ ] Monitor slow query log
- [ ] Track index usage
- [ ] Review InnoDB buffer pool hit ratio

---

## Summary

| Area | Key Actions |
|------|-------------|
| EXPLAIN | Analyze query execution plans |
| Slow Query Log | Identify problematic queries |
| Indexes | Design and maintain proper indexes |
| Query Rewriting | Optimize SQL patterns |
| Configuration | Tune MySQL parameters |
| InnoDB | Optimize buffer pool and I/O |
| Monitoring | Track performance metrics |

## Next Steps

- [MySQL Replication](../replication/) - High availability
- [MySQL High Availability](../HA/) - Cluster setup
- Query Optimization - General optimization concepts
