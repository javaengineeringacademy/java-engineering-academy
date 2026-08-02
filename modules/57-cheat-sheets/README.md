# Module 57: Java Cheat Sheets

## Overview
Quick reference guides for Java concepts, syntax, and best practices. Use these cheat sheets for rapid recall during development and interviews.

## Core Java

### Data Types

| Type | Size | Default | Range |
|------|------|---------|-------|
| byte | 1 byte | 0 | -128 to 127 |
| short | 2 bytes | 0 | -32,768 to 32,767 |
| int | 4 bytes | 0 | -2^31 to 2^31-1 |
| long | 8 bytes | 0L | -2^63 to 2^63-1 |
| float | 4 bytes | 0.0f | ±3.4E38 |
| double | 8 bytes | 0.0 | ±1.7E308 |
| char | 2 bytes | '\u0000' | 0 to 65,535 |
| boolean | 1 bit | false | true/false |

### Operators

| Category | Operators |
|----------|-----------|
| Arithmetic | +, -, *, /, % |
| Relational | ==, !=, <, >, <=, >= |
| Logical | &&, \|\|, ! |
| Bitwise | &, \|, ^, ~, <<, >> |
| Assignment | =, +=, -=, *=, /= |
| Ternary | ? : |

### Collections Quick Reference

| Collection | Ordered | Duplicate | Thread-Safe |
|------------|---------|-----------|-------------|
| ArrayList | Yes | Yes | No |
| LinkedList | Yes | Yes | No |
| HashSet | No | No | No |
| TreeSet | Yes | No | No |
| HashMap | No | No | No |
| TreeMap | Yes | No | No |
| LinkedHashMap | Yes | No | No |
| ConcurrentHashMap | No | No | Yes |
| Vector | Yes | Yes | Yes |
| Hashtable | No | No | Yes |

### Stream API Quick Reference

```java
// Create
Stream.of(values)
List.stream()
Arrays.stream(array)
Stream.iterate(seed, fn)
Stream.generate(supplier)

// Intermediate
.filter(predicate)
.map(function)
.flatMap(function)
.distinct()
.sorted()
.peek(consumer)
.limit(n)
.skip(n)

// Terminal
.collect(collector)
.forEach(consumer)
.reduce(accumulator)
.count()
.findFirst()
.findAny()
.anyMatch(predicate)
.allMatch(predicate)
noneMatch(predicate)
.min(comparator)
.max(comparator)
.toArray()
```

### Exception Handling

```java
try {
    // Risky code
} catch (SpecificException e) {
    // Handle
} catch (Exception e) {
    // Handle others
} finally {
    // Cleanup
}

// Try-with-resources
try (var resource = new Resource()) {
    // Use resource
}

// Custom exception
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}
```

### Multithreading

```java
// Thread creation
Thread t = new Thread(() -> {
    // Task
});
t.start();

// ExecutorService
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> {
    // Task
});
executor.shutdown();

// CompletableFuture
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenAccept(System.out::println);

// Synchronized
synchronized (lock) {
    // Critical section
}

// Lock
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();
}
```

### Design Patterns

| Pattern | Purpose | Example |
|---------|---------|---------|
| Singleton | Single instance | Runtime, Logger |
| Factory | Object creation | Collection.iterator() |
| Builder | Complex objects | StringBuilder |
| Observer | Notifications | Event listeners |
| Strategy | Algorithm selection | Comparator |
| Decorator | Add behavior | InputStream |
| Adapter | Interface conversion | InputStreamReader |

## Spring Boot

### Annotations

| Annotation | Purpose |
|------------|---------|
| @SpringBootApplication | Main class |
| @RestController | REST controller |
| @Service | Service layer |
| @Repository | Data access |
| @Component | Generic component |
| @Autowired | Dependency injection |
| @Configuration | Configuration class |
| @Bean | Bean definition |
| @Value | Property injection |
| @RequestMapping | URL mapping |
| @GetMapping | HTTP GET |
| @PostMapping | HTTP POST |
| @PutMapping | HTTP PUT |
| @DeleteMapping | HTTP DELETE |
| @PathVariable | URL path variable |
| @RequestParam | Query parameter |
| @RequestBody | Request body |
| @Transactional | Transaction management |
| @Cacheable | Caching |
| @Async | Async processing |

### REST API

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public List<UserDTO> getAll() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody CreateUserRequest request) {
        UserDTO created = userService.create(request);
        return ResponseEntity.status(201).body(created);
    }
    
    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Maven Commands

| Command | Purpose |
|---------|---------|
| mvn clean | Clean target directory |
| mvn compile | Compile source code |
| mvn test | Run tests |
| mvn package | Create JAR/WAR |
| mvn install | Install to local repo |
| mvn deploy | Deploy to remote repo |
| mvn dependency:tree | Show dependencies |
| mvn versions:set | Set version |

## Git Commands

| Command | Purpose |
|---------|---------|
| git init | Initialize repository |
| git clone url | Clone repository |
| git add . | Stage all changes |
| git commit -m "msg" | Commit changes |
| git push | Push to remote |
| git pull | Pull from remote |
| git branch | List branches |
| git checkout -b name | Create and switch branch |
| git merge branch | Merge branch |
| git log --oneline | View history |
| git stash | Stash changes |
| git stash pop | Apply stashed changes |

## SQL Quick Reference

| Operation | Syntax |
|-----------|--------|
| SELECT | SELECT * FROM table WHERE condition |
| INSERT | INSERT INTO table (col1, col2) VALUES (val1, val2) |
| UPDATE | UPDATE table SET col1 = val1 WHERE condition |
| DELETE | DELETE FROM table WHERE condition |
| JOIN | SELECT * FROM t1 INNER JOIN t2 ON t1.id = t2.id |
| GROUP BY | SELECT col, COUNT(*) FROM table GROUP BY col |
| ORDER BY | SELECT * FROM table ORDER BY col ASC |
| LIMIT | SELECT * FROM table LIMIT 10 OFFSET 20 |

## JVM Flags

| Flag | Purpose |
|------|---------|
| -Xms | Initial heap size |
| -Xmx | Maximum heap size |
| -Xss | Thread stack size |
| -XX:+UseG1GC | Use G1 garbage collector |
| -XX:+UseZGC | Use Z garbage collector |
| -XX:+PrintGCDetails | Print GC details |
| -XX:+HeapDumpOnOutOfMemoryError | Dump heap on OOM |

## Quick Reference Links

| Topic | URL |
|-------|-----|
| Java Documentation | docs.oracle.com |
| Spring Documentation | spring.io/docs |
| Maven Repository | mvnrepository.com |
| Baeldung | baeldung.com |
| Java Design Patterns | java-design-patterns.com |
