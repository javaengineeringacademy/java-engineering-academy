# Hibernate Fundamentals

## Comprehensive Guide to Hibernate ORM

Hibernate is an object-relational mapping framework for Java. This guide covers Session, SessionFactory, CRUD operations, and configuration.

---

## Table of Contents

1. [Hibernate Architecture](#hibernate-architecture)
2. [Configuration](#configuration)
3. [Session and SessionFactory](#session-and-sessionfactory)
4. [CRUD Operations](#crud-operations)
5. [Entity States](#entity-states)
6. [Best Practices](#best-practices)

---

## Hibernate Architecture

### Architecture Overview

```
Application
    |
    v
+------------------+
| Hibernate API    |
| (Session, etc.)  |
+------------------+
    |
    v
+------------------+
| JDBC             |
+------------------+
    |
    v
+------------------+
| Database         |
+------------------+
```

### Core Components

```
SessionFactory (Thread-safe, heavyweight)
    |
    v
Session (Thread-bound, lightweight)
    |
    v
Transaction
```

---

## Configuration

### Programmatic Configuration

```java
Configuration configuration = new Configuration();

// Database connection
configuration.setProperty("hibernate.connection.driver_class",
    "org.postgresql.Driver");
configuration.setProperty("hibernate.connection.url",
    "jdbc:postgresql://localhost:5432/mydb");
configuration.setProperty("hibernate.connection.username", "user");
configuration.setProperty("hibernate.connection.password", "password");

// Connection pool
configuration.setProperty("hibernate.hikari.maximumPoolSize", "20");
configuration.setProperty("hibernate.hikari.minimumIdle", "5");

// SQL settings
configuration.setProperty("hibernate.dialect",
    "org.hibernate.dialect.PostgreSQLDialect");
configuration.setProperty("hibernate.show_sql", "true");
configuration.setProperty("hibernate.format_sql", "true");
configuration.setProperty("hibernate.hbm2ddl.auto", "update");

// Add entity classes
configuration.addAnnotatedClass(User.class);
configuration.addAnnotatedClass(Order.class);

// Build SessionFactory
SessionFactory sessionFactory = configuration.buildSessionFactory();
```

### Spring Boot Configuration

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
    open-in-view: false
```

---

## Session and SessionFactory

### SessionFactory

```java
@Configuration
public class HibernateConfig {

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory =
            new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.example.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
            new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
```

### Session

```java
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public User findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Transactional
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        return user;
    }

    @Transactional
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        return session.merge(user);
    }

    @Transactional
    public void delete(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(user);
    }
}
```

---

## CRUD Operations

### Create

```java
@Transactional
public User createUser(User user) {
    Session session = sessionFactory.getCurrentSession();
    session.persist(user);
    return user;
}

// Batch insert
@Transactional
public void saveAll(List<User> users) {
    Session session = sessionFactory.getCurrentSession();
    int i = 0;
    for (User user : users) {
        session.persist(user);
        if (i % 50 == 0) {
            session.flush();
            session.clear();
        }
        i++;
    }
}
```

### Read

```java
@Transactional(readOnly = true)
public User findById(Long id) {
    Session session = sessionFactory.getCurrentSession();
    return session.get(User.class, id);
}

@Transactional(readOnly = true)
public List<User> findAll() {
    Session session = sessionFactory.getCurrentSession();
    return session.createQuery("FROM User", User.class)
        .getResultList();
}

@Transactional(readOnly = true)
public Optional<User> findByEmail(String email) {
    Session session = sessionFactory.getCurrentSession();
    return session.createQuery(
            "FROM User WHERE email = :email", User.class)
        .setParameter("email", email)
        .uniqueResultOptional();
}
```

### Update

```java
@Transactional
public User updateUser(Long id, User updates) {
    Session session = sessionFactory.getCurrentSession();
    User user = session.get(User.class, id);
    user.setName(updates.getName());
    user.setEmail(updates.getEmail());
    return user;
}

// Merge
@Transactional
public User merge(User user) {
    Session session = sessionFactory.getCurrentSession();
    return session.merge(user);
}
```

### Delete

```java
@Transactional
public void deleteUser(Long id) {
    Session session = sessionFactory.getCurrentSession();
    User user = session.get(User.class, id);
    session.remove(user);
}

@Transactional
public void deleteById(Long id) {
    Session session = sessionFactory.getCurrentSession();
    session.createQuery("DELETE FROM User WHERE id = :id")
        .setParameter("id", id)
        .executeUpdate();
}
```

---

## Entity States

### Entity State Diagram

```
Transient --persist()--> Persistent --remove()--> Removed
    ^                         |
    |                         v
    +--------detach()---- Detached --merge()--> Persistent
```

### State Examples

```java
// Transient - Not associated with session
User user = new User();
user.setName("John");

// Persistent - Associated with session
session.persist(user);
// OR
User persistentUser = session.get(User.class, 1L);

// Detached - Was persistent, session closed
session.evict(user);
// OR after session.close()

// Removed - Marked for deletion
session.remove(user);
```

---

## Best Practices

### 1. Use Session Factory Wisely

```java
// Create once, reuse everywhere
@Autowired
private SessionFactory sessionFactory;

// Don't create new SessionFactory per request
```

### 2. Always Use Transactions

```java
@Transactional
public void updateUser(Long id, User updates) {
    // Always in transaction
}
```

### 3. Batch Operations

```java
@Transactional
public void batchInsert(List<User> users) {
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

### 4. Use Caching

```java
@Entity
@Table(name = "users")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    // ...
}
```

### 5. Avoid N+1 Queries

```java
// Bad
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}

// Good - Use JOIN FETCH
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

---

## Further Reading

- [Hibernate ORM](https://hibernate.org/orm/)
- [Hibernate Getting Started](https://hibernate.org/orm/documentation/getting-started/)
- [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/)
