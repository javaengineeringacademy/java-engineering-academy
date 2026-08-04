# Trino (formerly PrestoSQL)

## Overview

Trino is a distributed SQL query engine designed for running interactive analytic queries against data sources of all sizes. It was originally developed as PrestoDB and later forked as Trino.

## Architecture

```
                    ┌─────────────┐
                    │   Client    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │ Coordinator │
                    │   (Query    │
                    │  Planning)  │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
        ┌─────▼─────┐ ┌───▼───┐ ┌─────▼─────┐
        │  Worker 1  │ │Worker 2│ │  Worker N  │
        └───────────┘ └───────┘ └───────────┘
```

### Components

| Component | Description |
|-----------|-------------|
| **Coordinator** | Parses queries, creates plans, manages workers |
| **Worker** | Executes tasks, processes data |
| **Connector** | Interface to data sources |
| **Catalog** | Metadata about data sources |

## Connectors

### Supported Connectors

| Connector | Data Source |
|-----------|-------------|
| Hive | Hive tables, S3, GCS, HDFS |
| MySQL | MySQL databases |
| PostgreSQL | PostgreSQL databases |
| Oracle | Oracle databases |
| SQL Server | Microsoft SQL Server |
| Redshift | Amazon Redshift |
| BigQuery | Google BigQuery |
| Cassandra | Apache Cassandra |
| Elasticsearch | Elasticsearch indices |
| Kafka | Kafka topics |
| MongoDB | MongoDB collections |
| Pinot | Apache Pinot tables |
| Druid | Apache Druid datasources |
| Delta Lake | Delta Lake tables |
| Iceberg | Apache Iceberg tables |
| Hudi | Apache Hudi tables |

### Catalog Configuration

```properties
# catalog/hive.properties
connector.name=hive
hive.metastore.uri=thrift://localhost:9083
hive.s3.region=us-east-1
hive.s3.access-key=AKIAIOSFODNN7EXAMPLE
hive.s3.secret-key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
```

## SQL Features

### Basic Queries

```sql
-- Simple SELECT
SELECT * FROM hive.default.users WHERE age > 25;

-- Aggregation
SELECT department, COUNT(*), AVG(salary)
FROM employees
GROUP BY department
HAVING COUNT(*) > 10;

-- Window functions
SELECT 
    name,
    salary,
    RANK() OVER (PARTITION BY department ORDER BY salary DESC) as rank
FROM employees;

-- CTEs
WITH high_earners AS (
    SELECT * FROM employees WHERE salary > 100000
)
SELECT department, COUNT(*)
FROM high_earners
GROUP BY department;
```

### Complex Queries

```sql
-- PIVOT
SELECT * FROM (
    SELECT department, salary FROM employees
)
PIVOT (
    AVG(salary)
    FOR department IN ('Engineering', 'Sales', 'Marketing')
);

-- UNNEST
SELECT student, course
FROM students,
UNNEST(courses) AS t(course);

-- JSON functions
SELECT 
    json_extract_scalar(data, '$.name') as name,
    json_extract_scalar(data, '$.age') as age
FROM json_data;

-- Map functions
SELECT 
    map_keys(properties) as keys,
    map_values(properties) as values
FROM map_data;
```

## Performance Tuning

### Query Optimization

```sql
-- Use EXPLAIN to analyze query plan
EXPLAIN SELECT * FROM large_table WHERE id = 123;

-- Use EXPLAIN ANALYZE for actual execution stats
EXPLAIN ANALYZE SELECT * FROM large_table WHERE id = 123;

-- Partition pruning
SELECT * FROM hive.default.events 
WHERE dt = '2024-01-01' AND hour = 12;

-- Predicate pushdown (automatic with proper connector)
SELECT * FROM mysql.default.users WHERE id = 100;
```

### Configuration Tuning

```properties
# Memory settings
query.max-memory=50GB
query.max-memory-per-node=8GB

# Concurrency
query.max-concurrent-queries=100
query.queue-config-file=queue-config.json

# Spill to disk
experimental.spill-enabled=true
query.max-spill-per-node=100MB

# Resource groups
resource-groups.configuration-file=resource-groups.json
```

### Resource Groups

```json
{
  "rootGroups": [
    {
      "name": "global",
      "maxQueued": 1000,
      "maxConcurrent": 100,
      "schedulingPolicy": "fair",
      "subGroups": [
        {
          "name": "interactive",
          "maxQueued": 100,
          "maxConcurrent": 50,
          "schedulingWeight": 1
        },
        {
          "name": "batch",
          "maxQueued": 500,
          "maxConcurrent": 20,
          "schedulingWeight": 2
        }
      ]
    }
  ]
}
```

## Monitoring

### Key Metrics

| Metric | Description |
|--------|-------------|
| `query.total` | Total queries executed |
| `query.running` | Currently running queries |
| `query.queued` | Queued queries |
| `query.failed` | Failed queries |
| `query.elapsed_time` | Query execution time |
| `cpu.user` | CPU user time |
| `memory.heap` | JVM heap usage |

### System Tables

```sql
-- Active queries
SELECT * FROM system.runtime.queries 
WHERE state = 'RUNNING';

-- Completed queries
SELECT * FROM system.runtime.queries 
WHERE state = 'FINISHED'
ORDER BY end_time DESC
LIMIT 10;

-- Query statistics
SELECT * FROM system.runtime.query_statistics;

-- Workers
SELECT * FROM system.runtime.nodes;
```

## Best Practices

1. **Use partitioning** - Always filter on partition columns for pruning
2. **Limit data scanned** - Use LIMIT and specific column selections
3. **Use resource groups** - Separate interactive and batch workloads
4. **Monitor query performance** - Track slow queries and optimize
5. **Use EXPLAIN** - Analyze query plans before production deployment
6. **Configure memory** - Set appropriate memory limits per query

## Trino vs Presto

| Feature | Trino | Presto |
|---------|-------|--------|
| Fork year | 2019 | Original |
| Language | Java | Java |
| Performance | Generally faster | Good |
| Connector support | More connectors | Fewer connectors |
| Community | Active development | Split community |
| SQL compatibility | High | High |

## Key Takeaways

- Trino is a distributed SQL engine for interactive analytics
- It supports a wide range of connectors for different data sources
- Resource groups enable workload management and isolation
- EXPLAIN and EXPLAIN ANALYZE are essential for query optimization
- Trino is the recommended fork over PrestoDB for new deployments
