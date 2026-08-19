# 05. Garbage Collection - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | GC observation with different collectors |
| `Solution2.java` | Memory leak identification and fix |
| `Solution3.java` | GC parameter tuning |

## Running Solutions

```bash
java -Xlog:gc* Solution1
java -Xms256m -Xmx256m Solution2
java -XX:+UseG1GC -XX:MaxGCPauseMillis=100 Solution3
```

## Common Mistakes to Avoid

1. **Calling System.gc() expecting immediate collection**: It is only a hint; JVM may ignore it
2. **Setting heap too large**: Larger heap = longer GC pauses
3. **Ignoring promotion failures**: Monitor for "to-space exhausted" in GC logs
4. **Using deprecated GC flags**: Use -Xlog:gc* instead of -XX:+PrintGCDetails
