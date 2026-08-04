# JPA Queries

## Comprehensive Guide to JPQL and Named Queries

JPA provides multiple query mechanisms. This guide covers JPQL, Native SQL, and Named Queries.

---

## Table of Contents

1. [JPQL Basics](#jpql-basics)
2. [Named Queries](#named-queries)
3. [Native SQL](#native-sql)
4. [Projections](#projections)
5. [Best Practices](#best-practices)

---

## JPQL Basics

### Basic Queries

```java
// Simple select
List<User> users = em.createQuery("SELECT u FROM User u", User.class)
    .getResultList();

// With WHERE clause
User user = em.createQuery(
        "SELECT u FROM User u WHERE u.email = :email", User.class)
    .setParameter("email", "john@example.com")
    .getSingleResult();

// With JOIN
List<Object[]> results = em.createQuery(
        "SELECT u, COUNT(o) FROM User u JOIN u.orders o GROUP BY u",
        Object[].class)
    .getResultList();
```

### Aggregation

```java
// COUNT
Long count = em.createQuery(
        "SELECT COUNT(u) FROM User u WHERE u.active = true",
        Long.class)
    .getSingleResult();

// SUM, AVG, MIN, MAX
Double avgAge = em.createQuery(
        "SELECT AVG(u.age) FROM User u", Double.class)
    .getSingleResult();
```

### Ordering and Pagination

```java
// ORDER BY
List<User> users = em.createQuery(
        "SELECT u FROM User u ORDER BY u.name ASC", User.class)
    .getResultList();

// Pagination
List<User> users = em.createQuery(
        "SELECT u FROM User u", User.class)
    .setFirstResult(0)    // offset
    .setMaxResults(10)    // limit
    .getResultList();
```

---

## Named Queries

### Entity Named Queries

```java
@Entity
@Table(name = "users")
@NamedQuery(
    name = "User.findByEmail",
    query = "SELECT u FROM User u WHERE u.email = :email"
)
@NamedQuery(
    name = "User.findActive",
    query = "SELECT u FROM User u WHERE u.active = true"
)
@NamedQueries({
    @NamedQuery(name = "User.count",
                query = "SELECT COUNT(u) FROM User u"),
    @NamedQuery(name = "User.findByStatus",
                query = "SELECT u FROM User u WHERE u.status = :status")
})
public class User { }
```

### Usage

```java
User user = em.createNamedQuery("User.findByEmail", User.class)
    .setParameter("email", "john@example.com")
    .getSingleResult();

List<User> users = em.createNamedQuery("User.findActive", User.class)
    .getResultList();
```

---

## Native SQL

### Basic Native Query

```java
// Simple query
List<User> users = em.createNativeQuery(
        "SELECT * FROM users WHERE active = true", User.class)
    .getResultList();

// With parameters
List<User> users = em.createNativeQuery(
        "SELECT * FROM users WHERE email = :email", User.class)
    .setParameter("email", "john@example.com")
    .getResultList();
```

### Scalar Queries

```java
// Single value
Long count = em.createNativeQuery(
        "SELECT COUNT(*) FROM users", Long.class)
    .getSingleResult();

// Multiple values
List<Object[]> results = em.createNativeQuery(
        "SELECT id, name, email FROM users")
    .getResultList();
```

---

## Projections

### Interface-Based Projections

```java
public interface UserProjection {
    Long getId();
    String getName();
    String getEmail();
}

// Repository
@Query("SELECT u.id as id, u.name as name, u.email as email FROM User u")
List<UserProjection> findAllProjected();
```

### Class-Based Projections

```java
public class UserSummary {
    private Long id;
    private String name;

    public UserSummary(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

@Query("SELECT new com.example.UserSummary(u.id, u.name) FROM User u")
List<UserSummary> findSummaries();
```

### Dynamic Projections

```java
// Repository
<T> List<T> findByClass(Class<T> projectionClass);

// Usage
List<UserName> names = userRepository.findByClass(UserName.class);
List<UserEmail> emails = userRepository.findByClass(UserEmail.class);
```

---

## Best Practices

### 1. Use Parameterized Queries

```java
// Good
em.createQuery("SELECT u FROM User u WHERE u.email = :email")
    .setParameter("email", userInput);

// Bad (SQL injection)
em.createQuery("SELECT u FROM User u WHERE u.email = '" + userInput + "'");
```

### 2. Use JOIN FETCH for Associations

```java
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

### 3. Use Projections for Read-Only

```java
@Query("SELECT new com.example.UserDTO(u.id, u.name) FROM User u")
List<UserDTO> findAllProjected();
```

### 4. Use Pagination

```java
Page<User> findAll(Pageable pageable);

@Query("SELECT u FROM User u")
Page<User> findAllProjected(Pageable pageable);
```

### 5. Use @EntityGraph

```java
@EntityGraph(attributePaths = {"orders", "profile"})
@Query("SELECT u FROM User u WHERE u.id = :id")
User findByIdWithGraph(@Param("id") Long id);
```

---

## Further Reading

- [JPQL](https://docs.oracle.com/javaee/7/api/javax/persistence/Query.html)
- [JPA Criteria API](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/CriteriaBuilder.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
