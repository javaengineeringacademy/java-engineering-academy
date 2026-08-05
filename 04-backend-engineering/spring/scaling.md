# Spring Scaling Strategies

## Horizontal Scaling

### Stateless Design

```java
@RestController
public class UserController {
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate user
        String token = jwtTokenProvider.generateToken(authentication);
        
        // Return token (no session state)
        return ResponseEntity.ok(new JwtResponse(token));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        // Validate token and return user data
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}
```

### Session Management

```properties
# Spring Session with Redis
spring.session.store-type=redis
spring.session.redis.namespace=myapp:session
server.servlet.session.timeout=30m
```

```java
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class SessionConfig {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }
}
```

## Load Balancing

### Client-Side Load Balancing

```java
@Configuration
public class LoadBalancedConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

### Service Discovery

```java
@Service
public class OrderService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public User getUser(Long userId) {
        // service-name resolves via discovery
        return restTemplate.getForObject(
            "http://user-service/api/users/{id}", 
            User.class, 
            userId
        );
    }
}
```

## Caching Strategies

### Multi-Level Caching

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats());
        return cacheManager;
    }
}

@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id", cacheManager = "cacheManager")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### Cache Distribution

```yaml
# Redis configuration
spring:
  redis:
    host: redis-cluster
    port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 600000
      cache-null-values: false
```

## Database Scaling

### Read Replicas

```properties
# Primary database
spring.datasource.primary.url=jdbc:mysql://primary:3306/mydb
spring.datasource.primary.username=root

# Read replica
spring.datasource.replica.url=jdbc:mysql://replica:3306/mydb
spring.datasource.replica.username=readonly
```

```java
@Configuration
public class DataSourceConfig {
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }
}

@ConditionalOnProperty(name = "app.read-only", havingValue = "true")
@Qualifier("replica")
```

### Connection Pooling

```properties
# HikariCP for multiple datasources
spring.datasource.primary.hikari.maximum-pool-size=20
spring.datasource.primary.hikari.minimum-idle=5

spring.datasource.replica.hikari.maximum-pool-size=10
spring.datasource.replica.hikari.minimum-idle=2
```

## Async Processing

### Message Queue Integration

```java
@Configuration
@EnableJms
public class JmsConfig {
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = 
            new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("3-10");
        return factory;
    }
}

@Service
public class OrderService {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    public void createOrder(Order order) {
        // Save order
        orderRepository.save(order);
        
        // Send to queue for async processing
        jmsTemplate.convertAndSend("order-queue", order);
    }
}

@Component
public class OrderListener {
    
    @JmsListener(destination = "order-queue", concurrency = "3-10")
    public void processOrder(Order order) {
        // Process order asynchronously
        paymentService.processPayment(order);
        notificationService.sendConfirmation(order);
    }
}
```

## Microservices Pattern

### API Gateway

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r
                .path("/api/users/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .addRequestHeader("X-Request-Source", "gateway"))
                .uri("lb://user-service"))
            .route("order-service", r -> r
                .path("/api/orders/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://order-service"))
            .build();
    }
}
```

### Circuit Breaker

```java
@Service
public class UserService {
    
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
    public User getUser(Long userId) {
        return restTemplate.getForObject(
            "http://user-service/api/users/{id}", 
            User.class, 
            userId
        );
    }
    
    public User fallbackGetUser(Long userId, Exception e) {
        return new User(userId, "Default User", "default@example.com");
    }
}
```

## Rate Limiting

```java
@Component
public class RateLimiter {
    
    private final LoadingCache<String, RateLimiter> limiters;
    
    public RateLimiter() {
        limiters = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public RateLimiter load(String key) {
                    return RateLimiter.create(100); // 100 requests per second
                }
            });
    }
    
    public boolean tryAcquire(String clientId) {
        return limiters.get(clientId).tryAcquire();
    }
}

@RestController
public class ApiController {
    
    @Autowired
    private RateLimiter rateLimiter;
    
    @GetMapping("/api/data")
    public ResponseEntity<?> getData(@RequestParam String clientId) {
        if (!rateLimiter.tryAcquire(clientId)) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }
        // Process request
        return ResponseEntity.ok(data);
    }
}
```

## Scaling Checklist

1. Design stateless services
2. Use external session storage
3. Implement caching strategy
4. Configure connection pooling
5. Use message queues for async
6. Implement circuit breakers
7. Add rate limiting
8. Use load balancers
9. Monitor and scale horizontally
10. Test failover scenarios
