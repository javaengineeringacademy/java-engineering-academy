# Java Best Practices

> 20 Java best practices with code examples.

## 1. Use Streams Properly

```java
// Bad: collecting to list then stream
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 5) result.add(s.toUpperCase());
}

// Good: stream pipeline
List<String> result = list.stream()
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .toList();
```

## 2. Prefer Immutability

```java
// Bad: mutable class
public class User {
    private String name;  // mutable
    public void setName(String name) { this.name = name; }
}

// Good: immutable class
public record User(String name, String email) {
    public User {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
    }
}
```

## 3. Use try-with-resources

```java
// Bad: manual close
Connection conn = null;
try {
    conn = dataSource.getConnection();
    // use connection
} finally {
    if (conn != null) conn.close();
}

// Good: try-with-resources
try (Connection conn = dataSource.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // use connection
}
```

## 4. Avoid Null Where Possible

```java
// Bad: null checks everywhere
String name = user != null ? user.getName() : null;
if (name != null) { /* ... */ }

// Good: Optional
Optional.ofNullable(user)
    .map(User::getName)
    .ifPresent(name -> process(name));

// Good: null-safe defaults
String name = Objects.requireNonNullElse(user.getName(), "Unknown");
```

## 5. Use Collections Utility Methods

```java
// Bad: manual initialization
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("c");

// Good: factory methods
List<String> list = List.of("a", "b", "c");  // immutable
List<String> list = new ArrayList<>(List.of("a", "b", "c"));  // mutable

// Bad: manual check
boolean found = false;
for (String s : list) {
    if (s.equals(target)) { found = true; break; }
}

// Good: utility methods
boolean found = list.contains(target);
boolean anyMatch = list.stream().anyMatch(s -> s.length() > 5);
```

## 6. Prefer Composition Over Inheritance

```java
// Bad: inheritance for code reuse
class Stack extends ArrayList<Object> { /* ... */ }

// Good: composition
public class Stack<T> {
    private final Deque<T> elements = new ArrayDeque<>();
    
    public void push(T item) { elements.push(item); }
    public T pop() { return elements.pop(); }
    public int size() { return elements.size(); }
}
```

## 7. Use Builder Pattern for Complex Objects

```java
// Good: builder pattern
public class HttpRequest {
    private final String url;
    private final HttpMethod method;
    private final Map<String, String> headers;
    private final byte[] body;
    
    private HttpRequest(Builder builder) { /* ... */ }
    
    public static class Builder {
        private final String url;
        private HttpMethod method = HttpMethod.GET;
        private Map<String, String> headers = Map.of();
        private byte[] body = new byte[0];
        
        public Builder(String url) { this.url = url; }
        public Builder method(HttpMethod method) { this.method = method; return this; }
        public Builder header(String key, String val) { headers.put(key, val); return this; }
        public Builder body(byte[] body) { this.body = body; return this; }
        public HttpRequest build() { return new HttpRequest(this); }
    }
}

HttpRequest request = new HttpRequest.Builder("https://api.example.com")
    .method(HttpMethod.POST)
    .header("Content-Type", "application/json")
    .body(jsonBytes)
    .build();
```

## 8. Use Effective Final Variables in Lambdas

```java
// Bad: mutating variable
int[] counter = {0};
list.forEach(item -> counter[0]++);

// Good: accumulator
long count = list.stream().filter(this::isValid).count();

// Good: use AtomicLong
AtomicLong counter = new AtomicLong();
list.forEach(item -> counter.incrementAndGet());
```

## 9. Prefer Specialized Streams

```java
// Bad: boxing overhead
IntStream.range(0, 1000)
    .boxed()
    .map(i -> i * 2);

// Good: specialized
IntStream.range(0, 1000)
    .map(i -> i * 2);

// Good: use appropriate type
DoubleStream doubles = DoubleStream.of(1.0, 2.0, 3.0);
```

## 10. Validate Inputs Early

```java
// Bad: validation scattered
public void processOrder(Order order) {
    // ... later in method
    if (order == null) throw new IllegalArgumentException("Order cannot be null");
}

// Good: validate early with Objects
public void processOrder(Order order) {
    Objects.requireNonNull(order, "Order cannot be null");
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    // ... rest of method
}
```

## 11. Use Proper Exception Hierarchy

```java
// Bad: catching everything
try {
    process();
} catch (Exception e) {
    // too broad
}

// Good: specific exceptions
try {
    process();
} catch (ParseException e) {
    logger.warn("Parse failed", e);
} catch (IOException e) {
    logger.error("IO error", e);
    throw new ServiceException("Processing failed", e);
}
```

## 12. Prefer var for Local Variables

```java
// Good: clear from context
var users = userRepository.findByActive(true);
var request = HttpRequest.newBuilder().uri(uri).build();
var result = service.process(input);

// Bad: unclear types
var result = compute();  // what type is this?
```

## 13. Use Records for Data Classes

```java
// Good: immutable data carrier
public record Coordinates(double lat, double lon) {
    public Coordinates {
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Invalid lat");
        if (lon < -180 || lon > 180) throw new IllegalArgumentException("Invalid lon");
    }
}
```

## 14. Avoid String Concatenation in Loops

```java
// Bad: O(n^2) string creation
String result = "";
for (String s : list) {
    result += s;
}

// Good: StringBuilder
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();

// Best: String.join
String result = String.join("", list);
```

## 15. Use Enums for Constants

```java
// Bad: magic values
public static final int STATUS_ACTIVE = 1;
public static final int STATUS_INACTIVE = 2;

// Good: enum
public enum Status {
    ACTIVE, INACTIVE, PENDING;
    
    public boolean isActive() { return this == ACTIVE; }
}
```

## 16. Prefer interface-based APIs

```java
// Bad: concrete type in API
public ArrayList<String> getNames() { /* ... */ }

// Good: interface type
public List<String> getNames() { /* ... */ }

// Good: interface as abstraction
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    void save(T entity);
    void delete(ID id);
}
```

## 17. Use Time API (java.time)

```java
// Bad: Date and Calendar
Date date = new Date();
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 7);

// Good: java.time
LocalDate today = LocalDate.now();
LocalDate nextWeek = today.plusWeeks(7);
Instant timestamp = Instant.now();
Duration duration = Duration.ofHours(2);
```

## 18. Handle InterruptedException Properly

```java
// Bad: swallowing exception
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // do nothing
}

// Good: restore interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new ServiceException("Operation interrupted", e);
}
```

## 19. Use Concurrent Collections

```java
// Bad: synchronized HashMap
Map<String, Object> map = Collections.synchronizedMap(new HashMap<>());

// Good: ConcurrentHashMap
ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

// Good: computeIfAbsent for atomic operations
map.computeIfAbsent(key, k -> createExpensiveObject(k));
```

## 20. Document API with Javadoc

```java
/**
 * Processes an order and returns the order ID.
 *
 * @param order the order to process, must not be null
 * @return the generated order ID
 * @throws IllegalArgumentException if order is invalid
 * @throws ServiceException if processing fails
 */
public String processOrder(Order order) {
    Objects.requireNonNull(order, "Order cannot be null");
    // implementation
}
```

## References

- [Effective Java - Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Baeldung Best Practices](https://www.baeldung.com/java-best-practices)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java patterns](patterns.md) | [Java pitfalls](pitfalls.md)
**Next:** [Java pitfalls](pitfalls.md)
