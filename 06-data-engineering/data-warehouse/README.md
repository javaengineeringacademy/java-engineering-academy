# Data Warehouse

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Data Warehouse Architecture](#data-warehouse-architecture)
- [Dimensional Modeling](#dimensional-modeling)
- [ETL vs ELT](#elt-vs-etl)
- [Data Warehouse Technologies](#data-warehouse-technologies)
- [Slowly Changing Dimensions](#slowly-changing-dimensions)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

A data warehouse is a centralized repository for storing structured, processed
data optimized for analytical querying and reporting. It serves as the single
source of truth for business intelligence and decision support systems.

### Key Characteristics

- **Subject-oriented**: Organized around business subjects
- **Integrated**: Consistent data from multiple sources
- **Time-variant**: Historical data for analysis
- **Non-volatile**: Data is read-only after loading

### Data Warehouse vs Data Lake

| Feature | Data Warehouse | Data Lake |
|---------|---------------|-----------|
| Data Types | Structured only | All types |
| Schema | Schema on write | Schema on read |
| Processing | ETL | ELT |
| Cost | Higher | Lower |
| Performance | Optimized | Variable |
| Users | Business Analysts | Data Engineers |

### When to Use Data Warehouse

- Business intelligence and reporting
- Ad-hoc analytics and querying
- Historical data analysis
- Regulatory compliance reporting
- Executive dashboards

---

## Core Concepts

### Data Warehouse Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Data Warehouse Architecture               │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Data Sources                                                │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐      │
│  │   ERP    │ │   CRM    │ │  Files   │ │   APIs   │      │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘      │
│                           │                                  │
│  ETL/ELT Process          │                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Extract → Transform → Load                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                  │
│  Data Warehouse                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐            │   │
│  │  │  Staging │ │  Core    │ │  Marts   │            │   │
│  │  │  Area    │ │  Data    │ │(Department)│            │   │
│  │  └──────────┘ └──────────┘ └──────────┘            │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                  │
│  Data Access Layer                                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐            │   │
│  │  │   BI     │ │ Reporting│ │ Analytics│            │   │
│  │  │  Tools   │ │  Tools   │ │  Tools   │            │   │
│  │  └──────────┘ └──────────┘ └──────────┘            │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Data Warehouse Layers

```python
# Staging Layer
# - Raw data from source systems
# - No transformations
# - Temporary storage

# Core Data Layer
# - Cleaned, validated data
# - Conformed dimensions
# - Integrated data

# Data Marts
# - Department-specific subsets
# - Optimized for specific use cases
# - Aggregated data

# Summary Layer
# - Pre-aggregated data
# - Materialized views
# - Performance optimization
```

---

## Dimensional Modeling

### Star Schema

```sql
-- Fact table
CREATE TABLE fact_sales (
    sale_id INT PRIMARY KEY,
    date_key INT,
    product_key INT,
    customer_key INT,
    store_key INT,
    quantity INT,
    amount DECIMAL(10,2),
    cost DECIMAL(10,2),
    profit DECIMAL(10,2)
);

-- Dimension tables
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year INT,
    quarter INT,
    month INT,
    day INT,
    day_of_week VARCHAR(10),
    is_holiday BOOLEAN
);

CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_id VARCHAR(50),
    product_name VARCHAR(100),
    category VARCHAR(50),
    subcategory VARCHAR(50),
    brand VARCHAR(50)
);

CREATE TABLE dim_customer (
    customer_key INT PRIMARY KEY,
    customer_id VARCHAR(50),
    customer_name VARCHAR(100),
    email VARCHAR(100),
    segment VARCHAR(50),
    region VARCHAR(50)
);
```

### Snowflake Schema

```sql
-- Normalized dimension tables
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year_key INT,
    month_key INT,
    day_key INT
);

CREATE TABLE dim_year (
    year_key INT PRIMARY KEY,
    year INT,
    decade INT,
    century INT
);

CREATE TABLE dim_month (
    month_key INT PRIMARY KEY,
    month INT,
    month_name VARCHAR(20),
    quarter INT
);
```

### Galaxy Schema (Fact Constellation)

```sql
-- Multiple fact tables sharing dimensions
CREATE TABLE fact_sales (
    sale_id INT PRIMARY KEY,
    date_key INT,
    product_key INT,
    customer_key INT,
    quantity INT,
    amount DECIMAL(10,2)
);

CREATE TABLE fact_inventory (
    inventory_id INT PRIMARY KEY,
    date_key INT,
    product_key INT,
    store_key INT,
    quantity_on_hand INT,
    quantity_on_order INT
);

CREATE TABLE fact_budget (
    budget_id INT PRIMARY KEY,
    date_key INT,
    department_key INT,
    budget_amount DECIMAL(10,2),
    actual_amount DECIMAL(10,2)
);
```

### Dimension Types

```python
# Conformed Dimension
# - Same structure across all data marts
# - Consistent business definitions
# - Shared across fact tables

# Role-Playing Dimension
# - Same dimension used multiple times in a fact table
# - Example: order_date, ship_date, delivery_date

# Junk Dimension
# - Low-cardinality flags and indicators
# - Example: is_returned, is_discounted, payment_type

# Degenerate Dimension
# - Dimension key in fact table without separate dimension
# - Example: order_number, transaction_id

# Slowly Changing Dimension (SCD)
# - Handle historical changes in dimension attributes
# - Types: SCD Type 1, 2, 3
```

---

## ETL vs ELT

### ETL (Extract, Transform, Load)

```python
# ETL Process
# 1. Extract data from source
# 2. Transform data in staging area
# 3. Load transformed data to warehouse

# Example ETL with Spark
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("ETL").getOrCreate()

# Extract
source_df = spark.read.format("jdbc") \
    .option("url", "jdbc:postgresql://source/db") \
    .option("dbtable", "orders") \
    .load()

# Transform
transformed_df = source_df \
    .dropDuplicates() \
    .na.drop() \
    .withColumn("order_date", to_date(col("order_date"))) \
    .withColumn("amount", col("amount").cast(DoubleType()))

# Load
transformed_df.write \
    .mode("overwrite") \
    .parquet("s3://warehouse/fact_orders/")
```

### ELT (Extract, Load, Transform)

```python
# ELT Process
# 1. Extract data from source
# 2. Load raw data to warehouse
# 3. Transform data in warehouse

# Example ELT with Spark SQL
# Load raw data
raw_df = spark.read.parquet("s3://raw/orders/")
raw_df.createOrReplaceTempView("raw_orders")

# Transform in warehouse
spark.sql("""
    CREATE OR REPLACE TABLE fact_orders AS
    SELECT
        order_id,
        customer_id,
        order_date,
        amount,
        ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY order_date) as order_seq
    FROM raw_orders
    WHERE amount > 0
""")
```

### ETL vs ELT Comparison

| Feature | ETL | ELT |
|---------|-----|-----|
| Transform Location | Staging area | Target warehouse |
| Latency | Higher | Lower |
| Flexibility | Less flexible | More flexible |
| Cost | Higher (separate infra) | Lower (use warehouse) |
| Data Volume | Better for small data | Better for large data |
| Complexity | More complex | Simpler |

---

## Data Warehouse Technologies

### Cloud Data Warehouses

```python
# Snowflake
import snowflake.connector

conn = snowflake.connector.connect(
    user="user",
    password="password",
    account="account",
    warehouse="warehouse",
    database="database",
    schema="schema"
)

# Execute query
cursor = conn.cursor()
cursor.execute("SELECT * FROM fact_sales")
results = cursor.fetchall()

# BigQuery
from google.cloud import bigquery

client = bigquery.Client()
query = "SELECT * FROM dataset.fact_sales"
results = client.query(query).to_dataframe()

# Redshift
import psycopg2

conn = psycopg2.connect(
    host="redshift-cluster",
    port="5439",
    dbname="dev",
    user="user",
    password="password"
)

cursor = conn.cursor()
cursor.execute("SELECT * FROM fact_sales")
results = cursor.fetchall()
```

### On-Premises Data Warehouses

```python
# Teradata
import teradata

udaExec = teradata.UdaExec(appName="DW", version="1.0")
session = udaExec.connect(method="odbc", system="teradata")

# Oracle Exadata
import cx_Oracle

conn = cx_Oracle.connect("user/password@host:port/service")
cursor = conn.cursor()
cursor.execute("SELECT * FROM fact_sales")
```

### Open Source Solutions

```python
# Apache Hive
from pyhive import hive

conn = hive.connect(host="hive-server", port="10000", database="default")
cursor = conn.cursor()
cursor.execute("SELECT * FROM fact_sales")
results = cursor.fetchall()

# ClickHouse
import clickhouse_connect

client = clickhouse_connect.get_client(host='localhost', port=8123)
results = client.query("SELECT * FROM fact_sales")
```

---

## Slowly Changing Dimensions

### SCD Type 1 (Overwrite)

```sql
-- Overwrite existing records
MERGE INTO dim_customer AS target
USING new_customers AS source
ON target.customer_id = source.customer_id
WHEN MATCHED THEN UPDATE SET
    customer_name = source.customer_name,
    email = source.email,
    segment = source.segment
WHEN NOT MATCHED THEN INSERT VALUES
    (source.customer_id, source.customer_name, source.email, source.segment);
```

### SCD Type 2 (Historical)

```sql
-- Add new record, expire old record
-- Add validity columns
ALTER TABLE dim_customer ADD COLUMN valid_from DATE;
ALTER TABLE dim_customer ADD COLUMN valid_to DATE;
ALTER TABLE dim_customer ADD COLUMN is_current BOOLEAN;

-- Insert new version
INSERT INTO dim_customer (
    customer_id, customer_name, email, segment,
    valid_from, valid_to, is_current
)
VALUES (
    'C001', 'Alice Smith', 'alice@example.com', 'Premium',
    '2024-01-01', NULL, TRUE
);

-- When customer changes
-- 1. Expire current record
UPDATE dim_customer
SET valid_to = CURRENT_DATE - 1, is_current = FALSE
WHERE customer_id = 'C001' AND is_current = TRUE;

-- 2. Insert new version
INSERT INTO dim_customer (
    customer_id, customer_name, email, segment,
    valid_from, valid_to, is_current
)
VALUES (
    'C001', 'Alice Johnson', 'alice.j@example.com', 'Premium',
    CURRENT_DATE, NULL, TRUE
);
```

### SCD Type 3 (Limited History)

```sql
-- Add columns for previous values
ALTER TABLE dim_customer ADD COLUMN previous_email VARCHAR(100);
ALTER TABLE dim_customer ADD COLUMN previous_segment VARCHAR(50);
ALTER TABLE dim_customer ADD COLUMN change_date DATE;

-- Update with previous values
UPDATE dim_customer
SET previous_email = email,
    previous_segment = segment,
    email = 'new@example.com',
    segment = 'VIP',
    change_date = CURRENT_DATE
WHERE customer_id = 'C001';
```

### SCD Type 4 (History Table)

```sql
-- Main table with current data
CREATE TABLE dim_customer (
    customer_key INT PRIMARY KEY,
    customer_id VARCHAR(50),
    customer_name VARCHAR(100),
    email VARCHAR(100),
    segment VARCHAR(50)
);

-- History table
CREATE TABLE dim_customer_history (
    history_key INT PRIMARY KEY,
    customer_key INT,
    customer_name VARCHAR(100),
    email VARCHAR(100),
    segment VARCHAR(50),
    valid_from DATE,
    valid_to DATE,
    FOREIGN KEY (customer_key) REFERENCES dim_customer(customer_key)
);
```

---

## Performance Optimization

### Indexing

```sql
-- Create indexes for frequent queries
CREATE INDEX idx_fact_sales_date ON fact_sales(date_key);
CREATE INDEX idx_fact_sales_product ON fact_sales(product_key);
CREATE INDEX idx_fact_sales_customer ON fact_sales(customer_key);

-- Bitmap indexes for low-cardinality columns
CREATE BITMAP INDEX idx_fact_sales_region ON fact_sales(region);

-- Columnstore indexes for analytics
CREATE COLUMNSTORE INDEX idx_fact_sales ON fact_sales(
    date_key, product_key, customer_key, amount
);
```

### Partitioning

```sql
-- Range partitioning by date
CREATE TABLE fact_sales (
    sale_id INT,
    date_key INT,
    product_key INT,
    amount DECIMAL(10,2)
) PARTITION BY RANGE (date_key) (
    PARTITION p2023 VALUES LESS THAN (20240101),
    PARTITION p2024 VALUES LESS THAN (20250101),
    PARTITION p2025 VALUES LESS THAN (20260101)
);

-- Hash partitioning
CREATE TABLE fact_sales (
    sale_id INT,
    date_key INT,
    product_key INT,
    amount DECIMAL(10,2)
) PARTITION BY HASH (product_key) PARTITIONS 8;
```

### Materialized Views

```sql
-- Create materialized view for frequent aggregations
CREATE MATERIALIZED VIEW mv_sales_summary AS
SELECT
    date_key,
    product_key,
    SUM(amount) as total_sales,
    COUNT(*) as transaction_count
FROM fact_sales
GROUP BY date_key, product_key;

-- Refresh materialized view
REFRESH MATERIALIZED VIEW mv_sales_summary;

-- Auto-refresh on data changes
CREATE MATERIALIZED VIEW mv_sales_summary
AS SELECT ...
WITH DATA REFRESH INTERVAL 1 HOUR;
```

### Query Optimization

```python
# Use appropriate file formats
df.write.parquet("s3://data/", compression="snappy")

# Partition pruning
df = spark.read.parquet("s3://data/")
filtered_df = df.filter(col("date_key") == 20240101)

# Predicate pushdown
filtered_df = df.filter(col("amount") > 100)

# Column pruning
selected_df = df.select("date_key", "amount")

# Use vectorized execution
spark.conf.set("spark.sql.parquet.enableVectorizedReader", True)
```

### Caching

```python
# Cache frequently accessed data
df.cache()

# Use appropriate storage level
from pyspark import StorageLevel
df.persist(StorageLevel.MEMORY_AND_DISK)

# Monitor cache usage
print(df.storageLevel)
print(df.is_cached)
```

---

## Best Practices

### Data Modeling

1. **Use appropriate schema**: Star schema for simplicity, snowflake for normalization
2. **Conform dimensions**: Consistent dimensions across data marts
3. **Handle SCDs**: Use appropriate SCD type for each dimension
4. **Optimize for queries**: Design for common query patterns

### Data Quality

1. **Validate data**: Check for completeness, accuracy, consistency
2. **Track lineage**: Know where data came from and how it was transformed
3. **Monitor freshness**: Ensure data is up-to-date
4. **Implement contracts**: Define expected schemas and quality

### Performance

1. **Partition wisely**: By date for time-series data
2. **Use appropriate indexes**: For frequent query patterns
3. **Materialize aggregations**: For common aggregations
4. **Optimize queries**: Avoid full table scans

### Security

1. **Implement access control**: Role-based access control
2. **Encrypt sensitive data**: At rest and in transit
3. **Audit access**: Log all data access and modifications
4. **Mask PII**: Protect personally identifiable information

---

## References

- [Data Warehouse Toolkit](https://www.kimballgroup.com/data-warehouse-business-intelligence-resources/kimball-techniques/dimensional-modeling-techniques/)
- [Building a Data Warehouse](https://www.amazon.com/Building-Data-Warehouse-WH-Inmon/dp/1118730596)
- [The Data Warehouse ETL Toolkit](https://www.amazon.com/Data-Warehouse-ETL-Toolkit-Techniques/dp/0764567578)
- [Dimensional Modeling](https://www.amazon.com/Star-Schema-Complete-Developers-Guide/dp/0471200247)
- [Data Warehouse Design](https://www.amazon.com/Data-Warehouse-Design-Modern-Techniques/dp/1491945969)
