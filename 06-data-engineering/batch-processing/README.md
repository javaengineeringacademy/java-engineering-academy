# Batch Processing

## Table of Contents

- [Overview](#overview)
- [Batch Processing Concepts](#batch-processing-concepts)
- [Batch Processing Architecture](#batch-processing-architecture)
- [Batch Processing Tools](#batch-processing-tools)
- [Batch Processing Patterns](#batch-processing-patterns)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Batch processing is a data processing approach that processes data in large
groups at scheduled intervals. It is designed to handle high volumes of data
that don't require immediate processing.

### Key Characteristics

- **Scheduled execution**: Runs at defined intervals
- **High throughput**: Processes large volumes of data
- **Latency**: Higher latency (minutes to hours)
- **Resource efficient**: Optimized for throughput over latency
- **Fault tolerant**: Can recover from failures

### When to Use Batch Processing

- Daily/weekly/monthly reporting
- ETL pipelines for data warehousing
- Large-scale data transformations
- Historical data analysis
- Compliance and regulatory reporting

### Batch vs Stream Processing

| Feature | Batch Processing | Stream Processing |
|---------|-----------------|-------------------|
| Latency | Minutes to hours | Milliseconds to seconds |
| Data Volume | High (TB-PB) | Variable |
| Processing | Scheduled | Continuous |
| Complexity | Lower | Higher |
| Cost | Lower | Higher |
| Use Case | Reporting, analytics | Real-time monitoring |

---

## Batch Processing Concepts

### Batch Job Structure

```python
# Typical batch job structure
class BatchJob:
    def __init__(self, config):
        self.config = config
        self.spark = None

    def initialize(self):
        """Initialize Spark session"""
        self.spark = SparkSession.builder \
            .appName(self.config['app_name']) \
            .getOrCreate()

    def extract(self):
        """Extract data from sources"""
        pass

    def transform(self, data):
        """Transform data"""
        pass

    def load(self, data):
        """Load data to target"""
        pass

    def validate(self):
        """Validate results"""
        pass

    def cleanup(self):
        """Cleanup resources"""
        if self.spark:
            self.spark.stop()

    def run(self):
        """Execute batch job"""
        try:
            self.initialize()
            data = self.extract()
            transformed = self.transform(data)
            self.load(transformed)
            self.validate()
        finally:
            self.cleanup()
```

### Batch Scheduling

```python
# Apache Airflow DAG
from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'data-engineering',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'email_on_failure': True,
    'email': ['team@example.com'],
    'retries': 3,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'daily_etl',
    default_args=default_args,
    description='Daily ETL pipeline',
    schedule_interval='@daily',
    catchup=False,
)

def extract_task():
    # Extract logic
    pass

def transform_task():
    # Transform logic
    pass

def load_task():
    # Load logic
    pass

extract = PythonOperator(
    task_id='extract',
    python_callable=extract_task,
    dag=dag,
)

transform = PythonOperator(
    task_id='transform',
    python_callable=transform_task,
    dag=dag,
)

load = PythonOperator(
    task_id='load',
    python_callable=load_task,
    dag=dag,
)

extract >> transform >> load
```

---

## Batch Processing Architecture

### Architecture Patterns

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Batch Processing Architecture                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Data Sources                                                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │ Databases │ │  Files   │ │  APIs    │ │  Streams │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Orchestration Layer      │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │   │
│  │  │ Airflow  │ │  Prefect │ │  Dagster │ │ Luigi    │       │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Processing Layer        │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │   │
│  │  │  Spark   │ │  Hive    │ │  Presto  │ │  Flink   │       │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Storage Layer           │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │   │
│  │  │   S3     │ │   HDFS   │ │   ADLS   │ │   GCS    │       │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Monitoring Layer        │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Metrics │ Logs │ Alerts │ Dashboards                       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Lambda Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Lambda Architecture                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Data Sources                                                        │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  All Data Sources                                             │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  ┌────────────────────────┼────────────────────────────────────┐   │
│  │                        │                                     │   │
│  │  Batch Layer           │    Speed Layer                      │   │
│  │  ┌─────────────────────┴─────────────────────┐             │   │
│  │  │  - Process all historical data              │             │   │
│  │  │  - High latency, high throughput            │             │   │
│  │  │  - Batch jobs (Spark, MapReduce)            │             │   │
│  │  │  - Complete and accurate                    │             │   │
│  │  └─────────────────────┬─────────────────────┘             │   │
│  │                        │                                     │   │
│  │                        │    ┌─────────────────────────────┐ │   │
│  │                        │    │  - Process real-time data    │ │   │
│  │                        │    │  - Low latency               │ │   │
│  │                        │    │  - Stream processing         │ │   │
│  │                        │    │  - Approximate results       │ │   │
│  │                        │    └─────────────────────────────┘ │   │
│  │                        │                                     │   │
│  │  Serving Layer         │                                     │   │
│  │  ┌─────────────────────┴─────────────────────┐             │   │
│  │  │  - Merge batch and speed views              │             │   │
│  │  │  - Query serving                            │             │   │
│  │  │  - Low-latency access                       │             │   │
│  │  └────────────────────────────────────────────┘             │   │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Kappa Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Kappa Architecture                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Data Sources                                                        │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  All Data Sources                                             │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Message Broker           │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐                                                  │  │
│  │  │  Kafka   │                                                  │  │
│  │  └──────────┘                                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Stream Processing        │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐                     │  │
│  │  │  Flink   │ │  Spark   │ │  Kafka   │                     │  │
│  │  │          │ │Streaming │ │ Streams  │                     │  │
│  │  └──────────┘ └──────────┘ └──────────┘                     │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Serving Layer            │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  - Real-time views                                            │  │
│  │  - Materialized views                                         │  │
│  │  - Query serving                                              │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Batch Processing Tools

### Apache Spark

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("BatchProcessing").getOrCreate()

# Read data
df = spark.read.parquet("s3://data/input/")

# Transform
transformed_df = df \
    .dropDuplicates() \
    .na.drop() \
    .withColumn("date", to_date(col("date"))) \
    .filter(col("amount") > 0)

# Write
transformed_df.write.mode("overwrite").parquet("s3://data/output/")
```

### Apache Hive

```sql
-- Create table
CREATE TABLE IF NOT EXISTS orders (
    order_id STRING,
    customer_id STRING,
    order_date DATE,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC;

-- Load data
LOAD DATA INPATH '/data/orders' INTO TABLE orders;

-- Query
SELECT year, month, SUM(amount) as total_sales
FROM orders
GROUP BY year, month;
```

### Apache Flink

```java
// Flink batch processing
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

// Read from file
DataStream<String> input = env.readTextFile("s3://data/input/");

// Transform
DataStream<Order> orders = input
    .map(line -> parseOrder(line))
    .filter(order -> order.getAmount() > 0);

// Write
orders.writeAsText("s3://data/output/");

env.execute("Batch Processing Job");
```

---

## Batch Processing Patterns

### Full Refresh Pattern

```python
# Overwrite entire target
def full_refresh(spark, source_path, target_path):
    """Full refresh pattern"""
    # Read source
    source_df = spark.read.parquet(source_path)

    # Write to target (overwrite)
    source_df.write.mode("overwrite").parquet(target_path)
```

### Incremental Pattern

```python
# Process only new/changed data
def incremental_load(spark, source_path, target_path, watermark_path):
    """Incremental load pattern"""
    # Get last watermark
    last_watermark = get_watermark(watermark_path)

    # Read new data
    new_data = spark.read.parquet(source_path) \
        .filter(col("modified_date") > last_watermark)

    # Write to target (append)
    if new_data.count() > 0:
        new_data.write.mode("append").parquet(target_path)

        # Update watermark
        new_watermark = new_data.agg(max("modified_date")).collect()[0][0]
        save_watermark(watermark_path, new_watermark)
```

### Upsert Pattern

```python
# Insert or update records
def upsert_load(spark, source_df, target_path, key_columns):
    """Upsert pattern using Delta Lake"""
    from delta import DeltaTable

    # Load target table
    delta_table = DeltaTable.forPath(spark, target_path)

    # Create merge condition
    merge_condition = " AND ".join([f"target.{col} = source.{col}" for col in key_columns])

    # Merge
    delta_table.alias("target").merge(
        source_df.alias("source"),
        merge_condition
    ).whenMatchedUpdateAll() \
     .whenNotMatchedInsertAll() \
     .execute()
```

### Snapshot Pattern

```python
# Create daily snapshots
def snapshot_load(spark, source_df, target_path, snapshot_date):
    """Snapshot pattern"""
    # Add snapshot date column
    snapshot_df = source_df.withColumn("snapshot_date", lit(snapshot_date))

    # Write to partitioned target
    snapshot_df.write.mode("overwrite") \
        .partitionBy("snapshot_date") \
        .parquet(target_path)
```

---

## Performance Optimization

### Partitioning

```python
# Partition by date for time-series data
df.write.partitionBy("year", "month", "day").parquet("s3://data/")

# Partition by high-cardinality column
df.write.partitionBy("region").parquet("s3://data/")

# Optimal partition size
# Rule: Each partition should be at least 128MB
partition_count = total_data_size / (128 * 1024 * 1024)
```

### Caching

```python
# Cache frequently used data
df.cache()

# Use appropriate storage level
from pyspark import StorageLevel
df.persist(StorageLevel.MEMORY_AND_DISK)

# Monitor cache usage
print(df.storageLevel)
print(df.is_cached)
```

### File Format Optimization

```python
# Use Parquet for analytics
df.write.parquet("s3://data/", compression="snappy")

# Use ORC for Hive workloads
df.write.orc("s3://data/", compression="zlib")

# Optimize file sizes
# Target: 128MB-256MB per file
df.repartition(100).write.parquet("s3://data/")
```

### Query Optimization

```python
# Use partition pruning
df = spark.read.parquet("s3://data/")
filtered_df = df.filter(col("date") == "2024-01-01")

# Use predicate pushdown
filtered_df = df.filter(col("amount") > 100)

# Use column pruning
selected_df = df.select("col1", "col2")

# Use vectorized execution
spark.conf.set("spark.sql.parquet.enableVectorizedReader", True)
```

---

## Best Practices

### Job Design

1. **Idempotency**: Design jobs to be re-runnable
2. **Modularity**: Break complex jobs into smaller tasks
3. **Error handling**: Implement retry and alerting mechanisms
4. **Monitoring**: Track job execution and metrics

### Performance

1. **Partition data**: By date for time-series data
2. **Use appropriate file formats**: Parquet or ORC for analytics
3. **Optimize file sizes**: 128MB-256MB per file
4. **Cache intermediate results**: For iterative processing

### Scheduling

1. **Avoid overlapping jobs**: Prevent resource contention
2. **Monitor job duration**: Track execution times
3. **Set appropriate retries**: Handle transient failures
4. **Implement dependency management**: Order jobs correctly

### Monitoring

1. **Track job metrics**: Duration, records processed, errors
2. **Set up alerts**: For failures and slow jobs
3. **Monitor resource usage**: CPU, memory, storage
4. **Log execution details**: For debugging and auditing

---

## References

- [Batch Processing with Spark](https://spark.apache.org/docs/latest/batch-processing.html)
- [Apache Airflow Documentation](https://airflow.apache.org/docs/)
- [Batch Processing Patterns](https://www.oreilly.com/library/view/batch-processing-patterns/9781492028161/)
- [Lambda Architecture](https://www.oreilly.com/library/view/lambda-architecture/9781491947838/)
- [Building Batch Processing Pipelines](https://www.oreilly.com/library/view/building-batch-processing/9781492028161/)
