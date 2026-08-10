# Enhanced For Loop Memory Behavior

## Memory Characteristics

### Iterator Creation
- Each enhanced for loop implicitly creates an Iterator object
- Iterator holds reference to the collection
- Extra heap allocation compared to indexed for loop

### No Index Variable
- No explicit index variable on stack
- Cleaner code but less control

## Implicit Iterator Pattern

```java
// This:
for (String s : list) {
    System.out.println(s);
}

// Is equivalent to:
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    System.out.println(s);
}
```

## Memory Overhead

| Operation | Memory Impact |
|-----------|---------------|
| Iterator creation | ~16-32 bytes per loop |
| hasNext/next | Minimal overhead |
| ConcurrentModificationException | Thrown if collection modified |

## Best Practices

1. Use enhanced for for simple read-only iteration
2. Don't modify collection during enhanced for
3. Use Iterator explicitly when you need remove()
4. Consider memory overhead for tight loops
