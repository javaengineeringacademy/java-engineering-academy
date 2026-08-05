# Big Data

## Table of Contents

1. [What is Big Data](#what-is-big-data)
2. [The Three Vs](#the-three-vs)
3. [Beyond the Three Vs](#beyond-the-three-vs)
4. [Big Data Architecture](#big-data-architecture)
5. [Distributed Storage](#distributed-storage)
6. [Distributed Processing](#distributed-processing)
7. [When to Use Big Data](#when-to-use-big-data)
8. [Limitations and Challenges](#limitations-and-challenges)
9. [Key Takeaways](#key-takeaways)

---

## What is Big Data

Big data refers to datasets that are too large or complex to be processed by traditional data processing applications. It encompasses the tools, techniques, and practices used to analyze and extract value from massive volumes of structured, semi-structured, and unstructured data.

### Historical Context

The term "big data" emerged in the early 2000s as data volumes began exceeding the capabilities of relational databases and single-server processing. Key milestones:

- **2003**: Google publishes the Google File System (GFS) paper
- **2004**: Google publishes the MapReduce paper
- **2006**: Doug Cutting and Mike Cafarella create Hadoop
- **2008**: "Nature" publishes a special issue on big data
- **2011**: McKinsey coins "big data" in business context
- **2012**: Hadoop ecosystem matures, Spark emerges
- **2014+**: Cloud-native big data solutions proliferate

### Why Big Data Matters

- **Volume**: Organizations generate exponentially more data each year
- **Velocity**: Data arrives faster than ever (IoT, social media, transactions)
- **Variety**: Data comes in many formats (text, images, video, logs)
- **Value**: Hidden patterns in large datasets drive competitive advantage
- **Veracity**: Data quality challenges increase with scale

---

## The Three Vs

### Volume

The amount of data generated and stored.

**Scale examples**:
- Social media: ~500 million tweets per day
- IoT: ~79.4 zettabytes of data by 2025
- Healthcare: ~2,314 exabytes of data by 2025
- Financial: ~10 billion transactions per day globally

**Storage implications**:
- Single-server storage limits (typically 10-100 TB)
- Need for distributed storage across many machines
- Tiered storage: hot, warm, cold, archive
- Compression and encoding strategies

### Velocity

The speed at which data is generated, processed, and analyzed.

**Data generation speeds**:
- Real-time: Milliseconds (IoT sensors, financial trades)
- Near real-time: Seconds (social media feeds, log streams)
- Micro-batch: Minutes (clickstream aggregation)
- Batch: Hours to days (nightly ETL, weekly reports)

**Processing considerations**:
- Stream processing for low-latency requirements
- Micro-batching as a compromise between batch and streaming
- Buffering and queuing mechanisms (Kafka, Pulsar)
- Back-pressure handling for variable input rates

### Variety

The different types and formats of data.

| Data Type | Format | Examples |
|-----------|--------|----------|
| Structured | Rows/Columns | SQL tables, CSV, Parquet |
| Semi-structured | Key-value, nested | JSON, XML, YAML, Avro |
| Unstructured | Binary/text | Images, videos, PDFs, emails |
| Time-series | Ordered data points | Sensor readings, stock prices |
| Graph | Nodes and edges | Social networks, knowledge graphs |
| Geospatial | Location data | GPS coordinates, maps |

**Variety challenges**:
- Schema evolution and versioning
- Data type conversion and normalization
- Multiple storage systems for different data types
- Unified query interfaces across data types

---

## Beyond the Three Vs

### Veracity

The quality, accuracy, and trustworthiness of data.

- **Data Quality**: Completeness, consistency, timeliness, accuracy
- **Data Provenance**: Tracking data origin and transformation history
- **Noise and Errors**: Outliers, missing values, duplicates
- **Bias**: Sampling bias, measurement bias, survivorship bias

### Value

The ability to extract meaningful insights from data.

- Raw data has no inherent value — it must be processed and analyzed
- Value varies by use case: same data, different insights
- Cost-benefit analysis: processing cost vs. insight value
- Data monetization strategies (direct, indirect, bundled)

### Variability

The inconsistency of data meaning and interpretation.

- Same field can have different meanings across contexts
- Temporal variability: data meaning changes over time
- Context-dependent interpretation
- Semantic ambiguity requiring business knowledge

---

## Big Data Architecture

### Generic Big Data Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Data Sources                              │
│   (Databases, APIs, Files, Streams, IoT, Social Media)      │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  Ingestion Layer                             │
│   (Kafka, Flume, Sqoop, NiFi, Custom Agents)               │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  Storage Layer                               │
│   (HDFS, S3, GCS, ADLS, Delta Lake, Iceberg)              │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│               Processing Layer                              │
│   (Spark, Flink, MapReduce, Beam, Presto)                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│               Serving Layer                                  │
│   (Data Warehouse, Data Lakehouse, Feature Store)          │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│               Consumption Layer                              │
│   (BI Tools, ML Platforms, Applications, APIs)             │
└─────────────────────────────────────────────────────────────┘
```

### Lambda Architecture

Combines batch and stream processing:

```
┌─────────────────┐     ┌──────────────────┐
│   Batch Layer   │     │   Speed Layer    │
│ (Master Dataset)│     │ (Real-time Views)│
└────────┬────────┘     └────────┬─────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│           Serving Layer                  │
│   (Merged Batch + Real-time Views)      │
└─────────────────────────────────────────┘
```

**Pros**: Fault-tolerant, scalable, handles both historical and real-time data
**Cons**: Maintaining two codebases (batch and speed layers), complexity

### Kappa Architecture

Single stream processing layer:

```
┌──────────┐    ┌─────────────┐    ┌──────────────────┐
│  Sources │───▶│    Kafka     │───▶│ Stream Processing │
└──────────┘    └─────────────┘    │ (Flink, Spark)   │
                                   └────────┬─────────┘
                                            │
                                   ┌────────▼─────────┐
                                   │   Serving Layer   │
                                   └──────────────────┘
```

**Pros**: Simpler, single codebase, replay from Kafka
**Cons**: Requires robust stream processing, Kafka as single point of failure

### Data Lakehouse Architecture

Combines data lake flexibility with data warehouse reliability:

```
┌─────────────────────────────────────────────┐
│          Query Engines                       │
│    (Spark, Trino, Presto, Athena)           │
├─────────────────────────────────────────────┤
│          Table Format Layer                  │
│    (Delta Lake, Iceberg, Hudi)              │
├─────────────────────────────────────────────┤
│          Object Storage                      │
│    (S3, GCS, ADLS, HDFS)                   │
└─────────────────────────────────────────────┘
```

---

## Distributed Storage

### Why Distributed Storage

- Single machines have limited disk capacity and I/O bandwidth
- Replication provides fault tolerance
- Parallel access improves throughput
- Horizontal scaling is more cost-effective than vertical scaling

### Data Partitioning

**Horizontal Partitioning (Sharding)**:
- Split rows across multiple nodes
- Each partition contains a subset of rows
- Enables parallel processing

**Vertical Partitioning**:
- Split columns across multiple nodes
- Useful when tables have many columns
- Reduces I/O for queries accessing few columns

**Hash Partitioning**:
```
partition = hash(key) % num_partitions
```
- Uniform distribution
- Good for point lookups
- Poor for range queries

**Range Partitioning**:
- Partitions based on value ranges
- Good for range queries
- Risk of hotspots with sequential keys

### Replication Strategies

**Leader-Follower (Master-Slave)**:
- One leader handles writes, followers replicate
- Followers can serve read requests
- Single point of failure for writes (unless leader is elected)

**Multi-Leader (Multi-Master)**:
- Multiple nodes can accept writes
- Conflict resolution required
- Higher write availability

**Leaderless (Dynamo-style)**:
- Any node can accept reads/writes
- Quorum-based consistency
- Tunable consistency levels

### Consistency Levels

| Level | Description | Use Case |
|-------|-------------|----------|
| ONE | Write/ack after one replica | High availability |
| QUORUM | Write/ack after majority (N/2+1) | Balanced consistency |
| ALL | Write/ack after all replicas | Strong consistency |

Formula: `W + R > N` guarantees strong consistency where W=write replicas, R=read replicas, N=total replicas.

---

## Distributed Processing

### MapReduce Approach

The foundational approach for distributed data processing:

```
Input → Split → Map → Shuffle/Sort → Reduce → Output
```

**Map Phase**:
- Input split into chunks
- Each chunk processed independently
- Produces intermediate key-value pairs

**Shuffle/Sort Phase**:
- Groups intermediate values by key
- Transfers data across network to reducer nodes

**Reduce Phase**:
- Processes all values for each key
- Produces final output

### Data Parallelism

- Same operation applied to different data partitions simultaneously
- Ideal for embarrassingly parallel problems
- Examples: filtering, mapping, aggregation

### Task Parallelism

- Different operations running simultaneously on different data
- Pipeline parallelism: stages overlap
- Examples: multiple independent transformations

### Fault Tolerance in Distributed Processing

**Task Retry**:
- Failed tasks automatically restarted on other nodes
- Requires idempotent operations

**Checkpointing**:
- Periodic snapshot of intermediate state
- Recovery from last checkpoint on failure
- Examples: Flink checkpointing, Spark RDD lineage

**Data Replication**:
- Input data replicated across nodes
- Lost tasks can re-read from local replica

### Resource Management

**Centralized Scheduler**:
- Single ResourceManager allocates resources
- Examples: YARN, Kubernetes
- Risk: single point of failure

**Decentralized Scheduler**:
- Each node manages its own resources
- Examples: Mesos
- More complex but more resilient

---

## When to Use Big Data

### Suitable Use Cases

1. **Data exceeds single machine capacity**: Dataset > 1TB or growing rapidly
2. **Real-time analytics**: Need to process millions of events per second
3. **Complex transformations**: Multi-step processing on large datasets
4. **Machine learning**: Training models on massive datasets
5. **Log aggregation**: Collecting and analyzing logs from thousands of servers
6. **Social graph analysis**: Analyzing relationships in networks with billions of edges

### When NOT to Use Big Data

1. **Small datasets**: Data fits comfortably on a single machine
2. **Simple queries**: Standard SQL on a relational database suffices
3. **Tight budgets**: Big data infrastructure has significant cost
4. **Limited expertise**: Team lacks distributed systems knowledge
5. **Real-time not required**: Batch processing on smaller systems works

### Decision Framework

```
Is your data > 1TB or growing > 100GB/month?
  → No: Use traditional database
  → Yes: Continue

Do you need distributed processing?
  → No: Consider vertical scaling
  → Yes: Continue

Do you have the expertise to manage distributed systems?
  → No: Consider managed cloud services (BigQuery, Redshift, Snowflake)
  → Yes: Continue

What's your latency requirement?
  → Batch: Hadoop/Spark
  → Real-time: Kafka/Flink
  → Interactive: Presto/Trino
```

### Cost Considerations

| Factor | Impact |
|--------|--------|
| Infrastructure | Compute, storage, network costs |
| Engineering | Hiring and retaining skilled engineers |
| Operations | Monitoring, maintenance, upgrades |
| Opportunity | Time to value, missed insights |
| Vendor Lock-in | Cloud-specific services reduce portability |

---

## Limitations and Challenges

### Technical Challenges

1. **Data Locality**: Moving computation to data is expensive
2. **Network Bottlenecks**: Shuffling data across nodes is slow
3. **Small Files Problem**: Too many small files overwhelm metadata systems
4. **Schema Evolution**: Changing schemas across distributed systems is complex
5. **State Management**: Maintaining state across failures is difficult
6. **Exactly-Once Semantics**: Achieving exactly-once processing is hard

### Organizational Challenges

1. **Skill Gap**: Shortage of qualified data engineers
2. **Data Silos**: Data trapped in departmental systems
3. **Governance**: Ensuring data quality and compliance at scale
4. **Cost Management**: Uncontrolled spending on cloud resources
5. **Tool Sprawl**: Too many tools creating complexity
6. **Time to Insight**: Long development cycles for new analytics

### Common Pitfalls

- **Over-engineering**: Building for scale you don't need yet
- **Ignoring data quality**: Garbage in, garbage out
- **Lack of monitoring**: Not knowing when things fail
- **No documentation**: Tribal knowledge that walks out the door
- **Premature optimization**: Tuning before understanding access patterns
- **Treating big data as a silver bullet**: Not every problem needs big data

### Anti-Patterns

| Anti-Pattern | Description | Better Approach |
|-------------|-------------|-----------------|
| Data Swamp | Unmanaged, undocumented data lake | Data lakehouse with governance |
| Schema-on-Read Chaos | No schema validation | Schema-on-write with contracts |
| Copy-Paste ETL | Duplicated pipeline logic | Reusable pipeline templates |
| Hero Culture | One person knows everything | Shared ownership and documentation |
| Premature Scaling | Oversized clusters from day one | Start small, scale based on metrics |

---

## Key Takeaways

1. **Big data is defined by the 3 Vs**: Volume, Velocity, and Variety — but also consider Veracity and Value
2. **Distributed storage and processing** enable handling data beyond single-machine limits
3. **Architecture choices** (Lambda, Kappa, Lakehouse) depend on latency requirements and complexity tolerance
4. **Not every problem needs big data** — evaluate scale requirements before adopting big data tools
5. **Fault tolerance** is a first-class concern in distributed systems, not an afterthought
6. **Data quality and governance** become more critical as data volume increases
7. **Cost management** is essential — big data infrastructure can be expensive
8. **The skill gap** is real — invest in training and hiring data engineers
9. **Start small and scale** — premature optimization is wasteful
10. **Modern alternatives** (cloud warehouses, managed services) have reduced the need for custom big data infrastructure in many cases

---

## Further Reading

- *Big Data: Principles and Best Practices* by Nathan Marz and James Warren
- *Designing Data-Intensive Applications* by Martin Kleppmann
- *The Big Data Startup* by Lee Schlesinger
- *Data-Intensive Text Processing with MapReduce* by Jimmy Lin and Chris Dyer
