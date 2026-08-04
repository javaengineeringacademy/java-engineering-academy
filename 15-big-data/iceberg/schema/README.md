# Iceberg Schema Design

## Schema Evolution
```java
// Add column
table.updateSchema().addColumn("email", Types.StringType.get()).commit();

// Rename column
table.updateSchema().renameColumn("name", "full_name").commit();

// Change column type (safe widening)
table.updateSchema().updateColumn("age", Types.IntegerType.get()).commit();

// Delete column
table.updateSchema().deleteColumn("temp_field").commit();
```

## Partitioning
```java
// Identity partitioning
PartitionSpec.builderFor(schema).identity("country").build();

// Time partitioning
PartitionSpec.builderFor(schema)
    .year("created_at")
    .month("created_at")
    .day("created_at")
    .build();

// Bucket partitioning
PartitionSpec.builderFor(schema).bucket("user_id", 16).build();

// Truncate partitioning
PartitionSpec.builderFor(schema).truncate("name", 10).build();
```

## Hidden Partitioning
```java
// Users query without knowing partitions
// SELECT * FROM users WHERE created_at > '2024-01-01'
// Iceberg automatically prunes partitions

// Partition evolution (no data rewrite)
table.updateSpec()
    .addField("created_at", Transform.day())
    .commit();
```

## Data Types
```
Primitive: boolean, int, long, float, double, decimal, date, time, timestamp, string, uuid, binary
Nested: struct, list, map
```

## Best Practices
1. Design schemas for query patterns
2. Use appropriate partition transforms
3. Enable schema evolution for flexibility
4. Monitor file count and size
5. Use column statistics for optimization
