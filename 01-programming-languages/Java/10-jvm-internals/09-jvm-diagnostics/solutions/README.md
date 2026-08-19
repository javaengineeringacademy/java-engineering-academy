# 09. JVM Diagnostics - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Thread dump capture and analysis |
| `Solution2.java` | Heap dump generation and analysis |
| `Solution3.java` | Automated diagnostic collection |

## Running Solutions

```bash
java Solution1 &
jstack <pid>

java -XX:+HeapDumpOnOutOfMemoryError Solution2

java Solution3
```

## Common Mistakes to Avoid

1. **Analyzing thread dumps without looking for BLOCKED threads**: BLOCKED indicates contention
2. **Not using -XX:+HeapDumpOnOutOfMemoryError**: Missing the chance to capture OOM dumps
3. **Ignoring jstat output**: GC statistics reveal memory issues early
4. **Using jmap in production**: Prefer jcmd for reliability
