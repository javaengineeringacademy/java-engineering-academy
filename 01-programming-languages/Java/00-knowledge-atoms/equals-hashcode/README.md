# equals() and hashCode() — Interactive Guide

## Why This Matters

Every Java developer who uses HashMap, HashSet, or any collection needs to understand equals() and hashCode(). Get this wrong, and your objects will "disappear" from collections, behave unpredictably, or cause subtle bugs that take days to debug.

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

## Interactive Examples

### Example 1: Same hashCode, Different Objects (Hash Collision)

Two different objects can have the same hashCode. This is called a hash collision.

```java
// String "Aa" and "BB" have same hashCode
String a = "Aa";
String b = "BB";
System.out.println(a.hashCode()); // 2112
System.out.println(b.hashCode()); // 2112
System.out.println(a.equals(b));  // false
```

**Why this happens**: HashMap uses the hashCode to find a bucket, but multiple keys can map to the same bucket. The bucket stores a linked list of entries, and equals() is used to find the exact match.

### Example 2: Equal Objects, Same hashCode (Correct Implementation)

When you override equals() correctly, equal objects MUST have same hashCode.

```java
public class Person {
    private String name;
    private int age;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Person p1 = new Person("Alice", 30);
Person p2 = new Person("Alice", 30);
System.out.println(p1.equals(p2));      // true
System.out.println(p1.hashCode() == p2.hashCode()); // true
```

### Example 3: Broken equals, Broken hashCode (Common Bug)

If you override equals() but not hashCode(), equal objects may have different hashCodes.

```java
public class BrokenPerson {
    private String name;
    private int age;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrokenPerson p = (BrokenPerson) o;
        return age == p.age && Objects.equals(name, p.name);
    }
    // NO hashCode() override!
}

Set<BrokenPerson> set = new HashSet<>();
BrokenPerson p1 = new BrokenPerson("Alice", 30);
BrokenPerson p2 = new BrokenPerson("Alice", 30);

set.add(p1);
set.add(p2);
System.out.println(set.size()); // 2! Should be 1
```

### Example 4: equals() with null

equals() must handle null gracefully.

```java
Person p = new Person("Alice", 30);
System.out.println(p.equals(null)); // false
System.out.println(p.equals("Alice")); // false (different type)
```

### Example 5: equals() with Different Types

equals() must handle different types gracefully.

```java
Person p = new Person("Alice", 30);
String s = "Alice";
System.out.println(p.equals(s)); // false (different class)
System.out.println(s.equals(p)); // false (String.equals checks instance of String)
```

---

## Production Checklist

- [ ] Override both equals() and hashCode() together
- [ ] Use Objects.hash() for hashCode() implementation
- [ ] Use Objects.equals() for null-safe field comparisons
- [ ] Include all fields that define logical equality
- [ ] Don't use mutable fields in hashCode()
- [ ] Test with HashMap and HashSet to verify behavior
- [ ] Consider using Lombok @EqualsAndHashCode for boilerplate reduction
- [ ] Don't override equals() for value objects (use record types in Java 14+)
- [ ] Verify symmetry and transitivity in your equals() implementation
- [ ] Test edge cases: null, different types, same object

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

## One-Minute Revision

| Concept | Rule |
|---------|------|
| **equals()** | Defines logical equality based on field values |
| **hashCode()** | Returns hash code for use in hash-based collections |
| **Contract** | Equal objects must have equal hash codes |
| **Override both** | Always override both or neither |
| **Use Objects.hash()** | Simplifies hashCode() implementation |
| **Use Objects.equals()** | Handles null safely in equals() |
| **== vs equals()** | == for reference identity, equals() for value equality |
| **Hash collision** | Different objects can have same hashCode |
| **Null handling** | equals() must return false for null |
| **Type checking** | equals() must handle different types gracefully |

---

## Runnable Examples

See the `examples/` directory for complete, runnable Java files:

- `HashCollisionDemo.java` - Demonstrates hash collisions with String objects
- `CorrectImplementation.java` - Shows proper equals() and hashCode() implementation
- `BrokenImplementation.java` - Shows what happens when you only override equals()
- `NullHandling.java` - Demonstrates null handling in equals()

## Exercises

See the `exercises/` directory for practice problems:

- `PersonExercise.java` - Implement equals() and hashCode() for a Person class

## Solutions

See the `solutions/` directory for completed implementations:

- `PersonSolution.java` - Complete solution to the Person exercise

## Quiz

Test your knowledge with the quiz in `quiz.md`.
