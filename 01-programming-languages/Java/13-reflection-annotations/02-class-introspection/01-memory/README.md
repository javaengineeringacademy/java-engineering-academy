# Memory: Class Introspection

## Memory Layout of Class Objects

### Heap Memory (Class Instance)

Each `Class` object on the heap contains:
- Object header: 16 bytes
- Name reference: 8 bytes
- ClassLoader reference: 8 bytes
- Superclass reference: 8 bytes
- Interface array reference: 8 bytes
- Field/Method array references: 8 bytes each
- Reflection data (SoftReference): 8 bytes

### Metaspace Memory

The heavier metadata lives in Metaspace:
- Constant pool: Variable size
- Method bytecode: Variable size
- Field descriptors: 20-40 bytes per field
- Method descriptors: 40-80 bytes per method

### Cost of Introspection

| Operation | Memory Cost | Notes |
|-----------|-------------|-------|
| `Class.forName()` | Loads entire class | Metaspace + heap |
| `getDeclaredFields()` | Creates Field array | Cached via SoftReference |
| `getDeclaredMethods()` | Creates Method array | Cached via SoftReference |
| `getSuperclass()` | Returns existing Class | No new allocation |

## Memory Leaks

### ClassLoader Leaks

If a ClassLoader is garbage collected but Class objects remain referenced, the entire ClassLoader memory cannot be reclaimed.

**Common cause:** Storing Class references in a static cache that outlives the classloader.

### Metaspace Exhaustion

Loading many classes (e.g., in frameworks with dynamic proxies) can exhaust Metaspace:

```bash
# Monitor metaspace
jcmd <pid> VM.metaspace
```

## Best Practices

1. Cache Class objects — avoid repeated Class.forName() calls
2. Use WeakReference for Class caches in dynamic classloader scenarios
3. Monitor Metaspace in long-running applications
4. Set -XX:MaxMetaspaceSize appropriately
