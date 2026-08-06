# Java Fundamentals

> **Difficulty:** ⭐ Beginner  
> **Reading:** 30 min | **Practice:** 60 min | **Total:** 90 min

## Overview
This module covers the essential building blocks of Java programming — data types, operators, control flow, methods, arrays, strings, packages, build tools, and reserved keywords. These are the foundation every Java developer must master before moving to object-oriented programming.

## Why This Concept Exists
Every programming language needs a way to store data, make decisions, repeat tasks, and organize code. Java fundamentals provide these building blocks in a strongly-typed, platform-independent manner. Without understanding these concepts, you cannot write reliable, maintainable Java programs.

## History
- **1995** — Java 1.0 released by Sun Microsystems with basic syntax, Applets, and core libraries
- **1997** — Java 1.1 added inner classes, JDBC, and reflection
- **2000** — Java 1.3 brought HotSpot JVM for better performance
- **2004** — Java 5 introduced generics, enums, annotations, and autoboxing
- **2011** — Java 7 added try-with-resources, diamond operator, and switch on Strings
- **2014** — Java 8 brought lambdas and Stream API
- **2017** — Java 9 introduced modules (JPMS)
- **2021** — Java 17 LTS added text blocks, sealed classes, and pattern matching
- **2023** — Java 21 LTS added virtual threads, record patterns, and sequenced collections

## Core Concepts

### Data Types
| Category | Types |
|----------|-------|
| Primitive | byte, short, int, long, float, double, boolean, char |
| Reference | Class, Interface, Array, Enum, Annotation |

### Operators
| Category | Operators |
|----------|-----------|
| Arithmetic | +, -, *, /, % |
| Relational | ==, !=, <, >, <=, >= |
| Logical | &&, \|\|, ! |
| Bitwise | &, \|, ^, ~, <<, >> |
| Assignment | =, +=, -=, *=, /= |

### Control Flow
| Statement | Purpose |
|-----------|---------|
| if/else | Conditional branching |
| switch | Multi-way branching |
| for | Counted loop |
| while | Conditional loop |
| do-while | Post-test loop |
| break/continue | Loop control |

### Methods
- Method signature: access modifier, return type, name, parameters
- Method overloading: same name, different parameter list
- `static` methods belong to the class, not instances
- `varargs` (`Type...`) for variable-length parameter lists

### Arrays
- Fixed-size, zero-indexed containers
- `int[] arr = new int[10];` or `int[] arr = {1, 2, 3};`
- `Arrays.sort()`, `Arrays.copyOf()`, `Arrays.fill()`

### Strings
- Immutable objects stored in the string pool
- `String`, `StringBuilder` (mutable), `StringBuffer` (thread-safe mutable)

### Packages and Project Structure
- Packages group related classes: `com.company.project.module`
- Naming convention: reversed domain name
- `import` statement for using classes from other packages

### Maven
- Build automation and dependency management
- `pom.xml` defines project configuration
- Lifecycle: validate → compile → test → package → install → deploy

### Java Keywords
Java has 67 reserved keywords including `abstract`, `assert`, `boolean`, `break`, `byte`, `case`, `catch`, `char`, `class`, `const`, `continue`, `default`, `do`, `double`, `else`, `enum`, `extends`, `final`, `finally`, `float`, `for`, `goto`, `if`, `implements`, `import`, `instanceof`, `int`, `interface`, `long`, `native`, `new`, `package`, `private`, `protected`, `public`, `return`, `short`, `static`, `strictfp`, `super`, `switch`, `synchronized`, `this`, `throw`, `throws`, `transient`, `try`, `void`, `volatile`, `while`.

## Internal Working

### Compilation Process
```
Source (.java) → javac → Bytecode (.class) → JVM → Machine Code
```

1. **Lexical Analysis** — Tokenizes source code
2. **Parsing** — Builds Abstract Syntax Tree (AST)
3. **Semantic Analysis** — Type checking, scope resolution
4. **Code Generation** — Produces JVM bytecode
5. **Verification** — Bytecode verifier ensures safety

### JVM Execution
```
Class Loading → Bytecode Verification → Interpretation → JIT Compilation → Native Code
```

### Memory Allocation
- **Stack** — Local variables, method parameters, return addresses
- **Heap** — Objects and instance variables
- **String Pool** — Interned string literals
- **Constant Pool** — Compile-time constants

## Examples

### Hello World
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

### Variables and Types
```java
public class Variables {
    public static void main(String[] args) {
        int age = 25;
        double salary = 75000.50;
        char grade = 'A';
        boolean active = true;
        String name = "Java";
        
        System.out.println(name + " developer, age " + age);
    }
}
```

### Control Flow
```java
public class ControlFlow {
    public static void main(String[] args) {
        int score = 85;
        
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade: B");
        } else {
            System.out.println("Grade: C");
        }
        
        for (int i = 1; i <= 5; i++) {
            System.out.println("Count: " + i);
        }
    }
}
```

## Performance

| Operation | Time | Space |
|-----------|------|-------|
| Primitive access | O(1) | Fixed (4-8 bytes) |
| String concatenation | O(n) | O(n) |
| Array access | O(1) | O(n) |
| Method call | O(1) | O(stack frame) |

- Primitives are faster than wrapper classes (no boxing/unboxing)
- `StringBuilder` is faster than `+` concatenation in loops
- String pool reduces memory for repeated literals

## Best Practices

**Do's:**
- Use `StringBuilder` for string concatenation in loops
- Declare variables close to first use
- Use meaningful variable and method names
- Prefer `final` for constants and parameters
- Use `enhanced for` when index is not needed

**Don'ts:**
- Don't use magic numbers — use named constants
- Don't use `==` for String comparison
- Don't ignore compiler warnings
- Don't use `System.out.println` for production logging
- Don't write overly long methods

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `==` vs `.equals()` | Reference vs value comparison | Use `.equals()` for strings |
| Array index out of bounds | Accessing non-existent index | Check `length - 1` |
| Integer overflow | Wraps around silently | Use `long` for large values |
| NullPointerException | Uninitialized reference | Check for null before use |
| String immutability confusion | `.replace()` returns new string | Reassign: `s = s.replace(...)` |

## Interview Questions

### Q1: What are the differences between `==` and `.equals()`?
**Answer:** `==` compares references (memory addresses). `.equals()` compares values. For String, `.equals()` checks content.

### Q2: Why is String immutable in Java?
**Answer:** Strings are stored in the string pool. Immutability enables string interning, security, and thread safety. It also allows class loading and network connections to be safe.

### Q3: What is the difference between `StringBuilder` and `StringBuffer`?
**Answer:** `StringBuilder` is not thread-safe but faster. `StringBuffer` is synchronized. Use `StringBuilder` for single-threaded scenarios.

### Q4: What happens when you use `null` in a switch statement?
**Answer:** A `NullPointerException` is thrown at runtime if the switch expression is `null`.

### Q5: What is the diamond operator (`<>`)?
**Answer:** Introduced in Java 7, it allows the compiler to infer type arguments: `List<String> list = new ArrayList<>();`.

### Q6: What are wrapper classes and autoboxing?
**Answer:** Wrapper classes (`Integer`, `Double`, etc.) wrap primitives as objects. Autoboxing automatically converts between them: `int i = Integer.valueOf(5);`.

### Q7: What is the difference between `finalize()` and `try-with-resources`?
**Answer:** `finalize()` is unreliable and deprecated. `try-with-resources` automatically closes `AutoCloseable` resources and is the preferred approach.

### Q8: What is the `var` keyword in Java 10+?
**Answer:** Local variable type inference. The compiler infers the type from the initializer: `var list = new ArrayList<String>();`.

## Cross-References

- **Next Module:** [02 - Object-Oriented Programming](../02-oop/)
- **Related:** [06 - Generics](../06-generics/) — type safety and parameterized types
- **Related:** [05 - Text Processing](../05-text-processing/) — deeper String and text handling
- **Related:** [04 - Collections](../04-collections/) — dynamic data structures beyond arrays
- **External:** [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- **External:** [Java Language Specification](https://docs.oracle.com/javase/specs/)

## Prerequisites

No prerequisites — this is the starting point.

## Related Topics

- [Pass by Value](../00-knowledge-atoms/pass-by-value/README.md)
- [Autoboxing](../00-knowledge-atoms/autoboxing/README.md)

## Next

- [OOP](../02-oop/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Java basics (variables, operators, control flow) |
| Complexity | N/A |
| Thread Safe | N/A |
| Ordered | N/A |
| Allows Null | N/A |
| Best Alternative | N/A |
| When to Use | Starting Java journey |
| When to Avoid | Skipping basics |
