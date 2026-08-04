# Data Lake Architecture

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Data Lake Architecture](#data-lake-architecture)
- [Storage Technologies](#storage-technologies)
- [Data Organization](#data-organization)
- [Data Ingestion](#data-ingestion)
- [Data Processing](#data-processing)
- [Data Governance](#data-governance)
- [Data Security](#data-security)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

A data lake is a centralized repository that allows you to store all your
structured and unstructured data at any scale. You can store your data as-is,
without having to first structure the data, and run different types of
analytics from dashboards to real-time big data analytics.

### Key Characteristics

- **Schema on read**: Data schema applied when reading, not writing
- **Raw storage**: Store data in native format
- **Scalable**: Petabyte-scale storage
- **Cost-effective**: Commodity hardware or cloud storage
- **Flexible**: Support for all data types

### Data Lake vs Data Warehouse

| Feature | Data Lake | Data Warehouse |
|---------|-----------|----------------|
| Data Types | All types | Structured only |
| Schema | Schema on read | Schema on write |
| Processing | ELT | ETL |
| Cost | Lower | Higher |
| Flexibility | High | Low |
| Performance | Variable | Optimized |
| Users | Data Engineers | Business Analysts |

### When to Use Data Lake

- Store diverse data types (structured, semi-structured, unstructured)
- Perform big data analytics and machine learning
- Cost-effective storage for large volumes of data
- Data exploration and discovery
- Support for multiple processing frameworks

---

## Core Concepts

### Data Lake Layers

```
┌─────────────────────────────────────────────────────────────┐
│                     Data Lake Layers                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                   Serving Layer                         ││
│  │   (Dashboards, Reports, APIs, ML Models)                ││
│  └─────────────────────────────────────────────────────────┘│
│                                                              │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                   Processing Layer                      ││
│  │   (ETL, Batch Processing, Stream Processing)            ││
│  └─────────────────────────────────────────────────────────┘│
│                                                              │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                   Storage Layer                         ││
│  │   (Raw, Cleansed, Curated, Aggregated)                  ││
│  └─────────────────────────────────────────────────────────┘│
│                                                              │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                   Ingestion Layer                        ││
│  │   (Batch, Streaming, Real-time)                         ││
│  └─────────────────────────────────────────────────────────┘│
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Data Zones

```
┌─────────────────────────────────────────────────────────────┐
│                      Data Zones                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐│
│  │   Raw Zone  │  │  Cleansed   │  │   Curated Zone      ││
│  │             │  │    Zone     │  │                     ││
│  │  (Landing)  │  │ (Standardized) │  │  (Business-ready) ││
│  │             │  │             │  │                     ││
│  │  - Original │  │  - Cleaned  │  │  - Modeled          ││
│  │  - Unprocessed │  - Validated │  │  - Aggregated      ││
│  │  - Immutable │  - Conformed  │  │  - Optimized        ││
│  └─────────────┘  └─────────────┘  └─────────────────────┘│
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐│
│  │  Sandbox    │  │  Archive    │  │   Discovery Zone    ││
│  │   Zone      │  │    Zone     │  │                     ││
│  │             │  │             │  │                     ││
│  │  - Testing  │  │  - Cold     │  │  - Ad-hoc analysis  ││
│  │  - Experimentation │  - Compliance │  - Data exploration ││
│  │  - Development │  - Long-term │  - Data science      ││
│  └─────────────┘  └─────────────┘  └─────────────────────┘│
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Data Lake Architecture

### Reference Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                        Data Lake Architecture                         │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Data Sources                                                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │ Databases │ │  Files   │ │  APIs    │ │  Streams │              │
│  │(RDBMS,NoSQL)│ │(CSV,JSON)│ │(REST,GraphQL)│ │(Kafka,Flume)│              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                                                                       │
│  Ingestion Layer                                                     │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  Batch Ingestion  │  Stream Ingestion  │  Real-time Ingestion│   │
│  │  (Sqoop, NiFi)    │  (Kafka, Flume)    │  (Flink, Spark)     │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  Storage Layer (HDFS, S3, ADLS, GCS)                                 │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐          │   │
│  │  │   Raw   │ │Cleansed │ │ Curated │ │ Archive │          │   │
│  │  │  Zone   │ │  Zone   │ │  Zone   │ │  Zone   │          │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘          │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  Processing Layer                                                    │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │   │
│  │  │  Spark   │ │  Hive    │ │  Presto  │ │  Flink   │       │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  Serving Layer                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │   │
│  │  │  BI Tools│ │   APIs   │ │   ML     │ │Analytics │       │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  Governance & Security                                               │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  Metadata │ Data Catalog │ Access Control │ Audit Logging   │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Storage Technologies

### Cloud Storage

```python
# AWS S3
s3_path = "s3://bucket-name/data/"

# Azure Data Lake Storage (ADLS)
adls_path = "abfss://container@account.dfs.core.windows.net/data/"

# Google Cloud Storage (GCS)
gcs_path = "gs://bucket-name/data/"

# HDFS
hdfs_path = "hdfs://namenode:8020/data/"
```

### Storage Formats

```python
# Parquet (recommended for analytics)
df.write.parquet("s3://bucket/data.parquet", compression="snappy")

# ORC (optimized for Hive)
df.write.orc("s3://bucket/data.orc", compression="zlib")

# Delta Lake (ACID transactions)
df.write.format("delta").save("s3://bucket/data.delta")

# Iceberg (open table format)
df.write.format("iceberg").save("s3://bucket/data.iceberg")

# Avro (row-based, good for streaming)
df.write.format("avro").save("s3://bucket/data.avro")
```

### Storage Optimization

```python
# Partitioning
df.write.partitionBy("year", "month", "day").parquet("s3://bucket/data/")

# Bucketing
df.write.bucketBy(100, "user_id").sortBy("user_id").saveAsTable("bucketed_table")

# Compression
df.write.parquet("s3://bucket/data/", compression="snappy")

# File size optimization
# Target: 128MB-256MB per file
df.repartition(100).write.parquet("s3://bucket/data/")
```

---

## Data Organization

### Directory Structure

```
s3://data-lake/
├── raw/
│   ├── source_system_1/
│   │   ├── year=2024/
│   │   │   ├── month=01/
│   │   │   │   ├── day=01/
│   │   │   │   │   ├── data_001.parquet
│   │   │   │   │   ├── data_002.parquet
│   │   │   │   │   └── ...
│   │   │   │   └── ...
│   │   │   └── ...
│   │   └── ...
│   └── source_system_2/
│       └── ...
├── cleansed/
│   ├── table_name/
│   │   ├── year=2024/
│   │   │   ├── month=01/
│   │   │   │   ├── data_001.parquet
│   │   │   │   └── ...
│   │   │   └── ...
│   │   └── ...
│   └── ...
├── curated/
│   ├── table_name/
│   │   ├── year=2024/
│   │   │   ├── month=01/
│   │   │   │   ├── data_001.parquet
│   │   │   │   └── ...
│   │   │   └── ...
│   │   └── ...
│   └── ...
└── archive/
    └── ...
```

### Naming Conventions

```python
# File naming
# Pattern: {source}_{table}_{date}_{batch_id}.parquet
# Example: sales_orders_20240101_001.parquet

# Directory naming
# Pattern: {layer}/{source_system}/{table_name}/year={YYYY}/month={MM}/day={DD}/
# Example: cleansed/sales/orders/year=2024/month=01/day=01/

# Table naming
# Pattern: {source}_{domain}_{entity}
# Example: sales_orders, crm_contacts, erp_products
```

### Data Lineage

```python
# Track data lineage
lineage = {
    "source": "s3://raw/sales/orders/",
    "transformation": "clean_and_enrich",
    "destination": "s3://curated/sales/orders/",
    "timestamp": "2024-01-01T00:00:00Z",
    "batch_id": "batch_001",
    "schema_version": "1.0"
}
```

---

## Data Ingestion

### Batch Ingestion

```python
# Scheduled batch ingestion
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("BatchIngestion").getOrCreate()

# Read from source
source_df = spark.read \
    .format("jdbc") \
    .option("url", "jdbc:postgresql://source-db/db") \
    .option("dbtable", "orders") \
    .option("user", "user") \
    .option("password", "password") \
    .load()

# Write to data lake (raw zone)
source_df.write \
    .mode("overwrite") \
    .partitionBy("order_date") \
    .parquet("s3://data-lake/raw/sales/orders/")
```

### Streaming Ingestion

```python
# Real-time streaming ingestion
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("StreamingIngestion").getOrCreate()

# Read from Kafka
stream_df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "orders") \
    .load()

# Parse and transform
parsed_df = stream_df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Write to data lake
query = parsed_df.writeStream \
    .outputMode("append") \
    .format("parquet") \
    .option("path", "s3://data-lake/raw/streaming/orders/") \
    .option("checkpointLocation", "s3://checkpoints/streaming/orders/") \
    .start()

query.awaitTermination()
```

### Incremental Ingestion

```python
# Incremental ingestion using watermark
def incremental_ingestion(spark, source_path, target_path, watermark_column):
    # Read last processed watermark
    last_watermark = get_last_watermark(target_path)

    # Read new data
    new_data = spark.read \
        .format("parquet") \
        .load(source_path) \
        .filter(col(watermark_column) > last_watermark)

    # Write to target
    if new_data.count() > 0:
        new_data.write \
            .mode("append") \
            .partitionBy("date") \
            .parquet(target_path)

        # Update watermark
        new_watermark = new_data.agg(max(watermark_column)).collect()[0][0]
        save_watermark(target_path, new_watermark)
```

---

## Data Processing

### Batch Processing

```python
# Spark batch processing
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("BatchProcessing").getOrCreate()

# Read raw data
raw_df = spark.read.parquet("s3://data-lake/raw/sales/orders/")

# Clean and transform
cleaned_df = raw_df \
    .dropDuplicates() \
    .na.drop(subset=["order_id", "customer_id"]) \
    .withColumn("order_date", to_date(col("order_date"))) \
    .withColumn("amount", col("amount").cast(DoubleType())) \
    .filter(col("amount") > 0)

# Enrich with customer data
customer_df = spark.read.parquet("s3://data-lake/curated/customers/")
enriched_df = cleaned_df \
    .join(customer_df, "customer_id", "left") \
    .withColumn("customer_segment",
        when(col("total_purchases") > 1000, "Premium")
        .when(col("total_purchases") > 100, "Regular")
        .otherwise("New"))

# Write to curated zone
enriched_df.write \
    .mode("overwrite") \
    .partitionBy("order_date") \
    .parquet("s3://data-lake/curated/sales/orders/")
```

### Stream Processing

```python
# Spark Structured Streaming
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("StreamProcessing").getOrCreate()

# Read from Kafka
stream_df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Process stream
processed_df = stream_df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*") \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "5 minutes"),
        "event_type"
    ) \
    .count()

# Write to sink
query = processed_df.writeStream \
    .outputMode("update") \
    .format("parquet") \
    .option("path", "s3://data-lake/processed/events/") \
    .option("checkpointLocation", "s3://checkpoints/events/") \
    .start()

query.awaitTermination()
```

---

## Data Governance

### Metadata Management

```python
# Schema registry
schema_registry = {
    "sales_orders": {
        "version": "1.0",
        "schema": {
            "order_id": "string",
            "customer_id": "string",
            "order_date": "date",
            "amount": "double",
            "status": "string"
        },
        "partition_columns": ["order_date"],
        "retention_days": 365
    }
}

# Data catalog
data_catalog = {
    "sales_orders": {
        "description": "Sales orders from e-commerce platform",
        "owner": "data-engineering-team",
        "classification": "internal",
        "retention_policy": "3 years",
        "sla": "daily"
    }
}
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
    "not_null": ["order_id", "customer_id", "order_date"],
    "range": {
        "amount": (0, 1000000)
    }
}

# Validate
violations = validate_data(df, rules)
if violations:
    raise ValueError(f"Data quality violations: {violations}")
```

### Access Control

```python
# Role-based access control
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

# Column-level security
column_security = {
    "customers": {
        "pii_columns": ["email", "phone", "address"],
        "allowed_roles": ["data-engineers", "security-team"]
    }
}
```

---

## Data Security

### Encryption

```python
# Encryption at rest
# AWS S3
s3_encryption = {
    "sse_algorithm": "AES256",
    "kms_key_id": "arn:aws:kms:region:account:key/key-id"
}

# Azure ADLS
adls_encryption = {
    "encryption_type": "ServiceManaged",
    "key_vault_uri": "https://vault.vault.azure.net/"
}

# GCS
gcs_encryption = {
    "default_kms_key_name": "projects/project/locations/global/keyRings/kr/cryptoKeys/k"
}

# Encryption in transit
# Use HTTPS/TLS for all data transfers
```

### Data Masking

```python
from pyspark.sql.functions import when, md5, concat, lit

# Mask PII data
def mask_email(df):
    return df.withColumn(
        "masked_email",
        when(
            col("email").isNotNull(),
            concat(
                substr(col("email"), 1, 2),
                lit("***@"),
                substring_INDEX(col("email"), "@", -1)
            )
        ).otherwise(None)
    )

def mask_phone(df):
    return df.withColumn(
        "masked_phone",
        when(
            col("phone").isNotNull(),
            concat(lit("***-***-"), substr(col("phone"), -4, 4))
        ).otherwise(None)
    )
```

### Audit Logging

```python
# Audit logging
audit_log = {
    "timestamp": "2024-01-01T00:00:00Z",
    "user": "data-engineer@example.com",
    "action": "read",
    "resource": "s3://data-lake/curated/sales/orders/",
    "ip_address": "192.168.1.100",
    "user_agent": "spark/3.5.0",
    "result": "success"
}
```

---

## Performance Optimization

### Partitioning Strategy

```python
# Partition by date for time-series data
df.write.partitionBy("year", "month", "day").parquet("s3://data/")

# Partition by high-cardinality column
df.write.partitionBy("region").parquet("s3://data/")

# Avoid over-partitioning
# Rule: Each partition should be at least 128MB
partition_count = total_data_size / (128 * 1024 * 1024)
```

### File Format Optimization

```python
# Use Parquet for analytics
df.write.parquet("s3://data/", compression="snappy")

# Use ORC for Hive workloads
df.write.orc("s3://data/", compression="zlib")

# Optimize file size
# Target: 128MB-256MB per file
df.repartition(100).write.parquet("s3://data/")

# Use columnar compression
df.write.parquet("s3://data/", compression="zstd")
```

### Query Optimization

```python
# Use partition pruning
df = spark.read.parquet("s3://data/")
filtered_df = df.filter(col("date") == "2024-01-01")  # Only reads relevant partition

# Use predicate pushdown
df = spark.read.parquet("s3://data/")
filtered_df = df.filter(col("amount") > 100)  # Pushed down to reader

# Use column pruning
df = spark.read.parquet("s3://data/")
selected_df = df.select("col1", "col2")  # Only reads needed columns

# Cache frequently used data
df.cache()
```

### Caching Strategy

```python
# Cache hot data
hot_data = spark.read.parquet("s3://data/hot/")
hot_data.cache()

# Use appropriate storage level
from pyspark import StorageLevel
hot_data.persist(StorageLevel.MEMORY_AND_DISK)

# Monitor cache usage
print(hot_data.storageLevel)
print(hot_data.is_cached)

# Unpersist when no longer needed
hot_data.unpersist()
```

---

## Best Practices

### Data Organization

1. **Use consistent naming conventions**: Clear, descriptive names
2. **Implement data zones**: Raw, cleansed, curated, archive
3. **Partition wisely**: By date for time-series data
4. **Optimize file sizes**: 128MB-256MB per file

### Data Quality

1. **Validate data**: Check for completeness, accuracy, consistency
2. **Track data lineage**: Know where data came from and how it was transformed
3. **Monitor data freshness**: Ensure data is up-to-date
4. **Implement data contracts**: Define expected schemas and quality

### Security

1. **Encrypt data**: At rest and in transit
2. **Implement access control**: Role-based access control
3. **Mask sensitive data**: PII and confidential information
4. **Audit access**: Log all data access and modifications

### Cost Optimization

1. **Use lifecycle policies**: Move old data to cheaper storage
2. **Compress data**: Use appropriate compression algorithms
3. **Optimize queries**: Avoid full table scans
4. **Monitor costs**: Track storage and compute costs

---

## References

- [Data Lake Architecture](https://www.databricks.com/glossary/data-lake)
- [AWS Data Lake](https://aws.amazon.com/big-data/what-is-a-data-lake/)
- [Azure Data Lake](https://azure.microsoft.com/en-us/solutions/data-lake/)
- [Google Cloud Data Lake](https://cloud.google.com/learn/what-is-a-data-lake)
- [Data Lake vs Data Warehouse](https://www.talend.com/resources/data-lake-vs-data-warehouse/)
- [Building a Data Lake](https://www.oreilly.com/library/view/building-a-data/9781492028161/)
