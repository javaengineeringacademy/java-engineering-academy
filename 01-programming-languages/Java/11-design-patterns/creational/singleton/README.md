# Singleton Pattern

## Overview
Singleton ensures a class has only one instance and provides a global point of access to it.

## History

| Version | Change |
|---------|--------|
| Pre-JDK | Singleton pattern formalized in GoF book (1994) — Java developers adopted it to manage shared resources like connection pools and configuration |
| JDK 5 | Enum singleton recommended by Joshua Bloch — the simplest thread-safe implementation with serialization support |

## Learning Objectives

By the end of this topic you will be able to:

- Explain why Singleton exists and what problem it solves
- Implement Singleton using 4 different approaches
- Know when Singleton helps and when it hurts
- Test code that uses Singleton
- Replace Singleton with dependency injection

## When to Use
- Database connection pools
- Configuration managers
- Logging services
- Cache managers
- Thread pools

## When NOT to Use Singleton

Singleton is one of the most overused patterns. Avoid it when:

**Testing becomes hard:**
```java
// Singleton makes testing difficult — you can't mock it easily
public class UserService {
    private final Database db = Database.getInstance();  // Hard to mock
}

// Better: dependency injection
public class UserService {
    private final Database db;
    public UserService(Database db) { this.db = db; }  // Easy to mock
}
```

**It creates hidden dependencies:**
```java
// Bad — UserService secretly depends on Database
public class UserService {
    public void save(User user) {
        Database.getInstance().save(user);  // Hidden dependency
    }
}
```

**You need multiple instances:**
```java
// Singleton forces one instance — what if you need two databases?
// Singleton can't handle this.
```

**It violates Single Responsibility:**
```java
// Singleton does two things: manages its lifecycle AND does its actual job
public class Config {
    private static Config instance;
    // ... lifecycle management ...

    public String get(String key) { ... }  // Actual job
}
```

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

## Best Practices

1. **Use dependency injection instead:**
```java
// Bad — Singleton
public class UserService {
    private static UserService instance;
    public static UserService getInstance() { ... }
}

// Good — Dependency Injection
public class UserService {
    private final Database db;
    public UserService(Database db) { this.db = db; }
}
```

2. **If you must use Singleton, use enum:**
```java
public enum Database {
    INSTANCE;
    public void query(String sql) { ... }
}
```

3. **Lazy initialization for expensive resources:**
```java
public class Config {
    private static Config instance;
    public static synchronized Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
}
```

4. **Thread safety is your responsibility:**
```java
// Double-checked locking for lazy initialization
public class Singleton {
    private static volatile Singleton instance;
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) instance = new Singleton();
            }
        }
        return instance;
    }
}
```

5. **Document why it's a Singleton:**
```java
/**
 * Configuration manager. Singleton because multiple instances
 * would cause conflicting configuration states.
 */
public class ConfigManager { ... }
```

## Common Mistakes

### Mistake 1: Not thread-safe
```java
// BAD — not thread-safe
public class Singleton {
    private static Singleton instance;
    public static Singleton getInstance() {
        if (instance == null) instance = new Singleton();  // Race condition
        return instance;
    }
}
```

### Mistake 2: Using it for everything
```java
// BAD — Singleton overkill
public class StringUtils {
    private static StringUtils instance;
    public static StringUtils getInstance() { ... }
    public boolean isEmpty(String s) { return s == null || s.isEmpty(); }
}

// GOOD — just a static utility
public class StringUtils {
    private StringUtils() {}  // Prevent instantiation
    public static boolean isEmpty(String s) { return s == null || s.isEmpty(); }
}
```

### Mistake 3: Forgetting about testing
```java
// BAD — hard to test
public class UserService {
    public void save(User user) {
        Database.getInstance().save(user);  // Can't mock in tests
    }
}

// GOOD — easy to test
public class UserService {
    private final Database db;
    public UserService(Database db) { this.db = db; }
}
```

## Trade-offs

Singleton gives you global access but costs:
- Testing: Hard to mock, hard to reset between tests
- Coupling: Classes secretly depend on the singleton
- Concurrency: Must handle thread safety
- Lifecycle: Hard to control initialization order

Use Singleton when:
- You genuinely need exactly one instance (connection pool, config)
- You're building a simple utility (not a service)
- Testing isn't a concern

Avoid Singleton when:
- You're building services (use DI instead)
- You need to test your code
- You need multiple instances
- You're in a framework that manages lifecycles (Spring)

## Alternatives

| Approach | Testability | Lifecycle Control | Complexity | Use When |
|----------|-------------|-------------------|------------|----------|
| Singleton | Hard | Manual | Low | Simple cases, no testing |
| Dependency Injection | Easy | Framework | Low | Most production code |
| Enum Singleton | Easy | JVM | Lowest | Simple constants |
| Container-managed | Easy | Framework | Medium | Spring, Jakarta EE |
| Static utility | Easy | None | Lowest | Stateless helpers |

## Interview Questions

1. **What is Singleton?**
   A pattern that ensures only one instance of a class exists and provides global access to it.

2. **What are the 4 ways to implement Singleton?**
   Eager initialization, lazy initialization, double-checked locking, enum.

3. **Why is Singleton considered an anti-pattern?**
   It makes testing hard, creates hidden dependencies, and violates Single Responsibility.

4. **When would you use Singleton?**
   Connection pools, configuration managers, logging — when you genuinely need one instance.

5. **How do you test code that uses Singleton?**
   Replace with dependency injection, or use a testing framework that can mock singletons.

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

### Production Examples
- Database connection pool manager
- Application configuration loader
- Logging service initialization
- Cache manager for shared data
- Thread pool for async task execution

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
