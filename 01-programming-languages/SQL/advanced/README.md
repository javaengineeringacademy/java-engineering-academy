# Advanced SQL

Window functions, CTEs, PIVOT/UNPIVOT, partitioning, and performance analysis.

## Table of Contents

- [Window Functions](#window-functions)
- [Common Table Expressions](#common-table-expressions)
- [Recursive CTEs](#recursive-ctes)
- [PIVOT and UNPIVOT](#pivot-and-unpivot)
- [EXPLAIN and ANALYZE](#explain-and-analyze)
- [Query Hints](#query-hints)
- [Partitioning](#partitioning)
- [Materialized Views](#materialized-views)
- [Advanced Aggregation](#advanced-aggregation)
- [Advanced Joins](#advanced-joins)

---

## Window Functions

Window functions perform calculations across a set of rows related to the current row.

### Syntax

```sql
function_name(args) OVER (
    [PARTITION BY partition_expression]
    [ORDER BY sort_expression [ASC|DESC]]
    [frame_clause]
)
```

### Frame Clauses

```sql
ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING
RANGE BETWEEN INTERVAL '1' DAY PRECEDING AND CURRENT ROW
GROUPS BETWEEN 2 PRECEDING AND 2 FOLLOWING
```

### ROW_NUMBER

Assigns unique sequential numbers to rows.

```sql
-- Unique row numbers
SELECT
    first_name,
    last_name,
    salary,
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
FROM employees;

-- Row numbers per department
SELECT
    first_name,
    department_id,
    salary,
    ROW_NUMBER() OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
    ) AS dept_rank
FROM employees;
```

### RANK

Assigns ranks with gaps for ties.

```sql
SELECT
    first_name,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank
FROM employees;

-- Example: 1, 2, 2, 4 (gap after tie)
```

### DENSE_RANK

Assigns ranks without gaps for ties.

```sql
SELECT
    first_name,
    salary,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank
FROM employees;

-- Example: 1, 2, 2, 3 (no gap after tie)
```

### NTILE

Divides rows into N roughly equal groups.

```sql
-- Divide employees into 4 quartiles
SELECT
    first_name,
    salary,
    NTILE(4) OVER (ORDER BY salary DESC) AS quartile
FROM employees;

-- Deciles
SELECT
    first_name,
    salary,
    NTILE(10) OVER (ORDER BY salary) AS decile
FROM employees;
```

### LAG

Accesses data from previous rows.

```sql
-- Previous order amount
SELECT
    order_date,
    amount,
    LAG(amount, 1) OVER (ORDER BY order_date) AS prev_amount,
    amount - LAG(amount, 1) OVER (ORDER BY order_date) AS change
FROM orders;

-- Previous row with default
SELECT
    order_date,
    amount,
    LAG(amount, 1, 0) OVER (ORDER BY order_date) AS prev_amount
FROM orders;

-- Lag by 2 rows
SELECT
    order_date,
    amount,
    LAG(amount, 2) OVER (ORDER BY order_date) AS two_days_ago
FROM orders;
```

### LEAD

Accesses data from subsequent rows.

```sql
-- Next order amount
SELECT
    order_date,
    amount,
    LEAD(amount, 1) OVER (ORDER BY order_date) AS next_amount,
    LEAD(amount, 1) OVER (ORDER BY order_date) - amount AS future_change
FROM orders;

-- With default
SELECT
    order_date,
    amount,
    LEAD(amount, 1, 0) OVER (ORDER BY order_date) AS next_amount
FROM orders;
```

### FIRST_VALUE / LAST_VALUE / NTH_VALUE

```sql
-- First value in partition
SELECT
    first_name,
    department_id,
    salary,
    FIRST_VALUE(salary) OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
    ) AS highest_salary
FROM employees;

-- Last value (careful with frame clause)
SELECT
    first_name,
    department_id,
    salary,
    LAST_VALUE(salary) OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS lowest_salary
FROM employees;

-- Nth value
SELECT
    first_name,
    department_id,
    salary,
    NTH_VALUE(salary, 2) OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS second_highest
FROM employees;
```

### Aggregate Window Functions

```sql
-- Running total
SELECT
    order_date,
    amount,
    SUM(amount) OVER (ORDER BY order_date) AS running_total
FROM orders;

-- Moving average (3-day window)
SELECT
    order_date,
    amount,
    AVG(amount) OVER (
        ORDER BY order_date
        ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING
    ) AS moving_avg_3
FROM orders;

-- Cumulative count
SELECT
    order_date,
    COUNT(*) OVER (ORDER BY order_date) AS cumulative_count
FROM orders;

-- Percentage of total
SELECT
    first_name,
    salary,
    ROUND(salary * 100.0 / SUM(salary) OVER (), 2) AS pct_of_total
FROM employees;

-- Percentage within partition
SELECT
    first_name,
    department_id,
    salary,
    ROUND(
        salary * 100.0 / SUM(salary) OVER (PARTITION BY department_id),
        2
    ) AS pct_of_dept
FROM employees;
```

### Percentage Change

```sql
-- Month-over-month growth
WITH monthly_sales AS (
    SELECT
        DATE_TRUNC('month', order_date) AS month,
        SUM(amount) AS total_sales
    FROM orders
    GROUP BY DATE_TRUNC('month', order_date)
)
SELECT
    month,
    total_sales,
    LAG(total_sales) OVER (ORDER BY month) AS prev_month,
    ROUND(
        (total_sales - LAG(total_sales) OVER (ORDER BY month)) * 100.0
        / NULLIF(LAG(total_sales) OVER (ORDER BY month), 0),
        2
    ) AS growth_pct
FROM monthly_sales;
```

### Gap Detection

```sql
-- Detect gaps in sequence
WITH numbered AS (
    SELECT
        id,
        id - ROW_NUMBER() OVER (ORDER BY id) AS gap_group
    FROM missing_ids
)
SELECT MIN(id) AS gap_start, MAX(id) + 1 AS gap_end
FROM numbered
GROUP BY gap_group
HAVING COUNT(*) > 1 OR MIN(id) != MAX(id);
```

---

## Common Table Expressions

### Basic CTE

```sql
WITH dept_stats AS (
    SELECT
        department_id,
        COUNT(*) AS emp_count,
        AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
)
SELECT d.name, ds.*
FROM departments d
JOIN dept_stats ds ON d.id = ds.department_id;
```

### Multiple CTEs

```sql
WITH
high_earners AS (
    SELECT * FROM employees WHERE salary > 80000
),
dept_totals AS (
    SELECT
        department_id,
        COUNT(*) AS total_employees,
        SUM(salary) AS total_salary
    FROM employees
    GROUP BY department_id
)
SELECT he.first_name, he.salary, dt.*
FROM high_earners he
JOIN dept_totals dt ON he.department_id = dt.department_id;
```

### CTE with Aggregation

```sql
WITH customer_orders AS (
    SELECT
        customer_id,
        COUNT(*) AS order_count,
        SUM(amount) AS total_spent,
        AVG(amount) AS avg_order_value
    FROM orders
    GROUP BY customer_id
),
customer_stats AS (
    SELECT
        *,
        NTILE(4) OVER (ORDER BY total_spent) AS spending_quartile,
        CASE
            WHEN total_spent > 10000 THEN 'VIP'
            WHEN total_spent > 5000 THEN 'Premium'
            ELSE 'Regular'
        END AS customer_tier
    FROM customer_orders
)
SELECT c.name, cs.*
FROM customers c
JOIN customer_stats cs ON c.id = cs.customer_id;
```

### CTE for Data Modification

```sql
-- DELETE with CTE
WITH inactive AS (
    SELECT id FROM users
    WHERE last_login < CURRENT_DATE - INTERVAL '1 year'
)
DELETE FROM user_sessions
WHERE user_id IN (SELECT id FROM inactive);

-- UPDATE with CTE
WITH price_updates AS (
    SELECT
        p.id,
        p.price * 1.05 AS new_price
    FROM products p
    JOIN categories c ON p.category_id = c.id
    WHERE c.name = 'Electronics'
)
UPDATE products
SET price = pu.new_price
FROM price_updates pu
WHERE products.id = pu.id;
```

---

## Recursive CTEs

### Syntax

```sql
WITH RECURSIVE cte_name AS (
    -- Base case (non-recursive term)
    SELECT ...
    UNION ALL
    -- Recursive term
    SELECT ... FROM cte_name JOIN ...
)
SELECT * FROM cte_name;
```

### Hierarchical Data

```sql
-- Employee hierarchy
WITH RECURSIVE org_chart AS (
    -- Base case: top-level managers
    SELECT id, name, manager_id, 1 AS level
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive: direct reports
    SELECT e.id, e.name, e.manager_id, oc.level + 1
    FROM employees e
    JOIN org_chart oc ON e.manager_id = oc.id
)
SELECT * FROM org_chart ORDER BY level, name;
```

### Category Tree

```sql
-- Product category hierarchy
WITH RECURSIVE category_tree AS (
    SELECT id, name, parent_id, name AS path
    FROM categories
    WHERE parent_id IS NULL

    UNION ALL

    SELECT c.id, c.name, c.parent_id,
           ct.path || ' > ' || c.name
    FROM categories c
    JOIN category_tree ct ON c.parent_id = ct.id
)
SELECT * FROM category_tree;
```

### Date Series Generation

```sql
-- Generate dates for next 30 days
WITH RECURSIVE date_series AS (
    SELECT CURRENT_DATE AS date
    UNION ALL
    SELECT date + 1 FROM date_series
    WHERE date < CURRENT_DATE + 30
)
SELECT date FROM date_series;
```

### Graph Traversal

```sql
-- Find all reachable nodes from node 1
WITH RECURSIVE reachable AS (
    SELECT id, name, ARRAY[id] AS path
    FROM nodes
    WHERE id = 1

    UNION ALL

    SELECT n.id, n.name, r.path || n.id
    FROM nodes n
    JOIN edges e ON n.id = e.to_node
    JOIN reachable r ON e.from_node = r.id
    WHERE n.id <> ALL(r.path)  -- Prevent cycles
)
SELECT * FROM reachable;
```

### Recursive Aggregation

```sql
-- Running total using recursive CTE
WITH RECURSIVE running AS (
    SELECT
        id,
        order_date,
        amount,
        amount AS running_total
    FROM orders
    WHERE order_date = (SELECT MIN(order_date) FROM orders)

    UNION ALL

    SELECT
        o.id,
        o.order_date,
        o.amount,
        r.running_total + o.amount
    FROM orders o
    JOIN running r ON o.order_date = (
        SELECT MIN(order_date)
        FROM orders
        WHERE order_date > r.order_date
    )
)
SELECT * FROM running;
```

---

## PIVOT and UNPIVOT

### Manual PIVOT (PostgreSQL)

```sql
-- Sales by product and month
SELECT
    product_id,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 1 THEN amount END) AS jan,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 2 THEN amount END) AS feb,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 3 THEN amount END) AS mar,
    SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = 4 THEN amount END) AS apr
FROM orders
GROUP BY product_id;
```

### crosstab (PostgreSQL)

```sql
-- Requires tablefunc extension
CREATE EXTENSION IF NOT EXISTS tablefunc;

SELECT *
FROM crosstab(
    'SELECT product_id, EXTRACT(MONTH FROM order_date)::INT, SUM(amount)
     FROM orders
     GROUP BY product_id, EXTRACT(MONTH FROM order_date)
     ORDER BY 1, 2',
    'SELECT generate_series(1, 12)'
) AS ct(product_id INT, jan NUMERIC, feb NUMERIC, mar NUMERIC,
        apr NUMERIC, may NUMERIC, jun NUMERIC, jul NUMERIC,
        aug NUMERIC, sep NUMERIC, oct NUMERIC, nov NUMERIC, dec NUMERIC);
```

### PIVOT (SQL Server)

```sql
SELECT *
FROM (
    SELECT product_id, order_date, amount
    FROM orders
) src
PIVOT (
    SUM(amount)
    FOR EXTRACT(MONTH FROM order_date) IN (
        [1], [2], [3], [4], [5], [6],
        [7], [8], [9], [10], [11], [12]
    )
) pvt;
```

### UNPIVOT (SQL Server)

```sql
-- Convert columns back to rows
SELECT product_id, month_num, amount
FROM monthly_sales
UNPIVOT (
    amount FOR month_num IN (jan, feb, mar, apr, may, jun,
                            jul, aug, sep, oct, nov, dec)
) unpvt;
```

### Dynamic PIVOT

```sql
-- PostgreSQL dynamic pivot
DO $$
DECLARE
    months TEXT;
    rec RECORD;
BEGIN
    SELECT STRING_AGG(DISTINCT
        'SUM(CASE WHEN EXTRACT(MONTH FROM order_date) = ' ||
        EXTRACT(MONTH FROM order_date)::TEXT ||
        ' THEN amount END) AS ' ||
        TO_CHAR(order_date, 'mon'),
        ', ' ORDER BY 1
    ) INTO months
    FROM orders;

    EXECUTE format('
        SELECT product_id, %s
        FROM orders
        GROUP BY product_id
    ', months);
END $$;
```

---

## EXPLAIN and ANALYZE

### Basic EXPLAIN

```sql
EXPLAIN SELECT * FROM employees WHERE salary > 50000;
```

### EXPLAIN with Detail

```sql
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM employees
WHERE department_id = 1 AND salary > 50000;
```

### EXPLAIN Formats

```sql
-- JSON format
EXPLAIN (ANALYZE, FORMAT JSON)
SELECT * FROM employees WHERE id = 1;

-- YAML format
EXPLAIN (ANALYZE, FORMAT YAML)
SELECT * FROM employees WHERE id = 1;
```

### Reading EXPLAIN Output

```
Seq Scan on employees  (cost=0.00..1234.00 rows=500 width=100)
  Filter: (salary > 50000)
  Rows Removed by Filter: 500

-- cost: estimated startup cost..total cost
-- rows: estimated number of rows
-- width: estimated row width in bytes
-- Filter: conditions applied after scanning
```

### Common Plan Types

```
Seq Scan       -- Sequential table scan (reads entire table)
Index Scan     -- Uses index to find rows
Index Only Scan-- Data comes entirely from index
Bitmap Scan    -- Uses multiple indexes
Nested Loop    -- For small result sets
Hash Join      -- For larger joins
Merge Join     -- For sorted data
Sort           -- In-memory or disk sort
HashAggregate  -- Hash-based aggregation
GroupAggregate -- Sort-based aggregation
```

### Identifying Slow Queries

```sql
-- Find queries using sequential scans on large tables
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
```

---

## Query Hints

### PostgreSQL Hints (pg_hint_plan)

```sql
-- Force index usage
/*+ IndexScan(employees idx_employees_salary) */
SELECT * FROM employees WHERE salary > 50000;

-- Force join method
/*+ HashJoin(e d) */
SELECT * FROM employees e JOIN departments d ON e.department_id = d.id;

-- Force parallel query
/*+ Parallel(e 4) */
SELECT * FROM employees e WHERE salary > 50000;

-- Force sequence scan
/*+ SeqScan(employees) */
SELECT * FROM employees WHERE salary > 50000;
```

### SQL Server Hints

```sql
-- Index hint
SELECT * FROM employees WITH (INDEX(idx_salary))
WHERE salary > 50000;

-- Join hint
SELECT *
FROM employees e
INNER LOOP JOIN departments d ON e.department_id = d.id;

-- Table hint
SELECT * FROM employees WITH (NOLOCK)
WHERE salary > 50000;
```

---

## Partitioning

### Range Partitioning

```sql
-- PostgreSQL declarative partitioning
CREATE TABLE orders (
    id SERIAL,
    order_date DATE NOT NULL,
    amount NUMERIC(10,2)
) PARTITION BY RANGE (order_date);

CREATE TABLE orders_2023 PARTITION OF orders
    FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');

CREATE TABLE orders_2024 PARTITION OF orders
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
```

### List Partitioning

```sql
CREATE TABLE employees (
    id SERIAL,
    name VARCHAR(100),
    region VARCHAR(20)
) PARTITION BY LIST (region);

CREATE TABLE employees_north PARTITION OF employees
    FOR VALUES IN ('north', 'northeast');
CREATE TABLE employees_south PARTITION OF employees
    FOR VALUES IN ('south', 'southeast');
CREATE TABLE employees_west PARTITION OF employees
    FOR VALUES IN ('west', 'northwest');
```

### Hash Partitioning

```sql
CREATE TABLE sessions (
    id SERIAL,
    user_id INTEGER,
    data JSONB
) PARTITION BY HASH (user_id);

CREATE TABLE sessions_0 PARTITION OF sessions
    FOR VALUES WITH (MODULUS 4, REMAINDER 0);
CREATE TABLE sessions_1 PARTITION OF sessions
    FOR VALUES WITH (MODULUS 4, REMAINDER 1);
CREATE TABLE sessions_2 PARTITION OF sessions
    FOR VALUES WITH (MODULUS 4, REMAINDER 2);
CREATE TABLE sessions_3 PARTITION OF sessions
    FOR VALUES WITH (MODULUS 4, REMAINDER 3);
```

### Partition Pruning

```sql
-- Query automatically scans only relevant partitions
SELECT * FROM orders
WHERE order_date BETWEEN '2024-01-01' AND '2024-12-31';

-- Verify partition pruning
EXPLAIN SELECT * FROM orders
WHERE order_date = '2024-06-15';
```

### Automatic Partition Creation

```sql
-- Create partition for next month
DO $$
DECLARE
    next_month DATE := DATE_TRUNC('month', CURRENT_DATE + INTERVAL '1 month');
    next_next_month DATE := next_month + INTERVAL '1 month';
    partition_name TEXT;
BEGIN
    partition_name := 'orders_' || TO_CHAR(next_month, 'YYYY_MM');
    EXECUTE format('
        CREATE TABLE IF NOT EXISTS %I PARTITION OF orders
        FOR VALUES FROM (%L) TO (%L)',
        partition_name, next_month, next_next_month
    );
END $$;
```

---

## Materialized Views

### Basic Materialized View

```sql
CREATE MATERIALIZED VIEW mv_dept_stats AS
SELECT
    d.id AS dept_id,
    d.name AS dept_name,
    COUNT(e.id) AS emp_count,
    AVG(e.salary) AS avg_salary,
    MAX(e.salary) AS max_salary
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id
GROUP BY d.id, d.name;
```

### Refreshing Materialized Views

```sql
-- Full refresh
REFRESH MATERIALIZED VIEW mv_dept_stats;

-- Concurrent refresh (requires unique index)
CREATE UNIQUE INDEX idx_mv_dept_stats ON mv_dept_stats (dept_id);
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_dept_stats;

-- Refresh with data
REFRESH MATERIALIZED VIEW mv_dept_stats WITH DATA;
```

### Auto-Refresh with Triggers

```sql
-- Create refresh function
CREATE OR REPLACE FUNCTION refresh_dept_stats()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_dept_stats;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
CREATE TRIGGER trg_refresh_dept_stats
AFTER INSERT OR UPDATE OR DELETE ON employees
FOR EACH STATEMENT
EXECUTE FUNCTION refresh_dept_stats();
```

### Indexing Materialized Views

```sql
CREATE INDEX idx_mv_dept_emp_count ON mv_dept_stats (emp_count);
CREATE INDEX idx_mv_dept_avg_salary ON mv_dept_stats (avg_salary);
```

### Refresh Policies

```sql
-- Check last refresh time
SELECT
    relname,
    last_refresh
FROM pg_stat_user_tables
WHERE relname = 'mv_dept_stats';

-- Scheduled refresh (via pg_cron)
SELECT cron.schedule('refresh-dept-stats', '0 2 * * *',
    'REFRESH MATERIALIZED VIEW CONCURRENTLY mv_dept_stats');
```

---

## Advanced Aggregation

### GROUPING

```sql
-- Check if a column is grouped
SELECT
    department_id,
    GROUPING(department_id) AS is_grand_total,
    SUM(salary)
FROM employees
GROUP BY ROLLUP(department_id);
```

### FILTER Clause

```sql
SELECT
    department_id,
    COUNT(*) AS total,
    COUNT(*) FILTER (WHERE salary > 75000) AS high_earners,
    COUNT(*) FILTER (WHERE hire_date > '2023-01-01') AS recent_hires
FROM employees
GROUP BY department_id;
```

### ARRAY Aggregation

```sql
-- PostgreSQL array aggregation
SELECT
    department_id,
    ARRAY_AGG(first_name ORDER BY last_name) AS employees
FROM employees
GROUP BY department_id;
```

### JSON Aggregation

```sql
-- Aggregate to JSON array
SELECT
    department_id,
    JSON_AGG(
        JSON_BUILD_OBJECT(
            'name', first_name || ' ' || last_name,
            'salary', salary
        )
    ) AS employees
FROM employees
GROUP BY department_id;
```

### String Aggregation

```sql
-- PostgreSQL
SELECT
    department_id,
    STRING_AGG(first_name, ', ' ORDER BY last_name) AS employee_list
FROM employees
GROUP BY department_id;

-- MySQL
SELECT
    department_id,
    GROUP_CONCAT(first_name ORDER BY last_name SEPARATOR ', ')
FROM employees
GROUP BY department_id;
```

### Cube and Rollup

```sql
-- ROLLUP: hierarchical subtotals
SELECT
    COALESCE(region, 'All Regions') AS region,
    COALESCE(department_id::TEXT, 'All Depts') AS dept,
    SUM(salary)
FROM employees
GROUP BY ROLLUP(region, department_id);

-- CUBE: all combinations
SELECT
    COALESCE(region, 'All') AS region,
    COALESCE(department_id::TEXT, 'All') AS dept,
    SUM(salary)
FROM employees
GROUP BY CUBE(region, department_id);
```

---

## Advanced Joins

### Anti-Join

```sql
-- Find employees without orders
SELECT e.*
FROM employees e
LEFT JOIN orders o ON e.id = o.customer_id
WHERE o.id IS NULL;

-- Using NOT EXISTS
SELECT e.*
FROM employees e
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.customer_id = e.id
);

-- Using NOT IN
SELECT e.*
FROM employees e
WHERE e.id NOT IN (
    SELECT DISTINCT customer_id FROM orders WHERE customer_id IS NOT NULL
);
```

### Semi-Join

```sql
-- Find employees with orders
SELECT DISTINCT e.*
FROM employees e
INNER JOIN orders o ON e.id = o.customer_id;

-- Using EXISTS
SELECT e.*
FROM employees e
WHERE EXISTS (
    SELECT 1 FROM orders o WHERE o.customer_id = e.id
);
```

### Non-Equi Join

```sql
-- Salary grade assignment
SELECT
    e.first_name,
    e.salary,
    sg.grade
FROM employees e
JOIN salary_grades sg
    ON e.salary BETWEEN sg.min_salary AND sg.max_salary;
```

### Partitioned Outer Join

```sql
-- Ensure all departments appear for each month
SELECT
    d.name,
    EXTRACT(MONTH FROM o.order_date) AS month,
    COALESCE(SUM(o.amount), 0) AS total
FROM departments d
CROSS JOIN generate_series(1, 12) AS month
LEFT JOIN employees e ON e.department_id = d.id
LEFT JOIN orders o ON o.customer_id = e.id
    AND EXTRACT(MONTH FROM o.order_date) = month
GROUP BY d.name, month;
```

---

## Best Practices

1. **Use window functions** instead of self-joins for ranking/running totals
2. **Write CTEs** for complex multi-step queries to improve readability
3. **Test recursive CTEs** with depth limits to prevent infinite loops
4. **Use EXPLAIN ANALYZE** to understand query performance
5. **Partition large tables** by date or frequently filtered columns
6. **Refresh materialized views** on a schedule or with triggers
7. **Prefer DENSE_RANK** when you need consecutive numbering
8. **Use frame clauses carefully** to avoid unexpected results
9. **Index materialized views** for faster queries
10. **Monitor query plans** after schema changes
