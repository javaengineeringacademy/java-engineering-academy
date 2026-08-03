# Module 76: Spring + Hibernate

## Overview
Master the integration of Spring Framework with Hibernate ORM. This module covers session management, transaction handling, caching, and best practices for data access layer.

## Learning Objectives
- Configure Hibernate with Spring
- Use SessionFactory and Session
- Implement DAO pattern with Hibernate
- Handle transactions declaratively
- Configure second-level cache
- Optimize queries with batching

## Prerequisites
- Module 33: Spring Core
- Module 36: Hibernate
- Module 35: Spring Data JPA

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Integration Overview | Spring + Hibernate architecture |
| 02 | Configuration | SessionFactory, DataSource setup |
| 03 | Session Management | OpenSessionInView, Contextual sessions |
| 04 | DAO Pattern | Data Access Object with Hibernate |
| 05 | Transactions | @Transactional, propagation |
| 06 | Caching | First-level, second-level cache |
| 07 | Query Optimization | Batch fetching, lazy loading |
| 08 | Callbacks | EntityListeners, lifecycle events |
| 09 | Mini Project | Complete data access layer |

## Key Concepts

### DAO Pattern with Hibernate
```java
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final SessionFactory sessionFactory;
    
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public User findById(Long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }
    
    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
        } else {
            session.merge(user);
        }
        return user;
    }
}
```

### Transaction Management
```java
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
```

## Module Structure
```
76-spring-hibernate/
├── README.md
├── pom.xml
├── src/main/java/academy/javaengineering/springhibernate/
│   ├── config/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── callback/
└── src/test/java/academy/javaengineering/springhibernate/
```

## References
- [Spring ORM Documentation](https://docs.spring.io/spring-framework/reference/data-access/orm.html)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
