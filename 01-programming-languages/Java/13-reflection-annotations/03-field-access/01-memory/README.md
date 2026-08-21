# Memory: Field Access

## Memory Cost of Field Objects

Each `Field` object contains:
- Object header: 16 bytes
- Name (String reference): 8 bytes
- Type (Class reference): 8 bytes
- Modifiers (int): 4 bytes
- Declaring class reference: 8 bytes
- Generic type data: 8 bytes (may be null)
- Annotation data: 8 bytes (may be null)

**Approximate total: 60-80 bytes per Field object**

### Array Overhead

When you call `getDeclaredFields()`, a new array is created:
- Array header: 16 bytes
- Per element: 8 bytes (reference)
- For n fields: 16 + 8n bytes

## Performance Impact

### Hot Loop Field Access

Accessing fields in a tight loop without caching:

```java
// SLOW: 50-100ns per iteration
for (Object obj : objects) {
    Field f = clazz.getDeclaredField("value");
    f.setAccessible(true);
    process(f.get(obj));
}

// FAST: 10-20ns per iteration
Field f = clazz.getDeclaredField("value");
f.setAccessible(true);
for (Object obj : objects) {
    process(f.get(obj));
}
```

The difference comes from:
1. Field lookup overhead (name resolution)
2. Security check overhead (setAccessible)
3. Both are O(n) in the loop vs O(1) outside

### setAccessible Cost

The `setAccessible(true)` call performs:
1. Security manager check
2. Module access check
3. Sets an internal flag

This costs approximately 20-40ns per call.

## Memory Leaks

Holding Field objects prevents garbage collection of the declaring class and its classloader. In plugin architectures, this can prevent classloader unloading.

**Mitigation:** Use WeakReference for Field caches.
