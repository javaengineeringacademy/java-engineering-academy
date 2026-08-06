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
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### Static Holder
```java
public class Singleton {
    private Singleton() {}

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

### Enum Singleton
```java
public enum Singleton {
    INSTANCE;

    public void doSomething() {
        // implementation
    }
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

## Engineering Decision Framework

### ✅ Use Singleton when:
- A single shared instance is naturally required (connection pools, caches)
- Global coordination point is needed (thread pools, registries)
- Resource management requires centralized control
- Stateless service with no testability concerns

### ❌ Avoid Singleton when:
- Dependency injection is available (preferred approach)
- Unit testing requires mockability
- Concurrency patterns need multiple instances
- Application server classloader lifecycle is a concern

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Dependency Injection | When DI container manages lifecycle |
| Enum Singleton | When serialization safety is required |
| Static utility class | When no state or instance is needed |
| Per-request instance | When statelessness is preferred |

### Production Examples
- Database connection pool manager
- Application configuration loader
- Logging service initialization
- Cache manager for shared data
- Thread pool for async task execution

### Common Production Mistakes
- Using Singleton when DI works better (tight coupling)
- Forgetting volatile in double-checked locking (broken on some JVMs)
- Making Singleton testable with static methods (hard to mock)
- Holding resources that prevent application undeployment
- Overusing Singleton as global mutable state

## Production Checklist

### ✅ Before using Singleton in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Internal Working

The JVM ensures a class is loaded only once via its class loader. Static holder uses this: the inner class `Holder` is loaded only when referenced, at which point the JVM guarantees thread-safe initialization. Double-checked locking uses `volatile` to prevent instruction reordering — without it, another thread might see a partially constructed object. The enum approach is enforced by the JVM specification: enum constants are singletons by construction.

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

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## References

- [Effective Java - Item 3: Enforce the singleton property with a private constructor or an enum type](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Documentation - Singleton Pattern](https://docs.oracle.com/javase/tutorial/essential/concepts/)
- [Baeldung - Singleton Pattern in Java](https://www.baeldung.com/java-singleton)

## Common Myths

### ❌ Myth 1: Singleton is always good
**Reality:** Testing and concurrency issues. Singletons make unit testing harder and can cause thread-safety problems.

### ❌ Myth 2: Singleton is thread-safe by default
**Reality:** Must be implemented. Simple lazy initialization without synchronization is not thread-safe.

### ❌ Myth 3: Singleton is one instance per JVM
**Reality:** Can be per classloader. In app servers, each classloader may create its own instance.
