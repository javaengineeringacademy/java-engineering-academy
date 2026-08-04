# Hibernate Caching

## Comprehensive Guide to Hibernate Cache Levels

Hibernate provides multi-level caching to improve performance.

---

## Table of Contents

1. [Cache Levels](#cache-levels)
2. [First-Level Cache](#first-level-cache)
3. [Second-Level Cache](#second-level-cache)
4. [Query Cache](#query-cache)
5. [Best Practices](#best-practices)

---

## Cache Levels

### Cache Architecture

```
Application
    |
    v
First-Level Cache (Session - Always ON)
    |
    v
Second-Level Cache (SessionFactory - Optional)
    |
    v
Query Cache (Optional)
    |
    v
Database
```

---

## First-Level Cache

### Session Cache

```java
// First-level cache is automatic within a session
Session session = sessionFactory.openSession();

User user1 = session.get(User.class, 1L);  // SQL query
User user2 = session.get(User.class, 1L);  // No SQL query (cached)

session.close(); // Cache cleared
```

### Cache Management

```java
Session session = sessionFactory.openSession();

User user = session.get(User.class, 1L);

// Evict single entity
session.evict(user);

// Clear entire session
session.clear();

// Contains check
boolean cached = session.contains(user);
```

---

## Second-Level Cache

### Configuration

```yaml
spring:
  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true
          region:
            factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
        javax:
          cache:
            provider: org.ehcache.jsr107.EhcacheCachingProvider
            uri: classpath:ehcache.xml
```

### Entity Caching

```java
@Entity
@Table(name = "users")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
}
```

### Cache Concurrency Strategies

```java
// Read-Only (never modified)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)

// Read-Write (occasionally modified)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

// Nonstrict-Read-Write (rarely modified)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

// Transactional (JTA only)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
```

---

## Query Cache

### Enable Query Cache

```java
// Enable query cache
session.setCacheMode(CacheMode.NORMAL);

List<User> users = session.createQuery("FROM User WHERE active = true")
    .setCacheable(true)
    .setCacheRegion("queries.users")
    .getResultList();
```

### Spring Data JPA

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.active = true")
    @Cacheable(value = "users", key = "#p0")
    List<User> findActiveUsers();
}
```

---

## Best Practices

### 1. Cache Frequently Read, Rarely Modified Data

```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country {
    // Country data rarely changes
}
```

### 2. Invalidate Cache on Updates

```java
@Service
public class UserService {

    @CacheEvict(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void clearCache() {
        // Clear all users from cache
    }
}
```

### 3. Use Cache Regions

```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,
       region = "userCache")
public class User { }

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,
       region = "productCache")
public class Product { }
```

### 4. Monitor Cache Hit Ratio

```java
// Enable statistics
properties.put(AvailableSettings.GENERATE_STATISTICS, "true");

// Check stats
Statistics stats = sessionFactory.getStatistics();
double hitRatio = stats.getSecondLevelCacheHitCount() /
    (double)(stats.getSecondLevelCacheHitCount() +
             stats.getSecondLevelCacheMissCount());
```

### 5. Don't Cache Everything

```java
// Don't cache entities with high write rates
@Entity
@Cacheable  // BAD for frequently updated data
public class Order {
    // Orders are created/updated frequently
}
```

---

## Further Reading

- [Hibernate Caching](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#caching)
- [JPA Caching](https://jakarta.ee/specifications/persistence/3.1/jakarta.persistence-spec-3.1.html#caching)
- [Ehcache](https://www.ehcache.org/)
