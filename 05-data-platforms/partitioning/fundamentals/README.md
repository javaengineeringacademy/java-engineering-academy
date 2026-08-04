# Partitioning Fundamentals

## Comprehensive Guide to Database Partitioning

Partitioning divides tables into smaller pieces for performance. This guide covers partition types, management, and best practices.

---

## Table of Contents

1. [Partitioning Types](#partitioning-types)
2. [Partition Management](#partition-management)
3. [Partition Pruning](#partition-pruning)
4. [Best Practices](#best-practices)

---

## Partitioning Types

### Range Partitioning

```sql
-- PostgreSQL range partitioning
CREATE TABLE orders (
  id INT,
  order_date DATE,
  amount DECIMAL(10,2)
) PARTITION BY RANGE (order_date);

CREATE TABLE orders_2024 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE orders_2025 PARTITION OF orders
FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
```

### List Partitioning

```sql
-- PostgreSQL list partitioning
CREATE TABLE orders (
  id INT,
  region VARCHAR(20),
  amount DECIMAL(10,2)
) PARTITION BY LIST (region);

CREATE TABLE orders_usa PARTITION OF orders
FOR VALUES IN ('USA', 'CANADA');

CREATE TABLE orders_europe PARTITION OF orders
FOR VALUES IN ('UK', 'FRANCE', 'GERMANY');
```

### Hash Partitioning

```sql
-- PostgreSQL hash partitioning
CREATE TABLE orders (
  id INT,
  customer_id INT,
  amount DECIMAL(10,2)
) PARTITION BY HASH (customer_id);

CREATE TABLE orders_p0 PARTITION OF orders
FOR VALUES WITH (MODULUS 4, REMAINDER 0);

CREATE TABLE orders_p1 PARTITION OF orders
FOR VALUES WITH (MODULUS 4, REMAINDER 1);
```

### MySQL Partitioning

```sql
-- MySQL range partitioning
CREATE TABLE orders (
  id INT,
  order_date DATE,
  amount DECIMAL(10,2)
) PARTITION BY RANGE (YEAR(order_date)) (
  PARTITION p2024 VALUES LESS THAN (2025),
  PARTITION p2025 VALUES LESS THAN (2026),
  PARTITION p2026 VALUES LESS THAN (2027)
);
```

---

## Partition Management

### Add Partition

```sql
-- Add new partition
CREATE TABLE orders_2026 PARTITION OF orders
FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');
```

### Drop Partition

```sql
-- Drop old partition
DROP TABLE orders_2024;
```

### Merge Partitions

```sql
-- Detach partitions
ALTER TABLE orders DETACH PARTITION orders_2024;
ALTER TABLE orders DETACH PARTITION orders_2025;

-- Create merged partition
CREATE TABLE orders_2024_2025 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2026-01-01');
```

### Split Partition

```sql
-- Detach partition
ALTER TABLE orders DETACH PARTITION orders_2024;

-- Create split partitions
CREATE TABLE orders_2024_h1 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2024-07-01');

CREATE TABLE orders_2024_h2 PARTITION OF orders
FOR VALUES FROM ('2024-07-01') TO ('2025-01-01');
```

---

## Partition Pruning

### How It Works

```sql
-- Query with partition pruning
SELECT * FROM orders
WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01';

-- Only scans orders_2024 partition
```

### Verify Pruning

```sql
-- PostgreSQL
EXPLAIN SELECT * FROM orders
WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01';

-- Should show only relevant partition
```

---

## Best Practices

### 1. Choose Right Partition Key

```sql
-- Good - High cardinality key
CREATE TABLE orders (
  id INT,
  order_date DATE,
  amount DECIMAL(10,2)
) PARTITION BY RANGE (order_date);

-- Bad - Low cardinality key
CREATE TABLE orders (
  id INT,
  status VARCHAR(20),
  amount DECIMAL(10,2)
) PARTITION BY LIST (status);
```

### 2. Use Partition Pruning

```sql
-- Good - Always include partition key
SELECT * FROM orders
WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01';

-- Bad - Missing partition key
SELECT * FROM orders
WHERE amount > 100;
```

### 3. Manage Partitions

```sql
-- Good - Create future partitions
CREATE TABLE orders_2026 PARTITION OF orders
FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');

-- Good - Drop old partitions
DROP TABLE orders_2023;
```

### 4. Monitor Partition Size

```sql
-- Check partition size
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
WHERE tablename LIKE 'orders_%';
```

### 5. Use Subpartitions

```sql
-- Create subpartitions
CREATE TABLE orders (
  id INT,
  order_date DATE,
  region VARCHAR(20),
  amount DECIMAL(10,2)
) PARTITION BY RANGE (order_date)
SUBPARTITION BY LIST (region);
```

---

## Further Reading

- [PostgreSQL Partitioning](https://www.postgresql.org/docs/current/ddl-partitioning.html)
- [MySQL Partitioning](https://dev.mysql.com/doc/refman/8.0/en/partitioning.html)
- [Partition Pruning](https://www.postgresql.org/docs/current/ddl-partitioning.html#DDL-PARTITION-PRUNING)
