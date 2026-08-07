# equals() and hashCode() Quiz

Test your understanding of equals() and hashCode() with these 10 questions.

## Questions

### 1. What is the primary purpose of overriding equals()?
a) To compare memory addresses
b) To define logical equality based on field values
c) To improve performance
d) To make objects immutable

### 2. What is the contract between equals() and hashCode()?
a) If two objects are equal, they must have different hash codes
b) If two objects are equal, they must have the same hash code
c) Hash codes must be unique for all objects
d) Hash codes must be sequential

### 3. What happens if you override equals() but not hashCode()?
a) Nothing, it works fine
b) Equal objects may have different hash codes, breaking collections
c) The code won't compile
d) hashCode() will automatically use equals() logic

### 4. Which method should you use for null-safe string comparison in equals()?
a) `this.name.equals(other.name)`
b) `Objects.equals(this.name, other.name)`
c) `this.name == other.name`
d) `String.equals(this.name, other.name)`

### 5. What is a hash collision?
a) When two objects have the same hashCode and are equal
b) When two different objects have the same hashCode
c) When an object's hashCode changes over time
d) When a collection can't find an object

### 6. Why should you avoid using mutable fields in hashCode()?
a) It causes compilation errors
b) It makes hashCode() slower
c) The hashCode changes when the field changes, breaking collection lookups
d) It's not necessary, mutable fields are fine

### 7. What is the result of this code?
```java
Set<String> set = new HashSet<>();
set.add("Aa");
set.add("BB");
System.out.println(set.size());
```
a) 1
b) 2
c) 0
d) Compilation error

### 8. Which of the following is NOT a requirement of the equals() contract?
a) Reflexive: x.equals(x) must be true
b) Symmetric: if x.equals(y) then y.equals(x)
c) Transitive: if x.equals(y) and y.equals(z) then x.equals(z)
d) Commutative: x.equals(y) must equal y.equals(x)

### 9. What is the default implementation of equals() in Object class?
a) Compares all fields
b) Compares memory addresses (reference equality)
c) Always returns true
d) Always returns false

### 10. What is the correct way to implement hashCode()?
a) Return a constant value
b) Return the memory address
c) Use Objects.hash() with all fields used in equals()
d) Return the hash of only the first field

---

## Answers

1. **b** - equals() defines logical equality based on field values
2. **b** - Equal objects must have equal hash codes
3. **b** - Equal objects may have different hash codes, breaking HashMap/HashSet
4. **b** - Objects.equals() handles null values safely
5. **b** - Hash collision is when two different objects have the same hashCode
6. **c** - The hashCode changes when the field changes, breaking collection lookups
7. **b** - "Aa" and "BB" are different strings (equals returns false), so both are added
8. **d** - Commutative is not a separate requirement (symmetric covers it)
9. **b** - Default equals() uses reference equality (==)
10. **c** - Use Objects.hash() with all fields used in equals()

---

## Scoring

- 9-10 correct: Excellent! You understand equals() and hashCode() well
- 7-8 correct: Good! Review the contract and common mistakes
- 5-6 correct: Fair. Re-read the examples and try the exercises
- Below 5: Needs work. Start with the basic examples in README.md

---

## Key Takeaways

1. **Always override both equals() and hashCode() together**
2. **Use Objects.hash() for hashCode() implementation**
3. **Use Objects.equals() for null-safe field comparisons**
4. **Test with HashMap and HashSet to verify correct behavior**
5. **Avoid mutable fields in hashCode()**
