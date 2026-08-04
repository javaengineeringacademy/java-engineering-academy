# Lakehouse Architecture

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Lakehouse Architecture](#lakehouse-architecture)
- [Table Formats](#table-formats)
- [Data Management](#data-management)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

The Lakehouse architecture combines the best features of data lakes and data
warehouses, providing a single platform for both data lake and data warehouse
use cases. It enables ACID transactions, schema enforcement, and data governance
on top of low-cost cloud storage.

### Key Characteristics

- **ACID transactions**: Support for atomicity, consistency, isolation, durability
- **Schema enforcement**: Ensure data quality with schema validation
- **Time travel**: Query historical data versions
- **Data governance**: Unified governance across all data types
- **Cost-effective**: Low-cost cloud storage

### Lakehouse vs Data Warehouse vs Data Lake

| Feature | Lakehouse | Data Warehouse | Data Lake |
|---------|-----------|---------------|-----------|
| Data Types | All types | Structured only | All types |
| ACID Transactions | Yes | Yes | No |
| Schema Enforcement | Yes | Yes | No |
| Time Travel | Yes | Limited | No |
| Cost | Low | High | Low |
| Performance | High | High | Variable |

### When to Use Lakehouse

- Unified analytics platform
- Machine learning workloads
- Real-time analytics
- Data engineering pipelines
- Data science and exploration

---

## Core Concepts

### Lakehouse Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Lakehouse Architecture                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Applications                                                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │   BI     │ │   SQL    │ │   ML     │ │Streaming │              │
│  │  Tools   │ │ Queries  │ │ Models   │ │  Apps    │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                                                                      │
│  Query Engine Layer                                                  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Spark   │ │  Presto  │ │   Trino  │ │  Flink   │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  Table Format Layer                                                  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Delta   │ │ Iceberg  │ │   Hudi   │ │  Paimon  │       │  │
│  │  │   Lake   │ │          │ │          │ │          │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  Storage Layer (Cloud Object Storage)                                │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │   S3     │ │   ADLS   │ │   GCS    │ │   HDFS   │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  Metadata Layer                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Schema │ Catalog │ Lineage │ Governance │ Quality          │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Lakehouse Architecture

### Delta Lake

```python
# Delta Lake ACID transactions
from delta import DeltaTable

# Create Delta table
df.write.format("delta").save("s3://lakehouse/sales")

# Read Delta table
df = spark.read.format("delta").load("s3://lakehouse/sales")

# Time travel
df = spark.read.format("delta").load("s3://lakehouse/sales").option("versionAsOf", 10)

# Schema evolution
df.write.format("delta").mode("append") \
    .option("mergeSchema", "true") \
    .save("s3://lakehouse/sales")

# Merge (UPSERT)
delta_table = DeltaTable.forPath(spark, "s3://lakehouse/sales")

delta_table.alias("target").merge(
    source_df.alias("source"),
    "target.id = source.id"
).whenMatchedUpdateAll() \
 .whenNotMatchedInsertAll() \
 .execute()
```

### Apache Iceberg

```python
# Iceberg table operations
# Create table
spark.sql("""
    CREATE TABLE catalog.db.sales (
        id INT,
        amount DOUBLE,
        sale_date DATE
    ) USING iceberg
    PARTITIONED BY (sale_date)
""")

# Insert data
df.writeTo("catalog.db.sales").append()

# Time travel
df = spark.read.format("iceberg").load("catalog.db.sales")

# Schema evolution
spark.sql("""
    ALTER TABLE catalog.db.sales ADD COLUMN region STRING
""")

# Merge
MERGE INTO catalog.db.sales AS target
USING source_df AS source
ON target.id = source.id
WHEN MATCHED THEN UPDATE SET
    amount = source.amount
WHEN NOT MATCHED THEN INSERT *
```

### Apache Hudi

```python
# Hudi table operations
# Create table
hudi_options = {
    'hoodie.table.name': 'sales',
    'hoodie.datasource.write.recordkey.field': 'id',
    'hoodie.datasource.write.precombine.field': 'timestamp',
    'hoodie.datasource.write.table.type': 'COPY_ON_WRITE'
}

# Write data
df.write.format("hudi").options(**hudi_options).save("s3://lakehouse/sales")

# Read data
df = spark.read.format("hudi").load("s3://lakehouse/sales")

# Merge
from hudi import HudiTable

hudi_table = HudiTable.forPath(spark, "s3://lakehouse/sales")
hudi_table.mergeInto(source_df, "target.id = source.id")
```

---

## Table Formats

### Comparison

| Feature | Delta Lake | Iceberg | Hudi |
|---------|-----------|---------|------|
| ACID Transactions | Yes | Yes | Yes |
| Schema Evolution | Yes | Yes | Yes |
| Time Travel | Yes | Yes | Yes |
| Partition Evolution | No | Yes | Yes |
| Hidden Partitioning | Yes | Yes | Yes |
| Copy-on-Write | Yes | Yes | Yes |
| Merge-on-Read | Yes | Yes | Yes |
| Compaction | Manual | Automatic | Automatic |
| Streaming | Yes | Yes | Yes |
| Community | Databricks | Apache | Apache |

### Delta Lake Features

```python
# VACUUM old files
spark.sql("VACUUM delta.`s3://lakehouse/sales` RETAIN 168 HOURS")

# OPTIMIZE table
spark.sql("OPTIMIZE delta.`s3://lakehouse/sales`")

# Z-ORDER for data skipping
spark.sql("OPTIMIZE delta.`s3://lakehouse/sales` ZORDER BY (product_id, sale_date)")

# Describe history
spark.sql("DESCRIBE HISTORY delta.`s3://lakehouse/sales`")

# Describe table details
spark.sql("DESCRIBE DETAIL delta.`s3://lakehouse/sales`")
```

### Iceberg Features

```python
# Snapshot management
spark.sql("ALTER TABLE catalog.db.sales SNAPSHOTS")
spark.sql("CALL catalog.system.rollback('db.sales', snapshot_id => 123)")

# Partition evolution
spark.sql("""
    ALTER TABLE catalog.db.sales ADD PARTITION FIELD region
""")

# Sort order
spark.sql("""
    ALTER TABLE catalog.db.sales WRITE ORDERED BY region, sale_date
""")

# Branch and tag
spark.sql("""
    ALTER TABLE catalog.db.sales CREATE BRANCH dev
""")
spark.sql("""
    ALTER TABLE catalog.db.sales CREATE TAG release_1_0
""")
```

### Hudi Features

```python
# Clustering
spark.sql("""
    CALL hudi.procedures.cluster(table => 'db.sales')
""")

# Compaction
spark.sql("""
    CALL hudi.procedures.compact(table => 'db.sales')
""")

# Clean
spark.sql("""
    CALL hudi.procedures.clean(table => 'db.sales')
""")

# Timeline
spark.sql("""
    CALL hudi.procedures.timeline(table => 'db.sales')
""")
```

---

## Data Management

### Schema Management

```python
# Schema enforcement
df.write.format("delta").mode("overwrite").save("s3://lakehouse/sales")

# Schema evolution
df.write.format("delta").mode("append") \
    .option("mergeSchema", "true") \
    .save("s3://lakehouse/sales")

# Schema validation
from pyspark.sql.types import StructType, StructField, StringType, DoubleType

schema = StructType([
    StructField("id", StringType(), False),
    StructField("amount", DoubleType(), False),
    StructField("sale_date", StringType(), False)
])

df = spark.read.schema(schema).format("delta").load("s3://lakehouse/sales")
```

### Data Quality

```python
# Data quality checks
def validate_data(df, rules):
    violations = []

    # Check for null values
    for column in rules.get("not_null", []):
        null_count = df.filter(col(column).isNull()).count()
        if null_count > 0:
            violations.append(f"Column {column} has {null_count} null values")

    # Check for valid ranges
    for column, (min_val, max_val) in rules.get("range", {}).items():
        invalid_count = df.filter(
            (col(column) < min_val) | (col(column) > max_val)
        ).count()
        if invalid_count > 0:
            violations.append(f"Column {column} has {invalid_count} out of range values")

    return violations

# Define rules
rules = {
    "not_null": ["id", "amount", "sale_date"],
    "range": {
        "amount": (0, 1000000)
    }
}

# Validate
violations = validate_data(df, rules)
if violations:
    raise ValueError(f"Data quality violations: {violations}")
```

### Data Governance

```python
# Access control
access_control = {
    "data-engineers": {
        "read": ["raw", "cleansed", "curated"],
        "write": ["raw", "cleansed", "curated"]
    },
    "data-analysts": {
        "read": ["curated"],
        "write": []
    },
    "business-users": {
        "read": ["curated"],
        "write": []
    }
}

# Data classification
data_classification = {
    "sales": {
        "sensitivity": "internal",
        "retention": "7 years",
        "owner": "data-engineering-team"
    }
}
```

---

## Performance Optimization

### File Optimization

```python
# Optimize file sizes
# Target: 128MB-256MB per file
df.repartition(100).write.format("delta").save("s3://lakehouse/sales")

# Optimize existing tables
spark.sql("OPTIMIZE delta.`s3://lakehouse/sales`")

# Z-ORDER for data skipping
spark.sql("OPTIMIZE delta.`s3://lakehouse/sales` ZORDER BY (product_id, sale_date)")
```

### Partitioning

```python
# Partition by date for time-series data
df.write.format("delta") \
    .partitionBy("year", "month", "day") \
    .save("s3://lakehouse/sales")

# Iceberg partition evolution
spark.sql("""
    ALTER TABLE catalog.db.sales ADD PARTITION FIELD region
""")
```

### Caching

```python
# Cache hot data
hot_data = spark.read.format("delta").load("s3://lakehouse/sales/hot/")
hot_data.cache()

# Use appropriate storage level
from pyspark import StorageLevel
hot_data.persist(StorageLevel.MEMORY_AND_DISK)

# Monitor cache usage
print(hot_data.storageLevel)
print(hot_data.is_cached)
```

### Query Optimization

```python
# Use partition pruning
df = spark.read.format("delta").load("s3://lakehouse/sales")
filtered_df = df.filter(col("sale_date") == "2024-01-01")

# Use predicate pushdown
filtered_df = df.filter(col("amount") > 100)

# Use column pruning
selected_df = df.select("id", "amount")

# Use vectorized execution
spark.conf.set("spark.sql.parquet.enableVectorizedReader", True)
```

### Compaction

```python
# Delta Lake compaction
spark.sql("OPTIMIZE delta.`s3://lakehouse/sales`")

# Iceberg compaction
spark.sql("""
    CALL catalog.system.rewrite_data_files('db.sales')
""")

# Hudi compaction
spark.sql("""
    CALL hudi.procedures.compact(table => 'db.sales')
""")
```

---

## Best Practices

### Table Design

1. **Use appropriate table format**: Delta, Iceberg, or Hudi based on use case
2. **Partition wisely**: By date for time-series data
3. **Optimize file sizes**: 128MB-256MB per file
4. **Use Z-ORDER**: For data skipping on frequently queried columns

### Data Management

1. **Implement schema evolution**: Handle schema changes gracefully
2. **Track data lineage**: Know where data came from and how it was transformed
3. **Monitor data freshness**: Ensure data is up-to-date
4. **Implement data contracts**: Define expected schemas and quality

### Performance

1. **Optimize tables regularly**: Use OPTIMIZE and VACUUM
2. **Cache hot data**: For frequently accessed data
3. **Use appropriate indexes**: For frequent query patterns
4. **Monitor query performance**: Track query times and resource usage

### Cost Optimization

1. **Use lifecycle policies**: Move old data to cheaper storage
2. **Compress data**: Use appropriate compression algorithms
3. **Optimize queries**: Avoid full table scans
4. **Monitor costs**: Track storage and compute costs

---

## References

- [Delta Lake Documentation](https://docs.delta.io/)
- [Apache Iceberg Documentation](https://iceberg.apache.org/docs/latest/)
- [Apache Hudi Documentation](https://hudi.apache.org/docs/overview/)
- [Lakehouse Architecture](https://www.databricks.com/blog/2020/01/30/what-is-a-data-lakehouse.html)
- [Data Lakehouse](https://www.databricks.com/glossary/data-lakehouse)
- [Building a Lakehouse](https://www.oreilly.com/library/view/building-a-lakehouse/9781098106478/)
