# Switch Expressions References

## Official Documentation

- [JEP 361: Switch Expressions](https://openjdk.org/jeps/361)
- [JEP 325: Switch Expressions (Preview)](https://openjdk.org/jeps/325)
- [Java Language Specification - Switch Expressions](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.28)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Arrow Case | `case X ->` - No fall-through |
| Colon Case | `case X:` - With fall-through |
| Yield | Returns value from colon case |
| Exhaustive | All cases must be handled |

## Code Examples

### Arrow Syntax
```java
String result = switch (day) {
    case "MON" -> "Monday";
    case "TUE" -> "Tuesday";
    case "WED", "THU", "FRI" -> "Weekday";
    case "SAT", "SUN" -> "Weekend";
    default -> throw new IllegalArgumentException("Invalid day");
};
```

### Colon Syntax with Yield
```java
String result = switch (day) {
    case "MON":
        yield "Monday";
    case "TUE":
        yield "Tuesday";
    default:
        yield "Unknown";
};
```

### Complex Logic with Yield
```java
String result = switch (input) {
    case "hello" -> "Hello!";
    case "bye" -> "Goodbye!";
    default -> {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            yield "Empty input";
        } else {
            yield "Input: " + trimmed;
        }
    }
};
```

## Common Patterns

1. **Simple mapping:** `case "A" -> "Alpha";`
2. **Multiple values:** `case 1, 2, 3 -> "Low";`
3. **Complex logic:** Use yield with code block
4. **Enum switch:** Often exhaustive without default
