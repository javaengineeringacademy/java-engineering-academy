# ORM Patterns

## Comprehensive Guide to DAO, Repository, and Unit of Work Patterns

ORM patterns provide structured approaches to data access. This guide covers DAO, Repository, and Unit of Work patterns.

---

## Table of Contents

1. [DAO Pattern](#dao-pattern)
2. [Repository Pattern](#repository-pattern)
3. [Unit of Work](#unit-of-work)
4. [Active Record](#active-record)
5. [Data Mapper](#data-mapper)
6. [Best Practices](#best-practices)

---

## DAO Pattern

### DAO Interface

```java
public interface UserDAO {
    User findById(Long id);
    List<User> findAll();
    User save(User user);
    void delete(User user);
    List<User> findByEmail(String email);
}
```

### Hibernate Implementation

```java
@Repository
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findById(Long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM User", User.class)
            .getResultList();
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
            return user;
        }
        return session.merge(user);
    }

    @Override
    public void delete(User user) {
        sessionFactory.getCurrentSession().remove(user);
    }
}
```

---

## Repository Pattern

### Spring Data Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findActiveUsers();

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
}
```

### Custom Repository

```java
public interface UserRepositoryCustom {
    List<User> findUsersWithOrders();
    List<User> findUsersByCriteria(UserSearchCriteria criteria);
}

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<User> findUsersWithOrders() {
        return em.createQuery(
                "SELECT DISTINCT u FROM User u JOIN FETCH u.orders",
                User.class)
            .getResultList();
    }

    @Override
    public List<User> findUsersByCriteria(UserSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getName() != null) {
            predicates.add(cb.like(root.get("name"),
                "%" + criteria.getName() + "%"));
        }
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"),
                criteria.getStatus()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }
}
```

---

## Unit of Work

### Hibernate Session as Unit of Work

```java
@Service
public class OrderService {

    private final EntityManager em;

    @Transactional
    public Order createOrder(OrderRequest request) {
        // Unit of work begins
        User user = em.find(User.class, request.getUserId());
        Order order = new Order(user, request.getItems());

        // All changes tracked
        em.persist(order);

        // Unit of work ends at commit
        return order;
    }
}
```

### Explicit Unit of Work

```java
@Component
public class UnitOfWork {

    private final EntityManager em;
    private final TransactionTemplate transactionTemplate;

    public <T> T executeInTransaction(Supplier<T> work) {
        return transactionTemplate.execute(status -> {
            try {
                T result = work.get();
                em.flush();
                return result;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}

// Usage
UnitOfWork unitOfWork = new UnitOfWork(em, transactionTemplate);

Order order = unitOfWork.executeInTransaction(() -> {
    User user = em.find(User.class, userId);
    Order order = new Order(user, items);
    em.persist(order);
    return order;
});
```

---

## Active Record

### Entity as Active Record

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Static finder methods
    public static User findById(EntityManager em, Long id) {
        return em.find(User.class, id);
    }

    public static List<User> findAll(EntityManager em) {
        return em.createQuery("FROM User", User.class).getResultList();
    }

    // Instance methods
    public void save(EntityManager em) {
        if (id == null) {
            em.persist(this);
        } else {
            em.merge(this);
        }
    }

    public void delete(EntityManager em) {
        em.remove(this);
    }
}
```

---

## Data Mapper

### Mapper Interface

```java
public interface UserMapper {
    User toEntity(UserDTO dto);
    UserDTO toDTO(User entity);
    List<UserDTO> toDTOList(List<User> entities);
}

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        return user;
    }

    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail()
        );
    }
}
```

---

## Best Practices

### 1. Use Repository Pattern

```java
// Prefer Spring Data Repository over DAO
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods
}
```

### 2. Separate Concerns

```java
// Entity - Domain model
@Entity
public class User { }

// Repository - Data access
@Repository
public interface UserRepository extends JpaRepository<User, Long> { }

// Service - Business logic
@Service
public class UserService { }
```

### 3. Use DTOs for Read Operations

```java
public record UserDTO(Long id, String name, String email) {}

@Query("SELECT new com.example.UserDTO(u.id, u.name, u.email) FROM User u")
List<UserDTO> findAllProjected();
```

### 4. Avoid Anemic Domain Model

```java
// Bad - Anemic
@Entity
public class User {
    private String name;
    // Only getters/setters
}

// Good - Rich domain
@Entity
public class User {
    private String name;

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.name = newName;
    }
}
```

### 5. Use Specification for Dynamic Queries

```java
public class UserSpecifications {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) ->
            cb.like(root.get("name"), "%" + name + "%");
    }
}
```

---

## Further Reading

- [Repository Pattern](https://martinfowler.com/eaaCatalog/repository.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM](https://hibernate.org/orm/)
