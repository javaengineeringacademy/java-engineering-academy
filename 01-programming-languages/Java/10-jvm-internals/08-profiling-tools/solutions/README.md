# 08. Profiling Tools - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | CPU hotspot profiling |
| `Solution2.java` | Memory allocation profiling |
| `Solution3.java` | Thread contention analysis |

## Running Solutions

```bash
java Solution1
java Solution2
java -XX:StartFlightRecording=duration=30s,filename=recording.jfr Solution3
```

## Common Mistakes to Avoid

1. **Profiling without warm-up**: First results include JIT compilation time
2. **Using instrumentation in production**: Use sampling for production (lower overhead)
3. **Ignoring JIT effects**: Compiled code looks different from source
4. **Not enough data**: Profile for sufficient time to get statistically significant results
