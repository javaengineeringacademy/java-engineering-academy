# Synchronization Decision Guide

## synchronized vs volatile

| Feature | synchronized | volatile |
|---------|-------------|----------|
| Atomicity | Yes | No |
| Visibility | Yes | Yes |
| Mutual exclusion | Yes | No |
| Blocking | Yes | No |
| Use for | Compound operations | Simple read/write flags |

## When to Use Each

| Situation | Solution |
|-----------|----------|
| Incrementing a counter | `synchronized` or `AtomicInteger` |
| Boolean flag for stop signal | `volatile` |
| Check-then-act pattern | `synchronized` |
| Simple value publishing | `volatile` |
| Complex state updates | `synchronized` or `Lock` |

## Deadlock Prevention

1. **Lock ordering**: Always acquire locks in same global order
2. **Timeout**: Use `tryLock()` with timeout
3. **Lock downgrading**: Acquire write lock, then read, then release write
4. **Avoid nested locks**: Minimize lock scope
