# PostgreSQL Pitfalls

## N+1 Query Problem

### Problem

```sql
-- Bad: N+1 queries
SELECT * FROM users;
-- Then for each user:
SELECT * FROM orders WHERE user_id = ?;
```

### Solution

```sql
-- Good: Single query with JOIN
SELECT u.*, o.*
FROM users u
LEFT JOIN orders o ON u.id = o.user_id;
```

## Missing Indexes

### Problem

```sql
-- Slow query without index
SELECT * FROM orders WHERE customer_id = 123;
```

### Solution

```sql
-- Add appropriate index
CREATE INDEX idx_orders_customer ON orders(customer_id);
```

## Over-Fetching Data

### Problem

```sql
-- Bad: Fetching all columns
SELECT * FROM large_table;

-- Bad: No LIMIT clause
SELECT * FROM orders ORDER BY created_at;
```

### Solution

```sql
-- Good: Select specific columns
SELECT id, name, email FROM large_table;

-- Good: Use LIMIT
SELECT * FROM orders ORDER BY created_at LIMIT 100;
```

## Implicit Type Casting

### Problem

```sql
-- Bad: Implicit casting
SELECT * FROM users WHERE id = '123';
```

### Solution

```sql
-- Good: Explicit casting
SELECT * FROM users WHERE id = 123;
```

## Transaction Locking

### Problem

```sql
-- Long-running transaction
BEGIN;
-- Expensive operation
UPDATE users SET status = 'active' WHERE id = 1;
-- Long pause
COMMIT;
```

### Solution

```sql
-- Short transaction
BEGIN;
UPDATE users SET status = 'active' WHERE id = 1;
COMMIT;
```

## Missing Foreign Key Indexes

### Problem

```sql
-- No index on foreign key
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id)
);
```

### Solution

```sql
-- Add index on foreign key
CREATE INDEX idx_orders_customer ON orders(customer_id);
```

## Autovacuum Not Keeping Up

### Problem

```sql
-- Table bloat due to dead tuples
SELECT schemaname, relname, n_dead_tup, last_autovacuum
FROM pg_stat_user_tables
WHERE n_dead_tup > 10000;
```

### Solution

```sql
-- Manual vacuum
VACUUM VERBOSE users;

-- Tune autovacuum
ALTER TABLE users SET (autovacuum_vacuum_scale_factor = 0.1);
```

## Connection Exhaustion

### Problem

```sql
-- Too many connections
SELECT count(*) FROM pg_stat_activity;
-- Returns 200+
```

### Solution

```ini
# Use connection pooling
# PgBouncer configuration
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 25
```

## Large Object Bloat

### Problem

```sql
-- Table bloat
SELECT pg_size_pretty(pg_total_relation_size('large_table'));
```

### Solution

```sql
-- Vacuum full (locks table)
VACUUM FULL large_table;

-- Or use pg_repack
pg_repack -d mydb -t large_table
```

## Unparameterized Queries

### Problem

```sql
-- Bad: Different query plans for different values
SELECT * FROM users WHERE status = 'active';
SELECT * FROM users WHERE status = 'inactive';
```

### Solution

```sql
-- Good: Use parameters
PREPARE query (text) AS
SELECT * FROM users WHERE status = $1;

EXECUTE query('active');
```

## Ignoring EXPLAIN

### Problem

```sql
-- Not checking query plan
SELECT * FROM users WHERE email LIKE '%@example.com';
```

### Solution

```sql
-- Always check EXPLAIN
EXPLAIN ANALYZE
SELECT * FROM users WHERE email LIKE '%@example.com';
```

## Best Practices

1. Always use EXPLAIN ANALYZE
2. Add indexes on foreign keys
3. Use connection pooling
4. Keep transactions short
5. Monitor slow queries
