# Hashtable in Java Collections Framework

## 1. Introduction

`Hashtable` is a legacy class in Java that implements a hash table data structure, storing key-value pairs similar to `HashMap`. Introduced in JDK 1.0, it is one of the original collection classes. `Hashtable` is synchronized, making it thread-safe, but also slower than its modern counterpart, `HashMap`.

```java
Hashtable<String, Integer> hashtable = new Hashtable<>();
hashtable.put("key", 42);
```

## 2. Learning Objectives

- Understand the `Hashtable` class and its legacy status
- Compare `Hashtable` with `HashMap`
- Learn when to use `Hashtable` in modern applications
- Understand thread safety and synchronization in `Hashtable`
- Recognize performance implications and enterprise usage patterns

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of hash-based data structures
- Familiarity with `HashMap` (recommended)
- Knowledge of multithreading concepts (recommended)

## 4. Why This Concept Exists

`Hashtable` was created in JDK 1.0 to provide a thread-safe hash table implementation. Before `ConcurrentHashMap` and modern concurrent utilities, `Hashtable` was the primary choice for thread-safe key-value storage. It exists for backward compatibility with legacy codebases.

## 5. Problem Statement

In multithreaded applications, concurrent access to a `HashMap` can cause data corruption and `ConcurrentModificationException`. `Hashtable` addresses this by synchronizing all public methods, but this synchronization comes with significant performance overhead. Understanding when to use `Hashtable` versus modern alternatives is crucial for writing efficient, thread-safe code.

## 6. Theory

### Key Characteristics
- **Synchronized**: All public methods are synchronized for thread safety
- **Legacy**: Introduced in JDK 1.0, superseded by `HashMap` (JDK 1.2) and `ConcurrentHashMap` (JDK 5.0)
- **Null restrictions**: Does not allow `null` keys or values
- **Unordered**: Does not guarantee any order of elements
- **Fail-fast**: Iterator will throw `ConcurrentModificationException` if modified during iteration

### Internal Structure
- Uses an array of `Entry` objects (buckets)
- Each `Entry` contains key, value, hash, and next pointer
- Hash function determines bucket index
- Collisions handled via chaining (linked list)
- Initial capacity: 11 (prime number)
- Load factor: 0.75

### Methods
- `put(K key, V value)`: Inserts a key-value pair
- `get(Object key)`: Returns the value for a key
- `remove(Object key)`: Removes a key-value pair
- `containsKey(Object key)`: Checks if a key exists
- `containsValue(Object value)`: Checks if a value exists
- `size()`: Returns the number of key-value pairs
- `isEmpty()`: Checks if the hashtable is empty
- `elements()`: Returns an `Enumeration` of values
- `keys()`: Returns an `Enumeration` of keys

## 7. Internal Working

### Hash Table Structure
```
Hashtable
├── Entry[] table (array of buckets)
│   ├── Entry [0] -> null
│   ├── Entry [1] -> Entry("key1", 100) -> Entry("key5", 500) -> null
│   ├── Entry [2] -> Entry("key2", 200) -> null
│   ├── ...
│   └── Entry [10] -> Entry("key3", 300) -> null
├── int count (number of entries)
├── int threshold (capacity * load factor)
└── float loadFactor
```

### Synchronization Mechanism
```java
// All public methods are synchronized
public synchronized V put(K key, V value) {
    // Implementation
}

public synchronized V get(Object key) {
    // Implementation
}
```

### Rehashing Process
When the number of entries exceeds `threshold`:
1. New capacity = old capacity * 2 + 1 (always odd)
2. New entry array created
3. All entries rehashed and placed in new array
4. Old array discarded

## 8. Syntax

```java
// Import
import java.util.Hashtable;

// Creating Hashtable
Hashtable<KeyType, ValueType> hashtable = new Hashtable<>();
Hashtable<String, Integer> hashtable = new Hashtable<>(16, 0.75f);
Hashtable<String, Integer> hashtable = new Hashtable<>(Map.of("a", 1, "b", 2));

// Basic operations
hashtable.put("key", value);           // Insert
V value = hashtable.get("key");        // Retrieve
hashtable.remove("key");               // Remove
boolean exists = hashtable.containsKey("key");  // Check key
boolean empty = hashtable.isEmpty();   // Check empty
int size = hashtable.size();           // Get size

// Enumeration traversal
Enumeration<String> keys = hashtable.keys();
Enumeration<Integer> values = hashtable.elements();
```

## 9. Easy Example

```java
import java.util.Hashtable;
import java.util.Enumeration;

public class HashtableBasic {
    public static void main(String[] args) {
        Hashtable<String, Integer> scores = new Hashtable<>();
        
        // Adding elements
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        
        // Retrieving elements
        System.out.println("Alice's score: " + scores.get("Alice"));  // 95
        
        // Check if key exists
        if (scores.containsKey("Bob")) {
            System.out.println("Bob's score: " + scores.get("Bob"));
        }
        
        // Remove an element
        scores.remove("Charlie");
        System.out.println("Size after removal: " + scores.size());  // 2
        
        // Iterate using Enumeration
        Enumeration<String> keys = scores.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.println(key + ": " + scores.get(key));
        }
    }
}
```

## 10. Medium Example

```java
import java.util.Hashtable;
import java.util.Map;

public class ThreadSafeCache {
    private final Hashtable<String, String> cache;
    private static final int MAX_SIZE = 100;
    
    public ThreadSafeCache() {
        this.cache = new Hashtable<>(MAX_SIZE);
    }
    
    public synchronized void put(String key, String value) {
        if (cache.size() >= MAX_SIZE) {
            // Remove oldest entry (simplified)
            String firstKey = cache.keys().nextElement();
            cache.remove(firstKey);
        }
        cache.put(key, value);
    }
    
    public synchronized String get(String key) {
        return cache.getOrDefault(key, "Not found");
    }
    
    public synchronized boolean contains(String key) {
        return cache.containsKey(key);
    }
    
    public synchronized int size() {
        return cache.size();
    }
    
    public static void main(String[] args) {
        ThreadSafeCache cache = new ThreadSafeCache();
        
        // Simulate concurrent access
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                cache.put("key" + i, "value" + i);
                System.out.println("Added: key" + i);
            }
        });
        
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                String value = cache.get("key" + i);
                System.out.println("Read: key" + i + " = " + value);
            }
        });
        
        writer.start();
        reader.start();
        
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final size: " + cache.size());
    }
}
```

## 11. Hard Example

```java
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DistributedLockManager {
    private final Hashtable<String, LockInfo> locks;
    private final AtomicInteger lockCount;
    private static final long LOCK_TIMEOUT_MS = 30000; // 30 seconds
    
    private static class LockInfo {
        final String owner;
        final long timestamp;
        final String resourceId;
        
        LockInfo(String owner, String resourceId) {
            this.owner = owner;
            this.resourceId = resourceId;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > LOCK_TIMEOUT_MS;
        }
    }
    
    public DistributedLockManager() {
        this.locks = new Hashtable<>(100);
        this.lockCount = new AtomicInteger(0);
    }
    
    public synchronized boolean acquireLock(String resourceId, String owner) {
        // Check if resource is already locked
        LockInfo existingLock = locks.get(resourceId);
        if (existingLock != null) {
            if (existingLock.isExpired()) {
                // Lock expired, remove it
                locks.remove(resourceId);
                lockCount.decrementAndGet();
            } else if (existingLock.owner.equals(owner)) {
                // Same owner, reentrant
                return true;
            } else {
                // Different owner, locked
                return false;
            }
        }
        
        // Acquire new lock
        locks.put(resourceId, new LockInfo(owner, resourceId));
        lockCount.incrementAndGet();
        System.out.println("Lock acquired: " + resourceId + " by " + owner);
        return true;
    }
    
    public synchronized boolean releaseLock(String resourceId, String owner) {
        LockInfo lockInfo = locks.get(resourceId);
        if (lockInfo != null && lockInfo.owner.equals(owner)) {
            locks.remove(resourceId);
            lockCount.decrementAndGet();
            System.out.println("Lock released: " + resourceId + " by " + owner);
            return true;
        }
        return false;
    }
    
    public synchronized void cleanupExpiredLocks() {
        int removed = 0;
        for (Map.Entry<String, LockInfo> entry : locks.entrySet()) {
            if (entry.getValue().isExpired()) {
                locks.remove(entry.getKey());
                lockCount.decrementAndGet();
                removed++;
            }
        }
        if (removed > 0) {
            System.out.println("Cleaned up " + removed + " expired locks");
        }
    }
    
    public synchronized int getActiveLockCount() {
        return lockCount.get();
    }
    
    public static void main(String[] args) {
        DistributedLockManager manager = new DistributedLockManager();
        
        // Simulate multiple clients
        Runnable client1 = () -> {
            boolean acquired = manager.acquireLock("resource-1", "client-1");
            if (acquired) {
                System.out.println("Client-1 processing resource-1");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                manager.releaseLock("resource-1", "client-1");
            }
        };
        
        Runnable client2 = () -> {
            boolean acquired = manager.acquireLock("resource-1", "client-2");
            if (acquired) {
                System.out.println("Client-2 processing resource-1");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                manager.releaseLock("resource-1", "client-2");
            } else {
                System.out.println("Client-1 is using resource-1, waiting...");
            }
        };
        
        new Thread(client1).start();
        new Thread(client2).start();
        
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        
        manager.cleanupExpiredLocks();
        System.out.println("Active locks: " + manager.getActiveLockCount());
    }
}
```

## 12. Enterprise Example

```java
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigurationService {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationService.class.getName());
    private final Hashtable<String, Object> configurations;
    private final Hashtable<String, Long> configTimestamps;
    private static final long CONFIG_CACHE_TTL = 300000; // 5 minutes
    
    private static ConfigurationService instance;
    
    private ConfigurationService() {
        this.configurations = new Hashtable<>(50);
        this.configTimestamps = new Hashtable<>(50);
        loadDefaultConfigurations();
    }
    
    public static synchronized ConfigurationService getInstance() {
        if (instance == null) {
            instance = new ConfigurationService();
        }
        return instance;
    }
    
    private void loadDefaultConfigurations() {
        configurations.put("database.url", "jdbc:mysql://localhost:3306/mydb");
        configurations.put("database.username", "admin");
        configurations.put("database.pool.size", 20);
        configurations.put("cache.enabled", true);
        configurations.put("cache.ttl", 3600);
        
        long now = System.currentTimeMillis();
        for (String key : configurations.keySet()) {
            configTimestamps.put(key, now);
        }
    }
    
    public synchronized <T> T getConfiguration(String key, Class<T> type) {
        Long timestamp = configTimestamps.get(key);
        if (timestamp != null && System.currentTimeMillis() - timestamp > CONFIG_CACHE_TTL) {
            LOGGER.info("Configuration cache expired for key: " + key);
            refreshConfiguration(key);
        }
        
        Object value = configurations.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Configuration not found: " + key);
        }
        return type.cast(value);
    }
    
    public synchronized void setConfiguration(String key, Object value) {
        configurations.put(key, value);
        configTimestamps.put(key, System.currentTimeMillis());
        LOGGER.info("Configuration updated: " + key);
    }
    
    private void refreshConfiguration(String key) {
        // Simulate fetching from database/remote service
        LOGGER.info("Refreshing configuration from remote source: " + key);
        configTimestamps.put(key, System.currentTimeMillis());
    }
    
    public synchronized Map<String, Object> getAllConfigurations() {
        return new Hashtable<>(configurations);
    }
    
    public static void main(String[] args) {
        ConfigurationService configService = ConfigurationService.getInstance();
        
        // Retrieve configurations
        String dbUrl = configService.getConfiguration("database.url", String.class);
        int poolSize = configService.getConfiguration("database.pool.size", Integer.class);
        boolean cacheEnabled = configService.getConfiguration("cache.enabled", Boolean.class);
        
        System.out.println("Database URL: " + dbUrl);
        System.out.println("Pool Size: " + poolSize);
        System.out.println("Cache Enabled: " + cacheEnabled);
        
        // Update configuration
        configService.setConfiguration("database.pool.size", 30);
        System.out.println("Updated Pool Size: " + 
            configService.getConfiguration("database.pool.size", Integer.class));
    }
}
```

## 13. Performance

### Time Complexity
- **put()**: O(1) amortized, O(n) worst case (many collisions)
- **get()**: O(1) amortized, O(n) worst case
- **remove()**: O(1) amortized, O(n) worst case
- **containsKey()**: O(1) amortized, O(n) worst case
- **containsValue()**: O(n)
- **size()**: O(1)

### Synchronization Overhead
- **Hashtable**: All methods synchronized, ~10-20% slower than `HashMap`
- **ConcurrentHashMap**: Fine-grained locking, ~5-10% slower than `HashMap`
- **SynchronizedMap**: Similar to `Hashtable` performance

### Memory Usage
- **Hashtable**: Higher memory due to synchronization overhead
- **HashMap**: Lower memory, no synchronization
- **ConcurrentHashMap**: Moderate memory, better concurrency than `Hashtable`

### Comparison Table
| Operation | Hashtable | HashMap | ConcurrentHashMap |
|-----------|-----------|---------|-------------------|
| put() | O(1) synchronized | O(1) | O(1) fine-grained |
| get() | O(1) synchronized | O(1) | O(1) fine-grained |
| Thread Safety | Yes (all methods) | No | Yes (segment-level) |
| Null Keys | Not allowed | Allowed (1) | Not allowed |
| Null Values | Not allowed | Allowed | Not allowed |
| Legacy | Yes (JDK 1.0) | No (JDK 1.2) | No (JDK 5.0) |

## 14. Best Practices

```java
// 1. Prefer ConcurrentHashMap over Hashtable
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 2. Use Hashtable for legacy compatibility only
Hashtable<String, Integer> legacy = new Hashtable<>();

// 3. Avoid null keys and values (Hashtable requirement)
// hashtable.put(null, 1);  // NullPointerException
// hashtable.put("key", null);  // NullPointerException

// 4. Use containsKey() instead of contains() for keys
if (hashtable.containsKey("key")) { /* ... */ }

// 5. Use Enumeration for traversal (legacy style)
Enumeration<String> keys = hashtable.keys();
while (keys.hasMoreElements()) {
    String key = keys.nextElement();
    // Process key
}

// 6. Initialize with expected capacity to avoid rehashing
Hashtable<String, Integer> optimized = new Hashtable<>(100);

// 7. Use Collections.synchronizedMap() for Map interface
Map<String, Integer> synchronizedMap = 
    Collections.synchronizedMap(new HashMap<>());
```

## 15. Common Mistakes

```java
// Mistake 1: Using Hashtable when ConcurrentHashMap is better
// Bad
Hashtable<String, Integer> table = new Hashtable<>();
// Good
ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

// Mistake 2: Using null keys or values
// Bad
table.put(null, 1);  // NullPointerException
table.put("key", null);  // NullPointerException
// Good
table.put("key", 1);

// Mistake 3: Using contains() instead of containsKey() or containsValue()
// Bad
boolean exists = table.contains("key");  // Checks both keys and values
// Good
boolean keyExists = table.containsKey("key");

// Mistake 4: Not handling synchronization for compound operations
// Bad
if (!table.containsKey("key")) {
    table.put("key", 1);  // Race condition
}
// Good
synchronized (table) {
    if (!table.containsKey("key")) {
        table.put("key", 1);
    }
}

// Mistake 5: Using Iterator instead of Enumeration
// Bad (will throw ConcurrentModificationException)
for (String key : table.keySet()) { /* ... */ }
// Good
Enumeration<String> keys = table.keys();
while (keys.hasMoreElements()) {
    String key = keys.nextElement();
}
```

## 16. Pitfalls

### Concurrency Issues
- **Compound operations**: `containsKey()` followed by `put()` is not atomic
- **Iterator invalidation**: Using `Iterator` on `Hashtable` can cause `ConcurrentModificationException`
- **Performance bottleneck**: Synchronization on all methods can be a bottleneck

### Legacy Limitations
- **No null keys/values**: Unlike `HashMap`, `Hashtable` doesn't allow nulls
- **Enumeration only**: Only supports `Enumeration` for traversal, not `Iterator`
- **Thread safety**: While synchronized, not as efficient as `ConcurrentHashMap`

### Migration Considerations
- **Code modernization**: Replace `Hashtable` with `ConcurrentHashMap` or `Collections.synchronizedMap()`
- **API compatibility**: `Hashtable` implements `Map` interface, so code can be migrated
- **Performance improvement**: Modern alternatives offer better performance

## 17. Interview Questions

### Q1: What is the difference between Hashtable and HashMap?
**Answer**: `Hashtable` is synchronized (thread-safe) and doesn't allow null keys/values. `HashMap` is not synchronized and allows one null key and multiple null values. `ConcurrentHashMap` is the modern alternative for thread-safe operations.

### Q2: When would you use Hashtable over HashMap?
**Answer**: Only in legacy codebases where backward compatibility with JDK 1.0 is required. For new code, use `ConcurrentHashMap` or `Collections.synchronizedMap()`.

### Q3: Why doesn't Hashtable allow null keys or values?
**Answer**: For thread safety. If null were allowed, `contains(null)` would return true for both key and value, making it ambiguous. Synchronizing null checks adds complexity.

### Q4: How does Hashtable handle collisions?
**Answer**: Uses chaining (linked list) at each bucket. When a collision occurs, the new entry is added to the linked list at that bucket.

### Q5: What is the default capacity and load factor of Hashtable?
**Answer**: Default capacity is 11 (prime number), and default load factor is 0.75. Capacity is always odd to ensure better distribution.

### Q6: Is Hashtable legacy? Should we still use it?
**Answer**: Yes, it's legacy (JDK 1.0). Use `ConcurrentHashMap` for new code. `Hashtable` is only needed for backward compatibility with very old codebases.

### Q7: What happens during rehashing in Hashtable?
**Answer**: When entries exceed `capacity * loadFactor`, a new array with size `2 * oldCapacity + 1` is created. All entries are rehashed and placed in the new array. Old array is discarded.

## 18. Exercises

### Exercise 1: Basic Operations
Create a `Hashtable` to store student names and their grades. Implement methods to add, retrieve, update, and remove students. Print all students with their grades.

### Exercise 2: Thread-Safe Counter
Implement a thread-safe counter using `Hashtable` that supports increment, decrement, and get operations. Test with multiple threads.

### Exercise 3: Legacy Migration
Convert the following `Hashtable` code to use `ConcurrentHashMap`:
```java
Hashtable<String, Integer> table = new Hashtable<>();
table.put("key1", 1);
table.put("key2", 2);
if (table.containsKey("key1")) {
    System.out.println(table.get("key1"));
}
```

### Exercise 4: Simple Cache
Implement a simple cache using `Hashtable` with expiration time. Items should be automatically removed after a specified TTL.

## 19. Summary

- `Hashtable` is a legacy class from JDK 1.0 for thread-safe key-value storage
- All methods are synchronized, making it thread-safe but slower than `HashMap`
- Does not allow null keys or values
- Uses chaining for collision handling
- Modern alternatives: `ConcurrentHashMap` (better performance) or `Collections.synchronizedMap()` (Map interface)
- Use only for backward compatibility with legacy codebases
- Default capacity is 11, load factor is 0.75
- Supports `Enumeration` for traversal, not `Iterator`

## 20. References

### Official Documentation
- [Java Hashtable Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Hashtable.html)
- [Java HashMap Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashMap.html)
- [Java ConcurrentHashMap Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)

### Books
- *Effective Java* by Joshua Bloch
- *Java Concurrency in Practice* by Brian Goetz
- *Java: The Complete Reference* by Herbert Schildt

### Online Resources
- [Baeldung - Hashtable vs HashMap](https://www.baeldung.com/java-hashtable-vs-hashmap)
- [GeeksforGeeks - Hashtable in Java](https://www.geeksforgeeks.org/java-util-hashtable-class-java/)
- [Oracle - Collections Framework](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/collections/)

### Related Topics
- [HashMap](../15-hashmap/README.md)
- [ConcurrentHashMap](../21-concurrent-hashmap/README.md)
- [Synchronized Collections](../22-synchronized-collections/README.md)
