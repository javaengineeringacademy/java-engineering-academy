# Apache Spark

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Spark Ecosystem](#spark-ecosystem)
- [Cluster Manager](#cluster-manager)
- [Resilient Distributed Datasets](#resilient-distributed-datasets)
- [Spark Sessions](#spark-sessions)
- [Memory Management](#memory-management)
- [Execution Model](#execution-model)
- [Configuration](#configuration)
- [Monitoring and Debugging](#monitoring-and-debugging)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Apache Spark is a unified analytics engine for large-scale data processing
providing an interface for programming entire clusters with implicit data
parallelism and fault tolerance. Spark was developed at UC Berkeley's AMPLab
and later donated to the Apache Software Foundation.

### Key Characteristics

- **Speed**: In-memory computing up to 100x faster than MapReduce
- **Ease of use**: High-level APIs in Java, Scala, Python, R, SQL
- **Unified engine**: Batch, streaming, SQL, ML, and graph processing
- **Fault tolerant**: RDD lineage for automatic recovery
- **Polyglot**: Support for multiple languages

### When to Use Spark

- Large-scale data processing and ETL
- Real-time stream processing
- Machine learning at scale
- Graph analytics
- Interactive SQL queries on big data
- Iterative algorithms (e.g., ML, graph)

### Spark vs Hadoop MapReduce

| Feature | Spark | MapReduce |
|---------|-------|-----------|
| Speed | 10-100x faster | Baseline |
| Ease of Use | High-level APIs | Verbose Java |
| Latency | Low | High |
| Memory | In-memory | Disk-based |
| Iterative | Excellent | Poor |
| Streaming | Native support | Requires additional tools |
| Community | Active, modern | Mature, declining |

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Application                           │
│           (Driver + Executors)                           │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                    Driver Program                        │
│  ┌────────────┐  ┌────────────┐  ┌────────────────┐   │
│  │ SparkContext│  │   DAG      │  │   Task         │   │
│  │            │  │  Scheduler │  │   Scheduler    │   │
│  └────────────┘  └────────────┘  └────────────────┘   │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                 Cluster Manager                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────────┐ │
│  │  Standalone│  │   YARN   │  │    Mesos/K8s        │ │
│  └──────────┘  └──────────┘  └──────────────────────┘ │
└──────────────────────────┬──────────────────────────────┘
                           │
    ┌──────────────────────┼──────────────────────┐
    │                      │                      │
┌───▼──────┐  ┌───────────▼───┐  ┌──────────────▼──┐
│ Executor │  │   Executor    │  │   Executor      │
│   JVM    │  │     JVM       │  │     JVM         │
│ ┌──────┐ │  │   ┌──────┐   │  │   ┌──────┐     │
│ │Tasks │ │  │   │Tasks │   │  │   │Tasks │     │
│ └──────┘ │  │   └──────┘   │  │   └──────┘     │
└──────────┘  └───────────────┘  └─────────────────┘
```

### Key Components

1. **Driver**: Coordinates the application, creates SparkContext
2. **Executor**: Runs tasks and stores data on worker nodes
3. **Cluster Manager**: Allocates resources across applications
4. **Task**: Smallest unit of work sent to an executor
5. **Job**: Set of stages triggered by an action
6. **Stage**: Set of tasks that can be computed in parallel
7. **DAG**: Directed Acyclic Graph of stages

---

## Spark Ecosystem

```
┌─────────────────────────────────────────────────────────┐
│                      Spark Core                           │
│               (RDDs, Task Scheduling, Memory)            │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │  Spark SQL   │  │   Spark     │  │   Spark         │ │
│  │  (DataFrames)│  │  Streaming  │  │   MLlib         │ │
│  └─────────────┘  └─────────────┘  └─────────────────┘ │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐                      │
│  │   GraphX    │  │   PySpark   │                      │
│  └─────────────┘  └─────────────┘                      │
└─────────────────────────────────────────────────────────┘
```

### Core Libraries

| Library | Purpose |
|---------|---------|
| **Spark Core** | RDDs, scheduling, memory management |
| **Spark SQL** | DataFrames, SQL queries, structured data |
| **Spark Streaming** | Micro-batch stream processing |
| **Structured Streaming** | Continuous stream processing |
| **MLlib** | Machine learning algorithms |
| **GraphX** | Graph processing |

---

## Cluster Manager

### Standalone Mode

```bash
# Start master
$SPARK_HOME/sbin/start-master.sh

# Start workers
$SPARK_HOME/sbin/start-workers.sh spark://master:7077

# Submit application
spark-submit \
  --master spark://master:7077 \
  --deploy-mode cluster \
  --executor-memory 4G \
  --total-executor-cores 8 \
  my_app.py
```

### YARN Mode

```bash
# Submit to YARN
spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --num-executors 10 \
  --executor-memory 4G \
  --executor-cores 4 \
  --driver-memory 2G \
  my_app.py

# YARN configuration
--conf spark.yarn.historyServer.address=history-server:18080
--conf spark.yarn.maxAppAttempts=2
```

### Kubernetes Mode

```bash
# Submit to Kubernetes
spark-submit \
  --master k8s://https://k8s-master:6443 \
  --deploy-mode cluster \
  --conf spark.kubernetes.container.image=my-spark-image:latest \
  --conf spark.kubernetes.namespace=spark \
  my_app.py
```

### Mesos Mode

```bash
# Submit to Mesos
spark-submit \
  --master mesos://mesos-master:5050 \
  --deploy-mode cluster \
  my_app.py
```

---

## Resilient Distributed Datasets

### RDD Characteristics

- **Immutable**: Once created, cannot be modified
- **Partitioned**: Data split across cluster nodes
- **Lazy**: Transformations not executed until action
- **Fault tolerant**: Lineage enables recovery
- **In-memory**: Cached in memory for performance

### RDD Operations

```python
# Transformations (lazy)
rdd = sc.parallelize([1, 2, 3, 4, 5])
filtered = rdd.filter(lambda x: x > 2)
mapped = rdd.map(lambda x: x * 2)
flat_mapped = rdd.flatMap(lambda x: [x, x * 2])

# Actions (trigger execution)
count = rdd.count()
collect = rdd.collect()
reduce_result = rdd.reduce(lambda a, b: a + b)
first = rdd.first()
take = rdd.take(3)
foreach = rdd.foreach(lambda x: print(x))
```

### RDD Persistence

```python
from pyspark import StorageLevel

# Cache in memory
rdd.cache()  # Same as persist(StorageLevel.MEMORY_ONLY)

# Persist with different storage levels
rdd.persist(StorageLevel.MEMORY_AND_DISK)
rdd.persist(StorageLevel.MEMORY_ONLY_SER)
rdd.persist(StorageLevel.MEMORY_AND_DISK_SER)
rdd.persist(StorageLevel.DISK_ONLY)

# Unpersist
rdd.unpersist()
```

---

## Spark Sessions

### Creating Spark Sessions

```python
from pyspark.sql import SparkSession

# Basic session
spark = SparkSession.builder \
    .appName("My Application") \
    .getOrCreate()

# With configuration
spark = SparkSession.builder \
    .appName("My Application") \
    .master("local[*]") \
    .config("spark.sql.shuffle.partitions", "200") \
    .config("spark.driver.memory", "4g") \
    .config("spark.executor.memory", "8g") \
    .getOrCreate()

# With Hive support
spark = SparkSession.builder \
    .appName("My Application") \
    .enableHiveSupport() \
    .getOrCreate()
```

### Spark Context and SQL Context

```python
# SparkContext (low-level)
sc = spark.sparkContext

# SQL Context
sql_context = spark._wrapped  # Deprecated, use SparkSession

# Hive Context
hive_context = spark._wrapped  # Deprecated, use SparkSession
```

---

## Memory Management

### Memory Layout

```
┌──────────────────────────────────────────────────────┐
│                    Executor Memory                    │
├──────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐│
│  │           Reserved Memory (300MB)                ││
│  ├─────────────────────────────────────────────────┤│
│  │           User Memory                           ││
│  │     (User data structures, UDFs)                ││
│  ├─────────────────────────────────────────────────┤│
│  │           Unified Memory                        ││
│  │  ┌──────────────┬──────────────────────────┐   ││
│  │  │  Execution   │       Storage            │   ││
│  │  │  Memory      │       Memory             │   ││
│  │  │              │                          │   ││
│  │  │  (Shuffles,  │       (Cached RDDs,     │   ││
│  │  │   Joins,     │        Broadcast vars)  │   ││
│  │  │   Sorts)     │                          │   ││
│  │  └──────────────┴──────────────────────────┘   ││
│  └─────────────────────────────────────────────────┘│
└──────────────────────────────────────────────────────┘
```

### Memory Configuration

```python
# Executor memory
spark.conf.set("spark.executor.memory", "8g")
spark.conf.set("spark.executor.memoryOverhead", "2g")

# Driver memory
spark.conf.set("spark.driver.memory", "4g")
spark.conf.set("spark.driver.memoryOverhead", "1g")

# Memory fractions
spark.conf.set("spark.memory.fraction", 0.6)  # 60% of heap
spark.conf.set("spark.memory.storageFraction", 0.5)  # 50% of unified

# Off-heap memory
spark.conf.set("spark.memory.offHeap.enabled", True)
spark.conf.set("spark.memory.offHeap.size", "4g")
```

---

## Execution Model

### Job Execution Flow

```
Action (e.g., collect(), count())
         │
         ▼
┌─────────────────────┐
│   DAG Scheduler     │
│   (Create Stages)   │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Task Scheduler     │
│  (Create Tasks)     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Executor Manager   │
│  (Launch Tasks)     │
└──────────┬──────────┘
           │
    ┌──────┼──────┐
    │      │      │
┌───▼──┐ ┌▼───┐ ┌▼───┐
│Task 1│ │Task2│ │Task3│
└──────┘ └────┘ └────┘
```

### DAG and Stages

```python
# This creates a DAG with 2 stages
rdd1 = sc.parallelize(range(1000))
rdd2 = rdd1.map(lambda x: x * 2)          # Stage 0
rdd3 = rdd1.filter(lambda x: x > 100)     # Stage 0
rdd4 = rdd2.intersection(rdd3)            # Stage 1 (shuffle)
result = rdd4.collect()                     # Action triggers execution

#查看执行计划
print(rdd4.toDebugString().decode())
```

---

## Configuration

### Configuration Hierarchy

1. **Programmatic**: `spark.conf.set()` in code
2. **Spark-submit**: `--conf` flags
3. **Properties file**: `spark-defaults.conf`
4. **Environment variables**: `SPARK_*`

### Common Settings

```python
# Application
spark.conf.set("spark.app.name", "MyApp")
spark.conf.set("spark.master", "local[*]")

# Parallelism
spark.conf.set("spark.default.parallelism", 200)
spark.conf.set("spark.sql.shuffle.partitions", 200)

# Serialization
spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
spark.conf.set("spark.kryo.registrationRequired", False)

# Compression
spark.conf.set("spark.io.compression.codec", "snappy")
spark.conf.set("spark.rdd.compress", True)

# Network
spark.conf.set("spark.rpc.message.maxSize", "256m")
spark.conf.set("spark.network.timeout", "600s")
```

### Environment Variables

```bash
export SPARK_HOME=/opt/spark
export SPARK_MASTER_HOST=master-node
export SPARK_MASTER_PORT=7077
export SPARK_WORKER_MEMORY=8G
export SPARK_WORKER_CORES=4
export SPARK_DAEMON_MEMORY=4G
export HADOOP_CONF_DIR=/etc/hadoop/conf
export YARN_CONF_DIR=/etc/hadoop/conf
```

---

## Monitoring and Debugging

### Spark UI

```
http://<driver-node>:4040/

# Key pages:
- Jobs: List of all jobs and their status
- Stages: Detailed stage information
- Storage: Cached RDDs and DataFrames
- Executors: Executor status and metrics
- SQL: Query execution plans
```

### Event Logging

```python
# Enable event logging
spark.conf.set("spark.eventLog.enabled", True)
spark.conf.set("spark.eventLog.dir", "hdfs:///spark-history")
spark.conf.set("spark.eventLog.compress", True)

# History server
$SPARK_HOME/sbin/start-history-server.sh
```

### Debugging Tools

```python
# Check execution plan
df.explain(True)  # Shows optimized plan

# Count partitions
print(rdd.getNumPartitions())

# Inspect data
rdd.take(5)
df.show(5)
df.printSchema()

# Check partitioning
print(rdd.glom().map(len).collect())
```

---

## Best Practices

### Memory Management

1. **Cache strategically**: Only cache data used multiple times
2. **Use appropriate storage levels**: MEMORY_AND_DISK for large datasets
3. **Monitor memory usage**: Check Spark UI for spills
4. **Tune memory fractions**: Adjust based on workload

### Performance

1. **Minimize shuffles**: Use `reduceByKey` instead of `groupByKey`
2. **Broadcast small tables**: Use `broadcast()` for joins
3. **Partition appropriately**: Match partition count to cluster size
4. **Use Kryo serialization**: Faster than Java serialization

### Code Quality

1. **Use DataFrame API**: Better optimization than RDD API
2. **Avoid UDFs when possible**: Built-in functions are optimized
3. **Test locally first**: Use local mode for development
4. **Profile and benchmark**: Measure performance improvements

### Resource Allocation

```python
# Dynamic allocation
spark.conf.set("spark.dynamicAllocation.enabled", True)
spark.conf.set("spark.shuffle.service.enabled", True)
spark.conf.set("spark.dynamicAllocation.minExecutors", 2)
spark.conf.set("spark.dynamicAllocation.maxExecutors", 20)

# Static allocation
spark.conf.set("spark.executor.instances", 10)
spark.conf.set("spark.executor.cores", 4)
```

---

## References

- [Apache Spark Documentation](https://spark.apache.org/docs/latest/)
- [Spark Programming Guide](https://spark.apache.org/docs/latest/rdd-programming-guide.html)
- [Spark SQL Guide](https://spark.apache.org/docs/latest/sql-programming-guide.html)
- [Spark Configuration](https://spark.apache.org/docs/latest/configuration.html)
- [Learning Spark](http://shop.oreilly.com/product/0636920028512.do)
- [High Performance Spark](http://shop.oreilly.com/product/0636920028512.do)
