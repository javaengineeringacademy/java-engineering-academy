# Java Capstone Project

## Overview

The capstone project integrates all 17 modules into a comprehensive, production-grade Java application. This project demonstrates mastery of Java fundamentals, OOP, collections, multithreading, design patterns, testing, and modern Java features.

## Learning Objectives

- Apply all 17 modules in a real-world scenario
- Design and implement a complete Java application
- Follow production best practices
- Demonstrate architectural thinking
- Create production-ready code with tests

## Prerequisites

- Completion of all 17 Java modules (00-16)
- Understanding of Maven/Gradle build tools
- Git version control knowledge

## History

Capstone projects have been used in computer science education since the 1970s. The concept originated at Carnegie Mellon University as a way to integrate learning across courses. Industry adopted capstone projects for technical interviews and hiring assessments.

## Production Notes

- **Where is it used?** Technical interviews, coding bootcamps, university courses, portfolio projects
- **Why is it useful?** Demonstrates holistic understanding and ability to integrate multiple concepts
- **When should it be avoided?** When learning individual concepts; focus on fundamentals first
- **Alternative?** None — capstone projects are the gold standard for demonstrating Java mastery

## Core Concepts

### Project Architecture

| Layer | Responsibility | Technologies |
|-------|---------------|--------------|
| Presentation | User interface, API endpoints | Spring Boot, REST APIs |
| Business Logic | Domain rules, workflows | OOP, Design Patterns, Functional Programming |
| Data Access | Persistence, caching | JPA, Collections, I/O |
| Infrastructure | Cross-cutting concerns | Logging, Monitoring, Security |

### Module Integration Map

| Module | Capstone Application |
|--------|---------------------|
| 00 - Knowledge Atoms | Immutability, equals/hashCode, memory management |
| 01 - Fundamentals | Variables, control flow, String handling |
| 02 - OOP | Domain model, inheritance, polymorphism |
| 03 - Exceptions | Error handling, custom exceptions |
| 04 - Collections | Data structures, caching |
| 05 - Text Processing | CSV/JSON parsing, report generation |
| 06 - Generics | Type-safe repositories, DTOs |
| 07 - Functional Programming | Stream processing, lambdas |
| 08 - I/O & NIO | File processing, configuration |
| 09 - Multithreading | Async processing, parallel streams |
| 10 - JVM Internals | Performance tuning, GC optimization |
| 11 - Design Patterns | Singleton, Factory, Observer, Strategy |
| 12 - Testing | Unit, Integration, TDD |
| 13 - Reflection | Dynamic loading, dependency injection |
| 14 - Logging | Structured logging, monitoring |
| 15 - Senior Engineer | Code review, architecture decisions |
| 16 - Modern Java | Records, sealed classes, virtual threads |

## Internal Working

### Project Structure

```
capstone-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/          # Domain objects (02-OOP, 16-Records)
│   │   │   ├── service/        # Business logic (07-Functional, 11-Patterns)
│   │   │   ├── repository/     # Data access (04-Collections, 06-Generics)
│   │   │   ├── config/         # Configuration (01-Fundamentals, 08-I/O)
│   │   │   ├── exception/      # Custom exceptions (03-Exceptions)
│   │   │   ├── util/           # Utilities (05-Text, 00-Atoms)
│   │   │   └── App.java        # Entry point (10-JVM, 14-Logging)
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback.xml
│   └── test/
│       ├── java/
│       │   ├── unit/           # Unit tests (12-Testing)
│       │   └── integration/    # Integration tests (12-Testing)
│       └── resources/
├── pom.xml
└── README.md
```

## Syntax

### Project Setup

```java
// Main Application Class
@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("Capstone Application started successfully");
    }
}

// Domain Model (Module 02-OOP + 16-Records)
public record Product(
    Long id,
    String name,
    BigDecimal price,
    ProductCategory category,
    LocalDateTime createdAt
) {
    // Validation (Module 00-Knowledge Atoms)
    public Product {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid ID");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}

// Repository (Module 06-Generics + 04-Collections)
@Repository
public class ProductRepository {
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();
    
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }
    
    public List<Product> findByCategory(ProductCategory category) {
        return products.values().stream()
            .filter(p -> p.category() == category)
            .collect(Collectors.toList());
    }
    
    public Product save(Product product) {
        if (product.id() == null) {
            product = new Product(idGenerator.incrementAndGet(), 
                product.name(), product.price(), product.category(), 
                LocalDateTime.now());
        }
        products.put(product.id(), product);
        return product;
    }
}

// Service (Module 11-Design Patterns - Strategy + 07-Functional)
@Service
@Slf4j
public class ProductService {
    private final ProductRepository repository;
    private final PricingStrategy pricingStrategy;
    
    public ProductService(ProductRepository repository, PricingStrategy pricingStrategy) {
        this.repository = repository;
        this.pricingStrategy = pricingStrategy;
    }
    
    public List<Product> getDiscountedProducts() {
        return repository.findAll().stream()
            .map(p -> new Product(p.id(), p.name(), 
                pricingStrategy.calculatePrice(p.price()), 
                p.category(), p.createdAt()))
            .collect(Collectors.toList());
    }
}

// Exception Handling (Module 03-Exceptions)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }
}

// Testing (Module 12-Testing)
@SpringBootTest
class ProductServiceTest {
    
    @MockBean
    private ProductRepository repository;
    
    @Autowired
    private ProductService service;
    
    @Test
    void shouldApplyDiscountToProducts() {
        // Given
        Product product = new Product(1L, "Test", BigDecimal.TEN, 
            ProductCategory.ELECTRONICS, LocalDateTime.now());
        when(repository.findAll()).thenReturn(List.of(product));
        
        // When
        List<Product> discounted = service.getDiscountedProducts();
        
        // Then
        assertThat(discounted).hasSize(1);
        assertThat(discounted.get(0).price()).isLessThan(BigDecimal.TEN);
    }
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Collections | Choose right data structure | ConcurrentHashMap for concurrent access |
| Streams | Avoid in hot loops | Use primitive streams; parallel for large datasets |
| Memory | Object pooling | Reuse objects; avoid autoboxing in loops |
| I/O | Buffered operations | Use BufferedInputStream/BufferedOutputStream |
| Threading | Virtual threads | Use for I/O-bound operations (Java 21+) |

## Best Practices

- Do:
  - Follow Single Responsibility Principle
  - Use dependency injection
  - Write tests before code (TDD)
  - Use immutable objects where possible
  - Handle exceptions properly
  - Log important events
  
- Don't:
  - Ignore compiler warnings
  - Use raw types
  - Catch generic Exception
  - Use System.out.println for logging
  - Ignore unchecked cast warnings
  - Use deprecated APIs

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Ignoring exceptions | Silent failures, data corruption | Always handle or log exceptions |
| Using raw types | ClassCastException at runtime | Use proper generics |
| Mutable shared state | Race conditions | Use concurrent collections |
| Platform default encoding | Data corruption across systems | Always specify charset explicitly |
| Ignoring thread safety | Heisenbugs, data races | Document and test thread safety |

## Interview Questions

### Q1: How would you design a rate limiter using Java concepts?
**Answer:** Use Guava's `RateLimiter` with `Semaphore` for concurrent access; apply Singleton pattern; use `ConcurrentHashMap` for tracking; implement exponential backoff with `ScheduledExecutorService`.

### Q2: Explain how you would implement a caching system.
**Answer:** Use `Caffeine` or `Guava Cache` with size-based eviction; implement `CacheLoader` for lazy loading; use `@Cacheable` annotation; monitor cache hit rates with JFR.

### Q3: How do you handle configuration in production?
**Answer:** Use Spring profiles; externalize config with `application.properties`; use `@ConfigurationProperties` for type-safe config; implement config refresh with `@RefreshScope`.

### Q4: Describe your approach to logging in production.
**Answer:** Use SLF4J with Logback; implement structured logging (JSON); use MDC for context; log at appropriate levels; implement correlation IDs for distributed tracing.

### Q5: How do you ensure code quality in a team?
**Answer:** Enforce code review; use static analysis (SonarQube); implement TDD; use CI/CD pipelines; maintain coding standards; conduct regular knowledge sharing.

### Q6: Explain your approach to performance optimization.
**Answer:** Profile first with JFR/async-profiler; identify hotspots; optimize algorithmically; use appropriate data structures; cache frequently accessed data; tune JVM parameters.

### Q7: How do you handle security in Java applications?
**Answer:** Use Spring Security; implement authentication/authorization; validate all inputs; use parameterized queries; encrypt sensitive data; follow OWASP guidelines.

### Q8: Describe your approach to testing.
**Answer:** Use TDD for new features; unit tests for business logic; integration tests for components; use Mockito for mocking; implement test data builders; achieve 80%+ coverage.

### Q9: How do you manage dependencies in a Java project?
**Answer:** Use Maven/Gradle; define dependency management; use BOM for version consistency; exclude transitive dependencies; implement dependency scanning; use private repositories.

### Q10: Explain your approach to database access.
**Answer:** Use JPA/Hibernate; implement repository pattern; use connection pooling (HikariCP); implement second-level cache; use batch processing; implement proper transaction management.

### Q11: How do you handle internationalization?
**Answer:** Use `ResourceBundle` for messages; implement `LocaleResolver`; use `MessageSource` in Spring; support Unicode throughout; implement number/date formatting per locale.

### Q12: Describe your approach to API design.
**Answer:** Follow REST principles; use proper HTTP methods; implement versioning; use DTOs for responses; implement pagination; use HATEOAS for discoverability.

### Q13: How do you handle file processing in production?
**Answer:** Use NIO.2 for modern file operations; implement streaming for large files; use try-with-resources; handle encoding explicitly; implement retry mechanisms; use batch processing.

### Q14: Explain your approach to monitoring and observability.
**Answer:** Use Micrometer for metrics; implement distributed tracing with Sleuth; use structured logging; monitor JVM metrics; implement health checks; use alerting.

### Q15: How do you ensure application scalability?
**Answer:** Design stateless services; use caching; implement async processing; use message queues; horizontal scaling; load testing; performance monitoring.

## Cross-References

- **Previous Module:** [16 - Modern Java Features](../16-modern-java/)
- **Related:** All modules (00-16)
- **External:** [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- **External:** [Baeldung Tutorials](https://www.baeldung.com/)

## Production Incidents

### Incident 1: N+1 Query Problem in Production

**Problem:** A REST API endpoint for order details was taking 5 seconds instead of 200ms under load.
**Cause:** JPA relationship loading caused N+1 queries — each order item triggered a separate SQL query.
**Impact:** API timeouts; 50% of requests failed; customer complaints.
**Detection:** SQL logging showed 1000+ queries per request; profiler identified N+1 pattern.
**Solution:** Added `@BatchSize` and `JOIN FETCH` queries; implemented DTOs with projections.
**Prevention:** Use `@EntityGraph` or `JOIN FETCH`; implement query monitoring; use DTOs for read operations.

### Incident 2: Memory Leak from ThreadLocal Usage

**Problem:** Application memory grew linearly over 24 hours until OutOfMemoryError.
**Cause:** ThreadLocal variables in servlet filters weren't cleaned up; thread pool threads retained references.
**Impact:** Application crashed every 24 hours; required restart; affected 10,000+ users.
**Detection:** Heap dumps showed ThreadLocalMap entries growing unbounded.
**Solution:** Added `finally` blocks to clean ThreadLocal; used `InheritableThreadLocal` with cleanup.
**Prevention:** Always clean ThreadLocal in finally blocks; use request-scoped beans instead; monitor ThreadLocal usage.

### Incident 3: Distributed Transaction Consistency Issue

**Problem:** Order creation and inventory update were inconsistent — orders existed without inventory reservations.
**Cause:** Two separate services updated different databases without distributed transaction coordination.
**Impact:** 500+ orders without inventory; manual reconciliation required; customer complaints.
**Detection:** Daily reconciliation job found inconsistencies; database queries showed orphaned records.
**Solution:** Implemented Saga pattern with compensation; added idempotency keys; used event sourcing.
**Prevention:** Use Saga pattern for distributed transactions; implement compensation logic; add idempotency.

### Incident 4: Connection Pool Exhaustion Under Load

**Problem:** Database connection pool exhausted under high load; all requests timed out.
**Cause:** Default HikariCP pool size (10) was too small for 200 concurrent users.
**Impact:** Complete service outage for 15 minutes; 1000+ failed requests.
**Detection:** Health checks failed; connection pool metrics showed 100% utilization.
**Solution:** Tuned pool size: `maximumPoolSize=50`; added connection timeout; implemented circuit breaker.
**Prevention:** Load test to determine optimal pool size; monitor connection pool metrics; implement circuit breakers.

### Incident 5: Cache Stampede in Production

**Problem:** Cache expiry caused 10,000 simultaneous database queries; database crashed.
**Cause:** All cache entries expired at the same time; thundering herd problem.
**Impact:** Database downtime for 5 minutes; 10,000+ failed requests; SLA violations.
**Detection:** Database CPU spike to 100%; connection pool exhaustion; cache hit rate dropped to 0%.
**Solution:** Implemented cache warming; added jitter to expiry times; used `ReentrantLock` for cache loading.
**Prevention:** Implement cache warming; add jitter to expiry; use probabilistic early expiration; implement circuit breakers.

## Production Checklist

- [ ] All 17 modules integrated into application
- [ ] Proper exception handling (03-Exceptions)
- [ ] Thread-safe code (09-Multithreading)
- [ ] Unit tests with 80%+ coverage (12-Testing)
- [ ] Integration tests for key workflows
- [ ] Performance testing completed
- [ ] Security review completed
- [ ] Logging configured (14-Logging)
- [ ] Configuration externalized
- [ ] Database migrations versioned
- [ ] API documentation complete
- [ ] Monitoring and alerting configured
- [ ] Deployment automation ready

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Can implement basic features; follows tutorials; needs guidance on architecture |
| Intermediate | Can implement full features; understands patterns; can review junior code |
| Advanced | Can design systems; makes architecture decisions; mentors team |
| Expert | Can design distributed systems; makes technology choices; leads technical direction |

## Common Myths

| Myth | Reality |
|------|---------|
| "Capstone projects are just for school" | Industry uses similar projects for technical assessments and hiring |
| "One project proves everything" | Projects demonstrate breadth; individual skills need validation through interviews |
| "The code must be perfect" | Learning from mistakes is valuable; clean code matters more than perfection |
| "Use every design pattern" | Patterns solve specific problems; don't force patterns where they're not needed |
| "More features = better project" | Focused, well-implemented features demonstrate skill better than many half-baked ones |

## One-Minute Revision

| Module | Key Takeaway for Capstone |
|--------|---------------------------|
| 00 | Immutability, equals/hashCode, memory management |
| 01 | Variables, control flow, String handling |
| 02 | Domain model, inheritance, polymorphism |
| 03 | Error handling, custom exceptions |
| 04 | Data structures, caching |
| 05 | CSV/JSON parsing, report generation |
| 06 | Type-safe repositories, DTOs |
| 07 | Stream processing, lambdas |
| 08 | File processing, configuration |
| 09 | Async processing, parallel streams |
| 10 | Performance tuning, GC optimization |
| 11 | Singleton, Factory, Observer, Strategy |
| 12 | Unit, Integration, TDD |
| 13 | Dynamic loading, dependency injection |
| 14 | Structured logging, monitoring |
| 15 | Code review, architecture decisions |
| 16 | Records, sealed classes, virtual threads |
