# Static Block

## Objective
Understand static initialization blocks for one-time class-level initialization.

## Theory

### What is a Static Block?
A **static block** (static initializer) is a block of code executed **once** when the class is loaded into memory. It runs before any instances are created or static members accessed.

### How It Works Internally
1. The JVM loads the class bytecode into Metaspace.
2. The class is initialized by executing all static blocks and static field initializers **in declaration order**.
3. Initialization is **thread-safe** — the JVM holds an intrinsic lock on the Class object during initialization.
4. If initialization throws an uncaught exception, the class enters an erroneous state; subsequent access throws `ExceptionInInitializerError`.

### When Does a Class Load?
A class is loaded on **first active use**:
- `new` operator on the class
- Accessing a static field (except `final` compile-time constants)
- Invoking a static method
- Reflection (`Class.forName()`)
- Initializing a subclass (parent loads first)
- JVM entry point (the class containing `main()`)

### Static Block vs Other Initializers
| Initializer | When It Runs | Scope | Use Case |
|-------------|--------------|-------|----------|
| Static block | Class loading (once) | Static fields | Complex static init |
| Static field init | Class loading (once) | Static fields | Simple static init |
| Instance initializer | Per instance creation | Instance fields | Shared pre-constructor logic |
| Constructor | Per instance creation | Instance fields | Full instance initialization |

## Syntax

### Basic Static Block
```java
public class DatabaseConfig {
    private static String url;
    private static String driver;

    static {
        // Executed once at class loading
        try {
            Properties props = new Properties();
            try (InputStream in = DatabaseConfig.class.getResourceAsStream("/db.properties")) {
                props.load(in);
                url = props.getProperty("url");
                driver = props.getProperty("driver");
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
}
```

### Multiple Static Blocks
```java
public class Config {
    private static final Map<String, String> MAP;

    static {
        // First block: load raw data
        MAP = new HashMap<>();
        MAP.put("key1", "value1");
    }

    static {
        // Second block: process data (runs after first)
        MAP.replaceAll((k, v) -> v.toUpperCase());
    }
}
```

### Static Block vs Static Field Initialization
```java
public class Constants {
    // Equivalent to: static { VALUE = 42; }
    private static final int VALUE = 42;

    private static final List<String> NAMES;

    // Use static block when initialization requires logic
    static {
        NAMES = new ArrayList<>();
        NAMES.add("Alice");
        NAMES.add("Bob");
        NAMES.addAll(loadNamesFromFile());
    }
}
```

## Execution Order

```
1. Parent static blocks (top to bottom)
2. Child static blocks (top to bottom)
3. Parent instance initializers
4. Parent constructor
5. Child instance initializers
5. Child constructor
```

```java
class Parent {
    static { System.out.println("Parent static"); }
    { System.out.println("Parent instance init"); }
    Parent() { System.out.println("Parent constructor"); }
}

class Child extends Parent {
    static { System.out.println("Child static"); }
    { System.out.println("Child instance init"); }
    Child() { System.out.println("Child constructor"); }
}

// Output:
// Parent static
// Child static
// Parent instance init
// Parent constructor
// Child instance init
// Child constructor
```

## Use Cases

### 1. Initialize Complex Static State
```java
class DatabaseConfig {
    private static ConnectionPool pool;

    static {
        try {
            pool = new ConnectionPool(
                "jdbc:postgresql://localhost:5432/db",
                "user", "pass", 20
            );
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
```

### 2. Initialize Collections/Maps
```java
class CurrencyConverter {
    private static final Map<String, BigDecimal> rates = new HashMap<>();

    static {
        rates.put("USD", BigDecimal.ONE);
        rates.put("EUR", BigDecimal.valueOf(0.85));
        rates.put("GBP", BigDecimal.valueOf(0.75));
        rates = Collections.unmodifiableMap(rates);
    }
}
```

### 3. Load Native Libraries
```java
class NativeLibrary {
    static {
        System.loadLibrary("native-lib");
    }
}
```

## Exception Handling

```java
static {
    try {
        // initialization that might throw
    } catch (Exception e) {
        throw new ExceptionInInitializerError(e);  // Wrap checked exceptions
    }
}
```

## Static Block vs Constructor

| Aspect | Static Block | Constructor |
|--------|--------------|-------------|
| Execution | Once per class load | Per instance |
| `this` available | No | Yes |
| Instance fields | No | Yes |
| Can throw checked | No (wrap in `ExceptionInInitializerError`) | Yes |

## Best Practices

1. **Prefer static field initializers for simple cases** — Use `private static final Type FIELD = value;` when no logic is needed.
2. **Reserve static blocks for complex logic** — Use them for try-catch, loops, or multi-step initialization.
3. **Wrap checked exceptions** — Always wrap in `ExceptionInInitializerError`; never let exceptions propagate raw.
4. **Avoid side effects** — Static blocks should only initialize static state; do not perform I/O or network calls unless unavoidable.
5. **Keep them short** — Long static blocks reduce readability; extract helper methods.
6. **Be aware of initialization order** — Static blocks run in declaration order; ensure dependencies are initialized before use.
7. **Avoid circular static dependencies** — If class A's static block references B, and B's static block references A, you get `ClassCircularityError` or deadlocks.
8. **Document the purpose** — Add comments explaining *why* a static block exists, not *what* it does.
9. **Consider using a factory method** — For complex initialization, a `static init()` method with explicit calling can improve clarity.
10. **Test static initialization** — Verify that the class loads correctly and that failure modes produce meaningful errors.

## Common Mistakes

| Mistake | Why It Fails | Fix |
|---------|--------------|-----|
| Throwing checked exception | Static blocks cannot throw checked exceptions | Wrap in `ExceptionInInitializerError` |
| Accessing instance members | No `this` reference exists in static context | Use static members only |
| Ignoring execution order | Multiple blocks run top-to-bottom; dependencies may not be initialized | Order blocks by dependency |
| Circular initialization | Class A loads B which loads A → deadlock or `ClassCircularityError` | Break the cycle with lazy initialization |
| Heavy I/O in static block | Delays class loading; hard to test; failure is permanent | Use lazy initialization or dependency injection |
| Catching and swallowing exceptions | Class enters erroneous state but failure is silent | Always rethrow wrapped in `ExceptionInInitializerError` |
| Relying on subclass loading order | Only parent classes guarantee load order | Do not assume child static blocks run before parent constructors |

## Interview Questions

1. **When does a static block execute?**
   - On first active use of the class: `new`, static method call, static field access, `Class.forName()`, or as the JVM entry point (`main()`).

2. **Can a static block throw a checked exception?**
   - No. The JVM specification requires checked exceptions to be wrapped in `ExceptionInInitializerError` (which is an `Error`, not an `Exception`).

3. **Static block vs static field initialization — which runs first?**
   - Static field initializers and static blocks run **interleaved in declaration order**. If a field is declared before a block, its initializer runs first.

4. **Can a static block access instance variables?**
   - No. There is no `this` reference in a static context. Only static members are accessible.

5. **What happens if a static block throws an exception?**
   - The class becomes unusable. Any subsequent attempt to use it throws `ExceptionInInitializerError`, wrapping the original exception.

6. **How do you prevent static initialization order problems?**
   - Use lazy initialization (e.g., `Supplier<T>`, holder class pattern), avoid circular dependencies, and keep static blocks minimal.

7. **What is the difference between a static block and a static factory method?**
   - A static block runs automatically on class load; a factory method runs on demand. Use blocks for mandatory one-time setup; use factories for controlled object creation.

## Related Topics
← [Static Members](static-members.md) | → [Instance Initializer Block](instance-initializer-block.md)

## References

- [JLS §8.7 — Static Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.7)
- [JLS §12.4.1 — When a Class is Initialized](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.4.1)
- [JLS §12.4.2 — Detailed Initialization Procedure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.4.2)
- [JEP 395 — Records (for comparison)](https://openjdk.org/jeps/395)
- [Baeldung — Java Static Block](https://www.baeldung.com/java-static-blocks)