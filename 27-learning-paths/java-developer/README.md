# Java Developer Learning Path

Comprehensive roadmap for becoming a proficient Java developer.

## Overview

This learning path covers Java from fundamentals to advanced topics, preparing you for backend development roles.

## Prerequisites

- Basic programming concepts
- Command line familiarity
- Git basics
- OOP concepts

## Learning Path

### Phase 1: Java Fundamentals (4-6 weeks)

#### Week 1-2: Core Java
- [ ] Java syntax and basic types
- [ ] Control structures (if/else, loops)
- [ ] Methods and functions
- [ ] Arrays and strings

**Resources:**
- Oracle Java Tutorials
- "Head First Java" book
- Codecademy Java course

**Practice:**
```java
// Basic Java program
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}

// Array manipulation
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}
```

#### Week 3-4: Object-Oriented Programming
- [ ] Classes and objects
- [ ] Inheritance and polymorphism
- [ ] Encapsulation and abstraction
- [ ] Interfaces and abstract classes

**Practice:**
```java
// Class definition
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void speak() {
        System.out.println(name + " makes a sound");
    }
}

// Inheritance
public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void speak() {
        System.out.println(name + " barks");
    }
}
```

#### Week 5-6: Collections and Generics
- [ ] Lists, Sets, Maps
- [ ] Iterators and for-each loops
- [ ] Generics and type safety
- [ ] Comparable and Comparator

**Practice:**
```java
// ArrayList usage
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
names.remove("Alice");

// HashMap usage
Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 25);
ages.put("Bob", 30);
int aliceAge = ages.get("Alice");

// Generics
public class Box<T> {
    private T content;
    
    public void set(T content) {
        this.content = content;
    }
    
    public T get() {
        return content;
    }
}
```

### Phase 2: Advanced Java (4-6 weeks)

#### Week 7-8: Exception Handling and I/O
- [ ] Try-catch-finally blocks
- [ ] Custom exceptions
- [ ] File I/O operations
- [ ] Serialization

**Practice:**
```java
// Exception handling
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Division by zero");
} finally {
    System.out.println("Cleanup code");
}

// Custom exception
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// File I/O
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

#### Week 9-10: Multithreading and Concurrency
- [ ] Thread creation and management
- [ ] Synchronization and locks
- [ ] Executor framework
- [ ] Concurrent collections

**Practice:**
```java
// Thread creation
Thread thread = new Thread(() -> {
    System.out.println("Running in thread");
});
thread.start();

// ExecutorService
ExecutorService executor = Executors.newFixedThreadPool(5);
for (int i = 0; i < 10; i++) {
    executor.submit(() -> {
        System.out.println("Task executed");
    });
}
executor.shutdown();

// Synchronized block
public class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}
```

#### Week 11-12: Java 8+ Features
- [ ] Lambda expressions
- [ ] Streams API
- [ ] Optional class
- [ ] Functional interfaces

**Practice:**
```java
// Lambda expressions
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(name -> System.out.println(name));

// Streams
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(n -> n)
    .sum();

// Optional
Optional<String> optional = Optional.of("value");
optional.ifPresent(System.out::println);
String result = optional.orElse("default");
```

### Phase 3: Spring Framework (4-6 weeks)

#### Week 13-14: Spring Core
- [ ] Dependency injection
- [ ] IoC container
- [ ] Bean lifecycle
- [ ] Configuration

**Practice:**
```java
// Spring configuration
@Configuration
public class AppConfig {
    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}

// Dependency injection
@Component
public class MyService {
    private final MessageService messageService;
    
    @Autowired
    public MyService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

#### Week 15-16: Spring Boot
- [ ] Auto-configuration
- [ ] Embedded servers
- [ ] REST APIs
- [ ] Data access

**Practice:**
```java
// Spring Boot application
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// REST controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

#### Week 17-18: Spring Data and Security
- [ ] JPA repositories
- [ ] Database operations
- [ ] Spring Security basics
- [ ] Authentication and authorization

**Practice:**
```java
// JPA Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThan(int age);
}

// Spring Security configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
```

### Phase 4: Testing and Tools (2-4 weeks)

#### Week 19-20: Unit Testing
- [ ] JUnit 5 basics
- [ ] Mockito framework
- [ ] Test-driven development
- [ ] Code coverage

**Practice:**
```java
// JUnit test
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testFindUserById() {
        User user = new User(1L, "Alice");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        User found = userService.findById(1L);
        
        assertEquals("Alice", found.getName());
    }
}
```

#### Week 21-22: Build Tools and IDE
- [ ] Maven or Gradle
- [ ] IntelliJ IDEA or Eclipse
- [ ] Debugging techniques
- [ ] Code quality tools

### Phase 5: Project Work (4-6 weeks)

#### Personal Project Ideas
1. **REST API**: Build a CRUD API with Spring Boot
2. **Web Application**: Create a full-stack web app
3. **Microservices**: Design a simple microservices architecture
4. **CLI Tool**: Build a command-line utility

**Project Structure:**
```
my-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── model/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
├── pom.xml
└── README.md
```

## Certification Path

### Recommended Certifications
1. **Oracle Certified Associate: Java SE 8 Programmer**
2. **Oracle Certified Professional: Java SE 11 Developer**
3. **Spring Professional Certification**

### Study Resources
- Oracle certification guides
- Practice exams
- Online courses

## Career Progression

### Junior Java Developer (0-2 years)
- Build basic applications
- Understand OOP principles
- Learn Spring framework basics
- Write unit tests

### Mid-Level Java Developer (2-5 years)
- Design complex systems
- Mentor junior developers
- Lead technical decisions
- Optimize performance

### Senior Java Developer (5+ years)
- Architect solutions
- Make technology choices
- Drive technical strategy
- Mentor teams

## Resources

### Books
- "Head First Java" by Kathy Sierra
- "Effective Java" by Joshua Bloch
- "Spring in Action" by Craig Walls
- "Java Concurrency in Practice" by Brian Goetz

### Online
- Oracle Java Tutorials
- Baeldung.com
- Spring.io documentation
- Java Brains (YouTube)

### Practice
- LeetCode Java problems
- HackerRank Java track
- Codewars kata

## Next Steps

After completing this path:
- 19-case-studies - Learn from real-world examples
- 20-interview-preparation - Prepare for interviews
- 24-certifications - Pursue certifications