# Spring Interview Questions

## Basic Questions

### 1. What is Spring Framework?

Spring is an open-source framework for building enterprise Java applications. It provides:
- IoC (Inversion of Control) container
- Dependency Injection
- AOP (Aspect-Oriented Programming)
- Transaction management
- MVC framework
- Integration with other frameworks

### 2. What is IoC and Dependency Injection?

**IoC (Inversion of Control)**: Container manages object creation and lifecycle.
**Dependency Injection**: Objects receive their dependencies from the container.

```java
// Without IoC/DI
public class UserService {
    private UserRepository repository = new UserRepository(); // Tight coupling
}

// With IoC/DI
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) { // Loose coupling
        this.repository = repository;
    }
}
```

### 3. What are Spring Beans?

Spring Beans are objects managed by the Spring IoC container. They are:
- Created by the container
- Configured via annotations or XML
- Managed lifecycle (init, destroy)
- Scoped (singleton, prototype, request, session)

### 4. What is the difference between @Component, @Service, @Repository, @Controller?

| Annotation | Use Case |
|-----------|----------|
| `@Component` | Generic stereotype |
| `@Service` | Business logic |
| `@Repository` | Data access (with exception translation) |
| `@Controller` | MVC controllers |
| `@RestController` | REST API controllers |

### 5. What is Spring Boot?

Spring Boot is an extension of Spring Framework that:
- Auto-configures Spring applications
- Provides embedded servers
- Simplifies dependency management
- Enables production-ready features
- Requires minimal configuration

## Intermediate Questions

### 6. What is Spring AOP?

Spring AOP enables:
- Cross-cutting concerns (logging, security, transactions)
- Separation of business logic from infrastructure code
- Dynamic proxy generation

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Method called: {}", joinPoint.getSignature().getName());
    }
}
```

### 7. What is the difference between @Transactional and programmatic transactions?

| Feature | @Transactional | Programmatic |
|---------|---------------|--------------|
| Syntax | Annotation | API calls |
| Scope | Method level | Code block |
| Propagation | Configurable | Manual |
| Exception handling | Automatic | Manual |

### 8. What are Spring Profiles?

Profiles allow different configurations for different environments:

```java
@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().build();
    }
}

@Configuration
@Profile("prod")
public class ProdConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```

### 9. What is Spring Security?

Spring Security provides:
- Authentication (login)
- Authorization (access control)
- Protection against attacks (CSRF, XSS)
- Session management
- OAuth2/OpenID Connect support

### 10. What is the difference between @Autowired and @Resource?

| Feature | @Autowired | @Resource |
|---------|-----------|-----------|
| Injection | By type | By name |
| Required | Required by default | Optional |
| Source | Spring | JSR-250 |
| Scope | Fields, constructors, setters | Fields, setters |

## Advanced Questions

### 11. What is Spring Cloud?

Spring Cloud provides:
- Service discovery (Eureka)
- Configuration management (Config Server)
- Circuit breakers (Hystrix/Resilience4j)
- API Gateway (Spring Cloud Gateway)
- Distributed tracing (Sleuth)

### 12. What is Spring Data JPA?

Spring Data JPA provides:
- Repository abstraction
- Automatic query generation
- Pagination and sorting
- Auditing
- Query methods

```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    Optional<User> findByUsernameAndPassword(String username, String password);
}
```

### 13. What is the difference between @RequestParam and @PathVariable?

```java
// @RequestParam - Query parameters
@GetMapping("/users")
public List<User> getUsers(@RequestParam(defaultValue = "0") int page) { }

// @PathVariable - Path segments
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) { }
```

### 14. What is Spring WebFlux?

Spring WebFlux is:
- Reactive web framework
- Non-blocking I/O
- Functional endpoints
- Backpressure support
- Reactive streams

### 15. What is the difference between Mono and Flux?

| Type | Description |
|------|-------------|
| `Mono<T>` | 0 or 1 element |
| `Flux<T>` | 0 to N elements |

```java
@GetMapping("/user/{id}")
public Mono<User> getUser(@PathVariable Long id) {
    return userService.findById(id);
}

@GetMapping("/users")
public Flux<User> getAllUsers() {
    return userService.findAll();
}
```

## System Design Questions

### 16. Design a REST API with Spring Boot

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() { }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) { }
    
    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO user) { }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO user) { }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) { }
}
```

### 17. Design a caching layer

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "orders");
    }
}

@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### 18. How do you handle exceptions in Spring Boot?

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body("Internal server error");
    }
}
```

### 19. How do you secure a Spring Boot application?

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 20. How do you test a Spring Boot application?

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldReturnUser() throws Exception {
        User user = new User(1L, "John", "john@example.com");
        when(userService.getUserById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```
