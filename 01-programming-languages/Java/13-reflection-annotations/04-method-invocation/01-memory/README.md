# Memory: Method Invocation

## Memory Cost of Method Objects

Each `Method` object contains:
- Object header: 16 bytes
- Name (String): 8 bytes
- Return type (Class): 8 bytes
- Parameter types array: 8 bytes
- Exception types array: 8 bytes
- Modifiers (int): 4 bytes
- Declaring class: 8 bytes
- Generic signature data: 8 bytes
- Annotation data: 8 bytes

**Approximate total: 80-120 bytes per Method object**

## Performance Impact

### Reflective vs Direct Invocation

| Operation | Time | Ratio |
|-----------|------|-------|
| Direct method call | ~2ns | 1x |
| Method.invoke() (cached) | ~20-50ns | 10-25x |
| Method.invoke() (uncached) | ~100-200ns | 50-100x |
| MethodHandle.invoke() | ~5-10ns | 2-5x |

### Boxing Overhead

Each reflective call boxes primitive arguments:
```java
// Each call creates Integer objects
method.invoke(obj, 42); // Integer.valueOf(42) called
```

The Integer cache (-128 to 127) mitigates this for small values.

### InvocationTargetException Memory

When a method throws, `InvocationTargetException` creates:
- Exception object: ~80 bytes
- Stack trace: ~200-500 bytes (depends on depth)
- Cause chain: variable

### Method Caching Strategies

```java
// Simple cache
Map<String, Method> cache = new HashMap<>();

// Weak cache (allows GC)
WeakHashMap<Class<?>, Map<String, Method>> weakCache = new WeakHashMap<>();

// ConcurrentHashMap for thread safety
ConcurrentHashMap<String, Method> concurrentCache = new ConcurrentHashMap<>();
```
