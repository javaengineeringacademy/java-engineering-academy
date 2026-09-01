# Module 16: Modern Java Features

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 40 min | **Practice:** 60 min | **Total:** 100 min

## Overview

Java has evolved significantly from a verbose, ceremony-heavy language into a concise, expressive platform. This module covers modern Java features from Java 10 through Java 21 — records, sealed classes, pattern matching, text blocks, switch expressions, var type inference, and more. These features reduce boilerplate, improve safety, and enable new coding patterns.

## Learning Objectives

- [ ] Use records for immutable data carriers
- [ ] Apply sealed classes to restrict class hierarchies
- [ ] Use pattern matching for type-safe conditionals
- [ ] Write multi-line strings with text blocks
- [ ] Use switch expressions for concise branching
- [ ] Apply var for local variable type inference
- [ ] Destructure records with record patterns

## Prerequisites

- Java fundamentals and OOP
- Understanding of inheritance and polymorphism
- Familiarity with generics and collections

## History

- **2018** — Java 10 introduced `var` for local variable type inference
- **2019** — Java 13 previewed text blocks
- **2020** — Java 14 previewed switch expressions and records
- **2021** — Java 16 finalized records, pattern matching for instanceof
- **2021** — Java 17 finalized text blocks, sealed classes
- **2022** — Java 19 previewed record patterns, pattern matching for switch
- **2023** — Java 21 finalized pattern matching for switch, record patterns

## Production Notes

- **Where is it used?** In modern Java applications using Java 17+ LTS
- **Why is it useful?** Reduces boilerplate, improves safety, enables new patterns
- **When should it be avoided?** When targeting older Java versions
- **Alternative?** Older Java syntax (verbose but compatible)

## Why This Concept Exists

Without modern features:
- Immutable data classes require 50+ lines of boilerplate
- Type-safe conditionals require verbose instanceof chains
- Multi-line strings require concatenation or StringBuilder
- Switch statements require break and fall-through
- Variable declarations require explicit types everywhere

## Core Concepts

### Records (Java 16)

```java
// Before: 50+ lines
public class User {
    private final String name;
    private final int age;
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public boolean equals(Object o) { ... }
    @Override
    public int hashCode() { ... }
    @Override
    public String toString() { ... }
}

// After: 1 line
public record User(String name, int age) {}
```

### Sealed Classes (Java 17)

```java
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {
}

public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Triangle(double a, double b, double c) implements Shape {}
```

### Pattern Matching for switch (Java 21)

```java
// Before
String description;
if (shape instanceof Circle) {
    description = "Circle with radius " + ((Circle) shape).radius();
} else if (shape instanceof Rectangle) {
    description = "Rectangle " + ((Rectangle) shape).width() + "x" + ((Rectangle) shape).height();
} else {
    description = "Unknown";
}

// After (Java 21)
String description = switch (shape) {
    case Circle c -> "Circle with radius " + c.radius();
    case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
    case Triangle t -> "Triangle with sides " + t.a() + ", " + t.b() + ", " + t.c();
    default -> "Unknown";
};
```

### Text Blocks (Java 15)

```java
// Before
String json = "{\n" +
    "  \"name\": \"Alice\",\n" +
    "  \"age\": 30\n" +
    "}";

// After
String json = """
    {
        "name": "Alice",
        "age": 30
    }
    """;
```

### Switch Expressions (Java 14)

```java
// Before
String result;
switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        result = "Weekend";
        break;
    case TUESDAY:
        result = "Weekday";
        break;
    default:
        result = "Other";
        break;
}

// After
String result = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> "Weekend";
    case TUESDAY -> "Weekday";
    default -> "Other";
};
```

### var Type Inference (Java 10)

```java
// Before
List<String> names = new ArrayList<>();
Map<String, List<Integer>> map = new HashMap<>();

// After
var names = new ArrayList<String>();
var map = new HashMap<String, List<Integer>>();
```

### Record Patterns (Java 21)

```java
// Destructure records
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle(double radius) -> "Circle with radius " + radius;
        case Rectangle(double w, double h) -> "Rectangle " + w + "x" + h;
        case Triangle(double a, double b, double c) -> "Triangle " + a + "," + b + "," + c;
    };
}

// Nested patterns
public record Order(String id, User user, List<Item> items) {}
public record User(String name, int age) {}
public record Item(String name, double price) }

String description = switch (order) {
    case Order(String id, User(String name, int age), var items) 
        -> "Order by " + name + " with " + items.size() + " items";
};
```

## Internal Working

### Records Internals

```
record User(String name, int age) compiles to:
- final class User
- private final fields: name, age
- constructor: User(String name, int age)
- accessor methods: name(), age()
- equals(), hashCode(), toString()
```

### Sealed Classes Internals

```
sealed interface Shape permits Circle, Rectangle:
- JVM enforces only permitted subclasses can implement
- Enables exhaustive switch/match
- Subclasses must be final, sealed, or non-sealed
```

## Syntax

```java
// Record
public record Point(int x, int y) {}
public record Range(int start, int end) {
    public Range { // Compact constructor
        if (start > end) throw new IllegalArgumentException();
    }
}

// Sealed class
public sealed interface Shape permits Circle, Rectangle {}
public final class Circle implements Shape {}
public non-sealed class CustomShape implements Shape {}

// Pattern matching
Object obj = "hello";
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}

// Text block
String html = """
    <html>
        <body>
            <p>Hello, World!</p>
        </body>
    </html>
    """;

// Switch expression
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY -> 7;
    default -> throw new IllegalArgumentException();
};
```

## Examples

### Easy: Records
```java
public record Person(String name, int age, String email) {
    public Person {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    }
    
    public String summary() {
        return name + " (" + age + ")";
    }
}

public class RecordDemo {
    public static void main(String[] args) {
        Person alice = new Person("Alice", 30, "alice@example.com");
        System.out.println(alice);          // Person[name=Alice, age=30, email=alice@example.com]
        System.out.println(alice.name());   // Alice
        System.out.println(alice.summary()); // Alice (30)
    }
}
```

### Medium: Sealed Classes + Pattern Matching
```java
public sealed interface Payment 
    permits CreditCard, BankTransfer, PayPal {
}

public record CreditCard(String number, String expiry) implements Payment {}
public record BankTransfer(String account, String routing) implements Payment {}
public record PayPal(String email) implements Payment {}

public class PaymentProcessor {
    public String process(Payment payment) {
        return switch (payment) {
            case CreditCard cc -> "Processing card " + cc.number().substring(0, 4) + "****";
            case BankTransfer bt -> "Transferring to account " + bt.account();
            case PayPal pp -> "Processing PayPal for " + pp.email();
        };
    }
}
```

### Hard: Nested Record Patterns
```java
public record Order(String id, Customer customer, List<LineItem> items) {}
public record Customer(String name, Address address) {}
public record Address(String city, String country) {}
public record LineItem(String product, int quantity, double price) {}

public class OrderAnalyzer {
    public String analyze(Order order) {
        return switch (order) {
            case Order(String id, Customer(String name, Address(String city, var _)), var items) 
                when items.size() > 3 -> 
                    "Large order #" + id + " by " + name + " from " + city;
            case Order(var id, Customer(var name, _), var items) 
                -> "Order #" + id + " by " + name + " with " + items.size() + " items";
        };
    }
}
```

### Enterprise: Text Blocks + Records
```java
public record ApiRequest(String endpoint, Map<String, String> headers, String body) {
    public static ApiRequest of(String endpoint, String body) {
        return new ApiRequest(endpoint, Map.of(), body);
    }
}

public class ApiClient {
    public String buildRequest(ApiRequest request) {
        return """
            {
                "endpoint": "%s",
                "headers": %s,
                "body": "%s"
            }
            """.formatted(
                request.endpoint(),
                request.headers().entrySet().stream()
                    .map(e -> "\"%s\": \"%s\"".formatted(e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", ", "{", "}")),
                request.body()
            );
    }
}
```

## Performance Considerations

| Feature | Cost | Notes |
|---------|------|-------|
| Records | Same as classes | No runtime overhead |
| Sealed classes | Minimal | JVM optimization |
| Pattern matching | Same as if-else | Compiler optimization |
| Text blocks | Same as strings | Compile-time only |
| var | Same as explicit types | Compile-time only |

## Best Practices

**Do's:**
- Use records for immutable data carriers
- Use sealed classes for restricted hierarchies
- Use pattern matching for type checks
- Use text blocks for multi-line strings
- Use var for obvious types
- Use switch expressions for concise branching

**Don'ts:**
- Don't use records for mutable state
- Don't use sealed classes without clear hierarchy
- Don't overuse var (hides types)
- Don't use text blocks for short strings
- Don't use switch statements when expressions work

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Mutable record fields | Records are immutable | Use classes for mutable state |
| Overusing var | Hides types | Use when type is obvious |
| Text blocks with trailing space | Extra whitespace | Trim or use `.` indentation |
| Incomplete switch on sealed | Compile error | Handle all permitted types |
| Record with validation | Compact constructor | Use compact constructor for validation |

## Interview Questions

### Q1: What is a record and when to use it?
**Answer:** A record is a compact class for immutable data. Use for DTOs, value objects, and data carriers. It auto-generates constructor, accessors, equals, hashCode, toString.

### Q2: What is the difference between sealed and final classes?
**Answer:** Sealed classes restrict which classes can extend them (permits clause). Final classes cannot be extended at all. Sealed classes enable exhaustive pattern matching.

### Q3: What is pattern matching for switch?
**Answer:** A switch that matches types and extracts values: `case Circle c ->`. Enables exhaustive matching on sealed types; eliminates verbose instanceof chains.

### Q4: What is the difference between text blocks and regular strings?
**Answer:** Text blocks preserve formatting and multi-line content without escape sequences. They're compiled to the same String class but are easier to write and read.

### Q5: When should you use var?
**Answer:** When the type is obvious from context: `var list = new ArrayList<String>()`. Don't use when type is unclear: `var result = compute()`.

### Q6: What are record patterns?
**Answer:** Patterns that destructure records: `case User(String name, int age) ->`. Enables extracting record components in pattern matching.

### Q7: What is a compact constructor?
**Answer:** A record constructor without parameters list: `public Person { if (age < 0) throw... }`. Used for validation and normalization.

### Q8: What is the difference between switch statements and expressions?
**Answer:** Statements use `case:` with `break`. Expressions use `->` without break, return values. Expressions are concise and exhaustive.

### Q9: Can records implement interfaces?
**Answer:** Yes. Records can implement interfaces but cannot extend classes (they implicitly extend `java.lang.Record`).

### Q10: What is the difference between sealed and non-sealed classes?
**Answer:** Sealed restricts inheritance to listed classes. Non-sealed allows any class to extend (for flexibility in sealed hierarchies).

## Cross-References

- **Previous Module:** [15 - Senior](../15-senior/)
- **Related:** [02 - OOP](../02-oop/) — inheritance, polymorphism
- **Related:** [06 - Generics](../06-generics/) — type inference
- **Related:** [07 - Functional Programming](../07-functional-programming/) — lambda integration
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — virtual threads (Java 21)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Record not serializing | Check fields | Verify all fields are serializable |
| Sealed class compile error | Check permits | Verify all permitted classes exist |
| Pattern matching incomplete | Check sealed types | Handle all permitted types |
| Text block whitespace | Check indentation | Use `.` for indent control |
| var type inference failure | Explicit type | Use explicit type when inference fails |

## Code Review Checklist

- [ ] Records used for immutable data
- [ ] Sealed classes have clear hierarchy
- [ ] Pattern matching is exhaustive
- [ ] Text blocks have correct indentation
- [ ] var used only when type is obvious
- [ ] Switch expressions handle all cases

## Architecture Considerations

Modern Java features enable cleaner, safer code at scale. Records reduce boilerplate for DTOs; sealed classes enable safe domain modeling; pattern matching simplifies complex conditionals. For microservices, records improve API contracts; sealed classes enable safe event modeling.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Records | DTOs, value objects | Pros: Immutable, concise; Cons: No mutability |
| Sealed classes | Domain modeling | Pros: Safe hierarchies; Cons: Coupling |
| Pattern matching | Type dispatch | Pros: Exhaustive, concise; Cons: Learning curve |
| Text blocks | SQL, JSON, HTML | Pros: Readable; Cons: Formatting sensitivity |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Record with sensitive data | Information exposure | Don't include passwords in records |
| Sealed class bypass | Invalid subtypes | Verify permits clause |
| Text block injection | Code injection | Validate text block content |
| Pattern matching bypass | Unhandled types | Use exhaustive matching |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 10 | `var` | Use for obvious types |
| Java 13 | Text blocks (preview) | Wait for finalization |
| Java 14 | Switch expressions (preview) | Wait for finalization |
| Java 15 | Text blocks (final) | Use for multi-line strings |
| Java 16 | Records, instanceof pattern | Use for immutable data |
| Java 17 | Sealed classes | Use for restricted hierarchies |
| Java 21 | Pattern matching for switch | Use for type dispatch |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| `var` | Java 10 | Stable |
| Text blocks | Java 15 | Stable |
| Switch expressions | Java 14 | Stable |
| Records | Java 16 | Stable |
| Sealed classes | Java 17 | Stable |
| Pattern matching for instanceof | Java 16 | Stable |
| Pattern matching for switch | Java 21 | Stable |
| Record patterns | Java 21 | Stable |

## Production Incidents

### Incident 1: Record Used for Mutable State

**Problem:** A developer used a record for a mutable entity; database updates failed because records are immutable.
**Cause:** Developer assumed records were like regular classes; didn't understand immutability.
**Impact:** Database updates silently failed; data inconsistency discovered 3 days later.
**Detection:** Data inconsistency in production; investigation revealed record immutability.
**Solution:** Changed to regular class with setters; documented record immutability.
**Prevention:** Understand record semantics; use records only for immutable data.

### Incident 2: Incomplete Pattern Matching

**Problem:** A sealed class had a new subtype added; switch on it was incomplete; compile error.
**Cause:** Developer added new subtype without updating all switch statements.
**Impact:** Compile error blocked deployment; 2-hour delay.
**Detection:** Compile error in CI/CD pipeline.
**Solution:** Added missing case; used default for forward compatibility.
**Prevention:** Use exhaustive matching on sealed types; add default case for future subtypes.

### Incident 3: var Hiding Type Information

**Problem:** A developer used `var` for a complex type; code was unreadable; new developers confused.
**Cause:** `var result = service.process(input)` — type not obvious from context.
**Impact:** Code review delays; onboarding time increased.
**Detection:** Code review feedback; new developer complaints.
**Solution:** Replaced with explicit type; documented var usage guidelines.
**Prevention:** Use var only when type is obvious; prefer explicit types for complex types.

## Production Checklist

- [ ] Records used for immutable data only
- [ ] Sealed classes have clear, documented hierarchies
- [ ] Pattern matching is exhaustive
- [ ] Text blocks have correct indentation
- [ ] var used only when type is obvious
- [ ] Switch expressions handle all cases
- [ ] Backward compatibility considered

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses traditional syntax; doesn't understand modern features |
| Intermediate | Uses records, var, text blocks; understands basics |
| Advanced | Uses sealed classes, pattern matching; applies features appropriately |
| Expert | Designs with modern features; contributes to Java evolution; mentors on modernization |

## Common Myths

1. **Myth**: Records are just shortcut for classes
   **Truth**: Records are immutable, have auto-generated methods, and implicitly extend `Record`. They're fundamentally different from classes.

2. **Myth**: var makes Java dynamically typed
   **Truth**: var is compile-time type inference. The type is still static; it's just inferred from the initializer.

3. **Myth**: Pattern matching is only for simple types
   **Truth**: Pattern matching works with sealed hierarchies, nested patterns, and guards. It's powerful for complex domain modeling.

4. **Myth**: Text blocks are just concatenation
   **Text blocks preserve formatting, support indentation control, and are compiled to String. They're more than concatenation.

5. **Myth**: Modern features are always better
   **Truth**: Use modern features when they add value. Traditional syntax may be clearer in some contexts.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Reduce boilerplate, improve safety, enable new patterns |
| Records | Immutable data carriers (Java 16) |
| Sealed classes | Restricted inheritance (Java 17) |
| Pattern matching | Type-safe conditionals (Java 21) |
| Text blocks | Multi-line strings (Java 15) |
| Switch expressions | Concise branching (Java 14) |
| var | Type inference (Java 10) |
| Record patterns | Destructure records (Java 21) |
| Best practice | Use when they add value |
| Common mistake | Overusing var, mutable records |
| When to use | Java 17+ applications |
| When to avoid | Older Java versions |
