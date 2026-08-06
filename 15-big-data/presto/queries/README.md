# Presto Queries

## Query Optimization
```sql
-- Use EXPLAIN to understand query plan
EXPLAIN SELECT * FROM orders WHERE total > 1000;

-- Use EXPLAIN (TYPE DISTRIBUTED) for distributed plan
EXPLAIN (TYPE DISTRIBUTED) SELECT count(*) FROM orders;

-- Filter pushdown
SELECT * FROM hive.default.orders WHERE year = 2024;

-- Partition pruning
SELECT * FROM hive.default.logs WHERE dt = '2024-01-15';
```

## Window Functions
```sql
SELECT
    customer_id,
    order_date,
    total,
    ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY order_date DESC) as rn,
    SUM(total) OVER (PARTITION BY customer_id ORDER BY order_date) as running_total,
    LAG(total) OVER (PARTITION BY customer_id ORDER BY order_date) as prev_total
FROM orders;
```

## CTEs and Subqueries
```sql
WITH customer_stats AS (
    SELECT customer_id, COUNT(*) as order_count, SUM(total) as total_spent
    FROM orders GROUP BY customer_id
)
SELECT c.name, cs.order_count, cs.total_spent
FROM customers c
JOIN customer_stats cs ON c.id = cs.customer_id
WHERE cs.order_count > 5;
```

## Join Optimization
```java
// Presto join strategies
// 1. Broadcast join (small table fits in memory)
SET SESSION join_distribution_type = 'BROADCAST';

// 2. Hash join (both sides fit in memory)
SET SESSION join_distribution_type = 'HASH';

// 3. Partition join (co-partitioned tables)
SELECT /*+ PARTITIONED(o, c) */ *
FROM orders o JOIN customers c ON o.customer_id = c.id;
```

## Performance Tips
```sql
-- Use approximate functions for large datasets
SELECT approx_distinct(user_id) FROM events;

-- Use array functions instead of joins for simple lookups
SELECT filter(orders, x -> x.total > 1000) FROM orders;

-- Use bucketed tables
SELECT * FROM hive.default.events_bucketed WHERE user_id = '123';
```
