# Immutability Quiz

Test your understanding of Java immutability concepts.

---

### Question 1

Which of the following is required for a class to be truly immutable?

A) Make the class abstract
B) Make all fields final and private, with no setters
C) Implement the Serializable interface
D) Use only primitive field types

---

### Question 2

What happens when you call `s.concat(" World")` on a String `s`?

A) The original string `s` is modified in place
B) A new String is returned; `s` remains unchanged
C) The string pool is updated with the new value
D) A compile-time error is thrown

---

### Question 3

Why are defensive copies important in immutable classes?

A) They improve performance
B) They allow fields to be null
C) They prevent callers from modifying mutable objects referenced by the class
D) They enable the class to be serialized

---

### Question 4

What does `List.of("A", "B")` return?

A) A mutable ArrayList
B) A synchronized list
C) An unmodifiable list
D) A copy-on-write list

---

### Question 5

Consider this code:

```java
List<String> mutable = new ArrayList<>(List.of("X", "Y"));
List<String> view = Collections.unmodifiableList(mutable);
mutable.add("Z");
```

How many elements does `view` contain after execution?

A) 2
B) 3
C) It throws UnsupportedOperationException
D) It depends on the JVM

---

### Question 6

Which Java feature guarantees immutability by design?

A) Enums
B) Records
C) Abstract classes
D) Interfaces

---

### Question 7

What is the problem with this code?

```java
public final class BrokenDate {
    private final Date date;

    public BrokenDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
```

A) The class should not be final
B) The Date field should not be final
C) Both constructor and getter should create defensive copies
D) Date cannot be used in an immutable class

---

### Question 8

Why is string concatenation with `+=` in a loop inefficient?

A) It causes a runtime exception
B) It creates a new String object on every iteration
C) It modifies the original String in place
D) It uses too much stack space

---

### Question 9

What does this code print?

```java
String a = "Hello";
String b = "Hello";
System.out.println(a == b);
```

A) `true` - both point to the same object in the string pool
B) `false` - each is a separate object
C) Compilation error
D) It varies by JVM implementation

---

### Question 10

Which is the correct way to create a truly immutable copy of a mutable list?

A) `Collections.unmodifiableList(mutableList)`
B) `List.copyOf(mutableList)` (Java 10+)
C) `(List<String>) mutableList`
D) Assign the mutable list to a new variable

---

## Answers

1. **B** - Final fields, private access, no setters are the core requirements.
2. **B** - Strings are immutable; `concat` returns a new String.
3. **C** - Defensive copies prevent external code from mutating internal mutable state.
4. **C** - `List.of()` returns an unmodifiable list (Java 9+).
5. **B** - The unmodifiable view is a wrapper; modifying the underlying list is reflected.
6. **B** - Records are immutable by design (final class, final components, no setters).
7. **C** - Without defensive copies, callers can modify the Date after construction.
8. **B** - Each `+=` creates a new String; use StringBuilder for loops.
9. **A** - String literals are interned; `a` and `b` reference the same pool object.
10. **B** - `List.copyOf()` creates an independent unmodifiable copy; `unmodifiableList` is only a view.
