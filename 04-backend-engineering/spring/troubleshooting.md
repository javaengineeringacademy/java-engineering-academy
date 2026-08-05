# Spring Troubleshooting

## Application Won't Start

### Bean Creation Failure

```bash
# Error: Consider defining a bean
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException

# Solutions:
# 1. Check component scanning
@SpringBootApplication(scanBasePackages = "com.example")

# 2. Check @Bean method
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}

# 3. Check dependencies
@ConditionalOnBean(DataSource.class)
```

### Property Binding Issues

```bash
# Error: Failed to bind properties
Caused by: org.springframework.boot.context.properties.bind.BindException

# Solutions:
# 1. Check property names
# 2. Use @ConfigurationProperties
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
}

# 3. Enable debug logging
logging.level.org.springframework.boot.context.properties=DEBUG
```

## Database Issues

### Connection Refused

```bash
# Error: Cannot create JDBC driver
Caused by: java.sql.SQLException: No suitable driver

# Solutions:
# 1. Check driver dependency
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

# 2. Check URL format
spring.datasource.url=jdbc:mysql://localhost:3306/mydb

# 3. Check driver class
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Connection Pool Exhaustion

```bash
# Error: Unable to acquire JDBC Connection
Caused by: java.sql.SQLException: Connection is not available

# Solutions:
# 1. Increase pool size
spring.datasource.hikari.maximum-pool-size=20

# 2. Check for connection leaks
spring.datasource.hikari.leak-detection-threshold=60000

# 3. Validate connections
spring.datasource.hikari.validation-timeout=5000
```

## HTTP Issues

### 404 Not Found

```bash
# Error: No mapping found for HTTP request
# Solutions:
# 1. Check @RequestMapping
@RestController
@RequestMapping("/api/users")
public class UserController { }

# 2. Check component scanning
@SpringBootApplication(scanBasePackages = "com.example")

# 3. Check servlet context
server.servlet.context-path=/api
```

### 415 Unsupported Media Type

```bash
# Error: Content type not supported
# Solutions:
# 1. Add @RequestBody
@PostMapping
public ResponseEntity<?> create(@RequestBody UserDTO user) { }

# 2. Check Content-Type header
Content-Type: application/json

# 3. Add Jackson dependency
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

### 500 Internal Server Error

```bash
# Error: Unhandled exception
# Solutions:
# 1. Add exception handling
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}

# 2. Check logs
logging.level.com.example=DEBUG
```

## Security Issues

### Authentication Failed

```bash
# Error: Bad credentials
# Solutions:
# 1. Check user details service
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Load user from database
    }
}

# 2. Check password encoder
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

# 3. Enable security debug
logging.level.org.springframework.security=DEBUG
```

### Access Denied

```bash
# Error: Forbidden
# Solutions:
# 1. Check roles
@PreAuthorize("hasRole('ADMIN')")

# 2. Check security configuration
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
);

# 3. Check CORS
corsConfigurationSource().allowedOrigins("http://localhost:3000")
```

## Cache Issues

### Cache Not Working

```bash
# Error: Cache not found
# Solutions:
# 1. Enable caching
@EnableCaching
@Configuration
public class CacheConfig { }

# 2. Check cache manager
@Bean
public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("myCache");
}

# 3. Check annotations
@Cacheable("myCache")
public User getUser(Long id) { }
```

## Async Issues

### Async Not Working

```bash
# Error: Async method not executing asynchronously
# Solutions:
# 1. Enable async
@EnableAsync
@Configuration
public class AsyncConfig { }

# 2. Check executor
@Bean
public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.initialize();
    return executor;
}

# 3. Check exception handling
@Async
public CompletableFuture<Result> asyncMethod() {
    try {
        return CompletableFuture.completedFuture(process());
    } catch (Exception e) {
        return CompletableFuture.failedFuture(e);
    }
}
```

## Transaction Issues

### Transaction Not Rolling Back

```bash
# Error: Data not rolled back
# Solutions:
# 1. Add @Transactional
@Transactional
public void method() {
    // Database operations
}

# 2. Check exception type
@Transactional(rollbackFor = Exception.class)

# 3. Check propagation
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

## Performance Issues

### Slow Queries

```bash
# Error: High database load
# Solutions:
# 1. Enable SQL logging
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG

# 2. Check N+1 queries
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();

# 3. Enable statistics
spring.jpa.properties.hibernate.generate_statistics=true
```

### Memory Issues

```bash
# Error: OutOfMemoryError
# Solutions:
# 1. Check heap size
java -Xmx1g -jar app.jar

# 2. Analyze memory
jmap -dump:live,format=b,file=heap.hprof <pid>

# 3. Check for memory leaks
logging.level.org.hibernate=WARN
```

## Logging Issues

### Too Much Logging

```bash
# Error: Log flooding
# Solutions:
# 1. Adjust log levels
logging.level.root=WARN
logging.level.com.example=INFO
logging.level.org.springframework=WARN

# 2. Use conditional logging
if (log.isDebugEnabled()) {
    log.debug("Expensive operation: {}", expensiveOperation());
}
```

### No Logging Output

```bash
# Error: No logs visible
# Solutions:
# 1. Check log configuration
logging.level.root=INFO

# 2. Check log file
logging.file.name=app.log

# 3. Check console output
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## Quick Fixes

```bash
# 1. Clean and rebuild
mvn clean install

# 2. Clear Spring cache
rm -rf target/

# 3. Check Java version
java -version

# 4. Check dependencies
mvn dependency:tree

# 5. Enable debug mode
java -Ddebug=true -jar app.jar
```

## Debugging Checklist

1. Check application logs
2. Verify configuration properties
3. Check bean definitions
4. Verify component scanning
5. Check database connectivity
6. Verify security configuration
7. Check HTTP mappings
8. Monitor thread usage
9. Analyze memory usage
10. Profile performance
