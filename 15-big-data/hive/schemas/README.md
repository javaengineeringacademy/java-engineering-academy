# Hive Schema Design

Hive schema design involves creating efficient table structures, partitioning strategies, bucketing approaches, and data organization patterns for optimal query performance and data management.

## Table of Contents

1. [Schema Design Principles](#schema-design-principles)
2. [Table Design](#table-design)
3. [Partitioning Strategies](#partitioning-strategies)
4. [Bucketing Strategies](#bucketing-strategies)
5. [File Format Selection](#file-format-selection)
6. [Data Organization](#data-organization)
7. [Schema Evolution](#schema-evolution)
8. [Data Lifecycle](#data-lifecycle)
9. [Best Practices](#best-practices)
10. [Common Patterns](#common-patterns)

---

## Schema Design Principles

### Design Goals

```
Schema Design Goals:
┌─────────────────────────────────────────────────────────────┐
│                      Query Performance                       │
│         - Fast queries                                       │
│         - Efficient joins                                    │
│         - Minimal data scanned                              │
├─────────────────────────────────────────────────────────────┤
│                      Data Management                         │
│         - Easy data loading                                 │
│         - Data lifecycle management                         │
│         - Schema evolution                                  │
├─────────────────────────────────────────────────────────────┤
│                      Storage Efficiency                      │
│         - Optimal file formats                              │
│         - Compression                                       │
│         - Partitioning                                      │
└─────────────────────────────────────────────────────────────┘
```

### Design Principles

| Principle | Description |
|-----------|-------------|
| **Normalize for writes, Denormalize for reads** | Design based on query patterns |
| **Partition by query patterns** | Partition on columns used in WHERE |
| **Bucket for joins** | Bucket on join keys |
| **Choose right file format** | ORC for Hive, Parquet for Spark |
| **Use appropriate compression** | Snappy for general, Gzip for archive |

---

## Table Design

### Star Schema

```sql
-- Fact table
CREATE TABLE fact_sales (
    sale_id BIGINT,
    product_id INT,
    customer_id INT,
    store_id INT,
    date_id INT,
    quantity INT,
    amount DOUBLE,
    discount DOUBLE
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC;

-- Dimension tables
CREATE TABLE dim_product (
    product_id INT,
    product_name STRING,
    category STRING,
    subcategory STRING,
    brand STRING
)
STORED AS ORC;

CREATE TABLE dim_customer (
    customer_id INT,
    customer_name STRING,
    email STRING,
    segment STRING
)
STORED AS ORC;

CREATE TABLE dim_store (
    store_id INT,
    store_name STRING,
    city STRING,
    state STRING,
    country STRING
)
STORED AS ORC;

CREATE TABLE dim_date (
    date_id INT,
    date_value DATE,
    year INT,
    month INT,
    day INT,
    quarter INT,
    week_of_year INT
)
STORED AS ORC;
```

### Snowflake Schema

```sql
-- Fact table
CREATE TABLE fact_orders (
    order_id BIGINT,
    product_id INT,
    customer_id INT,
    order_date_id INT,
    quantity INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC;

-- Dimension tables
CREATE TABLE dim_product (
    product_id INT,
    product_name STRING,
    category_id INT
)
STORED AS ORC;

CREATE TABLE dim_category (
    category_id INT,
    category_name STRING,
    department_id INT
)
STORED AS ORC;

CREATE TABLE dim_department (
    department_id INT,
    department_name STRING
)
STORED AS ORC;
```

### Wide Table

```sql
-- Wide denormalized table
CREATE TABLE wide_orders (
    order_id BIGINT,
    order_date DATE,
    customer_id INT,
    customer_name STRING,
    customer_email STRING,
    product_id INT,
    product_name STRING,
    category STRING,
    quantity INT,
    amount DOUBLE,
    discount DOUBLE,
    tax DOUBLE,
    total DOUBLE
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS ORC;
```

---

## Partitioning Strategies

### Time-Based Partitioning

```sql
-- Daily partitioning
CREATE TABLE events (
    event_id BIGINT,
    user_id INT,
    event_type STRING,
    event_data STRING
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS ORC;

-- Monthly partitioning
CREATE TABLE transactions (
    transaction_id BIGINT,
    customer_id INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC;

-- Hourly partitioning
CREATE TABLE logs (
    log_id BIGINT,
    message STRING,
    level STRING
)
PARTITIONED BY (year INT, month INT, day INT, hour INT)
STORED AS ORC;
```

### Category-Based Partitioning

```sql
-- Partition by region
CREATE TABLE sales (
    sale_id BIGINT,
    amount DOUBLE
)
PARTITIONED BY (region STRING)
STORED AS ORC;

-- Partition by department
CREATE TABLE employees (
    employee_id INT,
    name STRING,
    salary DOUBLE
)
PARTITIONED BY (department STRING)
STORED AS ORC;
```

### Partition Pruning

```sql
-- Partition pruning in queries
SELECT * FROM events 
WHERE year = 2024 AND month = 1 AND day = 1;

-- Partition pruning with functions
SELECT * FROM events 
WHERE year = YEAR(CURRENT_DATE()) AND month = MONTH(CURRENT_DATE());

-- Partition pruning with subqueries
SELECT * FROM events 
WHERE year IN (SELECT DISTINCT year FROM events WHERE year >= 2023);
```

### Partition Management

```sql
-- Add partition
ALTER TABLE events ADD PARTITION (year=2024, month=2, day=1);

-- Drop partition
ALTER TABLE events DROP PARTITION (year=2024, month=1, day=1);

-- Rename partition
ALTER TABLE events PARTITION (year=2024, month=1, day=1)
RENAME TO PARTITION (year=2024, month=2, day=1);

-- Show partitions
SHOW PARTITIONS events;
```

---

## Bucketing Strategies

### Basic Bucketing

```sql
-- Bucket by ID
CREATE TABLE users_bucketed (
    user_id INT,
    user_name STRING,
    email STRING
)
CLUSTERED BY (user_id) INTO 100 BUCKETS
STORED AS ORC;

-- Bucket by multiple columns
CREATE TABLE orders_bucketed (
    order_id INT,
    customer_id INT,
    product_id INT,
    amount DOUBLE
)
CLUSTERED BY (customer_id, product_id) INTO 100 BUCKETS
STORED AS ORC;
```

### Bucketing for Joins

```sql
-- Bucketed tables for join optimization
CREATE TABLE users_bucketed (
    user_id INT,
    user_name STRING
)
CLUSTERED BY (user_id) INTO 100 BUCKETS
STORED AS ORC;

CREATE TABLE orders_bucketed (
    order_id INT,
    user_id INT,
    amount DOUBLE
)
CLUSTERED BY (user_id) INTO 100 BUCKETS
STORED AS ORC;

-- Join benefits from bucketing
SELECT u.user_name, o.amount
FROM users_bucketed u
JOIN orders_bucketed o ON u.user_id = o.user_id;
```

### Sorted Bucketing

```sql
-- Sorted bucketing
CREATE TABLE users_sorted (
    user_id INT,
    user_name STRING
)
CLUSTERED BY (user_id) INTO 100 BUCKETS
SORTED BY (user_id) ASC
STORED AS ORC;

-- Benefits for range queries
SELECT * FROM users_sorted WHERE user_id BETWEEN 100 AND 200;
```

### Bucket Configuration

```sql
-- Enable bucket optimization
SET hive.optimize.bucketmapjoin=true;
SET hive.optimize.bucketmapjoin.sortedmerge=true;

-- Number of buckets
-- Choose based on data size
-- 100 buckets for 1TB data
-- 1000 buckets for 10TB data

-- Bucket pruning
SELECT * FROM users_bucketed WHERE user_id = 123;
```

---

## File Format Selection

### ORC (Optimized Row Columnar)

```sql
-- Best for Hive
CREATE TABLE employees_orc (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC;

-- ORC features
-- Built-in indexes
-- Columnar storage
-- ACID transactions
-- Compression
```

### Parquet

```sql
-- Best for Spark
CREATE TABLE employees_parquet (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS PARQUET;

-- Parquet features
-- Columnar storage
-- Compression
-- Schema evolution
-- Cross-platform support
```

### Avro

```sql
-- Best for schema evolution
CREATE TABLE employees_avro (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS AVRO;

-- Avro features
-- Row-based storage
-- Schema evolution
-- Cross-platform support
-- Compact format
```

### TextFile

```sql
-- Best for raw data
CREATE TABLE employees_text (
    id INT,
    name STRING,
    salary DOUBLE
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;

-- TextFile features
-- Human readable
-- Easy to load
-- No compression
-- No optimization
```

### Format Comparison

| Feature | ORC | Parquet | Avro | TextFile |
|---------|-----|---------|------|----------|
| **Storage** | Columnar | Columnar | Row | Row |
| **Compression** | High | High | Medium | Low |
| **Indexing** | Built-in | None | None | None |
| **ACID** | Yes | No | No | No |
| **Schema Evolution** | Limited | Yes | Yes | No |
| **Hive** | Best | Good | Good | Basic |
| **Spark** | Good | Best | Good | Basic |

---

## Data Organization

### Directory Structure

```
Data Directory Structure:
/data/
├── raw/
│   ├── 2024/
│   │   ├── 01/
│   │   │   └── data.csv
│   │   └── 02/
│   │       └── data.csv
├── processed/
│   ├── 2024/
│   │   ├── 01/
│   │   │   └── data.orc
│   │   └── 02/
│   │       └── data.orc
└── archive/
    └── 2023/
        └── data.orc
```

### Data Layout

```sql
-- Partitioned layout
/data/processed/year=2024/month=01/day=01/
/data/processed/year=2024/month=01/day=02/
/data/processed/year=2024/month=02/day=01/

-- Bucketed layout
/data/processed/bucket_00000/
/data/processed/bucket_00001/
/data/processed/bucket_00002/
```

### File Naming

```
File Naming Conventions:
├── Simple: data.csv
├── Partitioned: year=2024/month=01/data.orc
├── Bucketed: bucket_00000/data.orc
└── Timestamped: data_20240101.orc
```

---

## Schema Evolution

### Adding Columns

```sql
-- Add column
ALTER TABLE employees ADD COLUMNS (email STRING);

-- Add multiple columns
ALTER TABLE employees ADD COLUMNS (email STRING, phone STRING);

-- Add column with default value
ALTER TABLE employees ADD COLUMNS (status STRING DEFAULT 'active');
```

### Modifying Columns

```sql
-- Change column name
ALTER TABLE employees CHANGE COLUMN name employee_name STRING;

-- Change column type
ALTER TABLE employees CHANGE COLUMN salary salary DECIMAL(10,2);

-- Change column comment
ALTER TABLE employees CHANGE COLUMN name name STRING COMMENT 'Employee name';
```

### Dropping Columns

```sql
-- Drop column
ALTER TABLE employees DROP COLUMN phone;

-- Drop multiple columns
ALTER TABLE employees DROP COLUMN phone, DROP COLUMN address;
```

### Schema Evolution with ORC

```sql
-- ORC schema evolution
-- ORC supports schema evolution
-- New columns can be added
-- Existing columns can be modified

-- Create table with schema
CREATE TABLE employees_v1 (
    id INT,
    name STRING
)
STORED AS ORC;

-- Add column
ALTER TABLE employees_v1 ADD COLUMNS (email STRING);

-- Query works with new schema
SELECT * FROM employees_v1;
```

---

## Data Lifecycle

### Data Retention

```sql
-- Create partitioned table
CREATE TABLE events (
    event_id BIGINT,
    event_type STRING
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS ORC;

-- Retention policy: keep 90 days
-- Drop old partitions
ALTER TABLE events DROP PARTITION (year=2023, month=1, day=1);

-- Archive old data
INSERT OVERWRITE TABLE events_archive
SELECT * FROM events WHERE year = 2023 AND month = 1;

-- Drop archived partition
ALTER TABLE events DROP PARTITION (year=2023, month=1);
```

### Data Archival

```sql
-- Archive to cold storage
-- Move to slower storage tier
-- Compress with higher ratio

-- Archive table
CREATE TABLE events_archive (
    event_id BIGINT,
    event_type STRING
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="GZIP");

-- Archive old data
INSERT INTO events_archive
SELECT * FROM events WHERE year < 2023;
```

### Data Cleanup

```sql
-- Drop old partitions
ALTER TABLE events DROP PARTITION (year=2020);

-- Truncate table
TRUNCATE TABLE events;

-- Drop table
DROP TABLE events;
```

---

## Best Practices

### 1. Partition Design

```sql
-- Partition by time
CREATE TABLE events (
    event_id BIGINT,
    event_type STRING
)
PARTITIONED BY (year INT, month INT, day INT);

-- Avoid over-partitioning
-- 1000 partitions max recommended
-- Each partition should have enough data

-- Use meaningful partition names
PARTITIONED BY (year INT, month INT, day INT);
```

### 2. Bucketing Design

```sql
-- Bucket by join key
CREATE TABLE users_bucketed (
    user_id INT,
    user_name STRING
)
CLUSTERED BY (user_id) INTO 100 BUCKETS;

-- Choose bucket count based on data size
-- 100 buckets for 1TB
-- 1000 buckets for 10TB

-- Use sorted bucketing for range queries
CLUSTERED BY (user_id) INTO 100 BUCKETS
SORTED BY (user_id) ASC;
```

### 3. File Format Selection

```sql
-- Use ORC for Hive
STORED AS ORC;

-- Use Parquet for Spark
STORED AS PARQUET;

-- Use Avro for schema evolution
STORED AS AVRO;

-- Use TextFile for raw data
STORED AS TEXTFILE;
```

### 4. Compression Configuration

```sql
-- Use Snappy for general use
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Use Gzip for archive
TBLPROPERTIES ("orc.compress"="GZIP");

-- Use Zlib for high compression
TBLPROPERTIES ("orc.compress"="ZLIB");
```

### 5. Statistics and Optimization

```sql
-- Compute statistics
ANALYZE TABLE employees COMPUTE STATISTICS;
ANALYZE TABLE employees COMPUTE STATISTICS FOR COLUMNS;

-- Enable auto statistics
SET hive.stats.autogather=true;

-- Use vectorization
SET hive.vectorized.execution.enabled=true;
```

---

## Common Patterns

### Pattern 1: Time-Series Data

```sql
-- Time-series table
CREATE TABLE metrics (
    metric_id BIGINT,
    metric_name STRING,
    metric_value DOUBLE
)
PARTITIONED BY (year INT, month INT, day INT, hour INT)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Query pattern
SELECT * FROM metrics 
WHERE year = 2024 AND month = 1 AND day = 1 AND hour = 12;
```

### Pattern 2: User Activity

```sql
-- User activity table
CREATE TABLE user_activity (
    activity_id BIGINT,
    user_id INT,
    activity_type STRING,
    activity_data STRING
)
PARTITIONED BY (year INT, month INT, day INT)
CLUSTERED BY (user_id) INTO 100 BUCKETS
STORED AS ORC;

-- Query pattern
SELECT user_id, COUNT(*) as activity_count
FROM user_activity
WHERE year = 2024 AND month = 1
GROUP BY user_id;
```

### Pattern 3: E-commerce

```sql
-- Product table
CREATE TABLE products (
    product_id INT,
    product_name STRING,
    category STRING,
    price DOUBLE
)
STORED AS ORC;

-- Orders table
CREATE TABLE orders (
    order_id BIGINT,
    product_id INT,
    customer_id INT,
    quantity INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT)
CLUSTERED BY (customer_id) INTO 100 BUCKETS
STORED AS ORC;

-- Query pattern
SELECT p.category, SUM(o.amount) as total_sales
FROM orders o
JOIN products p ON o.product_id = p.product_id
WHERE o.year = 2024 AND o.month = 1
GROUP BY p.category;
```

### Pattern 4: Data Lake

```sql
-- Raw data layer
CREATE TABLE raw_events (
    event_id BIGINT,
    event_type STRING,
    event_data STRING
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS AVRO;

-- Processed data layer
CREATE TABLE processed_events (
    event_id BIGINT,
    user_id INT,
    event_type STRING,
    event_data STRING
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Aggregated data layer
CREATE TABLE aggregated_events (
    user_id INT,
    event_type STRING,
    event_count BIGINT
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");
```

---

## Conclusion

Hive schema design involves:

- **Table design** for query patterns
- **Partitioning** for data organization
- **Bucketing** for join optimization
- **File formats** for storage efficiency
- **Data lifecycle** for management

Key takeaways:

1. **Partition by query patterns** - Use columns in WHERE clauses
2. **Bucket by join keys** - Optimize joins
3. **Choose right file format** - ORC for Hive, Parquet for Spark
4. **Use compression** - Snappy for general, Gzip for archive
5. **Compute statistics** - Enable query optimization

Effective schema design is essential for achieving good performance and manageability in Hive.