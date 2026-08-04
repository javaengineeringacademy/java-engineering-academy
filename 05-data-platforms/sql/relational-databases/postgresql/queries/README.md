# PostgreSQL Queries

## Table of Contents

1. [Advanced Queries](#advanced-queries)
2. [Common Table Expressions](#common-table-expressions)
3. [Window Functions](#window-functions)
4. [JSON Queries](#json-queries)
5. [Array Queries](#array-queries)
6. [Full Text Search](#full-text-search)
7. [Lateral Joins](#lateral-joins)
8. [Recursive Queries](#recursive-queries)
9. [Query Optimization](#query-optimization)
10. [Advanced Techniques](#advanced-techniques)

---

## Advanced Queries

### Complex JOINs

```sql
-- Multi-table JOIN with aggregation
SELECT
  u.username,
  u.email,
  COUNT(DISTINCT o.id) AS order_count,
  COUNT(DISTINCT oi.product_id) AS unique_products,
  SUM(oi.quantity * oi.price) AS total_spent,
  AVG(o.total) AS avg_order_value
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
LEFT JOIN order_items oi ON o.id = oi.order_id
WHERE u.is_active = TRUE
  AND o.created_at >= NOW() - INTERVAL '90 days'
GROUP BY u.id, u.username, u.email
HAVING COUNT(DISTINCT o.id) >= 3
ORDER BY total_spent DESC;

-- LATERAL JOIN for row-dependent subqueries
SELECT
  u.username,
  recent.order_id,
  recent.total,
  recent.created_at
FROM users u
CROSS JOIN LATERAL (
  SELECT id AS order_id, total, created_at
  FROM orders
  WHERE user_id = u.id
  ORDER BY created_at DESC
  LIMIT 3
) recent;

-- Self-referential JOIN for hierarchical data
SELECT
  e.name AS employee,
  m.name AS manager,
  d.name AS department
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id
LEFT JOIN departments d ON e.department_id = d.id;
```

### Conditional Aggregation

```sql
-- Pivot-like aggregation
SELECT
  DATE_TRUNC('month', created_at) AS month,
  COUNT(*) AS total_orders,
  COUNT(*) FILTER (WHERE status = 'completed') AS completed,
  COUNT(*) FILTER (WHERE status = 'pending') AS pending,
  COUNT(*) FILTER (WHERE status = 'cancelled') AS cancelled,
  SUM(total) FILTER (WHERE status = 'completed') AS revenue,
  ROUND(
    COUNT(*) FILTER (WHERE status = 'cancelled') * 100.0 / COUNT(*), 2
  ) AS cancellation_rate
FROM orders
WHERE created_at >= NOW() - INTERVAL '12 months'
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY month;

-- Multiple aggregations
SELECT
  department,
  COUNT(*) AS emp_count,
  AVG(salary) AS avg_salary,
  PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY salary) AS median_salary,
  STDDEV(salary) AS salary_stddev,
  MAX(salary) - MIN(salary) AS salary_range
FROM employees
GROUP BY department;
```

### Set Operations

```sql
-- UNION ALL with aggregation
SELECT 'Q1' AS quarter, SUM(total) AS revenue
FROM orders WHERE created_at >= '2024-01-01' AND created_at < '2024-04-01'
UNION ALL
SELECT 'Q2', SUM(total)
FROM orders WHERE created_at >= '2024-04-01' AND created_at < '2024-07-01'
UNION ALL
SELECT 'Q3', SUM(total)
FROM orders WHERE created_at >= '2024-07-01' AND created_at < '2024-10-01'
UNION ALL
SELECT 'Q4', SUM(total)
FROM orders WHERE created_at >= '2024-10-01' AND created_at < '2025-01-01';

-- INTERSECT for common elements
SELECT user_id FROM orders_jan
INTERSECT
SELECT user_id FROM orders_feb;

-- EXCEPT for differences
SELECT user_id FROM all_users
EXCEPT
SELECT user_id FROM banned_users;
```

---

## Common Table Expressions

### Non-Recursive CTEs

```sql
-- Basic CTE
WITH active_users AS (
  SELECT id, username, email
  FROM users
  WHERE is_active = TRUE
),
user_orders AS (
  SELECT
    user_id,
    COUNT(*) AS order_count,
    SUM(total) AS total_spent
  FROM orders
  GROUP BY user_id
)
SELECT
  au.username,
  au.email,
  COALESCE(uo.order_count, 0) AS orders,
  COALESCE(uo.total_spent, 0) AS spent
FROM active_users au
LEFT JOIN user_orders uo ON au.id = uo.user_id
WHERE COALESCE(uo.total_spent, 0) > 100;

-- CTE with multiple aggregations
WITH
monthly_revenue AS (
  SELECT
    DATE_TRUNC('month', created_at) AS month,
    SUM(total) AS revenue,
    COUNT(*) AS order_count
  FROM orders
  WHERE status = 'completed'
  GROUP BY DATE_TRUNC('month', created_at)
),
revenue_stats AS (
  SELECT
    AVG(revenue) AS avg_revenue,
    STDDEV(revenue) AS stddev_revenue
  FROM monthly_revenue
)
SELECT
  mr.month,
  mr.revenue,
  mr.order_count,
  rs.avg_revenue,
  ROUND((mr.revenue - rs.avg_revenue) / rs.stddev_revenue, 2) AS z_score
FROM monthly_revenue mr
CROSS JOIN revenue_stats rs
ORDER BY mr.month;
```

### Recursive CTEs

```sql
-- Organizational hierarchy
WITH RECURSIVE org_chart AS (
  -- Base case: top-level managers
  SELECT
    id,
    name,
    manager_id,
    1 AS level,
    ARRAY[name] AS path
  FROM employees
  WHERE manager_id IS NULL

  UNION ALL

  -- Recursive case: employees with managers
  SELECT
    e.id,
    e.name,
    e.manager_id,
    oc.level + 1,
    oc.path || e.name
  FROM employees e
  INNER JOIN org_chart oc ON e.manager_id = oc.id
  WHERE oc.level < 10  -- Prevent infinite recursion
)
SELECT
  id,
  name,
  level,
  path,
  ARRAY_TO_STRING(path, ' > ') AS path_string
FROM org_chart
ORDER BY path;

-- Find all subordinates
WITH RECURSIVE subordinates AS (
  SELECT id, name, manager_id, 1 AS level
  FROM employees
  WHERE id = 1  -- Start with specific manager

  UNION ALL

  SELECT e.id, e.name, e.manager_id, s.level + 1
  FROM employees e
  INNER JOIN subordinates s ON e.manager_id = s.id
)
SELECT * FROM subordinates ORDER BY level, name;

-- Generate date series
WITH RECURSIVE dates AS (
  SELECT DATE '2024-01-01' AS date
  UNION ALL
  SELECT date + 1
  FROM dates
  WHERE date < DATE '2024-12-31'
)
SELECT date FROM dates;

-- Graph traversal
WITH RECURSIVE graph AS (
  SELECT
    source_node,
    target_node,
    ARRAY[source_node, target_node] AS path,
    1 AS depth
  FROM edges
  WHERE source_node = 'A'

  UNION ALL

  SELECT
    g.source_node,
    e.target_node,
    g.path || e.target_node,
    g.depth + 1
  FROM graph g
  INNER JOIN edges e ON g.target_node = e.source_node
  WHERE e.target_node != ALL(g.path)  -- Avoid cycles
    AND g.depth < 5  -- Limit depth
)
SELECT * FROM graph;
```

---

## Window Functions

### Ranking Functions

```sql
-- ROW_NUMBER: Unique sequential numbers
SELECT
  username,
  total_spent,
  ROW_NUMBER() OVER (ORDER BY total_spent DESC) AS rank
FROM user_stats;

-- RANK: Same rank for ties, gaps in sequence
SELECT
  username,
  total_spent,
  RANK() OVER (ORDER BY total_spent DESC) AS rank
FROM user_stats;

-- DENSE_RANK: Same rank for ties, no gaps
SELECT
  username,
  total_spent,
  DENSE_RANK() OVER (ORDER BY total_spent DESC) AS dense_rank
FROM user_stats;

-- NTILE: Divide into N groups
SELECT
  username,
  total_spent,
  NTILE(4) OVER (ORDER BY total_spent DESC) AS quartile
FROM user_stats;

-- PERCENT_RANK: Relative rank as percentage
SELECT
  username,
  total_spent,
  ROUND(PERCENT_RANK() OVER (ORDER BY total_spent DESC) * 100, 2) AS percentile
FROM user_stats;

-- CUME_DIST: Cumulative distribution
SELECT
  username,
  total_spent,
  ROUND(CUME_DIST() OVER (ORDER BY total_spent DESC) * 100, 2) AS cumulative_dist
FROM user_stats;
```

### Partition Functions

```sql
-- Partition by department
SELECT
  name,
  department,
  salary,
  ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) AS dept_rank,
  salary - LAG(salary) OVER (PARTITION BY department ORDER BY salary) AS diff_from_prev,
  salary - FIRST_VALUE(salary) OVER (PARTITION BY department ORDER BY salary DESC) AS diff_from_top
FROM employees;

-- Running totals
SELECT
  DATE(created_at) AS order_date,
  daily_total,
  SUM(daily_total) OVER (ORDER BY DATE(created_at)) AS running_total
FROM (
  SELECT DATE(created_at) AS created_at, SUM(total) AS daily_total
  FROM orders
  GROUP BY DATE(created_at)
) daily;

-- Moving average
SELECT
  DATE(created_at) AS order_date,
  daily_total,
  AVG(daily_total) OVER (
    ORDER BY DATE(created_at)
    ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
  ) AS moving_avg_7day
FROM (
  SELECT DATE(created_at) AS created_at, SUM(total) AS daily_total
  FROM orders
  GROUP BY DATE(created_at)
) daily;
```

### Navigation Functions

```sql
-- LAG: Previous row
-- LEAD: Next row
SELECT
  DATE(created_at) AS order_date,
  daily_revenue,
  LAG(daily_revenue, 1) OVER (ORDER BY DATE(created_at)) AS prev_day,
  LEAD(daily_revenue, 1) OVER (ORDER BY DATE(created_at)) AS next_day,
  daily_revenue - LAG(daily_revenue, 1) OVER (ORDER BY DATE(created_at)) AS day_over_day_change,
  ROUND(
    (daily_revenue - LAG(daily_revenue, 1) OVER (ORDER BY DATE(created_at))) * 100.0 /
    NULLIF(LAG(daily_revenue, 1) OVER (ORDER BY DATE(created_at)), 0), 2
  ) AS pct_change
FROM (
  SELECT DATE(created_at) AS created_at, SUM(total) AS daily_revenue
  FROM orders
  GROUP BY DATE(created_at)
) daily;

-- FIRST_VALUE, LAST_VALUE, NTH_VALUE
SELECT
  username,
  total_spent,
  FIRST_VALUE(username) OVER (ORDER BY total_spent DESC) AS top_customer,
  LAST_VALUE(username) OVER (
    ORDER BY total_spent DESC
    ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
  ) AS bottom_customer,
  NTH_VALUE(username, 3) OVER (ORDER BY total_spent DESC) AS third_customer
FROM user_stats;
```

### Window Frame Specifications

```sql
-- ROWS BETWEEN
SELECT
  DATE(created_at) AS order_date,
  daily_revenue,
  AVG(daily_revenue) OVER (
    ORDER BY DATE(created_at)
    ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
  ) AS moving_avg_3day,
  SUM(daily_revenue) OVER (
    ORDER BY DATE(created_at)
    ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
  ) AS rolling_7day_total
FROM (
  SELECT DATE(created_at) AS created_at, SUM(total) AS daily_revenue
  FROM orders
  GROUP BY DATE(created_at)
) daily;

-- RANGE BETWEEN
SELECT
  created_at,
  total,
  AVG(total) OVER (
    ORDER BY created_at
    RANGE BETWEEN INTERVAL '7 days' PRECEDING AND CURRENT ROW
  ) AS avg_last_7_days
FROM orders;
```

---

## JSON Queries

### JSONB Operators

```sql
-- Create table with JSONB
CREATE TABLE events (
  id SERIAL PRIMARY KEY,
  data JSONB NOT NULL,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Insert JSON
INSERT INTO events (data) VALUES
  ('{"type": "click", "user_id": 123, "page": "/home", "metadata": {"browser": "chrome"}}');

-- -> operator (returns JSONB)
SELECT data->'type' AS type FROM events;

-- ->> operator (returns text)
SELECT data->>'type' AS type FROM events;

-- #> operator (returns JSONB by path)
SELECT data#>'{metadata,browser}' AS browser FROM events;

-- #>> operator (returns text by path)
SELECT data#>>'{metadata,browser}' AS browser FROM events;

-- @> operator (contains)
SELECT * FROM events WHERE data @> '{"type": "click"}';

-- ? operator (key exists)
SELECT * FROM events WHERE data ? 'user_id';

-- ?| operator (any key exists)
SELECT * FROM events WHERE data ?| ARRAY['user_id', 'session_id'];

-- ?& operator (all keys exist)
SELECT * FROM events WHERE data ?& ARRAY['type', 'user_id'];

-- -> operator for nested objects
SELECT
  data->>'type' AS event_type,
  data->>'user_id' AS user_id,
  data->'metadata'->>'browser' AS browser
FROM events;
```

### JSON Aggregation

```sql
-- Aggregate to JSON array
SELECT
  user_id,
  JSONB_AGG(data ORDER BY created_at) AS events
FROM events
GROUP BY user_id;

-- Aggregate to JSON object
SELECT
  JSONB_OBJECT_AGG(type, count)
FROM (
  SELECT data->>'type' AS type, COUNT(*) AS count
  FROM events
  GROUP BY data->>'type'
) counts;

-- JSONB_AGG with filtering
SELECT
  user_id,
  JSONB_AGG(
    JSONB_BUILD_OBJECT(
      'event_type', data->>'type',
      'timestamp', created_at
    )
  ) AS user_events
FROM events
WHERE created_at >= NOW() - INTERVAL '24 hours'
GROUP BY user_id;

-- Pivot JSON to columns
SELECT
  user_id,
  MAX(CASE WHEN data->>'type' = 'click' THEN 1 ELSE 0 END) AS has_click,
  MAX(CASE WHEN data->>'type' = 'view' THEN 1 ELSE 0 END) AS has_view,
  MAX(CASE WHEN data->>'type' = 'purchase' THEN 1 ELSE 0 END) AS has_purchase
FROM events
GROUP BY user_id;
```

### JSON Transformation

```sql
-- Flatten JSON array
SELECT
  e.id,
  jsonb_array_elements_text(e.data->'tags') AS tag
FROM events e;

-- JSON to rows
SELECT
  e.id,
  kv.key,
  kv.value
FROM events e,
LATERAL jsonb_each(e.data) AS kv(key, value);

-- JSON to columns
SELECT
  e.id,
  (jsonb_each_text(e.data)).* AS (key, value)
FROM events e;

-- Update JSON
UPDATE events
SET data = jsonb_set(data, '{type}', '"page_view"')
WHERE id = 1;

-- Delete JSON key
UPDATE events
SET data = data - 'old_field'
WHERE id = 1;

-- Merge JSON
UPDATE events
SET data = data || '{"new_field": "value"}'
WHERE id = 1;
```

---

## Array Queries

### Array Operations

```sql
-- Create table with arrays
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  tags TEXT[],
  prices NUMERIC(10,2)[]
);

-- Insert arrays
INSERT INTO products (name, tags, prices)
VALUES ('Laptop', ARRAY['electronics', 'computers'], ARRAY[999.99, 899.99]);

-- Array contains element
SELECT * FROM products WHERE 'electronics' = ANY(tags);

-- Array contains all elements
SELECT * FROM products WHERE tags @> ARRAY['electronics', 'computers'];

-- Array overlap
SELECT * FROM products WHERE tags && ARRAY['electronics', 'clothing'];

-- Array length
SELECT * FROM products WHERE array_length(tags, 1) > 1;

-- Array append
SELECT name, array_append(tags, 'sale') AS with_sale FROM products;

-- Array remove
SELECT name, array_remove(tags, 'computers') AS without_computers FROM products;

-- Array cat
SELECT name, array_cat(prices, ARRAY[799.99]) AS all_prices FROM products;

-- Array position
SELECT name, array_position(tags, 'electronics') AS pos FROM products;

-- Array to string
SELECT name, array_to_string(tags, ', ') AS tags_string FROM products;

-- String to array
SELECT string_to_array('a,b,c', ',') AS arr;

-- Unnest array to rows
SELECT name, unnest(tags) AS tag FROM products;

-- Aggregate to array
SELECT
  category,
  ARRAY_AGG(DISTINCT tag ORDER BY tag) AS all_tags
FROM products,
     unnest(tags) AS tag
GROUP BY category;
```

---

## Full Text Search

### Basic FTS

```sql
-- Create table with FTS
CREATE TABLE articles (
  id SERIAL PRIMARY KEY,
  title VARCHAR(200),
  content TEXT,
  search_vector TSVECTOR
);

-- Create GIN index for FTS
CREATE INDEX idx_articles_search ON articles USING GIN (search_vector);

-- Update search vector
UPDATE articles
SET search_vector =
  setweight(to_tsvector('english', coalesce(title, '')), 'A') ||
  setweight(to_tsvector('english', coalesce(content, '')), 'B');

-- Basic search
SELECT * FROM articles
WHERE search_vector @@ to_tsquery('english', 'database & optimization');

-- Rank results
SELECT
  title,
  ts_rank(search_vector, query) AS rank
FROM articles, to_tsquery('english', 'database & optimization') AS query
WHERE search_vector @@ query
ORDER BY rank DESC;

-- Weighted ranking
SELECT
  title,
  ts_rank_cd(search_vector, query) AS rank
FROM articles, to_tsquery('english', 'database & optimization') AS query
WHERE search_vector @@ query
ORDER BY rank DESC;

-- Phrase search
SELECT * FROM articles
WHERE search_vector @@ to_tsquery('english', 'database <-> optimization');

-- Prefix search
SELECT * FROM articles
WHERE search_vector @@ to_tsquery('english', 'optim:*');

-- Headline (highlight matches)
SELECT
  ts_headline('english', content, to_tsquery('english', 'database & optimization'),
    'StartSel=<b>, StopSel=</b>, MaxWords=50') AS highlighted
FROM articles
WHERE search_vector @@ to_tsquery('english', 'database & optimization');
```

### Trigram Similarity

```sql
-- Enable pg_trgm extension
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Create GIN index for trigram similarity
CREATE INDEX idx_users_username_trgm ON users USING GIN (username gin_trgm_ops);

-- Similarity search
SELECT
  username,
  similarity(username, 'johndoe') AS sim
FROM users
WHERE similarity(username, 'johndoe') > 0.3
ORDER BY sim DESC;

-- Trigram similarity operator
SELECT * FROM users WHERE username % 'johndoe';

-- Word similarity
SELECT * FROM users WHERE username <<% 'johndoe';

-- Like with trigrams (faster)
SELECT * FROM users WHERE username ILIKE '%john%';
```

---

## Lateral Joins

### Basic LATERAL

```sql
-- Get top 3 orders per user
SELECT
  u.username,
  top_orders.order_id,
  top_orders.total
FROM users u
CROSS JOIN LATERAL (
  SELECT id AS order_id, total
  FROM orders
  WHERE user_id = u.id
  ORDER BY created_at DESC
  LIMIT 3
) top_orders;

-- Get most recent activity per user
SELECT
  u.username,
  recent.activity_type,
  recent.activity_time
FROM users u
CROSS JOIN LATERAL (
  SELECT activity_type, created_at AS activity_time
  FROM user_activities
  WHERE user_id = u.id
  ORDER BY created_at DESC
  LIMIT 1
) recent;

-- LATERAL with LEFT JOIN
SELECT
  u.username,
  COALESCE(latest.total, 0) AS last_order_total
FROM users u
LEFT JOIN LATERAL (
  SELECT total
  FROM orders
  WHERE user_id = u.id
  ORDER BY created_at DESC
  LIMIT 1
) latest ON TRUE;
```

### LATERAL with Functions

```sql
-- Using generate_series with LATERAL
SELECT
  u.username,
  date_series.day
FROM users u
CROSS JOIN LATERAL (
  SELECT generate_series(
    CURRENT_DATE - INTERVAL '7 days',
    CURRENT_DATE,
    '1 day'::interval
  )::date AS day
) date_series;

-- Using unnest with LATERAL
SELECT
  u.username,
  hobby.value AS hobby
FROM users u
CROSS JOIN LATERAL unnest(u.hobbies) AS hobby(value);
```

---

## Query Optimization

### EXPLAIN ANALYZE

```sql
-- Basic EXPLAIN ANALYZE
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';

-- With BUFFERS option
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT u.username, COUNT(o.id) AS order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;

-- With timing information
EXPLAIN (ANALYZE, TIMING, VERBOSE)
SELECT * FROM orders WHERE user_id = 1 AND created_at > '2024-01-01';

-- Read EXPLAIN output
-- Seq Scan on users  (cost=0.00..35.50 rows=1 width=84) (actual time=0.008..0.009 rows=1 loops=1)
--   Filter: (email = 'test@example.com'::text)
--   Rows Removed by Filter: 999
-- Planning Time: 0.150 ms
-- Execution Time: 0.180 ms
```

### Query Rewriting

```sql
-- BAD: Using OR with different columns
SELECT * FROM users WHERE email = 'test@example.com' OR username = 'test';

-- GOOD: Use UNION
SELECT * FROM users WHERE email = 'test@example.com'
UNION
SELECT * FROM users WHERE username = 'test';

-- BAD: Using functions on indexed columns
SELECT * FROM users WHERE EXTRACT(YEAR FROM created_at) = 2024;

-- GOOD: Range query
SELECT * FROM users
WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';

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

### Index Usage

```sql
-- Check index usage
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- Find unused indexes
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0
  AND indexname NOT LIKE '%pkey%'
ORDER BY pg_relation_size(indexrelid) DESC;

-- Check index size
SELECT
  indexname,
  pg_size_pretty(pg_relation_size(indexname::regclass)) AS index_size
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY pg_relation_size(indexname::regclass) DESC;
```

---

## Advanced Techniques

### Temporal Queries

```sql
-- Find overlapping intervals
SELECT
  a.id AS booking_a,
  b.id AS booking_b,
  a.start_date,
  a.end_date
FROM bookings a
INNER JOIN bookings b
  ON a.room_id = b.room_id
  AND a.id < b.id
  AND a.start_date < b.end_date
  AND b.start_date < a.end_date;

-- Calculate gaps between events
WITH ordered_events AS (
  SELECT
    id,
    end_time,
    LEAD(start_time) OVER (PARTITION BY room_id ORDER BY start_time) AS next_start
  FROM bookings
)
SELECT
  id,
  end_time AS gap_start,
  next_start AS gap_end,
  next_start - end_time AS gap_duration
FROM ordered_events
WHERE next_start - end_time > INTERVAL '0';
```

### Statistical Queries

```sql
-- Cumulative distribution
WITH ranked AS (
  SELECT
    id,
    salary,
    ROW_NUMBER() OVER (ORDER BY salary) AS row_num,
    COUNT(*) OVER () AS total_count
  FROM employees
)
SELECT
  id,
  salary,
  row_num,
  ROUND(row_num * 100.0 / total_count, 2) AS percentile
FROM ranked;

-- Moving average
WITH daily_sales AS (
  SELECT
    DATE(created_at) AS sale_date,
    SUM(total) AS daily_total
  FROM orders
  GROUP BY DATE(created_at)
)
SELECT
  sale_date,
  daily_total,
  AVG(daily_total) OVER (
    ORDER BY sale_date
    ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
  ) AS moving_avg_7day
FROM daily_sales;
```

---

## Summary

| Feature | Use Case |
|---------|----------|
| CTEs | Readable complex queries |
| Window Functions | Analytical calculations |
| JSONB | Semi-structured data |
| Arrays | Multi-value columns |
| Full Text Search | Text search |
| LATERAL | Row-dependent subqueries |
| Recursive CTEs | Hierarchical data |

## Next Steps

- [PostgreSQL Optimization](../optimization/) - Performance tuning
- [PostgreSQL Extensions](../extensions/) - Extension ecosystem
- [PostgreSQL Replication](../replication/) - High availability
