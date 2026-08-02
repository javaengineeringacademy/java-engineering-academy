# Module 14.5: Spring AOP (Aspect-Oriented Programming)

## 1. Introduction

Aspect-Oriented Programming (AOP) complements OOP by separating cross-cutting concerns (logging, security, transactions) from business logic. Spring AOP uses proxy-based implementation to enable declarative enterprise services.

## 2. Learning Objectives

- Understand AOP terminology: aspects, advice, pointcuts, join points
- Implement aspects with @Aspect annotation
- Apply Before, After, AfterReturning, AfterThrowing, and Around advice
- Define pointcut expressions to target methods
- Understand JDK vs CGLIB proxy mechanisms

## 3. Prerequisites

- Spring Fundamentals and Dependency Injection
- Java annotations and reflection
- Understanding of proxy patterns

## 4. Why This Concept Exists

Without AOP, cross-cutting concerns are duplicated across methods. AOP extracts these into reusable aspects, keeping business logic clean.

## 5. Problem Statement

Logging, security, and performance monitoring code scattered across every service method creates duplication and makes changes error-prone.

## 6. Theory

| Concept | Description |
|---------|-------------|
| **Aspect** | Module of cross-cutting concern |
| **Join Point** | Point during execution (method call) |
| **Advice** | Action taken at a join point |
| **Pointcut** | Expression matching join points |
| **Weaving** | Linking aspects with target objects |

**Advice Types**: @Before, @After, @AfterReturning, @AfterThrowing, @Around

## 7. Internal Working

Spring creates a proxy around target beans. Method calls hit the proxy first, which applies advice before/after invoking the actual method.

## 8. JVM Perspective

Proxies are created via java.lang.reflect.Proxy (JDK) or CGLIB bytecode generation. The proxy holds references to the target and advisor chain.

## 9. Memory Representation

```
Client → Proxy → Advice Chain → Target Method → Return
```

## 10. Architecture Diagram

```mermaid
graph TB
    A[Client] --> B[AOP Proxy]
    B --> C[Advice Chain]
    C --> D[@Before]
    C --> E[@Around]
    D --> F[Target Method]
    E --> F
    F --> G[@AfterReturning]
    F --> H[@AfterThrowing]
    G --> I[@After]
    H --> I
    I --> J[Return Value]
```

## 11. Flow Diagram

```mermaid
flowchart TD
    A[Method Call] --> B[Proxy Intercept]
    B --> C[Execute @Before]
    C --> D[Invoke Target]
    D --> E{Exception?}
    E -->|No| F[@AfterReturning]
    E -->|Yes| G[@AfterThrowing]
    F --> H[@After]
    G --> H
    H --> I[Return]
```

## 12. Syntax

```java
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {}

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint jp) {
        System.out.println("Before: " + jp.getSignature().getName());
    }

    @Around("execution(* com.example.service.*.*(..))")
    public Object logAround(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = jp.proceed();
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }
}
```

## 13. Easy Example

```java
@Aspect
@Component
public class SimpleAspect {
    @Before("execution(* com.example.service.*.*(..))")
    public void before() { System.out.println("Before method"); }

    @After("execution(* com.example.service.*.*(..))")
    public void after() { System.out.println("After method"); }
}

@Service
public class MyService {
    public void doWork() { System.out.println("Working..."); }
}

public class AopDemo {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AopConfig.class);
        ctx.getBean(MyService.class).doWork();
        ctx.close();
    }
}
```

## 14. Medium Example

```java
@Aspect
@Component
public class PerformanceAspect {
    @Around("execution(* com.example.service.*.*(..))")
    public Object measure(ProceedingJoinPoint jp) throws Throwable {
        long start = System.nanoTime();
        try { return jp.proceed(); }
        finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("[PERF] %s: %dms%n", jp.getSignature().getName(), ms);
        }
    }
}
```

## 15. Hard Example

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable { String operation(); }

@Aspect
@Component
public class AuditAspect {
    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint jp, Auditable auditable) throws Throwable {
        System.out.println("AUDIT: " + auditable.operation());
        try { Object r = jp.proceed(); System.out.println("AUDIT: SUCCESS"); return r; }
        catch (Exception e) { System.out.println("AUDIT: FAILED - " + e.getMessage()); throw e; }
    }
}
```

## 16. Enterprise Example

```java
@Aspect
@Component
public class SecurityAspect {
    private final Set<String> roles = Set.of("ADMIN");

    @Around("@annotation(secured)")
    public Object check(ProceedingJoinPoint jp, Secured secured) throws Throwable {
        for (String r : secured.roles()) {
            if (!roles.contains(r)) throw new SecurityException("No access: " + r);
        }
        return jp.proceed();
    }
}
```

## 17. Performance

| Operation | Impact |
|-----------|--------|
| Proxy creation | 10-50ms per bean |
| Method interception | 1-5μs overhead |
| CGLIB vs JDK | CGLIB slightly faster |

## 18. Time & Space Complexity

| Operation | Complexity |
|-----------|------------|
| Pointcut matching | O(n) where n = methods |
| Advice execution | O(1) per advice |
| Proxy creation | O(a) where a = aspects |

## 19. Thread Safety

Aspect instances are singletons by default. Ensure thread safety for any shared state within aspects.

## 20. Best Practices

1. Keep aspects focused on single concern
2. Use named pointcuts for reuse
3. Prefer @Around for performance measurement
4. Avoid expensive operations in aspects
5. Use @Order for aspect ordering

## 21. Common Mistakes

1. **Self-invocation**: AOP doesn't work on internal method calls
2. **Missing @EnableAspectJAutoProxy**: Aspects won't be processed
3. **Wrong pointcut**: Matching too broadly or narrowly

## 22. Pitfalls

- CGLIB proxies require non-final classes
- @AfterThrowing doesn't prevent exception propagation
- Multiple @Around advice ordering can be confusing

## 23. Debugging Tips

```java
// Check if bean is proxied
System.out.println("Is proxy: " + AopUtils.isAopProxy(bean));
System.out.println("Target class: " + AopUtils.getTargetClass(bean).getName());
```

## 24. Comparison Table

| Feature | Spring AOP | AspectJ |
|---------|-----------|---------|
| Weaving | Runtime proxy | Compile/load time |
| Performance | Good | Better |
| Scope | Spring beans only | Any Java class |
| Setup | Simple | Complex |

## 25. Decision Tree

- Cross-cutting concern in Spring? → Spring AOP
- Need to advise non-Spring objects? → AspectJ
- Performance critical? → AspectJ compile-time
- Simple logging/security? → Spring AOP

## 26. Interview Questions (15+)

1. What is AOP? How does it complement OOP?
2. What is the difference between Join Point and Pointcut?
3. Explain all five advice types.
4. What is the difference between @After and @AfterReturning?
5. How does @Around advice work?
6. What is JDK dynamic proxy vs CGLIB?
7. Why doesn't AOP work with self-invocation?
8. What is @EnableAspectJAutoProxy?
9. How do you order multiple aspects?
10. What pointcut designators are available?
11. Can AOP be used with prototype beans?
12. What is the performance impact of AOP?
13. How do you test aspects?
14. What is load-time weaving?
15. When would you use AspectJ over Spring AOP?

## 27. Exercises

**Level 1**: Create a logging aspect that logs all service method calls.

**Level 2**: Create a retry aspect that retries failed methods up to N times.

**Level 3**: Create an audit trail aspect that records method calls to a database.

## 28. Summary

Spring AOP provides a clean way to separate cross-cutting concerns using proxy-based implementation. Use @Aspect, define pointcuts, and apply advice to enhance business logic without modifying it.

## 29. References

- [Spring AOP Documentation](https://docs.spring.io/spring-framework/reference/core.html#aop)
- [AspectJ Pointcut Expressions](https://docs.spring.io/spring-framework/reference/core.html#aop-pointcuts)
- *Spring in Action* by Craig Walls - Chapter 6
