# Java 17 LTS Deep Dive

## Sealed Classes (Why Restricted Inheritance)

### The Problem

Traditional inheritance allows any class to extend any non-final class:

```java
public class Shape {
    // Any class can extend Shape
}

public class Circle extends Shape { ... }
public class Square extends Shape { ... }
public class EvilClass extends Shape { ... } // Uncontrolled
```

### The Solution

Sealed classes restrict which classes can extend them:

```java
public sealed class Shape permits Circle, Square, Triangle {
    // Only Circle, Square, and Triangle can extend this
}

public final class Circle extends Shape { ... }
public final class Square extends Shape { ... }
public non-sealed class Triangle extends Shape { ... }
```

### Why Sealed Classes?

1. **Domain Modeling**: Enforce complete type hierarchies
2. **Pattern Matching**: Enable exhaustive switch expressions
3. **Security**: Prevent unauthorized subclassing
4. **Performance**: JVM optimizations for known subtypes
5. **Documentation**: Self-documenting design intent

### Permitted Subclasses

```java
public sealed class Shape 
    permits Circle, Square, Triangle {
    // Permits clause lists allowed subclasses
}

// Subclasses must be final, sealed, or non-sealed
public final class Circle extends Shape { ... }
public sealed class Polygon extends Shape 
    permits Rectangle, Pentagon { ... }
public non-sealed class Triangle extends Shape { ... }
```

### Pattern Matching Integration

```java
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle with radius " + c.getRadius();
        case Square s -> "Square with side " + s.getSide();
        case Triangle t -> "Triangle with base " + t.getBase();
        // No default needed - exhaustive!
    };
}
```

### Module System Integration

```java
// module-info.java
module com.example.shapes {
    exports com.example.shapes;
}
// Sealed classes work with module boundaries
```

---

## Pattern Matching instanceof (Why)

### Before Java 16

```java
// Traditional instanceof
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}
```

### After Java 16

```java
// Pattern matching instanceof
if (obj instanceof String s) {
    System.out.println(s.length());
}
// s is in scope here
```

### Why Pattern Matching?

1. **Reduced Boilerplate**: No explicit casting
2. **Type Safety**: Compile-time checking
3. **Scope Control**: Variable only available after check
4. **Pattern Matching Integration**: Works with switch

### Advanced Patterns

```java
// Guarded patterns
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}

// Pattern matching in switch
String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String: " + s;
    case null -> "Null";
    default -> "Unknown";
};

// Destructuring with pattern matching
if (obj instanceof Point(int x, int y)) {
    System.out.println("Point at " + x + ", " + y);
}
```

### Pattern Variables

```java
// Pattern variable scope
if (obj instanceof String s) {
    // s is in scope
    System.out.println(s);
}
// s is NOT in scope here

// Binding in conditional
if (obj instanceof String s && s.length() > 5) {
    // s is in scope
}
```

### Exhaustiveness

```java
// Exhaustive switch with sealed types
sealed interface Shape permits Circle, Square {}
record Circle(double radius) implements Shape {}
record Square(double side) implements Shape {}

String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle";
        case Square s -> "Square";
        // No default needed - exhaustive!
    };
}
```

---

## Text Blocks (Why)

### The Problem

Multi-line strings were painful:

```java
// Pre-Java 15
String json = "{\n" +
    "  \"name\": \"John\",\n" +
    "  \"age\": 30,\n" +
    "  \"address\": {\n" +
    "    \"city\": \"New York\"\n" +
    "  }\n" +
    "}";
```

### The Solution

```java
// Java 15+ text blocks
String json = """
    {
      "name": "John",
      "age": 30,
      "address": {
        "city": "New York"
      }
    }
    """;
```

### Why Text Blocks?

1. **Readability**: Visual structure matches output
2. **Maintainability**: No escaped quotes or newlines
3. **Formatting Control**: Preserves intended indentation
4. **Multi-line Support**: SQL, JSON, HTML, XML

### Formatting Rules

```java
// Leading whitespace
String text = """
        Hello
        World
        """;
// Equivalent to "Hello\nWorld\n"

// Trailing spaces preserved
String text = """
    Hello   
    World   
    """;

// Line terminator
String text = """
    Line 1
    Line 2"""; // No newline at end
```

### Incubator Features

```java
// String formatting (Java 15 preview)
String name = "John";
String text = """
    Hello, %s!
    Welcome to Java.
    """.formatted(name);

// Template expressions (Java 21 preview)
String text = """
    Hello, \{name}!
    Welcome to Java.
    """;
```

---

## Hidden Classes (Why)

### The Problem

Frameworks need dynamic classes, but existing mechanisms had issues:

```java
// Anonymous classes: Visible in bytecode, limited lifecycle
// Dynamic proxies: Only for interfaces
// Unsafe.defineClass: Deprecated, security concerns
```

### The Solution

Hidden classes are not discoverable via reflection:

```java
// Creating hidden class
byte[] bytecode = ...;
MethodHandles.Lookup lookup = MethodHandles.lookup();
Class<?> hiddenClass = lookup.defineHiddenClass(bytecode, true, 
    MethodHandles.Lookup.ClassOption.NESTMATE)
    .lookupClass();

// Hidden class is not visible in class hierarchy
// Cannot be discovered via Class.forName()
// Cannot be reflected upon from outside
```

### Why Hidden Classes?

1. **Framework Optimization**: Lambda internals, proxies
2. **Security**: Not accessible via reflection
3. **Memory**: Unloadable when classloader is collected
4. **Performance**: Better than anonymous classes

### Lambda Implementation

```java
// Lambdas use hidden classes internally
Runnable r = () -> System.out.println("Hello");
// This creates a hidden class, not an anonymous class

// Benefits:
// - No class file generated
// - Better performance
// - Memory efficient
```

### Framework Usage

```java
// Framework creating hidden classes
Class<?> hiddenClass = lookup.defineHiddenClass(bytecode, true,
    MethodHandles.Lookup.ClassOption.NESTMATE,
    MethodHandles.Lookup.ClassOption.ALWAYS_NEST)
    .lookupClass();

// Create instance
Object instance = hiddenClass.getDeclaredConstructor().newInstance();

// Better than:
// - Proxy.newProxyInstance (interface only)
// - CGLIB (requires external library)
// - Javassist (requires external library)
```

---

## Strong Encapsulation (Why)

### Before Java 9

```java
// Internal APIs were accessible
import sun.misc.Unsafe;
import com.sun.net.httpserver.HttpServer;

// Code relying on internal APIs
Unsafe unsafe = Unsafe.getUnsafe();
```

### After Java 17

```java
// Strong encapsulation by default
// Internal APIs are not accessible
// Must use --add-opens or module system

// module-info.java
module com.example {
    requires java.net.http;
    exports com.example.api;
}
```

### Why Strong Encapsulation?

1. **Security**: Prevent access to internal APIs
2. **Stability**: Internal APIs can change without notice
3. **Modularity**: Clean API boundaries
4. **Performance**: JVM optimizations for encapsulated code

### Encapsulation Levels

```java
// Public API
public class PublicClass { }

// Package-private
class PackagePrivateClass { }

// Module-private (not exported)
module com.example {
    // Not accessible outside module
    internal class InternalClass { }
}

// Exported
module com.example {
    exports com.example.api;
}
```

### Migration Path

```java
// Step 1: Identify internal API usage
// Search for: sun.*, com.sun.*, jdk.internal.*

// Step 2: Replace with public API
// sun.misc.Unsafe -> VarHandle, MethodHandles
// com.sun.net.httpserver -> java.net.http.HttpServer

// Step 3: If must use internal API
// --add-opens java.base/sun.misc=ALL-UNNAMED
```

### Best Practices

1. **Avoid Internal APIs**: Use public alternatives
2. **Use Module System**: Proper encapsulation
3. **Test Encapsulation**: Ensure no illegal access
4. **Document Dependencies**: What APIs you use

---

## Migration from Java 11 to Java 17

### Step 1: Update Build Tools

```xml
<!-- Maven -->
<properties>


---

## Overview

Java 17 LTS (September 2021) consolidated years of preview features into stable releases: sealed classes, pattern matching `instanceof`, text blocks, hidden classes, and strong encapsulation. It closed the `SecurityManager` deprecation path and added foreign function and memory API previews. Java 17 was the first LTS after the 6-month cadence change, offering a significant modernization jump from Java 11.

## Why This Concept Exists

Java 17 existed because enterprise teams needed a stable target after years of preview features in Java 14-16. Sealed classes enable exhaustive pattern matching. Pattern matching `instanceof` eliminates boilerplate casting. Text blocks solve multi-line string pain. Strong encapsulation secures the platform by default. These features collectively move Java toward algebraic data types and safer, more expressive code.

## Internal Working

### Sealed Classes: JVM Verification

```java
// Bytecode verification ensures permits clause
public sealed class Shape permits Circle, Square, Triangle {
    // JVM checks: subclasses must be final, sealed, or non-sealed
    // In same module, or same package (unnamed module)
}
```

The JVM verifier checks sealed class constraints at class loading time. If a subclass doesn't appear in the `permits` clause, a `VerifyError` is thrown. The `PermittedSubclasses` attribute in bytecode lists allowed subclasses.

### Pattern Matching `instanceof`: Bytecode Transformation

```java
// Source
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s);
}

// Bytecode (simplified)
if (!(obj instanceof String)) goto end;
String s = (String) obj;
if (s.length() <= 5) goto end;
System.out.println(s);
end:
```

The compiler inserts the cast after the type check, then evaluates the guard condition. The pattern variable `s` is only in scope after both the type check and guard pass.

### Text Blocks: Compile-Time Processing

```java
// Source
String json = """
        {
          "key": "value"
        }
        """;

// Compiled to (equivalent)
String json = "{\n  \"key\": \"value\"\n}\n";
```

The compiler strips leading whitespace based on the common indent of all non-blank lines. The trailing `"""` position determines whether a final newline is included.

### Hidden Classes: Runtime Generation

```java
// Lookup defines a hidden class not visible to Class.forName()
MethodHandles.Lookup lookup = MethodHandles.lookup();
Class<?> hidden = lookup.defineHiddenClass(bytecode, true,
    MethodHandles.Lookup.ClassOption.NESTMATE)
    .lookupClass();

// Hidden class:
// - Cannot be discovered via reflection from outside
// - Unloaded when its classloader is GC'd
// - Better than anonymous classes for lambda internals
```

## Examples

### Sealed Class + Pattern Matching Pattern

```java
// Domain modeling with sealed hierarchy
public sealed interface Payment permits CreditCard, BankTransfer, PayPal {
    default String describe() {
        return switch (this) {
            case CreditCard cc -> "Credit card ending " + cc.lastFour();
            case BankTransfer bt -> "Transfer to " + bt.accountNumber();
            case PayPal pp -> "PayPal: " + pp.email();
        };
    }
}

public record CreditCard(String number, String lastFour, LocalDate expiry) implements Payment {}
public record BankTransfer(String accountNumber, String routing) implements Payment {}
public record PayPal(String email) implements Payment {}

// Exhaustive handling ensures all payment types are covered
public BigDecimal calculateFee(Payment payment) {
    return switch (payment) {
        case CreditCard cc -> cc.amount().multiply(BigDecimal.valueOf(0.029));
        case BankTransfer bt -> BigDecimal.valueOf(0.50);
        case PayPal pp -> pp.amount().multiply(BigDecimal.valueOf(0.035));
    };
}
```

### Strong Encapsulation Migration

```java
// BEFORE: Using internal API (Java 8-16)
import sun.misc.Unsafe;
Unsafe unsafe = Unsafe.getUnsafe(); // Accesses internal API

// AFTER: Java 17+ (strong encapsulation)
// Option 1: Use public API
VarHandle handle = MethodHandles.lookup()
    .findVarHandle(MyClass.class, "field", int.class);

// Option 2: If must use internal API (not recommended)
// java --add-opens java.base/sun.misc=ALL-UNNAMED MyApp

// Option 3: Migrate to VarHandle
AtomicInteger counter = new AtomicInteger(0);
VarHandle vh = MethodHandles.lookup()
    .findVarHandle(Counter.class, "value", int.class);
```

### Hidden Class for Framework Internals

```java
// Framework creating dynamic proxy without CGLIB
public class DynamicProxyFactory {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public static <T> T createProxy(Class<T> iface, InvocationHandler handler) {
        byte[] bytecode = generateProxyBytecode(iface);
        try {
            Class<?> hiddenClass = LOOKUP.defineHiddenClass(bytecode, true,
                MethodHandles.Lookup.ClassOption.NESTMATE,
                MethodHandles.Lookup.ClassOption.ALWAYS_NEST)
                .lookupClass();
            return iface.cast(hiddenClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

## Performance

### Sealed Classes: Pattern Matching Optimization

| Scenario | switch (sealed) | switch (non-sealed) | if-else chain |
|----------|----------------|---------------------|---------------|
| 3 cases | 2.1ns | 2.8ns | 3.2ns |
| 10 cases | 3.5ns | 8.2ns | 12.1ns |
| 50 cases | 4.2ns | 35ns | 85ns |

Sealed classes enable the JVM to generate a jump table instead of sequential type checks.

### Text Blocks: No Runtime Overhead

Text blocks are processed entirely at compile time. The resulting bytecode is identical to manual concatenation. Zero runtime cost.

### Hidden Classes vs Anonymous Classes

| Metric | Anonymous Class | Hidden Class | Improvement |
|--------|----------------|--------------|-------------|
| Class loading | Yes | Yes | Same |
| Memory per instance | ~200 bytes | ~120 bytes | 40% less |
| GC pressure | High (class files) | Low (unloadable) | Better |
| Reflection accessible | Yes | No | Security |

## Pitfalls

### 1. Sealed Class Across Modules

```java
// BAD: Permitted subclass in different module
module com.example.shapes {
    exports com.example.shapes;
}
// Circle.java in com.example.shapes must be in same module
// or same package (unnamed module only)

// GOOD: Keep sealed hierarchy in same module
module com.example.shapes {
    exports com.example.shapes;
    // All permitted subclasses in com.example.shapes package
}
```

### 2. Pattern Variable Scope Confusion

```java
// BAD: Variable not in scope
if (obj instanceof String s || s.length() > 5) { // Error: s not in scope
    System.out.println(s);
}

// GOOD: Use && (not ||) for guards
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s); // s is in scope
}

// NOTE: In Java 21+, pattern variables in || are in scope after the if
```

### 3. Text Block Indentation Issues

```java
// BAD: Mixing indentation styles
String sql = """
        SELECT * FROM users
        WHERE id = ?""";  // May include unexpected whitespace

// GOOD: Consistent indentation
String sql = """
        SELECT * FROM users
        WHERE id = ?
        """;
// Result: "SELECT * FROM users\nWHERE id = ?\n"
```

### 4. Strong Encapsulation Breaking Reflection

```java
// BAD: Assuming internal API access works
Method method = Class.class.getDeclaredMethod("getModule");
// May fail with InaccessibleObjectException in Java 17+

// GOOD: Use public module API
Module module = MyClass.class.getModule();
String name = module.getName();
```

### 5. Not Testing on Target JDK

```java
// BAD: Only testing on Java 11
// SOLUTION: Test on Java 17 with --enable-preview for preview features
// Add to CI: matrix with Java 11, 17, 21
```

## References

- [Java 17 Release Notes](https://www.oracle.com/java/technologies/javase/17-relnote-articles.html)
- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [JEP 394: Pattern Matching instanceof](https://openjdk.org/jeps/394)
- [JEP 378: Text Blocks](https://openjdk.org/jeps/378)
- [JEP 371: Hidden Classes](https://openjdk.org/jeps/371)
- [JEP 403: Strong Encapsulation](https://openjdk.org/jeps/403)
- *Effective Java* by Joshua Bloch
- [OpenJDK Source Code](https://github.com/openjdk/jdk)
