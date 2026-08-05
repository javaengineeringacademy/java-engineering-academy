# Iterator Pattern

## Overview
The Iterator pattern provides a way to access elements of a collection sequentially without exposing its underlying representation. It separates the traversal algorithm from the collection structure.

## When to Use
- Accessing collection elements without exposing internal structure
- Supporting multiple traversal methods for a collection
- Providing a uniform interface for different collection types
- Collections with complex internal structures (trees, graphs)

## Code Structure
```
Iterator<T> (interface)    Iterable<T> (interface)
    |                          |
BookIterator (concrete)   BookCollection (concrete)
```

## Key Benefits
- Supports traversal of complex data structures
- Simplifies the collection interface
- Multiple iterators can traverse same collection simultaneously
- Follows Single Responsibility Principle

## Common Mistakes
- Modifying collection during iteration causing ConcurrentModificationException
- Not implementing reset when needed
- Forgetting to handle empty collections

## Interview Questions
1. What is the difference between Iterator and Iterable?
2. How does Java's Iterator differ from ListIterator?
3. What happens when collection is modified during iteration?
4. How would you implement a reverse iterator?
