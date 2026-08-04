# Higher-Order Functions

Higher-order functions either take functions as arguments, return functions, or both. They enable powerful abstractions like callbacks, combinators, and functional composition.

## Table of Contents

1. [Concepts](#concepts)
2. [Functions as Values](#functions-as-values)
3. [Callbacks](#callbacks)
4. [Combinators](#combinators)
5. [Function Composition](#function-composition)
6. [Currying and Partial Application](#currying-and-partial-application)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Higher-Order Function?

A higher-order function operates on other functions. It can:
- Accept one or more functions as parameters
- Return a function as its result

```
┌─────────────────────────────────────────────────┐
│           Higher-Order Function                 │
│                                                 │
│  ┌─────────────────────────────────────────┐   │
│  │  Input Function(s)                      │   │
│  │  ┌─────────┐  ┌─────────┐              │   │
│  │  │  f(x)   │  │  g(x)   │              │   │
│  │  └────┬────┘  └────┬────┘              │   │
│  │       │            │                    │   │
│  │       ▼            ▼                    │   │
│  │  ┌─────────────────────────┐           │   │
│  │  │   Higher-Order Logic    │           │   │
│  │  └─────────────────────────┘           │   │
│  │            │                            │   │
│  │            ▼                            │   │
│  │  ┌─────────────────────────┐           │   │
│  │  │   Output Function       │           │   │
│  │  │   h(x) = f(x) + g(x)   │           │   │
│  │  └─────────────────────────┘           │   │
│  └─────────────────────────────────────────┘   │
└─────────────────────────────────────────────────┘
```

### Why Higher-Order Functions?

- **Abstraction** - encapsulate patterns of behavior
- **Reusability** - write generic algorithms
- **Flexibility** - customize behavior at runtime
- **Composability** - build complex behavior from simple parts

---

## Functions as Values

### Storing Functions

```java
// Functions as variables
Function<String, Integer> length = String::length;
Supplier<List<String>> listFactory = ArrayList::new;
Consumer<String> printer = System.out::println;

// Functions in collections
List<Function<Integer, Integer>> operations = List.of(
    n -> n + 1,
    n -> n * 2,
    n -> n * n
);

// Apply operations
int result = operations.stream()
    .reduce(Function.identity(), Function::andThen)
    .apply(5);  // ((5 + 1) * 2) ^ 2 = 144
```

### Returning Functions

```java
// Factory that returns a function
public static Function<Integer, Integer> createMultiplier(int factor) {
    return n -> n * factor;
}

// Usage
Function<Integer, Integer> double = createMultiplier(2);
Function<Integer, Integer> triple = createMultiplier(3);

System.out.println(double.apply(5));  // 10
System.out.println(triple.apply(5));  // 15

// Conditional function creation
public static <T> Predicate<T> createFilter(Predicate<T> condition, boolean negate) {
    return negate ? condition.negate() : condition;
}
```

---

## Callbacks

### Basic Callback Pattern

```java
// Callback interface
@FunctionalInterface
public interface Callback<T> {
    void onComplete(T result);
    default void onError(Throwable error) {
        error.printStackTrace();
    }
}

// Async operation with callback
public class DataLoader {
    public <T> void load(String url, Callback<T> callback) {
        // Simulate async operation
        CompletableFuture.supplyAsync(() -> {
            @SuppressWarnings("unchecked")
            T data = (T) fetchData(url);
            return data;
        }).whenComplete((result, error) -> {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onComplete(result);
            }
        });
    }
}

// Usage
DataLoader loader = new DataLoader();
loader.load("https://api.example.com/users", new Callback<List<User>>() {
    @Override
    public void onComplete(List<User> users) {
        System.out.println("Loaded " + users.size() + " users");
    }

    @Override
    public void onError(Throwable error) {
        System.err.println("Failed to load users: " + error.getMessage());
    }
});

// With lambda
loader.<List<User>>load("https://api.example.com/users",
    users -> System.out.println("Loaded " + users.size()),
    error -> System.err.println("Error: " + error)
);
```

### Event Handling

```java
// Event emitter with callbacks
public class EventEmitter<T> {
    private final Map<String, List<Consumer<T>>> listeners = new HashMap<>();

    public void on(String event, Consumer<T> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    public void emit(String event, T data) {
        List<Consumer<T>> eventListeners = listeners.getOrDefault(event, List.of());
        eventListeners.forEach(listener -> listener.accept(data));
    }
}

// Usage
EventEmitter<Order> orderEmitter = new EventEmitter<>();

orderEmitter.on("created", order -> 
    System.out.println("Order created: " + order.getId()));
orderEmitter.on("shipped", order -> 
    System.out.println("Order shipped: " + order.getId()));

orderEmitter.emit("created", new Order("123"));
```

---

## Combinators

### Function Combinators

```java
// Compose two functions
public static <T, U, V> Function<T, V> compose(
        Function<T, U> f, 
        Function<U, V> g) {
    return t -> g.apply(f.apply(t));
}

// Usage
Function<String, String> trim = String::trim;
Function<String, String> lower = String::toLowerCase;
Function<String, String> normalize = compose(trim, lower);

System.out.println(normalize.apply("  Hello  "));  // "hello"
```

### Predicate Combinators

```java
// Combine predicates
public static <T> Predicate<T> and(Predicate<T> a, Predicate<T> b) {
    return t -> a.test(t) && b.test(t);
}

public static <T> Predicate<T> or(Predicate<T> a, Predicate<T> b) {
    return t -> a.test(t) || b.test(t);
}

public static <T> Predicate<T> not(Predicate<T> predicate) {
    return t -> !predicate.test(t);
}

// Usage
Predicate<String> isNotNull = Objects::nonNull;
Predicate<String> isNotEmpty = s -> !s.isEmpty();
Predicate<String> isValid = and(isNotNull, isNotEmpty);

List<String> valid = List.of("a", null, "", "b", null).stream()
    .filter(isValid)
    .toList();  // ["a", "b"]
```

### Combinators for Configuration

```java
// Builder combinator pattern
public class PipelineBuilder<T> {
    private final List<Function<T, T>> stages = new ArrayList<>();

    public PipelineBuilder<T> addStage(Function<T, T> stage) {
        stages.add(stage);
        return this;
    }

    public Function<T, T> build() {
        return stages.stream()
            .reduce(Function.identity(), Function::andThen);
    }
}

// Usage
Function<String, String> pipeline = new PipelineBuilder<String>()
    .addStage(String::trim)
    .addStage(String::toLowerCase)
    .addStage(s -> s.replaceAll("\\s+", " "))
    .addStage(s -> s.replaceAll("[^a-z0-9 ]", ""))
    .build();
```

---

## Function Composition

### AndThen and Compose

```java
// andThen: apply this, then other
Function<Integer, Integer> addOne = n -> n + 1;
Function<Integer, Integer> doubleIt = n -> n * 2;

Function<Integer, Integer> addOneThenDouble = addOne.andThen(doubleIt);
System.out.println(addOneThenDouble.apply(3));  // 8 ((3+1)*2)

// compose: apply other, then this
Function<Integer, Integer> doubleThenAddOne = addOne.compose(doubleIt);
System.out.println(doubleThenAddOne.apply(3));  // 7 ((3*2)+1)
```

### Complex Composition

```java
// Composing multiple functions
public class FunctionComposition {
    @SafeVarargs
    public static <T> Function<T, T> compose(Function<T, T>... functions) {
        return Arrays.stream(functions)
            .reduce(Function.identity(), Function::andThen);
    }

    public static <T, U, V> Function<T, V> pipe(
            Function<T, U> first,
            Function<U, V> second) {
        return first.andThen(second);
    }
}

// Usage
Function<String, String> process = FunctionComposition.compose(
    String::trim,
    String::toLowerCase,
    s -> s.replaceAll("\\s+", "_")
);

System.out.println(process.apply("  Hello World  "));  // "hello_world"
```

### Pipe Operator

```java
// Simulating pipe operator
public static <T> T pipe(T value, Function<T, T>... functions) {
    return Arrays.stream(functions)
        .reduce(value, (v, f) -> f.apply(v), (a, b) -> b);
}

// Usage
String result = pipe("  Hello World  ",
    String::trim,
    String::toLowerCase,
    s -> s.replaceAll("\\s+", "_")
);
// "hello_world"
```

---

## Currying and Partial Application

### Currying

```java
// Convert multi-parameter function to chain of single-parameter functions
public class Curry {
    // Curry a two-parameter function
    public static <A, B, C> Function<A, Function<B, C>> curry(
            BiFunction<A, B, C> function) {
        return a -> b -> function.apply(a, b);
    }

    // Curry a three-parameter function
    public static <A, B, C, D> Function<A, Function<B, Function<C, D>>> curry3(
            TriFunction<A, B, C, D> function) {
        return a -> b -> c -> function.apply(a, b, c);
    }
}

// Usage
BiFunction<Integer, Integer, Integer> add = Integer::sum;
Function<Integer, Function<Integer, Integer>> curriedAdd = Curry.curry(add);

Function<Integer, Integer> add5 = curriedAdd.apply(5);
System.out.println(add5.apply(3));   // 8
System.out.println(add5.apply(10));  // 15
```

### Partial Application

```java
// Partial application - fix some arguments, leave others open
public class Partial {
    public static <A, B, C> Function<B, C> partialFirst(
            BiFunction<A, B, C> function, A fixed) {
        return b -> function.apply(fixed, b);
    }

    public static <A, B, C> Function<A, C> partialSecond(
            BiFunction<A, B, C> function, B fixed) {
        return a -> function.apply(a, fixed);
    }
}

// Usage
BiFunction<String, String, String> concat = String::concat;
Function<String, String> prependHello = Partial.partialFirst(concat, "Hello ");
System.out.println(prependHello.apply("World"));  // "Hello World"
```

---

## Best Practices

### Do

```java
// 1. Use higher-order functions for customization
public <T> List<T> filter(List<T> items, Predicate<T> predicate) {
    return items.stream().filter(predicate).toList();
}

// 2. Return functions for deferred computation
public Supplier<ExpensiveResult> createExpensiveOperation() {
    return () -> computeExpensiveResult();
}

// 3. Use combinators for complex logic
Predicate<User> isAdult = user -> user.getAge() >= 18;
Predicate<User> isActive = User::isActive;
Predicate<User> canAccess = isAdult.and(isActive);
```

### Don't

```java
// 1. Don't over-complicate simple operations
// BAD:
Function<String, String> process = s -> s.trim().toLowerCase();
// GOOD:
Function<String, String> process = s -> s.trim().toLowerCase();

// 2. Don't create deep composition chains
// BAD: Hard to read
Function<String, String> f = s -> s.trim();
Function<String, String> g = s -> s.toLowerCase();
Function<String, String> h = s -> s.replaceAll("\\s+", "_");
Function<String, String> i = s -> s.replaceAll("[^a-z0-9_]", "");
Function<String, String> chain = f.andThen(g).andThen(h).andThen(i);

// GOOD: Use meaningful names
Function<String, String> normalize = String::trim;
Function<String, String> lowercase = String::toLowerCase;
Function<String, String> slugify = s -> s.replaceAll("\\s+", "_");
Function<String, String> sanitize = s -> s.replaceAll("[^a-z0-9_]", "");
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Higher-Order Function** | Takes/returns functions |
| **Callbacks** | Functions passed for async completion |
| **Combinators** | Functions that combine other functions |
| **Composition** | Build complex functions from simple ones |
| **Currying** | Convert multi-arg to single-arg chain |
| **Partial Application** | Fix some arguments, leave others open |
| **andThen/Compose** | Standard composition methods |
| **Deferred Execution** | Return functions for later invocation |
| **Customization** | Use functions as strategy parameters |
