# Map as Cache

## In-Memory Cache with HashMap

### Simple Cache
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

    public void invalidate(K key) { cache.remove(key); }
    public void clear() { cache.clear(); }
    public int size() { return cache.size(); }
}
```

### LRU Cache with LinkedHashMap
```java
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

### TTL Cache
```java
public class TTLCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final long ttlMillis;

    public TTLCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired(ttlMillis)) return entry.value;
        cache.remove(key);
        return null;
    }

    private record CacheEntry<V>(V value, long timestamp) {
        boolean isExpired(long ttl) { return System.currentTimeMillis() - timestamp > ttl; }
    }
}
```

## Distributed Cache

### When You Need Distributed Cache
- Multiple application instances
- High availability required
- Large data that doesn't fit single JVM
- Cross-service data sharing

### Common Distributed Cache Solutions

| Solution | Type | Use Case |
|----------|------|----------|
| Redis | In-memory key-value | General purpose, pub/sub |
| Memcached | In-memory key-value | Simple caching, multi-threaded |
| Hazelcast | In-memory data grid | Java-native, distributed collections |
| Ehcache | Distributed cache | JSR-107 compatible |

### Redis Example
```java
// Jedis
Jedis jedis = new Jedis("localhost", 6379);
jedis.set("user:1", "Alice");
String user = jedis.get("user:1");

// With TTL
jedis.setex("session:abc", 3600, "userData");

// Hash
jedis.hset("user:1", "name", "Alice");
jedis.hset("user:1", "age", "30");
Map<String, String> userData = jedis.hgetAll("user:1");
```

### Cache Patterns

#### Cache-Aside (Lazy Loading)
```java
public User getUser(String id) {
    User user = cache.get(id);
    if (user == null) {
        user = database.load(id);
        cache.put(id, user);
    }
    return user;
}
```

#### Write-Through
```java
public void updateUser(User user) {
    database.save(user);
    cache.put(user.getId(), user);
}
```

#### Write-Behind (Async)
```java
public void updateUser(User user) {
    cache.put(user.getId(), user);
    asyncExecutor.submit(() -> database.save(user));
}
```

### Cache Invalidation Strategies
- **TTL**: Auto-expire after time
- **LRU**: Remove least recently used
- **LFU**: Remove least frequently used
- **Event-driven**: Invalidate on data change

### Distributed Cache Considerations
- **Consistency**: eventual vs strong
- **Partitioning**: hash-based distribution
- **Replication**: for high availability
- **Serialization**: Java, JSON, Protobuf
- **Connection pooling**: for performance
