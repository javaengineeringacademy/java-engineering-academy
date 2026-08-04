# Spring Cloud

## Comprehensive Guide to Spring Cloud Microservices

Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems. This guide covers service discovery, configuration, circuit breakers, gateways, and load balancing.

---

## Table of Contents

1. [Service Discovery](#service-discovery)
2. [Config Server](#config-server)
3. [Circuit Breaker](#circuit-breaker)
4. [API Gateway](#api-gateway)
5. [Load Balancing](#load-balancing)
6. [Distributed Tracing](#distributed-tracing)
7. [Best Practices](#best-practices)

---

## Service Discovery

### Netflix Eureka

#### Eureka Server

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

```yaml
# application.yml for Eureka Server
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: true
    eviction-interval-timer-in-ms: 60000
```

#### Eureka Client

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

```yaml
# application.yml for Eureka Client
spring:
  application:
    name: user-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
```

### Consul

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

```yaml
spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        instance-id: ${spring.application.name}:${random.value}
        health-check-interval: 10s
        health-check-path: /actuator/health
```

### Service Discovery Client Usage

```java
@RestController
public class UserController {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @GetMapping("/services")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }
    
    @GetMapping("/service-instances/{serviceName}")
    public List<ServiceInstance> getInstances(@PathVariable String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }
}
```

---

## Config Server

### Config Server Setup

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

```yaml
# application.yml for Config Server
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/myorg/config-repo
          default-label: main
          search-paths:
            - '{application}'
            - '{application}/{profile}'
          username: ${GIT_USERNAME}
          password: ${GIT_PASSWORD}
        
        native:
          search-locations: classpath:/config
        
        bootstrap:
          enabled: true
```

### Config Client

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

```yaml
# bootstrap.yml
spring:
  application:
    name: user-service
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
      retry:
        max-attempts: 5
        initial-interval: 1000
        multiplier: 1.5
      profile: dev
```

### Config Client Usage

```java
@RestController
@RefreshScope
public class ConfigController {
    
    @Value("${app.feature.enabled:false}")
    private boolean featureEnabled;
    
    @Value("${app.config.message:default}")
    private String message;
    
    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        return Map.of(
            "featureEnabled", featureEnabled,
            "message", message
        );
    }
}
```

### Config Encryption

```yaml
# Encrypt config values
encrypt:
  key: my-encryption-key

# In config file
app:
  password: '{cipher}encrypted-value'
```

```java
// Encryption endpoint
POST /encrypt
Content-Type: text/plain

my-secret-password

// Decryption endpoint
POST /decrypt
Content-Type: text/plain

{cipher}encrypted-value
```

### Config Watch

```yaml
spring:
  cloud:
    config:
      watch:
        enabled: true
        delay: 1000
```

```java
@EventListener
public void onRefresh(RefreshScopeRefreshedEvent event) {
    // Handle config refresh
    log.info("Configuration refreshed");
}
```

---

## Circuit Breaker

### Resilience4j

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

```yaml
resilience4j:
  circuitbreaker:
    instances:
      myService:
        sliding-window-size: 10
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
        permitted-number-of-calls-in-half-open-state: 3
        automatic-transition-from-open-to-half-open-enabled: true
  
  retry:
    instances:
      myService:
        max-attempts: 3
        wait-duration: 500ms
        enable-exponential-backoff: true
        exponential-backoff-multiplier: 2
        retry-exceptions:
          - java.io.IOException
          - java.util.concurrent.TimeoutException
  
  timelimiter:
    instances:
      myService:
        timeout-duration: 3s
```

### Circuit Breaker Usage

```java
@Service
public class ExternalServiceClient {
    
    @CircuitBreaker(name = "myService", fallbackMethod = "fallback")
    @Retry(name = "myService")
    @TimeLimiter(name = "myService")
    public CompletableFuture<String> callExternalService() {
        return CompletableFuture.supplyAsync(() -> {
            // Call external service
            return restTemplate.getForObject("http://external-api/data", String.class);
        });
    }
    
    public CompletableFuture<String> fallback(Throwable t) {
        return CompletableFuture.completedFuture("Fallback response");
    }
}
```

### Bulkhead Pattern

```yaml
resilience4j:
  bulkhead:
    instances:
      myService:
        max-concurrent-calls: 25
        max-wait-duration: 0
```

```java
@Bulkhead(name = "myService")
public String callService() {
    // Service call with limited concurrency
}
```

### Rate Limiter

```yaml
resilience4j:
  ratelimiter:
    instances:
      myService:
        limit-for-period: 10
        limit-refresh-period: 1s
        timeout-duration: 0
```

```java
@RateLimiter(name = "myService")
public String callService() {
    // Rate limited service call
}
```

---

## API Gateway

### Spring Cloud Gateway

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

```yaml
server:
  port: 8080

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
        
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
            - Method=GET,POST
          filters:
            - AddRequestHeader=X-Request-Source, gateway
            - AddRequestParameter=color, blue
            - name: RequestRateLimiter
              args:
                redis-rate-limiter:
                  replenishRate: 10
                  burstCapacity: 20
```

### Custom Gateway Filters

```java
@Component
public class CustomFilter implements GatewayFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Pre-processing
        ServerHttpRequest request = exchange.getRequest().mutate()
            .header("X-Custom-Header", "value")
            .build();
        
        return chain.filter(exchange.mutate().request(request).build())
            .then(Mono.fromRunnable(() -> {
                // Post-processing
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().add("X-Response-Time", Instant.now().toString());
            }));
    }
    
    @Override
    public int getOrder() {
        return -1; // Filter order
    }
}

// Custom Global Filter
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Request: {} {} - Response: {} - Duration: {}ms",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getResponse().getStatusCode(),
                duration);
        }));
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}
```

### Route Configuration

```java
@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("custom-route", r -> r
                .path("/api/custom/**")
                .filters(f -> f
                    .rewritePath("/api/custom/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Custom", "value"))
                .uri("lb://custom-service"))
            .build();
    }
}
```

### Load Balancing with Gateway

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: loadbalanced-route
          uri: lb://my-service  # lb: indicates load balancing
          predicates:
            - Path=/api/**
```

---

## Load Balancing

### Spring Cloud LoadBalancer

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

```java
@Configuration
public class LoadBalancerConfig {
    
    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        
        return new RoundRobinLoadBalancer(
            loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
            name);
    }
}
```

### Using LoadBalancer

```java
@Service
public class UserServiceClient {
    
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    
    public String callUserService() {
        ServiceInstance instance = loadBalancerClient.choose("user-service");
        String baseUrl = String.format("http://%s:%s", 
            instance.getHost(), instance.getPort());
        
        return restTemplate.getForObject(baseUrl + "/users", String.class);
    }
    
    // Or using @LoadBalanced
    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;
    
    public String callUserServiceWithLoadBalancing() {
        return restTemplate.getForObject("http://user-service/users", String.class);
    }
}
```

### Load Balancer Configuration

```yaml
spring:
  cloud:
    loadbalancer:
      ribbon:
        enabled: false  # Disable Ribbon
      
      cache:
        enabled: true
        ttl: 35s
      
      retry:
        enabled: true
        max-retries-on-same-service-instance: 0
        max-retries-on-next-service-instance: 3
```

---

## Distributed Tracing

### Micrometer Tracing

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0  # 100% sampling for development
    
  zipkin:
    base-url: http://localhost:9411
    sender:
      type: web
```

### Custom Tracing

```java
@RestController
public class TracedController {
    
    @Autowired
    private Tracer tracer;
    
    @GetMapping("/traced")
    public String tracedEndpoint() {
        Span span = tracer.nextSpan().name("custom-span");
        try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {
            // Your code here
            return "Traced response";
        } finally {
            span.end();
        }
    }
}
```

---

## Best Practices

### Service Design

```java
// 1. Use @RefreshScope for dynamic configuration
@RestController
@RefreshScope
public class FeatureController {
    
    @Value("${features.new-ui.enabled:false}")
    private boolean newUiEnabled;
    
    @GetMapping("/features")
    public Map<String, Boolean> getFeatures() {
        return Map.of("newUi", newUiEnabled);
    }
}

// 2. Implement health checks
@Component
public class ServiceHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Check dependencies
            boolean dbHealthy = checkDatabase();
            boolean cacheHealthy = checkCache();
            
            if (dbHealthy && cacheHealthy) {
                return Health.up().build();
            } else {
                return Health.down()
                    .withDetail("database", dbHealthy)
                    .withDetail("cache", cacheHealthy)
                    .build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}

// 3. Use circuit breakers for external calls
@Service
public class OrderService {
    
    @CircuitBreaker(name = "inventory", fallbackMethod = "inventoryFallback")
    public boolean checkInventory(String productId) {
        return inventoryClient.checkAvailability(productId);
    }
    
    public boolean inventoryFallback(String productId, Throwable t) {
        log.warn("Inventory check failed for product: {}", productId, t);
        return true; // Assume available on failure
    }
}
```

### Configuration Management

```yaml
# Use config server for centralized configuration
spring:
  cloud:
    config:
      fail-fast: true
      retry:
        max-attempts: 5
  
# Use profiles for environment-specific config
---
spring:
  config:
    activate:
      on-profile: prod
  
  cloud:
    config:
      username: ${CONFIG_USERNAME}
      password: ${CONFIG_PASSWORD}
```

### Security

```java
// Secure inter-service communication
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));
        
        return http.build();
    }
}
```

### Monitoring and Observability

```yaml
# Enable comprehensive monitoring
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  
  metrics:
    export:
      prometheus:
        enabled: true
    
    distribution:
      percentiles-histogram:
        http.server.requests: true
```

---

## Common Patterns

### Service Aggregation

```java
@RestController
@RequestMapping("/api/aggregate")
public class AggregationController {
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @GetMapping("/user-orders/{userId}")
    public CompletableFuture<AggregateResponse> getUserOrders(@PathVariable Long userId) {
        CompletableFuture<UserDTO> userFuture = CompletableFuture.supplyAsync(
            () -> userServiceClient.getUser(userId));
        
        CompletableFuture<List<OrderDTO>> ordersFuture = CompletableFuture.supplyAsync(
            () -> orderServiceClient.getOrdersByUser(userId));
        
        return userFuture.thenCombine(ordersFuture, (user, orders) -> 
            new AggregateResponse(user, orders));
    }
}
```

### Event-Driven Communication

```java
// Publisher
@Service
public class EventPublisher {
    
    @Autowired
    private StreamBridge streamBridge;
    
    public void publishUserCreatedEvent(UserCreatedEvent event) {
        streamBridge.send("userCreated-out-0", event);
    }
}

// Consumer
@Component
public class EventConsumer {
    
    @Bean
    public Function<Flux<UserCreatedEvent>, Mono<Void>> processUserCreated() {
        return flux -> flux
            .flatMap(event -> processEvent(event))
            .then();
    }
    
    private Mono<Void> processEvent(UserCreatedEvent event) {
        // Process event
        return Mono.empty();
    }
}
```

---

## Further Reading

- [Spring Cloud Official Documentation](https://spring.io/projects/spring-cloud)
- [Spring Cloud Netflix](https://github.com/spring-cloud/spring-cloud-netflix)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Resilience4j Documentation](https://resilience4j.readme.io/)
