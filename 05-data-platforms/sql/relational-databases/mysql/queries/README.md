# MySQL Queries

## Table of Contents

1. [Complex Queries](#complex-queries)
2. [JOIN Operations](#join-operations)
3. [Subqueries](#subqueries)
4. [Window Functions](#window-functions)
5. [Common Table Expressions](#common-table-expressions)
6. [Aggregation](#aggregation)
7. [Set Operations](#set-operations)
8. [Pivot Queries](#pivot-queries)
9. [Query Optimization](#query-optimization)
10. [Advanced Techniques](#advanced-techniques)

---

## Complex Queries

### Multi-Table Queries

```sql
-- Complex query with multiple conditions
SELECT
  u.id,
  u.username,
  u.email,
  o.id AS order_id,
  o.total,
  o.status,
  p.name AS product_name,
  oi.quantity,
  oi.price
FROM users u
INNER JOIN orders o ON u.id = o.user_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id
WHERE u.is_active = TRUE
  AND o.status IN ('pending', 'processing')
  AND o.created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
  AND oi.quantity > 0
ORDER BY o.created_at DESC, u.username
LIMIT 100;
```

### Query with Calculated Fields

```sql
SELECT
  u.id,
  u.username,
  CONCAT(u.first_name, ' ', u.last_name) AS full_name,
  u.email,
  COUNT(DISTINCT o.id) AS total_orders,
  COALESCE(SUM(o.total), 0) AS lifetime_value,
  COALESCE(AVG(o.total), 0) AS avg_order_value,
  MIN(o.created_at) AS first_order_date,
  MAX(o.created_at) AS last_order_date,
  DATEDIFF(NOW(), MAX(o.created_at)) AS days_since_last_order,
  CASE
    WHEN MAX(o.created_at) >= DATE_SUB(NOW(), INTERVAL 30 DAY) THEN 'Active'
    WHEN MAX(o.created_at) >= DATE_SUB(NOW(), INTERVAL 90 DAY) THEN 'At Risk'
    ELSE 'Churned'
  END AS customer_status,
  DENSE_RANK() OVER (ORDER BY COALESCE(SUM(o.total), 0) DESC) AS value_rank
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username, u.first_name, u.last_name, u.email
HAVING COUNT(DISTINCT o.id) > 0
ORDER BY lifetime_value DESC;
```

### Conditional Aggregation

```sql
SELECT
  DATE_FORMAT(o.created_at, '%Y-%m') AS month,
  COUNT(*) AS total_orders,
  COUNT(CASE WHEN o.status = 'completed' THEN 1 END) AS completed_orders,
  COUNT(CASE WHEN o.status = 'cancelled' THEN 1 END) AS cancelled_orders,
  ROUND(
    COUNT(CASE WHEN o.status = 'cancelled' THEN 1 END) * 100.0 / COUNT(*), 2
  ) AS cancellation_rate,
  SUM(CASE WHEN o.status = 'completed' THEN o.total ELSE 0 END) AS revenue,
  COUNT(DISTINCT o.user_id) AS unique_customers,
  ROUND(
    SUM(CASE WHEN o.status = 'completed' THEN o.total ELSE 0 END) /
    COUNT(DISTINCT o.user_id), 2
  ) AS revenue_per_customer
FROM orders o
WHERE o.created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')
ORDER BY month;
```

---

## JOIN Operations

### INNER JOIN

```sql
-- Basic INNER JOIN
SELECT u.username, o.total
FROM users u
INNER JOIN orders o ON u.id = o.user_id;

-- Multi-table INNER JOIN
SELECT
  u.username,
  o.id AS order_id,
  p.name AS product,
  oi.quantity
FROM users u
INNER JOIN orders o ON u.id = o.user_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id;
```

### LEFT JOIN

```sql
-- All users with their orders (including users without orders)
SELECT
  u.id,
  u.username,
  COUNT(o.id) AS order_count,
  COALESCE(SUM(o.total), 0) AS total_spent
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username
ORDER BY total_spent DESC;

-- Find users without orders
SELECT u.id, u.username
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE o.id IS NULL;
```

### RIGHT JOIN

```sql
-- All orders with user info (including orphaned orders)
SELECT
  o.id AS order_id,
  o.total,
  u.username
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;

-- Equivalent to LEFT JOIN with tables swapped
SELECT
  o.id AS order_id,
  o.total,
  u.username
FROM orders o
LEFT JOIN users u ON o.user_id = u.id;
```

### CROSS JOIN

```sql
-- Cartesian product
SELECT
  p.name AS product,
  c.name AS category
FROM products p
CROSS JOIN categories c;

-- Generate date series
SELECT
  DATE_ADD('2024-01-01', INTERVAL n DAY) AS date
FROM (
  SELECT @rownum := @rownum + 1 AS n
  FROM information_schema.COLUMNS,
       (SELECT @rownum := 0) r
  LIMIT 366
) numbers
WHERE DATE_ADD('2024-01-01', INTERVAL n DAY) <= '2024-12-31';
```

### SELF JOIN

```sql
-- Find employees and their managers
SELECT
  e.name AS employee,
  m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;

-- Find employees in the same department
SELECT
  e1.name AS employee1,
  e2.name AS employee2,
  e1.department
FROM employees e1
INNER JOIN employees e2
  ON e1.department = e2.department
  AND e1.id < e2.id;
```

### Multi-Join with Aggregation

```sql
SELECT
  c.name AS category,
  p.brand,
  COUNT(DISTINCT o.id) AS total_orders,
  COUNT(DISTINCT oi.product_id) AS unique_products,
  SUM(oi.quantity) AS units_sold,
  SUM(oi.quantity * oi.price) AS revenue,
  AVG(oi.price) AS avg_price
FROM categories c
INNER JOIN products p ON c.id = p.category_id
INNER JOIN order_items oi ON p.id = oi.product_id
INNER JOIN orders o ON oi.order_id = o.id
WHERE o.status = 'completed'
  AND o.created_at >= DATE_SUB(NOW(), INTERVAL 90 DAY)
GROUP BY c.name, p.brand
HAVING COUNT(DISTINCT o.id) >= 10
ORDER BY revenue DESC;
```

---

## Subqueries

### Scalar Subqueries

```sql
-- Single value return
SELECT
  username,
  email,
  (SELECT COUNT(*) FROM orders WHERE user_id = users.id) AS order_count
FROM users;

-- In WHERE clause
SELECT * FROM products
WHERE price > (SELECT AVG(price) FROM products);

-- In HAVING clause
SELECT user_id, COUNT(*) AS order_count
FROM orders
GROUP BY user_id
HAVING COUNT(*) > (SELECT AVG(order_count)
                   FROM (SELECT COUNT(*) AS order_count
                         FROM orders GROUP BY user_id) avg_orders);
```

### IN Subqueries

```sql
-- Users who have placed orders
SELECT * FROM users
WHERE id IN (SELECT DISTINCT user_id FROM orders);

-- Users who have NOT placed orders
SELECT * FROM users
WHERE id NOT IN (SELECT DISTINCT user_id FROM orders);

-- Products in active orders
SELECT * FROM products
WHERE id IN (
  SELECT oi.product_id
  FROM order_items oi
  INNER JOIN orders o ON oi.order_id = o.id
  WHERE o.status = 'active'
);
```

### EXISTS Subqueries

```sql
-- EXISTS (often faster than IN for large datasets)
SELECT * FROM users u
WHERE EXISTS (
  SELECT 1 FROM orders o
  WHERE o.user_id = u.id
  AND o.created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
);

-- NOT EXISTS
SELECT * FROM users u
WHERE NOT EXISTS (
  SELECT 1 FROM orders o WHERE o.user_id = u.id
);

-- Correlated EXISTS
SELECT p.name, p.price
FROM products p
WHERE EXISTS (
  SELECT 1 FROM order_items oi
  WHERE oi.product_id = p.id
  AND oi.price > p.price * 0.8  -- Discounted price
);
```

### Derived Tables (Subqueries in FROM)

```sql
SELECT
  d.department,
  d.total_salary,
  d.avg_salary,
  e.name AS highest_paid
FROM (
  SELECT
    department,
    SUM(salary) AS total_salary,
    AVG(salary) AS avg_salary,
    MAX(salary) AS max_salary
  FROM employees
  GROUP BY department
) d
INNER JOIN employees e
  ON e.department = d.department
  AND e.salary = d.max_salary;
```

### Lateral Derived Tables (MySQL 8.0.14+)

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
```

---

## Window Functions

### Ranking Functions

```sql
-- ROW_NUMBER: Unique sequential numbers
SELECT
  username,
  total_spent,
  ROW_NUMBER() OVER (ORDER BY total_spent DESC) AS row_num
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
```

### Aggregate Window Functions

```sql
SELECT
  username,
  department,
  salary,
  AVG(salary) OVER (PARTITION BY department) AS dept_avg,
  SUM(salary) OVER (PARTITION BY department) AS dept_total,
  COUNT(*) OVER (PARTITION BY department) AS dept_count,
  salary - AVG(salary) OVER (PARTITION BY department) AS diff_from_avg,
  ROUND(salary * 100.0 / SUM(salary) OVER (PARTITION BY department), 2) AS pct_of_dept
FROM employees;
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
    LAG(daily_revenue, 1) OVER (ORDER BY DATE(created_at)), 2
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
    RANGE BETWEEN INTERVAL 7 DAY PRECEDING AND CURRENT ROW
  ) AS avg_last_7_days
FROM orders;
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

-- Multiple CTEs
WITH
monthly_revenue AS (
  SELECT
    DATE_FORMAT(created_at, '%Y-%m') AS month,
    SUM(total) AS revenue
  FROM orders
  WHERE status = 'completed'
  GROUP BY DATE_FORMAT(created_at, '%Y-%m')
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
  rs.avg_revenue,
  (mr.revenue - rs.avg_revenue) / rs.stddev_revenue AS z_score
FROM monthly_revenue mr
CROSS JOIN revenue_stats rs;
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
    CAST(name AS CHAR(1000)) AS path
  FROM employees
  WHERE manager_id IS NULL

  UNION ALL

  -- Recursive case: employees with managers
  SELECT
    e.id,
    e.name,
    e.manager_id,
    oc.level + 1,
    CONCAT(oc.path, ' > ', e.name)
  FROM employees e
  INNER JOIN org_chart oc ON e.manager_id = oc.id
)
SELECT
  id,
  name,
  level,
  path
FROM org_chart
ORDER BY path;

-- Find all subordinates
WITH RECURSIVE subordinates AS (
  SELECT id, name, manager_id
  FROM employees
  WHERE id = 1  -- Start with specific manager

  UNION ALL

  SELECT e.id, e.name, e.manager_id
  FROM employees e
  INNER JOIN subordinates s ON e.manager_id = s.id
)
SELECT * FROM subordinates;

-- Generate date series
WITH RECURSIVE dates AS (
  SELECT '2024-01-01' AS date
  UNION ALL
  SELECT DATE_ADD(date, INTERVAL 1 DAY)
  FROM dates
  WHERE date < '2024-12-31'
)
SELECT date FROM dates;
```

---

## Aggregation

### GROUP BY Variations

```sql
-- Basic aggregation
SELECT
  department,
  COUNT(*) AS emp_count,
  AVG(salary) AS avg_salary,
  MIN(salary) AS min_salary,
  MAX(salary) AS max_salary
FROM employees
GROUP BY department;

-- GROUP BY with ROLLUP
SELECT
  COALESCE(department, 'TOTAL') AS department,
  COUNT(*) AS emp_count,
  SUM(salary) AS total_salary
FROM employees
GROUP BY department WITH ROLLUP;

-- GROUP BY with CUBE
SELECT
  COALESCE(department, 'ALL') AS department,
  COALESCE(location, 'ALL') AS location,
  COUNT(*) AS emp_count
FROM employees
GROUP BY department, location WITH CUBE;

-- GROUP BY with GROUPING SETS
SELECT
  department,
  location,
  COUNT(*) AS emp_count
FROM employees
GROUP BY GROUPING SETS (
  (department),
  (location),
  ()
);
```

### HAVING Clause

```sql
-- Filter after aggregation
SELECT
  user_id,
  COUNT(*) AS order_count,
  SUM(total) AS total_spent
FROM orders
GROUP BY user_id
HAVING COUNT(*) >= 5
  AND SUM(total) > 500
ORDER BY total_spent DESC;

-- HAVING with subquery
SELECT
  department,
  AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > (
  SELECT AVG(salary) FROM employees
);
```

### GROUP_CONCAT

```sql
-- Concatenate group values
SELECT
  department,
  GROUP_CONCAT(name ORDER BY name SEPARATOR ', ') AS employees,
  COUNT(*) AS emp_count
FROM employees
GROUP BY department;

-- With DISTINCT
SELECT
  user_id,
  GROUP_CONCAT(DISTINCT product_category ORDER BY product_category) AS categories_purchased
FROM orders o
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id
GROUP BY user_id;

-- With length limit
SELECT
  department,
  GROUP_CONCAT(name SEPARATOR ', ') AS employees
FROM employees
GROUP BY department
HAVING LENGTH(GROUP_CONCAT(name SEPARATOR ', ')) < 1000;
```

---

## Set Operations

```sql
-- UNION: Combine results, remove duplicates
SELECT username, email FROM active_users
UNION
SELECT username, email FROM premium_users;

-- UNION ALL: Keep duplicates
SELECT user_id FROM orders_2023
UNION ALL
SELECT user_id FROM orders_2024;

-- INTERSECT (MySQL 8.0.31+)
SELECT user_id FROM orders_jan
INTERSECT
SELECT user_id FROM orders_feb;

-- EXCEPT (MySQL 8.0.31+)
SELECT user_id FROM all_users
EXCEPT
SELECT user_id FROM banned_users;
```

---

## Pivot Queries

```sql
-- Manual pivot using CASE
SELECT
  DATE_FORMAT(order_date, '%Y-%m') AS month,
  SUM(CASE WHEN status = 'completed' THEN total ELSE 0 END) AS completed,
  SUM(CASE WHEN status = 'pending' THEN total ELSE 0 END) AS pending,
  SUM(CASE WHEN status = 'cancelled' THEN total ELSE 0 END) AS cancelled
FROM orders
GROUP BY DATE_FORMAT(order_date, '%Y-%m')
ORDER BY month;

-- Dynamic pivot
SET @sql = NULL;
SELECT
  GROUP_CONCAT(DISTINCT
    CONCAT(
      'SUM(CASE WHEN department = ''',
      department,
      ''' THEN salary ELSE 0 END) AS `',
      department, '`'
    )
  ) INTO @sql
FROM employees;

SET @sql = CONCAT('SELECT name, ', @sql, '
                    FROM employees
                    GROUP BY name');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
```

---

## Query Optimization

### EXPLAIN Analysis

```sql
-- Basic EXPLAIN
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';

-- EXPLAIN with JSON format
EXPLAIN FORMAT=JSON SELECT * FROM users WHERE email = 'test@example.com';

-- EXPLAIN ANALYZE (MySQL 8.0.18+)
EXPLAIN ANALYZE
SELECT u.username, COUNT(o.id) AS order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;

-- EXPLAIN output columns
-- id: Query select identifier
-- select_type: SELECT type (SIMPLE, PRIMARY, SUBQUERY, etc.)
-- table: Table name
-- type: Access type (const, eq_ref, ref, range, index, ALL)
-- possible_keys: Possible indexes
-- key: Actually used index
-- key_len: Length of used index
-- rows: Estimated rows to scan
-- Extra: Additional information
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
SELECT * FROM users WHERE YEAR(created_at) = 2024;

-- GOOD: Range query
SELECT * FROM users
WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';

-- BAD: Using LIKE with leading wildcard
SELECT * FROM users WHERE username LIKE '%test%';

-- GOOD: Use full-text index
SELECT * FROM users WHERE MATCH(username) AGAINST('test' IN BOOLEAN MODE);

-- BAD: NOT IN with subquery
SELECT * FROM users WHERE id NOT IN (SELECT user_id FROM banned_users);

-- GOOD: NOT EXISTS
SELECT * FROM users u
WHERE NOT EXISTS (SELECT 1 FROM banned_users b WHERE b.user_id = u.id);
```

### Index Usage Hints

```sql
-- Force index
SELECT * FROM users FORCE INDEX (idx_email) WHERE email = 'test@example.com';

-- Ignore index
SELECT * FROM users IGNORE INDEX (idx_email) WHERE email = 'test@example.com';

-- Use index
SELECT * FROM users USE INDEX (idx_email) WHERE email = 'test@example.com';
```

---

## Advanced Techniques

### PIVOT with Dynamic SQL

```sql
-- Create stored procedure for dynamic pivot
DELIMITER //
CREATE PROCEDURE PivotOrdersByMonth()
BEGIN
  DECLARE sql_text TEXT;

  SELECT GROUP_CONCAT(DISTINCT
    CONCAT(
      'SUM(CASE WHEN DATE_FORMAT(order_date, ''%Y-%m'') = ''',
      DATE_FORMAT(order_date, '%Y-%m'),
      ''' THEN total ELSE 0 END) AS `',
      DATE_FORMAT(order_date, '%Y-%m'), '`'
    )
  ) INTO sql_text
  FROM orders
  WHERE order_date >= DATE_SUB(NOW(), INTERVAL 12 MONTH);

  SET @sql = CONCAT(
    'SELECT DATE_FORMAT(order_date, ''%Y-%m'') AS month, ',
    sql_text,
    ' FROM orders
     WHERE order_date >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
     GROUP BY DATE_FORMAT(order_date, ''%Y-%m'')
     ORDER BY month'
  );

  PREPARE stmt FROM @sql;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
END //
DELIMITER ;
```

### JSON Aggregation

```sql
-- Aggregate to JSON
SELECT
  u.id,
  u.username,
  JSON_ARRAYAGG(
    JSON_OBJECT(
      'order_id', o.id,
      'total', o.total,
      'date', o.created_at
    )
  ) AS orders
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username;

-- Parse JSON aggregation
SELECT
  JSON_EXTRACT(orders_json, '$[0].order_id') AS first_order_id,
  JSON_LENGTH(orders_json) AS order_count
FROM (
  SELECT
    user_id,
    JSON_ARRAYAGG(id) AS orders_json
  FROM orders
  GROUP BY user_id
) user_orders;
```

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
    start_time,
    end_time,
    LEAD(start_time) OVER (PARTITION BY room_id ORDER BY start_time) AS next_start
  FROM bookings
)
SELECT
  id,
  end_time AS gap_start,
  next_start AS gap_end,
  TIMESTAMPDIFF(HOUR, end_time, next_start) AS gap_hours
FROM ordered_events
WHERE TIMESTAMPDIFF(HOUR, end_time, next_start) > 0;
```

### Running Statistics

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
    DATE(order_date) AS sale_date,
    SUM(total) AS daily_total
  FROM orders
  GROUP BY DATE(order_date)
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

| Technique | Use Case |
|-----------|----------|
| JOINs | Combining related data |
| Subqueries | Complex filtering logic |
| Window Functions | Analytical calculations |
| CTEs | Readable complex queries |
| Aggregation | Summarizing data |
| Set Operations | Combining result sets |
| JSON Functions | Semi-structured data |
| Temporal Queries | Time-based analysis |

## Next Steps

- [MySQL Optimization](../optimization/) - Performance tuning
- [MySQL Replication](../replication/) - High availability
- [PostgreSQL Queries](../../postgresql/queries/) - Advanced PostgreSQL queries
