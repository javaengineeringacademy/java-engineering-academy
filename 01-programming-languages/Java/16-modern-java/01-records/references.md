# Records References

## Official Documentation

- [JEP 395: Records and Sealed Classes](https://openjdk.org/jeps/395)
- [JEP 359: Records (Preview)](https://openjdk.org/jeps/359)
- [Java Language Specification - Records](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.10)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Record Component | A field declared in the record header |
| Canonical Constructor | Constructor with all components as parameters |
| Compact Constructor | Validation-only constructor without assignments |
| Accessor Method | Method named after component (not getX()) |

## Code Examples

### Basic Record
```java
public record Person(String name, int age) {}
```

### Record with Validation
```java
public record Email(String address) {
    public Email {
        if (!address.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
```

### Record Implementing Interface
```java
public record Money(double amount, String currency) 
    implements Comparable<Money> {
    
    @Override
    public int compareTo(Money other) {
        return Double.compare(this.amount, other.amount);
    }
}
```

## Common Patterns

1. **Value Objects:** `record Money(double amount, String currency) {}`
2. **DTOs:** `record UserDTO(String name, String email) {}`
3. **Events:** `record OrderCreated(String orderId, Instant timestamp) {}`
4. **Coordinates:** `record Point(int x, int y) {}`
