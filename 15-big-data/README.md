# 15-Big-Data

Comprehensive guide to big data technologies, frameworks, and platforms for distributed computing, storage, and analytics.

## Table of Contents

### Hadoop Ecosystem
- [HDFS](hadoop/hdfs/README.md) - Hadoop Distributed File System
- [MapReduce](hadoop/mapreduce/README.md) - Distributed data processing
- [YARN](hadoop/yarn/README.md) - Yet Another Resource Negotiator
- [Hadoop Ecosystem](hadoop/ecosystem/README.md) - Ecosystem tools overview

### Apache Spark
- [RDD](spark/rdd/README.md) - Resilient Distributed Datasets
- [DataFrames](spark/dataframes/README.md) - Structured data processing
- [Streaming](spark/streaming/README.md) - Real-time data processing
- [MLlib](spark/mlib/README.md) - Machine learning library
- [GraphX](spark/graphx/README.md) - Graph processing
- [Performance](spark/performance/README.md) - Tuning and optimization

### Apache Hive
- [Fundamentals](hive/fundamentals/README.md) - Hive basics and HQL
- [Queries](hive/queries/README.md) - HQL query patterns
- [Optimization](hive/optimization/README.md) - Performance tuning
- [Schemas](hive/schemas/README.md) - Schema design patterns

### Apache Pig
- [Fundamentals](pig/fundamentals/README.md) - Pig Latin basics
- [UDF](pig/udf/README.md) - User-defined functions

### Apache HBase
- [Fundamentals](hbase/fundamentals/README.md) - Column-family NoSQL
- [Queries](hbase/queries/README.md) - Scan and filter patterns
- [Optimization](hbase/optimization/README.md) - Performance tuning
- [Schema](hbase/schema/README.md) - Table design patterns

### Presto/Trino
- [Fundamentals](presto/fundamentals/README.md) - Distributed SQL engine
- [Queries](presto/queries/README.md) - Query patterns and connectors
- [Optimization](presto/optimization/README.md) - Performance tuning

### Apache Druid
- [Fundamentals](druid/fundamentals/README.md) - Real-time analytics
- [Queries](druid/queries/README.md) - Druid SQL and aggregations
- [Indexing](druid/indexing/README.md) - Data ingestion

### Apache Pinot
- [Fundamentals](pinot/fundamentals/README.md) - Real-time OLAP
- [Queries](pinot/queries/README.md) - Query patterns
- [Indexing](pinot/indexing/README.md) - Segment management

### Apache Iceberg
- [Fundamentals](iceberg/fundamentals/README.md) - Open table format
- [Schema](iceberg/schema/README.md) - Schema evolution
- [Operations](iceberg/operations/README.md) - Snapshots and time travel

### Delta Lake
- [Fundamentals](delta-lake/fundamentals/README.md) - ACID transactions
- [Schema](delta-lake/schema/README.md) - Schema evolution
- [Operations](delta-lake/operations/README.md) - Merge, update, delete

### Apache Hudi
- [Fundamentals](hudi/fundamentals/README.md) - Incremental processing
- [Schema](hudi/schema/README.md) - Schema and types
- [Operations](hudi/operations/README.md) - COW and MOR tables

### Columnar Formats
- [Parquet](parquet/fundamentals/README.md) - Columnar storage format
- [Parquet Schema](parquet/schema/README.md) - Schema design
- [Parquet Optimization](parquet/optimization/README.md) - Performance tuning
- [Avro](avro/fundamentals/README.md) - Row-based serialization
- [Avro Schema](avro/schema/README.md) - Schema design
- [Avro Optimization](avro/optimization/README.md) - Performance tuning
- [ORC](orc/fundamentals/README.md) - Optimized Row Columnar
- [ORC Schema](orc/schema/README.md) - Schema and stripes
- [ORC Optimization](orc/optimization/README.md) - Performance tuning

### Data Format Comparison
- [Comparison](data-formats/comparison/README.md) - Format comparison
- [Selection Guide](data-formats/selection/README.md) - Choosing the right format

### Data Warehouses
- [Redshift](data-warehouse/redshift/README.md) - Amazon Redshift
- [BigQuery](data-warehouse/bigquery/README.md) - Google BigQuery
- [Snowflake](data-warehouse/snowflake/README.md) - Snowflake
- [Synapse](data-warehouse/synapse/README.md) - Azure Synapse Analytics
- [ClickHouse](data-warehouse/clickhouse/README.md) - ClickHouse

### ML Platforms
- [MLflow](ml-platforms/mlflow/README.md) - Experiment tracking
- [Kubeflow](ml-platforms/kubeflow/README.md) - ML on Kubernetes
- [Airflow for ML](ml-platforms/airflow-ml/README.md) - ML pipeline orchestration
- [Feature Store](ml-platforms/feature-store/README.md) - Feast and feature management

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Data Sources                             │
│  Logs · Events · Databases · APIs · Files · Streams            │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                      Ingestion Layer                            │
│  Kafka · Flume · Sqoop · NiFi · Flink · Spark Streaming       │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                     Storage Layer                               │
│  HDFS · S3 · GCS · ADLS · Iceberg · Delta Lake · Hudi         │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                   Processing Layer                              │
│  MapReduce · Spark · Flink · Tez · Beam · Storm                │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                     Query Layer                                 │
│  Hive · Presto · Impala · Drill · Druid · Pinot · ClickHouse   │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                   Serving Layer                                 │
│  dashboards · APIs · Applications · ML Models                  │
└─────────────────────────────────────────────────────────────────┘
```

## Key Concepts

### Data Lake vs Data Warehouse vs Data Lakehouse

| Feature | Data Lake | Data Warehouse | Data Lakehouse |
|---------|-----------|----------------|----------------|
| Data Type | Raw, unstructured | Structured | All types |
| Schema | Schema-on-read | Schema-on-write | Both |
| Cost | Low | High | Medium |
| ACID | No | Yes | Yes |
| Examples | S3 + Hadoop | Redshift, Snowflake | Delta Lake, Iceberg |

### CAP Theorem Trade-offs

- **CP Systems**: HBase, MongoDB (consistency + partition tolerance)
- **AP Systems**: Cassandra, DynamoDB (availability + partition tolerance)
- **CA Systems**: Traditional RDBMS (consistency + availability, no partition tolerance)

### Lambda vs Kappa Architecture

**Lambda Architecture**:
- Batch layer + Speed layer + Serving layer
- Handles both historical and real-time data
- Complex but comprehensive

**Kappa Architecture**:
- Stream processing only
- Simpler architecture
- Re-process by replaying streams

## Technology Selection Guide

| Use Case | Recommended Technology |
|----------|----------------------|
| Batch ETL | Spark, Hive, dbt |
| Real-time streaming | Kafka + Flink/Spark Streaming |
| Ad-hoc SQL analytics | Presto, Trino, BigQuery |
| Real-time dashboards | Druid, Pinot, ClickHouse |
| ML feature engineering | Spark, Feast |
| Time-series data | Druid, InfluxDB, TimescaleDB |
| Graph processing | GraphX, Neo4j, TigerGraph |
| Data lakehouse | Iceberg, Delta Lake, Hudi |
| Columnar storage | Parquet, ORC |
| Row serialization | Avro |
