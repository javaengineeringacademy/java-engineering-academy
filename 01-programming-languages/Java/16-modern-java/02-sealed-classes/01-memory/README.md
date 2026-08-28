# Sealed Classes Memory

## Memory Characteristics

### Object Layout
Sealed classes have the same memory layout as regular classes. The sealed nature is metadata only.

### Type Information
The JVM stores the permitted subclasses list, but this doesn't affect instance memory.

### instanceof Optimization
The JVM can optimize `instanceof` checks for sealed hierarchies because it knows all possible types.

### Performance Considerations

| Operation | Impact |
|-----------|--------|
| instanceof | Potentially faster with sealed |
| Pattern matching | Compiler can optimize |
| Memory usage | Same as regular classes |
| GC behavior | Same as regular classes |

## Thread Safety

Sealed classes don't add thread safety. Use records or synchronization for thread-safe sealed hierarchies.

## Best Practices

1. **Use records for leaf nodes** - They're already immutable
2. **Keep hierarchies small** - Large hierarchies may impact pattern matching performance
3. **Consider sealed interfaces** - They're often more flexible than sealed classes
