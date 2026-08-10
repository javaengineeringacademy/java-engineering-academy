# Transforming Operations Memory Usage

## Per-Operation Overhead

```
Transforming operation creates:
- Stream object: ~16 bytes
- Pipeline stage: ~32 bytes
- Total overhead: ~48 bytes per operation
```

## Memory During Processing

```java
list.stream()
    .map(...)  // +48 bytes (stream + pipeline)
    .collect(...);   // +result collection
```

## Scaling with Input Size

| Input Size | Stream Overhead | Result Size | Total |
|------------|-----------------|-------------|-------|
| 100 items | 48 bytes | ~400 bytes | ~448 bytes |
| 1,000 items | 48 bytes | ~4,000 bytes | ~4,048 bytes |
| 10,000 items | 48 bytes | ~40,000 bytes | ~40,048 bytes |

## Comparison: Loop vs Stream

```
Loop approach:
- No stream overhead
- Direct processing
- Manual memory management

Stream approach:
- Stream object: 16 bytes
- Pipeline stage: 32 bytes
- Lambda capture: varies
- Total: ~48+ bytes overhead
```

## Memory Layout

```
Original List:
┌────┬────┬────┬────┬────┐
│ 1  │ 2  │ 3  │ 4  │ 5  │
└────┴────┴────┴────┴────┘

Stream Pipeline:
┌─────────────────────────┐
│ Stream object (16B)     │
│ Pipeline stage (32B)    │
│ Lambda reference (8B)   │
└─────────────────────────┘

Result Collection:
┌────┬────┬────┐
│ 2  │ 4  │ 6  │
└────┴────┴────┘
```

## Optimization Tips

1. **Reuse streams** when possible
2. **Avoid boxing/unboxing** with primitive streams
3. **Use parallel streams** for large datasets
4. **Consider memory** for very large streams
