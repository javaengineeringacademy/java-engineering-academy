# Sprint 1 Mini Project: Smart Calculator

> **Capstone Project for Sprint 1** — Build a menu-driven calculator with history tracking and expression evaluation.

---

## 📋 Project Overview

Build a command-line calculator that demonstrates mastery of Sprint 1 concepts:
- Basic arithmetic operations
- Advanced mathematical functions
- Expression parsing and evaluation
- History tracking
- Menu-driven interface
- Comprehensive error handling

---

## 🎯 Requirements

### Core Features (Required)

1. **Basic Operations**
   - Addition (+), Subtraction (-), Multiplication (*), Division (/), Modulo (%)

2. **Advanced Operations**
   - Power (a^b) with integer exponents
   - Square root (√a)
   - Factorial (a!) for 0-20

3. **Expression Evaluation**
   - Support operator precedence: ^ > * / % > + -
   - Parentheses for grouping
   - Left-to-right associativity (except power: right-associative)
   - Decimal number support

4. **History Tracking**
   - Store last 10 calculations
   - Display history on command
   - Clear history option

5. **Menu-Driven CLI**
   - Interactive command loop
   - Help command
   - Quit command
   - Direct expression input

6. **Error Handling**
   - Division by zero
   - Invalid expressions
   - Mismatched parentheses
   - Domain errors (sqrt of negative, factorial > 20)
   - Invalid characters

### Bonus Features (Optional)

- Scientific notation output for large numbers
- Memory functions (M+, M-, MR, MC)
- Constants (π, e)
- Trigonometric functions (sin, cos, tan)

---

## 🏗️ Architecture

### Project Structure

```
project/
├── pom.xml
├── README.md
├── SPEC.md
└── src/
    ├── main/
    │   └── java/com/javaacademy/sprint1/calculator/
    │       ├── CalculatorApp.java          # Main application
    │       ├── expression/
    │       │   ├── Tokenizer.java          # Expression tokenization
    │       │   ├── ShuntingYard.java       # Infix to postfix conversion
    │       │   └── PostfixEvaluator.java   # Postfix evaluation
    │       ├── operations/
    │       │   ├── BasicOperations.java    # +, -, *, /, %
    │       │   └── AdvancedOperations.java # pow, sqrt, factorial
    │       ├── history/
    │       │   └── CalculationHistory.java # History management
    │       └── ui/
    │           └── CalculatorUI.java       # CLI interface
    └── test/
        └── java/com/javaacademy/sprint1/calculator/
            ├── CalculatorAppTest.java
            ├── expression/
            │   ├── TokenizerTest.java
            │   ├── ShuntingYardTest.java
            │   └── PostfixEvaluatorTest.java
            ├── operations/
            │   ├── BasicOperationsTest.java
            │   └── AdvancedOperationsTest.java
            └── history/
                └── CalculationHistoryTest.java
```

### Design Patterns Used

| Pattern | Usage |
|---------|-------|
| **Shunting Yard** | Infix to postfix conversion for expression evaluation |
| **Command** | Menu commands (Help, History, Clear, Quit) |
| **Strategy** | Operation implementations (Basic, Advanced) |
| **Singleton** | CalculationHistory (single history per session) |
| **Factory** | Operation creation based on user input |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+

### Build & Run

```bash
cd java-fundamentals/project
mvn clean compile exec:java
```

### Run Tests

```bash
mvn test
```

### Run with Quality Checks

```bash
mvn clean verify
```

---

## 🎮 Usage

### Commands

| Command | Description |
|---------|-------------|
| `help`, `h`, `?` | Show help message |
| `quit`, `exit`, `q` | Exit calculator |
| `history`, `hist` | Show calculation history |
| `clear` | Clear history |
| `basic` | Basic arithmetic mode |
| `advanced` | Advanced operations mode |
| `eval`, `evaluate` | Expression evaluation mode |

### Direct Expression Input

You can directly type mathematical expressions:

```text
calc> 10 + 5 * 2
Result: 20

calc> (3 + 4) * 2
Result: 14

calc> 2 ^ 10
Result: 1024

calc> sqrt(16)
Result: 4

calc> 5!
Result: 120
```

### Supported Operators

| Operator | Description | Precedence | Associativity |
|----------|-------------|------------|---------------|
| `^` | Power | 3 (highest) | Right |
| `*`, `/`, `%` | Multiply, Divide, Modulo | 2 | Left |
| `+`, `-` | Add, Subtract | 1 (lowest) | Left |
| `()` | Parentheses | Override | - |

### Examples

```text
calc> 10 + 5 * 2
Result: 20

calc> (3 + 4) * 2
Result: 14

calc> 2 ^ 10
Result: 1024

calc> (3 + 4) * 2 - 10 / 5
Result: 12

calc> 5!
Result: 120
```

---

## 🧪 Testing

### Test Coverage Requirements

| Component | Minimum Coverage |
|-----------|------------------|
| Expression parsing | 90% |
| Shunting Yard algorithm | 95% |
| Postfix evaluation | 95% |
| Basic operations | 100% |
| Advanced operations | 90% |
| History management | 85% |
| CLI interface | 80% |

### Test Categories

1. **Unit Tests** - Individual components
2. **Integration Tests** - End-to-end expression evaluation
3. **Edge Case Tests** - Division by zero, overflow, invalid input
4. **Performance Tests** - Large expression evaluation

---

## 📝 Implementation Checklist

### Phase 1: Core Engine
- [ ] Tokenizer: Split expression into tokens
- [ ] Shunting Yard: Convert infix to postfix
- [ ] Postfix Evaluator: Evaluate postfix expressions
- [ ] Number parsing (integers, decimals, negatives)

### Phase 2: Operations
- [ ] Basic operations: +, -, *, /, %
- [ ] Advanced: power, sqrt, factorial
- [ ] Operator precedence and associativity
- [ ] Parentheses handling

### Phase 3: CLI & History
- [ ] Menu system with commands
- [ ] Expression evaluation mode
- [ ] History tracking (last 10)
- [ ] Help system

### Phase 4: Quality
- [ ] Error handling (division by zero, invalid input)
- [ ] Input validation
- [ ] Unit tests (80%+ coverage)
- [ ] Code quality (Checkstyle, SpotBugs, PMD)

### Phase 5: Polish
- [ ] Help system
- [ ] Pretty output formatting
- [ ] Performance optimization
- [ ] Documentation

---

## 📊 Evaluation Rubric (100 Points)

| Criteria | Points | Details |
|----------|--------|---------|
| **Functionality** | 35 | All core features work correctly |
| **Code Quality** | 20 | Clean code, SOLID, naming, Javadoc |
| **Test Coverage** | 20 | 80%+ coverage, meaningful tests |
| **Error Handling** | 10 | Graceful error handling, user feedback |
| **Documentation** | 10 | README, SPEC, inline comments |
| **Git Hygiene** | 5 | Logical commits, meaningful messages |

---

## 💡 Hints & Tips

1. **Start Simple**: Get basic + - * / working first
2. **Test Early**: Write tests for each component as you build
3. **Use BigDecimal**: For precise decimal arithmetic
4. **Shunting Yard**: Implement carefully - test with many expressions
5. **Error Messages**: Make them user-friendly
6. **Commit Often**: Small, logical commits with clear messages

---

## 🔗 References

- [Shunting Yard Algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm)
- [BigDecimal Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html)
- [Java 21 MathContext](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/MathContext.html)

---

## 📝 Submission

1. Push to your fork
2. Create PR to main repo
3. Ensure CI passes
4. Request review

---

**Good luck!** 🎯 This project consolidates everything from Sprint 1.