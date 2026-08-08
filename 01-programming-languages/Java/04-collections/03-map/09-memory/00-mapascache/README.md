# Map as Cache - Complete Guide

## What is Caching
Storing frequently accessed data in fast storage to avoid slow repeated computation or database calls.

## Why Use Map as Cache
- HashMap get/put is O(1)
- Avoid database calls
- Reduce computation time
- Improve response time

---

## 1. Basic In-Memory Cache (No Thread Safety)

### Simple HashMap Cache
```java
public class SimpleCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final Function<K, V> loader;

    public SimpleCache(Function<K, V> loader) {
        this.loader = loader;
    }

    public V get(K key) {
        return cache.computeIfAbsent(key, loader);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void invalidate(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
```

**Problem**: Not thread-safe. Two threads can corrupt HashMap.

---

## 2. Synchronized Cache

### Using Collections.synchronizedMap()
```java
Map<K, V> cache = Collections.synchronizedMap(new HashMap<>());
```

**Pros**: Simple, thread-safe
**Cons**: Entire map locked. Single lock for all operations. Poor concurrency.

### Using Hashtable
```java
Map<K, V> cache = new Hashtable<>();
```

**Pros**: Thread-safe
**Cons**: Same as synchronizedMap - single lock. Legacy. No null keys/values.

### Custom Synchronized Cache
```java
public class SynchronizedCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final Function<K, V> loader;

    public SynchronizedCache(Function<K, V> loader) {
        this.loader = loader;
    }

    public synchronized V get(K key) {
        return cache.computeIfAbsent(key, loader);
    }

    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    public synchronized void invalidate(K key) {
        cache.remove(key);
    }

    public synchronized int size() {
        return cache.size();
    }
}
```

**Problem**: Still single lock. All threads wait on same lock.

---

## 3. ConcurrentHashMap Cache

### Basic ConcurrentHashMap Cache
```java
public class ConcurrentCache<K, V> {
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final Function<K, V> loader;

    public ConcurrentCache(Function<K, V> loader) {
        this.loader = loader;
    }

    public V get(K key) {
        return cache.computeIfAbsent(key, loader);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void invalidate(K key) {
        cache.remove(key);
    }
}
```

**Pros**: Better concurrency. CAS-based. No single lock.
**Cons**: No null keys/values.

---

## 4. LRU Cache (Least Recently Used)

### LinkedHashMap LRU Cache
```java
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

**How it works**:
- accessOrder = true: iteration order is access order
- When size > capacity: removes eldest (least recently accessed)
- get() moves accessed entry to end

### Thread-Safe LRU Cache
```java
public class ThreadSafeLRUCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;

    public ThreadSafeLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = Collections.synchronizedMap(
            new LinkedHashMap<K, V>(capacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > capacity;
                }
            }
        );
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}
```

---

## 5. TTL Cache (Time-To-Live)

```java
public class TTLCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public TTLCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired(ttlMillis)) {
            return entry.value;
        }
        cache.remove(key);
        return null;
    }

    public void cleanup() {
        cache.entrySet().removeIf(e -> e.getValue().isExpired(ttlMillis));
    }

    private static class CacheEntry<V> {
        final V value;
        final long timestamp;

        CacheEntry(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        boolean isExpired(long ttl) {
            return System.currentTimeMillis() - timestamp > ttl;
        }
    }
}
```

---

## 6. Cache Patterns

### Cache-Aside (Lazy Loading)
```java
public User getUser(String id) {
    // Check cache first
    User user = cache.get(id);
    if (user != null) return user;

    // Load from database
    user = database.load(id);
    if (user != null) {
        cache.put(id, user);
    }
    return user;
}
```

### Write-Through
```java
public void updateUser(User user) {
    database.save(user);      // Write to DB first
    cache.put(user.getId(), user);  // Then update cache
}
```

### Write-Behind (Async)
```java
public void updateUser(User user) {
    cache.put(user.getId(), user);  // Update cache first
    asyncExecutor.submit(() -> database.save(user));  // Async write to DB
}
```

### Read-Through
```java
public User getUser(String id) {
    return cache.computeIfAbsent(id, key -> database.load(key));
}
```

---

## 7. Distributed Cache

### When to Use Distributed Cache
- Multiple application instances
- Data too large for single JVM
- High availability required
- Cross-service data sharing

### Common Solutions

| Solution | Type | Use Case |
|----------|------|----------|
| Redis | In-memory KV | General purpose, pub/sub, persistence |
| Memcached | In-memory KV | Simple caching, multi-threaded |
| Hazelcast | Data grid | Java-native, distributed collections |
| Ehcache | Distributed | JSR-107 compatible |
| Apache Ignite | Data grid | SQL, transactions, co-located processing |

### Redis Example
```java
// Jedis
Jedis jedis = new Jedis("localhost", 6379);

// String cache
jedis.set("user:1", "Alice");
String user = jedis.get("user:1");

// With TTL
jedis.setex("session:abc", 3600, "userData");

// Hash cache
jedis.hset("user:1", "name", "Alice");
jedis.hset("user:1", "age", "30");
Map<String, String> userData = jedis.hgetAll("user:1");

// List cache
jedis.lpush("queue", "task1", "task2");
String task = jedis.rpop("queue");

// Set cache
jedis.sadd("tags", "java", "spring", "docker");
Set<String> tags = jedis.smembers("tags");
```

### Hazelcast Example
```java
HazelcastInstance hz = Hazelcast.newHazelcastInstance();
IMap<String, User> cache = hz.getMap("users");

// Distributed map - automatically replicated
cache.put("user:1", new User("Alice"));
User user = cache.get("user:1");

// With TTL
cache.put("session:abc", sessionData, 1, TimeUnit.HOURS);

// Distributed queries
Collection<User> users = cache.values(
    Predicates.equal("department", "Engineering")
);
```

---

## 8. Cache Invalidation

### Time-Based (TTL)
```java
cache.put(key, value);
// Automatic expiry after TTL
```

### Event-Based
```java
// Invalidate when data changes
public void onUserUpdate(User user) {
    cache.remove("user:" + user.getId());
}
```

### Size-Based (LRU/LFU)
```java
// LinkedHashMap with removeEldestEntry
@Override
protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > MAX_SIZE;
}
```

---

## 9. Performance Comparison

| Cache Type | get() | put() | Thread Safe | Use Case |
|------------|-------|-------|-------------|----------|
| HashMap | O(1) | O(1) | No | Single-thread only |
| synchronizedMap | O(1) | O(1) | Yes (coarse) | Low concurrency |
| ConcurrentHashMap | O(1) | O(1) | Yes (fine) | High concurrency |
| LinkedHashMap LRU | O(1) | O(1) | No | Bounded cache |
| TreeMap | O(log n) | O(log n) | No | Sorted cache |

---

## 10. Best Practices

1. **Set maximum size** - prevent memory overflow
2. **Use TTL** - expire stale data
3. **Monitor hit rate** - track cache effectiveness
4. **Handle cache miss** - graceful fallback
5. **Use appropriate concurrency** - ConcurrentHashMap for high traffic
6. **Consider distributed cache** - for multi-instance apps
7. **Warm up cache** - pre-load frequently accessed data
8. **Handle stampede** - prevent thundering herd on cache miss
