# Sprint 1 Assignment: Temperature Converter CLI

## 📋 Assignment Overview

Build a **command-line temperature converter** that demonstrates mastery of Sprint 1 concepts.

**Due:** End of Sprint 1  
**Weight:** 100 points  
**Submission:** Push to your fork, create PR to main repo

---

## 🎯 Requirements

### Functional Requirements

1. **Accept Input**
   - Temperature value (double)
   - Source unit (C, F, or K)
   - Target unit (C, F, or K) - optional, default: convert to ALL

2. **Perform Conversions**
   - Celsius ↔ Fahrenheit: `F = C × 9/5 + 32`
   - Celsius ↔ Kelvin: `K = C + 273.15`
   - Fahrenheit ↔ Kelvin: Convert via Celsius

3. **Display Output**
   - Formatted to 2 decimal places
   - Show all three units if target not specified
   - Clear labels with units

4. **Error Handling**
   - Invalid number format → "Error: Invalid temperature value"
   - Invalid unit → "Error: Invalid unit. Use C, F, or K"
   - Below absolute zero → "Error: Temperature below absolute zero"

5. **Interactive Mode** (Bonus)
   - Loop until user types "quit" or "exit"
   - Show prompt: `temp> `

### Example Runs

```
$ java TemperatureConverter 100 C F
100.00°C = 212.00°F

$ java TemperatureConverter 32 F
32.00°F = 0.00°C = 273.15K

$ java TemperatureConverter 0 K C
0.00K = -273.15°C = -459.67°F

$ java TemperatureConverter abc C
Error: Invalid temperature value

$ java TemperatureConverter 100 X
Error: Invalid unit. Use C, F, or K

$ java TemperatureConverter -300 C
Error: Temperature below absolute zero
```

---

## 🏗️ Technical Requirements

### Code Structure
```
src/main/java/com/javaacademy/sprint1/assignment/
├── TemperatureConverter.java    # Main class with main()
├── Temperature.java             # Value object (immutable)
├── Unit.java                    # Enum: CELSIUS, FAHRENHEIT, KELVIN
├── Converter.java               # Conversion logic (static methods)
├── Parser.java                  # Input parsing & validation
└── CLI.java                     # Command-line interface
```

### Mandatory Features
- [ ] **Enums** for temperature units
- [ ] **Immutable** Temperature value object
- [ ] **Static methods** for conversions
- [ ] **Exception handling** with custom exceptions
- [ ] **Javadoc** on all public classes/methods
- [ ] **Java 21** features (records, pattern matching if applicable)
- [ ] **Google Java Style** (Checkstyle passes)

### Testing Requirements
- [ ] **JUnit 5** tests in `src/test/...`
- [ ] Minimum **80% code coverage**
- [ ] Test all conversion formulas
- [ ] Test edge cases (absolute zero, invalid input)
- [ ] Test CLI parsing

### Quality Gates (Must Pass)
```bash
mvn clean verify -Pci
```
- ✅ Checkstyle (Google style)
- ✅ SpotBugs (Max effort, Low threshold)
- ✅ PMD (Custom ruleset)
- ✅ All tests pass
- ✅ Javadoc generates without warnings

---

## 📊 Evaluation Rubric (100 Points)

| Criteria | Points | Details |
|----------|--------|---------|
| **Correctness** | 35 | All conversions accurate, edge cases handled |
| **Code Quality** | 20 | Clean architecture, SOLID, naming, Javadoc |
| **Test Coverage** | 20 | 80%+ coverage, meaningful assertions |
| **Documentation** | 10 | README, Javadoc, inline comments |
| **Git Hygiene** | 10 | Logical commits, meaningful messages, no merge commits |
| **Build Passes** | 5 | `mvn clean verify -Pci` succeeds |

### Deductions
- -10: Checkstyle violations
- -10: SpotBugs findings
- -10: PMD violations
- -15: Tests fail
- -20: Doesn't compile
- -30: No tests
- -50: Plagiarism / AI-generated without understanding

---

## 🚀 Getting Started

```bash
# 1. Create feature branch
git checkout -b assignment/temperature-converter

# 2. Implement in java-fundamentals/assignment/

# 3. Run tests locally
cd java-fundamentals
mvn test

# 4. Run quality checks
mvn checkstyle:check spotbugs:check pmd:check

# 5. Commit frequently with meaningful messages
git add .
git commit -m "feat: add Temperature enum with absolute zero constants"
git commit -m "feat: implement conversion logic with unit tests"
git commit -m "feat: add CLI parsing with validation"

# 6. Push and create PR
git push origin assignment/temperature-converter
# Create PR to main branch
```

---

## 📚 Reference Implementation Hints

### Temperature Enum
```java
public enum Unit {
    CELSIUS('C', -273.15),
    FAHRENHEIT('F', -459.67),
    KELVIN('K', 0.0);
    
    private final char symbol;
    private final double absoluteZero;
    
    Unit(char symbol, double absoluteZero) { ... }
    
    public static Unit fromChar(char c) { ... }
}
```

### Temperature Record (Java 16+)
```java
public record Temperature(double value, Unit unit) {
    public Temperature {
        if (value < unit.absoluteZero()) {
            throw new IllegalArgumentException("Below absolute zero");
        }
    }
    
    public Temperature to(Unit target) { ... }
}
```

### Converter
```java
public final class Converter {
    private Converter() {}
    
    public static double celsiusToFahrenheit(double c) { ... }
    public static double fahrenheitToCelsius(double f) { ... }
    // ... all 6 combinations
}
```

---

## 📝 Submission Checklist

- [ ] Code compiles: `mvn compile`
- [ ] Tests pass: `mvn test`
- [ ] Quality gates: `mvn verify -Pci`
- [ ] Javadoc generates: `mvn javadoc:javadoc`
- [ ] README.md with build/run instructions
- [ ] Meaningful commit history (5+ commits)
- [ ] PR created with description
- [ ] Self-review completed (check rubric)

---

## 💡 Tips for Success

1. **Start with tests** - Write conversion tests first
2. **Use records** - Temperature is perfect for `record`
3. **Enum methods** - Put conversion logic in enum
4. **Validate early** - Parse and validate in CLI layer
5. **Immutable objects** - No setters, validate in constructor
5. **Run checks often** - `mvn checkstyle:check` during development

---

## 🎓 Learning Outcomes

After completing this assignment, you will have demonstrated:
- Java program structure & compilation
- Primitive types, operators, control flow
- Enums, records, immutable objects
- Static methods, method overloading
- Exception handling
- Command-line argument parsing
- Unit testing with JUnit 5
- Code quality tools (Checkstyle, SpotBugs, PMD)
- Git workflow & PR process

---

**Good luck!** 🎯 This assignment covers everything from Sprint 1.