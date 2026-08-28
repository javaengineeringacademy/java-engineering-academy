# instanceof Pattern Matching References

## Official Documentation

- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [Java Language Specification - instanceof](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.20.2)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Pattern Variable | Variable introduced by a pattern |
| Guarded Pattern | Pattern with additional boolean condition |
| Definite Assignment | When a variable is guaranteed to be assigned |
| Scope | Region where variable is accessible |

## Code Examples

### Basic Pattern Matching
```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### Guarded Pattern
```java
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}
```

### Multiple Conditions
```java
if (obj instanceof String s && !s.isEmpty() && s.length() > 10) {
    System.out.println("Valid string: " + s);
}
```

### Pattern in Conditional
```java
String result = (obj instanceof String s) ? s.toUpperCase() : "Not a string";
```

## Common Patterns

1. **Type check + cast:** `if (obj instanceof String s)`
2. **With guard:** `if (obj instanceof String s && s.length() > 5)`
3. **Multiple guards:** `if (obj instanceof String s && !s.isEmpty() && s.length() > 10)`
4. **In ternary:** `(obj instanceof String s) ? s : default`
