# Apache Iceberg

## Overview

Apache Iceberg is an open table format for large analytic datasets. It provides ACID transactions, schema evolution, hidden partitioning, and time travel capabilities on top of data lakes.

## Architecture

### Table Metadata

```
Table Metadata
├── table-metadata.json
│   ├── format-version
│   ├── table-uuid
│   ├── location
│   ├── last-updated-ms
│   ├── schemas
│   ├── partition-specs
│   ├── properties
│   └── snapshots
├── snapshots/
│   ├── snap-001.avro
│   ├── snap-002.avro
│   └── ...
└── data/
    ├── year=2024/
    │   └── month=01/
    │       └── day=01/
    │           └── file.parquet
    └── ...
```

### Snapshots

| Component | Description |
|-----------|-------------|
| **Snapshot** | Point-in-time view of the table |
| **Manifest List** | List of manifest files for a snapshot |
| **Manifest File** | List of data files with partition values |
| **Data File** | Actual Parquet/ORC/Avro data files |

## Quick Start

### Create Table

```java
// Using Java API
TableIdentifier identifier = TableIdentifier.of("default", "my_table");
Schema schema = new Schema(
    Types.NestedField.required(1, "id", Types.LongType.get()),
    Types.NestedField.required(2, "name", Types.StringType.get()),
    Types.NestedField.optional(3, "age", Types.IntegerType.get())
);

Table table = catalog.createTable(identifier, schema)
    .partitionSpec(PartitionSpec.builderFor(schema).day("id").build())
    .create();
```

### Using Spark SQL

```sql
-- Create table
CREATE TABLE default.my_table (
    id BIGINT,
    name STRING,
    age INT
) USING iceberg
PARTITIONED BY (days(id));

-- Insert data
INSERT INTO default.my_table VALUES (1, 'Alice', 30);
INSERT INTO default.my_table VALUES (2, 'Bob', 25);

-- Query data
SELECT * FROM default.my_table WHERE age > 20;

-- Time travel
SELECT * FROM default.my_table AS OF TIMESTAMP '2024-01-01 00:00:00';
SELECT * FROM default.my_table VERSION AS OF 1234567890;
```

## Schema Evolution

```sql
-- Add column
ALTER TABLE default.my_table ADD COLUMN email STRING;

-- Rename column
ALTER TABLE default.my_table RENAME COLUMN name TO full_name;

-- Drop column
ALTER TABLE default.my_table DROP COLUMN age;

-- Update column type (widening only)
ALTER TABLE default.my_table ALTER COLUMN age TYPE BIGINT;
```

### Schema Evolution Rules

| Operation | Allowed |
|-----------|---------|
| Add column | Yes |
| Drop column | Yes |
| Rename column | Yes |
| Widen type (INT → BIGINT) | Yes |
| Narrow type (BIGINT → INT) | No |
| Reorder columns | Yes |

## Hidden Partitioning

```sql
-- Define partitioning
CREATE TABLE default.events (
    id BIGINT,
    event_time TIMESTAMP,
    user_id BIGINT,
    event_type STRING
) USING iceberg
PARTITIONED BY (days(event_time), bucket(16, user_id));

-- Queries automatically use partition pruning
SELECT * FROM default.events 
WHERE event_time BETWEEN '2024-01-01' AND '2024-01-31';

-- No need to specify partition columns
SELECT * FROM default.events WHERE user_id = 123;
```

### Partition Transforms

| Transform | Description | Example |
|-----------|-------------|---------|
| `years(col)` | Partition by year | `PARTITIONED BY (years(event_time))` |
| `months(col)` | Partition by month | `PARTITIONED BY (months(event_time))` |
| `days(col)` | Partition by day | `PARTITIONED BY (days(event_time))` |
| `hours(col)` | Partition by hour | `PARTITIONED BY (hours(event_time))` |
| `bucket(N, col)` | Hash into N buckets | `PARTITIONED BY (bucket(16, user_id))` |
| `truncate(N, col)` | Truncate to N chars | `PARTITIONED BY (truncate(2, name))` |

## Time Travel

```sql
-- Query by timestamp
SELECT * FROM default.events 
AS OF TIMESTAMP '2024-01-01 12:00:00';

-- Query by snapshot ID
SELECT * FROM default.events 
VERSION AS OF 1234567890;

-- Query previous snapshot
SELECT * FROM default.events 
VERSION AS OF (SELECT snapshot_id - 1 FROM default.events.history LIMIT 1);

-- Rollback to snapshot
ALTER TABLE default.events 
ROLLBACK TO SNAPSHOT 1234567890;

-- Expire old snapshots
CALL default.system.expire_snapshots(
    table => 'events',
    older_than => TIMESTAMP '2024-01-15 00:00:00',
    retain_last => 10
);
```

## DML Operations

### MERGE (Upsert)

```sql
MERGE INTO default.events AS target
USING default.updates AS source
ON target.id = source.id
WHEN MATCHED AND source.operation = 'update' THEN
    UPDATE SET 
        target.name = source.name,
        target.age = source.age
WHEN MATCHED AND source.operation = 'delete' THEN
    DELETE
WHEN NOT MATCHED THEN
    INSERT (id, name, age)
    VALUES (source.id, source.name, source.age);
```

### INSERT OVERWRITE

```sql
-- Overwrite partition
INSERT OVERWRITE default.events
SELECT * FROM default.events_staging
WHERE dt = '2024-01-01';
```

### DELETE

```sql
-- Delete specific rows
DELETE FROM default.events WHERE id = 123;

-- Delete partition
DELETE FROM default.events WHERE dt = '2024-01-01';
```

## Compaction

```sql
-- Compact small files
CALL default.system.rewrite_data_files(
    table => 'events',
    options => map('min-input-files', '5')
);

-- Rewrite manifest files
CALL default.system.rewrite_manifests(table => 'events');

-- Sort data files
CALL default.system.rewrite_data_files(
    table => 'events',
    sort_order => 'event_time ASC, user_id ASC'
);
```

## Catalog Integration

### Hive Catalog

```properties
# hive-site.xml
hive.metastore.uris=thrift://localhost:9083
```

### Glue Catalog

```properties
catalog_impl=org.apache.iceberg.aws.glue.GlueCatalog
warehouse=s3://my-bucket/iceberg/warehouse
glue_catalog.region=us-east-1
```

### REST Catalog

```properties
catalog_impl=org.apache.iceberg.rest.RESTCatalog
uri=http://localhost:8181
```

## Best Practices

1. **Partition wisely** - Use time-based partitioning for time-series data
2. **Compact regularly** - Rewrite small files to improve query performance
3. **Manage snapshots** - Expire old snapshots to control metadata size
4. **Use equality deletes** - For upsert workloads, prefer equality deletes
5. **Monitor file counts** - Track data file counts per partition
6. **Use appropriate formats** - Parquet for analytics, Avro for streaming

## Key Takeaways

- Iceberg provides ACID transactions on data lakes
- Hidden partitioning simplifies query writing
- Schema evolution allows safe schema changes
- Time travel enables point-in-time queries
- Snapshot management controls metadata growth
- Compaction improves query performance by reducing file count
