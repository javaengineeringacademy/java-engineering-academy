# Apache Parquet Fundamentals

## Overview
Parquet is a columnar storage file format optimized for analytics workloads with efficient compression and encoding.

## Key Features
- Columnar storage
- Efficient compression
- Schema evolution
- Predicate pushdown
- Column pruning
- Nested data support

## File Structure
```
Parquet File:
  Row Group 1 (default 128MB):
    Column Chunk: id (statistics, encoding)
    Column Chunk: name (statistics, encoding)
    Column Chunk: value (statistics, encoding)
  Row Group 2:
    ...
  Footer (metadata)
```

## Writing Parquet
```python
# PySpark
df.write.parquet("/output/events", mode="overwrite")

# With options
df.write.parquet("/output/events",
    compression="snappy",
    rowGroupSize=134217728,  # 128MB
    parquetBlockSize=134217728
)

# Java
ParquetWriter<GenericData.Record> writer = ParquetWriter
    .builder(new Path("/output/events"))
    .withSchema(schema)
    .withCompressionCodec(CompressionCodecName.SNAPPY)
    .build();
```

## Reading Parquet
```python
# PySpark
df = spark.read.parquet("/output/events")

# With predicate pushdown
df = spark.read.parquet("/output/events").filter("year = 2024")

# Column pruning
df = spark.read.parquet("/output/events").select("id", "name")

# Java
ParquetReader<GenericData.Record> reader = ParquetReader
    .builder(new GenericRecordReadFunction(schema), new Path("/output/events"))
    .build();
```

## Compression
| Codec | Speed | Compression | Use Case |
|-------|-------|-------------|----------|
| Snappy | Fast | Medium | Default, balanced |
| GZIP | Slow | High | Cold storage |
| LZ4 | Fastest | Low | Hot data |
| ZSTD | Medium | High | Balanced high compression |

## Best Practices
1. Use Snappy for balanced performance
2. Set row group size based on workload
3. Enable dictionary encoding for low cardinality
4. Use predicate pushdown in queries
5. Monitor file count and size distribution
