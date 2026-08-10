# Comparator Memory Behavior

## Memory Usage Patterns

### Comparator Object Memory
- **Stateless comparators**: Minimal memory overhead (just method pointer)
- **Stateful comparators**: Additional memory for captured state
- **Lambda comparators**: Capture variables from surrounding scope

### Memory Allocation Patterns
```java
// Stateless comparator - minimal memory
Comparator<String> comp = (a, b) -> a.compareTo(b);

// Stateful comparator - captures variable
int threshold = 10;
Comparator<String> comp = (a, b) -> {
    if (a.length() > threshold) return -1;
    return a.compareTo(b);
};
```

### Memory Footprint Analysis
- **Lambda comparators**: Capture variables in hidden fields
- **Anonymous classes**: Create new .class files in metaspace
- **Method references**: Most memory-efficient

### Cache Behavior
- **Comparator calls**: May cause cache misses during sorting
- **Data locality**: Poor comparator design can hurt cache performance
- **Branch prediction**: Complex comparators may cause branch mispredictions

### Memory-Efficient Patterns
1. **Use method references**: Most efficient memory usage
2. **Avoid capturing variables**: Keep comparators stateless
3. **Reuse comparators**: Don't create new comparators for each sort

### Memory Leaks to Avoid
1. **Capturing large objects**: Don't capture large objects in lambdas
2. **Static references**: Avoid static references in comparators
3. **Circular references**: Don't create circular references

### Performance vs Memory Trade-offs
- **Simpler comparators**: Less memory, faster execution
- **Complex comparators**: More memory, may be slower
- **Precomputed values**: Can trade memory for speed

### Example Memory Analysis
```java
// Memory usage for different comparator implementations
// Stateless lambda: ~16 bytes (method pointer + captured variables)
// Stateful lambda: ~32 bytes (includes captured variables)
// Anonymous class: ~48 bytes (includes class metadata)
// Method reference: ~8 bytes (just method pointer)
```

### Garbage Collection Considerations
- **Short-lived comparators**: Collect quickly in young generation
- **Long-lived comparators**: May survive to old generation
- **Captured variables**: Keep references to captured variables alive