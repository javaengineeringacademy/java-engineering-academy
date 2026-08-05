# SQL Query Examples

50+ practical SQL queries with explanations covering joins, aggregations, window functions, and more.

## Schema Reference

```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    city VARCHAR(50),
    country VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    price NUMERIC(10,2),
    stock INTEGER
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id),
    order_date DATE,
    status VARCHAR(20),
    total NUMERIC(10,2)
);

CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(id),
    product_id INTEGER REFERENCES products(id),
    quantity INTEGER,
    price NUMERIC(10,2)
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(50),
    salary NUMERIC(10,2),
    hire_date DATE,
    manager_id INTEGER REFERENCES employees(id)
);
```

---

## Simple Queries

### 1. Basic SELECT with Filter

```sql
SELECT name, email, city
FROM customers
WHERE country = 'USA';
```

### 2. Multiple Conditions

```sql
SELECT *
FROM products
WHERE price BETWEEN 10 AND 50
  AND stock > 0
  AND category IN ('Electronics', 'Books');
```

### 3. Pattern Matching

```sql
SELECT name, email
FROM customers
WHERE email LIKE '%@gmail.com'
   OR name LIKE 'J%';
```

### 4. Sorted Results

```sql
SELECT name, price
FROM products
ORDER BY price DESC
LIMIT 10;
```

### 5. Unique Values

```sql
SELECT DISTINCT city
FROM customers
ORDER BY city;
```

---

## Join Queries

### 6. INNER JOIN - Orders with Customer Names

```sql
SELECT
    o.id AS order_id,
    c.name AS customer_name,
    o.order_date,
    o.total
FROM orders o
INNER JOIN customers c ON o.customer_id = c.id
ORDER BY o.order_date DESC;
```

### 7. LEFT JOIN - All Customers with Orders

```sql
SELECT
    c.name,
    COUNT(o.id) AS order_count,
    COALESCE(SUM(o.total), 0) AS total_spent
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id
GROUP BY c.id, c.name
ORDER BY total_spent DESC;
```

### 8. RIGHT JOIN - All Order Items with Products

```sql
SELECT
    p.name AS product_name,
    oi.quantity,
    oi.price,
    oi.quantity * oi.price AS line_total
FROM order_items oi
RIGHT JOIN products p ON oi.product_id = p.id
ORDER BY p.name;
```

### 9. FULL OUTER JOIN - Customer and Product Overlap

```sql
SELECT
    c.name AS customer_name,
    p.name AS product_name
FROM customers c
FULL OUTER JOIN orders o ON c.id = o.customer_id
FULL OUTER JOIN order_items oi ON o.id = oi.order_id
FULL OUTER JOIN products p ON oi.product_id = p.id
WHERE c.name IS NULL OR p.name IS NULL;
```

### 10. SELF JOIN - Employee and Manager

```sql
SELECT
    e.name AS employee,
    m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;
```

### 11. Multi-Table JOIN

```sql
SELECT
    c.name AS customer,
    p.name AS product,
    oi.quantity,
    o.order_date
FROM customers c
JOIN orders o ON c.id = o.customer_id
JOIN order_items oi ON o.id = oi.order_id
JOIN products p ON oi.product_id = p.id
WHERE o.order_date >= '2024-01-01'
ORDER BY o.order_date;
```

### 12. CROSS JOIN - Product Combinations

```sql
SELECT
    p1.name AS product1,
    p2.name AS product2
FROM products p1
CROSS JOIN products p2
WHERE p1.id < p2.id
LIMIT 10;
```

### 13. LATERAL JOIN - Top 3 Products per Category

```sql
SELECT
    p.category,
    top_products.*
FROM (SELECT DISTINCT category FROM products) p
LEFT JOIN LATERAL (
    SELECT name, price
    FROM products
    WHERE category = p.category
    ORDER BY price DESC
    LIMIT 3
) top_products ON TRUE;
```

---

## Aggregation Queries

### 14. Basic Aggregation

```sql
SELECT
    category,
    COUNT(*) AS product_count,
    AVG(price) AS avg_price,
    MIN(price) AS min_price,
    MAX(price) AS max_price
FROM products
GROUP BY category;
```

### 15. HAVING Clause

```sql
SELECT
    customer_id,
    COUNT(*) AS order_count,
    SUM(total) AS total_spent
FROM orders
GROUP BY customer_id
HAVING COUNT(*) > 5 AND SUM(total) > 1000
ORDER BY total_spent DESC;
```

### 16. Conditional Aggregation

```sql
SELECT
    category,
    COUNT(*) AS total_products,
    COUNT(*) FILTER (WHERE price > 100) AS expensive,
    COUNT(*) FILTER (WHERE price <= 100) AS affordable,
    SUM(stock) AS total_stock
FROM products
GROUP BY category;
```

### 17. Percentage of Total

```sql
SELECT
    category,
    COUNT(*) AS count,
    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) AS percentage
FROM products
GROUP BY category
ORDER BY percentage DESC;
```

### 18. Monthly Revenue

```sql
SELECT
    DATE_TRUNC('month', order_date) AS month,
    COUNT(*) AS orders,
    SUM(total) AS revenue,
    AVG(total) AS avg_order_value
FROM orders
WHERE order_date >= '2023-01-01'
GROUP BY DATE_TRUNC('month', order_date)
ORDER BY month;
```

---

## Window Function Queries

### 19. Ranking Employees by Salary

```sql
SELECT
    name,
    department,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS overall_rank,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank,
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
FROM employees;
```

### 20. Department Salary Rankings

```sql
SELECT
    name,
    department,
    salary,
    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS dept_rank
FROM employees
ORDER BY department, dept_rank;
```

### 21. Running Total

```sql
SELECT
    order_date,
    total,
    SUM(total) OVER (
        ORDER BY order_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total
FROM orders
ORDER BY order_date;
```

### 22. Moving Average

```sql
SELECT
    order_date,
    total,
    AVG(total) OVER (
        ORDER BY order_date
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
    ) AS moving_avg_3day
FROM orders
ORDER BY order_date;
```

### 23. Previous and Next Orders

```sql
SELECT
    id,
    order_date,
    total,
    LAG(total, 1) OVER (ORDER BY order_date) AS prev_order,
    LEAD(total, 1) OVER (ORDER BY order_date) AS next_order,
    total - LAG(total, 1) OVER (ORDER BY order_date) AS change
FROM orders
ORDER BY order_date;
```

### 24. First and Last Order per Customer

```sql
SELECT
    c.name,
    o.order_date,
    o.total,
    FIRST_VALUE(o.total) OVER (
        PARTITION BY o.customer_id
        ORDER BY o.order_date
    ) AS first_order,
    LAST_VALUE(o.total) OVER (
        PARTITION BY o.customer_id
        ORDER BY o.order_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS last_order
FROM orders o
JOIN customers c ON o.customer_id = c.id;
```

### 25. Salary Quartiles

```sql
SELECT
    name,
    salary,
    NTILE(4) OVER (ORDER BY salary) AS quartile,
    CASE NTILE(4) OVER (ORDER BY salary)
        WHEN 1 THEN 'Bottom 25%'
        WHEN 2 THEN '25-50%'
        WHEN 3 THEN '50-75%'
        WHEN 4 THEN 'Top 25%'
    END AS salary_band
FROM employees;
```

### 26. Year-over-Year Growth

```sql
WITH yearly_sales AS (
    SELECT
        EXTRACT(YEAR FROM order_date) AS year,
        SUM(total) AS revenue
    FROM orders
    GROUP BY EXTRACT(YEAR FROM order_date)
)
SELECT
    year,
    revenue,
    LAG(revenue) OVER (ORDER BY year) AS prev_year,
    ROUND(
        (revenue - LAG(revenue) OVER (ORDER BY year)) * 100.0
        / NULLIF(LAG(revenue) OVER (ORDER BY year), 0),
        2
    ) AS growth_pct
FROM yearly_sales;
```

### 27. Cumulative Distribution

```sql
SELECT
    name,
    salary,
    CUME_DIST() OVER (ORDER BY salary) AS cumulative_pct,
    PERCENT_RANK() OVER (ORDER BY salary) AS percentile_rank
FROM employees;
```

---

## Subquery Queries

### 28. Scalar Subquery

```sql
SELECT
    name,
    salary,
    salary - (SELECT AVG(salary) FROM employees) AS diff_from_avg
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```

### 29. Correlated Subquery

```sql
SELECT
    e.name,
    e.department,
    e.salary
FROM employees e
WHERE e.salary = (
    SELECT MAX(salary)
    FROM employees
    WHERE department = e.department
);
```

### 30. EXISTS Subquery

```sql
SELECT c.name, c.email
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
      AND o.order_date >= '2024-01-01'
);
```

### 31. IN Subquery

```sql
SELECT name, category, price
FROM products
WHERE category IN (
    SELECT DISTINCT category
    FROM products
    WHERE price > 500
)
ORDER BY category, price;
```

### 32. Derived Table

```sql
SELECT
    d.category,
    d.avg_price,
    d.product_count
FROM (
    SELECT
        category,
        AVG(price) AS avg_price,
        COUNT(*) AS product_count
    FROM products
    GROUP BY category
) d
WHERE d.product_count > 10
ORDER BY d.avg_price DESC;
```

---

## CTE Queries

### 33. Basic CTE

```sql
WITH customer_stats AS (
    SELECT
        customer_id,
        COUNT(*) AS orders,
        SUM(total) AS spent
    FROM orders
    GROUP BY customer_id
)
SELECT c.name, cs.orders, cs.spent
FROM customers c
JOIN customer_stats cs ON c.id = cs.customer_id
WHERE cs.spent > 1000
ORDER BY cs.spent DESC;
```

### 34. Multiple CTEs

```sql
WITH
high_spenders AS (
    SELECT customer_id, SUM(total) AS total_spent
    FROM orders
    GROUP BY customer_id
    HAVING SUM(total) > 5000
),
recent_orders AS (
    SELECT customer_id, MAX(order_date) AS last_order
    FROM orders
    GROUP BY customer_id
    HAVING MAX(order_date) >= '2024-01-01'
)
SELECT c.name, hs.total_spent, ro.last_order
FROM customers c
JOIN high_spenders hs ON c.id = hs.customer_id
JOIN recent_orders ro ON c.id = ro.customer_id;
```

### 35. Recursive CTE - Employee Hierarchy

```sql
WITH RECURSIVE org AS (
    SELECT id, name, manager_id, 1 AS level, name AS path
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    SELECT e.id, e.name, e.manager_id, o.level + 1,
           o.path || ' > ' || e.name
    FROM employees e
    JOIN org o ON e.manager_id = o.id
)
SELECT
    name,
    level,
    path,
    REPEAT('  ', level - 1) || name AS tree
FROM org
ORDER BY path;
```

### 36. Recursive CTE - Date Series

```sql
WITH RECURSIVE dates AS (
    SELECT '2024-01-01'::DATE AS date
    UNION ALL
    SELECT date + 1
    FROM dates
    WHERE date < '2024-12-31'
)
SELECT
    date,
    EXTRACT(DOW FROM date) AS day_of_week,
    EXTRACT(MONTH FROM date) AS month
FROM dates
WHERE EXTRACT(DOW FROM date) NOT IN (0, 6);  -- Weekdays only
```

---

## Date and String Operations

### 37. Date Range Queries

```sql
-- Orders in the last 30 days
SELECT * FROM orders
WHERE order_date >= CURRENT_DATE - INTERVAL '30 days';

-- Orders this month
SELECT * FROM orders
WHERE order_date >= DATE_TRUNC('month', CURRENT_DATE)
  AND order_date < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month';

-- Orders between two dates
SELECT * FROM orders
WHERE order_date BETWEEN '2024-01-01' AND '2024-12-31';
```

### 38. Date Extraction

```sql
SELECT
    order_date,
    EXTRACT(YEAR FROM order_date) AS year,
    EXTRACT(MONTH FROM order_date) AS month,
    EXTRACT(DAY FROM order_date) AS day,
    EXTRACT(DOW FROM order_date) AS day_of_week,
    TO_CHAR(order_date, 'Day') AS day_name,
    TO_CHAR(order_date, 'Month') AS month_name
FROM orders;
```

### 39. Date Arithmetic

```sql
SELECT
    order_date,
    order_date + INTERVAL '7 days' AS plus_week,
    order_date - INTERVAL '1 month' AS minus_month,
    AGE(CURRENT_DATE, order_date) AS age,
    DATE_PART('day', AGE(CURRENT_DATE, order_date)) AS days_since
FROM orders;
```

### 40. String Manipulation

```sql
SELECT
    name,
    UPPER(name) AS upper_name,
    LOWER(name) AS lower_name,
    LENGTH(name) AS name_length,
    LEFT(name, 3) AS first_three,
    RIGHT(name, 3) AS last_three,
    SUBSTRING(name FROM 2 FOR 3) AS chars_2_to_4,
    REPLACE(name, 'old', 'new') AS replaced,
    TRIM(BOTH ' ' FROM name) AS trimmed,
    CONCAT(first_name, ' ', last_name) AS full_name,
    name || ' (' || city || ')' AS name_city
FROM customers;
```

### 41. String Splitting

```sql
-- PostgreSQL: Split comma-separated values
SELECT
    id,
    name,
    UNNEST(STRING_TO_ARRAY(tags, ',')) AS tag
FROM products;

-- Split and aggregate
SELECT
    tag,
    COUNT(*) AS count
FROM products,
LATERAL UNNEST(STRING_TO_ARRAY(tags, ',')) AS tag
GROUP BY tag
ORDER BY count DESC;
```

---

## Gap and Island Problems

### 42. Find Gaps in Sequence

```sql
-- Find missing IDs
WITH RECURSIVE seq AS (
    SELECT MIN(id) AS id FROM my_table
    UNION ALL
    SELECT id + 1 FROM seq WHERE id < (SELECT MAX(id) FROM my_table)
)
SELECT s.id AS missing_id
FROM seq s
LEFT JOIN my_table t ON s.id = t.id
WHERE t.id IS NULL;
```

### 43. Find Consecutive Days (Islands)

```sql
-- Find consecutive order dates per customer
WITH ordered_dates AS (
    SELECT
        customer_id,
        order_date,
        order_date - ROW_NUMBER() OVER (
            PARTITION BY customer_id ORDER BY order_date
        )::INT AS island_group
    FROM orders
)
SELECT
    customer_id,
    MIN(order_date) AS streak_start,
    MAX(order_date) AS streak_end,
    COUNT(*) AS consecutive_days
FROM ordered_dates
GROUP BY customer_id, island_group
HAVING COUNT(*) >= 3
ORDER BY consecutive_days DESC;
```

### 44. Find Gaps in Time Series

```sql
-- Find gaps larger than 1 day
WITH ordered AS (
    SELECT
        id,
        created_at,
        LEAD(created_at) OVER (ORDER BY created_at) AS next_created
    FROM events
)
SELECT
    id,
    created_at,
    next_created,
    next_created - created_at AS gap
FROM ordered
WHERE next_created - created_at > INTERVAL '1 day';
```

### 45. Fill Gaps with Default Values

```sql
-- Fill missing dates with zero sales
WITH date_series AS (
    SELECT generate_series(
        '2024-01-01'::DATE,
        '2024-12-31'::DATE,
        '1 day'
    ) AS date
),
daily_sales AS (
    SELECT
        order_date::DATE AS date,
        SUM(total) AS revenue
    FROM orders
    GROUP BY order_date::DATE
)
SELECT
    ds.date,
    COALESCE(daily.revenue, 0) AS revenue
FROM date_series ds
LEFT JOIN daily_sales daily ON ds.date = daily.date
ORDER BY ds.date;
```

---

## Pivoting Queries

### 46. Manual Pivot

```sql
SELECT
    category,
    COUNT(*) FILTER (WHERE price < 25) AS under_25,
    COUNT(*) FILTER (WHERE price >= 25 AND price < 100) AS mid_range,
    COUNT(*) FILTER (WHERE price >= 100) AS premium
FROM products
GROUP BY category;
```

### 47. Pivot with CASE

```sql
SELECT
    c.name AS customer,
    SUM(CASE WHEN EXTRACT(MONTH FROM o.order_date) = 1 THEN o.total END) AS jan,
    SUM(CASE WHEN EXTRACT(MONTH FROM o.order_date) = 2 THEN o.total END) AS feb,
    SUM(CASE WHEN EXTRACT(MONTH FROM o.order_date) = 3 THEN o.total END) AS mar,
    SUM(CASE WHEN EXTRACT(MONTH FROM o.order_date) = 4 THEN o.total END) AS apr
FROM customers c
JOIN orders o ON c.id = o.customer_id
WHERE EXTRACT(YEAR FROM o.order_date) = 2024
GROUP BY c.id, c.name;
```

### 48. Unpivot with UNION ALL

```sql
-- Convert pivot table back to rows
SELECT product_id, 'jan' AS month, jan AS amount FROM monthly_sales
UNION ALL
SELECT product_id, 'feb', feb FROM monthly_sales
UNION ALL
SELECT product_id, 'mar', mar FROM monthly_sales;
```

---

## Advanced Patterns

### 49. Deduplication

```sql
-- Keep only the most recent record per group
WITH ranked AS (
    SELECT *,
        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY created_at DESC
        ) AS rn
    FROM customer_updates
)
DELETE FROM customer_updates
WHERE ctid IN (
    SELECT ctid FROM ranked WHERE rn > 1
);
```

### 50. Running Difference

```sql
-- Calculate running difference from initial value
WITH running AS (
    SELECT
        id,
        order_date,
        amount,
        FIRST_VALUE(amount) OVER (ORDER BY order_date) AS initial,
        SUM(amount) OVER (ORDER BY order_date) AS cumulative
    FROM orders
)
SELECT
    order_date,
    amount,
    cumulative,
    cumulative - initial AS diff_from_start
FROM running;
```

### 51. Top-N per Group

```sql
-- Top 3 products per category
WITH ranked AS (
    SELECT
        category,
        name,
        price,
        ROW_NUMBER() OVER (
            PARTITION BY category
            ORDER BY price DESC
        ) AS rank
    FROM products
)
SELECT category, name, price, rank
FROM ranked
WHERE rank <= 3;
```

### 52. Session Analysis

```sql
-- Calculate session duration
WITH sessions AS (
    SELECT
        user_id,
        event_time,
        event_time - LAG(event_time) OVER (
            PARTITION BY user_id
            ORDER BY event_time
        ) AS time_diff
    FROM user_events
),
session_groups AS (
    SELECT *,
        SUM(CASE WHEN time_diff > INTERVAL '30 minutes' THEN 1 ELSE 0 END)
            OVER (PARTITION BY user_id ORDER BY event_time) AS session_id
    FROM sessions
)
SELECT
    user_id,
    session_id,
    MIN(event_time) AS session_start,
    MAX(event_time) AS session_end,
    MAX(event_time) - MIN(event_time) AS duration
FROM session_groups
GROUP BY user_id, session_id;
```

### 53. Percentile Analysis

```sql
-- Revenue percentiles by customer
SELECT
    c.name,
    SUM(o.total) AS total_spent,
    NTILE(10) OVER (ORDER BY SUM(o.total)) AS decile,
    PERCENT_RANK() OVER (ORDER BY SUM(o.total)) AS percentile
FROM customers c
JOIN orders o ON c.id = o.customer_id
GROUP BY c.id, c.name;
```

### 54. Rolling Aggregation

```sql
-- 7-day rolling sum
SELECT
    order_date,
    daily_total,
    SUM(daily_total) OVER (
        ORDER BY order_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS rolling_7day_sum
FROM (
    SELECT order_date::DATE AS order_date, SUM(total) AS daily_total
    FROM orders
    GROUP BY order_date::DATE
) daily;
```

### 55. Gap-Filling with Join

```sql
-- Fill missing months with zero revenue
WITH months AS (
    SELECT generate_series(
        '2024-01-01'::DATE,
        '2024-12-31'::DATE,
        '1 month'
    ) AS month
),
monthly_revenue AS (
    SELECT
        DATE_TRUNC('month', order_date) AS month,
        SUM(total) AS revenue
    FROM orders
    GROUP BY DATE_TRUNC('month', order_date)
)
SELECT
    m.month,
    COALESCE(mr.revenue, 0) AS revenue
FROM months m
LEFT JOIN monthly_revenue mr ON m.month = mr.month
ORDER BY m.month;
```

### 56. Hierarchical Sum

```sql
-- Sum including all descendants
WITH RECURSIVE tree AS (
    SELECT id, name, parent_id, amount
    FROM accounts
    WHERE parent_id IS NULL

    UNION ALL

    SELECT a.id, a.name, a.parent_id, a.amount
    FROM accounts a
    JOIN tree t ON a.parent_id = t.id
)
SELECT
    parent_id,
    SUM(amount) AS total_amount
FROM tree
GROUP BY parent_id;
```

### 57. Consecutive Count

```sql
-- Count consecutive occurrences
WITH flagged AS (
    SELECT
        id,
        status,
        status != LAG(status) OVER (ORDER BY id) AS change_flag
    FROM events
),
groups AS (
    SELECT *,
        SUM(change_flag) OVER (ORDER BY id) AS grp
    FROM flagged
)
SELECT
    status,
    MIN(id) AS start_id,
    MAX(id) AS end_id,
    COUNT(*) AS consecutive_count
FROM groups
GROUP BY status, grp
HAVING COUNT(*) >= 3;
```

### 58. Data Quality Check

```sql
-- Find duplicate records
SELECT
    email,
    COUNT(*) AS occurrences
FROM customers
GROUP BY email
HAVING COUNT(*) > 1;

-- Find overlapping date ranges
SELECT
    a.id AS reservation_a,
    b.id AS reservation_b,
    a.start_date AS a_start,
    a.end_date AS a_end,
    b.start_date AS b_start,
    b.end_date AS b_end
FROM reservations a
JOIN reservations b
    ON a.room_id = b.room_id
    AND a.id < b.id
    AND a.start_date <= b.end_date
    AND b.start_date <= a.end_date;
```

### 59. Median Calculation

```sql
-- Median salary
WITH ranked AS (
    SELECT
        salary,
        ROW_NUMBER() OVER (ORDER BY salary) AS rn,
        COUNT(*) OVER () AS total
    FROM employees
)
SELECT AVG(salary) AS median
FROM ranked
WHERE rn IN ((total + 1) / 2, (total + 2) / 2);
```

### 60. First Non-Null

```sql
-- Get first non-null value per group
WITH filled AS (
    SELECT
        customer_id,
        COALESCE(phone, email, 'no contact') AS contact
    FROM customer_contacts
)
SELECT customer_id, contact
FROM (
    SELECT *,
        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY
                CASE
                    WHEN phone IS NOT NULL THEN 1
                    WHEN email IS NOT NULL THEN 2
                    ELSE 3
                END
        ) AS rn
    FROM customer_contacts
) ranked
WHERE rn = 1;
```

---

## Quick Reference

| Pattern | Use Case |
|---------|----------|
| `ROW_NUMBER()` | Unique sequential numbering |
| `RANK()` | Ranking with gaps |
| `DENSE_RANK()` | Ranking without gaps |
| `LAG/LEAD` | Access previous/next rows |
| `NTILE()` | Divide into N groups |
| `EXISTS` | Check for existence |
| `WITH RECURSIVE` | Hierarchical/graph data |
| `FILTER (WHERE)` | Conditional aggregation |
| `STRING_AGG()` | Concatenate values |
| `UNNEST(ARRAY)` | Expand array to rows |
