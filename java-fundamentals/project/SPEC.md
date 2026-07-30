# Smart Calculator - Design Specification

## Overview
A menu-driven CLI calculator application demonstrating Java Fundamentals (Sprint 1) concepts.

---

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-1 | Basic arithmetic: +, -, *, /, % | Must |
| FR-2 | Advanced: power (^), sqrt, factorial (!) | Must |
| FR-3 | Expression evaluation with precedence | Must |
| FR-4 | Parentheses support | Must |
| FR-5 | History (last 10 calculations) | Must |
| FR-4 | Menu-driven CLI interface | Must |
| FR-5 | Error handling with user-friendly messages | Must |

### Non-Functional Requirements

| ID | Requirement | Target |
|----|-------------|--------|
| NFR-1 | Java 21 compatible | Must |
| NFR-2 | Compiles with Maven | Must |
| NFR-3 | Passes Checkstyle (Google) | Must |
| NFR-4 | Passes SpotBugs (Max effort) | Must |
| NFR-5 | Passes PMD | Must |
| NFR-6 | JUnit 5 tests, 80%+ coverage | Must |
| NFR-6 | BigDecimal for precision | Must |

---

## Architecture

```
┌─────────────────────────────────────┐
│         CalculatorApp               │
│  (Main loop, command processing)    │
└──────────────┬──────────────────────┘
               │
    ┌──────────┼──────────┐
    ▼          ▼          ▼
┌────────┐ ┌─────────┐ ┌─────────────┐
│BasicOps│ │AdvOps   │ │Expression   │
│ (+,-,*, │ │ (pow,   │ │Evaluator    │
│ /,%,^)  │ │ sqrt,   │ │(Shunting    │
│         │ │ fact)   │ │ Yard)       │
└────────┘ └─────────┘ └─────────────┘
               │
               ▼
        ┌──────────────┐
        │   History    │
        │ (Deque<Calc>) │
        └──────────────┘
```

---

## Component Specifications

### 1. CalculatorApp
- Main entry point
- Command loop
- Command dispatch

**Commands:**
| Command | Aliases | Description |
|---------|---------|-------------|
| help | h, ? | Show help |
| quit | exit, q | Exit application |
| basic | b | Basic arithmetic mode |
| advanced | a | Advanced operations mode |
| eval | evaluate | Expression evaluation |
| history | hist | Show calculation history |
| clear | | Clear history |

### 2. ExpressionEvaluator
Implements Shunting Yard algorithm for infix to postfix conversion.

**Supported Operators:**
| Operator | Precedence | Associativity |
|----------|------------|---------------|
| ^ | 3 | Right |
| *, /, % | 2 | Left |
| +, - | 1 | Left |

**Token Types:**
- NUMBER: Digits with optional decimal point
- OPERATOR: + - * / % ^ ( )
- WHITESPACE: Ignored

### 3. BasicOperations
| Operation | Method | Edge Cases |
|-----------|--------|------------|
| Add | a + b | Overflow (BigDecimal) |
| Subtract | a - b | Overflow |
| Multiply | a * b | Overflow |
| Divide | a / b | Division by zero |
| Modulo | a % b | Modulo by zero |

### 4. AdvancedOperations
| Operation | Method | Constraints |
|-----------|--------|-------------|
| Power | a^b | b must be integer |
| Square Root | √a | a ≥ 0 |
| Factorial | n! | 0 ≤ n ≤ 20 |

### 4. HistoryManager
- Deque with max size 10
- FIFO eviction (removeLast when full)
- Thread-safe not required (single-threaded)

---

## Data Structures

### Calculation Record
```java
public record Calculation(String expression, BigDecimal result) {}
```

### Token
```java
public enum TokenType {
    NUMBER, OPERATOR, LEFT_PAREN, RIGHT_PAREN, END
}
```

---

## Error Handling

| Error | User Message | Recovery |
|-------|--------------|----------|
| Division by zero | "Error: Division by zero" | Continue |
| Modulo by zero | "Error: Modulo by zero" | Continue |
| Invalid number | "Error: Invalid number format" | Continue |
| Mismatched parens | "Error: Mismatched parentheses" | Continue |
| Invalid operator | "Error: Unknown operator" | Continue |
| Factorial overflow | "Error: Factorial too large (max 20)" | Continue |
| Negative sqrt | "Error: Cannot sqrt negative number" | Continue |

---

## Testing Strategy

### Unit Tests
- ExpressionEvaluator: Tokenizer, Shunting Yard, Postfix evaluation
- BasicOperations: All operators, edge cases
- AdvancedOperations: Power, sqrt, factorial, edge cases
- HistoryManager: Add, eviction, clear
- CalculatorApp: Command dispatch, integration

### Integration Tests
- Full expression evaluation flow
- History persistence across operations
- Command processing loop

### Test Data
| Expression | Expected |
|------------|----------|
| 2 + 3 | 5 |
| 10 - 4 | 6 |
| 6 * 7 | 42 |
| 20 / 4 | 5 |
| 17 % 5 | 2 |
| 2 ^ 10 | 1024 |
| sqrt(16) | 4 |
| 5! | 120 |
| 3 + 4 * 2 | 11 |
| (3 + 4) * 2 | 14 |
| 2 ^ 3 ^ 2 | 512 (right assoc) |

---

## UI/UX Design

### Help Text
```
=== Smart Calculator ===
Commands:
  help, h, ?     - Show this help
  quit, exit, q  - Exit calculator
  basic, b       - Basic arithmetic (+ - * / %)
  advanced, a    - Advanced (pow sqrt fact)
  eval, evaluate - Evaluate expression
  history, hist  - Show history (last 10)
  clear          - Clear history

Examples:
  calc> 3 + 4 * 2
  Result: 11
  calc> (3 + 4) * 2
  Result: 14
  calc> basic
  --- Basic Operation ---
  Operators: +, -, *, /, %
  Enter expression: 10 / 3
  Result: 3.3333333333333335
```

### History Display
```
=== History (last 10) ===
1. 3 + 4 * 2 = 11
2. (3 + 4) * 2 = 14
3. sqrt(16) = 4
4. 5! = 120
```

---

## Configuration

### MathContext
```java
MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);
```

### History Limit
```java
int MAX_HISTORY = 10;
```

---

## Deliverables

| File | Description |
|------|-------------|
| `pom.xml` | Maven project with dependencies |
| `CalculatorApp.java` | Main application |
| `ExpressionEvaluator.java` | Expression parsing & evaluation |
| `BasicOperations.java` | Basic arithmetic |
| `AdvancedOperations.java` | Power, sqrt, factorial |
| `HistoryManager.java` | Calculation history |
| `CalculatorTest.java` | Unit tests |
| `README.md` | Project documentation |
| `SPEC.md` | This file |

---

## Acceptance Criteria

- [ ] All functional requirements implemented
- [ ] All non-functional requirements met
- [ ] `mvn clean verify` passes
- [ ] Test coverage ≥ 80%
- [ ] No Checkstyle violations
- [ ] No SpotBugs findings
- [ ] No PMD violations
- [ ] Javadoc generates without warnings
- [ ] Manual testing: all commands work
- [ ] README.md complete with build/run instructions

---

## Timeline

| Phase | Duration | Deliverable |
|-------|----------|-------------|
| Setup & Tokenizer | Day 1 | Tokenizer tests pass |
| Shunting Yard | Day 2 | Postfix conversion works |
| Postfix Evaluation | Day 3 | Expression evaluation works |
| Basic Operations | Day 4 | + - * / % work |
| Advanced Operations | Day 5 | ^ sqrt ! work |
| History & CLI | Day 6 | Menu, history work |
| Integration & Polish | Day 7 | Full app works, tests pass |
| Documentation | Day 7 | README, SPEC complete |

---

## Notes

- Use `BigDecimal` for all arithmetic - no `double`/`float`
- Use `MathContext(15, RoundingMode.HALF_UP)` for division
- Exponentiation by squaring for integer powers
- Newton's method for square root
- Factorial: iterative, validate 0-20 range
- Keep methods small, single responsibility
- Javadoc all public APIs