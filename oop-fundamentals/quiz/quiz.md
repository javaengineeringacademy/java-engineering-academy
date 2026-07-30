# Sprint 2 Quiz - Object Oriented Programming

---

## 📝 Instructions
- **Time limit:** 30 minutes
- **Format:** Multiple choice (single/multiple), True/False, Code output
- **Passing score:** 70%
- **No external resources allowed**

---

## Section A: Multiple Choice (Single Answer)

### 1. Which keyword is used to define an abstract class?
- A) `interface`
- B) `abstract` ✓
- C) `final`
- D) `static`

### 2. A class can extend only one parent class in Java.
- A) True ✓
- B) False

### 3. What is the output?
```java
Animal a = new Dog();
a.makeSound();
```
- A) Animal sound
- B) Dog sound ✓
- C) Compile error
- D) Runtime exception

### 4. Which modifier prevents a class from being extended?
- A) `abstract`
- B) `final` ✓
- C) `static`
- D) `private`

### 5. What does `super()` do?
- A) Calls parent constructor ✓
- B) Calls parent method
- C) Accesses parent field
- D) Creates new parent instance

---

## Section B: Multiple Choice (Multiple Answers)

### 6. Which are valid access modifiers for interface methods? (Select all)
- [ ] `private` (Java 9+)
- [ ] `public` ✓
- [ ] `protected` ✗
- [ ] `default` ✓
- [ ] `static` ✓

### 7. Which statements about `abstract` classes are true? (Select all)
- [ ] Can have constructors ✓
- [ ] Can have `private` methods ✓
- [ ] Can be instantiated ✗
- [ ] Must have at least one abstract method ✗

### 8. Which are part of the `Object` class contract? (Select all)
- [ ] `equals()` ✓
- [ ] `hashCode()` ✓
- [ ] `toString()` ✓
- [ ] `clone()` ✓
- [ ] `compareTo()` ✗

---

## Section C: True/False

### 9. Java supports multiple inheritance through classes.
- [ ] True
- [ ] False ✓

### 10. A `final` method can be overridden.
- [ ] True
- [ ] False ✓

### 11. An interface can have `private` methods (Java 9+).
- [ ] True ✓
- [ ] False

### 12. `equals()` and `hashCode()` must be overridden together.
- [ ] True ✓
- [ ] False

### 13. A `static` method can be overridden.
- [ ] True
- [ ] False ✓

### 14. Records (Java 16+) are implicitly `final`.
- [ ] True ✓
- [ ] False

---

## Section D: Code Output

### 15. What is the output?
```java
class A { void print() { System.out.print("A"); } }
class B extends A { void print() { System.out.print("B"); } }
public class Test {
    public static void main(String[] args) {
        A a = new B();
        a.print();
    }
}
```
- A) A
- B) B ✓
- C) AB
- D) Compile error

### 16. What is the output?
```java
class Parent { void show() { System.out.print("P"); } }
class Child extends Parent { 
    void show() { System.out.print("C"); }
}
public class Test {
    public static void main(String[] args) {
        Parent p = new Child();
        p.show();
    }
}
```
- A) P
- B) C ✓
- C) PC
- D) CP

### 17. What is the output?
```java
interface I { default void m() { System.out.print("I"); } }
class C implements I { public void m() { System.out.print("C"); } }
public class Test {
    public static void main(String[] args) {
        I i = new C();
        i.m();
    }
}
```
- A) I
- B) C ✓
- C) IC
- D) Compile error

### 18. What is the output?
```java
public record Point(int x, int y) {}
public class Test {
    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println(p1.equals(p2));
    }
}
```
- A) true ✓
- B) false
- C) Compile error
- D) Runtime exception

---

## Section E: Fill in the Blank

### 19. The `__________` keyword is used to call a parent class constructor.
**Answer:** `super`

### 20. A class that cannot be instantiated is marked `__________`.
**Answer:** `abstract`

### 21. The `__________` keyword prevents method overriding.
**Answer:** `final`

### 22. To achieve runtime polymorphism, you use method `__________`.
**Answer:** overriding

### 23. The `__________` operator checks if an object is an instance of a class.
**Answer:** `instanceof`

---

## Section F: Short Answer

### 24. What is the difference between `==` and `.equals()` for String comparison?

### 25. Why must you override `hashCode()` when you override `equals()`?

### 26. What is the "diamond problem" and how does Java solve it?

### 27. When would you use an abstract class vs an interface?

### 28. What is the Liskov Substitution Principle? Give an example violation.

### 29. What is the difference between composition and inheritance?

### 30. What is Dependency Injection? Name the three types.

---

## Answers Summary
| Q | Answer | Q | Answer |
|---|--------|---|--------|
| 1 | B | 16 | B |
| 2 | A | 17 | B |
| 3 | B | 18 | A |
| 4 | B | 19 | super |
| 5 | A | 20 | abstract |
| 6 | A,B,D,E | 21 | final |
| 7 | A,B | 22 | overriding |
| 8 | A,B,C,D | 23 | instanceof |
| 9 | B | 24 | == compares refs, .equals() compares content |
| 10 | B | 25 | HashMap/HashSet contract |
| 11 | A | 26 | Multiple inheritance ambiguity; Java uses interfaces |
| 12 | A | 27 | Abstract class: shared code; Interface: contract |
| 13 | B | 28 | Subtype substitutable; Square/Rectangle |
| 14 | A | 28 | Has-a vs Is-a; composition = loose coupling |

---

## Score Interpretation
- **28-30:** Excellent (Mastery)
- **24-27:** Good (Proficient)
- **20-23:** Fair (Needs review)
- **<20:** Retake recommended

---

*Self-grade honestly. Review wrong answers with theory.md.*