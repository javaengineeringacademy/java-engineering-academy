# Apache Drill: Schema-Free SQL Query Engine

## Table of Contents
1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Schema-Free Queries](#schema-free-queries)
4. [SQL Interface](#sql-interface)
5. [Data Sources](#data-sources)
6. [Storage Plugins](#storage-plugins)
7. [Query Planning](#query-planning)
8. [Performance Optimization](#performance-optimization)
9. [Drill vs Trino/Presto](#drill-vs-trinopresto)
10. [Best Practices](#best-practices)
11. [Key Takeaways](#key-takeaways)

---

## Introduction

Apache Drill is a distributed, schema-free SQL query engine designed for big data exploration. It enables users to query diverse data sources using SQL without requiring schema definitions or ETL processes.

### Core Features

- **Schema-Free**: Query data without predefined schemas
- **SQL Interface**: Standard SQL with extensions for complex data
- **Multi-Source**: Query across files, NoSQL databases, and streaming platforms
- **Real-Time**: Low-latency queries on live data
- **Distributed**: Horizontal scaling for petabyte-scale datasets
- **Developer-Friendly**: REST API, JDBC/ODBC drivers, and web UI

### Use Cases

- Ad-hoc data exploration
- Log analysis and troubleshooting
- Data lake querying
- Cross-system joins
- Real-time analytics
- Schema evolution handling

### Installation

```bash
# Download and extract
wget https://dlcdn.apache.org/drill/drill-1.21.1/apache-drill-1.21.1.tar.gz
tar -xzf apache-drill-1.21.1.tar.gz
cd apache-drill-1.21.1

# Start Drill
bin/drill-embedded

# Or start with distributed mode
bin/drillbit.sh start
```

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Client Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Web UI   │  │ JDBC/    │  │ REST     │             │
│  │          │  │ ODBC     │  │ API      │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                   Query Layer                            │
│  ┌──────────────────────────────────────────────────┐  │
│  │              Query Parser                        │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐│  │
│  │  │   SQL      │  │   Logical  │  │  Physical  ││  │
│  │  │   Parse    │  │   Plan     │  │  Plan      ││  │
│  │  └────────────┘  └────────────┘  └────────────┘│  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                   Execution Layer                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Plan    │  │  Plan    │  │  Plan    │             │
│  │  Node 1  │  │  Node 2  │  │  Node N  │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                   Storage Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  HDFS    │  │  S3      │  │  MongoDB │             │
│  │  Plugin  │  │  Plugin  │  │  Plugin  │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
```

### Key Components

| Component | Description |
|-----------|-------------|
| **Drillbit** | Core process that handles query parsing, planning, and execution |
| **Query Parser** | Converts SQL to logical plan |
| **Logical Planner** | Creates optimized logical plan |
| **Physical Planner** | Generates physical execution plan |
| **Execution Engine** | Distributed query execution |
| **Storage Plugins** | Connectors to various data sources |

### Query Processing Flow

```
SQL Query
    │
    ▼
┌─────────────┐
│   Parser    │
└─────────────┘
    │
    ▼
┌─────────────┐
│   Logical   │
│   Planner   │
└─────────────┘
    │
    ▼
┌─────────────┐
│   Physical  │
│   Planner   │
└─────────────┘
    │
    ▼
┌─────────────┐
│  Optimizer  │
└─────────────┘
    │
    ▼
┌─────────────┐
│  Executor   │
└─────────────┘
    │
    ▼
┌─────────────┐
│  Results    │
└─────────────┘
```

---

## Schema-Free Queries

### Understanding Schema-Free

Drill's schema-free capability means you can query data without defining tables or schemas beforehand. Drill infers the schema at query time.

### Querying Nested Data

```sql
-- Query nested JSON without schema definition
SELECT 
    t.address.city,
    t.address.zipcode,
    t.orders[0].amount
FROM dfs.`/data/customers.json` t
WHERE t.address.state = 'CA';

-- Query array elements
SELECT 
    t.name,
    FLATTEN(t.tags) as tag
FROM dfs.`/data/products.json` t;

-- Query nested arrays
SELECT 
    t.customer_id,
    VALUE(t.order_items) as item
FROM dfs.`/data/orders.json` t;

-- Use LATERAL JOIN for complex nesting
SELECT 
    t.customer_id,
    item.product_name,
    item.quantity
FROM dfs.`/data/orders.json` t,
LATERAL FLATTEN(t.order_items) item;
```

### Schema Evolution Handling

```sql
-- Drill handles schema changes automatically
-- Old schema with fewer fields
SELECT * FROM dfs.`/data/legacy.json`;

-- New schema with more fields
SELECT * FROM dfs.`/data/new_format.json`;

-- Drill adapts to both without configuration
-- Fields that don't exist return NULL
SELECT 
    t.name,
    t.old_field,
    t.new_field  -- Returns NULL for old data
FROM dfs.`/data/mixed_data.json` t;

-- Check schema at runtime
SELECT TABLE_SCHEMA 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_NAME = 'customers';

-- Describe table structure
DESCRIBE dfs.`/data/customers.json`;
```

### Complex Data Types

```sql
-- Map operations
SELECT 
    t.name,
    t.metadata['version'] as version,
    MAP_KEYS(t.attributes) as attribute_keys
FROM dfs.`/data/configs.json` t;

-- Union types
SELECT 
    t.id,
    CASE 
        WHEN TYPEOF(t.value) = 'VARCHAR' THEN t.value
        WHEN TYPEOF(t.value) = 'INT' THEN CAST(t.value AS VARCHAR)
        ELSE 'unknown'
    END as string_value
FROM dfs.`/data/mixed_types.json` t;

-- Working with repeated fields
SELECT 
    t.student_id,
    scores.grade,
    scores.score
FROM dfs.`/data/students.json` t,
LATERAL FLATTEN(t.scores) scores;

-- Aggregating nested data
SELECT 
    t.department,
    AVG(salary.amount) as avg_salary,
    COUNT(*) as emp_count
FROM dfs.`/data/employees.json` t,
LATERAL FLATTEN(t.salary_history) salary
GROUP BY t.department;
```

---

## SQL Interface

### Basic SQL Operations

```sql
-- SELECT with nested fields
SELECT 
    t.first_name,
    t.last_name,
    t.address.city,
    t.address.state
FROM dfs.`/data/users.json` t
WHERE t.age > 25
ORDER BY t.last_name;

-- Aggregations
SELECT 
    t.category,
    COUNT(*) as product_count,
    AVG(t.price) as avg_price,
    MAX(t.stock) as max_stock
FROM dfs.`/data/products.json` t
GROUP BY t.category
HAVING COUNT(*) > 10;

-- JOINs across different sources
SELECT 
    c.customer_name,
    o.order_date,
    o.total_amount
FROM mongo.customers c
JOIN dfs.`/data/orders.json` o
ON c.customer_id = o.customer_id;

-- Subqueries
SELECT 
    t.name,
    t.salary
FROM dfs.`/data/employees.json` t
WHERE t.salary > (
    SELECT AVG(salary) 
    FROM dfs.`/data/employees.json`
);

-- Window functions
SELECT 
    t.employee_name,
    t.department,
    t.salary,
    ROW_NUMBER() OVER (
        PARTITION BY t.department 
        ORDER BY t.salary DESC
    ) as salary_rank
FROM dfs.`/data/employees.json` t;
```

### Drill-Specific Functions

```sql
-- Flatten nested arrays
SELECT 
    t.name,
    FLATTEN(t.tags) as tag
FROM dfs.`/data/articles.json` t;

-- Unnest arrays
SELECT 
    t.order_id,
    item.product_id,
    item.quantity
FROM dfs.`/data/orders.json` t,
UNNEST(t.items) item;

-- KV functions for key-value pairs
SELECT 
    t.id,
    KV(t.metadata, 'key') as value
FROM dfs.`/data/logs.json` t;

-- Convert types
SELECT 
    t.date_string,
    CONVERT_TO(t.date_string, 'DATE') as parsed_date
FROM dfs.`/data/events.json` t;

-- Text functions for JSON
SELECT 
    t.raw_log,
    REGEXP_REPLACE(t.raw_log, 'ERROR', 'CRITICAL') as cleaned_log
FROM dfs.`/data/app_logs.json` t;

-- Table functions
SELECT * FROM TABLE(
    (SHOW FILES 's3://bucket/path')
) fs;

-- Read HDFS files
SELECT * FROM TABLE(
    HADOOP_FS_LIST('/user/data')
) f;
```

### Information Schema Queries

```sql
-- List all schemas
SELECT SCHEMA_NAME 
FROM INFORMATION_SCHEMA.SCHEMATA;

-- List all tables in current schema
SELECT TABLE_NAME, TABLE_TYPE 
FROM INFORMATION_SCHEMA.TABLES;

-- List columns for a table
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'users';

-- List storage plugins
SHOW SCHEMAS;

-- Get plugin configuration
SELECT * FROM SYS.OPTIONS 
WHERE NAME LIKE 'store.%';
```

---

## Data Sources

### File Systems

```sql
-- Local filesystem
SELECT * FROM dfs.`/path/to/data.json`;

-- HDFS
SELECT * FROM hdfs.`/user/data/orders.parquet`;

-- Amazon S3
SELECT * FROM s3.`my-bucket/data/`.json;

-- Azure Blob Storage
SELECT * FROM azure.`container/path/`.csv;

-- Google Cloud Storage
SELECT * FROM gcs.`bucket/path/`.json;

-- Read multiple files
SELECT * FROM dfs.`/data/logs/2024-01-*.json`;

-- Read compressed files
SELECT * FROM dfs.`/data/compressed.json.gz`;

-- Read with specific format
SELECT * FROM TABLE(
    CSVPARSE(
        LOAD_FILE('/data/file.csv'),
        'header=true,delimiter=,'
    )
) data;
```

### NoSQL Databases

```sql
-- MongoDB
SELECT * FROM mongo.`database.collection`;
SELECT * FROM mongo.`database.collection` 
WHERE age > 25;

-- HBase
SELECT * FROM hbase.`table-name`;
SELECT * FROM hbase.`table-name` 
WHERE ROW_KEY = 'some-key';

-- Cassandra
SELECT * FROM cassandra.`keyspace.table`;
SELECT * FROM cassandra.`keyspace.table` 
WHERE user_id = '12345';

-- Elasticsearch
SELECT * FROM elastic.`index-name`;
SELECT * FROM elastic.`index-name` 
WHERE MATCH(query, 'search term');
```

### Streaming Platforms

```sql
-- Kafka
SELECT * FROM kafka.`topic-name`;
SELECT * FROM kafka.`topic-name` 
WHERE offset > 1000;

-- Kafka with schema
SELECT 
    t.message_id,
    t.payload.order_id,
    t.payload.amount
FROM kafka.`orders` t;

-- MapR Streams
SELECT * FROM mapr-streams.`cluster:stream`;
```

### Relational Databases

```sql
-- MySQL
SELECT * FROM mysql.`database.table`;
SELECT * FROM mysql.`database.table` 
WHERE id > 100;

-- PostgreSQL
SELECT * FROM pg.`database.schema.table`;

-- Oracle
SELECT * FROM oracle.`schema.table`;

-- SQL Server
SELECT * FROM sqlserver.`database.dbo.table`;
```

---

## Storage Plugins

### Plugin Configuration

```sql
-- List storage plugins
SHOW SCHEMAS;

-- Get plugin details
SELECT * FROM SYS.OPTIONS 
WHERE NAME LIKE 'store.%';

-- Create new plugin
CREATE MEMORY STORE my_plugin AS '/path/to/data';

-- Update plugin configuration
ALTER SESSION SET `store.s3.endpoint` = 'https://s3.amazonaws.com';

-- Drop plugin
DROP SCHEMA my_plugin;
```

### Custom Storage Plugin Configuration

```json
{
  "type": "file",
  "connection": "s3",
  "config": {
    "endpoint": "https://s3.amazonaws.com",
    "accessKey": "AKIA...",
    "secretKey": "...",
    "region": "us-east-1"
  },
  "workspaces": {
    "my-bucket": {
      "location": "/my-bucket",
      "writable": false,
      "defaultInputFormat": null
    }
  },
  "formats": {
    "json": {
      "type": "json"
    },
    "csv": {
      "type": "text",
      "extensions": ["csv"],
      "delimiter": ","
    }
  }
}
```

### Storage Plugin Examples

```sql
-- S3 plugin with custom configuration
ALTER SESSION SET `store.s3.endpoint` = 'https://s3-us-west-2.amazonaws.com';
ALTER SESSION SET `store.s3.access_key` = 'AKIA...';
ALTER SESSION SET `store.s3.secret_key` = '...';
ALTER SESSION SET `store.s3.region` = 'us-west-2';

-- HDFS plugin
ALTER SESSION SET `store.hdfs.url` = 'namenode:8020';
ALTER SESSION SET `store.hdfs.conf.fs.defaultFS` = 'hdfs://namenode:8020';

-- MongoDB plugin
ALTER SESSION SET `store.mongo.host` = 'mongodb://localhost:27017';
ALTER SESSION SET `store.mongo.credentials` = 'user:password';

-- Kafka plugin
ALTER SESSION SET `store.kafka.brokers` = 'broker1:9092,broker2:9092';
ALTER SESSION SET `store.kafka.zookeeper` = 'zookeeper:2181';
```

---

## Query Planning

### Logical Plan

```sql
-- Drill generates logical plan
EXPLAIN PLAN FOR 
SELECT t.name, t.age 
FROM dfs.`/data/users.json` t 
WHERE t.age > 25;

-- Output shows:
-- LogicalPlan
--   Project [t.name, t.age]
--     Filter [t.age > 25]
--       Scan [dfs.`/data/users.json`]
```

### Physical Plan

```sql
-- Physical plan shows execution details
EXPLAIN PLAN FOR 
SELECT t.department, AVG(t.salary) as avg_salary
FROM dfs.`/data/employees.json` t
GROUP BY t.department;

-- Output shows:
-- PhysicalPlan
--   Screen
--     Project [t.department, avg_salary]
--       HashAgg [t.department]
--         Scan [dfs.`/data/employees.json`]
```

### Query Optimization

```sql
-- Drill automatically optimizes queries
-- Predicate pushdown
SELECT * FROM dfs.`/data/large_file.json` t
WHERE t.year = 2024 AND t.month = 1;

-- Column pruning (only reads required columns)
SELECT t.name, t.email 
FROM dfs.`/data/wide_table.json` t;

-- Join optimization
SELECT c.name, o.total
FROM mongo.customers c
JOIN dfs.`/data/orders.json` o
ON c.id = o.customer_id;

-- Parallel execution
SELECT COUNT(*) 
FROM dfs.`/data/large_dataset.json`;
```

### Query Profile Analysis

```sql
-- Enable query profiling
SET `exec.query.profile` = true;

-- Run query
SELECT t.category, COUNT(*) 
FROM dfs.`/data/products.json` t
GROUP BY t.category;

-- Query profile available at:
-- http://localhost:8047/profiles

-- Analyze query performance
SELECT 
    query_id,
    start_time,
    end_time,
    outcome,
    PLAN
FROM SYS.OPTIONS 
WHERE name = 'exec.query.profile';
```

---

## Performance Optimization

### Configuration Tuning

```sql
-- Memory settings
ALTER SESSION SET `exec.memory.operator.max` = 1073741824;  -- 1GB
ALTER SESSION SET `exec.buffer.size` = 4194304;  -- 4MB
ALTER SESSION SET `exec.sort.external.buffer.size` = 268435456;  -- 256MB

-- Parallelism settings
ALTER SESSION SET `exec.parallel` = true;
ALTER SESSION SET `planner.width.max` = 8;
ALTER SESSION SET `planner.width.min` = 2;

-- Hash join settings
ALTER SESSION SET `exec.hashjoin.fallback` = true;
ALTER SESSION SET `planner.join.batch_size` = 1000;

-- Sort settings
ALTER SESSION SET `exec.sort.external.spill.batch.size` = 100000;
```

### Query Optimization Tips

```sql
-- 1. Use column pruning
-- Bad: SELECT *
SELECT * FROM dfs.`/data/wide_table.json`;

-- Good: Select only needed columns
SELECT name, email FROM dfs.`/data/wide_table.json`;

-- 2. Apply filters early
-- Bad: Filter after aggregation
SELECT category, COUNT(*) 
FROM dfs.`/data/products.json`
GROUP BY category
WHERE price > 100;

-- Good: Filter before aggregation
SELECT category, COUNT(*) 
FROM dfs.`/data/products.json`
WHERE price > 100
GROUP BY category;

-- 3. Use partition pruning
-- Bad: Scan all partitions
SELECT * FROM dfs.`/data/`.json
WHERE year = 2024;

-- Good: Use partitioned data
SELECT * FROM dfs.`/data/year=2024/month=01/`.json;

-- 4. Avoid SELECT DISTINCT when possible
-- Bad
SELECT DISTINCT customer_id 
FROM dfs.`/data/orders.json`;

-- Good: Use GROUP BY
SELECT customer_id 
FROM dfs.`/data/orders.json`
GROUP BY customer_id;
```

### Memory Management

```sql
-- Monitor memory usage
SELECT * FROM SYS.OPTIONS 
WHERE NAME LIKE 'exec.memory%';

-- Configure memory per query
ALTER SESSION SET `exec.memory.operator.max` = 2147483648;  -- 2GB

-- Configure sort buffer
ALTER SESSION SET `exec.sort.external.buffer.size` = 536870912;  -- 512MB

-- Configure hash join memory
ALTER SESSION SET `exec.hashjoin.fallback` = true;
ALTER SESSION SET `exec.hashjoin.fallback_ratio` = 0.8;
```

### Data Format Optimization

```sql
-- Use columnar formats for analytics
-- Parquet (recommended)
SELECT * FROM dfs.`/data/analytics.parquet`;

-- ORC
SELECT * FROM dfs.`/data/analytics.orc`;

-- Avro
SELECT * FROM dfs.`/data/events.avro`;

-- Optimize file sizes (aim for 128MB-1GB)
-- Use Drill's FORMAT command
ALTER SESSION SET `store.format` = 'parquet';

-- Convert data format
CREATE TABLE dfs.`/data/parquet/` AS
SELECT * FROM dfs.`/data/json/`;
```

---

## Drill vs Trino/Presto

### Architecture Comparison

| Aspect | Apache Drill | Trino/Presto |
|--------|--------------|--------------|
| **Architecture** | Peer-to-peer, shared-nothing | Coordinator-worker |
| **Schema** | Schema-free, read at query time | Schema-on-write, requires table definitions |
| **Data Sources** | File systems, NoSQL, streaming | Primarily SQL databases, file systems |
| **Query Language** | SQL with nested data extensions | Standard SQL |
| **Deployment** | Embedded or distributed | Requires separate coordinator/workers |
| **Fault Tolerance** | Automatic failover | Configurable fault tolerance |
| **Memory Management** | Per-query memory limits | Global memory management |

### Feature Comparison

| Feature | Drill | Trino/Presto |
|---------|-------|--------------|
| **Schema Evolution** | Automatic | Manual schema updates |
| **Nested Data** | Native support | Limited support |
| **Streaming** | Kafka integration | Limited streaming support |
| **NoSQL** | Native plugins | Limited support |
| **File Formats** | Many formats | Many formats |
| **UDFs** | Java UDFs | Java/Python UDFs |
| **Security** | Authentication, encryption | Authentication, authorization |
| **Monitoring** | Web UI, metrics | Web UI, metrics |

### Performance Comparison

```sql
-- Drill: Optimized for schema-free exploration
-- Best for: Ad-hoc queries, nested data, mixed sources
SELECT 
    t.logs[0].message,
    t.logs[0].level,
    COUNT(*) as log_count
FROM dfs.`/data/app_logs.json` t
WHERE t.logs[0].level = 'ERROR'
GROUP BY t.logs[0].message;

-- Trino: Optimized for structured analytics
-- Best for: Large joins, aggregations, structured data
SELECT 
    c.customer_name,
    SUM(o.amount) as total_spent
FROM hive.customers c
JOIN hive.orders o ON c.id = o.customer_id
GROUP BY c.customer_name;
```

### When to Choose Drill

- **Schema-free exploration** of diverse data sources
- **Nested data** with complex structures
- **Mixed sources** (files + NoSQL + streaming)
- **Ad-hoc queries** without schema definitions
- **Real-time log analysis**

### When to Choose Trino/Presto

- **Large-scale analytics** on structured data
- **Complex joins** across multiple tables
- **Enterprise data warehouses** with defined schemas
- **High-concurrency** workloads
- **Advanced security** requirements

---

## Best Practices

### 1. Data Organization

```sql
-- Use partitioned directories
-- Bad: Single large file
SELECT * FROM dfs.`/data/all_data.json`;

-- Good: Partitioned by date
SELECT * FROM dfs.`/data/year=2024/month=01/day=15/`.json;

-- Use appropriate file formats
-- For analytics: Parquet or ORC
-- For raw data: JSON or CSV
-- For logs: JSON or text

-- Optimize file sizes
-- Aim for 128MB-1GB per file
-- Avoid many small files
```

### 2. Query Optimization

```sql
-- 1. Project only needed columns
-- Bad
SELECT * FROM dfs.`/data/wide_table.json`;

-- Good
SELECT name, email, age FROM dfs.`/data/wide_table.json`;

-- 2. Filter early
-- Bad
SELECT category, COUNT(*) 
FROM dfs.`/data/products.json`
GROUP BY category
HAVING COUNT(*) > 10;

-- Good
SELECT category, COUNT(*) 
FROM dfs.`/data/products.json`
WHERE price > 0  -- Filter before aggregation
GROUP BY category
HAVING COUNT(*) > 10;

-- 3. Use appropriate join strategies
-- Small table + large table: Broadcast join
/*+ BROADCAST(small_table) */
SELECT * 
FROM large_table l
JOIN small_table s ON l.id = s.id;

-- 4. Avoid unnecessary DISTINCT
-- Bad
SELECT DISTINCT customer_id 
FROM dfs.`/data/orders.json`;

-- Good
SELECT customer_id 
FROM dfs.`/data/orders.json`
GROUP BY customer_id;
```

### 3. Memory Management

```sql
-- Set appropriate memory limits
ALTER SESSION SET `exec.memory.operator.max` = 1073741824;  -- 1GB

-- Monitor memory usage
SELECT * FROM SYS.OPTIONS 
WHERE NAME LIKE 'exec.memory%';

-- Configure spill to disk
ALTER SESSION SET `exec.sort.external.spill.batch.size` = 100000;
ALTER SESSION SET `exec.sort.external.spill.direct` = true;
```

### 4. Security Best Practices

```sql
-- Enable authentication
ALTER SYSTEM SET `security.auth.enabled` = true;
ALTER SYSTEM SET `security.auth.mechanism` = 'PLAIN';

-- Configure encryption
ALTER SYSTEM SET `security.ssl.enabled` = true;
ALTER SYSTEM SET `security.ssl.keyStorePath` = '/path/to/keystore';
ALTER SYSTEM SET `security.ssl.trustStorePath` = '/path/to/truststore';

-- Set up authorization
ALTER SYSTEM SET `security.authorization.enabled` = true;
ALTER SYSTEM SET `security.authorization.class` = 'org.apache.drill.security.impl.DefaultAuthorizer';
```

### 5. Monitoring and Troubleshooting

```sql
-- Enable query profiling
SET `exec.query.profile` = true;

-- Check query status
SELECT * FROM SYS.OPTIONS 
WHERE NAME = 'exec.query.profile';

-- Monitor system metrics
SELECT * FROM SYS.OPTIONS 
WHERE NAME LIKE 'exec%';

-- View query history
SELECT * FROM SYS.OPTIONS 
WHERE NAME = 'exec.query.profile';

-- Check storage plugins
SHOW SCHEMAS;
SELECT * FROM SYS.OPTIONS WHERE NAME LIKE 'store.%';
```

---

## Key Takeaways

### 1. **Schema-Free by Design**
Drill eliminates the need for schema definitions, enabling rapid data exploration across diverse sources.

### 2. **SQL for Everything**
Query files, NoSQL databases, and streaming platforms using standard SQL with extensions for complex data.

### 3. **Automatic Schema Evolution**
Drill adapts to schema changes automatically, making it ideal for evolving data landscapes.

### 4. **Native Nested Data Support**
First-class support for complex nested structures in JSON, Parquet, and other formats.

### 5. **Distributed Execution**
Horizontal scaling for petabyte-scale datasets with automatic parallelization.

### 6. **Storage Plugin Architecture**
Extensible plugin system for connecting to diverse data sources.

### 7. **Real-Time Querying**
Low-latency queries on live data without ETL processes.

### 8. **Developer-Friendly**
REST API, JDBC/ODBC drivers, and web UI for easy integration.

### 9. **Cost-Effective**
Query data where it lives without moving it to a central warehouse.

### 10. **Complementary to Other Tools**
Works alongside data warehouses and ETL pipelines for exploration and ad-hoc analysis.

---

## References

- [Apache Drill Documentation](https://drill.apache.org/docs/)
- [Drill GitHub](https://github.com/apache/drill)
- [Drill Query Language](https://drill.apache.org/docs/sql-reference/)
- [Drill Storage Plugins](https://drill.apache.org/docs/storage-plugins/)
- [Drill Best Practices](https://drill.apache.org/docs/performance-tuning/)
