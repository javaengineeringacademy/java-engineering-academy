# Spring Pitfalls and Anti-Patterns

## Circular Dependencies

### Problem

```java
@Service
public class ServiceA {
    private final ServiceB serviceB;
    public ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    public ServiceB(ServiceA serviceA) { this.serviceA = serviceA; }
}
```

### Solutions

```java
// Solution 1: Use @Lazy
@Service
public class ServiceA {
    private final ServiceB serviceB;
    public ServiceA(@Lazy ServiceB serviceB) { this.serviceB = serviceB; }
}

// Solution 2: Extract to third service
@Service
public class ServiceC {
    public void doWork(ServiceA a, ServiceB b) {
        // Combined logic
    }
}

// Solution 3: Use setter injection
@Service
public class ServiceA {
    private ServiceB serviceB;
    @Autowired
    public void setServiceB(ServiceB serviceB) { this.serviceB = serviceB; }
}
```

## N+1 Query Problem

### Problem

```java
@Entity
public class Order {
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderItem> items;
}

// This triggers N+1 queries
List<Order> orders = orderRepository.findAll();
for (Order order : orders) {
    order.getItems().size(); // N additional queries
}
```

### Solutions

```java
// Solution 1: JOIN FETCH
@Query("SELECT o FROM Order o JOIN FETCH o.items")
List<Order> findAllWithItems();

// Solution 2: EntityGraph
@EntityGraph(attributePaths = {"items"})
List<Order> findAll();

// Solution 3: @BatchSize
@OneToMany(fetch = FetchType.LAZY)
@BatchSize(size = 20)
private List<OrderItem> items;
```

## Field Injection

### Problem

```java
@Service
public class UserService {
    @Autowired
    private UserRepository repository; // Hard to test
    
    @Autowired
    private EmailService emailService; // Hidden dependencies
}
```

### Solution

```java
@Service
public class UserService {
    private final UserRepository repository;
    private final EmailService emailService;
    
    public UserService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }
}
```

## Transaction Issues

### Problem

```java
@Service
public class OrderService {
    // Missing @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order); // Saved
        throw new RuntimeException("Error"); // Not rolled back
    }
}
```

### Solution

```java
@Service
public class OrderService {
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order); // Rolled back on exception
        throw new RuntimeException("Error");
    }
}
```

## Lazy Initialization Issues

### Problem

```java
@Component
public class MyComponent {
    @Autowired
    private LazyService service; // Proxy injected
    
    public void doWork() {
        // Service initialized on first call
        service.process(); // May fail at runtime
    }
}
```

### Solution

```java
// Use @DependsOn for initialization order
@Component
@DependsOn("service")
public class MyComponent {
    @Autowired
    private Service service;
}

// Or use ApplicationContextAware
@Component
public class MyComponent implements ApplicationContextAware {
    private ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.context = ctx;
    }
    
    public void init() {
        Service service = context.getBean(Service.class);
    }
}
```

## Bean Scope Issues

### Problem

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    private int count = 0;
    
    public void increment() { count++; }
    public int getCount() { return count; }
}

@Service
public class MyService {
    @Autowired
    private PrototypeBean bean; // Same proxy, different instances
}
```

### Solution

```java
@Service
public class MyService {
    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanFactory;
    
    public void doWork() {
        PrototypeBean bean = prototypeBeanFactory.getObject();
        bean.increment(); // New instance each time
    }
}
```

## Async Issues

### Problem

```java
@Service
public class MyService {
    @Async
    public void asyncMethod() {
        // Exception swallowed
        throw new RuntimeException("Error");
    }
}
```

### Solution

```java
@Service
public class MyService {
    @Async
    public CompletableFuture<Result> asyncMethod() {
        try {
            Result result = process();
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

// Configure async exception handler
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Async exception in {}: {}", method.getName(), ex.getMessage());
    }
}
```

## Property Placeholder Issues

### Problem

```java
@Component
public class MyComponent {
    @Value("${missing.property}")
    private String value; // Application fails to start
}
```

### Solution

```java
@Component
public class MyComponent {
    @Value("${missing.property:default}")
    private String value; // Uses default if missing
}

// Or use Optional
@Component
public class MyComponent {
    @Value("${missing.property:}")
    private Optional<String> value;
}
```

## Configuration Binding Issues

### Problem

```java
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private List<String> items; // Fails with comma-separated values
}
```

### Solution

```java
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private List<String> items = new ArrayList<>();
    
    // Use @ConstructorBinding for immutability
}

// Or use separate properties
app.items[0]=item1
app.items[1]=item2
```

## Profile Issues

### Problem

```java
@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public DataSource dataSource() { }
}

// Missing profile causes bean not found
```

### Solution

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @Profile("dev")
    public DataSource devDataSource() { }
    
    @Bean
    @Profile("prod")
    public DataSource prodDataSource() { }
    
    @Bean
    @Primary
    public DataSource defaultDataSource() {
        // Fallback
        return new EmbeddedDatabaseBuilder().build();
    }
}
```

## RestTemplate Issues

### Problem

```java
@Service
public class MyService {
    @Autowired
    private RestTemplate restTemplate; // No timeout configured
    
    public String callExternal() {
        return restTemplate.getForObject("http://slow-api.com", String.class);
    }
}
```

### Solution

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}
```

## Logging Issues

### Problem

```java
@Service
public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    
    public void process(Data data) {
        log.info("Processing: " + data.toString()); // String concatenation
    }
}
```

### Solution

```java
@Service
public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    
    public void process(Data data) {
        log.info("Processing: {}", data); // Parameterized logging
    }
}
```

## Common Pitfalls Checklist

1. Avoid circular dependencies
2. Use JOIN FETCH for lazy collections
3. Prefer constructor injection
4. Always use @Transactional
5. Handle async exceptions properly
6. Provide default values for properties
7. Configure timeouts for external calls
8. Use parameterized logging
9. Test with proper profiles
10. Use DTOs instead of entities in controllers
