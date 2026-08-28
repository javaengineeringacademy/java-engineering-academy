# Records Memory

## Memory Characteristics

### Immutability Guarantee
Records are immutable - all components are final. This means:
- No race conditions on component access
- Safe to share across threads
- Can be used as map keys (once hashCode is computed)

### Memory Efficiency

| Aspect | Record | Traditional Class |
|--------|--------|-------------------|
| Fields | Final, no padding needed | May need padding |
| Access | Direct field access via method | Getter overhead |
| GC | Same as regular objects | Same |
| Serialization | Canonical constructor | May be complex |

### Compact Constructor Memory

Compact constructors don't allocate new objects - they validate and return:
```java
public record Range(int start, int end) {
    public Range {
        if (start > end) throw new IllegalArgumentException();
    }
}
```

### Thread Safety

Records are inherently thread-safe because:
1. All fields are final
2. Final fields are guaranteed to be visible after construction
3. No synchronization needed for reads

## Best Practices for Memory

1. **Use small records** - Avoid records with many components
2. **Consider primitive vs boxed** - Use `int` not `Integer` when possible
3. **Cache computed values** - Use private fields for expensive computations
4. **Avoid large arrays** - Records with large arrays may have performance implications
