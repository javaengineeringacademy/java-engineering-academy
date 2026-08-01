# Sprint 2: Object Oriented Programming

> **Master the pillars of OOP** — encapsulation, inheritance, polymorphism, and abstraction — with enterprise-grade examples.

---

## 🎯 Learning Objectives

By the end of this sprint, you will be able to:

- ✅ Design classes with proper encapsulation
- ✅ Implement inheritance hierarchies with `extends`
- ✅ Apply polymorphism (compile-time & runtime)
- ✅ Design abstractions with `abstract` classes and `interface`
- ✅ Implement `equals()`, `hashCode()`, `toString()` correctly
- ✅ Apply composition, aggregation, and association
- ✅ Understand Dependency Injection concepts
- ✅ Apply SOLID principles introduction
- ✅ Build a complete Bank Management System

---

## 📚 Module Contents

| Topic | Theory | Examples | Exercises | Solutions | Quiz |
|-------|--------|----------|-----------|-----------|------|
| [Classes & Objects](docs/classes.md) | [✓](docs/classes.md) | [Code](src/main/java/academy/javaengineering/oop/classes) | [Exercises](../exercises/classes.md) | [Solutions](../solutions/classes.md) | [Quiz](../quiz/classes.md) |
| [Constructors](docs/constructors.md) | [✓](docs/constructors.md) | [Code](src/main/java/academy/javaengineering/oop/constructors) | [Exercises](../exercises/constructors.md) | [Solutions](../solutions/constructors.md) | [Quiz](../quiz/constructors.md) |
| [Methods](docs/methods.md) | [✓](docs/methods.md) | [Code](src/main/java/academy/javaengineering/oop/methods) | [Exercises](../exercises/methods.md) | [Solutions](../solutions/methods.md) | [Quiz](../quiz/methods.md) |
| [Encapsulation](docs/encapsulation.md) | [✓](docs/encapsulation.md) | [Code](src/main/java/academy/javaengineering/oop/encapsulation) | [Exercises](../exercises/encapsulation.md) | [Solutions](../solutions/encapsulation.md) | [Quiz](../quiz/encapsulation.md) |
| [Inheritance](docs/inheritance.md) | [✓](docs/inheritance.md) | [Code](src/main/java/academy/javaengineering/oop/inheritance) | [Exercises](../exercises/inheritance.md) | [Solutions](../solutions/inheritance.md) | [Quiz](../quiz/inheritance.md) |
| [Polymorphism](docs/polymorphism.md) | [✓](docs/polymorphism.md) | [Code](src/main/java/academy/javaengineering/oop/polymorphism) | [Exercises](../exercises/polymorphism.md) | [Solutions](../solutions/polymorphism.md) | [Quiz](../quiz/polymorphism.md) |
| [Abstraction](docs/abstraction.md) | [✓](docs/abstraction.md) | [Code](src/main/java/academy/javaengineering/oop/abstraction) | [Exercises](../exercises/abstraction.md) | [Solutions](../solutions/abstraction.md) | [Quiz](../quiz/abstraction.md) |
| [Interfaces](docs/interfaces.md) | [✓](docs/interfaces.md) | [Code](src/main/java/academy/javaengineering/oop/interfaces) | [Exercises](../exercises/interfaces.md) | [Solutions](../solutions/interfaces.md) | [Quiz](../quiz/interfaces.md) |
| [Abstract Classes](docs/abstract-classes.md) | [✓](docs/abstract-classes.md) | [Code](src/main/java/academy/javaengineering/oop/abstract-classes) | [Exercises](../exercises/abstract-classes.md) | [Solutions](../solutions/abstract-classes.md) | [Quiz](../quiz/abstract-classes.md) |
| [Object Class](docs/object-class.md) | [✓](docs/object-class.md) | [Code](src/main/java/academy/javaengineering/oop/object-class) | [Exercises](../exercises/object-class.md) | [Solutions](../solutions/object-class.md) | [Quiz](../quiz/object-class.md) |
| [Equals & HashCode](docs/equals-hashcode.md) | [✓](docs/equals-hashcode.md) | [Code](src/main/java/academy/javaengineering/oop/equals-hashcode) | [Exercises](../exercises/equals-hashcode.md) | [Solutions](../solutions/equals-hashcode.md) | [Quiz](../quiz/equals-hashcode.md) |
| [Composition & Aggregation](docs/composition-aggregation.md) | [✓](docs/composition-aggregation.md) | [Code](src/main/java/academy/javaengineering/oop/composition-aggregation) | [Exercises](../exercises/composition-aggregation.md) | [Solutions](../solutions/composition-aggregation.md) | [Quiz](../quiz/composition-aggregation.md) |
| [Dependency Injection](docs/dependency-injection.md) | [✓](docs/dependency-injection.md) | [Code](src/main/java/academy/javaengineering/oop/dependency-injection) | [Exercises](../exercises/dependency-injection.md) | [Solutions](../solutions/dependency-injection.md) | [Quiz](../quiz/dependency-injection.md) |
| [SOLID Principles](docs/solid.md) | [✓](docs/solid.md) | [Code](src/main/java/academy/javaengineering/oop/solid) | [Exercises](../exercises/solid.md) | [Solutions](../solutions/solid.md) | [Quiz](../quiz/solid.md) |
| [Object Copying](docs/object-copying.md) | [✓](docs/object-copying.md) | [Code](src/main/java/academy/javaengineering/oop/object-copying) | [Exercises](../exercises/object-copying-easy.md) | [Solutions](../solutions/) | [Quiz](../quiz/) |
| [Serialization](docs/serialization.md) | [✓](docs/serialization.md) | [Code](src/main/java/academy/javaengineering/oop/serialization) | [Exercises](../exercises/serialization-easy.md) | [Solutions](../solutions/) | [Quiz](../quiz/) |

---

## 🏗️ Project Structure

```
oop-fundamentals/
├── src/
│   ├── main/
│   │   ├── java/academy/javaengineering/oop/
│   │   │   ├── bank/                 # Bank Management System (Final Project)
│   │   │   ├── classes/              # Class design fundamentals
│   │   │   ├── constructors/         # Constructor patterns
│   │   │   ├── methods/              # Method design
│   │   │   ├── encapsulation/        # Getters, setters, immutability
│   │   │   ├── inheritance/          # extends, super, method overriding
│   │   │   ├── polymorphism/         # Compile-time & runtime
│   │   │   ├── abstraction/          # abstract classes
│   │   │   ├── interfaces/           # interface, default methods
│   │   │   ├── abstract-classes/     # Abstract class design
│   │   │   ├── object-class/         # equals, hashCode, toString, clone
│   │   │   ├── equals-hashcode/      # Proper implementation
│   │   │   ├── composition-aggregation/ # has-a relationships
│   │   │   ├── dependency-injection/ # Constructor/Setter/Field injection
│   │   │   ├── solid/                # SOLID principle examples
│   │   │   ├── object-copying/       # Reference, Shallow, Deep copy
│   │   │   └── serialization/        # Serializable, Externalizable, transient
│   │   └── resources/
│   └── test/
│       └── java/academy/javaengineering/oop/
│           └── ... (mirrors main structure)
├── docs/                       # Theory documents with diagrams
├── exercises/                  # Practice problems
├── solutions/                  # Exercise solutions with explanations
├── quiz/                       # Self-assessment quizzes
├── interview/                  # Interview questions & answers
├── assignment/                 # Graded assignments
├── project/                    # Mini project: Bank Management System
└── diagrams/                   # Mermaid & UML diagrams
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 21+** (LTS)
- **Maven 3.9+**
- **Git**
- Completed Sprint 1: Java Fundamentals

### Build & Test

```bash
# From repository root
mvn clean compile test -pl oop-fundamentals

# Run specific test class
mvn test -pl oop-fundamentals -Dtest=InheritanceTest

# Run with code quality checks
mvn clean verify -pl oop-fundamentals -Pci
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

---

## 💻 Code Examples

Every concept includes **production-quality** examples:

```java
// Example: Proper equals() and hashCode() implementation
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && 
               currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
```

### Example Categories

| Package | Description |
|---------|-------------|
| `classes` | Class design, fields, methods, constructors |
| `constructors` | No-arg, parameterized, copy, builder pattern |
| `methods` | Instance, static, final, varargs, method references |
| `encapsulation` | Getters/setters, immutability, validation |
| `inheritance` | extends, super, method overriding, final |
| `polymorphism` | Compile-time (overloading) & runtime (overriding) |
| `abstraction` | Abstract classes, template method pattern |
| `interfaces` | interface, default/static methods, functional interfaces |
| `abstract-classes` | Abstract class design, template method |
| `object-class` | equals, hashCode, toString, clone, finalize |
| `equals-hashcode` | Contract, common implementations, pitfalls |
| `composition-aggregation` | has-a vs is-a, UML notation |
| `dependency-injection` | Constructor, setter, field injection |
| `solid` | SRP, OCP, LSP, ISP, DIP with examples |

---

## 🏋️ Exercises & Solutions

Progressive difficulty levels:

| Level | Description | Example |
|-------|-------------|---------|
| **Beginner** | Syntax practice, single concept | "Create a Person class with proper encapsulation" |
| **Intermediate** | Combine 2-3 concepts | "Design inheritance hierarchy for shapes" |
| **Advanced** | Algorithm/design implementation | "Implement Strategy pattern for payment processing" |

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
| **Classes & Objects** | "Difference between class and object?" |
| **Constructors** | "Can a constructor be private? When?" |
| **Encapsulation** | "Why make fields private? Getters/setters?" |
| **Inheritance** | "What is the diamond problem? How does Java solve it?" |
| **Polymorphism** | "Compile-time vs runtime polymorphism?" |
| **Abstraction** | "Abstract class vs interface? When to use each?" |
| **Interfaces** | "What are default methods? When to use?" |
| **Object Class** | "Why override equals/hashCode together?" |
| **Composition** | "Composition vs inheritance? Favor composition?" |
| **SOLID** | "Explain each SOLID principle with example" |

---

## 📋 Assignment

**Graded Assignment: Employee Management System**

Design a class hierarchy for an employee management system:
1. Abstract base class `Employee` with common fields
2. Concrete subclasses: `FullTimeEmployee`, `PartTimeEmployee`, `Contractor`
3. Interface `Payable` for salary calculation
4. Proper `equals()`, `hashCode()`, `toString()` implementations
5. Composition for `Department` and `Project` relationships
6. Unit tests with 85%+ coverage
7. Passes Checkstyle, SpotBugs, PMD

**Evaluation Criteria:**
- OOP design correctness (35%)
- Code quality & style (25%)
- Test coverage (20%)
- Documentation (10%)
- Git hygiene (10%)

---

## 🎓 Mini Project: Bank Management System

**Capstone Project for Sprint 2**

Build a complete banking system with:
- Account hierarchy: `Account` (abstract) → `SavingsAccount`, `CurrentAccount`
- Customer management with composition
- Transaction processing with history
- Interest calculation for savings
- Overdraft facility for current accounts
- Employee management (Manager, Teller)
- Loan processing with status tracking
- Proper exception handling
- JUnit 5 test suite (90%+ coverage)
- Maven build with quality gates

### Project Structure
```
project/
├── README.md              # Project requirements
├── SPEC.md                # Design document
├── src/main/java/...      # Implementation
├── src/test/java/...      # Tests
├── pom.xml                # Project configuration
└── diagrams/              # Class, sequence, ER diagrams
```

---

## 📊 Progress Tracking

| Topic | Theory | Examples | Exercises | Quiz | Status |
|-------|--------|----------|-----------|------|--------|
| Classes & Objects | ✅ | ✅ | ✅ | ✅ | Done |
| Constructors | ✅ | ✅ | ✅ | ✅ | Done |
| Methods | ✅ | ✅ | ✅ | ✅ | Done |
| Encapsulation | ✅ | ✅ | ✅ | ✅ | Done |
| Inheritance | ✅ | ✅ | ✅ | ✅ | Done |
| Polymorphism | ✅ | ✅ | ✅ | ✅ | Done |
| Abstraction | ✅ | ✅ | ✅ | ✅ | Done |
| Interfaces | ✅ | ✅ | ✅ | ✅ | Done |
| Abstract Classes | ✅ | ✅ | ✅ | ✅ | Done |
| Object Class | ✅ | ✅ | ✅ | ✅ | Done |
| Equals & HashCode | ✅ | ✅ | ✅ | ✅ | Done |
| Composition & Aggregation | ✅ | ✅ | ✅ | ✅ | Done |
| Dependency Injection | ✅ | ✅ | ✅ | ✅ | Done |
| SOLID Principles | ✅ | ✅ | ✅ | ✅ | Done |
| **Assignment** | — | — | — | — | **Pending** |
| **Mini Project** | — | — | — | — | **Pending** |

---

## 🔗 References & Further Reading

### Official Documentation
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/index.html)
- [Java Tutorials - Oracle](https://docs.oracle.com/javase/tutorial/)
- [OpenJDK 21 Documentation](https://openjdk.org/projects/jdk/21/)

### Books
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 10-20
- *Java: The Complete Reference* — Herbert Schildt — Ch. 7-12
- *Head First Java* — Sierra & Bates — Ch. 6-10
- *Design Patterns* — GoF — Chapters 1-5

### Articles & Blogs
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [SOLID Principles](https://blog.cleancoder.com/uncle-bob/2014/05/08/SOLID.html)
- [Effective Java Item 10: equals()](https://www.oracle.com/technical-resources/articles/java/effectivejava.html)

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

> **Next Sprint:** [Sprint 3 - Collections & Generics](../collections-fundamentals/README.md)