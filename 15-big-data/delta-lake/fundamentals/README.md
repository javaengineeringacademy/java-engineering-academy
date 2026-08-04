# Delta Lake Fundamentals

## Overview
Delta Lake is an open-source storage layer that brings ACID transactions to Apache Spark and big data workloads.

## Key Features
- ACID transactions
- Schema enforcement and evolution
- Time travel
- Data versioning
- Unified batch and streaming
- Z-ordering for data skipping

## Creating Tables
```python
# Python
df.write.format("delta").save("/delta/events")

# With partitioning
df.write.format("delta").partitionBy("year", "month").save("/delta/events")

# SQL
CREATE TABLE delta.`/delta/events` (
    id BIGINT,
    event_type STRING,
    timestamp TIMESTAMP
) USING DELTA
PARTITIONED BY (year, month)
```

## ACID Transactions
```python
# Write with merge
from delta.tables import DeltaTable

deltaTable = DeltaTable.forPath(spark, "/delta/events")

deltaTable.alias("target").merge(
    updates.alias("source"),
    "target.id = source.id"
).whenMatchedUpdateAll()  .whenNotMatchedInsertAll()  .execute()
```

## Best Practices
1. Use Delta Lake for streaming + batch workloads
2. Enable auto-optimize for small file compaction
3. Configure vacuum retention for time travel
4. Use Z-ordering for common filter columns
5. Monitor transaction log size
