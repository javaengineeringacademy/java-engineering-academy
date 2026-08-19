# 04. Memory Model - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Data race demonstration |
| `Solution2.java` | Thread-safe lazy initialization |
| `Solution3.java` | Lock-free stack with CAS |

## Running Solutions

```bash
javac Solution1.java && java Solution1
javac Solution2.java && java Solution2
javac Solution3.java && java Solution3
```

## Common Mistakes to Avoid

1. **Assuming volatile provides atomicity**: volatile only provides visibility, not atomicity
2. **Using double-checked locking without volatile**: Broken in Java 5+ without volatile
3. **Ignoring thread confinement**: Often simpler than shared-memory synchronization
4. **Forgetting final field guarantee**: Must publish safely for the guarantee to hold
