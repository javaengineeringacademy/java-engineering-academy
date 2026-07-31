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

## References
- [JEP 395: Records](https://openjdk.org/jeps/395)