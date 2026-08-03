# Module 36: Spring Data

## Overview
Spring Data provides a consistent data access layer for relational databases. It builds on top of JPA/Hibernate and provides repository abstractions, query methods, and auditing.

**Note:** This module covers Spring Data's repository pattern. For JPA fundamentals, see `jpa/01-fundamentals`. For Hibernate-specific features, see `hibernate/01-fundamentals`.

## Learning Objectives
- Master Spring Data repositories
- Implement query methods
- Use JPA specifications
- Implement auditing
- Handle projections

## Prerequisites
- JPA basics (see `jpa/01-fundamentals`)
- Hibernate knowledge (see `hibernate/01-fundamentals`)
- Spring Boot basics

## Why This Concept Exists
Without Spring Data, you need:
- Manual repository implementations
- Boilerplate CRUD code
- Custom query methods
- Manual auditing

Spring Data provides:
- Repository interface
- Query derivation
- Custom queries
- Pagination & sorting
- Auditing support

## Problem Statement
How do you reduce boilerplate code for data access?

## Theory

### Repository Hierarchy

| Interface | Purpose |
|-----------|---------|
| Repository | Base marker interface |
| CrudRepository | CRUD operations |
| PagingAndSortingRepository | Pagination support |
| JpaRepository | JPA-specific methods |

### Query Methods

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Derived queries
    List<User> findByName(String name);
    List<User> findByEmailContaining(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // JPQL
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") UserRole role);
    
    // Native
    @Query(value = "SELECT * FROM users WHERE active = true", nativeQuery = true)
    List<User> findActiveUsers();
}
```

### Specifications

```java
public class UserSpecifications {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
    
    public static Specification<User> hasRole(UserRole role) {
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }
}

// Usage
userRepository.findAll(
    Specification.where(UserSpecifications.hasName("John"))
        .and(UserSpecifications.hasRole(UserRole.ADMIN))
);
```

### Auditing

```java
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String modifiedBy;
}
```

## Examples

### Basic Repository
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
}
```

### Custom Repository Implementation
```java
public interface CustomProductRepository {
    List<Product> findExpensiveProducts(BigDecimal threshold);
}

public class CustomProductRepositoryImpl implements CustomProductRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<Product> findExpensiveProducts(BigDecimal threshold) {
        return entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.price > :threshold", Product.class)
            .setParameter("threshold", threshold)
            .getResultList();
    }
}
```

## Common Pitfalls
1. **N+1 queries** - Use `@EntityGraph` or `JOIN FETCH`
2. **Lazy loading exceptions** - Use `@Transactional(readOnly = true)`
3. **Query naming** - Follow Spring Data naming conventions

## Interview Questions
1. Difference between `JpaRepository` and `CrudRepository`?
2. How do you implement pagination?
3. What are specifications and when to use them?
4. How does Spring Data generate queries?
5. Explain auditing in Spring Data.

## Further Reading
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods)
