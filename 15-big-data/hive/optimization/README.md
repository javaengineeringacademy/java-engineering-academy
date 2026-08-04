# Hive Optimization

Hive optimization involves tuning various aspects of the Hive query engine, storage formats, and execution strategies to achieve better performance. This guide covers Tez, LLAP, vectorization, ORC, and other optimization techniques.

## Table of Contents

1. [Optimization Overview](#optimization-overview)
2. [Tez Execution Engine](#tez-execution-engine)
3. [LLAP (Live Long and Process)](#llap-live-long-and-process)
4. [Vectorization](#vectorization)
5. [ORC File Format](#orc-file-format)
6. [Query Optimization](#query-optimization)
7. [Partition and Bucketing](#partition-and-bucketing)
8. [Compression](#compression)
9. [Statistics](#statistics)
10. [Best Practices](#best-practices)

---

## Optimization Overview

### Performance Bottlenecks

```
Hive Performance Bottlenecks:
┌─────────────────────────────────────────────────────────────┐
│                      Query Compilation                       │
│         - Parsing time                                      │
│         - Optimization time                                 │
├─────────────────────────────────────────────────────────────┤
│                      Execution Engine                        │
│         - MapReduce overhead                                │
│         - Task startup time                                 │
├─────────────────────────────────────────────────────────────┤
│                      Data Access                             │
│         - I/O operations                                    │
│         - Network transfer                                  │
├─────────────────────────────────────────────────────────────┤
│                      Memory Usage                            │
│         - JVM overhead                                      │
│         - Garbage collection                                │
└─────────────────────────────────────────────────────────────┘
```

### Optimization Strategies

| Strategy | Impact | Complexity |
|----------|--------|------------|
| **Tez Engine** | High | Low |
| **LLAP** | High | Medium |
| **Vectorization** | High | Low |
| **ORC Format** | High | Low |
| **Partitioning** | Medium | Low |
| **Bucketing** | Medium | Medium |
| **Compression** | Medium | Low |
| **Statistics** | Medium | Low |

---

## Tez Execution Engine

### What is Tez?

Apache Tez is a framework for creating a high-performance directed acyclic graph (DAG) of tasks for data processing. It replaces MapReduce as the execution engine for Hive, providing better performance.

### Tez Configuration

```sql
-- Enable Tez
SET hive.execution.engine=tez;

-- Tez configuration
SET tez.grouping.min-size=16777216;
SET tez.grouping.max-size=1073741824;

-- Tez memory settings
SET tez.task.resource.memory.mb=2048;
SET tez.task.resource.vcores=1;

-- Tez queue
SET tez.queue.name=default;
```

### Tez vs MapReduce

| Feature | Tez | MapReduce |
|---------|-----|-----------|
| **Execution** | DAG | Two-phase |
| **Intermediate Data** | In-memory | Disk-based |
| **Startup Time** | Low | High |
| **Performance** | 10-100x faster | Baseline |
| **Fault Tolerance** | Yes | Yes |

### Tez Optimization

```sql
-- Enable speculative execution
SET hive.exec.speculative=true;

-- Configure Tez sessions
SET tez.am.container.reuse.enabled=true;
SET tez.am.container.reuse.enabled=true;

-- Memory management
SET tez.task.io.sort.mb=256;
SET tez.task.io.sort.factor=100;

-- Network optimization
SET tez.runtime.shuffle.parallel.copies=10;
SET tez.runtime.shuffle.connect.timeout=180000;
```

---

## LLAP (Live Long and Process)

### What is LLAP?

LLAP (Live Long and Process) is a hybrid execution model in Hive that combines long-running daemons with DAG processing for low-latency queries.

### LLAP Architecture

```
LLAP Architecture:
┌─────────────────────────────────────────────────────────────┐
│                      LLAP Daemons                            │
│         (Long-running processes with cached data)          │
├─────────────────────────────────────────────────────────────┤
│                      YARN Containers                         │
│         (Dynamic container allocation)                     │
├─────────────────────────────────────────────────────────────┤
│                      HDFS Cache                              │
│         (Cached data for fast access)                      │
├─────────────────────────────────────────────────────────────┤
│                      Query Compiler                          │
│         (Optimizes queries for LLAP)                       │
└─────────────────────────────────────────────────────────────┘
```

### LLAP Configuration

```sql
-- Enable LLAP
SET hive.llap.enabled=true;

-- LLAP daemon settings
SET hive.llap.daemon.num.executors=8;
SET hive.llap.daemon.yarn.container.mb=4096;
SET hive.llap.daemon.yarn.container.vcores=4;

-- LLAP I/O settings
SET hive.llap.io.enabled=true;
SET hive.llap.io.memory.mode=folder;
SET hive.llap.io.memory.size=1073741824;

-- LLAP cache settings
SET hive.llap.io.allocator.size=1073741824;
SET hive.llap.io.cache.alloc.size=1073741824;
```

### LLAP Benefits

```sql
-- Low latency queries
-- LLAP daemons keep data in memory
-- Avoids repeated disk I/O

-- Interactive queries
-- Faster than traditional MapReduce/Tez

-- Data caching
-- Frequently accessed data stays cached

-- Mixed workloads
-- Supports both batch and interactive queries
```

---

## Vectorization

### What is Vectorization?

Vectorization processes data in batches of 1024 rows at a time, instead of one row at a time, improving CPU cache utilization and reducing function call overhead.

### Vectorization Configuration

```sql
-- Enable vectorization
SET hive.vectorized.execution.enabled=true;
SET hive.vectorized.execution.reduce.enabled=true;

-- Vectorized reader
SET hive.vectorized.input.format.support.enabled=true;
SET hive.vectorized.input.format.support.innermost.level.enabled=true;

-- Vectorized execution
SET hive.vectorized.execution.reduce.groupby.enabled=true;
SET hive.vectorized.execution.reduce aggregation.enabled=true;
```

### Vectorization Benefits

```
Vectorization Performance:
┌─────────────────────────────────────────────────────────────┐
│                      Row-by-Row Processing                   │
│         - 1 row at a time                                   │
│         - High function call overhead                       │
│         - Poor CPU cache utilization                        │
├─────────────────────────────────────────────────────────────┤
│                      Vectorized Processing                   │
│         - 1024 rows at a time                               │
│         - Reduced function call overhead                    │
│         - Better CPU cache utilization                      │
│         - 5-10x performance improvement                    │
└─────────────────────────────────────────────────────────────┘
```

### Vectorized Operations

```sql
-- Vectorized filter
SELECT * FROM employees WHERE age > 25;

-- Vectorized aggregation
SELECT department, COUNT(*), AVG(salary)
FROM employees
GROUP BY department;

-- Vectorized join
SELECT e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;

-- Vectorized window functions
SELECT name, salary,
       ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary) as rank
FROM employees;
```

---

## ORC File Format

### What is ORC?

ORC (Optimized Row Columnar) is a highly efficient file format for Hive, providing built-in indexes, compression, and ACID transactions.

### ORC Configuration

```sql
-- Create ORC table
CREATE TABLE employees_orc (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC;

-- ORC compression
CREATE TABLE employees_compressed (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- ORC with stripe size
CREATE TABLE employees_stripe (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC
TBLPROPERTIES ("orc.stripe.size"="67108864");
```

### ORC Features

```sql
-- ORC indexes
-- ORC builds indexes on each stripe
-- Min/max indexes for each column
-- Bloom filters for equality queries

-- ORC compression
-- Supports ZLIB, Snappy, LZO, ZSTD
-- Columnar compression for better ratios

-- ORC ACID
-- Supports transactional tables
-- INSERT, UPDATE, DELETE operations
-- Snapshot isolation
```

### ORC Optimization

```sql
-- ORC with dictionary encoding
CREATE TABLE employees_dict (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC
TBLPROPERTIES ("orc.dictionary.key.threshold"="0.8");

-- ORC with bloom filter
CREATE TABLE employees_bloom (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC
TBLPROPERTIES ("orc.bloom.filter.columns"="id,name");

-- ORC with stripe size
CREATE TABLE employees_stripe (
    id INT,
    name STRING,
    salary DOUBLE
)
STORED AS ORC
TBLPROPERTIES ("orc.stripe.size"="67108864");
```

---

## Query Optimization

### Predicate Pushdown

```sql
-- Push predicates to data source
SELECT * FROM employees WHERE age > 25;

-- ORC predicate pushdown
-- ORC reads only relevant stripes
-- Uses min/max indexes to skip data

-- Partition predicate pushdown
SELECT * FROM sales WHERE year = 2024 AND month = 1;
```

### Column Pruning

```sql
-- Read only required columns
SELECT id, name FROM employees;

-- ORC column pruning
-- ORC reads only required columns
-- Skips irrelevant column data

-- Parquet column pruning
-- Parquet also supports column pruning
```

### Join Optimization

```sql
-- Enable map join
SET hive.auto.convert.join=true;
SET hive.mapjoin.smalltable.filesize=25000000;

-- Map join for small tables
SELECT /*+ MAPJOIN(d) */ e.name, d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.id;

-- Sort-merge join for large tables
SET hive.optimize.sortmerge.bucketmapjoin=true;
SET hive.optimize.bucketmapjoin.sortedmerge=true;
```

### Bucket Optimization

```sql
-- Enable bucket optimization
SET hive.optimize.bucketmapjoin=true;
SET hive.optimize.bucketmapjoin.sortedmerge=true;

-- Bucket map join
CREATE TABLE employees_bucketed (
    id INT,
    name STRING
)
CLUSTERED BY (id) INTO 100 BUCKETS
STORED AS ORC;

-- Query benefits from bucketing
SELECT * FROM employees_bucketed WHERE id = 123;
```

---

## Partition and Bucketing

### Partition Optimization

```sql
-- Create partitioned table
CREATE TABLE sales (
    id INT,
    amount DOUBLE
)
PARTITIONED BY (year INT, month INT, day INT);

-- Partition pruning
SELECT * FROM sales WHERE year = 2024 AND month = 1 AND day = 1;

-- Dynamic partitioning
SET hive.exec.dynamic.partition=true;
SET hive.exec.dynamic.partition.mode=nonstrict;

-- Partition management
ALTER TABLE sales ADD PARTITION (year=2024, month=2, day=1);
ALTER TABLE sales DROP PARTITION (year=2024, month=1, day=1);
```

### Bucketing Optimization

```sql
-- Create bucketed table
CREATE TABLE employees_bucketed (
    id INT,
    name STRING,
    salary DOUBLE
)
CLUSTERED BY (id) INTO 100 BUCKETS
STORED AS ORC;

-- Bucket optimization
SET hive.optimize.bucketmapjoin=true;
SET hive.optimize.bucketmapjoin.sortedmerge=true;

-- Bucket pruning
SELECT * FROM employees_bucketed WHERE id = 123;
```

### Partition vs Bucketing

| Feature | Partitioning | Bucketing |
|---------|--------------|-----------|
| **Data Distribution** | By column values | By hash |
| **Query Pruning** | Partition pruning | Bucket pruning |
| **Join Optimization** | Limited | Excellent |
| **Cardinality** | Low-medium | High |
| **Management** | Manual | Automatic |

---

## Compression

### Compression Codecs

```sql
-- Enable compression
SET hive.exec.compress.output=true;
SET mapreduce.output.fileoutputformat.compress=true;

-- Snappy compression
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

-- Gzip compression
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.GzipCodec;

-- LZO compression
SET mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzoCodec;
```

### Compression Strategies

```sql
-- Snappy: Fast compression/decompression
-- Best for: General use, intermediate data

-- Gzip: High compression ratio
-- Best for: Archive data, cold storage

-- LZO: Splittable compression
-- Best for: MapReduce, large files

-- ZSTD: Balanced compression
-- Best for: General use, good ratio
```

### Compression Configuration

```sql
-- Output compression
SET hive.exec.compress.output=true;
SET mapreduce.output.fileoutputformat.compress=true;
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

-- Intermediate compression
SET hive.exec.compress.intermediate=true;
SET mapreduce.map.output.compress=true;
SET mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

-- ORC compression
CREATE TABLE employees_compressed (
    id INT,
    name STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");
```

---

## Statistics

### Table Statistics

```sql
-- Compute table statistics
ANALYZE TABLE employees COMPUTE STATISTICS;

-- Compute column statistics
ANALYZE TABLE employees COMPUTE STATISTICS FOR COLUMNS id, name, salary;

-- View statistics
DESC FORMATTED employees;
DESC EXTENDED employees;

-- Statistics for partitioned tables
ANALYZE TABLE sales PARTITION (year=2024, month=1) COMPUTE STATISTICS;
```

### Statistics Benefits

```sql
-- Query optimization
-- Hive uses statistics for query planning
-- Better join strategies
-- Better join order

-- Partition pruning
-- Statistics help with partition pruning
-- Faster queries on partitioned tables

-- Resource estimation
-- Statistics help estimate resource usage
-- Better resource allocation
```

### Statistics Configuration

```sql
-- Enable automatic statistics
SET hive.stats.autogather=true;

-- Statistics for columns
SET hive.stats.column.autogather=true;

-- Statistics for partitions
SET hive.stats.fetch.column.stats=true;

-- Statistics for joins
SET hive.optimize.sortmerge.bucketmapjoin=true;
```

---

## Best Practices

### 1. File Format Selection

```sql
-- Use ORC for Hive
CREATE TABLE employees (
    id INT,
    name STRING
)
STORED AS ORC;

-- Use Parquet for Spark
CREATE TABLE employees_spark (
    id INT,
    name STRING
)
STORED AS PARQUET;

-- Use Avro for schema evolution
CREATE TABLE employees_avro (
    id INT,
    name STRING
)
STORED AS AVRO;
```

### 2. Compression Configuration

```sql
-- Use Snappy for general use
SET hive.exec.compress.output=true;
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

-- Use Gzip for archive
SET mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.GzipCodec;

-- Use ORC compression
CREATE TABLE employees_compressed (
    id INT,
    name STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");
```

### 3. Query Optimization

```sql
-- Use partition pruning
SELECT * FROM sales WHERE year = 2024 AND month = 1;

-- Use column pruning
SELECT id, name FROM employees;

-- Use map join for small tables
SET hive.auto.convert.join=true;

-- Use vectorization
SET hive.vectorized.execution.enabled=true;
```

### 4. Statistics Management

```sql
-- Compute statistics
ANALYZE TABLE employees COMPUTE STATISTICS;
ANALYZE TABLE employees COMPUTE STATISTICS FOR COLUMNS;

-- Enable auto statistics
SET hive.stats.autogather=true;

-- Monitor statistics
DESC FORMATTED employees;
```

### 5. Memory Configuration

```sql
-- Configure Tez memory
SET tez.task.resource.memory.mb=2048;

-- Configure LLAP memory
SET hive.llap.daemon.yarn.container.mb=4096;

-- Configure memory for queries
SET hive.exec.parallel=true;
SET hive.exec.parallel.thread.number=8;
```

---

## Common Patterns

### Pattern 1: Performance Tuning

```sql
-- Enable optimizations
SET hive.execution.engine=tez;
SET hive.vectorized.execution.enabled=true;
SET hive.auto.convert.join=true;

-- Use ORC
CREATE TABLE employees_optimized (
    id INT,
    name STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Compute statistics
ANALYZE TABLE employees_optimized COMPUTE STATISTICS;
```

### Pattern 2: Large Table Optimization

```sql
-- Partition large table
CREATE TABLE large_table (
    id INT,
    data STRING
)
PARTITIONED BY (year INT, month INT, day INT);

-- Use ORC with compression
CREATE TABLE large_table_orc (
    id INT,
    data STRING
)
PARTITIONED BY (year INT, month INT, day INT)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Compute statistics
ANALYZE TABLE large_table_orc COMPUTE STATISTICS;
```

### Pattern 3: Interactive Queries

```sql
-- Enable LLAP
SET hive.llap.enabled=true;
SET hive.llap.daemon.num.executors=8;

-- Use ORC with bloom filters
CREATE TABLE interactive_table (
    id INT,
    name STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.bloom.filter.columns"="id");

-- Use vectorization
SET hive.vectorized.execution.enabled=true;
```

### Pattern 4: Batch Processing

```sql
-- Enable Tez
SET hive.execution.engine=tez;

-- Use compression
SET hive.exec.compress.intermediate=true;
SET hive.exec.compress.output=true;

-- Use appropriate file format
CREATE TABLE batch_table (
    id INT,
    data STRING
)
STORED AS ORC
TBLPROPERTIES ("orc.compress"="SNAPPY");

-- Enable speculative execution
SET hive.exec.speculative=true;
```

---

## Conclusion

Hive optimization involves:

- **Tez engine** for faster execution
- **LLAP** for low-latency queries
- **Vectorization** for batch processing
- **ORC format** for efficient storage
- **Query optimization** for better planning
- **Partitioning and bucketing** for data organization
- **Compression** for reduced I/O
- **Statistics** for better optimization

Key takeaways:

1. **Use Tez** as the execution engine
2. **Enable vectorization** for batch queries
3. **Use ORC** for Hive tables
4. **Partition large tables** for query performance
5. **Compute statistics** for query optimization

Hive optimization is essential for achieving good performance on Hadoop workloads.