# Java Hands-on Labs

> 10 practical exercises from beginner to advanced.

## Lab 1: Hello World and Build Tools (Beginner)

**Objective**: Set up Java development environment and run first program.

**Steps**:
1. Install JDK 21 via SDKMAN or manual download
2. Verify `java -version` and `javac -version`
3. Create `HelloWorld.java` with main method
4. Compile with `javac HelloWorld.java`
5. Run with `java HelloWorld`
6. Set up Maven project with `mvn archetype:generate`
7. Run with `mvn exec:java`

**Expected Output**: "Hello, World!" printed to console.

## Lab 2: Collections and Streams (Beginner)

**Objective**: Work with Java collections and stream API.

**Steps**:
1. Create a list of Person objects (name, age, city)
2. Filter persons over 30 using streams
3. Group by city using `Collectors.groupingBy`
4. Sort by age using `Comparator.comparing`
5. Calculate average age using `Collectors.averagingInt`
6. Find oldest person using `max()`
7. Convert results to Map using `toMap`

**Starter Code**:
```java
record Person(String name, int age, String city) {}
```

## Lab 3: Exception Handling (Beginner)

**Objective**: Implement proper exception handling patterns.

**Steps**:
1. Create custom exceptions: `ValidationException`, `NotFoundException`
2. Create a service that throws these exceptions
3. Implement try-catch-finally blocks
4. Use try-with-resources for AutoCloseable
5. Create exception handler for multiple exception types
6. Add finally block for cleanup

## Lab 4: File I/O with NIO (Intermediate)

**Objective**: Read, write, and process files using Java NIO.

**Steps**:
1. Read file using `Files.readString()`
2. Write file using `Files.writeString()`
3. List directory contents using `Files.list()`
4. Find files using `Files.walkFileTree()`
5. Copy file using `Files.copy()`
6. Watch directory for changes using `WatchService`
7. Process large file using `BufferedReader`

## Lab 5: Multi-threading (Intermediate)

**Objective**: Implement concurrent programming patterns.

**Steps**:
1. Create thread using `Runnable` and `Thread`
2. Implement `Callable` with `Future`
3. Use `ExecutorService` with fixed thread pool
4. Implement producer-consumer with `BlockingQueue`
5. Use `CountDownLatch` for synchronization
6. Implement thread-safe counter with `AtomicInteger`
7. Use `CompletableFuture` for async operations

## Lab 6: HTTP Client (Intermediate)

**Objective**: Build REST API client using Java HttpClient.

**Steps**:
1. Create GET request to public API
2. Parse JSON response using Jackson
3. Implement POST request with JSON body
4. Add error handling for HTTP status codes
5. Implement retry logic for failed requests
6. Add timeout configuration
7. Create reusable API client class

## Lab 7: Database with JDBC (Intermediate)

**Objective**: Connect to database and perform CRUD operations.

**Steps**:
1. Set up PostgreSQL with Docker
2. Create user table
3. Implement CRUD operations with JDBC
4. Use PreparedStatement for parameterized queries
5. Implement connection pooling with HikariCP
6. Add transaction management
7. Create repository pattern

## Lab 8: Design Patterns (Advanced)

**Objective**: Implement common design patterns in Java.

**Steps**:
1. **Singleton**: Enum singleton with lazy initialization
2. **Builder**: Generic builder for complex objects
3. **Observer**: Event listener pattern
4. **Strategy**: Algorithm selection at runtime
5. **Decorator**: Enhance functionality dynamically
6. **Factory**: Create objects without specifying class

## Lab 9: Virtual Threads (Advanced)

**Objective**: Explore Java 21 virtual threads.

**Steps**:
1. Create virtual thread executor
2. Run 10,000 concurrent tasks
3. Compare with platform threads
4. Implement structured concurrency
5. Use scoped values for thread-local data
6. Build concurrent web scraper
7. Measure performance vs traditional threads

## Lab 10: Microservice Project (Advanced)

**Objective**: Build complete microservice with Spring Boot.

**Steps**:
1. Create Spring Boot project with Maven
2. Implement REST controller with validation
3. Add JPA repository with PostgreSQL
4. Implement service layer with business logic
5. Add caching with Caffeine
6. Implement health checks
7. Add Prometheus metrics
8. Create Dockerfile
9. Write unit and integration tests
10. Deploy to local Kubernetes with Minikube

## References

- [Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Baeldung Tutorials](https://www.baeldung.com/)

---
**Prerequisites:** [Java installation](installation.md)
**Related:** [Java core-concepts](core-concepts.md) | [Java best-practices](best-practices.md)
**Next:** [Java cheat-sheet](cheat-sheet.md)
