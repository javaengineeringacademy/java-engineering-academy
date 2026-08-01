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

### Mistake 1: Accessing Instance Members in Static Context

```java
public class UserService {
    private String name;  // Instance field

    public static void greet() {
        System.out.println(name);  // Compile error: non-static field
    }
}
```

**Fix:** Remove the `static` modifier or access only static members.

### Mistake 2: Thread-Unsafe Static Mutable State

```java
public class Counter {
    private static int count = 0;  // Shared, not thread-safe

    public static void increment() {
        count++;  // Race condition
    }
}
```

**Fix:** Use `AtomicInteger` or synchronize access.

### Mistake 3: Static Methods That Should Be Instance Methods

```java
public class Order {
    private BigDecimal total;

    // Bad: Why static? It uses logic that belongs to an instance
    public static boolean isValid(Order order) {
        return order.total.compareTo(BigDecimal.ZERO) > 0;
    }
}
```

**Fix:** Make it an instance method: `public boolean isValid()`.

### Mistake 4: Static Import Abuse

```java
// Bad: Reduces readability
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Calculator {
    public int compute() {
        return add(1, 2);  // Which add? Confusing.
    }
}
```

**Fix:** Import only what you use frequently and sparingly.

## Interview Questions

### Basic

1. **What is the `static` keyword used for?**
   It declares class-level members that belong to the class itself, not to any specific instance. Static members can be accessed without creating an object.

2. **Can static methods access instance variables?**
   No. Static methods have no `this` reference. To access instance state, you must create an instance explicitly.

3. **When does a static block execute?**
   During class loading, which happens once before the class is first used. It is executed in the order it appears in the source code.

4. **Can static methods be overridden?**
   No. Static methods are hidden, not overridden. The method called depends on the reference type, not the actual object type.

5. **Can a static method be overloaded?**
   Yes. Overloading is based on the parameter list. Static methods can be overloaded like any other method.

### Intermediate

6. **Why is the `main` method static?**
   The JVM needs to call `main` without instantiating the class. Making it static allows direct invocation: `public static void main(String[] args)`.

7. **What is the difference between `static` and `final`?**
   `static` means the member belongs to the class. `final` means the value cannot be changed after initialization. They are independent: a field can be `static final`, `static` (non-final), `final` (non-static), or neither.

8. **Can a static method access a non-static (instance) variable?**
   Not directly. You must create an instance of the class and access the variable through that instance.

9. **What is the lifetime of a static variable?**
   From class loading to class unloading. Static variables exist for the entire lifetime of the program (or until the class is garbage collected in some scenarios).

10. **Can static methods be synchronized?**
    Yes. The lock is on the `Class` object itself, so only one thread can execute any synchronized static method of that class at a time.

### Advanced

11. **What happens if you try to access an instance variable from a static method?**
    A compile-time error: "non-static variable this cannot be referenced from a static context."

12. **How do static variables interact with class loading and the JVM?**
    Static variables are stored in the Metaspace (formerly PermGen). They are initialized during the `<clinit>` phase of class loading. The class loader triggers this.

13. **What is the Static Initialization Order in Java?**
    1. Parent class static blocks and static variables (in declaration order)
    2. Child class static blocks and static variables (in declaration order)
    Parent is always initialized before child.

14. **Can a static nested class access outer class instance members?**
    No. Static nested classes can only access static members of the outer class. To access instance members, create an instance of the outer class.

15. **What is the `static` import and when should you use it?**
    `static import` lets you access static members without class name qualification. Use it sparingly for constants (`PI`, `MAX_VALUE`) and utility methods (`Math.sqrt`, `List.of`) to improve readability.

## Related Topics
← [Instance Members](instance-members.md) | → [Static Block](static-block.md)

## References
- [JLS - Static Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3.1.1)
- [JLS - Static Methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.3.2)
- [JLS - Static Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.7)
- [Effective Java - Item 19: Design and document for inheritance or else prohibit it](https://books.google.com/books?id=BIpKEttKoLYC)