# Apache Hive

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [HiveQL (HQL)](#hiveql-hql)
- [Data Types](#data-types)
- [Tables and Databases](#tables-and-databases)
- [Partitions and Buckets](#partitions-and-buckets)
- [Internal vs External Tables](#internal-vs-external-tables)
- [Hive Execution Engine](#hive-execution-engine)
- [Optimization](#optimization)
- [SerDe (Serializer/Deserializer)](#serde-serializerdeserializer)
- [UDFs (User Defined Functions)](#udfs-user-defined-functions)
- [Security and Governance](#security-and-governance)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Apache Hive is a data warehouse infrastructure built on top of Apache Hadoop
that provides SQL-like query capabilities (HiveQL) for querying and managing
large datasets stored in HDFS. Hive abstracts the complexity of MapReduce
programming, allowing users familiar with SQL to query data at scale.

### Key Characteristics

- **SQL-like interface**: HiveQL for declarative data processing
- **Schema on read**: Data schema applied when reading, not writing
- **Extensible**: Supports custom UDFs, SerDe, and formats
- **Scalable**: Processes petabytes of data across Hadoop clusters
- **Integration**: Works with HDFS, HBase, S3, and other storage systems

### When to Use Hive

- Ad-hoc querying of large datasets
- Data warehousing and analytics on Hadoop
- ETL pipelines using SQL-like syntax
- Reporting and business intelligence on big data
- Batch processing of structured and semi-structured data

### Hive vs Traditional RDBMS

| Feature | Hive | RDBMS |
|---------|------|-------|
| Data Storage | HDFS, S3 | Local/NAS |
| Schema | Schema on Read | Schema on Write |
| Processing | Batch (MapReduce/Tez) | Interactive |
| Latency | High (seconds-minutes) | Low (milliseconds) |
| Data Size | Petabytes | Terabytes |
| ACID | Limited (since 0.13) | Full Support |
| Indexing | Limited | Full Indexing |
| Normalization | Denormalized preferred | Normalized |

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Client Layer                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │   CLI    │  │   JDBC   │  │  Web Interface   │  │
│  │          │  │  /ODBC   │  │  (Hue/Tabview)   │  │
│  └────┬─────┘  └────┬─────┘  └────────┬─────────┘  │
└───────┼──────────────┼─────────────────┼─────────────┘
        │              │                 │
┌───────▼──────────────▼─────────────────▼─────────────┐
│                   Hive Server                         │
│            (HiveServer2 / Thrift)                     │
├──────────────────────────────────────────────────────┤
│              Driver / Compiler                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  Parser  │  │Optimizer │  │   Query Planner  │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
├──────────────────────────────────────────────────────┤
│              Execution Engine                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  MapReduce│  │   Tez    │  │     Spark       │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
├──────────────────────────────────────────────────────┤
│              Metastore                                │
│  ┌──────────────────────────────────────────────┐   │
│  │  Database (MySQL/PostgreSQL/Derby)           │   │
│  │  - Table metadata                            │   │
│  │  - Partition information                     │   │
│  │  - Column statistics                         │   │
│  └──────────────────────────────────────────────┘   │
├──────────────────────────────────────────────────────┤
│              Storage Layer                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │   HDFS   │  │   S3     │  │   HBase          │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
└──────────────────────────────────────────────────────┘
```

### Components

1. **HiveServer2**: Thrift-based server for client connections
2. **Metastore**: Central repository for table metadata
3. **Driver**: Parses, optimizes, and executes queries
4. **Compiler**: Converts HiveQL to execution plans
5. **Optimizer**: Applies logical and physical optimizations
6. **Execution Engine**: Runs the compiled query (MR, Tez, Spark)

---

## HiveQL (HQL)

### Data Definition Language (DDL)

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS analytics
COMMENT 'Analytics database'
LOCATION '/user/hive/analytics';

-- Use database
USE analytics;

-- Create table
CREATE TABLE IF NOT EXISTS users (
    user_id INT,
    username STRING,
    email STRING,
    age INT,
    created_at TIMESTAMP
)
COMMENT 'User information'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/user/hive/users';

-- Alter table
ALTER TABLE users ADD COLUMNS (phone STRING);
ALTER TABLE users CHANGE COLUMN age user_age INT;
ALTER TABLE users DROP COLUMN phone;

-- Drop table
DROP TABLE IF EXISTS users;
```

### Data Manipulation Language (DML)

```sql
-- Insert data
INSERT INTO TABLE users
SELECT user_id, username, email, age, created_at
FROM staging_users
WHERE status = 'active';

-- Insert overwrite (replace existing data)
INSERT OVERWRITE TABLE user_summary
SELECT department, COUNT(*) as user_count
FROM users
GROUP BY department;

-- Load data from file
LOAD DATA INPATH '/data/users.csv'
INTO TABLE users;

-- Update (requires ACID support)
UPDATE users SET age = age + 1
WHERE created_at > '2024-01-01';

-- Delete
DELETE FROM users WHERE user_id = 100;

-- Merge (UPSERT)
MERGE INTO users AS target
USING new_users AS source
ON target.user_id = source.user_id
WHEN MATCHED THEN UPDATE SET
    email = source.email,
    age = source.age
WHEN NOT MATCHED THEN INSERT VALUES
    (source.user_id, source.username, source.email, source.age);
```

### Data Query Language (DQL)

```sql
-- Basic SELECT
SELECT user_id, username, email
FROM users
WHERE age >= 18
ORDER BY username
LIMIT 100;

-- Aggregation
SELECT
    department,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary
FROM employees
GROUP BY department
HAVING COUNT(*) > 10;

-- Joins
SELECT
    o.order_id,
    o.order_date,
    u.username,
    p.product_name,
    oi.quantity,
    oi.price
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id;

-- Subqueries
SELECT * FROM users
WHERE user_id IN (
    SELECT user_id FROM orders
    WHERE order_date >= '2024-01-01'
);

-- Window functions
SELECT
    user_id,
    order_date,
    amount,
    SUM(amount) OVER (
        PARTITION BY user_id
        ORDER BY order_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) as running_total,
    ROW_NUMBER() OVER (
        PARTITION BY user_id
        ORDER BY order_date
    ) as order_rank
FROM orders;
```

---

## Data Types

### Primitive Types

```sql
-- Numeric types
TINYINT         -- 1-byte signed integer
SMALLINT        -- 2-byte signed integer
INT             -- 4-byte signed integer
BIGINT          -- 8-byte signed integer
FLOAT           -- 4-byte single precision
DOUBLE          -- 8-byte double precision
DECIMAL(p,s)    -- Arbitrary precision

-- String types
STRING          -- Variable-length string
VARCHAR(n)      -- Variable-length with max length
CHAR(n)         -- Fixed-length string

-- Date/Time types
DATE            -- Date (YYYY-MM-DD)
TIMESTAMP       -- Date and time
INTERVAL        -- Time interval

-- Binary types
BINARY          -- Binary data

-- Boolean types
BOOLEAN         -- TRUE/FALSE
```

### Complex Types

```sql
-- Array
CREATE TABLE array_example (
    id INT,
    tags ARRAY<STRING>
);

SELECT tags[0] FROM array_example;

-- Map
CREATE TABLE map_example (
    id INT,
    properties MAP<STRING, STRING>
);

SELECT properties['key'] FROM map_example;

-- Struct
CREATE TABLE struct_example (
    id INT,
    name STRUCT<first: STRING, last: STRING, middle: STRING>
);

SELECT name.first, name.last FROM struct_example;

-- Nested complex types
CREATE TABLE nested_example (
    id INT,
    departments ARRAY<STRUCT<
        name: STRING,
        employees: MAP<STRING, ARRAY<STRING>>
    >>
);
```

---

## Tables and Databases

### Table Types

```sql
-- Managed (Internal) Table
CREATE MANAGED TABLE managed_table (
    id INT,
    name STRING
)
STORED AS ORC;

-- External Table
CREATE EXTERNAL TABLE external_table (
    id INT,
    name STRING
)
LOCATION '/data/external/'
TBLPROPERTIES ('external' = 'true');

-- Temporary Table (session-scoped)
CREATE TEMPORARY TABLE temp_table AS
SELECT * FROM permanent_table WHERE condition;

-- Transaction Table (ACID)
CREATE TABLE acid_table (
    id INT,
    name STRING
)
CLUSTERED BY (id) INTO 4 BUCKETS
STORED AS ORC
TBLPROPERTIES (
    'transactional' = 'true',
    'orc.compress' = 'SNAPPY'
);
```

### Table Properties

```sql
-- Set table properties
ALTER TABLE table_name SET TBLPROPERTIES (
    'orc.compress' = 'SNAPPY',
    'orc.bloom.filter.columns' = 'id',
    'orc.create.index' = 'true'
);

-- View table properties
SHOW TBLPROPERTIES table_name;

-- Describe table
DESCRIBE FORMATTED table_name;
DESCRIBE EXTENDED table_name;
```

---

## Partitions and Buckets

### Partitioning

Partitions divide table data into separate directories based on column values.

```sql
-- Static partitioning
CREATE TABLE sales (
    id INT,
    amount DOUBLE,
    product STRING
)
PARTITIONED BY (year INT, month INT)
STORED AS ORC;

-- Insert into specific partition
INSERT INTO sales PARTITION (year=2024, month=1)
SELECT id, amount, product FROM staging_sales
WHERE year = 2024 AND month = 1;

-- Dynamic partitioning
SET hive.exec.dynamic.partition = true;
SET hive.exec.dynamic.partition.mode = nonstrict;

INSERT INTO sales PARTITION (year, month)
SELECT id, amount, product, year, month FROM staging_sales;

-- Show partitions
SHOW PARTITIONS sales;

-- Add partition
ALTER TABLE sales ADD PARTITION (year=2025, month=1)
LOCATION '/user/hive/sales/year=2025/month=1';

-- Drop partition
ALTER TABLE sales DROP PARTITION (year=2024, month=12);
```

### Bucketing

Buckets distribute data into fixed number of files based on hash of a column.

```sql
-- Create bucketed table
CREATE TABLE bucketed_users (
    user_id INT,
    username STRING,
    email STRING
)
CLUSTERED BY (user_id) INTO 256 BUCKETS
STORED AS ORC;

-- Enable bucketing optimization
SET hive.enforce.bucketing = true;

-- Insert bucketed data
INSERT OVERWRITE TABLE bucketed_users
SELECT * FROM staging_users;

-- Bucket sampling
SELECT * FROM bucketed_users TABLESAMPLE(BUCKET 1 OUT OF 256 ON user_id);
```

---

## Internal vs External Tables

### Managed (Internal) Tables

```sql
-- Hive manages data lifecycle
CREATE TABLE managed_data (
    id INT,
    value STRING
);

-- Dropping table removes data from HDFS
DROP TABLE managed_data;
-- Data at /user/hive/warehouse/managed_data is DELETED
```

### External Tables

```sql
-- Hive does NOT manage data lifecycle
CREATE EXTERNAL TABLE external_data (
    id INT,
    value STRING
)
LOCATION '/data/external/';

-- Dropping table does NOT remove data from HDFS
DROP TABLE external_data;
-- Data at /data/external/ is PRESERVED
```

### Comparison

| Aspect | Internal Table | External Table |
|--------|---------------|----------------|
| Data ownership | Hive manages | User manages |
| DROP behavior | Deletes data | Preserves data |
| Location | Warehouse directory | Any HDFS location |
| Use case | Temporary/staging data | Shared data |
| Schema changes | Full control | Read-only |
| Performance | Same | Same |

---

## Hive Execution Engine

### MapReduce (Default, Deprecated)

```sql
SET hive.execution.engine = mr;
-- Slow, disk-intensive
-- Intermediate data written to HDFS
```

### Tez (Recommended)

```sql
SET hive.execution.engine = tez;
-- Faster, DAG-based execution
-- Better resource utilization
-- Reduced I/O operations
```

### Spark

```sql
SET hive.execution.engine = spark;
-- In-memory processing
-- Good for iterative workloads
-- Requires Spark installation
```

### Configuration

```sql
-- Set execution engine
SET hive.execution.engine = tez;

-- Tez-specific settings
SET tez.grouping.min-size = 16777216;
SET tez.grouping.max-size = 1073741824;
SET tez.task.resource.memory.mb = 4096;

-- Hive-specific settings
SET hive.exec.parallel = true;
SET hive.exec.parallel.thread.number = 8;
SET hive.vectorized.execution.enabled = true;
```

---

## Optimization

### Partitioning Strategy

```sql
-- Choose low-cardinality columns for partitioning
-- Good: year, month, day, region
-- Bad: user_id, timestamp

-- Partition by date for time-series data
CREATE TABLE logs (
    timestamp BIGINT,
    level STRING,
    message STRING
)
PARTITIONED BY (year INT, month INT, day INT);
```

### Bucketing Strategy

```sql
-- Use high-cardinality columns for bucketing
-- Good: user_id, transaction_id
-- Bad: boolean flags

-- Bucket for efficient joins
CREATE TABLE users_bucketed (
    user_id INT,
    username STRING
)
CLUSTERED BY (user_id) INTO 100 BUCKETS;

CREATE TABLE orders_bucketed (
    order_id INT,
    user_id INT,
    amount DOUBLE
)
CLUSTERED BY (user_id) INTO 100 BUCKETS;

-- Bucketed join optimization
SET hive.optimize.bucketmapjoin = true;
SET hive.optimize.bucketmapjoin.sortedmerge = true;
```

### File Format Optimization

```sql
-- ORC (Optimized Row Columnar) - Recommended
CREATE TABLE optimized_table (
    id INT,
    name STRING,
    value DOUBLE
)
STORED AS ORC
TBLPROPERTIES (
    'orc.compress' = 'SNAPPY',
    'orc.create.index' = 'true',
    'orc.bloom.filter.columns' = 'id'
);

-- Parquet
CREATE TABLE parquet_table (
    id INT,
    name STRING,
    value DOUBLE
)
STORED AS PARQUET
TBLPROPERTIES (
    'parquet.compression' = 'SNAPPY'
);
```

### Query Optimization

```sql
-- Enable optimizations
SET hive.optimize.cp = true;              // Column pruning
SET hive.optimize.ppd = true;             // Predicate pushdown
SET hive.optimize.skewjoin = true;        // Skew join optimization
SET hive.auto.convert.join = true;        // Map-side join
SET hive.auto.convert.join.noconditionaltask = true;

-- Analyze table statistics
ANALYZE TABLE users COMPUTE STATISTICS;
ANALYZE TABLE users COMPUTE STATISTICS FOR COLUMNS user_id, username;

-- Caching
SET hive.cache.expr.evaluation = true;
SET hive.vectorized.execution.enabled = true;
```

### Memory Configuration

```sql
-- Map-side settings
SET mapreduce.map.memory.mb = 4096;
SET mapreduce.map.java.opts = -Xmx3276m;

-- Reduce-side settings
SET mapreduce.reduce.memory.mb = 8192;
SET mapreduce.reduce.java.opts = -Xmx6553m;

-- Hive settings
SET hive.exec.memory.limit = 838860800;
SET hive.vectorized.execution.enabled = true;
```

---

## SerDe (Serializer/Deserializer)

### Built-in SerDes

```sql
-- LazySimpleSerDe (default for text)
CREATE TABLE text_table (
    id INT,
    name STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
WITH SERDEPROPERTIES (
    'field.delim' = '\t',
    'line.delim' = '\n'
);

-- JsonSerDe
CREATE TABLE json_table (
    id INT,
    name STRING,
    data MAP<STRING, STRING>
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.JsonSerDe';

-- RegexSerDe (for log parsing)
CREATE TABLE log_table (
    ip STRING,
    timestamp STRING,
    method STRING,
    url STRING,
    status INT
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.RegexSerDe'
WITH SERDEPROPERTIES (
    'input.regex' = '([\\d.]+) \\S+ \\S+ \\[(.+?)\\] "(.+?) (.+?)" (\\d+)'
);

-- OpenCSVSerDe
CREATE TABLE csv_table (
    id INT,
    name STRING,
    email STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES (
    'separatorChar' = ',',
    'quoteChar' = '"',
    'escapeChar' = '\\'
);
```

### Custom SerDe

```java
public class CustomSerDe implements SerDe {

    @Override
    public void initialize(Configuration conf, Properties tbl)
            throws SerDeException {
        // Initialize with table properties
    }

    @Override
    public Object serialize(Object obj, ObjectInspector oi)
            throws SerDeException {
        // Convert object to writable
        return null;
    }

    @Override
    public Object deserialize(Writable writable)
            throws SerDeException {
        // Convert writable to object
        return null;
    }

    @Override
    public ObjectInspector getObjectInspector()
            throws SerDeException {
        // Return object inspector
        return null;
    }
}
```

---

## UDFs (User Defined Functions)

### Types of UDFs

```sql
-- Regular UDF (row-level)
CREATE FUNCTION my_upper AS 'com.example.MyUpper'
USING JAR 'hdfs:///jars/my-udf.jar';

-- Aggregate UDF (multi-row)
CREATE FUNCTION my_sum AS 'com.example.MySum'
USING JAR 'hdfs:///jars/my-udf.jar';

-- Table-generating UDF (one-to-many)
CREATE FUNCTION my_explode AS 'com.example.MyExplode'
USING JAR 'hdfs:///jars/my-udf.jar';
```

### Creating UDFs in Java

```java
// Row-level UDF
public class MyUpper extends UDF {
    public Text evaluate(Text input) {
        if (input == null) return null;
        return new Text(input.toString().toUpperCase());
    }
}

// Aggregate UDF
public class MySum extends GenericUDAFResolver2 {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters)
            throws SemanticException {
        return new MySumEvaluator();
    }

    public static class MySumEvaluator extends GenericUDAFEvaluator {
        private DoubleWritable result;

        @Override
        public void init(Mode mode, ObjectInspector[] parameters)
                throws HiveException {
            result = new DoubleWritable(0);
        }

        @Override
        public AggregationBuffer getAggregationBuffer()
                throws HiveException {
            return new SumBuffer();
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters)
                throws HiveException {
            SumBuffer buffer = (SumBuffer) agg;
            Double value = ((DoubleObjectInspector) parameters[0])
                .get(parameters[1]);
            buffer.sum += value;
        }

        @Override
        public Object terminate(AggregationBuffer agg)
                throws HiveException {
            SumBuffer buffer = (SumBuffer) agg;
            result.set(buffer.sum);
            return result;
        }

        static class SumBuffer implements AggregationBuffer {
            double sum = 0;
        }
    }
}
```

### Python UDFs

```python
# Using PyHive
from pyhive import hive
from hive_udf import UDF

class MyUpper(UDF):
    def evaluate(self, value):
        if value is None:
            return None
        return value.upper()

# Register in Hive
# CREATE FUNCTION my_upper AS 'com.example.MyUpper'
# USING JAR 'hdfs:///jars/my-udf.jar';
```

### Using UDFs

```sql
-- Row-level UDF
SELECT my_upper(username) FROM users;

-- Aggregate UDF
SELECT department, my_sum(salary) FROM employees GROUP BY department;

-- Built-in functions
SELECT
    upper(name),
    lower(email),
    concat(first_name, ' ', last_name),
    substr(phone, 1, 3),
    regexp_replace(email, '@.*', ''),
    from_unixtime(timestamp, 'yyyy-MM-dd'),
    unix_timestamp(date_string, 'yyyy-MM-dd'),
    datediff(end_date, start_date),
    coalesce(field1, field2, 'default'),
    nvl(field1, 'default'),
    if(condition, true_val, false_val),
    case when condition then value else default end
FROM users;
```

---

## Security and Governance

### Authentication

```sql
-- Kerberos authentication
SET hive.server2.authentication = KERBEROS;
SET hive.server2.kerberos.keytab = /etc/hive.keytab;
SET hive.server2.kerberos.principal = hive/_HOST@REALM.COM;

-- LDAP authentication
SET hive.server2.authentication = LDAP;
SET hive.server2.authentication.ldap.url = ldap://ldap.example.com;
SET hive.server2.authentication.ldap.baseDN = ou=users,dc=example,dc=com;
```

### Authorization

```sql
-- SQL standard authorization
SET hive.security.authorization.enabled = true;
SET hive.security.authorization.manager =
    org.apache.hadoop.hive.ql.security.authorization.plugin.sqlstd.SQLStdHiveAuthorizerFactory;

-- Grant permissions
GRANT SELECT ON DATABASE analytics TO ROLE analyst;
GRANT INSERT ON TABLE users TO ROLE etl_user;
GRANT ALL ON TABLE temp_table TO USER admin;

-- Show grants
SHOW GRANT ROLE analyst ON DATABASE analytics;
```

### Data Masking

```sql
-- Create masking policy
SET hive.semantic.analyzer.factory.output=
    org.apache.hadoop.hive.ql.security.authorization.plugin.AuthorizationFactory;

-- Column-level masking
CREATE VIEW masked_users AS
SELECT
    user_id,
    mask(email) as email_masked,      // xxx@xxx.com
    mask_first_n(phone, 3) as phone_masked,  // ***5678
    unmask(email) as email_original   // Only for authorized users
FROM users;
```

### Audit Logging

```sql
-- Enable audit logging
SET hive.server2.audit.logger = org.apache.hadoop.hive.ql.security.AuditLogger;
SET hive.server2.audit.logger.destination = LOG4J;

-- Log query details
SET hive.querylog.enable.plan.progress = true;
SET hive.querylog.location = /tmp/hive/querylog;
```

---

## Best Practices

### Table Design

1. **Use ORC or Parquet**: Columnar formats for better compression and performance
2. **Partition wisely**: Low-cardinality columns (year, month, region)
3. **Bucket for joins**: High-cardinality columns for even distribution
4. **Normalize data types**: Use appropriate INT sizes (TINYINT for small values)

### Query Optimization

1. **Filter early**: Push WHERE clauses as close to source as possible
2. **Select specific columns**: Avoid `SELECT *`
3. **Use partitions**: Always include partition columns in WHERE clause
4. **Enable vectorization**: Process batches of rows at once

### ETL Patterns

```sql
-- Incremental loading pattern
INSERT OVERWRITE TABLE target_table PARTITION (date)
SELECT
    column1,
    column2,
    date
FROM source_table
WHERE date >= '${start_date}' AND date < '${end_date}';

-- Slowly changing dimensions
MERGE INTO dim_users AS target
USING new_users AS source
ON target.user_id = source.user_id
WHEN MATCHED AND source.version > target.version THEN
    UPDATE SET
        name = source.name,
        email = source.email,
        version = source.version
WHEN NOT MATCHED THEN
    INSERT VALUES (source.user_id, source.name, source.email, source.version);
```

### Monitoring

```sql
-- Check query performance
EXPLAIN EXTENDED SELECT * FROM users WHERE user_id = 100;

-- Analyze table statistics
ANALYZE TABLE users COMPUTE STATISTICS;
ANALYZE TABLE users COMPUTE STATISTICS FOR COLUMNS;

-- View query execution plan
EXPLAIN SELECT * FROM users JOIN orders ON users.user_id = orders.user_id;
```

---

## Examples

### Complete ETL Pipeline

```sql
-- 1. Create staging table
CREATE EXTERNAL TABLE staging_events (
    event_id STRING,
    user_id STRING,
    event_type STRING,
    event_data STRING,
    event_timestamp STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.JsonSerDe'
LOCATION '/data/events/staging/';

-- 2. Create partitioned target table
CREATE TABLE fact_events (
    event_id STRING,
    user_id INT,
    event_type STRING,
    event_data MAP<STRING, STRING>
)
PARTITIONED BY (event_date STRING, event_hour INT)
STORED AS ORC;

-- 3. Load and transform
SET hive.exec.dynamic.partition = true;
SET hive.exec.dynamic.partition.mode = nonstrict;

INSERT OVERWRITE TABLE fact_events PARTITION (event_date, event_hour)
SELECT
    event_id,
    CAST(user_id AS INT),
    event_type,
    cast(json_tuple(event_data, 'key1', 'key2') as map<string,string>),
    substr(event_timestamp, 1, 10) as event_date,
    hour(event_timestamp) as event_hour
FROM staging_events
WHERE event_timestamp IS NOT NULL;

-- 4. Analyze
SELECT
    event_date,
    event_type,
    COUNT(*) as event_count,
    COUNT(DISTINCT user_id) as unique_users
FROM fact_events
WHERE event_date >= '2024-01-01'
GROUP BY event_date, event_type;
```

---

## References

- [Apache Hive Documentation](https://cwiki.apache.org/confluence/display/Hive/)
- [Hive LanguageManual](https://cwiki.apache.org/confluence/display/Hive/LanguageManual)
- [Hive Optimization](https://cwiki.apache.org/confluence/display/Hive/Performance+Tuning)
- [Hive SerDe](https://cwiki.apache.org/confluence/display/Hive/SerDe)
- [Hive UDFs](https://cwiki.apache.org/confluence/display/Hive/Hive+Plugins)
