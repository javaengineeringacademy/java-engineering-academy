# Part 5: Functional Interfaces & Advanced Lambda Patterns

This guide covers the Java built-in functional interfaces, method reference syntax, ternary operator usage in lambdas, and stream operation types.

---

## 1. Each Functional Interface Explained

### Predicate\<T\> — "Does this pass the test?"

`Predicate<T>` takes a single argument and returns a boolean. It's used for filtering and conditional checks.

```java
Predicate<String> isLong = s -> s.length() > 10;
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<String> startsWithA = s -> s.startsWith("A");
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `test(T t)` | Evaluates predicate on argument | `isLong.test("Hello World")` → `true` |
| `and(Predicate other)` | Chain with AND logic | `isLong.and(startsWithA)` |
| `or(Predicate other)` | Chain with OR logic | `isLong.or(startsWithA)` |
| `negate()` | Inverts the result | `isLong.negate()` |
| `isEqual(Object target)` | Tests equality | `Predicate.isEqual("target")` |

```java
// Predicate chaining
Predicate<String> isLongAndStartsWithA = isLong.and(startsWithA);
Predicate<String> isLongOrStartsWithA = isLong.or(startsWithA);
Predicate<String> negateLong = isLong.negate();

// Real-world: Filter active users over 18
Predicate<User> isAdult = u -> u.getAge() >= 18;
Predicate<User> isActive = User::isActive;
Predicate<User> canAccess = isAdult.and(isActive);

List<User> allowed = users.stream()
    .filter(canAccess)
    .collect(Collectors.toList());
```

---

### Function\<T,R\> — "Transform this into that"

`Function<T,R>` takes an input of type T and returns an output of type R. It's the primary transformation interface.

```java
Function<String, Integer> toLength = String::length;
Function<Integer, String> toString = Object::toString;
Function<String, String> toUpper = String::toUpperCase;
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `apply(T t)` | Apply function to argument | `toLength.apply("Hello")` → `5` |
| `andThen(Function after)` | Chain forward | `toUpper.andThen(toLength)` |
| `compose(Function before)` | Chain backward | `toLength.compose(toUpper)` |
| `identity()` | Returns input unchanged | `Function.identity()` |

```java
// Function chaining — forward
Function<String, String> normalize = toUpper.andThen(s -> s.trim());
Function<String, Integer> lengthAfterUpper = toUpper.andThen(toLength);

// Function chaining — backward (compose)
Function<String, Integer> lengthThenUpper = toLength.compose(toUpper);
// This is equivalent to: toLength.apply(toUpper.apply(input))

// Real-world: DTO to Entity conversion
Function<UserDTO, User> toEntity = dto -> new User(
    dto.getId(),
    dto.getFirstName() + " " + dto.getLastName(),
    dto.getEmail().toLowerCase()
);

// Pipeline: parse string to int, double it, convert back to string
Function<String, Integer> parseInt = Integer::parseInt;
Function<Integer, Integer> doubleIt = n -> n * 2;
Function<Integer, String> backToString = Object::toString;

Function<String, String> process = parseInt.andThen(doubleIt).andThen(backToString);
// process.apply("21") → "42"
```

---

### Consumer\<T\> — "Do something with this"

`Consumer<T>` takes a single argument and returns nothing. It's used for side effects like logging, printing, or modifying state.

```java
Consumer<String> print = System.out::println;
Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
Consumer<List<Integer>> printAll = list -> list.forEach(System.out::println);
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `accept(T t)` | Execute the consumer | `print.accept("Hello")` |
| `andThen(Consumer after)` | Chain consumers | `print.andThen(printUpper)` |

```java
// Consumer chaining
Consumer<String> logAndPrint = print.andThen(printUpper);
logAndPrint.accept("hello");
// Output:
// hello
// HELLO

// Real-world: Process order and update inventory
Consumer<Order> validateOrder = order -> {
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order is empty");
    }
};

Consumer<Order> updateInventory = order -> {
    order.getItems().forEach(item -> {
        inventory.decrement(item.getProductId(), item.getQuantity());
    });
};

Consumer<Order> sendConfirmation = order -> {
    emailService.send(order.getCustomerEmail(), "Order confirmed");
};

Consumer<Order> processOrder = validateOrder
    .andThen(updateInventory)
    .andThen(sendConfirmation);

processOrder.accept(order);
```

---

### Supplier\<T\> — "Give me one of these"

`Supplier<T>` takes no arguments and returns a value. It's used as a factory or lazy initializer.

```java
Supplier<List<String>> listFactory = ArrayList::new;
Supplier<Double> randomValue = Math::random;
Supplier<LocalDateTime> now = LocalDateTime::now;

List<String> list = listFactory.get();
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `get()` | Create/get the value | `listFactory.get()` |

```java
// Lazy initialization with Supplier
Supplier<ExpensiveObject> lazyInit = () -> new ExpensiveObject(); // created only when get() called

// Real-world: Database connection factory
Supplier<Connection> connectionFactory = () -> {
    try {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    } catch (SQLException e) {
        throw new RuntimeException("Failed to create connection", e);
    }
};

// Supplier with Optional for safe initialization
Supplier<Optional<Config>> configLoader = () -> {
    try {
        return Optional.of(Config.loadFromFile("config.json"));
    } catch (IOException e) {
        return Optional.empty();
    }
};
```

---

### UnaryOperator\<T\> — "Transform same type"

`UnaryOperator<T>` extends `Function<T,T>`. It takes an input of type T and returns an output of the same type T. Used for in-place transformations.

```java
UnaryOperator<String> toUpper = String::toUpperCase;
UnaryOperator<Integer> doubleIt = n -> n * 2;
UnaryOperator<List<String>> sort = list -> { list.sort(null); return list; };
```

**Key Methods (inherited from Function):**

| Method | Description | Example |
|--------|-------------|---------|
| `apply(T t)` | Apply transformation | `doubleIt.apply(5)` → `10` |

```java
// Common usage with map()
List<String> uppercased = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

List<Integer> doubled = numbers.stream()
    .map(n -> n * 2)
    .collect(Collectors.toList());

// Real-world: Sanitize user input
UnaryOperator<String> sanitize = input -> input
    .trim()
    .toLowerCase()
    .replaceAll("[^a-z0-9 ]", "");

List<String> cleanNames = rawNames.stream()
    .map(sanitize)
    .collect(Collectors.toList());

// Real-world: Normalize prices (round to 2 decimals)
UnaryOperator<Double> roundToCents = price -> 
    Math.round(price * 100.0) / 100.0;

List<Double> normalizedPrices = prices.stream()
    .map(roundToCents)
    .collect(Collectors.toList());
```

---

### BinaryOperator\<T\> — "Combine two of same type"

`BinaryOperator<T>` extends `BiFunction<T,T,T>`. It takes two arguments of the same type and returns a result of that same type. Used for aggregation and reduction.

```java
BinaryOperator<Integer> sum = Integer::sum;
BinaryOperator<String> concat = (a, b) -> a + b;
BinaryOperator<Integer> max = BinaryOperator.maxBy(Comparator.naturalOrder());
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `apply(T t1, T t2)` | Combine two values | `sum.apply(3, 4)` → `7` |
| `maxBy(Comparator)` | Return larger value | `BinaryOperator.maxBy(Comparator.naturalOrder())` |
| `minBy(Comparator)` | Return smaller value | `BinaryOperator.minBy(Comparator.naturalOrder())` |

```java
// Reduce with BinaryOperator
int total = numbers.stream()
    .reduce(0, Integer::sum);

Optional<String> longest = words.stream()
    .reduce((a, b) -> a.length() > b.length() ? a : b);

// Real-world: Merge maps
BinaryOperator<Map<String, Integer>> mergeMaps = (map1, map2) -> {
    Map<String, Integer> result = new HashMap<>(map1);
    map2.forEach((key, value) -> 
        result.merge(key, value, Integer::sum)
    );
    return result;
};

Map<String, Integer> combined = mergeMaps.apply(salesQ1, salesQ2);

// Real-world: Find most expensive item
BinaryOperator<Product> moreExpensive = BinaryOperator.maxBy(
    Comparator.comparing(Product::getPrice)
);

Optional<Product> mostExpensive = products.stream()
    .reduce(moreExpensive);
```

---

### BiFunction\<T,U,R\> — "Two inputs, one output"

`BiFunction<T,U,R>` takes two arguments of different types and returns a result. Used when transformation needs two inputs.

```java
BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
BiFunction<Integer, Integer, Integer> add = Integer::sum;
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `apply(T t, U u)` | Apply to both args | `add.apply(3, 4)` → `7` |
| `andThen(Function after)` | Chain forward | `add.andThen(Object::toString)` |

```java
// Real-world: Calculate discount
BiFunction<Double, Double, Double> applyDiscount = 
    (price, discountPercent) -> price * (1 - discountPercent / 100);

double finalPrice = applyDiscount.apply(100.0, 20.0); // 80.0

// Real-world: Map two collections together
BiFunction<List<String>, List<Integer>, List<User>> buildUsers = 
    (names, ages) -> IntStream.range(0, names.size())
        .mapToObj(i -> new User(names.get(i), ages.get(i)))
        .collect(Collectors.toList());

// Real-world: Join first and last name
BiFunction<String, String, String> fullName = 
    (first, last) -> first + " " + last;

List<String> fullNames = firstNames.stream()
    .map((first, last) -> fullName.apply(first, last)) // Not directly possible
    // Use zip or indexed approach instead
```

---

### BiPredicate\<T,U\> — "Two inputs, boolean output"

`BiPredicate<T,U>` takes two arguments and returns a boolean. Used for multi-argument filtering.

```java
BiPredicate<String, String> equals = String::equals;
BiPredicate<String, Integer> hasLength = (s, n) -> s.length() == n;
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `test(T t, U u)` | Evaluate predicate | `equals.test("a", "a")` → `true` |
| `and(BiPredicate other)` | Chain with AND | `hasLength.and(equals)` |
| `or(BiPredicate other)` | Chain with OR | `hasLength.or(equals)` |
| `negate()` | Invert result | `hasLength.negate()` |

```java
// Real-world: Match user to role
BiPredicate<User, Role> hasRole = (user, role) -> 
    user.getRoles().contains(role);

BiPredicate<User, Role> canAccess = (user, role) -> 
    user.isActive() && hasRole.test(user, role);

boolean allowed = canAccess.test(currentUser, Role.ADMIN);

// Real-world: Validate age for category
BiPredicate<Integer, String> ageForCategory = (age, category) -> {
    return switch (category) {
        case "child" -> age < 13;
        case "teen" -> age >= 13 && age < 18;
        case "adult" -> age >= 18;
        default -> false;
    };
};
```

---

### BiConsumer\<T,U\> — "Do something with two inputs"

`BiConsumer<T,U>` takes two arguments and returns nothing. Used for side effects with two parameters.

```java
BiConsumer<String, Integer> printWithIndex = (s, i) -> System.out.println(i + ": " + s);
```

**Key Methods:**

| Method | Description | Example |
|--------|-------------|---------|
| `accept(T t, U u)` | Execute with both args | `printWithIndex.accept("Hello", 0)` |
| `andThen(BiConsumer after)` | Chain consumers | `consumer1.andThen(consumer2)` |

```java
// Real-world: Process key-value pairs
BiConsumer<String, Integer> updateStock = (product, quantity) -> {
    inventory.merge(product, quantity, Integer::sum);
    log.info("Updated {} stock by {}", product, quantity);
};

productQuantities.forEach(updateStock);

// Real-world: Build a report
BiConsumer<String, Double> addRevenue = (department, amount) -> {
    report.put(department, report.getOrDefault(department, 0.0) + amount);
};

quarterlyRevenue.forEach(addRevenue);

// Real-world: Validate and log
BiConsumer<String, String> validateAndLog = (field, value) -> {
    if (value == null || value.isBlank()) {
        log.warn("Empty value for field: {}", field);
    }
};

Map.of("name", "Alice", "email", "", "phone", null)
    .forEach(validateAndLog);
```

---

## 2. Method Reference Syntax (::)

Method references provide a shorthand for lambdas that only call a specific method. They make code more readable and concise.

### Four Types of Method References

#### 1. Static Method Reference — `ClassName::staticMethod`

```java
Function<String, Integer> parseInt = Integer::parseInt;
// Equivalent to: Function<String, Integer> parseInt = s -> Integer.parseInt(s);

Supplier<LocalDateTime> now = LocalDateTime::now;
// Equivalent to: Supplier<LocalDateTime> now = () -> LocalDateTime.now();

Predicate<String> isBlank = String::isEmpty;
// Equivalent to: Predicate<String> isBlank = s -> s.isEmpty();
```

#### 2. Instance Method of Particular Object — `object::instanceMethod`

```java
String str = "hello";
Function<String, String> toUpper = str::toUpperCase;
// Equivalent to: Function<String, String> toUpper = s -> str.toUpperCase(s); (not right)
// Actually: Function<String, String> toUpper = s -> str.toUpperCase(); // No, this ignores input

// Correct: The object is captured, but function still takes input
// Actually for instance method of particular object, the method has no parameters
Supplier<Integer> length = str::length;
// Equivalent to: Supplier<Integer> length = str::length; // () -> str.length()

Consumer<String> print = System.out::println;
// Equivalent to: Consumer<String> print = System.out::println;
```

#### 3. Instance Method of Arbitrary Object of Given Type — `ClassName::instanceMethod`

```java
Function<String, String> toLower = String::toLowerCase;
// Equivalent to: Function<String, String> toLower = s -> s.toLowerCase();

Function<String, String> trim = String::trim;
// Equivalent to: Function<String, String> trim = s -> s.trim();

BiFunction<String, String, Integer> compare = String::compareToIgnoreCase;
// Equivalent to: BiFunction<String, String, Integer> compare = String::compareToIgnoreCase;
// Wait, this is actually: (s1, s2) -> s1.compareToIgnoreCase(s2)
```

#### 4. Constructor Reference — `ClassName::new`

```java
Supplier<ArrayList> listFactory = ArrayList::new;
// Equivalent to: Supplier<ArrayList> listFactory = () -> new ArrayList();

Function<Integer, ArrayList> sizedList = ArrayList::new;
// Equivalent to: Function<Integer, ArrayList> sizedList = n -> new ArrayList(n);

Function<String, StringBuilder> sbFactory = StringBuilder::new;
// Equivalent to: Function<String, StringBuilder> sbFactory = s -> new StringBuilder(s);

// Array constructor reference
Function<Integer, String[]> arrayFactory = String[]::new;
// Equivalent to: Function<Integer, String[]> arrayFactory = n -> new String[n];
```

### Method Reference vs Lambda — When to Use Which

```java
// Method reference: when lambda just calls a single method
Function<String, Integer> f2 = String::length;          // Method reference ✓
Function<String, Integer> f1 = s -> s.length();        // Lambda (less clear)

// Lambda: when you need additional logic
Function<String, String> f3 = s -> s.trim().toUpperCase();  // Lambda needed
Function<String, String> f4 = String::trim;                 // Method reference

// Real-world examples:
// Use method reference
List<String> uppercased = names.stream()
    .map(String::toUpperCase)           // Clear and concise
    .collect(Collectors.toList());

// Use lambda when combining operations
List<String> normalized = names.stream()
    .map(s -> s.trim().toLowerCase())   // Multiple operations
    .collect(Collectors.toList());

// Method reference with predicates
List<String> nonEmpty = names.stream()
    .filter(Predicate.not(String::isEmpty))  // Elegant
    .collect(Collectors.toList());
```

---

## 3. Ternary Operator in Lambdas

The ternary operator (`condition ? trueValue : falseValue`) is often used inside lambdas for concise conditional logic.

```java
// Basic ternary in lambda
Function<Integer, String> classify = n -> n > 0 ? "positive" : "negative";

// Nested ternary (avoid in production, but know for interviews)
Function<Integer, String> complex = n -> 
    n > 0 ? "positive" : n < 0 ? "negative" : "zero";

// Ternary with Predicate
Predicate<String> isShort = s -> s.length() <= 5;

// Ternary for null handling
Function<String, String> defaultIfNull = s -> s != null ? s : "default";

// Ternary with method reference (not possible — must use lambda)
// Function<String, Integer> len = s -> s.length(); // Lambda required
```

### Real-World Examples

```java
// Price formatting with discount
Function<Product, String> priceDisplay = product -> 
    product.isOnSale() 
        ? String.format("$%.2f (was $%.2f)", product.getSalePrice(), product.getPrice())
        : String.format("$%.2f", product.getPrice());

// Status mapping
Function<OrderStatus, String> statusMessage = status -> 
    switch (status) {
        case PENDING -> "Order is being processed";
        case SHIPPED -> "Order has been shipped";
        case DELIVERED -> "Order has been delivered";
        case CANCELLED -> "Order was cancelled";
        default -> "Unknown status";
    };

// Null-safe accessor with default
Function<User, String> displayName = user -> 
    user != null 
        ? Optional.ofNullable(user.getNickname()).orElse(user.getName())
        : "Anonymous";

// Conditional transformation
UnaryOperator<String> censorProfanity = word -> 
    isProfane(word) ? "*".repeat(word.length()) : word;

// Age group classification
Function<Integer, String> ageGroup = age -> 
    age < 13 ? "Child" :
    age < 18 ? "Teenager" :
    age < 65 ? "Adult" :
    "Senior";
```

---

## 4. Terminal vs Intermediate Operations

Stream operations are divided into two categories. Understanding the difference is crucial for writing efficient functional code.

### Intermediate Operations (Lazy)

Intermediate operations return a new Stream and are **lazy** — they're not executed until a terminal operation is invoked.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

Stream<String> filtered = names.stream()
    .filter(n -> n.length() > 3)    // intermediate
    .map(String::toUpperCase)        // intermediate
    .sorted();                       // intermediate
// Nothing has executed yet!
```

**Common Intermediate Operations:**

| Operation | Description | Example |
|-----------|-------------|---------|
| `filter(Predicate)` | Keep matching elements | `.filter(n -> n.length() > 3)` |
| `map(Function)` | Transform each element | `.map(String::toUpperCase)` |
| `flatMap(Function)` | Flatten nested streams | `.flatMap(Collection::stream)` |
| `sorted()` | Sort elements | `.sorted()` |
| `sorted(Comparator)` | Sort with comparator | `.sorted(Comparator.reverseOrder())` |
| `distinct()` | Remove duplicates | `.distinct()` |
| `peek(Consumer)` | Debug/inspect elements | `.peek(System.out::println)` |
| `limit(long)` | Take first n elements | `.limit(10)` |
| `skip(long)` | Skip first n elements | `.skip(5)` |

### Terminal Operations (Eager)

Terminal operations trigger the actual processing and produce a result or side effect.

```java
// Terminal operation triggers execution
long count = filtered.count();       // terminal — returns long

List<String> result = names.stream()
    .filter(n -> n.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());   // terminal — returns List

// forEach is also terminal
names.stream()
    .filter(n -> n.length() > 3)
    .forEach(System.out::println);   // terminal — void
```

**Common Terminal Operations:**

| Operation | Description | Return Type |
|-----------|-------------|-------------|
| `collect(Collector)` | Accumulate into collection | `R` |
| `forEach(Consumer)` | Perform action for each | `void` |
| `reduce(BinaryOperator)` | Combine into single value | `Optional<T>` |
| `reduce(T, BinaryOperator)` | Combine with identity | `T` |
| `count()` | Count elements | `long` |
| `anyMatch(Predicate)` | Any element matches? | `boolean` |
| `allMatch(Predicate)` | All elements match? | `boolean` |
| `noneMatch(Predicate)` | No elements match? | `boolean` |
| `findFirst()` | Get first element | `Optional<T>` |
| `findAny()` | Get any element | `Optional<T>` |
| `toArray()` | Convert to array | `Object[]` |
| `min(Comparator)` | Find minimum | `Optional<T>` |
| `max(Comparator)` | Find maximum | `Optional<T>` |

### Lazy Evaluation Demo

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

// Nothing prints — no terminal operation
System.out.println("Before stream");
names.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n.length() > 3;
    })
    .map(n -> {
        System.out.println("Mapping: " + n);
        return n.toUpperCase();
    });
// "Before stream" prints, but nothing else!

// NOW it executes — terminal operation triggers processing
names.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n.length() > 3;
    })
    .forEach(System.out::println);
// Output:
// Filtering: Alice
// Filtering: Bob
// Filtering: Charlie
// Charlie
// Filtering: David
// David
// Filtering: Eve
// Eve

// IMPORTANT: Each terminal operation creates a new stream
// You cannot reuse a stream after a terminal operation
```

---

## 5. Complete Cheat Sheet

| Interface | Input | Output | Method | Use Case |
|-----------|-------|--------|--------|----------|
| `Predicate<T>` | T | boolean | `test()` | Filtering |
| `Function<T,R>` | T | R | `apply()` | Transformation |
| `Consumer<T>` | T | void | `accept()` | Side effects |
| `Supplier<T>` | none | T | `get()` | Factory |
| `UnaryOperator<T>` | T | T | `apply()` | Same-type transform |
| `BinaryOperator<T>` | T,T | T | `apply()` | Combine two values |
| `BiFunction<T,U,R>` | T,U | R | `apply()` | Two-input transform |
| `BiPredicate<T,U>` | T,U | boolean | `test()` | Two-input test |
| `BiConsumer<T,U>` | T,U | void | `accept()` | Two-input side effect |

### Quick Reference: Method References

| Syntax | Description | Example |
|--------|-------------|---------|
| `Class::staticMethod` | Static method | `Integer::parseInt` |
| `object::instanceMethod` | Instance method of object | `System.out::println` |
| `Class::instanceMethod` | Instance method of any object | `String::toLowerCase` |
| `Class::new` | Constructor | `ArrayList::new` |

### Stream Operations at a Glance

| Category | Operations |
|----------|------------|
| **Intermediate** | `filter`, `map`, `flatMap`, `sorted`, `distinct`, `peek`, `limit`, `skip` |
| **Terminal** | `collect`, `forEach`, `reduce`, `count`, `anyMatch`, `allMatch`, `noneMatch`, `findFirst`, `findAny`, `min`, `max` |

---

**Next:** [Part 6: Real-World Projects](README-part6.md)
