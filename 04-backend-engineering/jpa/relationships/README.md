# JPA Relationships

## Comprehensive Guide to Entity Relationships

JPA defines four relationship types. This guide covers OneToOne, OneToMany, ManyToOne, and ManyToMany mappings.

---

## Table of Contents

1. [Relationship Types](#relationship-types)
2. [OneToOne](#onetoone)
3. [OneToMany/ManyToOne](#onetomany-manytoone)
4. [ManyToMany](#manytomany)
5. [Fetch Types](#fetch-types)
6. [Cascade Types](#cascade-types)
7. [Best Practices](#best-practices)

---

## Relationship Types

### Overview

```
One-to-One:    User <-> UserProfile
One-to-Many:   User -> Orders
Many-to-One:   Orders -> User
Many-to-Many:  Students <-> Courses
```

---

## OneToOne

### Bidirectional

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL,
              fetch = FetchType.LAZY,
              mappedBy = "user")
    private UserProfile profile;
}

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
```

### Unidirectional

```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_spot_id")
    private ParkingSpot parkingSpot;
}

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
}
```

---

## OneToMany/ManyToOne

### Bidirectional

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }
}

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
```

### With Join Table

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_addresses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses = new ArrayList<>();
}
```

---

## ManyToMany

### Bidirectional

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
}

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();
}
```

---

## Fetch Types

### EAGER vs LAZY

```java
// EAGER - Loads immediately
@ManyToOne(fetch = FetchType.EAGER)
private User user;

// LAZY - Loads on access (default for collections)
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

### Recommendation

```
@ManyToOne   -> FetchType.LAZY
@OneToOne    -> FetchType.LAZY (use JOIN FETCH when needed)
@OneToMany   -> FetchType.LAZY (always)
@ManyToMany  -> FetchType.LAZY (always)
```

---

## Cascade Types

### Cascade Options

```java
// ALL - Cascades everything
@OneToMany(cascade = CascadeType.ALL)
private List<Order> orders;

// PERSIST and MERGE only
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List<Order> orders;

// REMOVE with orphanRemoval
@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
private List<Order> orders;
```

---

## Best Practices

### 1. Use LAZY Fetching

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

### 2. Avoid Bidirectional When Possible

```java
// Unidirectional is simpler
@ManyToOne
private User user;
```

### 3. Use CascadeType Appropriately

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
private List<Order> orders;
```

### 4. Override equals() and hashCode()

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

### 5. Use JOIN FETCH for Queries

```java
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

---

## Further Reading

- [JPA Relationships](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html)
- [Hibernate Associations](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#associations)
