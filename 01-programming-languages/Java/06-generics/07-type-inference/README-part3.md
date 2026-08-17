# 07 - Type Inference (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

## Advanced Type Inference

### Diamond Operator (`<>`) Inference

Java 7 introduced the diamond operator, letting the compiler infer type arguments from the left-hand side:

```java
// Java 6: must repeat type arguments
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// Java 7+: compiler infers from the left
Map<String, List<Integer>> map = new HashMap<>();

// Works with nested generics too
List<Map<String, List<Integer>>> nested = new ArrayList<>();
```

**When inference fails with diamond:**

```java
// Error: cannot infer type arguments for ArrayList<>
List<String> list = new ArrayList<>();    // OK
var list2 = new ArrayList<>();            // Error: target type unknown

// Workaround: provide the type
var list3 = new ArrayList<String>();      // OK
```

### Target Type Inference

The compiler uses the target type (the expected type) to infer method type arguments:

```java
// Target type from variable declaration
List<String> names = List.of("Alice", "Bob");  // T inferred as String

// Target type from return statement
public List<Integer> getNumbers() {
    return List.of(1, 2, 3);  // T inferred as Integer
}

// Target type from method argument
public void process(List<String> items) { ... }
process(List.of("a", "b"));  // T inferred as String

// Target type from assignment
Object obj = "hello";
String s = (String) obj;     // No inference here — explicit cast
```

### Type Inference in Method Chains

Java 8+ enables inference through fluent API chains:

```java
// Stream API chain — each operation preserves type context
List<String> result = people.stream()
    .filter(p -> p.getAge() > 18)
    .map(Person::getName)
    .collect(Collectors.toList());

// Builder pattern with inferred types
List<String> names = Stream.<String>builder()
    .add("Alice")
    .add("Bob")
    .build()
    .collect(Collectors.toList());

// Without explicit type on builder, inference works from context
List<String> names = Stream.builder()
    .add("Alice")
    .add("Bob")
    .build()
    .collect(Collectors.toList());
```

### Lambda and Method Reference Inference

Java 8+ infers lambda parameter types from the functional interface:

```java
// Lambda parameter types inferred from Predicate<String>
Predicate<String> isLong = s -> s.length() > 5;  // s inferred as String

// Method reference — type inferred from context
Function<String, Integer> length = String::length;

// Chained method references
Comparator<String> cmp = Comparator.comparingInt(String::length)
    .thenComparing(Comparator.naturalOrder());

// Generic functional interface with inference
UnaryOperator<String> toUpper = String::toUpperCase;
Function<String, List<Character>> toChars = s -> s.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.toList());
```

### Inference with Bounded Wildcards

The compiler infers bounds when working with wildcards:

```java
// Capture conversion — compiler creates a fresh type variable
public static <T> void swap(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}

// Inference with upper bounds
public static <T extends Comparable<T>> T max(List<? extends T> list) {
    return list.stream().max(Comparator.naturalOrder()).orElse(null);
}

// Inference with lower bounds
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {
        dest.add(item);
    }
}
```

### Type Inference in Generic Classes

Constructors and static methods of generic classes benefit from inference:

```java
public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    // Static factory with inference
    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);  // <> infers A, B
    }
}

// Usage — types inferred from arguments
Pair<String, Integer> pair = Pair.of("age", 25);
// A inferred as String, B inferred as Integer
```

### Inference with Var (Java 10+)

The `var` keyword interacts with generic type inference:

```java
// var with generic types
var list = List.of(1, 2, 3);           // List<Integer>
var map = Map.of("key", "value");      // Map<String, String>
var pair = new Pair<>("hello", 42);    // Pair<String, Integer>

// var with streams
var result = Stream.of(1, 2, 3)
    .map(n -> n * 2)
    .collect(Collectors.toList());     // List<Integer>

// Limitation: cannot use var with anonymous classes
// var obj = new Comparable<String>() { ... };  // Error
```

### Wildcard Capture and Inference

The compiler uses capture conversion to infer types from wildcards:

```java
// Wildcard capture — the compiler creates a fresh type variable
public static void addStamp(List<? extends Number> list) {
    // Cannot directly: list.add(42);  — compile error
    // Must use a helper method with capture
    helper(list);
}

private static <T> void helper(List<T> list) {
    list.add(null);  // Now we can add null
    T item = list.get(0);
}

// Practical example: swapping elements in a wildcard list
public static void swap(List<?> list, int i, int j) {
    swapHelper(list, i, j);
}

private static <T> void swapHelper(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}
```

### Inference Across Method Overloads

When multiple overloads exist, the compiler uses return type and argument types together:

```java
// Overloaded methods
public static <T> List<T> asList(T a) { ... }
public static <T> List<T> asList(T a, T b) { ... }
public static <T> List<T> asList(T a, T b, T c) { ... }

// Inference picks the right overload based on arguments
List<Integer> one = asList(1);           // T = Integer
List<String> two = asList("a", "b");    // T = String

// Ambiguity can occur
public static <T> void process(T item) { ... }
public static <T> void process(T item, T other) { ... }

// When called with compatible types, compiler infers from context
process("hello");         // OK: T = String
process(1, 2);            // OK: T = Integer
```

---

## Java Version Improvements

### Java 7 — Diamond Operator

```java
// Before: redundant type arguments
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// After: diamond operator
Map<String, List<Integer>> map = new HashMap<>();
```

### Java 8 — Lambda Type Inference

```java
// Before: explicit parameter types required
Comparator<String> cmp = (String a, String b) -> a.compareTo(b);

// After: parameter types inferred
Comparator<String> cmp = (a, b) -> a.compareTo(b);
```

### Java 9 — Effectively Final Variables in Try-with-resources

```java
// Before: must be effectively final or declared in try
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // ...
}

// After (Java 9): effectively final variables work
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
try (br) {
    // ...
}
```

### Java 10 — Local Variable Type Inference (`var`)

```java
// Before
Map<String, List<Integer>> map = new HashMap<>();

// After
var map = new HashMap<String, List<Integer>>();
```

### Java 11 — Lambda Parameters with `var`

```java
// Before (Java 8+)
(x, y) -> x + y

// After (Java 11+)
(var x, var y) -> x + y
(var x, int y) -> x + y   // Can add annotations
```

---

## Common Inference Pitfalls

### 1. Inference Failure with Raw Types

```java
// Raw type breaks inference chain
List raw = new ArrayList();
raw.add("hello");
// String s = raw.get(0);  // Error: unchecked cast needed

// Fix: use parameterized types
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);    // OK: inference works
```

### 2. Inference with Multiple Bounds

```java
// Complex bounds can confuse inference
public static <T extends Comparable<T> & Serializable> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Sometimes you must help the compiler
<T extends Comparable<T> & Serializable> T safeMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### 3. Inference with Generics and Primitives

```java
// Autoboxing can interfere with inference
List<Integer> nums = List.of(1, 2, 3);     // OK
// List<int> nums = List.of(1, 2, 3);      // Error: no generic primitives

// Use wrapper types
Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87);
```

### 4. Inference with Wildcards in Return Types

```java
// Return type must match exactly
public static List<? extends Number> getNumbers() {
    return List.of(1, 2, 3);
}

// This doesn't work:
// List<Integer> nums = getNumbers();  // Error: wildcard mismatch

// Fix: use a type parameter
public static <T extends Number> List<T> getNumbers() {
    return (List<T>) List.of(1, 2, 3);
}
```

---

## Debugging Type Inference

### Compile-Time Errors

```java
// "incompatible types: inference variable T has incompatible bounds"
List<Number> nums = List.of(1, 2, 3);  // Error: Integer ≠ Number
// Fix:
List<Integer> nums = List.of(1, 2, 3);

// "cannot infer type-variable T"
// Usually means the compiler lacks enough context
// Fix: provide explicit type arguments
<T> T pick(List<T> list, int index);
String s = pick(List.of("a", "b"), 0);  // T inferred as String
```

### Using IDE Hints

Most IDEs show inferred types on hover:

```java
var list = List.of(1, 2, 3);
// Hover over 'list' → shows: List<Integer>

Function<String, Integer> len = String::length;
// Hover over 'len' → shows: Function<String, Integer>
```

---

## Quick Reference

| Feature | Java Version | Example |
|---------|-------------|---------|
| Diamond operator | 7+ | `new ArrayList<>()` |
| Lambda inference | 8+ | `(a, b) -> a + b` |
| Method reference | 8+ | `String::length` |
| `var` | 10+ | `var x = 42` |
| `var` in lambdas | 11+ | `(var x) -> x + 1` |

---

## Summary

- Type inference eliminates redundant type declarations
- The compiler uses **target type**, **argument types**, and **return type** context to infer generics
- Java 7+ diamond operator, Java 8+ lambdas, and Java 10+ `var` progressively reduce boilerplate
- Wildcard capture enables safe operations on wildcard-typed collections
- When inference fails, provide explicit type arguments to help the compiler

---

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)
