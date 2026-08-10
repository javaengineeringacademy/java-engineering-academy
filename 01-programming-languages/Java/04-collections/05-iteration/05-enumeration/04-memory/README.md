# Enumeration Memory Behavior

## Memory Characteristics

### Enumeration Object
- Lightweight interface implementation
- No iterator object created
- Direct access to Vector internals

### Thread Safety
- Synchronized access for Vector
- Enumeration is not thread-safe itself
- Better for single-threaded scenarios

## Enumeration vs Iterator

| Feature | Enumeration | Iterator |
|---------|-------------|----------|
| Memory | Minimal | Iterator object |
| Remove | Not supported | remove() method |
| Thread Safety | Vector synchronized | Not synchronized |
| Legacy | Java 1.0 | Java 1.2+ |

## Memory Patterns

```java
// Enumeration usage
Enumeration<T> en = vector.elements();
while (en.hasMoreElements()) {
    T item = en.nextElement();
}

// Convert to Iterator for remove
Iterator<T> it = vector.iterator();
while (it.hasNext()) {
    if (condition) it.remove();
}
```

## Best Practices

1. Use Enumeration for legacy Vector/Hashtable
2. Prefer Iterator for modern collections
3. Consider Vector synchronization overhead
4. Use Collections.synchronizedList for thread safety
