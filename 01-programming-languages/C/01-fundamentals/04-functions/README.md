# Functions — C Language

## What it is
Functions are reusable blocks of code that perform specific tasks.

## Why it exists
To organize code, enable reuse, and improve maintainability.

## When to use it
Whenever you need to perform a task that may be repeated or is complex.

## How it works

### Function Declaration

```c
int add(int a, int b);  // Prototype
```

### Function Definition

```c
int add(int a, int b) {
    return a + b;
}
```

### Function Call

```c
int result = add(5, 3);
```

### Parameters and Return Types

```c
void print_message(const char *msg) {  // No return
    printf("%s\n", msg);
}

double calculate(double x, double y) {  // Returns double
    return x * y;
}
```

### Pass by Value

```c
void swap(int a, int b) {  // Doesn't affect originals
    int temp = a;
    a = b;
    b = temp;
}
```

### Pass by Reference (using pointers)

```c
void swap(int *a, int *b) {  // Affects originals
    int temp = *a;
    *a = *b;
    *b = temp;
}
```

### Recursion

```c
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```

### Static Functions

```c
static void helper(void) {  // File scope only
    // implementation
}
```

## Production Checklist

- [ ] Keep functions short (max 50 lines)
- [ ] Single responsibility principle
- [ ] Use descriptive names
- [ ] Validate parameters
- [ ] Avoid side effects when possible

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Writes simple functions |
| Intermediate | Uses pointers for pass-by-reference |
| Advanced | Masters recursion and function pointers |

## Common Myths

1. **Myth**: Functions always pass by value
   **Truth**: You can simulate pass-by-reference using pointers

2. **Myth**: Recursion is always slower
   **Truth**: Recursion can be optimized by compilers (tail recursion)

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Declaration | Prototype (header) |
| Definition | Implementation |
| Parameters | Input values |
| Return | Output value |
| Pass by Value | Copy of data |
| Pass by Reference | Pointer to data |
| Recursion | Function calls itself |
| Static | File scope only |

## Related Topics

- [Variables](../01-variables/README.md)
- [Pointers](../07-pointers/README.md)
