# Pattern Matching References

## Official Documentation

- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 427: Pattern Matching for switch (Third Preview)](https://openjdk.org/jeps/427)
- [JEP 406: Pattern Matching for switch (Second Preview)](https://openjdk.org/jeps/406)
- [JEP 394: Pattern Matching for switch](https://openjdk.org/jeps/394)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Type Pattern | Matches by type and binds a variable |
| Guarded Pattern | Type pattern with additional condition |
| Null Pattern | Matches null specifically |
| Pattern Variable | A variable introduced by a pattern |
| Exhaustiveness | Compiler verifies all cases are handled |

## Code Examples

### Basic Pattern Matching
```java
Object obj = "Hello";
String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String: " + s;
    case null -> "null";
    default -> "Unknown";
};
```

### Guarded Pattern
```java
case String s && s.length() > 5 -> "Long string: " + s;
```

### Multiple Patterns
```java
case 1, 2, 3 -> "Small number";
case 4, 5, 6 -> "Medium number";
```

### Nested Patterns
```java
case Point(int x, int y) && x > 0 && y > 0 -> "Positive point";
```

## Common Patterns

1. **Type checking with binding:** `case String s -> process(s);`
2. **Complex conditions:** `case List<?> list && !list.isEmpty() -> ...`
3. **Null safety:** `case null -> handleNull(); default -> handleNonNull();`
4. **Multiple values:** `case 1, 2, 3 -> "Low";`
