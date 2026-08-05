# Spring Best Practices

## 1. Use Constructor Injection

```java
// Recommended
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// Avoid field injection
@Service
public class UserService {
    @Autowired
    private UserRepository repository; // Not recommended
}
```

## 2. Prefer Immutability

```java
// Use records for DTOs
public record UserDTO(Long id, String name, String email) {}

// Use final fields
@Service
public class UserService {
    private final UserRepository repository;
    private final CacheManager cacheManager;
}
```

## 3. Use Proper Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body("Internal server error");
    }
}
```

## 4. Validate Input

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.create(user));
    }
}

@Data
public class UserDTO {
    @NotNull
    private String name;
    
    @Email
    private String email;
}
```

## 5. Use Profiles

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb
spring.jpa.show-sql=true

# application-prod.properties
spring.datasource.url=jdbc:mysql://localhost:3306/proddb
spring.jpa.show-sql=false
```

## 6. Externalize Configuration

```java
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int port = 8080;
}
```

## 7. Use Caching

```java
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

## 8. Use Async for Long Operations

```java
@Service
public class EmailService {
    
    @Async
    public void sendEmail(String to, String subject, String body) {
        // Send email asynchronously
    }
}
```

## 9. Use Proper Logging

```java
@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    public User createUser(UserDTO userDTO) {
        log.info("Creating user: {}", userDTO.name());
        try {
            User user = userRepository.save(toEntity(userDTO));
            log.info("User created: {}", user.getId());
            return user;
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw e;
        }
    }
}
```

## 10. Write Tests

```java
@SpringBootTest
class UserServiceTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    void shouldCreateUser() {
        // Given
        UserDTO dto = new UserDTO(null, "John", "john@example.com");
        User user = new User(1L, "John", "john@example.com");
        when(userRepository.save(any())).thenReturn(user);
        
        // When
        User result = userService.createUser(dto);
        
        // Then
        assertThat(result.getName()).isEqualTo("John");
        verify(userRepository).save(any());
    }
}
```

## 11. Use Transaction Management

```java
@Service
public class OrderService {
    
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        inventoryService.updateStock(order);
        paymentService.processPayment(order);
    }
}
```

## 12. Use Proper REST Design

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
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

## 13. Use DTOs

```java
// Don't expose entities directly
@RestController
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(toDTO(user));
    }
}
```

## 14. Use Proper HTTP Status Codes

```java
@PostMapping("/users")
public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
}

@GetMapping("/{id}")
public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

## 15. Use Proper Content Negotiation

```java
@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<UserDTO>> getAllJson() { }

@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
public ResponseEntity<List<UserDTO>> getAllXml() { }
```

## 16. Use Pagination

```java
@GetMapping
public ResponseEntity<Page<UserDTO>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(userService.findAll(PageRequest.of(page, size)));
}
```

## 17. Use Proper Error Responses

```java
public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {}

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(
            404,
            ex.getMessage(),
            LocalDateTime.now()
        ));
    }
}
```

## 18. Use Health Checks

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Check health
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}
```

## 19. Use Proper Security

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
}
```

## 20. Monitor Your Application

```java
@Component
public class Metrics {
    
    private final Counter requestCounter;
    private final Timer requestTimer;
    
    public Metrics(MeterRegistry registry) {
        this.requestCounter = Counter.builder("app.requests")
            .description("Total requests")
            .register(registry);
        
        this.requestTimer = Timer.builder("app.request.time")
            .description("Request duration")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }
}
```
