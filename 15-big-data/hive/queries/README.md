# Hive Queries

HiveQL is the query language for Apache Hive, providing SQL-like syntax to query and manage large datasets. This guide covers basic to advanced queries, joins, window functions, UDFs, and optimization techniques.

## Table of Contents

1. [HiveQL Basics](#hiveql-basics)
2. [SELECT Queries](#select-queries)
3. [Joins](#joins)
4. [Window Functions](#window-functions)
5. [Subqueries](#subqueries)
6. [Aggregations](#aggregations)
7. [User-Defined Functions (UDFs)](#user-defined-functions-udfs)
8. [Complex Queries](#complex-queries)
9. [Performance Optimization](#performance-optimization)
10. [Best Practices](#best-practices)

---

## HiveQL Basics

### SELECT Statement

```sql
-- Simple select
SELECT * FROM employees;

-- Select specific columns
SELECT id, name, age FROM employees;

-- Select with aliases
SELECT id AS employee_id, name AS employee_name FROM employees;

-- Select distinct values
SELECT DISTINCT department FROM employees;

-- Select with limit
SELECT * FROM employees LIMIT 10;
```

### WHERE Clause

```sql
-- Comparison operators
SELECT * FROM employees WHERE age > 25;
SELECT * FROM employees WHERE salary >= 50000;
SELECT * FROM employees WHERE department = 'Engineering';
SELECT * FROM employees WHERE department != 'Sales';

-- Logical operators
SELECT * FROM employees WHERE age > 25 AND department = 'Engineering';
SELECT * FROM employees WHERE age > 25 OR salary > 100000;
SELECT * FROM employees WHERE NOT department = 'Sales';

-- BETWEEN operator
SELECT * FROM employees WHERE age BETWEEN 25 AND 35;

-- IN operator
SELECT * FROM employees WHERE department IN ('Engineering', 'Sales');

-- LIKE operator
SELECT * FROM employees WHERE name LIKE '%John%';
SELECT * FROM employees WHERE name LIKE 'J%';
SELECT * FROM employees WHERE name LIKE '%son';

-- IS NULL / IS NOT NULL
SELECT * FROM employees WHERE email IS NULL;
SELECT * FROM employees WHERE email IS NOT NULL;
```

### ORDER BY and GROUP BY

```sql
-- ORDER BY
SELECT * FROM employees ORDER BY name ASC;
SELECT * FROM employees ORDER BY salary DESC;
SELECT * FROM employees ORDER BY department ASC, salary DESC;

-- GROUP BY
SELECT department, COUNT(*) as count FROM employees GROUP BY department;
SELECT department, AVG(salary) as avg_salary FROM employees GROUP BY department;

-- HAVING clause
SELECT department, AVG(salary) as avg_salary 
FROM employees 
GROUP BY department 
HAVING AVG(salary) > 75000;
```

---

## SELECT Queries

### Basic Queries

```sql
-- Select all columns
SELECT * FROM employees;

-- Select specific columns
SELECT id, name, salary FROM employees;

-- Select with expressions
SELECT name, salary, salary * 0.1 as bonus FROM employees;

-- Select with string functions
SELECT UPPER(name), LOWER(email), LENGTH(name) FROM employees;

-- Select with date functions
SELECT name, YEAR(join_date), MONTH(join_date), DAY(join_date) FROM employees;
```

### Filtering Queries

```sql
-- Filter with conditions
SELECT * FROM employees WHERE age > 25;
SELECT * FROM employees WHERE salary BETWEEN 50000 AND 100000;
SELECT * FROM employees WHERE department IN ('Engineering', 'Product');

-- Filter with pattern matching
SELECT * FROM employees WHERE name LIKE '%John%';
SELECT * FROM employees WHERE email LIKE '%@company.com';

-- Filter with null checks
SELECT * FROM employees WHERE manager_id IS NULL;
SELECT * FROM employees WHERE email IS NOT NULL;
```

### Sorting Queries

```sql
-- Sort ascending
SELECT * FROM employees ORDER BY name ASC;

-- Sort descending
SELECT * FROM employees ORDER BY salary DESC;

-- Sort by multiple columns
SELECT * FROM employees ORDER BY department ASC, salary DESC;

-- Sort with null handling
SELECT * FROM employees ORDER BY email ASC NULLS FIRST;
SELECT * FROM employees ORDER BY email DESC NULLS LAST;
```

---

## Joins

### Inner Join

```sql
-- Inner join
SELECT e.name, d.department_name
FROM employees e
INNER JOIN departments d ON e.department_id = d.id;

-- Using column name
SELECT name, department_name
FROM employees
JOIN departments USING (department_id);

-- Multiple joins
SELECT e.name, d.department_name, p.project_name
FROM employees e
JOIN departments d ON e.department_id = d.id
JOIN projects p ON e.project_id = p.id;
```

### Outer Joins

```sql
-- Left outer join
SELECT e.name, d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id;

-- Right outer join
SELECT e.name, d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.id;

-- Full outer join
SELECT e.name, d.department_name
FROM employees e
FULL OUTER JOIN departments d ON e.department_id = d.id;
```

### Cross Join

```sql
-- Cross join (cartesian product)
SELECT e.name, p.project_name
FROM employees e
CROSS JOIN projects p;

-- Cross join with filter
SELECT e.name, p.project_name
FROM employees e
CROSS JOIN projects p
WHERE e.department_id = p.department_id;
```

### Self Join

```sql
-- Self join
SELECT e.name AS employee, m.name AS manager
FROM employees e
JOIN employees m ON e.manager_id = m.id;

-- Self join with hierarchy
SELECT e.name, m.name AS manager, mm.name AS grand_manager
FROM employees e
JOIN employees m ON e.manager_id = m.id
JOIN employees mm ON m.manager_id = mm.id;
```

### Map Join

```sql
-- Enable map join
SET hive.auto.convert.join=true;
SET hive.mapjoin.smalltable.filesize=25000000;

-- Map join hint
SELECT /*+ MAPJOIN(d) */ e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;
```

---

## Window Functions

### Basic Window Functions

```sql
-- Row number
SELECT name, department, salary,
       ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as row_num
FROM employees;

-- Rank
SELECT name, department, salary,
       RANK() OVER (PARTITION BY department ORDER BY salary DESC) as rank
FROM employees;

-- Dense rank
SELECT name, department, salary,
       DENSE_RANK() OVER (PARTITION BY department ORDER BY salary DESC) as dense_rank
FROM employees;
```

### Aggregate Window Functions

```sql
-- Sum over window
SELECT name, department, salary,
       SUM(salary) OVER (PARTITION BY department) as dept_total
FROM employees;

-- Average over window
SELECT name, department, salary,
       AVG(salary) OVER (PARTITION BY department) as dept_avg
FROM employees;

-- Count over window
SELECT name, department,
       COUNT(*) OVER (PARTITION BY department) as dept_count
FROM employees;

-- Min/Max over window
SELECT name, department, salary,
       MIN(salary) OVER (PARTITION BY department) as dept_min,
       MAX(salary) OVER (PARTITION BY department) as dept_max
FROM employees;
```

### Navigation Functions

```sql
-- Lead
SELECT name, salary,
       LEAD(salary, 1) OVER (ORDER BY salary) as next_salary
FROM employees;

-- Lag
SELECT name, salary,
       LAG(salary, 1) OVER (ORDER BY salary) as prev_salary
FROM employees;

-- First value
SELECT name, salary,
       FIRST_VALUE(salary) OVER (PARTITION BY department ORDER BY salary) as first_salary
FROM employees;

-- Last value
SELECT name, salary,
       LAST_VALUE(salary) OVER (PARTITION BY department ORDER BY salary 
                                ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as last_salary
FROM employees;
```

### Window Frame Specifications

```sql
-- Rows between
SELECT name, salary,
       SUM(salary) OVER (ORDER BY salary 
                         ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) as rolling_sum
FROM employees;

-- Range between
SELECT name, salary,
       AVG(salary) OVER (ORDER BY salary 
                         RANGE BETWEEN 1000 PRECEDING AND 1000 FOLLOWING) as moving_avg
FROM employees;

-- Unbounded
SELECT name, salary,
       SUM(salary) OVER (ORDER BY salary 
                         ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) as cumulative_sum
FROM employees;
```

---

## Subqueries

### Scalar Subqueries

```sql
-- Scalar subquery in SELECT
SELECT name, salary,
       salary - (SELECT AVG(salary) FROM employees) as diff_from_avg
FROM employees;

-- Scalar subquery in WHERE
SELECT * FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```

### IN Subqueries

```sql
-- IN subquery
SELECT * FROM employees
WHERE department_id IN (SELECT id FROM departments WHERE location = 'NYC');

-- NOT IN subquery
SELECT * FROM employees
WHERE department_id NOT IN (SELECT id FROM departments WHERE location = 'NYC');
```

### EXISTS Subqueries

```sql
-- EXISTS subquery
SELECT * FROM employees e
WHERE EXISTS (SELECT 1 FROM departments d WHERE d.id = e.department_id);

-- NOT EXISTS subquery
SELECT * FROM employees e
WHERE NOT EXISTS (SELECT 1 FROM departments d WHERE d.id = e.department_id);
```

### Correlated Subqueries

```sql
-- Correlated subquery
SELECT name, salary,
       (SELECT AVG(salary) FROM employees e2 WHERE e2.department_id = e1.department_id) as dept_avg
FROM employees e1;

-- Correlated subquery in WHERE
SELECT * FROM employees e1
WHERE salary > (SELECT AVG(salary) FROM employees e2 WHERE e2.department_id = e1.department_id);
```

### Lateral Views

```sql
-- Lateral view with explode
SELECT name, skill
FROM employees
LATERAL VIEW explode(skills) skills_table AS skill;

-- Lateral view with posexplode
SELECT name, pos, skill
FROM employees
LATERAL VIEW posexplode(skills) skills_table AS pos, skill;

-- Multiple lateral views
SELECT name, skill, hobby
FROM employees
LATERAL VIEW explode(skills) skills_table AS skill
LATERAL VIEW explode(hobbies) hobbies_table AS hobby;
```

---

## Aggregations

### Basic Aggregations

```sql
-- COUNT
SELECT COUNT(*) FROM employees;
SELECT COUNT(DISTINCT department) FROM employees;

-- SUM
SELECT SUM(salary) FROM employees;
SELECT department, SUM(salary) as total_salary FROM employees GROUP BY department;

-- AVG
SELECT AVG(salary) FROM employees;
SELECT department, AVG(salary) as avg_salary FROM employees GROUP BY department;

-- MIN/MAX
SELECT MIN(salary), MAX(salary) FROM employees;
SELECT department, MIN(salary), MAX(salary) FROM employees GROUP BY department;
```

### Grouping Sets

```sql
-- Grouping sets
SELECT department, year, SUM(salary)
FROM employees
GROUP BY department, year
GROUPING SETS (department, year, ());

-- Cube
SELECT department, year, SUM(salary)
FROM employees
GROUP BY department, year WITH CUBE;

-- Rollup
SELECT department, year, SUM(salary)
FROM employees
GROUP BY department, year WITH ROLLUP;
```

### Advanced Aggregations

```sql
-- Collect list
SELECT department, COLLECT_LIST(name) as employees
FROM employees
GROUP BY department;

-- Collect set
SELECT department, COLLECT_SET(department) as unique_departments
FROM employees;

-- Histogram
SELECT department,
       HISTOGRAM_NUMERIC(salary, 10) as salary_histogram
FROM employees
GROUP BY department;

-- Percentiles
SELECT department,
       PERCENTILE(salary, 0.5) as median_salary
FROM employees
GROUP BY department;
```

---

## User-Defined Functions (UDFs)

### Creating UDFs

```python
# Python UDF
@udf(returnType=StringType())
def format_name(name):
    return name.title()

# Register UDF
spark.udf.register("format_name", format_name, StringType())

# Use in query
SELECT format_name(name) FROM employees;
```

### Built-in UDFs

```sql
-- String functions
SELECT UPPER(name), LOWER(email), TRIM(name) FROM employees;
SELECT CONCAT(first_name, ' ', last_name) FROM employees;
SELECT SUBSTRING(name, 1, 3) FROM employees;
SELECT LENGTH(name) FROM employees;

-- Date functions
SELECT CURRENT_DATE(), CURRENT_TIMESTAMP() FROM employees;
SELECT YEAR(join_date), MONTH(join_date), DAY(join_date) FROM employees;
SELECT DATEDIFF(CURRENT_DATE(), join_date) FROM employees;

-- Mathematical functions
SELECT ROUND(salary, 2), CEIL(salary), FLOOR(salary) FROM employees;
SELECT ABS(salary - 50000) FROM employees;

-- Conditional functions
SELECT name,
       CASE WHEN salary > 100000 THEN 'High'
            WHEN salary > 50000 THEN 'Medium'
            ELSE 'Low' END as salary_level
FROM employees;

SELECT COALESCE(email, 'N/A') FROM employees;
SELECT NVL(email, 'N/A') FROM employees;
SELECT IF(salary > 50000, 'High', 'Low') FROM employees;
```

### Custom UDFs

```java
// Java UDF
public class FormatName extends UDF {
    public String evaluate(String name) {
        if (name == null) return null;
        return name.substring(0, 1).toUpperCase() + 
               name.substring(1).toLowerCase();
    }
}

// Register in Hive
CREATE FUNCTION format_name AS 'com.example.FormatName';
```

---

## Complex Queries

### PIVOT Query

```sql
-- Pivot rows to columns
SELECT department,
       SUM(CASE WHEN year = 2023 THEN salary ELSE 0 END) as salary_2023,
       SUM(CASE WHEN year = 2024 THEN salary ELSE 0 END) as salary_2024
FROM employees
GROUP BY department;
```

### UNPIVOT Query

```sql
-- Unpivot columns to rows
SELECT department, year, salary
FROM employees
UNPIVOT (
    salary FOR year IN (salary_2023, salary_2024)
) as unpvt;
```

### Recursive Queries

```sql
-- Recursive CTE for hierarchy
WITH RECURSIVE hierarchy AS (
    -- Base case
    SELECT id, name, manager_id, 1 as level
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive case
    SELECT e.id, e.name, e.manager_id, h.level + 1
    FROM employees e
    JOIN hierarchy h ON e.manager_id = h.id
)
SELECT * FROM hierarchy;
```

### Analytical Queries

```sql
-- Running total
SELECT name, salary,
       SUM(salary) OVER (ORDER BY salary 
                         ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) as running_total
FROM employees;

-- Moving average
SELECT name, salary,
       AVG(salary) OVER (ORDER BY salary 
                         ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) as moving_avg
FROM employees;

-- Year-over-year comparison
SELECT department, year, salary,
       salary - LAG(salary, 1) OVER (PARTITION BY department ORDER BY year) as yoy_change
FROM employees;
```

---

## Performance Optimization

### Query Optimization

```sql
-- Use partition pruning
SELECT * FROM sales WHERE year = 2024 AND month = 1;

-- Use column pruning
SELECT id, name FROM employees;

-- Use approximate queries
SELECT APPROX_COUNT_DISTINCT(user_id) FROM events;

-- Use MAPJOIN for small tables
SET hive.auto.convert.join=true;
SELECT /*+ MAPJOIN(d) */ e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;
```

### Execution Configuration

```sql
-- Enable vectorization
SET hive.vectorized.execution.enabled=true;
SET hive.vectorized.execution.reduce.enabled=true;

-- Use Tez engine
SET hive.execution.engine=tez;

-- Enable LLAP
SET hive.llap.enabled=true;

-- Configure parallelism
SET hive.exec.parallel=true;
SET hive.exec.parallel.thread.number=8;
```

### Statistics

```sql
-- Update statistics
ANALYZE TABLE employees COMPUTE STATISTICS;
ANALYZE TABLE employees COMPUTE STATISTICS FOR COLUMNS;

-- View statistics
DESC FORMATTED employees;
DESC EXTENDED employees;
```

---

## Best Practices

### 1. Query Writing

```sql
-- Use explicit column names
SELECT id, name, salary FROM employees;

-- Use appropriate JOIN syntax
SELECT e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;

-- Use WHERE for filtering
SELECT * FROM employees WHERE age > 25;

-- Use GROUP BY for aggregation
SELECT department, COUNT(*) FROM employees GROUP BY department;
```

### 2. Performance

```sql
-- Use partition pruning
SELECT * FROM sales WHERE year = 2024;

-- Use column pruning
SELECT id, name FROM employees;

-- Use map join for small tables
SET hive.auto.convert.join=true;

-- Use vectorization
SET hive.vectorized.execution.enabled=true;
```

### 3. Data Quality

```sql
-- Validate data
SELECT COUNT(*) FROM employees WHERE id IS NULL;

-- Check duplicates
SELECT id, COUNT(*) FROM employees GROUP BY id HAVING COUNT(*) > 1;

-- Validate constraints
SELECT * FROM employees WHERE salary < 0;
```

### 4. Readability

```sql
-- Use aliases
SELECT e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;

-- Use CTEs for complex queries
WITH dept_stats AS (
    SELECT department, AVG(salary) as avg_salary
    FROM employees
    GROUP BY department
)
SELECT * FROM dept_stats WHERE avg_salary > 75000;

-- Use comments
-- This query calculates department statistics
SELECT department, AVG(salary)
FROM employees
GROUP BY department;
```

---

## Common Patterns

### Pattern 1: Reporting Query

```sql
-- Monthly sales report
SELECT 
    YEAR(sale_date) as year,
    MONTH(sale_date) as month,
    department,
    SUM(amount) as total_sales,
    COUNT(*) as transaction_count,
    AVG(amount) as avg_sale
FROM sales
WHERE sale_date >= '2024-01-01'
GROUP BY YEAR(sale_date), MONTH(sale_date), department
ORDER BY year, month, department;
```

### Pattern 2: Data Quality Check

```sql
-- Data quality report
SELECT 
    COUNT(*) as total_records,
    COUNT(CASE WHEN id IS NULL THEN 1 END) as null_ids,
    COUNT(CASE WHEN name = '' THEN 1 END) as empty_names,
    COUNT(DISTINCT id) as unique_ids,
    MIN(create_date) as earliest_date,
    MAX(create_date) as latest_date
FROM employees;
```

### Pattern 3: Trend Analysis

```sql
-- Year-over-year comparison
SELECT 
    department,
    year,
    salary,
    LAG(salary, 1) OVER (PARTITION BY department ORDER BY year) as prev_year_salary,
    salary - LAG(salary, 1) OVER (PARTITION BY department ORDER BY year) as yoy_change,
    ROUND(
        (salary - LAG(salary, 1) OVER (PARTITION BY department ORDER BY year)) / 
        LAG(salary, 1) OVER (PARTITION BY department ORDER BY year) * 100, 2
    ) as yoy_pct_change
FROM employees;
```

### Pattern 4: Cohort Analysis

```sql
-- Cohort analysis
WITH cohorts AS (
    SELECT 
        user_id,
        DATE_FORMAT(join_date, 'yyyy-MM') as cohort_month
    FROM users
),
activity AS (
    SELECT 
        user_id,
        DATE_FORMAT(activity_date, 'yyyy-MM') as activity_month
    FROM user_activity
)
SELECT 
    c.cohort_month,
    a.activity_month,
    COUNT(DISTINCT c.user_id) as active_users
FROM cohorts c
JOIN activity a ON c.user_id = a.user_id
GROUP BY c.cohort_month, a.activity_month
ORDER BY c.cohort_month, a.activity_month;
```

---

## Conclusion

HiveQL provides:

- **SQL-like syntax** for querying Hadoop data
- **Window functions** for analytical queries
- **UDFs** for custom functionality
- **Optimization techniques** for performance

Key takeaways:

1. **Use partition pruning** for large tables
2. **Use column pruning** to reduce data scanned
3. **Use map joins** for small tables
4. **Use window functions** for analytics
5. **Optimize queries** with statistics and configuration

HiveQL is essential for SQL-based analytics on Hadoop, providing a familiar interface for data analysts and engineers.