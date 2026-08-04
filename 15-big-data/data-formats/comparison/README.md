# Data Format Comparison

## Overview
Comparison of major columnar and row-based data formats for big data workloads.

## Format Overview

| Format | Type | Compression | Schema Evolution | Best For |
|--------|------|-------------|------------------|----------|
| Parquet | Columnar | Excellent | Yes | Spark, Athena, BigQuery |
| ORC | Columnar | Excellent | Yes | Hive, Presto |
| Avro | Row | Good | Yes | Kafka, streaming |
| JSON | Row | Poor | N/A | APIs, config |
| CSV | Row | Poor | N/A | Data exchange |

## Performance Comparison

| Metric | Parquet | ORC | Avro | CSV |
|--------|---------|-----|------|-----|
| File Size | Small | Smallest | Medium | Large |
| Read Speed | Fast | Fastest | Medium | Slow |
| Write Speed | Medium | Medium | Fast | Fast |
| Compression | Excellent | Excellent | Good | Poor |
| Predicate Pushdown | Yes | Yes | No | No |

## When to Use What

### Parquet
- Spark workloads
- Cloud data lakes (S3, GCS)
- Column-oriented queries
- Athena, BigQuery, Redshift Spectrum

### ORC
- Hive workloads
- ACID transactions needed
- Best compression ratio
- Presto/Trino

### Avro
- Kafka message serialization
- Event streaming
- Schema evolution critical
- Row-oriented processing

### JSON
- API responses
- Configuration files
- Semi-structured data
- Low-volume data

## Hybrid Approaches
```
Kafka -> Avro (streaming) -> Parquet (lake) -> ORC (Hive warehouse)
```

## Migration Strategies
1. Use schema registry for Avro
2. Test schema evolution carefully
3. Benchmark with real data
4. Consider query patterns
5. Plan for data backfill
