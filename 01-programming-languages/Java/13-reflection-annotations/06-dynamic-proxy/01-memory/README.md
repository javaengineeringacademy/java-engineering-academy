# Memory: Dynamic Proxy

## Memory Cost of Proxy Classes

### Generated Proxy Class

Each call to `Proxy.newProxyInstance()` generates a new class:
- Class definition: ~200-500 bytes in Metaspace
- Method stubs: ~50 bytes per interface method
- Interface method table: ~20 bytes per method

### Proxy Instance

Each proxy instance contains:
- Object header: 16 bytes
- InvocationHandler reference: 8 bytes
- Interface vtable pointers: 8 bytes per interface

**Approximate total: 40-80 bytes per proxy instance**

### ClassLoader Impact

Each ClassLoader that creates proxies generates unique proxy classes. If ClassLoaders are not properly cleaned up, proxy classes accumulate in Metaspace.

## Performance Characteristics

| Operation | Time | Notes |
|-----------|------|-------|
| Proxy creation | ~1-5us | Includes class generation |
| Proxy method call | ~20-100ns | Via InvocationHandler |
| Direct method call | ~2ns | Baseline comparison |

## Memory Leak Patterns

### Proxy Class Accumulation

```java
// LEAK: Creates new proxy class every call
public Object getProxy(Object target) {
    return Proxy.newProxyInstance(...);
}

// FIX: Cache proxy instances
private final Map<Object, Object> cache = new WeakHashMap<>();
public Object getProxy(Object target) {
    return cache.computeIfAbsent(target, t -> Proxy.newProxyInstance(...));
}
```

### ClassLoader Leak

If the ClassLoader used to create proxies is not garbage collected, all its proxy classes remain in Metaspace.

## Best Practices

1. Cache proxy instances — one proxy per target object
2. Use WeakHashMap for proxy caches
3. Monitor Metaspace for proxy class accumulation
4. Reuse ClassLoaders when possible
