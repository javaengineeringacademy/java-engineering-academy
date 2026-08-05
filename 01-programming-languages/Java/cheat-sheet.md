# Java Cheat Sheet

> Quick reference for syntax, collections, streams, and concurrency.

## Basic Syntax

```java
// Variables
int x = 10;
double d = 3.14;
String s = "hello";
boolean b = true;
var inferred = "type inferred";

// Constants
final int MAX = 100;
static final String APP_NAME = "MyApp";

// Arrays
int[] arr = {1, 2, 3};
String[] args = new String[10];
```

## Control Flow

```java
// If-else
if (condition) {
    // do something
} else if (otherCondition) {
    // do something else
} else {
    // default
}

// Switch expression (Java 14+)
String result = switch (day) {
    case MONDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Unknown";
};

// For loops
for (int i = 0; i < 10; i++) { /* ... */ }
for (String s : list) { /* ... */ }
while (condition) { /* ... */ }
do { /* ... */ } while (condition);
```

## Classes and Objects

```java
// Class
public class User {
    private String name;
    
    public User(String name) { this.name = name; }
    public String getName() { return name; }
}

// Record (Java 16+)
public record Point(double x, double y) {}

// Enum
public enum Status { ACTIVE, INACTIVE, PENDING }

// Interface
public interface Payable {
    BigDecimal calculatePayment();
    default void process() { /* ... */ }
}
```

## Collections

```java
// List
List<String> list = List.of("a", "b", "c");  // immutable
List<String> mutable = new ArrayList<>(list);

// Set
Set<Integer> set = Set.of(1, 2, 3);
Set<Integer> hashSet = new HashSet<>(set);

// Map
Map<String, Integer> map = Map.of("a", 1, "b", 2);
Map<String, Integer> mutableMap = new HashMap<>(map);

// Queue
Queue<String> queue = new LinkedList<>();
queue.offer("item");
String head = queue.poll();

// Stack
Deque<String> stack = new ArrayDeque<>();
stack.push("item");
String top = stack.pop();
```

## Streams

```java
// Pipeline
list.stream()
    .filter(x -> x.length() > 5)
    .map(String::toUpperCase)
    .sorted()
    .limit(10)
    .collect(Collectors.toList());

// Collectors
Collectors.groupingBy(Function.identity())
Collectors.partitioningBy(x -> x > 5)
Collectors.averagingInt(x -> x)
Collectors.joining(", ")
Collectors.toUnmodifiableList()

// Reduction
list.stream().reduce(0, Integer::sum);
list.stream().min(Integer::compareTo);
list.stream().max(Integer::compareTo);

// Parallel
list.parallelStream().forEach(this::process);
```

## Concurrency

```java
// Virtual Threads (Java 21+)
Thread.startVirtualThread(() -> doWork());
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> doWork());
}

// CompletableFuture
CompletableFuture.supplyAsync(() -> fetch())
    .thenApply(data -> transform(data))
    .thenAccept(result -> process(result));

// Synchronized
synchronized (lock) {
    // critical section
}

// ReentrantLock
ReentrantLock lock = new ReentrantLock();
lock.lock();
try { /* ... */ } finally { lock.unlock(); }

// ConcurrentHashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.computeIfAbsent(key, k -> expensiveComputation(k));
```

## Exception Handling

```java
// Try-with-resources
try (var conn = dataSource.getConnection()) {
    // use connection
} catch (SQLException e) {
    // handle error
} finally {
    // cleanup (optional with try-with-resources)
}

// Multi-catch
try {
    parse(input);
} catch (NumberFormatException | IllegalArgumentException e) {
    // handle both
}

// Custom exception
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Optional

```java
Optional<String> opt = Optional.ofNullable(value);
String result = opt.orElse("default");
String result = opt.orElseGet(() -> computeDefault());
opt.ifPresent(this::process);
opt.filter(s -> s.length() > 5).ifPresent(this::handle);
```

## Java Time API

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plusDays(1);
LocalDateTime dateTime = LocalDateTime.now();
Instant timestamp = Instant.now();
Duration duration = Duration.ofHours(2);
Period period = Period.ofDays(7);

String formatted = today.format(DateTimeFormatter.ISO_DATE);
LocalDate parsed = LocalDate.parse("2024-01-15");
```

## Common Methods

```java
// String
"hello".length()
"hello".substring(0, 3)
"hello".contains("ell")
"hello".replace("l", "r")
"hello".toUpperCase()
"hello".toLowerCase()
"  hello  ".strip()
"hello".isEmpty()
"hello".isBlank()
String.join(",", list)
String.format("Name: %s", name)

// Objects
Objects.requireNonNull(obj)
Objects.equals(a, b)
Objects.hash(a, b)
Objects.toString(obj, "null")

// Collections
Collections.unmodifiableList(list)
Collections.sort(list)
Collections.reverse(list)
Collections.frequency(list, element)
```

## References

- [Java Language Specification](https://docs.oracle.com/javase/specs/)
- [Java API Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java patterns](patterns.md) | [Java best-practices](best-practices.md)
**Next:** [Java roadmap](roadmap.md)
