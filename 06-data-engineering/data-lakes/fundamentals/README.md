# Data Lake Fundamentals

## Overview

A data lake is a centralized repository that stores structured, semi-structured, and unstructured data at any scale. Unlike data warehouses, data lakes store data in its raw format, allowing for schema-on-read rather than schema-on-write.

## Table of Contents

- [Architecture](#architecture)
- [Data Lake Layers](#data-lake-layers)
- [Storage Formats](#storage-formats)
- [Data Organization](#data-organization)
- [Lakehouse Architecture](#lakehouse-architecture)
- [Data Lake vs Data Warehouse](#data-lake-vs-data-warehouse)
- [Governance & Security](#governance--security)
- [Best Practices](#best-practices)

## Architecture

### Traditional Data Lake

```
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAKE ARCHITECTURE                  │
├─────────────────────────────────────────────────────────────┤
│  Raw Zone          │  Curated Zone       │  Business Zone   │
│  • Ingested data   │  • Cleaned data     │  • Aggregated    │
│  • Schema-on-read  │  • Validated        │  • Analytics     │
│  • Immutable       │  • Enriched         │  • ML features   │
├─────────────────────────────────────────────────────────────┤
│  Processing Layer                                             │
│  Batch: Spark, Hive, Presto                                  │
│  Streaming: Flink, Kafka Streams, Spark Streaming            │
├─────────────────────────────────────────────────────────────┤
│  Storage Layer                                               │
│  Object Storage: S3, ADLS, GCS, MinIO                       │
│  File Formats: Parquet, ORC, Delta Lake, Iceberg             │
└─────────────────────────────────────────────────────────────┘
```

### Modern Data Lakehouse

```
┌─────────────────────────────────────────────────────────────┐
│                    DATA LAKEHOUSE                            │
├─────────────────────────────────────────────────────────────┤
│  Transaction Layer (ACID, Schema Evolution)                  │
├─────────────────────────────────────────────────────────────┤
│  Table Format (Delta Lake, Iceberg, Hudi)                   │
├─────────────────────────────────────────────────────────────┤
│  Object Storage (S3, ADLS, GCS)                             │
└─────────────────────────────────────────────────────────────┘
```

## Data Lake Layers

### Bronze Layer (Raw)

```python
# Bronze layer: Raw data ingestion
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("BronzeLayer").getOrCreate()

# Ingest raw JSON
raw_events = spark.read \
    .format("json") \
    .option("mode", "PERMISSIVE") \
    .option("columnNameOfCorruptRecord", "_corrupt_record") \
    .load("s3://raw-data/events/")

# Write to bronze with metadata
bronze_events = raw_events \
    .withColumn("_ingestion_timestamp", current_timestamp()) \
    .withColumn("_source_file", input_file_name()) \
    .withColumn("_batch_id", lit("batch_20240101"))

bronze_events.write \
    .partitionBy("event_date") \
    .parquet("s3://data-lake/bronze/events/")
```

### Silver Layer (Curated)

```python
# Silver layer: Cleaned and validated
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

# Read from bronze
bronze_events = spark.read.parquet("s3://data-lake/bronze/events/")

# Clean and validate
silver_events = bronze_events \
    .filter(col("_corrupt_record").isNull()) \
    .filter(col("event_type").isNotNull()) \
    .withColumn("event_time", to_timestamp("event_time")) \
    .withColumn("event_date", to_date("event_time")) \
    .dropDuplicates(["event_id"]) \
    .withColumn("_cleaned_timestamp", current_timestamp())

silver_events.write \
    .partitionBy("event_date") \
    .parquet("s3://data-lake/silver/events/")
```

### Gold Layer (Business)

```python
# Gold layer: Business-ready aggregates
silver_events = spark.read.parquet("s3://data-lake/silver/events/")

# Create business aggregates
gold_daily_stats = silver_events \
    .groupBy(
        window("event_time", "1 day"),
        "event_type",
        "user_segment"
    ).agg(
        count("*").alias("event_count"),
        countDistinct("user_id").alias("unique_users"),
        sum("revenue").alias("total_revenue"),
        avg("session_duration").alias("avg_session_duration")
    )

gold_daily_stats.write \
    .mode("overwrite") \
    .parquet("s3://data-lake/gold/daily_stats/")
```

## Storage Formats

### Columnar Formats

```python
# Parquet - Most popular columnar format
events.write \
    .mode("overwrite") \
    .option("compression", "snappy") \
    .parquet("s3://data-lake/events/")

# ORC - Optimized Row Columnar
events.write \
    .mode("overwrite") \
    .option("compression", "zlib") \
    .orc("s3://data-lake/events_orc/")

# Comparison
"""
Format  | Compression | Read Speed | Write Speed | Splitting | Schema Evolution
--------|-------------|------------|-------------|-----------|------------------
Parquet | Excellent   | Excellent  | Good        | Excellent | Yes
ORC     | Excellent   | Good       | Good        | Excellent | Yes
Avro    | Good        | Good       | Excellent   | Excellent | Yes
JSON    | Poor        | Fair       | Fair        | Limited   | Yes
CSV     | Poor        | Good       | Good        | Yes       | No
"""
```

### File Organization

```python
# Optimal file sizing (128MB-1GB per file)
events.coalesce(100) \  # Reduce small files
    .write \
    .mode("overwrite") \
    .parquet("s3://data-lake/events/")

# Partition pruning
events.write \
    .partitionBy("year", "month", "day") \
    .parquet("s3://data-lake/events/")

# Bucketing for frequent joins
events.write \
    .bucketBy(256, "user_id") \
    .sortBy("event_time") \
    .saveAsTable("events_bucketed")
```

## Data Organization

### Partitioning Strategy

```python
# Time-based partitioning (most common)
events.write \
    .partitionBy("event_date") \
    .parquet("s3://data-lake/events/")

# Multi-level partitioning
events.write \
    .partitionBy("region", "year", "month") \
    .parquet("s3://data-lake/events/")

# Avoid over-partitioning
# Bad: partition by user_id (millions of partitions)
# Good: partition by event_date, event_type
```

### Data Layout Optimization

```python
# Z-Ordering for multi-column queries
from pyspark.sql.functions import *

events.repartitionByRange("event_date", "event_type") \
    .write \
    .parquet("s3://data-lake/events/")

# Compaction to merge small files
spark.sql("""
    ALTER TABLE events COMPACT 'parquet'
""")
```

### Metadata Management

```python
# Add metadata columns
events_with_meta = events \
    .withColumn("_source_system", lit("order_service")) \
    .withColumn("_ingestion_time", current_timestamp()) \
    .withColumn("_data_domain", lit("commerce")) \
    .withColumn("_schema_version", lit("2.1.0"))
```

## Lakehouse Architecture

### Delta Lake

```python
# Delta Lake operations
from delta.tables import DeltaTable

# Write as Delta
events.write \
    .format("delta") \
    .mode("overwrite") \
    .save("s3://data-lake/events_delta/")

# ACID transactions
delta_table = DeltaTable.forPath(spark, "s3://data-lake/events_delta/")

# Merge (upsert)
delta_table.alias("target").merge(
    new_events.alias("source"),
    "target.event_id = source.event_id"
).whenMatchedUpdateAll() \
 .whenNotMatchedInsertAll() \
 .execute()

# Time travel
old_events = spark.read \
    .format("delta") \
    .option("versionAsOf", 10) \
    .load("s3://data-lake/events_delta/")
```

### Apache Iceberg

```python
# Iceberg table operations
spark.sql("""
    CREATE TABLE catalog.db.events (
        event_id STRING,
        event_time TIMESTAMP,
        event_type STRING,
        user_id STRING
    ) USING iceberg
    PARTITIONED BY (days(event_time))
    LOCATION 's3://data-lake/iceberg/events'
""")

# Schema evolution
spark.sql("""
    ALTER TABLE catalog.db.events 
    ADD COLUMNS (new_column STRING)
""")

# Time travel
spark.sql("""
    SELECT * FROM catalog.db.events 
    TIMESTAMP AS OF '2024-01-01 00:00:00'
""")
```

## Data Lake vs Data Warehouse

| Aspect | Data Lake | Data Warehouse |
|--------|-----------|----------------|
| Data Types | All types | Structured only |
| Schema | Schema-on-read | Schema-on-write |
| Storage Cost | Low (object storage) | High (optimized) |
| Query Speed | Variable | Optimized |
| Use Case | ML, exploration | BI, reporting |
| Flexibility | High | Lower |
| ACID Support | Via table formats | Native |

## Governance & Security

### Data Classification

```python
# Classify data sensitivity
data_classification = {
    "public": ["product_catalog", "marketing_content"],
    "internal": ["sales_reports", "operational_metrics"],
    "confidential": ["customer_pii", "financial_data"],
    "restricted": ["health_records", "payment_data"]
}

# Apply classification labels
events_classified = events \
    .withColumn("_data_classification", 
                classify_columns_udf(schema_columns))
```

### Access Control

```python
# Lakehouse access control (Iceberg example)
spark.sql("""
    CREATE ROLE data_engineer;
    GRANT SELECT ON catalog.db.events TO ROLE data_engineer;
    GRANT INSERT ON catalog.db.events TO ROLE data_engineer;
""")

# Column-level security
spark.sql("""
    CREATE ROW FILTER customer_events 
    FOR ROLE analyst 
    USING (region = current_region())
""")
```

### Data Lineage

```python
# Track data lineage
lineage_metadata = {
    "source": "s3://raw-data/events/",
    "processing": "spark_job_20240101",
    "output": "s3://data-lake/silver/events/",
    "transformation": "clean_and_validate",
    "timestamp": "2024-01-01T00:00:00Z"
}
```

## Best Practices

### 1. Data Quality Gates

```python
# Validate data quality before promoting layers
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, count, when

def validate_silver_layer(df):
    """Validate silver layer data quality"""
    quality_checks = {
        "null_check": df.filter(col("event_id").isNull()).count() == 0,
        "duplicate_check": df.count() == df.dropDuplicates(["event_id"]).count(),
        "freshness_check": df.filter(
            col("_ingestion_timestamp") > current_timestamp() - expr("1 hour")
        ).count() > 0
    }
    return all(quality_checks.values())
```

### 2. Cost Optimization

```python
# Implement lifecycle policies
lifecycle_rules = {
    "bronze": {"retention_days": 90, "storage_class": "STANDARD"},
    "silver": {"retention_days": 365, "storage_class": "STANDARD_IA"},
    "gold": {"retention_days": 730, "storage_class": "GLACIER"}
}

# Use spot instances for batch processing
spark.conf.set("spark.executor.instances", "20")
spark.conf.set("spark.dynamicAllocation.enabled", "true")
```

### 3. Monitoring

```python
# Monitor data lake health
metrics = {
    "file_count": count_files("s3://data-lake/"),
    "total_size_gb": calculate_size("s3://data-lake/"),
    "small_files_count": count_small_files("s3://data-lake/", threshold_mb=128),
    "freshness_hours": calculate_freshness("s3://data-lake/silver/")
}
```

### 4. Documentation

```yaml
data_lake_schema:
  bronze:
    description: "Raw ingested data"
    retention: "90 days"
    access: "data_engineers"
  
  silver:
    description: "Cleaned and validated data"
    retention: "1 year"
    access: "data_engineers, analysts"
  
  gold:
    description: "Business-ready aggregates"
    retention: "2 years"
    access: "all_data_users"
```

## Further Reading

- [Databricks Lakehouse Architecture](https://www.databricks.com/research/lakehouse-a-new-generation-of-open-platforms)
- [Delta Lake Documentation](https://docs.delta.io/)
- [Apache Iceberg Documentation](https://iceberg.apache.org/)
- [Data Mesh Principles](https://martinfowler.com/articles/data-mesh.html)
