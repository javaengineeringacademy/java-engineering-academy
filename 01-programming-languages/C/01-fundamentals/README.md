# Fundamentals — C Language

## What it is
The building blocks of C programming: variables, operators, control flow, functions, arrays, strings, pointers, and memory basics.

## Why it exists
Every C program relies on these concepts. Mastering them is essential before moving to advanced topics.

## When to use it
Always. These are the foundation of every C program you will ever write.

## How it works

### Variables
Named storage locations with specific types.

```c
int age = 25;
float salary = 50000.50;
char grade = 'A';
```

### Operators
Symbols that perform operations on values.

```c
int sum = a + b;
int product = a * b;
int mod = a % b;
```

### Control Flow
Decision making and loops.

```c
if (condition) {
    // do something
} else {
    // do something else
}

for (int i = 0; i < 10; i++) {
    // loop body
}

while (condition) {
    // loop body
}
```

### Functions
Reusable blocks of code.

```c
int add(int a, int b) {
    return a + b;
}
```

### Arrays
Fixed-size collections of same-type elements.

```c
int numbers[5] = {1, 2, 3, 4, 5};
```

### Strings
Character arrays terminated by null character.

```c
char name[] = "Hello";
```

### Pointers
Variables that store memory addresses.

```c
int x = 10;
int *ptr = &x;
```

### Memory Basics
Stack vs heap allocation.

## Production Checklist

- [ ] All variables initialized before use
- [ ] Array bounds checked
- [ ] Null pointers checked before dereferencing
- [ ] Return values checked
- [ ] Compiler warnings enabled (-Wall -Wextra)

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Can write simple programs with variables and loops |
| Intermediate | Uses functions, arrays, and basic pointers |
| Advanced | Manages memory, understands pointer arithmetic |

## Common Myths

1. **Myth**: C is outdated
   **Truth**: C powers operating systems, embedded systems, and performance-critical applications

2. **Myth**: Pointers are dangerous
   **Truth**: Pointers are powerful when used correctly

3. **Myth**: C has no string type
   **Truth**: C uses char arrays, which are flexible and efficient

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Variables | Named storage with types |
| Operators | Symbols for operations |
| Control Flow | if/else, for, while, switch |
| Functions | Reusable code blocks |
| Arrays | Fixed-size collections |
| Strings | Null-terminated char arrays |
| Pointers | Memory address holders |
| Memory | Stack (automatic) vs heap (dynamic) |

## Related Topics

- [Structures](../02-structures/README.md)
- [Advanced Pointers](../05-pointers-advanced/README.md)
- [Memory Management](../08-memory-management/README.md)
