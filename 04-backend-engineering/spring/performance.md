# Spring Performance Optimization

## Connection Pooling

### HikariCP (Default)

```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.leak-detection-threshold=60000
```

### Custom HikariCP Config

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }
    
    @Bean
    public DataSource dataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }
}
```

## Caching

### Enable Caching

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "orders");
    }
}
```

### Use Cache Annotations

```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void clearCache() {
        // Clear all cached users
    }
}
```

### Redis Cache

```java
@Configuration
@EnableCaching
public class RedisCacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(new GenericJackson2JsonRedisSerializer())
            );
        
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

## Async Processing

### Enable Async

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
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}
```

### Use Async

```java
@Service
public class EmailService {
    
    @Async
    public void sendEmail(String to, String subject, String body) {
        // Send email asynchronously
    }
    
    @Async
    public CompletableFuture<Result> processAsync(Data data) {
        Result result = processData(data);
        return CompletableFuture.completedFuture(result);
    }
}
```

## Lazy Loading

```java
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}

@Service
public class UserService {
    @Transactional
    public User getUserWithOrders(Long id) {
        User user = userRepository.findById(id).orElse(null);
        // Access orders triggers lazy loading
        user.getOrders().size();
        return user;
    }
}
```

## Query Optimization

### JPA Queries

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Select specific fields
    @Query("SELECT new com.example.dto.UserDTO(u.id, u.name) FROM User u")
    List<UserDTO> findAllUserDTOs();
    
    // Fetch join for lazy loading
    @Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
    User findByIdWithOrders(@Param("id") Long id);
    
    // Pagination
    @Query("SELECT u FROM User u")
    Page<User> findAllUsers(Pageable pageable);
    
    // Batch fetching
    @Query("SELECT u FROM User u")
    @BatchSize(size = 20)
    List<User> findAllBatch();
}
```

### Entity Graph

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @EntityGraph(attributePaths = {"orders", "profile"})
    Optional<User> findById(Long id);
    
    @Query("SELECT u FROM User u")
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = {"orders"})
    List<User> findAllWithOrders();
}
```

## Profiling

### Enable Profiling

```properties
spring.profiles.active=dev
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.stat=DEBUG
```

### Hibernate Statistics

```properties
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=DEBUG
```

### Measure Performance

```java
@Component
public class PerformanceMonitor {
    
    @Around("@annotation(org.springframework.stereotype.Service)")
    public Object measureService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        
        if (duration > 1000) {
            log.warn("Slow service method: {} took {}ms", 
                joinPoint.getSignature().getName(), duration);
        }
        
        return result;
    }
}
```

## HTTP Client Optimization

```java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .responseTimeout(Duration.ofSeconds(5))
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            ))
            .build();
    }
}
```

## Response Compression

```properties
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,application/json,application/javascript
server.compression.min-response-size=1024
```

## Session Management

```java
@Configuration
@EnableSpringHttpSession
public class SessionConfig {
    @Bean
    public SessionRepository sessionRepository() {
        return new MapSessionRepository();
    }
}
```

## Database Optimization

### Batch Operations

```java
@Service
public class BatchService {
    
    @Transactional
    public void batchInsert(List<User> users) {
        int batchSize = 50;
        for (int i = 0; i < users.size(); i++) {
            entityManager.persist(users.get(i));
            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
```

### Second-Level Cache

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
```

## Performance Best Practices

1. Use connection pooling (HikariCP)
2. Enable caching for read-heavy operations
3. Use async processing for non-critical tasks
4. Implement pagination for large datasets
5. Use projection for specific fields
6. Enable HTTP compression
7. Monitor slow queries
8. Use batch operations for bulk inserts
9. Implement lazy loading properly
10. Profile and optimize hot paths
