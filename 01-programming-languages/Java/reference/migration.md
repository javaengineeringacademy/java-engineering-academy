# Java Migration

> Java 8 to 21 migration, JPMS, records, sealed classes, and modern language features.

## Migration Timeline

| Version | Key Features | LTS |
|---------|--------------|-----|
| Java 8 | Lambdas, Streams, Optional | Yes |
| Java 9 | JPMS, JShell, HTTP Client | No |
| Java 10 | var keyword, Application CDS | No |
| Java 11 | HTTP Client stable, String methods, ZGC | Yes |
| Java 12 | Switch expressions preview | No |
| Java 13 | Text blocks preview | No |
| Java 14 | Records preview, Pattern matching preview | No |
| Java 15 | Text blocks, Sealed classes preview, Hidden classes | No |
| Java 16 | Records stable, Pattern matching for instanceof | No |
| Java 17 | Sealed classes, Pattern matching preview | Yes |
| Java 18 | Simple web server, Code snippets in Javadoc | No |
| Java 19 | Virtual threads preview, Structured concurrency | No |
| Java 20 | Scoped values, Pattern matching preview | No |
| Java 21 | Virtual threads, Pattern matching, Record patterns | Yes |

## Key Migration Steps

### 1. Update Build Configuration

```xml
<!-- Maven pom.xml -->
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

### 2. Replace Deprecated APIs

```java
// Java 8: old date API
Date date = new Date();
Calendar cal = Calendar.getInstance();

// Java 21: java.time API
LocalDate date = LocalDate.now();
LocalDateTime dateTime = LocalDateTime.now();
Instant instant = Instant.now();

// Java 8: old Random
Random random = new Random();
int val = random.nextInt(100);

// Java 21: ThreadLocalRandom
int val = ThreadLocalRandom.current().nextInt(100);

// Java 8: Hashtable, Vector
Hashtable<String, String> table = new Hashtable<>();
Vector<String> vector = new Vector<>();

// Java 21: ConcurrentHashMap, CopyOnWriteArrayList
ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
```

### 3. Use var Keyword (Java 10+)

```java
// Before
Map<String, List<User>> usersByRole = new HashMap<>();
PreparedStatement stmt = conn.prepareStatement(sql);

// After
var usersByRole = new HashMap<String, List<User>>();
var stmt = conn.prepareStatement(sql);

// Effective for complex types
var stream = list.stream().filter(x -> x.length() > 5).map(String::toUpperCase);
var future = CompletableFuture.supplyAsync(() -> fetch());
```

### 4. HTTP Client (Java 11+)

```java
// Java 8: HttpURLConnection
URL url = new URL("https://api.example.com/data");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
int code = conn.getResponseCode();

// Java 21: HttpClient
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .GET()
    .build();
HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
int code = response.statusCode();
```

## Module System (JPMS) Migration

### Module Info

```java
// module-info.java
module com.example.myapp {
    requires java.sql;
    requires java.net.http;
    requires org.slf4j;
    
    exports com.example.api;
    exports com.example.model;
    
    opens com.example.config to spring.core;
    
    provides com.example.spi.Service with 
        com.example.impl.ServiceImpl;
}
```

### Module Commands

```bash
# List modules
java --list-modules

# Module path
java --module-path /path/to/modules -m com.example/myapp.Main

# Add modules to unnamed module
java --add-modules java.sql,jdk.unsupported -jar app.jar
```

## Records (Java 16+)

```java
// Before: POJO
public class User {
    private final String name;
    private final String email;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public boolean equals(Object o) { /* ... */ }
    @Override
    public int hashCode() { /* ... */ }
    @Override
    public String toString() { /* ... */ }
}

// After: Record
public record User(String name, String email) {
    public User {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
    }
}
```

## Sealed Classes (Java 17+)

```java
// Before: abstract class with limited subclasses
public abstract class Shape {
    public abstract double area();
}

// After: sealed class
public sealed class Shape permits Circle, Rectangle, Triangle {
    public abstract double area();
}

public record Circle(double radius) implements Shape {
    public double area() { return Math.PI * radius * radius; }
}

public record Rectangle(double width, double height) implements Shape {
    public double area() { return width * height; }
}

public non-sealed class Triangle extends Shape {
    private final double base, height;
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    public double area() { return 0.5 * base * height; }
}
```

## Pattern Matching (Java 21)

```java
// Before: instanceof + cast
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// After: pattern matching
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Pattern matching with && guard
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}

// Switch with patterns
String describe(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s  -> "String: " + s;
        case null      -> "null";
        default        -> "Unknown: " + obj;
    };
}
```

## Text Blocks (Java 15+)

```java
// Before
String json = "{\n" +
    "  \"name\": \"John\",\n" +
    "  \"age\": 30\n" +
    "}";

// After
String json = """
        {
          "name": "John",
          "age": 30
        }
        """;

// SQL example
String sql = """
        SELECT id, name, email
        FROM users
        WHERE active = true
        ORDER BY name
        """;
```

## Switch Expressions (Java 14+)

```java
// Before
String dayType;
switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        dayType = "Weekday";
        break;
    case SATURDAY:
    case SUNDAY:
        dayType = "Weekend";
        break;
    default:
        dayType = "Unknown";
}

// After
String dayType = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY -> "Weekend";
};
```

## Migration Checklist

- [ ] Update Java version in build configuration
- [ ] Replace deprecated APIs (Date, Hashtable, Vector)
- [ ] Update third-party dependencies for Java 21 compatibility
- [ ] Add module-info.java if using JPMS
- [ ] Replace raw types with generics
- [ ] Use try-with-resources for AutoCloseable
- [ ] Replace anonymous classes with lambdas where appropriate
- [ ] Use var for local variables (optional)
- [ ] Test all functionality after migration
- [ ] Update CI/CD pipeline Java version

## References

- [Java Migration Guide](https://docs.oracle.com/en/java/javase/21/migrate/)
- [Java Feature Comparison](https://www.oracle.com/java/technologies/javase/21-release-notes.html)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java configuration](configuration.md) | [Java best-practices](best-practices.md)
**Next:** [Java interview](interview.md)
