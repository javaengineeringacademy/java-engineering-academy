# Encapsulation in Java

## Table of Contents
- [Introduction](#introduction)
- [Learning Objectives](#learning-objectives)
- [Prerequisites](#prerequisites)
- [Why This Concept Exists](#why-this-concept-exists)
- [Internal Working](#internal-working)
- [Syntax](#syntax)
- [Easy Examples](#easy-examples)
- [Medium Examples](#medium-examples)
- [Hard Examples](#hard-examples)
- [Exercises](#exercises)
- [Interview Questions](#interview-questions)
- [Common Pitfalls](#common-pitfalls)
- [Best Practices](#best-practices)
- [Real World Usage](#real-world-usage)
- [References](#references)
- [Summary](#summary)

---

## Introduction

Encapsulation is one of the four fundamental pillars of object-oriented programming, alongside inheritance, polymorphism, and abstraction. It is the practice of bundling data (fields) and the methods that operate on that data into a single unit (class), while restricting direct access to some of the object's components. In Java, encapsulation is achieved through access modifiers — `private`, `protected`, `public`, and package-private — which control what code can read, write, or invoke on an object's internal state. This mechanism ensures data integrity, reduces coupling between components, and provides a stable API that can evolve without breaking existing code. Mastering encapsulation is essential for building robust, maintainable, and secure Java applications.

---

## Learning Objectives

- Understand the principles of data hiding and information hiding in Java
- Implement proper getters and setters with validation logic
- Apply access modifiers strategically to control visibility
- Follow JavaBean conventions for interoperability with frameworks

---

## Prerequisites

- [01-classes-and-objects/README.md](../01-classes-and-objects/README.md) — Classes, objects, fields, and methods
- [03-methods/README.md](../03-methods/README.md) — Method definitions and parameters
- [06-this-keyword/README.md](../06-this-keyword/README.md) — Using `this` in setters

---

## Why This Concept Exists

### The Problem

Without encapsulation, any code can directly access and modify an object's fields:

```java
public class BankAccount {
    public double balance; // Anyone can modify this!
}

// Later...
BankAccount account = new BankAccount();
account.balance = -1000; // Negative balance? No validation!
account.balance = Double.NaN; // Corrupted state!
```

This leads to:
- **Invalid states:** Objects can be put into inconsistent or meaningless states
- **Tight coupling:** Changes to internal representation break all calling code
- **Security vulnerabilities:** Sensitive data (passwords, keys) can be exposed
- **Debugging nightmares:** With unrestricted access, it's impossible to track who changed what

### The Solution

Encapsulation restricts direct access to fields and requires all interactions through controlled methods (getters/setters):

```java
public class BankAccount {
    private double balance; // Only accessible within this class

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
    }
}
```

### Real-World Analogy

Think of a car's dashboard. You don't have direct access to the engine, fuel lines, or transmission. Instead, you interact through a steering wheel, pedals, and gear shift. These controls (methods) validate your inputs and translate them into safe operations on the internal components (fields). Encapsulation provides the same controlled interface to objects.

---

## Internal Working

### How Access Modifiers Work at the JVM Level

Access modifiers (`private`, `protected`, `public`, and package-private) are enforced at compile time by the Java compiler, not at runtime by the JVM. The JVM bytecode does not contain access modifier checks — the compiler ensures that only authorized code can access restricted members.

#### Access Level Matrix

```
┌──────────────────┬───────────┬─────────────┬───────────┬─────────────┐
│ Modifier         │ Class     │ Package     │ Subclass  │ World       │
├──────────────────┼───────────┼─────────────┼───────────┼─────────────┤
│ private          │ ✓         │ ✗           │ ✗         │ ✗           │
│ (default)        │ ✓         │ ✓           │ ✗         │ ✗           │
│ protected        │ ✓         │ ✓           │ ✓         │ ✗           │
│ public           │ ✓         │ ✓           │ ✓         │ ✓           │
└──────────────────┴───────────┴─────────────┴───────────┴─────────────┘
```

### Memory Impact

Encapsulation has zero runtime overhead. The JVM stores private and public fields identically in the object's memory layout. The access restriction is purely a compile-time construct.

```
Object memory layout (same regardless of access modifiers):
┌─────────────────────────────┐
│ Object header (class info)  │
│ Field: name (private)       │  ← Stored identically
│ Field: age (public)         │  ← Stored identically
│ Field: salary (private)     │  ← Stored identically
└─────────────────────────────┘
```

### Bytecode Evidence

When accessing a private field, the bytecode is identical to accessing a public field:
```bytecode
aload_0       // Load 'this'
getfield      #N  // Get field (same instruction for public and private)
```

The compiler simply refuses to generate bytecode that violates access rules.

---

## Syntax

### 1. Private Fields with Public Getters/Setters

```java
public class Person {
    private String name;
    private int age;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

### 2. Read-Only Properties (Getter Only)

```java
public class Employee {
    private final String employeeId;

    public Employee(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() { return employeeId; }
    // No setter — immutable after construction
}
```

### 3. Computed Properties

```java
public class Rectangle {
    private double width;
    private double height;

    public double getArea() { return width * height; }
    public double getPerimeter() { return 2 * (width + height); }
}
```

### 4. Validation in Setters

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
    this.age = age;
}
```

---

## Easy Examples

### Example 1: Basic Encapsulation with Validation

**Problem Statement:**
Create a `BankAccount` class that prevents invalid operations like negative deposits, overdrafts, and corrupted state.

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

public class BankAccount {
    private String accountId;
    private String ownerName;
    private double balance;
    private boolean active;

    public BankAccount(String accountId, String ownerName, double initialBalance) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (ownerName == null || ownerName.isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.active = true;
    }

    public String getAccountId() { return accountId; }

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
        this.ownerName = ownerName;
    }

    public double getBalance() { return balance; }

    public boolean isActive() { return active; }

    public void deposit(double amount) {
        if (!active) throw new IllegalStateException("Account is not active");
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
        System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
    }

    public void withdraw(double amount) {
        if (!active) throw new IllegalStateException("Account is not active");
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "BankAccount{id='" + accountId + "', owner='" + ownerName +
               "', balance=$" + String.format("%.2f", balance) +
               ", active=" + active + "}";
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("ACC001", "Alice Smith", 1000.00);
        System.out.println(account);

        account.deposit(500.00);
        account.withdraw(200.00);
        System.out.println(account);

        try {
            account.withdraw(5000.00); // Insufficient funds
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            account.deposit(-100); // Invalid amount
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        account.deactivate();
        try {
            account.deposit(100); // Account inactive
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
BankAccount{id='ACC001', owner='Alice Smith', balance=$1000.00, active=true}
Deposited $500.00. New balance: $1500.00
Withdrew $200.00. New balance: $1300.00
BankAccount{id='ACC001', owner='Alice Smith', balance=$1300.00, active=true}
Error: Insufficient funds
Error: Deposit amount must be positive
Error: Account is not active
```

**Best Practices:**
- Validate all inputs in setters and business methods
- Use `final` for fields that should never change after construction
- Provide descriptive error messages for validation failures
- Keep validation logic in the class, not in calling code

---

### Example 2: JavaBean Convention

**Problem Statement:**
Create a class that follows JavaBean conventions, which are required by many Java frameworks (Spring, Hibernate, Jackson, JSP).

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

public class Employee implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private double salary;
    private boolean active;

    // Default constructor (required by JavaBeans spec)
    public Employee() {
        this.active = true;
    }

    // Parameterized constructor
    public Employee(Long id, String firstName, String lastName, String email, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        setSalary(salary);
        this.active = true;
    }

    // Getters and Setters following JavaBean naming conventions
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = email;
    }

    public double getSalary() { return salary; }
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Computed property (read-only)
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Business logic methods
    public void giveRaise(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Invalid raise percentage");
        }
        this.salary *= (1 + percentage / 100);
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + getFullName() + "', email='" + email +
               "', salary=" + String.format("%.2f", salary) + ", active=" + active + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id != null && id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static void main(String[] args) {
        Employee emp = new Employee(1L, "John", "Doe", "john.doe@company.com", 75000.00);
        System.out.println(emp);

        emp.giveRaise(10);
        System.out.println("After 10% raise: " + emp);

        System.out.println("Full Name: " + emp.getFullName());

        // JavaBean introspection example
        try {
            java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(Employee.class);
            System.out.println("\nJavaBean Properties:");
            for (java.beans.PropertyDescriptor pd : info.getPropertyDescriptors()) {
                System.out.println("  " + pd.getName() + " (" + pd.getPropertyType().getSimpleName() + ")");
            }
        } catch (java.beans.IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
```

**Output:**
```
Employee{id=1, name='John Doe', email='john.doe@company.com', salary=75000.00, active=true}
After 10% raise: Employee{id=1, name='John Doe', email='john.doe@company.com', salary=82500.00, active=true}
Full Name: John Doe

JavaBean Properties:
  active (boolean)
  email (String)
  firstName (String)
  fullName (String)
  id (Long)
  lastName (String)
  salary (double)
```

**Best Practices:**
- Always provide a no-arg constructor for framework compatibility
- Implement `Serializable` for persistence and session storage
- Override `equals()` and `hashCode()` when objects will be used in collections
- Use wrapper types (`Long`, `Double`) instead of primitives for nullable fields

---

### Example 3: Immutable Class with Encapsulation

**Problem Statement:**
Create a truly immutable class where the state cannot change after construction, providing thread safety and predictability.

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.util.Collections;
import java.util.List;

public final class Money {
    private final double amount;
    private final String currency;

    public Money(double amount, String currency) {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO code");
        }
        this.amount = Math.round(amount * 100.0) / 100.0; // Round to 2 decimal places
        this.currency = currency.toUpperCase();
    }

    // Only getters, no setters
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }

    // Operations return new instances instead of modifying state
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract different currencies");
        }
        return new Money(this.amount - other.amount, this.currency);
    }

    public Money multiply(double factor) {
        return new Money(this.amount * factor, this.currency);
    }

    public boolean isPositive() { return amount > 0; }
    public boolean isNegative() { return amount < 0; }
    public boolean isZero() { return amount == 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0 &&
               currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(amount);
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", currency, amount);
    }

    public static void main(String[] args) {
        Money price = new Money(19.99, "USD");
        Money tax = new Money(1.60, "USD");
        Money total = price.add(tax);

        System.out.println("Price: " + price);
        System.out.println("Tax: " + tax);
        System.out.println("Total: " + total);

        // Original objects remain unchanged
        System.out.println("Price unchanged: " + price);

        Money doubled = price.multiply(2);
        System.out.println("Doubled: " + doubled);
        System.out.println("Original still: " + price);

        System.out.println("Is positive? " + price.isPositive());
        System.out.println("Equal? " + price.equals(new Money(19.99, "USD")));
    }
}
```

**Output:**
```
Price: USD 19.99
Tax: USD 1.60
Total: USD 21.59
Price unchanged: USD 19.99
Doubled: USD 39.98
Original still: USD 19.99
Is positive? true
Equal? true
```

**Best Practices:**
- Make the class `final` to prevent subclassing
- Make all fields `final`
- Don't provide setters
- Return new objects from mutating methods
- Deep copy mutable objects in getters

---

## Medium Examples

### Example 1: Encapsulated Collection

**Problem Statement:**
Allow controlled access to an internal collection without exposing the raw collection for modification.

**Requirements:**
- Store a list of students internally
- Allow adding, removing, and querying students
- Prevent external code from modifying the list directly
- Maintain invariants (no duplicate IDs, non-empty names)

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.util.*;

public class StudentRoster {
    private final Map<String, Student> studentsById;
    private final String courseName;

    public StudentRoster(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        this.courseName = courseName;
        this.studentsById = new LinkedHashMap<>(); // Maintain insertion order
    }

    public void addStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (studentsById.containsKey(student.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + student.getStudentId() + " already enrolled");
        }
        studentsById.put(student.getStudentId(), student);
        System.out.println(student.getName() + " enrolled in " + courseName);
    }

    public Student removeStudent(String studentId) {
        Student removed = studentsById.remove(studentId);
        if (removed == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found");
        }
        System.out.println(removed.getName() + " dropped from " + courseName);
        return removed;
    }

    public Student findStudent(String studentId) {
        Student student = studentsById.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found");
        }
        return student;
    }

    public List<Student> findByName(String namePart) {
        List<Student> matches = new ArrayList<>();
        for (Student s : studentsById.values()) {
            if (s.getName().toLowerCase().contains(namePart.toLowerCase())) {
                matches.add(s);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    public int getEnrollmentCount() {
        return studentsById.size();
    }

    public boolean isEnrolled(String studentId) {
        return studentsById.containsKey(studentId);
    }

    // Return unmodifiable view — callers can't modify the collection
    public Collection<Student> getAllStudents() {
        return Collections.unmodifiableCollection(studentsById.values());
    }

    public String getCourseName() { return courseName; }

    public void displayRoster() {
        System.out.println("\n=== " + courseName + " Roster ===");
        System.out.println("Enrolled: " + studentsById.size());
        studentsById.values().forEach(System.out::println);
    }

    public static void main(String[] args) {
        StudentRoster roster = new StudentRoster("Java Programming");

        roster.addStudent(new Student("S001", "Alice Johnson", 3.8));
        roster.addStudent(new Student("S002", "Bob Smith", 3.5));
        roster.addStudent(new Student("S003", "Charlie Brown", 3.9));

        roster.displayRoster();

        System.out.println("\nLooking up S002: " + roster.findStudent("S002"));

        List<Student> searchResults = roster.findByName("alice");
        System.out.println("Search for 'alice': " + searchResults);

        System.out.println("Is S001 enrolled? " + roster.isEnrolled("S001"));

        roster.removeStudent("S003");
        roster.displayRoster();

        // This would throw UnsupportedOperationException:
        // roster.getAllStudents().clear();
    }
}

class Student {
    private final String studentId;
    private final String name;
    private final double gpa;

    public Student(String studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }

    @Override
    public String toString() {
        return String.format("[%s] %s (GPA: %.1f)", studentId, name, gpa);
    }
}
```

**Output:**
```
Alice Johnson enrolled in Java Programming
Bob Smith enrolled in Java Programming
Charlie Brown enrolled in Java Programming

=== Java Programming Roster ===
Enrolled: 3
[S001] Alice Johnson (GPA: 3.8)
[S002] Bob Smith (GPA: 3.5)
[S003] Charlie Brown (GPA: 3.9)

Looking up S002: [S002] Bob Smith (GPA: 3.5)
Search for 'alice': [[S001] Alice Johnson (GPA: 3.8)]
Is S001 enrolled? true
Charlie Brown dropped from Java Programming

=== Java Programming Roster ===
Enrolled: 2
[S001] Alice Johnson (GPA: 3.8)
[S002] Bob Smith (GPA: 3.5)
```

**Alternative:**
Use `java.util.Collections.unmodifiableList()` or `List.of()` for read-only views, or consider Guava's `ImmutableList` for additional safety.

---

### Example 2: Configuration Object with Builder

**Problem Statement:**
Create a configuration object that is immutable after construction and validates all settings.

**Requirements:**
- Support database, cache, and HTTP settings
- Validate ranges and required fields
- Provide sensible defaults
- Thread-safe after construction

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.time.Duration;

public final class AppConfig {
    private final String databaseUrl;
    private final int databasePoolSize;
    private final Duration cacheExpiration;
    private final int cacheMaxSize;
    private final String httpBaseUrl;
    private final Duration httpTimeout;
    private final int httpMaxRetries;
    private final boolean debugMode;

    private AppConfig(Builder builder) {
        this.databaseUrl = builder.databaseUrl;
        this.databasePoolSize = builder.databasePoolSize;
        this.cacheExpiration = builder.cacheExpiration;
        this.cacheMaxSize = builder.cacheMaxSize;
        this.httpBaseUrl = builder.httpBaseUrl;
        this.httpTimeout = builder.httpTimeout;
        this.httpMaxRetries = builder.httpMaxRetries;
        this.debugMode = builder.debugMode;
    }

    // All getters, no setters
    public String getDatabaseUrl() { return databaseUrl; }
    public int getDatabasePoolSize() { return databasePoolSize; }
    public Duration getCacheExpiration() { return cacheExpiration; }
    public int getCacheMaxSize() { return cacheMaxSize; }
    public String getHttpBaseUrl() { return httpBaseUrl; }
    public Duration getHttpTimeout() { return httpTimeout; }
    public int getHttpMaxRetries() { return httpMaxRetries; }
    public boolean isDebugMode() { return debugMode; }

    @Override
    public String toString() {
        return String.format(
            "AppConfig{db='%s', pool=%d, cache=%s(%d), http='%s', timeout=%s, retries=%d, debug=%s}",
            databaseUrl, databasePoolSize, cacheExpiration, cacheMaxSize,
            httpBaseUrl, httpTimeout, httpMaxRetries, debugMode
        );
    }

    public static class Builder {
        // Required
        private String databaseUrl;

        // Optional with defaults
        private int databasePoolSize = 10;
        private Duration cacheExpiration = Duration.ofMinutes(30);
        private int cacheMaxSize = 1000;
        private String httpBaseUrl = "https://api.example.com";
        private Duration httpTimeout = Duration.ofSeconds(30);
        private int httpMaxRetries = 3;
        private boolean debugMode = false;

        public Builder(String databaseUrl) {
            if (databaseUrl == null || databaseUrl.isEmpty()) {
                throw new IllegalArgumentException("Database URL is required");
            }
            this.databaseUrl = databaseUrl;
        }

        public Builder databasePoolSize(int size) {
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("Pool size must be between 1 and 100");
            }
            this.databasePoolSize = size;
            return this;
        }

        public Builder cacheExpiration(Duration expiration) {
            if (expiration == null || expiration.isNegative() || expiration.isZero()) {
                throw new IllegalArgumentException("Cache expiration must be positive");
            }
            this.cacheExpiration = expiration;
            return this;
        }

        public Builder cacheMaxSize(int maxSize) {
            if (maxSize < 1) {
                throw new IllegalArgumentException("Cache max size must be at least 1");
            }
            this.cacheMaxSize = maxSize;
            return this;
        }

        public Builder httpBaseUrl(String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("HTTP base URL cannot be empty");
            }
            this.httpBaseUrl = url;
            return this;
        }

        public Builder httpTimeout(Duration timeout) {
            if (timeout == null || timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException("HTTP timeout must be positive");
            }
            this.httpTimeout = timeout;
            return this;
        }

        public Builder httpMaxRetries(int retries) {
            if (retries < 0 || retries > 10) {
                throw new IllegalArgumentException("Max retries must be between 0 and 10");
            }
            this.httpMaxRetries = retries;
            return this;
        }

        public Builder debugMode(boolean debug) {
            this.debugMode = debug;
            return this;
        }

        public AppConfig build() {
            return new AppConfig(this);
        }
    }

    public static void main(String[] args) {
        // Default config
        AppConfig defaultConfig = new AppConfig.Builder("jdbc:mysql://localhost:3306/mydb")
                .build();
        System.out.println("Default: " + defaultConfig);

        // Custom config
        AppConfig customConfig = new AppConfig.Builder("jdbc:mysql://prod:3306/mydb")
                .databasePoolSize(50)
                .cacheExpiration(Duration.ofHours(1))
                .cacheMaxSize(5000)
                .httpBaseUrl("https://api.production.com")
                .httpTimeout(Duration.ofSeconds(10))
                .httpMaxRetries(5)
                .debugMode(true)
                .build();
        System.out.println("Custom: " + customConfig);

        // Validation errors
        try {
            new AppConfig.Builder(null);
        } catch (IllegalArgumentException e) {
            System.out.println("\nValidation: " + e.getMessage());
        }

        try {
            new AppConfig.Builder("jdbc:mysql://localhost/mydb")
                    .databasePoolSize(200); // Out of range
        } catch (IllegalArgumentException e) {
            System.out.println("Validation: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Default: AppConfig{db='jdbc:mysql://localhost:3306/mydb', pool=10, cache=PT30M(1000), http='https://api.example.com', timeout=PT30S, retries=3, debug=false}
Custom: AppConfig{db='jdbc:mysql://prod:3306/mydb', pool=50, cache=PT1H(5000), http='https://api.production.com', timeout=PT10S, retries=5, debug=true}

Validation: Database URL is required
Validation: Pool size must be between 1 and 100
```

**Alternative:**
Use records (Java 16+) for simpler immutable data carriers:
```java
public record AppConfig(String dbUrl, int poolSize, Duration cacheTtl) {}
```

---

### Example 3: Encapsulated State Machine

**Problem Statement:**
Model a vending machine's state transitions with encapsulated state management that prevents invalid transitions.

**Requirements:**
- States: IDLE, SELECTED, INSERTING_MONEY, DISPENSING, COMPLETED
- Each state allows only specific actions
- Invalid state transitions throw exceptions
- Track inserted money and selected product

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.util.*;

public class VendingMachine {
    public enum State {
        IDLE, SELECTED, INSERTING_MONEY, DISPENSING, COMPLETED
    }

    private State currentState;
    private String selectedProduct;
    private double insertedAmount;
    private final Map<String, Double> products;
    private final Map<String, Integer> inventory;

    public VendingMachine() {
        this.currentState = State.IDLE;
        this.insertedAmount = 0;
        this.products = new HashMap<>();
        this.inventory = new HashMap<>();
        initializeProducts();
    }

    private void initializeProducts() {
        products.put("A1", 1.50);
        products.put("A2", 2.00);
        products.put("B1", 1.75);
        products.put("B2", 2.50);

        inventory.put("A1", 5);
        inventory.put("A2", 3);
        inventory.put("B1", 10);
        inventory.put("B2", 2);
    }

    public List<String> getAvailableProducts() {
        List<String> available = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                available.add(entry.getKey() + " - $" + String.format("%.2f", products.get(entry.getKey())));
            }
        }
        return available;
    }

    public void selectProduct(String productId) {
        if (currentState != State.IDLE) {
            throw new IllegalStateException("Cannot select product in state: " + currentState);
        }
        if (!products.containsKey(productId)) {
            throw new IllegalArgumentException("Invalid product: " + productId);
        }
        if (inventory.get(productId) <= 0) {
            throw new IllegalArgumentException("Product " + productId + " is out of stock");
        }

        this.selectedProduct = productId;
        this.currentState = State.SELECTED;
        System.out.println("Selected: " + productId + " ($" + String.format("%.2f", products.get(productId)) + ")");
    }

    public void insertMoney(double amount) {
        if (currentState != State.SELECTED && currentState != State.INSERTING_MONEY) {
            throw new IllegalStateException("Cannot insert money in state: " + currentState);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        this.insertedAmount += amount;
        this.currentState = State.INSERTING_MONEY;
        System.out.printf("Inserted $%.2f. Total: $%.2f%n", amount, insertedAmount);
    }

    public void dispense() {
        if (currentState != State.INSERTING_MONEY) {
            throw new IllegalStateException("Cannot dispense in state: " + currentState);
        }

        double price = products.get(selectedProduct);
        if (insertedAmount < price) {
            throw new IllegalStateException(
                String.format("Insufficient funds. Need $%.2f more", price - insertedAmount)
            );
        }

        this.currentState = State.DISPENSING;
        double change = insertedAmount - price;
        inventory.put(selectedProduct, inventory.get(selectedProduct) - 1);

        System.out.println("Dispensing " + selectedProduct + "...");
        if (change > 0) {
            System.out.printf("Returning change: $%.2f%n", change);
        }

        reset();
        this.currentState = State.COMPLETED;
        System.out.println("Transaction complete!");
    }

    public void cancel() {
        if (insertedAmount > 0) {
            System.out.printf("Returning $%.2f%n", insertedAmount);
        }
        reset();
        System.out.println("Transaction cancelled");
    }

    private void reset() {
        this.selectedProduct = null;
        this.insertedAmount = 0;
        this.currentState = State.IDLE;
    }

    public State getCurrentState() { return currentState; }
    public double getInsertedAmount() { return insertedAmount; }

    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();

        System.out.println("Available products: " + vm.getAvailableProducts());

        System.out.println("\n=== Successful Transaction ===");
        vm.selectProduct("A1");
        vm.insertMoney(1.00);
        vm.insertMoney(1.00);
        vm.dispense();

        System.out.println("\n=== Cancelled Transaction ===");
        vm.selectProduct("B2");
        vm.insertMoney(2.00);
        vm.cancel();

        System.out.println("\n=== Error: Insufficient Funds ===");
        vm.selectProduct("B2");
        vm.insertMoney(1.00);
        try {
            vm.dispense();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        vm.cancel();

        System.out.println("\n=== Error: Invalid State ===");
        try {
            vm.insertMoney(1.00); // No product selected
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Available products: [A1 - $1.50, A2 - $2.00, B1 - $1.75, B2 - $2.50]

=== Successful Transaction ===
Selected: A1 ($1.50)
Inserted $1.00. Total: $1.00
Inserted $1.00. Total: $2.00
Dispensing A1...
Returning change: $0.50
Transaction complete!

=== Cancelled Transaction ===
Selected: B2 ($2.50)
Inserted $2.00. Total: $2.00
Returning $2.00
Transaction cancelled

=== Error: Insufficient Funds ===
Selected: B2 ($2.50)
Inserted $1.00. Total: $1.00
Error: Insufficient funds. Need $1.50 more
Returning $1.00
Transaction cancelled

=== Error: Invalid State ===
Error: Cannot insert money in state: IDLE
```

**Alternative:**
Use the State design pattern with separate state classes for each state, providing better separation of concerns for complex state machines.

---

## Hard Examples

### Example 1: Encapsulated Validation Framework

**Architecture:**
A type-safe validation framework that encapsulates validation rules, providing reusable, composable validators with clear error messages.

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.util.*;
import java.util.function.Predicate;

public class Validator<T> {
    private final List<ValidationRule<T>> rules;
    private final String fieldName;

    private Validator(String fieldName) {
        this.fieldName = fieldName;
        this.rules = new ArrayList<>();
    }

    public static <T> Validator<T> of(String fieldName) {
        return new Validator<>(fieldName);
    }

    public Validator<T> isNotNull() {
        rules.add(new ValidationRule<>(
            Objects::nonNull,
            fieldName + " cannot be null"
        ));
        return this;
    }

    public Validator<T> isNotEmpty() {
        rules.add(new ValidationRule<>(
            value -> {
                if (value == null) return false;
                if (value instanceof String) return !((String) value).isEmpty();
                if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
                return true;
            },
            fieldName + " cannot be empty"
        ));
        return this;
    }

    public Validator<T> satisfies(Predicate<T> predicate, String message) {
        rules.add(new ValidationRule<>(predicate, message));
        return this;
    }

    public ValidationResult validate(T value) {
        List<String> errors = new ArrayList<>();
        for (ValidationRule<T> rule : rules) {
            if (!rule.test(value)) {
                errors.add(rule.getMessage());
            }
        }
        return new ValidationResult(errors.isEmpty(), errors, fieldName, value);
    }

    private static class ValidationRule<T> {
        private final Predicate<T> predicate;
        private final String message;

        ValidationRule(Predicate<T> predicate, String message) {
            this.predicate = predicate;
            this.message = message;
        }

        boolean test(T value) {
            return predicate.test(value);
        }

        String getMessage() {
            return message;
        }
    }

    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final String fieldName;
        private final Object value;

        ValidationResult(boolean valid, List<String> errors, String fieldName, Object value) {
            this.valid = valid;
            this.errors = Collections.unmodifiableList(errors);
            this.fieldName = fieldName;
            this.value = value;
        }

        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public String getFieldName() { return fieldName; }
        public Object getValue() { return value; }

        @Override
        public String toString() {
            if (valid) {
                return fieldName + "=" + value + " [VALID]";
            }
            return fieldName + "=" + value + " [INVALID: " + String.join(", ", errors) + "]";
        }
    }
}

class EntityValidator<T> {
    private final Map<String, Validator<?>> validators;
    private final String entityName;

    public EntityValidator(String entityName) {
        this.entityName = entityName;
        this.validators = new LinkedHashMap<>();
    }

    public <F> EntityValidator<T> addValidator(String fieldName, Validator<F> validator) {
        validators.put(fieldName, validator);
        return this;
    }

    public List<Validator.ValidationResult> validate(Object entity) {
        List<Validator.ValidationResult> results = new ArrayList<>();
        // In a real implementation, use reflection to get field values
        // This is simplified for demonstration
        return results;
    }
}

class ValidationExample {
    public static void main(String[] args) {
        // Validate a username
        Validator<String> usernameValidator = Validator.of("username")
                .isNotNull()
                .isNotEmpty()
                .satisfies(s -> s.length() >= 3, "username must be at least 3 characters")
                .satisfies(s -> s.length() <= 20, "username must be at most 20 characters")
                .satisfies(s -> s.matches("^[a-zA-Z0-9_]+$"),
                    "username must contain only letters, digits, and underscores");

        System.out.println("=== Username Validation ===");
        System.out.println(usernameValidator.validate("alice_johnson"));
        System.out.println(usernameValidator.validate("al"));
        System.out.println(usernameValidator.validate("alice johnson!"));
        System.out.println(usernameValidator.validate(null));

        // Validate an email
        Validator<String> emailValidator = Validator.of("email")
                .isNotNull()
                .satisfies(s -> s.contains("@"), "email must contain @")
                .satisfies(s -> s.contains("."), "email must contain a domain")
                .satisfies(s -> s.indexOf("@") < s.lastIndexOf("."),
                    "email must have a valid domain");

        System.out.println("\n=== Email Validation ===");
        System.out.println(emailValidator.validate("user@example.com"));
        System.out.println(emailValidator.validate("user-at-example.com"));

        // Validate an age
        Validator<Integer> ageValidator = Validator.of("age")
                .isNotNull()
                .satisfies(age -> age >= 0, "age cannot be negative")
                .satisfies(age -> age <= 150, "age must be realistic");

        System.out.println("\n=== Age Validation ===");
        System.out.println(ageValidator.validate(25));
        System.out.println(ageValidator.validate(-5));
        System.out.println(ageValidator.validate(200));
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.encapsulation;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void testValidUsername() {
        Validator<String> validator = Validator.of("username")
                .isNotNull()
                .isNotEmpty()
                .satisfies(s -> s.length() >= 3, "too short");

        Validator.ValidationResult result = validator.validate("alice");
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    public void testInvalidUsername() {
        Validator<String> validator = Validator.of("username")
                .isNotNull()
                .isNotEmpty()
                .satisfies(s -> s.length() >= 3, "too short");

        Validator.ValidationResult result = validator.validate("ab");
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals("username", result.getFieldName());
    }

    @Test
    public void testNullUsername() {
        Validator<String> validator = Validator.of("username")
                .isNotNull();

        Validator.ValidationResult result = validator.validate(null);
        assertFalse(result.isValid());
        assertEquals("username cannot be null", result.getErrors().get(0));
    }

    @Test
    public void testMultipleErrors() {
        Validator<String> validator = Validator.of("password")
                .isNotNull()
                .satisfies(s -> s.length() >= 8, "too short")
                .satisfies(s -> s.matches(".*[A-Z].*"), "no uppercase")
                .satisfies(s -> s.matches(".*[0-9].*"), "no digit");

        Validator.ValidationResult result = validator.validate("abc");
        assertFalse(result.isValid());
        assertEquals(3, result.getErrors().size());
    }
}
```

**Complexity:**
- Validator creation: O(n) where n is number of rules
- Validation: O(n) per field
- Memory: O(n) for storing rules

**Best Practices:**
- Use generics to ensure type safety
- Compose validators from smaller, reusable pieces
- Provide clear, actionable error messages
- Make validators immutable (no shared mutable state)

---

### Example 2: Thread-Safe Encapsulated Cache

**Architecture:**
A thread-safe LRU cache that encapsulates internal data structures and provides a clean public API.

**Implementation:**

```java
package academy.javaengineering.oop.encapsulation;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> {
    private final int maxSize;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final ReentrantReadWriteLock lock;
    private int hits;
    private int misses;

    public LRUCache(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Max size must be positive");
        this.maxSize = maxSize;
        this.map = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
        this.lock = new ReentrantReadWriteLock();
        this.hits = 0;
        this.misses = 0;
    }

    public V get(K key) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.get(key);
            if (node != null) {
                moveToHead(node);
                hits++;
                return node.value;
            }
            misses++;
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.get(key);
            if (node != null) {
                node.value = value;
                moveToHead(node);
            } else {
                Node<K, V> newNode = new Node<>(key, value);
                map.put(key, newNode);
                addToHead(newNode);

                if (map.size() > maxSize) {
                    Node<K, V> removed = removeTail();
                    map.remove(removed.key);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V remove(K key) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.remove(key);
            if (node != null) {
                removeNode(node);
                return node.value;
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
            head.next = tail;
            tail.prev = head;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public CacheStats getStats() {
        lock.readLock().lock();
        try {
            int total = hits + misses;
            double hitRate = total > 0 ? (double) hits / total * 100 : 0;
            return new CacheStats(hits, misses, hitRate, size());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<K> getKeysInOrder() {
        lock.readLock().lock();
        try {
            List<K> keys = new ArrayList<>();
            Node<K, V> current = head.next;
            while (current != tail) {
                keys.add(current.key);
                current = current.next;
            }
            return Collections.unmodifiableList(keys);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    private Node<K, V> removeTail() {
        Node<K, V> node = tail.prev;
        removeNode(node);
        return node;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class CacheStats {
        private final int hits;
        private final int misses;
        private final double hitRate;
        private final int size;

        CacheStats(int hits, int misses, double hitRate, int size) {
            this.hits = hits;
            this.misses = misses;
            this.hitRate = hitRate;
            this.size = size;
        }

        public int getHits() { return hits; }
        public int getMisses() { return misses; }
        public double getHitRate() { return hitRate; }
        public int getSize() { return size; }

        @Override
        public String toString() {
            return String.format("CacheStats{hits=%d, misses=%d, hitRate=%.1f%%, size=%d}",
                hits, misses, hitRate, size);
        }
    }

    public static void main(String[] args) {
        LRUCache<String, String> cache = new LRUCache<>(3);

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        System.out.println("Keys (most to least recent): " + cache.getKeysInOrder());

        cache.get("key1"); // Access key1, moves to front
        System.out.println("After accessing key1: " + cache.getKeysInOrder());

        cache.put("key4", "value4"); // Evicts key2 (least recently used)
        System.out.println("After adding key4: " + cache.getKeysInOrder());

        System.out.println("Get key1: " + cache.get("key1"));
        System.out.println("Get key2: " + cache.get("key2")); // null, evicted

        System.out.println("Stats: " + cache.getStats());
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.encapsulation;

import org.junit.Test;
import static org.junit.Assert.*;

public class LRUCacheTest {

    @Test
    public void testBasicOperations() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        assertEquals(Integer.valueOf(1), cache.get("a"));
        assertEquals(Integer.valueOf(2), cache.get("b"));
        assertEquals(Integer.valueOf(3), cache.get("c"));
        assertEquals(3, cache.size());
    }

    @Test
    public void testEviction() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3); // Should evict "a"

        assertNull(cache.get("a"));
        assertEquals(Integer.valueOf(2), cache.get("b"));
        assertEquals(Integer.valueOf(3), cache.get("c"));
    }

    @Test
    public void testAccessUpdatesOrder() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a"); // Access "a", making "b" least recent
        cache.put("c", 3); // Should evict "b"

        assertEquals(Integer.valueOf(1), cache.get("a"));
        assertNull(cache.get("b"));
        assertEquals(Integer.valueOf(3), cache.get("c"));
    }

    @Test
    public void testLRUCacheStats() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a"); // hit
        cache.get("c"); // miss

        LRUCache.CacheStats stats = cache.getStats();
        assertEquals(1, stats.getHits());
        assertEquals(1, stats.getMisses());
    }
}
```

**Complexity:**
- `get()`: O(1) amortized (hash map + linked list operations)
- `put()`: O(1) amortized
- `remove()`: O(1)
- Space: O(n) where n is cache capacity

**Best Practices:**
- Use `ReentrantReadWriteLock` for better read concurrency
- Encapsulate internal data structures completely
- Provide statistics for monitoring and tuning
- Consider using `ConcurrentHashMap` for lock-free reads in read-heavy scenarios

---

## Exercises

### Easy

1. **Encapsulate a Person Class:**
   Create a `Person` class with private fields (`name`, `age`, `email`) and appropriate getters/setters with validation (age 0-150, valid email format).

2. **Read-Only Configuration:**
   Create a `DatabaseConfig` class that can only be set through the constructor (immutable). Include `host`, `port`, `database`, `username`, `password` fields.

3. **Computed Property:**
   Create a `Circle` class with a private `radius` field and a `getArea()` method that computes the area. Include validation to prevent negative radius.

### Medium

4. **Encapsulated List:**
   Create a `ShoppingCart` class that encapsulates a list of items. Provide methods to add, remove, and list items, but prevent external code from directly modifying the internal list.

5. **Validated Builder:**
   Create a `Product` builder that validates `name` (non-empty), `price` (positive), and `weight` (positive) before building the object.

6. **State Machine:**
   Implement a `TrafficLight` class that encapsulates state transitions (RED → GREEN → YELLOW → RED) and prevents invalid transitions.

### Hard

7. **Type-Safe Configuration:**
   Create a `TypedConfig` class that stores key-value pairs with type safety. Support `getInt()`, `getString()`, `getBoolean()`, etc., with proper type checking and default values.

8. **Thread-Safe Observable:**
   Implement a `PropertyChangeSupport`-like system where objects can register listeners for property changes, with proper encapsulation and thread safety.

9. **Encrypted Storage:**
   Create a `SecureStorage` class that stores sensitive data (passwords, API keys) encrypted at rest, with a clean API for storing and retrieving values.

---

## Interview Questions

### Easy

1. **What is encapsulation in Java?**
   Encapsulation is the bundling of data and methods into a single unit (class) while restricting direct access to some components. It is achieved using access modifiers (`private`, `protected`, `public`).

2. **What is the difference between `private` and `public`?**
   `private` members are only accessible within the same class. `public` members are accessible from any other class. Package-private (default) is accessible within the same package.

3. **Why should fields be `private`?**
   Private fields prevent external code from directly modifying the object's state, allowing the class to control access through validation in setters, maintain invariants, and change internal representation without breaking external code.

### Medium

4. **What is the JavaBean convention?**
   JavaBeans are classes that follow specific naming conventions: a no-arg constructor, private fields with public getters/setters following `getPropertyName()`/`setPropertyName()` naming, and implementing `Serializable`. These conventions enable framework integration through introspection.

5. **How do you create an immutable class?**
   Make the class `final`, make all fields `final`, don't provide setters, deep copy mutable objects in the constructor and getters, and don't allow `this` to escape during construction.

6. **What is the difference between `Collections.unmodifiableList()` and an immutable list?**
   `Collections.unmodifiableList()` creates a view that throws `UnsupportedOperationException` on modification attempts, but the underlying list can still change. `List.of()` (Java 9+) creates a truly immutable list with no underlying mutable state.

### Hard

7. **How does encapsulation interact with reflection?**
   Reflection can bypass access modifiers using `setAccessible(true)`, which means encapsulation is not a security boundary but a design tool. It protects against accidental misuse, not deliberate circumvention.

8. **What is the principle of least privilege in the context of encapsulation?**
   Give each class the minimum access it needs to function. Use private fields, package-private methods when possible, and avoid making everything public "just in case." This reduces the attack surface and makes code easier to refactor.

---

## Common Pitfalls

### Pitfall 1: Exposing Mutable Internal State

**Wrong:**
```java
public class Team {
    private List<String> members = new ArrayList<>();

    public List<String> getMembers() {
        return members; // Exposes internal list for modification
    }
}

// Calling code:
team.getMembers().clear(); // Corrupts the object's state
```

**Right:**
```java
public class Team {
    private final List<String> members = new ArrayList<>();

    public List<String> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public void addMember(String member) {
        members.add(member);
    }
}
```

### Pitfall 2: No Validation in Setters

**Wrong:**
```java
public class BankAccount {
    private double balance;

    public void setBalance(double balance) {
        this.balance = balance; // No validation!
    }
}

// Calling code:
account.setBalance(-1000); // Invalid state
```

**Right:**
```java
public class BankAccount {
    private double balance;

    public void setBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        this.balance = balance;
    }

    // Better: provide business methods instead of direct balance control
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }
}
```

### Pitfall 3: Over-Encapsulation (Anemic Domain Model)

**Wrong:**
```java
public class Order {
    private List<OrderItem> items;
    private double discount;

    // Only getters and setters, no business logic
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}

// Business logic scattered in service classes
orderService.calculateTotal(order);
orderService.applyDiscount(order, 0.1);
```

**Right:**
```java
public class Order {
    private final List<OrderItem> items;
    private double discount;

    // Encapsulated business logic
    public double calculateTotal() {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum() * (1 - discount);
    }

    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }
        this.discount = percentage;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }
}
```

---

## Best Practices

1. **Make Fields Private by Default:**
   Always start with `private` and only increase visibility when there's a specific reason. It's easy to make something more visible later, but hard to restrict it.

2. **Validate Inputs in Setters and Constructors:**
   Never trust external input. Validate ranges, nullability, and format in setters to prevent invalid states. Use `Objects.requireNonNull()` for null checks.

3. **Prefer Immutable Objects When Possible:**
   Immutable objects are thread-safe, have no defensive copying issues, and can be shared freely. Use `final` fields and no setters.

4. **Return Defensive Copies of Mutable Objects:**
   If your class contains mutable objects (Date, List, Map), return copies in getters to prevent external modification of internal state.

5. **Follow the Principle of Least Privilege:**
   Use the most restrictive access modifier that works. Package-private is often better than `protected`. Don't expose implementation details through public APIs.

---

## Real World Usage

### Spring Framework
- `@ConfigurationProperties` classes use encapsulation with getters/setters for type-safe configuration
- `@Value` injection relies on encapsulated fields with proper getters
- Bean validation (`@Valid`, `@NotNull`) works through encapsulated setters

### Hibernate / JPA
- Entity classes encapsulate database state with JPA annotations
- Lazy loading depends on encapsulated proxy objects
- `@Embedded` and `@ElementCollection` encapsulate complex mappings

### JDK Source Code
- `String` is immutable — all methods return new strings
- `Collections.unmodifiableList()` returns encapsulated views
- `java.util.concurrent` classes encapsulate thread-safety mechanics

### Enterprise Applications
- DTOs (Data Transfer Objects) encapsulate API contracts
- Value Objects encapsulate domain concepts (Money, Address, DateRange)
- Configuration objects encapsulate application settings

---

## References

- [Java Language Specification — Access Modifiers](https://docs.oracle.com/javase/specs/jls/se17/html/jls-6.html#jls-6.6)
- [Effective Java, 3rd Edition — Item 15: Minimize the accessibility of classes and members](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Tutorials — Controlling Access to Members](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
- [Baeldung — Encapsulation in Java](https://www.baeldung.com/java-encapsulation)
- [JavaBeans Specification](https://www.oracle.com/java/technologies/javase/javabeans-spec.html)

---

## Summary

Encapsulation is the foundation of robust object-oriented design in Java. Key takeaways:

- **Data Hiding:** Use `private` fields and expose behavior through public methods
- **Validation:** Always validate inputs in setters and constructors
- **Immutable Objects:** Prefer immutability for thread safety and predictability
- **JavaBean Conventions:** Follow standard naming for framework compatibility
- **Defensive Copies:** Return copies of mutable objects to protect internal state

Encapsulation is not just about hiding data — it's about designing stable, maintainable APIs that evolve safely over time.

---

**Navigation:**
- Previous: [07-static-keyword](../07-static-keyword/README.md)
- Next: [09-inheritance](../09-inheritance/README.md)
- [Back to OOP Module](../README.md)
