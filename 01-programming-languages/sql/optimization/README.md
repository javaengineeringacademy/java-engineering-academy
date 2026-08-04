# SQL Optimization

Indexing strategies, query plans, performance tuning, and database design optimization.

## Table of Contents

- [Indexing Strategies](#indexing-strategies)
- [EXPLAIN Plans](#explain-plans)
- [Query Rewriting](#query-rewriting)
- [Statistics](#statistics)
- [Partitioning](#partitioning)
- [Denormalization](#denormalization)
- [Caching](#caching)
- [Connection Pooling](#connection-pooling)
- [Common Anti-Patterns](#common-anti-patterns)

---

## Indexing Strategies

### B-Tree Index (Default)

Most common index type, works for equality and range queries.

```sql
-- Single column
CREATE INDEX idx_employees_salary ON employees (salary);

-- Composite index (column order matters)
CREATE INDEX idx_employees_dept_salary ON employees (department_id, salary);

-- Why order matters:
-- Query: WHERE department_id = 1 AND salary > 50000
-- Uses both columns ✓
-- Query: WHERE salary > 50000
-- Cannot use composite index efficiently ✗
```

### Hash Index

Optimized for equality comparisons only.

```sql
-- PostgreSQL hash index
CREATE INDEX idx_employees_email_hash ON employees USING HASH (email);

-- Best for exact match queries
SELECT * FROM employees WHERE email = 'john@example.com';
```

### Composite Index Best Practices

```sql
-- Follow the ESR rule: Equality, Sort, Range
-- Query: WHERE a = 1 AND b = 2 AND c > 100 ORDER BY d
CREATE INDEX idx_optimal ON table_name (a, b, d, c);

-- Column selectivity: high selectivity first
-- email (high) > department_id (medium) > is_active (low)
CREATE INDEX idx_selectivity ON employees (email, department_id, is_active);
```

### Partial Index

Index only rows meeting a condition.

```sql
-- Index only active employees
CREATE INDEX idx_active_emp ON employees (department_id)
WHERE is_active = TRUE;

-- Index only recent orders
CREATE INDEX idx_recent_orders ON orders (customer_id)
WHERE order_date >= '2024-01-01';

-- Saves space and improves write performance
```

### Covering Index

Includes all columns needed by a query.

```sql
-- Query: SELECT name, email FROM employees WHERE department_id = 1
CREATE INDEX idx_covering ON employees (department_id, name, email);

-- PostgreSQL: INCLUDE clause
CREATE INDEX idx_covering_include ON employees (department_id)
INCLUDE (name, email, salary);
```

### Expression Index

Index on computed values.

```sql
-- Index on lowercased email
CREATE INDEX idx_email_lower ON employees (LOWER(email));

-- Query: WHERE LOWER(email) = 'john@example.com'
-- Uses the index ✓

-- Index on date parts
CREATE INDEX idx_order_month ON orders (DATE_TRUNC('month', order_date));
```

### GiST and GIN Indexes

For complex data types.

```sql
-- GiST for geometric/地理 data
CREATE INDEX idx_locations ON stores USING GiST (location);

-- GIN for full-text search
CREATE INDEX idx_products_search ON products USING GIN (
    to_tsvector('english', name || ' ' || description)
);

-- GIN for JSONB
CREATE INDEX idx_metadata ON products USING GIN (metadata);
```

### BRIN Index

Block Range Index for naturally ordered data.

```sql
-- Great for time-series data
CREATE INDEX idx_events_time ON events USING BRIN (created_at);

-- Much smaller than B-tree for large, ordered tables
```

### Index Monitoring

```sql
-- Find unused indexes
SELECT
    schemaname,
    relname,
    indexrelname,
    idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0
  AND indexrelname NOT LIKE '%_pkey'
ORDER BY pg_relation_size(indexrelid) DESC;

-- Find missing indexes
SELECT
    schemaname,
    relname,
    seq_scan,
    seq_tup_read,
    idx_scan,
    n_live_tup
FROM pg_stat_user_tables
WHERE seq_scan > 100
  AND n_live_tup > 10000
ORDER BY seq_tup_read DESC;

-- Index size
SELECT
    pg_size_pretty(pg_relation_size(indexrelid)) AS index_size,
    indexrelname,
    relname
FROM pg_stat_user_indexes
ORDER BY pg_relation_size(indexrelid) DESC
LIMIT 20;
```

---

## EXPLAIN Plans

### Basic Usage

```sql
EXPLAIN SELECT * FROM employees WHERE salary > 50000;
```

### With Analysis

```sql
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM employees WHERE department_id = 1 AND salary > 50000;
```

### Reading the Output

```
Seq Scan on employees  (cost=0.00..1234.00 rows=500 width=100)
  Filter: ((salary > 50000) AND (department_id = 1))
  Rows Removed by Filter: 9500
Planning Time: 0.125 ms
Execution Time: 12.345 ms
```

**Key Metrics:**
- `cost`: Estimated cost (startup..total)
- `rows`: Estimated row count
- `width`: Estimated row width in bytes
- `actual time`: Real execution time
- `loops`: Number of times plan was executed

### Common Plan Types

| Plan | When Used | Performance |
|------|-----------|-------------|
| `Seq Scan` | Small tables, no suitable index | Slow for large tables |
| `Index Scan` | Index matches WHERE clause | Fast |
| `Index Only Scan` | All data in index | Fastest |
| `Bitmap Scan` | Multiple index conditions | Medium |
| `Nested Loop` | Small result sets | Fast for small data |
| `Hash Join` | Larger joins | Good for medium data |
| `Merge Join` | Pre-sorted data | Good for large data |

### Identifying Problems

```sql
-- Seq scan on large table (needs index)
EXPLAIN SELECT * FROM orders WHERE customer_id = 123;

-- Nested loop on large tables (needs different plan)
EXPLAIN SELECT * FROM orders o
JOIN customers c ON o.customer_id = c.id;

-- Sort spill to disk (needs more memory or index)
EXPLAIN ANALYZE SELECT * FROM large_table ORDER BY column;
```

### Plan Comparison

```sql
-- Compare two query plans
EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM employees WHERE department_id = 1;

EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM employees WHERE department_id IN (1, 2, 3);
```

---

## Query Rewriting

### Replace Subqueries with JOINs

```sql
-- Slow: Correlated subquery
SELECT *
FROM employees e
WHERE e.salary > (
    SELECT AVG(salary)
    FROM employees
    WHERE department_id = e.department_id
);

-- Fast: JOIN
SELECT e.*
FROM employees e
JOIN (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
) d ON e.department_id = d.department_id
WHERE e.salary > d.avg_salary;
```

### Replace IN with EXISTS

```sql
-- Slow: Large IN list
SELECT * FROM customers
WHERE id IN (SELECT customer_id FROM orders WHERE total > 1000);

-- Fast: EXISTS
SELECT c.*
FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o
    WHERE o.customer_id = c.id
      AND o.total > 1000
);
```

### Use EXISTS Instead of COUNT

```sql
-- Slow: Counts all matching rows
SELECT *
FROM customers c
WHERE (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.id) > 0;

-- Fast: Stops at first match
SELECT *
FROM customers c
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id);
```

### Avoid SELECT *

```sql
-- Slow: Fetches all columns
SELECT * FROM orders WHERE customer_id = 123;

-- Fast: Only needed columns
SELECT id, order_date, total FROM orders WHERE customer_id = 123;
```

### Optimize LIKE Queries

```sql
-- Slow: Leading wildcard
SELECT * FROM products WHERE name LIKE '%phone%';

-- Fast: Trailing wildcard (uses index)
SELECT * FROM products WHERE name LIKE 'phone%';

-- Fast: Full-text search for complex patterns
SELECT * FROM products
WHERE to_tsvector('english', name) @@ to_tsquery('phone');
```

### Batch Operations

```sql
-- Slow: Row-by-row updates
UPDATE orders SET status = 'processed' WHERE id = 1;
UPDATE orders SET status = 'processed' WHERE id = 2;
UPDATE orders SET status = 'processed' WHERE id = 3;

-- Fast: Single batch update
UPDATE orders SET status = 'processed' WHERE id IN (1, 2, 3);

-- For large batches: Process in chunks
DO $$
DECLARE
    batch_size INT := 1000;
    affected INT;
BEGIN
    LOOP
        UPDATE orders
        SET status = 'processed'
        WHERE id IN (
            SELECT id FROM orders
            WHERE status = 'pending'
            LIMIT batch_size
        );
        GET DIAGNOSTICS affected = ROW_COUNT;
        EXIT WHEN affected = 0;
        COMMIT;
    END LOOP;
END $$;
```

---

## Statistics

### Update Statistics

```sql
-- PostgreSQL: Update table statistics
ANALYZE employees;

-- Update specific columns
ANALYZE employees (salary, department_id);

-- Full vacuum and analyze
VACUUM ANALYZE employees;
```

### Check Statistics

```sql
-- Table statistics
SELECT
    relname,
    n_live_tup,
    n_dead_tup,
    last_vacuum,
    last_autovacuum,
    last_analyze,
    last_autoanalyze
FROM pg_stat_user_tables
WHERE relname = 'employees';

-- Column statistics
SELECT
    attname,
    n_distinct,
    most_common_vals,
    most_common_freqs,
    histogram_bounds
FROM pg_stats
WHERE tablename = 'employees'
  AND attname = 'salary';
```

### Statistics Target

```sql
-- Increase statistics for specific columns
ALTER TABLE employees ALTER COLUMN salary SET STATISTICS 1000;

-- Update statistics
ANALYZE employees;
```

---

## Partitioning

### When to Partition

- Tables with millions of rows
- Time-series data
- Data with clear access patterns
- Need to archive old data efficiently

### Range Partitioning

```sql
-- Partition by date range
CREATE TABLE orders (
    id SERIAL,
    order_date DATE NOT NULL,
    amount NUMERIC(10,2)
) PARTITION BY RANGE (order_date);

-- Create partitions
CREATE TABLE orders_2023 PARTITION OF orders
    FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');
CREATE TABLE orders_2024_q1 PARTITION OF orders
    FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');
CREATE TABLE orders_2024_q2 PARTITION OF orders
    FOR VALUES FROM ('2024-04-01') TO ('2024-07-01');
```

### Partition Pruning

```sql
-- Query only scans relevant partitions
EXPLAIN SELECT * FROM orders
WHERE order_date BETWEEN '2024-01-01' AND '2024-03-31';

-- Verify with EXPLAIN
-- Should show only orders_2024_q1 partition
```

### Automatic Partition Management

```sql
-- Create monthly partitions
DO $$
DECLARE
    start_date DATE := '2024-01-01';
    end_date DATE;
    partition_name TEXT;
BEGIN
    FOR i IN 0..11 LOOP
        end_date := start_date + INTERVAL '1 month';
        partition_name := 'orders_' || TO_CHAR(start_date, 'YYYY_MM');

        EXECUTE format('
            CREATE TABLE IF NOT EXISTS %I PARTITION OF orders
            FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date
        );

        start_date := end_date;
    END LOOP;
END $$;
```

---

## Denormalization

### When to Denormalize

- Read-heavy workloads
- Complex joins are slow
- Reporting/analytics queries
- Caching derived data

### Materialized Views

```sql
-- Create materialized view for complex aggregation
CREATE MATERIALIZED VIEW mv_sales_summary AS
SELECT
    DATE_TRUNC('month', order_date) AS month,
    category,
    COUNT(*) AS order_count,
    SUM(amount) AS total_revenue,
    AVG(amount) AS avg_order_value
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
JOIN products p ON oi.product_id = p.id
GROUP BY 1, 2;

-- Refresh periodically
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_sales_summary;
```

### Summary Tables

```sql
-- Daily sales summary
CREATE TABLE daily_sales (
    sale_date DATE PRIMARY KEY,
    total_orders INTEGER,
    total_revenue NUMERIC(12,2),
    avg_order_value NUMERIC(10,2),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Update with triggers or batch jobs
```

### Redundant Columns

```sql
-- Add computed columns for fast reads
ALTER TABLE orders ADD COLUMN customer_name VARCHAR(100);

-- Keep in sync with triggers
CREATE OR REPLACE FUNCTION sync_customer_name()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE orders
    SET customer_name = (SELECT name FROM customers WHERE id = NEW.customer_id)
    WHERE id = NEW.id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

---

## Caching

### Application-Level Caching

```python
# Pseudocode for cache pattern
import redis

cache = redis.Redis()

def get_customer(customer_id):
    cache_key = f"customer:{customer_id}"
    cached = cache.get(cache_key)
    if cached:
        return json.loads(cached)

    customer = db.query("SELECT * FROM customers WHERE id = %s", customer_id)
    cache.setex(cache_key, 3600, json.dumps(customer))  # 1 hour TTL
    return customer
```

### Query Result Caching

```sql
-- PostgreSQL: pg_prewarm for buffer cache
CREATE EXTENSION pg_prewarm;
SELECT pg_prewarm('employees');

-- pg_store_plans for plan caching
CREATE EXTENSION pg_store_plans;
```

### Cache Invalidation

```sql
-- Invalidate cache on data change
CREATE OR REPLACE FUNCTION invalidate_customer_cache()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM pg_notify('cache_invalidate', 'customer:' || NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_invalidate_cache
AFTER INSERT OR UPDATE ON customers
FOR EACH ROW
EXECUTE FUNCTION invalidate_customer_cache();
```

---

## Connection Pooling

### PgBouncer Configuration

```ini
; pgbouncer.ini
[databases]
mydb = host=localhost port=5432 dbname=mydb

[pgbouncer]
listen_port = 6432
listen_addr = 127.0.0.1
auth_type = md5
auth_file = /etc/pgbouncer/userlist.txt
pool_mode = transaction
max_client_conn = 100
default_pool_size = 20
min_pool_size = 5
reserve_pool_size = 5
```

### Connection Pool Sizing

```
Formula: connections = (2 × CPU cores) + effective_spindle_count

For SSD: connections ≈ 2 × CPU cores
For HDD: connections ≈ (2 × CPU cores) + number_of_disks
```

### Monitoring Connections

```sql
-- Active connections
SELECT
    datname,
    usename,
    state,
    COUNT(*)
FROM pg_stat_activity
GROUP BY datname, usename, state;

-- Connection limits
SHOW max_connections;
SELECT current_setting('max_connections');
```

---

## Common Anti-Patterns

### 1. N+1 Query Problem

```sql
-- Bad: N+1 queries
SELECT * FROM customers;
-- Then for each customer:
SELECT * FROM orders WHERE customer_id = ?;

-- Good: Single JOIN
SELECT c.*, o.*
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id;
```

### 2. Implicit Type Conversion

```sql
-- Bad: Forces index scan failure
SELECT * FROM employees WHERE department_id = '1';

-- Good: Use correct type
SELECT * FROM employees WHERE department_id = 1;
```

### 3. Functions on Indexed Columns

```sql
-- Bad: Cannot use index
SELECT * FROM employees WHERE LOWER(email) = 'john@example.com';

-- Good: Expression index
CREATE INDEX idx_email_lower ON employees (LOWER(email));
```

### 4. Large Offset Pagination

```sql
-- Bad: Slow for large offsets
SELECT * FROM employees ORDER BY id LIMIT 20 OFFSET 1000000;

-- Good: Keyset pagination
SELECT * FROM employees
WHERE id > 1000000
ORDER BY id
LIMIT 20;
```

### 5. OR Conditions

```sql
-- Bad: May not use index efficiently
SELECT * FROM employees
WHERE department_id = 1 OR department_id = 2;

-- Good: Use IN
SELECT * FROM employees
WHERE department_id IN (1, 2);
```

### 6. SELECT DISTINCT

```sql
-- Bad: Requires sorting/hashing
SELECT DISTINCT department_id FROM employees;

-- Good: Use GROUP BY or EXISTS
SELECT department_id FROM employees GROUP BY department_id;
```

### 7. NOT IN with NULLs

```sql
-- Bad: Returns nothing if subquery has NULL
SELECT * FROM employees
WHERE id NOT IN (SELECT employee_id FROM terminated);

-- Good: Use NOT EXISTS
SELECT * FROM employees e
WHERE NOT EXISTS (
    SELECT 1 FROM terminated t WHERE t.employee_id = e.id
);
```

---

## Performance Checklist

- [ ] All foreign keys have indexes
- [ ] Queries use covering indexes where possible
- [ ] EXPLAIN shows Index Scan, not Seq Scan on large tables
- [ ] No SELECT * in production queries
- [ ] Batch operations instead of row-by-row
- [ ] Statistics are up to date
- [ ] Connection pooling is configured
- [ ] Slow query logging is enabled
- [ ] Regular VACUUM and ANALYZE
- [ ] Partitioning for large tables
