# JPA Fundamentals

## Overview
Java Persistence API (JPA) is a Java specification for accessing, persisting, and managing data between Java objects and a relational database. It's a standard API, not an implementation.

## Key Concepts

### JPA vs Hibernate
| Aspect | JPA | Hibernate |
|--------|-----|-----------|
| Type | Specification (API) | Implementation |
| Purpose | Standard interface | Full ORM framework |
| Annotations | `javax.persistence.*` | `org.hibernate.*` |
| Flexibility | Any implementation | Feature-rich |

### JPA Annotations
| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as persistent entity |
| `@Table` | Specifies database table name |
| `@Id` | Marks primary key field |
| `@GeneratedValue` | Auto-generates primary key |
| `@Column` | Maps field to column |
| `@ManyToOne` | Many-to-one relationship |
| `@OneToMany` | One-to-many relationship |
| `@ManyToMany` | Many-to-many relationship |
| `@OneToOne` | One-to-one relationship |

### JPA Repository Interface
```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    List<User> findByEmailContaining(String email);
    Optional<User> findByEmail(String email);
}
```

## Topics
- Entity Mapping
- Relationships (One-to-One, One-to-Many, Many-to-Many)
- JPQL (Java Persistence Query Language)
- Criteria API
- Named Queries
- Transaction Management
- Auditing
- Projections
- Specifications

## Learning Objectives
- Map Java objects to database tables
- Define relationships between entities
- Write JPQL and Criteria queries
- Implement repository pattern

## Prerequisites
- Spring Boot basics
- SQL fundamentals
- OOP concepts
