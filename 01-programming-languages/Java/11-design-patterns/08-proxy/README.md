# Proxy Pattern

## 1. Introduction

The Proxy Pattern is a structural design pattern that provides a surrogate or placeholder for another object to control access to it. A proxy controls access to the original object, allowing you to perform something either before or after the request gets through to the original object.

The Proxy pattern is particularly useful for lazy initialization, logging, access control, caching, and remote resource management.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement different types of proxies (virtual, protection, remote, caching)
- Understand proxy vs. decorator vs. adapter
- Recognize proxy usage in Java (RMI, Spring AOP, Hibernate)
- Handle proxy creation with JDK dynamic proxies
- Understand performance implications of proxies

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of Java reflection
- Familiarity with design patterns concepts
- Understanding of lazy initialization

---

## 4. Why This Concept Exists

The Proxy pattern exists because:

- **Access control**: Restrict who can access the real object
- **Lazy initialization**: Delay expensive object creation
- **Logging/Auditing**: Track object usage
- **Caching**: Cache expensive operations
- **Remote resources**: Represent remote objects locally
- **Performance**: Add indirection for optimization

Without Proxy, you'd embed control logic directly in business objects.

---

## 5. Problem Statement

Consider image loading:

```java
// BAD: Loading all images at startup
public class ImageLoader {
    public void loadAllImages() {
        // Loads ALL images, even if not displayed
        for (String imageUrl : allImageUrls) {
            Image image = new HighResImage(imageUrl); // Expensive!
            images.add(image);
        }
    }
}

// Problem: Loading 1000 high-res images at startup
// Most images are never displayed
// Application becomes slow and memory-intensive
```

---

## 6. Theory

### 6.1 Proxy Types

| Type | Purpose | Example |
|------|---------|---------|
| Virtual | Lazy initialization | Image loading |
| Protection | Access control | File access |
| Remote | Network access | RMI |
| Caching | Cache results | Database query |
| Logging | Track operations | API calls |

### 6.2 Proxy vs. Similar Patterns

| Pattern | Purpose | Interface |
|---------|---------|-----------|
| Proxy | Control access | Same as real object |
| Decorator | Add behavior | Same as real object |
| Adapter | Convert interface | Different interface |

### 6.3 Java Proxy Mechanisms

- **JDK Dynamic Proxy**: For interfaces
- **CGLIB Proxy**: For classes
- **Spring AOP**: Framework support

---

## 7. Internal Working

### 7.1 Proxy Flow

```
Client → Proxy → Real Object
           ↓
    Before advice
           ↓
    Real method call
           ↓
    After advice
```

### 7.2 JDK Dynamic Proxy Flow

```
1. Client calls method on proxy
2. Proxy invokes InvocationHandler.invoke()
3. Handler performs pre-processing
4. Handler invokes real method via reflection
5. Handler performs post-processing
6. Result returned to client
```

---

## 8. JVM Perspective

### 8.1 Dynamic Proxy Generation

- JDK creates proxy class at runtime
- Proxy implements same interfaces
- Method calls routed through InvocationHandler
- Bytecode generated in memory

### 8.2 Memory Impact

- Proxy class loaded by classloader
- Each proxy instance holds reference to handler
- Real object may be lazily created
- Consider class loading overhead

---

## 9. Memory Representation

### 9.1 Proxy Memory Model

```
┌─────────────────────────────────────┐
│             Client                  │
└──────────────┬──────────────────────┘
               │ references
               ↓
┌─────────────────────────────────────┐
│         Proxy (dynamic)             │
│  - handler: InvocationHandler       │
└──────────────┬──────────────────────┘
               │ delegates to
               ↓
┌─────────────────────────────────────┐
│      InvocationHandler              │
│  - realObject: RealObject           │
└──────────────┬──────────────────────┘
               │ holds
               ↓
┌─────────────────────────────────────┐
│        RealObject                   │
│  (actual implementation)            │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Static Proxy

```java
public interface Image {
    void display();
}

public class HighResImage implements Image {
    private final String filename;

    public HighResImage(String filename) {
        this.filename = filename;
        loadFromDisk(); // Expensive
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

public class ImageProxy implements Image {
    private HighResImage realImage;
    private final String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new HighResImage(filename);
        }
        realImage.display();
    }
}
```

### 10.2 JDK Dynamic Proxy

```java
public interface Service {
    void doWork();
}

public class RealService implements Service {
    @Override
    public void doWork() {
        System.out.println("Working...");
    }
}

public class LoggingHandler implements InvocationHandler {
    private final Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("After: " + method.getName());
        return result;
    }
}

// Usage
Service service = (Service) Proxy.newProxyInstance(
    Service.class.getClassLoader(),
    new Class[]{Service.class},
    new LoggingHandler(new RealService())
);
service.doWork();
```

---

## 11. Easy Example

### Virtual Proxy for Image Loading

```java
public interface Image {
    void display();
}

public class RealImage implements Image {
    private final String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk...");
        // Simulate expensive operation
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

public class ImageProxy implements Image {
    private RealImage realImage;
    private final String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}

// Usage
Image image = new ImageProxy("photo.jpg");
System.out.println("Proxy created");
// Image not loaded yet
image.display(); // Now loads and displays
```

---

## 12. Medium Example

### Protection Proxy with Access Control

```java
public interface Document {
    String read();
    void write(String content);
}

public class RealDocument implements Document {
    private String content;
    private final String owner;

    public RealDocument(String content, String owner) {
        this.content = content;
        this.owner = owner;
    }

    @Override
    public String read() {
        return content;
    }

    @Override
    public void write(String content) {
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }
}

public class ProtectionProxy implements Document {
    private final RealDocument document;
    private final String currentUser;

    public ProtectionProxy(RealDocument document, String currentUser) {
        this.document = document;
        this.currentUser = currentUser;
    }

    @Override
    public String read() {
        return document.read();
    }

    @Override
    public void write(String content) {
        if (!currentUser.equals(document.getOwner())) {
            throw new SecurityException("User " + currentUser + " cannot write to this document");
        }
        document.write(content);
    }
}

// Usage
RealDocument doc = new RealDocument("Secret content", "admin");
Document userProxy = new ProtectionProxy(doc, "user");
Document adminProxy = new ProtectionProxy(doc, "admin");

userProxy.read(); // OK
// userProxy.write("hacked"); // Throws SecurityException
adminProxy.write("Updated content"); // OK
```

---

## 13. Hard Example

### Caching Proxy with TTL

```java
public interface DataProvider {
    String getData(String key);
}

public class DatabaseDataProvider implements DataProvider {
    @Override
    public String getData(String key) {
        // Simulate database query
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        return "Data from DB: " + key;
    }
}

public class CachingProxy implements DataProvider {
    private final DataProvider realProvider;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final Duration ttl;

    public CachingProxy(DataProvider realProvider, Duration ttl) {
        this.realProvider = realProvider;
        this.ttl = ttl;
    }

    @Override
    public String getData(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            System.out.println("Cache hit: " + key);
            return entry.getValue();
        }

        System.out.println("Cache miss: " + key);
        String data = realProvider.getData(key);
        cache.put(key, new CacheEntry(data, Instant.now().plus(ttl)));
        return data;
    }

    private static class CacheEntry {
        private final String value;
        private final Instant expiresAt;

        CacheEntry(String value, Instant expiresAt) {
            this.value = value;
            this.expiresAt = expiresAt;
        }

        String getValue() {
            return value;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}

// Usage
DataProvider provider = new CachingProxy(
    new DatabaseDataProvider(),
    Duration.ofMinutes(5)
);

provider.getData("user-123"); // Cache miss
provider.getData("user-123"); // Cache hit
```

---

## 14. Enterprise Example

### Spring-Style AOP Proxy

```java
public interface UserService {
    User findById(String id);
    void save(User user);
}

public class UserServiceImpl implements UserService {
    @Override
    public User findById(String id) {
        // Database query
        return new User(id, "John");
    }

    @Override
    public void save(User user) {
        // Save to database
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecutionTime {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    int ttlSeconds() default 300;
}

public class AopProxy implements InvocationHandler {
    private final Object target;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public AopProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Check for @LogExecutionTime
        if (method.isAnnotationPresent(LogExecutionTime.class)) {
            long start = System.currentTimeMillis();
            Object result = method.invoke(target, args);
            long duration = System.currentTimeMillis() - start;
            System.out.println(method.getName() + " executed in " + duration + "ms");
            return result;
        }

        // Check for @Cacheable
        if (method.isAnnotationPresent(Cacheable.class)) {
            String cacheKey = method.getName() + ":" + Arrays.toString(args);
            if (cache.containsKey(cacheKey)) {
                System.out.println("Returning cached result for " + cacheKey);
                return cache.get(cacheKey);
            }
            Object result = method.invoke(target, args);
            cache.put(cacheKey, result);
            return result;
        }

        return method.invoke(target, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            new AopProxy(target)
        );
    }
}

// Usage
UserService realService = new UserServiceImpl();
UserService proxy = AopProxy.createProxy(realService);

proxy.findById("123"); // Logged execution time
proxy.save(new User("456", "Jane"));
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Static proxy | O(1) | Direct delegation |
| Dynamic proxy | O(1) | + reflection overhead |
| Cache lookup | O(1) | HashMap access |
| Lazy init | O(1) first call | + creation cost |

### 15.2 Optimization Tips

1. **Cache dynamic proxy classes**: Reuse proxy classes
2. **Minimize reflection**: Cache Method objects
3. **Lazy initialization**: Only create real object when needed
4. **Profile proxy overhead**: Measure actual impact

---

## 16. Best Practices

1. **Use interfaces**: Dynamic proxy requires interfaces
2. **Keep proxy thin**: Minimal logic in proxy
3. **Document proxy purpose**: Clear Javadoc
4. **Handle errors gracefully**: Don't swallow exceptions
5. **Consider performance**: Proxy adds overhead
6. **Use framework support**: Spring AOP for complex cases
7. **Test proxy behavior**: Verify proxy works correctly
8. **Consider alternatives**: Decorator, Chain of Responsibility

---

## 17. Common Mistakes

1. **Too much logic in proxy**: Proxy should be thin
2. **Ignoring errors**: Swallowing exceptions
3. **Performance degradation**: Too many proxies
4. **Tight coupling**: Proxy depends on concrete classes
5. **Not testing**: Proxy behavior untested

---

## 18. Pitfalls

- **Complexity**: More classes and indirection
- **Performance overhead**: Reflection, delegation
- **Debugging difficulty**: Hard to trace
- **Class loading**: Dynamic proxy classes
- **Memory usage**: Proxy objects, cached data

---

## 19. Debugging Tips

1. **Add logging**: Track proxy invocations
2. **Use debugger**: Step through proxy logic
3. **Monitor performance**: Measure proxy overhead
4. **Check class loading**: Verify proxy classes loaded
5. **Test in isolation**: Unit test proxy logic

---

## 20. Comparison Table

| Pattern | Purpose | Interface | Dynamic |
|---------|---------|-----------|---------|
| Proxy | Control access | Same | Yes/No |
| Decorator | Add behavior | Same | No |
| Adapter | Convert interface | Different | No |
| Facade | Simplify interface | Different | No |

---

## 21. Decision Tree

```
Need to control access?
├── Lazy initialization? → Virtual proxy
├── Access control? → Protection proxy
├── Caching? → Caching proxy
├── Logging? → Logging proxy
├── Remote resource? → Remote proxy
└── Adding behavior? → Consider Decorator
```

---

## 22. Interview Questions

### Q1: What is the Proxy pattern?
**Answer**: A structural pattern that provides a placeholder for another object to control access to it.

### Q2: Proxy vs. Decorator?
**Answer**: Proxy controls access. Decorator adds behavior. Proxy may create real object, Decorator always wraps it.

### Q3: How does Spring use proxies?
**Answer**: Spring AOP uses JDK dynamic proxy (interfaces) or CGLIB (classes) to implement aspects like transactions, security.

### Q4: What is a virtual proxy?
**Answer**: A proxy that creates the real object only when first needed (lazy initialization).

### Q5: Performance impact of proxies?
**Answer**: Proxies add indirection and possibly reflection overhead. Measure actual impact in your use case.

---

## 23. Exercises

### Exercise 1: Virtual Proxy
Create a lazy-loading proxy for a heavy object.

### Exercise 2: Protection Proxy
Implement access control proxy for a file system.

### Exercise 3: Caching Proxy
Create a caching proxy with TTL and cache invalidation.

---

## 24. Assignments

1. **Assignment 1**: Create a remote proxy for a network service
2. **Assignment 2**: Build a logging proxy for API calls
3. **Assignment 3**: Implement a proxy with rate limiting

---

## 25. Mini Project

### Image Gallery with Proxies
Create an image gallery that:
- Uses virtual proxy for lazy loading
- Implements caching proxy for frequent images
- Adds logging proxy for analytics
- Supports protection proxy for access control

---

## 26. Summary

- Proxy controls access to real object
- Multiple types: virtual, protection, remote, caching
- JDK dynamic proxy for interfaces, CGLIB for classes
- Proxy adds indirection and overhead
- Consider alternatives like Decorator
- Use framework support for complex cases

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 4
2. Bloch, J. (2018). *Effective Java*, Item 42
3. Walls, C. (2018). *Spring in Action* (5th Edition)
4. Refactoring Guru: https://refactoring.guru/design-patterns/proxy
5. Java Design Patterns: https://java-design-patterns.com/patterns/proxy/
