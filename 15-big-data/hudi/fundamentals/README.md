# Apache Hudi Fundamentals

## Overview
Hudi is a data lake platform that provides incremental data processing with upserts, incremental pulls, and time travel.

## Key Features
- Upserts (insert + update)
- Incremental data processing
- Record-level operations (CRUD)
- Secondary indexing
- Concurrency control
- Snapshot isolation

## Table Types
- **Copy-on-Write (CoW)**: Rewrites data files on update
- **Merge-on-Read (MoR)**: Uses log files for updates

## Creating Tables
```scala
// Spark
val hudiOptions = Map(
  "hoodie.table.name" -> "events",
  "hoodie.datasource.write.recordkey.field" -> "id",
  "hoodie.datasource.write.precombine.field" -> "timestamp",
  "hoodie.datasource.write.table.type" -> "COPY_ON_WRITE"
)

df.write.format("hudi").options(hudiOptions).mode("append").save("/hudi/events")
```

## Upsert Operations
```scala
// Upsert
df.write.format("hudi").options(hudiOptions).mode("append").save("/hudi/events")

// Delete
val deleteDF = spark.read.json("/delete-ids.json")
deleteDF.write.format("hudi").options(deleteOptions).mode("append").save("/hudi/events")

// Incremental read
val incDF = spark.read.format("hudi")
  .option("read.streaming.enabled", "true")
  .option("read.streaming.start.Offset", "earliest")
  .load("/hudi/events")
```

## Best Practices
1. Use CoW for read-heavy workloads
2. Use MoR for write-heavy workloads
3. Configure record keys carefully
4. Monitor file size and count
5. Use clustering for performance
