# Spring Core Concepts

## Dependency Injection (DI)

### Types of Injection

```java
// Constructor injection (recommended)
@Service
public class UserService {
    private final UserRepository repository;
    
    @Autowired  // Optional if single constructor
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// Setter injection
@Service
public class UserService {
    private UserRepository repository;
    
    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}

// Field injection (not recommended)
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
}
```

### Constructor vs Setter vs Field

| Type | Pros | Cons |
|------|------|------|
| Constructor | Immutable, testable, required deps | Verbose |
| Setter | Optional deps, reconfigurable | Mutable |
| Field | Concise, readable | Hard to test, hidden deps |

## IoC (Inversion of Control)

### What IoC Does

- Container creates and manages objects
- Dependencies injected automatically
- Lifecycle managed by container
- Configuration externalized

### Benefits

- Loose coupling
- Easier testing
- Centralized configuration
- Lifecycle management

## Bean Definition

### Annotation-Based

```java
@Component
@Service
@Repository
@Controller
@RestController
@Configuration
```

### Java Configuration

```java
@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
    
    @Bean
    @Scope("prototype")
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }
}
```

### Component Scanning

```java
@SpringBootApplication
// Scans package and sub-packages
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Custom scan path
@ComponentScan(basePackages = "com.example")
```

## Profiles

### Configuration Profiles

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
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        return new HikariDataSource(config);
    }
}
```

### Profile Properties

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb
spring.jpa.show-sql=true

# application-prod.properties
spring.datasource.url=jdbc:mysql://localhost:3306/proddb
spring.jpa.show-sql=false
```

### Activate Profiles

```properties
spring.profiles.active=dev
```

```java
@SpringBootTest
@ActiveProfiles("test")
class MyTest {
    // Test-specific configuration
}
```

## Events

### Application Events

```java
// Custom event
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;
    
    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
}

// Publishing events
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher publisher;
    
    public void createUser(User user) {
        // Save user
        userRepository.save(user);
        
        // Publish event
        publisher.publishEvent(new UserCreatedEvent(this, user));
    }
}

// Listening to events
@Component
public class UserEventListener {
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("User created: " + event.getUser().getName());
    }
    
    @EventListener(condition = "#event.user.age > 18")
    public void handleAdultUser(UserCreatedEvent event) {
        // Only for adult users
    }
}
```

### Async Events

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }
}

@Component
public class UserEventListener {
    @Async
    @EventListener
    public void handleUserCreatedAsync(UserCreatedEvent event) {
        // Process asynchronously
    }
}
```

## Validation

### Bean Validation

```java
@Data
public class User {
    @NotNull
    private String name;
    
    @Email
    private String email;
    
    @Min(0)
    @Max(150)
    private Integer age;
    
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
```

### Custom Validation

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findByEmail(email).isEmpty();
    }
}
```

### Validation in Controllers

```java
@RestController
public class UserController {
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userService.createUser(user));
    }
}
```

## Conditionals

```java
@Configuration
public class FeatureConfig {
    @Bean
    @ConditionalOnProperty(name = "feature.logging.enabled", havingValue = "true")
    public LoggingService loggingService() {
        return new LoggingService();
    }
    
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
    
    @Bean
    @ConditionalOnClass(name = "org.postgresql.Driver")
    public DataSource postgresDataSource() {
        return new PgDataSource();
    }
}
```

## Bean Lifecycle Hooks

```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        // Called after properties are set
    }
    
    @Override
    public void destroy() throws Exception {
        // Called when bean is destroyed
    }
    
    @PostConstruct
    public void init() {
        // Called after dependency injection
    }
    
    @PreDestroy
    public void cleanup() {
        // Called before destruction
    }
}
```

## Resource Loading

```java
@Component
public class ResourceLoader {
    @Value("classpath:data.json")
    private Resource dataFile;
    
    @Value("https://example.com/data.json")
    private Resource remoteFile;
    
    public void loadData() throws IOException {
        InputStream is = dataFile.getInputStream();
        // Process data
    }
}
```

## Environment

```java
@Component
public class EnvironmentChecker {
    @Autowired
    private Environment environment;
    
    public void check() {
        String dbUrl = environment.getProperty("spring.datasource.url");
        boolean isDev = environment.acceptsProfiles(Profiles.of("dev"));
    }
}
```

## Property Sources

```java
@Configuration
@PropertySource("classpath:custom.properties")
@PropertySource("classpath:override.properties")
public class PropertyConfig {
    @Value("${custom.property}")
    private String customProperty;
    
    @Value("${database.url:${default.url}}")
    private String dbUrl;
}
```

## SpEL (Spring Expression Language)

```java
@Component
public class SpELExample {
    @Value("#{systemProperties['user.home']}")
    private String homeDir;
    
    @Value("#{T(java.lang.Math).random() * 100}")
    private double randomValue;
    
    @Value("#{userRepository.findById(1).orElse(null)}")
    private User defaultUser;
    
    @Value("#{config.maxRetries ?: 3}")
    private int maxRetries;
}
```
