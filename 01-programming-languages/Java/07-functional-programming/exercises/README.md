# Functional Programming Exercises

Practice exercises covering Java's functional programming features: lambdas, streams, Optional, and function composition.

---

## Lambdas (3 Exercises)

### Exercise 1: Custom Sort Comparator

**Problem:** Create lambda expressions to sort a list of `Person` objects by different criteria: by name alphabetically, by age, and by name length. Use `Comparator.comparing()` with method references and lambdas.

**Expected Behavior:**
```java
List<Person> people = List.of(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
);

people.sort(Comparator.comparing(Person::getName));
// [Alice(30), Bob(25), Charlie(35)]

people.sort(Comparator.comparingInt(Person::getAge).reversed());
// [Charlie(35), Alice(30), Bob(25)]

people.sort(Comparator.comparingInt(p -> p.getName().length()));
// [Bob(25), Alice(30), Charlie(35)]
```

**Implementation Hints:**
- Use `Comparator.comparing()` with method references
- Chain comparators with `.thenComparing()`
- Use `.reversed()` for descending order

**Solution Reference:** `LambdaExercises.java` — method `sortWithComparator()`

---

### Exercise 2: Filter Even Numbers

**Problem:** Write a method that filters a list of integers, keeping only even numbers. Implement using both a traditional loop and a lambda expression, then compare the approaches.

**Expected Behavior:**
```java
filterEven(List.of(1, 2, 3, 4, 5, 6, 7, 8)) -> [2, 4, 6, 8]
filterEven(List.of(1, 3, 5, 7)) -> []
filterEven(List.of(2, 4, 6)) -> [2, 4, 6]
```

**Implementation Hints:**
- Lambda approach: `list.stream().filter(n -> n % 2 == 0).collect(Collectors.toList())`
- Consider using `Predicate<Integer>` for reusability
- Method reference version: `n -> n % 2 == 0` can be extracted

**Solution Reference:** `LambdaExercises.java` — method `filterEvenNumbers()`

---

### Exercise 3: Transform List

**Problem:** Create a method that transforms a list of strings to their uppercase versions using a lambda. Then extend it to accept a generic transformation function.

**Expected Behavior:**
```java
transformToUpperCase(List.of("hello", "world")) -> ["HELLO", "WORLD"]

transform(List.of(1, 2, 3, 4), n -> n * 2) -> [2, 4, 6, 8]
transform(List.of("a", "bb", "ccc"), String::length) -> [1, 2, 3]
```

**Implementation Hints:**
- Use `Function<T, R>` as the transformation parameter
- Apply with `list.stream().map(transformation).collect(...)`
- Method references work well for simple transformations

**Solution Reference:** `LambdaExercises.java` — method `transformList()`

---

## Streams (3 Exercises)

### Exercise 4: Calculate Average

**Problem:** Given a list of student grades, calculate the average using streams. Handle edge cases like empty lists.

**Expected Behavior:**
```java
averageGrade(List.of(85, 90, 78, 92, 88)) -> 86.6
averageGrade(List.of(100, 100)) -> 100.0
averageGrade(List.of()) -> 0.0  // or handle with Optional
```

**Implementation Hints:**
- Use `stream().mapToInt().average()`
- Returns `OptionalDouble` — handle with `.orElse(0.0)`
- For Integer list: `list.stream().mapToInt(Integer::intValue).average()`

**Solution Reference:** `StreamExercises.java` — method `calculateAverage()`

---

### Exercise 5: Grouping

**Problem:** Given a list of `Order` objects, group them by status (PENDING, SHIPPED, DELIVERED) and count orders in each group.

**Expected Behavior:**
```java
List<Order> orders = List.of(
    new Order("001", Status.PENDING),
    new Order("002", Status.SHIPPED),
    new Order("003", Status.PENDING),
    new Order("004", Status.DELIVERED),
    new Order("005", Status.SHIPPED)
);

groupByStatus(orders) -> {
    PENDING: [Order(001), Order(003)],
    SHIPPED: [Order(002), Order(005)],
    DELIVERED: [Order(004)]
}

countByStatus(orders) -> {PENDING: 2, SHIPPED: 2, DELIVERED: 1}
```

**Implementation Hints:**
- Use `Collectors.groupingBy()` with `Order::getStatus`
- For counting: `Collectors.groupingBy(Order::getStatus, Collectors.counting())`
- Can chain with `Collectors.toMap()` for custom results

**Solution Reference:** `StreamExercises.java` — method `groupOrdersByStatus()`

---

### Exercise 6: flatMap

**Problem:** Given a list of sentences, use flatMap to extract all unique words across all sentences.

**Expected Behavior:**
```java
List<String> sentences = List.of(
    "hello world",
    "hello java",
    "java streams"
);

extractUniqueWords(sentences) -> [hello, world, java, streams]
```

**Implementation Hints:**
- Split each sentence into words: `sentence.split(" ")`
- Use `flatMap(Arrays::stream)` to flatten
- Collect with `Collectors.toSet()` for uniqueness

**Solution Reference:** `StreamExercises.java` — method `extractUniqueWords()`

---

## Optional (3 Exercises)

### Exercise 7: Safe Navigation

**Problem:** Create a method chain that safely navigates a nested object graph using Optional. Avoid null checks and NullPointerExceptions.

**Expected Behavior:**
```java
Company company = new Company("Acme", 
    new Department("Engineering", 
        new Employee("Alice", "lead")));

getEmployeeName(company) -> "Alice"
getEmployeeName(nullCompany) -> "Unknown"

getDepartmentHead(company, "Engineering") -> "Alice"
getDepartmentHead(company, "Marketing") -> "No head assigned"
```

**Implementation Hints:**
- Wrap nullable values in `Optional.ofNullable()`
- Use `.map()` for transformations
- Use `.orElse()` / `.orElseGet()` for defaults
- Chain with `.flatMap()` for nested Optionals

**Solution Reference:** `OptionalExercises.java` — method `safeNavigation()`

---

### Exercise 8: Default Values

**Problem:** Write methods that return default values when data is missing, using Optional to handle absent values gracefully.

**Expected Behavior:**
```java
getDisplayName(Optional.empty()) -> "Anonymous"
getDisplayName(Optional.of("Alice")) -> "Alice"

getPrice(Optional.empty()) -> 0.0
getPrice(Optional.of(99.99)) -> 99.99

getConfigValue("timeout", Optional.empty()) -> 30  // default
getConfigValue("timeout", Optional.of(60)) -> 60
```

**Implementation Hints:**
- `optional.orElse(defaultValue)` for simple defaults
- `optional.orElseGet(() -> computeDefault())` for lazy computation
- `optional.orElseThrow(() -> new Exception())` when default not acceptable

**Solution Reference:** `OptionalExercises.java` — method `defaultValueDemo()`

---

### Exercise 9: Chaining with Optional

**Problem:** Chain multiple Optional operations to safely process data that might be absent at various stages.

**Expected Behavior:**
```java
processUserEmail(Optional.of("user@example.com")) -> "USER@EXAMPLE.COM"
processUserEmail(Optional.empty()) -> "NO EMAIL"
processUserEmail(Optional.of("  ")) -> "INVALID EMAIL"

validateAndProcess(Optional.of("valid@email.com")) -> "Processed: valid@email.com"
validateAndProcess(Optional.of("")) -> "Invalid input"
validateAndProcess(Optional.empty()) -> "No input provided"
```

**Implementation Hints:**
- Chain `.filter()`, `.map()`, `.flatMap()` in sequence
- Use `.filter()` to validate intermediate results
- End with `.orElse()` or `.orElseGet()` for final default

**Solution Reference:** `OptionalExercises.java` — method `chainedProcessing()`

---

## Composition (1 Exercise)

### Exercise 10: Pipeline Builder

**Problem:** Create a pipeline builder that composes multiple functions into a processing chain. Each function transforms the input, and the pipeline applies them in sequence.

**Expected Behavior:**
```java
Pipeline<String, String> pipeline = Pipeline.of(String::trim)
    .add(s -> s.toLowerCase())
    .add(s -> s.replaceAll("[^a-z ]", ""))
    .add(s -> s.replaceAll("\\s+", " "));

pipeline.execute("  Hello, World!  ") -> "hello world"

Pipeline<Integer, Integer> mathPipeline = Pipeline.of(n -> n * 2)
    .add(n -> n + 10)
    .add(n -> n / 2);

mathPipeline.execute(5) -> 10  // (5*2 + 10) / 2 = 10
```

**Classes to Create:**
- `Pipeline<I, O>` — generic pipeline builder with compose functionality

**Implementation Hints:**
- Use `Function<I, O>` internally
- Chain with `Function.andThen()` or manual composition
- Type parameters ensure type safety across pipeline stages
- Store functions in a list and apply sequentially

**Solution Reference:** `CompositionExercises.java` — class `Pipeline`

---

## Solutions

All solutions are provided in the `solutions/` directory with complete implementations and test cases.

```bash
javac -d out exercises/functional/*.java
java -cp out exercises.functional.LambdaExercises
```
