# Module 35: Spring Data JPA

## Overview
Spring Data JPA simplifies data access layer implementation. It provides repository abstraction, query methods, and automatic query generation, reducing boilerplate code for database operations.

## Learning Objectives
- Master repository interfaces
- Understand query methods
- Use @Query annotations
- Implement auditing
- Handle transactions

## Prerequisites
- Spring Core basics
- JPA/Hibernate knowledge
- SQL fundamentals

## Why This Concept Exists
Traditional data access requires:
- Boilerplate DAO code
- Manual query writing
- Transaction management
- Connection handling

Spring Data JPA provides:
- Repository abstraction
- Auto-generated queries
- Built-in pagination
- Auditing support

## Problem Statement
How do you reduce boilerplate while maintaining flexible data access?

## Theory

### Repository Types

| Type | Purpose |
|------|---------|
| CrudRepository | Basic CRUD operations |
| PagingAndSortingRepository | Pagination support |
| JpaRepository | JPA-specific methods |

### Query Methods

| Pattern | Example |
|---------|---------|
| find | findBy Name |
| count | countByStatus |
| exists | existsByEmail |
| delete | deleteByAge |
| OrderBy | findByNameOrderById |

## Internal Working

### Query Generation
1. Parse method name
2. Generate JPQL
3. Execute query
4. Return results

### Proxy Creation
1. Create proxy class
2. Implement repository interface
3. Inject EntityManager
4. Handle transactions

## JVM Perspective

### Proxy Mechanism
- JDK dynamic proxies
- CGLIB proxies
- Runtime class generation
- Method interception

### Query Cache
- First-level cache (persistence context)
- Second-level cache (shared)
- Query cache (query results)
- Natural ID cache

## Memory Representation
```
Repository Proxy:
┌─────────────────────────────────────┐
│ Interface Methods                    │
│  ├─ findBy*()                       │
│  ├─ save()                          │
│  └─ delete()                        │
├─────────────────────────────────────┤
│ EntityManager                       │
│  ├─ Persistence Context             │
│  └─ Query Cache                     │
└─────────────────────────────────────┘
```

## Architecture Diagram

```mermaid
graph TD
    A[Spring Data JPA] --> B[Repositories]
    A --> C[Query Methods]
    A --> D[Auditing]
    A --> E[Transactions]
    
    B --> F[CrudRepository]
    B --> G[PagingAndSortingRepository]
    B --> H[JpaRepository]
    
    C --> I[Method Names]
    C --> J[@Query]
    C --> K[Named Queries]
    
    D --> L[CreatedDate]
    D --> M[LastModifiedDate]
```

## Flow Diagram

```mermaid
graph TD
    A[Define Repository] --> B[Extend Interface]
    B --> C{Query Type?}
    C -->|Derived| D[Method Name]
    C -->|Custom| E[@Query]
    C -->|Named| F[JPQL File]
    
    D --> G[Generate Query]
    E --> G
    F --> G
    
    G --> H[Execute]
    H --> I[Return Results]
```

## Syntax

### Repository Interface
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Derived query
    User findByEmail(String email);
    
    List<User> findByAgeGreaterThan(int age);
    
    // @Query annotation
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") String status);
    
    // Native query
    @Query(value = "SELECT * FROM users WHERE age > :age", nativeQuery = true)
    List<User> findOlderThan(@Param("age") int age);
}
```

### Entity
```java
import jakarta.persistence.*;
import org.springframework.data.annotation.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true)
    private String email;
    
    private int age;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors, getters, setters
}
```

### Repository Usage
```java
@Service
public class UserService {
    private final UserRepository repository;
    
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public List<User> findAll() {
        return repository.findAll();
    }
    
    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    public User save(User user) {
        return repository.save(user);
    }
    
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
```

## Easy Example
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByPriceLessThan(double price);
}

// Entity
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double price;
}

// Service
@Service
public class ProductService {
    private final ProductRepository repository;
    
    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    
    public List<Product> findCheapProducts() {
        return repository.findByPriceLessThan(100.0);
    }
}
```

## Medium Example
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

// Repository with custom queries
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.total > :minTotal ORDER BY o.createdAt DESC")
    List<Order> findHighValueOrders(@Param("minTotal") double minTotal);
    
    @Query(value = "SELECT * FROM orders WHERE created_at > :date", nativeQuery = true)
    List<Order> findRecentOrders(@Param("date") LocalDateTime date);
    
    // Pagination
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
}

// Service with pagination
@Service
public class OrderService {
    private final OrderRepository repository;
    
    public Page<Order> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable);
    }
}
```

## Hard Example
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.*;

// Complex repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Projection
    @Query("SELECT new com.example.dto.UserDTO(u.name, u.email) FROM User u")
    List<UserDTO> findAllUserDTOs();
    
    // Aggregation
    @Query("SELECT u.department, COUNT(u) FROM User u GROUP BY u.department")
    List<Object[]> countByDepartment();
    
    // Dynamic query
    @Query("SELECT u FROM User u WHERE " +
           "(:name IS NULL OR u.name = :name) AND " +
           "(:age IS NULL OR u.age = :age)")
    List<User> findByOptionalCriteria(
        @Param("name") String name,
        @Param("age") Integer age);
    
    // Specification
    List<User> findAll(Specification<User> spec);
}

// Specification builder
public class UserSpecs {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> 
            name == null ? null : cb.equal(root.get("name"), name);
    }
    
    public static Specification<User> ageGreaterThan(int age) {
        return (root, query, cb) -> 
            cb.greaterThan(root.get("age"), age);
    }
}
```

## Enterprise Example
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;
import java.util.concurrent.CompletableFuture;

// Repository with auditing
@EntityListeners(AuditingEntityListener.class)
@Entity
public class AuditEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedBy
    private String lastModifiedBy;
    
    @Version
    private Long version;
}

// Async repository
public interface AsyncRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.status = :status")
    CompletableFuture<List<User>> findByStatusAsync(@Param("status") String status);
}

// Transactional service
@Service
@Transactional
public class TransactionalService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    @Transactional
    public void processOrder(Long userId, Order order) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        order.setUser(user);
        orderRepository.save(order);
        
        user.setOrderCount(user.getOrderCount() + 1);
        userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}
```

## Performance Considerations
- Use projections for read-only queries
- Enable second-level cache
- Use batch fetching for collections
- Avoid N+1 queries with @EntityGraph

## Time & Space Complexity
| Operation | Time | Space |
|-----------|------|-------|
| findById | O(1) | O(1) |
| findAll | O(n) | O(n) |
| save | O(1) | O(1) |
| delete | O(1) | O(1) |

## Thread Safety
- Repositories are thread-safe
- Entities are not thread-safe
- Use @Transactional for consistency
- Persistence context is thread-bound

## Best Practices
1. Use constructor injection
2. Keep repositories focused
3. Use projections for read-only
4. Enable auditing
5. Use specifications for dynamic queries

## Common Mistakes
1. N+1 query problems
2. Not using pagination
3. Missing @Transactional
4. Lazy loading issues

## Pitfalls & Warnings
1. Transient state issues
2. Detached entity problems
3. Cascade operations
4. Locking conflicts

## Debugging Tips
1. Enable SQL logging
2. Use QueryUtils for analysis
3. Check generated queries
4. Monitor query performance

## Comparison Table

| Feature | Spring Data JPA | JDBC | MyBatis |
|---------|-----------------|------|---------|
| Boilerplate | Low | High | Medium |
| Type Safety | Yes | No | Yes |
| Dynamic Queries | Limited | Full | Full |
| Performance | Good | Best | Good |

## Decision Tree

```mermaid
graph TD
    A[Data Access] --> B{Complexity?}
    B -->|Simple| C[Spring Data JPA]
    B -->|Complex| D{Type?}
    D -->|Dynamic| E[Specifications]
    D -->|Native| F[@Query]
    D -->|Stored Proc| G[StoredProcedureQuery]
```

## Interview Questions

### Q1: What is Spring Data JPA?
**Answer:** Simplifies JPA data access with repository abstraction.

### Q2: What is CrudRepository?
**Answer:** Interface providing basic CRUD operations.

### Q3: How do you define custom queries?
**Answer:** Use @Query annotation or method naming conventions.

### Q4: What is pagination?
**Answer:** Splitting results into pages for better performance.

### Q5: What is auditing?
**Answer:** Automatic tracking of entity creation/modification.

### Q6: What is @Transactional?
**Answer:** Annotation for transaction management.

### Q7: What is the difference between find and query?
**Answer:** find uses derived queries, query uses JPQL.

### Q8: What is a projection?
**Answer:** Partial entity loading for performance.

### Q9: What is N+1 problem?
**Answer:** Multiple queries when one would suffice.

### Q10: What is @EntityGraph?
**Answer:** Annotation to define fetch plans.

### Q11: What is Specification?
**Answer:** Type-safe query building for dynamic queries.

### Q12: What is the difference between JPQL and SQL?
**Answer:** JPQL uses entity names, SQL uses table names.

### Q13: How do you handle transactions?
**Answer:** Use @Transactional annotation.

### Q14: What is cascade in JPA?
**Answer:** Propagation of operations to related entities.

### Q15: What is lazy vs eager loading?
**Answer:** Lazy loads on access, eager loads immediately.

## Exercises

### Easy
1. Create a simple repository
2. Use derived query methods
3. Implement CRUD operations

### Medium
1. Add pagination support
2. Use @Query annotations
3. Implement auditing

### Hard
1. Build specification-based queries
2. Implement async repositories
3. Create custom repository methods

## Summary
Spring Data JPA simplifies data access with repository abstraction and automatic query generation.

## References
- Spring Data JPA Documentation
- Hibernate Documentation
- Baeldung Spring Data JPA
