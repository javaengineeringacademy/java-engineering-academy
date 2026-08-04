# Query Optimization

## Comprehensive Guide to Query Optimization

Query optimization improves database performance by writing efficient queries. This guide covers query analysis, indexing, and tuning.

---

## Table of Contents

1. [Query Analysis](#query-analysis)
2. [Query Optimization](#query-optimization)
3. [Index Optimization](#index-optimization)
4. [Best Practices](#best-practices)

---

## Query Analysis

### EXPLAIN

```sql
-- PostgreSQL
EXPLAIN SELECT * FROM users WHERE email = 'john@example.com';

-- MySQL
EXPLAIN SELECT * FROM users WHERE email = 'john@example.com';
```

### EXPLAIN ANALYZE

```sql
-- PostgreSQL
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john@example.com';

-- MySQL
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john@example.com';
```

### Query Plan

```
Seq Scan on users  (cost=0.00..1234.00 rows=1 width=100)
  Filter: (email = 'john@example.com')
```

---

## Query Optimization

### Avoid SELECT *

```sql
-- Bad
SELECT * FROM users WHERE email = 'john@example.com';

-- Good
SELECT id, name, email FROM users WHERE email = 'john@example.com';
```

### Use JOIN Instead of Subquery

```sql
-- Bad
SELECT * FROM users
WHERE id IN (SELECT user_id FROM orders);

-- Good
SELECT DISTINCT u.* FROM users u
JOIN orders o ON u.id = o.user_id;
```

### Use LIMIT

```sql
-- Bad
SELECT * FROM orders ORDER BY order_date DESC;

-- Good
SELECT * FROM orders ORDER BY order_date DESC LIMIT 100;
```

### Use WHERE Instead of HAVING

```sql
-- Bad
SELECT customer_id, COUNT(*) as order_count
FROM orders
GROUP BY customer_id
HAVING customer_id > 100;

-- Good
SELECT customer_id, COUNT(*) as order_count
FROM orders
WHERE customer_id > 100
GROUP BY customer_id;
```

### Use EXISTS Instead of COUNT

```sql
-- Bad
SELECT * FROM users
WHERE (SELECT COUNT(*) FROM orders WHERE orders.user_id = users.id) > 0;

-- Good
SELECT * FROM users u
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

### Use UNION ALL Instead of UNION

```sql
-- Bad
SELECT * FROM orders_2024
UNION
SELECT * FROM orders_2025;

-- Good
SELECT * FROM orders_2024
UNION ALL
SELECT * FROM orders_2025;
```

### Use IN Instead of OR

```sql
-- Bad
SELECT * FROM users
WHERE id = 1 OR id = 2 OR id = 3;

-- Good
SELECT * FROM users
WHERE id IN (1, 2, 3);
```

### Use Batch Operations

```sql
-- Bad
INSERT INTO orders VALUES (1, 100);
INSERT INTO orders VALUES (2, 200);
INSERT INTO orders VALUES (3, 300);

-- Good
INSERT INTO orders VALUES (1, 100), (2, 200), (3, 300);
```

---

## Index Optimization

### Create Appropriate Indexes

```sql
-- Good - Index on WHERE clause
CREATE INDEX idx_users_email ON users(email);

-- Good - Index on JOIN column
CREATE INDEX idx_orders_customer_id ON orders(customer_id);

-- Good - Composite index
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);
```

### Drop Unused Indexes

```sql
-- Check index usage
SELECT * FROM pg_stat_user_indexes WHERE idx_scan = 0;

-- Drop unused index
DROP INDEX idx_unused;
```

### Reindex

```sql
-- PostgreSQL
REINDEX INDEX idx_users_email;
REINDEX TABLE users;

-- MySQL
ALTER TABLE users ENGINE=InnoDB;
```

---

## Best Practices

### 1. Use EXPLAIN

```sql
-- Always check query plan
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john@example.com';
```

### 2. Use Appropriate Data Types

```sql
-- Good
CREATE TABLE users (
  id INT PRIMARY KEY,
  email VARCHAR(100),
  name VARCHAR(100)
);

-- Bad
CREATE TABLE users (
  id VARCHAR(100),
  email TEXT,
  name TEXT
);
```

### 3. Use Connection Pooling

```python
# Good - Use connection pooling
import psycopg2
from psycopg2 import pool

connection_pool = psycopg2.pool.ThreadedConnectionPool(
    minconn=5,
    maxconn=20,
    dsn="dbname=mydb user=myuser"
)
```

### 4. Use Caching

```python
# Good - Cache frequent queries
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

def get_user(user_id):
    cached = r.get(f"user:{user_id}")
    if cached:
        return json.loads(cached)
    
    user = db.query("SELECT * FROM users WHERE id = %s", user_id)
    r.setex(f"user:{user_id}", 3600, json.dumps(user))
    return user
```

### 5. Monitor Performance

```sql
-- Check slow queries
SHOW VARIABLES LIKE 'slow_query_log';

-- Check query cache
SHOW VARIABLES LIKE 'query_cache_size';

-- Check connections
SHOW STATUS LIKE 'Threads_connected';
```

---

## Further Reading

- [Query Optimization](https://www.postgresql.org/docs/current/performance-tips.html)
- [MySQL Query Optimization](https://dev.mysql.com/doc/refman/8.0/en/optimization.html)
- [EXPLAIN](https://www.postgresql.org/docs/current/using-explain.html)
