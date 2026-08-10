# For Loop Memory Behavior

## Memory Characteristics

### Index Variable
- `int i` is stored on the stack
- No heap allocation for the loop control variable
- Very low memory overhead

### ArrayList Access
- `list.get(i)` - O(1) random access via array index
- No iterator object created
- Memory efficient for indexed traversal

### LinkedList Access
- `list.get(i)` - O(n) traversal from head/tail
- Creates temporary node references
- Memory overhead from pointer chasing

## Comparison with Other Iteration Types

| Feature | For Loop | Enhanced For | Iterator |
|---------|----------|--------------|----------|
| Stack usage | 1 int variable | Iterator object reference | Iterator object reference |
| Heap usage | None | Iterator object | Iterator object |
| Access pattern | Random | Sequential | Sequential |

## Best Practices

1. Use for loop when you need the index
2. Use for loop for reverse iteration
3. Avoid for loop on LinkedList (use Iterator instead)
4. Consider ArrayList for indexed access patterns
