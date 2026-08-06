# Java 8 Deep Dive

## Why Lambdas Were Introduced

### The Problem Java 8 Solved

Before Java 8, creating anonymous inner classes was verbose and cumbersome:

```java
// Pre-Java 8: Anonymous inner class
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

### The Functional Programming Revolution

Java 8 introduced lambdas to address several issues:

1. **Verbosity**: Anonymous classes required boilerplate code
2. **Lack of First-Class Functions**: No way to pass behavior as data
3. **Parallel Processing**: Difficulty leveraging multi-core processors
4. **External Iteration**: Only `for` loops, no declarative processing

### Lambda Syntax

```java
// Traditional anonymous class
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda expression
Runnable r2 = () -> System.out.println("Hello");

// With parameters
Comparator<String> comp = (s1, s2) -> s1.compareTo(s2);

// With block body
Function<String, Integer> length = s -> {
    int len = s.length();
    return len;
};
```

### Functional Interfaces

Lambdas work with functional interfaces (interfaces with single abstract method):

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}

// Built-in functional interfaces
// Predicate<T> - T -> boolean
// Function<T,R> - T -> R
// Consumer<T> - T -> void
// Supplier<T> - () -> T
```

### Real-World Impact

- **Collections**: `forEach()`, `removeIf()`, `replaceAll()`
- **Streams**: Declarative data processing
- **Parallel Execution**: `parallelStream()`
- **Optional**: Null safety

---

## How Streams Work Internally

### Stream Pipeline Architecture

Streams use a three-stage pipeline:

```
Source -> Intermediate Operations -> Terminal Operation
```

### Source Creation

```java
// From collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();

// From array
Stream<int[]> arrayStream = Arrays.stream(new int[]{1, 2, 3});

// From values
Stream<String> valueStream = Stream.of("a", "b", "c");

// From files
Stream<String> lines = Files.lines(Path.of("file.txt"));
```

### Intermediate Operations (Lazy)

```java
// map - Transform elements
Stream<String> mapped = stream.map(String::toUpperCase);

// filter - Select elements
Stream<String> filtered = stream.filter(s -> s.startsWith("a"));

// flatMap - Flatten nested structures
Stream<String> flatMapped = stream.flatMap(s -> Arrays.stream(s.split("")));

// distinct - Remove duplicates
Stream<String> distinct = stream.distinct();

// sorted - Order elements
Stream<String> sorted = stream.sorted();

// peek - Debug stream
Stream<String> peeked = stream.peek(System.out::println);
```

### Terminal Operations (Eager)

```java
// collect - Accumulate results
List<String> result = stream.collect(Collectors.toList());

// forEach - Process each element
stream.forEach(System.out::println);

// reduce - Combine elements
Optional<String> concatenated = stream.reduce(String::concat);

// count - Count elements
long count = stream.count();

// findFirst - Find first element
Optional<String> first = stream.findFirst();

// anyMatch, allMatch, noneMatch - Testing
boolean hasA = stream.anyMatch(s -> s.startsWith("a"));
```

### Internal Implementation

```java
// Stream interface (simplified)
public interface Stream<T> {
    // Intermediate operations return new Stream
    Stream<T> filter(Predicate<? super T> predicate);
    <R> Stream<R> map(Function<? super T, ? extends R> mapper);
    
    // Terminal operations trigger processing
    void forEach(Consumer<? super T> action);
    <R, A> R collect(Collector<? super T, A, R> collector);
}
```

### Lazy Evaluation

```java
// Nothing happens until terminal operation
Stream<String> s = list.stream()
    .filter(s -> {
        System.out.println("Filtering: " + s);
        return s.length() > 3;
    })
    .map(s -> {
        System.out.println("Mapping: " + s);
        return s.toUpperCase();
    });

// Processing starts here
List<String> result = s.collect(Collectors.toList());
// Output: Filtering: a, Filtering: b, Mapping: abcd, ...
```

### Short-Circuit Operations

```java
// Stop processing early
Optional<String> first = stream
    .filter(s -> s.startsWith("a"))
    .findFirst();

// Take first 5 elements
List<String> limited = stream
    .limit(5)
    .collect(Collectors.toList());
```

### Parallel Streams

```java
// Parallel processing
List<String> parallelResult = list.parallelStream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Fork/Join framework under the hood
// Uses common pool by default
```

### Performance Considerations

1. **Boxing/Unboxing**: `IntStream` avoids wrapper overhead
2. **Stateful Operations**: `distinct()`, `sorted()` require buffering
3. **Parallel Overhead**: Not always faster for small datasets
4. **Source Spliterator**: Affects parallel performance

---

## Optional Design Decisions

### Why Optional Was Created

Java's null reference was called the "billion-dollar mistake" by Tony Hoare. Optional was designed to:

1. **Explicit Null Handling**: Force developers to consider absence
2. **Readable Code**: `Optional<String>` vs. `@Nullable String`
3. **Chain Operations**: Fluent API for handling optional values
4. **Documentation**: Self-documenting code

### Optional API

```java
// Creating Optional
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("value");
Optional<String> nullable = Optional.ofNullable(null);

// Using Optional
String result = nullable.orElse("default");
String result2 = nullable.orElseGet(() -> computeDefault());
String result3 = nullable.orElseThrow(() -> new Exception());

// Chaining
String upper = Optional.of("hello")
    .map(String::toUpperCase)
    .orElse("DEFAULT");

// Filtering
Optional<String> filtered = Optional.of("hello")
    .filter(s -> s.length() > 3);

// Flat mapping
Optional<Integer> length = Optional.of("hello")
    .flatMap(s -> Optional.of(s.length()));
```

### Design Controversies

**Pros:**
- Forces null checks
- Chainable API
- Self-documenting

**Cons:**
- Not serializable
- Can be misused (optional fields, parameters, return types)
- Performance overhead
- Not a replacement for all null checks

### Best Practices

```java
// GOOD: Return type
public Optional<User> findUser(String id) { ... }

// BAD: Parameter type
public void process(Optional<String> value) { ... }

// BAD: Field type
private Optional<String> name; // Use null instead

// GOOD: Nullable field handling
public Optional<String> getName() {
    return Optional.ofNullable(this.name);
}
```

### Integration with Streams

```java
Optional<User> user = users.stream()
    .filter(u -> u.getId().equals(id))
    .findFirst();

user.map(User::getName)
    .ifPresent(name -> System.out.println("Found: " + name));
```

---

## Default Methods Controversy

### The Problem

Java interfaces couldn't evolve without breaking implementations:

```java
// Pre-Java 8
public interface Collection {
    boolean add(E e);
    // Adding new method breaks all implementations
}
```

### The Solution

```java
// Java 8 default methods
public interface Collection {
    boolean add(E e);
    
    default void forEach(Consumer<? super E> action) {
        for (E e : this) {
            action.accept(e);
        }
    }
}
```

### Controversy Points

1. **Multiple Inheritance**: Default methods enable multiple inheritance
2. **Diamond Problem**: What happens with conflicting defaults?
3. **Interface Evolution**: Is it appropriate for interfaces to have state?
4. **Compatibility**: Breaking changes for existing implementations

### Diamond Problem Resolution

```java
public interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

public interface B extends A {
    default void hello() {
        System.out.println("Hello from B");
    }
}

public class C implements A, B {
    // Must override to resolve conflict
    @Override
    public void hello() {
        B.super.hello(); // Explicit choice
    }
}
```

### Static Methods in Interfaces

```java
public interface Util {
    static String format(String s) {
        return s.toUpperCase();
    }
}

// Usage
String result = Util.format("hello");
```

### Private Methods in Interfaces (Java 9)

```java
public interface Logger {
    default void log(String message) {
        if (isLoggable(message)) {
            doLog(format(message));
        }
    }
    
    private boolean isLoggable(String message) {
        return message != null;
    }
    
    private String format(String message) {
        return message.toUpperCase();
    }
    
    void doLog(String message);
}
```

### Migration Impact

- **Existing Code**: No breaking changes
- **New Methods**: Can add default methods to existing interfaces
- **Implementation Choice**: Classes can override defaults


---

**Continue to Part 2**: README-part2.md