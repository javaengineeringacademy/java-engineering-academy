# equals() and hashCode() Exercises

## Exercise 1: Person Class

Implement a `Person` class with proper `equals()` and `hashCode()` methods.

### Requirements:

1. Create a `Person` class with fields:
   - `String name`
   - `int age`
   - `String email`

2. Implement `equals()` that:
   - Returns `true` if both objects are the same reference
   - Returns `false` if the other object is `null` or a different class
   - Compares all three fields for equality
   - Uses `Objects.equals()` for null-safe String comparison

3. Implement `hashCode()` that:
   - Uses `Objects.hash()` with all three fields
   - Returns consistent results for equal objects

4. Implement `toString()` for easy debugging

### Test Cases:

```java
Person p1 = new Person("Alice", 30, "alice@email.com");
Person p2 = new Person("Alice", 30, "alice@email.com");
Person p3 = new Person("Bob", 25, "bob@email.com");
Person p4 = new Person(null, 30, "alice@email.com");

// Test equals()
assert p1.equals(p2);           // true
assert !p1.equals(p3);          // false
assert !p1.equals(null);        // false
assert !p1.equals("Alice");     // false
assert p1.equals(p1);           // true (reflexive)
assert p1.equals(p2) == p2.equals(p1);  // true (symmetric)

// Test hashCode()
assert p1.hashCode() == p2.hashCode();  // true
assert p1.hashCode() != p3.hashCode();  // may be true (not required)

// Test with collections
Set<Person> set = new HashSet<>();
set.add(p1);
set.add(p2);
assert set.size() == 1;  // p2 not added because p1.equals(p2) is true

Map<Person, String> map = new HashMap<>();
map.put(p1, "Engineer");
assert map.get(p2).equals("Engineer");  // p2 finds p1's value
```

### Files to modify:

- `PersonExercise.java` - Implement the Person class

### Solution:

See `../solutions/PersonSolution.java` for the complete implementation.
