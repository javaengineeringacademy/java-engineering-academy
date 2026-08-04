# JPA Fundamentals

## Comprehensive Guide to Java Persistence API

JPA is the standard API for object-relational mapping in Java. This guide covers EntityManager, persistence context, and entity lifecycle.

---

## Table of Contents

1. [JPA Architecture](#jpa-architecture)
2. [EntityManager](#entitymanager)
3. [Persistence Context](#persistence-context)
4. [Entity Lifecycle](#entity-lifecycle)
5. [Best Practices](#best-practices)

---

## JPA Architecture

### JPA vs Hibernate

```
JPA (Specification)     - API definition
Hibernate (Implementation) - Concrete implementation
Spring Data JPA          - Repository abstraction
```

### Architecture

```
Application
    |
    v
JPA API (EntityManager)
    |
    v
JPA Provider (Hibernate)
    |
    v
JDBC
    |
    v
Database
```

---

## EntityManager

### Basic Operations

```java
@PersistenceContext
private EntityManager em;

// Persist (create)
@Transactional
public User createUser(User user) {
    em.persist(user);
    return user;
}

// Find (read)
public User findById(Long id) {
    return em.find(User.class, id);
}

// Merge (update)
@Transactional
public User updateUser(User user) {
    return em.merge(user);
}

// Remove (delete)
@Transactional
public void deleteUser(Long id) {
    User user = em.find(User.class, id);
    em.remove(user);
}

// Refresh
@Transactional
public void refreshUser(User user) {
    em.refresh(user);
}

// Flush
@Transactional
public void saveImmediately(User user) {
    em.persist(user);
    em.flush();
}
```

### JPQL Queries

```java
// Simple query
List<User> users = em.createQuery("SELECT u FROM User u", User.class)
    .getResultList();

// With parameters
User user = em.createQuery(
        "SELECT u FROM User u WHERE u.email = :email", User.class)
    .setParameter("email", "john@example.com")
    .getSingleResult();

// Pagination
List<User> users = em.createQuery("SELECT u FROM User u", User.class)
    .setFirstResult(0)
    .setMaxResults(10)
    .getResultList();
```

---

## Persistence Context

### What is Persistence Context?

```java
// Persistence context is a cache of managed entities
@PersistenceContext
private EntityManager em;

@Transactional
public void updateUser(Long id, String name) {
    // First select - entity becomes managed
    User user = em.find(User.class, id);

    // Modify managed entity
    user.setName(name);

    // No UPDATE needed - dirty checking handles it
}

// UPDATE is executed at flush/commit
```

### Persistence Context Scope

```java
// Transaction-scoped (default)
@PersistenceContext
private EntityManager em;

// Extended (stateful sessions)
@PersistenceContext(type = PersistenceContextType.EXTENDED)
private EntityManager em;
```

---

## Entity Lifecycle

### Lifecycle States

```
New (Transient)
    |
    v
Managed (Persistent) <---+
    |                     |
    v                     |
Detached <---------+     |
    |               |     |
    v               |     |
Removed --------->+      |
                  |      |
                  v      |
              Merged ----+
```

### State Transitions

```java
// New -> Managed
User user = new User();
em.persist(user);  // Now managed

// Detached -> Managed
User user = em.find(User.class, id);
em.detach(user);   // Now detached
User merged = em.merge(user);  // Now managed again

// Managed -> Removed
em.remove(user);   // Marked for deletion
```

---

## Best Practices

### 1. Use @Transactional Correctly

```java
@Transactional(readOnly = true)
public User findById(Long id) {
    return em.find(User.class, id);
}

@Transactional
public User save(User user) {
    if (user.getId() == null) {
        em.persist(user);
        return user;
    }
    return em.merge(user);
}
```

### 2. Avoid LazyInitializationException

```java
// Eager fetch for small associations
@ManyToOne(fetch = FetchType.LAZY)
private User user;

// Use EntityGraph for dynamic fetching
@EntityGraph(attributePaths = {"orders"})
User findByIdWithOrders(Long id);
```

### 3. Use Batch Operations

```java
@Transactional
public void batchSave(List<User> users) {
    for (int i = 0; i < users.size(); i++) {
        em.persist(users.get(i));
        if (i % 50 == 0) {
            em.flush();
            em.clear();
        }
    }
}
```

### 4. Use Projections

```java
@Query("SELECT new com.example.UserDTO(u.id, u.name) FROM User u")
List<UserDTO> findAllProjected();
```

### 5. Close Resources

```java
EntityManager em = emFactory.createEntityManager();
try {
    // Use entity manager
} finally {
    em.close();
}
```

---

## Further Reading

- [JPA Specification](https://jakarta.ee/specifications/persistence/3.1/jakarta.persistence-spec-3.1.html)
- [Hibernate ORM](https://hibernate.org/orm/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
