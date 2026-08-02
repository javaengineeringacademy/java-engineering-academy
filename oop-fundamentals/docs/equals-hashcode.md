# equals() and hashCode()

## Introduction

The `equals()` and `hashCode()` methods are fundamental to Java's object comparison and collection framework. Understanding their contract is essential for correct behavior in HashMap, HashSet, and other data structures.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand the equals/hashCode contract
- Implement equals() correctly
- Implement hashCode() correctly
- Use java.util.Objects utility class
- Understand implications for collections
- Avoid common pitfalls

## Prerequisites

- Classes and Objects
- Object class
- Collections (basic understanding)

## Why This Concept Exists

### The Problem

Without proper equals/hashCode:

- Objects compared by reference, not content
- Collections cannot find objects correctly
- HashMap/HashSet behave unpredictably
- Duplicate objects not detected

### The Solution

equals/hashCode contract provides:

- **Logical equality**: Compare by content, not reference
- **Collection consistency**: Correct behavior in HashMap/HashSet
- **Performance**: Efficient object lookup
- **Predictability**: Consistent behavior across the application

### Real-World Analogy

Think of equals/hashCode as **identification**:

- equals() is like checking if two IDs belong to the same person
- hashCode() is like a zip code - helps narrow down where to look
- Two people with the same zip code might be different people (hash collision)
- Two people with the same ID must have the same zip code (contract)

## The Contract

### equals() Contract

1. **Reflexive**: `x.equals(x)` must return `true`
2. **Symmetric**: `x.equals(y)` must equal `y.equals(x)`
3. **Transitive**: If `x.equals(y)` and `y.equals(z)`, then `x.equals(z)`
4. **Consistent**: Multiple calls return same result if objects unchanged
5. **Null**: `x.equals(null)` must return `false`

### hashCode() Contract

1. **Consistent**: Multiple calls return same integer if objects unchanged
2. **Equal objects must have equal hash codes**: If `a.equals(b)`, then `a.hashCode() == b.hashCode()`
3. **Unequal objects may have equal hash codes**: Hash collisions are allowed

## Syntax

### Basic equals() Implementation

```java
@Override
public boolean equals(Object obj) {
    // 1. Same reference?
    if (this == obj) return true;
    
    // 2. Null or different class?
    if (obj == null || getClass() != obj.getClass()) return false;
    
    // 3. Cast and compare fields
    MyClass other = (MyClass) obj;
    return primitiveField == other.primitiveField &&
           Objects.equals(referenceField, other.referenceField);
}
```

### Basic hashCode() Implementation

```java
@Override
public int hashCode() {
    return Objects.hash(field1, field2, field3);
}
```

## Easy Examples

### Example 1: Point Class

**Problem Statement**: Implement equals/hashCode for a Point class.

**Implementation**:

```java
import java.util.Objects;

public class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "Point{x=" + x + ", y=" + y + "}";
    }
}
```

**Output**:
```java
Point p1 = new Point(10, 20);
Point p2 = new Point(10, 20);
Point p3 = new Point(30, 40);

// Without equals: compares references
System.out.println(p1 == p2); // false (different objects)

// With equals: compares content
System.out.println(p1.equals(p2)); // true (same content)
System.out.println(p1.equals(p3)); // false

// hashCode contract
System.out.println(p1.hashCode() == p2.hashCode()); // true

// In collections
Set<Point> points = new HashSet<>();
points.add(p1);
points.add(p2); // Won't be added (duplicate)
System.out.println(points.size()); // 1
```

**Complexity**: O(1) for equals and hashCode

**Best Practices**:
- Use `Objects.hash()` for hashCode
- Use `Objects.equals()` for reference field comparison
- Make fields final when possible

### Example 2: Record Class (Automatic)

**Problem Statement**: See how records automatically implement equals/hashCode.

**Implementation**:

```java
// Record automatically generates equals, hashCode, toString
public record Person(String name, int age, String email) {
    // Compact constructor for validation
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
    }
}
```

**Output**:
```java
Person p1 = new Person("Alice", 30, "alice@example.com");
Person p2 = new Person("Alice", 30, "alice@example.com");
Person p3 = new Person("Bob", 25, "bob@example.com");

// Records generate correct equals/hashCode automatically
System.out.println(p1.equals(p2)); // true
System.out.println(p1.equals(p3)); // false
System.out.println(p1.hashCode() == p2.hashCode()); // true

// Works correctly in collections
Set<Person> people = new HashSet<>();
people.add(p1);
people.add(p2); // Won't be added
System.out.println(people.size()); // 1
```

## Medium Examples

### Example 3: Employee with Multiple Fields

**Problem Statement**: Implement equals/hashCode for a class with multiple field types.

**Implementation**:

```java
import java.util.Objects;

public class Employee {
    private final int id;
    private final String name;
    private final String department;
    private final LocalDate hireDate;
    
    public Employee(int id, String name, String department, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hireDate = hireDate;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Employee other = (Employee) obj;
        return id == other.id &&
               Objects.equals(name, other.name) &&
               Objects.equals(department, other.department) &&
               Objects.equals(hireDate, other.hireDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, department, hireDate);
    }
    
    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', dept='%s', hire=%s}",
            id, name, department, hireDate);
    }
    
    // Getters...
}
```

**Output**:
```java
Employee emp1 = new Employee(1001, "Alice", "Engineering", LocalDate.of(2020, 1, 15));
Employee emp2 = new Employee(1001, "Alice", "Engineering", LocalDate.of(2020, 1, 15));
Employee emp3 = new Employee(1002, "Bob", "Marketing", LocalDate.of(2021, 3, 20));

System.out.println(emp1.equals(emp2)); // true
System.out.println(emp1.equals(emp3)); // false
System.out.println(emp1.hashCode() == emp2.hashCode()); // true

// In HashMap
Map<Employee, String> ratings = new HashMap<>();
ratings.put(emp1, "Excellent");
ratings.put(emp2, "Excellent"); // Overwrites (same key)
System.out.println(ratings.size()); // 1
```

### Example 4: Using java.util.Objects Utility

**Problem Statement**: Simplify equals/hashCode using java.util.Objects.

**Implementation**:

```java
import java.util.Objects;

public class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final String name;
    
    public Color(int red, int green, int blue, String name) {
        this.red = clamp(red);
        this.green = clamp(green);
        this.blue = clamp(blue);
        this.name = name;
    }
    
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
    
    @Override
    public boolean equals(Object obj) {
        // Objects.equals handles null and reference check
        if (this == obj) return true;
        if (!(obj instanceof Color other)) return false;
        
        // Objects.equals handles null fields
        return red == other.red &&
               green == other.green &&
               blue == other.blue &&
               Objects.equals(name, other.name);
    }
    
    @Override
    public int hashCode() {
        // Objects.hash handles all field types
        return Objects.hash(red, green, blue, name);
    }
    
    @Override
    public String toString() {
        return String.format("Color{r=%d, g=%d, b=%d, name='%s'}",
            red, green, blue, name);
    }
}
```

**Output**:
```java
Color red1 = new Color(255, 0, 0, "Red");
Color red2 = new Color(255, 0, 0, "Red");
Color blue = new Color(0, 0, 255, "Blue");

System.out.println(red1.equals(red2)); // true
System.out.println(red1.equals(blue)); // false
```

## Hard Examples

### Example 5: Broken Implementation (Warning!)

**Problem**: Show what happens when you break the contract.

**Implementation**:

```java
// BROKEN: equals uses id and name, hashCode uses only name
public class BadEmployee {
    private final int id;
    private final String name;
    
    public BadEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BadEmployee other = (BadEmployee) obj;
        return id == other.id && Objects.equals(name, other.name);
    }
    
    // BROKEN: Only uses name, missing id
    @Override
    public int hashCode() {
        return Objects.hash(name); // Should include id!
    }
    
    @Override
    public String toString() {
        return "BadEmployee{id=" + id + ", name='" + name + "'}";
    }
}
```

**Output**:
```java
BadEmployee bad1 = new BadEmployee(1001, "Alice");
BadEmployee bad2 = new BadEmployee(1001, "Alice");

// equals says they're equal
System.out.println(bad1.equals(bad2)); // true

// But hashCode is different! (Contract broken)
System.out.println(bad1.hashCode() == bad2.hashCode()); // FALSE!

// This causes issues in collections
Set<BadEmployee> set = new HashSet<>();
set.add(bad1);
set.add(b2); // Added! (different hashCode)
System.out.println(set.size()); // 2 (should be 1!)

// Cannot find the object
BadEmployee lookup = new BadEmployee(1001, "Alice");
System.out.println(set.contains(lookup)); // FALSE! (different hashCode)
```

**Lesson**: Always override both equals AND hashCode, using the same fields!

### Example 6: Custom equals with Pattern Matching

**Problem**: Modern Java equals implementation.

**Implementation**:

```java
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    @Override
    public boolean equals(Object obj) {
        // Pattern matching (Java 16+)
        if (this == obj) return true;
        if (!(obj instanceof Money other)) return false;
        
        // Compare by value, not reference
        return amount.compareTo(other.amount) == 0 &&
               currency.equals(other.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    // For BigDecimal, use compareTo, not equals
    public boolean isGreaterThan(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
        return amount.compareTo(other.amount) > 0;
    }
}
```

**Output**:
```java
Money m1 = new Money(new BigDecimal("100.00"), Currency.getInstance("USD"));
Money m2 = new Money(new BigDecimal("100.00"), Currency.getInstance("USD"));
Money m3 = new Money(new BigDecimal("200.00"), Currency.getInstance("USD"));

System.out.println(m1.equals(m2)); // true
System.out.println(m1.equals(m3)); // false
System.out.println(m1.isGreaterThan(m3)); // false
```

## Exercises

### Easy

1. **Rectangle Class**: Implement equals/hashCode for a Rectangle class with width and height.

2. **Book Class**: Implement equals/hashCode for a Book class with ISBN, title, and author.

### Medium

3. **Address Class**: Implement equals/hashCode for an Address with street, city, state, zip.

4. **Student Class**: Implement equals/hashCode for a Student with ID, name, and GPA.

### Hard

5. **Custom Collection**: Implement a simple Set that uses your equals/hashCode.

6. **Immutable Class**: Create an immutable class with proper equals/hashCode and defensive copying.

## Interview Questions

### Beginner

1. **What is the difference between == and equals()?**
   `==` compares references (same object), `.equals()` compares content (logical equality).

2. **Why must you override hashCode when overriding equals?**
   Collections like HashMap and HashSet use hashCode for bucket placement. If equal objects have different hash codes, collections won't work correctly.

3. **Can you override equals for one field but hashCode for all fields?**
   No, you must use the same fields for both. Breaking this contract causes bugs in collections.

### Intermediate

4. **What is the contract between equals and hashCode?**
   - If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` must be true
   - If `a.hashCode() == b.hashCode()`, `a.equals(b)` may be false (hash collision)
   - Equal objects must have equal hash codes, but not vice versa

5. **How do you handle BigDecimal in equals?**
   Use `compareTo()` instead of `equals()` because BigDecimal has precision: `new BigDecimal("1.0").equals(new BigDecimal("1.00"))` returns false.

6. **What is the role of getClass() in equals?**
   It ensures you're comparing the exact same class, not just a superclass. This maintains symmetry.

### Senior

7. **How do you implement equals for a class with inheritance?**
   Use `instanceof` instead of `getClass()` to allow subclass equality, but document this decision. Or make the class `final`.

8. **What are the implications of mutable fields in hashCode?**
   If a field used in hashCode changes after the object is added to a collection, the collection won't find it. Use only immutable fields.

9. **How do records solve the equals/hashCode problem?**
   Records auto-generate equals/hashCode using all components. They're final and immutable, preventing contract violations.

### Architecture

10. **When would you NOT override equals?**
    - When each object is inherently unique (Thread, Connection)
    - When default Object.equals (reference equality) is correct
    - When the class is `final` and you control all instances

11. **How do you handle equals across different class hierarchies?**
    Usually you don't. Different hierarchies should have independent equality logic. If needed, use a common interface.

12. **How do you version equals/hashCode in a serializable class?**
    Be careful adding fields - it can break existing serialized objects. Consider using serialVersionUID and defensive implementation.

### Scenario

13. **You have a class with a Set field. How do you implement equals?**

14. **You need to compare objects across different class versions. How do you handle this?**

15. **You're debugging a HashMap that's not finding entries. What's the likely cause?**

### Coding

16. **Implement equals/hashCode for a class with an array field.**

17. **Create an immutable class with proper equals/hashCode and defensive copying.**

18. **Design a equals method that handles inheritance properly.

### Production

19. **How would you test your equals/hashCode implementation?**

20. **What happens if you add a field to equals/hashCode in a published library?**

### Debugging

21. **Why is my HashSet not detecting duplicates?**

22. **How do I debug a broken hashCode implementation?**

## Common Pitfalls

### 1. Breaking the Contract

**Wrong**:
```java
public class Bad {
    private int id;
    private String name;
    
    @Override
    public boolean equals(Object o) {
        return id == o.id; // Uses id only
    }
    
    @Override
    public int hashCode() {
        return name.hashCode(); // Uses name only!
    }
}
```

**Right**:
```java
public class Good {
    private int id;
    private String name;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good other = (Good) o;
        return id == other.id && Objects.equals(name, other.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name); // Same fields as equals
    }
}
```

### 2. Using == for Reference Fields

**Wrong**:
```java
@Override
public boolean equals(Object o) {
    return name == o.name; // Compares references, not content
}
```

**Right**:
```java
@Override
public boolean equals(Object o) {
    return Objects.equals(name, o.name); // Compares content
}
```

### 3. Not Handling Null

**Wrong**:
```java
@Override
public boolean equals(Object o) {
    return name.equals(o.name); // NPE if name is null
}
```

**Right**:
```java
@Override
public boolean equals(Object o) {
    return Objects.equals(name, o.name); // Handles null
}
```

## Best Practices

### 1. Use Objects.hash() and Objects.equals()

These utility methods handle nulls and simplify implementation.

### 2. Use instanceof for Type Check

Pattern matching (`if (obj instanceof MyClass other)`) is clean and modern.

### 3. Make Classes Immutable

Immutable classes simplify equals/hashCode because fields can't change.

### 4. Consider Records

For data classes, use records - they auto-generate correct equals/hashCode.

### 5. Test Your Implementation

Use Apache Commons Lang EqualsBuilder/HashCodeBuilder for testing.

## Real World Usage

### JDK Examples

```java
// String implements proper equals/hashCode
String s1 = new String("hello");
String s2 = new String("hello");
s1.equals(s2); // true
s1.hashCode() == s2.hashCode(); // true

// Integer caches values
Integer i1 = 127;
Integer i2 = 127;
i1 == i2; // true (cached)
```

### Collections Framework

```java
// HashMap uses hashCode to find bucket, then equals to find entry
Map<Person, String> map = new HashMap<>();
Person key = new Person("Alice", 30);
map.put(key, "Engineer");

// Later, lookup works because equals/hashCode are correct
Person lookup = new Person("Alice", 30);
System.out.println(map.get(lookup)); // "Engineer"
```

### JPA/Hibernate

```java
// JPA entities must implement equals/hashCode for identity
@Entity
public class User {
    @Id
    private Long id;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Object Model                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐       ┌──────────────┐              │
│  │   equals()   │──────▶│  compareTo() │              │
│  │  (content)   │       │  (ordering)  │              │
│  └──────┬───────┘       └──────────────┘              │
│         │                                               │
│         ▼                                               │
│  ┌──────────────┐                                      │
│  │  hashCode()  │                                      │
│  │  (bucketing) │                                      │
│  └──────┬───────┘                                      │
│         │                                               │
│         ▼                                               │
│  ┌──────────────────────────────────────────┐         │
│  │         Collection Framework             │         │
│  ├──────────────────────────────────────────┤         │
│  │                                          │         │
│  │  ┌──────────┐  ┌──────────┐  ┌───────┐ │         │
│  │  │ HashMap  │  │ HashSet  │  │ Cache │ │         │
│  │  │  bucket  │  │  bucket  │  │ lookup│ │         │
│  │  └────┬─────┘  └────┬─────┘  └───┬───┘ │         │
│  │       │              │            │      │         │
│  │       └──────┬───────┘────────────┘      │         │
│  │              │                           │         │
│  │              ▼                           │         │
│  │     ┌────────────────┐                  │         │
│  │     │  equals() for  │                  │         │
│  │     │  exact match   │                  │         │
│  │     └────────────────┘                  │         │
│  │                                          │         │
│  └──────────────────────────────────────────┘         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## Flow Diagram

```
                    ┌─────────────────┐
                    │  Object Lookup   │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Compute Hash   │
                    │  (hashCode())   │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Find Bucket    │
                    │  (hash & mask)  │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Bucket Empty?  │
                    └────────┬────────┘
                        Yes/ │ \No
                          /  │  \
            ┌───────────┐    │    ┌──────────────────┐
            │ Not Found │    │    │  Iterate Entries  │
            └───────────┘    │    └────────┬─────────┘
                             │             │
                    ┌────────▼────────┐    │
                    │    Entry?       │◀───┘
                    │  equals() match │
                    └────────┬────────┘
                        Yes/ │ \No
                          /  │  \
            ┌───────────┐    │    ┌──────────────────┐
            │  Found!   │    │    │  Next Entry/Node │
            └───────────┘    │    │  (linked list/   │
                             │    │   tree)           │
                    ┌────────▼────────┐    │
                    │ Return Value    │    │
                    └─────────────────┘    │
                                          │
                                    ┌─────▼──────┐
                                    │ More nodes? │
                                    └──────┬─────┘
                                       Yes/ \No
                                         /    \
                              ┌──────────┐  ┌──────────┐
                              │ Continue │  │ Not Found│
                              └──────────┘  └──────────┘
```

## Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| `hashCode()` | O(1) | O(n) | Depends on field count/type |
| `equals()` | O(1) | O(n) | Depends on field count/type |
| `HashMap.put()` | O(1) | O(n) | Uses both methods |
| `HashMap.get()` | O(1) | O(n) | Uses both methods |
| `HashSet.add()` | O(1) | O(n) | Uses both methods |
| `HashSet.contains()` | O(1) | O(n) | Uses both methods |

### hashCode() Breakdown

```java
// Objects.hash() complexity
public int hashCode() {
    return Objects.hash(id, name, department, hireDate);
    // int:    O(1)
    // String: O(n) where n = length
    // Date:   O(1)
    // Total:  O(n) for string fields
}
```

### equals() Breakdown

```java
public boolean equals(Object obj) {
    if (this == obj) return true;                    // O(1)
    if (!(obj instanceof Employee other)) return false; // O(1)
    return id == other.id &&                         // O(1)
           Objects.equals(name, other.name) &&       // O(n) string compare
           Objects.equals(department, other.department) && // O(n) string compare
           Objects.equals(hireDate, other.hireDate); // O(1)
    // Total: O(n) dominated by String comparisons
}
```

## Space Complexity

| Component | Space | Description |
|-----------|-------|-------------|
| hashCode() result | O(1) | Single int value |
| equals() | O(1) | No additional allocations |
| HashMap bucket | O(1) | Per entry overhead |
| HashMap entry | O(1) | Key + value + hash + next pointer |

### Object Footprint

```java
// Each HashMap.Entry contains:
// - int hash (4 bytes)
// - K key (8 bytes reference)
// - V value (8 bytes reference)
// - Entry<K,V> next (8 bytes reference)
// Total: ~28 bytes overhead per entry (64-bit JVM)

// Your class overhead:
// - Object header: 12-16 bytes
// - Fields: varies
// - Padding: up to 8-byte alignment
```

### Memory-Efficient Implementation

```java
// Use compact hashCode for large collections
public class EfficientEntity {
    private final long id;
    
    // Compact: O(1) space, O(1) time
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EfficientEntity other && id == other.id;
    }
}
```

## Thread Safety

### Immutable Classes (Safe)

```java
// Immutable = automatically thread-safe for equals/hashCode
public final class ImmutablePoint {
    private final int x;
    private final int y;
    
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ImmutablePoint other)) return false;
        return x == other.x && y == other.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
```

### Mutable Classes (Unsafe)

```java
// DANGER: Mutable field used in hashCode breaks under concurrency
public class MutableEntity {
    private int id;
    private String name; // Can change!
    
    // Thread A: adds to HashMap
    // Thread B: changes name
    // HashMap can't find the entry anymore!
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name); // name is mutable!
    }
}
```

### Safe Mutable Approach

```java
public class SafeMutableEntity {
    private int id;
    private String name;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    // Use only immutable fields in equals/hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SafeMutableEntity other)) return false;
        return id == other.id; // Only immutable field
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id); // Only immutable field
    }
    
    public void setName(String name) {
        lock.writeLock().lock();
        try {
            this.name = name;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

## Pitfalls

### Pitfall 1: Using Mutable Fields in hashCode

```java
// BROKEN: Date is mutable
public class Event {
    private final String name;
    private Date scheduledDate; // Mutable!
    
    @Override
    public int hashCode() {
        return Objects.hash(name, scheduledDate);
    }
}

// Bug: Date changes, hashCode changes, collection loses the entry
Event e = new Event("Meeting", new Date());
Set<Event> events = new HashSet<>();
events.add(e);
e.scheduledDate.setTime(e.scheduledDate.getTime() + 86400000); // +1 day
events.remove(e); // Fails! hashCode changed
System.out.println(events.size()); // Still 1
```

### Pitfall 2: Including Derived Fields

```java
// BAD: fullName is derived from firstName + lastName
public class User {
    private String firstName;
    private String lastName;
    
    // BAD: fullName can be computed
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, getFullName());
        // getFullName() is redundant and wasteful
    }
}
```

### Pitfall 3: Forgetting Null Check

```java
// NPE waiting to happen
public class Container {
    private String value;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Container other = (Container) obj;
        return value.equals(other.value); // NPE if value is null
    }
}
```

### Pitfall 4: Using getClass() Instead of instanceof

```java
// Breaks subclass equality
public class Animal {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        // Dog extends Animal - getClass() won't match!
        Animal other = (Animal) obj;
        return name.equals(other.name);
    }
}
```

### Pitfall 5: Overriding hashCode Only

```java
// If you override hashCode, you MUST override equals
public class Broken {
    @Override
    public int hashCode() {
        return 42;
    }
    // equals() not overridden - uses reference equality
    // HashMap behavior becomes unpredictable
}
```

## Debugging Tips

### 1. Add toString() for Debugging

```java
@Override
public String toString() {
    return String.format("Point{x=%d, y=%d, hashCode=%d}", x, y, hashCode());
}
```

### 2. Verify Contract Programmatically

```java
public class EqualsHashCodeTest {
    public static <T> void verifyContract(T a, T b, T c) {
        // Reflexive
        assert a.equals(a) : "Reflexive violated";
        assert a.hashCode() == a.hashCode() : "HashCode consistency violated";
        
        // Symmetric
        assert a.equals(b) == b.equals(a) : "Symmetric violated";
        
        // Transitive
        if (a.equals(b) && b.equals(c)) {
            assert a.equals(c) : "Transitive violated";
        }
        
        // Consistent
        assert a.equals(b) == a.equals(b) : "Consistency violated";
        
        // Null
        assert !a.equals(null) : "Null check violated";
    }
}
```

### 3. Use IntelliJ IDEA Debugger

```java
// Set breakpoint and inspect:
// 1. hashCode() return value
// 2. equals() comparison at each step
// 3. Collection internal state
```

### 4. Enable HashMap Debugging

```java
// Add logging to track bucket placement
Map<Point, String> map = new HashMap<>();
Point p = new Point(1, 2);

// Debug: check bucket index
int hash = p.hashCode();
int bucket = hash & (16 - 1); // 16 = default capacity
System.out.printf("Hash: %d, Bucket: %d%n", hash, bucket);
```

### 5. Write Contract Tests

```java
@Test
void testEqualsHashCodeContract() {
    Point a = new Point(1, 2);
    Point b = new Point(1, 2);
    Point c = new Point(3, 4);
    
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
    assertNotEquals(a, null);
    assertEquals(a, a); // reflexive
}
```

## Comparison Table

| Aspect | == | equals() | Objects.equals() |
|--------|-----|----------|------------------|
| Compares | Reference | Content | Content |
| Null-safe | N/A | No | Yes |
| Override needed | No | Yes | No |
| Use case | Identity | Equality | Utility |
| Performance | O(1) | O(n) | O(n) |
| Example | `a == b` | `a.equals(b)` | `Objects.equals(a, b)` |

### Field Comparison Methods

| Method | Type | Null-safe | Notes |
|--------|------|-----------|-------|
| `==` | Primitives | N/A | Direct comparison |
| `.equals()` | Objects | No | Must override |
| `Objects.equals()` | Objects | Yes | Null-safe wrapper |
| `Arrays.equals()` | Arrays | Yes | Element-wise |
| `Objects.deepEquals()` | Nested | Yes | Recursive |
| `Comparator.compare()` | Objects | No | Ordering |

### hashCode() Strategies

| Strategy | Implementation | Collision Rate | Use Case |
|----------|---------------|----------------|----------|
| `Objects.hash()` | Composite | Low | General purpose |
| `Long.hashCode()` | Direct | None | Single long |
| `String.hashCode()` | Polynomial | Low | String fields |
| Custom | XOR/Prime | Variable | Special cases |

## Decision Tree

```
Should you override equals()?
│
├─ Is the class a Record?
│  └─ NO → Do you need value equality?
│           ├─ YES → Override equals()
│           └─ NO  → Use reference equality
│
├─ Is the class final?
│  ├─ YES → Use getClass() check
│  └─ NO  → Use instanceof check
│           │
│           └─ Document subclass behavior
│
├─ Does the class have mutable fields?
│  ├─ YES → Can you make them immutable?
│  │        ├─ YES → Make immutable, override
│  │        └─ NO  → Use only immutable fields in equals
│  └─ NO  → Safe to override
│
└─ Will it be used in collections?
   ├─ YES → Must override equals AND hashCode
   └─ NO  → Consider if override is needed
```

```
Which fields to include in equals/hashCode?
│
├─ Identity fields (ID, UUID)?
│  └─ YES → Include in equals AND hashCode
│
├─ Value fields (name, price)?
│  └─ YES → Include in equals AND hashCode
│
├─ Derived fields (fullName, area)?
│  └─ YES → Exclude (redundant)
│
├─ Mutable fields?
│  └─ YES → Use only immutable fields
│
└─ Null-possible fields?
   └─ YES → Use Objects.equals() and Objects.hash()
```

## Assignments

### Beginner

1. **Coordinate System**: Create a `Coordinate` class with latitude/longitude. Override equals/hashCode. Test that two coordinates at the same location are equal.

2. **Password Manager**: Create a `Credential` class with username and encrypted password. Implement equals based on username only. Why not include password?

3. **Card Game**: Create a `PlayingCard` class with suit and rank. Implement equals/hashCode. Create a `Hand` class that uses a Set to store unique cards.

### Intermediate

4. **ISBN Validator**: Create an `ISBN` class that validates ISBN-10 and ISBN-13. Implement equals based on the numeric value, ignoring dashes.

5. **Color Mixer**: Create a `Color` class with RGB values. Implement equals with tolerance (two colors within 1 unit difference are "equal"). Adjust hashCode accordingly.

6. **Cache Implementation**: Build a simple LRU cache using LinkedHashMap. Implement equals/hashCode for cache keys. Handle expiration.

### Advanced

7. **ORM Entity**: Create a JPA entity with `@Id` field. Implement equals/hashCode using only the ID. Write tests proving it works with Hibernate's persistence context.

8. **Custom Collection**: Implement a `UniqueList` that maintains insertion order but rejects duplicates using equals/hashCode.

9. **Graph Node**: Create a `GraphNode` class with equals/hashCode that excludes circular references. Test with deeply nested structures.

## Mini Project

### Project: Contact Manager

Build a contact management system demonstrating equals/hashCode best practices.

**Requirements**:

```java
// 1. Contact class with proper equals/hashCode
public class Contact {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private final Set<String> groups;
    
    // equals: UUID only
    // hashCode: UUID only
    // Reason: Contact identity is UUID, not name
}

// 2. ContactBook using HashMap for fast lookup
public class ContactBook {
    private final Map<UUID, Contact> contacts;
    private final Map<String, Set<UUID>> groupIndex;
    
    public void addContact(Contact contact);
    public Contact findByEmail(String email);  // O(n) scan
    public Contact findById(UUID id);          // O(1) lookup
    public Set<Contact> findByGroup(String group);
}

// 3. Group class with value equality
public class Group {
    private String name;
    private String description;
    
    // equals: name only (group names are unique)
    // hashCode: name only
}

// 4. Tests proving correctness
@Test
void testContactEquality() {
    Contact c1 = new Contact("Alice", "alice@test.com");
    Contact c2 = new Contact("Alice", "alice@test.com");
    assertEquals(c1, c2); // Different UUIDs, different objects
    // Wait - this should NOT be equal!
    // Two contacts with same email but different IDs are different
}

@Test
void testGroupEquality() {
    Group g1 = new Group("Friends");
    Group g2 = new Group("Friends");
    assertEquals(g1, g2); // Same name = same group
}

@Test
void testCollectionBehavior() {
    ContactBook book = new ContactBook();
    Contact alice = new Contact("Alice", "alice@test.com");
    book.addContact(alice);
    
    // Can find by ID
    assertNotNull(book.findById(alice.getId()));
    
    // Cannot find by email (not indexed)
    // This is by design - email lookup is O(n)
}
```

**Extension**: Add a `ChangeTracker` that logs when equals/hashCode fields change, demonstrating why immutability matters.

## Performance Optimization

### Profile-Guided Field Selection

```java
// BAD: All fields in hashCode, but only id is used for equality
public class SlowEntity {
    private final long id;
    private String largeDescription; // 1KB string
    private byte[] blob;
    
    @Override
    public int hashCode() {
        return Objects.hash(id, largeDescription, blob); // Expensive!
    }
}

// GOOD: Only essential fields
public class FastEntity {
    private final long id;
    private String largeDescription;
    private byte[] blob;
    
    @Override
    public int hashCode() {
        return Long.hashCode(id); // Fast!
    }
}
```

### Cache Hash Code

```java
public class CachedHash {
    private final int x;
    private final int y;
    private int cachedHash; // Cache for expensive computation
    
    public CachedHash(int x, int y) {
        this.x = x;
        this.y = y;
        this.cachedHash = computeHash();
    }
    
    private int computeHash() {
        return Objects.hash(x, y);
    }
    
    @Override
    public int hashCode() {
        return cachedHash; // O(1) after construction
    }
}
```

### Benchmark

```java
// JMH benchmark showing impact
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EqualsHashCodeBenchmark {
    
    @Benchmark
    public void objectsHash(Blackhole bh) {
        Point p = new Point(1, 2);
        bh.consume(p.hashCode());
    }
    
    @Benchmark
    public void customHash(Blackhole bh) {
        FastPoint p = new FastPoint(1, 2);
        bh.consume(p.hashCode());
    }
}

// Results (typical):
// objectsHash: ~15ns
// customHash:  ~3ns (6x faster)
```

## Testing Strategies

### Contract Compliance Test

```java
public abstract class EqualsHashCodeContractTest<T> {
    
    protected abstract T createValue1();
    protected abstract T createValue2(); // Equal to value1
    protected abstract T createValue3(); // Different from value1
    
    @Test
    void reflexive() {
        T value = createValue1();
        assertEquals(value, value);
        assertEquals(value.hashCode(), value.hashCode());
    }
    
    @Test
    void symmetric() {
        T a = createValue1();
        T b = createValue2();
        assertEquals(a.equals(b), b.equals(a));
    }
    
    @Test
    void transitive() {
        T a = createValue1();
        T b = createValue2();
        T c = createValue2(); // Same as b
        if (a.equals(b) && b.equals(c)) {
            assertEquals(a, c);
        }
    }
    
    @Test
    void consistent() {
        T a = createValue1();
        T b = createValue2();
        for (int i = 0; i < 100; i++) {
            assertEquals(a.equals(b), a.equals(b));
        }
    }
    
    @Test
    void notEqualToNull() {
        T value = createValue1();
        assertFalse(value.equals(null));
    }
    
    @Test
    void differentType() {
        T value = createValue1();
        assertFalse(value.equals("not a Point"));
    }
    
    @Test
    void hashCodeConsistency() {
        T a = createValue1();
        T b = createValue2();
        int hash1 = a.hashCode();
        int hash2 = b.hashCode();
        // Equal objects must have equal hash codes
        if (a.equals(b)) {
            assertEquals(hash1, hash2);
        }
    }
}
```

### Collection Behavior Test

```java
@Test
void testInHashSet() {
    Set<Point> set = new HashSet<>();
    Point p1 = new Point(1, 2);
    Point p2 = new Point(1, 2);
    
    set.add(p1);
    set.add(p2);
    
    assertEquals(1, set.size());
    assertTrue(set.contains(new Point(1, 2)));
}

@Test
void testInHashMap() {
    Map<Point, String> map = new HashMap<>();
    Point key = new Point(1, 2);
    
    map.put(key, "value");
    
    assertEquals("value", map.get(new Point(1, 2)));
    assertEquals(1, map.size());
}
```

### Mutation Test

```java
@Test
void testAfterMutation() {
    Set<MutablePoint> set = new HashSet<>();
    MutablePoint p = new MutablePoint(1, 2);
    set.add(p);
    
    p.x = 100; // Mutate!
    
    // Bug demonstration
    assertTrue(set.contains(p)); // true (same reference)
    assertFalse(set.contains(new MutablePoint(1, 2))); // false (hashCode changed)
    assertEquals(1, set.size()); // Can't remove!
}
```

## Code Generation

### Lombok

```java
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EqualsAndHashCode
public class LombokPoint {
    private final int x;
    private final int y;
}

// Equivalent generated code:
// public boolean equals(Object o) { ... }
// public int hashCode() { return Objects.hash(x, y); }
```

### IntelliJ IDEA

```
1. Right-click in editor
2. Generate → equals() and hashCode()
3. Select fields
4. Choose template (java.util.Objects recommended)
5. Generates both methods
```

### Records (Java 16+)

```java
// Best option for data classes
public record Point(int x, int y) {
    // Auto-generates: constructor, equals, hashCode, toString
    // Can't be subclassed - contract always valid
}

// With validation
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException("start > end");
        }
    }
}
```

### Java 21 Pattern Matching

```java
// Modern equals with pattern matching
@Override
public boolean equals(Object obj) {
    return switch (obj) {
        case Point other -> x == other.x && y == other.y;
        default -> false;
    };
}
```

## Common Libraries

### Apache Commons Lang

```java
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ApachePoint {
    private int x;
    private int y;
    
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        // Or manual:
        // return new EqualsBuilder()
        //     .append(x, other.x)
        //     .append(y, other.y)
        //     .isEquals();
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
```

### Guava

```java
import com.google.common.base.Objects;

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Point other)) return false;
    return x == other.x && y == other.y;
}

@Override
public int hashCode() {
    return Objects.hashCode(x, y);
}
```

### Spring Framework

```java
// Use for JPA entities
@Entity
public class User {
    @Id
    private Long id;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

## When NOT to Override

### Use Reference Equality When

```java
// 1. Object identity matters, not content
public class Thread {
    // Each thread is unique by identity
    // Don't override equals/hashCode
}

// 2. Objects are singletons
public class ApplicationContext {
    // Only one instance exists
    // Reference equality is correct
}

// 3. Objects represent unique resources
public class DatabaseConnection {
    // Each connection is a unique resource
    // Don't compare by content
}

// 4. Mutable objects in collections (risky)
// If you must use mutable objects as keys,
// override equals/hashCode with extreme caution
```

### Record vs Class Decision

```
Use a Record when:
├─ Data carrier (no behavior)
├─ All fields define equality
├─ Immutable by design
└─ No inheritance needed

Use a Class when:
├─ Has behavior/methods
├─ Equality is subset of fields
├─ Needs inheritance
├─ Mutable state is required
└─ Custom equals logic needed
```

## Glossary

| Term | Definition |
|------|-----------|
| **equals()** | Method to compare objects for logical equality |
| **hashCode()** | Method returning int hash for bucket placement |
| **Hash Collision** | Two objects with same hashCode |
| **Bucket** | Array slot in HashMap/HashSet determined by hashCode |
| **Symmetric** | If a.equals(b) then b.equals(a) |
| **Transitive** | If a=b and b=c then a=c |
| **Reflexive** | a.equals(a) always true |
| **Consistent** | Multiple calls return same result |
| **Pattern Matching** | instanceof with variable binding (Java 16+) |
| **Record** | Immutable data class with auto-generated methods |
| **Objects.hash()** | Utility to compute hashCode from fields |
| **Objects.equals()** | Null-safe equals wrapper |

## Summary

equals() and hashCode() are fundamental to Java's object model. Key takeaways:

- **Contract**: Equal objects must have equal hash codes
- **Purpose**: Enable correct behavior in collections
- **Implementation**: Use Objects.hash() and Objects.equals()
- **Pitfalls**: Breaking the contract causes subtle bugs
- **Best practices**: Use immutable fields, consider records
- **Testing**: Verify your implementation works in collections

**Next Steps**: Learn about immutable objects for thread safety, or records for automatic implementation.
