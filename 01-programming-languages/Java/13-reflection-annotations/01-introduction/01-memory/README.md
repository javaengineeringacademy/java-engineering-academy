# Memory: Reflection and Annotations

## Memory Footprint of Reflection Objects

### Class Object Memory

Each `Class` object occupies approximately 40-80 bytes of heap memory, plus additional memory for its metadata arrays:

| Component | Approximate Size |
|-----------|-----------------|
| `Class` object header | 16 bytes |
| `Field[]` array | 16 + (n × 8) bytes |
| `Method[]` array | 16 + (n × 8) bytes |
| `Constructor[]` array | 16 + (n × 8) bytes |
| String constants (names) | Variable |

### Metaspace Usage

Reflection metadata is stored in Metaspace (not heap), which is limited by `-XX:MaxMetaspaceSize`:

```
Metaspace Layout:
├── Klass structures (one per loaded class)
├── Method metadata
├── Constant pool
├── Annotation data
└── Generic type signatures
```

### Memory Leaks from Reflection

**Common leak patterns:**

1. **ClassLoader leaks:** Loading classes dynamically but never garbage collecting the ClassLoader
2. **Method/Field cache growth:** Caching Method objects indefinitely without bounds
3. **Proxy class accumulation:** Creating new proxy classes without reuse

```java
// LEAK: Creates a new proxy class every call
public Object createProxy(Object target) {
    return Proxy.newProxyInstance(...); // New class generated each time
}

// FIX: Cache proxy instances
private final Map<Object, Object> proxyCache = new WeakHashMap<>();
public Object createProxy(Object target) {
    return proxyCache.computeIfAbsent(target,
        t -> Proxy.newProxyInstance(...));
}
```

## Annotation Memory

Annotations with `@Retention(RetentionPolicy.RUNTIME)` consume memory because:
- The annotation class itself is loaded into Metaspace
- Each annotated element stores a reference to the annotation instance
- Annotation values are stored as constant pool entries

## Memory Monitoring

```bash
# Monitor Metaspace usage
jcmd <pid> VM.flags | grep Metaspace
jcmd <pid> GC.class_stats

# Track loaded classes
jcmd <pid> VM.classloader_stats
```

## Best Practices

1. **Cache Class/Method/Field objects** — Avoid repeated lookups
2. **Use WeakHashMap for proxy caches** — Allow GC of unused proxies
3. **Monitor Metaspace** — Watch for ClassLoader leaks in long-running apps
4. **Limit annotation retention** — Use SOURCE or CLASS when RUNTIME isn't needed
