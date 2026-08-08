# 09 — Real-World Use Cases

## Why This Topic Matters

You have learned how reflection and annotations work in theory. Now you need to see how every major Java framework actually uses them. Understanding these real-world patterns helps you debug framework issues, make informed architectural decisions, and appreciate the tradeoffs frameworks make.

---

## Spring Framework

### @Autowired — Dependency Injection

Spring uses reflection to scan for @Autowired annotations and inject dependencies:

```java
@Service
public class UserService {
    @Autowired
    private UserRepository repository;  // Spring injects via reflection
    
    @Autowired
    private EmailService emailService;
    
    public UserService(UserRepository repository) {  // Constructor injection
        this.repository = repository;
    }
}

// How Spring does it (simplified):
// 1. Scan for @Service, @Component, etc.
// 2. Create instances via Constructor.newInstance()
// 3. Find @Autowired fields via getDeclaredFields()
// 4. field.setAccessible(true)
// 5. field.set(instance, dependency)
```

### @RequestMapping — Web Routing

Spring MVC uses reflection to map HTTP endpoints:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return repository.findById(id);
    }
    
    @PostMapping
    @ResponseBody
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }
}

// Spring does:
// 1. Find classes with @RestController
// 2. Find methods with @GetMapping, @PostMapping
// 3. Read @RequestMapping values to build URL map
// 4. At runtime, use Method.invoke() to call the right handler
```

### @Transactional — AOP Proxy

Spring creates dynamic proxies for transaction management:

```java
@Service
public class OrderService {
    @Transactional
    public void placeOrder(Order order) {
        // This entire method runs inside a transaction
        repository.save(order);
        inventory.reserve(order);
        payment.charge(order);
        // If any exception: all changes rolled back
    }
}

// Spring wraps OrderService in a proxy:
// 1. Proxy.newProxyInstance() creates OrderService$$EnhancerBySpringCGLIB
// 2. Before method: open transaction
// 3. Call real method via Method.invoke()
// 4. After method: commit (or rollback on exception)
```

---

## JPA (Java Persistence API)

### @Entity — ORM Mapping

JPA uses reflection to map Java objects to database tables:

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @Enumerated(EnumType.STRING)
    private Status status;
}

// JPA does:
// 1. Find @Entity classes at startup
// 2. Read @Table for table name
// 3. Read @Column for column mappings
// 4. Read @Id for primary key
// 5. Generate SQL: INSERT INTO users (username, status) VALUES (?, ?)
// 6. At runtime: use reflection to read field values for SQL parameters
```

### @Query — Custom Queries

```java
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);
    
    @Query(value = "SELECT * FROM users WHERE created_at > ?1", nativeQuery = true)
    List<User> findRecentUsers(Date since);
}
```

---

## JUnit

### @Test — Test Discovery

JUnit uses reflection to find and run test methods:

```java
public class UserServiceTest {
    @Test
    void shouldCreateUser() {
        // Test implementation
    }
    
    @Test
    @Disabled("Not yet implemented")
    void shouldValidateEmail() {
        // This test is skipped
    }
    
    @BeforeEach
    void setUp() {
        // Runs before each test
    }
    
    @AfterEach
    void tearDown() {
        // Runs after each test
    }
}

// JUnit does:
// 1. Find all classes ending in "Test" (or with @Test methods)
// 2. Find methods annotated with @Test via getDeclaredMethods()
// 3. Check if @Disabled is present — skip if so
// 4. Find @BeforeEach methods — call before each test
// 5. Call test method via Method.invoke()
// 6. Catch exceptions — report as test failures
```

### @ParameterizedTest

```java
@ParameterizedTest
@ValueSource(strings = {"racecar", "radar", "level"})
void isPalindrome(String word) {
    assertTrue(isPalindrome(word));
}

// JUnit reads @ValueSource, creates test instances, calls method for each value
```

---

## Jackson (JSON Serialization)

### @JsonProperty — Field Mapping

```java
public class User {
    @JsonProperty("user_name")
    private String name;
    
    @JsonProperty("user_age")
    private int age;
    
    @JsonIgnore
    private String password;  // Not serialized to JSON
}

// Jackson does:
// 1. Find @JsonProperty on fields
// 2. Use field.getAnnotation(JsonProperty.class) to get JSON key name
// 3. Use field.get(obj) to read values
// 4. Build JSON: {"user_name": "Alice", "user_age": 30}
```

### @JsonCreator — Deserialization

```java
public class User {
    private final String name;
    private final int age;
    
    @JsonCreator
    public User(@JsonProperty("user_name") String name,
                @JsonProperty("user_age") int age) {
        this.name = name;
        this.age = age;
    }
}

// Jackson uses Constructor.newInstance() with parameter matching
```

---

## Lombok

### @Data — Code Generation

```java
@Data
public class User {
    private String name;
    private int age;
}

// Lombok generates at COMPILE TIME:
// - getName(), setName()
// - getAge(), setAge()
// - toString(), equals(), hashCode()
// - RequiredArgsConstructor

// Lombok uses annotation processing (compile-time) + bytecode manipulation
// It does NOT use reflection at runtime — that is its performance advantage
```

### @Slf4j — Logger Injection

```java
@Slf4j
public class UserService {
    public void saveUser(User user) {
        log.info("Saving user: {}", user.getName());
        // log is generated by Lombok as:
        // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    }
}
```

---

## Hibernate

### @Entity — ORM with Lazy Loading

```java
@Entity
public class User {
    @Id
    private Long id;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;  // Proxy created at runtime
}

// Hibernate creates a proxy for lazy-loaded collections:
// 1. orders field is actually a PersistentSet proxy
// 2. When you call orders.size(), the proxy loads from DB
// 3. This is done via ByteBuddy/CGLIB creating a subclass at runtime
```

---

## Guice — Dependency Injection

```java
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(JpaUserRepository.class);
    }
}

// Guice uses reflection to:
// 1. Find @Inject constructors/fields
// 2. Create instances via Constructor.newInstance()
// 3. Resolve dependencies recursively
// 4. Inject via field.set() or constructor invocation
```

---

## Serialization Frameworks

### Custom Serialization

```java
public class JsonSerializer {
    public String toJson(Object obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder("{");
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(obj);
            
            sb.append("\"").append(fields[i].getName()).append("\":");
            
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value == null) {
                sb.append("null");
            } else {
                sb.append(toJson(value)); // Recursive
            }
            
            if (i < fields.length - 1) sb.append(",");
        }
        
        sb.append("}");
        return sb.toString();
    }
}
```

---

## ORM Frameworks — Pattern Summary

| Framework | Annotation | Reflection Usage |
|-----------|-----------|-----------------|
| Spring | @Autowired, @Service, @Transactional | Field injection, proxy creation |
| JPA/Hibernate | @Entity, @Column, @Id | Field access, SQL generation |
| JUnit | @Test, @BeforeEach, @BeforeAll | Method discovery, invocation |
| Jackson | @JsonProperty, @JsonIgnore | Field read/write for serialization |
| Lombok | @Data, @Getter, @Setter | Compile-time only (no runtime reflection) |
| Guice | @Inject | Constructor/field injection |

---

## Performance Impact in Frameworks

| Operation | Frequency | Reflection Cost |
|-----------|-----------|----------------|
| Spring DI startup | Once at boot | Acceptable |
| JPA entity mapping | Once at boot | Acceptable |
| JUnit test discovery | Once per test run | Acceptable |
| Jackson serialization | Per request | High — mitigated by caching |
| Hibernate lazy loading | Per access | High — mitigated by proxy caching |

---

## Code Review Checklist

- [ ] Are annotations used for metadata, not logic?
- [ ] Are annotation processors compile-time (not runtime) when possible?
- [ ] Are reflection operations cached (Method, Field objects)?
- [ ] Is the performance impact of runtime reflection acceptable?
- [ ] Are errors from reflection wrapped in meaningful messages?
- [ ] Is Java module system compatibility considered?

---

## Debugging Tips

1. **Spring:** Enable `--debug` to see bean creation logs
2. **JPA:** Set `hibernate.show_sql=true` to see generated SQL
3. **JUnit:** Use `@DisplayName` for readable test output
4. **Jackson:** Use `objectMapper.writerWithDefaultPrettyPrinter()` for debugging
5. **Hibernate:** Enable `hibernate.show_sql` and `hibernate.format_sql`

---

## Interview Questions

1. How does Spring implement @Autowired?
2. What is the difference between compile-time and runtime annotation processing?
3. Why does Lombok not use runtime reflection?
4. How does JPA map Java objects to database tables?
5. What is a dynamic proxy and how does Spring use it for @Transactional?

---

## Summary

| Framework | Key Annotations | Reflection Used |
|-----------|----------------|----------------|
| Spring | @Autowired, @Service | Field injection, proxy creation |
| JPA | @Entity, @Column | Field access, SQL generation |
| JUnit | @Test, @BeforeEach | Method discovery, invocation |
| Jackson | @JsonProperty | Field read/write |
| Lombok | @Data, @Getter | Compile-time only |
| Hibernate | @Entity, @OneToMany | Proxy creation, lazy loading |

---

*This concludes the Reflection and Annotations module. Return to [Module README](../README.md)*
