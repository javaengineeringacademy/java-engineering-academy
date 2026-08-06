# Template Method Pattern

## Overview
The Template Method pattern defines the skeleton of an algorithm in a method, deferring some steps to subclasses. It lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

## When to Use
- Multiple classes share similar behavior but differ in details
- Common algorithm structure with varying implementations
- Avoid code duplication with shared logic
- Data mining, parsing, test frameworks

## Code Structure
```
DataMiner (abstract) - Template Method
    |                  mine() [final]
CSVDataMiner           |
                    abstract methods
JSONDataMiner        implemented by subclasses
```

## Key Benefits
- Code reuse: common behavior in one place
- Enforces algorithm structure
- Easy to add new variations
- Follows Hollywood Principle: "Don't call us, we'll call you"

## Common Mistakes
- Making template method overridable when it shouldn't be
- Too many abstract methods overwhelming subclasses
- Tight coupling between base and derived classes

## Interview Questions
1. What is the Hollywood Principle?
2. How does Template Method differ from Strategy pattern?
3. Can you override a final template method?
4. When should you use Template Method over composition?

## Performance

Template method adds one virtual method call (~5ns) for hook/abstract method invocation. The main benefit is code reuse — shared logic in the template method avoids duplication. Performance is identical to calling the steps directly. JUnit's `setUp()`/`tearDown()` and Spring's `JdbcTemplate` use template methods efficiently.

## Examples

```java
// Data processing pipeline
abstract class DataProcessor {
    // Template method - final to prevent override
    public final void process() {
        readData();
        processData();
        writeResults();
    }
    
    protected abstract void readData();
    protected abstract void processData();
    
    // Hook method with default implementation
    protected void writeResults() {
        System.out.println("Writing results to console");
    }
}

class CSVProcessor extends DataProcessor {
    @Override protected void readData() {
        System.out.println("Reading CSV file");
    }
    @Override protected void processData() {
        System.out.println("Parsing CSV rows");
    }
    @Override protected void writeResults() {
        System.out.println("Writing CSV output");
    }
}

class DatabaseProcessor extends DataProcessor {
    @Override protected void readData() {
        System.out.println("Executing SQL query");
    }
    @Override protected void processData() {
        System.out.println("Processing result set");
    }
    // Uses default writeResults()
}

// Usage
DataProcessor csv = new CSVProcessor();
csv.process(); // read → process → write

DataProcessor db = new DatabaseProcessor();
db.process(); // read → process → default write
```

## Internal Working

The template method is defined in the abstract base class and marked `final` (optional). It calls abstract or hook methods that subclasses implement. The base class controls the algorithm structure; subclasses fill in the details. The Hollywood Principle applies: "Don't call us, we'll call you." The base class calls subclass methods, not the other way around.

## Why This Concept Exists

Many algorithms share the same structure but differ in specific steps: data mining reads → parses → analyzes → reports; each step varies by format. Without template method, each subclass duplicates the algorithm structure. Template method puts the structure in one place and lets subclasses customize steps. It enforces consistency while enabling flexibility.

## Pitfalls

1. **Inheritance lock-in**: Subclasses are tightly coupled to the base class — hard to change the template
2. **Fragile base class**: Changes to the template method can break all subclasses
3. **Limited flexibility**: Cannot change the algorithm structure at runtime (unlike Strategy)
4. **Overriding confusion**: Which methods should be abstract vs hook vs final requires careful design
5. **Testing**: Testing the template method requires testing each subclass variant

## References

- [Refactoring.Guru - Template Method](https://refactoring.guru/design-patterns/template-method)
- [Head First Design Patterns - Template Method](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [JUnit Lifecycle](https://junit.org/junit5/docs/current/userguide/#writing-tests)
