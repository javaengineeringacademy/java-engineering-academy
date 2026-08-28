# Text Blocks Memory

## Memory Characteristics

### String Pool
Text blocks are String objects and follow the same interning rules:
- Compile-time constants are interned
- Dynamic text blocks create new String objects

### Memory Usage
Text blocks have the same memory usage as equivalent concatenated strings:
- Same character array size
- Same object overhead
- Same GC behavior

### Indentation Handling
The compiler strips leading whitespace during compilation, not at runtime.

## Performance Considerations

| Operation | Impact |
|-----------|--------|
| Creation | Same as concatenated strings |
| Concatenation | Same as regular strings |
| Interning | Same as regular strings |
| GC | Same as regular strings |

## Best Practices

1. **Use text blocks for readability** - No performance penalty
2. **Consider string pool** - Large text blocks may not be interned
3. **Use StringBuilder for dynamic content** - When building strings at runtime
4. **Cache large text blocks** - If used frequently
