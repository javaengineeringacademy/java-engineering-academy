# var Type Inference Memory

## Memory Characteristics

### Stack-Based Variables
var variables are stack-allocated, just like explicit type declarations:
- No heap allocation
- Fast access
- Automatic cleanup when scope ends

### Type Erasure
The compiler erases the inferred type at compile time:
- Same bytecode as explicit declaration
- No runtime type information
- Same performance

### Effectively Final
var variables are effectively final:
- Cannot be reassigned
- Safe for lambda capture
- Thread-safe by design

## Performance Considerations

| Operation | Impact |
|-----------|--------|
| Declaration | Same as explicit type |
| Access | Same as explicit type |
| Assignment | Same as explicit type |
| GC | Same as explicit type |

## Best Practices

1. **Use var for readability** - When type is obvious
2. **Use var for complex types** - When explicit type would be verbose
3. **Avoid var for unclear types** - When type is not obvious
4. **Don't use var for fields** - Only for local variables
