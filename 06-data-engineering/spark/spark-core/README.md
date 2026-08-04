# Spark Core - RDDs and Low-Level API

## Table of Contents

- [Overview](#overview)
- [RDD Fundamentals](#rdd-fundamentals)
- [Creating RDDs](#creating-rdds)
- [Transformations](#transformations)
- [Actions](#actions)
- [RDD Persistence](#rdd-persistence)
- [Shared Variables](#shared-variables)
- [Partitioning](#partitioning)
- [Pair RDD Operations](#pair-rdd-operations)
- [Accumulators and Broadcast Variables](#accumulators-and-broadcast-variables)
- [Performance Optimization](#performance-optimization)
- [Examples](#examples)
- [References](#references)

---

## Overview

Spark Core is the foundation of Apache Spark providing the basic functionality
including RDD (Resilient Distributed Dataset) abstraction, task scheduling,
memory management, fault recovery, and interacting with storage systems.

### Key Concepts

- **RDD**: Immutable, distributed collection of objects
- **Transformation**: Lazy operation that produces a new RDD
- **Action**: Operation that triggers computation and returns results
- **Lineage**: Graph of transformations used for fault recovery
- **Partition**: Logical chunk of data processed by a task

### When to Use Core API

- Custom partitioning logic
- Low-level performance optimization
- Complex data transformations
- Integration with external systems
- When DataFrame API is insufficient

---

## RDD Fundamentals

### RDD Properties

```python
# RDD is immutable
rdd1 = sc.parallelize([1, 2, 3])
rdd2 = rdd1.map(lambda x: x * 2)  # New RDD, rdd1 unchanged

# RDD is distributed
# Data is split across multiple partitions on different nodes

# RDD is lazy
# Transformations are not executed until an action is called

# RDD is fault tolerant
# Can be recomputed from lineage if a partition is lost
```

### RDD Lineage

```python
# Check RDD lineage
print(rdd2.toDebugString().decode())

# Output shows:
# (2) PythonRDD[2] at parallelize at <stdin>:2 []
#  |   ParallelCollectionRDD[0] at parallelize at <stdin>:1 []
```

---

## Creating RDDs

### From Python Collections

```python
# From list
data = [1, 2, 3, 4, 5]
rdd = sc.parallelize(data)

# With partition count
rdd = sc.parallelize(data, numSlices=4)

# From range
rdd = sc.parallelize(range(1000))

# From tuple
data = [(1, 'a'), (2, 'b'), (3, 'c')]
rdd = sc.parallelize(data)
```

### From External Sources

```python
# From text file (returns RDD of lines)
rdd = sc.textFile("hdfs://path/to/file.txt")

# From multiple files
rdd = sc.textFile("hdfs://path/to/file1.txt,hdfs://path/to/file2.txt")

# From directory
rdd = sc.textFile("hdfs://path/to/directory/")

# From entire directory with pattern
rdd = sc.wholeTextFiles("hdfs://path/to/directory/*.txt")

# From CSV
rdd = sc.textFile("hdfs://path/to/data.csv")
parsed = rdd.map(lambda line: line.split(","))

# From JSON
import json
rdd = sc.textFile("hdfs://path/to/data.json")
parsed = rdd.map(lambda line: json.loads(line))

# From sequence file
rdd = sc.sequenceFile("hdfs://path/to/seqfile")

# From object file
rdd = sc.objectFile("hdfs://path/to/objfile")
```

### From Other RDDs

```python
# Filter creates new RDD
filtered_rdd = rdd.filter(lambda x: x > 3)

# Map creates new RDD
mapped_rdd = rdd.map(lambda x: (x, x * 2))

# Union of RDDs
combined_rdd = rdd1.union(rdd2)

# Intersection
common_rdd = rdd1.intersection(rdd2)

# Distinct
unique_rdd = rdd.distinct()
```

---

## Transformations

### Narrow Transformations

Each input partition contributes to at most one output partition.

```python
# map - Apply function to each element
rdd.map(lambda x: x * 2)

# filter - Select elements matching predicate
rdd.filter(lambda x: x > 5)

# flatMap - Map and flatten
rdd.flatMap(lambda x: [x, x * 2, x * 3])

# mapPartitions - Apply function to each partition
def process_partition(iterator):
    for val in iterator:
        yield val * 2

rdd.mapPartitions(process_partition)

# mapPartitionsWithIndex - Include partition index
def process_with_index(idx, iterator):
    for val in iterator:
        yield (idx, val * 2)

rdd.mapPartitionsWithIndex(process_with_index)

# sample - Random sample
rdd.sample(withReplacement=False, fraction=0.1)

# distinct - Remove duplicates
rdd.distinct()

# union - Combine RDDs
rdd1.union(rdd2)

# intersection - Common elements
rdd1.intersection(rdd2)

# subtract - Elements in first but not second
rdd1.subtract(rdd2)

# cartesian - Cartesian product
rdd1.cartesian(rdd2)

# pipe - External command
rdd.pipe("grep pattern")
```

### Wide Transformations

Each input partition may contribute to multiple output partitions.

```python
# groupByKey - Group values by key
rdd.groupByKey()

# reduceByKey - Reduce values by key
rdd.reduceByKey(lambda a, b: a + b)

# sortByKey - Sort by key
rdd.sortByKey()

# join - Inner join
rdd1.join(rdd2)

# leftOuterJoin - Left outer join
rdd1.leftOuterJoin(rdd2)

# rightOuterJoin - Right outer join
rdd1.rightOuterJoin(rdd2)

# fullOuterJoin - Full outer join
rdd1.fullOuterJoin(rdd2)

# combineByKey - General aggregation
rdd.combineByKey(
    lambda x: (x, 1),                    # createCombiner
    lambda acc, x: (acc[0] + x, acc[1] + 1),  # mergeValue
    lambda acc1, acc2: (acc1[0] + acc2[0], acc1[1] + acc2[1])  # mergeCombiners
)

# aggregateByKey - Aggregate by key
rdd.aggregateByKey(0)(lambda acc, x: acc + x, lambda acc1, acc2: acc1 + acc2)

# foldByKey - Fold by key
rdd.foldByKey(0, lambda a, b: a + b)

# coalesce - Reduce partitions (no shuffle)
rdd.coalesce(numPartitions=10)

# repartition - Redistribute data (shuffle)
rdd.repartition(numPartitions=20)

# repartitionAndSortWithinPartitions
rdd.repartitionAndSortWithinPartitions(numPartitions=10)
```

---

## Actions

### Collecting Data

```python
# collect - Return all elements as list
data = rdd.collect()

# take - Return first n elements
first_10 = rdd.take(10)

# first - Return first element
first = rdd.first()

# top - Return top n elements
top_10 = rdd.top(10)

# takeOrdered - Return ordered first n
ordered = rdd.takeOrdered(10, key=lambda x: -x)

# count - Count elements
count = rdd.count()

# countApprox - Approximate count
count = rdd.countApprox(timeout=1000)

# isEmpty - Check if RDD is empty
empty = rdd.isEmpty()
```

### Saving Data

```python
# saveAsTextFile - Save as text file
rdd.saveAsTextFile("hdfs://path/to/output")

# saveAsSequenceFile - Save as sequence file
rdd.saveAsSequenceFile("hdfs://path/to/output")

# saveAsObjectFile - Save as object file
rdd.saveAsObjectFile("hdfs://path/to/output")

# saveAsNewAPIHadoopFile - Save using new Hadoop API
rdd.saveAsNewAPIHadoopFile(
    "hdfs://path/to/output",
    "org.apache.hadoop.mapreduce.lib.output.TextOutputFormat"
)
```

### Aggregation Actions

```python
# reduce - Aggregate all elements
total = rdd.reduce(lambda a, b: a + b)

# fold - Aggregate with initial value
total = rdd.fold(0, lambda a, b: a + b)

# aggregate - Aggregate with different types
result = rdd.aggregate(
    (0, 0),  # initial value
    lambda acc, x: (acc[0] + x, acc[1] + 1),  # seqOp
    lambda acc1, acc2: (acc1[0] + acc2[0], acc1[1] + acc2[1])  # combOp
)

# treeReduce - Parallel reduce
total = rdd.treeReduce(lambda a, b: a + b, depth=2)

# treeAggregate - Parallel aggregate
result = rdd.treeAggregate(
    0,
    lambda acc, x: acc + x,
    lambda acc1, acc2: acc1 + acc2,
    depth=2
)
```

### Other Actions

```python
# foreach - Apply function to each element (no return)
rdd.foreach(lambda x: print(x))

# foreachPartition - Apply function to each partition
def process_partition(iterator):
    # Process partition data
    for val in iterator:
        process(val)

rdd.foreachPartition(process_partition)

# countByKey - Count elements per key
counts = rdd.countByKey()

# zip - Zip two RDDs
zipped = rdd1.zip(rdd2)

# zipWithIndex - Add index
indexed = rdd.zipWithIndex()

# zipWithUniqueId - Add unique ID
with_id = rdd.zipWithUniqueId()

# collectAsMap - Collect as dictionary
data = rdd.collectAsMap()

# lookup - Lookup key values
values = rdd.lookup(key=5)
```

---

## RDD Persistence

### Storage Levels

```python
from pyspark import StorageLevel

# MEMORY_ONLY - Store as deserialized objects in JVM heap
# If not enough memory, some partitions will not be cached
rdd.persist(StorageLevel.MEMORY_ONLY)

# MEMORY_AND_DISK - Spill to disk if memory is insufficient
rdd.persist(StorageLevel.MEMORY_AND_DISK)

# MEMORY_ONLY_SER - Store as serialized objects
# More space efficient but slower
rdd.persist(StorageLevel.MEMORY_ONLY_SER)

# MEMORY_AND_DISK_SER - Serialized with disk spill
rdd.persist(StorageLevel.MEMORY_AND_DISK_SER)

# DISK_ONLY - Store only on disk
rdd.persist(StorageLevel.DISK_ONLY)

# MEMORY_ONLY_2, MEMORY_AND_DISK_2, etc.
# Replicate each partition on two nodes
rdd.persist(StorageLevel.MEMORY_ONLY_2)

# OFF_HEAP - Store in off-heap memory
rdd.persist(StorageLevel.OFF_HEAP)
```

### Cache and Persist

```python
# cache() is shorthand for persist(MEMORY_ONLY)
rdd.cache()

# persist with specific storage level
rdd.persist(StorageLevel.MEMORY_AND_DISK)

# unpersist to remove from cache
rdd.unpersist()

# Check if RDD is persisted
print(rdd.is_cached)
```

### Persistence Best Practices

```python
# Use MEMORY_AND_DISK for large datasets
large_rdd.persist(StorageLevel.MEMORY_AND_DISK)

# Use MEMORY_ONLY_SER for memory efficiency
serialized_rdd.persist(StorageLevel.MEMORY_ONLY_SER)

# Cache intermediate results in iterative algorithms
data = sc.textFile("hdfs://large-dataset.txt")
parsed = data.map(parse_line).filter(is_valid)
parsed.cache()  # Cache before iterative use

# Unpersist when no longer needed
parsed.unpersist()
```

---

## Shared Variables

### Broadcast Variables

```python
# Broadcast large read-only data to all executors
lookup_table = sc.broadcast({
    "US": "United States",
    "UK": "United Kingdom",
    "CA": "Canada"
})

# Use in transformations
rdd.map(lambda x: lookup_table.value.get(x, "Unknown"))

# Update broadcast variable (new variable, not in-place)
lookup_table.unpersist()
lookup_table = sc.broadcast(updated_table)
```

### Accumulators

```python
# Accumulator for aggregating values from executors
error_count = sc.accumulator(0)
line_count = sc.accumulator(0)

def process_line(line):
    global error_count, line_count
    line_count.add(1)
    if "ERROR" in line:
        error_count.add(1)
    return line

rdd.foreach(process_line)

# Read accumulator value (only on driver)
print(f"Total lines: {line_count.value}")
print(f"Errors: {error_count.value}")
```

### Custom Accumulators

```python
from pyspark.accumulators import AccumulatorParam

class VectorAccumulatorParam(AccumulatorParam):
    def zero(self, initialValue):
        return [0.0] * len(initialValue)

    def addInPlace(self, v1, v2):
        return [v1[i] + v2[i] for i in range(len(v1))]

# Usage
vec_acc = sc.accumulator(
    [0.0, 0.0, 0.0],
    VectorAccumulatorParam()
)

def add_vector(vec):
    global vec_acc
    vec_acc.addInPlace(vec)

rdd.foreach(add_vector)
```

---

## Partitioning

### Default Partitioning

```python
# Check number of partitions
print(rdd.getNumPartitions())

# Check partition sizes
print(rdd.glom().map(len).collect())

# Get partition content
print(rdd.glom().collect())
```

### Controlling Partitioning

```python
# coalesce - Reduce partitions (no shuffle)
rdd.coalesce(10)

# repartition - Increase or decrease partitions (shuffle)
rdd.repartition(20)

# repartition by range
rdd.repartitionAndSortWithinPartitions(10)

# Custom partitioner
from pyspark import Partitioner

class CustomPartitioner(Partitioner):
    def __init__(self, numPartitions):
        super().__init__(numPartitions)

    def __call__(self, key):
        # Custom partitioning logic
        return hash(key) % self.numPartitions

# Use custom partitioner
partitioned_rdd = rdd.partitionBy(10, CustomPartitioner)
```

### Hash Partitioning

```python
# Hash partitioning
rdd.partitionBy(numPartitions=10)

# With custom hash function
rdd.partitionBy(10, lambda key: hash(key) % 10)
```

### Range Partitioning

```python
# Range partitioning for ordered data
from pyspark import RangePartitioner

# Create range partitioner
partitioner = RangePartitioner(numPartitions=10, ascending=True)

# Apply range partitioning
partitioned_rdd = rdd.partitionBy(10, partitioner)
```

---

## Pair RDD Operations

### Creating Pair RDDs

```python
# From tuple list
pair_rdd = sc.parallelize([(1, 'a'), (2, 'b'), (3, 'c')])

# From regular RDD
pair_rdd = rdd.map(lambda x: (x % 3, x))

# From text file
rdd = sc.textFile("data.txt")
pair_rdd = rdd.map(lambda line: line.split(",")).map(lambda x: (x[0], x[1]))
```

### Pair RDD Transformations

```python
# reduceByKey - Reduce values for each key
reduced = pair_rdd.reduceByKey(lambda a, b: a + b)

# groupByKey - Group values by key
grouped = pair_rdd.groupByKey()

# combineByKey - General aggregation
combined = pair_rdd.combineByKey(
    lambda x: (x, 1),  # createCombiner
    lambda acc, x: (acc[0] + x, acc[1] + 1),  # mergeValue
    lambda acc1, acc2: (acc1[0] + acc2[0], acc1[1] + acc2[1])  # mergeCombiners
)

# aggregateByKey - Aggregate with initial value
aggregated = pair_rdd.aggregateByKey(0)(lambda a, b: a + b, lambda a, b: a + b)

# foldByKey - Fold with initial value
folded = pair_rdd.foldByKey(0, lambda a, b: a + b)

# sortByKey - Sort by key
sorted_rdd = pair_rdd.sortByKey()

# keys - Extract keys
keys = pair_rdd.keys()

# values - Extract values
values = pair_rdd.values()

# mapValues - Transform values
mapped_values = pair_rdd.mapValues(lambda v: v * 2)

# flatMapValues - FlatMap values
flat_mapped = pair_rdd.flatMapValues(lambda v: [v, v * 2])

# countByKey - Count per key
counts = pair_rdd.countByKey()

# lookup - Lookup key
values = pair_rdd.lookup(1)
```

### Join Operations

```python
# Inner join
joined = rdd1.join(rdd2)

# Left outer join
left_joined = rdd1.leftOuterJoin(rdd2)

# Right outer join
right_joined = rdd1.rightOuterJoin(rdd2)

# Full outer join
full_joined = rdd1.fullOuterJoin(rdd2)

# Cogroup - Group by key from multiple RDDs
cogrouped = rdd1.cogroup(rdd2)

# Subtract keys
subtracted = rdd1.subtractByKey(rdd2)
```

---

## Accumulators and Broadcast Variables

### Accumulator Patterns

```python
# Counter accumulator
counter = sc.accumulator(0)

def count_elements(x):
    global counter
    counter.add(1)
    return x

rdd.foreach(count_elements)
print(f"Count: {counter.value}")

# Sum accumulator
total = sc.accumulator(0.0)

def sum_values(x):
    global total
    total.add(x)
    return x

rdd.foreach(sum_values)
print(f"Total: {total.value}")

# Set accumulator
unique_values = sc.accumulator(set())

def collect_unique(x):
    global unique_values
    unique_values.add(x)
    return x

rdd.foreach(collect_unique)
print(f"Unique: {unique_values.value}")
```

### Broadcast Variable Patterns

```python
# Lookup table
lookup = sc.broadcast({"key1": "value1", "key2": "value2"})

def transform_with_lookup(x):
    return lookup.value.get(x, "default")

transformed = rdd.map(transform_with_lookup)

# Configuration
config = sc.broadcast({
    "threshold": 100,
    "multiplier": 2,
    "filter_pattern": ".*@example.com"
})

def process_with_config(x):
    cfg = config.value
    if x > cfg["threshold"]:
        return x * cfg["multiplier"]
    return x

# ML model broadcast
model = sc.broadcast(trained_model)

def predict(x):
    return model.value.predict(x)

predictions = rdd.map(predict)
```

---

## Performance Optimization

### Partitioning Optimization

```python
# Optimal partition count
# Rule of thumb: 2-4 partitions per CPU core
num_cores = 8
optimal_partitions = num_cores * 3

# Check current partitions
print(f"Current partitions: {rdd.getNumPartitions()}")

# Repartition if needed
if rdd.getNumPartitions() > optimal_partitions * 2:
    rdd = rdd.coalesce(optimal_partitions)
elif rdd.getNumPartitions() < optimal_partitions:
    rdd = rdd.repartition(optimal_partitions)
```

### Shuffle Optimization

```python
# Avoid shuffles when possible
# Use reduceByKey instead of groupByKey + reduce
bad = rdd.groupByKey().mapValues(lambda v: sum(v))  # Shuffle + more shuffle
good = rdd.reduceByKey(lambda a, b: a + b)  # Single shuffle

# Use map-side combine
rdd.combineByKey(
    lambda x: x,  # createCombiner
    lambda acc, x: acc + x,  # mergeValue
    lambda acc1, acc2: acc1 + acc2  # mergeCombiners
)

# Control shuffle partition count
spark.conf.set("spark.sql.shuffle.partitions", 200)
```

### Caching Strategy

```python
# Cache data used multiple times
data = sc.textFile("hdfs://large-file.txt")
parsed = data.map(parse_line).filter(is_valid)

# Cache before iterative use
parsed.cache()

# Use in multiple operations
count1 = parsed.filter(lambda x: x["type"] == "A").count()
count2 = parsed.filter(lambda x: x["type"] == "B").count()
count3 = parsed.filter(lambda x: x["type"] == "C").count()

# Unpersist when done
parsed.unpersist()
```

### Serialization

```python
# Use Kryo serialization
spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
spark.conf.set("spark.kryo.registrationRequired", False)

# Register classes for better performance
spark.conf.set("spark.kryo.classesToRegister", "com.example.MyClass")
```

---

## Examples

### Word Count

```python
# Read text file
text_rdd = sc.textFile("hdfs://path/to/text.txt")

# Split into words
words = text_rdd.flatMap(lambda line: line.split(" "))

# Create word counts
word_counts = words.map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)

# Sort by count
sorted_counts = word_counts.sortBy(lambda x: x[1], ascending=False)

# Get top 10
top_10 = sorted_counts.take(10)

# Display results
for word, count in top_10:
    print(f"{word}: {count}")
```

### Data Processing Pipeline

```python
# Load data
raw_data = sc.textFile("hdfs://data/sales.csv")

# Parse CSV
parsed = raw_data.map(lambda line: line.split(",")).filter(lambda x: len(x) >= 5)

# Extract fields
sales = parsed.map(lambda x: {
    "date": x[0],
    "product": x[1],
    "quantity": int(x[2]),
    "price": float(x[3]),
    "total": float(x[2]) * float(x[3])
})

# Filter valid records
valid_sales = sales.filter(lambda x: x["total"] > 0)

# Aggregate by product
product_totals = valid_sales.map(
    lambda x: (x["product"], x["total"])
).reduceByKey(lambda a, b: a + b)

# Sort by total
top_products = product_totals.sortBy(lambda x: x[1], ascending=False)

# Save results
top_products.saveAsTextFile("hdfs://output/top_products")
```

### Complex Aggregation

```python
# Create pair RDD
data = sc.parallelize([
    ("A", 1), ("B", 2), ("A", 3), ("B", 4), ("C", 5),
    ("A", 6), ("B", 7), ("C", 8), ("A", 9), ("B", 10)
])

# combineByKey for complex aggregation
result = data.combineByKey(
    lambda x: (x, 1),  # createCombiner
    lambda acc, x: (acc[0] + x, acc[1] + 1),  # mergeValue
    lambda acc1, acc2: (acc1[0] + acc2[0], acc1[1] + acc2[1])  # mergeCombiners
).mapValues(lambda x: x[0] / x[1])  # Calculate average

print(result.collect())
# [('A', 4.75), ('B', 5.75), ('C', 6.5)]
```

### Join Example

```python
# Users RDD
users = sc.parallelize([
    (1, "Alice"), (2, "Bob"), (3, "Charlie"), (4, "Diana")
])

# Orders RDD
orders = sc.parallelize([
    (1, "order1"), (1, "order2"), (2, "order3"), (3, "order4")
])

# Inner join
joined = users.join(orders)
print(joined.collect())
# [(1, ('Alice', 'order1')), (1, ('Alice', 'order2')), ...]

# Left outer join
left_joined = users.leftOuterJoin(orders)
print(left_joined.collect())
# [(1, ('Alice', 'order1')), (1, ('Alice', 'order2')), (2, ('Bob', 'order3')), ...]

# Count orders per user
order_counts = orders.map(lambda x: (x[0], 1)).reduceByKey(lambda a, b: a + b)
enriched = users.join(order_counts)
print(enriched.collect())
# [(1, ('Alice', 2)), (2, ('Bob', 1)), (3, ('Charlie', 1))]
```

---

## References

- [Spark RDD Programming Guide](https://spark.apache.org/docs/latest/rdd-programming-guide.html)
- [Spark API Documentation](https://spark.apache.org/docs/latest/api/python/)
- [Advanced Analytics with Spark](http://shop.oreilly.com/product/0636920028512.do)
- [Learning Spark](http://shop.oreilly.com/product/0636920028512.do)
- [Spark: The Definitive Guide](http://shop.oreilly.com/product/0636920028512.do)
