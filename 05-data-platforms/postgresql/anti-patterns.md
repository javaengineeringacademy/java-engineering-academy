# PostgreSQL Anti-Patterns

## 1. N+1 Query Problem
**Description:** Executing separate queries for related data instead of using JOINs.

**Why it's bad:** Excessive database round trips, poor performance.

**Example (bad code):**
```sql
SELECT * FROM orders;
-- Then for each order:
SELECT * FROM customers WHERE id = order.customer_id;
```

**Better approach:** Use JOINs:
```sql
SELECT o.*, c.* 
FROM orders o
JOIN customers c ON o.customer_id = c.id;
```

**Impact:** Single query, better performance.

---

## 2. Missing Indexes
**Description:** Not creating indexes for frequently queried columns.

**Why it's bad:** Full table scans, slow queries, poor performance at scale.

**Example (bad code):**
```sql
-- No index on email
SELECT * FROM users WHERE email = 'user@example.com';
```

**Better approach:** Create appropriate indexes:
```sql
CREATE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX idx_users_email_unique ON users(email);

-- Composite indexes for common queries
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
```

**Impact:** Fast lookups, better query performance.

---

## 3. Over-Indexing
**Description:** Creating too many indexes on a table.

**Why it's bad:** Slows down writes, increases storage, maintenance overhead.

**Example (bad code):**
```sql
CREATE INDEX idx1 ON users(name);
CREATE INDEX idx2 ON users(email);
CREATE INDEX idx3 ON users(created_at);
CREATE INDEX idx4 ON users(status);
-- Too many single-column indexes
```

**Better approach:** Use composite indexes:
```sql
-- Single composite index for common queries
CREATE INDEX idx_users_composite ON users(status, created_at, name);
```

**Impact:** Fewer indexes, better write performance.

---

## 4. SELECT *
**Description:** Fetching all columns when only some are needed.

**Why it's bad:** Wasted bandwidth, increased latency, prevents index-only scans.

**Example (bad code):**
```sql
SELECT * FROM users WHERE status = 'active';
```

**Better approach:** Select specific columns:
```sql
SELECT id, name, email FROM users WHERE status = 'active';
```

**Impact:** Reduced data transfer, enables index-only scans.

---

## 5. Not Using EXPLAIN
**Description:** Not analyzing query execution plans.

**Why it's bad:** Missed optimization opportunities, slow queries in production.

**Example (bad code):**
```sql
-- Running queries without understanding performance
SELECT * FROM large_table WHERE column = 'value';
```

**Better approach:** Analyze with EXPLAIN:
```sql
EXPLAIN ANALYZE
SELECT * FROM large_table WHERE column = 'value';

-- Check for sequential scans, sort operations
```

**Impact:** Query optimization, performance improvements.

---

## 6. Ignoring Connection Pooling
**Description:** Creating new connections for each query.

**Why it's bad:** Connection overhead, exhausted connections under load.

**Example (bad code):**
```python
# New connection for each operation
conn = psycopg2.connect(dATABASE_URL)
# execute query
conn.close()
```

**Better approach:** Use connection pooling:
```python
from psycopg2 import pool

connection_pool = pool.ThreadedConnectionPool(
    minconn=5,
    maxconn=20,
    dATABASE=DATABASE_URL
)
```

**Impact:** Better performance, resource efficiency.

---

## 7. Using TRUNCATE Without Backup
**Description:** Using TRUNCATE or DELETE without WHERE clause on production data.

**Why it's bad:** Data loss, no recovery possible.

**Example (bad code):**
```sql
TRUNCATE TABLE important_data;
DELETE FROM logs WHERE created_at < '2020-01-01';
```

**Better approach:** Backup first, use safe operations:
```sql
-- Backup first
CREATE TABLE backup AS SELECT * FROM important_data;

-- Or use LIMIT with DELETE
DELETE FROM logs WHERE created_at < '2020-01-01' LIMIT 10000;
```

**Impact:** Data safety, recoverability.

---

## 8. Not Using Transactions
**Description:** Not wrapping multi-step operations in transactions.

**Why it's bad:** Data inconsistency if operation fails midway.

**Example (bad code):**
```sql
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
-- Application crashes here
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
```

**Better approach:** Use transactions:
```sql
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;
```

**Impact:** Data consistency, atomic operations.

---

## 9. Ignoring Vacuum and Analyze
**Description:** Not running VACUUM and ANALYZE regularly.

**Why it's bad:** Table bloat, poor query plans, performance degradation.

**Example (bad code):**
```sql
-- No vacuum schedule
-- Table grows with dead tuples
-- Query planner uses outdated statistics
```

**Better approach:** Configure autovacuum:
```sql
ALTER TABLE large_table SET (
    autovacuum_vacuum_scale_factor = 0.1,
    autovacuum_analyze_scale_factor = 0.05
);

-- Manual vacuum for large operations
VACUUM ANALYZE large_table;
```

**Impact:** Optimal performance, accurate query plans.

---

## 10. Using LIKE with Leading Wildcard
**Description:** Using '%value' pattern in LIKE queries.

**Why it's bad:** Cannot use indexes, full table scan.

**Example (bad code):**
```sql
SELECT * FROM users WHERE name LIKE '%john%';
```

**Better approach:** Use full-text search or trigram indexes:
```sql
-- Full-text search
SELECT * FROM users WHERE to_tsvector('english', name) @@ to_tsquery('john');

-- Or trigram index
CREATE EXTENSION pg_trgm;
CREATE INDEX idx_users_name_trgm ON users USING gin(name gin_trgm_ops);
SELECT * FROM users WHERE name LIKE '%john%';
```

**Impact:** Index utilization, faster searches.

---

## 11. Ignoring Data Types
**Description:** Using inappropriate data types (e.g., VARCHAR for dates).

**Why it's bad:** Slower queries, more storage, no validation.

**Example (bad code):**
```sql
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    event_date VARCHAR(20)  -- Should be DATE or TIMESTAMP
);
```

**Better approach:** Use appropriate types:
```sql
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    event_date TIMESTAMP NOT NULL,
    status VARCHAR(20)
);
```

**Impact:** Better performance, data integrity, less storage.

---

## 12. Not Using Prepared Statements
**Description:** Not using prepared statements for repeated queries.

**Why it's bad:** SQL injection risk, repeated parsing overhead.

**Example (bad code):**
```sql
-- String concatenation
EXECUTE 'SELECT * FROM users WHERE id = ' || user_id;
```

**Better approach:** Use prepared statements:
```sql
PREPARE get_user AS SELECT * FROM users WHERE id = $1;
EXECUTE get_user(123);
```

**Impact:** Security, better performance.