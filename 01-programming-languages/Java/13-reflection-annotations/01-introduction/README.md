# 01 — Introduction to Reflection

## What Is Reflection?

Reflection is the ability of a running Java program to examine and modify its own structure — classes, methods, fields, constructors, and even annotations — at runtime. It is Java's self-introspection mechanism.

Think of it this way: normally, when you write `String s = "hello"`, the compiler knows exactly what `String` is, what methods it has, and what fields exist. With reflection, you can discover all of that information *while the program is running*, without the compiler knowing anything about the types involved.

```java
// Normal compile-time code
String s = "hello";
int len = s.length(); // Compiler knows String.length() exists

// Reflective code — no compile-time knowledge of the type
Object obj = getSomeObject(); // Could be anything
Class<?> clazz = obj.getClass();
Method method = clazz.getMethod("length");
int len = (int) method.invoke(obj);
```

---

## Why Does Reflection Exist?

### The Framework Problem

Frameworks face a fundamental challenge: they need to work with user code that doesn't exist yet. When Spring starts up, it needs to instantiate classes the developer hasn't written yet. When JUnit runs, it needs to find and invoke methods annotated with `@Test`. When Jackson deserializes JSON, it needs to map JSON keys to Java field names.

Without reflection, frameworks would require every user to write configuration files specifying every class, method, and field. Reflection lets frameworks *discover* this information automatically.

### The Plugin Problem

Plugin architectures need to load classes dynamically. An IDE doesn't know at compile time what plugins will be installed. A game engine doesn't know what mods users will create. Reflection enables loading and using unknown classes at runtime.

### The Configuration Problem

Sometimes you want to read configuration values and inject them into fields without writing boilerplate code for every field. Reflection automates this.

---

## When to Use Reflection

### Use Reflection When:

1. **Building frameworks** — Spring, Hibernate, JUnit, Jackson all use reflection heavily
2. **Creating plugin systems** — Loading unknown classes at runtime
3. **Writing test utilities** — Accessing private fields/methods for testing
4. **Implementing serialization** — Mapping objects to/from JSON, XML, etc.
5. **Building dependency injection containers** — Automatically wiring dependencies
6. **Creating AOP (Aspect-Oriented Programming) proxies** — Intercepting method calls

### Avoid Reflection When:

1. **Business logic** — If you know the type at compile time, use it directly
2. **Performance-critical code** — Reflection is 10-50x slower than direct access
3. **Simple delegation** — If you're just calling a method on a known type, don't reflect
4. **Compile-time verification** — Reflection errors appear at runtime, not compile time
5. **Security-sensitive code** — Reflection can bypass access controls

---

## The Reflection API — Overview

The reflection API lives in `java.lang.reflect` and consists of these core classes:

| Class | Purpose |
|-------|---------|
| `Class<T>` | Represents a loaded class or interface |
| `Field` | Represents a single field (member variable) |
| `Method` | Represents a single method |
| `Constructor<T>` | Represents a single constructor |
| `Modifier` | Utility for decoding access modifiers |
| `Array` | Utility for dynamically creating and accessing arrays |
| `Proxy` | Creates dynamic proxy instances |
| `InvocationHandler` | Handles method invocations on proxy instances |

### Supporting Types

| Type | Purpose |
|------|---------|
| `Parameter` | Represents a method/constructor parameter |
| `AnnotatedElement` | Common interface for types that can be annotated |
| `GenericArrayType` | Represents a generic array type |
| `ParameterizedType` | Represents a parameterized type (e.g., `List<String>`) |
| `TypeVariable` | Represents a type variable (e.g., `T` in `List<T>`) |
| `WildcardType` | Represents a wildcard type (e.g., `? extends Number`) |

---

## Getting a Class Object — Three Ways

The entry point to all reflection is obtaining a `Class` object. There are three ways:

```java
// Way 1: Using .class literal (compile-time known)
Class<String> clazz1 = String.class;

// Way 2: Using getClass() on an instance (runtime object)
String s = "hello";
Class<?> clazz2 = s.getClass();

// Way 3: Using Class.forName() with fully-qualified name (dynamic)
Class<?> clazz3 = Class.forName("java.lang.String");
```

### When to Use Each

| Method | When to Use |
|--------|------------|
| `String.class` | You know the exact type at compile time |
| `obj.getClass()` | You have an instance and want its runtime type |
| `Class.forName(name)` | The type name comes from a string (config, user input) |

---

## What Can You Do with Reflection?

### 1. Inspect a Class

```java
Class<?> clazz = Class.forName("com.example.User");

// Get all declared fields (including private)
Field[] fields = clazz.getDeclaredFields();
for (Field field : fields) {
    System.out.println(field.getName() + " : " + field.getType().getSimpleName());
}

// Get all declared methods (including private)
Method[] methods = clazz.getDeclaredMethods();
for (Method method : methods) {
    System.out.println(method.getName() + "()");
}

// Get all constructors
Constructor<?>[] constructors = clazz.getDeclaredConstructors();
```

### 2. Create Instances

```java
Class<?> clazz = Class.forName("com.example.User");

// Using default constructor
Object obj = clazz.getDeclaredConstructor().newInstance();

// Using parameterized constructor
Constructor<?> ctor = clazz.getDeclaredConstructor(String.class, int.class);
Object obj2 = ctor.newInstance("Alice", 30);
```

### 3. Access and Modify Fields

```java
Field nameField = clazz.getDeclaredField("name");
nameField.setAccessible(true); // Bypass private access
nameField.set(obj, "Bob");     // Set value
String name = (String) nameField.get(obj); // Get value
```

### 4. Invoke Methods

```java
Method method = clazz.getDeclaredMethod("greet", String.class);
method.setAccessible(true);
Object result = method.invoke(obj, "World"); // obj.greet("World")
```

---

## Performance Characteristics

Reflection is significantly slower than direct access. Here's a rough comparison:

| Operation | Direct | Reflection | Slowdown |
|-----------|--------|-----------|----------|
| Field read | 1 ns | 10-50 ns | 10-50x |
| Method call | 2 ns | 20-100 ns | 10-50x |
| Object creation | 5 ns | 50-200 ns | 10-40x |

### Why Is Reflection Slow?

1. **Type checking** — JVM must verify types at every reflective call
2. **Security checks** — Access control is enforced at each step
3. **Boxing/unboxing** — Primitive types must be wrapped in objects
4. **No JIT optimization** — HotSpot can't inline reflective calls
5. **Lookup overhead** — Finding methods/fields requires name resolution

### Mitigation Strategies

```java
// BAD: Looking up Method every time
for (int i = 0; i < 1000000; i++) {
    Method m = clazz.getDeclaredMethod("getValue");
    m.invoke(obj);
}

// GOOD: Cache the Method object
Method m = clazz.getDeclaredMethod("getValue");
for (int i = 0; i < 1000000; i++) {
    m.invoke(obj);
}

// BETTER: Use setAccessible to skip security checks
Method m = clazz.getDeclaredMethod("getValue");
m.setAccessible(true);
for (int i = 0; i < 1000000; i++) {
    m.invoke(obj);
}
```

---

## Security Considerations

Reflection can bypass access controls, which creates security concerns:

```java
// This breaks encapsulation!
Field f = clazz.getDeclaredField("password");
f.setAccessible(true); // Bypasses private access
String password = (String) f.get(userObject);
```

### Security Best Practices

1. **Don't expose reflection in public APIs** — It breaks encapsulation guarantees
2. **Use SecurityManager** — Restrict reflective access in untrusted code
3. **Validate inputs** — When using `Class.forName()`, validate the class name
4. **Consider Java module system** — Java 9+ restricts reflective access by default
5. **Log reflective access** — Monitor for unexpected reflective operations

---

## Common Exceptions

| Exception | Cause | Fix |
|-----------|-------|-----|
| `ClassNotFoundException` | Wrong fully-qualified name | Verify package + class name |
| `NoSuchFieldException` | Field name doesn't exist | Check field name, use `getDeclaredField` |
| `NoSuchMethodException` | Method name/params don't match | Check method name and parameter types |
| `IllegalAccessException` | No access to the member | Call `setAccessible(true)` |
| `InstantiationException` | Class is abstract/interface | Use a concrete class |
| `InvocationTargetException` | Thrown method raised an exception | Use `getTargetException()` to unwrap |
| `SecurityException` | SecurityManager blocks access | Adjust security policy |
| `InaccessibleObjectException` | Java 9+ module blocks access | Add `--add-opens` JVM flag |

---

## Code Review Checklist

When reviewing code that uses reflection:

- [ ] Is reflection truly necessary? Could direct access work?
- [ ] Are `Class`/`Method`/`Field` objects cached?
- [ ] Are exceptions handled properly (especially `InvocationTargetException`)?
- [ ] Is `setAccessible(true)` justified, or does it bypass a necessary security check?
- [ ] Is the code compatible with Java module system (Java 9+)?
- [ ] Are reflective lookups done at initialization, not in hot loops?
- [ ] Is there documentation explaining *why* reflection is used?

---

## Production Incident: Reflection in Hot Loop

**Incident:** A financial application used reflection to call a pricing method in a tight loop processing 10,000 trades/second. The reflective call overhead caused the system to miss latency SLAs.

**Root cause:** The developer cached the `Method` object but forgot to call `setAccessible(true)`, so every invocation went through the full security check path.

**Fix:** Adding `setAccessible(true)` reduced latency by 60%. Later, the team replaced reflection entirely with a direct method call, reducing latency by 95%.

**Lesson:** Reflection in hot paths is a performance time bomb. Profile first, optimize second.

---

## Engineering Decision Framework

```
Do you need to work with a type unknown at compile time?
├── YES → Is this a framework/plugin use case?
│         ├── YES → Use reflection (it's the right tool)
│         └── NO  → Can you use interfaces/generics instead?
│                   ├── YES → Prefer compile-time type safety
│                   └── NO  → Use reflection, but document why
└── NO  → Don't use reflection. Use direct access.
```

---

## Debugging Tips

1. **Use `toString()` on reflection objects** — `Field.toString()`, `Method.toString()` show full signatures
2. **Print `getModifiers()`** — Decode with `Modifier.toString(modifiers)` to see access level
3. **Check `getDeclaredFields()` vs `getFields()`** — Declared = this class only; Fields = including inherited public
4. **Use `clazz.getSuperclass()`** — Walk the class hierarchy
5. **Set breakpoints in reflective code** — IDEs handle this well in modern versions

---

## Interview Questions

1. **What is reflection and why does it exist?**
   - Self-introspection at runtime; needed for frameworks that work with unknown types

2. **What are the three ways to get a `Class` object?**
   - `.class`, `getClass()`, `Class.forName()`

3. **When should you avoid reflection?**
   - Business logic, performance-critical code, when compile-time safety is available

4. **What is the performance overhead of reflection?**
   - 10-50x slower than direct access; can be mitigated by caching and `setAccessible`

5. **How does the Java module system affect reflection?**
   - Java 9+ restricts reflective access to internal APIs; requires `--add-opens`

---

## Summary

| Concept | Key Point |
|---------|-----------|
| Reflection | Examining/modifying code structure at runtime |
| Entry point | `Class` object (3 ways to get one) |
| Core classes | `Class`, `Field`, `Method`, `Constructor` |
| Performance | 10-50x slower; cache everything |
| Security | Can bypass access controls; use carefully |
| Use case | Frameworks, plugins, serialization, AOP |
| Avoid when | You know the type at compile time |

---

*Next: [02 — Class Introspection](../02-class-introspection/README.md)*
