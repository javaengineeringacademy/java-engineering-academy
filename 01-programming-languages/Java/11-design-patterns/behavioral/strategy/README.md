# Strategy Pattern

## Overview

You've probably written a method with a big `if-else` or `switch` that picks which algorithm to run — sorting, compression, payment processing. Every time you add a new option, you're back in that same method, modifying code that was already working. The Strategy pattern fixes this by letting you define each algorithm in its own class, then swap them at runtime without touching the code that uses them.

## When to Use

- Multiple algorithms for a specific task that can be selected at runtime
- Avoiding conditional statements for selecting algorithm behavior
- Payment processing, sorting algorithms, compression strategies

## When NOT to Use This

- You only have 2 variants and they're unlikely to change — a simple if-else is fine
- The "algorithms" differ by a single parameter, not actual behavior — use a config value instead
- You're adding strategies for the sake of patterns, not because the code actually needs it

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

## Trade-offs

| Aspect | Strategy Pattern | Simple if-else |
|--------|-----------------|----------------|
| Extensibility | Add new strategy without modifying context | Must modify existing code |
| Testability | Each strategy is independently testable | Test the whole conditional block |
| Complexity | More classes, indirection overhead | Simple, easy to follow |
| Runtime flexibility | Swap algorithms on the fly | Fixed at compile time |

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

## Production Checklist

### ✅ Before using Strategy Pattern in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## References

## Alternatives

| Approach | Runtime Swap | Extensibility | Complexity | Use When |
|----------|-------------|---------------|------------|----------|
| Strategy pattern | Yes | High (OCP) | Moderate | Algorithms need runtime switching |
| If-else/switch | No | Low | Low | 2-3 stable variants |
| Enum with behavior | Yes | Low | Low | Fixed set of behaviors |
| Lambda/Method ref | Yes | Moderate | Low | Single-method strategies |
| Inheritance | No | Moderate | Moderate | Is-a relationship, compile-time |

## Trade-offs

Strategy pattern provides flexibility because it:
- Adds indirection (one virtual method call overhead, ~5ns)
- Can lead to class explosion for minor variations (consider parameterizing instead)
- Requires understanding of each strategy's lifecycle (stateless strategies are safest)
- Combining with Factory adds complexity (justify the indirection)
- Testing each strategy separately increases test surface (factor out shared behavior)

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## References

- [Refactoring.Guru - Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
- [Head First Design Patterns - Strategy Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Effective Java - Item 21: Use method references instead of lambdas where possible](https://learning.oreilly.com/library/view/effective-java/9780134686097/)

## Common Myths

### ❌ Myth 1: Strategy is always overkill
**Reality:** Simple if-else is fine. Use Strategy when algorithms need to be swapped at runtime.

### ❌ Myth 2: Strategy requires interfaces
**Reality:** Can use lambdas. Java 8+ functional interfaces enable concise strategy definitions.

### ❌ Myth 3: Strategy is only for algorithms
**Reality:** Any behavior. Strategy encapsulates any interchangeable behavior, not just algorithms.

## Learning Objectives

By the end of this topic you will be able to:

- Identify code smells like long conditionals that Strategy pattern can clean up
- Design a strategy interface and implement concrete strategies for real use cases
- Decide when Strategy adds value vs when it's over-engineering
- Combine Strategy with Factory to manage strategy creation at runtime
- Write unit tests that verify each strategy independently
