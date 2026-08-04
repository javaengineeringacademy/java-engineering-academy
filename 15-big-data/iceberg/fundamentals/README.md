# Apache Iceberg Fundamentals

## Overview
Iceberg is an open table format for large analytic datasets with ACID transactions, schema evolution, and hidden partitioning.

## Key Features
- ACID transactions on data lakes
- Schema evolution without rewriting data
- Hidden partitioning
- Time travel queries
- Snapshot isolation
- Partition evolution

## Table Structure
```
table/
  metadata/
    v1.metadata.json      # Table metadata
    v2.metadata.json      # Updated metadata
    snap-001.avro          # Snapshot manifest
  data/
    year=2024/
      month=01/
        day=15/
          part-00000.parquet
```

## Creating Tables
```java
Table table = catalog.createTable(
    TableIdentifier.of("db", "users"),
    Schema.of(
        Types.NestedField.required(1, "id", Types.LongType.get()),
        Types.NestedField.required(2, "name", Types.StringType.get()),
        Types.NestedField.required(3, "created_at", Types.TimestampType.withoutZone())
    ),
    PartitionSpec.builderFor(schema)
        .day("created_at")
        .build()
);
```

## Best Practices
1. Use partition transforms for time-series data
2. Enable column-level statistics
3. Configure compaction for small files
4. Set appropriate snapshot retention
5. Use time travel for debugging and auditing
