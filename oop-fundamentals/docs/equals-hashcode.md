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

## Summary

equals() and hashCode() are fundamental to Java's object model. Key takeaways:

- **Contract**: Equal objects must have equal hash codes
- **Purpose**: Enable correct behavior in collections
- **Implementation**: Use Objects.hash() and Objects.equals()
- **Pitfalls**: Breaking the contract causes subtle bugs
- **Best practices**: Use immutable fields, consider records
- **Testing**: Verify your implementation works in collections

**Next Steps**: Learn about immutable objects for thread safety, or records for automatic implementation.
