# Functional Programming - References

## Official Documentation
- [Java Tutorials: Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
- [Java Tutorials: Streams](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/streams.html)
- [Java Tutorials: Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)
- [Java API: java.util.function Package](https://docs.oracle.com/java/docs/8/docs/api/java/util/function/package-summary.html)
- [Java API: java.util.stream Package](https://docs.oracle.com/java/docs/8/docs/api/java/util/stream/package-summary.html)

## Books
- *Effective Java* (Joshua Bloch) - Items on lambdas, streams, and Optional
- *Modern Java in Action* (Urma, Fusco, Mycroft) - Comprehensive functional Java
- *Java 8 Lambdas* (Richard Warburton) - Practical lambda guide

## Functional Interface Quick Reference

| Interface | Arity | Input | Output | Example Use |
|-----------|-------|-------|--------|-------------|
| `Predicate<T>` | 1 | T | boolean | `filter()` |
| `Function<T,R>` | 1 | T | R | `map()` |
| `Consumer<T>` | 1 | T | void | `forEach()` |
| `Supplier<T>` | 0 | - | T | `generate()` |
| `UnaryOperator<T>` | 1 | T | T | `replace()` |
| `BinaryOperator<T>` | 2 | T, T | T | `reduce()` |
| `BiFunction<T,U,R>` | 2 | T, U | R | Bi-map operations |
| `BiPredicate<T,U>` | 2 | T, U | boolean | Combined conditions |
| `BiConsumer<T,U>` | 2 | T, U | void | Paired iteration |

## Stream Terminal Operations

| Operation | Return Type | Description |
|-----------|-------------|-------------|
| `collect()` | R | Accumulate to collection |
| `reduce()` | Optional<T> | Combine into single value |
| `forEach()` | void | Iterate with side effect |
| `count()` | long | Count elements |
| `anyMatch()` | boolean | Any element matches? |
| `allMatch()` | boolean | All elements match? |
| `findFirst()` | Optional<T> | First element |
| `findAny()` | Optional<T> | Any element (parallel) |
| `toList()` | List<T> | Collect to list (Java 16+) |

## Collectors Reference
| Collector | Purpose |
|-----------|---------|
| `toList()` | Collect to List |
| `toSet()` | Collect to Set |
| `toMap()` | Collect to Map |
| `groupingBy()` | Group by classifier |
| `partitioningBy()` | Partition by predicate |
| `joining()` | Join strings |
| `summarizingInt()` | Statistical summary |
| `counting()` | Count elements |
| `reducing()` | Custom reduction |

## Related Topics
- [Reactive Streams (Flow API)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html)
- [CompletableFuture Composition](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- [Records as Value Objects](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8-10.html)
