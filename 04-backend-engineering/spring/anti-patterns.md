# Spring Anti-Patterns

## 1. Circular Dependencies
**Description:** Having beans that depend on each other in a cycle.

**Why it's bad:** Causes startup failures, indicates poor design, makes code hard to understand.

**Example (bad code):**
```java
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB;
}

@Service
public class ServiceB {
    @Autowired
    private ServiceA serviceA;
}
```

**Better approach:** Extract common logic to a third service or use setter injection:
```java
@Service
public class CommonService {
    // shared logic
}

@Service
public class ServiceA {
    private final CommonService commonService;
    public ServiceA(CommonService commonService) {
        this.commonService = commonService;
    }
}
```

**Impact:** Clean dependency graph, easier to understand and test.

---

## 2. God Service
**Description:** A service class that handles too many responsibilities.

**Why it's bad:** Violates Single Responsibility Principle, hard to test and maintain.

**Example (bad code):**
```java
@Service
public class UserService {
    public void createUser() { /* ... */ }
    public void sendEmail() { /* ... */ }
    public void generateReport() { /* ... */ }
    public void processPayment() { /* ... */ }
}
```

**Better approach:** Split into focused services:
```java
@Service
public class UserService { /* ... */ }

@Service
public class EmailService { /* ... */ }

@Service
public class ReportService { /* ... */ }

@Service
public class PaymentService { /* ... */ }
```

**Impact:** Easier testing, better separation of concerns.

---

## 3. Field Injection with @Autowired
**Description:** Using @Autowired on fields instead of constructor injection.

**Why it's bad:** Makes testing difficult, hides dependencies, not immutable.

**Example (bad code):**
```java
@Service
public class MyService {
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private EmailService emailService;
}
```

**Better approach:** Use constructor injection:
```java
@Service
public class MyService {
    private final UserRepository repository;
    private final EmailService emailService;
    
    public MyService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }
}
```

**Impact:** Immutable dependencies, easier testing, explicit dependencies.

---

## 4. N+1 Query Problem
**Description:** Fetching related entities in a loop instead of using joins or fetch joins.

**Why it's bad:** Causes excessive database queries, degrades performance.

**Example (bad code):**
```java
List<Order> orders = orderRepository.findAll();
for (Order order : orders) {
    // triggers separate query for each order
    Customer customer = order.getCustomer();
}
```

**Better approach:** Use @EntityGraph or JOIN FETCH:
```java
@Query("SELECT o FROM Order o JOIN FETCH o.customer")
List<Order> findAllWithCustomer();

// or
@EntityGraph(attributePaths = {"customer"})
List<Order> findAll();
```

**Impact:** Reduces database queries from N+1 to 1 or 2.

---

## 5. Using @Transactional on Private Methods
**Description:** Trying to use @Transactional on private methods.

**Why it's bad:** Spring AOP cannot intercept private methods, transaction won't work.

**Example (bad code):**
```java
@Service
public class MyService {
    @Transactional
    private void doSomething() {
        // transaction won't apply
    }
}
```

**Better approach:** Use public methods or self-injection:
```java
@Service
public class MyService {
    @Transactional
    public void doSomething() {
        // transaction applies
    }
}
```

**Impact:** Transactions work correctly, proper transaction management.

---

## 6. Catching Exceptions in Controllers
**Description:** Handling exceptions in controllers instead of using @ControllerAdvice.

**Why it's bad:** Duplicated error handling, inconsistent error responses.

**Example (bad code):**
```java
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            return new User(); // inconsistent
        }
    }
}
```

**Better approach:** Use @ControllerAdvice:
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
    }
}
```

**Impact:** Consistent error handling, DRY principle.

---

## 7. Not Using DTOs
**Description:** Returning entity objects directly from controllers.

**Why it's bad:** Exposes internal structure, couples API to database, potential security issues.

**Example (bad code):**
```java
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

**Better approach:** Use DTOs:
```java
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toDTO(user);
    }
}
```

**Impact:** API stability, security, separation of concerns.

---

## 8. Ignoring Connection Pool Configuration
**Description:** Using default database connection pool settings.

**Why it's bad:** Can cause connection leaks, poor performance under load.

**Example (bad code):**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost/db
    # no pool configuration
```

**Better approach:** Configure connection pool:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

**Impact:** Better performance, prevents connection leaks.

---

## 9. Using @RestController Without @Validated
**Description:** Not validating request parameters.

**Why it's bad:** Allows invalid data, can cause runtime errors or security issues.

**Example (bad code):**
```java
@RestController
public class UserController {
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }
}
```

**Better approach:** Add validation:
```java
@RestController
@Validated
public class UserController {
    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }
}

public class User {
    @NotBlank
    private String name;
    
    @Email
    private String email;
}
```

**Impact:** Input validation, prevents invalid data.

---

## 10. Overusing @PostConstruct
**Description:** Putting too much logic in @PostConstruct methods.

**Why it's bad:** Slows startup, hard to test, can cause circular dependencies.

**Example (bad code):**
```java
@Service
public class MyService {
    @PostConstruct
    public void init() {
        // complex initialization logic
        // database queries
        // external API calls
    }
}
```

**Better approach:** Use ApplicationRunner or CommandLineRunner:
```java
@Component
public class MyStartupRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        // initialization logic
    }
}
```

**Impact:** Better testability, cleaner startup process.

---

## 11. Not Using Profiles
**Description:** Hardcoding environment-specific configurations.

**Why it's bad:** Different configs for different environments, error-prone deployments.

**Example (bad code):**
```yaml
# application.yml - same for all environments
spring:
  datasource:
    url: jdbc:mysql://prod-server/db
```

**Better approach:** Use profiles:
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost/devdb

# application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://prod-server/db
```

**Impact:** Environment-specific configs, easier deployments.

---

## 12. Ignoring Bean Scopes
**Description:** Not considering bean scopes (singleton, prototype, request, session).

**Why it's bad:** Stateful beans as singletons cause issues, wrong scope affects performance.

**Example (bad code):**
```java
@Service // default singleton
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();
    // shared across all requests - problem!
}
```

**Better approach:** Use appropriate scope:
```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();
}
```

**Impact:** Correct state management, better performance.