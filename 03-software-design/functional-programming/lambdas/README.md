# Lambdas

Lambdas are anonymous functions that enable functional programming by treating behavior as first-class values. They concise express instances of single-method interfaces (functional interfaces).

## Table of Contents

1. [Concepts](#concepts)
2. [Lambda Syntax](#lambda-syntax)
3. [Closures](#closures)
4. [Variable Capture](#variable-capture)
5. [Scoping Rules](#scoping-rules)
6. [Method References](#method-references)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Lambda?

A lambda is a concise way to represent an anonymous function - a function without a name that can be passed around.

```java
// Traditional anonymous class
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda equivalent
Runnable r2 = () -> System.out.println("Hello");
```

### Functional Interface Requirement

Lambdas can only be used where a functional interface is expected.

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);  // Single abstract method
}

// Lambda assigned to functional interface
Transformer<String, Integer> length = s -> s.length();
```

---

## Lambda Syntax

### Basic Forms

```java
// No parameters
Runnable noArgs = () -> System.out.println("No args");

// Single parameter (parentheses optional)
Consumer<String> single = s -> System.out.println(s);
Consumer<String> singleExplicit = (String s) -> System.out.println(s);

// Multiple parameters
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
BiFunction<Integer, Integer, Integer> addExplicit = (Integer a, Integer b) -> a + b;

// No return value (statement lambda)
Consumer<String> printer = s -> System.out.println(s);

// With return value (expression lambda)
Function<String, Integer> length = s -> s.length();
Function<String, Integer> lengthExplicit = s -> { return s.length(); };
```

### Complex Lambdas

```java
// Multi-line lambda
Function<String, String> process = input -> {
    String trimmed = input.trim();
    String lower = trimmed.toLowerCase();
    return lower.replaceAll("\\s+", " ");
};

// Lambda with type declarations
Comparator<String> compare = (String a, String b) -> a.compareTo(b);

// Lambda returning a lambda
Function<Integer, Function<Integer, Integer>> curriedAdd = 
    a -> b -> a + b;
```

### Lambda with Collections

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");

// forEach
names.forEach(name -> System.out.println(name));

// filter
List<String> longNames = names.stream()
    .filter(name -> name.length() > 4)
    .toList();

// map
List<Integer> lengths = names.stream()
    .map(String::length)
    .toList();

// sort
List<String> sorted = names.stream()
    .sorted((a, b) -> a.length() - b.length())
    .toList();

// reduce
int totalLength = names.stream()
    .map(String::length)
    .reduce(0, Integer::sum);
```

---

## Closures

### Basic Closure

```java
// Lambda capturing external variable
public Runnable createGreeting(String name) {
    return () -> System.out.println("Hello, " + name);
    // 'name' is captured from the enclosing scope
}

// Usage
Runnable greetAlice = createGreeting("Alice");
greetAlice.run();  // "Hello, Alice"
```

### Closure with Mutable State

```java
// Counter using closure
public Supplier<Integer> createCounter() {
    int[] count = {0};  // Array to allow mutation
    return () -> count[0]++;
}

// Usage
Supplier<Integer> counter = createCounter();
System.out.println(counter.get());  // 0
System.out.println(counter.get());  // 1
System.out.println(counter.get());  // 2
```

### Closure in Loop

```java
// PROBLEM: Closure captures variable, not value
List<Runnable> runnables = new ArrayList<>();
for (int i = 0; i < 5; i++) {
    runnables.add(() -> System.out.println(i));
}
// All print 5! The closure captures 'i', not its value at creation time.

// SOLUTION 1: Create new variable in each iteration
for (int i = 0; i < 5; i++) {
    final int j = i;  // New variable per iteration
    runnables.add(() -> System.out.println(j));
}

// SOLUTION 2: Use forEach with effectively final parameter
IntStream.range(0, 5).forEach(i -> {
    runnables.add(() -> System.out.println(i));
});
```

---

## Variable Capture

### Effectively Final Variables

```java
public void example() {
    String message = "Hello";  // Effectively final
    int count = 10;            // Effectively final

    Runnable r = () -> {
        System.out.println(message);  // OK - effectively final
        System.out.println(count);    // OK - effectively final
    };

    // message = "World";  // Would make it not effectively final
}

// Effectively final = never reassigned after initialization
```

### Capturing Different Types

```java
// Capturing primitive
public Consumer<Integer> createAdder(int base) {
    return n -> System.out.println(base + n);
}

// Capturing object reference
public <T> Supplier<T> createSupplier(T value) {
    return () -> value;
}

// Capturing this
public class Counter {
    private int count = 0;

    public Runnable createIncrementer() {
        return () -> count++;  // Captures 'this'
    }
}
```

### Capturing in Nested Lambdas

```java
public void nestedCapture() {
    String outer = "outer";

    Runnable r = () -> {
        String inner = "inner";

        Runnable nested = () -> {
            System.out.println(outer);  // OK
            System.out.println(inner);  // OK
        };

        nested.run();
    };
}
```

---

## Scoping Rules

### Lexical Scoping

```java
public class ScopeExample {
    private String instanceVar = "instance";

    public void example() {
        String localVar = "local";

        // Lambda sees enclosing scope
        Runnable r = () -> {
            System.out.println(instanceVar);  // OK
            System.out.println(localVar);     // OK
        };
    }
}

// Shadowing rules
public class ShadowExample {
    private String x = "instance";

    public void example() {
        String x = "local";  // Shadows instance variable

        Runnable r = () -> {
            System.out.println(x);  // Prints "local"
        };
    }
}
```

### Variable Scope Rules

```java
public void scopeRules() {
    // 1. Can capture effectively final local variables
    String local = "captured";
    Runnable r1 = () -> System.out.println(local);

    // 2. Cannot capture non-final/non-effectively-final variables
    int mutable = 0;
    // Runnable r2 = () -> System.out.println(mutable);  // Compile error if mutable changes
    mutable = 1;

    // 3. Can capture instance/static fields
    // (they're accessed through 'this' or class reference)

    // 4. Cannot declare local variables that shadow
    // Runnable r3 = () -> {
    //     int local = 5;  // Compile error - already defined in scope
    // };
}

// Lambda parameter scoping
Consumer<String> consumer = s -> {
    // 's' is in scope here
    System.out.println(s);
};

// Different lambdas can use same parameter name
Consumer<String> c1 = s -> System.out.println(s);
Consumer<String> c2 = s -> System.out.println(s);  // OK - different scope
```

---

## Method References

### Types of Method References

```java
// 1. Static method reference
Function<String, Integer> parseInt = Integer::parseInt;
// Equivalent to: s -> Integer.parseInt(s)

// 2. Bound method reference (specific instance)
String str = "hello";
Supplier<Integer> lengthSupplier = str::length;
// Equivalent to: () -> str.length()

// 3. Unbound method reference (first parameter is receiver)
BiFunction<String, Integer, Character> charAt = String::charAt;
// Equivalent to: (s, i) -> s.charAt(i)

// 4. Constructor reference
Supplier<ArrayList<String>> listFactory = ArrayList::new;
// Equivalent to: () -> new ArrayList<>()

Function<Integer, ArrayList<String>> sizedList = ArrayList::new;
// Equivalent to: n -> new ArrayList<>(n)
```

### Practical Examples

```java
// Sorting with method reference
List<String> names = List.of("Charlie", "Alice", "Bob");
List<String> sorted = names.stream()
    .sorted(String::compareToIgnoreCase)
    .toList();

// Mapping with constructor reference
List<String> strings = List.of("1", "2", "3");
List<Integer> integers = strings.stream()
    .map(Integer::parseInt)
    .toList();

// Filtering with method reference
List<String> nonEmpty = List.of("a", "", "b", "", "c").stream()
    .filter(Predicate.not(String::isEmpty))
    .toList();

// Grouping
Map<Integer, List<String>> grouped = names.stream()
    .collect(Collectors.groupingBy(String::length));
```

---

## Best Practices

### Do

```java
// 1. Keep lambdas short
list.forEach(item -> process(item));

// 2. Use method references when clearer
list.stream()
    .map(String::toUpperCase)
    .toList();

// 3. Name complex lambdas
Predicate<User> isAdult = user -> user.getAge() >= 18;
list.stream().filter(isAdult).toList();

// 4. Use descriptive parameter names
BiFunction<User, String, Boolean> hasRole = (user, role) -> 
    user.getRoles().contains(role);
```

### Don't

```java
// 1. Don't create multi-line lambdas (extract method)
// BAD:
list.stream().map(item -> {
    int x = item.getValue();
    int y = transform(x);
    return y * 2;
}).toList();

// GOOD:
list.stream().map(this::transformAndDouble).toList();

// 2. Don't use lambdas for trivial operations
// BAD:
list.forEach(item -> System.out.println(item));
// GOOD:
list.forEach(System.out::println);

// 3. Don't capture mutable variables
// BAD:
int count = 0;
list.forEach(item -> count++);  // Compile error
// GOOD:
int count = list.size();  // Or use reduce/collect
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Lambda** | Anonymous function implementing functional interface |
| **Syntax** | `(params) -> expression` or `(params) -> { statements }` |
| **Closure** | Lambda capturing variables from enclosing scope |
| **Effectively Final** | Variables captured must not be reassigned |
| **Method Reference** | Shorthand for simple lambdas (`Class::method`) |
| **Scoping** | Lambdas have access to enclosing scope variables |
| **Functional Interface** | Required target type for lambdas |
| **Conciseness** | Replace anonymous classes with lambdas |
| **Readability** | Keep lambdas short and use method references |
