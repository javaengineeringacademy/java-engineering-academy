# Observer Pattern

## Overview
The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. It implements a publish-subscribe mechanism.

## When to Use
- When changes to one object require changing others, and you don't know how many objects need to be changed
- When an object should notify other objects without being tightly coupled to them
- Event handling systems, GUI frameworks, message queues, stock price updates

## Code Structure
```
Subject (interface)          Observer (interface)
    |                             |
NewsAgency (concrete)        NewsReader (concrete)
```

## Key Benefits
- Loose coupling between subject and observers
- Dynamic relationships at runtime
- Open/Closed Principle: new observers can be added without modifying the subject

## Common Mistakes
- Memory leaks from not properly detaching observers
- Circular dependencies between subjects and observers
- Cascading updates causing performance issues

## Interview Questions
1. What is the difference between Observer and Mediator?
2. How does the Observer pattern relate to the Publish-Subscribe model?
3. What problems can occur with notification order?
4. How do you prevent memory leaks in Observer pattern?
