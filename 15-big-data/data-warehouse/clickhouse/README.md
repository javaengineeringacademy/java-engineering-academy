# ClickHouse

## Overview
ClickHouse is a column-oriented OLAP database designed for real-time analytics with sub-second query performance.

## Architecture
- **ClickHouse Server**: Query processing
- **ZooKeeper/ClickHouse Keeper**: Cluster coordination
- **Replicas**: Data replication

## Creating Tables
```sql
-- MergeTree table
CREATE TABLE events (
    id UInt64,
    event_type String,
    user_id String,
    revenue Float64,
    timestamp DateTime
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(timestamp)
ORDER BY (event_type, user_id, timestamp);

-- Distributed table
CREATE TABLE events_dist AS events
ENGINE = Distributed('cluster', 'default', 'events', xxHash64(user_id));

-- Materialized view
CREATE MATERIALIZED VIEW events_mv
ENGINE = SummingMergeTree()
PARTITION BY toYYYYMM(timestamp)
ORDER BY (event_type, timestamp)
AS SELECT
    event_type,
    toStartOfMonth(timestamp) as month,
    count() as event_count,
    sum(revenue) as total_revenue
FROM events GROUP BY 1, 2;
```

## Query Optimization
```sql
-- Use FINAL for deduplication
SELECT * FROM events FINAL WHERE event_type = 'click';

-- Use PREWHERE for filtering
SELECT * FROM events PREWHERE event_type = 'click' AND timestamp > '2024-01-01';

-- Use approximate functions
SELECT uniqHLL12(user_id) FROM events;

-- Use array functions
SELECT arrayFilter(x -> x > 100, revenue_array) FROM events;
```

## Best Practices
1. Design ORDER BY for query patterns
2. Use appropriate MergeTree variants
3. Enable compression (LZ4, ZSTD)
4. Use materialized views for aggregations
5. Monitor query performance and resource usage
