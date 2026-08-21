# Memory: Constructor Access

## Memory Cost of Constructor Objects

Each `Constructor` object contains:
- Object header: 16 bytes
- Name (String): 8 bytes
- Parameter types array: 8 bytes
- Exception types array: 8 bytes
- Modifiers (int): 4 bytes
- Declaring class: 8 bytes
- Generic signature: 8 bytes
- Annotation data: 8 bytes

**Approximate total: 70-100 bytes per Constructor object**

## Performance: Constructor.newInstance() vs Direct new

| Operation | Time | Notes |
|-----------|------|-------|
| `new Object()` | ~5ns | Direct allocation |
| `Constructor.newInstance()` | ~50-200ns | 10-40x slower |

The overhead comes from:
1. Type checking and validation
2. Security checks
3. JNI boundary crossing
4. No JIT inlining

## Object Allocation Patterns

### TLAB (Thread-Local Allocation Buffer)

Small objects are allocated in TLABs, which are thread-local regions of the Eden space. This avoids synchronization but limits allocation rate.

### Large Object Allocation

Objects larger than `-XX:TLABSize` are allocated directly in Old Gen, which requires synchronization.

## Memory Monitoring

```java
// Track constructor allocation
Runtime rt = Runtime.getRuntime();
long before = rt.freeMemory();
Constructor<?> ctor = clazz.getDeclaredConstructor();
Object obj = ctor.newInstance();
long after = rt.freeMemory();
System.out.println("Allocated: " + (before - after) + " bytes");
```

## Best Practices

1. Cache Constructor objects — avoid repeated lookup
2. Prefer static factory methods over constructor reflection
3. Consider using `MethodHandles.Lookup.findConstructor()` for better performance
