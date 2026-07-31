# Static Block

## Objective
Understand static initialization blocks for one-time class-level initialization.

## Theory

### What is a Static Block?
A **static block** (static initializer) is a block of code executed **once** when the class is loaded into memory. It runs before any instances are created or static members accessed.

## Syntax
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

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Throwing checked exception | Wrap in `ExceptionInInitializerError` |
| Accessing instance members | Only static members accessible |
| Multiple static blocks order | Execute in declaration order |
| Circular initialization | Avoid static dependencies between classes |

## Interview Questions

1. **When does static block execute?**
   - Class loading (first access: new, static method, static field)

2. **Can static block throw checked exception?**
   - No, wrap in `ExceptionInInitializerError`

3. **Static block vs static field initialization?**
   - Static fields initialized first, then static blocks (in order)

3. **Can static block access instance variables?**
   - No, no `this` reference

## Related Topics
← [Static Members](static-members.md) | → [Instance Initializer Block](instance-initializer-block.md)

## References
- [JLS - Static Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.7)