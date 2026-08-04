# Indexing Fundamentals

## Comprehensive Guide to Database Indexing

Indexes improve query performance by allowing fast data retrieval. This guide covers index types, creation, and optimization.

---

## Table of Contents

1. [Index Types](#index-types)
2. [Index Creation](#index-creation)
3. [Index Management](#index-management)
4. [Best Practices](#best-practices)

---

## Index Types

### B-Tree Index

```sql
-- Default index type
CREATE INDEX idx_users_email ON users(email);

-- Composite index
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);
```

```
Pros:
- Fast equality and range queries
- Supports ordering
- Works with comparisons

Cons:
- Not ideal for full-text search
- Larger index size
```

### Hash Index

```sql
-- Hash index (PostgreSQL)
CREATE INDEX idx_users_email_hash ON users USING hash(email);
```

```
Pros:
- Fast equality queries
- Smaller index size

Cons:
- No range queries
- No ordering
```

### GIN Index

```sql
-- GIN index for arrays and full-text
CREATE INDEX idx_posts_tags ON posts USING gin(tags);

-- Full-text search
CREATE INDEX idx_posts_content ON posts USING gin(to_tsvector('english', content));
```

```
Pros:
- Fast full-text search
- Array operations
- JSONB queries

Cons:
- Slower updates
- Larger index size
```

### GiST Index

```sql
-- GiST index for geometric data
CREATE INDEX idx_locations_point ON locations USING gist(point);

-- Full-text search
CREATE INDEX idx_posts_content ON posts USING gist(to_tsvector('english', content));
```

```
Pros:
- Geometric data
- Full-text search
- Range queries

Cons:
- Slower than B-tree for simple queries
```

### BRIN Index

```sql
-- BRIN index for large tables
CREATE INDEX idx_logs_timestamp ON logs USING brin(timestamp);
```

```
Pros:
- Very small index size
- Fast for ordered data
- Low maintenance

Cons:
- Less precise than B-tree
```

---

## Index Creation

### Create Index

```sql
-- Basic index
CREATE INDEX idx_users_email ON users(email);

-- Unique index
CREATE UNIQUE INDEX idx_users_email ON users(email);

-- Composite index
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);

-- Partial index
CREATE INDEX idx_orders_pending ON orders(order_date)
WHERE status = 'pending';
```

### Create Index Concurrently

```sql
-- PostgreSQL concurrent index creation
CREATE INDEX CONCURRENTLY idx_users_email ON users(email);

-- MySQL online index creation
CREATE INDEX idx_users_email ON users(email) ALGORITHM=INPLACE;
```

### Create Index on Expression

```sql
-- Functional index
CREATE INDEX idx_users_lower_email ON users(lower(email));

-- Expression index
CREATE INDEX idx_orders_year ON orders(extract(year from order_date));
```

---

## Index Management

### Drop Index

```sql
-- Drop index
DROP INDEX idx_users_email;

-- Drop index if exists
DROP INDEX IF EXISTS idx_users_email;
```

### Rename Index

```sql
-- PostgreSQL
ALTER INDEX idx_users_email RENAME TO idx_users_email_addr;

-- MySQL
ALTER TABLE users RENAME INDEX idx_users_email TO idx_users_email_addr;
```

### Reindex

```sql
-- PostgreSQL
REINDEX INDEX idx_users_email;
REINDEX TABLE users;

-- MySQL
ALTER TABLE users ENGINE=InnoDB;
```

### Check Index Usage

```sql
-- PostgreSQL
SELECT * FROM pg_stat_user_indexes WHERE indexrelname = 'idx_users_email';

-- MySQL
SHOW INDEX FROM users;
```

---

## Best Practices

### 1. Index Frequently Queried Columns

```sql
-- Good - Index on WHERE clause
CREATE INDEX idx_users_email ON users(email);

-- Good - Index on JOIN column
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
```

### 2. Use Composite Indexes

```sql
-- Good - Composite index
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);

-- Query uses composite index
SELECT * FROM orders
WHERE customer_id = 123 AND order_date > '2024-01-01';
```

### 3. Avoid Over-Indexing

```sql
-- Bad - Too many indexes
CREATE INDEX idx1 ON users(email);
CREATE INDEX idx2 ON users(name);
CREATE INDEX idx3 ON users(phone);
CREATE INDEX idx4 ON users(address);
CREATE INDEX idx5 ON users(city);
```

### 4. Use Partial Indexes

```sql
-- Good - Partial index
CREATE INDEX idx_orders_pending ON orders(order_date)
WHERE status = 'pending';

-- Query uses partial index
SELECT * FROM orders WHERE status = 'pending' AND order_date > '2024-01-01';
```

### 5. Monitor Index Performance

```sql
-- Check index usage
SELECT * FROM pg_stat_user_indexes;

-- Check index size
SELECT * FROM pg_indexes WHERE tablename = 'users';

-- Check missing indexes
SELECT * FROM pg_stat_user_tables WHERE seq_scan > 100;
```

---

## Further Reading

- [PostgreSQL Indexes](https://www.postgresql.org/docs/current/indexes.html)
- [MySQL Indexes](https://dev.mysql.com/doc/refman/8.0/en/create-index.html)
- [Index Types](https://www.postgresql.org/docs/current/indexes-types.html)
