# Records (Java 16+)

## What are Records?
Immutable data carriers with automatic implementations.

## Syntax
```java
public record Person(String name, int age, String email) {
    public Person {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (age < 0) throw new IllegalArgumentException();
    }
}
```

## Auto-Generated
- Constructor
- Accessors: `name()`, `age()`, `email()`
- `equals()`, `hashCode()`, `toString()`
- `record` is `final`

## Compact Constructor (Validation)
```java
public record Person(String name, int age) {
    public Person {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
    }
}
```

## Custom Methods
```java
public record Person(String name, int age) {
    public boolean isAdult() { return age >= 18; }
}
```

## Serialization
```java
record Person(String name, int age) implements Serializable {
    // Custom serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
```

## Pattern Matching (Java 17+)
```java
if (obj instanceof Person p) {
    System.out.println(p.name());
}
```

## When to Use
- DTOs, value objects, data carriers
- Immutable data carriers
- Not for mutable entities with behavior

## Best Practices

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

## References

- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JLS §8.10 — Record Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.10)
- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [Baeldung — Java Records](https://www.baeldung.com/java-records)
- [Oracle — Records Tutorial](https://docs.oracle.com/en/java/javase/21/language/records.html)