# When to Use Keywords

## Decision Guide

### Keyword Categories

| Category | Keywords | Use When |
|----------|----------|----------|
| Data types | `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char` | Declaring variables |
| Control flow | `if`, `else`, `switch`, `case`, `for`, `while`, `do`, `break`, `continue`, `return` | Logic control |
| Modifiers | `public`, `private`, `protected`, `static`, `final`, `abstract`, `synchronized`, `volatile`, `transient`, `native`, `strictfp` | Access and behavior |
| Class-related | `class`, `interface`, `enum`, `extends`, `implements`, `package`, `import`, `instanceof`, `new`, `this`, `super` | OOP structure |
| Exception handling | `try`, `catch`, `finally`, `throw`, `throws` | Error handling |
| Reserved (unused) | `const`, `goto` | Never use (reserved for future) |
| Primitive literals | `true`, `false`, `null` | Literal values |
| Type inference | `var` (Java 10+) | Local variable type inference |

### Access Modifier Decision Tree

| Access Level | `private` | Default | `protected` | `public` |
|--------------|-----------|---------|-------------|----------|
| Same class | Yes | Yes | Yes | Yes |
| Same package | No | Yes | Yes | Yes |
| Subclass | No | No | Yes | Yes |
| Anywhere | No | No | No | Yes |

### When to Use Each Modifier

| Modifier | Use When | Example |
|----------|----------|---------|
| `public` | API, available everywhere | `public class Service` |
| `private` | Internal implementation | `private int count;` |
| `protected` | Subclass access needed | `protected void helper()` |
| `default` | Package-private access | `void internal()` |
| `static` | Class-level, not instance | `static int counter;` |
| `final` | Immutable, constant | `final int MAX = 100;` |
| `abstract` | Incomplete, must be extended | `abstract class Shape` |

### Static vs Instance

| Use Static When | Use Instance When |
|-----------------|-------------------|
| No object state needed | Accesses/modifies instance fields |
| Utility method | Part of object behavior |
| Constant value | Varies per instance |
| Factory method | Needs object context |

## Production Guidelines

### Naming Conventions
```java
// Classes: PascalCase
public class UserService { }

// Methods: camelCase
public void processOrder() { }

// Variables: camelCase
int userCount = 0;

// Constants: UPPER_SNAKE_CASE
public static final int MAX_RETRIES = 3;

// Packages: lowercase
package com.company.project;
```

### Reserved Keywords
```java
// NEVER use as identifiers:
// const, goto, true, false, null

// These are reserved but unused:
// You cannot create variables named "const" or "goto"
```

### Contextual Keywords
```java
// These are keywords only in specific contexts:
// module, requires, exports, opens, uses, provides, with, to, open

// They can be used as identifiers in other contexts:
int module = 10;  // Valid (not in module context)
```
