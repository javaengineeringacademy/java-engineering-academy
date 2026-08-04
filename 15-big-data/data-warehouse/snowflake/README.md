# Snowflake

## Overview
Snowflake is a cloud-native data warehouse with separation of compute and storage, enabling independent scaling.

## Architecture
- **Storage Layer**: Cloud storage (S3, Azure Blob, GCS)
- **Compute Layer**: Virtual warehouses
- **Cloud Services**: Metadata, optimization, security

## Creating Objects
```sql
-- Database and schema
CREATE DATABASE analytics;
CREATE SCHEMA raw;

-- Table
CREATE TABLE users (
    id INTEGER AUTOINCREMENT,
    name VARCHAR(100),
    email VARCHAR(255),
    created_at TIMESTAMP_NTZ DEFAULT CURRENT_TIMESTAMP()
);

-- Stage (external)
CREATE STAGE s3_stage
    URL = 's3://bucket/data/'
    STORAGE_INTEGRATION = s3_integration;

-- Pipe (auto-ingest)
CREATE PIPE s3_pipe
    AUTO_INGEST = TRUE
    AS COPY INTO users
    FROM @s3_stage
    FILE_FORMAT = (TYPE = PARQUET);
```

## Time Travel
```sql
-- Query historical data
SELECT * FROM users AT (TIMESTAMP => '2024-01-15 10:00:00');

-- Restore table
CREATE TABLE users_backup CLONE users
    AT (TIMESTAMP => '2024-01-15 10:00:00');
```

## Best Practices
1. Use clustering keys for large tables
2. Set appropriate warehouse sizes
3. Use materialized views for repeated queries
4. Monitor credit usage
5. Use time travel for data recovery
