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

## Performance

Strategy invocation adds one virtual method call (~5ns) plus the cost of the strategy implementation itself. Switching strategies at runtime is O(1) — just reassigning a reference. In hot paths, prefer strategy instances over lambda creation per call. The JVM can devirtualize calls if the strategy type is monomorphic.

## Examples

```java
// Sorting strategy
interface SortStrategy<T extends Comparable<T>> {
    void sort(List<T> list);
}

class BubbleSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // Bubble sort implementation
        System.out.println("Sorting with Bubble Sort");
    }
}

class QuickSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // QuickSort implementation
        System.out.println("Sorting with QuickSort");
    }
}

class Sorter<T extends Comparable<T>> {
    private SortStrategy<T> strategy;
    
    Sorter(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }
    
    void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }
    
    void sort(List<T> data) {
        strategy.sort(data);
    }
}

// Usage
Sorter<Integer> sorter = new Sorter<>(new QuickSortStrategy<>());
sorter.sort(List.of(3, 1, 4, 1, 5));

sorter.setStrategy(new BubbleSortStrategy<>());
sorter.sort(List.of(3, 1, 4, 1, 5));

// Lambda approach (Java 8+)
Sorter<Integer> lambdaSorter = new Sorter<>(list -> {
    System.out.println("Lambda sort");
    list.sort(Comparable::compareTo);
});
```

## Internal Working

The context class holds a reference to a strategy interface. At runtime, different implementations can be swapped in. The context delegates the algorithm call to the strategy object. This replaces conditional logic (if/else or switch) with polymorphism. The strategy is typically stateless and shared, or stateful and per-context.

## Why This Concept Exists

Algorithms often have multiple variants: sorting (bubble, quick, merge), compression (zip, gzip, snappy), payment (credit card, PayPal, crypto). Without strategy, you end up with long switch statements or conditional chains that violate open/closed principle — adding a new algorithm means modifying existing code. Strategy encapsulates each algorithm in its own class, making the system extensible without modification.

## Pitfalls

1. **Over-engineering**: If there are only 2 variants and they never change, a simple if-else is fine
2. **State management**: Stateless strategies are safe; stateful ones require careful lifecycle management
3. **Strategy explosion**: Too many strategies for minor variations — consider parameterizing instead
4. **Factory overhead**: Combining strategy with factory adds complexity — justify the indirection
5. **Testing**: Each strategy needs its own unit tests — factor out shared behavior

## References

- [Refactoring.Guru - Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
- [Head First Design Patterns - Strategy Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Effective Java - Item 21: Use method references instead of lambdas where possible](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
