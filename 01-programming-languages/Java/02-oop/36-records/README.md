# Records

## History

| Version | Change |
|---------|--------|
| JDK 14 | Records preview introduced — Java added a concise syntax for immutable data carriers to eliminate getter/equals/hashCode/toString boilerplate |
| JDK 16 | Records finalized — sealed classes and pattern matching integrated |

Records (introduced in Java 16, preview in 14/15) provide a concise way to declare immutable data carriers. The compiler auto-generates the constructor, getters, `equals()`, `hashCode()`, and `toString()`.

## Learning Objectives

By the end of this topic you will be able to:

• Explain why records exist and what problem they solve
• Create records with compact constructors and validation
• Use records with pattern matching (Java 21+)
• Know when records are better than classes
• Understand record limitations (immutability, no inheritance)

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

## Internal Working

Records are syntactic sugar for immutable data carriers. When you write:
```java
public record Point(int x, int y) {}
```

The compiler generates:
```java
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }

    public int x() { return x; }
    public int y() { return y; }

    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    @Override public String toString() { ... }
}
```

Key points:
- The class is `final` (can't be extended)
- Fields are `final` (immutable)
- No setters (immutable)
- `x()` not `getX()` (accessor method naming)

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

## When NOT to Use Records

Records are not always the right choice:

**When you need mutability:**
```java
// Records can't have mutable fields
public record User(String name) {
    // Can't add: public void setName(String name) { this.name = name; }
}
// Use a class instead
```

**When you need inheritance:**
```java
// Records can't extend classes (they're implicitly final)
// public record Employee(String name) extends Person { }  // COMPILE ERROR
// Use a class with extends instead
```

**When you need no-arg constructors:**
```java
// Records always have the canonical constructor
// You can add a compact constructor, but not a no-arg one
// Use a class with a builder pattern instead
```

**When you need validation across multiple fields:**
```java
// Records validate per-field in compact constructor
// For cross-field validation, use a class with a factory method
```

## Engineering Decision Framework

### Use Records when:
- Data carriers or DTOs with no mutable state
- Value objects that need equals/hashCode/toString
- Map entries, tuple-like returns, or intermediate data
- Pattern matching with switch expressions (Java 21+)
- Reducing boilerplate for immutable data classes

### Avoid Records when:
- Mutable fields are required after construction
- Complex behavior or business logic in the class
- Custom equals/hashCode implementation is needed
- Serialization with custom protocols (JSON, etc.)

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

## Alternatives

| Approach | Immutability | Boilerplate | Validation | Pattern Matching | Use When |
|----------|--------------|-------------|------------|------------------|----------|
| Record | Yes | Minimal | Compact constructor | Yes (Java 21+) | Data carriers |
| Class with getters | Configurable | High | Constructor/setter | No | Mutable objects |
| Lombok @Value | Yes | Low | Constructor | No | Reduce boilerplate |
| Guava ImmutableSet | Yes | Medium | Builder | No | Collections |
| Inner class | No | Medium | Any | No | Helper objects |

## Trade-offs

Records give you immutability and less code but cost:
- No inheritance (can't extend classes)
- No mutability (fields are final)
- No no-arg constructors (for serialization frameworks)
- Limited validation (per-field only, not cross-field)

Use records when:
- You need immutable data carriers
- You want equals/hashCode/toString for free
- You're working with Java 21+ pattern matching
- You're designing APIs that return multiple values

Skip records when:
- You need mutable state
- You need inheritance
- You need no-arg constructors (for JPA, Jackson)
- You need complex validation

## Best Practices

1. **Use compact constructors for validation:**
```java
public record Email(String value) {
    public Email {
        if (value == null || !value.contains("@")) throw new IllegalArgumentException("Invalid email");
    }
}
```

2. **Use records for API return types:**
```java
public record SearchResult(List<Item> items, int totalHits, boolean hasMore) {}
```

3. **Use records with pattern matching (Java 21+):**
```java
if (obj instanceof Point(int x, int y)) {
    System.out.println("Point at " + x + ", " + y);
}
```

4. **Keep records small** — Records with 1-6 fields are ideal. Larger records become unwieldy.

5. **Don't use records for JPA entities** — JPA requires setters and no-arg constructors. Use classes instead.

## Common Mistakes

### Mistake 1: Trying to make records mutable
```java
// COMPILE ERROR — records are implicitly final
public record Point(int x, int y) {
    // Can't do this:
    public void setX(int x) { this.x = x; }  // Error
}
```

### Mistake 2: Extending records
```java
// COMPILE ERROR — records are implicitly final
public record Employee(String name) extends Person { }  // Error
```

### Mistake 3: Using records for JPA
```java
// BAD — JPA needs setters and no-arg constructor
@Entity
public record User(String name) { }  // Won't work with JPA

// GOOD — use a class for JPA
@Entity
public class User {
    private String name;
    // JPA needs this
}
```

### Mistake 4: Confusing accessor naming
```java
public record Point(int x, int y) {}

Point p = new Point(1, 2);
p.x();   // Correct — accessor method
// p.getX();  // COMPILE ERROR — no getX()
```

## Interview Questions

1. **What is a record?**
   A concise way to create immutable data carriers. The compiler generates constructor, accessors, equals, hashCode, toString.

2. **What can't records do?**
   Can't extend classes, can't have mutable fields, can't have no-arg constructors.

3. **How do records work with pattern matching?**
   Java 21+ allows `if (obj instanceof Point(int x, int y))` to destructure records.

4. **When would you use a class instead of a record?**
   When you need mutability, inheritance, or no-arg constructors (JPA, Jackson).

5. **What's the difference between a record and a tuple?**
   Records are named (Point, not Pair), can have validation, and work with pattern matching.

## Production Checklist

### Before using Records in production:

- I know the time/space complexity
- I know thread safety guarantees
- I know memory impact
- I know common mistakes
- I know alternatives
- I know limitations
- I know how to debug it
- I've tested with realistic data volume

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

### Myth 1: Records are just data classes
**Reality:** Immutability, equals/hashCode/toString are auto-generated. They enforce immutability by design.

### Myth 2: Records can't have methods
**Reality:** Can have methods. Records can contain instance methods, static methods, and even custom constructors.

### Myth 3: Records are always better
**Reality:** Not for mutable state. Use classes when you need mutable fields after construction.
