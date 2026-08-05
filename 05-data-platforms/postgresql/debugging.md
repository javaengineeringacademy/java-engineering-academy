# PostgreSQL Debugging

## EXPLAIN ANALYZE

### Basic Usage

```sql
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'alice@example.com';
```

### Understanding Output

```
Seq Scan on users  (cost=0.00..10.00 rows=1 width=100)
  Filter: (email = 'alice@example.com'::text)
  Rows Removed by Filter: 99
  Planning Time: 0.100 ms
  Execution Time: 0.200 ms
```

### Key Metrics

- cost: Estimated cost
- rows: Estimated rows
- actual time: Actual execution time
- loops: Number of iterations
- Planning Time: Time to generate plan
- Execution Time: Total execution time

## Logging

### Enable Logging

```sql
-- In postgresql.conf
logging_collector = on
log_directory = 'log'
log_filename = 'postgresql-%Y-%m-%d.log'
log_min_duration_statement = 1000
log_statement = 'ddl'
log_duration = on
```

### Log Queries

```sql
-- Log all queries
ALTER SYSTEM SET log_statement = 'all';

-- Log slow queries
ALTER SYSTEM SET log_min_duration_statement = 1000;

-- Reload configuration
SELECT pg_reload_conf();
```

## pg_stat_statements

### Installation

```sql
-- Enable extension
CREATE EXTENSION pg_stat_statements;

-- In postgresql.conf
shared_preload_libraries = 'pg_stat_statements'
```

### Useful Queries

```sql
-- Top 10 slowest queries
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;

-- Most frequent queries
SELECT query, calls, rows
FROM pg_stat_statements
ORDER BY calls DESC
LIMIT 10;

-- Reset statistics
SELECT pg_stat_statements_reset();
```

## pg_stat_activity

### Active Queries

```sql
-- Current activity
SELECT pid, usename, datname, state, query, query_start
FROM pg_stat_activity
WHERE state = 'active';

-- Long running queries
SELECT pid, now() - query_start AS duration, query
FROM pg_stat_activity
WHERE state = 'active'
AND now() - query_start > interval '5 minutes';
```

### Blocking Queries

```sql
-- Find blocking queries
SELECT blocked_locks.pid AS blocked_pid,
       blocking_locks.pid AS blocking_pid,
       blocked_activity.usename AS blocked_user,
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

## Debug Functions

### RAISE NOTICE

```sql
-- Debug function
CREATE OR REPLACE FUNCTION debug_test()
RETURNS void AS $$
BEGIN
  RAISE NOTICE 'Variable value: %', 'test';
  RAISE NOTICE 'Current time: %', now();
END;
$$ LANGUAGE plpgsql;

-- Call function
SELECT debug_test();
```

### pg_backend_pid()

```sql
-- Get current process ID
SELECT pg_backend_pid();
```

## Performance Analysis

### Table Statistics

```sql
-- Table bloat
SELECT schemaname, relname, n_live_tup, n_dead_tup,
       round(n_dead_tup / nullif(n_live_tup, 0) * 100, 2) as dead_pct
FROM pg_stat_user_tables
WHERE n_dead_tup > 1000
ORDER BY n_dead_tup DESC;
```

### Index Usage

```sql
-- Unused indexes
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0;
```

## Lock Analysis

```sql
-- Current locks
SELECT pid, relation, mode, granted
FROM pg_locks
WHERE NOT granted;

-- Lock conflicts
SELECT a.pid, a.usename, a.query, l.mode, l.granted
FROM pg_stat_activity a
JOIN pg_locks l ON a.pid = l.pid
WHERE NOT l.granted;
```

## Best Practices

1. Always use EXPLAIN ANALYZE
2. Enable logging for debugging
3. Use pg_stat_statements
4. Monitor long-running queries
5. Check for blocking queries
