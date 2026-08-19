# 10. JVM Tuning - Examples

## Example Files

| File | Description |
|------|-------------|
| *(See practices/ and solutions/ for runnable examples)* | JVM tuning exercises and benchmarks |

## Running Examples

```bash
# Baseline measurement
java -Xms512m -Xmx512m Exercise1

# G1 tuning
java -XX:+UseG1GC -XX:MaxGCPauseMillis=100 Exercise1

# ZGC tuning
java -XX:+UseZGC -XX:SoftMaxHeapSize=4g Exercise1
```

## Key Concepts Demonstrated

- Heap sizing effects on GC behavior
- GC algorithm comparison
- Tuning methodology and measurement
- Container-aware JVM configuration
