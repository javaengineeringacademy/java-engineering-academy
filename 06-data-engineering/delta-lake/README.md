# Delta Lake

## Overview

Delta Lake is an open-source storage layer that brings ACID transactions to Apache Spark and big data workloads. It provides schema enforcement, time travel, and unified batch and streaming data processing.

## Architecture

### Transaction Log

```
my_table/
├── _delta_log/
│   ├── 00000000000000000000.json
│   ├── 00000000000000000001.json
│   ├── 00000000000000000002.checkpoint.parquet
│   └── ...
├── part-00000-...parquet
├── part-00001-...parquet
└── ...
```

### JSON Transaction Log

```json
{
  "add": {
    "path": "part-00000-abc123.parquet",
    "partitionValues": {"year": "2024", "month": "01"},
    "size": 1234567,
    "modificationTime": 1704067200000,
    "dataChange": true,
    "stats": "{\"numRecords\":1000,\"minValues\":{\"id\":1},\"maxValues\":{\"id\":1000}}"
  }
}
```

## Quick Start

### Create Table

```python
# Using PySpark
from delta.tables import DeltaTable
from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("Delta Lake") \
    .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension") \
    .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog") \
    .getOrCreate()

# Create Delta table
data = spark.range(0, 1000)
data.write.format("delta").save("/delta/events")

# Create with partitioning
data.write.format("delta") \
    .partitionBy("year", "month") \
    .save("/delta/events_partitioned")
```

### SQL Operations

```sql
-- Create table
CREATE TABLE delta.`/delta/events` (
    id BIGINT,
    event_time TIMESTAMP,
    user_id BIGINT,
    event_type STRING
) USING delta
PARTITIONED BY (year(event_time), month(event_time));

-- Insert data
INSERT INTO delta.`/delta/events` 
VALUES (1, '2024-01-01 10:00:00', 100, 'click');

-- Merge (Upsert)
MERGE INTO delta.`/delta/events` AS target
USING delta.`/delta/updates` AS source
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET *
WHEN NOT MATCHED THEN
    INSERT *;
```

## Schema Enforcement

```python
# Schema enforcement prevents bad writes
from pyspark.sql import Row

# This will fail if schema doesn't match
try:
    bad_data = spark.createDataFrame([
        Row(id="not_a_number", name="test")  # Wrong type
    ])
    bad_data.write.format("delta").mode("append").save("/delta/events")
except Exception as e:
    print(f"Schema mismatch: {e}")

# Schema evolution with mergeSchema option
new_data = spark.createDataFrame([
    Row(id=1, name="test", new_column="value")
])
new_data.write.format("delta") \
    .option("mergeSchema", "true") \
    .mode("append") \
    .save("/delta/events")
```

## Time Travel

```python
# Read previous version
df_v0 = spark.read.format("delta").load("/delta/events")
df_v1 = spark.read.format("delta").option("versionAsOf", 1).load("/delta/events")

# Read by timestamp
df_ts = spark.read.format("delta") \
    .option("timestampAsOf", "2024-01-01") \
    .load("/delta/events")

# View history
history_df = DeltaTable.forPath(spark, "/delta/events").history()
history_df.show()
```

### SQL Time Travel

```sql
-- Read version 5
SELECT * FROM delta.`/delta/events` VERSION AS OF 5;

-- Read as of timestamp
SELECT * FROM delta.`/delta/events` TIMESTAMP AS OF '2024-01-01';

-- View history
DESCRIBE HISTORY delta.`/delta/events`;
```

## OPTIMIZE and Z-ORDER

```sql
-- Optimize table (compaction)
OPTIMIZE delta.`/delta/events`;

-- Optimize with Z-ORDER for better data skipping
OPTIMIZE delta.`/delta/events`
ZORDER BY (user_id, event_type);

-- Optimize specific partitions
OPTIMIZE delta.`/delta/events` WHERE year = 2024 AND month = 1;
```

```python
# Using DeltaTable API
delta_table = DeltaTable.forPath(spark, "/delta/events")

# Optimize
delta_table.optimize().executeCompaction()

# Z-ORDER
delta_table.optimize().executeZOrderBy("user_id", "event_type")
```

## VACUUM

```sql
-- Remove old files (default retention: 7 days)
VACUUM delta.`/delta/events`;

-- Remove with custom retention
VACUUM delta.`/delta/events` RETAIN 168 HOURS;
```

```python
# Using DeltaTable API
delta_table.vacuum()  # Default 7 days
delta_table.vacuum(168)  # Custom retention in hours
```

## Change Data Feed

```sql
-- Enable Change Data Feed
ALTER TABLE delta.`/delta/events` 
SET TBLPROPERTIES ('delta.enableChangeDataFeed' = 'true');

-- Read changes between versions
SELECT * FROM table_changes('delta.`/delta/events`', 0, 5);

-- Read changes for specific partition
SELECT * FROM table_changes('delta.`/delta/events`', 0, 5, 'year=2024')
```

## Schema Evolution

```sql
-- Add column
ALTER TABLE delta.`/delta/events` ADD COLUMN new_col STRING;

-- Rename column
ALTER TABLE delta.`/delta/events` RENAME COLUMN old_name TO new_name;

-- Drop column
ALTER TABLE delta.`/delta/events` DROP COLUMN obsolete_col;

-- Change column type (must be compatible)
ALTER TABLE delta.`/delta/events` ALTER COLUMN age TYPE BIGINT;
```

## Configuration

```python
# Table properties
spark.sql("ALTER TABLE delta.`/delta/events` SET TBLPROPERTIES " +
    "('delta.autoOptimize.optimizeWrite' = 'true', " +
    "'delta.autoOptimize.autoCompact' = 'true')")
```

| Property | Description | Default |
|----------|-------------|---------|
| `delta.autoOptimize.optimizeWrite` | Auto-compaction on write | false |
| `delta.autoOptimize.autoCompact` | Auto-compaction on merge | false |
| `delta.enableChangeDataFeed` | Enable CDF | false |
| `delta.logRetentionDuration` | Log retention | interval 30 days |
| `delta.deletedFileRetentionDuration` | File retention | interval 7 days |

## Best Practices

1. **Use OPTIMIZE regularly** - Compact small files to improve performance
2. **Z-ORDER on filter columns** - Improve data skipping for common queries
3. **Enable auto-optimize** - For write-heavy workloads
4. **Manage retention** - Set appropriate VACUUM retention periods
5. **Monitor file count** - Track data file counts per table
6. **Use partition wisely** - Avoid over-partitioning

## Key Takeaways

- Delta Lake provides ACID transactions on data lakes
- Schema enforcement prevents bad data writes
- Time travel enables point-in-time queries and auditing
- OPTIMIZE and Z-ORDER improve query performance
- VACUUM removes old files to reclaim storage
- Change Data Feed enables downstream data replication
