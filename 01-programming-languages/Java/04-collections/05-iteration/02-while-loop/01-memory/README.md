# While Loop Memory Behavior

## Memory Characteristics

### Iterator Reference
- Explicit Iterator object on heap
- More control over iteration state
- Can be reused across iterations

### Stack Usage
- While condition check on each iteration
- No automatic variable like for loop
- Slightly more stack frames in some JVMs

## Common Patterns

```java
// While loop with Iterator
Iterator<T> it = list.iterator();
while (it.hasNext()) {
    T item = it.next();
    // process
}

// While loop with condition
while (!list.isEmpty()) {
    T item = list.remove(0);
    // process
}
```

## Memory Considerations

| Pattern | Memory Impact |
|---------|---------------|
| Iterator while loop | Iterator object + condition check |
| Condition-based while | May create garbage if removing |
| Nested while loops | Multiple iterators on heap |

## Best Practices

1. Use while loop for conditional iteration
2. Prefer Iterator.remove() over Collection.remove()
3. Consider break/continue for complex conditions
4. Avoid infinite loops - always update condition
