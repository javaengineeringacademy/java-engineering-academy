# Records

Records (introduced in Java 16, preview in 14/15) provide a concise way to declare immutable data carriers. The compiler auto-generates the constructor, getters, `equals()`, `hashCode()`, and `toString()`.

## Record Declaration Syntax

```java
// Basic record
public record Point(int x, int y) {}

// Record with multiple fields
public record Person(String name, int age, String email) {}

// Record with compact constructor (validation)
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException("start must be <= end");
        }
    }
}

// Record implementing interfaces
public record Money(double amount, String currency) implements Comparable<Money> {
    @Override
    public int compareTo(Money other) {
        return Double.compare(this.amount, other.amount);
    }
}

// Record with static fields and methods
public record Color(int r, int g, int b) {
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);

    public String toHex() {
        return String.format("#%02X%02X%02X", r, g, b);
    }
}
```

## Compact Canonical Constructor

The compact constructor lets you validate or transform fields without repeating parameter names:

```java
public record EmailAddress(String local, String domain) {
    // Compact constructor - no parameter list needed
    public EmailAddress {
        Objects.requireNonNull(local, "local must not be null");
        Objects.requireNonNull(domain, "domain must not be null");
        if (local.isEmpty()) throw new IllegalArgumentException("local must not be empty");
        if (domain.isEmpty()) throw new IllegalArgumentException("domain must not be empty");
        local = local.toLowerCase();  // normalize
        domain = domain.toLowerCase();
    }

    // Canonical constructor still exists if needed
    // public EmailAddress(String local, String domain) { ... }
}
```

## Pattern Matching with Records (Java 21+)

```java
// Record pattern in switch
sealed interface Shape permits Circle, Rectangle {}
public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}

// Pattern matching
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle(var r) -> "Circle with radius " + r;
        case Rectangle(var w, var h) -> "Rectangle " + w + "x" + h;
    };
}

// Nested record patterns
public record Point(int x, int y) {}
public record Line(Point start, Point end) {}

public static void processLine(Line line) {
    if (line instanceof Line(Point(var x1, var y1), Point(var x2, var y2))) {
        System.out.printf("Line from (%d,%d) to (%d,%d)%n", x1, y1, x2, y2);
    }
}

// Pattern matching with guards
public static String categorize(Shape shape) {
    return switch (shape) {
        case Circle(var r) when r < 10 -> "Small circle";
        case Circle(var r) when r >= 10 -> "Large circle";
        case Rectangle(var w, var h) when w == h -> "Square";
        case Rectangle(var w, var h) -> "Rectangle";
    };
}
```

## Auto-generated equals() and hashCode()

Records auto-generate `equals()` and `hashCode()` based on all components:

```java
public record Point(int x, int y) {}

// Generated equals():
// public boolean equals(Object o) {
//     if (this == o) return true;
//     if (!(o instanceof Point)) return false;
//     Point p = (Point) o;
//     return x == p.x && y == p.y;
// }

// Generated hashCode():
// public int hashCode() {
//     return Objects.hash(x, y);
// }

// Usage
Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);
p1.equals(p2);    // true
p1.hashCode() == p2.hashCode();  // true

// Custom equals/hashCode is NOT allowed in records
// public record Point(int x, int y) {
//     public boolean equals(Object o) { ... }  // COMPILE ERROR
// }
```

## Why Records Over Classes?

| Criteria | Record | Class |
|----------|--------|-------|
| Immutable by default | Yes | No (must enforce) |
| Boilerplate | Auto-generated | Manual |
| Pattern matching | Built-in | Manual |
| Performance | Same | Same |
| Use when | Data carrier, DTO, value object | Mutable state, complex behavior |

### Decision Flowchart
Need a data carrier? → Yes → Need mutability? → No → Use Record
Need mutability? → Yes → Use Class
Need complex behavior? → Yes → Use Class

| Criteria | Record | Class |
|----------|--------|-------|
| Immutable by default | Yes | No (must enforce) |
| Boilerplate | Auto-generated | Manual |
| Pattern matching | Built-in | Manual |
| Performance | Same | Same |
| Use when | Data carrier, DTO, value object | Mutable state, complex behavior |

### Decision Flowchart
Need a data carrier? → Yes → Need mutability? → No → Use Record
Need mutability? → Yes → Use Class
Need complex behavior? → Yes → Use Class

## Engineering Decision Framework

### ✅ Use Records when:
- Data carriers or DTOs with no mutable state
- Value objects that need equals/hashCode/toString
- Map entries, tuple-like returns, or intermediate data
- Pattern matching with switch expressions (Java 21+)
- Reducing boilerplate for immutable data classes

### ❌ Avoid Records when:
- Mutable fields are required after construction
- Complex behavior or business logic in the class
- Custom equals/hashCode implementation is needed
- Serialization with custom protocols (JSON, etc.)

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Classes with final fields | Need constructor validation logic |
| Sealed classes | Restricting type hierarchies with records |
| Lombok @Value | Pre-Java 16 immutable classes |
| Builder pattern | Complex object construction with many optional fields |

### Production Examples
- API response DTOs (UserResponse, OrderSummary)
- Database query result projections
- Event/message payload objects
- Configuration property holders
- Cache entry wrappers

### Common Production Mistakes
- Adding mutable fields to records (defeats purpose)
- Using records for entities requiring JPA/Hibernate proxies
- Not understanding compact canonical constructors
- Assuming records are always more efficient (same as classes)
- Using records when sealed classes with interfaces would be better

## Production Checklist

### ✅ Before using Records in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## Related Topics
- [Immutability](../../00-knowledge-atoms/immutability/) — Records are immutable by design
- [equals() and hashCode](../../00-knowledge-atoms/equals-hashcode/) — Auto-generated by records
- [Value Objects](../32-value-objects/) — Related concept
- [Sealed Classes](../34-sealed-hierarchy/) — Often used together
- Pattern Matching — Records + pattern matching

## Common Myths

### ❌ Myth 1: Records are just data classes
**Reality:** Immutability, equals/hashCode/toString are auto-generated. They enforce immutability by design.

### ❌ Myth 2: Records can't have methods
**Reality:** Can have methods. Records can contain instance methods, static methods, and even custom constructors.

### ❌ Myth 3: Records are always better
**Reality:** Not for mutable state. Use classes when you need mutable fields after construction.
