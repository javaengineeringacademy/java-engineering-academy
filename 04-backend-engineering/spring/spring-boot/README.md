# Spring Boot

## Comprehensive Guide to Spring Boot Development

Spring Boot is an opinionated framework that makes it easy to create stand-alone, production-grade Spring-based applications. This guide covers everything from basic setup to advanced configuration.

---

## Table of Contents

1. [Auto-Configuration](#auto-configuration)
2. [Starters](#starters)
3. [Properties & Configuration](#properties--configuration)
4. [Profiles](#profiles)
5. [Actuator](#actuator)
6. [DevTools](#devtools)
7. [Best Practices](#best-practices)

---

## Auto-Configuration

### How Auto-Configuration Works

Spring Boot auto-configuration automatically configures your Spring application based on the dependencies present on the classpath.

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### @SpringBootApplication Annotation

This is a convenience annotation that combines:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication {
    // ...
}
```

### Conditional Annotations

```java
// Conditional on class presence
@ConditionalOnClass(DataSource.class)
@Configuration
public class DataSourceAutoConfiguration {
    // ...
}

// Conditional on missing bean
@ConditionalOnMissingBean
@Bean
public MyService myService() {
    return new DefaultMyService();
}

// Conditional on property
@ConditionalOnProperty(name = "app.feature.enabled", havingValue = "true")
@Configuration
public class FeatureConfiguration {
    // ...
}

// Conditional on web application
@ConditionalOnWebApplication
@Configuration
public class WebConfiguration {
    // ...
}
```

### Custom Auto-Configuration

```java
// 1. Create auto-configuration class
@Configuration
@ConditionalOnClass(MyLibrary.class)
@EnableConfigurationProperties(MyLibraryProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class MyLibraryAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public MyLibrary myLibrary(MyLibraryProperties properties) {
        return new MyLibrary(properties.getHost(), properties.getPort());
    }
}

// 2. Create properties class
@ConfigurationProperties(prefix = "my-library")
public class MyLibraryProperties {
    private String host = "localhost";
    private int port = 8080;
    // getters and setters
}

// 3. Register in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.example.MyLibraryAutoConfiguration
```

---

## Starters

### What Are Starters?

Starters are a set of dependency descriptors that you can include in your application.

### Common Starters

```xml
<!-- Web starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data JPA starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Security starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Validation starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Actuator starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Creating Custom Starters

```java
// 1. Create auto-configuration module
@Configuration
@ConditionalOnClass(ConditionalOnMissingBean.class)
@EnableConfigurationProperties(CustomProperties.class)
public class CustomAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public CustomService customService(CustomProperties properties) {
        return new CustomServiceImpl(properties);
    }
}

// 2. Create properties class
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private String apiKey;
    private int timeout = 30;
    // getters and setters
}

// 3. Create starter module with pom.xml
<project>
    <groupId>com.example</groupId>
    <artifactId>custom-spring-boot-starter</artifactId>
    <version>1.0.0</version>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

### Using Custom Starters

```xml
<!-- In your application -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>custom-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

```yaml
# application.yml
custom:
  api-key: my-api-key
  timeout: 60
```

---

## Properties & Configuration

### Application Properties

```yaml
# application.yml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: user
    password: password
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migration

logging:
  level:
    root: INFO
    com.example: DEBUG
```

### Property Sources

```java
// 1. Using @Value
@Service
public class MyService {
    @Value("${app.name:default-name}")
    private String appName;
    
    @Value("${app.features[0]}")
    private String firstFeature;
}

// 2. Using @ConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private List<String> features;
    private Map<String, String> metadata;
    // getters and setters
}

// 3. Using Environment
@Component
public class MyComponent {
    @Autowired
    private Environment env;
    
    public void someMethod() {
        String value = env.getProperty("app.name");
    }
}
```

### External Configuration

```properties
# Application properties (highest priority)
application.properties

# Profile-specific properties
application-dev.properties
application-prod.properties

# Command line arguments
--server.port=9090

# Environment variables
SERVER_PORT=9090

# JNDI properties
java:comp/env/myapp/config

# System properties
-Dserver.port=9090
```

### Configuration Metadata

```json
// META-INF/spring-configuration-metadata.json
{
  "properties": [
    {
      "name": "app.name",
      "type": "java.lang.String",
      "description": "Application name",
      "defaultValue": "my-app"
    },
    {
      "name": "app.features",
      "type": "java.util.List<java.lang.String>",
      "description": "Enabled features"
    }
  ]
}
```

---

## Profiles

### Using Profiles

```java
// Profile-specific configuration
@Configuration
@Profile("dev")
public class DevConfiguration {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@Configuration
@Profile("prod")
public class ProdConfiguration {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/proddb");
        return new HikariDataSource(config);
    }
}
```

### Profile Properties

```yaml
# application.yml (default)
spring:
  profiles:
    active: dev

---
# application-dev.yml
spring:
  config:
    activate:
      on-profile: dev

server:
  port: 8080

logging:
  level:
    root: DEBUG

---
# application-prod.yml
spring:
  config:
    activate:
      on-profile: prod

server:
  port: 443

logging:
  level:
    root: WARN
```

### Programmatic Profile Activation

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setAdditionalProfiles("dev", "custom");
        app.run(args);
    }
}
```

### Profile Groups

```yaml
spring:
  profiles:
    group:
      production:
        - proddb
        - prodmq
        - prod monitoring
      development:
        - devdb
        - devmq
```

### Conditional Beans with Profiles

```java
@Bean
@Profile("!production")
public DataSource devDataSource() {
    // Development datasource
}

@Bean
@Profile("production")
public DataSource prodDataSource() {
    // Production datasource
}

// Or using profile expressions
@Bean
@Profile("dev & !slow")
public DataSource fastDevDataSource() {
    // Fast development datasource
}
```

---

## Actuator

### Basic Actuator Setup

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Actuator Endpoints

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,beans,conditions
      base-path: /actuator
  
  endpoint:
    health:
      show-details: always
    shutdown:
      enabled: true
  
  health:
    db:
      enabled: true
    diskspace:
      enabled: true
  
  metrics:
    export:
      prometheus:
        enabled: true
    distribution:
      percentiles-histogram:
        http.server.requests: true
```

### Custom Health Indicators

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Check custom service health
            boolean isHealthy = checkServiceHealth();
            
            if (isHealthy) {
                return Health.up()
                    .withDetail("service", "Available")
                    .withDetail("version", "1.0.0")
                    .build();
            } else {
                return Health.down()
                    .withDetail("error", "Service unavailable")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withException(e)
                .build();
        }
    }
    
    private boolean checkServiceHealth() {
        // Implementation
        return true;
    }
}
```

### Custom Metrics

```java
@Component
public class CustomMetrics {
    
    private final MeterRegistry meterRegistry;
    private Counter requestCounter;
    private Timer requestTimer;
    
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestCounter = Counter.builder("app.requests.total")
            .description("Total requests")
            .tag("method", "GET")
            .register(meterRegistry);
        
        this.requestTimer = Timer.builder("app.request.duration")
            .description("Request duration")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
    }
    
    public void recordRequest() {
        requestCounter.increment();
    }
    
    public void recordDuration(long duration) {
        requestTimer.record(duration, TimeUnit.MILLISECONDS);
    }
}
```

### Info Endpoint

```java
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of(
            "name", "My Application",
            "version", "1.0.0",
            "description", "A sample application"
        ));
        
        builder.withDetail("git", Map.of(
            "commit", "abc123",
            "branch", "main"
        ));
    }
}
```

---

## DevTools

### DevTools Setup

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### DevTools Configuration

```yaml
# application-dev.yml
spring:
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
      exclude: static/**,public/**
    
    live-reload:
      enabled: true
    
    remote:
      secret: mysecretkey
    
    livereload:
      enabled: true
```

### Custom DevTools Configuration

```java
@Configuration
public class DevToolsConfiguration {
    
    @Bean
    public LocalServerPortCustomizer localServerPortCustomizer() {
        return (serverProperties) -> {
            serverProperties.setPort(8080);
        };
    }
}
```

### DevTools in Production

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // DevTools is automatically disabled in production
        SpringApplication.run(Application.class, args);
    }
}
```

---

## Best Practices

### Project Structure

```
src/main/java/
├── com.example.demo/
│   ├── Application.java
│   ├── config/
│   │   └── AppConfig.java
│   ├── controller/
│   │   └── UserController.java
│   ├── service/
│   │   └── UserService.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── model/
│   │   └── User.java
│   ├── dto/
│   │   └── UserDTO.java
│   └── exception/
│       └── GlobalExceptionHandler.java

src/main/resources/
├── application.yml
├── application-dev.yml
├── application-prod.yml
└── db/migration/
    └── V1__create_user_table.sql
```

### Configuration Best Practices

```java
// 1. Use @ConfigurationProperties for type-safe configuration
@ConfigurationProperties(prefix = "app.database")
public class DatabaseProperties {
    @NotBlank
    private String url;
    
    @Min(1)
    @Max(100)
    private int poolSize = 10;
    
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration timeout = Duration.ofSeconds(30);
}

// 2. Use @ConditionalOnProperty for feature flags
@Configuration
@ConditionalOnProperty(name = "app.features.cache.enabled", havingValue = "true")
public class CacheConfiguration {
    // Cache configuration
}

// 3. Use profiles for environment-specific configuration
@Configuration
@Profile("prod")
public class ProductionConfiguration {
    // Production-specific beans
}
```

### Testing Best Practices

```java
@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {
    
    @Test
    void contextLoads() {
        // Test that context loads
    }
}

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldReturnUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(new UserDTO(1L, "John"));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### Production Readiness

```yaml
# Production configuration
spring:
  main:
    allow-bean-definition-overriding: false
  
  jpa:
    open-in-view: false
  
  flyway:
    enabled: true
    baseline-on-migrate: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  
  endpoint:
    health:
      show-details: never

logging:
  level:
    root: WARN
    com.example: INFO
```

---

## Common Pitfalls

### 1. Circular Dependencies

```java
// Bad
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

// Good - Use constructor injection and restructure
@Service
public class ServiceA {
    private final SharedService sharedService;
    
    public ServiceA(SharedService sharedService) {
        this.sharedService = sharedService;
    }
}
```

### 2. Property Binding Issues

```java
// Bad - @Value doesn't support complex objects
@Value("${app.config}")
private Map<String, String> config;

// Good - Use @ConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private Map<String, String> config;
}
```

### 3. Profile Activation

```java
// Bad - Hardcoding profiles
@SpringBootApplication
@Profile("dev")
public class Application {
    // Only works in dev
}

// Good - Use conditional configuration
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## Further Reading

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Boot GitHub](https://github.com/spring-projects/spring-boot)
- [Baeldung Spring Boot](https://www.baeldung.com/spring-boot)
