# Redpanda Performance

## Performance Tuning, Benchmarking, and Optimization

---

## Table of Contents

- [Overview](#overview)
- [Performance Characteristics](#performance-characteristics)
- [Tuning Guidelines](#tuning-guidelines)
- [Benchmarking](#benchmarking)
- [Optimization](#optimization)
- [Best Practices](#best-practices)

---

## Overview

Redpanda provides superior performance compared to Kafka due to its C++ implementation and thread-per-core architecture. This guide covers performance tuning and optimization.

### Performance Advantages

| Advantage | Description |
|-----------|-------------|
| No JVM | No garbage collection pauses |
| Thread-per-Core | Efficient CPU utilization |
| Zero-Copy | Direct memory access |
| Sequential I/O | Optimized disk access |
| Adaptive batching | Dynamic batching |

---

## Performance Characteristics

### Latency

```
Latency Comparison:
┌─────────────────────────────────────────────────────────────┐
│                    Latency (ms)                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Kafka:    ████████████████████ 5-10ms                      │
│                                                              │
│  Redpanda: ████████ 1-3ms                                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Redpanda achieves lower latency due to:
- No GC pauses
- Thread-per-core model
- Efficient I/O
```

### Throughput

```
Throughput Comparison:
┌─────────────────────────────────────────────────────────────┐
│                    Throughput (MB/s)                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Kafka:    ████████████████████ 500 MB/s                    │
│                                                              │
│  Redpanda: ████████████████████████████ 800 MB/s            │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Redpanda achieves higher throughput due to:
- No JVM overhead
- Efficient batching
- Zero-copy transfers
```

---

## Tuning Guidelines

### CPU Configuration

```yaml
# redpanda.yaml
redpanda:
  # Allocate all CPU cores
  reactor_threads: 4
  
  # Pin threads to cores
  thread_affinity: true
  
  # Use huge pages
  use_huge_pages: true
```

### Memory Configuration

```yaml
redpanda:
  # Memory allocation
  memory: 4G
  
  # Segment cache
  segment_cache_capacity: 256M
  
  # Read-ahead cache
  read_ahead_cache: 64M
```

### Disk Configuration

```yaml
redpanda:
  # Data directory
  data_directory: /var/lib/redpanda/data
  
  # Segment size
  log_segment_size: 134217728  # 128MB
  
  # Compaction
  log_compaction_interval: 300000  # 5 minutes
```

### Network Configuration

```yaml
redpanda:
  # Kafka API
  kafka_api:
    - address: 0.0.0.0
      port: 9092
  
  # RPC
  rpc_server:
    address: 0.0.0.0
    port: 33145
  
  # Max connections
  max_connections: 10000
```

---

## Benchmarking

### rpk Topic Produce

```bash
# Benchmark produce
rpk topic produce \
  --topic benchmark \
  --num 1000000 \
  --size 1024 \
  --parallel 4

# Benchmark with key
rpk topic produce \
  --topic benchmark \
  --num 1000000 \
  --size 1024 \
  --key "key-%p"
```

### rpk Topic Consume

```bash
# Benchmark consume
rpk topic consume \
  --topic benchmark \
  --num 1000000 \
  --parallel 4

# Benchmark with offset
rpk topic consume \
  --topic benchmark \
  --offset 0 \
  --num 1000000
```

### Kafka Performance Tools

```bash
# Kafka producer performance test
kafka-producer-perf-test.sh \
  --topic benchmark \
  --num-records 1000000 \
  --record-size 1024 \
  --throughput -1 \
  --producer-props bootstrap.servers=localhost:9092

# Kafka consumer performance test
kafka-consumer-perf-test.sh \
  --topic benchmark \
  --messages 1000000 \
  --broker-list localhost:9092
```

### Benchmark Results

```
Benchmark Results (3-node cluster):
┌─────────────────────────────────────────────────────────────┐
│                    Performance Metrics                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Produce Throughput: 800 MB/s                                │
│  Consume Throughput: 1200 MB/s                               │
│  P99 Latency: 2ms                                            │
│  CPU Utilization: 85%                                        │
│  Memory Usage: 2GB                                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Optimization

### Batch Optimization

```yaml
redpanda:
  # Batch settings
  batch_size: 16384  # 16KB
  linger_ms: 5       # 5ms
  
  # Adaptive batching
  adaptive_batching: true
```

### Compression Optimization

```yaml
redpanda:
  # Compression
  compression: lz4
  
  # Topic-level compression
  default_topic_compression: lz4
```

### Memory Optimization

```yaml
redpanda:
  # Memory allocation
  memory: 8G
  
  # Cache settings
  segment_cache_capacity: 512M
  
  # Reduce memory usage
  write_caching: true
```

### I/O Optimization

```yaml
redpanda:
  # Use direct I/O
  direct_io: true
  
  # Async I/O
  async_io: true
  
  # Read-ahead
  read_ahead: 64K
```

---

## Best Practices

### Hardware

1. **Use NVMe SSDs** - For low latency
2. **Dedicate CPU cores** - Don't share with other services
3. **Use fast network** - 10GbE or faster
4. **Allocate sufficient memory** - 4GB minimum

### Configuration

1. **Tune for workload** - Adjust batch size, compression
2. **Monitor metrics** - Track CPU, memory, disk
3. **Use adaptive batching** - Dynamic batch sizes
4. **Enable write caching** - For better throughput

### Operations

1. **Use rpk CLI** - For management
2. **Monitor health** - Track cluster status
3. **Plan capacity** - Scale as needed
4. **Test failover** - Verify recovery

### Benchmarking

1. **Benchmark before changes** - Establish baseline
2. **Test realistic workloads** - Use production patterns
3. **Monitor during benchmark** - Track resource usage
4. **Document results** - Maintain benchmark history

---

## Further Reading

- [Redpanda Performance](https://docs.redpanda.com/docs/reference/tuning/)
- [Redpanda Benchmarking](https://docs.redpanda.com/docs/reference/rpk/)
- [Redpanda Configuration](https://docs.redpanda.com/docs/reference/configuration/)
