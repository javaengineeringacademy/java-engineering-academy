# 05. Garbage Collection - Complete Coverage

## Introduction

Garbage Collection (GC) is one of the most important features of the Java Virtual Machine. It automatically manages memory by identifying and reclaiming objects that are no longer in use, eliminating the need for manual memory management.

## Complete GC Comparison Table

| Feature | Serial | Parallel | CMS | G1 | ZGC | Shenandoah | Epsilon |
|---------|--------|----------|-----|-----|-----|------------|---------|
| **Type** | Stop-the-world | Stop-the-world | Concurrent | Concurrent | Concurrent | Concurrent | No-op |
| **Threads** | Single | Multi | Multi | Multi | Multi | Multi | None |
| **Pause Target** | >500ms | >200ms | <200ms | <200ms | <10ms | <10ms | N/A |
| **Heap Size** | <256MB | 256MB-4GB | 256MB-4GB | 4GB-16GB | 16GB-16TB | 4GB-16GB | Any |
| **Java Version** | 1.3+ | 1.3+ | 1.4+ | 9+ | 11+ | 13+ | 11+ |
| **Compaction** | Yes | Yes | No | Yes | Yes | Yes | No |
| **Best For** | Small apps | Throughput | Low latency | Balanced | Ultra-low lat. | Low latency | Testing |

## When to Use Each Collector

### Serial GC
- Small applications (< 100MB)
- Single CPU machines
- Client-side applications
- Development/testing environments

### Parallel GC
- Batch processing
- Scientific computing
- Throughput-focused applications
- Background data processing

### CMS (Deprecated)
- Latency-sensitive applications (pre-Java 14)
- Web servers (pre-Java 14)
- Replaced by G1, ZGC, or Shenandoah

### G1 GC (Default)
- Balanced latency and throughput
- Large heaps (4GB - 16GB)
- Web applications
- Applications requiring predictable pauses

### ZGC
- Ultra-low latency applications
- Large heaps (16GB - 16TB)
- Real-time systems
- Financial trading platforms

### Shenandoah
- Latency-sensitive applications
- Large heaps
- Applications requiring consistent pause times

### Epsilon
- Performance testing
- Short-lived applications
- Applications with bounded memory
- Latency-sensitive workloads with bounded memory

## GC Configuration

```bash
# Serial GC
java -XX:+UseSerialGC MyApp

# Parallel GC
java -XX:+UseParallelGC MyApp

# CMS (deprecated)
java -XX:+UseConcMarkSweepGC MyApp

# G1 GC (default)
java -XX:+UseG1GC MyApp

# ZGC
java -XX:+UseZGC MyApp

# Shenandoah
java -XX:+UseShenandoahGC MyApp

# Epsilon
java -XX:+UseEpsilonGC MyApp
```

## Tuning Methodology

1. **Set clear performance goals**
   - Max pause time target
   - Throughput target (e.g., 95%)
   - Memory footprint target

2. **Enable GC logging**
   ```bash
   java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApp
   ```

3. **Run representative workload**
   - Simulate production traffic
   - Run for sufficient time (hours)
   - Include warm-up period

4. **Analyze GC logs**
   - Check pause times
   - Check throughput
   - Check memory usage

5. **Tune parameters iteratively**
   - Change ONE parameter at a time
   - Measure impact
   - Repeat until goals are met

## Common GC Issues

### Long GC Pauses
- **Symptom**: Application unresponsive
- **Solution**: Tune GC parameters, increase heap size

### Memory Leaks
- **Symptom**: Memory keeps growing
- **Solution**: Find and fix leak, analyze heap dump

### Frequent GC
- **Symptom**: High CPU usage
- **Solution**: Increase heap size, reduce allocation rate

### Promotion Failure
- **Symptom**: Full GC too often
- **Solution**: Increase old gen size, reduce promotion rate

## GC Log Analysis Tools

- **GCEasy**: Online GC log analyzer (https://gceasy.io)
- **GCViewer**: Desktop application (https://github.com/chewiebug/GCViewer)
- **JClarity Censum**: Commercial (advanced analysis)

## Best Practices

1. **Right-Size the Heap**: Match heap to application needs
2. **Choose Appropriate GC**: Based on latency/throughput requirements
3. **Monitor GC Activity**: Enable GC logging in production
4. **Optimize Object Creation**: Reuse objects, minimize temporary objects
5. **Prevent Memory Leaks**: Close resources, use weak references

## Interview Questions

1. **What is the difference between Serial and Parallel GC?** - Serial is single-threaded, Parallel is multi-threaded
2. **What is G1 GC?** - Region-based GC with predictable pause times
3. **What is ZGC?** - Ultra-low latency GC using load barriers
4. **When would you use Epsilon GC?** - Performance testing only

## References

- [Garbage Collection Tuning](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/vm/gctuning/index.html)
- "Java Performance" by Scott Oaks
- "The Garbage Collection Handbook" by Richard Jones
