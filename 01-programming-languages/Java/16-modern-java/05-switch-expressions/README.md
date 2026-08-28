# Switch Expressions (Java 14)

Switch expressions extend the traditional switch statement to be used as an expression, returning a value. They also introduce the arrow (`->`) syntax for concise, fall-through-free cases.

## Key Features

- **Return values** - Switch can be used as an expression
- **Arrow syntax** - No fall-through, no break needed
- **Exhaustive** - All cases must be handled
- **Concise** - Reduces boilerplate code

## Syntax

```java
// Arrow syntax (no fall-through)
String result = switch (day) {
    case "MON" -> "Monday";
    case "TUE" -> "Tuesday";
    case "WED", "THU", "FRI" -> "Weekday";
    case "SAT", "SUN" -> "Weekend";
    default -> throw new IllegalArgumentException("Invalid day");
};

// Colon syntax (with break)
String result = switch (day) {
    case "MON":
        yield "Monday";
    case "TUE":
        yield "Tuesday";
    default:
        yield "Unknown";
};
```

## Arrow vs Colon

| Feature | Arrow (`->`) | Colon (`:`) |
|---------|--------------|-------------|
| Fall-through | No | Yes |
| Break required | No | Yes |
| Yield required | No | Yes |
| Code blocks | Single expression | Multiple statements |

## Rules

1. All cases must be exhaustive (or have default)
2. Arrow cases must be single expressions or throw
3. Colon cases must use `yield` to return values
4. Arrow cases cannot fall through

## When to Use

- When you need to return a value from switch
- When you want concise, readable code
- When fall-through is not needed
