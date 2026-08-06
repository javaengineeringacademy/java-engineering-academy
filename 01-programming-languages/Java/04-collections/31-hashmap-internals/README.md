# HashMap Load Factor Deep Dive

## Why Load Factor is 0.75

The load factor (LF) represents the trade-off between space (memory) and time (collisions). At LF = 0.75:

- **Poisson distribution optimization**: Average 0.5 collisions per bucket
- **Mathematical basis**: Chosen by Doug Lea based on analysis
- **Balanced approach**: Good performance without excessive memory waste

### Comparison

| Load Factor | Memory | Collisions | Resize Frequency |
|-------------|--------|------------|------------------|
| 0.5 | High (50% wasted) | Low | Frequent |
| 0.75 | Balanced | Low | Moderate |
| 1.0 | Low | High | Rare |

## Why Bucket Count is Power of 2

Power of 2 enables efficient modulo using bitwise AND:

```java
// Slow modulo
int index = hash % capacity;

// Fast bitwise AND (capacity is power of 2)
int index = hash & (capacity - 1);
```

- Bitwise AND is a single CPU instruction
- Modulo requires division (slow)
- Power of 2 ensures `capacity - 1` is all 1s in binary

## Why Treeification Threshold is 8

Based on Poisson distribution analysis at LF = 0.75:

- P(8 entries in bucket) ≈ 1 in 1 million
- P(9 entries in bucket) ≈ 1 in 10 million
- Treeification is extremely rare
- Most buckets have 0-1 entries

**Why not lower?** Trees have higher overhead; linked lists are faster for small N.

## Why Untreeification Threshold is 6

Hysteresis prevents oscillation:

- If both thresholds were 8, entries would oscillate between tree and list
- With gap (8 vs 6), stable performance under fluctuating loads
- Reduces expensive tree/list conversions

## Resizing Strategy

When `entries > capacity × loadFactor`:

1. Create new array with double capacity
2. Rehash all entries
3. Replace old array

**Cost**: O(n) time complexity for rehashing.

## Memory vs Performance Trade-offs

| Scenario | Recommendation |
|----------|----------------|
| Read-heavy | Use LF = 0.5 |
| Write-heavy | Use LF = 0.9 |
| Memory-constrained | Use LF = 0.9 |
| Latency-sensitive | Use LF = 0.5 |

## JDK Version Changes

| JDK Version | Changes |
|-------------|---------|
| JDK 1.2 | HashMap introduced, LF = 0.75 |
| JDK 7 | Improved hash function |
| JDK 8 | Added treeification (threshold 8/6) |
| JDK 9 | Factory methods (Map.of()) |
| JDK 17 | No structural changes |
| JDK 21 | HashMap unchanged |

## Key Takeaways

1. Load factor 0.75 is mathematically optimal
2. Power of 2 enables fast bitwise modulo
3. Treeify threshold 8 based on Poisson distribution
4. Untreeify threshold 6 prevents oscillation
5. Set initial capacity to avoid resizing
