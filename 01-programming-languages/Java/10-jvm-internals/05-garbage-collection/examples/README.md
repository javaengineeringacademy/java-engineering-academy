# 05. Garbage Collection - Examples

## Example Files

| File | Description |
|------|-------------|
| `GCDemo.java` | Basic GC demonstration showing object lifecycle and memory usage |
| `AllCollectors.java` | Comparison of different GC algorithms with timing |
| `GcTuningDeepDive.java` | Deep dive into GC tuning with different parameters |

## Running Examples

```bash
# Basic GC demo
java -Xlog:gc* GCDemo

# Compare collectors
java -XX:+UseSerialGC AllCollectors
java -XX:+UseParallelGC AllCollectors
java -XX:+UseG1GC AllCollectors

# GC tuning
java -Xms512m -Xmx512m -XX:+UseG1GC -Xlog:gc* GcTuningDeepDive
```

## Key Concepts Demonstrated

- Object allocation and garbage collection lifecycle
- Minor GC vs Major GC behavior
- Different collector characteristics
- GC pause time measurement
- Heap size effects on GC frequency
