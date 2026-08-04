# Hibernate Querying

## Comprehensive Guide to HQL, Criteria, and Native SQL

Hibernate provides multiple query APIs. This guide covers HQL, Criteria API, and Native SQL queries.

---

## Table of Contents

1. [HQL (Hibernate Query Language)](#hql)
2. [Criteria API](#criteria-api)
3. [Native SQL](#native-sql)
4. [Named Queries](#named-queries)
5. [Best Practices](#best-practices)

---

## HQL

### Basic Queries

```java
Session session = sessionFactory.getCurrentSession();

// Simple SELECT
List<User> users = session.createQuery("FROM User", User.class)
    .getResultList();

// With WHERE clause
User user = session.createQuery(
        "FROM User WHERE email = :email", User.class)
    .setParameter("email", "john@example.com")
    .uniqueResult();

// With JOIN
List<User> users = session.createQuery(
        "SELECT DISTINCT u FROM User u JOIN FETCH u.orders", User.class)
    .getResultList();

// With aggregation
Long count = session.createQuery(
        "SELECT COUNT(u) FROM User u WHERE u.active = true", Long.class)
    .uniqueResult();
```

### Parameter Binding

```java
// Named parameters
session.createQuery("FROM User WHERE name = :name", User.class)
    .setParameter("name", "John")
    .getResultList();

// Positional parameters
session.createQuery("FROM User WHERE id = ?", User.class)
    .setParameter(1, 1L)
    .uniqueResult();

// Collection parameters
session.createQuery("FROM User WHERE id IN :ids", User.class)
    .setParameter("ids", List.of(1L, 2L, 3L))
    .getResultList();
```

### Pagination

```java
session.createQuery("FROM User", User.class)
    .setFirstResult(0)    // Offset
    .setMaxResults(10)    // Limit
    .getResultList();
```

### DML Queries

```java
// UPDATE
int updated = session.createQuery(
        "UPDATE User SET active = false WHERE lastLogin < :date")
    .setParameter("date", LocalDateTime.now().minusMonths(6))
    .executeUpdate();

// DELETE
int deleted = session.createQuery(
        "DELETE FROM User WHERE active = false")
    .executeUpdate();
```

---

## Criteria API

### Basic Criteria

```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);

Root<User> root = query.from(User.class);
query.select(root);

// WHERE
query.where(cb.equal(root.get("active"), true));

List<User> users = session.createQuery(query).getResultList();
```

### Complex Queries

```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);

Root<User> root = query.from(User.class);

// Multiple conditions
Predicate active = cb.equal(root.get("active"), true);
Predicate emailDomain = cb.like(root.get("email"), "%@example.com");
Predicate ageRange = cb.between(root.get("age"), 18, 65);

query.where(cb.and(active, emailDomain, ageRange));

// ORDER BY
query.orderBy(cb.desc(root.get("createdAt")));

// SELECT specific fields
CriteriaQuery<Tuple> tupleQuery = cb.createTupleQuery();
tupleQuery.select(cb.tuple(
    root.get("id"),
    root.get("name"),
    root.get("email")
));
```

### Aggregation

```java
CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
Root<User> root = countQuery.from(User.class);
countQuery.select(cb.count(root));

Long count = session.createQuery(countQuery).uniqueResult();
```

---

## Native SQL

### Basic Native Query

```java
// Simple query
List<User> users = session.createNativeQuery(
        "SELECT * FROM users WHERE active = true", User.class)
    .getResultList();

// With parameters
List<User> users = session.createNativeQuery(
        "SELECT * FROM users WHERE email = :email", User.class)
    .setParameter("email", "john@example.com")
    .getResultList();
```

### Scalar Queries

```java
// Single value
Long count = session.createNativeQuery(
        "SELECT COUNT(*) FROM users", Long.class)
    .getSingleResult();

// Multiple values
List<Object[]> results = session.createNativeQuery(
        "SELECT id, name, email FROM users")
    .getResultList();
```

### SQL with JOIN

```java
List<Object[]> results = session.createNativeQuery(
        "SELECT u.id, u.name, COUNT(o.id) " +
        "FROM users u " +
        "LEFT JOIN orders o ON u.id = o.user_id " +
        "GROUP BY u.id, u.name")
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
    query = "FROM User WHERE email = :email"
)
@NamedQuery(
    name = "User.findActive",
    query = "FROM User WHERE active = true"
)
public class User {
    // ...
}
```

### Usage

```java
User user = session.createNamedQuery("User.findByEmail", User.class)
    .setParameter("email", "john@example.com")
    .uniqueResult();

List<User> users = session.createNamedQuery("User.findActive", User.class)
    .getResultList();
```

---

## Best Practices

### 1. Use Parameterized Queries

```java
// Good - Parameterized
session.createQuery("FROM User WHERE email = :email")
    .setParameter("email", userInput);

// Bad - String concatenation (SQL injection risk)
session.createQuery("FROM User WHERE email = '" + userInput + "'");
```

### 2. Use Fetch Joins for Associations

```java
// Avoids N+1 problem
session.createQuery(
        "SELECT u FROM User u JOIN FETCH u.orders", User.class)
    .getResultList();
```

### 3. Use Projections for Read-Only Data

```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<UserSummary> query = cb.createQuery(UserSummary.class);

Root<User> root = query.from(User.class);
query.select(cb.construct(UserSummary.class,
    root.get("id"),
    root.get("name")
));
```

### 4. Use ScrollableResults for Large Datasets

```java
ScrollableResults<User> scroll = session.createQuery("FROM User", User.class)
    .scroll(ScrollMode.FORWARD_ONLY);

while (scroll.next()) {
    User user = scroll.get();
    processUser(user);
}
```

### 5. Use StatelessSession for Batch Operations

```java
StatelessSession statelessSession = sessionFactory.openStatelessSession();
Transaction tx = statelessSession.beginTransaction();

for (User user : users) {
    statelessSession.insert(user);
}

tx.commit();
statelessSession.close();
```

---

## Further Reading

- [Hibernate Query Language](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#query-language)
- [Criteria API](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/CriteriaBuilder.html)
- [JPA Native Queries](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html)
