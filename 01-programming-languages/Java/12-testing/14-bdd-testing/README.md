# 11.14 BDD Testing with Cucumber

## 1. Introduction

Behavior-Driven Development (BDD) extends TDD by writing test cases in natural language. Cucumber is the most popular BDD framework for Java, using Gherkin syntax for feature files.

## 2. Learning Objectives

- Understand BDD principles and Gherkin syntax
- Write feature files with scenarios
- Implement step definitions
- Use Cucumber with JUnit 5
- Integrate BDD into development workflow

## 3. Prerequisites

- JUnit 5 knowledge
- Understanding of TDD
- Basic testing concepts

## 4. Why This Concept Exists

BDD addresses:
- Communication gap between developers and stakeholders
- Ambiguous requirements
- Missing acceptance criteria
- Unclear test documentation
- Need for living documentation

## 5. Problem Statement

How do we write tests that serve as documentation and ensure the system behaves as expected from a business perspective?

## 6. Theory

### Gherkin Syntax

```gherkin
Feature: Calculator

  Scenario: Add two numbers
    Given I have a calculator
    When I add 2 and 3
    Then the result should be 5
```

### BDD Cycle

```
Discovery → Formulation → Automation
   ↓            ↓            ↓
Discuss    Write Gherkin   Implement
requirements  scenarios    step defs
```

### Cucumber Components

| Component | Purpose |
|-----------|---------|
| Feature file | Gherkin scenarios (.feature) |
| Step definitions | Java methods for steps |
| Runner | Executes scenarios |
| Hooks | Setup/teardown |
| Data tables | Tabular test data |
| Tags | Scenario categorization |

## 7. Internal Working

### Cucumber Execution Flow

1. Parse feature files
2. Match steps to definitions
3. Execute step definitions
4. Report results
5. Generate HTML/JSON reports

### Step Matching

```
Given I have a calculator
    ↓
@Given("I have a calculator")
public void iHaveACalculator() {
    calculator = new Calculator();
}
```

## 8. JVM Perspective

- Cucumber runs in test JVM
- Feature files parsed at startup
- Step definitions instantiated per scenario
- Reports generated after execution

## 9. Memory Representation

```
Cucumber Memory Model:
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - Feature file parsers             │
│  - Step definition instances        │
│  - Scenario state                   │
│  - World (shared state)             │
├─────────────────────────────────────┤
│         Cucumber Engine             │
│  - Step matcher                     │
│  - Scenario executor                │
│  - Report generator                 │
└─────────────────────────────────────┘
```

## 10. Easy Example

```gherkin
# calculator.feature
Feature: Calculator

  Scenario: Add two numbers
    Given I have entered 5 into the calculator
    And I have entered 7 into the calculator
    When I press add
    Then the result should be 12
```

```java
import io.cucumber.java.en.*;

public class CalculatorSteps {
    private Calculator calculator;
    private int result;

    @Given("I have entered {int} into the calculator")
    public void iHaveEntered(int number) {
        calculator = new Calculator();
        calculator.enter(number);
    }

    @When("I press add")
    public void iPressAdd() {
        result = calculator.add();
    }

    @Then("the result should be {int}")
    public void theResultShouldBe(int expected) {
        assertEquals(expected, result);
    }
}
```

## 11. Medium Example

```gherkin
Feature: User Registration

  Background:
    Given the user is on the registration page

  Scenario: Successful registration
    When the user enters valid credentials
    And the user submits the form
    Then the user should see a welcome message
    And the user should receive a confirmation email

  Scenario: Duplicate email
    When the user enters an existing email
    And the user submits the form
    Then the user should see an error message
```

## 12. Hard Example

```gherkin
Feature: Shopping Cart

  Scenario Outline: Add items to cart
    Given the cart is empty
    When the user adds <quantity> of "<product>"
    Then the cart should contain <quantity> items
    And the total should be $<total>

    Examples:
      | quantity | product  | total  |
      | 1        | Laptop   | 999.99 |
      | 2        | Mouse    | 59.98  |
      | 3        | Keyboard | 149.97 |

  Scenario: Apply discount
    Given the cart contains items totaling $100
    When the user applies discount code "SAVE10"
    Then the total should be $90
```

## Interview Questions

1. **What is BDD?**
   BDD is a software development practice where tests are written in natural language to describe system behavior.

2. **What is Gherkin?**
   Gherkin is the language used to write feature files in Cucumber.

3. **What is a step definition?**
   Step definitions are Java methods that implement the steps in Gherkin scenarios.

4. **What is a feature file?**
   Feature files contain Gherkin scenarios that describe expected system behavior.

5. **How does Cucumber differ from JUnit?**
   Cucumber uses natural language scenarios; JUnit uses Java test methods.
