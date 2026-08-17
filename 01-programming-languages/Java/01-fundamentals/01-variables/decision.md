# When to Use Variables & Data Types

## Decision Guide

### Choosing the Right Primitive Type

| Data Type | Use When | Avoid When |
|-----------|----------|------------|
| `byte` | Memory-critical arrays, network protocols, file I/O | General-purpose integer math |
| `short` | Large arrays where memory matters, legacy systems | When `int` works fine |
| `int` | Most integer operations (default choice) | Numbers exceeding ±2 billion |
| `long` | Timestamps, large IDs, astronomical calculations | Simple counters (use `int`) |
| `float` | Graphics, memory-critical scientific computing | Financial calculations |
| `double` | Most decimal operations (default choice) | When exact decimal precision required |
| `boolean` | Flags, conditions, state | Bit manipulation (use `byte`) |
| `char` | Single characters, ASCII operations | Text processing (use `String`) |

### When to Use Each Reference Type

| Type | Use When | Avoid When |
|------|----------|------------|
| `String` | Text data, immutability needed | Mutable text buffers (use `StringBuilder`) |
| Arrays | Fixed-size homogeneous collections | Dynamic resizing needed (use `List`) |
| Custom Objects | Modeling real-world entities | Simple data grouping (use records) |

### Type Casting Decisions

| Scenario | Recommended Approach |
|----------|---------------------|
| `int` to `long` | Implicit widening (automatic) |
| `double` to `int` | Explicit narrowing with cast |
| `String` to `int` | Use `Integer.parseInt()` |
| `int` to `String` | Use `String.valueOf()` or `Integer.toString()` |
| `Object` to specific type | Use `instanceof` check before casting |

## Production Guidelines

### Financial Calculations
```java
// WRONG: Loss of precision
double price = 0.1 + 0.2; // 0.30000000000000004

// CORRECT: Exact decimal arithmetic
BigDecimal price = new BigDecimal("0.1").add(new BigDecimal("0.2"));
```

### Memory Optimization
```java
// Use byte for large arrays of small numbers
byte[] pixelData = new byte[1000000]; // 1MB vs 4MB with int[]

// Use int for most business logic
int employeeCount = 250;
```

### Null Safety
```java
// Use wrapper classes when null is meaningful
Integer userId = null; // User not found

// Use primitives when null is not applicable
int count = 0; // Always has a value
```
