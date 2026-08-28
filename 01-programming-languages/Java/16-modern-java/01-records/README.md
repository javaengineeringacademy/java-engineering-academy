# Records (Java 14+)

Records provide a compact syntax for declaring immutable data carriers. They automatically generate equals(), hashCode(), toString(), and accessor methods.

## Key Features

- **Immutable by design** - All fields are final
- **Compact syntax** - Components declared in header
- **Auto-generated methods** - equals(), hashCode(), toString()
- **Accessor methods** - Generated for each component (not getters)

## Syntax

```java
public record Point(int x, int y) {}

// With compact constructor for validation
public record PositiveNumber(int value) {
    public PositiveNumber {
        if (value < 0) throw new IllegalArgumentException("Must be positive");
    }
}
```

## What's Generated

| Method | Description |
|--------|-------------|
| `accessor()` | Returns component value (not getXxx()) |
| `equals(Object)` | Structural equality comparison |
| `hashCode()` | Based on component values |
| `toString()` | "RecordName[field1=value1, ...]" |

## Limitations

- Cannot extend classes (implicitly extends java.lang.Record)
- Can implement interfaces
- Cannot have instance fields beyond components
- Cannot be abstract
- All components must be declared in header

## When to Use

- DTOs and transfer objects
- Value objects (money, coordinates)
- Cases where immutability is required
- When you need structural equality
