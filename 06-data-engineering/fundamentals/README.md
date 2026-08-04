# Data Engineering Fundamentals

## Table of Contents

1. [What is Data Engineering](#what-is-data-engineering)
2. [Role of a Data Engineer](#role-of-a-data-engineer)
3. [Data Lifecycle](#data-lifecycle)
4. [Data Flow Architecture](#data-flow-architecture)
5. [Batch vs Real-Time Processing](#batch-vs-real-time-processing)
6. [Data Modeling Basics](#data-modeling-basics)
7. [Data Pipeline Design](#data-pipeline-design)
8. [SLAs and Data Contracts](#slas-and-data-contracts)
9. [Key Takeaways](#key-takeaways)

---

## What is Data Engineering

Data engineering is the discipline of designing, building, and maintaining systems and infrastructure that collect, store, process, and deliver data efficiently and reliably. It bridges the gap between raw data and actionable insights by providing the foundational layer upon which analytics, machine learning, and business intelligence operate.

### Core Principles

- **Reliability**: Systems must produce consistent, correct results under all conditions
- **Scalability**: Infrastructure must handle growing data volumes without degradation
- **Maintainability**: Pipelines and systems should be easy to understand, modify, and debug
- **Performance**: Data must be delivered within acceptable time windows
- **Cost Efficiency**: Resources should be utilized optimally

### Data Engineering vs Related Roles

| Aspect | Data Engineer | Data Scientist | Data Analyst |
|--------|--------------|----------------|--------------|
| Primary Focus | Infrastructure & Pipelines | Models & Insights | Reports & Analysis |
| Key Skills | SQL, Python, Distributed Systems | Statistics, ML, Python | SQL, BI Tools, Statistics |
| Output | Pipelines, Datasets | Models, Notebooks | Dashboards, Reports |
| Audience | Internal Teams | Business & Research | Business Stakeholders |

---

## Role of a Data Engineer

### Responsibilities

1. **Data Pipeline Development**: Build and maintain ETL/ELT pipelines that move data between systems
2. **Data Storage**: Design and manage data warehouses, data lakes, and lakehouses
3. **Data Quality**: Implement validation, cleansing, and monitoring mechanisms
4. **Infrastructure Management**: Provision and maintain compute and storage resources
5. **Performance Optimization**: Tune queries, partitions, and resource allocation
6. **Security & Compliance**: Ensure data access controls and regulatory compliance
7. **Documentation**: Maintain schemas, lineage, and operational runbooks

### Modern Data Stack

```
┌─────────────────────────────────────────────────────────┐
│                    Consumption Layer                     │
│         (BI Tools, ML Platforms, Applications)           │
├─────────────────────────────────────────────────────────┤
│                    Transformation Layer                   │
│         (dbt, Spark, Airflow, Custom Code)              │
├─────────────────────────────────────────────────────────┤
│                     Storage Layer                        │
│      (Data Warehouse, Data Lake, Lakehouse)             │
├─────────────────────────────────────────────────────────┤
│                     Ingestion Layer                      │
│        (Kafka, Flink, Airbyte, Custom ETL)             │
├─────────────────────────────────────────────────────────┤
│                     Source Layer                         │
│    (Databases, APIs, Files, IoT Devices, Logs)          │
└─────────────────────────────────────────────────────────┘
```

### Essential Skills

- **Programming**: Python, SQL, Scala, Java
- **Cloud Platforms**: AWS (Redshift, Glue, EMR), GCP (BigQuery, Dataflow, Dataproc), Azure (Synapse, Data Factory)
- **Orchestration**: Apache Airflow, Prefect, Dagster
- **Processing Frameworks**: Apache Spark, Flink, Beam
- **Messaging Systems**: Kafka, Pulsar, RabbitMQ
- **Containerization**: Docker, Kubernetes

---

## Data Lifecycle

The data lifecycle describes the journey data takes from creation to archival or deletion.

### Stages

```
Creation → Ingestion → Storage → Processing → Analytics → Archival → Deletion
```

1. **Creation/Source**: Data is generated at source systems (applications, sensors, logs)
2. **Ingestion**: Data is collected and moved into the data platform (batch or streaming)
3. **Storage**: Data is persisted in appropriate storage systems (raw, curated, aggregated)
4. **Processing**: Data is transformed, enriched, cleaned, and aggregated
5. **Analytics/Consumption**: Data is queried, visualized, and used for decision-making
6. **Archival**: Older data is moved to cheaper storage tiers
7. **Deletion**: Data is securely removed per retention policies

### Data States

| State | Description | Example |
|-------|-------------|---------|
| Raw | Unprocessed, as-is from source | JSON logs in S3 |
| Staged | Validated, lightly cleaned | Parsed CSV in staging |
| Curated | Transformed, enriched | Star schema in warehouse |
| Aggregated | Pre-computed summaries | Daily revenue tables |
| Archived | Compressed, cold storage | Parquet in Glacier |

### Data Governance Throughout Lifecycle

- **Lineage Tracking**: Know where data comes from and how it transforms
- **Access Control**: Restrict data access based on roles and sensitivity
- **Quality Checks**: Validate data at each stage transition
- **Retention Policies**: Define how long data is kept at each state
- **Audit Logging**: Track who accessed or modified data

---

## Data Flow Architecture

### Push vs Pull Architecture

**Push Architecture** (Event-Driven):
- Sources emit events as they occur
- Downstream systems react to events
- Low latency, real-time capable
- Examples: Kafka producers, webhooks

**Pull Architecture** (Polling-Based):
- Consumers periodically check for new data
- Simpler to implement, but higher latency
- Higher load on source systems
- Examples: Cron jobs, scheduled queries

### Lambda Architecture

```
                    ┌─────────────────────┐
                    │   Batch Layer       │
                    │  (Historical Data)  │
                    └─────────┬───────────┘
                              │
┌──────────┐    ┌─────────────┴──────────┐    ┌──────────────┐
│  Sources │───▶│     Speed Layer        │───▶│  Serving     │
│          │    │  (Real-time Stream)    │    │  Layer       │
└──────────┘    └────────────────────────┘    └──────────────┘
```

- **Batch Layer**: Processes complete datasets, produces batch views
- **Speed Layer**: Processes recent data in real-time, produces real-time views
- **Serving Layer**: Merges batch and real-time views for queries

### Kappa Architecture

```
┌──────────┐    ┌────────────────────┐    ┌──────────────┐
│  Sources │───▶│  Message Queue     │───▶│  Stream      │
│          │    │  (Kafka)           │    │  Processing  │
└──────────┘    └────────────────────┘    └──────────────┘
```

- Single processing layer for both real-time and historical replay
- All data flows through a message queue (Kafka)
- Simpler than Lambda but requires robust stream processing

### Data Mesh Principles

1. **Domain Ownership**: Each business domain owns its data as a product
2. **Data as a Product**: Treated with same rigor as customer-facing products
3. **Self-Serve Platform**: Infrastructure teams provide tooling for domains
4. **Federated Governance**: Global standards with local autonomy

---

## Batch vs Real-Time Processing

### Batch Processing

- Processes large volumes of data at scheduled intervals
- Higher throughput, lower latency tolerance
- Ideal for historical analysis, reporting, aggregations

**Characteristics**:
- Data collected over a period, processed together
- Typically runs on schedules (hourly, daily)
- Easier to debug and reprocess
- Higher resource efficiency for large datasets

**Tools**: Apache Spark, MapReduce, Hive, Airflow, dbt

### Real-Time (Stream) Processing

- Processes data as it arrives, event by event
- Low latency (milliseconds to seconds)
- Ideal for fraud detection, monitoring, live dashboards

**Characteristics**:
- Continuous processing of unbounded data streams
- Event-time and processing-time considerations
- Stateful processing for aggregations over time windows
- Exactly-once semantics for correctness

**Tools**: Apache Flink, Kafka Streams, Spark Structured Streaming, Storm

### Comparison

| Aspect | Batch | Real-Time |
|--------|-------|-----------|
| Latency | Minutes to hours | Milliseconds to seconds |
| Throughput | Very high | Moderate |
| Complexity | Lower | Higher |
| Cost | Lower per GB | Higher |
| Use Cases | Reports, ML training | Monitoring, alerts |
| Fault Tolerance | Easier (reprocess) | Complex (state management) |
| Data Completeness | 100% complete | Approximate (windowing) |

### Hybrid Approaches

- **Micro-batching**: Small batch windows (e.g., Spark Streaming)
- **Near real-time**: Periodic processing with short intervals (e.g., every 5 minutes)
- **Lambda/Kappa**: Combine batch and streaming in unified architecture

---

## Data Modeling Basics

### Why Data Modeling Matters

- Organizes data for efficient querying
- Reduces redundancy and improves consistency
- Provides clear documentation of business concepts
- Enables scalable analytics

### Dimensional Modeling

**Fact Tables**:
- Contain measurements/metrics of business processes
- Foreign keys to dimension tables
- Examples: `fact_sales`, `fact_page_views`, `fact_transactions`

**Dimension Tables**:
- Contain descriptive attributes for filtering/grouping
- Examples: `dim_customer`, `dim_product`, `dim_date`, `dim_location`

### Star Schema

```
              ┌──────────────┐
              │  dim_customer │
              └──────┬───────┘
                     │
┌──────────────┐     │     ┌──────────────┐
│  dim_product │─────┼─────│  dim_date    │
└──────────────┘     │     └──────────────┘
                     │
              ┌──────┴───────┐
              │  fact_sales   │
              └──────────────┘
```

### Snowflake Schema

- Dimension tables are normalized into multiple related tables
- Reduces storage redundancy
- More complex joins

### Data Vault Modeling

- **Hubs**: Business keys (entities)
- **Links**: Relationships between hubs
- **Satellites**: Descriptive attributes with history
- Append-only, auditable, designed for parallel loading

### Normalization Forms

| Normal Form | Rule |
|-------------|------|
| 1NF | Atomic values, no repeating groups |
| 2NF | 1NF + no partial dependencies |
| 3NF | 2NF + no transitive dependencies |

### Modern Modeling Approaches

- **Wide Denormalized Tables**: Optimized for columnar query engines
- **Event Sourcing**: Store all state changes as a sequence of events
- **Activity Schema**: Versioned dimensional models tracking business activities
- **One Big Table (OBT)**: Pre-joined denormalized tables for fast analytics

---

## Data Pipeline Design

### Pipeline Patterns

**ETL (Extract, Transform, Load)**:
```
Source → Extract → Transform → Load → Target
```
- Transform before loading into the warehouse
- Better for data quality enforcement
- Traditional approach

**ELT (Extract, Load, Transform)**:
```
Source → Extract → Load → Transform → Target
```
- Load raw data first, transform in the warehouse
- Leverages warehouse compute for transformations
- Modern approach with cloud warehouses

### Pipeline Design Principles

1. **Idempotency**: Re-running a pipeline produces the same result
2. **Atomicity**: Each step either fully succeeds or fully fails
3. **Observability**: Logging, metrics, and alerting at every stage
4. **Modularity**: Components can be independently developed and tested
5. **Version Control**: Pipeline code is versioned and reviewed
6. **Backfill Support**: Ability to reprocess historical data

### Error Handling Strategies

- **Retry with Backoff**: Transient failures retry with increasing delays
- **Dead Letter Queue**: Failed records routed to a separate queue for investigation
- **Circuit Breaker**: Stop processing when error rate exceeds threshold
- **Graceful Degradation**: Continue with partial data when possible
- **Alerting**: Notify on-call engineers for critical failures

### Pipeline Orchestration

```python
# Example Airflow DAG
from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime

with DAG(
    'etl_pipeline',
    start_date=datetime(2024, 1, 1),
    schedule_interval='@daily',
    catchup=False
) as dag:

    extract = PythonOperator(
        task_id='extract',
        python_callable=extract_data
    )

    transform = PythonOperator(
        task_id='transform',
        python_callable=transform_data
    )

    load = PythonOperator(
        task_id='load',
        python_callable=load_data
    )

    extract >> transform >> load
```

### Testing Data Pipelines

- **Unit Tests**: Test individual transformation functions
- **Integration Tests**: Test end-to-end pipeline with test data
- **Data Quality Tests**: Validate schema, constraints, and business rules
- **Performance Tests**: Benchmark pipeline execution time and resource usage
- **Contract Tests**: Verify output schema matches expectations

---

## SLAs and Data Contracts

### Service Level Agreements (SLAs)

SLAs define the expected guarantees for data delivery:

- **Freshness**: Maximum acceptable delay for data availability
- **Completeness**: Minimum percentage of records that must be processed
- **Accuracy**: Tolerance for data quality issues
- **Availability**: Uptime guarantees for data access

### SLA Definition Example

```yaml
pipeline: daily_revenue_report
sla:
  freshness: "data available by 6:00 AM UTC"
  completeness: ">= 99.9% of records"
  accuracy: "revenue within 0.01% of source"
  availability: "99.5% uptime during business hours"
escalation:
  - level: "P2"
    response_time: "30 minutes"
  - level: "P1"
    response_time: "15 minutes"
```

### Data Contracts

Data contracts define the schema, semantics, and quality guarantees between data producers and consumers.

**Components**:
1. **Schema**: Field names, types, nullable, constraints
2. **Semantics**: Business meaning of each field
3. **Quality Metrics**: Expected distributions, value ranges
4. **SLAs**: Freshness, completeness, accuracy guarantees
5. **Ownership**: Who to contact for issues
6. **Versioning**: Schema evolution rules

### Data Contract Example

```yaml
contract: v2.1
dataset: customer_events
producer: platform-team
consumers:
  - analytics-team
  - ml-ops-team
schema:
  - name: event_id
    type: string
    nullable: false
    description: "Unique event identifier"
  - name: customer_id
    type: string
    nullable: false
    description: "Customer UUID"
  - name: event_type
    type: enum
    values: [click, view, purchase, add_to_cart]
    nullable: false
  - name: timestamp
    type: timestamp
    nullable: false
    description: "Event time in UTC"
  - name: properties
    type: map<string, string>
    nullable: true
    description: "Additional event properties"
quality:
  null_rate: "< 0.01%"
  uniqueness: "event_id is unique"
  timeliness: "events arrive within 5 minutes"
```

### Schema Evolution Strategies

- **Backward Compatible**: New fields have defaults, old consumers can read new data
- **Forward Compatible**: New consumers can read old data with missing fields
- **Full Compatible**: Both backward and forward compatible
- **Breaking Changes**: Require coordinated migration

### Monitoring and Alerting

Key metrics to monitor:
- Pipeline execution time vs SLA thresholds
- Data volume anomalies (sudden drops or spikes)
- Schema drift and contract violations
- Resource utilization (CPU, memory, storage)
- Error rates and retry counts
- Data freshness relative to expected schedules

---

## Key Takeaways

1. **Data engineering is the foundation** of any data-driven organization, providing reliable infrastructure for analytics and ML
2. **Understand the data lifecycle** from creation to deletion to make informed design decisions
3. **Choose the right architecture** (Lambda, Kappa, or Data Mesh) based on your organization's needs
4. **Batch vs real-time** is a spectrum, not a binary choice — hybrid approaches are common
5. **Data modeling** directly impacts query performance and maintainability
6. **Pipeline design** should prioritize idempotency, observability, and modularity
7. **SLAs and data contracts** establish clear expectations between producers and consumers
8. **Testing and monitoring** are not optional — they are essential for reliable data systems
9. **Documentation and lineage** prevent knowledge silos and enable self-service
10. **Continuous learning** is essential as the data engineering landscape evolves rapidly

---

## Further Reading

- *Fundamentals of Data Engineering* by Joe Reis and Matt Housley
- *Designing Data-Intensive Applications* by Martin Kleppmann
- *The Data Warehouse Toolkit* by Ralph Kimball
- *Data Mesh* by Zhamak Dehghani
- *Database Internals* by Alex Petrov
