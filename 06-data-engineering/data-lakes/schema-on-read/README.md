# Schema-on-Read vs Schema-on-Write

## Overview

Schema-on-read and schema-on-write represent two fundamentally different approaches to managing data schemas in storage systems. Understanding when to use each approach is critical for effective data lake and warehouse design.

## Table of Contents

- [Schema-on-Write](#schema-on-write)
- [Schema-on-Read](#schema-on-read)
- [Comparison](#comparison)
- [Hybrid Approaches](#hybrid-approaches)
- [Use Cases](#use-cases)
- [Best Practices](#best-practices)

## Schema-on-Write

Schema validation occurs when data is written to storage.

### Traditional Data Warehouse

```sql
-- Define schema before inserting data
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(10,2),
    status VARCHAR(20) CHECK (status IN ('pending', 'shipped', 'delivered')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Data must conform to schema
INSERT INTO orders (order_id, customer_id, order_date, total_amount, status)
VALUES (12345, 67890, '2024-01-15', 99.99, 'pending');
```

### Schema Enforcement in Data Lakes

```python
# PySpark with explicit schema
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

schema = StructType([
    StructField("order_id", IntegerType(), False),
    StructField("customer_id", StringType(), False),
    StructField("amount", DoubleType(), True),
    StructField("status", StringType(), True)
])

# Enforce schema on write
df = spark.read.schema(schema).json("s3://raw/orders/")
df.write.parquet("s3://lake/orders/")
```

### Delta Lake Schema Enforcement

```python
# Delta Lake with schema enforcement
from delta.tables import DeltaTable

# Create table with schema
spark.sql("""
    CREATE TABLE orders (
        order_id INT NOT NULL,
        customer_id STRING NOT NULL,
        amount DOUBLE,
        status STRING
    ) USING delta
""")

# Schema enforcement - will fail if schema doesn't match
df.write.format("delta").mode("append").save("s3://lake/orders/")
```

## Schema-on-Read

Schema is applied when data is read, not when written.

### Reading Raw Data

```python
# Read JSON without schema enforcement
raw_df = spark.read \
    .option("mode", "PERMISSIVE") \
    .option("columnNameOfCorruptRecord", "_corrupt") \
    .json("s3://raw/events/")

# Apply schema on read
events = raw_df.select(
    col("event_id").cast("string"),
    col("timestamp").cast("timestamp"),
    col("user_id").cast("string"),
    col("event_type").cast("string"),
    col("_corrupt")
)
```

### Schema Evolution

```python
# Schema evolution with Parquet
df = spark.read.parquet("s3://data/events/")

# New schema with additional column
new_schema = df.withColumn("new_field", lit(None).cast("string"))

# Write with schema merge
new_df.write \
    .mode("append") \
    .option("mergeSchema", "true") \
    .parquet("s3://data/events/")
```

### Iceberg Schema Evolution

```python
# Iceberg supports schema evolution without rewriting data
spark.sql("ALTER TABLE events ADD COLUMNS (new_column STRING)")
spark.sql("ALTER TABLE events ALTER COLUMN event_id TYPE STRING")
spark.sql("ALTER TABLE events DROP COLUMN legacy_field")
```

## Comparison

| Aspect | Schema-on-Write | Schema-on-Read |
|--------|-----------------|----------------|
| Data Quality | Enforced at write | Enforced at read |
| Write Performance | Slower (validation) | Faster (raw) |
| Read Performance | Faster (pre-validated) | Slower (schema apply) |
| Flexibility | Rigid | Flexible |
| Data Exploration | Limited | Excellent |
| Storage Efficiency | May require transforms | Raw storage |
| Error Handling | Rejects bad data | Preserves all data |

## Hybrid Approaches

### Medallion Architecture

```
┌─────────────────────────────────────────────────────────────┐
│  BRONZE (Schema-on-Read)                                    │
│  • Raw data ingestion                                       │
│  • No schema enforcement                                    │
│  • Preserve all data                                        │
├─────────────────────────────────────────────────────────────┤
│  SILVER (Schema-on-Write)                                   │
│  • Cleaned and validated                                    │
│  • Schema enforced                                          │
│  • Data quality checks                                      │
├─────────────────────────────────────────────────────────────┤
│  GOLD (Schema-on-Write)                                     │
│  • Business-ready                                           │
│  • Aggregated                                               │
│  • Optimized for queries                                    │
└─────────────────────────────────────────────────────────────┘
```

## Use Cases

### Schema-on-Write: Financial Systems

- Regulatory compliance requirements
- Strict data quality standards
- ACID transaction needs
- Real-time dashboards requiring consistent data

### Schema-on-Read: Data Lakes

- Data exploration and discovery
- Machine learning feature engineering
- Historical data analysis
- Multi-source data integration
- Rapid prototyping

## Best Practices

### 1. Start with Schema-on-Read

```python
# Ingest raw data first
raw_data = spark.read.json("s3://incoming/")
raw_data.write.parquet("s3://lake/raw/")

# Validate later
validated = raw_data.filter(col("event_id").isNotNull())
validated.write.parquet("s3://lake/validated/")
```

### 2. Use Table Formats for Evolution

```python
# Delta Lake for schema evolution
df.write \
    .format("delta") \
    .mode("append") \
    .option("mergeSchema", "true") \
    .save("s3://lake/events/")
```

### 3. Document Schemas

```yaml
# Schema documentation
schemas:
  events:
    version: "2.1"
    columns:
      - name: event_id
        type: string
        required: true
        description: "Unique event identifier"
      - name: event_time
        type: timestamp
        required: true
        description: "When the event occurred"
```

### 4. Monitor Schema Drift

```python
# Detect schema changes
current_schema = spark.read.parquet("s3://lake/events/").schema
expected_schema = StructType([...])

if current_schema != expected_schema:
    alert("Schema drift detected!")
```

## Further Reading

- [Schema-on-Read vs Schema-on-Write](https://www.databricks.com/blog/2020/01/30/what-is-a-data-lakehouse.html)
- [Delta Lake Schema Evolution](https://docs.delta.io/latest/delta-schema-evolution.html)
- [Apache Iceberg Schema Evolution](https://iceberg.apache.org/docs/latest/schema-evolution/)
