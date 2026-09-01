# Module 13: Reflection & Annotations

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 35 min | **Practice:** 45 min | **Total:** 80 min

## Overview

Java Reflection enables inspection and modification of classes, methods, fields, and constructors at runtime. Annotations provide metadata for code generation, validation, and framework configuration. Together, they power frameworks like Spring, Hibernate, Jackson, and JUnit. This module covers the Reflection API, custom annotations, annotation processing, and dynamic proxies.

## Learning Objectives

- [ ] Inspect classes, methods, fields, and constructors at runtime
- [ ] Create and use custom annotations
- [ ] Implement annotation processors for compile-time code generation
- [ ] Create dynamic proxies for AOP and interceptors
- [ ] Understand reflection performance implications
- [ ] Apply reflection in framework development

## Prerequisites

- Java fundamentals and OOP
- Understanding of interfaces and abstract classes
- Basic knowledge of generics

## History

- **1996** — Java 1.0 introduced basic reflection for introspection
- **1998** — Java 1.2 added `getDeclaredMethod()` and `setAccessible()` for private access
- **2004** — Java 5 introduced annotations and `RetentionPolicy`
- **2006** — Java 6 added annotation processing API (`javax.annotation.processing`)
- **2011** — Java 7 added `MethodHandle` as lightweight reflection alternative
- **2014** — Java 8 added `Type annotations` and repeated annotations
- **2017** — Java 9 added module system affecting reflection access
- **2023** — Java 21 added pattern matching reducing need for some reflection patterns

## Production Notes

- **Where is it used?** In every Java framework (Spring, Hibernate, Jackson, JUnit)
- **Why is it useful?** Enables dynamic behavior, code generation, and framework magic
- **When should it be avoided?** When performance is critical; reflection is slower than direct calls
- **Alternative?** Compile-time annotation processing, code generation, static typing

## Why This Concept Exists

Without reflection:
- Frameworks cannot discover and configure beans
- Serialization cannot inspect object structure
- Testing cannot discover and run test methods
- Dynamic proxies cannot intercept method calls
- Code cannot be generated from annotations

## Core Concepts

### Reflection API Hierarchy

```
java.lang.Class
├── Fields (java.lang.reflect.Field)
├── Methods (java.lang.reflect.Method)
├── Constructors (java.lang.reflect.Constructor)
├── Interfaces (java.lang.Class)
└── Annotations (java.lang.annotation.Annotation)

java.lang.reflect.Proxy
├── Dynamic Proxy (java.lang.reflect.InvocationHandler)
└── Proxy.newProxyInstance()
```

### Annotation Types

| Retention | Target | Purpose |
|-----------|--------|---------|
| SOURCE | Classes, methods | Compile-time only (e.g., `@Override`) |
| CLASS | Classes, methods | Bytecode, not runtime (e.g., `@Generated`) |
| RUNTIME | Classes, methods | Available at runtime via reflection (e.g., `@Autowired`) |

### Dynamic Proxy

```java
public interface UserService {
    User findById(Long id);
}

public class UserServiceProxy implements InvocationHandler {
    private final Object target;
    
    public UserServiceProxy(Object target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long duration = System.currentTimeMillis() - start;
        System.out.println(method.getName() + " took " + duration + "ms");
        return result;
    }
}

// Usage
UserService service = (UserService) Proxy.newProxyInstance(
    UserService.class.getClassLoader(),
    new Class[]{UserService.class},
    new UserServiceProxy(new UserServiceImpl())
);
```

## Internal Working

### Reflection Performance

```
Direct call: ~1ns
MethodHandle: ~2ns
Reflection: ~50ns
Dynamic Proxy: ~100ns
```

- Reflection is 50x slower than direct calls
- Dynamic proxy adds another 2x overhead
- Use caching for repeated reflective operations
- Consider MethodHandle as lighter alternative

### Annotation Processing Flow

```
Source Code → Annotation Processor → Generated Code → Compiler
```

## Syntax

```java
// Getting Class object
Class<?> clazz = Class.forName("com.example.User");
Class<?> clazz = User.class;
Class<?> clazz = user.getClass();

// Inspecting fields
Field[] fields = clazz.getDeclaredFields();
Field nameField = clazz.getDeclaredField("name");
nameField.setAccessible(true);
Object value = nameField.get(user);
nameField.set(user, "New Name");

// Inspecting methods
Method[] methods = clazz.getDeclaredMethods();
Method getName = clazz.getDeclaredMethod("getName");
Object result = getName.invoke(user);

// Creating annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecutionTime {}

// Using annotations
@LogExecutionTime
public void process() { }

// Processing annotations
Method[] methods = clazz.getDeclaredMethods();
for (Method method : methods) {
    if (method.isAnnotationPresent(LogExecutionTime.class)) {
        // Process annotation
    }
}
```

## Examples

### Easy: Class Introspection
```java
public class ClassInspector {
    public static void inspect(Class<?> clazz) {
        System.out.println("Class: " + clazz.getName());
        System.out.println("Superclass: " + clazz.getSuperclass().getName());
        System.out.println("Interfaces: " + List.of(clazz.getInterfaces()));
        
        System.out.println("Fields:");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  " + field.getName() + ": " + field.getType().getName());
        }
        
        System.out.println("Methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + method.getName() + "()");
        }
    }
}
```

### Medium: Custom Annotation
```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateNotNull {
    String message() default "Field cannot be null";
}

public class Validator {
    public static void validate(Object obj) throws ValidationException {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ValidateNotNull.class)) {
                field.setAccessible(true);
                try {
                    if (field.get(obj) == null) {
                        ValidateNotNull annotation = field.getAnnotation(ValidateNotNull.class);
                        throw new ValidationException(annotation.message());
                    }
                } catch (IllegalAccessException e) {
                    throw new ValidationException("Cannot access field: " + field.getName());
                }
            }
        }
    }
}

class User {
    @ValidateNotNull(message = "Name is required")
    private String name;
    
    @ValidateNotNull(message = "Email is required")
    private String email;
}
```

### Hard: Dynamic Proxy
```java
import java.lang.reflect.*;
import java.util.*;

public class LoggingProxy implements InvocationHandler {
    private final Object target;
    private final List<String> logs = new ArrayList<>();
    
    public LoggingProxy(Object target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String log = String.format("Calling %s(%s)", method.getName(), 
            args == null ? "" : Arrays.toString(args));
        logs.add(log);
        
        long start = System.nanoTime();
        Object result = method.invoke(target, args);
        long duration = System.nanoTime() - start;
        
        logs.add(String.format("Called %s in %dns", method.getName(), duration));
        return result;
    }
    
    public List<String> getLogs() { return Collections.unmodifiableList(logs); }
    
    @SuppressWarnings("unchecked")
    public static <T> T create(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
            interfaceType.getClassLoader(),
            new Class[]{interfaceType},
            new LoggingProxy(target)
        );
    }
}
```

### Enterprise: Annotation Processor
```java
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes("com.example.GenerateBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class BuilderProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateBuilder.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) element;
                generateBuilder(classElement);
            }
        }
        return true;
    }
    
    private void generateBuilder(TypeElement classElement) {
        // Generate builder class
        processingEnv.getMessager().printMessage(
            Diagnostic.Kind.NOTE,
            "Generating builder for " + classElement.getSimpleName()
        );
    }
}
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| Class.forName() | ~1ms | First call, cached after |
| getDeclaredField() | ~10μs | Use caching |
| Field.get() | ~100ns | With setAccessible(true) |
| Method.invoke() | ~50ns | Slower than direct call |
| Proxy.newProxyInstance() | ~10μs | Creation cost |

## Best Practices

**Do's:**
- Cache reflective lookups
- Use `setAccessible(true)` for performance
- Consider MethodHandle for better performance
- Use annotation processing for compile-time code generation
- Validate reflective access with SecurityManager

**Don'ts:**
- Don't use reflection in hot loops
- Don't access private fields without good reason
- Don't ignore `IllegalAccessException`
- Don't use reflection when direct calls are possible
- Don't forget about module system restrictions (Java 9+)

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Reflecting in hot loop | Performance penalty | Cache reflective lookups |
| Not setting accessible | InaccessibleObjectException | Use `setAccessible(true)` |
| Ignoring module system | Inaccessible in Java 9+ | Use `--add-opens` or module exports |
| Not caching annotations | Performance penalty | Cache annotation lookups |
| Using reflection unnecessarily | Complexity | Use direct calls when possible |

## Interview Questions

### Q1: What is reflection and when to use it?
**Answer:** Reflection inspects and modifies classes at runtime. Use for frameworks (Spring, Hibernate), serialization (Jackson), testing (JUnit), and dynamic proxies. Avoid in hot paths due to performance.

### Q2: What is the difference between `getClass()` and `.class`?
**Answer:** `getClass()` returns runtime class of an object. `.class` returns Class object at compile time. Both return `Class<?>`, but `.class` is faster and works for primitives.

### Q3: What is a dynamic proxy?
**Answer:** A proxy instance created at runtime that implements one or more interfaces. The `InvocationHandler` intercepts method calls. Used for AOP, logging, transaction management.

### Q4: What is the difference between `Field.get()` and `Method.invoke()`?
**Answer:** `Field.get()` reads field value. `Method.invoke()` calls a method. Both are reflective operations with similar performance. Use the appropriate one for your use case.

### Q5: What is annotation processing?
**Answer:** Compile-time processing of annotations to generate code, validate, or transform. Implement `AbstractProcessor` to process annotations. Used by Lombok, MapStruct, Dagger.

### Q6: How does the module system affect reflection?
**Answer:** Java 9+ modules restrict reflective access. Use `--add-opens` to open packages for reflection, or export packages in module-info.java.

### Q7: What is the difference between `@Retention(RUNTIME)` and `@Retention(CLASS)`?
**Answer:** `RUNTIME` annotations are available via reflection at runtime. `CLASS` annotations are in bytecode but not available at runtime. Use `RUNTIME` for reflective processing.

### Q8: What is MethodHandle and how does it compare to reflection?
**Answer:** MethodHandle is a typed, direct reference to a method. It's faster than reflection (~2ns vs ~50ns) but less flexible. Use for performance-critical reflective calls.

### Q9: What is the difference between `getDeclaredField()` and `getField()`?
**Answer:** `getDeclaredField()` returns fields declared in the class (including private). `getField()` returns only public fields (including inherited). Use `getDeclaredField()` for private access.

### Q10: What are type annotations (Java 8+)?
**Answer:** Annotations on types (not just declarations). Example: `List<@NonNull String>`. Used for nullability checking, type validation, and static analysis tools.

## Cross-References

- **Previous Module:** [12 - Testing](../12-testing/)
- **Next Module:** [14 - Logging](../14-logging/)
- **Related:** [06 - Generics](../06-generics/) — type erasure and reflection
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — thread safety in reflection
- **Related:** [11 - Design Patterns](../11-design-patterns/) — Proxy pattern

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| InaccessibleObjectException | Module opens | Add `--add-opens` or check module-info |
| NoSuchFieldException | Debug field name | Verify exact field name; check inheritance |
| InvocationTargetException | Check cause | `e.getCause()` reveals actual exception |
| Performance issue | JMH benchmark | Benchmark reflective vs direct calls |
| Annotation not found | Check retention | Verify `@Retention(RUNTIME)` |

## Code Review Checklist

- [ ] Reflection used only when necessary
- [ ] Reflective lookups are cached
- [ ] `setAccessible(true)` used appropriately
- [ ] Module system restrictions respected
- [ ] Annotations have correct retention policy
- [ ] Dynamic proxies implement correct interfaces

## Architecture Considerations

Reflection and annotations are the foundation of Java frameworks. At scale, reflection performance impacts throughput; annotation processing affects build time. For microservices, reflection enables dependency injection; annotations provide declarative configuration.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Reflection | Framework introspection | Pros: Dynamic; Cons: Slow, fragile |
| Annotation processing | Code generation | Pros: Compile-time; Cons: Complexity |
| Dynamic proxy | AOP, interceptors | Pros: Transparent; Cons: Performance |
| MethodHandle | Performance-critical reflection | Pros: Fast; Cons: Less flexible |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Reflection bypassing access control | Unauthorized access | Restrict reflective access with SecurityManager |
| Annotation injection | Code injection | Validate annotation values |
| Proxy manipulation | Security bypass | Verify proxy types |
| Dynamic class loading | Remote code execution | Restrict class loading sources |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Basic reflection | N/A — foundational |
| Java 5 | Annotations | Use annotations for metadata |
| Java 8 | Type annotations | Use for nullability checking |
| Java 9 | Module system | Update reflective access |
| Java 17 | Sealed classes | Reduce need for reflection |
| Java 21 | Pattern matching | Reduce need for instanceof reflection |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Basic reflection | Java 1.0 | Stable |
| Annotations | Java 5 | Stable |
| Annotation processing | Java 6 | Stable |
| Type annotations | Java 8 | Stable |
| Module system | Java 9 | Stable |
| Pattern matching | Java 21 | Stable |

## Production Incidents

### Incident 1: Reflection Performance Bottleneck

**Problem:** A serialization library using reflection was 10x slower than expected.
**Cause:** Field lookups were not cached; every call used `getDeclaredField()`.
**Impact:** Serialization latency increased from 1ms to 10ms per object.
**Detection:** Profiling showed 80% of time in reflection calls.
**Solution:** Cached field and method lookups in ConcurrentHashMap.
**Prevention:** Always cache reflective lookups; use MethodHandle for performance.

### Incident 2: Module System Breaking Reflection

**Problem:** After upgrading to Java 17, Spring-based application failed to start with InaccessibleObjectException.
**Cause:** Java 9+ modules restrict reflective access to internal APIs.
**Impact:** Application couldn't start; 100% downtime.
**Detection:** Startup failure with InaccessibleObjectException.
**Solution:** Added `--add-opens` flags; updated Spring to version that handles modules.
**Prevention:** Test with target Java version; use `--add-opens` for framework access.

### Incident 3: Annotation Not Found at Runtime

**Problem:** A custom annotation was not detected by the framework; features silently disabled.
**Cause:** Annotation was defined with `@Retention(RetentionPolicy.CLASS)` instead of `RUNTIME`.
**Impact:** Features silently disabled; 20% of requests missing functionality.
**Detection:** Framework logs showed annotation not found; code review revealed retention policy.
**Solution:** Changed to `@Retention(RetentionPolicy.RUNTIME)`.
**Prevention:** Verify retention policy matches usage; add tests for annotation detection.

## Production Checklist

- [ ] Reflection used only when necessary
- [ ] Reflective lookups are cached
- [ ] `setAccessible(true)` used appropriately
- [ ] Module system restrictions respected
- [ ] Annotations have correct retention policy
- [ ] Dynamic proxies implement correct interfaces
- [ ] Performance tested for reflective operations
- [ ] SecurityManager configured for reflection

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses basic reflection; doesn't understand performance; ignores modules |
| Intermediate | Caches lookups; uses annotations; understands retention policies |
| Advanced | Creates custom annotations; implements annotation processors; uses dynamic proxies |
| Expert | Designs framework APIs; optimizes reflection; mentors on reflection patterns |

## Common Myths

1. **Myth**: Reflection is always slow
   **Truth**: Reflection is slower than direct calls but can be optimized with caching and MethodHandle. For most use cases, the overhead is acceptable.

2. **Myth**: Annotations are just comments
   **Truth**: Annotations are metadata that can be processed at compile-time or runtime. They power frameworks and enable declarative programming.

3. **Myth**: Dynamic proxy is the only way to do AOP
   **Truth**: AspectJ, ByteBuddy, and cglib provide alternative AOP implementations. Dynamic proxy is simplest but limited to interfaces.

4. **Myth**: Reflection bypasses all access controls
   **Truth**: Java 9+ modules restrict reflective access. SecurityManager can limit reflective operations.

5. **Myth**: Annotation processing is only for compile-time
   **Truth**: Annotations can be processed at runtime via reflection. Compile-time processing is for code generation.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Runtime class inspection and modification |
| API | Class, Field, Method, Constructor, Proxy |
| Annotations | Metadata for code generation, validation |
| Retention | SOURCE, CLASS, RUNTIME |
| Dynamic Proxy | Runtime proxy creation with InvocationHandler |
| Performance | 50x slower than direct calls |
| Best practice | Cache reflective lookups |
| Common mistake | Not caching field/method lookups |
| When to use | Frameworks, serialization, AOP |
| When to avoid | Hot paths, performance-critical code |
