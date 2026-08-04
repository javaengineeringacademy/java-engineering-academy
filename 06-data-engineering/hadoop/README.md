# Hadoop Ecosystem

## Table of Contents

1. [Hadoop History](#hadoop-history)
2. [Hadoop Architecture](#hadoop-architecture)
3. [HDFS](#hdfs)
4. [MapReduce](#mapreduce)
5. [YARN](#yarn)
6. [Ecosystem Components](#ecosystem-components)
7. [Deployment Modes](#deployment-modes)
8. [Hadoop Distributions](#hadoop-distributions)
9. [Key Takeaways](#key-takeaways)

---

## Hadoop History

### Origins

- **2003**: Google publishes the Google File System (GFS) paper
- **2004**: Google publishes the MapReduce paper
- **2004-2006**: Doug Cutting and Mike Cafarella create Hadoop (named after Cutting's son's toy elephant)
- **2006**: Hadoop becomes a top-level Apache project
- **2008**: Yahoo! runs 10,000-node Hadoop cluster
- **2011**: Cloudera, Hortonworks, MapR commercialize Hadoop
- **2016**: Apache Spark surpasses MapReduce in popularity
- **2020+**: Cloud-native alternatives reduce on-premise Hadoop adoption

### Hadoop Versions

| Version | Year | Key Features |
|---------|------|--------------|
| Hadoop 1.x | 2006 | HDFS + MapReduce only |
| Hadoop 2.x | 2013 | YARN, HDFS Federation, NameNode HA |
| Hadoop 3.x | 2017 | Erasure coding, YARN Timeline Service v2, intra-DataNode balancing |

---

## Hadoop Architecture

### Core Components

```
┌─────────────────────────────────────────────────┐
│                  Hadoop Core                      │
├─────────────────────────────────────────────────┤
│  ┌─────────┐  ┌───────────┐  ┌───────────────┐ │
│  │  HDFS   │  │ MapReduce │  │     YARN      │ │
│  │ Storage │  │Processing │  │   Resource    │ │
│  │         │  │           │  │  Management   │ │
│  └─────────┘  └───────────┘  └───────────────┘ │
└─────────────────────────────────────────────────┘
```

### Hadoop 1.x Architecture

```
┌──────────────────────────────────────────┐
│              NameNode                     │
│         (Master, Metadata)               │
└───────────────────┬──────────────────────┘
                    │
    ┌───────────────┼───────────────┐
    │               │               │
┌───▼───┐      ┌───▼───┐      ┌───▼───┐
│DataNode│      │DataNode│      │DataNode│
│ (Slave)│      │ (Slave)│      │ (Slave)│
└───────┘      └───────┘      └───────┘
```

**Limitations**:
- Single NameNode = single point of failure
- MapReduce only processing framework
- Scaling limited by NameNode memory

### Hadoop 2.x/3.x Architecture

```
┌─────────────────────────────────────────────────┐
│                  YARN Layer                       │
│  ┌─────────────────┐  ┌──────────────────────┐  │
│  │ ResourceManager  │  │  NodeManager (×N)    │  │
│  │ (Scheduler +     │  │  (Container Runtime) │  │
│  │  Applications    │  │                      │  │
│  │  Manager)        │  └──────────────────────┘  │
│  └─────────────────┘                              │
├─────────────────────────────────────────────────┤
│                  HDFS Layer                       │
│  ┌──────────────┐  ┌────────────────────────┐   │
│  │  NameNode     │  │  DataNode (×N)         │   │
│  │  (Metadata)   │  │  (Block Storage)       │   │
│  └──────────────┘  └────────────────────────┘   │
└─────────────────────────────────────────────────┘
```

---

## HDFS

### Overview

Hadoop Distributed File System (HDFS) is a distributed, scalable, and fault-tolerant filesystem designed for storing very large files across machines in a large cluster.

### Key Characteristics

- **Write-Once, Read-Many**: Files are immutable after creation
- **Large Files**: Optimized for files > 1GB
- **Block-Based**: Files split into fixed-size blocks (128MB default)
- **Replication**: Each block replicated 3 times (default)
- **Commodity Hardware**: Designed for inexpensive hardware

### Architecture

- **NameNode**: Stores filesystem metadata (file→block mapping, block locations)
- **DataNode**: Stores actual data blocks, sends heartbeats to NameNode
- **Secondary NameNode**: Checkpointing helper (not a hot standby)

### Block Storage

```
File: 512MB
Block Size: 128MB

Block 1: 0-128MB    → Replica on DN1, DN2, DN4
Block 2: 128-256MB  → Replica on DN2, DN3, DN5
Block 3: 256-384MB  → Replica on DN1, DN3, DN6
Block 4: 384-512MB  → Replica on DN2, DN4, DN5
```

### Fault Tolerance

- Default replication factor: 3
- Blocks distributed across racks for rack awareness
- DataNode failure detected via heartbeat loss
- Under-replicated blocks automatically re-replicated

### HDFS High Availability (HDFS HA)

```
┌───────────────────────────────────────────┐
│          Active NameNode                   │
└───────────────────┬───────────────────────┘
                    │ JournalNodes
┌───────────────────▼───────────────────────┐
│         Standby NameNode                  │
│       (Hot standby, reads metadata)       │
└───────────────────────────────────────────┘
```

- Two NameNodes: Active and Standby
- Shared EditLog via JournalNodes (3+ for quorum)
- Automatic failover via ZKFC (ZooKeeper Failover Controller)

### HDFS Federation

- Multiple NameNodes manage different namespaces
- Each NameNode manages a portion of the filesystem
- Enables scaling beyond single NameNode memory limits
- All NameNodes share the same DataNodes

---

## MapReduce

### Processing Model

1. **Input Phase**: Input split into chunks (one per Map task)
2. **Map Phase**: Each chunk processed independently, emits key-value pairs
3. **Shuffle Phase**: Intermediate data transferred to reducers, sorted by key
4. **Reduce Phase**: Processes all values for each key, produces output
5. **Output Phase**: Results written to HDFS

### Job Execution

```
Input (HDFS) → InputFormat → RecordReader → Mapper
    → Partitioner → Combiner → Sort → Reducer → OutputFormat → Output (HDFS)
```

### Example: Word Count

```java
// Mapper
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) {
        for (String word : value.toString().split("\\s+")) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}

// Reducer
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context) {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
```

### Limitations

- Disk-based intermediate data
- No native support for iterative algorithms
- Rigid two-phase model
- High latency for small datasets
- Largely replaced by Spark for new workloads

---

## YARN

### Yet Another Resource Negotiator

YARN separates resource management from job scheduling/monitoring in Hadoop 2.x+.

### Components

**ResourceManager (RM)**:
- Master daemon managing cluster resources
- Scheduler: Allocates resources based on policies (FIFO, Capacity, Fair)
- ApplicationsManager: Manages application lifecycle

**NodeManager (NM)**:
- Agent on each worker node
- Manages containers on that node
- Reports resource usage to RM

**ApplicationMaster (AM)**:
- Per-application process
- Negotiates resources from RM
- Works with NMs to execute tasks

**Container**:
- Allocation of resources (CPU, memory) on a node
- JVM process running a task
- Isolated environment

### Application Lifecycle

```
1. Client submits application to RM
2. RM launches AM in a container
3. AM registers with RM
4. AM requests containers for tasks
5. RM allocates containers
6. AM launches tasks in containers
7. Tasks report progress to AM
8. AM reports to RM
9. Application completes, containers released
```

### YARN Scheduler Types

| Scheduler | Description |
|-----------|-------------|
| FIFO | First In, First Out, simple but unfair |
| Capacity | Divides cluster into queues with guaranteed capacity |
| Fair | Equal share of resources among all applications |

---

## Ecosystem Components

### Apache Hive

- SQL-like interface (HiveQL) for querying data in HDFS
- Translates queries to MapReduce/Tez/Spark jobs
- Supports tables, partitions, buckets
- Schema-on-read approach

### Apache Pig

- High-level language (Pig Latin) for data analysis
- Compiles to MapReduce jobs
- Supports UDFs for custom processing
- Good for ad-hoc data exploration

### Apache HBase

- NoSQL database on top of HDFS
- Column-family storage model
- Random read/write access
- Real-time access to large datasets

### Apache Sqoop

- Import/export data between HDFS and relational databases
- Supports full and incremental imports
- Parallel import/export for performance

### Apache Flume

- Distributed service for collecting log data
- Streaming data into HDFS
- Sources, Channels, Sinks architecture

### Apache Oozie

- Workflow scheduler for Hadoop jobs
- DAG-based workflow execution
- SupportsMapReduce, Pig, Hive, Spark, and custom actions

### Apache ZooKeeper

- Distributed coordination service
- Leader election, configuration management, synchronization
- Foundation for HDFS HA, HBase, and many other systems

### Apache Spark

- Fast, general-purpose cluster computing
- In-memory processing (10-100x faster than MapReduce)
- Supports SQL, streaming, ML, and graph processing
- Runs on YARN, Mesos, Kubernetes, or standalone

### Apache Tez

- DAG execution engine for Hadoop
- Optimizes MapReduce chains into a single DAG
- Used as Hive execution engine (Hive on Tez)

### Apache Ambari

- Web-based management and monitoring for Hadoop
- Cluster provisioning, management, and monitoring
- REST API for automation

---

## Deployment Modes

### Standalone Mode

- Single JVM for all Hadoop daemons
- Good for development and debugging
- No distributed processing

### Pseudo-Distributed Mode

- All daemons run on a single machine but in separate JVMs
- Simulates a cluster environment
- Good for testing

### Fully Distributed Mode

- Daemons spread across multiple machines
- Production deployment
- Requires proper hardware sizing and network configuration

### Cloud Deployment

- **EMR** (AWS): Managed Hadoop on EC2
- **Dataproc** (GCP): Managed Hadoop on Compute Engine
- **HDInsight** (Azure): Managed Hadoop on Azure VMs

---

## Hadoop Distributions

### Cloudera (CDP - Cloudera Data Platform)

- Enterprise data platform combining Hadoop and cloud
- Cloudera Manager for cluster management
- Includes: HDFS, YARN, Hive, Spark, Kafka, and more
- Strong security and governance features

### Hortonworks (HDP)

- 100% open-source Hadoop distribution
- Ambari for cluster management
- Focus on governance and operations
- Merged with Cloudera in 2019

### MapR (now HPE Ezmeral)

- Converged data platform
- Custom filesystem (MapR-FS) replacing HDFS
- No NameNode bottleneck
- High performance but less open-source community

### Comparison

| Feature | CDP | HDP | MapR |
|---------|-----|-----|------|
| Management | Cloudera Manager | Ambari | MapR Control System |
| Filesystem | HDFS | HDFS | MapR-FS |
| Security | Sentry/Ranger | Ranger | Native ACLs |
| Open Source | Partial | Yes | Partial |

---

## Key Takeaways

1. **Hadoop** pioneered distributed storage and processing for big data
2. **HDFS** provides fault-tolerant distributed storage with replication
3. **MapReduce** introduced the map-shuffle-reduce paradigm but has been superseded by Spark
4. **YARN** separated resource management from processing, enabling multiple frameworks
5. **The ecosystem** includes Hive, Pig, HBase, Sqoop, Flume, and many more tools
6. **Hadoop 3.x** added erasure coding, YARN improvements, and better scalability
7. **Cloud-managed services** (EMR, Dataproc) simplify Hadoop operations
8. **Hadoop distributions** (CDP, HDP) provide enterprise features and support
9. **Hadoop is still relevant** for on-premise big data, though Spark has replaced MapReduce for processing
10. **Understanding Hadoop** is essential for data engineers as many modern tools are inspired by or built on Hadoop concepts

---

## Further Reading

- *Hadoop: The Definitive Guide* by Tom White
- *Hadoop Operations* by Eric Sammer
- *Programming Hadoop* by Benjamin Bengfort and Jenny Kim
- Apache Hadoop Official Documentation
