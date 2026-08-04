# JPA Criteria API

## Comprehensive Guide to Type-Safe Queries

The Criteria API provides a programmatic, type-safe way to build queries. This guide covers CriteriaBuilder, CriteriaQuery, and dynamic queries.

---

## Table of Contents

1. [Criteria Basics](#criteria-basics)
2. [Predicates](#predicates)
3. [Joins](#joins)
4. [Aggregation](#aggregation)
5. [Dynamic Queries](#dynamic-queries)
6. [Best Practices](#best-practices)

---

## Criteria Basics

### Simple Query

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);

Root<User> root = query.from(User.class);
query.select(root);

List<User> users = em.createQuery(query).getResultList();
```

### With WHERE

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);

Root<User> root = query.from(User.class);
query.where(cb.equal(root.get("active"), true));

List<User> users = em.createQuery(query).getResultList();
```

### With ORDER BY

```java
query.orderBy(
    cb.desc(root.get("createdAt")),
    cb.asc(root.get("name"))
);
```

---

## Predicates

### Comparison Operators

```java
// EQUALS
cb.equal(root.get("name"), "John")

// NOT EQUALS
cb.notEqual(root.get("status"), UserStatus.INACTIVE)

// GREATER THAN
cb.greaterThan(root.get("age"), 18)

// LESS THAN
cb.lessThan(root.get("price"), 100.0)

// BETWEEN
cb.between(root.get("age"), 18, 65)

// LIKE
cb.like(root.get("email"), "%@example.com")

// IN
root.get("status").in(UserStatus.ACTIVE, UserStatus.PENDING)
```

### Logical Operators

```java
// AND
cb.and(predicate1, predicate2)

// OR
cb.or(predicate1, predicate2)

// NOT
cb.not(predicate)

// Combined
cb.and(
    cb.equal(root.get("active"), true),
    cb.or(
        cb.like(root.get("email"), "%@example.com"),
        cb.like(root.get("email"), "%@partner.com")
    )
)
```

---

## Joins

### Simple Join

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);

Root<User> root = query.from(User.class);
Join<User, Order> orderJoin = root.join("orders");

query.where(cb.greaterThan(orderJoin.get("total"), 100.0));

List<User> users = em.createQuery(query).getResultList();
```

### Fetch Join

```java
Root<User> root = query.from(User.class);
root.fetch("orders", JoinType.LEFT);

query.distinct(true);
```

### Multiple Joins

```java
Root<User> root = query.from(User.class);
Join<User, Order> orderJoin = root.join("orders");
Join<Order, Product> productJoin = orderJoin.join("products");

query.where(cb.equal(productJoin.get("category"), "Electronics"));
```

---

## Aggregation

### COUNT

```java
CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
Root<User> root = countQuery.from(User.class);
countQuery.select(cb.count(root));

Long count = em.createQuery(countQuery).getSingleResult();
```

### GROUP BY

```java
CriteriaQuery<Tuple> query = cb.createTupleQuery();
Root<User> root = query.from(User.class);

query.select(cb.tuple(
    root.get("status"),
    cb.count(root)
));
query.groupBy(root.get("status"));

List<Tuple> results = em.createQuery(query).getResultList();
```

---

## Dynamic Queries

### Builder Pattern

```java
public class UserQuery {

    private final CriteriaBuilder cb;
    private final Root<User> root;
    private final List<Predicate> predicates = new ArrayList<>();

    public UserQuery(CriteriaBuilder cb, Root<User> root) {
        this.cb = cb;
        this.root = root;
    }

    public UserQuery withName(String name) {
        if (name != null) {
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public UserQuery withStatus(UserStatus status) {
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        return this;
    }

    public UserQuery withAgeBetween(Integer min, Integer max) {
        if (min != null && max != null) {
            predicates.add(cb.between(root.get("age"), min, max));
        }
        return this;
    }

    public Predicate build() {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

// Usage
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> root = query.from(User.class);

Predicate predicate = new UserQuery(cb, root)
    .withName("John")
    .withStatus(UserStatus.ACTIVE)
    .withAgeBetween(18, 65)
    .build();

query.where(predicate);
```

### Specification (Spring Data JPA)

```java
public class UserSpecifications {

    public static Specification<User> hasName(String name) {
        return (root, query, cb) ->
            cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) ->
            cb.equal(root.get("status"), status);
    }

    public static Specification<User> ageBetween(int min, int max) {
        return (root, query, cb) ->
            cb.between(root.get("age"), min, max);
    }
}

// Usage
List<User> users = userRepository.findAll(
    Specification
        .where(UserSpecifications.hasName("John"))
        .and(UserSpecifications.hasStatus(UserStatus.ACTIVE))
);
```

---

## Best Practices

### 1. Use Type-Safe Queries

```java
// Good - Type-safe
cb.equal(root.get("email"), "john@example.com")

// Bad - String-based (error-prone)
em.createQuery("SELECT u FROM User u WHERE u.email = :email")
```

### 2. Use Specifications for Dynamic Queries

```java
public interface UserSpecification extends Specification<User> {
    default void and(Specification<User> other) {
        return this.and(other);
    }
}
```

### 3. Use Tuple for Multiple Columns

```java
CriteriaQuery<Tuple> query = cb.createTupleQuery();
query.select(cb.tuple(
    root.get("id"),
    root.get("name"),
    root.get("email")
));
```

### 4. Use Fetch for Eager Loading

```java
root.fetch("orders", JoinType.LEFT);
query.distinct(true);
```

### 5. Cache Query Results

```java
@Cacheable("users")
List<User> findUsers(Specification<User> spec) {
    return userRepository.findAll(spec);
}
```

---

## Further Reading

- [JPA Criteria API](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/CriteriaBuilder.html)
- [Spring Data JPA Specifications](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specs)
- [Hibernate Criteria](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#criteria)
