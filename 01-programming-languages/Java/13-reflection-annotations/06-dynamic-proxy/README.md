# 06 — Dynamic Proxy

## Why Dynamic Proxy Matters

Dynamic proxy is the mechanism behind AOP (Aspect-Oriented Programming), which powers Spring's `@Transactional`, security annotations, caching, and logging interceptors. Instead of modifying a class's code, you wrap it in a proxy that intercepts every method call and adds behavior before, after, or around the original call.

This is reflection's most powerful application — it lets you modify *behavior* without modifying *code*.

---

## The Proxy Pattern — Static vs Dynamic

### Static Proxy (Manual)

```java
interface UserService {
    void saveUser(User user);
}

// Real implementation
class UserServiceImpl implements UserService {
    public void saveUser(User user) { /* save to DB */ }
}

// Manual proxy
class UserServiceProxy implements UserService {
    private final UserService real;
    
    UserServiceProxy(UserService real) { this.real = real; }
    
    public void saveUser(User user) {
        System.out.println("Before save");
        real.saveUser(user); // Delegate
        System.out.println("After save");
    }
}

// Usage
UserService service = new UserServiceProxy(new UserServiceImpl());
```

**Problem:** You must create a proxy class for every interface. With 100 interfaces, you need 100 proxy classes.

### Dynamic Proxy (Automatic)

```java
// One handler works for ALL interfaces
class LoggingHandler implements InvocationHandler {
    private final Object target;
    
    LoggingHandler(Object target) { this.target = target; }
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        System.out.println("Before: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("After: " + method.getName());
        return result;
    }
}

// Create proxy for ANY interface
UserService service = (UserService) Proxy.newProxyInstance(
    UserService.class.getClassLoader(),
    new Class[]{UserService.class},
    new LoggingHandler(new UserServiceImpl())
);

service.saveUser(user); // Intercepted!
```

**One handler, unlimited proxies.**

---

## `Proxy.newProxyInstance()` — Deep Dive

### Parameters

```java
Object proxy = Proxy.newProxyInstance(
    ClassLoader loader,        // ClassLoader to define the proxy class
    Class<?>[] interfaces,     // Interfaces the proxy implements
    InvocationHandler h        // The handler that intercepts calls
);
```

### ClassLoader

```java
// Option 1: Use the target's classloader
ClassLoader loader = target.getClass().getClassLoader();

// Option 2: Use the interface's classloader
ClassLoader loader = UserService.class.getClassLoader();

// Option 3: Use context classloader (for web apps)
ClassLoader loader = Thread.currentThread().getContextClassLoader();

// Option 4: Use a custom classloader (for plugin systems)
ClassLoader loader = new PluginClassLoader(pluginPath);
```

### Interfaces

```java
// Single interface
Class<?>[] interfaces = new Class[]{UserService.class};

// Multiple interfaces
Class<?>[] interfaces = new Class[]{UserService.class, Serializable.class};

// From a list
List<Class<?>> ifaceList = discoverInterfaces();
Class<?>[] interfaces = ifaceList.toArray(new Class<?>[0]);
```

---

## `InvocationHandler` — The Interceptor

```java
public interface InvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```

### Parameters Explained

| Parameter | Type | Description |
|-----------|------|-------------|
| `proxy` | `Object` | The proxy instance itself (avoid calling methods on this!) |
| `method` | `Method` | The method being invoked |
| `args` | `Object[]` | Arguments passed to the method (null if no args) |

### Return Value

- Return the method's actual result (or a substitute)
- Return `null` for void methods
- Throw an exception to propagate it

---

## Common Patterns

### Pattern 1: Logging Interceptor

```java
class LoggingHandler implements InvocationHandler {
    private final Object target;
    private final Logger logger;
    
    LoggingHandler(Object target, Logger logger) {
        this.target = target;
        this.logger = logger;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        logger.info("Calling: " + method.getName() + 
            " with args: " + Arrays.toString(args));
        long start = System.nanoTime();
        
        try {
            Object result = method.invoke(target, args);
            long elapsed = System.nanoTime() - start;
            logger.info("Completed: " + method.getName() + 
                " in " + elapsed / 1_000_000 + "ms");
            return result;
        } catch (InvocationTargetException e) {
            logger.error("Failed: " + method.getName() + 
                " - " + e.getTargetException().getMessage());
            throw e.getTargetException();
        }
    }
}
```

### Pattern 2: Transaction Management

```java
class TransactionHandler implements InvocationHandler {
    private final Object target;
    private final DataSource dataSource;
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Store connection in thread-local for DAO access
            TransactionContext.setConnection(conn);
            
            Object result = method.invoke(target, args);
            
            conn.commit();
            return result;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
            TransactionContext.clear();
        }
    }
}
```

### Pattern 3: Caching

```java
class CachingHandler implements InvocationHandler {
    private final Object target;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        String key = method.getName() + ":" + Arrays.toString(args);
        
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        
        Object result = method.invoke(target, args);
        cache.put(key, result);
        return result;
    }
}
```

### Pattern 4: Access Control

```java
class SecurityHandler implements InvocationHandler {
    private final Object target;
    private final SecurityManager securityManager;
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        if (!securityManager.isAllowed(method)) {
            throw new SecurityException(
                "Access denied: " + method.getName());
        }
        return method.invoke(target, args);
    }
}
```

### Pattern 5: Rate Limiting

```java
class RateLimitHandler implements InvocationHandler {
    private final Object target;
    private final AtomicInteger calls = new AtomicInteger(0);
    private final int maxCallsPerSecond;
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        if (calls.incrementAndGet() > maxCallsPerSecond) {
            throw new RuntimeException("Rate limit exceeded");
        }
        
        // Reset counter every second
        scheduledExecutor.scheduleAtFixedRate(
            () -> calls.set(0), 1, 1, TimeUnit.SECONDS);
        
        return method.invoke(target, args);
    }
}
```

---

## Proxying Classes (CGLIB Alternative)

JDK dynamic proxy only works with interfaces. For classes, you need CGLIB or ByteBuddy:

```java
// JDK Proxy — requires interface
interface Greetable {
    String greet(String name);
}

class HelloService {
    public String greet(String name) { return "Hello, " + name; }
}

// This WON'T work with JDK Proxy (HelloService is a class, not interface)
// Proxy.newProxyInstance(..., new Class[]{HelloService.class}, handler); // Error!

// CGLIB approach (used by Spring)
// Creates a subclass of HelloService that intercepts method calls
```

**Key point:** JDK dynamic proxy = interface-based. CGLIB = class-based.

---

## Avoiding Stack Overflow

A common mistake is calling methods on the proxy inside the handler:

```java
class Handler implements InvocationHandler {
    private final Object target;
    
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
        // DANGEROUS: This calls toString() on the proxy, which calls invoke() again!
        System.out.println("Proxy: " + proxy);
        
        // BETTER: Call on the target
        System.out.println("Target: " + target);
        
        // Or avoid calling methods on proxy entirely
        return method.invoke(target, args);
    }
}
```

---

## Complete Example: AOP Framework

```java
import java.lang.reflect.*;
import java.util.*;

public class MiniAOP {

    public static <T> T createProxy(T target, Class<?>... interceptors) {
        Class<?>[] interfaces = target.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalArgumentException(
                "Cannot proxy class without interfaces. Use CGLIB.");
        }
        
        InvocationHandler handler = new CompositeHandler(target, interceptors);
        
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            interfaces,
            handler
        );
    }

    static class CompositeHandler implements InvocationHandler {
        private final Object target;
        private final List<MethodInterceptor> interceptors;
        
        CompositeHandler(Object target, Class<?>... interceptorClasses) {
            this.target = target;
            this.interceptors = new ArrayList<>();
            for (Class<?> cls : interceptorClasses) {
                try {
                    interceptors.add((MethodInterceptor) cls.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException("Cannot instantiate interceptor", e);
                }
            }
        }
        
        public Object invoke(Object proxy, Method method, Object[] args) 
                throws Throwable {
            // Build interceptor chain
            Invocation chain = () -> method.invoke(target, args);
            
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                final Invocation next = chain;
                MethodInterceptor interceptor = interceptors.get(i);
                chain = () -> interceptor.intercept(proxy, method, args, next);
            }
            
            return chain.invoke();
        }
    }
    
    @FunctionalInterface
    interface Invocation {
        Object invoke() throws Throwable;
    }
    
    interface MethodInterceptor {
        Object intercept(Object proxy, Method method, Object[] args, 
                        Invocation next) throws Throwable;
    }
}
```

---

## Production Incident: Proxy Memory Leak

**Incident:** A Spring application created dynamic proxies in a loop but never cleared references. Over 24 hours, the heap grew by 2GB until the application crashed with `OutOfMemoryError`.

**Root cause:** Each `Proxy.newProxyInstance()` call creates a new class in the JVM. These classes are never garbage collected because the classloader holds references. In a loop creating proxies for different interfaces, this is catastrophic.

**Fix:** Cache proxy instances. Create one proxy per target object, not per method call.

```java
// BAD: Creates new proxy every call
public Object getProxy(Object target) {
    return Proxy.newProxyInstance(...); // Memory leak!
}

// GOOD: Cache proxies
private final Map<Object, Object> proxyCache = new WeakHashMap<>();

public Object getProxy(Object target) {
    return proxyCache.computeIfAbsent(target, 
        t -> Proxy.newProxyInstance(...));
}
```

---

## Code Review Checklist

- [ ] Are `Class` and `Method` objects cached (not created per invocation)?
- [ ] Is `method.invoke(target, args)` called on the TARGET, not the PROXY?
- [ ] Is `InvocationTargetException` properly unwrapped?
- [ ] Are proxy instances cached (not recreated per request)?
- [ ] Is the correct ClassLoader used (not `null`)?
- [ ] Are multiple interfaces handled correctly?
- [ ] Is there documentation explaining what the proxy intercepts?

---

## Security Considerations

| Risk | Description | Mitigation |
|------|-------------|-----------|
| Arbitrary interception | Proxy can modify any method call | Restrict proxy creation to trusted code |
| Security bypass | Proxy can skip security checks | Ensure security interceptor runs first |
| DoS via proxy creation | Creating millions of proxies exhausts memory | Cache and limit proxy creation |

---

## Debugging Tips

1. **Print `proxy.getClass().getName()`** — Shows the generated proxy class name
2. **Use `method.toString()` in handler** — Identifies which method is being intercepted
3. **Check `proxy instanceof YourInterface`** — Proxy implements the same interfaces
4. **Use `-Djdk.proxy.Proxy.debug.dump`** — Dumps generated proxy class files
5. **Profile with `jvisualvm`** — Monitor proxy class loading

---

## Interview Questions

1. **What is a dynamic proxy and when would you use one?**
   - A proxy created at runtime that intercepts method calls; used for AOP, logging, transactions, caching

2. **What's the difference between JDK dynamic proxy and CGLIB?**
   - JDK proxy requires interfaces; CGLIB creates subclasses of concrete classes

3. **What does `InvocationHandler.invoke()` receive?**
   - The proxy instance, the method being called, and the arguments

4. **How do you avoid infinite recursion in an InvocationHandler?**
   - Don't call methods on the `proxy` parameter; call on the `target` object

5. **Name three real-world uses of dynamic proxy**
   - Spring `@Transactional`, JPA lazy loading, JUnit mocking, logging frameworks

---

## Summary

| Concept | Key Point |
|---------|-----------|
| `Proxy.newProxyInstance()` | Creates a proxy implementing given interfaces |
| `InvocationHandler` | Intercepts all method calls on the proxy |
| `method.invoke(target, args)` | Delegates to the real implementation |
| Interface-based | JDK proxy only works with interfaces |
| CGLIB alternative | For proxying concrete classes |
| Performance | Cache `Class`, `Method`, and proxy instances |
| Common uses | AOP, transactions, logging, caching, security |

---

*Next: [07 — Custom Annotations](../07-custom-annotations/README.md)*
