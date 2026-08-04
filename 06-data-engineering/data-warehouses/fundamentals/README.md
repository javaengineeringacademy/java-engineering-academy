# Data Warehouse Fundamentals

## Overview

A data warehouse is a centralized repository designed for analytical reporting, structured queries, and data analysis. It stores current and historical data from multiple sources, optimized for complex queries and business intelligence.

## Table of Contents

- [Architecture](#architecture)
- [Key Concepts](#key-concepts)
- [Data Modeling](#data-modeling)
- [ETL Processes](#etl-processes)
- [Query Optimization](#query-optimization)
- [Cloud Data Warehouses](#cloud-data-warehouses)
- [Best Practices](#best-practices)

## Architecture

### Traditional Data Warehouse

```
┌─────────────────────────────────────────────────────────────┐
│                    DATA WAREHOUSE                            │
├─────────────────────────────────────────────────────────────┤
│  Presentation Layer (BI Tools, Dashboards, Reports)         │
├─────────────────────────────────────────────────────────────┤
│  OLAP Cubes (Multidimensional Analysis)                     │
├─────────────────────────────────────────────────────────────┤
│  Data Marts (Department-Specific Views)                     │
├─────────────────────────────────────────────────────────────┤
│  Enterprise Data Warehouse (Integrated Data)                │
├─────────────────────────────────────────────────────────────┤
│  Staging Area (Data Cleansing, Integration)                 │
├─────────────────────────────────────────────────────────────┤
│  Data Sources (Operational Systems, External Data)          │
└─────────────────────────────────────────────────────────────┘
```

### Modern Cloud Warehouse

```
┌─────────────────────────────────────────────────────────────┐
│  Compute Layer (Query Execution, Processing)                │
│  Snowflake │ BigQuery │ Redshift │ Databricks               │
├─────────────────────────────────────────────────────────────┤
│  Storage Layer (Columnar, Compressed)                       │
│  S3 │ ADLS │ GCS │ Cloud Storage                           │
├─────────────────────────────────────────────────────────────┤
│  Metadata Layer (Schema, Lineage, Access)                   │
└─────────────────────────────────────────────────────────────┘
```

## Key Concepts

### OLTP vs OLAP

| Aspect | OLTP | OLAP |
|--------|------|------|
| Purpose | Transaction processing | Analytical processing |
| Queries | Short, simple | Complex, aggregations |
| Data | Current, operational | Historical, analytical |
| Users | Front-end applications | Analysts, BI tools |
| Design | Normalized (3NF) | Denormalized (Star/Snowflake) |
| Performance | Write-optimized | Read-optimized |

### Dimensional Modeling

```sql
-- Star Schema Example
-- Fact Table: Sales
CREATE TABLE fact_sales (
    sale_key BIGINT PRIMARY KEY,
    date_key INT REFERENCES dim_date(date_key),
    product_key INT REFERENCES dim_product(product_key),
    customer_key INT REFERENCES dim_customer(customer_key),
    store_key INT REFERENCES dim_store(store_key),
    quantity_sold INT,
    unit_price DECIMAL(10,2),
    discount_amount DECIMAL(10,2),
    total_amount DECIMAL(10,2)
);

-- Dimension Tables
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year INT,
    quarter INT,
    month INT,
    day_of_week VARCHAR(10),
    is_holiday BOOLEAN
);

CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_id VARCHAR(50),
    product_name VARCHAR(200),
    category VARCHAR(100),
    brand VARCHAR(100),
    unit_cost DECIMAL(10,2)
);
```

### Slowly Changing Dimensions (SCD)

```sql
-- SCD Type 1: Overwrite
UPDATE dim_customer
SET address = 'New Address'
WHERE customer_id = 123;

-- SCD Type 2: Add new row
INSERT INTO dim_customer (
    customer_id, name, address, effective_date, expiry_date, is_current
)
SELECT 
    customer_id, name, new_address, 
    CURRENT_DATE, '9999-12-31', TRUE
FROM staging_customers
WHERE address != (SELECT address FROM dim_customer WHERE customer_id = 123);

-- SCD Type 3: Add new column
ALTER TABLE dim_customer ADD COLUMN previous_address VARCHAR(200);
UPDATE dim_customer 
SET previous_address = address, address = 'New Address'
WHERE customer_id = 123;
```

## Data Modeling

### Third Normal Form (3NF)

```sql
-- 3NF for operational systems
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT REFERENCES customers(customer_id),
    order_date DATE
);

CREATE TABLE order_items (
    item_id INT PRIMARY KEY,
    order_id INT REFERENCES orders(order_id),
    product_id INT REFERENCES products(product_id),
    quantity INT,
    price DECIMAL(10,2)
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    name VARCHAR(100),
    city_id INT REFERENCES cities(city_id)
);
```

### Star Schema

```sql
-- Star Schema for analytics
CREATE TABLE fact_orders (
    order_key BIGINT PRIMARY KEY,
    date_key INT,
    customer_key INT,
    product_key INT,
    quantity INT,
    amount DECIMAL(10,2)
);

CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    date DATE,
    year INT,
    month INT,
    day INT
);

CREATE TABLE dim_customer (
    customer_key INT PRIMARY KEY,
    customer_name VARCHAR(100),
    segment VARCHAR(50),
    city VARCHAR(100)
);
```

### Snowflake Schema

```sql
-- Snowflake Schema (normalized dimensions)
CREATE TABLE fact_sales (
    sale_key BIGINT PRIMARY KEY,
    date_key INT,
    product_key INT,
    store_key INT,
    amount DECIMAL(10,2)
);

CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_name VARCHAR(200),
    category_key INT
);

CREATE TABLE dim_category (
    category_key INT PRIMARY KEY,
    category_name VARCHAR(100),
    department_key INT
);

CREATE TABLE dim_department (
    department_key INT PRIMARY KEY,
    department_name VARCHAR(100)
);
```

## ETL Processes

### Extract

```python
# Extract from multiple sources
import pandas as pd
from sqlalchemy import create_engine

# Source systems
sources = {
    "erp": create_engine("postgresql://host/erp"),
    "crm": create_engine("mysql://host/crm"),
    "web": create_engine("sqlite:///web.db")
}

# Extract data
def extract_data(source_name, query, incremental=False):
    engine = sources[source_name]
    
    if incremental:
        last_sync = get_last_sync(source_name)
        query += f" WHERE updated_at > '{last_sync}'"
    
    return pd.read_sql(query, engine)
```

### Transform

```python
# Transform data
def transform_orders(raw_orders, customers):
    # Join with customers
    orders = raw_orders.merge(
        customers[['customer_id', 'segment', 'region']],
        on='customer_id',
        how='left'
    )
    
    # Apply business rules
    orders['total_amount'] = orders['quantity'] * orders['unit_price']
    orders['discount_amount'] = orders.apply(
        lambda x: x['total_amount'] * 0.1 if x['segment'] == 'premium' else 0,
        axis=1
    )
    orders['net_amount'] = orders['total_amount'] - orders['discount_amount']
    
    # Add date dimensions
    orders['order_date'] = pd.to_datetime(orders['order_date'])
    orders['year'] = orders['order_date'].dt.year
    orders['month'] = orders['order_date'].dt.month
    orders['quarter'] = orders['order_date'].dt.quarter
    
    return orders
```

### Load

```python
# Load to data warehouse
def load_to_warehouse(df, table_name, mode='append'):
    warehouse_engine = create_engine('snowflake://account/db/schema')
    
    # Truncate and reload for full load
    if mode == 'full':
        with warehouse_engine.connect() as conn:
            conn.execute(f"TRUNCATE TABLE {table_name}")
    
    # Load data
    df.to_sql(
        table_name,
        warehouse_engine,
        if_exists='append',
        index=False,
        chunksize=10000
    )
```

## Query Optimization

### Indexing

```sql
-- Create indexes for common query patterns
CREATE INDEX idx_orders_date ON fact_sales(date_key);
CREATE INDEX idx_orders_customer ON fact_sales(customer_key);
CREATE INDEX idx_orders_product ON fact_sales(product_key);

-- Composite index
CREATE INDEX idx_orders_date_customer 
ON fact_sales(date_key, customer_key);
```

### Partitioning

```sql
-- Partition fact table by date
ALTER TABLE fact_sales
PARTITION BY (date_key);

-- Partition by range
CREATE TABLE fact_sales (
    sale_key BIGINT,
    date_key INT,
    amount DECIMAL(10,2)
) PARTITION BY RANGE (date_key);
```

### Materialized Views

```sql
-- Create materialized view for common aggregation
CREATE MATERIALIZED VIEW mv_daily_sales AS
SELECT 
    date_key,
    SUM(amount) as total_sales,
    COUNT(*) as order_count
FROM fact_sales
GROUP BY date_key;

-- Refresh materialized view
REFRESH MATERIALIZED VIEW mv_daily_sales;
```

## Cloud Data Warehouses

### Snowflake

```sql
-- Snowflake specifics
CREATE WAREHOUSE analytics_wh
    WAREHOUSE_SIZE = 'MEDIUM'
    AUTO_SUSPEND = 300
    AUTO_RESUME = TRUE;

CREATE DATABASE analytics;
USE DATABASE analytics;

CREATE TABLE fact_sales (
    sale_key INT AUTOINCREMENT PRIMARY KEY,
    date DATE,
    amount NUMBER(10,2)
);

-- Time travel
SELECT * FROM fact_sales 
TIMESTAMP AT ('2024-01-15 10:00:00');
```

### BigQuery

```sql
-- BigQuery specifics
CREATE TABLE `project.dataset.fact_sales` (
    sale_key INT64,
    date DATE,
    amount NUMERIC(10,2)
)
PARTITION BY date
CLUSTER BY sale_key;

-- Query with partitioning
SELECT * FROM `project.dataset.fact_sales`
WHERE date BETWEEN '2024-01-01' AND '2024-01-31';
```

### Redshift

```sql
-- Redshift specifics
CREATE TABLE fact_sales (
    sale_key INTEGER PRIMARY KEY,
    date DATE,
    amount DECIMAL(10,2)
)
DISTSTYLE KEY
DISTKEY(date_key)
SORTKEY(date_key);

-- Vacuum and analyze
VACUUM fact_sales;
ANALYZE fact_sales;
```

## Best Practices

### 1. Design for Queries

```sql
-- Denormalize for query performance
CREATE VIEW v_sales_analysis AS
SELECT 
    s.sale_key,
    d.full_date,
    d.year,
    d.quarter,
    p.product_name,
    p.category,
    c.customer_name,
    c.segment,
    s.quantity,
    s.amount
FROM fact_sales s
JOIN dim_date d ON s.date_key = d.date_key
JOIN dim_product p ON s.product_key = p.product_key
JOIN dim_customer c ON s.customer_key = c.customer_key;
```

### 2. Incremental Loading

```python
# Only load changed data
def incremental_load(source, target, watermark_column):
    last_watermark = get_watermark(target)
    
    query = f"""
        SELECT * FROM {source}
        WHERE {watermark_column} > '{last_watermark}'
    """
    
    new_data = extract(query)
    load_to_warehouse(new_data, target)
    update_watermark(target, new_data[watercolumn].max())
```

### 3. Data Quality Checks

```python
# Validate before loading
def validate_data(df, rules):
    errors = []
    
    for rule in rules:
        if rule['type'] == 'not_null':
            null_count = df[rule['column']].isnull().sum()
            if null_count > 0:
                errors.append(f"{rule['column']} has {null_count} nulls")
        
        elif rule['type'] == 'unique':
            dup_count = df[rule['column']].duplicated().sum()
            if dup_count > 0:
                errors.append(f"{rule['column']} has {dup_count} duplicates")
    
    return errors
```

### 4. Monitor Performance

```sql
-- Track query performance
SELECT 
    query_id,
    query_text,
    execution_time_ms,
    bytes_scanned,
    rows_produced
FROM query_history
WHERE execution_time_ms > 10000
ORDER BY execution_time_ms DESC;
```

## Further Reading

- [The Data Warehouse Toolkit - Ralph Kimball](https://www.kimballgroup.com/data-warehouse-business-intelligence-resources/kimball-techniques/dimensional-modeling-techniques/)
- [Snowflake Documentation](https://docs.snowflake.com/)
- [BigQuery Documentation](https://cloud.google.com/bigquery/docs)
- [Redshift Documentation](https://docs.aws.amazon.com/redshift/)
