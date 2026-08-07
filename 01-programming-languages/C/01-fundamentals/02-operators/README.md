# Operators — C Language

## What it is
Operators are symbols that perform operations on operands (values and variables).

## Why it exists
To manipulate data and make decisions in programs.

## When to use it
Whenever you need to perform calculations, comparisons, or logical operations.

## How it works

### Arithmetic Operators

```c
int a = 10, b = 3;
int sum = a + b;       // 13
int diff = a - b;     // 7
int product = a * b;  // 30
int quotient = a / b; // 3
int remainder = a % b; // 1
```

### Relational Operators

```c
int x = 5, y = 10;
x == y   // false
x != y   // true
x < y    // true
x > y    // false
x <= y   // true
x >= y   // false
```

### Logical Operators

```c
int a = 1, b = 0;
a && b   // false (AND)
a || b   // true (OR)
!a       // false (NOT)
```

### Bitwise Operators

```c
int x = 5, y = 3;
x & y    // 1 (AND)
x | y    // 7 (OR)
x ^ y    // 6 (XOR)
~x       // -6 (NOT)
x << 1   // 10 (left shift)
x >> 1   // 2 (right shift)
```

### Assignment Operators

```c
int x = 10;
x += 5;   // x = x + 5
x -= 3;   // x = x - 3
x *= 2;   // x = x * 2
x /= 4;   // x = x / 4
x %= 3;   // x = x % 3
```

### Operator Precedence

| Priority | Operators |
|----------|-----------|
| 1 | () [] -> . |
| 2 | ! ~ ++ -- + - * & (type) sizeof |
| 3 | * / % |
| 4 | + - |
| 5 | << >> |
| 6 | < <= > >= |
| 7 | == != |
| 8-13 | & ^ | && || ?: = += -= etc. |

## Production Checklist

- [ ] Use parentheses for clarity
- [ ] Avoid side effects in complex expressions
- [ ] Be careful with integer division
- [ ] Check for division by zero
- [ ] Use appropriate operator precedence

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows arithmetic and relational |
| Intermediate | Uses logical and bitwise |
| Advanced | Masters precedence and side effects |

## Common Myths

1. **Myth**: && and & are the same
   **Truth**: && is logical AND, & is bitwise AND

2. **Myth**: ++x and x++ are always the same
   **Truth**: ++x increments then returns, x++ returns then increments

## One-Minute Revision

| Type | Examples |
|------|----------|
| Arithmetic | + - * / % |
| Relational | == != < > <= >= |
| Logical | && \|\| ! |
| Bitwise | & \| ^ ~ << >> |
| Assignment | = += -= *= /= %= |

## Related Topics

- [Variables](../01-variables/README.md)
- [Control Flow](../03-control-flow/README.md)
