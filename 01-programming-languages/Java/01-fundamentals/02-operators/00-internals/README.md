# Operators Internals

## How Java Evaluates Operators

### Operator Precedence

Java evaluates expressions based on operator precedence. Higher precedence operators are evaluated first:

```
Postfix:      expr++ expr--
Unary:        ++expr --expr +expr -expr ~ !
Multiplicative: * / %
Additive:     + -
Shift:        << >> >>>
Relational:   < > <= >= instanceof
Equality:     == !=
Bitwise AND:  &
Bitwise XOR:  ^
Bitwise OR:   |
Logical AND:  &&
Logical OR:   ||
Ternary:      ? :
Assignment:   = += -= *= /= etc.
```

### Short-Circuit Evaluation

Logical operators `&&` and `||` use short-circuit evaluation:

```java
// && (AND): If left side is false, right side is NOT evaluated
if (x != null && x.getValue() > 0) {
    // x.getValue() only runs if x != null
}

// || (OR): If left side is true, right side is NOT evaluated
if (x == null || x.isEmpty()) {
    // x.isEmpty() only runs if x == null is false
}
```

This is critical for null safety and performance.

### Integer Overflow Internals

Java integers use two's complement representation and wrap around silently:

```java
int max = Integer.MAX_VALUE;  // 2,147,483,647
int overflow = max + 1;       // -2,147,483,648 (wraps to MIN_VALUE)

// Binary representation:
// MAX_VALUE: 0111...1111
// MAX_VALUE + 1: 1000...0000 (this is MIN_VALUE in two's complement)
```

### Floating-Point Arithmetic

IEEE 754 representation causes precision issues:

```java
double a = 0.1;
double b = 0.2;
double sum = a + b;  // 0.30000000000000004

// Why? 0.1 in binary is a repeating fraction:
// 0.1 = 0.0001100110011001100110011... (repeating)
// The closest double representation has a small error
```

### Bitwise Operation Performance

Bitwise operations are single-cycle CPU instructions:

```java
// These are extremely fast:
int result = a & b;    // 1 cycle
int result = a | b;    // 1 cycle
int result = a ^ b;    // 1 cycle
int result = a << 2;   // 1 cycle
```

### Switch Statement Internals

Java optimizes switch statements based on the value type:

- **Small ranges (int, char):** Jump table (O(1) lookup)
- **Large ranges or String:** Binary search or hash-based dispatch
- **Enum:** Direct ordinal mapping (fastest)
