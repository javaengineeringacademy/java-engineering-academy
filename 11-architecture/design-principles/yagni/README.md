# YAGNI - You Aren't Gonna Need It

## Overview

YAGNI states that you should not add functionality until it is actually needed.

## Violations

### Anticipating Future Needs
```java
// BAD - Building for future requirements
public class UserService {
    // Current requirement: simple user creation
    
    // Future requirement: maybe we'll need multi-tenancy?
    private TenantContext tenantContext;
    
    // Future requirement: maybe we'll need audit?
    private AuditService auditService;
    
    // Future requirement: maybe we'll need caching?
    private CacheManager cacheManager;
    
    // Future requirement: maybe we'll need events?
    private EventPublisher eventPublisher;
    
    public User createUser(UserRequest request) {
        // Complex logic for features we don't need yet
    }
}

// GOOD - Only what's needed now
public class UserService {
    private final UserRepository userRepository;
    
    public User createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }
}
```

### Premature Abstraction
```java
// BAD - Abstract before knowing the patterns
public interface IRepository<T> {
    T findById(Long id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
    List<T> findByCriteria(Criteria criteria);
    Page<T> findPage(Pageable pageable);
    long count();
    boolean exists(Long id);
    void bulkInsert(List<T> entities);
    void bulkUpdate(List<T> entities);
    void flush();
    void clear();
    // ... 20 more methods
}

// GOOD - Concrete implementation first
@Repository
public class UserRepository {
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
    
    public User save(User user) {
        return entityManager.merge(user);
    }
}
```

### Over-Engineering
```java
// BAD - Complex configuration for simple use case
@Configuration
public class OverEngineeredConfig {
    @Bean
    public FeatureFlagService featureFlagService() {
        return new RemoteFeatureFlagService.Builder()
            .withLocalCache(new CaffeineCache())
            .withRemoteStore(new RedisStore())
            .withFallback(new DefaultFeatureFlags())
            .withMetrics(new FeatureFlagMetrics())
            .build();
    }
}

// GOOD - Simple configuration
@Configuration
public class SimpleConfig {
    @Value("${feature.new-ui:false}")
    private boolean newUiEnabled;
    
    @Bean
    public boolean newUiEnabled() {
        return newUiEnabled;
    }
}
```

## Questions to Ask

1. Is this feature in the current requirements?
2. Has this need been confirmed by users?
3. Will this be used within the next 2 sprints?
4. What is the cost of adding it later?
5. What is the cost of maintaining it now?

## Best Practices

1. Implement only confirmed requirements
2. Defer decisions until necessary
3. Resist "just in case" additions
4. Focus on current user needs
5. Keep code minimal and focused
6. Remove unused code promptly
7. Question every new abstraction
8. Measure actual needs before building
