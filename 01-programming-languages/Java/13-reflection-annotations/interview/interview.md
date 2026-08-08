# Reflection & Annotations — Interview Questions

## Fundamentals

### 1. What is reflection and when should you use it?

**Answer:** Reflection is the ability of a program to examine and modify its own structure at runtime. Use it when building frameworks, plugin systems, or serialization libraries that need to work with unknown types. Avoid it in business logic and performance-critical code.

---

### 2. What are the three ways to get a Class object?

**Answer:**
- `.class` literal: `String.class` — compile-time known type
- `getClass()`: `"hello".getClass()` — runtime type of an instance
- `Class.forName()`: `Class.forName("java.lang.String")` — dynamic class name from string

---

### 3. Explain the reflection API hierarchy

**Answer:**
- `Class` — represents a loaded class
- `Field` — represents a member variable
- `Method` — represents a method
- `Constructor` — represents a constructor
- `Modifier` — utility for decoding access modifiers
- `Proxy` — creates dynamic proxy instances

---

## Field Access

### 4. How do you access a private field via reflection?

**Answer:**
```java
Field field = clazz.getDeclaredField("secret");
field.setAccessible(true);
Object value = field.get(obj);
```

### 5. Can you modify a final field? What are the risks?

**Answer:** Technically yes in most JVMs by using `field.set()`. However, the JVM may have inlined the original value as a constant, making the change invisible in some code paths. This is unreliable and should be avoided.

### 6. What is the difference between `getFields()` and `getDeclaredFields()`?

**Answer:**
- `getFields()`: Returns only public fields from this class AND inherited public fields
- `getDeclaredFields()`: Returns ALL fields declared in this class (any access modifier), but NOT inherited fields

---

## Method Invocation

### 7. How do you handle exceptions thrown by a reflectively invoked method?

**Answer:** Catch `InvocationTargetException` and unwrap it with `getTargetException()`:
```java
try {
    method.invoke(obj, args);
} catch (InvocationTargetException e) {
    Throwable actual = e.getTargetException();
    // Handle the actual exception
}
```

### 8. How do you resolve overloaded methods via reflection?

**Answer:** Specify exact parameter types in `getDeclaredMethod()`:
```java
Method m1 = clazz.getDeclaredMethod("process", int.class);
Method m2 = clazz.getDeclaredMethod("process", String.class);
```

### 9. What is the performance impact of `setAccessible(true)` vs without?

**Answer:** `setAccessible(true)` skips the security check on each invocation. In hot loops, calling it once outside the loop vs inside can give 5-10x improvement for reflective field/method access.

---

## Dynamic Proxy

### 10. What is a dynamic proxy and when would you use one?

**Answer:** A dynamic proxy is a proxy instance created at runtime that implements one or more interfaces and intercepts all method calls via an `InvocationHandler`. Used for AOP, logging, transactions, caching, and security.

### 11. What is the difference between JDK dynamic proxy and CGLIB?

**Answer:**
- JDK Proxy: Creates a proxy that implements interfaces. The target must implement at least one interface.
- CGLIB: Creates a subclass of the target class. Can proxy concrete classes without interfaces, but cannot proxy final classes or methods.

### 12. How do you avoid infinite recursion in an InvocationHandler?

**Answer:** Never call methods on the `proxy` parameter. Always call methods on the `target` object:
```java
// BAD: proxy.toString() -> invoke() -> proxy.toString() -> ...
// GOOD: method.invoke(target, args)
```

---

## Annotations

### 13. What are the three retention policies and when do you use each?

**Answer:**
- `SOURCE`: Only in source code, deleted after compilation. Used for `@Override`, `@SuppressWarnings`.
- `CLASS`: In .class file but not available at runtime. Used for bytecode manipulation (AspectJ).
- `RUNTIME`: Available at runtime via reflection. Used for frameworks like Spring, JPA.

### 14. Can annotations have constructors or instance fields?

**Answer:** No. Annotations are special interface types. They cannot have constructors, instance fields, or methods with parameters. Elements are declared like abstract methods.

### 15. What is the difference between @Inherited and @Repeatable?

**Answer:**
- `@Inherited`: Allows a class-level annotation to be inherited by subclasses
- `@Repeatable`: Allows an annotation to be applied multiple times to the same element

---

## Annotation Processing

### 16. When does annotation processing happen?

**Answer:** Standard annotation processing happens at compile time via `AbstractProcessor`. Runtime annotation processing happens via reflection when annotations have `RUNTIME` retention.

### 17. How does a processor claim annotations?

**Answer:** If `process()` returns `true`, the processor claims those annotations and other processors will not see them in subsequent rounds. Return `false` to let other processors also process them.

---

## Framework Questions

### 18. How does Spring's @Autowired work internally?

**Answer:**
1. Spring scans for @Component, @Service, @Repository classes
2. Creates instances via Constructor.newInstance()
3. Finds @Autowired fields via getDeclaredFields()
4. Calls setAccessible(true) on each field
5. Calls field.set(instance, dependency) to inject

### 19. How does JPA map Java objects to database tables?

**Answer:** JPA uses reflection to read @Entity, @Table, @Column, @Id annotations. It reads field names and types to generate SQL DDL. At runtime, it uses field.get() to read values for INSERT/UPDATE statements.

### 20. Why is Lombok faster than reflection-based alternatives?

**Answer:** Lombok generates getters, setters, and other code at compile time. The generated code is regular Java with no reflection overhead. Runtime frameworks that use reflection have the overhead of field lookups, security checks, and boxing/unboxing.

---

## Coding Challenges

### 21. Write a method that copies all fields from one object to another using reflection.

```java
public static void copy(Object source, Object target) throws Exception {
    for (Field f : source.getClass().getDeclaredFields()) {
        if (Modifier.isStatic(f.getModifiers())) continue;
        Field tf = target.getClass().getDeclaredField(f.getName());
        f.setAccessible(true);
        tf.setAccessible(true);
        tf.set(target, f.get(source));
    }
}
```

### 22. Write a method that creates an instance from a Map of field values.

```java
public static <T> T fromMap(Class<T> clazz, Map<String, Object> map) throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
        Field f = clazz.getDeclaredField(entry.getKey());
        f.setAccessible(true);
        f.set(instance, entry.getValue());
    }
    return instance;
}
```

### 23. Write a simple annotation-based validation framework.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {}

public static List<String> validate(Object obj) throws Exception {
    List<String> errors = new ArrayList<>();
    for (Field f : obj.getClass().getDeclaredFields()) {
        if (f.isAnnotationPresent(NotNull.class)) {
            f.setAccessible(true);
            if (f.get(obj) == null) errors.add(f.getName() + " is null");
        }
    }
    return errors;
}
```
