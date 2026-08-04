# SQL Server Fundamentals

## Comprehensive Guide to Microsoft SQL Server

SQL Server is a relational database management system by Microsoft. This guide covers T-SQL basics, schemas, and core features.

---

## Table of Contents

1. [SQL Server Architecture](#sql-server-architecture)
2. [T-SQL Basics](#tsql-basics)
3. [Schemas](#schemas)
4. [Data Types](#data-types)
5. [Indexes](#indexes)
6. [Best Practices](#best-practices)

---

## SQL Server Architecture

### Components

```
+------------------+
| SQL Server       |
| Engine           |
+------------------+
| Query Processor  |
| Storage Engine   |
| Buffer Pool      |
| Transaction Log  |
+------------------+
```

### Editions

```
Enterprise    - Full features, high availability
Standard      - Core features
Express       - Free, limited
Developer     - Free, dev/test only
Azure SQL     - Cloud-based
```

---

## T-SQL Basics

### DDL Statements

```sql
-- Create database
CREATE DATABASE MyDatabase;
GO

USE MyDatabase;
GO

-- Create schema
CREATE SCHEMA hr;
GO

-- Create table
CREATE TABLE hr.employees (
    employee_id INT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    email NVARCHAR(100) UNIQUE,
    hire_date DATE DEFAULT GETDATE(),
    salary DECIMAL(10,2),
    department_id INT,
    created_at DATETIME2 DEFAULT SYSDATETIME()
);
GO

-- Create index
CREATE INDEX idx_employees_email ON hr.employees (email);
GO

-- Create view
CREATE VIEW hr.active_employees AS
SELECT * FROM hr.employees WHERE deleted_at IS NULL;
GO
```

### DML Statements

```sql
-- INSERT
INSERT INTO hr.employees (first_name, last_name, email, salary)
VALUES ('John', 'Doe', 'john@example.com', 75000.00);

-- INSERT multiple rows
INSERT INTO hr.employees (first_name, last_name, email, salary)
VALUES
    ('Jane', 'Smith', 'jane@example.com', 85000.00),
    ('Bob', 'Johnson', 'bob@example.com', 65000.00);

-- SELECT
SELECT * FROM hr.employees WHERE salary > 70000;

-- UPDATE
UPDATE hr.employees
SET salary = salary * 1.1
WHERE department_id = 10;

-- DELETE
DELETE FROM hr.employees WHERE employee_id = 100;
```

### Stored Procedures

```sql
CREATE PROCEDURE hr.usp_get_employee
    @employee_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT employee_id, first_name, last_name, email, salary
    FROM hr.employees
    WHERE employee_id = @employee_id;
END;
GO

-- Execute
EXEC hr.usp_get_employee @employee_id = 1;
GO
```

### Functions

```sql
CREATE FUNCTION hr.fn_calculate_bonus (
    @salary DECIMAL(10,2),
    @percentage DECIMAL(5,2)
)
RETURNS DECIMAL(10,2)
AS
BEGIN
    RETURN @salary * (@percentage / 100);
END;
GO

-- Usage
SELECT first_name, salary, hr.fn_calculate_bonus(salary, 10) AS bonus
FROM hr.employees;
```

---

## Schemas

### Creating Schemas

```sql
-- Create schema
CREATE SCHEMA hr;
GO

CREATE SCHEMA finance;
GO

-- Move table to schema
ALTER SCHEMA hr TRANSFER dbo.employees;
GO

-- Create table in schema
CREATE TABLE finance.transactions (
    transaction_id INT IDENTITY PRIMARY KEY,
    amount DECIMAL(10,2),
    transaction_date DATETIME2 DEFAULT SYSDATETIME()
);
GO
```

### Schema Security

```sql
-- Grant permissions on schema
GRANT SELECT, INSERT, UPDATE ON SCHEMA::hr TO app_user;
GO

-- Deny permissions
DENY DELETE ON SCHEMA::hr TO app_user;
GO
```

---

## Data Types

### Numeric Types

```sql
INT             -- 4 bytes, -2^31 to 2^31-1
BIGINT          -- 8 bytes, -2^63 to 2^63-1
SMALLINT        -- 2 bytes, -32768 to 32767
TINYINT         -- 1 byte, 0 to 255
DECIMAL(10,2)   -- Exact precision
FLOAT           -- Approximate
MONEY           -- Currency
```

### String Types

```sql
CHAR(10)        -- Fixed length
VARCHAR(100)    -- Variable length
NVARCHAR(100)   -- Unicode variable length
TEXT            -- Large text (deprecated)
VARCHAR(MAX)    -- Large variable length
```

### Date/Time Types

```sql
DATE            -- '2024-01-15'
TIME            -- '14:30:00'
DATETIME        -- '2024-01-15 14:30:00.000'
DATETIME2       -- '2024-01-15 14:30:00.0000000'
SMALLDATETIME   -- '2024-01-15 14:30:00'
DATETIMEOFFSET  -- With timezone
```

### Other Types

```sql
BIT             -- Boolean (0, 1, NULL)
UNIQUEIDENTIFIER -- GUID
XML             -- XML data
VARBINARY(MAX)  -- Binary data
```

---

## Indexes

### Index Types

```sql
-- Clustered index (one per table)
CREATE CLUSTERED INDEX idx_emp_id ON hr.employees (employee_id);

-- Non-clustered index
CREATE NONCLUSTERED INDEX idx_emp_email ON hr.employees (email);

-- Unique index
CREATE UNIQUE INDEX idx_emp_email_unique ON hr.employees (email);

-- Composite index
CREATE INDEX idx_emp_dept_salary ON hr.employees (department_id, salary);

-- Filtered index
CREATE INDEX idx_emp_active ON hr.employees (department_id)
WHERE deleted_at IS NULL;

-- Included columns (covering index)
CREATE INDEX idx_emp_covering ON hr.employees (department_id)
INCLUDE (first_name, last_name, salary);
```

---

## Best Practices

### 1. Use Schemas for Organization

```sql
CREATE SCHEMA hr;
CREATE SCHEMA finance;
CREATE SCHEMA audit;

-- Organize objects
CREATE TABLE hr.employees (...);
CREATE TABLE finance.transactions (...);
CREATE TABLE audit.logs (...);
```

### 2. Use Appropriate Data Types

```sql
-- Good
DECIMAL(10,2)   -- For money
VARCHAR(100)    -- For names
DATE            -- For dates

-- Bad
FLOAT           -- For money (precision issues)
NVARCHAR(MAX)   -- For short strings
DATETIME        -- Use DATETIME2 instead
```

### 3. Create Proper Indexes

```sql
-- Analyze query patterns
SET STATISTICS IO ON;

-- Create indexes based on WHERE clauses
CREATE INDEX idx_emp_dept ON hr.employees (department_id);
CREATE INDEX idx_emp_salary ON hr.employees (salary);
```

### 4. Use Stored Procedures

```sql
-- Encapsulate logic
CREATE PROCEDURE hr.usp_process_order
    @order_id INT
AS
BEGIN
    -- Business logic
END;
```

### 5. Implement Proper Security

```sql
-- Create login
CREATE LOGIN app_user WITH PASSWORD = 'StrongPassword123!';

-- Create user
CREATE USER app_user FOR LOGIN app_user;

-- Grant permissions
GRANT SELECT, INSERT, UPDATE ON hr.employees TO app_user;
```

---

## Further Reading

- [SQL Server Documentation](https://docs.microsoft.com/en-us/sql/sql-server/)
- [T-SQL Reference](https://docs.microsoft.com/en-us/sql/t-sql/language-elements/language-elements-sql-server)
- [SQL Server Best Practices](https://docs.microsoft.com/en-us/sql/database-engine/configure-windows/server-memory-server-configuration-options)
