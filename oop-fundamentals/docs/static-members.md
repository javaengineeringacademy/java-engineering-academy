# Static Members

## Static Variables (Class Variables)

Shared across all instances of a class.

```java
public class Counter {
    private static int count = 0;  // Shared by all instances

    public Counter() {
        count++;
    }

    public static int getCount() {
        return count;
    }
}
```

## Static Methods

```java
public class MathUtils {
    // Utility method - no instance needed
    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    // Factory method
    public static User createGuest() {
        return new User("Guest");
    }
}
```

## Static Block

Executed once when class is loaded.

```java
public class DatabaseConfig {
    private static String url;
    private static String driver;

    static {
        // Executed once at class loading
        try {
            Properties props = new Properties();
            try (InputStream in = MathUtils.class.getResourceAsStream("/db.properties")) {
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

## Static Import

```java
import static java.lang.Math.*;

public class Calculator {
    public double calculate() {
        return sqrt(pow(2, 10)) + PI;  // Direct access
    }
}
```

## Static vs Instance

| Aspect | Static | Instance |
|--------|--------|----------|
| Memory | Class area (single copy) | Heap (per object) |
| Access | `ClassName.member` | `object.member` |
| `this` reference | Not available | Available |
| Override | No (hidden) | Yes (overridden) |
| Serialize | Not serialized | Serialized |

## Static Nested Classes

```java
public class Outer {
    private static String message = "Hello";

    static class Nested {
        void print() {
            System.out.println(message);  // Access static outer member
        }
    }
}
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Access `this` in static method | Remove `this` or make method instance |
| Modify static field in instance | Use static setter or synchronize |
| Static method calls instance method | Create instance or make method static |

## Interview Questions

1. **Can we override static method?** No, static methods are hidden, not overridden
2. **Can static method access instance variable?** No, no `this` reference
3. **When does static block execute?** Class loading (before first use)
4. **Can static method be overridden?** No, it's method hiding

## Related Topics
← [Instance Members](instance-members.md) | → [Static Block](static-block.md)

## References
- [JLS - Static Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3.1.1)