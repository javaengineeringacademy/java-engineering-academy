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

**Continue to Part 2**: [README-part2.md](README-part2.md)
```
