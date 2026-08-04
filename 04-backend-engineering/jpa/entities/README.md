# JPA Entities

## Comprehensive Guide to Entity Mapping

JPA entities map Java classes to database tables. This guide covers @Entity, @Id, @Column, and other mapping annotations.

---

## Table of Contents

1. [Entity Basics](#entity-basics)
2. [Primary Keys](#primary-keys)
3. [Column Mapping](#column-mapping)
4. [Temporal Mapping](#temporal-mapping)
5. [Best Practices](#best-practices)

---

## Entity Basics

### Basic Entity

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User() {}  // Required

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }
}
```

### Entity Rules

```
1. Must have no-arg constructor
2. Must not be final
3. Must have @Entity annotation
4. Must have @Id for primary key
5. Fields must be private with getters/setters
6. Must implement equals() and hashCode()
```

---

## Primary Keys

### ID Generation Strategies

```java
// Identity (auto-increment)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// Sequence (database sequence)
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator = "user_seq")
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq",
                   allocationSize = 1)
private Long id;

// Table (ID table)
@Id
@GeneratedValue(strategy = GenerationType.TABLE,
                generator = "user_table_gen")
@TableGenerator(name = "user_table_gen",
                table = "id_generator",
                pkColumnValue = "user_id")
private Long id;

// UUID
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

// Custom
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```

### Composite Keys

```java
@Embeddable
public class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;

    // equals() and hashCode()
}

@Entity
@Table(name = "order_items")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    private int quantity;
}
```

---

## Column Mapping

### Column Annotations

```java
@Entity
@Table(name = "users",
       indexes = @Index(name = "idx_email", columnList = "email"),
       uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @Lob
    private byte[] avatar;

    @Transient
    private String temporary;
}
```

### Enum Mapping

```java
// String (recommended)
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private UserStatus status;

// Ordinal (avoid)
@Enumerated(EnumType.ORDINAL)
private UserStatus status;

public enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED
}
```

---

## Temporal Mapping

### Date/Time Types

```java
@Entity
public class Event {

    // Date
    @Temporal(TemporalType.DATE)
    private Date eventDate;

    // Time
    @Temporal(TemporalType.TIME)
    private Date eventTime;

    // Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Java 8+ (recommended)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
```

### Auto-Timestamps

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {

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

---

## Best Practices

### 1. Use Meaningful Table Names

```java
@Entity
@Table(name = "user_accounts")  // Not "user" (reserved word)
public class UserAccount { }
```

### 2. Always Define Column Length

```java
@Column(length = 100)  // Not VARCHAR(255) by default
private String name;
```

### 3. Use EnumType.STRING

```java
@Enumerated(EnumType.STRING)  // Not ORDINAL
private Status status;
```

### 4. Implement equals() and hashCode()

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id != null && id.equals(user.id);
}

@Override
public int hashCode() {
    return getClass().hashCode();
}
```

### 5. Use DTOs for Read Operations

```java
public record UserDTO(Long id, String name, String email) {
    public static UserDTO from(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
```

---

## Further Reading

- [JPA Entities](https://docs.oracle.com/javaee/7/api/javax/persistence/Entity.html)
- [JPA Annotations](https://jakarta.ee/specifications/persistence/3.1/jakarta.persistence-spec-3.1.html)
- [Hibernate Mapping](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#mapping)
