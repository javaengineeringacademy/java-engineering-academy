# Apache ORC Fundamentals

## Overview
ORC (Optimized Row Columnar) is a columnar file format designed for Hive workloads with efficient compression and fast reads.

## Key Features
- Columnar storage
- Built-in indexes (min/max per stripe)
- Lightweight indexes for filtering
- ACID transaction support
- Complex types (struct, map, array)

## File Structure
```
ORC File:
  File Footer (metadata)
  Stripe 1 (default 64MB):
    Index (min/max per column)
    Data
    Statistics
  Stripe 2:
    ...
```

## Schema
```sql
CREATE TABLE events (
    id BIGINT,
    event_type STRING,
    revenue DOUBLE,
    user_id STRING,
    metadata MAP<STRING, STRING>,
    created_at TIMESTAMP
) STORED AS ORC
TBLPROPERTIES (
    'orc.compress'='SNAPPY',
    'orc.create.index'='true',
    'orc.stripe.size'='67108864'
);
```

## Writing ORC
```python
# PySpark
df.write.orc("/output/events", mode="overwrite")

# With options
df.write.orc("/output/events",
    compression="snappy",
    stripeSize=67108864,  # 64MB
    batchSize=1000
)

# SQL
INSERT INTO TABLE events_orc SELECT * FROM events_raw;
```

## Reading ORC
```python
# PySpark
df = spark.read.orc("/output/events")

# With predicate pushdown
df = spark.read.orc("/output/events").filter("event_type = 'click'")

# Column pruning
df = spark.read.orc("/output/events").select("id", "event_type")
```

## Comparison: ORC vs Parquet

| Feature | ORC | Parquet |
|---------|-----|---------|
| Compression | Better | Good |
| Read Speed | Faster | Good |
| Ecosystem | Hive-first | Spark-first |
| Indexing | Built-in | No |
| Transactions | ACID | No |

## Best Practices
1. Use ORC for Hive-centric workloads
2. Enable stripe-level indexing
3. Set appropriate stripe size
4. Use Snappy compression
5. Monitor file size distribution
