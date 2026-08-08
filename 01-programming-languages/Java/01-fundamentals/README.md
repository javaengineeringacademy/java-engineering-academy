# Java Fundamentals

> **Difficulty:** ⭐ Beginner  
> **Reading:** 30 min | **Practice:** 60 min | **Total:** 90 min

## Overview
Every application needs to store data, make decisions, repeat tasks, and organize code. Java fundamentals provide these building blocks — data types, operators, control flow, methods, arrays, strings, packages, and build tools — in a strongly-typed, platform-independent manner. Without them, you cannot write reliable, maintainable Java programs.

## Why This Concept Exists
Every programming language needs a way to store data, make decisions, repeat tasks, and organize code. Java fundamentals provide these building blocks in a strongly-typed, platform-independent manner. Without understanding these concepts, you cannot write reliable, maintainable Java programs.

## History
- **1995** — Java 1.0 introduced basic syntax, Applets, and core libraries to provide a platform-independent, object-oriented language for web and enterprise applications
- **1997** — Java 1.1 added inner classes, JDBC, and reflection to improve database connectivity, code organization, and dynamic class inspection
- **2000** — Java 1.3 brought HotSpot JVM to significantly improve runtime performance through just-in-time compilation and adaptive optimization
- **2004** — Java 5 introduced generics, enums, annotations, and autoboxing to enhance type safety, metadata handling, and code conciseness
- **2011** — Java 7 added try-with-resources, diamond operator, and switch on Strings to simplify resource management, reduce boilerplate, and expand switch flexibility
- **2014** — Java 8 brought lambdas and Stream API to enable functional programming patterns and efficient bulk data processing
- **2017** — Java 9 introduced modules (JPMS) to improve large-scale application maintainability, security, and performance through modular design
- **2021** — Java 17 LTS added text blocks, sealed classes, and pattern matching to enhance readability, control inheritance, and simplify data inspection
- **2023** — Java 21 LTS added virtual threads, record patterns, and sequenced collections to improve concurrency, destructure records, and provide uniform collection ordering

## Production Notes
- **Where is it used?** In every Java application as the basic building blocks for variables, control flow, methods, and data structures
- **Why is it useful?** Provides the essential syntax and constructs needed to write any Java program, ensuring type safety and platform independence
- **When should it be avoided?** Not applicable; these fundamentals are required for all Java development
- **Alternative?** None; these are core language features

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

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| `==` vs `.equals()` confusion | IntelliJ debugger + Evaluate Expression | Use Evaluate to compare `str1 == str2` vs `str1.equals(str2)` at runtime |
| ArrayIndexOutOfBoundsException | Step-through debugging | Set breakpoint before loop; step through with F7 to watch index values |
| NullPointerException on config loading | Stack trace analysis | Read stack trace bottom-up; identify last line before exception to find null source |
| String concatenation performance | JMH microbenchmarks | Benchmark `+=` vs `StringBuilder` in loops to quantify O(n²) impact |
| Integer overflow | Unit tests with edge cases | Test with `Integer.MAX_VALUE` and verify wraparound behavior |

## Code Review Checklist

- [ ] String comparisons use `.equals()`, never `==`
- [ ] Loop boundaries use `<` not `<=` for array access
- [ ] Null checks performed before method calls on references
- [ ] `StringBuilder` used for string concatenation in loops
- [ ] Variables declared close to first use
- [ ] Magic numbers replaced with named constants
- [ ] No `System.out.println` in production code

## Architecture Considerations

Java fundamentals underpin every layer of a system architecture. At scale, choices around data types (primitive vs wrapper), string handling, and control flow directly affect memory footprint and throughput. For microservices, understanding JVM fundamentals like stack vs heap allocation enables proper sizing of container memory limits. For event-driven systems, knowing how strings are interned and how autoboxing works prevents memory leaks in high-throughput message processors.

When building large-scale systems, fundamental patterns like defensive copying, input validation, and error handling at method boundaries create robust APIs that scale across teams. These fundamentals become the contract that distributed systems depend on.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Input validation at boundaries | API endpoints, method entry | Pros: Fail-fast, clear error messages; Cons: Code duplication across layers |
| Defensive copying | Returning mutable objects from APIs | Pros: Prevents external mutation; Cons: Object allocation overhead |
| Constants over magic numbers | Configuration, thresholds | Pros: Readability, single source of truth; Cons: Slightly more code |
| Optional for nullable returns | Method return values | Pros: Explicit null handling; Cons: API verbosity, learning curve |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| String comparison via `==` in authentication | Bypass authentication, unauthorized access | Use `.equals()` or `MessageDigest.isEqual()` for security comparisons |
| Buffer overflow via unchecked array access | Data corruption, crashes | Validate array bounds before access; use enhanced for-each loops |
| Null pointer injection | Denial of service, information leakage | Validate all inputs; use `Objects.requireNonNull()` for preconditions |
| Integer overflow in financial calculations | Incorrect balances, data corruption | Use `BigDecimal` for financial values; validate ranges |
| Information leakage via exception messages | Attack surface exposure | Return generic messages to clients; log detailed errors server-side |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0–1.4 | Basic types, no generics | Migrate raw types to generic equivalents (Java 5+) |
| Java 5 | Generics, autoboxing, enhanced for | Replace raw collections with parameterized types; review autoboxing |
| Java 7 | try-with-resources, diamond operator | Replace manual finally blocks with try-with-resources |
| Java 8 | Lambda, Stream API | Replace anonymous classes with lambdas; use Streams for bulk operations |
| Java 10 | `var` for local variables | Use `var` for obvious type declarations to reduce verbosity |
| Java 17 | Text blocks, sealed classes, records | Use records for immutable data carriers; use text blocks for multi-line strings |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| `var` local variable inference | Java 10 | Stable |
| Text blocks (`"""`) | Java 15 | Stable |
| Switch expressions | Java 14 | Stable |
| Records | Java 16 | Stable |
| Pattern matching for instanceof | Java 16 | Stable |
| Sealed classes | Java 17 | Stable |

## Production Incidents

### Incident 1: String Comparison Bug in Authentication System

**Problem:** A login system failed to authenticate valid users intermittently, returning "invalid credentials" for correct passwords.
**Cause:** Developers used `==` instead of `.equals()` for password comparison, comparing object references instead of string content.
**Impact:** 15% of login attempts failed randomly, causing customer frustration and support tickets.
**Detection:** Users reported intermittent login failures; code review revealed the `==` comparison.
**Solution:** Replaced `password == storedPassword` with `password.equals(storedPassword)`.
**Prevention:** Use static analysis tools to flag `==` comparisons on String objects; enforce code review guidelines.

### Incident 2: ArrayIndexOutOfBoundsException in Data Processing

**Problem:** A data processing pipeline crashed daily with `ArrayIndexOutOfBoundsException` during peak hours.
**Cause:** Off-by-one error in loop boundary: `for (int i = 0; i <= array.length; i++)` instead of `< array.length`.
**Impact:** Data processing delayed by 2-3 hours daily, affecting reporting deadlines.
**Detection:** Exception logs showed the error occurring at the same line consistently.
**Solution:** Changed `<=` to `<` in the loop condition.
**Prevention:** Use enhanced for-each loops when index isn't needed; add boundary checks in code review.

### Incident 3: NullPointerException in Configuration Loading

**Problem:** Application startup failed with `NullPointerException` when loading configuration from properties files.
**Cause:** Configuration value was null when property key was missing, and code didn't check for null before calling methods.
**Impact:** Application couldn't start, causing 30-minute downtime during deployments.
**Detection:** Stack trace showed NPE in configuration loader class.
**Solution:** Added null checks and used `Optional` for configuration values with sensible defaults.
**Prevention:** Use `Optional` for potentially missing values; validate configuration at startup.

## Production Checklist

- [ ] Use `.equals()` for String comparison, never `==`
- [ ] Use enhanced for-each loops when index isn't needed
- [ ] Check for null before calling methods on references
- [ ] Use `StringBuilder` for string concatenation in loops
- [ ] Declare variables close to first use
- [ ] Use meaningful variable and method names
- [ ] Prefer `final` for constants and parameters
- [ ] Avoid magic numbers — use named constants
- [ ] Don't use `System.out.println` for production logging
- [ ] Write methods that do one thing well

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Writes basic syntax; uses `==` for strings; doesn't think about null safety |
| Intermediate | Uses `.equals()` correctly; understands null pointer risks; writes clean methods |
| Advanced | Applies immutability; uses Optional effectively; writes defensive code |
| Expert | Designs APIs that prevent misuse; mentors others on fundamentals; optimizes for correctness |

## Common Myths

1. **Myth**: `==` is fine for comparing Strings
   **Truth**: `==` compares object references, not content. Two String objects with identical text have different references. Always use `.equals()`.

2. **Myth**: Primitive types are always better than wrapper classes
   **Truth**: Wrapper classes are needed for generics, collections, and null values. Autoboxing is appropriate in many contexts.

3. **Myth**: `System.out.println` is acceptable for debugging
   **Truth**: Production code should use logging frameworks (SLF4J) with proper levels, rotation, and structured output.

4. **Myth**: Longer variable names are always better
   **Truth**: Variable names should be meaningful but concise. `i` is perfectly appropriate for loop indices.

5. **Myth**: Comments explain code better than good naming
   **Truth**: Well-named variables and methods reduce the need for comments. Comments should explain why, not what.

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
