# C++ Fundamentals

## What it is
The building blocks of C++ programming: variables, control structures, functions, and basic I/O.

## Why it exists
To provide the essential tools for writing any C++ program, from simple scripts to complex systems.

## When to use it
Every C++ program uses these fundamentals. They are the foundation for all advanced topics.

## How it works

### Variables and Data Types
```cpp
int age = 25;
double salary = 50000.50;
char grade = 'A';
bool isActive = true;
std::string name = "John";
```

### Control Structures
```cpp
// If-else
if (condition) {
    // code
} else {
    // code
}

// Loops
for (int i = 0; i < 10; i++) {
    // code
}

while (condition) {
    // code
}
```

### Functions
```cpp
int add(int a, int b) {
    return a + b;
}

// Function overloading
double add(double a, double b) {
    return a + b;
}
```

### Pointers and References
```cpp
int x = 10;
int* ptr = &x;  // Pointer
int& ref = x;   // Reference
```

## Production Checklist
- [ ] Use meaningful variable names
- [ ] Initialize variables at declaration
- [ ] Use `const` for constants
- [ ] Prefer references over pointers when possible
- [ ] Use `nullptr` instead of `NULL`
- [ ] Always check array bounds

## Maturity Levels
- **Beginner**: Basic variables, if-else, loops
- **Intermediate**: Functions, pointers, references
- **Advanced**: Templates, lambdas, move semantics

## Common Myths
- ❌ "C++ is too complex for beginners"
- ❌ "Pointers are always dangerous"
- ❌ "You need to use raw pointers everywhere"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Variables | Named storage for data |
| Control Flow | if/else, loops, switch |
| Functions | Reusable code blocks |
| Pointers | Memory addresses |
| References | Aliases for variables |

## Related Topics
- [OOP](../02-oop/)
- [Memory Management](../05-memory-management/)
- [Modern C++](../08-modern-cpp/)