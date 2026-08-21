# Memory: Real-World Use Cases

## Framework Memory Profiles

### Spring

| Component | Memory Cost | Notes |
|-----------|-------------|-------|
| Bean definitions | ~500 bytes per bean | Cached at startup |
| AOP proxies | ~100 bytes per proxy | One per proxied bean |
| Annotation cache | ~1KB per class | Cached during scanning |
| Total (typical app) | 10-50MB | Depends on bean count |

### Hibernate

| Component | Memory Cost | Notes |
|-----------|-------------|-------|
| Entity metadata | ~2KB per entity | Cached in SessionFactory |
| Lazy proxies | ~100 bytes per collection | Created on demand |
| SQL queries | Variable | Cached in query plan cache |

### Jackson

| Component | Memory Cost | Notes |
|-----------|-------------|-------|
| Serializer cache | ~100 bytes per type | Cached per ObjectWriter |
| Type metadata | ~500 bytes per class | Cached during first use |
| Per-request | ~1-10KB | Depends on object graph |

## Performance Benchmarks

| Operation | Spring DI | Hibernate | Jackson |
|-----------|-----------|-----------|---------|
| Startup | 1-5s | 0.5-2s | N/A |
| Per-request | ~0 (cached) | ~1-10ms | ~0.1-1ms |
| Memory per request | ~0 | ~1-10KB | ~1-5KB |

## Best Practices

1. Profile memory usage in production
2. Monitor Metaspace for classloader leaks
3. Cache reflection data at startup
4. Use compile-time processing when possible
5. Limit proxy creation to necessary cases
