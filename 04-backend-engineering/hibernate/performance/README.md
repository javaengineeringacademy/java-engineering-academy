# Hibernate Performance

## Comprehensive Guide to N+1 Problem and Optimization

The N+1 query problem is a common performance issue. This guide covers fetch strategies, batching, and optimization techniques.

---

## Table of Contents

1. [N+1 Problem](#n1-problem)
2. [Fetch Strategies](#fetch-strategies)
3. [Batch Fetching](#batch-fetching)
4. [Query Optimization](#query-optimization)
5. [Best Practices](#best-practices)

---

## N+1 Problem

### What is N+1?

```java
// Entity with lazy loading
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}

// This query causes N+1
List<User> users = session.createQuery("FROM User", User.class)
    .getResultList();  // 1 query

for (User user : users) {
    user.getOrders().size();  // N queries (one per user)
}
```

### Solution: JOIN FETCH

```java
// Single query with JOIN
List<User> users = session.createQuery(
        "SELECT DISTINCT u FROM User u JOIN FETCH u.orders",
        User.class)
    .getResultList();
```

### Solution: @BatchSize

```java
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private List<Order> orders;
}
```

---

## Fetch Strategies

### FetchType

```java
// EAGER - Loads immediately
@ManyToOne(fetch = FetchType.EAGER)
private User user;

// LAZY - Loads on access (default for collections)
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

### FetchMode

```java
@Entity
@Table(name = "users")
@Fetch(FetchMode.JOIN)
public class User {

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Order> orders;
}
```

### EntityGraph

```java
@EntityGraph(attributePaths = {"orders", "profile"})
@Query("FROM User")
List<User> findAllWithGraph();
```

---

## Batch Fetching

### Configuration

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
        default_batch_fetch_size: 25
```

### Entity Configuration

```java
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private List<Order> orders;
}
```

---

## Query Optimization

### Use Projections

```java
// Load only needed columns
@Query("SELECT new com.example.UserSummary(u.id, u.name) FROM User u")
List<UserSummary> findSummaries();
```

### Use ScrollableResults

```java
ScrollableResults<User> scroll = session
    .createQuery("FROM User", User.class)
    .scroll(ScrollMode.FORWARD_ONLY);

while (scroll.next()) {
    User user = scroll.get();
    // Process
}
scroll.close();
```

### Use StatelessSession

```java
StatelessSession session = sessionFactory.openStatelessSession();
// No first-level cache
// No dirty checking
// Useful for batch operations
```

---

## Best Practices

### 1. Use LAZY Loading

```java
@OneToMany(fetch = FetchType.LAZY)  // Default
private List<Order> orders;

@ManyToOne(fetch = FetchType.LAZY)  // Specify explicitly
private User user;
```

### 2. Use JOIN FETCH for Known Associations

```java
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

### 3. Use EntityGraph for Dynamic Fetching

```java
@EntityGraph(attributePaths = {"orders", "profile"})
@Query("SELECT u FROM User u WHERE u.id = :id")
User findByIdWithGraph(@Param("id") Long id);
```

### 4. Batch Operations

```java
@Transactional
public void batchSave(List<User> users) {
    Session session = sessionFactory.getCurrentSession();
    for (int i = 0; i < users.size(); i++) {
        session.persist(users.get(i));
        if (i % 50 == 0) {
            session.flush();
            session.clear();
        }
    }
}
```

### 5. Monitor SQL Queries

```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        log_sql: true
```

---

## Further Reading

- [Hibernate Performance Tuning](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#performance)
- [N+1 Problem](https://www.baeldung.com/hibernate-common-performance-problems-in-logs)
- [Hibernate Batch Processing](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#batch)
