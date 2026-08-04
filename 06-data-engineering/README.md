# 06 - Data Engineering

Data engineering encompasses the practices, architectures, and technologies used to collect, process, store, and serve data at scale. This module covers the complete data engineering lifecycle from ingestion to analytics.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Data Pipelines](#data-pipelines)
- [ETL/ELT Processes](#etl-elt-processes)
- [Streaming vs Batch Processing](#streaming-vs-batch-processing)
- [Data Lakes & Warehouses](#data-lakes--warehouses)
- [Orchestration](#orchestration)
- [Data Governance](#governance)
- [Best Practices](#best-practices)

## Overview

Data engineering is the discipline of designing, building, and maintaining the infrastructure that enables data to be collected, transformed, and made available for analysis. It bridges the gap between raw data storage and data consumption by analysts, data scientists, and business users.

### Key Responsibilities

```
┌─────────────────────────────────────────────────────────────────┐
│                     DATA ENGINEERING                            │
├─────────────────────────────────────────────────────────────────┤
│  Ingestion → Storage → Processing → Serving → Governance       │
│                                                                 │
│  • Data Collection    • Data Quality     • Data Cataloging     │
│  • Data Ingestion     • Data Cleaning    • Data Lineage        │
│  • Data Storage       • Data Transform   • Data Security       │
│  • Data Processing    • Data Enrichment  • Data Compliance     │
│  • Data Serving       • Data Validation  • Data Monitoring     │
└─────────────────────────────────────────────────────────────────┘
```

### Data Engineering Stack

```
┌──────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                         │
│  Dashboards │ Reports │ ML Models │ APIs │ Applications     │
├──────────────────────────────────────────────────────────────┤
│                    PROCESSING LAYER                          │
│  Spark │ Flink │ Kafka Streams │ Beam │ Storm │ Trino       │
├──────────────────────────────────────────────────────────────┤
│                    STORAGE LAYER                             │
│  HDFS │ S3 │ Delta Lake │ Iceberg │ Hudi │ Snowflake        │
├──────────────────────────────────────────────────────────────┤
│                    INGESTION LAYER                           │
│  Kafka │ Pulsar │ NiFi │ Airbyte │ Debezium │ Flink CDC    │
├──────────────────────────────────────────────────────────────┤
│                    ORCHESTRATION LAYER                       │
│  Airflow │ Luigi │ Dagster │ Prefect │ Argo Workflows      │
├──────────────────────────────────────────────────────────────┤
│                    GOVERNANCE LAYER                          │
│  Data Catalog │ Lineage │ Quality │ Security │ Compliance   │
└──────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Data Modeling

Data modeling defines how data is organized, stored, and accessed:

- **Dimensional Modeling**: Star schema, snowflake schema for analytics
- **Data Vault**: Hub-and-spoke model for enterprise data warehouses
- **One Big Table (OBT)**: Denormalized flat tables for query performance

### Data Quality Dimensions

```yaml
data_quality:
  completeness: "All required data is present"
  accuracy: "Data correctly represents the real-world entity"
  consistency: "Data is consistent across different systems"
  timeliness: "Data is available when needed"
  validity: "Data conforms to defined formats and rules"
  uniqueness: "No duplicate records exist"
```

### Data Processing Patterns

- **Lambda Architecture**: Batch + speed layers for historical + real-time
- **Kappa Architecture**: Stream-only processing for all workloads
- **ELT**: Extract, Load, Transform - modern cloud-native pattern
- **ETL**: Extract, Transform, Load - traditional data warehouse pattern

## Data Pipelines

### Pipeline Design Principles

```python
# Example: Idempotent pipeline stage
class IdempotentStage:
    def __init__(self, checkpoint_store):
        self.checkpoint_store = checkpoint_store

    def process(self, batch_id, data):
        if self.checkpoint_store.is_completed(batch_id):
            return self.checkpoint_store.get_result(batch_id)

        result = self.transform(data)
        self.checkpoint_store.mark_completed(batch_id, result)
        return result
```

### Pipeline Reliability

- **Exactly-once semantics**: Ensures each record is processed exactly once
- **At-least-once semantics**: May process duplicates but never misses
- **At-most-once semantics**: May miss records but never duplicates

### Fault Tolerance

- **Checkpointing**: Periodic state snapshots for recovery
- **WAL (Write-Ahead Log)**: Durability guarantee for state changes
- **Dead Letter Queues**: Capture failed messages for later processing

## ETL/ELT Processes

### ETL vs ELT

```
ETL (Traditional):
Source → Extract → Transform → Load → Target

ELT (Modern Cloud):
Source → Extract → Load → Transform → Target
```

### Data Transformation Types

| Type | Description | Example |
|------|-------------|---------|
| Cleaning | Remove errors, normalize | Standardize dates |
| Enrichment | Add external data | Geolocation lookup |
| Aggregation | Summarize data | Daily totals |
| Normalization | Scale values | 0-1 normalization |
| Denormalization | Flatten for queries | Join tables |
| Filtering | Remove unwanted data | Active users only |
| Deduplication | Remove duplicates | Unique records |

## Streaming vs Batch Processing

### Batch Processing

```python
# Apache Spark batch processing example
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("BatchETL").getOrCreate()

# Read source data
raw_data = spark.read.parquet("s3://data-lake/raw/events/")

# Transform
cleaned = raw_data.filter("event_type IS NOT NULL") \
                  .withColumn("processed_date", current_date())

# Write to target
cleaned.write.partitionBy("event_date") \
            .parquet("s3://data-lake/processed/events/")
```

### Stream Processing

```python
# Apache Flink streaming example
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.table import StreamTableEnvironment

env = StreamExecutionEnvironment.get_execution_environment()
t_env = StreamTableEnvironment.create(env)

# Define source from Kafka
t_env.execute_sql("""
    CREATE TABLE events (
        event_id STRING,
        event_type STRING,
        event_time TIMESTAMP(3),
        payload STRING,
        WATERMARK FOR event_time AS event_time - INTERVAL '5' SECOND
    ) WITH (
        'connector' = 'kafka',
        'topic' = 'events',
        'properties.bootstrap.servers' = 'kafka:9092',
        'format' = 'json'
    )
""")

# Process with windowing
t_env.execute_sql("""
    SELECT
        event_type,
        TUMBLE_START(event_time, INTERVAL '1' MINUTE) as window_start,
        COUNT(*) as event_count
    FROM events
    GROUP BY event_type, TUMBLE(event_time, INTERVAL '1' MINUTE)
""")
```

### Comparison

| Aspect | Batch | Streaming |
|--------|-------|-----------|
| Latency | Minutes to hours | Milliseconds to seconds |
| Throughput | High (optimized) | Moderate (real-time) |
| Complexity | Lower | Higher |
| Cost | Lower per GB | Higher per GB |
| Use case | Analytics, reporting | Monitoring, alerts |
| State management | Stateless | Stateful |

## Data Lakes & Warehouses

### Data Lake Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    DATA LAKE                            │
├─────────────────────────────────────────────────────────┤
│  Bronze Layer (Raw)     │ Original data, immutable     │
│  Silver Layer (Curated) │ Cleaned, validated           │
│  Gold Layer (Business)  │ Aggregated, business-ready   │
└─────────────────────────────────────────────────────────┘
```

### Data Warehouse Concepts

```sql
-- Star Schema Example
-- Fact Table
CREATE TABLE fact_sales (
    sale_id BIGINT PRIMARY KEY,
    date_key INT REFERENCES dim_date(date_key),
    product_key INT REFERENCES dim_product(product_key),
    customer_key INT REFERENCES dim_customer(customer_key),
    store_key INT REFERENCES dim_store(store_key),
    quantity INT,
    amount DECIMAL(10,2),
    discount DECIMAL(5,2)
);

-- Dimension Tables
CREATE TABLE dim_product (
    product_key INT PRIMARY KEY,
    product_id VARCHAR(50),
    product_name VARCHAR(200),
    category VARCHAR(100),
    brand VARCHAR(100)
);

CREATE TABLE dim_date (
    date_key INT PRIMARY KEY,
    full_date DATE,
    year INT,
    quarter INT,
    month INT,
    day_of_week VARCHAR(10)
);
```

### Lakehouse Architecture

The lakehouse combines data lake flexibility with data warehouse reliability:

- **ACID transactions** on data lake storage
- **Schema enforcement** with evolution support
- **Time travel** for data versioning
- **Unified batch and streaming** processing

## Orchestration

### Apache Airflow

```python
from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime

default_args = {
    'owner': 'data-team',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'retries': 2,
    'retry_delay': timedelta(minutes=5),
}

with DAG('daily_etl', default_args=default_args,
         schedule_interval='@daily') as dag:

    extract = PythonOperator(
        task_id='extract',
        python_callable=extract_data,
    )

    transform = PythonOperator(
        task_id='transform',
        python_callable=transform_data,
    )

    load = PythonOperator(
        task_id='load',
        python_callable=load_data,
    )

    extract >> transform >> load
```

### Dagster

```python
from dagster import job, op, HourlyPartitionsDefinition

@op
def extract():
    return fetch_data_from_source()

@op
def transform(raw_data):
    return clean_and_enrich(raw_data)

@op
def load(transformed_data):
    write_to_warehouse(transformed_data)

@job(partitions_def=HourlyPartitionsDefinition(start_date="2024-01-01"))
def daily_etl():
    load(transform(extract()))
```

## Governance

### Data Governance Framework

```yaml
governance:
  cataloging:
    - Asset inventory
    - Metadata management
    - Search and discovery

  lineage:
    - Data flow tracking
    - Impact analysis
    - Compliance audit

  quality:
    - Automated testing
    - Monitoring dashboards
    - Alerting policies

  security:
    - Access controls
    - Encryption
    - Data masking
    - PII detection

  compliance:
    - GDPR
    - HIPAA
    - SOC 2
    - CCPA
```

### Data Catalog

```python
# Example metadata schema
metadata_schema = {
    "asset_name": "string",
    "asset_type": "string",  # table, view, pipeline, model
    "owner": "string",
    "description": "string",
    "tags": ["string"],
    "schema": {
        "columns": [
            {
                "name": "string",
                "type": "string",
                "description": "string",
                "nullable": "boolean"
            }
        ]
    },
    "lineage": {
        "upstream": ["string"],
        "downstream": ["string"]
    },
    "quality_metrics": {
        "completeness": "float",
        "accuracy": "float",
        "freshness": "timestamp"
    }
}
```

## Best Practices

### Pipeline Design

1. **Idempotency**: Ensure pipelines can be safely re-run
2. **Observability**: Include logging, metrics, and tracing
3. **Backpressure handling**: Gracefully handle load spikes
4. **Data contracts**: Define schemas between producer and consumer
5. **Testing**: Unit, integration, and data quality tests

### Performance Optimization

```python
# Partitioning strategy
def optimize_partitioning(df, partition_columns):
    """Optimize partitioning based on data distribution"""
    for col in partition_columns:
        distinct_count = df.select(col).distinct().count()
        if distinct_count > 10000:
            print(f"Consider hashing {col} for better distribution")
        elif distinct_count < 10:
            print(f"Consider removing {col} from partitioning")

# File format optimization
def optimize_file_sizes(df, target_size_mb=128):
    """Optimize output file sizes"""
    row_count = df.count()
    estimated_size_mb = (row_count * 100) / (1024 * 1024)  # rough estimate
    optimal_partitions = max(1, int(estimated_size_mb / target_size_mb))
    return df.repartition(optimal_partitions)
```

### Monitoring

- **Pipeline metrics**: Duration, throughput, error rates
- **Data metrics**: Completeness, freshness, volume
- **Resource metrics**: CPU, memory, disk, network
- **Business metrics**: Data SLAs, user satisfaction

### Cost Optimization

- Use spot/preemptible instances for batch workloads
- Implement auto-scaling based on workload patterns
- Optimize file formats (Parquet, ORC) and compression
- Partition and bucket data for efficient queries
- Use lifecycle policies for data retention

## Tools & Technologies

| Category | Tools |
|----------|-------|
| Processing | Spark, Flink, Beam, Trino, Presto |
| Storage | S3, ADLS, GCS, HDFS, Delta Lake, Iceberg |
| Ingestion | Kafka, Pulsar, NiFi, Airbyte, Debezium |
| Orchestration | Airflow, Dagster, Prefect, Luigi |
| Streaming | Kafka Streams, Flink, Storm, Samza |
| Warehousing | Snowflake, BigQuery, Redshift, Databricks |
| Quality | Great Expectations, Monte Carlo, dbt |
| Catalog | DataHub, Amundsen, OpenMetadata, Atlas |

## Further Reading

- [Data Pipelines](pipelines/) - Pipeline architectures and patterns
- [ETL Processes](etl/) - Extract, Transform, Load patterns
- [Streaming](streaming/) - Stream processing technologies
- [Data Lakes](data-lakes/) - Data lake architecture
- [Data Warehouses](data-warehouses/) - Warehouse design
- [Orchestration](orchestration/) - Workflow orchestration
