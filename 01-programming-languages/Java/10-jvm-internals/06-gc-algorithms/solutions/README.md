# 06. GC Algorithms - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | GC algorithm comparison |
| `Solution2.java` | G1 parameter tuning |
| `Solution3.java` | GC log analysis and optimization |

## Running Solutions

```bash
java -XX:+UseG1GC -Xlog:gc* Solution1
java -XX:+UseZGC -Xlog:gc* Solution1
java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 Solution2
```

## Common Mistakes to Avoid

1. **Not enabling GC logging**: Cannot tune without data
2. **Changing too many parameters at once**: Change one at a time
3. **Ignoring humongous objects in G1**: Can cause long pauses
4. **Assuming ZGC has no overhead**: Higher memory cost than G1
