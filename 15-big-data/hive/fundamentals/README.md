# Hive Fundamentals

Apache Hive is a data warehouse infrastructure built on top of Hadoop for providing SQL-like queries (HiveQL) to query and manage large datasets stored in HDFS. It abstracts the complexity of MapReduce and provides a familiar SQL interface for data analysts and engineers.

## Table of Contents

1. [Hive Overview](#hive-overview)
2. [Architecture](#architecture)
3. [HiveQL Basics](#hiveql-basics)
4. [Managed vs External Tables](#managed-vs-external-tables)
5. [Partitions](#partitions)
6. [Table Types](#table-types)
7. [Data Types](#data-types)
8. [Loading Data](#loading-data)
9. [Best Practices](#best-practices)
10. [Common Patterns](#common-patterns)

---

## Hive Overview

### What is Hive?

Hive is a data warehouse software project built on top of Apache Hadoop for providing data summarization, query, and analysis. It supports processing structured data in Hadoop using a SQL-like language called HiveQL.

### Hive Features

- **SQL-like interface**: HiveQL for querying data
- **Schema on read**: Schema applied when reading data
- **Horizontal scalability**: Scales across Hadoop cluster
- **Extensible**: UDFs for custom functions
- **Integration**: Works with Hadoop ecosystem

### Hive vs Traditional RDBMS

| Feature | Hive | RDBMS |
|---------|------|-------|
| **Processing** | Batch | Real-time |
| **Schema** | Schema on read | Schema on write |
| **ACID** | Limited | Full ACID |
| **Latency** | High (minutes) | Low (seconds) |
| **Data Storage** | HDFS | Block storage |
| **Indexing** | Limited | Full indexing |

---

## Architecture

### Hive Architecture

```
Hive Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      User Interface                          │
│         (CLI, JDBC/ODBC, Web UI)                           │
├─────────────────────────────────────────────────────────────┤
│                      HiveQL Compiler                         │
│         (Parser → Semantic Analyzer → Optimizer)            │
├─────────────────────────────────────────────────────────────┤
│                      Execution Engine                        │
│         (MapReduce / Tez / Spark)                           │
├─────────────────────────────────────────────────────────────┤
│                      Metastore                               │
│         (Schema, Partitions, Statistics)                    │
├─────────────────────────────────────────────────────────────┤
│                      HDFS Storage                            │
│         (Data Files, Partitions)                            │
└─────────────────────────────────────────────────────────────┘
```

### Components

```python
# Metastore: Stores metadata
# - Table definitions
# - Column information
# - Partition information
# - Statistics

# Driver: Manages query lifecycle
# - Compiles HiveQL
# - Optimizes query
# - Executes query

# Execution Engine: Executes queries
# - MapReduce (default)
# - Tez (recommended)
# - Spark (alternative)
```

---

## HiveQL Basics

### Creating Tables

```sql
-- Create simple table
CREATE TABLE employees (
    id INT,
    name STRING,
    age INT,
    department STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;

-- Create table with more options
CREATE TABLE employees (
    id INT,
    name STRING,
    age INT,
    department STRING
)
PARTITIONED BY (year INT, month INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");
```

### Querying Data

```sql
-- Simple select
SELECT * FROM employees;

-- Filter data
SELECT * FROM employees WHERE age > 25;

-- Aggregate data
SELECT department, COUNT(*) as count
FROM employees
GROUP BY department;

-- Join tables
SELECT e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;
```

### Inserting Data

```sql
-- Insert values
INSERT INTO employees VALUES (1, 'Alice', 30, 'Engineering');

-- Insert from select
INSERT INTO employees_new
SELECT * FROM employees WHERE age > 25;

-- Insert overwrite
INSERT OVERWRITE TABLE employees_archive
SELECT * FROM employees WHERE year < 2020;
```

---

## Managed vs External Tables

### Managed Tables

```sql
-- Create managed table
CREATE TABLE managed_table (
    id INT,
    name STRING
);

-- Data is stored in Hive warehouse
-- Dropping table deletes data
DROP TABLE managed_table;
```

### External Tables

```sql
-- Create external table
CREATE EXTERNAL TABLE external_table (
    id INT,
    name STRING
)
LOCATION '/data/external/';

-- Data is stored outside Hive warehouse
-- Dropping table does NOT delete data
DROP TABLE external_table;
```

### Differences

| Feature | Managed Table | External Table |
|---------|---------------|----------------|
| **Data Location** | Hive warehouse | External location |
| **Drop Table** | Deletes data | Preserves data |
| **Data Control** | Hive manages | User manages |
| **Use Case** | Hive-managed data | Shared data |

---

## Partitions

### Partitioned Tables

```sql
-- Create partitioned table
CREATE TABLE sales (
    id INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT);

-- Load data into partition
LOAD DATA INPATH '/data/2024/01'
INTO TABLE sales PARTITION (year=2024, month=1);

-- Query specific partition
SELECT * FROM sales WHERE year=2024 AND month=1;

-- Show partitions
SHOW PARTITIONS sales;
```

### Partition Management

```sql
-- Add partition
ALTER TABLE sales ADD PARTITION (year=2024, month=2);

-- Drop partition
ALTER TABLE sales DROP PARTITION (year=2024, month=1);

-- Rename partition
ALTER TABLE sales PARTITION (year=2024, month=1)
RENAME TO PARTITION (year=2024, month=2);
```

### Dynamic Partitioning

```sql
-- Enable dynamic partitioning
SET hive.exec.dynamic.partition=true;
SET hive.exec.dynamic.partition.mode=nonstrict;

-- Insert with dynamic partitions
INSERT INTO TABLE sales_dynamic
PARTITION (year, month)
SELECT id, amount, year, month FROM sales_raw;
```

---

## Table Types

### Internal (Managed) Tables

```sql
-- Data stored in Hive warehouse
-- Data lifecycle managed by Hive
CREATE TABLE internal_table (
    id INT,
    name STRING
);
```

### External Tables

```sql
-- Data stored outside Hive
-- Hive only manages metadata
CREATE EXTERNAL TABLE external_table (
    id INT,
    name STRING
)
LOCATION '/data/external/';
```

### Temporary Tables

```sql
-- Temporary table for session
CREATE TEMPORARY TABLE temp_table AS
SELECT * FROM employees WHERE age > 25;

-- Dropped at end of session
```

### View Tables

```sql
-- Create view
CREATE VIEW high_salary_employees AS
SELECT * FROM employees WHERE salary > 100000;

-- Query view
SELECT * FROM high_salary_employees;

-- Drop view
DROP VIEW high_salary_employees;
```

---

## Data Types

### Primitive Types

```sql
-- Numeric types
TINYINT      -- 1 byte signed integer
SMALLINT     -- 2 bytes signed integer
INT          -- 4 bytes signed integer
BIGINT       -- 8 bytes signed integer
FLOAT        -- 4 bytes floating point
DOUBLE       -- 8 bytes floating point
DECIMAL      -- Arbitrary precision

-- String types
STRING       -- Variable length string
VARCHAR      -- Variable length with max length
CHAR         -- Fixed length string

-- Date/Time types
DATE         -- Date value
TIMESTAMP    -- Date and time value
INTERVAL     -- Time interval

-- Binary types
BINARY       -- Binary data

-- Boolean type
BOOLEAN      -- TRUE/FALSE
```

### Complex Types

```sql
-- Array
CREATE TABLE array_table (
    id INT,
    skills ARRAY<STRING>
);

-- Map
CREATE TABLE map_table (
    id INT,
    properties MAP<STRING, STRING>
);

-- Struct
CREATE TABLE struct_table (
    id INT,
    address STRUCT<street:STRING, city:STRING, state:STRING>
);

-- Nested types
CREATE TABLE nested_table (
    id INT,
    data STRUCT<
        name:STRING,
        scores:ARRAY<INT>,
        metadata:MAP<STRING, STRING>
    >
);
```

---

## Loading Data

### Loading from Files

```sql
-- Load from local filesystem
LOAD DATA LOCAL INPATH '/local/path/file.csv'
INTO TABLE employees;

-- Load from HDFS
LOAD DATA INPATH '/hdfs/path/file.csv'
INTO TABLE employees;

-- Load with overwrite
LOAD DATA INPATH '/hdfs/path/file.csv'
OVERWRITE INTO TABLE employees;
```

### Inserting from Queries

```sql
-- Insert from select
INSERT INTO TABLE employees_new
SELECT * FROM employees WHERE age > 25;

-- Insert overwrite
INSERT OVERWRITE TABLE employees_archive
SELECT * FROM employees WHERE year < 2020;

-- Multi-table insert
FROM employees
INSERT INTO TABLE high_salary
SELECT * WHERE salary > 100000
INSERT INTO TABLE low_salary
SELECT * WHERE salary < 50000;
```

### Loading External Data

```sql
-- Create external table for loading
CREATE EXTERNAL TABLE staging_employees (
    id INT,
    name STRING,
    age INT
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LOCATION '/data/staging/employees';

-- Load data into staging
-- Then insert into main table
INSERT INTO TABLE employees
SELECT * FROM staging_employees;
```

---

## Best Practices

### 1. Table Design

```sql
-- Use appropriate file formats
CREATE TABLE employees (
    id INT,
    name STRING,
    age INT
)
STORED AS ORC;  -- Use ORC for analytics

-- Use partitioning for large tables
CREATE TABLE sales (
    id INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT);

-- Use bucketing for join optimization
CREATE TABLE users (
    id INT,
    name STRING
)
CLUSTERED BY (id) INTO 100 BUCKETS;
```

### 2. Query Optimization

```sql
-- Use partition pruning
SELECT * FROM sales WHERE year=2024 AND month=1;

-- Use column pruning
SELECT id, name FROM employees;

-- Use approximate queries
SELECT COUNT(DISTINCT user_id) FROM events;

-- Use MAPJOIN for small tables
SET hive.auto.convert.join=true;
SELECT /*+ MAPJOIN(departments) */ *
FROM employees
JOIN departments ON employees.dept_id = departments.id;
```

### 3. Performance Tuning

```sql
-- Enable vectorization
SET hive.vectorized.execution.enabled=true;
SET hive.vectorized.execution.reduce.enabled=true;

-- Use Tez execution engine
SET hive.execution.engine=tez;

-- Enable LLAP
SET hive.llap.enabled=true;

-- Configure memory
SET hive.exec.parallel=true;
SET hive.exec.parallel.thread.number=8;
```

### 4. Data Management

```sql
-- Use appropriate compression
SET hive.exec.compress.output=true;
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

-- Use ORC with compression
CREATE TABLE employees (
    id INT,
    name STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Manage partitions
ALTER TABLE sales DROP PARTITION (year=2020);
ALTER TABLE sales ADD PARTITION (year=2025);
```

### 5. Security

```sql
-- Use authorization
GRANT SELECT ON TABLE employees TO ROLE analyst;

-- Use column-level security
GRANT SELECT (id, name) ON TABLE employees TO ROLE analyst;

-- Use row-level security
CREATE VIEW employees_view AS
SELECT * FROM employees
WHERE department = current_user();
```

---

## Common Patterns

### Pattern 1: Data Warehouse

```sql
-- Create data warehouse schema
CREATE TABLE dim_customers (
    customer_id INT,
    name STRING,
    email STRING
);

CREATE TABLE fact_sales (
    sale_id INT,
    customer_id INT,
    amount DOUBLE,
    sale_date DATE
)
PARTITIONED BY (year INT, month INT);

-- Load data
INSERT INTO dim_customers
SELECT DISTINCT customer_id, name, email FROM raw_sales;

INSERT INTO fact_sales
PARTITION (year, month)
SELECT sale_id, customer_id, amount, 
       YEAR(sale_date), MONTH(sale_date)
FROM raw_sales;
```

### Pattern 2: ETL Pipeline

```sql
-- Create staging tables
CREATE EXTERNAL TABLE staging_data (
    id INT,
    data STRING
)
LOCATION '/data/staging/';

-- Transform and load
INSERT OVERWRITE TABLE main_table
SELECT 
    id,
    TRANSFORM(data) AS processed_data
FROM staging_data;

-- Archive old data
INSERT OVERWRITE TABLE archive_table
SELECT * FROM main_table WHERE date < DATE_SUB(CURRENT_DATE, 90);

-- Clean up
DELETE FROM main_table WHERE date < DATE_SUB(CURRENT_DATE, 90);
```

### Pattern 3: Analytics

```sql
-- Create analytics views
CREATE VIEW daily_sales AS
SELECT 
    sale_date,
    SUM(amount) as total_sales,
    COUNT(*) as transaction_count
FROM sales
GROUP BY sale_date;

-- Create summary tables
CREATE TABLE monthly_summary
PARTITIONED BY (year INT, month INT)
AS
SELECT 
    YEAR(sale_date) as year,
    MONTH(sale_date) as month,
    SUM(amount) as total_sales,
    COUNT(*) as transaction_count
FROM sales
GROUP BY YEAR(sale_date), MONTH(sale_date);
```

### Pattern 4: Data Quality

```sql
-- Create data quality checks
CREATE VIEW data_quality AS
SELECT 
    COUNT(*) as total_records,
    COUNT(CASE WHEN id IS NULL THEN 1 END) as null_ids,
    COUNT(CASE WHEN name = '' THEN 1 END) as empty_names,
    COUNT(DISTINCT id) as unique_ids
FROM employees;

-- Run quality checks
SELECT * FROM data_quality;

-- Create alerts
CREATE VIEW quality_alerts AS
SELECT 
    'HIGH_NULL_RATE' as alert_type,
    COUNT(CASE WHEN id IS NULL THEN 1 END) / COUNT(*) as null_rate
FROM employees
HAVING COUNT(CASE WHEN id IS NULL THEN 1 END) / COUNT(*) > 0.1;
```

---

## Conclusion

Hive provides:

- **SQL-like interface** for querying Hadoop data
- **Schema on read** for flexible data management
- **Scalability** across Hadoop clusters
- **Integration** with Hadoop ecosystem

Key takeaways:

1. **Use appropriate table types** (managed vs external)
2. **Partition large tables** for query performance
3. **Choose right file formats** (ORC, Parquet)
4. **Optimize queries** with partition pruning
5. **Manage data lifecycle** with proper retention

Hive is essential for SQL-based analytics on Hadoop, providing a familiar interface for data analysts and engineers.