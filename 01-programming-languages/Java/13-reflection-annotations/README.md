# Reflection and Annotations Module

> **Difficulty:** ⭐⭐⭐⭐ Advanced  
> **Reading:** 30 min | **Practice:** 40 min | **Total:** 70 min

## Overview
This module covers Java Reflection API and Annotations, enabling runtime inspection and modification of classes, methods, fields, and creating custom annotations.

## Key Concepts

### 1. Reflection API
- Runtime inspection of classes, interfaces, fields, and methods
- Dynamic object creation and method invocation
- Access to private members (with `setAccessible(true)`)

### 2. Annotations
- Metadata for Java code
- Compile-time and runtime processing
- Built-in annotations: `@Override`, `@Deprecated`, `@SuppressWarnings`

### 3. Custom Annotations
- Create with `@interface`
- Retention policies: SOURCE, CLASS, RUNTIME
- Target types: METHOD, FIELD, CLASS, etc.

### 4. Dynamic Proxy
- Create proxy instances at runtime
- Implement `InvocationHandler`
- AOP and middleware patterns

## Module Structure
- `ReflectionBasics.java` - Core reflection operations
- `AnnotationsDemo.java` - Working with annotations
- `FieldManipulation.java` - Field access and modification
- `MethodInvocation.java` - Method invocation via reflection
- `DynamicProxyExample.java` - Dynamic proxy pattern
- `RealWorldReflection.java` - Practical applications

## Performance

| Operation | Relative Cost | Notes |
|-----------|--------------|-------|
| Direct field access | O(1) | Baseline |
| Reflection field access | ~5-20x slower | Includes security checks |
| Method invocation | O(1) | |
| Reflection method invocation | ~10-50x slower | Includes lookup, boxing/unboxing |
| Constructor invocation | O(1) | |
| Reflective constructor | ~10-30x slower | |
| `Class.forName()` | ~100-1000μs | Includes classloading |

**Benchmarks (approximate):**
- Direct call: ~5ns per invocation
- Reflective call: ~50-200ns per invocation
- Cached Method + setAccessible(true): ~15-30ns per invocation
- `MethodHandle`: ~10-20ns per invocation (near-direct performance)

**Optimization tips:**
1. Cache `Class`, `Method`, `Field` objects — avoid repeated lookups
2. Use `setAccessible(true)` to skip security checks after caching
3. Use `MethodHandle` (Java 7+) for near-direct invocation performance
4. Prefer annotations with compile-time processing (APT) over runtime reflection
5. Use `isAccessible()` checks before expensive operations

## Examples

```java
// Reflection — inspecting classes at runtime
import java.lang.reflect.*;

Class<?> clazz = Class.forName("com.example.User");
Field[] fields = clazz.getDeclaredFields();
Method[] methods = clazz.getDeclaredMethods();

// Accessing private fields
Field nameField = clazz.getDeclaredField("name");
nameField.setAccessible(true);
Object value = nameField.get(instance);
nameField.set(instance, "new value");

// Invoking methods dynamically
Method method = clazz.getMethod("getName");
Object result = method.invoke(instance);

// Creating instances
Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
Object newUser = constructor.newInstance("Alice", 30);

// Dynamic Proxy
interface Greeting {
    String greet(String name);
}
Greeting proxy = (Greeting) Proxy.newProxyInstance(
    Greeting.class.getClassLoader(),
    new Class[]{Greeting.class},
    (p, method, args) -> {
        System.out.println("Before: " + method.getName());
        String result = "Hello, " + args[0];
        System.out.println("After");
        return result;
    }
);
proxy.greet("World"); // prints Before/After + returns "Hello, World"

// Custom Annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateNotNull {}

// Annotation processor at runtime
public static void validate(Object obj) throws IllegalAccessException {
    for (Field field : obj.getClass().getDeclaredFields()) {
        if (field.isAnnotationPresent(ValidateNotNull.class)) {
            field.setAccessible(true);
            if (field.get(obj) == null) {
                throw new ValidationException(field.getName() + " must not be null");
            }
        }
    }
}
```

## Internal Working

**How Reflection works under the hood:**

1. **Class loading**: When `Class.forName()` is called, the JVM's class loader loads the `.class` file into the metaspace (previously permgen), creating a `Class` object that represents the type's metadata.

2. **Field/Method access**: Reflection looks up field/method metadata from the `Class` object's internal structures (the constant pool, field table, method table). Each lookup involves string comparisons and security checks.

3. **Security checks**: Every reflective access goes through `AccessController.doPrivileged()` and checks `setAccessible()` flags. This is the main performance cost.

4. **Dynamic Proxy**: `Proxy.newProxyInstance()` generates a new class at runtime that implements the specified interfaces. The proxy class extends `java.lang.reflect.Proxy` and delegates all method calls to the `InvocationHandler`.

5. **Annotations**: The JVM stores annotation data in the class file's RuntimeVisibleAnnotations attribute. At runtime, this data is parsed into `Annotation` objects accessible via reflection APIs.

6. **Method handles** (Java 7+): `MethodHandle` provides a more direct invocation path, bypassing some reflection overhead by creating a typed, callable reference.

## Why This Concept Exists

Reflection and annotations exist because:

1. **Frameworks need metadata** — Spring, Hibernate, JUnit need to inspect and modify classes at runtime without compile-time dependencies
2. **Decoupling** — Code can operate on types it doesn't know at compile time (dependency injection, serialization)
3. **Convention over configuration** — Annotations like `@Autowired`, `@Entity` reduce XML/boilerplate configuration
4. **Tooling support** — IDEs, debuggers, and profilers use reflection to inspect objects
5. **Dynamic behavior** — Proxy, AOP, and monitoring need to intercept method calls without modifying source code

Without reflection, every framework would require compile-time code generation or extensive configuration files.
- `ReflectionBasics.java` - Comprehensive reflection examples

## Common Mistakes
1. Not handling `ClassNotFoundException` and `NoSuchMethodException`
2. Breaking encapsulation unnecessarily
3. Performance overhead of reflection
4. Not considering security implications

## Interview Questions
1. What is Reflection in Java?
2. How do you access private fields via reflection?
3. What are annotations and their retention policies?
4. How do you create custom annotations?
5. What is Dynamic Proxy and when would you use it?
6. What are the performance implications of reflection?

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)

## Prerequisites

- [OOP](../02-oop/README.md)

## Related Topics

- [Logging](../14-logging/README.md)

## Next

- [Logging](../14-logging/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Runtime type information |
| Complexity | O(1) to O(n) |
| Thread Safe | Yes |
| Ordered | No |
| Allows Null | Yes |
| Best Alternative | Direct access (for performance) |
| When to Use | Frameworks, metadata |
| When to Avoid | Normal code |
