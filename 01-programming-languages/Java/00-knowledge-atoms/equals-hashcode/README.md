# equals() and hashCode()

## Overview

The `equals()` and `hashCode()` methods are fundamental to Java's object model. They work together to determine object identity in collections like `HashMap`, `HashSet`, and `Hashtable`. Understanding their contract is essential for correct behavior in these collections.

---

## The Contract

### equals() Contract

1. **Reflexive**: `x.equals(x)` must return `true`
2. **Symmetric**: If `x.equals(y)` is `true`, then `y.equals(x)` must be `true`
3. **Transitive**: If `x.equals(y)` and `y.equals(z)` are `true`, then `x.equals(z)` must be `true`
4. **Consistent**: Multiple calls to `x.equals(y)` must return the same result
5. **Non-null**: `x.equals(null)` must return `false`

### hashCode() Contract

1. **Consistent**: Multiple calls must return the same integer (within one execution)
2. **Equal objects must have equal hash codes**: If `x.equals(y)` is `true`, then `x.hashCode() == y.hashCode()` must be `true`
3. **Unequal objects may have equal hash codes**: If `!x.equals(y)`, `x.hashCode()` and `y.hashCode()` may or may not be equal (hash collision)

### The Critical Rule

> If two objects are equal according to `equals()`, they MUST have the same `hashCode()`. The reverse is NOT required.

---

## Default Behavior

### equals() Default (Object class)

```java
// Default implementation uses identity comparison (==)
public boolean equals(Object obj) {
    return (this == obj);
}
```

This means two different objects are never equal, even if they have the same field values.

```java
public class Person {
    String name;
    int age;
}

Person p1 = new Person("Alice", 30);
Person p2 = new Person("Alice", 30);

System.out.println(p1.equals(p2));  // false! Different objects
System.out.println(p1 == p2);       // false! Different references
```

### hashCode() Default (Object class)

```java
// Default implementation typically returns memory address
public native int hashCode();
```

---

## Why Both Must Be Overridden Together

### The Problem with Only equals()

```java
public class Person {
    String name;
    int age;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    // hashCode() NOT overridden - uses default!
}

Person p1 = new Person("Alice", 30);
Person p2 = new Person("Alice", 30);

// Problem 1: equals() says they're equal
System.out.println(p1.equals(p2));  // true

// Problem 2: hashCode() says they're different
System.out.println(p1.hashCode());  // e.g., 20123456
System.out.println(p2.hashCode());  // e.g., 30987654

// Problem 3: HashSet fails!
Set<Person> set = new HashSet<>();
set.add(p1);
set.add(p2);
System.out.println(set.size());  // 2! Should be 1

// Problem 4: HashMap fails!
Map<Person, String> map = new HashMap<>();
map.put(p1, "value");
System.out.println(map.get(p2));  // null! Should return "value"
```

### The Problem with Only hashCode()

```java
public class Person {
    String name;
    int age;
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    // equals() NOT overridden - uses default!
}

Person p1 = new Person("Alice", 30);
Person p2 = new Person("Alice", 30);

// hashCode() is the same (good)
System.out.println(p1.hashCode() == p2.hashCode());  // true

// But equals() says they're different
System.out.println(p1.equals(p2));  // false

// HashSet behavior is unpredictable
Set<Person> set = new HashSet<>();
set.add(p1);
set.add(p2);
System.out.println(set.size());  // 2 (even though hashCode is same)
```

---

## Best Practices for Implementation

### Using Objects Utility Class

```java
public class Person {
    private String name;
    private int age;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }
}
```

### Using @EqualsAndHashCode (Lombok)

```java
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Person {
    private String name;
    private int age;
    private String email;
}
```

### Excluding Fields

```java
@EqualsAndHashCode(exclude = {"id", "createdAt"})
public class Person {
    private Long id;
    private String name;
    private int age;
    private LocalDateTime createdAt;
}
```

### Only Including Specific Fields

```java
@EqualsAndHashCode(of = {"name", "age"})
public class Person {
    private Long id;
    private String name;
    private int age;
    private String email;
}
```

### Inheritance Considerations

```java
// Call super if parent class has meaningful equals/hashCode
public class Employee extends Person {
    private String department;

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department);
    }
}
```

---

## When to Use equals vs ==

### Use == For:
- **Primitives**: `int x = 5; if (x == 5)` 
- **Reference identity**: Checking if two references point to the same object
- **Null checks**: `if (obj == null)`
- **Enum comparison**: `if (status == Status.ACTIVE)`

### Use equals() For:
- **Object value comparison**: When you want to compare field values
- **Collection lookups**: `map.containsKey()`, `set.contains()`
- **String comparison**: Always use `equals()` for strings
- **Custom objects**: When identity-based comparison is not meaningful

```java
// == comparison (reference identity)
String s1 = new String("hello");
String s2 = new String("hello");
System.out.println(s1 == s2);       // false (different objects)
System.out.println(s1.equals(s2));  // true (same value)

// String pool optimization
String s3 = "hello";
String s4 = "hello";
System.out.println(s3 == s4);       // true (same object in pool)
System.out.println(s3.equals(s4));  // true (same value)

// When to use ==
if (obj == null) { ... }           // null check
if (status == Status.ACTIVE) { ... } // enum comparison
if (this == other) { return true; }  // identity check in equals()
```

---

## Common Mistakes

### 1. Forgetting hashCode()
```java
// WRONG: Only overriding equals()
@Override
public boolean equals(Object o) { ... }
// hashCode() uses default (memory address)
```

### 2. Using Mutable Fields in hashCode()
```java
// WRONG: hashCode changes when field changes
public int hashCode() {
    return Objects.hash(name, age, mutableField); // Bad!
}

// RIGHT: Use only immutable fields
public int hashCode() {
    return Objects.hash(name, id); // Good!
}
```

### 3. Inconsistent equals()
```java
// WRONG: equals() depends on mutable state
@Override
public boolean equals(Object o) {
    Person p = (Person) o;
    return this.age == p.age && this.name.equals(p.name);
    // If 'age' changes, equals() result changes!
}
```

### 4. Not Handling null
```java
// WRONG: Potential NullPointerException
@Override
public boolean equals(Object o) {
    Person p = (Person) o;
    return this.name.equals(p.name); // NPE if name is null
}

// RIGHT: Use Objects.equals()
@Override
public boolean equals(Object o) {
    Person p = (Person) o;
    return Objects.equals(this.name, p.name);
}
```

### 5. Breaking Symmetry
```java
// WRONG: Asymmetric equals()
public class Parent {
    @Override
    public boolean equals(Object o) {
        return o instanceof Parent;
    }
}

public class Child extends Parent {
    @Override
    public boolean equals(Object o) {
        return o instanceof Child;
    }
}

Parent p = new Parent();
Child c = new Child();
// p.equals(c) = true (o is Parent)
// c.equals(p) = false (o is not Child)
// SYMMETRY BROKEN!
```

---

## Summary

| Concept | Rule |
|---------|------|
| **equals()** | Defines logical equality based on field values |
| **hashCode()** | Returns hash code for use in hash-based collections |
| **Contract** | Equal objects must have equal hash codes |
| **Override both** | Always override both or neither |
| **Use Objects.hash()** | Simplifies hashCode() implementation |
| **Use Objects.equals()** | Handles null safely in equals() |
| **== vs equals()** | == for reference identity, equals() for value equality |
