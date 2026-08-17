# Decision Guide: Functional Interfaces

## When to Use
- Callbacks and event handlers (e.g., button click listeners)
- Stream API operations (`map`, `filter`, `reduce`)
- Lambda expressions and method references
- Strategy pattern implementations
- CompletableFuture and async pipelines

## When NOT to Use
- When you need multiple abstract methods — use a regular interface
- When behavior must hold state across calls — use a class
- When the interface may need to evolve with new abstract methods — use an abstract class

## Trade-offs

| Aspect | Functional Interface | Regular Interface | Abstract Class |
|--------|---------------------|-------------------|----------------|
| Methods | Exactly 1 abstract | Multiple abstract | Mix of abstract/concrete |
| Lambda support | Yes | No | No |
| State | Stateless lambdas | N/A | Can hold state |
| Flexibility | Limited to 1 method | Multiple methods | Multiple + defaults |
| Extensibility | New default methods OK | New abstract = breaking | New abstract = breaking |

## Expert Recommendation
Use functional interfaces for single-behavior abstractions (callbacks, strategies, transformers). Prefer `java.util.function` built-ins (`Predicate`, `Function`, `Consumer`, `Supplier`) over custom ones. Mark custom functional interfaces with `@FunctionalInterface` for compile-time safety.
