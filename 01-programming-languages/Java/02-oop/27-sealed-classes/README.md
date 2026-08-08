# Sealed Classes

## Why Sealed Classes Exist

Inheritance in Java has always been fully open — any class can extend any non-final class. This makes reasoning about code difficult: you cannot know the full set of subtypes at compile time. Sealed classes solve this by restricting which classes may extend or implement a type, giving you controlled, auditable inheritance hierarchies. This restriction is the foundation for exhaustive pattern matching and compiler-verified domain models.

## What You'll Learn

- Declare sealed classes and interfaces with the `permits` clause
- Control subclass access with `final`, `sealed`, and `non-sealed` modifiers
- Combine sealed classes with pattern matching and exhaustive `switch`
- Design type-safe domain models that the compiler can fully verify
- Recognize when sealed classes are the wrong tool

## When to Use Sealed Classes

| Use Case | Why Sealed | Alternative |
|----------|------------|-------------|
| Domain model with fixed subtypes | Compiler enforces completeness | Abstract class + enum |
| State machine modeling | Exhaustive switch catches missing states | Visitor pattern |
| API design with controlled extension | Partners implement known subtypes | Interface + documentation |
| Pattern matching over type hierarchies | Compiler guarantees exhaustiveness | `instanceof` chains |
| Algebraic data types | Maps sum types directly to Java | Sealed interface + records |
| Error/success result types | Two known subtypes, exhaustive handling | Optional + exceptions |

## How Sealed Classes Work Internally

A sealed class restricts which classes may extend it using the `permits` clause:

```java
public sealed class Shape permits Circle, Rectangle, Triangle {
}

public final class Circle extends Shape {
    private final double radius;
    public Circle(double radius) { this.radius = radius; }
    public double radius() { return radius; }
}

public final class Rectangle extends Shape {
    private final double width, height;
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    public double width() { return width; }
    public double height() { return height; }
}

public final class Triangle extends Shape {
    private final double base, height;
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    public double base() { return base; }
    public double height() { return height; }
}
```

Each permitted subclass **must** use one of three modifiers:

| Modifier | Meaning | Use When |
|----------|---------|----------|
| `final` | Cannot be extended further | Leaf node in the hierarchy |
| `sealed` | Further restricts its own subclasses | Intermediate level of hierarchy |
| `non-sealed` | Opens the class to anyone | You want to break the chain (rare) |

```java
// Sealed hierarchy can continue through intermediate sealed class
public sealed class Quadrilateral permits Rectangle, Square {
}

public final class Square extends Quadrilateral {
    private final double side;
    public Square(double side) { this.side = side; }
    public double side() { return side; }
}
```

### Permitted Subclasses in the Same File

All permitted subclasses can live in the same file:

```java
public sealed class Result permits Success, Failure {
    private Result() {} // private constructor prevents external extension
}

public final class Success extends Result {
    private final Object value;
    public Success(Object value) { this.value = value; }
    public Object value() { return value; }
}

public final class Failure extends Result {
    private final String message;
    public Failure(String message) { this.message = message; }
    public String message() { return message; }
}
```

### Sealed Interfaces

Interfaces can also be sealed:

```java
public sealed interface Payment permits CreditCardPayment, BankTransferPayment {
}

public record CreditCardPayment(String cardNumber, String cvv) implements Payment {
}

public record BankTransferPayment(String accountNumber, String routingNumber) implements Payment {
}
```

### Combining with Pattern Matching

The real power of sealed classes emerges with exhaustive `switch` expressions:

```java
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle with radius " + c.radius();
        case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
        case Triangle t -> "Triangle with base " + t.base();
        // No default needed — compiler knows all cases are covered
    };
}
```

If you add a new subtype to `Shape` later, the compiler will flag every `switch` that is missing the new case. This is **compile-time exhaustiveness checking**.

```java
// Destructuring with pattern matching
public static double area(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> 0.5 * t.base() * t.height();
    };
}
```

### JVM Encoding

At the bytecode level, sealed classes are implemented using `PermittedSubclasses` attributes in the class file (JEP 409). The JVM does not enforce sealing at runtime — it is a compile-time contract. The `permits` clause generates metadata that tools and the compiler use to verify exhaustive coverage.

```java
// The compiler generates a PermittedSubclasses attribute for Shape:
// PermittedSubclasses: Circle, Rectangle, Triangle
// This is metadata only — not enforced by the JVM at class load time
```

### Module System Interaction

Sealed classes work across modules. If the sealed class is in module A and permitted subclasses are in module B, the subclasses must be in a package that is `exported` from module B:

```java
// Module A
public sealed class Plugin permits com.app.PluginA, com.app.PluginB {
}

// Module B must export the package containing PluginA
// module-info.java in module B:
// exports com.app;
```

## Production Checklist

### Before using sealed classes in production:

- [ ] I understand the `permits` clause and how to list permitted subclasses
- [ ] I know the three subclass modifiers: `final`, `sealed`, `non-sealed`
- [ ] I know how sealed classes work with pattern matching and exhaustive `switch`
- [ ] I know when to use sealed classes vs plain interface + enum
- [ ] I have tested exhaustive switch statements compile without `default` when all cases are covered
- [ ] I understand that sealing is compile-time only — not enforced at the JVM level
- [ ] I have considered the impact on serialization and deserialization
- [ ] I have documented why the hierarchy is sealed (for future maintainers)
- [ ] I know that sealed classes require all permitted subclasses to be in the same module (or same package if in the unnamed module)

## Engineering Maturity Levels

### Level 1: Can Use
- Knows the `sealed` keyword and basic syntax
- Can declare a sealed class with `permits`
- Can use `final` on permitted subclasses

### Level 2: Understands
- Knows when to use sealed vs abstract class
- Understands the three permitted subclass modifiers
- Can combine sealed classes with records

### Level 3: Deep Knowledge
- Understands JVM `PermittedSubclasses` attribute
- Knows how sealed classes interact with the module system
- Can use sealed interfaces with pattern matching
- Understands serialization implications

### Level 4: Expert
- Can design sealed hierarchies for public APIs
- Knows how sealed classes interact with reflection
- Can design multi-level sealed hierarchies
- Understands performance characteristics

### Level 5: Master
- Can teach others when NOT to use sealed classes
- Can design systems that leverage exhaustiveness checking across module boundaries
- Understands the evolution of sealed classes across Java versions (preview in 15/16, final in 17)
- Can evaluate trade-offs between sealed classes and other type-safety mechanisms

## Common Myths

### Myth 1: Sealed classes are just final classes

**Reality:** `final` prevents *all* extension. Sealed classes restrict extension to a *known set* of subtypes. A sealed class is not final — it permits specific subclasses while blocking all others. Think of it as "final with an explicit allowlist."

### Myth 2: Sealed classes hurt extensibility

**Reality:** Sealed classes make extensibility *explicit and controlled*. They do not eliminate extension — they make it a deliberate, documented decision. If you want open extension, use a regular interface. If you want controlled extension, use sealed. The `non-sealed` modifier even allows breaking the seal when needed.

### Myth 3: Sealed classes are only for enums

**Reality:** Enums model fixed constants. Sealed classes model fixed *types* with their own behavior, state, and polymorphism. A `Color` enum holds red, green, blue. A sealed `Shape` hierarchy holds `Circle`, `Rectangle`, `Triangle` — each with distinct fields and methods. They solve different problems.

### Myth 4: Sealed classes are enforced at runtime

**Reality:** Sealing is a compile-time contract. The JVM stores `PermittedSubclasses` metadata but does not enforce it during class loading. A forged class file could technically bypass sealing. Trust the compiler, not the runtime.

### Myth 5: You must put permitted subclasses in the same file

**Reality:** Permitted subclasses can be in separate files, separate packages, and even separate modules — as long as they are accessible (same package or exported package in another module). The `permits` clause lists fully qualified names when subclasses are in different files.

## Common Mistakes

### Mistake 1: Forgetting that permitted subclasses must be in the same module

```java
// Module A
public sealed class Plugin permits com.b.PluginImpl { }

// Module B — this FAILS if the package isn't exported
public final class PluginImpl extends Plugin { }

// Fix: ensure Module B exports com.b
// module-info.java in Module B:
// exports com.b;
```

### Mistake 2: Using `non-sealed` without understanding the consequences

```java
public sealed class Vehicle permits Car, Truck {
}

// non-sealed opens the hierarchy to ANYONE — defeats the purpose
public non-sealed class Car extends Vehicle { }

// Now anyone can extend Car:
public class SportsCar extends Car { } // legal, breaks the guarantee
```

### Mistake 3: Adding a new subtype and forgetting to update switch statements

```java
public sealed class Status permits Active, Inactive, Pending { }

// This compiles fine:
String label(Status s) {
    return switch (s) {
        case Active a -> "Active";
        case Inactive i -> "Inactive";
    };
    // Wait — Pending is missing! In Java 17+ with sealed + switch expression,
    // this WILL cause a compile error because the switch is not exhaustive.
    // But with switch STATEMENT (not expression), you might silently miss it.
}
```

### Mistake 4: Assuming sealed classes are serializable

```java
// Sealed classes don't automatically support serialization.
// If you need serialization, implement Serializable explicitly
// and handle the permitted subclasses in readObject/writeObject.
public sealed class Command implements Serializable
    permits LoginCommand, LogoutCommand {
}

// Java will serialize/deserialize the concrete type (LoginCommand),
// but you must ensure the subclasses are also Serializable.
```

### Mistake 5: Using sealed when the hierarchy might grow

```java
// Bad: this seals to specific error types, but new error types will be added
public sealed class AppException permits NetworkError, DatabaseError, ValidationError {
}

// Better: if you expect new error types, use a regular abstract class
// and document the extension policy
public abstract class AppException {
    // leave open for new error types
}
```

## Production Implications

### API Design

Sealed classes are powerful for public APIs where you control the set of subtypes. They allow you to:

- Guarantee exhaustiveness in pattern matching across versions
- Document the closed set of subtypes in the Javadoc
- Refactor internal implementations without breaking clients

```java
// Public API — library authors control the hierarchy
public sealed interface Response permits SuccessResponse, ErrorResponse {
}

// Clients can pattern match exhaustively:
String message(Response r) {
    return switch (r) {
        case SuccessResponse s -> s.body();
        case ErrorResponse e -> e.message();
    };
}
```

### Serialization

When using Java serialization with sealed classes:

- Permitted subclasses must also be `Serializable`
- The concrete type is written to the stream, so deserialization reconstructs the correct subclass
- Consider using records with sealed interfaces for immutable, serializable ADTs

### Testing

Sealed classes make testing more deterministic:

- You know every possible subtype at compile time
- Test suites can be verified against the complete set of subtypes
- The compiler will warn if a new subtype is added and tests don't handle it

### Performance

Sealed classes have negligible runtime overhead. The `PermittedSubclasses` attribute is metadata only. JVM optimizations like monomorphic dispatch can still apply since the set of subtypes is known at compile time.

### Framework Compatibility

Some frameworks may not recognize sealed classes properly:

- **Serialization frameworks** (Jackson, Gson): May need custom deserializers
- **Dependency injection** (Spring): Can instantiate permitted subclasses normally
- **ORM** (Hibernate): May need `@DiscriminatorValue` for sealed hierarchies mapped to tables
- **Reflection**: `Class.getPermittedSubclasses()` returns the list at runtime

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Keyword | `sealed` |
| Clause | `permits Subclass1, Subclass2, ...` |
| Subclass modifiers | `final`, `sealed`, `non-sealed` |
| Package requirement | Same module, or exported package |
| JVM enforcement | Compile-time only (metadata in class file) |
| Best combined with | Pattern matching, records, switch expressions |
| Java version | Final in Java 17 (preview in 15/16) |
| Sealed interfaces | Supported since Java 17 |
| Exhaustiveness | Compiler checks all cases covered in switch |
| `non-sealed` purpose | Breaks the seal, allows open extension |

## Related Topics

- [Records](../28-records/README.md) — Immutable data carriers, perfect partners for sealed interfaces
- [Pattern Matching](../29-pattern-matching/README.md) — Exhaustive matching over sealed hierarchies
- [Switch Expressions](../30-switch-expressions/README.md) — Arrow syntax with sealed class exhaustiveness
- [Interfaces](../16-interfaces/README.md) — Sealed interfaces extend interface capabilities
- [Abstract Classes](../18-abstract-classes/README.md) — When to use sealed vs abstract
- [Enums](../17-enums/README.md) — Fixed constants vs fixed types

## References

- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409) — Java 17 final release
- [JEP 360: Sealed Classes](https://openjdk.org/jeps/360) — Java 15 preview
- [JEP 397: Sealed Classes](https://openjdk.org/jeps/397) — Java 16 second preview
- [JLS 8.1.1.2: sealed](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.1.2) — Language specification
- [JLS 9.1.1.4: sealed](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.1.1.4) — Interface modifier
- [Oracle Tutorial: Sealed Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/sealed.html)
- [Inside Java: Sealed Classes](https://inside.java/2021/05/27/sealed-classes/)
- [Baeldung: Java Sealed Classes](https://www.baeldung.com/java-sealed-classes)

## Version Validation

- Verified against: Java 21 LTS
- Language feature: Final (not preview) since Java 17
- Preview versions: Java 15 (JEP 360), Java 16 (JEP 397)
- Compatible with: Records (Java 16+), Pattern Matching for switch (Java 21+)
- `PermittedSubclasses` attribute: ClassFile version 61+ (Java 17+)
