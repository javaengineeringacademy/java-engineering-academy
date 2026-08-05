# SQL Fundamentals

Comprehensive guide to core SQL concepts from basic queries to complex joins and subqueries.

## Table of Contents

- [Data Types](#data-types)
- [SELECT Statement](#select-statement)
- [WHERE Clause](#where-clause)
- [JOIN Operations](#join-operations)
- [GROUP BY and HAVING](#group-by-and-having)
- [ORDER BY](#order-by)
- [Aggregate Functions](#aggregate-functions)
- [Subqueries](#subqueries)
- [UNION Operations](#union-operations)
- [CASE Expressions](#case-expressions)
- [NULL Handling](#null-handling)
- [Data Manipulation](#data-manipulation)
- [Data Definition](#data-definition)

---

## Data Types

### Numeric Types

```sql
-- Exact numbers
SMALLINT          -- 2 bytes, -32768 to 32767
INTEGER           -- 4 bytes, -2^31 to 2^31-1
BIGINT            -- 8 bytes, -2^63 to 2^63-1
NUMERIC(p,s)      -- Exact precision, e.g., NUMERIC(10,2)
DECIMAL(p,s)      -- Same as NUMERIC

-- Approximate
REAL              -- 4 bytes, single precision
DOUBLE PRECISION  -- 8 bytes, double precision
```

### String Types

```sql
CHAR(n)           -- Fixed length, padded with spaces
VARCHAR(n)        -- Variable length, max n characters
TEXT              -- Variable length, unlimited
BINARY(n)         -- Fixed length binary
VARBINARY(n)      -- Variable length binary
```

### Date/Time Types

```sql
DATE              -- 'YYYY-MM-DD'
TIME              -- 'HH:MI:SS'
TIMESTAMP         -- 'YYYY-MM-DD HH:MI:SS'
TIMESTAMPTZ       -- Timestamp with timezone
INTERVAL          -- Time duration (e.g., '1 year 2 months')
```

### Boolean Type

```sql
BOOLEAN           -- TRUE, FALSE, or NULL
```

### Creating Tables

```sql
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    hire_date DATE DEFAULT CURRENT_DATE,
    salary NUMERIC(10,2),
    department_id INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## SELECT Statement

### Basic SELECT

```sql
-- Select all columns
SELECT * FROM employees;

-- Select specific columns
SELECT first_name, last_name, email FROM employees;

-- Aliases
SELECT first_name AS "First Name",
       last_name AS "Last Name"
FROM employees;

-- Distinct values
SELECT DISTINCT department_id FROM employees;

-- Distinct combinations
SELECT DISTINCT department_id, is_active FROM employees;
```

### Computed Columns

```sql
SELECT
    first_name,
    last_name,
    salary,
    salary * 12 AS annual_salary,
    salary * 0.1 AS bonus
FROM employees;
```

### String Functions

```sql
SELECT
    UPPER(first_name),
    LOWER(last_name),
    LENGTH(email),
    CONCAT(first_name, ' ', last_name) AS full_name,
    SUBSTRING(email, 1, 3) AS email_prefix,
    REPLACE(email, '@company.com', '@corp.com'),
    TRIM(first_name),
    LEFT(last_name, 3),
    RIGHT(email, 4)
FROM employees;
```

---

## WHERE Clause

### Comparison Operators

```sql
SELECT * FROM employees WHERE salary > 50000;
SELECT * FROM employees WHERE salary >= 50000;
SELECT * FROM employees WHERE salary != 50000;
SELECT * FROM employees WHERE salary <> 50000;
```

### Logical Operators

```sql
-- AND
SELECT * FROM employees
WHERE salary > 50000 AND department_id = 1;

-- OR
SELECT * FROM employees
WHERE department_id = 1 OR department_id = 2;

-- NOT
SELECT * FROM employees
WHERE NOT is_active;

-- Combined
SELECT * FROM employees
WHERE (department_id = 1 OR department_id = 2)
  AND salary > 50000
  AND is_active = TRUE;
```

### BETWEEN

```sql
SELECT * FROM employees
WHERE salary BETWEEN 40000 AND 60000;

-- With dates
SELECT * FROM employees
WHERE hire_date BETWEEN '2023-01-01' AND '2023-12-31';
```

### IN

```sql
SELECT * FROM employees
WHERE department_id IN (1, 2, 3);

-- With subquery
SELECT * FROM employees
WHERE department_id IN (
    SELECT id FROM departments WHERE location = 'NYC'
);
```

### LIKE

```sql
-- % matches any sequence of characters
SELECT * FROM employees WHERE email LIKE '%@company.com';
SELECT * FROM employees WHERE first_name LIKE 'J%';
SELECT * FROM employees WHERE last_name LIKE '%son';

-- _ matches single character
SELECT * FROM employees WHERE first_name LIKE 'J_hn';

-- Escaping wildcards
SELECT * FROM products WHERE name LIKE '100\% off%';
```

### IS NULL / IS NOT NULL

```sql
SELECT * FROM employees WHERE email IS NULL;
SELECT * FROM employees WHERE email IS NOT NULL;
```

---

## JOIN Operations

### Sample Schema

```sql
CREATE TABLE customers (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    city VARCHAR(50)
);

CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    customer_id INTEGER,
    amount NUMERIC(10,2),
    order_date DATE
);

CREATE TABLE products (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    price NUMERIC(10,2)
);

CREATE TABLE order_items (
    id INTEGER PRIMARY KEY,
    order_id INTEGER,
    product_id INTEGER,
    quantity INTEGER
);
```

### INNER JOIN

Returns only matching rows from both tables.

```sql
-- Basic INNER JOIN
SELECT c.name, o.amount, o.order_date
FROM customers c
INNER JOIN orders o ON c.id = o.customer_id;

-- With conditions
SELECT c.name, o.amount
FROM customers c
INNER JOIN orders o ON c.id = o.customer_id
WHERE o.amount > 100;

-- Multi-table JOIN
SELECT c.name, o.id, p.name, oi.quantity
FROM customers c
INNER JOIN orders o ON c.id = o.customer_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id;
```

### LEFT JOIN (LEFT OUTER JOIN)

Returns all rows from the left table and matching rows from the right.

```sql
-- All customers with their orders (including those with no orders)
SELECT c.name, o.id AS order_id, o.amount
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id;

-- Find customers with no orders
SELECT c.name
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id
WHERE o.id IS NULL;
```

### RIGHT JOIN (RIGHT OUTER JOIN)

Returns all rows from the right table and matching rows from the left.

```sql
-- All orders with their customers (including orphan orders)
SELECT o.id AS order_id, o.amount, c.name
FROM customers c
RIGHT JOIN orders o ON c.id = o.customer_id;

-- Convert RIGHT to LEFT
SELECT o.id AS order_id, o.amount, c.name
FROM orders o
LEFT JOIN customers c ON o.customer_id = c.id;
```

### FULL OUTER JOIN

Returns all rows from both tables, with NULLs for non-matching rows.

```sql
-- All customers and all orders
SELECT c.name, o.id AS order_id, o.amount
FROM customers c
FULL OUTER JOIN orders o ON c.id = o.customer_id;

-- Find unmatched records in either table
SELECT c.name, o.id AS order_id
FROM customers c
FULL OUTER JOIN orders o ON c.id = o.customer_id
WHERE c.id IS NULL OR o.id IS NULL;
```

### SELF JOIN

Joining a table to itself.

```sql
-- Find employees and their managers
CREATE TABLE employees (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    manager_id INTEGER
);

SELECT e.name AS employee, m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;
```

### CROSS JOIN

Cartesian product of two tables.

```sql
-- All combinations of customers and products
SELECT c.name, p.name, p.price
FROM customers c
CROSS JOIN products p;

-- Generating date series
SELECT d.date
FROM (
    SELECT generate_series('2024-01-01'::date, '2024-12-31'::date, '1 day') AS date
) d;
```

### NATURAL JOIN

Joins on all columns with the same name.

```sql
-- Automatically joins on matching column names
SELECT *
FROM customers
NATURAL JOIN orders;
```

### LATERAL JOIN

Allows subqueries to reference columns from preceding tables.

```sql
-- Get top 3 orders per customer
SELECT c.name, top_orders.*
FROM customers c
LEFT JOIN LATERAL (
    SELECT o.id, o.amount
    FROM orders o
    WHERE o.customer_id = c.id
    ORDER BY o.amount DESC
    LIMIT 3
) top_orders ON TRUE;
```

---

## GROUP BY and HAVING

### Basic GROUP BY

```sql
-- Count orders per customer
SELECT customer_id, COUNT(*) AS order_count
FROM orders
GROUP BY customer_id;

-- Multiple grouping columns
SELECT customer_id, order_date, COUNT(*) AS orders_per_day
FROM orders
GROUP BY customer_id, order_date;
```

### HAVING Clause

Filters groups (unlike WHERE which filters rows).

```sql
-- Customers with more than 5 orders
SELECT customer_id, COUNT(*) AS order_count
FROM orders
GROUP BY customer_id
HAVING COUNT(*) > 5;

-- Departments with average salary above 60000
SELECT department_id, AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 60000;
```

### GROUP BY with ROLLUP

```sql
-- Subtotals and grand total
SELECT
    COALESCE(department_id::TEXT, 'TOTAL') AS dept,
    SUM(salary)
FROM employees
GROUP BY ROLLUP(department_id);
```

### GROUP BY with CUBE

```sql
-- All possible grouping combinations
SELECT department_id, is_active, COUNT(*)
FROM employees
GROUP BY CUBE(department_id, is_active);
```

### GROUPING SETS

```sql
-- Custom grouping combinations
SELECT department_id, is_active, SUM(salary)
FROM employees
GROUP BY GROUPING SETS (
    (department_id, is_active),
    (department_id),
    (is_active),
    ()
);
```

---

## ORDER BY

```sql
-- Ascending (default)
SELECT * FROM employees ORDER BY last_name ASC;

-- Descending
SELECT * FROM employees ORDER BY salary DESC;

-- Multiple columns
SELECT * FROM employees
ORDER BY department_id ASC, salary DESC;

-- By expression
SELECT * FROM employees
ORDER BY LENGTH(last_name) DESC;

-- By column alias
SELECT first_name, salary * 12 AS annual_salary
FROM employees
ORDER BY annual_salary DESC;

-- NULLS first/last
SELECT * FROM employees
ORDER BY email NULLS FIRST;
```

---

## Aggregate Functions

### COUNT

```sql
SELECT COUNT(*) FROM employees;              -- All rows (including NULLs)
SELECT COUNT(email) FROM employees;          -- Non-NULL emails
SELECT COUNT(DISTINCT department_id) FROM employees;
```

### SUM

```sql
SELECT SUM(salary) FROM employees;
SELECT SUM(salary) FROM employees WHERE department_id = 1;
```

### AVG

```sql
SELECT AVG(salary) FROM employees;
SELECT ROUND(AVG(salary), 2) FROM employees;
```

### MIN and MAX

```sql
SELECT MIN(salary), MAX(salary) FROM employees;
SELECT MIN(hire_date), MAX(hire_date) FROM employees;
```

### Aggregate with Filter

```sql
-- Conditional aggregation
SELECT
    COUNT(*) AS total_employees,
    COUNT(*) FILTER (WHERE is_active) AS active_count,
    SUM(salary) FILTER (WHERE department_id = 1) AS dept1_salary
FROM employees;
```

### Window Aggregate Functions

```sql
SELECT
    first_name,
    salary,
    SUM(salary) OVER () AS total_salary,
    salary * 100.0 / SUM(salary) OVER () AS salary_pct
FROM employees;
```

---

## Subqueries

### Scalar Subquery

Returns a single value.

```sql
SELECT first_name, salary,
    salary - (SELECT AVG(salary) FROM employees) AS diff_from_avg
FROM employees;
```

### Row Subquery

Returns a single row.

```sql
SELECT * FROM employees
WHERE (department_id, salary) = (
    SELECT department_id, MAX(salary)
    FROM employees
    WHERE department_id = 1
);
```

### Table Subquery (Derived Table)

```sql
SELECT dept_avg.department_id, dept_avg.avg_salary
FROM (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
) dept_avg
WHERE dept_avg.avg_salary > 50000;
```

### Correlated Subquery

References outer query.

```sql
-- Employees earning more than their department average
SELECT e.first_name, e.salary, e.department_id
FROM employees e
WHERE e.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department_id = e.department_id
);
```

### EXISTS

```sql
-- Customers who have placed orders
SELECT c.name
FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o WHERE o.customer_id = c.id
);

-- Customers with no orders
SELECT c.name
FROM customers c
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.customer_id = c.id
);
```

### IN Subquery

```sql
-- Customers in NYC
SELECT * FROM customers
WHERE id IN (
    SELECT customer_id FROM orders
    WHERE city = 'NYC'
);
```

### ANY / ALL

```sql
-- Salary greater than any in department 1
SELECT * FROM employees
WHERE salary > ANY (SELECT salary FROM employees WHERE department_id = 1);

-- Salary greater than all in department 1
SELECT * FROM employees
WHERE salary > ALL (SELECT salary FROM employees WHERE department_id = 1);
```

### LATERAL Subquery

```sql
-- Correlated subquery in FROM clause
SELECT c.name, recent.order_id, recent.amount
FROM customers c
LEFT JOIN LATERAL (
    SELECT o.id AS order_id, o.amount
    FROM orders o
    WHERE o.customer_id = c.id
    ORDER BY o.order_date DESC
    LIMIT 1
) recent ON TRUE;
```

---

## UNION Operations

### UNION

Combines results, removing duplicates.

```sql
SELECT name FROM customers
UNION
SELECT name FROM suppliers;
```

### UNION ALL

Keeps all rows including duplicates.

```sql
SELECT name FROM customers
UNION ALL
SELECT name FROM suppliers;
```

### INTERSECT

Returns only rows present in both queries.

```sql
SELECT customer_id FROM orders_2023
INTERSECT
SELECT customer_id FROM orders_2024;
```

### EXCEPT / MINUS

Returns rows in the first query but not the second.

```sql
SELECT customer_id FROM orders_2023
EXCEPT
SELECT customer_id FROM orders_2024;
```

### Rules

```sql
-- All queries must have same number of columns
-- Corresponding columns must have compatible types
-- ORDER BY can only appear at the end
SELECT name, city FROM customers
UNION
SELECT name, city FROM suppliers
ORDER BY name;
```

---

## CASE Expressions

### Simple CASE

```sql
SELECT
    first_name,
    department_id,
    CASE department_id
        WHEN 1 THEN 'Engineering'
        WHEN 2 THEN 'Marketing'
        WHEN 3 THEN 'Sales'
        ELSE 'Other'
    END AS department_name
FROM employees;
```

### Searched CASE

```sql
SELECT
    first_name,
    salary,
    CASE
        WHEN salary >= 100000 THEN 'Executive'
        WHEN salary >= 75000 THEN 'Senior'
        WHEN salary >= 50000 THEN 'Mid-level'
        ELSE 'Junior'
    END AS salary_band
FROM employees;
```

### CASE in WHERE

```sql
SELECT * FROM employees
WHERE CASE
    WHEN department_id = 1 THEN salary > 60000
    ELSE salary > 40000
END;
```

### CASE in ORDER BY

```sql
SELECT * FROM employees
ORDER BY
    CASE
        WHEN is_active THEN 0
        ELSE 1
    END,
    last_name;
```

### CASE with Aggregation

```sql
SELECT
    department_id,
    COUNT(CASE WHEN salary > 75000 THEN 1 END) AS high_earners,
    COUNT(CASE WHEN salary <= 75000 THEN 1 END) AS others
FROM employees
GROUP BY department_id;
```

### COALESCE

```sql
-- Returns first non-NULL value
SELECT
    COALESCE(nickname, first_name, 'Unknown') AS display_name
FROM employees;
```

### NULLIF

```sql
-- Returns NULL if both values are equal
SELECT
    salary,
    bonus,
    salary / NULLIF(bonus, 0) AS salary_to_bonus_ratio
FROM employees;
```

---

## NULL Handling

### NULL Comparison

```sql
-- NULL cannot be compared with = or !=
SELECT * FROM employees WHERE email = NULL;    -- WRONG
SELECT * FROM employees WHERE email IS NULL;   -- CORRECT
SELECT * FROM employees WHERE email IS NOT NULL;
```

### NULL in Arithmetic

```sql
-- NULL propagates through arithmetic
SELECT 5 + NULL;          -- NULL
SELECT 5 * NULL;          -- NULL
SELECT NULL + NULL;       -- NULL
```

### NULL in Aggregates

```sql
-- COUNT(*) includes NULLs
SELECT COUNT(*) FROM employees;          -- All rows
SELECT COUNT(email) FROM employees;     -- Non-NULL emails only

-- Other aggregates ignore NULLs
SELECT AVG(bonus) FROM employees;       -- Ignores NULL bonuses
```

### NULL in Comparisons

```sql
-- NULL in comparisons returns NULL (treated as false)
SELECT * FROM employees WHERE salary > NULL;  -- Returns nothing
```

### COALESCE Function

```sql
SELECT
    first_name,
    COALESCE(bonus, 0) AS bonus,
    COALESCE(salary, 0) + COALESCE(bonus, 0) AS total_comp
FROM employees;
```

### NULLS FIRST / NULLS LAST

```sql
SELECT * FROM employees ORDER BY email NULLS FIRST;
SELECT * FROM employees ORDER BY email NULLS LAST;
```

### NULL Handling Patterns

```sql
-- Replace NULL with default
SELECT COALESCE(region, 'N/A') FROM customers;

-- Conditional logic with NULL
SELECT
    CASE
        WHEN bonus IS NULL THEN 'No bonus'
        WHEN bonus = 0 THEN 'Zero bonus'
        ELSE 'Has bonus'
    END AS bonus_status
FROM employees;

-- Filter with NULL awareness
SELECT * FROM employees
WHERE COALESCE(email, '') LIKE '%@company.com';
```

---

## Data Manipulation

### INSERT

```sql
-- Single row
INSERT INTO employees (first_name, last_name, email, salary)
VALUES ('John', 'Doe', 'john@example.com', 50000);

-- Multiple rows
INSERT INTO employees (first_name, last_name, email, salary)
VALUES
    ('Jane', 'Smith', 'jane@example.com', 55000),
    ('Bob', 'Johnson', 'bob@example.com', 60000);

-- From SELECT
INSERT INTO employee_archive
SELECT * FROM employees WHERE is_active = FALSE;

-- ON CONFLICT (PostgreSQL)
INSERT INTO employees (email, first_name, last_name)
VALUES ('john@example.com', 'John', 'Doe')
ON CONFLICT (email)
DO UPDATE SET first_name = EXCLUDED.first_name;
```

### UPDATE

```sql
-- Single column
UPDATE employees SET salary = 55000 WHERE id = 1;

-- Multiple columns
UPDATE employees
SET salary = salary * 1.1,
    updated_at = CURRENT_TIMESTAMP
WHERE department_id = 1;

-- From another table
UPDATE employees e
SET salary = e.salary * 1.1
FROM departments d
WHERE e.department_id = d.id
  AND d.name = 'Engineering';
```

### DELETE

```sql
-- Single row
DELETE FROM employees WHERE id = 1;

-- Multiple rows
DELETE FROM employees WHERE is_active = FALSE;

-- With subquery
DELETE FROM employees
WHERE department_id IN (
    SELECT id FROM departments WHERE is_deleted = TRUE
);

-- Truncate (faster, resets auto-increment)
TRUNCATE TABLE employees;
```

---

## Data Definition

### CREATE TABLE

```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    total NUMERIC(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### ALTER TABLE

```sql
-- Add column
ALTER TABLE employees ADD COLUMN phone VARCHAR(20);

-- Drop column
ALTER TABLE employees DROP COLUMN phone;

-- Rename column
ALTER TABLE employees RENAME COLUMN name TO full_name;

-- Modify column type
ALTER TABLE employees ALTER COLUMN salary TYPE NUMERIC(12,2);

-- Add constraint
ALTER TABLE employees ADD CONSTRAINT unique_email UNIQUE (email);
```

### DROP TABLE

```sql
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS employees CASCADE;
```

### Indexes

```sql
-- Create index
CREATE INDEX idx_employees_email ON employees (email);

-- Composite index
CREATE INDEX idx_employees_dept_salary ON employees (department_id, salary);

-- Unique index
CREATE UNIQUE INDEX idx_employees_email_unique ON employees (email);

-- Partial index
CREATE INDEX idx_active_employees ON employees (department_id)
WHERE is_active = TRUE;
```

### Views

```sql
CREATE VIEW active_employees AS
SELECT id, first_name, last_name, email, department_id
FROM employees
WHERE is_active = TRUE;

-- Use the view
SELECT * FROM active_employees WHERE department_id = 1;
```

---

## Common Patterns

### Pagination

```sql
-- LIMIT/OFFSET (PostgreSQL, MySQL)
SELECT * FROM employees
ORDER BY id
LIMIT 20 OFFSET 40;

-- OFFSET-FETCH (SQL Server)
SELECT * FROM employees
ORDER BY id
OFFSET 40 ROWS FETCH NEXT 20 ROWS ONLY;
```

### Ranking

```sql
SELECT
    first_name,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank,
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
FROM employees;
```

### Running Total

```sql
SELECT
    order_date,
    amount,
    SUM(amount) OVER (ORDER BY order_date) AS running_total
FROM orders;
```

### Deduplication

```sql
-- Keep first occurrence
DELETE FROM employees
WHERE ctid NOT IN (
    SELECT MIN(ctid)
    FROM employees
    GROUP BY email
);

-- Using ROW_NUMBER
WITH ranked AS (
    SELECT *,
        ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
    FROM employees
)
DELETE FROM ranked WHERE rn > 1;
```

### Pivot (Manual)

```sql
SELECT
    department_id,
    COUNT(*) FILTER (WHERE is_active = TRUE) AS active_count,
    COUNT(*) FILTER (WHERE is_active = FALSE) AS inactive_count
FROM employees
GROUP BY department_id;
```

---

## Best Practices

1. **Always use explicit JOIN syntax** instead of comma joins
2. **Alias tables** for readability in complex queries
3. **Use parameterized queries** to prevent SQL injection
4. **Avoid SELECT *** in production code
5. **Index columns** used in WHERE, JOIN, and ORDER BY
6. **Use IS NULL** instead of `= NULL`
7. **Prefer EXISTS over IN** for subqueries returning many rows
8. **Use COALESCE** for NULL handling
9. **Write CTEs** for complex multi-step queries
10. **Test queries** with EXPLAIN before running on large tables
