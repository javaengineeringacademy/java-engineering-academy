# Object Class Methods

## Introduction

In Java, every class implicitly or explicitly extends `java.lang.Object`. This makes `Object` the root of the class hierarchy and provides fundamental methods that every Java object inherits. Understanding these methods is essential for writing correct, efficient, and well-behaved Java classes.

The `Object` class defines methods for object comparison (`equals`, `hashCode`), string representation (`toString`), cloning (`clone`), garbage collection (`finalize`), and thread synchronization (`wait`, `notify`, `notifyAll`). Properly overriding these methods is critical for objects to behave correctly in collections, debugging, and concurrent programming.

## Learning Objectives

By the end of this topic, you will be able to:

- Explain the role of `java.lang.Object` as the root of all Java classes
- Properly override `toString()` for meaningful string representation
- Implement `equals()` and `hashCode()` following the contract
- Understand when and how to use `clone()` (and its limitations)
- Recognize why `finalize()` is deprecated and what to use instead
- Apply `getClass()`, `wait()`, `notify()`, and `notifyAll()` appropriately
- Use modern alternatives like records for data classes

## Prerequisites

Before studying Object class methods, you should be familiar with:

- Basic Java class definitions and inheritance
- Understanding of method overriding with `@Override`
- Familiarity with the `instanceof` operator
- Basic knowledge of collections (HashMap, HashSet)
- Understanding of threads (for `wait`/`notify`)

## Why This Concept Exists

Without properly overriding Object methods, classes don't behave correctly in common Java scenarios:

1. **Debugging**: Without `toString()`, you get unhelpful output like `Person@1a2b3c`
2. **Collections**: Without proper `equals()`/`hashCode()`, objects can't be used correctly in `HashMap` or `HashSet`
3. **Cloning**: Without understanding `clone()`, you may create unintended shallow copies
4. **Garbage collection**: Without understanding `finalize()`, you may misuse it for cleanup
5. **Concurrency**: Without `wait()`/`notify()`, implementing thread-safe objects is difficult

## Problem Statement

Consider this common scenario:

```java
class Person {
    String name;
    int age;
}

List<Person> people = new ArrayList<>();
people.add(new Person("Alice", 30));

// This returns false! Why?
people.contains(new Person("Alice", 30));  // false
```

The `contains` method uses `equals()` to compare objects. Without overriding `equals()`, it uses reference equality (`==`), so two `Person` objects with the same data are considered different. How do we fix this?

## Theory

### The Object Contract

The `Object` class defines methods that every Java object inherits:

| Method | Purpose | Overridable |
|--------|---------|-------------|
| `toString()` | String representation | Yes |
| `equals(Object)` | Logical equality | Yes |
| `hashCode()` | Hash value for collections | Yes |
| `clone()` | Object copying | Yes (with limitations) |
| `finalize()` | Pre-garbage collection cleanup | Deprecated |
| `getClass()` | Runtime class information | No (final) |
| `wait()` | Thread waiting | Yes |
| `notify()` | Thread notification | Yes |
| `notifyAll()` | Wake all waiting threads | Yes |

### equals() and hashCode() Contract

If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` must be true. This contract is essential for correct behavior in hash-based collections like `HashMap`, `HashSet`, and `Hashtable`.

## Key Methods to Override

### toString()
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### equals(Object obj) & hashCode()
**Contract:** If `a.equals(b)` then `a.hashCode() == b.hashCode()`

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Person person = (Person) obj;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### clone()
```java
@Override
protected Person clone() throws CloneNotSupportedException {
    return (Person) super.clone();  // Shallow copy
}
```

## Other Methods

### finalize() (Deprecated)
```java
@Deprecated(since = "9", forRemoval = true)
@Override
protected void finalize() throws Throwable {
    try {
        // Cleanup
    } finally {
        super.finalize();
    }
}
```

**Use try-with-resources or Cleaner instead.**

### getClass()
Returns runtime class of object.

### notify(), notifyAll(), wait()
Thread synchronization methods.

## Internal Working

When you call `equals()`, `hashCode()`, or `toString()` on an object, the JVM performs dynamic method dispatch:

1. **Method Lookup**: The JVM looks up the method in the object's actual class (not the reference type)
2. **Virtual Dispatch**: If the method is overridden, the overridden version is called
3. **Inheritance Chain**: If not overridden in the class, the JVM traverses the inheritance chain until it finds an implementation
4. **Object Class**: The default implementations in `Object` are used if no override exists

For `equals()` and `hashCode()`, the JVM uses these methods when objects are stored in hash-based collections:

- `HashMap` calls `hashCode()` to determine the bucket, then `equals()` to find the exact key
- `HashSet` uses `hashCode()` for bucketing and `equals()` to detect duplicates
- Incorrect implementations lead to silent data loss and bugs

## JVM Perspective

1. **Method Dispatch**: `invokevirtual` bytecode instruction is used for Object methods
2. **Method Table**: Each class has a vtable pointing to the correct method implementation
3. **`getClass()` is final**: Cannot be overridden—returns the runtime `Class` object
4. **`wait()`/`notify()` are native**: Implemented in C, interact with the JVM's thread scheduler
5. **`finalize()` is called by the GC**: The garbage collector calls `finalize()` before reclaiming memory (deprecated since Java 9)
6. **`clone()` uses native methods**: The `Object.clone()` method is a native method that performs bitwise copying of fields

## Memory Representation

```java
Person p = new Person("Alice", 30);
```

```
Stack Frame          Heap Memory
┌──────────┐        ┌─────────────────┐
│ p        │───────►│ Person Object    │
│ ref      │        │ ┌─────────────┐ │
└──────────┘        │ │ name (ref)  │──────► "Alice"
                    │ │ age: 30     │ │
                    │ └─────────────┘ │
                    │                 │
                    │ Object methods: │
                    │ - toString()    │
                    │ - equals()      │
                    │ - hashCode()    │
                    │ - clone()       │
                    └─────────────────┘
```

- Every object inherits Object methods
- The JVM's vtable points to the correct implementation (overridden or default)
- `hashCode()` values are cached or computed on demand depending on implementation

## Syntax

### toString()

```java
@Override
public String toString() {
    return "ClassName{field1='%s', field2=%d}".formatted(field1, field2);
}
```

### equals() and hashCode()

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassName that = (ClassName) o;
    return Objects.equals(field1, that.field1)
        && Objects.equals(field2, that.field2);
}

@Override
public int hashCode() {
    return Objects.hash(field1, field2);
}
```

### clone()

```java
@Override
protected ClassName clone() throws CloneNotSupportedException {
    return (ClassName) super.clone();
}
```

### Record Alternative (Java 16+)

```java
public record Person(String name, int age) {
    // equals, hashCode, toString auto-generated
}
```

## Easy Example

```java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point(%d, %d)".formatted(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point other)) return false;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
```

## Medium Example

```java
public class Student {
    private final String id;
    private final String name;
    private final LocalDate enrollmentDate;

    public Student(String id, String name, LocalDate enrollmentDate) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.enrollmentDate = Objects.requireNonNull(enrollmentDate);
    }

    @Override
    public String toString() {
        return "Student{id='%s', name='%s', enrollment=%s}".formatted(
            id, name, enrollmentDate
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Accessors
    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
}
```

## Hard Example

```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(currency.getCurrencyCode(), amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money other)) return false;
        return amount.compareTo(other.amount) == 0
            && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Currency mismatch: %s vs %s".formatted(currency, other.currency)
            );
        }
    }
}
```

## Enterprise Example

```java
public final class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String value) {
        return new UserId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "UserId{%s}".formatted(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

public final class Email {
    private final String address;

    public Email(String address) {
        Objects.requireNonNull(address);
        if (!address.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email: " + address);
        }
        this.address = address.toLowerCase();
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return address.equals(other.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
```

## Performance

| Method | Performance Impact |
|--------|-------------------|
| `toString()` | Minimal—called on demand |
| `equals()` | O(n) based on field count |
| `hashCode()` | Minimal—cached in some implementations |
| `clone()` | Native—fast bitwise copy |
| `wait()`/`notify()` | Kernel context switch—expensive |

**Optimization tips:**

- Cache `hashCode()` if computation is expensive
- Use `Objects.hash()` for concise and efficient hash computation
- Avoid `toString()` in performance-critical loops
- Use records (Java 16+) for automatic, optimized implementations
- Avoid `finalize()` entirely—it has unpredictable timing and performance overhead

## Best Practices

1. **Always override both** `equals` and `hashCode`
2. **Use `Objects.hash()`** for hashCode
3. **Use `Objects.equals()`** for null-safe comparison
4. **Make `toString()` informative**
5. **Avoid `finalize()`** - use try-with-resources/Cleaner
6. **Prefer records for data classes**: Records auto-generate `equals`, `hashCode`, and `toString`
7. **Use pattern matching for equals**: `instanceof` with pattern matching simplifies type checks
8. **Document the equals contract**: If your class has non-obvious equality semantics, document them
9. **Consider immutability**: Immutable objects are safer to use in `equals` and `hashCode`

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Overriding `equals` but not `hashCode` | Always override both together |
| Using `getClass()` instead of `instanceof` in `equals` | Consider using `instanceof` for subclass compatibility |
| Mutating fields used in `hashCode` | Use immutable fields for hash computation |
| Calling `finalize()` for cleanup | Use try-with-resources or `Cleaner` |
| Using `clone()` on complex objects | Consider copy constructors or builders instead |

## Pitfalls

1. **equals/hashCode contract violation**: If `equals` returns true but `hashCode` differs, objects won't be found in hash-based collections
2. **Mutable hashCode fields**: If fields used in `hashCode` change after insertion into a `HashSet`, the object becomes unfindable
3. **finalize() timing**: `finalize()` is called unpredictably—if at all—before garbage collection
4. **clone() shallow copy**: `clone()` creates shallow copies—mutable reference fields are shared between original and clone
5. **wait/notify misuse**: Using `wait()`/`notify()` without holding the object's monitor causes `IllegalMonitorStateException`
6. **Thread-unsafe equals**: `equals()` implementations that access mutable shared state without synchronization can produce incorrect results

## Debugging Tips

1. **Override toString()**: Always override `toString()` to get meaningful debug output
2. **Use IDE debugging**: IDEs display `toString()` output in debugger variable views
3. **Test equals/hashCode**: Use `EqualsVerifier` library to verify correct implementations
4. **Check collection behavior**: When objects aren't found in collections, verify `equals`/`hashCode` implementation
5. **Use jshell**: Test Object methods interactively
6. **Inspect with javap**: Use `javap -p` to see which methods are inherited from `Object`

## Comparison Table

| Method | Default Behavior | When to Override |
|--------|-----------------|------------------|
| `toString()` | `ClassName@hashcode` | Always—for debugging |
| `equals()` | Reference equality (`==`) | When logical equality differs from reference equality |
| `hashCode()` | Identity hash code | When overriding `equals` |
| `clone()` | Shallow copy | When copying is needed (consider alternatives) |
| `finalize()` | Empty body | Never (deprecated) |
| `getClass()` | Runtime class | Never (final) |
| `wait()` | Thread waiting | When implementing thread coordination |
| `notify()` | Wake one thread | When implementing thread coordination |

## Decision Tree

```
Should you override this Object method?

toString() ──► YES, always—for debugging
       │
equals()/hashCode() ──► Does logical equality differ from reference equality?
       │                          │
       YES                       NO
       │                          │
  Override both              Don't override
       │
clone() ──► Do you need object copying?
       │              │
       YES           NO
       │              │
  Consider copy     Don't override
  constructor instead
       │
finalize() ──► NEVER—use try-with-resources or Cleaner
       │
wait()/notify() ──► Are you implementing thread coordination?
       │              │
       YES           NO
       │              │
  Use proper       Don't override
  synchronization
```

## Interview Questions

1. **Why override toString()?** For meaningful debug output and logging
2. **equals/hashCode contract?** If `a.equals(b)` then `a.hashCode() == b.hashCode()` must hold
3. **Why avoid finalize()?** Unpredictable timing, performance overhead, can keep objects alive longer
4. **clone() vs copy constructor?** Copy constructor is more flexible, avoids `CloneNotSupportedException`
5. **Can you override getClass()?** No—it's final in `Object`
6. **How does HashMap use hashCode/equals?** `hashCode()` determines the bucket; `equals()` finds the exact key
7. **What is a record?** Java 16+ feature that auto-generates `equals`, `hashCode`, `toString`, and accessors
8. **Why use Objects.hash()?** Concise, null-safe, and efficient hash computation

## Exercises

1. Create a `FullName` class with `firstName` and `lastName` fields. Override `toString()`, `equals()`, and `hashCode()`. Test it in a `HashSet` to verify correct behavior.
2. Implement a `Color` class with red, green, blue components (0-255). Override all relevant Object methods and ensure `equals()` validates component ranges.
3. Create a `Temperature` class that overrides `equals()` to consider temperatures equal if they differ by less than 0.1 degrees.
4. Write unit tests for a class's `equals()` implementation covering reflexivity, symmetry, transitivity, consistency, and null cases.

## Assignments

1. **Value Object**: Create an immutable `Address` class with `equals()`, `hashCode()`, and `toString()` using records. Then create a version without records and compare the boilerplate.
2. **Collection-Ready Class**: Design a `Product` class that works correctly in `HashMap`, `HashSet`, and `TreeSet`. Ensure `hashCode()` is efficient and `equals()` uses the product ID.
3. **Defensive equals**: Implement `equals()` for a `User` class where equality depends on the user ID only, not on mutable fields like name or email.

## Mini Project

**Object Methods Utility Library**

Build a utility library demonstrating Object method best practices:

- `EqualsVerifier`: A utility class that tests whether a class correctly implements `equals()` and `hashCode()`
- `ToStringFormatter`: A utility that generates `toString()` implementations using reflection
- `HashCodeBuilder`: A utility that computes hash codes from fields with proper null handling
- `CloneHelper`: A utility that performs deep cloning using serialization

Requirements:
- Test all edge cases: null, different types, same reference
- Verify the equals/hashCode contract
- Handle mutable and immutable fields correctly
- Provide clear error messages for contract violations

## Summary

The `Object` class is the root of all Java classes and provides fundamental methods that every object inherits. Key takeaways:

- **`toString()`**: Override for meaningful debug output—use `String.formatted()` or records
- **`equals()` and `hashCode()`**: Always override both together; follow the contract for correct collection behavior
- **`clone()`**: Creates shallow copies—prefer copy constructors or records instead
- **`finalize()`**: Deprecated since Java 9—use try-with-resources or `Cleaner` for cleanup
- **`getClass()`**: Final method—returns runtime class information, cannot be overridden
- **`wait()`/`notify()`**: Thread synchronization methods—use with proper synchronization
- **Records** (Java 16+): Auto-generate `equals`, `hashCode`, `toString`, and accessors for data classes
- **Performance**: Cache `hashCode()` for expensive computations; use `Objects.hash()` for concise implementations
- **Best practices**: Override `toString()` always, follow equals/hashCode contract, avoid `finalize()`

## References

- Effective Java, 3rd Edition, Joshua Bloch - Item 10-17 (equals, hashCode, toString, clone, Comparable)
- Java Language Specification - The Class Object (https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)
- Oracle Java Tutorials - Object Class (https://docs.oracle.com/en/java/javase/21/java/javaOO/objectclass.html)
- Head First Java, 3rd Edition - Object class chapters
- Clean Code, Robert Martin - Chapter on Meaningful Names (toString)
