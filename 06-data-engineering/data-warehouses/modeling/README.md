# Data Warehouse Modeling

## Overview

Data warehouse modeling defines how data is organized for analytical queries. The choice of modeling approach impacts query performance, storage efficiency, and maintainability.

## Table of Contents

- [Dimensional Modeling](#dimensional-modeling)
- [Star Schema](#star-schema)
- [Snowflake Schema](#snowflake-schema)
- [Data Vault](#data-vault)
- [One Big Table (OBT)](#one-big-table-obt)
- [Modeling Patterns](#modeling-patterns)
- [Best Practices](#best-practices)

## Dimensional Modeling

### Kimball Methodology

```
┌─────────────────────────────────────────────────────────────┐
│                 KIMBALL METHODOLOGY                          │
├─────────────────────────────────────────────────────────────┤
│  1. Select Business Process                                  │
│     • Order fulfillment                                      │
│     • Customer support                                       │
│     • Inventory management                                   │
├─────────────────────────────────────────────────────────────┤
│  2. Declare Grain                                           │
│     • One row per order line item                            │
│     • One row per daily summary                             │
├─────────────────────────────────────────────────────────────┤
│  3. Identify Dimensions                                      │
│     • Who: Customer, Employee                                │
│     • What: Product, Service                                 │
│     • When: Date, Time                                       │
│     • Where: Store, Region                                   │
├─────────────────────────────────────────────────────────────┤
│  4. Identify Facts                                           │
│     • Additive: Quantity, Amount                             │
│     • Semi-additive: Balance, Rate                           │
│     • Non-additive: Ratio, Percentage                        │
└─────────────────────────────────────────────────────────────┘
```

## Star Schema

### Design

```sql
-- Fact Table: Grain = one row per order line item
CREATE TABLE fact_order_lines (
    order_line_key BIGINT PRIMARY KEY,
    order_date_key INT REFERENCES dim_date(date_key),
    customer_key INT REFERENCES dim_customer(customer_key),
    product_key INT REFERENCES dim_product(product_key),
    store_key INT REFERENCES dim_store(store_key),
    quantity INT,
    unit_price DECIMAL(10,2),
    discount_amount DECIMAL(10,2),
    line_total DECIMAL(10,2)
);

-- Dimension: Date
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE NOT NULL,
    year INT,
    quarter INT,
    month INT,
    week INT,
    day_of_week INT,
    day_name VARCHAR(10),
    month_name VARCHAR(10),
    is_weekend BOOLEAN,
    is_holiday BOOLEAN
);

-- Dimension: Customer
CREATE TABLE dim_customer (
    customer_key INT PRIMARY KEY,
    customer_id VARCHAR(50),
    customer_name VARCHAR(200),
    email VARCHAR(200),
    segment VARCHAR(50),
    city VARCHAR(100),
    state VARCHAR(50),
    country VARCHAR(100),
    first_order_date DATE,
    is_active BOOLEAN
);

-- Dimension: Product
CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_id VARCHAR(50),
    product_name VARCHAR(200),
    category VARCHAR(100),
    subcategory VARCHAR(100),
    brand VARCHAR(100),
    unit_cost DECIMAL(10,2),
    unit_price DECIMAL(10,2),
    is_active BOOLEAN
);
```

### Query Pattern

```sql
-- Common analytical query
SELECT 
    d.month_name,
    p.category,
    c.segment,
    SUM(f.quantity) as total_quantity,
    SUM(f.line_total) as total_revenue
FROM fact_order_lines f
JOIN dim_date d ON f.order_date_key = d.date_key
JOIN dim_product p ON f.product_key = p.product_key
JOIN dim_customer c ON f.customer_key = c.customer_key
WHERE d.year = 2024
GROUP BY d.month_name, p.category, c.segment
ORDER BY total_revenue DESC;
```

## Snowflake Schema

### Design

```sql
-- Normalized dimensions
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year INT,
    month_key INT REFERENCES dim_month(month_key),
    day_of_week_key INT REFERENCES dim_day_of_week(day_of_week_key)
);

CREATE TABLE dim_month (
    month_key INT PRIMARY KEY,
    month_name VARCHAR(10),
    quarter_key INT REFERENCES dim_quarter(quarter_key)
);

CREATE TABLE dim_quarter (
    quarter_key INT PRIMARY KEY,
    quarter_name VARCHAR(10),
    year INT
);

-- Product hierarchy
CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_name VARCHAR(200),
    category_key INT REFERENCES dim_category(category_key)
);

CREATE TABLE dim_category (
    category_key INT PRIMARY KEY,
    category_name VARCHAR(100),
    department_key INT REFERENCES dim_department(department_key)
);

CREATE TABLE dim_department (
    department_key INT PRIMARY KEY,
    department_name VARCHAR(100)
);
```

### Trade-offs

| Aspect | Star Schema | Snowflake Schema |
|--------|-------------|------------------|
| Query Complexity | Simple | Complex (more joins) |
| Storage | Higher (redundant) | Lower (normalized) |
| Query Performance | Faster | Slower |
| Maintenance | Easier | Harder |
| BI Tool Support | Excellent | Good |

## Data Vault

### Architecture

```sql
-- Hubs: Business keys
CREATE TABLE hub_customer (
    hub_customer_key BIGINT PRIMARY KEY,
    customer_id VARCHAR(50),
    load_date TIMESTAMP,
    record_source VARCHAR(100)
);

CREATE TABLE hub_product (
    hub_product_key BIGINT PRIMARY KEY,
    product_id VARCHAR(50),
    load_date TIMESTAMP,
    record_source VARCHAR(100)
);

-- Links: Relationships
CREATE TABLE link_order_customer (
    link_key BIGINT PRIMARY KEY,
    hub_order_key BIGINT,
    hub_customer_key BIGINT,
    load_date TIMESTAMP,
    record_source VARCHAR(100)
);

-- Satellites: Descriptive attributes
CREATE TABLE sat_customer_details (
    sat_key BIGINT PRIMARY KEY,
    hub_customer_key BIGINT,
    load_date TIMESTAMP,
    load_end_date TIMESTAMP,
    name VARCHAR(200),
    email VARCHAR(200),
    segment VARCHAR(50),
    city VARCHAR(100),
    record_source VARCHAR(100)
);

CREATE TABLE sat_order_details (
    sat_key BIGINT PRIMARY KEY,
    hub_order_key BIGINT,
    load_date TIMESTAMP,
    order_date DATE,
    status VARCHAR(50),
    total_amount DECIMAL(10,2),
    record_source VARCHAR(100)
);
```

### Data Vault Benefits

- **Auditability**: Complete history of all changes
- **Flexibility**: Easy to add new sources
- **Parallel loading**: Independent hub/satellite loading
- **Traceability**: Every record has source and timestamp

## One Big Table (OBT)

### Design

```sql
-- Denormalized for query performance
CREATE TABLE obt_orders (
    -- Order attributes
    order_id VARCHAR(50),
    order_date DATE,
    order_status VARCHAR(50),
    order_total DECIMAL(10,2),
    
    -- Customer attributes (denormalized)
    customer_id VARCHAR(50),
    customer_name VARCHAR(200),
    customer_segment VARCHAR(50),
    customer_city VARCHAR(100),
    customer_country VARCHAR(100),
    
    -- Product attributes (denormalized)
    product_id VARCHAR(50),
    product_name VARCHAR(200),
    product_category VARCHAR(100),
    product_brand VARCHAR(100),
    
    -- Store attributes (denormalized)
    store_id VARCHAR(50),
    store_name VARCHAR(200),
    store_region VARCHAR(100),
    
    -- Metrics
    quantity INT,
    unit_price DECIMAL(10,2),
    discount_amount DECIMAL(10,2),
    line_total DECIMAL(10,2),
    
    -- Derived attributes
    order_year INT,
    order_month INT,
    order_quarter INT,
    is_weekend BOOLEAN
);
```

### When to Use OBT

- **Use when**: Read-heavy workloads, simple queries, BI tools
- **Avoid when**: Complex relationships, frequent updates, data integrity critical

## Modeling Patterns

### Aggregate Fact Table

```sql
-- Daily aggregates
CREATE TABLE fact_daily_sales (
    date_key INT,
    product_key INT,
    store_key INT,
    daily_quantity INT,
    daily_revenue DECIMAL(10,2),
    daily_transactions INT
);

-- Monthly aggregates
CREATE TABLE fact_monthly_sales (
    month_key INT,
    product_key INT,
    store_key INT,
    monthly_quantity INT,
    monthly_revenue DECIMAL(10,2),
    monthly_avg_order_value DECIMAL(10,2)
);
```

### Degenerate Dimensions

```sql
-- Order number as degenerate dimension
CREATE TABLE fact_order_lines (
    order_line_key BIGINT PRIMARY KEY,
    order_number VARCHAR(50) DEGENERATE DIMENSION,
    order_date_key INT,
    product_key INT,
    quantity INT,
    amount DECIMAL(10,2)
);
```

### Junk Dimensions

```sql
-- Combine low-cardinality flags
CREATE TABLE dim_order_flags (
    flag_key INT PRIMARY KEY,
    is_priority BOOLEAN,
    is_gift BOOLEAN,
    is_return BOOLEAN,
    payment_method VARCHAR(20),
    shipping_method VARCHAR(20)
);

-- Reference from fact table
CREATE TABLE fact_orders (
    order_key BIGINT PRIMARY KEY,
    date_key INT,
    customer_key INT,
    flag_key INT REFERENCES dim_order_flags(flag_key),
    amount DECIMAL(10,2)
);
```

### Confirmed Dimensions

```sql
-- Shared across fact tables
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year INT,
    month INT
);

-- Used by multiple facts
CREATE TABLE fact_sales (
    sale_key BIGINT PRIMARY KEY,
    date_key INT REFERENCES dim_date(date_key),
    ...
);

CREATE TABLE fact_inventory (
    inventory_key BIGINT PRIMARY KEY,
    date_key INT REFERENCES dim_date(date_key),
    ...
);
```

## Best Practices

### 1. Choose the Right Grain

```sql
-- Document grain clearly
-- Grain: One row per order line item per day
CREATE TABLE fact_order_lines (
    order_line_key BIGINT PRIMARY KEY,
    order_date_key INT,  -- Grain identifier
    customer_key INT,
    product_key INT,
    quantity INT,
    amount DECIMAL(10,2)
);
```

### 2. Handle Late Arriving Data

```sql
-- Late arriving dimension
CREATE TABLE dim_customer_late_arriving (
    customer_key INT PRIMARY KEY,
    customer_id VARCHAR(50),
    effective_date DATE,
    expiry_date DATE,
    is_current BOOLEAN
);

-- Use surrogate key for late arrivals
INSERT INTO fact_orders (order_key, date_key, customer_key, amount)
VALUES (12345, 20240115, -1, 99.99);  -- -1 for unknown customer
```

### 3. Document Your Models

```yaml
dimension_model:
  fact_sales:
    grain: "One row per order line item"
    refresh_frequency: "Daily"
    retention: "7 years"
    owner: "data-commerce-team"
    
  dim_customer:
    grain: "One row per customer"
    scd_type: 2
    refresh_frequency: "Daily"
    key_columns:
      - customer_id
```

### 4. Performance Optimization

```sql
-- Cluster fact tables
ALTER TABLE fact_sales CLUSTER BY (order_date_key, customer_key);

-- Create summary tables
CREATE TABLE fact_sales_summary AS
SELECT 
    date_key,
    product_key,
    SUM(quantity) as total_quantity,
    SUM(amount) as total_amount
FROM fact_sales
GROUP BY date_key, product_key;
```

## Further Reading

- [The Data Warehouse Toolkit - Ralph Kimball](https://www.kimballgroup.com/data-warehouse-business-intelligence-resources/kimball-techniques/dimensional-modeling-techniques/)
- [Data Vault Modeling](https://www.data-vault.com/)
- [Dimensional Modeling Patterns](https://www.kimballgroup.com/2011/09/design-techniques-for-managing-polarity/)
