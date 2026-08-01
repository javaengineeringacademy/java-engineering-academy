# Records (Java 16+)

## 1. Introduction

Records provide a compact syntax for declaring immutable data carriers. Introduced as a preview feature in Java 14 and finalized in Java 16, records reduce boilerplate while enforcing immutability and structural transparency.

## 2. Learning Objectives

- Understand the syntax and semantics of record classes
- Differentiate records from traditional POJOs
- Implement custom logic within records
- Apply records in enterprise patterns such as DTOs and value objects
- Leverage pattern matching with records (Java 16+)

## 3. Prerequisites

- Basic Java syntax and OOP concepts
- Familiarity with classes, constructors, and `equals`/`hashCode`
- Understanding of immutability principles

## 4. Why This Concept Exists

Traditional Java classes for data transfer require verbose constructors, getters, `equals`, `hashCode`, and `toString` methods. Records eliminate this boilerplate, enforce immutability at the language level, and provide a standardized structure for data carriers.

## 5. Problem Statement

Consider a `Point` class without records:

```java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() { return x; }
    public int y() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point p)) return false;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() { return Objects.hash(x, y); }

    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

This is over 30 lines for a simple data carrier. A record reduces this to a single line.

## 6. Theory

A record is a special kind of class that implicitly extends `java.lang.Record`. It declares its state as a compact list of components, from which the compiler generates:

- A canonical constructor
- Accessor methods for each component
- `equals()`, `hashCode()`, and `toString()` implementations
- The class is implicitly `final`

Records cannot extend other classes (they extend `Record`), but they can implement interfaces. All fields are `private final`.

## 7. Internal Working

The compiler generates a class file equivalent to:

```java
// What the compiler generates for: record Person(String name, int age)
public final class Person extends Record {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name() { return name; }
    public int age() { return age; }

    @Override
    public boolean equals(Object o) { /* based on all components */ }

    @Override
    public int hashCode() { /* based on all components */ }

    @Override
    public String toString() {
        return "Person[name=" + name + ", age=" + age + "]";
    }

    @Override
    public String getdeclaringclass() { return Person.class; }
}
```

## 8. JVM Perspective

The JVM treats records as regular classes with the `Record` flag set in the class metadata. The `Record` attribute stores component names and descriptors in the class file. The JVM does not have special bytecode instructions for records; the compiler desugars them into standard class structures.

From a reflection standpoint, `Class.isRecord()` returns `true`, and `RecordComponent[] getRecordComponents()` provides metadata about each component.

## 9. Memory Representation

```
Heap Layout:
┌─────────────────────────────────┐
│  Person (extends Record)        │
│  ┌───────────────────────────┐  │
│  │ String name  ──────────►  │  │──► Heap: "Alice"
│  │ int age     = 30          │  │
│  └───────────────────────────┘  │
│  Class metadata + Record flag   │
└─────────────────────────────────┘
```

All fields are `final`, ensuring safe publication across threads without synchronization.

## 10. Architecture Diagram

```
          ┌──────────────┐
          │  Record Class │
          └──────┬───────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
Components   Methods    Generated
  (fields)   (custom)   Methods
                             │
               ┌─────────────┼─────────────┐
               │             │             │
               ▼             ▼             ▼
          equals()     hashCode()    toString()
```

## 11. Flow Diagram

```
Source: record Person(String name, int age)
    │
    ▼
Compiler Analysis
    │
    ├─► Generate canonical constructor
    ├─► Generate accessor methods
    ├─► Generate equals/hashCode/toString
    ├─► Mark class as final
    └─► Validate no mutable state
    │
    ▼
Bytecode: Class with Record attribute
```

## 12. Syntax

### Basic Record
```java
public record Point(int x, int y) {}
```

### Record with Compact Constructor
```java
public record Person(String name, int age) {
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age must be non-negative");
        }
    }
}
```

### Record with Custom Methods
```java
public record Person(String name, int age) {
    public boolean isAdult() {
        return age >= 18;
    }
}
```

### Record Implementing Interfaces
```java
public interface Printable {
    void print();
}

public record Person(String name, int age) implements Printable {
    @Override
    public void print() {
        System.out.println(name() + ", age " + age);
    }
}
```

### Generic Record
```java
public record Pair<A, B>(A first, B second) {}
```

## 13. Easy Example

```java
public record Color(int red, int green, int blue) {}

public class Main {
    public static void main(String[] args) {
        Color red = new Color(255, 0, 0);
        System.out.println(red.red());           // 255
        System.out.println(red.toString());       // Color[red=255, green=0, blue=0]

        Color same = new Color(255, 0, 0);
        System.out.println(red.equals(same));     // true
    }
}
```

## 14. Medium Example

```java
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException(
                "start (%d) must be <= end (%d)".formatted(start, end)
            );
        }
    }

    public int length() {
        return end - start;
    }

    public boolean contains(int value) {
        return value >= start && value < end;
    }

    public Range intersect(Range other) {
        int newStart = Math.max(this.start, other.start);
        int newEnd = Math.min(this.end, other.end);
        return new Range(newStart, Math.max(newStart, newEnd));
    }
}

public class Main {
    public static void main(String[] args) {
        Range r = new Range(1, 10);
        System.out.println(r.length());          // 9
        System.out.println(r.contains(5));       // true

        Range r2 = new Range(5, 15);
        Range intersection = r.intersect(r2);
        System.out.println(intersection);        // Range[start=5, end=10]
    }
}
```

## 15. Hard Example

```java
import java.util.List;
import java.util.Objects;

public record Money(String currency, long amountInCents) {
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (amountInCents < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
    }

    public static Money of(String currency, long amountInCents) {
        return new Money(currency, amountInCents);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(currency, this.amountInCents + other.amountInCents);
    }

    public Money multiply(int factor) {
        return new Money(currency, this.amountInCents * factor);
    }

    public String format() {
        long whole = amountInCents / 100;
        long cents = amountInCents % 100;
        return "%s %d.%02d".formatted(currency, whole, cents);
    }
}

public class Main {
    public static void main(String[] args) {
        Money price = Money.of("USD", 1999);
        Money tax = Money.of("USD", 160);
        Money total = price.add(tax);
        System.out.println(total.format()); // USD 21.59
    }
}
```

## 16. Enterprise Example

```java
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        Objects.requireNonNull(value, "value");
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

public record OrderItem(
    String productId,
    String name,
    int quantity,
    long priceInCents
) {
    public OrderItem {
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(name, "name");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (priceInCents < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
    }

    public long totalPrice() {
        return priceInCents * quantity;
    }
}

public record Order(
    OrderId id,
    String customerId,
    List<OrderItem> items,
    Instant createdAt
) {
    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(createdAt, "createdAt");
        items = List.copyOf(items); // Defensive copy for immutability
    }

    public long totalAmount() {
        return items.stream()
            .mapToLong(OrderItem::totalPrice)
            .sum();
    }

    public int itemCount() {
        return items.stream()
            .mapToInt(OrderItem::quantity)
            .sum();
    }
}
```

## 17. Performance

Records have no runtime performance overhead compared to manually written classes. The compiler generates equivalent bytecode. Accessor methods are not inlined by default, but the JVM's JIT compiler can inline them during hot path execution.

Memory layout is identical to an equivalent hand-written class with the same field types and order.

## 18. Time Complexity

| Operation | Complexity |
|-----------|------------|
| Constructor | O(n) where n = number of components |
| Accessor | O(1) |
| equals() | O(n) worst case, short-circuits on mismatch |
| hashCode() | O(n) |
| toString() | O(n) |
| RecordComponent access | O(1) per component |

## 19. Space Complexity

A record uses O(n) space where n is the number of components, identical to a manually written class. The class metadata adds a fixed overhead per class. Records do not introduce additional per-instance overhead beyond their fields.

## 20. Thread Safety

Records are inherently thread-safe due to immutability:

- All fields are `final`, guaranteeing safe publication under the Java Memory Model
- No setter methods exist
- Defensive copies (via `List.copyOf()`) prevent external mutation of mutable objects within the record
- The canonical constructor should avoid publishing `this` references before all fields are assigned

```java
public record ThreadSafePoint(int x, int y) {
    // Safe: final fields, no mutable state
}
```

## 21. Best Practices

1. **Use for immutable data only** — Records are inherently immutable; do not add mutable fields.
2. **Validate in compact constructor** — Enforce invariants before the canonical constructor assigns fields.
3. **Keep records small** — Large records with many fields reduce readability; consider splitting.
4. **Implement interfaces, not abstract classes** — Records are implicitly `final`; they cannot extend classes.
5. **Override `equals()` / `hashCode()` carefully** — Auto-generated methods use all components; exclude fields if needed.
6. **Use serialization sparingly** — Records implement `Serializable` by default but may not suit all serialization frameworks.
7. **Prefer records over `@Value` (Lombok)** — Records are language-level, require no annotation processor, and are more transparent.
8. **Use pattern matching with records** — Combine with `instanceof` patterns for expressive destructuring (Java 17+).
9. **Name components clearly** — Accessor methods drop the `get` prefix (`name()` not `getName()`); choose names accordingly.
10. **Document invariants** — Compact constructors should clearly state and enforce preconditions.

## 22. Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Adding mutable fields | Breaks immutability contract | Use unmodifiable wrappers for collections |
| No validation | Invalid state allowed | Validate in compact constructor |
| Extending a class | Records implicitly extend `Record` | Use composition or interfaces |
| Overriding equals to exclude fields | Breaks symmetry contract | Override carefully or exclude via design |
| Returning mutable internal state | External mutation possible | Return defensive copies |
| Using records for entities with identity | Records use value equality | Use traditional classes for identity-based objects |

## 23. Pitfalls

- Records use **value equality** (all components), not identity equality. This may cause unexpected behavior when used in sets or maps.
- Records cannot have instance fields beyond the declared components.
- Serialization is automatic but may not be compatible with all frameworks (e.g., Jackson before 2.12 requires configuration).
- Generic type parameters are erased at runtime, so `instanceof Pair<String, Integer>` is not valid.
- Compact constructors must not assign fields directly; the canonical constructor handles assignment after the compact constructor runs.

## 24. Debugging Tips

- Use `toString()` output to verify record content during debugging.
- Inspect `RecordComponent[]` via reflection to dynamically access component metadata.
- Verify immutability by checking that all fields are `final` and collections are wrapped with `List.copyOf()`, `Map.copyOf()`, or `Set.copyOf()`.
- Use `jshell` for quick record prototyping: `jshell> record Point(int x, int y) {}`
- Check `Class.isRecord()` in debug code to confirm a type is a record.

## 25. Comparison Table

| Feature | Record | POJO (Lombok @Value) | Enum |
|---------|--------|----------------------|------|
| Immutability | Language-level | Annotation-based | Language-level |
| Boilerplate | Minimal | Minimal | Moderate |
| Extends class | No (extends Record) | Any | No (extends Enum) |
| Implements interface | Yes | Yes | Yes |
| Pattern matching | Native support | No | Limited |
| Serialization | Built-in (Serializable) | Depends on annotations | Custom required |
| Validation | Compact constructor | Constructor / setter | Constructor only |
| Reflection support | `getRecordComponents()` | Standard | Limited |

## 26. Decision Tree

```
Need an immutable data carrier?
├── Yes
│   ├── Simple data with no behavior? → Record
│   ├── Need validation? → Record with compact constructor
│   ├── Need custom equals/hashCode? → Record with override
│   └── Need mutable state? → Traditional class
├── Need an enum-like fixed set? → Enum
└── Need a mutable entity? → Traditional class
```

## 27. Interview Questions

1. **What is a record in Java?**
   A record is a special kind of class that serves as an immutable data carrier. It automatically generates a canonical constructor, accessor methods, `equals()`, `hashCode()`, and `toString()`.

2. **Can records implement interfaces?**
   Yes. Records cannot extend classes but can implement one or more interfaces.

3. **What is the compact constructor?**
   A compact constructor validates or transforms arguments without manually assigning fields. The canonical constructor handles field assignment after the compact constructor runs.

4. **Are records thread-safe?**
   Yes, due to immutability. All fields are `final`, ensuring safe publication.

5. **Can records have static fields?**
   Yes. Records can contain static fields, static methods, and static constants.

6. **How do records handle equality?**
   Two records are equal if they are of the same type and all components are equal (value equality).

7. **Can a record override `equals()`?**
   Yes, but it is rarely necessary. The auto-generated implementation uses all components.

8. **What is `Record` class in Java?**
   `java.lang.Record` is the implicit superclass of all record classes. It provides the `getdeclaringclass()` method and serves as the base for record reflection APIs.

9. **Can records be generic?**
   Yes. Example: `record Pair<A, B>(A first, B second) {}`

10. **When should you not use a record?**
    When you need mutable state, inheritance hierarchy, or identity-based equality.

## 28. Exercises

1. **Create a `Vector2D` record** with methods for `add`, `subtract`, `dotProduct`, and `magnitude`.
2. **Create a `Range` record** with methods for `contains`, `intersect`, and `union`. Validate that start <= end.
3. **Create a `Currency` record** with a compact constructor that normalizes the currency code to uppercase.
4. **Implement a `JsonSerializable` interface** and create a record that implements it with a `toJson()` method.
5. **Use pattern matching** in a `switch` expression to process a `Shape` hierarchy where `Circle`, `Rectangle`, and `Triangle` are records.

## 29. Assignments

1. **Refactor an existing POJO** (Student, Employee, or Product) into a record. Identify what validation logic moves into the compact constructor.
2. **Build a configuration loader** that reads properties into a record with validation.
3. **Create a generic `Result<T>` record** representing success or failure, with methods like `isOk()`, `isError()`, and `orElse()`.

## 30. Mini Project

**Order Processing System**

Create a record-based order processing system:

```java
public record Product(String id, String name, long priceCents) {}
public record OrderItem(Product product, int quantity) {
    public long subtotal() { return product.priceCents() * quantity; }
}
public record Order(String id, List<OrderItem> items) {
    public long total() { return items.stream().mapToLong(OrderItem::subtotal).sum(); }
}
```

Extend with:
- Validation in each compact constructor
- A `Receipt` record for printing order summaries
- A `Discount` interface implemented by various discount record types

## 31. Summary

Records provide a concise, language-level mechanism for creating immutable data carriers. They reduce boilerplate, enforce immutability, integrate with pattern matching, and produce clean, maintainable code. Use them for DTOs, value objects, configuration holders, and any scenario where immutable, transparent data representation is needed.

## 32. References

- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JLS §8.10 — Record Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.10)
- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [Baeldung — Java Records](https://www.baeldung.com/java-records)
- [Oracle — Records Tutorial](https://docs.oracle.com/en/java/javase/21/language/records.html)
- [Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
