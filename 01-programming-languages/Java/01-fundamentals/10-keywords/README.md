# Java Keywords Reference

## Overview

Java has 67 reserved keywords that cannot be used as identifiers (variable names, method names, class names). They fall into categories based on their purpose.

## Complete List by Category

### 1. Data Types (8)

| Keyword | Type | Size | Range |
|---------|------|------|-------|
| `byte` | Integer | 1 byte | -128 to 127 |
| `short` | Integer | 2 bytes | -32,768 to 32,767 |
| `int` | Integer | 4 bytes | -2^31 to 2^31-1 |
| `long` | Integer | 8 bytes | -2^63 to 2^63-1 |
| `float` | Floating | 4 bytes | ±3.4e38 |
| `double` | Floating | 8 bytes | ±1.7e308 |
| `char` | Character | 2 bytes | 0 to 65,535 |
| `boolean` | Logical | 1 bit | true/false |

### 2. Control Flow (11)

| Keyword | Purpose |
|---------|---------|
| `if` | Conditional branch |
| `else` | Alternative branch |
| `switch` | Multi-way branch |
| `case` | Switch branch label |
| `default` | Default switch branch |
| `for` | Loop (counted) |
| `while` | Loop (condition-first) |
| `do` | Loop (condition-last) |
| `break` | Exit loop/switch |
| `continue` | Skip to next iteration |
| `return` | Return from method |

### 3. Access Modifiers (4)

| Keyword | Visibility |
|---------|------------|
| `public` | Everywhere |
| `protected` | Package + subclasses |
| `private` | Same class only |
| *(default)* | Package only (no keyword) |

### 4. Object-Oriented (9)

| Keyword | Purpose |
|---------|---------|
| `class` | Define a class |
| `interface` | Define an interface |
| `enum` | Define an enumeration |
| `extends` | Inherit from class |
| `implements` | Implement interface |
| `this` | Reference to current object |
| `super` | Reference to parent class |
| `new` | Create new instance |
| `instanceof` | Type check |

### 5. Static & Final (4)

| Keyword | Purpose |
|---------|---------|
| `static` | Class-level member |
| `final` | Cannot be changed |
| `abstract` | Incomplete definition |
| `native` | Implemented in C/C++ |

### 6. Exception Handling (5)

| Keyword | Purpose |
|---------|---------|
| `try` | Monitor for exceptions |
| `catch` | Handle exception |
| `finally` | Always execute |
| `throw` | Throw exception |
| `throws` | Declare exceptions |

### 7. Concurrency (8)

| Keyword | Purpose |
|---------|---------|
| `synchronized` | Thread-safe access |
| `volatile` | Disable caching |
| `transient` | Exclude from serialization |
| `assert` | Debug validation |

### 8. Package & Import (3)

| Keyword | Purpose |
|---------|---------|
| `package` | Define package |
| `import` | Import classes |
| `void` | No return value |

### 9. Reserved for Future Use (2)

| Keyword | Status |
|---------|--------|
| `const` | Reserved, not used |
| `goto` | Reserved, not used |

### 10. Literals (3)

| Keyword | Value |
|---------|-------|
| `true` | Boolean literal |
| `false` | Boolean literal |
| `null` | No object reference |

## Usage Guidelines

### When to Use Each Category

**Data Types:**
- Use `int` for most integer operations
- Use `long` for large numbers (IDs, timestamps)
- Use `double` for most decimal operations
- Use `float` only for memory-critical scenarios
- Use `boolean` for true/false values

**Control Flow:**
- Use `if-else` for binary decisions
- Use `switch` for multi-way decisions with constants
- Use `for` when you know iteration count
- Use `while` when condition determines loop
- Use `do-while` when body must execute at least once

**Object-Oriented:**
- Always use `@Override` annotation with `extends`
- Use `this` for field disambiguation
- Use `super` to call parent constructors/methods
- Use `instanceof` sparingly (prefer polymorphism)

**Exception Handling:**
- Always catch specific exceptions
- Use `finally` for resource cleanup
- Use try-with-resources when possible
- Declare checked exceptions in method signature

## Misconceptions

1. **"`goto` is unused"** - Correct, but reserved. Some bytecode uses it.
2. **"`const` is unused"** - Reserved, use `final` instead.
3. **"`final` means immutable"** - Only for reference, not content.
4. **"`static` means constant"** - Means class-level, not constant.
5. **"`volatile` means synchronized"** - Only ensures visibility, not atomicity.

## Interview Questions

1. Why are `goto` and `const` reserved but unused?
2. What is the difference between `==` and `.equals()`?
3. When would you use `volatile` instead of `synchronized`?
4. What does `instanceof` return for `null`?
5. How does `this()` differ from `super()`?
6. Can you override a `static` method? Why or why not?
7. What happens if you don't catch a checked exception?

## Cross-References

- See `01-variables/` for variable declarations
- See `02-oop/` for class/interface/enum keywords
- See `06-generics/` for type parameters
- See `09-multithreading-&-concurrency/` for synchronized/volatile
- See `10-jvm-internals/` for class loading and bytecode
