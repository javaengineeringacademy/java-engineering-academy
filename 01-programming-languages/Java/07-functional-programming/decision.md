# Functional Programming - Decision Guide

## Lambda Expressions
- Use lambdas for short, single-method implementations
- Use method references when they are clearer: `String::toUpperCase` vs `s -> s.toUpperCase()`
- Keep lambdas concise; extract to named methods if logic is complex

## Method References
| Type | Syntax | Equivalent Lambda |
|------|--------|-------------------|
| Static | `Math::sqrt` | `x -> Math.sqrt(x)` |
| Bound | `System.out::println` | `x -> System.out.println(x)` |
| Unbound | `String::toLowerCase` | `s -> s.toLowerCase()` |
| Constructor | `ArrayList::new` | `() -> new ArrayList<>()` |

## Functional Interfaces
| Interface | Method | Use Case |
|-----------|--------|----------|
| `Predicate<T>` | `boolean test(T)` | Filtering |
| `Function<T,R>` | `R apply(T)` | Transformation |
| `Consumer<T>` | `void accept(T)` | Side effects |
| `Supplier<T>` | `T get()` | Factory/lazy creation |
| `UnaryOperator<T>` | `T apply(T)` | Same-type transformation |
| `BinaryOperator<T>` | `T apply(T,T)` | Combining two values |
| `BiFunction<T,U,R>` | `R apply(T,U)` | Two-argument transform |
| `BiPredicate<T,U>` | `boolean test(T,U)` | Two-argument predicate |

## Stream Operations
- **Intermediate** (lazy): `filter`, `map`, `flatMap`, `distinct`, `sorted`, `peek`
- **Terminal** (trigger execution): `forEach`, `collect`, `reduce`, `count`, `anyMatch`, `allMatch`
- **Short-circuit**: `findFirst`, `findAny`, `limit`, `skip`

## When to Use Each Stream Operation
- `filter()` - Select elements matching a predicate
- `map()` - Transform each element
- `flatMap()` - Flatten nested structures (List<List<T>> → List<T>)
- `reduce()` - Combine elements into a single value
- `collect()` - Accumulate into a collection or use Collectors
- `groupingBy()` - Group elements by a classifier
- `partitioningBy()` - Split into two groups (true/false)
- `toMap()` - Collect to Map with key/value/value-merge functions

## Optional
- Return `Optional<T>` instead of null for optional values
- Use `orElse()`, `orElseGet()`, `orElseThrow()` for unwrapping
- Chain with `map()`, `filter()`, `flatMap()`
- Never call `get()` without a preceding `isPresent()` or terminal operation

## Composition
- Compose functions with `andThen()` and `compose()`
- Negate predicates with `negate()`
- Combine predicates with `and()` and `or()`
- Chain Consumers with `andThen()`

## Common Pitfalls
- Don't use `forEach` with side effects on shared state
- Don't modify external variables from lambdas
- Don't forget that streams are single-use
- Don't use `parallelStream()` without measuring performance gain
- Avoid `collect(toList())` when `toList()` suffices (Java 16+)
