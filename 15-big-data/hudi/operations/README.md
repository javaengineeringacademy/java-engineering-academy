# Hudi Operations

## CRUD Operations
```scala
// Insert
df.write.format("hudi").options(writeOptions).mode("append").save(path)

// Upsert (insert or update based on record key)
df.write.format("hudi").options(upsertOptions).mode("append").save(path)

// Delete
val deleteDF = spark.read.json("/delete-ids.json")
deleteDF.write.format("hudi").options(deleteOptions).mode("append").save(path)
```

## Incremental Processing
```scala
// Get incremental commits
val commits = spark.read.format("hudi")
  .option("hoodie.datasource.read.begin.instanttime", "20240101000000")
  .load(path)

// Streaming
val streamingDF = spark.readStream.format("hudi")
  .option("hoodie.datasource.read.begin.instanttime", "20240101000000")
  .load(path)
```

## Compaction
```scala
// Schedule compaction (MoR tables)
hoodieCompactor.scheduleCompaction(spark, metaClient, Option.empty())

// Run compaction
hoodieCompactor.runCompaction(spark, metaClient, compactionInstantTime)
```

## Clustering
```scala
// Schedule clustering
val scheduleClustering = client.scheduleClustering().commit()

// Run clustering
client.compact(scheduleClustering.getLeft())
```

## Table Services
```python
# Clean old commits
spark._jvm.org.apache.hudi.cli.HoodieCLI.main(
    "cleans", "--spark-master", "local[*]", path)

# Archive old commits
spark._jvm.org.apache.hudi.cli.HoodieCLI.main(
    "archive", "--spark-master", "local[*]", path)
```

## Best Practices
1. Schedule regular compaction for MoR tables
2. Use clustering for write-optimized storage
3. Configure commit intervals based on SLA
4. Monitor pending compactions
5. Clean old data based on retention policy
