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
