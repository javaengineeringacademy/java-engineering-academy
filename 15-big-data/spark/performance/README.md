# Spark Performance Optimization

Spark performance optimization involves tuning various aspects of the Spark application to achieve better execution times, resource utilization, and cost efficiency. This guide covers shuffling, partitioning, caching, broadcast variables, and other optimization techniques.

## Table of Contents

1. [Performance Overview](#performance-overview)
2. [Shuffling](#shuffling)
3. [Partitioning](#partitioning)
4. [Caching and Persistence](#caching-and-persistence)
5. [Broadcast Variables](#broadcast-variables)
6. [Memory Management](#memory-management)
7. [Data Skew](#data-skew)
8. [Query Optimization](#query-optimization)
9. [Resource Configuration](#resource-configuration)
10. [Best Practices](#best-practices)

---

## Performance Overview

### Performance Metrics

```
Key Performance Metrics:
┌─────────────────────────────────────────────────────────────┐
│                      Execution Time                          │
│         - Job duration                                       │
│         - Stage duration                                     │
│         - Task duration                                      │
├─────────────────────────────────────────────────────────────┤
│                      Resource Utilization                     │
│         - CPU utilization                                    │
│         - Memory usage                                       │
│         - Disk I/O                                           │
├─────────────────────────────────────────────────────────────┤
│                      Shuffle Metrics                          │
│         - Shuffle read/write                                 │
│         - Shuffle spill                                      │
│         - Network transfer                                   │
└─────────────────────────────────────────────────────────────┘
```

### Performance Bottlenecks

| Bottleneck | Impact | Solution |
|------------|--------|----------|
| **Shuffling** | High network I/O | Minimize shuffles |
| **Data Skew** | Uneven task execution | Salting, repartition |
| **Memory Pressure** | GC overhead, spills | Tune memory settings |
| **Small Files** | High metadata overhead | Compact files |
| **Inefficient Code** | Slow transformations | Optimize logic |

---

## Shuffling

### What is Shuffling?

Shuffling is the process of redistributing data across partitions, typically during wide transformations like `reduceByKey`, `join`, and `groupByKey`.

### Shuffle Operations

```python
# Wide transformations that cause shuffles
# reduceByKey
rdd.reduceByKey(lambda a, b: a + b)

# groupByKey
rdd.groupByKey()

# join
rdd1.join(rdd2)

# repartition
rdd.repartition(100)

# sortByKey
rdd.sortByKey()
```

### Shuffle Optimization

```python
# 1. Use reduceByKey instead of groupByKey
# Bad
rdd.groupByKey().mapValues(sum)

# Good
rdd.reduceByKey(lambda a, b: a + b)

# 2. Use broadcast joins for small tables
from pyspark.sql.functions import broadcast

result = df1.join(broadcast(df2), "id")

# 3. Use partitionBy for output
df.write.partitionBy("date").parquet("output_path")

# 4. Control shuffle partitions
spark.conf.set("spark.sql.shuffle.partitions", "200")
```

### Shuffle Configuration

```python
# Shuffle buffer size
spark.conf.set("spark.shuffle.file.buffer", "64k")

# Shuffle block size
spark.conf.set("spark.sql.shuffle.partitions", "200")

# Shuffle compression
spark.conf.set("spark.shuffle.compress", "true")

# Shuffle spill compression
spark.conf.set("spark.shuffle.spill.compress", "true")

# Max shuffle file size
spark.conf.set("spark.sql.files.maxPartitionBytes", "134217728")
```

### Shuffle Monitoring

```python
# Monitor shuffle metrics
# In Spark UI:
# - Shuffle Read: Data read during shuffle
# - Shuffle Write: Data written during shuffle
# - Shuffle Spill: Data spilled to disk

# Access shuffle metrics programmatically
from pyspark import SparkContext

def get_shuffle_metrics(sc):
    # Get shuffle read/write metrics
    status_tracker = sc.statusTracker()
    # Process metrics
    return metrics
```

---

## Partitioning

### Partition Strategies

```python
# 1. Default partitioning
rdd = sc.parallelize(data)
print(f"Default partitions: {rdd.getNumPartitions()}")

# 2. Repartition (full shuffle)
rdd = rdd.repartition(100)

# 3. Coalesce (reduce without full shuffle)
rdd = rdd.coalesce(10)

# 4. Custom partitioning
from pyspark import Partitioner

class CustomPartitioner(Partitioner):
    def __init__(self, numPartitions):
        self.numPartitions = numPartitions
    
    def getPartition(self, key):
        return hash(key) % self.numPartitions

partitioned_rdd = rdd.partitionBy(
    numPartitions=10,
    partitionFunc=lambda key: hash(key) % 10
)
```

### Partition Optimization

```python
# 1. Partition by key
rdd = rdd.partitionBy(100)

# 2. Partition by column (DataFrame)
df = df.repartition("department")

# 3. Partition by multiple columns
df = df.repartition("department", "year")

# 4. Check partition count
print(f"Partitions: {df.rdd.getNumPartitions()}")

# 5. Check partition structure
print(df.rdd.glom().collect())
```

### Partition Tuning

```python
# 1. Optimal partition size (128MB - 256MB)
# Calculate optimal partitions
total_size = 10 * 1024 * 1024 * 1024  # 10GB
optimal_partitions = total_size / (128 * 1024 * 1024)  # ~80 partitions

# 2. Partition for parallelism
spark.conf.set("spark.sql.shuffle.partitions", "200")

# 3. Partition for data locality
df = df.repartition("join_key")

# 4. Partition for output
df.write.partitionBy("date", "hour").parquet("output_path")
```

### Bucketing

```python
# Bucketing for join optimization
df1.write \
    .bucketBy(100, "id") \
    .sortBy("id") \
    .saveAsTable("bucketed_table1")

df2.write \
    .bucketBy(100, "id") \
    .sortBy("id") \
    .saveAsTable("bucketed_table2")

# Join bucketed tables
result = spark.table("bucketed_table1").join(
    spark.table("bucketed_table2"), "id"
)
```

---

## Caching and Persistence

### Cache Levels

```python
from pyspark import StorageLevel

# Memory only
df.cache()
df.persist(StorageLevel.MEMORY_ONLY)

# Memory and disk
df.persist(StorageLevel.MEMORY_AND_DISK)

# Disk only
df.persist(StorageLevel.DISK_ONLY)

# Off-heap memory
df.persist(StorageLevel.OFF_HEAP)

# With replication
df.persist(StorageLevel.MEMORY_ONLY_2)
df.persist(StorageLevel.MEMORY_AND_DISK_2)
```

### Cache Management

```python
# Check if cached
print(f"Is cached: {df.is_cached}")

# Get storage level
print(f"Storage level: {df.storageLevel}")

# Unpersist
df.unpersist()

# Cache strategy
# Cache before multiple operations
df.cache()
df.filter(df.age > 25).count()
df.groupBy("department").count()
df.unpersist()
```

### Cache Optimization

```python
# 1. Cache frequently used data
frequently_used_df = spark.read.parquet("large_dataset")
frequently_used_df.cache()

# 2. Use appropriate storage level
large_df.persist(StorageLevel.MEMORY_AND_DISK)

# 3. Cache intermediate results
intermediate_df = raw_df.filter(process_data).transform(clean_data)
intermediate_df.cache()

# 4. Unpersist when done
final_result = intermediate_df.collect()
intermediate_df.unpersist()
```

### Cache Monitoring

```python
# Monitor cache usage
# In Spark UI:
# - Storage tab shows cached DataFrames
# - Memory usage per partition
# - Disk spill if any

# Access cache metrics
from pyspark import SparkContext

def get_cache_metrics(sc):
    # Get cache metrics
    status_tracker = sc.statusTracker()
    # Process metrics
    return metrics
```

---

## Broadcast Variables

### Broadcast Overview

Broadcast variables allow efficient sharing of read-only data across all nodes in a cluster.

### Creating Broadcast Variables

```python
# Create broadcast variable
lookup_table = {"A": 1, "B": 2, "C": 3}
broadcast_lookup = sc.broadcast(lookup_table)

# Use in transformations
result = rdd.map(lambda x: broadcast_lookup.value.get(x, 0))

# Access broadcast value
print(broadcast_lookup.value)

# Unpersist broadcast variable
broadcast_lookup.unpersist()
```

### Broadcast Joins

```python
from pyspark.sql.functions import broadcast

# Broadcast small DataFrame for join
result = df1.join(broadcast(df2), "id")

# Configure broadcast threshold
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "10m")

# Manual broadcast
broadcast_df = broadcast(df2)
result = df1.join(broadcast_df, "id")
```

### Broadcast Optimization

```python
# 1. Use broadcast for small datasets
small_df = spark.read.parquet("small_dataset")
broadcast_df = broadcast(small_df)

# 2. Configure broadcast threshold
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "10m")

# 3. Monitor broadcast metrics
# In Spark UI:
# - Broadcast exchanged
# - Broadcast time

# 4. Use broadcast for joins
result = large_df.join(broadcast(small_df), "id")
```

---

## Memory Management

### Memory Configuration

```python
# Executor memory
spark.conf.set("spark.executor.memory", "8g")

# Driver memory
spark.conf.set("spark.driver.memory", "4g")

# Memory overhead
spark.conf.set("spark.executor.memoryOverhead", "2g")

# Off-heap memory
spark.conf.set("spark.memory.offHeap.enabled", "true")
spark.conf.set("spark.memory.offHeap.size", "2g")

# Memory fractions
spark.conf.set("spark.memory.fraction", "0.6")
spark.conf.set("spark.memory.storageFraction", "0.5")
```

### Memory Tuning

```python
# 1. Avoid memory spills
# Increase executor memory
spark.conf.set("spark.executor.memory", "16g")

# 2. Reduce memory pressure
# Cache less data
df.unpersist()

# Use appropriate storage level
df.persist(StorageLevel.MEMORY_AND_DISK)

# 3. Optimize data structures
# Use primitive types
# Avoid unnecessary objects

# 4. Monitor memory usage
# In Spark UI:
# - Storage tab shows memory usage
# - Task tab shows memory per task
```

### Garbage Collection Tuning

```python
# Use G1GC for large heaps
spark.conf.set("spark.executor.extraJavaOptions", 
    "-XX:+UseG1GC -XX:MaxGCPauseMillis=200")

# Monitor GC
spark.conf.set("spark.executor.extraJavaOptions", 
    "-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps")

# Tune GC thresholds
spark.conf.set("spark.executor.extraJavaOptions", 
    "-XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=16m")
```

---

## Data Skew

### Identifying Data Skew

```python
# Check partition sizes
partition_sizes = df.rdd.mapPartitions(lambda x: [sum(1 for _ in x)]).collect()

# Check key distribution
key_counts = df.groupBy("key").count().orderBy("count", ascending=False)
key_counts.show()

# Check task duration
# In Spark UI:
# - Task tab shows task duration
# - Look for tasks with significantly longer duration
```

### Handling Data Skew

```python
# 1. Salting technique
from pyspark.sql.functions import rand, floor

# Add salt column
salt_range = 10
df_salted = df.withColumn(
    "salt",
    (floor(rand() * salt_range)).cast("integer")
)

# Repartition by salt
df_salted = df_salted.repartition("key", "salt")

# 2. Broadcast join for skewed tables
result = df_skewed.join(broadcast(df_small), "key")

# 3. Repartition by key
df = df.repartition("key")

# 4. Use AQE (Adaptive Query Execution)
spark.conf.set("spark.sql.adaptive.enabled", "true")
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", "true")
```

### Skew Optimization

```python
# 1. Use salting for joins
# Add salt to skewed table
df_skewed_salted = df_skewed.withColumn(
    "salt",
    (floor(rand() * salt_range)).cast("integer")
)

# Replicate small table with salt
df_small_salted = df_small.crossJoin(
    spark.range(salt_range).withColumnRenamed("id", "salt")
)

# Join on key and salt
result = df_skewed_salted.join(df_small_salted, ["key", "salt"])

# 2. Use AQE
spark.conf.set("spark.sql.adaptive.enabled", "true")
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", "true")
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", "true")

# 3. Pre-aggregate
pre_aggregated = df.groupBy("key").agg(sum("value").alias("total"))
result = pre_aggregated.join(small_df, "key")
```

---

## Query Optimization

### Catalyst Optimizer

```python
# View logical plan
df.explain(True)

# View optimized plan
df.filter(df.age > 25).select("name").explain()

# View physical plan
df.filter(df.age > 25).select("name").explain("extended")

# View code generation
df.filter(df.age > 25).select("name").explain("codegen")
```

### Query Optimization Techniques

```python
# 1. Predicate pushdown
# Push filters to data source
df.filter(df.age > 25).select("name", "age")

# 2. Projection pruning
# Read only required columns
df.select("name", "age")

# 3. Constant folding
# Evaluate constants at compile time
df.withColumn("result", lit(2) + lit(3))

# 4. Join optimization
# Use broadcast joins for small tables
result = df1.join(broadcast(df2), "id")

# 5. Partition pruning
# Read only relevant partitions
df.filter(df.date == "2024-01-01")
```

### AQE (Adaptive Query Execution)

```python
# Enable AQE
spark.conf.set("spark.sql.adaptive.enabled", "true")

# AQE features
spark.conf.set("spark.sql.adaptive.coalescePartitions.enabled", "true")
spark.conf.set("spark.sql.adaptive.skewJoin.enabled", "true")
spark.conf.set("spark.sql.adaptive.join.enabled", "true")
spark.conf.set("spark.sql.adaptive.advisoryPartitionSizeInMB", "64")

# AQE optimizes:
# - Partition coalescing
# - Skew join handling
# - Join strategy selection
# - Dynamic partition pruning
```

---

## Resource Configuration

### Executor Configuration

```python
# Executor memory
spark.conf.set("spark.executor.memory", "8g")

# Executor cores
spark.conf.set("spark.executor.cores", "4")

# Number of executors
spark.conf.set("spark.executor.instances", "10")

# Executor memory overhead
spark.conf.set("spark.executor.memoryOverhead", "2g")

# Executor JVM options
spark.conf.set("spark.executor.extraJavaOptions", 
    "-XX:+UseG1GC -XX:MaxGCPauseMillis=200")
```

### Driver Configuration

```python
# Driver memory
spark.conf.set("spark.driver.memory", "4g")

# Driver cores
spark.conf.set("spark.driver.cores", "2")

# Driver memory overhead
spark.conf.set("spark.driver.memoryOverhead", "1g")

# Driver JVM options
spark.conf.set("spark.driver.extraJavaOptions", 
    "-XX:+UseG1GC -XX:MaxGCPauseMillis=200")
```

### Cluster Configuration

```python
# Dynamic allocation
spark.conf.set("spark.dynamicAllocation.enabled", "true")
spark.conf.set("spark.dynamicAllocation.minExecutors", "2")
spark.conf.set("spark.dynamicAllocation.maxExecutors", "20")
spark.conf.set("spark.dynamicAllocation.executorIdleTimeout", "60s")

# Shuffle service
spark.conf.set("spark.shuffle.service.enabled", "true")

# yarn queue
spark.conf.set("spark.yarn.queue", "production")
```

### Resource Tuning

```python
# 1. Optimal executor configuration
# Executor memory: 4-8GB
# Executor cores: 4-5
# Memory overhead: 10-15% of executor memory

# 2. Parallelism
spark.conf.set("spark.sql.shuffle.partitions", "200")
spark.conf.set("spark.default.parallelism", "200")

# 3. Serialization
spark.conf.set("spark.serializer", 
    "org.apache.spark.serializer.KryoSerializer")
spark.conf.set("spark.kryoserializer.buffer.max", "512m")

# 4. Compression
spark.conf.set("spark.io.compression.codec", "snappy")
spark.conf.set("spark.shuffle.compress", "true")
```

---

## Best Practices

### 1. Minimize Shuffles

```python
# Use reduceByKey instead of groupByKey
rdd.reduceByKey(lambda a, b: a + b)

# Use broadcast joins for small tables
result = df1.join(broadcast(df2), "id")

# Use partitionBy for output
df.write.partitionBy("date").parquet("output_path")

# Avoid unnecessary repartition
df = df.repartition(100)  # Only if needed
```

### 2. Cache Strategically

```python
# Cache frequently used data
frequently_used_df.cache()

# Use appropriate storage level
large_df.persist(StorageLevel.MEMORY_AND_DISK)

# Unpersist when done
final_result = df.collect()
df.unpersist()
```

### 3. Optimize Data Structures

```python
# Use primitive types
df = df.withColumn("id", col("id").cast("integer"))

# Avoid unnecessary columns
df = df.select("col1", "col2", "col3")

# Use appropriate file formats
df.write.parquet("output_path")

# Partition data appropriately
df.write.partitionBy("date", "hour").parquet("output_path")
```

### 4. Monitor Performance

```python
# Check Spark UI
# - Jobs tab: Job duration and status
# - Stages tab: Stage duration and shuffle metrics
# - Tasks tab: Task duration and resource usage
# - Storage tab: Cached DataFrames
# - Executors tab: Executor memory and CPU usage

# Access metrics programmatically
from pyspark import SparkContext

def get_performance_metrics(sc):
    # Get job metrics
    status_tracker = sc.statusTracker()
    # Process metrics
    return metrics
```

### 5. Use Appropriate APIs

```python
# Use DataFrame API over RDD
df.filter(df.age > 25).select("name")

# Use built-in functions
from pyspark.sql.functions import col, when, avg

# Use SQL for complex queries
spark.sql("SELECT department, AVG(salary) FROM employees GROUP BY department")

# Use broadcast for small datasets
result = df1.join(broadcast(df2), "id")
```

---

## Common Patterns

### Pattern 1: ETL Optimization

```python
# Optimize ETL pipeline
# 1. Read only required columns
df = spark.read.parquet("input_path").select("col1", "col2", "col3")

# 2. Filter early
df = df.filter(df.date >= "2024-01-01")

# 3. Cache intermediate results
df.cache()

# 4. Write with partitioning
df.write.partitionBy("date").parquet("output_path")

# 5. Unpersist when done
df.unpersist()
```

### Pattern 2: Join Optimization

```python
# Optimize joins
# 1. Use broadcast for small tables
result = df1.join(broadcast(df2), "id")

# 2. Repartition by join key
df1 = df1.repartition("id")
df2 = df2.repartition("id")

# 3. Use bucketed tables
df1.write.bucketBy(100, "id").saveAsTable("table1")
df2.write.bucketBy(100, "id").saveAsTable("table2")

# 4. Use AQE
spark.conf.set("spark.sql.adaptive.enabled", "true")
```

### Pattern 3: Aggregation Optimization

```python
# Optimize aggregations
# 1. Use reduceByKey for RDD
rdd.reduceByKey(lambda a, b: a + b)

# 2. Use DataFrame API
df.groupBy("key").agg(sum("value"))

# 3. Pre-aggregate
pre_aggregated = df.groupBy("key").agg(sum("value").alias("total"))

# 4. Use window functions
from pyspark.sql import Window
from pyspark.sql.functions import sum

windowSpec = Window.partitionBy("key").orderBy("date")
df.withColumn("running_total", sum("value").over(windowSpec))
```

### Pattern 4: Iterative Algorithm Optimization

```python
# Optimize iterative algorithms
# 1. Cache input data
input_data.cache()

# 2. Use checkpointing
input_data.checkpoint()

# 3. Broadcast variables for small data
broadcast_data = sc.broadcast(small_data)

# 4. Monitor convergence
for i in range(max_iterations):
    result = compute_iteration(result)
    
    # Check convergence
    if converged(result, previous_result):
        break
    
    previous_result = result
```

### Pattern 5: Memory Optimization

```python
# Optimize memory usage
# 1. Use appropriate data types
df = df.withColumn("id", col("id").cast("integer"))

# 2. Cache with appropriate storage level
df.persist(StorageLevel.MEMORY_AND_DISK)

# 3. Avoid unnecessary objects
df = df.select("col1", "col2")

# 4. Monitor memory usage
# In Spark UI: Storage tab

# 5. Tune memory configuration
spark.conf.set("spark.executor.memory", "8g")
spark.conf.set("spark.memory.fraction", "0.6")
```

---

## Conclusion

Spark performance optimization involves:

- **Minimizing shuffles** through appropriate transformations
- **Optimizing partitioning** for parallelism and data locality
- **Strategic caching** for frequently used data
- **Broadcast variables** for small dataset distribution
- **Memory management** for efficient resource utilization
- **Data skew handling** for balanced execution
- **Query optimization** through Catalyst and AQE

Key takeaways:

1. **Profile first** - Identify bottlenecks before optimizing
2. **Minimize shuffles** - Use reduceByKey, broadcast joins
3. **Cache strategically** - Cache frequently used data
4. **Optimize partitions** - Right-size for your workload
5. **Monitor performance** - Use Spark UI and metrics

Performance optimization is an iterative process that requires understanding your specific workload and tuning accordingly.