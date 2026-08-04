# ELT Patterns

## Table of Contents

- [Overview](#overview)
- [ELT Process](#elt-process)
- [ELT vs ETL](#elt-vs-etl)
- [ELT Patterns](#elt-patterns)
- [ELT Tools and Frameworks](#elt-tools-and-frameworks)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

ELT (Extract, Load, Transform) is a data integration process that loads raw
data directly into the target system before transformation. Unlike ETL, ELT
leverages the processing power of modern data warehouses to transform data
in-place.

### Key Characteristics

- **Extract**: Retrieve data from source systems
- **Load**: Load raw data directly to target warehouse
- **Transform**: Transform data within the warehouse
- **Leverage warehouse**: Use warehouse compute for transformations
- **Flexible**: Handle schema changes more easily

### When to Use ELT

- Modern cloud data warehouses (Snowflake, BigQuery, Redshift)
- Large data volumes requiring distributed processing
- Data science and machine learning workloads
- Real-time analytics requirements
- Data exploration and discovery

### ELT Benefits

1. **Flexibility**: Transform data as needed without re-extracting
2. **Performance**: Leverage warehouse compute for transformations
3. **Cost**: Use existing warehouse infrastructure
4. **Agility**: Quick iteration on transformations
5. **Scalability**: Scale transformations with warehouse

---

## ELT Process

### ELT Workflow

```
┌─────────────────────────────────────────────────────────────────────┐
│                        ELT Process                                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Source Systems                                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │   ERP    │ │   CRM    │ │  Files   │ │   APIs   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Extract Phase            │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Extract raw data from sources                             │   │
│  │  - Minimal transformation                                    │   │
│  │  - Preserve data fidelity                                    │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Load Phase              │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Load raw data to target warehouse                         │   │
│  │  - Schema on read                                            │   │
│  │  - No transformation                                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Transform Phase         │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Transform data within warehouse                           │   │
│  │  - Use SQL and warehouse compute                             │   │
│  │  - Create views and tables                                   │   │
│  │  - Apply business logic                                      │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Target Systems                                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │   Data   │ │  Data    │ │  Data    │ │   Data   │              │
│  │ Warehouse│ │   Lake   │ │  Lakehouse│ │   Mart   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## ELT vs ETL

### Comparison

| Feature | ELT | ETL |
|---------|-----|-----|
| Transform Location | Target warehouse | Staging area |
| Latency | Lower | Higher |
| Flexibility | More flexible | Less flexible |
| Cost | Lower (use warehouse) | Higher (separate infra) |
| Data Volume | Better for large data | Better for small data |
| Schema Changes | Easier to handle | Requires re-extraction |
| Data Exploration | Better | Limited |
| Performance | Leverages warehouse | Limited by staging |

### When to Choose ELT

- **Cloud data warehouses**: Snowflake, BigQuery, Redshift
- **Large data volumes**: Warehouse can handle transformations
- **Frequent schema changes**: More flexible than ETL
- **Data exploration**: Transform data as needed
- **Real-time requirements**: Lower latency than ETL

### When to Choose ETL

- **Sensitive data**: Transform before loading to warehouse
- **Complex transformations**: Require specialized tools
- **Compliance requirements**: Data must be transformed before storage
- **Limited warehouse compute**: Insufficient resources for transformations

---

## ELT Patterns

### Staging Layer Pattern

```sql
-- Create staging tables for raw data
CREATE TABLE stg_orders (
    raw_data VARIANT,
    _loaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    _source_file VARCHAR(100)
);

-- Load raw data to staging
COPY INTO stg_orders
FROM @raw_stage/orders/
FILE_FORMAT = (TYPE = 'JSON');

-- Create clean tables from staging
CREATE TABLE clean_orders AS
SELECT
    raw_data:order_id::VARCHAR AS order_id,
    raw_data:customer_id::VARCHAR AS customer_id,
    raw_data:order_date::DATE AS order_date,
    raw_data:amount::DOUBLE AS amount,
    _loaded_at
FROM stg_orders;
```

### View Pattern

```sql
-- Create views for data access
CREATE VIEW v_orders AS
SELECT
    o.order_id,
    o.customer_id,
    c.customer_name,
    o.order_date,
    o.amount,
    o.status
FROM clean_orders o
LEFT JOIN clean_customers c ON o.customer_id = c.customer_id;

-- Create aggregated views
CREATE VIEW v_daily_sales AS
SELECT
    order_date,
    COUNT(*) AS order_count,
    SUM(amount) AS total_sales,
    AVG(amount) AS avg_order_value
FROM v_orders
GROUP BY order_date;
```

### Incremental Pattern

```sql
-- Track watermarks for incremental processing
CREATE TABLE watermarks (
    table_name VARCHAR(100),
    last_watermark TIMESTAMP,
    PRIMARY KEY (table_name)
);

-- Incremental load
INSERT INTO clean_orders
SELECT
    raw_data:order_id::VARCHAR,
    raw_data:customer_id::VARCHAR,
    raw_data:order_date::DATE,
    raw_data:amount::DOUBLE,
    CURRENT_TIMESTAMP()
FROM stg_orders
WHERE _loaded_at > (
    SELECT last_watermark
    FROM watermarks
    WHERE table_name = 'orders'
);

-- Update watermark
UPDATE watermarks
SET last_watermark = CURRENT_TIMESTAMP()
WHERE table_name = 'orders';
```

### Slowly Changing Dimension Pattern

```sql
-- SCD Type 2 implementation
CREATE TABLE dim_customers (
    customer_key INT AUTOINCREMENT,
    customer_id VARCHAR(50),
    customer_name VARCHAR(100),
    email VARCHAR(100),
    valid_from TIMESTAMP,
    valid_to TIMESTAMP,
    is_current BOOLEAN
);

-- Merge new data
MERGE INTO dim_customers AS target
USING new_customers AS source
ON target.customer_id = source.customer_id AND target.is_current = TRUE
WHEN MATCHED AND (
    target.customer_name != source.customer_name OR
    target.email != source.email
) THEN
    UPDATE SET
        valid_to = CURRENT_TIMESTAMP(),
        is_current = FALSE
WHEN NOT MATCHED THEN
    INSERT (customer_id, customer_name, email, valid_from, is_current)
    VALUES (source.customer_id, source.customer_name, source.email, CURRENT_TIMESTAMP(), TRUE);

-- Insert new versions
INSERT INTO dim_customers (customer_id, customer_name, email, valid_from, is_current)
SELECT
    customer_id,
    customer_name,
    email,
    CURRENT_TIMESTAMP(),
    TRUE
FROM new_customers
WHERE customer_id IN (
    SELECT customer_id
    FROM dim_customers
    WHERE is_current = FALSE
);
```

### Data Vault Pattern

```sql
-- Hub tables (business keys)
CREATE TABLE hub_customers (
    customer_hk VARCHAR(64) PRIMARY KEY,
    customer_id VARCHAR(50),
    load_date TIMESTAMP,
    record_source VARCHAR(100)
);

-- Link tables (relationships)
CREATE TABLE link_orders_customers (
    order_customer_hk VARCHAR(64) PRIMARY KEY,
    order_hk VARCHAR(64),
    customer_hk VARCHAR(64),
    load_date TIMESTAMP,
    record_source VARCHAR(100)
);

-- Satellite tables (descriptive attributes)
CREATE TABLE sat_customers (
    customer_hk VARCHAR(64),
    customer_name VARCHAR(100),
    email VARCHAR(100),
    load_date TIMESTAMP,
    load_end_date TIMESTAMP,
    record_source VARCHAR(100),
    PRIMARY KEY (customer_hk, load_date)
);
```

---

## ELT Tools and Frameworks

### dbt (data build tool)

```sql
-- models/staging/stg_orders.sql
WITH source AS (
    SELECT * FROM {{ source('raw', 'orders') }}
),
renamed AS (
    SELECT
        id AS order_id,
        customer_id,
        order_date,
        amount,
        status
    FROM source
)
SELECT * FROM renamed

-- models/marts/fct_orders.sql
WITH orders AS (
    SELECT * FROM {{ ref('stg_orders') }}
),
customers AS (
    SELECT * FROM {{ ref('stg_customers') }}
),
final AS (
    SELECT
        o.order_id,
        o.customer_id,
        c.customer_name,
        c.segment AS customer_segment,
        o.order_date,
        o.amount,
        o.status
    FROM orders o
    LEFT JOIN customers c ON o.customer_id = c.customer_id
)
SELECT * FROM final
```

### SQLMesh

```sql
-- models/orders.sql
MODEL (
    name orders,
    kind FULL,
    owner data_engineering
);

SELECT
    id AS order_id,
    customer_id,
    order_date,
    amount,
    status
FROM raw.orders;

-- models/daily_sales.sql
MODEL (
    name daily_sales,
    kind FULL,
    owner data_engineering
);

SELECT
    order_date,
    COUNT(*) AS order_count,
    SUM(amount) AS total_sales
FROM orders
GROUP BY order_date;
```

### Apache Spark

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("ELT").getOrCreate()

# Extract
raw_df = spark.read.parquet("s3://raw/orders/")

# Load to staging
raw_df.write.mode("overwrite").parquet("s3://staging/orders/")

# Transform using SQL
raw_df.createOrReplaceTempView("raw_orders")
spark.sql("""
    CREATE OR REPLACE TABLE clean_orders AS
    SELECT
        order_id,
        customer_id,
        order_date,
        amount,
        status
    FROM raw_orders
    WHERE amount > 0
""")
```

---

## Best Practices

### Pipeline Design

1. **Idempotency**: Design pipelines to be re-runnable
2. **Modularity**: Break complex transformations into smaller steps
3. **Version control**: Track changes to transformation logic
4. **Testing**: Validate transformations with tests

### Performance

1. **Partition data**: By date for time-series data
2. **Use appropriate file formats**: Parquet or ORC for staging
3. **Optimize warehouse queries**: Use clustering and materialized views
4. **Monitor query performance**: Track execution times

### Data Quality

1. **Validate data**: Check for completeness, accuracy, consistency
2. **Track lineage**: Know where data came from and how it was transformed
3. **Monitor freshness**: Ensure data is up-to-date
4. **Implement contracts**: Define expected schemas and quality

### Security

1. **Encrypt sensitive data**: At rest and in transit
2. **Implement access control**: Role-based access control
3. **Audit data access**: Log all data access and modifications
4. **Mask PII**: Protect personally identifiable information

---

## Examples

### Complete ELT Pipeline

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

class SalesELTPipeline:
    def __init__(self, config):
        self.config = config
        self.spark = None

    def extract(self):
        """Extract data from source systems"""
        # Read from JDBC
        orders_df = self.spark.read.format("jdbc") \
            .option("url", self.config['source_url']) \
            .option("dbtable", "orders") \
            .option("user", self.config['source_user']) \
            .option("password", self.config['source_password']) \
            .load()

        return orders_df

    def load_to_staging(self, raw_df):
        """Load raw data to staging"""
        raw_df.write.mode("overwrite") \
            .parquet(self.config['staging_path'])

    def transform(self):
        """Transform data in warehouse"""
        # Read from staging
        staging_df = self.spark.read.parquet(self.config['staging_path'])

        # Create temp view
        staging_df.createOrReplaceTempView("staging_orders")

        # Transform using SQL
        self.spark.sql("""
            CREATE OR REPLACE TABLE clean_orders AS
            SELECT
                order_id,
                customer_id,
                order_date,
                amount,
                status,
                CURRENT_TIMESTAMP() AS processed_at
            FROM staging_orders
            WHERE amount > 0
        """)

    def run(self):
        """Execute ELT pipeline"""
        self.spark = SparkSession.builder \
            .appName("SalesELT") \
            .getOrCreate()

        try:
            # Extract
            raw_df = self.extract()

            # Load to staging
            self.load_to_staging(raw_df)

            # Transform in warehouse
            self.transform()
        finally:
            self.spark.stop()

# Execute pipeline
config = {
    'source_url': 'jdbc:postgresql://host/db',
    'source_user': 'user',
    'source_password': 'password',
    'staging_path': 's3://staging/orders/'
}

pipeline = SalesELTPipeline(config)
pipeline.run()
```

### dbt Project Structure

```
my_dbt_project/
├── dbt_project.yml
├── models/
│   ├── staging/
│   │   ├── _staging.yml
│   │   ├── stg_orders.sql
│   │   └── stg_customers.sql
│   ├── marts/
│   │   ├── _marts.yml
│   │   ├── fct_orders.sql
│   │   └── dim_customers.sql
│   └── analytics/
│       ├── _analytics.yml
│       └── daily_sales.sql
├── tests/
│   ├── assert_orders_positive.sql
│   └── assert_customers_unique.sql
└── snapshots/
    └── scd_customers.sql
```

---

## References

- [ELT vs ETL](https://www.stitchdata.com/resources/etl/etl-vs-elt/)
- [ELT Best Practices](https://www.talend.com/resources/what-is-elt/)
- [dbt Documentation](https://docs.getdbt.com/)
- [ELT in the Cloud](https://www.snowflake.com/blog/elt-vs-etl/)
- [Building ELT Pipelines](https://www.oreilly.com/library/view/building-elt-pipelines/9781492028161/)
