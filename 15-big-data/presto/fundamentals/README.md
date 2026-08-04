# Presto/Trino Fundamentals

## Overview
Presto (now Trino) is a distributed SQL query engine for running interactive analytical queries against data sources of any size.

## Architecture
- **Coordinator**: Parses, plans, and schedules queries
- **Worker**: Executes tasks and processes data
- **Catalog**: Connection to data source
- **Schema**: Database within a catalog
- **Split**: Unit of parallelism

## Supported Connectors
- Hive (HDFS, S3)
- MySQL, PostgreSQL, Oracle
- Kafka
- Elasticsearch
- Cassandra
- MongoDB

## Basic Usage
```sql
-- Connect
presto --server localhost:8080 --catalog hive

-- Query
SELECT nation, count(*) as cnt
FROM orders o
JOIN customers c ON o.customer_id = c.id
WHERE order_date >= '2024-01-01'
GROUP BY nation
ORDER BY cnt DESC;
```

## Configuration
```properties
# coordinator.properties
coordinator=true
node-scheduler.include-coordinator=false
http-server.http.port=8080
query.max-memory=50GB
query.max-memory-per-node=8GB
discovery-server.enabled=true
discovery.uri=http://localhost:8080

# worker.properties
coordinator=false
http-server.http.port=8081
query.max-memory=50GB
query.max-memory-per-node=8GB
discovery.uri=http://localhost:8080
```

## Best Practices
1. Use proper data partitioning
2. Tune memory settings per node
3. Monitor query performance with EXPLAIN
4. Use appropriate file formats (ORC, Parquet)
5. Set query timeouts to prevent resource exhaustion
