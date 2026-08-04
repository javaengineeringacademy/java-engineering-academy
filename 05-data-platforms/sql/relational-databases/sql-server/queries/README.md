# SQL Server Queries

## Comprehensive Guide to T-SQL Queries

T-SQL is Microsoft's extension to SQL. This guide covers complex queries, window functions, CTEs, and PIVOT operations.

---

## Table of Contents

1. [Common Table Expressions](#ctes)
2. [Window Functions](#window-functions)
3. [PIVOT and UNPIVOT](#pivot-and-unpivot)
4. [Merge Statement](#merge-statement)
5. [Best Practices](#best-practices)

---

## CTEs

### Basic CTE

```sql
WITH EmployeeCTE AS (
    SELECT
        e.employee_id,
        e.first_name,
        e.last_name,
        d.department_name,
        e.salary
    FROM hr.employees e
    JOIN hr.departments d ON e.department_id = d.department_id
)
SELECT * FROM EmployeeCTE WHERE salary > 75000;
```

### Recursive CTE

```sql
WITH EmployeeHierarchy AS (
    -- Anchor member
    SELECT
        employee_id,
        first_name,
        manager_id,
        1 AS level
    FROM hr.employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive member
    SELECT
        e.employee_id,
        e.first_name,
        e.manager_id,
        eh.level + 1
    FROM hr.employees e
    JOIN EmployeeHierarchy eh ON e.manager_id = eh.employee_id
)
SELECT * FROM EmployeeHierarchy ORDER BY level;
```

---

## Window Functions

### Row Number

```sql
SELECT
    employee_id,
    first_name,
    salary,
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
FROM hr.employees;
```

### Rank and Dense Rank

```sql
SELECT
    employee_id,
    first_name,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank
FROM hr.employees;
```

### Partition By

```sql
SELECT
    employee_id,
    first_name,
    department_id,
    salary,
    ROW_NUMBER() OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
    ) AS dept_rank
FROM hr.employees;
```

### Aggregate Window Functions

```sql
SELECT
    employee_id,
    first_name,
    department_id,
    salary,
    SUM(salary) OVER (PARTITION BY department_id) AS dept_total,
    AVG(salary) OVER (PARTITION BY department_id) AS dept_avg,
    COUNT(*) OVER (PARTITION BY department_id) AS dept_count
FROM hr.employees;
```

### LAG and LEAD

```sql
SELECT
    employee_id,
    first_name,
    salary,
    LAG(salary, 1) OVER (ORDER BY hire_date) AS prev_salary,
    LEAD(salary, 1) OVER (ORDER BY hire_date) AS next_salary,
    salary - LAG(salary, 1) OVER (ORDER BY hire_date) AS salary_diff
FROM hr.employees;
```

### Running Total

```sql
SELECT
    employee_id,
    first_name,
    salary,
    SUM(salary) OVER (
        ORDER BY hire_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total
FROM hr.employees;
```

---

## PIVOT and UNPIVOT

### PIVOT

```sql
-- Rows to columns
SELECT *
FROM (
    SELECT
        department_id,
        salary
    FROM hr.employees
) AS source
PIVOT (
    AVG(salary)
    FOR department_id IN ([10], [20], [30], [40])
) AS pivot_table;
```

### Dynamic PIVOT

```sql
DECLARE @columns NVARCHAR(MAX);
DECLARE @sql NVARCHAR(MAX);

-- Get column list
SELECT @columns = STRING_AGG(
    QUOTENAME(department_id), ',')
FROM (SELECT DISTINCT department_id FROM hr.employees) AS dept;

-- Build dynamic query
SET @sql = N'
SELECT *
FROM (
    SELECT department_id, salary
    FROM hr.employees
) AS source
PIVOT (
    AVG(salary)
    FOR department_id IN (' + @columns + N')
) AS pivot_table;';

EXEC sp_executesql @sql;
```

### UNPIVOT

```sql
-- Columns to rows
SELECT employee_id, salary_type, amount
FROM (
    SELECT
        employee_id,
        base_salary,
        bonus,
        commission
    FROM hr.employees
) AS source
UNPIVOT (
    amount FOR salary_type IN (base_salary, bonus, commission)
) AS unpivot_table;
```

---

## Merge Statement

### Basic MERGE

```sql
MERGE INTO hr.employee_archive AS target
USING hr.employees AS source
ON target.employee_id = source.employee_id

WHEN MATCHED AND source.salary > target.salary THEN
    UPDATE SET
        target.salary = source.salary,
        target.last_updated = SYSDATETIME()

WHEN NOT MATCHED THEN
    INSERT (employee_id, first_name, last_name, salary)
    VALUES (source.employee_id, source.first_name, source.last_name, source.salary)

WHEN NOT MATCHED BY SOURCE THEN
    DELETE;
```

---

## Best Practices

### 1. Use CTEs for Readability

```sql
-- Good - Readable
WITH ActiveEmployees AS (
    SELECT * FROM hr.employees WHERE deleted_at IS NULL
),
DepartmentStats AS (
    SELECT
        department_id,
        COUNT(*) AS emp_count,
        AVG(salary) AS avg_salary
    FROM ActiveEmployees
    GROUP BY department_id
)
SELECT * FROM DepartmentStats WHERE emp_count > 5;
```

### 2. Use Window Functions Instead of Subqueries

```sql
-- Good - Window function
SELECT
    employee_id,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank
FROM hr.employees;

-- Bad - Subquery
SELECT
    e.*,
    (SELECT COUNT(*) FROM hr.employees e2
     WHERE e2.salary > e.salary) + 1 AS rank
FROM hr.employees e;
```

### 3. Use MERGE for Upserts

```sql
MERGE INTO target_table AS t
USING source_table AS s
ON t.id = s.id
WHEN MATCHED THEN
    UPDATE SET t.value = s.value
WHEN NOT MATCHED THEN
    INSERT (id, value) VALUES (s.id, s.value);
```

### 4. Use STRING_AGG for Aggregation

```sql
SELECT
    department_id,
    STRING_AGG(first_name, ', ') AS employees
FROM hr.employees
GROUP BY department_id;
```

### 5. Use OFFSET-FETCH for Pagination

```sql
SELECT * FROM hr.employees
ORDER BY employee_id
OFFSET 0 ROWS
FETCH NEXT 10 ROWS ONLY;
```

---

## Further Reading

- [T-SQL Reference](https://docs.microsoft.com/en-us/sql/t-sql/)
- [Window Functions](https://docs.microsoft.com/en-us/sql/t-sql/functions/window-functions-sql-server)
- [PIVOT and UNPIVOT](https://docs.microsoft.com/en-us/sql/t-sql/queries/from-using-pivot-and-unpivot)
