# Internals: MDC Implementation

## ThreadLocal Storage

```java
// MDC uses ThreadLocal<Map<String, String>>
// Each thread gets its own copy of the map
// Values are only visible within the thread that set them

public class MDC {
    private static ThreadLocal<Map<String, String>> threadLocal = 
        ThreadLocal.withInitial(HashMap::new);
    
    public static void put(String key, String value) {
        getMap().put(key, value);
    }
    
    public static String get(String key) {
        return getMap().get(key);
    }
    
    public static void clear() {
        threadLocal.remove(); // Important for thread pools!
    }
}
```

## Thread Pool Behavior

```
Thread Pool Thread 1: [requestId=abc, userId=user1]
    │
    ├── Submits task to executor
    │
    ▼
Thread Pool Thread 2: []  ← MDC NOT inherited!
    │
    └── Need to copy/restore MDC manually
```

**Why it doesn't propagate:**
- Thread pools reuse threads
- ThreadLocal is thread-specific
- No automatic copy mechanism in Java

## Performance Characteristics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| `put()` | O(1) amortized | HashMap put |
| `get()` | O(1) average | HashMap get |
| `remove()` | O(1) | HashMap remove |
| `clear()` | O(1) | ThreadLocal.remove() |
| `getCopyOfContextMap()` | O(n) | Creates new HashMap |

## Memory Usage

```java
// Each MDC entry: ~100-200 bytes (key + value + overhead)
// Typical request MDC: 3-5 keys = ~500-1000 bytes
// Thread pool with 100 threads: ~50-100 KB

// Memory concerns:
// 1. Large values in MDC → memory bloat per thread
// 2. Not cleaning up → values persist in thread pool
// 3. Many threads → more memory used
```

## Logback vs Log4j 2 MDC

| Aspect | SLF4J MDC (Logback) | Log4j 2 ThreadContext |
|--------|---------------------|----------------------|
| Storage | ThreadLocal<Map> | ThreadLocal<Map> |
| API | `MDC.put/get/clear` | `ThreadContext.put/get/clearAll` |
| Serialization | Map-based | Array-based (faster) |
| Performance | Standard | Optimized (garbage-free) |
| Thread propagation | Manual | Manual (same limitation) |
