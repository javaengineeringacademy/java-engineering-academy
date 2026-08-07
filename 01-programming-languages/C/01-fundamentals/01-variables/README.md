# Variables — C Language

## What it is
Variables are named storage locations in memory that hold values of a specific type.

## Why it exists
To store and manipulate data during program execution.

## When to use it
Whenever you need to store data that will be used or modified.

## How it works

### Basic Types

```c
int age = 25;              // Integer
float salary = 50000.50;   // Single precision float
double pi = 3.14159265;    // Double precision float
char grade = 'A';          // Single character
```

### Type Modifiers

```c
unsigned int positive = 100;
long long big_number = 9999999999LL;
short small = 10;
```

### Constants

```c
const int MAX = 100;
#define PI 3.14159
```

### Scope and Lifetime

```c
int global;           // Global scope

void func() {
    int local;        // Local scope
    static int s;     // Static lifetime
}
```

## Production Checklist

- [ ] Use appropriate type for data range
- [ ] Initialize all variables at declaration
- [ ] Use const for values that don't change
- [ ] Avoid unused variables
- [ ] Check for integer overflow

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows int, float, char |
| Intermediate | Uses type modifiers and const |
| Advanced | Understands storage classes and lifetime |

## Common Myths

1. **Myth**: int is always 32-bit
   **Truth**: int size is platform-dependent (16-bit on some systems)

2. **Myth**: char is always signed
   **Truth**: char signedness is implementation-defined

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Types | int, float, double, char |
| Modifiers | unsigned, long, short |
| Constants | const, #define |
| Scope | local, global, static |
| Lifetime | automatic, static, dynamic |

## Related Topics

- [Operators](../02-operators/README.md)
- [Pointers](../07-pointers/README.md)
