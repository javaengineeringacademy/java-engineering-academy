# Presto Performance Optimization

## Memory Tuning
```properties
# Per node
query.max-memory-per-node=8GB
query.max-total-memory-per-node=12GB

# Global
query.max-memory=50GB

# Exchange memory
query.exchange.deduplication-buffer-size=256MB
```

## Connector Tuning
```properties
# Hive connector
hive.max-concurrent-fetches=50
hive.s3.max-connections=500
hive.orc.max-buffer-size=16MB
hive.parquet.max-buffer-size=16MB
```

## Query Optimization
```java
// Use materialized views
CREATE MATERIALIZED VIEW monthly_orders AS
SELECT date_trunc('month', order_date) as month, sum(total) as total
FROM orders GROUP BY 1;

// Use partitioned tables
-- Partition by date for time-series queries
-- Partition by region for geographic queries
```

## Monitoring
```sql
-- Active queries
SELECT * FROM system.runtime.queries WHERE state = 'RUNNING';

-- Query statistics
SELECT query_id, user, source, query, started, end
FROM system.runtime.queries
WHERE started > now() - interval '1 hour'
ORDER BY started DESC;

-- Worker status
SELECT * FROM system.runtime.nodes;
```

## Best Practices
1. Use ORC or Parquet with columnar storage
2. Partition large tables appropriately
3. Set appropriate timeout limits
4. Monitor queue depth and query wait times
5. Use resource groups for workload management
6. Cache frequently accessed small datasets
