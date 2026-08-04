# Apache Sqoop: Data Transfer Between Hadoop and RDBMS

## Table of Contents
1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Import Operations](#import-operations)
4. [Export Operations](#export-operations)
5. [Connectors](#connectors)
6. [Incremental Imports](#incremental-imports)
7. [Direct Connector](#direct-connector)
8. [Performance Tuning](#performance-tuning)
9. [Advanced Features](#advanced-features)
10. [Best Practices](#best-practices)
11. [Key Takeaways](#key-takeaways)

---

## Introduction

Apache Sqoop (SQL-to-Hadoop) is a tool designed for efficiently transferring bulk data between Apache Hadoop and structured datastores such as relational databases. Sqoop uses MapReduce to parallelize data transfer, providing high throughput and fault tolerance.

### Core Features

- **Parallel Import/Export**: Uses MapReduce for high-throughput data transfer
- **Incremental Import**: Only transfer new or updated data
- **Direct Connector**: Fast transfer using database-native bulk operations
- **Compression**: Support for gzip, bzip2, and other compression formats
- **Partitioning**: Import data in parallel partitions
- **Kerberos Support**: Secure authentication for Hadoop clusters

### Use Cases

- Data migration from RDBMS to Hadoop
- ETL pipelines for data warehousing
- Backup and archival to HDFS
- Analytics data preparation
- Data lake population
- Cross-system data synchronization

### Installation

```bash
# Install Sqoop
sudo apt-get install sqoop

# Or using yum
sudo yum install sqoop

# Verify installation
sqoop version

# Configure environment variables
export SQOOP_HOME=/usr/lib/sqoop
export PATH=$PATH:$SQOOP_HOME/bin
```

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    User Interface                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ Command  │  │   Web    │  │   API    │             │
│  │   Line   │  │   UI     │  │          │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                   Sqoop Server                           │
│  ┌──────────────────────────────────────────────────┐  │
│  │              Job Manager                          │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐│  │
│  │  │  Import    │  │  Export    │  │  Metadata  ││  │
│  │  │  Engine    │  │  Engine    │  │  Store     ││  │
│  │  └────────────┘  └────────────┘  └────────────┘│  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                 MapReduce Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Mapper  │  │  Reducer │  │  Output  │             │
│  │  Tasks   │  │  Tasks   │  │  Format  │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────┐
│                  Data Sources                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  MySQL   │  │PostgreSQL│  │  Oracle  │             │
│  │          │  │          │  │          │             │
│  └──────────┘  └──────────┘  └──────────┘             │
└─────────────────────────────────────────────────────────┘
```

### Key Components

| Component | Description |
|-----------|-------------|
| **Sqoop Client** | Command-line interface for users |
| **Job Manager** | Manages import/export jobs |
| **Import Engine** | Reads data from RDBMS using JDBC |
| **Export Engine** | Writes data to RDBMS using JDBC |
| **Metadata Store** | Stores job configurations and history |
| **MapReduce Jobs** | Parallel data transfer using Hadoop |

### Import Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   RDBMS     │───▶│   Sqoop     │───▶│    HDFS     │
│   Table     │    │   Import    │    │   Directory │
└─────────────┘    └─────────────┘    └─────────────┘
                           │
                           ▼
                   ┌─────────────┐
                   │  MapReduce  │
                   │    Jobs     │
                   └─────────────┘
```

### Export Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    HDFS     │───▶│   Sqoop     │───▶│   RDBMS     │
│   Files     │    │   Export    │    │   Table     │
└─────────────┘    └─────────────┘    └─────────────┘
                           │
                           ▼
                   ┌─────────────┐
                   │  MapReduce  │
                   │    Jobs     │
                   └─────────────┘
```

---

## Import Operations

### Basic Import

```bash
# Import entire table
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users

# Import with specific columns
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --columns "id,name,email" \
  --target-dir /user/hadoop/users_subset

# Import with WHERE clause
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --where "age > 25" \
  --target-dir /user/hadoop/adult_users
```

### Import to Hive

```bash
# Import directly to Hive table
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --hive-import \
  --hive-database analytics \
  --hive-table raw_users \
  --hive-overwrite

# Import with Hive partition
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table orders \
  --hive-import \
  --hive-database analytics \
  --hive-table orders \
  --hive-partition-key year \
  --hive-partition-value 2024

# Import with custom Hive delimiters
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --hive-import \
  --hive-delimiter-replacement "|" \
  --fields-terminated-by ","
```

### Import to HBase

```bash
# Import to HBase table
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --hbase-table user_profiles \
  --column-family personal \
  --hbase-row-key id \
  --hbase-create-table

# Import with specific HBase columns
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --hbase-table user_profiles \
  --column-family personal \
  --hbase-row-key id \
  --hbase-columns "personal:name,personal:email,personal:phone"
```

### Import Options

```bash
# Parallel import with mappers
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --num-mappers 8

# Import with split-by column
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --split-by id \
  --num-mappers 4

# Import with boundary query
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --boundary-query "SELECT MIN(id), MAX(id) FROM users WHERE status='active'"

# Import compressed data
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --compress \
  --compression-codec org.apache.hadoop.io.compress.SnappyCodec
```

### Import with Custom Query

```bash
# Import using custom SQL query
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --query "SELECT id, name, email FROM users WHERE age > 25 AND \$CONDITIONS" \
  --target-dir /user/hadoop/adult_users \
  --split-by id \
  --num-mappers 4

# Import with join query
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --query "SELECT u.id, u.name, o.total FROM users u JOIN orders o ON u.id = o.user_id WHERE \$CONDITIONS" \
  --target-dir /user/hadoop/user_orders \
  --split-by u.id \
  --num-mappers 4
```

---

## Export Operations

### Basic Export

```bash
# Export from HDFS to RDBMS
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users

# Export with specific columns
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --columns "id,name,email" \
  --export-dir /user/hadoop/users_subset

# Export with input delimiter
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --input-fields-terminated-by "|"
```

### Export with Staging

```bash
# Export using staging table
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --staging-table users_staging \
  --export-dir /user/hadoop/users \
  --clear-staging-table

# Export with batch size
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --batch \
  --num-mappers 4
```

### Export Options

```bash
# Export with update key
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --update-key id \
  --update-mode allowinsert

# Export with direct mode
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --direct

# Export with null handling
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --input-null-string "\\\\N" \
  --input-null-non-string "\\\\N"
```

### Export from Hive

```bash
# Export Hive table to RDBMS
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hive/warehouse/analytics.db/raw_users

# Export with Hive partition
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table orders \
  --export-dir /user/hive/warehouse/analytics.db/orders/year=2024
```

---

## Connectors

### Supported Databases

```bash
# MySQL
sqoop import \
  --connect jdbc:mysql://hostname:3306/database \
  --driver com.mysql.jdbc.Driver \
  --table users

# PostgreSQL
sqoop import \
  --connect jdbc:postgresql://hostname:5432/database \
  --driver org.postgresql.Driver \
  --table users

# Oracle
sqoop import \
  --connect jdbc:oracle:thin:@hostname:1521:database \
  --driver oracle.jdbc.OracleDriver \
  --table users

# SQL Server
sqoop import \
  --connect jdbc:sqlserver://hostname:1433;databaseName=database \
  --driver com.microsoft.sqlserver.jdbc.SQLServerDriver \
  --table users
```

### Connector Configuration

```bash
# List available connectors
sqoop list-connectors

# Import connector-specific options
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --driver com.mysql.jdbc.Driver \
  --connection-manager org.apache.sqoop.manager.MySQLManager

# Oracle-specific options
sqoop import \
  --connect jdbc:oracle:thin:@localhost:1521:orcl \
  --username system \
  --password oracle \
  --table USERS \
  --oracle-escaping-enabled \
  --fetch-size 1000

# PostgreSQL-specific options
sqoop import \
  --connect jdbc:postgresql://localhost:5432/mydb \
  --username postgres \
  --password secret \
  --table users \
  --driver org.postgresql.Driver \
  --fetch-size 1000
```

### Custom JDBC Drivers

```bash
# Add custom JDBC driver
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --driver com.mysql.jdbc.Driver \
  --jar-file /path/to/mysql-connector-java.jar

# Use alternative driver path
export SQOOP_JDBC_JARS=/path/to/drivers
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users
```

---

## Incremental Imports

### Last Modified Import

```bash
# Incremental import based on last modified timestamp
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --incremental lastmodified \
  --check-column last_updated \
  --last-value "2024-01-01 00:00:00" \
  --merge-key id

# Incremental import with append mode
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table orders \
  --target-dir /user/hadoop/orders \
  --incremental append \
  --check-column order_id \
  --last-value 1000
```

### Incremental Import with Metastore

```bash
# Create incremental import job
sqoop job --create incremental_users -- import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --incremental lastmodified \
  --check-column last_updated \
  --merge-key id

# Execute saved job
sqoop job --exec incremental_users

# List saved jobs
sqoop job --list

# Show job details
sqoop job --show incremental_users

# Delete job
sqoop job --delete incremental_users
```

### Incremental Import Strategies

```bash
# Strategy 1: Append (new rows only)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table orders \
  --target-dir /user/hadoop/orders \
  --incremental append \
  --check-column order_id \
  --last-value 1000

# Strategy 2: Last Modified (new and updated rows)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --incremental lastmodified \
  --check-column last_updated \
  --last-value "2024-01-01 00:00:00" \
  --merge-key id

# Strategy 3: Full reload (replace all data)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --delete-target-dir
```

### Automating Incremental Imports

```bash
#!/bin/bash
# incremental_import.sh

# Get last value from metastore
LAST_VALUE=$(sqoop job --show incremental_users | grep "Last value" | awk '{print $NF}')

# Run incremental import
sqoop job --exec incremental_users \
  -- --last-value "$LAST_VALUE"

# Update metadata
echo "Last import: $(date)" >> /var/log/sqoop/incremental.log
```

---

## Direct Connector

### Direct Import Mode

```bash
# Direct import from MySQL (faster)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --direct

# Direct import from PostgreSQL
sqoop import \
  --connect jdbc:postgresql://localhost:5432/mydb \
  --username postgres \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --direct

# Direct import with compression
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --direct \
  --compress
```

### Direct Export Mode

```bash
# Direct export to MySQL (faster)
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --direct

# Direct export with batch mode
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --direct \
  --batch
```

### Direct Connector vs JDBC

```bash
# JDBC mode (default)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users

# Direct mode (faster for MySQL/PostgreSQL)
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --direct

# Direct mode advantages:
# - Uses native bulk export tools (mysqldump, pg_dump)
# - Faster data transfer
# - Lower CPU usage
# - Better compression support
```

---

## Performance Tuning

### Parallel Import

```bash
# Increase number of mappers
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --num-mappers 16

# Use split-by for better parallelism
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --split-by id \
  --num-mappers 8

# Use boundary query for custom splits
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --boundary-query "SELECT MIN(id), MAX(id) FROM users WHERE status='active'" \
  --num-mappers 8
```

### Batch Processing

```bash
# Enable batch inserts for export
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --batch \
  --num-mappers 8

# Set batch size
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --batch \
  --num-mappers 8 \
  --batch-size 1000
```

### Memory and Buffer Settings

```bash
# Increase fetch size
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --fetch-size 10000

# Set split limit
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --split-limit 100000

# Configure connection pool
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --connection-param-file /path/to/connection.properties
```

### Compression

```bash
# Import with Snappy compression
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --compress \
  --compression-codec org.apache.hadoop.io.compress.SnappyCodec

# Import with Gzip compression
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --compress \
  --compression-codec org.apache.hadoop.io.compress.GzipCodec

# Import with Bzip2 compression
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --compress \
  --compression-codec org.apache.hadoop.io.compress.BZip2Codec
```

### Performance Monitoring

```bash
# Enable verbose logging
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --verbose

# Monitor MapReduce progress
# Check YARN application logs
yarn logs -applicationId application_1234567890123_0001

# Check HDFS data
hdfs dfs -ls /user/hadoop/users
hdfs dfs -du -h /user/hadoop/users

# Monitor database connections
SHOW PROCESSLIST;  -- MySQL
SELECT * FROM pg_stat_activity;  -- PostgreSQL
```

---

## Advanced Features

### Import with Columns Mapping

```bash
# Map columns between source and target
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --columns "id,name,email,created_at" \
  --map-column-hive id=bigint,name=string,email=string,created_at=timestamp

# Map columns to HBase
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --hbase-table user_profiles \
  --column-family personal \
  --hbase-row-key id \
  --map-column-hbase id=string,name=string,email=string
```

### Import with Validation

```bash
# Validate import results
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --validate \
  --validation-threshold 0.1 \
  --validation-failure-handler org.apache.sqoop.validation.AbsoluteValidationFailureHandler

# Count validation
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --validate \
  --validation-threshold 0.05
```

### Import with Free-form Query

```bash
# Complex free-form query
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --query "SELECT 
             u.id, 
             u.name, 
             u.email,
             COUNT(o.id) as order_count,
             SUM(o.total) as total_spent
           FROM users u
           LEFT JOIN orders o ON u.id = o.user_id
           WHERE u.status = 'active'
           AND \$CONDITIONS
           GROUP BY u.id, u.name, u.email" \
  --target-dir /user/hadoop/user_analytics \
  --split-by u.id \
  --num-mappers 4
```

### Import with Encoding

```bash
# Import with character encoding
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF-8 \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --encoding UTF-8

# Import with specific charset
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --connection-param-file /path/to/encoding.properties
```

---

## Best Practices

### 1. Table Design

```bash
# Good: Use primary key for splitting
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --split-by id \
  --target-dir /user/hadoop/users

# Bad: Using non-unique column for splitting
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --split-by status  # Non-unique, causes imbalance
```

### 2. File Management

```bash
# Good: Use target directory structure
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/raw/mysql/users/$(date +%Y%m%d)

# Good: Clean up old data
hdfs dfs -rm -r /user/hadoop/raw/mysql/users/20240101

# Good: Use compression
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --compress
```

### 3. Performance Optimization

```bash
# Good: Use appropriate number of mappers
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --num-mappers 8  # Based on table size and cluster capacity

# Good: Use direct mode for supported databases
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --direct

# Good: Use batch mode for exports
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --export-dir /user/hadoop/users \
  --batch
```

### 4. Error Handling

```bash
# Good: Use staging table for exports
sqoop export \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --staging-table users_staging \
  --export-dir /user/hadoop/users \
  --clear-staging-table

# Good: Validate import results
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --validate

# Good: Use error handling in scripts
set -e
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users || {
    echo "Import failed"
    exit 1
  }
```

### 5. Security

```bash
# Good: Use password file
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password-file /path/to/password.file \
  --table users \
  --target-dir /user/hadoop/users

# Good: Use Kerberos authentication
sqoop import \
  --connect jdbc:mysql://localhost:3306/mydb \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users \
  --kerberos-principal hdfs/_HOST@REALM.COM \
  --keytab /path/to/keytab

# Good: Use SSL for database connections
sqoop import \
  --connect "jdbc:mysql://localhost:3306/mydb?useSSL=true&requireSSL=true" \
  --username root \
  --password secret \
  --table users \
  --target-dir /user/hadoop/users
```

---

## Key Takeaways

### 1. **Parallel Data Transfer**
Sqoop uses MapReduce to parallelize data transfer, providing high throughput and fault tolerance.

### 2. **Incremental Import**
Support for incremental imports allows efficient synchronization of changed data between systems.

### 3. **Direct Connector**
Direct mode uses database-native bulk operations for faster data transfer when supported.

### 4. **Multiple Target Formats**
Import data directly to HDFS, Hive, or HBase with appropriate format optimizations.

### 5. **Compression Support**
Built-in support for various compression codecs reduces storage space and network transfer.

### 6. **Custom Queries**
Free-form queries enable complex data extraction with joins and transformations.

### 7. **Validation**
Import validation ensures data integrity and catches issues early.

### 8. **Security**
Support for Kerberos, SSL, and password files enables secure data transfer.

### 9. **Job Management**
Persistent job configurations enable scheduled and repeatable data transfers.

### 10. **Extensible Architecture**
Connector architecture allows support for various databases and data sources.

---

## References

- [Apache Sqoop Documentation](https://sqoop.apache.org/docs/)
- [Sqoop User Guide](https://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html)
- [Sqoop Commands](https://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html#sqoop-call)
- [Sqoop Connectors](https://sqoop.apache.org/docs/1.4.7/SqoopConnectors.html)
- [Sqoop Best Practices](https://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html#best-practices)
