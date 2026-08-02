# Module 70: Java Version History

## Overview
Complete history of Java versions from 1.0 to 21+, covering major features, improvements, and the evolution of the Java platform.

## Learning Objectives
- Track Java evolution
- Understand major feature releases
- Learn version-specific features
- Plan version upgrades
- Understand release cadence

## Version Timeline

### Major Releases

| Version | Year | Key Features |
|---------|------|--------------|
| Java 1.0 | 1996 | Initial release |
| Java 1.2 | 1998 | Collections, Swing |
| Java 1.4 | 2002 | NIO, Regex, Logging |
| Java 5 | 2004 | Generics, Enums, Annotations, Autoboxing |
| Java 6 | 2006 | Scripting, JDBC 4.0 |
| Java 7 | 2011 | Diamond operator, Try-with-resources, Switch strings |
| Java 8 | 2014 | Lambda, Streams, Optional, Date/Time API |
| Java 9 | 2017 | Module system, JShell, HTTP Client |
| Java 10 | 2018 | Local variable type inference (var) |
| Java 11 | 2018 | LTS, HTTP Client, String methods, ZGC |
| Java 12 | 2019 | Switch expressions (preview) |
| Java 13 | 2019 | Text blocks (preview) |
| Java 14 | 2020 | Records (preview), Pattern matching (preview) |
| Java 15 | 2020 | Text blocks, Sealed classes (preview) |
| Java 16 | 2021 | Records, Pattern matching instanceof |
| Java 17 | 2021 | LTS, Sealed classes, Pattern matching switch (preview) |
| Java 18 | 2022 | Simple web server, Code snippets |
| Java 19 | 2022 | Virtual threads (preview), Structured concurrency (preview) |
| Java 20 | 2023 | Scoped values (preview), Record patterns (preview) |
| Java 21 | 2023 | LTS, Virtual threads, Record patterns, Pattern matching switch |

### Feature Details

#### Java 8 (2014)
```java
// Lambda expressions
list.stream().filter(x -> x > 5).forEach(System.out::println);

// Optional
Optional.ofNullable(value).orElse(defaultValue);

// Stream API
list.stream().map(String::toUpperCase).collect(Collectors.toList());

// Date/Time API
LocalDate.now();
LocalDateTime.now();
```

#### Java 11 (2018 LTS)
```java
// HTTP Client
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com"))
    .build();
HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());

// String methods
"Hello".isBlank();
"Hello".strip();
"Hello".repeat(3);
"Hello".lines();
```

#### Java 17 (2021 LTS)
```java
// Records
public record Point(int x, int y) {}

// Sealed classes
public sealed interface Shape permits Circle, Rectangle {}

// Pattern matching instanceof
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Text blocks
String json = """
    {
        "name": "John",
        "age": 30
    }
    """;
```

#### Java 21 (2023 LTS)
```java
// Virtual threads
Thread.startVirtualThread(() -> {
    // Lightweight thread
});

// Structured concurrency
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    scope.fork(() -> task1());
    scope.fork(() -> task2());
    scope.join();
}

// Record patterns
if (obj instanceof Point(int x, int y)) {
    System.out.println(x + y);
}

// Pattern matching switch
String result = switch (shape) {
    case Circle c -> "Circle: " + c.radius();
    case Rectangle r -> "Rectangle: " + r.width();
};
```

## Performance Considerations
- Newer versions have performance improvements
- GC improvements in each version
- JVM optimizations over time
- Modern language features improve code quality

## Best Practices
1. Stay on LTS versions
2. Upgrade regularly
3. Test thoroughly after upgrades
4. Use modern language features
5. Follow release notes

## Interview Questions

### Q1: What are the LTS versions of Java?
**Answer:** Java 8, 11, 17, 21.

### Q2: What is the release cadence?
**Answer:** Every 6 months for new features, every 2 years for LTS.

### Q3: What are virtual threads?
**Answer:** Lightweight threads managed by the JVM, not the OS.

### Q4: What are records?
**Answer:** Immutable data classes with automatic methods.

### Q5: What is pattern matching?
**Answer:** Combining type checking and casting in one operation.

## Summary
Java has evolved significantly over 25+ years, adding modern features while maintaining backward compatibility.

## References
- Oracle Java Documentation
- OpenJDK Releases
- Java Version History
