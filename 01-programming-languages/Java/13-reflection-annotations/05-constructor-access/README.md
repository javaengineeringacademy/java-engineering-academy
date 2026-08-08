# 05 — Constructor Access

## Why Constructor Access Matters

Constructor access via reflection enables creating objects without knowing their type at compile time. This is the foundation of dependency injection containers, factories, and deserialization frameworks. When Spring creates a new instance of your `@Service` class, it's using constructor reflection.

---

## Getting Constructors

### `getDeclaredConstructors()` — All Constructors

```java
public class User {
    public User() { }
    public User(String name) { }
    private User(String name, int age) { }
    protected User(int id) { }
}

Constructor<?>[] constructors = User.class.getDeclaredConstructors();
for (Constructor<?> c : constructors) {
    System.out.printf("%s %s(%s)%n",
        Modifier.toString(c.getModifiers()),
        c.getName(),
        Arrays.toString(c.getParameterTypes()));
}
// Output:
// public User()
// public User(java.lang.String)
// private User(java.lang.String, int)
// protected User(int)
```

### `getConstructors()` — Public Constructors Only

```java
Constructor<?>[] publicConstructors = User.class.getConstructors();
```

### Getting a Specific Constructor

```java
// By exact parameter types
Constructor<User> ctor1 = User.class.getDeclaredConstructor();
Constructor<User> ctor2 = User.class.getDeclaredConstructor(String.class);
Constructor<User> ctor3 = User.class.getDeclaredConstructor(String.class, int.class);
```

---

## Creating Instances

### Using `newInstance()`

```java
// Default constructor
Constructor<User> defaultCtor = User.class.getDeclaredConstructor();
User user1 = defaultCtor.newInstance();

// Parameterized constructor
Constructor<User> nameCtor = User.class.getDeclaredConstructor(String.class);
User user2 = nameCtor.newInstance("Alice");

// Private constructor (must call setAccessible first)
Constructor<User> privateCtor = User.class.getDeclaredConstructor(String.class, int.class);
privateCtor.setAccessible(true);
User user3 = privateCtor.newInstance("Bob", 25);
```

### Using `Class.newInstance()` (Deprecated)

```java
// Deprecated since Java 9 — doesn't propagate checked exceptions properly
User user = User.class.newInstance(); // Only works for no-arg constructors
```

**Always prefer `Constructor.newInstance()` over `Class.newInstance()`.**

---

## Constructor Metadata

```java
Constructor<User> ctor = User.class.getDeclaredConstructor(String.class, int.class);

// Name
String name = ctor.getName(); // "User"

// Parameter types
Class<?>[] paramTypes = ctor.getParameterTypes(); // [String.class, int.class]
int paramCount = ctor.getParameterCount(); // 2

// Modifiers
int mods = ctor.getModifiers();
boolean isPrivate = Modifier.isPrivate(mods);
boolean isPublic = Modifier.isPublic(mods);

// Declaring class
Class<?> declaringClass = ctor.getDeclaringClass();

// Exception types
Class<?>[] exceptions = ctor.getExceptionTypes();

// Generic parameter types
Type[] genericParams = ctor.getGenericParameterTypes();
```

---

## Annotations on Constructors

```java
public class Service {
    @Inject
    public Service(Dependency dep, Config config) { }
    
    @Autowired
    private Service() { }
}

Constructor<Service> ctor = Service.class.getDeclaredConstructor(Dependency.class, Config.class);

boolean hasInject = ctor.isAnnotationPresent(Inject.class); // true

Inject inject = ctor.getAnnotation(Inject.class);
// inject has no attributes in this example, but presence matters
```

---

## Inner Class Construction

Constructing inner class instances requires the enclosing class instance:

```java
public class Outer {
    private int x;
    
    public class Inner {
        public Inner() { }
        public Inner(int x) { this.x = x; }
    }
}

// Get the inner class
Class<?> innerClass = Outer.Inner.class;

// Get the constructor
Constructor<?> innerCtor = innerClass.getDeclaredConstructor(Outer.class);

// Create an Outer instance first
Outer outer = new Outer();

// Pass the outer instance to the inner constructor
Object inner = innerCtor.newInstance(outer);
```

---

## Anonymous and Local Class Construction

```java
public class Factory {
    public Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Running");
            }
        };
    }
}

// Anonymous classes have synthetic constructors
Constructor<?>[] ctors = Runnable.class.getDeclaredConstructors();
// Note: You can't directly construct anonymous classes via reflection
// They are typically created through their enclosing method
```

---

## Handling Constructor Exceptions

```java
public class RiskyService {
    public RiskyService() throws IOException, SQLException {
        // Constructor that throws checked exceptions
    }
}

Constructor<RiskyService> ctor = RiskyService.class.getDeclaredConstructor();

try {
    RiskyService service = ctor.newInstance();
} catch (InvocationTargetException e) {
    Throwable actual = e.getTargetException();
    if (actual instanceof IOException) {
        System.out.println("IO error during construction: " + actual.getMessage());
    } else if (actual instanceof SQLException) {
        System.out.println("SQL error during construction: " + actual.getMessage());
    }
} catch (InstantiationException | IllegalAccessException e) {
    System.out.println("Cannot instantiate: " + e.getMessage());
}
```

---

## Constructor vs Factory Method

| Feature | Constructor Reflection | Factory Method |
|---------|----------------------|----------------|
| Compile-time safety | No | Yes |
| Exception handling | Wrapped in InvocationTargetException | Direct |
| Access modifiers | Can bypass private | Must be accessible |
| Performance | Slower | Faster |
| Use case | Frameworks, DI | Application code |

---

## Complete Example: Dynamic Instantiation

```java
import java.lang.reflect.*;
import java.util.*;

public class DynamicInstantiator {

    /**
     * Create an instance of a class using its default constructor.
     */
    public static <T> T create(Class<T> clazz) throws Exception {
        Constructor<T> ctor = clazz.getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    /**
     * Create an instance with constructor argument matching.
     */
    public static <T> T create(Class<T> clazz, Object... args) throws Exception {
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
            // Handle primitive type matching
            if (paramTypes[i] == Integer.class) paramTypes[i] = int.class;
            if (paramTypes[i] == Long.class) paramTypes[i] = long.class;
            if (paramTypes[i] == Double.class) paramTypes[i] = double.class;
            if (paramTypes[i] == Boolean.class) paramTypes[i] = boolean.class;
        }

        Constructor<T> ctor = clazz.getDeclaredConstructor(paramTypes);
        ctor.setAccessible(true);
        return ctor.newInstance(args);
    }

    /**
     * Find the best matching constructor for given argument types.
     */
    public static Constructor<?> findBestConstructor(Class<?> clazz, Class<?>... argTypes) 
            throws NoSuchMethodException {
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            Class<?>[] params = ctor.getParameterTypes();
            if (params.length != argTypes.length) continue;
            
            boolean match = true;
            for (int i = 0; i < params.length; i++) {
                if (!params[i].isAssignableFrom(argTypes[i])) {
                    match = false;
                    break;
                }
            }
            if (match) return ctor;
        }
        throw new NoSuchMethodException("No matching constructor for " + 
            Arrays.toString(argTypes));
    }
}
```

---

## Production Incident: Constructor Ambiguity

**Incidence:** A DI framework tried to instantiate a class with two constructors: `Service(Config)` and `Service(Config, Logger)`. When only `Config` was available, the framework used the wrong constructor due to a bug in parameter matching.

**Root cause:** The framework matched constructors by name, not by parameter types, and chose the first match.

**Fix:** Always match constructors by exact parameter types. Use `Constructor.getParameterTypes()` for precise matching.

---

## Code Review Checklist

- [ ] Is `Constructor.newInstance()` used instead of the deprecated `Class.newInstance()`?
- [ ] Is `setAccessible(true)` needed and justified for private constructors?
- [ ] Is `InvocationTargetException` unwrapped when handling constructor exceptions?
- [ ] Are inner class constructors passed the correct enclosing instance?
- [ ] Is the constructor cached when creating multiple instances?
- [ ] Are primitive types handled correctly in parameter matching?

---

## Debugging Tips

1. **Use `constructor.toString()`** — Shows full constructor signature
2. **Print `Arrays.toString(constructor.getParameterTypes())`** — Verify parameter types
3. **Check `constructor.getModifiers()`** — Decode with `Modifier.toString()`
4. **Unwrap `InvocationTargetException`** — The constructor's exception is the target
5. **For inner classes, pass enclosing instance** — First parameter must be the outer class

---

## Interview Questions

1. **What's the difference between `Class.newInstance()` and `Constructor.newInstance()`?**
   - `Class.newInstance()` is deprecated, only works for no-arg constructors, and doesn't handle checked exceptions properly
   - `Constructor.newInstance()` works with any constructor and properly wraps exceptions

2. **How do you construct an inner class via reflection?**
   - Pass the enclosing class instance as the first argument to the constructor

3. **What exception does `Constructor.newInstance()` throw?**
   - `InvocationTargetException` (wrapping any exception the constructor throws), `InstantiationException`, `IllegalAccessException`

4. **Why would you use `setAccessible(true)` on a constructor?**
   - To instantiate a class with a private constructor (e.g., singleton enforcement, factory pattern)

5. **How do you find the right constructor when there are multiple overloads?**
   - Use `getDeclaredConstructor(Class<?>... parameterTypes)` with exact types

---

## Summary

| Operation | Method | Notes |
|-----------|--------|-------|
| Get all constructors | `getDeclaredConstructors()` | All access levels |
| Get public constructors | `getConstructors()` | Only public |
| Get specific constructor | `getDeclaredConstructor(types...)` | Exact parameter types |
| Create instance | `constructor.newInstance(args...)` | Unwraps to InvocationTargetException |
| Parameter types | `constructor.getParameterTypes()` | Array of `Class<?>` |
| Inner class | Pass outer instance as first arg | `innerCtor.newInstance(outer, ...)` |
| Private constructor | `setAccessible(true)` then `newInstance()` | Bypasses access check |

---

*Next: [06 — Dynamic Proxy](../06-dynamic-proxy/README.md)*
