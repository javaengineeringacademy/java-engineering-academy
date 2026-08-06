# Java 21 LTS Deep Dive

## Virtual Threads (Why, How, When to Use)

### The Problem

Traditional threads are expensive:

```java
// Traditional threads
for (int i = 0; i < 10000; i++) {
    new Thread(() -> {
        // Each thread uses ~1MB stack space
        // Creating 10000 threads = ~10GB RAM
        blockingIOOperation();
    }).start();
}
```

### The Solution

Virtual threads are lightweight:

```java
// Virtual threads (Java 21)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10000; i++) {
        executor.submit(() -> {
            blockingIOOperation(); // Same code
        });
    }
}
// 10000 virtual threads use ~10MB RAM
```

### Why Virtual Threads?

1. **Scalability**: Millions of concurrent tasks
2. **Simplicity**: Same blocking code, different execution
3. **Resource Efficiency**: Use OS threads only when needed
4. **No Code Changes**: Existing blocking code works
5. **Reactive Benefits**: Without reactive complexity

### How They Work

```java
// Virtual thread creation
Thread.startVirtualThread(() -> {
    System.out.println("Running in virtual thread");
});

// Virtual thread factory
ThreadFactory factory = Thread.ofVirtual()
    .name("worker-", 0)
    .factory();

// Executor service
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Structured concurrency (preview)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    scope.fork(() -> task1());
    scope.fork(() -> task2());
    scope.join();
}
```

### When to Use Virtual Threads

**Good Use Cases:**
```java
// I/O-bound tasks
// - HTTP requests
// - Database queries
// - File operations
// - External service calls

// High concurrency
// - Chat servers
// - WebSocket handlers
// - Long-polling endpoints
```

**Bad Use Cases:**
```java
// CPU-bound tasks (no benefit)
// - Mathematical calculations
// - Image processing
// - Data transformation

// Thread-local state heavy
// - Sessions with large thread-local data
```

### Pinning Problem

```java
// Virtual threads can be pinned (blocked on OS thread)
synchronized (lock) {
    // This pins the virtual thread!
    blockingOperation();
}

// Better: Use ReentrantLock instead
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    blockingOperation(); // Won't pin virtual thread
} finally {
    lock.unlock();
}
```

### Performance Comparison

```java
// Platform threads
ExecutorService platform = Executors.newFixedThreadPool(200);
// Limited by OS thread count

// Virtual threads
ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
// Limited by memory, not thread count

// Benchmark results:
// 10,000 concurrent tasks:
// Platform threads: 200 tasks/second
// Virtual threads: 10,000 tasks/second
```

---

## Record Patterns (Why)

### The Problem

Destructuring data required manual extraction:

```java
// Before record patterns
if (obj instanceof Point) {
    Point p = (Point) obj;
    int x = p.x();
    int y = p.y();
    // Use x and y
}
```

### The Solution

```java
// After record patterns
if (obj instanceof Point(int x, int y)) {
    // x and y are directly available
    System.out.println("Point at " + x + ", " + y);
}
```

### Why Record Patterns?

1. **Conciseness**: Less boilerplate code
2. **Readability**: Clear data structure
3. **Safety**: Compile-time checking
4. **Pattern Matching Integration**: Works with switch

### Nested Patterns

```java
// Nested record patterns
record Point(int x, int y) {}
record Line(Point start, Point end) {}

if (obj instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    // Direct access to all components
    System.out.println("Line from " + x1 + "," + y1 + " to " + x2 + "," + y2);
}
```

### Pattern Variables

```java
// Pattern variables in conditions
if (obj instanceof Point(int x, int y) && x > 0 && y > 0) {
    // x and y are available
}

// Pattern variables in switch
String description = switch (shape) {
    case Circle(double r) -> "Circle with radius " + r;
    case Square(double s) -> "Square with side " + s;
    case null -> "Null shape";
};
```

### Exhaustiveness

```java
// Exhaustive switch with sealed types
sealed interface Shape permits Circle, Square {}
record Circle(double radius) implements Shape {}
record Square(double side) implements Shape {}

String describe(Shape shape) {
    return switch (shape) {
        case Circle(double r) -> "Circle: " + r;
        case Square(double s) -> "Square: " + s;
        // No default needed - exhaustive!
    };
}
```

---

## Pattern Matching Switch (Why)

### Before Java 21

```java
// Traditional switch
switch (obj.getClass().getSimpleName()) {
    case "Circle":
        Circle c = (Circle) obj;
        return "Circle: " + c.getRadius();
    case "Square":
        Square s = (Square) obj;
        return "Square: " + s.getSide();
    default:
        return "Unknown";
}
```

### After Java 21

```java
// Pattern matching switch
return switch (obj) {
    case Circle c -> "Circle: " + c.getRadius();
    case Square s -> "Square: " + s.getSide();
    case null -> "Null";
    default -> "Unknown";
};
```

### Why Pattern Matching Switch?

1. **Type Safety**: Compile-time type checking
2. **Readability**: Clear pattern matching
3. **Exhaustiveness**: Ensures all cases handled
4. **Integration**: Works with sealed classes and records

### Guarded Patterns

```java
// Guarded patterns
return switch (shape) {
    case Circle c && c.radius() > 10 -> "Large circle";
    case Circle c -> "Small circle";
    case Square s -> "Square: " + s.side();
    case null -> "Null";
};
```

### Null Handling

```java
// Null-safe switch
return switch (obj) {
    case null -> "Null value";
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    default -> "Unknown";
};
```

### Multiple Patterns

```java
// Multiple patterns in one case
return switch (obj) {
    case Integer i && i > 0 -> "Positive integer";
    case Integer i && i < 0 -> "Negative integer";
    case Integer i -> "Zero";
    case String s -> "String: " + s;
    case null -> "Null";
};
```

---

## Sequenced Collections (Why)

### The Problem

No unified way to access first/last elements:

```java
// Before sequenced collections
List<String> list = List.of("a", "b", "c");
String first = list.get(0);
String last = list.get(list.size() - 1);

Deque<String> deque = ArrayDeque.of("a", "b", "c");
String first = deque.getFirst();
String last = deque.getLast();
```

### The Solution

```java
// After sequenced collections
SequencedCollection<String> list = List.of("a", "b", "c");
String first = list.getFirst();
String last = list.getLast();

// Reverse view
SequencedCollection<String> reversed = list.reversed();
```

### Why Sequenced Collections?

1. **Consistency**: Unified API for ordered collections
2. **Readability**: Clear method names
3. **Reversibility**: Built-in reverse view
4. **Deque Integration**: Works with Deque implementations

### Interface Hierarchy

```java
// Sequenced collection hierarchy
public interface SequencedCollection<E> extends Collection<E> {
    E getFirst();
    E getLast();
    void addFirst(E e);
    void addLast(E e);
    E removeFirst();
    E removeLast();
    SequencedCollection<E> reversed();
}

public interface SequencedMap<K, V> extends Map<K, V> {
    Map.Entry<K, V> firstEntry();
    Map.Entry<K, V> lastEntry();
    Map.Entry<K, V> pollFirstEntry();
    Map.Entry<K, V> pollLastEntry();
    SequencedMap<K, V> reversed();
}
```

### Implementation Support

```java
// All major collections implement SequencedCollection
SequencedCollection<String> arrayList = new ArrayList<>();
SequencedCollection<String> linkedList = new LinkedList<>();
SequencedCollection<String> treeSet = new TreeSet<>();
SequencedCollection<String> linkedHashSet = new LinkedHashSet<>();

// SequencedMap
SequencedMap<String, Integer> treeMap = new TreeMap<>();
SequencedMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
```

---

## String Templates (Preview)

### The Problem

String concatenation is verbose and error-prone:

```java
// Before string templates
String name = "John";
int age = 30;
String message = "Hello, " + name + "! You are " + age + " years old.";
```

### The Solution

```java
// After string templates (Java 21 preview)
String name = "John";
int age = 30;
String message = STR."Hello, \{name}! You are \{age} years old.";
```

### Why String Templates?

1. **Readability**: Clear interpolation syntax
2. **Type Safety**: Compile-time checking
3. **Performance**: Optimized concatenation
4. **Flexibility**: Custom processors

### Template Processors

```java
// Built-in processors
String json = STR."""
    {
      "name": "\{name}",
      "age": \{age}
    }
    """;

// JSON processor (preview)
Json json = JSON."""
    {
      "name": "\{name}",
      "age": \{age}
    }
    """;

// SQL processor (preview)
Sql sql = SQL."""
    SELECT * FROM users 
    WHERE name = \{name}
    """;
```

### Custom Processors

```java
// Custom template processor
public class Greeting {
    private final String value;
    
    public Greeting(String value) {
        this.value = value;
    }
    
    public static Greeting process(StringTemplate template) {
        return new Greeting(template.interpolate());
    }
}

// Usage
String name = "World";
Greeting greeting = GREETING."Hello, \{name}!";
```

### Preview Status

```java
// Enable preview features
// javac --enable-preview --release 21 Hello.java
// java --enable-preview Hello

// In Maven
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            <arg>--enable-preview</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

---

## Migration from Java 17 to Java 21

### Step 1: Update Build Tools

```xml
<!-- Maven -->
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

<!-- Gradle -->
sourceCompatibility = '21'
targetCompatibility = '21'
```

### Step 2: Update Dependencies

```xml
<!-- Check for Java 21 compatibility -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>library</artifactId>
    <version>3.0</version> <!-- Version supporting Java 21 -->
</dependency>
```

### Step 3: Enable Virtual Threads

```java
// Replace platform threads with virtual threads
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Or use structured concurrency
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    scope.fork(() -> task1());
    scope.fork(() -> task2());
    scope.join();
}
```

### Step 4: Adopt Pattern Matching

```java
// Replace instanceof + cast
if (obj instanceof String s) {
    // Use s directly
}

// Replace switch statements
return switch (obj) {
    case Circle c -> "Circle";
    case Square s -> "Square";
    case null -> "Null";
};
```

### Step 5: Use Record Patterns

```java
// Destructure records
if (obj instanceof Point(int x, int y)) {
    // Use x and y directly
}
```

### Step 6: Test Thoroughly

- Unit tests
- Integration tests
- Performance tests
- Virtual thread tests
- Pattern matching tests

### Common Issues

1. **Pinning**: Virtual threads blocked by synchronized
2. **Preview Features**: Not production-ready
3. **Dependencies**: Some libraries may not support Java 21
4. **Performance**: Virtual threads not always faster

---

## Best Practices for Java 21

1. **Use LTS**: Java 21 has 8-year support
2. **Adopt Virtual Threads**: For I/O-bound tasks
3. **Use Pattern Matching**: Simplify type checks
4. **Use Record Patterns**: For data destructuring
5. **Test Thoroughly**: After version upgrade
6. **Monitor Performance**: Virtual threads have overhead

---

## References

- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [OpenJDK 21](https://openjdk.org/projects/jdk/21/)
- [Java 21 JEPs](https://openjdk.org/jeps/21)
- [Virtual Threads Guide](https://www.oracle.com/java/technologies/javase/virtual-threads.html)
- [Pattern Matching Guide](https://www.oracle.com/java/technologies/javase/pattern-matching-instanceof-guide.html)
