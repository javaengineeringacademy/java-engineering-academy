# Sprint 1: Java Fundamentals

> **Master the foundation of Java programming** — from variables to methods, everything you need to write your first production-quality Java code.

---

## 🎯 Learning Objectives

By the end of this sprint, you will be able to:

- ✅ Understand Java program structure and execution model
- ✅ Declare variables with appropriate data types
- ✅ Apply operators and expressions effectively
- ✅ Control program flow with conditionals and loops
- ✅ Work with arrays and strings efficiently
- ✅ Design and implement reusable methods
- ✅ Write clean, documented, and tested Java code

---

## 📚 Module Contents

| Topic | Theory | Examples | Exercises | Solutions | Quiz |
|-------|--------|----------|-----------|-----------|------|
| [Java Basics](docs/basics.md) | [✓](docs/basics.md) | [Code](src/main/java/com/javaacademy/sprint1/basics) | [Exercises](../exercises/basics.md) | [Solutions](../solutions/basics.md) | [Quiz](../quiz/basics.md) |
| [Data Types](docs/datatypes.md) | [✓](docs/datatypes.md) | [Code](src/main/java/com/javaacademy/sprint1/datatypes) | [Exercises](../exercises/datatypes.md) | [Solutions](../solutions/datatypes.md) | [Quiz](../quiz/datatypes.md) |
| [Operators](docs/operators.md) | [✓](docs/operators.md) | [Code](src/main/java/com/javaacademy/sprint1/operators) | [Exercises](../exercises/operators.md) | [Solutions](../solutions/operators.md) | [Quiz](../quiz/operators.md) |
| [Control Flow](docs/controlflow.md) | [✓](docs/controlflow.md) | [Code](src/main/java/com/javaacademy/sprint1/controlflow) | [Exercises](../exercises/controlflow.md) | [Solutions](../solutions/controlflow.md) | [Quiz](../quiz/controlflow.md) |
| [Arrays](docs/arrays.md) | [✓](docs/arrays.md) | [Code](src/main/java/com/javaacademy/sprint1/arrays) | [Exercises](../exercises/arrays.md) | [Solutions](../solutions/arrays.md) | [Quiz](../quiz/arrays.md) |
| [Strings](docs/strings.md) | [✓](docs/strings.md) | [Code](src/main/java/com/javaacademy/sprint1/strings) | [Exercises](../exercises/strings.md) | [Solutions](../solutions/strings.md) | [Quiz](../quiz/strings.md) |
| [Methods](docs/methods.md) | [✓](docs/methods.md) | [Code](src/main/java/com/javaacademy/sprint1/methods) | [Exercises](../exercises/methods.md) | [Solutions](../solutions/methods.md) | [Quiz](../quiz/methods.md) |

---

## 🏗️ Project Structure

```
java-fundamentals/
├── src/
│   ├── main/
│   │   ├── java/com/javaacademy/sprint1/
│   │   │   ├── basics/         # Java program structure, Hello World
│   │   │   ├── datatypes/      # Primitive & reference types
│   │   │   ├── operators/      # Arithmetic, relational, logical, bitwise
│   │   │   ├── controlflow/    # if, switch, loops, break/continue
│   │   │   ├── arrays/         # Single & multi-dimensional arrays
│   │   │   ├── strings/        # String, StringBuilder, StringBuffer
│   │   │   └── methods/        # Method declaration, overloading, recursion
│   │   └── resources/
│   └── test/
│       └── java/com/javaacademy/sprint1/
│           └── ... (mirrors main structure)
├── docs/                       # Theory documents with diagrams
├── exercises/                  # Practice problems
├── solutions/                  # Exercise solutions with explanations
├── quiz/                       # Self-assessment quizzes
├── interview/                  # Interview questions & answers
├── assignment/                 # Graded assignments
├── project/                    # Mini project: Calculator App
└── diagrams/                   # Mermaid & UML diagrams
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 21+** (LTS)
- **Maven 3.9+**
- **Git**

### Build & Test

```bash
# From repository root
mvn clean compile test -pl java-fundamentals

# Run specific test class
mvn test -pl java-fundamentals -Dtest=BasicsTest

# Run with code quality checks
mvn clean verify -pl java-fundamentals -Pci
```

### Run Examples

```bash
# Compile and run any example
cd java-fundamentals
mvn compile exec:java -Dexec.mainClass="com.javaacademy.sprint1.basics.HelloWorld"
```

---

## 📖 Theory Documents

Each topic includes a comprehensive theory document covering:

| Section | Description |
|---------|-------------|
| **Introduction** | What, why, and real-world analogy |
| **Architecture** | How it works under the hood (JVM perspective) |
| **Syntax & Rules** | Complete syntax with edge cases |
| **Best Practices** | Industry-standard conventions |
| **Common Mistakes** | Pitfalls and how to avoid them |
| **Performance Notes** | Memory & CPU considerations |
| **Mermaid Diagrams** | Visual representations |

### Available Theory Docs

- [📘 Java Basics](docs/basics.md) — Program structure, JVM, compilation
- [📘 Data Types](docs/datatypes.md) — Primitives, wrappers, type casting
- [📘 Operators](docs/operators.md) — All operator categories with precedence
- [📘 Control Flow](docs/controlflow.md) — Decision making & loops
- [📘 Arrays](docs/arrays.md) — Declaration, initialization, algorithms
- [📘 Strings](docs/strings.md) — Immutability, builders, formatting
- [📘 Methods](docs/methods.md) — Signature, overloading, varargs, recursion

---

## 💻 Code Examples

Every concept includes **production-quality** examples:

```java
// Example: Method overloading with varargs
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int add(int... numbers) {
        return Arrays.stream(numbers).sum();
    }
    
    public double add(double a, double b) {
        return a + b;
    }
}
```

### Example Categories

| Package | Description |
|---------|-------------|
| `basics` | HelloWorld, ProgramStructure, CompilationProcess |
| `datatypes` | PrimitivesDemo, WrapperClasses, TypeCasting, Literals |
| `operators` | ArithmeticOps, RelationalOps, LogicalOps, BitwiseOps, PrecedenceDemo |
| `controlflow` | IfElseDemo, SwitchExpressions, ForLoops, WhileLoops, NestedLoops |
| `arrays` | ArrayBasics, MultiDimensionalArrays, ArrayAlgorithms, ArrayUtils |
| `strings` | StringBasics, StringBuilderDemo, StringFormatting, StringAlgorithms |
| `methods` | MethodBasics, OverloadingDemo, VarargsDemo, RecursionDemo |

---

## 🏋️ Exercises & Solutions

Progressive difficulty levels:

| Level | Description | Example |
|-------|-------------|---------|
| **Beginner** | Syntax practice, single concept | "Convert Celsius to Fahrenheit" |
| **Intermediate** | Combine 2-3 concepts | "Find max in array without Math.max" |
| **Advanced** | Algorithm implementation | "Implement binary search recursively" |

Each exercise includes:
- ✅ Clear requirements
- ✅ Input/output examples
- ✅ Constraints & edge cases
- ✅ Hints (optional)
- ✅ Complete solution with explanation
- ✅ Time/space complexity analysis

---

## 🧠 Quiz & Interview Prep

### Quiz Format
- Multiple choice (single/multiple answer)
- Code snippet analysis
- Output prediction
- True/False with justification

### Interview Questions Covered

| Category | Sample Questions |
|----------|------------------|
| **Fundamentals** | "Why is Java platform independent?" |
| **Data Types** | "Difference between `int` and `Integer`?" |
| **Operators** | "What is short-circuit evaluation?" |
| **Control Flow** | "When to use `switch` vs `if-else`?" |
| **Arrays** | "How does `Arrays.sort()` work internally?" |
| **Strings** | "Why is String immutable?" |
| **Methods** | "What is method signature? Can return type be part of it?" |

---

## 📋 Assignment

**Graded Assignment: Temperature Converter CLI**

Build a command-line application that:
1. Accepts temperature value and unit (C/F/K)
2. Converts to all other units
3. Handles invalid input gracefully
4. Includes unit tests (80%+ coverage)
5. Passes Checkstyle, SpotBugs, PMD

**Evaluation Criteria:**
- Code correctness (40%)
- Code quality & style (20%)
- Test coverage (20%)
- Documentation (10%)
- Git hygiene (10%)

---

## 🎓 Mini Project: Smart Calculator

**Capstone Project for Sprint 1**

Build a calculator supporting:
- Basic operations (+, -, *, /, %)
- Advanced operations (power, sqrt, factorial)
- History tracking (last 10 calculations)
- Expression evaluation (e.g., "3 + 4 * 2")
- Menu-driven CLI interface
- Comprehensive error handling
- JUnit 5 test suite
- Maven build with quality gates

### Project Structure
```
project/
├── README.md              # Project requirements
├── SPEC.md                # Design document
├── src/main/java/...      # Implementation
├── src/test/java/...      # Tests
└── pom.xml                # Project configuration
```

---

## 📊 Progress Tracking

| Topic | Theory | Examples | Exercises | Quiz | Status |
|-------|--------|----------|-----------|------|--------|
| Java Basics | ✅ | ✅ | ✅ | ✅ | Done |
| Data Types | ✅ | ✅ | ✅ | ✅ | Done |
| Operators | ✅ | ✅ | ✅ | ✅ | Done |
| Control Flow | ✅ | ✅ | ✅ | ✅ | Done |
| Arrays | ✅ | ✅ | ✅ | ✅ | Done |
| Strings | ✅ | ✅ | ✅ | ✅ | Done |
| Methods | ✅ | ✅ | ✅ | ✅ | Done |
| **Assignment** | — | — | — | — | **Pending** |
| **Mini Project** | — | — | — | — | **Pending** |

---

## 🔗 References & Further Reading

### Official Documentation
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/index.html)
- [Java Tutorials - Oracle](https://docs.oracle.com/en/java/javase/21/)
- [OpenJDK 21 Documentation](https://openjdk.org/projects/jdk/21/)

### Books
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 1-9
- *Java: The Complete Reference* — Herbert Schildt — Ch. 1-6
- *Head First Java* — Sierra & Bates — Ch. 1-5

### Articles & Blogs
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [JVM Internals](https://blog.codefx.org/java/jvm/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

---

## ✅ Quality Checklist

- [x] All code compiles on Java 21
- [x] All examples have expected output
- [x] All exercises have tested solutions
- [x] Checkstyle passes (Google style)
- [x] SpotBugs passes (Max effort, Low threshold)
- [x] PMD passes (Custom ruleset)
- [x] Javadoc generated for all public APIs
- [x] Mermaid diagrams render correctly
- [x] Cross-references validated
- [x] No TODO/FIXME in production code

---

## 📝 Changelog

See [CHANGELOG.md](../CHANGELOG.md) for detailed history.

---

## 🤝 Contributing

See [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

---

## 📄 License

Apache License 2.0 — See [LICENSE](../LICENSE)

---

> **Next Sprint:** [Sprint 2 - Object Oriented Programming](../oop-fundamentals/README.md)