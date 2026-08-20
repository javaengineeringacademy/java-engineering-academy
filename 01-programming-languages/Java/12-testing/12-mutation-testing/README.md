# 11.12 Mutation Testing

## 1. Introduction

Mutation testing is a technique to evaluate test quality by introducing mutations (changes) to the source code and checking if tests detect them. PIT (Pitest) is the most popular mutation testing tool for Java.

## 2. Learning Objectives

- Understand mutation testing concepts
- Use PIT mutation testing tool
- Interpret mutation scores
- Identify weak tests
- Improve test quality through mutation analysis

## 3. Prerequisites

- JUnit 5 knowledge
- Understanding of code coverage
- Basic testing concepts

## 4. Why This Concept Exists

Mutation testing addresses:
- High code coverage with weak tests
- Tests that pass but don't catch bugs
- Code coverage doesn't equal test quality
- Identifying missing assertions

## 5. Problem Statement

How do we measure test quality beyond code coverage?

## 6. Theory

### Mutation Types

| Mutation | Description |
|----------|-------------|
| Condition boundary | > to >=, < to <= |
| Negate condition | == to != |
| Return values | Return true to false |
| Math operations | + to -, * to / |
| Void method calls | Remove void method calls |
| Null returns | Return null instead of object |

### Mutation Score

```
Mutation Score = Killed Mutations / Total Mutations × 100%
```

- **Killed**: Test detected the mutation (good)
- **Survived**: Test didn't detect (needs improvement)
- **No coverage**: Test doesn't cover mutated code

### PIT Architecture

```
Source Code → Bytecode Instrumentation → Mutants
    ↓
Test Execution → Result Comparison
    ↓
Kill/Survive Report → Mutation Score
```

## 7. Internal Working

### Mutation Process

1. Parse source code and bytecode
2. Generate mutants by applying mutations
3. For each mutant:
   - Instrument bytecode
   - Run tests against mutant
   - Compare results with original
4. Classify as killed or survived
5. Generate mutation report

### Bytecode Instrumentation

PIT modifies bytecode at the instruction level:
- Replaces comparison operators
- Changes return values
- Removes method calls
- Modifies arithmetic operations

## 8. JVM Perspective

- Mutants run in test JVM
- Each mutant is a separate execution
- Instrumentation adds overhead
- Memory usage scales with mutation count

## 9. Memory Representation

```
Mutation Testing Memory:
┌─────────────────────────────────────┐
│           JVM Heap                  │
│  - Original classes                 │
│  - Mutated classes (one at a time)  │
│  - Test execution results           │
├─────────────────────────────────────┤
│         PIT Engine                  │
│  - Mutation generators              │
│  - Bytecode manipulator             │
│  - Result analyzer                  │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
// Source code
class Calculator {
    int add(int a, int b) {
        return a + b;
    }
}

// Mutant 1: Change + to -
class Calculator {
    int add(int a, int b) {
        return a - b; // Mutated!
    }
}

// Test that kills the mutant
@Test
void shouldAdd() {
    assertEquals(5, calculator.add(2, 3));
    // Fails with mutant: 2 - 3 = -1, not 5
}
```

## 11. Medium Example

```java
// Source code
class PasswordValidator {
    boolean isValid(String password) {
        return password != null && password.length() >= 8;
    }
}

// Mutant: Change >= to >
class PasswordValidator {
    boolean isValid(String password) {
        return password != null && password.length() > 8; // Mutated!
    }
}

// Test that kills the mutant
@Test
void shouldAccept8CharPassword() {
    assertTrue(validator.isValid("12345678"));
    // Fails with mutant: 8 > 8 is false
}
```

## 12. Hard Example

```java
// Source code
class OrderService {
    double calculateDiscount(Order order) {
        if (order.getTotal() > 1000) {
            return order.getTotal() * 0.1;
        } else if (order.getTotal() > 500) {
            return order.getTotal() * 0.05;
        }
        return 0;
    }
}

// Mutant: Change > to >=
class OrderService {
    double calculateDiscount(Order order) {
        if (order.getTotal() >= 1000) { // Mutated!
            return order.getTotal() * 0.1;
        } else if (order.getTotal() >= 500) { // Mutated!
            return order.getTotal() * 0.05;
        }
        return 0;
    }
}

// Tests that kill the mutants
@Test
void shouldApply10PercentForExactly1000() {
    Order order = new Order(1000);
    assertEquals(100, service.calculateDiscount(order));
}

@Test
void shouldApply5PercentForExactly500() {
    Order order = new Order(500);
    assertEquals(25, service.calculateDiscount(order));
}
```

## Interview Questions

1. **What is mutation testing?**
   Mutation testing evaluates test quality by introducing code mutations and checking if tests detect them.

2. **What is a mutation score?**
   The percentage of mutants killed by tests (killed / total × 100).

3. **How does mutation testing differ from code coverage?**
   Code coverage measures execution; mutation testing measures test effectiveness.

4. **What is PIT (Pitest)?**
   PIT is a Java mutation testing framework that works at the bytecode level.

5. **When should you run mutation tests?**
   As part of CI/CD to ensure test quality, typically on changed code.
