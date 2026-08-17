# Lambda Expressions in Stream Context

## What Are Lambda Expressions

Lambda expressions, introduced in Java 8, are anonymous functions that provide a concise way to implement functional interfaces. In the context of stream operations, lambdas serve as the behavioral parameters that drive filtering, transformation, and aggregation operations.

Before lambdas, Java relied on anonymous inner classes, which required verbose boilerplate code. Lambdas eliminate this verbosity while maintaining full type safety.

## Why Lambda

- **Concise**: One line replaces ten lines of anonymous class boilerplate
- **Readable**: Code reads like natural language (`filter(x -> x > 5)`)
- **Functional**: Pass behavior as a parameter to methods
- **Stream integration**: Essential for `filter()`, `map()`, `reduce()`, `forEach()`, and all stream intermediate and terminal operations
- **Parallel-friendly**: Stateless lambdas enable safe parallel stream processing
- **Testable**: Behavior can be passed as a parameter, making testing easier
- **Composable**: Functions and predicates can be combined into complex operations

## Syntax Variations

```java
// Full syntax with explicit types and block body
(String s) -> { return s.length(); }

// Single parameter, inferred type, expression body
s -> s.length()

// Single parameter with type, expression body
(String s) -> s.length()

// Multiple parameters
(a, b) -> a + b

// No parameters
() -> System.out.println("hello")

// Block body with multiple statements
(x, y) -> {
    int sum = x + b;
    System.out.println("Sum: " + sum);
    return sum;
}

// Returning a lambda from a method
Function<Integer, Integer> createMultiplier(int factor) {
    return x -> x * factor;
}
```

## Variable Capture

Lambdas can capture variables from the enclosing scope, but those variables must be effectively final. A variable is effectively final if it is never reassigned after initialization.

```java
int factor = 10;
Function<Integer, Integer> multiplier = x -> x * factor; // OK
factor = 20; // Error: cannot reassign effectively final variable

// Effectively final - never reassigned
final int constant = 42;
Function<Integer, Integer> addConstant = x -> x + constant; // OK

// Effectively final in loop
for (int i = 0; i < 5; i++) {
    final int captured = i; // effectively final within each iteration
    Supplier<Integer> supplier = () -> captured;
}
```

## Functional Interfaces

Functional interfaces have exactly one abstract method and serve as the target type for lambda expressions. The `@FunctionalInterface` annotation is optional but recommended for compile-time verification.

| Interface | Signature | Use Case |
|-----------|-----------|----------|
| `Predicate<T>` | `T → boolean` | Filtering elements |
| `Function<T, R>` | `T → R` | Transforming elements |
| `Consumer<T>` | `T → void` | Performing side effects |
| `Supplier<T>` | `() → T` | Creating values |
| `UnaryOperator<T>` | `T → T` | Transform same type |
| `BinaryOperator<T>` | `(T, T) → T` | Combine two values |
| `BiPredicate<T, U>` | `(T, U) → boolean` | Two-argument predicate |
| `BiFunction<T, U, R>` | `(T, U) → R` | Two-argument function |
| `BiConsumer<T, U>` | `(T, U) → void` | Two-argument consumer |

## Method References

Method references are shorthand for lambdas that simply call an existing method. They improve readability when the lambda body is a single method call.

| Type | Syntax | Equivalent Lambda |
|------|--------|-------------------|
| Static | `Integer::parseInt` | `x -> Integer.parseInt(x)` |
| Instance | `str::length` | `() -> str.length()` |
| Arbitrary | `String::compareToIgnoreCase` | `(s1, s2) -> s1.compareToIgnoreCase(s2)` |
| Constructor | `ArrayList::new` | `() -> new ArrayList()` |

## Lambda in Stream Operations

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Filter, transform, and collect
List<String> result = names.stream()
    .filter(name -> name.length() > 3)           // Predicate lambda
    .map(String::toUpperCase)                     // Method reference
    .sorted()                                     // Natural order
    .collect(Collectors.toList());                // Terminal operation

// Reduce to sum
int sum = IntStream.rangeClosed(1, 100)
    .reduce(0, (a, b) -> a + b);

// forEach with consumer
names.forEach(name -> System.out.println(name));

// Collect to map
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(n -> n, String::length));

// FlatMap for nested structures
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2), Arrays.asList(3, 4)
);
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());

// Distinct and count
long distinctCount = names.stream()
    .map(String::toLowerCase)
    .distinct()
    .count();
```

## Ternary Operator in Lambda

```java
list.stream()
    .map(x -> x > 5 ? "big" : "small")
    .collect(Collectors.toList());
```

## Composing Functions

Function composition allows building complex transformations from simple ones using `andThen()` and `compose()`.

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

// andThen: apply this first, then other
Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
doubleThenAdd.apply(5); // 20

// compose: apply other first, then this
Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
addThenDouble.apply(5); // 30

// Chain multiple transformations
Function<String, String> normalize = String::trim
    .andThen(String::toLowerCase)
    .andThen(s -> s.replaceAll("\\s+", " "));
normalize.apply("  Hello   World  "); // "hello world"

// Identity function
Function<String, String> identity = Function.identity();
identity.apply("hello"); // "hello"
```

## Composing Predicates

Predicates support logical operations through `and()`, `or()`, and `negate()` methods.

```java
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> hasLengthFour = s -> s.length() == 4;

// AND - both conditions must be true
Predicate<String> startsWithAAndLengthFour = startsWithA.and(hasLengthFour);

// OR - either condition can be true
Predicate<String> startsWithAOrLengthFour = startsWithA.or(hasLengthFour);

// NEGATE - inverts the result
Predicate<String> notStartsWithA = startsWithA.negate();

// Chain multiple conditions
Predicate<Integer> inRange = n -> n >= 1 && n <= 100;
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> evenInRange = inRange.and(isEven);
```

## Composing Consumers

Consumers can be chained using `andThen()` to perform multiple operations in sequence.

```java
Consumer<String> log = s -> System.out.println("[LOG] " + s);
Consumer<String> validate = s -> {
    if (s == null) throw new IllegalArgumentException("null value");
};
Consumer<String> process = validate.andThen(log);

// Process multiple items
Arrays.asList("a", "b", "c").forEach(process);
```

## Stream Operations with Lambdas

### Intermediate Operations (Lazy)

```java
.filter(x -> x > 5)          // Predicate
.map(x -> x * 2)             // Function
.flatMap(x -> x.stream())    // Function returning Stream
.distinct()                   // No lambda needed
.sorted()                     // Natural order
.sorted(Comparator.reverseOrder())  // Custom comparator
.limit(10)                    // No lambda needed
.skip(5)                      // No lambda needed
.peek(x -> System.out.println(x))  // Consumer for debugging
```

### Terminal Operations (Eager)

```java
.forEach(x -> System.out.println(x))   // Consumer
.reduce(0, (a, b) -> a + b)            // BinaryOperator
.collect(Collectors.toList())            // No lambda needed
.count()                                 // No lambda needed
.anyMatch(x -> x > 5)                  // Predicate
.allMatch(x -> x > 0)                  // Predicate
.noneMatch(x -> x < 0)                 // Predicate
.findFirst()                            // No lambda needed
.min(Comparator.naturalOrder())         // Optional Comparator
.max(Comparator.naturalOrder())         // Optional Comparator
```

## Creating Custom Functional Interfaces

```java
@FunctionalInterface
public interface Transformer<T> {
    T transform(T input);
    
    default Transformer<T> andThen(Transformer<T> after) {
        return input -> after.transform(this.transform(input));
    }
    
    static <T> Transformer<T> identity() {
        return input -> input;
    }
}

// Usage
Transformer<String> shout = s -> s.toUpperCase() + "!";
Transformer<String> whisper = s -> s.toLowerCase() + "...";
Transformer<String> shoutThenWhisper = shout.andThen(whisper);
```

## Best Practices

1. Keep lambdas short and focused — one operation per lambda
2. Prefer method references when they improve readability
3. Avoid mutating captured variables
4. Name complex predicates and functions for clarity
5. Use `@FunctionalInterface` annotation on custom interfaces
6. Be cautious with side effects in `forEach()` — prefer `peek()` for debugging
7. Prefer `collect()` over `reduce()` for accumulating into collections
8. Use `UnaryOperator<T>` instead of `Function<T, T>` for same-type transformations
9. Extract complex lambdas into named methods for better readability
10. Use `Predicate.not()` instead of `predicate.negate()` for clarity (Java 11+)

## Common Pitfalls

- Forgetting that captured variables must be effectively final
- Using `var` in lambda parameters (not supported in Java 8-9)
- Confusing `reduce()` (returns Optional) with `collect()` (returns collection)
- Overusing `peek()` — it is intended for debugging, not side effects
- Creating stateful lambdas that break parallel stream correctness
- Not handling `NullPointerException` in lambda bodies
- Using `forEach()` where `map()` + `collect()` would be more functional
- Creating overly complex single lambdas instead of composing small ones
- Forgetting that `reduce()` with identity must be associative for parallel streams
- Using `count()` on a stream when `size()` on the collection would suffice
