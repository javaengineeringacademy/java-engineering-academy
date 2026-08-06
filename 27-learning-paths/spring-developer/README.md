# Spring Developer Learning Path

Comprehensive roadmap for mastering the Spring ecosystem.

## Overview

This learning path covers Spring Framework from core concepts to advanced topics like Spring Cloud and microservices.

## Prerequisites

- Java fundamentals (see Java Developer path)
- Basic understanding of web applications
- REST API concepts
- Database basics

## Learning Path

### Phase 1: Spring Core (3-4 weeks)

#### Week 1-2: Dependency Injection and IoC
- [ ] IoC container concepts
- [ ] Bean definition and lifecycle
- [ ] Dependency injection types
- [ ] Configuration (XML, Annotations, Java Config)

**Resources:**
- Spring Framework documentation
- "Spring in Action" by Craig Walls
- Spring.io guides

**Practice:**
```java
// Bean definition
@Component
public class MyService {
    private final MyRepository repository;
    
    @Autowired
    public MyService(MyRepository repository) {
        this.repository = repository;
    }
}

// Configuration
@Configuration
@ComponentScan("com.example")
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService(myRepository());
    }
    
    @Bean
    public MyRepository myRepository() {
        return new MyRepositoryImpl();
    }
}
```

#### Week 3-4: Spring AOP and Events
- [ ] Aspect-Oriented Programming
- [ ] Cross-cutting concerns
- [ ] Event handling
- [ ] Custom annotations

**Practice:**
```java
// AOP aspect
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Calling: " + joinPoint.getSignature().getName());
    }
    
    @After("execution(* com.example.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Completed: " + joinPoint.getSignature().getName());
    }
}

// Custom annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    String action() default "";
}

// Event handling
@Component
public class UserEventHandler {
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("User created: " + event.getUserId());
    }
}
```

### Phase 2: Spring Boot (4-6 weeks)

#### Week 5-6: Spring Boot Fundamentals
- [ ] Auto-configuration
- [ ] Embedded servers
- [ ] Properties and profiles
- [ ] Actuator and monitoring

**Practice:**
```java
// Spring Boot application
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Application properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.jpa.hibernate.ddl-auto=update

// Profiles
spring.profiles.active=dev

// application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb

// application-prod.properties
spring.datasource.url=jdbc:mysql://prod-server:3306/proddb
```

#### Week 7-8: RESTful APIs
- [ ] Controller annotations
- [ ] Request mapping
- [ ] Data transfer objects
- [ ] Exception handling

**Practice:**
```java
// REST controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
        return ResponseEntity.badRequest().body(error);
    }
}
```

#### Week 9-10: Data Access with Spring Data
- [ ] JPA repositories
- [ ] Query methods
- [ ] Custom queries
- [ ] Transactions

**Practice:**
```java
// Entity
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    
    // Getters and setters
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThan(int age);
    List<User> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateUserName(@Param("id") Long id, @Param("name") String name);
}

// Service with transactions
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
            .map(this::toDTO);
    }
    
    public UserDTO createUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        User saved = userRepository.save(user);
        return toDTO(saved);
    }
}
```

### Phase 3: Spring Security (3-4 weeks)

#### Week 11-12: Authentication and Authorization
- [ ] Security configuration
- [ ] User authentication
- [ ] Role-based access control
- [ ] OAuth2 integration

**Practice:**
```java
// Security configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://your-auth-server/.well-known/jwks.json")
            .build();
    }
}

// Custom user details service
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))
            .build();
    }
}
```

#### Week 13-14: JWT and API Security
- [ ] JWT token generation
- [ ] Token validation
- [ ] Refresh tokens
- [ ] API key authentication

**Practice:**
```java
// JWT utility
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
        return claimsResolver.apply(claims);
    }
}
```

### Phase 4: Spring Cloud (3-4 weeks)

#### Week 15-16: Service Discovery and Configuration
- [ ] Eureka server and client
- [ ] Config server
- [ ] Dynamic configuration
- [ ] Feature flags

**Practice:**
```java
// Eureka server
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

// Eureka client
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

// Config server application.yml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-org/config-repo
          default-label: main
```

#### Week 17-18: API Gateway and Circuit Breakers
- [ ] Spring Cloud Gateway
- [ ] Circuit breaker patterns
- [ ] Rate limiting
- [ ] Load balancing

**Practice:**
```java
// Gateway configuration
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: user-service
                fallbackUri: forward:/fallback/users
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20

// Circuit breaker
@Service
public class UserService {
    
    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackGetUser")
    public UserDTO getUser(Long id) {
        return restTemplate.getForObject(
            "http://user-service/api/users/" + id, 
            UserDTO.class);
    }
    
    public UserDTO fallbackGetUser(Long id, Exception e) {
        return new UserDTO(id, "Fallback User", "fallback@example.com");
    }
}
```

### Phase 5: Testing and Best Practices (2-3 weeks)

#### Week 19-20: Testing
- [ ] Unit testing with JUnit 5
- [ ] Integration testing
- [ ] MockMvc for REST APIs
- [ ] Test containers

**Practice:**
```java
// Unit test
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    public void shouldReturnUser() throws Exception {
        UserDTO user = new UserDTO(1L, "Alice", "alice@example.com");
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Alice"));
    }
    
    @Test
    public void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isNotFound());
    }
}

// Integration test with Testcontainers
@SpringBootTest
@Testcontainers
public class UserRepositoryTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void shouldSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        
        User saved = userRepository.save(user);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }
}
```

## Project Ideas

### 1. E-commerce API
- User management
- Product catalog
- Order processing
- Payment integration

### 2. Blog Platform
- User authentication
- Post CRUD operations
- Comments system
- Search functionality

### 3. Task Management System
- User roles and permissions
- Project and task management
- Time tracking
- Reporting

## Certification Path

### Recommended Certifications
- **Spring Professional Certification**
- **Oracle Certified Professional: Java SE**

### Study Resources
- VMware Spring Professional certification guide
- Spring.io official documentation
- Practice exams

## Career Progression

### Junior Spring Developer (0-2 years)
- Build REST APIs
- Understand Spring basics
- Write unit tests
- Learn database operations

### Mid-Level Spring Developer (2-5 years)
- Design complex systems
- Implement security
- Use Spring Cloud
- Mentor junior developers

### Senior Spring Developer (5+ years)
- Architect microservices
- Make technology choices
- Drive technical strategy
- Lead teams

## Resources

### Books
- "Spring in Action" by Craig Walls
- "Spring Boot in Action" by Craig Walls
- "Cloud Native Java" by Kenneth Kousen

### Online
- Spring.io documentation
- Baeldung.com
- Spring Guides
- YouTube: Spring Developer

### Practice
- Spring PetClinic example
- Spring Boot samples
- Open source contributions

## Next Steps

After completing this path:
- 19-case-studies - Learn from real-world examples
- 20-interview-preparation - Prepare for interviews
- 24-certifications - Pursue certifications