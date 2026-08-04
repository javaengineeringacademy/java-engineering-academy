# Apache Hudi

## Overview

Apache Hudi (Hadoop Upsert Delete Incremental) is an open-source data lake platform that provides upsert, incremental processing, and table management capabilities on top of Apache Spark, Flink, and Presto.

## Architecture

### Table Types

| Type | Storage | Write Pattern | Read Pattern |
|------|---------|---------------|--------------|
| **Copy-on-Write (COW)** | Parquet | Copy entire file on update | Fast reads |
| **Merge-on-Read (MOR)** | Parquet + Log | Append to log file | Log merge on read |

### Timeline

```
Timeline
  2024-01-01T00:00:00  CREATING
  2024-01-01T00:00:01  COMMIT (2024-01-01)
  2024-01-02T00:00:00  COMMIT (2024-01-02)
  2024-01-03T00:00:00  COMMIT (2024-01-03)
```

## Quick Start

### Create Table (Spark)

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("Hudi Example") \
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer") \
    .config("spark.sql.hive.convertMetastoreParquet", "false") \
    .getOrCreate()

hudi_options = {
    'hoodie.table.name': 'my_table',
    'hoodie.datasource.write.recordkey.field': 'id',
    'hoodie.datasource.write.precombine.field': 'timestamp',
    'hoodie.datasource.write.table.type': 'COPY_ON_WRITE',
    'hoodie.datasource.write.operation': 'upsert',
    'hoodie.datasource.write.reconcile.schema': 'true'
}

df.write.format("hudi"). \
    options(**hudi_options). \
    mode("overwrite"). \
    save("/hudi/my_table")
```

### SQL Operations (Spark SQL)

```sql
-- Create table
CREATE TABLE hudi.my_table (
    id BIGINT,
    name STRING,
    age INT,
    timestamp BIGINT
) USING hudi
TBLPROPERTIES (
    'hoodie.table.type' = 'COPY_ON_WRITE',
    'hoodie.datasource.write.recordkey.field' = 'id',
    'hoodie.datasource.write.precombine.field' = 'timestamp'
);

-- Insert
INSERT INTO hudi.my_table VALUES (1, 'Alice', 30, 1704067200);

-- Upsert
INSERT INTO hudi.my_table VALUES (1, 'Alice Updated', 31, 1704153600);

-- Delete
DELETE FROM hudi.my_table WHERE id = 1;

-- Merge
MERGE INTO hudi.my_table AS target
USING hudi.updates AS source
ON target.id = source.id
WHEN MATCHED THEN UPDATE SET *
WHEN NOT MATCHED THEN INSERT *
```

## Incremental Queries

```python
# Read incrementally from a commit time
inc_df = spark.read.format("hudi") \
    .option("hoodie.datasource.query.type", "incremental") \
    .option("hoodie.datasource.read.begin.instanttime", "20240101000000") \
    .load("/hudi/my_table")

# Get commits
hudi_inc = spark.read.format("hudi") \
    .option("hoodie.datasource.query.type", "incremental") \
    .option("hoodie.datasource.read.begin.instanttime", "20240101000000") \
    .load("/hudi/my_table")
```

## COW vs MOR

### Copy-on-Write (COW)

```python
hudi_options = {
    'hoodie.table.type': 'COPY_ON_WRITE',
    'hoodie.datasource.write.operation': 'upsert'
}
```

- On update: rewrites entire Parquet file
- Better read performance
- Higher write amplification
- Best for: read-heavy workloads

### Merge-on-Read (MOR)

```python
hudi_options = {
    'hoodie.table.type': 'MERGE_ON_READ',
    'hoodie.datasource.write.operation': 'upsert',
    'hoodie.logfile.max.size': '1073741824'
}
```

- On update: appends to log file
- Better write performance
- Read requires log merge
- Best for: write-heavy workloads

## Compaction

```python
# Schedule compaction
spark.sql("""
    CALL hudi.compact_schedule(
        table => 'hudi.my_table',
        timestamp => '20240101000000'
    )
""")

# Run compaction
spark.sql("""
    CALL hudi.compact_run(
        table => 'hudi.my_table',
        timestamp => '20240101000000'
    )
""")
```

## Clustering

```python
# Schedule clustering
spark.sql("""
    CALL hudi.cluster_schedule(
        table => 'hudi.my_table',
        timestamp => '20240101000000'
    )
""")

# Run clustering
spark.sql("""
    CALL hudi.cluster_run(
        table => 'hudi.my_table',
        timestamp => '20240101000000'
    )
""")
```

## Secondary Indexes

```python
# Create secondary index
spark.sql("""
    CREATE INDEX idx_name ON hudi.my_table (name)
""")

# Create bloom filter index
spark.sql("""
    CREATE BLOOM INDEX idx_bloom ON hudi.my_table (id)
""")

# Create column statistics index
spark.sql("""
    CREATE COLUMNSTATS INDEX idx_stats ON hudi.my_table (age)
""")
```

## Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `hoodie.table.type` | Table type (COW/MOR) | COPY_ON_WRITE |
| `hoodie.datasource.write.recordkey.field` | Record key field | Required |
| `hoodie.datasource.write.precombine.field` | Precombine field | Required |
| `hoodie.datasource.write.operation` | Write operation | upsert |
| `hoodie.compact.inline.max.delta.commits` | Auto-compact threshold | 5 |
| `hoodie.cleaner.policy` | Clean policy | KEEP_LATEST_COMMITS |

## Best Practices

1. **Choose COW for read-heavy** - COW provides faster reads
2. **Choose MOR for write-heavy** - MOR reduces write amplification
3. **Set record key wisely** - Use a unique identifier
4. **Use precombine field** - For ordering records during upsert
5. **Schedule compaction** - For MOR tables to maintain read performance
6. **Monitor timeline** - Track commit frequency and size

## Key Takeaways

- Hudi provides upsert capabilities on data lakes
- COW is better for read-heavy, MOR for write-heavy workloads
- Incremental queries enable efficient downstream processing
- Compaction and clustering improve performance over time
- Secondary indexes enable faster point lookups
- Hudi integrates with Spark, Flink, and Presto
