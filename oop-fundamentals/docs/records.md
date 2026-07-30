# Records (Java 16+)

## What are Records?
Immutable data carriers with automatic implementations:
```java
public record Person(String name, int age, String email) { }

// Equivalent to:
public final class Person {
    private final String name;
    private final int age;
    private final String email;

    public Person(String name, int age, String email) { ... }
    public String name() { return name; }
    public int age() { return age; }
    public String email() { return email; }
    // equals, hashCode, toString auto-generated
}
```

## Compact Constructors (Validation)
```java
public record Person(String name, int age) {
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
    }
}
```

## When to Use
- DTOs, value objects, data carriers
- Immutable data carriers
- Not for mutable entities with behavior