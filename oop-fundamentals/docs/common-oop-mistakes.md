# Common OOP Mistakes: Top 20 Pitfalls and How to Avoid Them

## 1. Introduction

Object-Oriented Programming in Java is powerful but full of subtle pitfalls. Even experienced developers make mistakes that lead to bugs, performance issues, and design rot. This guide documents the top 20 OOP mistakes with wrong/correct code, explanations, and enterprise examples to help you write production-quality code.

## 2. Learning Objectives

- Identify the 20 most common OOP mistakes in Java
- Understand why each mistake is harmful
- Apply correct patterns and idioms to avoid each mistake
- Recognize these mistakes in code reviews
- Build muscle memory for correct Java practices

## 3. Prerequisites

- Solid understanding of Java OOP (classes, inheritance, interfaces)
- Familiarity with collections, exceptions, and generics
- Basic understanding of JVM memory model
- Knowledge of `equals()`, `hashCode()`, and `toString()` contracts

## 4. Why This Concept Exists

OOP mistakes often stem from:
- **Incomplete understanding** of Java semantics (`==` vs `.equals()`)
- **Assuming behavior** from other languages (Python, C++)
- **Skipping fundamentals** (equals/hashCode contract)
- **Over-engineering** (unnecessary inheritance hierarchies)
- **Ignoring thread safety** (mutable shared state)

These mistakes compound over time, creating technical debt that becomes expensive to fix.

## 5. Problem Statement

A codebase with OOP mistakes exhibits:
- Subtle bugs (`HashMap` lookups failing silently)
- Performance degradation (unnecessary object creation)
- Concurrency issues (race conditions, deadlocks)
- Design rigidity (can't change implementations)
- Test difficulty (tight coupling, static dependencies)

The goal is to recognize and fix these issues systematically.

## 6. Theory

### 6.1 The equals/hashCode Contract

If two objects are equal via `equals()`, they **must** have the same `hashCode()`. Violating this breaks `HashMap`, `HashSet`, and any hash-based collection.

### 6.2 The Liskov Substitution Principle (LSP)

Subtypes must be substitutable for their base types without altering correctness. `Square extends Rectangle` violates LSP because `setWidth()` changes height.

### 6.3 Encapsulation

Internal state should be hidden behind a stable API. Public fields, mutable returns, and leaking `this` break encapsulation.

### 6.4 Immutability

Immutable objects are thread-safe, easier to reason about, and safe to share. Mutable objects require synchronization.

## 7. Internal Working

### How `HashMap` Uses `hashCode()` and `equals()`

```
map.put(key, value)
  1. key.hashCode() → bucket index
  2. Store entry in bucket

map.get(key)
  1. key.hashCode() → bucket index
  2. For each entry in bucket:
     if (entry.key.equals(key)) → return value
  3. If no match → return null
```

If two equal objects have different `hashCode()`, they go to different buckets. `get()` will never find the matching entry.

### How `instanceof` Checks Work

```
if (obj instanceof Dog) {
    Dog d = (Dog) obj; // Downcast
}
```

The JVM checks the object's actual type at runtime. If you use `instanceof` + cast everywhere, you're not using polymorphism.

## 8. JVM Perspective

### String Comparison

```java
String a = "hello";
String b = "hello";
String c = new String("hello");

a == b;     // true — string pool, same reference
a == c;     // false — different heap objects
a.equals(c); // true — same content
```

### Integer Caching

```java
Integer a = 127;
Integer b = 127;
a == b; // true — Integer cache (-128 to 127)

Integer x = 128;
Integer y = 128;
x == y; // false — different objects
```

### Memory Leaks with Static References

```java
class Cache {
    static Map<String, Object> data = new HashMap<>(); // Never cleared
}
```

Static fields are never garbage collected. Objects referenced by static fields live forever.

## 9. Memory Representation

```
String Comparison
┌──────────────┐     ┌──────────────┐
│ a ──────────┼────→│ "hello" (pool)│ ←── b
└──────────────┘     └──────────────┘
┌──────────────┐
│ c ──────────┼────→ "hello" (heap) ←── different object!
└──────────────┘

Integer Caching
┌──────────────┐     ┌──────────────┐
│ a (127) ─────┼────→│ Integer(127) │ ←── b (same object)
└──────────────┘     └──────────────┘
┌──────────────┐     ┌──────────────┐
│ x (128) ─────┼────→│ Integer(128) │
└──────────────┘     └──────────────┘
┌──────────────┐     ┌──────────────┐
│ y (128) ─────┼────→│ Integer(128) │ ←── different object!
└──────────────┘     └──────────────┘
```

## 10. Syntax

### Mistake 1: String == Comparison
```java
// WRONG
if (str == "hello") { }

// CORRECT
if ("hello".equals(str)) { } // null-safe
// OR
if (Objects.equals(str, "hello")) { } // null-safe
```

### Mistake 2: Missing hashCode with equals
```java
// WRONG
@Override
public boolean equals(Object o) {
    return this.name.equals(((User) o).name);
}
// hashCode not overridden — HashMap breaks!

// CORRECT
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User u)) return false;
    return Objects.equals(name, u.name);
}

@Override
public int hashCode() {
    return Objects.hash(name);
}
```

### Mistake 3: Mutable static state
```java
// WRONG
class Config {
    static Map<String, String> settings = new HashMap<>();
}

// CORRECT
class Config {
    private static final Map<String, String> SETTINGS = Map.copyOf(Map.of(
        "db.host", "localhost",
        "db.port", "5432"
    ));
}
```

## 11. Easy Example

```java
public class EasyMistakes {
    public static void main(String[] args) {
        // Mistake 1: String == comparison
        String a = "hello";
        String b = new String("hello");
        System.out.println(a == b);      // false — WRONG assumption
        System.out.println(a.equals(b)); // true — correct

        // Mistake 2: Integer == comparison
        Integer x = 200;
        Integer y = 200;
        System.out.println(x == y);      // false — outside cache
        System.out.println(x.equals(y)); // true — correct

        // Mistake 3: Null returns
        String result = findName("unknown");
        // result.length() → NPE!
    }

    // Mistake 3: Returning null
    static String findName(String id) {
        if ("unknown".equals(id)) return null; // BAD
        return "Alice";
    }

    // CORRECT: Return Optional
    static Optional<String> findNameSafe(String id) {
        if ("unknown".equals(id)) return Optional.empty();
        return Optional.of("Alice");
    }
}
```

## 12. Medium Example

```java
public class MediumMistakes {
    public static void main(String[] args) {
        // Mistake 4: equals without hashCode
        var set = new java.util.HashSet<>();
        set.add(new User("Alice"));
        System.out.println(set.contains(new User("Alice"))); // false! WRONG

        // Mistake 5: Mutable hashCode
        var map = new java.util.HashMap<>();
        var user = new MutableUser("Bob");
        map.put(user, "admin");
        user.setName("Robert"); // hashCode changes!
        System.out.println(map.get(user)); // null — lost in hash table!

        // Mistake 6: String concatenation in loop
        String result = "";
        for (int i = 0; i < 10000; i++) {
            result += i; // O(n²) — creates new String each time
        }

        // CORRECT: Use StringBuilder
        var sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append(i); // O(n)
        }
    }

    record User(String name) {
        // Missing hashCode() — breaks HashSet
    }

    static class MutableUser {
        private String name;

        MutableUser(String name) { this.name = name; }
        void setName(String name) { this.name = name; }

        @Override
        public int hashCode() {
            return Objects.hash(name); // Breaks when name changes!
        }
    }
}
```

## 13. Hard Example

```java
public class HardMistakes {
    public static void main(String[] args) throws Exception {
        // Mistake 7: Calling overridable method in constructor
        var parent = new Parent();
        // Child's overridden method called before child is initialized!

        // Mistake 8: Mutable shared state without synchronization
        var counter = new SharedCounter();
        var t1 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });
        var t2 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Counter: " + counter.getCount()); // Not 2000!

        // Mistake 9: Deep inheritance hierarchy
        // See Vehicle hierarchy in Mistakes section
    }

    static class Parent {
        Parent() {
            doSomething(); // Called before Child is fully constructed
        }

        void doSomething() {
            System.out.println("Parent.doSomething()");
        }
    }

    static class Child extends Parent {
        private String field = "initialized";

        @Override
        void doSomething() {
            System.out.println("Child field: " + field); // null! Not yet initialized
        }
    }

    static class SharedCounter {
        private int count = 0;

        void increment() {
            count++; // NOT atomic — race condition
        }

        int getCount() { return count; }
    }
}
```

## 14. Enterprise Example

```java
// Enterprise example: Order processing with OOP mistakes fixed
public final class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items;
    private final Money total;
    private final LocalDateTime createdAt;

    public Order(String id, Customer customer, List<OrderItem> items) {
        this.id = Objects.requireNonNull(id);
        this.customer = Objects.requireNonNull(customer);
        this.items = List.copyOf(items); // Defensive copy
        this.total = calculateTotal();
        this.createdAt = LocalDateTime.now();
    }

    private Money calculateTotal() {
        return items.stream()
            .map(OrderItem::subtotal)
            .reduce(Money.ZERO, Money::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getters return defensive copies
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Money getTotal() { return total; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

public record Customer(String id, String name, String email) {
    public Customer {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
    }
}

public record OrderItem(String productId, String name, int quantity, Money price) {
    public OrderItem {
        Objects.requireNonNull(productId);
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
    }

    public Money subtotal() {
        return price.multiply(quantity);
    }
}

public record Money(BigDecimal amount, String currency) {
    public static final Money ZERO = new Money(BigDecimal.ZERO, "USD");

    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount.add(other.amount), currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), currency);
    }
}
```

## 15. Performance

| Mistake | Impact | Fix |
|---------|--------|-----|
| String `+` in loops | O(n²) time, O(n²) memory | `StringBuilder` |
| `==` for Integer | Wrong equality check | `.equals()` or cache |
| Mutable static state | Memory leak, thread safety | Immutable or synchronized |
| `finalize()` | Unpredictable timing, slow | Try-with-resources / Cleaner |
| Deep inheritance | Extra method dispatch overhead | Composition |
| `instanceof` chains | Slow virtual dispatch | Polymorphism |
| Public mutable fields | No JIT optimization possible | Private + getters |
| Unused imports/dependencies | Slower compilation | Clean imports |

## 16. Best Practices

1. **Always override `hashCode()` with `equals()`** — And vice versa
2. **Use `record` for data carriers** — Auto-generates `equals`, `hashCode`, `toString`
3. **Use `Objects.requireNonNull()`** — Fail fast on null arguments
4. **Use `Objects.equals()` for null-safe comparison** — Avoids NPE
5. **Prefer `final` fields** — Immutable, thread-safe, self-documenting
6. **Return `Optional<T>` instead of `null`** — Forces callers to handle absence
7. **Use `sealed` interfaces** — Restricted type hierarchies
8. **Favor composition over inheritance** — Avoid fragile base class
9. **Use `try-with-resources`** — Guaranteed resource cleanup
10. **Avoid mutable static state** — Use dependency injection instead

## 17. Common Mistakes

| # | Mistake | Why It's Wrong | Fix |
|---|---------|---------------|-----|
| 1 | `str == "hello"` | Reference comparison | `.equals()` or `Objects.equals()` |
| 2 | `Integer a = 128; a == 128` | Auto-unboxing pitfall | `.equals()` |
| 3 | `equals()` without `hashCode()` | Breaks `HashMap`/`HashSet` | Override both |
| 4 | Mutable `hashCode()` field | Hash table entries lost | Immutable fields only |
| 5 | `finalize()` for cleanup | Deprecated, unreliable | Try-with-resources |
| 6 | `instanceof` chains | Not polymorphic | Use polymorphism |
| 7 | Returning `null` | NPE risk | `Optional<T>` or empty collection |
| 8 | Public fields | No encapsulation | Private + getters |
| 9 | Mutable static state | Thread safety, memory leaks | Immutable or synchronized |
| 10 | `Square extends Rectangle` | LSP violation | Composition |
| 11 | Deep inheritance (5+ levels) | Fragile, hard to maintain | Flatten or compose |
| 12 | Calling overridable in constructor | Uses uninitialized subclass | Avoid or use factory |
| 13 | Catching `Exception`/`Throwable` | Masks bugs | Catch specific exceptions |
| 14 | Swallowing exceptions | Hides errors | Log or rethrow |
| 15 | Empty catch block | Silent failures | At minimum, log |
| 16 | `throw e` (loses stack) | Stack trace lost | `throw` or `throw new X(e)` |
| 17 | String `+` in loops | O(n²) performance | `StringBuilder` |
| 18 | `==` for enum comparison | Works but misleading | Use `==` (it's fine for enums) |
| 19 | `volatile` for compound actions | Not atomic | Use `Atomic*` or locks |
| 20 | `Thread.stop()` / `suspend()` | Deprecated, dangerous | Use interruption |

## 18. Pitfalls

1. **The `equals()` null trap** — `this.field.equals(other.field)` throws NPE if `field` is null
2. **The Integer cache trap** — `==` works for small values (≤127), fails for large ones
3. **The `this` leak in constructor** — Passing `this` to another object before construction completes
4. **The overridable method trap** — Calling overridable methods in constructors
5. **The `String` pool assumption** — Assuming `==` works for all Strings
6. **The `finalize()` deprecation** — `finalize()` is deprecated since Java 9, unreliable
7. **The `volatile` misconception** — `volatile` doesn't make compound operations atomic

## 19. Debugging Tips

1. **Use `equals()` for value comparison** — Never use `==` for object equality
2. **Use `Objects.hash()` for `hashCode()`** — Consistent with `equals()`
3. **Enable `-Xlog:gc*`** — Detect memory leaks from mutable statics
4. **Use `jconsole` or `jvisualvm`** — Inspect live objects and threads
5. **Use IDE inspections** — IntelliJ/Eclipse detect missing `hashCode()`, null risks
6. **Write tests for `equals()`/`hashCode()`** — Contract compliance tests
7. **Use `@Override`** — Catches signature mismatches at compile time
8. **Check stack traces** — NPE with line number tells you which field was null

## 20. Comparison Table

| Mistake | Language | Severity | Detection | Fix Difficulty |
|---------|----------|----------|-----------|----------------|
| `==` for String | Java | High | Runtime bug | Easy |
| Missing `hashCode()` | Java | High | Silent failure | Easy |
| Mutable `hashCode()` | Java | Critical | Hard to debug | Medium |
| Deep inheritance | OOP | Medium | Design smell | Hard |
| Constructor calling overridable | Java | Critical | Subtle bug | Medium |
| Swallowing exceptions | Java | High | Hidden failures | Easy |
| Mutable static state | Java | Critical | Concurrency bug | Hard |
| `finalize()` usage | Java | Medium | Performance | Easy |
| Public fields | OOP | Medium | Design smell | Easy |
| Returning null | Java | High | NPE risk | Easy |

## 21. Decision Tree

```
Are you comparing objects?
│
├── YES → Are they primitives?
│   ├── YES → Use == / != / .equals()
│   └── NO → Use .equals() (not ==)
│
├── Are you overriding equals()?
│   ├── YES → You MUST override hashCode() too
│   └── NO → Use default (identity-based)
│
├── Are you returning an object that might not exist?
│   ├── YES → Return Optional<T>
│   └── NO → Return the object directly
│
├── Are you creating a class hierarchy?
│   ├── Is it "is-a" with behavioral subtyping?
│   │   ├── YES → Use inheritance
│   │   └── NO → Use composition
│   └── Is the hierarchy deeper than 3 levels?
│       ├── YES → Flatten or use composition
│       └── NO → Inheritance is acceptable
│
├── Are you handling exceptions?
│   ├── Can you handle specifically?
│   │   ├── YES → Catch specific exception
│   │   └── NO → Catch Exception (log + rethrow)
│   └── Is the catch block empty?
│       └── YES → At minimum, log the exception
│
└── Are you sharing state across threads?
    ├── YES → Use immutable objects or synchronization
    └── NO → Regular objects are fine
```

## 22. Interview Questions

**Q1: What happens if you override `equals()` but not `hashCode()`?**
A: Objects that are logically equal will have different hash codes. `HashMap` and `HashSet` will treat them as different entries, causing silent failures (e.g., `contains()` returns false for logically equal objects).

**Q2: Why is `==` wrong for String comparison?**
A: `==` compares references (memory addresses), not content. Two different String objects with the same content will return `false` with `==`.

**Q3: What is the Liskov Substitution Principle?**
A: Subtypes must be substitutable for their base types without altering correctness. `Square extends Rectangle` violates LSP because `setWidth()` would also change height.

**Q4: Why is `finalize()` deprecated?**
A: It's unreliable (no guarantee of when or if it runs), slow (adds GC overhead), and can resurrect objects. Use try-with-resources or `java.lang.ref.Cleaner` instead.

**Q5: How do you make a class immutable?**
A: Make fields `final`, use a constructor to set them, don't provide setters, return defensive copies of mutable fields, and don't allow subclassing.

**Q6: What is the fragile base class problem?**
A: Changes to a base class can unexpectedly break subclasses. Adding new methods, changing existing methods, or modifying field access can all cause subclass failures.

**Q7: Why use `Optional<T>` instead of returning `null`?**
A: `Optional` makes the API explicit about potential absence. It forces callers to handle the empty case, eliminating NPEs.

## 23. Exercises

1. **Fix the equals/hashCode bug:** Given a `User` class with `equals()` but no `hashCode()`, write a test that demonstrates the `HashSet` failure, then fix it.

2. **Refactor inheritance to composition:** Take a `Vehicle → Car → SportsCar` hierarchy and refactor to use composition with a `VehicleType` enum.

3. **Thread-safe counter:** Implement a thread-safe counter using `AtomicInteger` instead of `volatile int`.

4. **Null-safe comparison:** Write a utility method that safely compares two objects that might be null.

5. **String optimization:** Refactor a method that concatenates strings in a loop to use `StringBuilder`.

## 24. Assignments

1. **OOP Code Review:** Review a provided codebase (200+ lines) and identify all OOP mistakes. Document each mistake with the fix.

2. **Immutable Class:** Design and implement an immutable `Money` class with `equals()`, `hashCode()`, `compareTo()`, and arithmetic operations.

3. **Inheritance Refactor:** Take a deep inheritance hierarchy (4+ levels) and refactor to use composition and interfaces. Write tests to verify behavior is preserved.

4. **Exception Handling Audit:** Review exception handling in a codebase. Replace all `catch (Exception e)` with specific exceptions. Add logging to all empty catch blocks.

5. **Thread Safety Review:** Identify all mutable shared state in a concurrent application. Refactor to use immutable objects or proper synchronization.

## 25. Mini Project

### OOP Quality Analyzer

Build a tool that scans Java source files and detects common OOP mistakes:

**Requirements:**
- Detect `==` comparison on `String`, `Integer`, and other object types
- Detect `equals()` without `hashCode()` override
- Detect public fields in non-record classes
- Detect `catch (Exception e)` with empty body
- Detect `finalize()` method usage
- Detect deep inheritance hierarchies (4+ levels)
- Output a report with line numbers, mistake type, and suggested fix

**Deliverables:**
- Java implementation using regex/AST parsing
- Unit tests for each detection rule
- Sample test files with intentional mistakes
- Generated report showing detected issues

## 26. Summary

- **Always use `.equals()` for value comparison** — `==` is for reference identity only
- **Override `hashCode()` with `equals()`** — Hash-based collections depend on this
- **Use immutable objects** — Thread-safe, easier to reason about
- **Favor composition over inheritance** — Avoid fragile base class
- **Return `Optional<T>` instead of `null`** — Explicit absence handling
- **Catch specific exceptions** — Don't mask bugs
- **Use `final` fields** — Immutability and clarity
- **Avoid mutable static state** — Thread safety and memory leaks
- **Use `try-with-resources`** — Guaranteed cleanup
- **Use `record` for data carriers** — Auto-generated `equals`, `hashCode`, `toString`

## 27. References

- *Effective Java* by Joshua Bloch — Items 10-17, 21, 54
- *Clean Code* by Robert C. Martin — Chapter 17: Smells and Heuristics
- *Java Concurrency in Practice* by Brian Goetz — Chapter 3: Visibility, Chapter 4: Liveness
- *Design Patterns* by GoF — Chapter 4: Structural Patterns
- Java SE Documentation — Records — https://docs.oracle.com/en/java/javase/21/language/records.html
- Joshua Bloch, "How to Write an Equality Method in Java"

---

*Last updated: August 2026*
