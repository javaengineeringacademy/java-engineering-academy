# Singleton Pattern

## Overview
Singleton ensures a class has only one instance and provides a global point of access to it.

## When to Use
- Database connection pools
- Configuration managers
- Logging services
- Cache managers
- Thread pools

## Implementation Approaches

### Double-Checked Locking
```java
if (instance == null) {
    synchronized (Singleton.class) {
        if (instance == null) {
            instance = new Singleton();
        }
    }
}
```

### Static Holder
```java
private static class Holder {
    private static final Singleton INSTANCE = new Singleton();
}
```

### Enum Singleton
```java
public enum Singleton {
    INSTANCE;
}
```

## Thread Safety
- Double-checked locking requires `volatile` keyword
- Static holder is inherently thread-safe
- Enum approach is thread-safe by JVM guarantee

## Common Mistakes
1. Using Singleton when dependency injection works better
2. Forgetting volatile in double-checked locking
3. Making Singleton testable (static methods hard to mock)
4. Overusing Singleton as global state

## Interview Questions
1. What are the three ways to implement Singleton in Java?
2. Why is double-checked locking thread-safe?
3. What is the advantage of enum Singleton?
4. When should you avoid Singleton?
5. How does Static Holder pattern achieve lazy initialization?

## Performance

Singleton initialization has negligible overhead. Double-checked locking adds a volatile write barrier (~10-20ns) on first access. Static holder and enum approaches have zero synchronization cost after class loading. In hot paths, the cost is a single null check (branch prediction handles this well).

## Internal Working

The JVM ensures a class is loaded only once via its class loader. Static holder leverages this: the inner class `Holder` is loaded only when referenced, at which point the JVM guarantees thread-safe initialization. Double-checked locking uses `volatile` to prevent instruction reordering — without it, another thread might see a partially constructed object. The enum approach is enforced by the JVM specification: enum constants are singletons by construction.

## Why This Concept Exists

Many objects are naturally singular: a configuration manager, a thread pool, a logging service. Creating multiple instances wastes resources or causes inconsistent behavior. Singleton provides a controlled access point while hiding the instantiation mechanism. It solves the problem of "who owns the single instance" without global variables.

## Examples

```java
// Bill Pugh Singleton (recommended)
public class DatabaseConnection {
    private DatabaseConnection() {}
    
    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
    
    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }
    
    public void query(String sql) { /* ... */ }
}

// Enum Singleton (Joshua Bloch recommended)
public enum Configuration {
    INSTANCE;
    
    private final Map<String, String> settings = new HashMap<>();
    
    public void set(String key, String value) {
        settings.put(key, value);
    }
    
    public String get(String key) {
        return settings.get(key);
    }
}

// Thread-safe lazy singleton with double-checked locking
public class CacheManager {
    private static volatile CacheManager instance;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    private CacheManager() {}
    
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }
    
    public void put(String key, Object value) {
        cache.put(key, value);
    }
}
```

## Pitfalls

1. **Global state**: Singletons introduce global mutable state, making testing harder
2. **Tight coupling**: Classes depending directly on a singleton are hard to refactor
3. **Testability**: Static `getInstance()` makes mocking difficult without frameworks like Mockito
4. **Class loader leaks**: In app servers, singleton held by a web app's class loader can prevent undeployment
5. **Overuse**: Use dependency injection instead when possible; singletons are often a code smell

## References

- [Effective Java - Item 3: Enforce the singleton property with a private constructor or an enum type](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Documentation - Singleton Pattern](https://docs.oracle.com/javase/tutorial/essential/concepts/)
- [Baeldung - Singleton Pattern in Java](https://www.baeldung.com/java-singleton)
