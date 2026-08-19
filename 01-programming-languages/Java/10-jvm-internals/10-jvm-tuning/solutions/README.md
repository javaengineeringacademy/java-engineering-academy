# 10. JVM Tuning - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Baseline performance measurement |
| `Solution2.java` | GC latency tuning |
| `Solution3.java` | Container JVM optimization |

## Running Solutions

```bash
java -Xms512m -Xmx512m Solution1
java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 Solution2
java -XX:MaxRAMPercentage=75.0 Solution3
```

## Common Mistakes to Avoid

1. **Not measuring before tuning**: Always establish a baseline first
2. **Changing too many flags at once**: Change one parameter at a time
3. **Ignoring non-heap memory**: Metaspace and Code Cache also matter
4. **Setting heap too large for containers**: Causes OOM kills
