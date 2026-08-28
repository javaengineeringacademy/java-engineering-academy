# Switch Expressions Memory

## Memory Characteristics

### Stack-Based Execution
Switch expressions execute on the stack, just like traditional switch:
- No additional heap allocation
- Fast execution
- No GC impact

### String Interning
String switch expressions benefit from string interning:
- Repeated strings share the same object
- Reduces memory usage
- Faster comparisons

### Pattern Matching
Pattern matching in switch doesn't create additional objects:
- Pattern variables are stack-local
- No heap allocation for matched objects
- Same performance as instanceof checks

## Performance Considerations

| Operation | Impact |
|-----------|--------|
| Integer switch | Very fast (tableswitch) |
| String switch | Fast (interning + hash) |
| Pattern matching | Same as instanceof |
| Yield | Stack-based return |

## Best Practices

1. **Use integer/enum switches** - They're fastest
2. **Consider string interning** - For repeated string switches
3. **Keep switch bodies small** - Complex logic may impact performance
4. **Use pattern matching wisely** - For type-based dispatch
