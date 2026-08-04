# ETL Pipelines

## Table of Contents

- [Overview](#overview)
- [ETL Process](#etl-process)
- [Extract Phase](#extract-phase)
- [Transform Phase](#transform-phase)
- [Load Phase](#load-phase)
- [ETL Tools and Frameworks](#etl-tools-and-frameworks)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

ETL (Extract, Transform, Load) is a data integration process that combines
data from multiple sources into a single, consistent data store. ETL pipelines
are fundamental to data warehousing and analytics.

### Key Characteristics

- **Extract**: Retrieve data from source systems
- **Transform**: Clean, validate, and enrich data
- **Load**: Load transformed data to target system
- **Batch oriented**: Process data in batches
- **Scheduled**: Run on regular schedules

### When to Use ETL

- Data warehousing and business intelligence
- Data migration between systems
- Data consolidation from multiple sources
- Reporting and analytics
- Compliance and regulatory reporting

### ETL vs ELT

| Feature | ETL | ELT |
|---------|-----|-----|
| Transform Location | Staging area | Target warehouse |
| Latency | Higher | Lower |
| Flexibility | Less flexible | More flexible |
| Cost | Higher (separate infra) | Lower (use warehouse) |
| Data Volume | Better for small data | Better for large data |

---

## ETL Process

### ETL Workflow

```
┌─────────────────────────────────────────────────────────────────────┐
│                        ETL Process                                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Source Systems                                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │   ERP    │ │   CRM    │ │  Files   │ │   APIs   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Extract Phase            │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Full Extract                                              │   │
│  │  - Incremental Extract                                       │   │
│  │  - Change Data Capture                                       │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Transform Phase         │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Data Cleaning                                             │   │
│  │  - Data Validation                                           │   │
│  │  - Data Enrichment                                           │   │
│  │  - Data Aggregation                                          │   │
│  │  - Data Deduplication                                        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                           │                                          │
│  Load Phase              │                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  - Full Load                                                 │   │
│  │  - Incremental Load                                          │   │
│  │  - Upsert (Merge)                                            │   │
│  │  - Partition Load                                            │   │
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

### ETL Job Structure

```python
# ETL job structure
class ETLPipeline:
    def __init__(self, config):
        self.config = config
        self.spark = None
        self.metrics = {}

    def extract(self):
        """Extract data from source systems"""
        pass

    def transform(self, data):
        """Transform data"""
        pass

    def load(self, data):
        """Load data to target system"""
        pass

    def run(self):
        """Execute ETL pipeline"""
        # Initialize
        self.spark = SparkSession.builder \
            .appName(self.config['app_name']) \
            .getOrCreate()

        # Extract
        raw_data = self.extract()

        # Transform
        transformed_data = self.transform(raw_data)

        # Load
        self.load(transformed_data)

        # Cleanup
        self.spark.stop()
```

---

## Extract Phase

### Extraction Methods

```python
# Full Extract
def full_extract(spark, source_path):
    """Extract all data from source"""
    return spark.read.format("parquet").load(source_path)

# Incremental Extract
def incremental_extract(spark, source_path, last_watermark):
    """Extract only new/changed data"""
    return spark.read.format("parquet").load(source_path) \
        .filter(col("modified_date") > last_watermark)

# Change Data Capture (CDC)
def cdc_extract(spark, source_path, last_watermark):
    """Extract changes using CDC"""
    return spark.read.format("delta") \
        .option("readChangeFeed", True) \
        .option("startingVersion", last_watermark) \
        .load(source_path)
```

### Source Connectors

```python
# JDBC (RDBMS)
df = spark.read.format("jdbc") \
    .option("url", "jdbc:postgresql://host/db") \
    .option("dbtable", "table") \
    .option("user", "user") \
    .option("password", "password") \
    .option("fetchsize", 10000) \
    .option("partitionColumn", "id") \
    .option("lowerBound", 1) \
    .option("upperBound", 1000000) \
    .option("numPartitions", 10) \
    .load()

# Kafka
df = spark.read.format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic") \
    .option("startingOffsets", "earliest") \
    .load()

# Cloud Storage
df = spark.read.format("parquet") \
    .load("s3://bucket/data/")

# API
import requests
response = requests.get("https://api.example.com/data")
data = response.json()
df = spark.createDataFrame(data)
```

### Extraction Patterns

```python
# Scheduled extraction
def scheduled_extraction(spark, schedule):
    """Extract data on schedule"""
    if schedule == "daily":
        return incremental_extract(spark, "s3://raw/", get_yesterday())
    elif schedule == "hourly":
        return incremental_extract(spark, "s3://raw/", get_last_hour())
    elif schedule == "realtime":
        return stream_extract(spark, "kafka://topic/")

# Watermark-based extraction
def watermark_extraction(spark, source_path, watermark_path):
    """Extract using watermark for incremental processing"""
    # Get last watermark
    last_watermark = get_watermark(watermark_path)

    # Extract new data
    new_data = spark.read.format("parquet") \
        .load(source_path) \
        .filter(col("modified_date") > last_watermark)

    # Update watermark
    if new_data.count() > 0:
        new_watermark = new_data.agg(max("modified_date")).collect()[0][0]
        save_watermark(watermark_path, new_watermark)

    return new_data
```

---

## Transform Phase

### Data Cleaning

```python
# Remove duplicates
df = df.dropDuplicates()

# Remove null values
df = df.na.drop(subset=["col1", "col2"])

# Fill null values
df = df.na.fill({"col1": "default", "col2": 0})

# Fix data types
df = df.withColumn("amount", col("amount").cast(DoubleType()))
df = df.withColumn("date", to_date(col("date")))

# Standardize strings
df = df.withColumn("name", trim(lower(col("name"))))

# Remove invalid records
df = df.filter(col("amount") > 0)
df = df.filter(col("email").rlike("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"))
```

### Data Validation

```python
# Validate data quality
def validate_data(df, rules):
    """Validate data against rules"""
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

    # Check for valid values
    for column, valid_values in rules.get("values", {}).items():
        invalid_count = df.filter(~col(column).isin(valid_values)).count()
        if invalid_count > 0:
            violations.append(f"Column {column} has {invalid_count} invalid values")

    return violations

# Define rules
rules = {
    "not_null": ["id", "amount", "date"],
    "range": {
        "amount": (0, 1000000),
        "quantity": (0, 10000)
    },
    "values": {
        "status": ["active", "inactive", "pending"]
    }
}

# Validate
violations = validate_data(df, rules)
if violations:
    raise ValueError(f"Data quality violations: {violations}")
```

### Data Enrichment

```python
# Join with reference data
reference_df = spark.read.parquet("s3://reference/customers/")
enriched_df = df.join(reference_df, "customer_id", "left")

# Add calculated columns
enriched_df = enriched_df \
    .withColumn("total_amount", col("quantity") * col("price")) \
    .withColumn("tax", col("total_amount") * 0.1) \
    .withColumn("final_amount", col("total_amount") + col("tax"))

# Add date components
enriched_df = enriched_df \
    .withColumn("year", year(col("date"))) \
    .withColumn("month", month(col("date"))) \
    .withColumn("day", dayofmonth(col("date"))) \
    .withColumn("day_of_week", dayofweek(col("date")))

# Add business logic
enriched_df = enriched_df \
    .withColumn("customer_segment",
        when(col("total_purchases") > 1000, "Premium")
        .when(col("total_purchases") > 100, "Regular")
        .otherwise("New"))
```

### Data Aggregation

```python
# Aggregate by time
daily_agg = df.groupBy("date").agg(
    count("*").alias("transaction_count"),
    sum("amount").alias("total_amount"),
    avg("amount").alias("avg_amount")
)

# Aggregate by category
category_agg = df.groupBy("category").agg(
    count("*").alias("item_count"),
    sum("amount").alias("total_amount")
)

# Window aggregation
windowSpec = Window.partitionBy("customer_id").orderBy("date")
df = df.withColumn("running_total", sum("amount").over(windowSpec))
```

### Data Deduplication

```python
# Remove duplicates
df = df.dropDuplicates()

# Remove duplicates based on specific columns
df = df.dropDuplicates(["id", "date"])

# Keep latest record
windowSpec = Window.partitionBy("id").orderBy(col("modified_date").desc())
df = df.withColumn("row_num", row_number().over(windowSpec))
df = df.filter(col("row_num") == 1).drop("row_num")
```

---

## Load Phase

### Loading Methods

```python
# Full Load
def full_load(spark, data, target_path):
    """Load all data to target"""
    data.write.mode("overwrite").parquet(target_path)

# Incremental Load
def incremental_load(spark, data, target_path):
    """Load only new/changed data"""
    data.write.mode("append").parquet(target_path)

# Upsert (Merge)
def upsert_load(spark, data, target_path, key_columns):
    """Upsert data to target"""
    from delta import DeltaTable

    delta_table = DeltaTable.forPath(spark, target_path)

    merge_condition = " AND ".join([f"target.{col} = source.{col}" for col in key_columns])

    delta_table.alias("target").merge(
        data.alias("source"),
        merge_condition
    ).whenMatchedUpdateAll() \
     .whenNotMatchedInsertAll() \
     .execute()

# Partition Load
def partition_load(spark, data, target_path, partition_columns):
    """Load data with partitioning"""
    data.write.mode("overwrite") \
        .partitionBy(*partition_columns) \
        .parquet(target_path)
```

### Target Connectors

```python
# Parquet
df.write.mode("overwrite").parquet("s3://target/data/")

# Delta Lake
df.write.format("delta").mode("overwrite").save("s3://target/data/")

# JDBC (RDBMS)
df.write.format("jdbc") \
    .option("url", "jdbc:postgresql://host/db") \
    .option("dbtable", "table") \
    .option("user", "user") \
    .option("password", "password") \
    .mode("overwrite") \
    .save()

# Hive
df.write.saveAsTable("db.table")

# Iceberg
df.writeTo("catalog.db.table").createOrReplace()
```

### Loading Patterns

```python
# Micro-batch loading
def micro_batch_load(spark, data, target_path, batch_size=10000):
    """Load data in micro-batches"""
    total_rows = data.count()
    num_batches = (total_rows + batch_size - 1) // batch_size

    for i in range(num_batches):
        batch = data.limit(batch_size).offset(i * batch_size)
        batch.write.mode("append").parquet(target_path)

# Streaming loading
def streaming_load(spark, source_stream, target_path):
    """Load streaming data"""
    query = source_stream.writeStream \
        .outputMode("append") \
        .format("parquet") \
        .option("path", target_path) \
        .option("checkpointLocation", f"{target_path}_checkpoint") \
        .start()

    query.awaitTermination()
```

---

## ETL Tools and Frameworks

### Apache Spark

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("ETL").getOrCreate()

# Extract
raw_df = spark.read.parquet("s3://raw/data/")

# Transform
transformed_df = raw_df \
    .dropDuplicates() \
    .na.drop() \
    .withColumn("date", to_date(col("date")))

# Load
transformed_df.write.mode("overwrite").parquet("s3://curated/data/")
```

### Apache Airflow

```python
from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime

def extract():
    # Extract logic
    pass

def transform():
    # Transform logic
    pass

def load():
    # Load logic
    pass

dag = DAG(
    'etl_pipeline',
    start_date=datetime(2024, 1, 1),
    schedule_interval='@daily'
)

extract_task = PythonOperator(task_id='extract', python_callable=extract, dag=dag)
transform_task = PythonOperator(task_id='transform', python_callable=transform, dag=dag)
load_task = PythonOperator(task_id='load', python_callable=load, dag=dag)

extract_task >> transform_task >> load_task
```

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
        amount
    FROM source
)
SELECT * FROM renamed

-- models/marts/fct_orders.sql
WITH orders AS (
    SELECT * FROM {{ ref('stg_orders') }}
),
customers AS (
    SELECT * FROM {{ ref('stg_customers') }}
)
SELECT
    o.order_id,
    o.customer_id,
    c.customer_name,
    o.order_date,
    o.amount
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
```

---

## Best Practices

### Pipeline Design

1. **Idempotency**: Design pipelines to be re-runnable
2. **Modularity**: Break complex pipelines into smaller tasks
3. **Monitoring**: Track pipeline execution and metrics
4. **Error handling**: Implement retry and alerting mechanisms

### Performance

1. **Partition data**: By date for time-series data
2. **Use appropriate file formats**: Parquet or ORC for analytics
3. **Optimize file sizes**: 128MB-256MB per file
4. **Cache intermediate results**: For iterative processing

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

### Complete ETL Pipeline

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.types import *
from delta import DeltaTable

class SalesETLPipeline:
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

        # Read from file
        products_df = self.spark.read.parquet(self.config['products_path'])

        return orders_df, products_df

    def transform(self, orders_df, products_df):
        """Transform data"""
        # Clean orders
        cleaned_orders = orders_df \
            .dropDuplicates() \
            .na.drop(subset=["order_id", "customer_id"]) \
            .withColumn("order_date", to_date(col("order_date"))) \
            .withColumn("amount", col("amount").cast(DoubleType())) \
            .filter(col("amount") > 0)

        # Enrich with products
        enriched_orders = cleaned_orders \
            .join(products_df, "product_id", "left") \
            .withColumn("category", coalesce(col("category"), lit("Unknown")))

        # Add business logic
        final_orders = enriched_orders \
            .withColumn("order_year", year(col("order_date"))) \
            .withColumn("order_month", month(col("order_date"))) \
            .withColumn("customer_segment",
                when(col("total_purchases") > 1000, "Premium")
                .when(col("total_purchases") > 100, "Regular")
                .otherwise("New"))

        return final_orders

    def load(self, data):
        """Load data to target"""
        # Write to Delta Lake
        data.write.format("delta") \
            .mode("overwrite") \
            .partitionBy("order_year", "order_month") \
            .save(self.config['target_path'])

        # Optimize table
        DeltaTable.forPath(self.spark, self.config['target_path']).optimize()

    def run(self):
        """Execute ETL pipeline"""
        self.spark = SparkSession.builder \
            .appName("SalesETL") \
            .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension") \
            .getOrCreate()

        try:
            orders_df, products_df = self.extract()
            transformed_df = self.transform(orders_df, products_df)
            self.load(transformed_df)
        finally:
            self.spark.stop()

# Execute pipeline
config = {
    'source_url': 'jdbc:postgresql://host/db',
    'source_user': 'user',
    'source_password': 'password',
    'products_path': 's3://data/products/',
    'target_path': 's3://warehouse/sales/'
}

pipeline = SalesETLPipeline(config)
pipeline.run()
```

---

## References

- [ETL Best Practices](https://www.talend.com/resources/what-is-etl/)
- [ETL vs ELT](https://www.stitchdata.com/resources/etl/etl-vs-elt/)
- [Apache Spark for ETL](https://spark.apache.org/docs/latest/etl-guide.html)
- [ETL Pipeline Design](https://www.dataengineeringweekly.com/etl-pipeline-design)
- [Building ETL Pipelines](https://www.oreilly.com/library/view/building-etl-pipelines/9781492028161/)
