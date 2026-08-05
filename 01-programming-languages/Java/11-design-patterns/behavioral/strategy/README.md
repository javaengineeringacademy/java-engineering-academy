# Strategy Pattern

## Overview
The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. It lets the algorithm vary independently from clients that use it.

## When to Use
- Multiple algorithms for a specific task that can be selected at runtime
- Avoiding conditional statements for selecting algorithm behavior
- Payment processing, sorting algorithms, compression strategies

## Code Structure
```
Strategy (interface)           Context
    |                            |
SortStrategy              holds Strategy reference
    |
BubbleSort, QuickSort (concrete)
```

## Key Benefits
- Eliminates conditional statements
- Algorithms can be switched at runtime
- Open/Closed Principle: new strategies don't require modifying context

## Common Mistakes
- Creating too many strategies for simple variations
- Not considering strategy lifecycle and state management
- Over-engineering when a simple if-else would suffice

## Interview Questions
1. How does Strategy pattern differ from State pattern?
2. Can you combine Strategy with Factory pattern?
3. What are the performance implications of strategy switching?
4. When should you prefer Strategy over inheritance?
