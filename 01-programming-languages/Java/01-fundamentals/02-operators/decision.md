# When to Use Operators

## Decision Guide

### Choosing the Right Operator Type

| Operator | Use When | Example |
|----------|----------|---------|
| Arithmetic (+, -, *, /, %) | Mathematical calculations | `int area = width * height;` |
| Relational (==, !=, <, >, <=, >=) | Comparing values | `if (age >= 18)` |
| Logical (&&, \|\|, !) | Combining boolean conditions | `if (age >= 18 && hasId)` |
| Bitwise (&, \|, ^, ~) | Low-level bit manipulation | `int flags = flag \| MASK;` |
| Assignment (=, +=, -=, etc.) | Storing and updating values | `count += 1;` |
| Ternary (?:) | Simple conditional assignment | `String s = x > 0 ? "pos" : "neg";` |
| Instanceof | Type checking before casting | `if (obj instanceof String)` |

### When to Use Each Arithmetic Operator

| Operation | Use | Avoid |
|-----------|-----|-------|
| Addition (+) | Numeric sum, string concatenation | Overloading for custom logic |
| Subtraction (-) | Difference, negation | Chaining multiple minus signs |
| Multiplication (*) | Scaling, area calculations | When overflow is possible (use long) |
| Division (/) | Ratios, averages | Integer division when you need decimals |
| Modulus (%) | Remainder, parity, cycling | Division by zero (throws ArithmeticException) |

### Logical Operator Short-Circuiting

| Expression | Behavior |
|------------|----------|
| `A && B` | If A is false, B is NOT evaluated |
| `A \|\| B` | If A is true, B is NOT evaluated |
| `A & B` | Both A and B are ALWAYS evaluated |
| `A \| B` | Both A and B are ALWAYS evaluated |

Use `&&` and `||` (short-circuit) unless you specifically need both sides evaluated (e.g., when both sides have side effects).

### Bitwise Operations Decision Tree

| Task | Operator | Example |
|------|----------|---------|
| Set a bit | OR with mask | `flags \|= BIT_MASK;` |
| Clear a bit | AND with inverted mask | `flags &= ~BIT_MASK;` |
| Toggle a bit | XOR with mask | `flags ^= BIT_MASK;` |
| Check a bit | AND with mask | `boolean isSet = (flags & BIT_MASK) != 0;` |
| Check if power of 2 | AND with n-1 | `(n & (n - 1)) == 0` |

## Production Guidelines

### Avoiding Integer Overflow
```java
// DANGEROUS: Can overflow silently
int result = Integer.MAX_VALUE + 1; // -2147483648 (wraps)

// SAFE: Check before operation
if (a > Integer.MAX_VALUE - b) {
    throw new ArithmeticException("Integer overflow");
}
int result = a + b;
```

### Floating-Point Comparisons
```java
// WRONG: Direct comparison
if (x == 0.3) { ... }

// CORRECT: Use epsilon comparison
double epsilon = 1e-9;
if (Math.abs(x - 0.3) < epsilon) { ... }
```

### Null-Safe Comparisons
```java
// DANGEROUS: NPE if str is null
if (str.equals("target")) { ... }

// SAFE: Null-safe comparison
if ("target".equals(str)) { ... }

// BETTER: Use Objects.equals()
if (Objects.equals(str, "target")) { ... }
```
