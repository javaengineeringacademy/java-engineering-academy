# Presto

## Overview

Presto is an open-source distributed SQL query engine designed for running fast, interactive queries against large datasets. It supports querying data from various sources including Hive, HDFS, S3, and relational databases.

## Architecture

### Components

| Component | Description |
|-----------|-------------|
| **Coordinator** | Parses queries, creates execution plans, manages workers |
| **Worker** | Executes tasks, processes data |
| **Connector** | Plugin interface to data sources |
| **Catalog** | Metadata about data sources |

### Query Execution Flow

```
Client → Coordinator → Parse → Plan → Optimize → Schedule → Workers → Result
```

## Connectors

### Hive Connector

```properties
# catalog/hive.properties
connector.name=hive
hive.metastore.uri=thrift://localhost:9083
hive.config.resources=/etc/hadoop/core-site.xml,/etc/hadoop/hdfs-site.xml
```

### MySQL Connector

```properties
# catalog/mysql.properties
connector.name=mysql
connection-url=jdbc:mysql://localhost:3306/mydb
connection-user=root
connection-password=secret
```

### Memory Connector

```properties
# catalog/memory.properties
connector.name=memory
```

## SQL Features

### Basic Queries

```sql
-- Simple SELECT
SELECT * FROM hive.default.users LIMIT 100;

-- Filtering
SELECT * FROM orders WHERE amount > 100 AND status = 'completed';

-- Aggregation
SELECT 
    department,
    COUNT(*) as emp_count,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary
FROM employees
GROUP BY department;

-- Window functions
SELECT 
    name,
    salary,
    ROW_NUMBER() OVER (ORDER BY salary DESC) as rank,
    LAG(salary) OVER (ORDER BY hire_date) as prev_salary
FROM employees;
```

### Complex Queries

```sql
-- Subqueries
SELECT * FROM employees 
WHERE department_id IN (
    SELECT id FROM departments WHERE name = 'Engineering'
);

-- CTEs
WITH monthly_sales AS (
    SELECT 
        DATE_TRUNC('month', order_date) as month,
        SUM(amount) as total
    FROM orders
    GROUP BY 1
)
SELECT * FROM monthly_sales WHERE total > 10000;

-- JSON functions
SELECT 
    json_extract_scalar(data, '$.name') as name,
    json_array_length(json_extract(data, '$.items')) as item_count
FROM json_data;

-- UNNEST
SELECT student, course
FROM students,
UNNEST(courses) AS t(course);
```

### Hive-Specific Features

```sql
-- Partitioned tables
SELECT * FROM logs WHERE dt = '2024-01-01' AND hour = 12;

-- Bucketed tables
SELECT * FROM users_bucketed WHERE user_id = 123;

-- ACID transactions
SELECT * FROM transactions FOR SYSTEM_VERSION AS OF 1234567890;

-- Schema evolution
ALTER TABLE users ADD COLUMN email STRING;
```

## Performance Tuning

### Query Optimization

```sql
-- Use EXPLAIN to analyze query plan
EXPLAIN SELECT * FROM large_table WHERE id = 123;

-- Use EXPLAIN ANALYZE for actual execution stats
EXPLAIN ANALYZE SELECT * FROM large_table WHERE id = 123;

-- Force partition pruning
SELECT * FROM logs 
WHERE dt = '2024-01-01' AND hour BETWEEN 9 AND 17;
```

### Configuration

```properties
# Memory settings
query.max-memory=50GB
query.max-memory-per-node=8GB
query.max-total-memory-per-node=10GB

# Concurrency
query.max-concurrent-queries=100
query.queue-config-file=queue-config.json

# Spill to disk
experimental.spill-enabled=true
query.max-spill-per-node=100MB
spill-path=/var/presto/spill

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

## Fault-Tolerant Execution

```properties
# Enable fault-tolerant execution
query.execution-policy=fault-tolerant
exchange.deduplication-buffer-size=1GB
exchange.compression-enabled=true
```

## Monitoring

### System Tables

```sql
-- Active queries
SELECT * FROM system.runtime.queries 
WHERE state = 'RUNNING';

-- Completed queries
SELECT * FROM system.runtime.completed_queries
ORDER BY end_time DESC
LIMIT 10;

-- Query statistics
SELECT * FROM system.runtime.query_statistics;

-- Workers
SELECT * FROM system.runtime.nodes;

-- Tasks
SELECT * FROM system.runtime.tasks;
```

### Key Metrics

| Metric | Description |
|--------|-------------|
| `presto.query.total` | Total queries executed |
| `presto.query.running` | Currently running queries |
| `presto.query.queued` | Queued queries |
| `presto.query.failed` | Failed queries |
| `presto.query.elapsed_time` | Query execution time |
| `presto.cpu.user` | CPU user time |
| `presto.memory.heap` | JVM heap usage |

## Presto vs Trino

| Feature | Presto | Trino |
|---------|--------|-------|
| Origin | Facebook | Fork of Presto |
| Maintainer | Facebook/Meta | Trino Software Foundation |
| Release cycle | Less frequent | More frequent |
| Connector support | Good | Better |
| Performance | Good | Generally faster |
| Community | Smaller | Larger |

## Best Practices

1. **Use partitioning** - Filter on partition columns for pruning
2. **Limit data scanned** - Use LIMIT and specific column selections
3. **Use resource groups** - Separate interactive and batch workloads
4. **Monitor query performance** - Track slow queries and optimize
5. **Use EXPLAIN** - Analyze query plans before production
6. **Configure memory** - Set appropriate memory limits per query

## Key Takeaways

- Presto is a distributed SQL engine for interactive analytics
- It supports querying data from Hive, S3, relational databases, and more
- Resource groups enable workload management and isolation
- Fault-tolerant execution provides resilience for long-running queries
- Monitor using system tables and JMX metrics
